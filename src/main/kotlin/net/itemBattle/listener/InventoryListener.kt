package net.itemBattle.listener

import net.itemBattle.inventories.FoundItemsPreview
import net.itemBattle.inventories.TeamAddMemberInv
import net.itemBattle.inventories.TeamConfiguratorInv
import net.itemBattle.manager.BattleManager
import net.itemBattle.team.TeamManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

class InventoryListener : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.view.topInventory
        val holder = inventory.holder

        if (holder is TeamConfiguratorInv) {
            event.isCancelled = true
            holder.onClick(event.slot)
        }
        if (holder is TeamAddMemberInv) {
            event.isCancelled = true
            holder.onClick(event.slot)
        }
        if (holder is FoundItemsPreview) {
            event.isCancelled = true
            holder.onClick(event.slot, event)
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val inventory = event.view.topInventory
        val holder = inventory.holder

        if (holder is TeamAddMemberInv) holder.onClose()
        if (holder is FoundItemsPreview && !holder.finished) holder.onClose(event.player)
    }

    @EventHandler
    fun onItemInteract(event: PlayerInteractEvent) {
        val player = event.player
        val meta = player.inventory.itemInMainHand.itemMeta

        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return

        if (meta == null || !meta.persistentDataContainer.has(
                BattleManager.getSkipNamespaceKey(),
                PersistentDataType.BYTE
            )
        ) return

        event.isCancelled = true

        player.inventory.itemInMainHand.amount -= 1

        val team = TeamManager.getTeamFromPlayer(player) ?: return

        team.skipCurrentItem(player)
    }
}