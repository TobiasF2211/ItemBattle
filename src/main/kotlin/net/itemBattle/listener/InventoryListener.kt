package net.itemBattle.listener

import net.itemBattle.inventories.TeamConfiguratorInv
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryListener : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.clickedInventory ?: return
        val holder = inventory.getHolder(false)

        if (holder is TeamConfiguratorInv) {
            event.isCancelled = true
            holder.onClick(event, event.slot)
        }
    }
}