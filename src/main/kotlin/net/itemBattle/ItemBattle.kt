package net.itemBattle

import net.itemBattle.commands.ItemBattleMainCMD
import net.itemBattle.listener.InventoryListener
import net.itemBattle.team.TeamManager
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class ItemBattle : JavaPlugin() {

    companion object {
        lateinit var instance: ItemBattle
            private set
    }

    override fun onEnable() {
        instance = this

        getCommand("itembattle")?.setExecutor(ItemBattleMainCMD())

        registerListener()

        logger.info("Loaded successfully!")
    }

    override fun onDisable() {
        // remove our garbage

        for (team in TeamManager.teams) {
            team.scoreboardTeam.unregister()
        }

        for (member in TeamManager.getAllPlayers()) {
            val player = Bukkit.getPlayer(member) ?: continue

            player.displayName(Component.text(player.name))
        }

        logger.info("Unloaded successfully!")
    }

    private fun registerListener() {
        val pl = Bukkit.getPluginManager()

        pl.registerEvents(InventoryListener(), this)
    }
}
