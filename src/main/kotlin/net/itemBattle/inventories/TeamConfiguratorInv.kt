package net.itemBattle.inventories

import net.itemBattle.team.TeamManager
import net.itemBattle.utils.Prefix
import net.kyori.adventure.text.Component
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
        val grayGlassStack = ItemStack(Material.GRAY_STAINED_GLASS)
        val grayGlassMeta = grayGlassStack.itemMeta
        grayGlassMeta.displayName(Component.text(""))
        grayGlassStack.setItemMeta(grayGlassMeta)

        val createTeamStack = ItemStack(Material.LIME_WOOL)
        val createTeamMeta = createTeamStack.itemMeta
        createTeamMeta.displayName(Component.text("<dark_gray>» <yellow>Create New Team"))
        createTeamStack.setItemMeta(createTeamMeta)

        val deleteTeamStack = ItemStack(Material.RED_WOOL)
        val deleteTeamMeta = deleteTeamStack.itemMeta
        deleteTeamMeta.displayName(Component.text("<dark_gray>» <red>Delete Team"))
        deleteTeamStack.setItemMeta(deleteTeamMeta)

        var teamIndex = 0

        // display teams
        for (team in TeamManager.teams) {
            val stack = getTeamStack(team.teamIndex)

            gui.setItem(teamIndex, stack)

            // render member
            var memberIndex = teamIndex

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

            gui.setItem(teamIndex + 9 * 5, deleteTeamStack)

            // render glas
            teamIndex++

            if (teamIndex > 8) break

            for (glasIndex in 0..8) {
                gui.setItem(teamIndex * glasIndex, grayGlassStack)
            }
        }

        if (teamIndex < 8) gui.setItem(teamIndex, createTeamStack)

        player.openInventory(gui)
    }

    private fun getTeamStack(teamIndex: Int): ItemStack {
        val teamStack = ItemStack(Material.NETHERITE_BLOCK)
        val teamStackMeta = teamStack.itemMeta
        teamStackMeta.displayName(Component.text("<dark_gray>» <yellow>Team $teamIndex"))
        teamStack.setItemMeta(teamStackMeta)

        return teamStack
    }

    private fun getMemberStack(member: Player): ItemStack {
        val memberStack = ItemStack(Material.PLAYER_HEAD)
        val headStackMeta = memberStack.itemMeta as SkullMeta
        headStackMeta.displayName(Component.text("<dark_gray>» <yellow>${member.name}"))
        headStackMeta.owningPlayer = member
        memberStack.setItemMeta(headStackMeta)

        return memberStack
    }

    fun onClick(event: InventoryClickEvent, slot: Int) {
        val createTeamPos = (TeamManager.teams.size - 1) * 2
        val deleteTeamPos = createTeamPos + 5 * 9

        if (slot == createTeamPos) {
            TeamManager.createTeam()
            player.updateInventory()
        }

        if (slot == deleteTeamPos) {
            TeamManager.deleteTeam(createTeamPos / 2)
            player.updateInventory()
        }
    }

    override fun getInventory(): Inventory = gui
}