package net.itemBattle

import net.itemBattle.commands.BackpackCMD
import net.itemBattle.commands.ItemBattleMainCMD
import net.itemBattle.commands.MsgCMD
import net.itemBattle.commands.MsgTeamCMD
import net.itemBattle.listener.InventoryListener
import net.itemBattle.listener.PlayerListener
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
        getCommand("msg")?.setExecutor(MsgCMD())
        getCommand("msgteam")?.setExecutor(MsgTeamCMD())
        getCommand("backpack")?.setExecutor(BackpackCMD())

        registerListener()

        // check if some of our teams still exist and remove them (cause of server crashes for example)
        for (scoreboardTeam in Bukkit.getScoreboardManager().mainScoreboard.teams) {
            if (scoreboardTeam.name.startsWith("itembattle_")) scoreboardTeam.unregister()
        }

        logger.info("Loaded successfully!")
    }

    override fun onDisable() {
        // remove our garbage

        cleanUp()

        logger.info("Unloaded successfully!")
    }

    fun cleanUp() {
        for (team in TeamManager.teams) {
            team.scoreboardTeam.unregister()
        }

        for (member in TeamManager.getAllPlayers()) {
            val player = Bukkit.getPlayer(member) ?: continue

            player.displayName(Component.text(player.name))
        }
    }

    private fun registerListener() {
        val pl = Bukkit.getPluginManager()

        pl.registerEvents(InventoryListener(), this)
        pl.registerEvents(PlayerListener(), this)
    }
}
