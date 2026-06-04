package net.itemBattle.listener

import net.itemBattle.inventories.TeamAddMemberInv
import net.itemBattle.inventories.TeamConfiguratorInv
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

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
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val inventory = event.view.topInventory
        val holder = inventory.holder

        if (holder is TeamAddMemberInv) holder.onClose()
    }
}