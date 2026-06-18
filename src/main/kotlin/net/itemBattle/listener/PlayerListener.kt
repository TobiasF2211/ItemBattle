package net.itemBattle.listener

import net.itemBattle.ItemBattle
import net.itemBattle.manager.BattleManager
import net.itemBattle.team.TeamManager
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.persistence.PersistentDataType


class PlayerListener : Listener {

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        event.drops.removeIf({ item ->
            val meta = item.itemMeta
            meta != null &&
                    meta.persistentDataContainer.has(
                        NamespacedKey(ItemBattle.instance, "skip_itemstack"),
                        PersistentDataType.BYTE
                    )
        })
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        val team = TeamManager.getTeamFromPlayer(player) ?: return

        val skipStack = BattleManager.getSkipItemStack()
        skipStack.amount = team.skipsPerPlayer[player.uniqueId] ?: return

        player.inventory.addItem(skipStack)
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (event.blockPlaced.type == Material.BARRIER &&
            BattleManager.activ &&
            event.player.gameMode != GameMode.CREATIVE) event.isCancelled = true
    }
}