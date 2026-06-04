package net.itemBattle.commands.subcmds

import net.itemBattle.commands.IBCmd
import net.itemBattle.inventories.TeamConfiguratorInv
import net.itemBattle.utils.Prefix
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TeamConfiguratorCMD : IBCmd {

    override val cmd: String = "teamconfig"

    override fun execute(commandSender: CommandSender, args: Array<out String>) {
        if (commandSender !is Player) {
            commandSender.sendMessage(Prefix.get() + "§cYou have to be a player to access this manager.")
            return
        }

        TeamConfiguratorInv(commandSender).openInventory()
    }

    override fun getTabCompleter(commandSender: CommandSender, args: Array<String>): List<String> {
        return emptyList()
    }
}