package net.itemBattle

import net.itemBattle.commands.ItemBattleMainCMD
import net.itemBattle.listener.InventoryListener
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
        logger.info("Unloaded successfully!")
    }

    private fun registerListener() {
        val pl = Bukkit.getPluginManager()

        pl.registerEvents(InventoryListener(), this)
    }
}
