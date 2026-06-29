package net.itemBattle.listener

import net.itemBattle.manager.BattleManager
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.persistence.PersistentDataType
import java.util.*

class PlayerListener : Listener {

    val skipsFromDeath = HashMap<UUID, Int>()

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        var skipAmount = 0

        val iterator = event.drops.iterator()

        while (iterator.hasNext()) {
            val drop = iterator.next()

            val dropMeta = drop.itemMeta

            if (dropMeta == null ||
                !dropMeta.persistentDataContainer.has(
                    BattleManager.getSkipNamespaceKey(),
                    PersistentDataType.BYTE
                )
            ) continue

            skipAmount += drop.amount
            iterator.remove()
        }

        skipsFromDeath[event.player.uniqueId] = skipAmount
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        if (!BattleManager.activ) return

        val player = event.player

        val skipStack = BattleManager.getSkipItemStack()
        skipStack.amount = skipsFromDeath[player.uniqueId] ?: return

        player.inventory.addItem(skipStack)
        skipsFromDeath.remove(player.uniqueId)
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (
            event.blockPlaced.type == Material.BARRIER &&
            BattleManager.activ &&
            event.player.gameMode != GameMode.CREATIVE
        ) event.isCancelled = true
    }
}