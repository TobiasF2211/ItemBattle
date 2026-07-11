package net.itemBattle.inventories

import net.itemBattle.ItemBattle
import net.itemBattle.team.Team
import net.itemBattle.team.TeamManager
import net.itemBattle.utils.Format
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class TeamAddMemberInv(
    private var player: Player,
    private var team: Team,
    private var teamConfigInv: TeamConfiguratorInv
) : InventoryHolder {

    private var gui: Inventory =
        Bukkit.createInventory(this, 3 * 9, Format.of("Add members to team <yellow>${team.index() + 1}"))
    private var playerSlot = HashMap<Int, Player>()

    fun openInventory() {
        var index = 0

        for (player in Bukkit.getOnlinePlayers()) {
            if (TeamManager.getTeamFromPlayer(player) != null) continue

            gui.setItem(index, getPlayerStack(player))
            playerSlot[index] = player
            index++
        }

        player.openInventory(gui)
    }

    private fun getPlayerStack(player: Player): ItemStack {
        val playerStack = ItemStack(Material.PLAYER_HEAD)
        val playerStackMeta = playerStack.itemMeta as SkullMeta

        playerStackMeta.displayName(Format.of("<dark_gray>» <yellow>${player.name}"))
        playerStackMeta.lore(listOf(Format.of("<dark_gray>➥ <gray>Click to add this player to the team.")))
        playerStackMeta.owningPlayer = player
        playerStack.setItemMeta(playerStackMeta)

        return playerStack
    }

    fun onClick(slot: Int) {
        val target = playerSlot[slot] ?: return

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F)
        team.addMember(target)
        gui.clear()
        openInventory()
    }

    fun onClose() {
        // fix stackoverflow
        Bukkit.getScheduler().runTaskLater(ItemBattle.instance, Runnable {
            teamConfigInv.openInventory()
        }, 1L)
    }

    override fun getInventory(): Inventory = gui
}