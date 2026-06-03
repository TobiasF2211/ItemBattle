package net.itemBattle.inventories

import net.itemBattle.ItemBattle
import net.itemBattle.team.TeamManager
import net.itemBattle.utils.Format
import net.itemBattle.utils.Prefix
import net.kyori.adventure.text.Component
import net.wesjd.anvilgui.AnvilGUI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class TeamConfiguratorInv(private var player: Player) : InventoryHolder {

    private var gui: Inventory = Bukkit.createInventory(this, 6 * 9, Component.text("TeamConfigurator"))

    fun openInventory() {
        val grayGlassStack = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
        val grayGlassMeta = grayGlassStack.itemMeta
        grayGlassMeta.displayName(Component.text(""))
        grayGlassStack.setItemMeta(grayGlassMeta)

        val createTeamStack = ItemStack(Material.LIME_WOOL)
        val createTeamMeta = createTeamStack.itemMeta
        createTeamMeta.displayName(Format.of("<dark_gray>» <green>Create New Team"))
        createTeamStack.setItemMeta(createTeamMeta)

        val deleteTeamStack = ItemStack(Material.RED_WOOL)
        val deleteTeamMeta = deleteTeamStack.itemMeta
        deleteTeamMeta.displayName(Format.of("<dark_gray>» <red>Delete Team"))
        deleteTeamStack.setItemMeta(deleteTeamMeta)

        val addMemberStack = ItemStack(Material.DIAMOND_CHESTPLATE)
        val addMemberMeta = addMemberStack.itemMeta
        addMemberMeta.displayName(Format.of("<dark_gray>» <green>Add member"))
        addMemberStack.setItemMeta(addMemberMeta)

        var teamIndex = 0

        // display teams
        for (team in TeamManager.teams) {
            val stack = getTeamStack(team.index())

            gui.setItem(teamIndex * 2, stack)

            // render member
            var memberIndex = teamIndex * 2

            for (member in team.members) {
                val target = Bukkit.getPlayer(member)

                if (target == null) {
                    player.sendMessage(Prefix.get() + "§cPlayer with UUID $member needs to be online! The player was removed from the team.")
                    team.remove(member)
                    continue
                }

                memberIndex += 9

                if (memberIndex > 9 * 5) break

                gui.setItem(memberIndex, getMemberStack(target))
            }

            // render add member
            val addMemberIndex = team.members.size * 9 + team.index() * 2 + 9

            if (memberIndex > 9 * 5) break

            gui.setItem(addMemberIndex, addMemberStack)

            // render delet stack
            gui.setItem(teamIndex * 2 + 9 * 5, deleteTeamStack)

            // render glas
            teamIndex++

            if (teamIndex >= 5) break

            for (glasIndex in 0..<6) {
                gui.setItem(teamIndex * 2 - 1 + glasIndex * 9, grayGlassStack)
            }
        }

        if (teamIndex < 5) gui.setItem(teamIndex * 2, createTeamStack)

        player.openInventory(gui)
    }

    private fun getTeamStack(teamIndex: Int): ItemStack {
        val teamStack = ItemStack(Material.NETHERITE_BLOCK)
        val teamStackMeta = teamStack.itemMeta
        teamStackMeta.displayName(Format.of("<dark_gray>» <yellow>Team ${teamIndex + 1}"))
        teamStack.setItemMeta(teamStackMeta)

        return teamStack
    }

    private fun getMemberStack(member: Player): ItemStack {
        val memberStack = ItemStack(Material.PLAYER_HEAD)
        val headStackMeta = memberStack.itemMeta as SkullMeta
        headStackMeta.displayName(Format.of("<dark_gray>» <yellow>${member.name}"))
        headStackMeta.lore(listOf(Format.of("<dark_gray>➥ <gray>Click to remove this player from his team.")))
        headStackMeta.owningPlayer = member
        memberStack.setItemMeta(headStackMeta)

        return memberStack
    }

    private fun openAddMemberInput(teamIndex: Int) {
        AnvilGUI.Builder()
            .onClick { slot, snapshot ->
                if (slot != AnvilGUI.Slot.OUTPUT) {
                    return@onClick listOf()
                }

                if (snapshot.text == "") {
                    return@onClick listOf(AnvilGUI.ResponseAction.close())
                }

                val target = Bukkit.getPlayer(snapshot.text)

                if (target != null) {
                    val team = TeamManager.getTeam(teamIndex) ?: return@onClick listOf(AnvilGUI.ResponseAction.close())

                    if (team.members.contains(target.uniqueId)) {
                        return@onClick listOf(AnvilGUI.ResponseAction.replaceInputText("Player already has a team."))
                    }

                    team.addMember(target.uniqueId)
                    return@onClick listOf(AnvilGUI.ResponseAction.close())
                } else {
                    return@onClick listOf(AnvilGUI.ResponseAction.replaceInputText("Player was not found."))
                }

                listOf()
            }
            .onClose { openInventory() }
            .title("Add a teammember")
            .text("Player name or nothing to cancel")
            .plugin(ItemBattle.instance)
            .open(player)
    }

    fun onClick(event: InventoryClickEvent, slot: Int) {
        val createTeamPos = TeamManager.teams.size * 2

        if (slot == createTeamPos) {
            TeamManager.createTeam()
            openInventory()
        }

        for (team in TeamManager.teams) {
            val delSlot = 5 * 9 + team.index() * 2
            val addMemberIndex = team.members.size * 9 + team.index() * 2 + 9

            if (slot == delSlot) {
                TeamManager.deleteTeam(team.index())
                gui.clear()
                openInventory()
                break
            }

            if (slot == addMemberIndex) {
                openAddMemberInput(team.index())
                break
            }
        }
    }

    override fun getInventory(): Inventory = gui
}