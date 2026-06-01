package net.itemBattle.commands.subcmds

import net.itemBattle.commands.IBCmd
import org.bukkit.command.CommandSender

class TeamConfiguratorCMD : IBCmd {

    override val cmd: String = "teamconfigurator"

    override fun execute(commandSender: CommandSender, args: Array<out String>) {

    }

    override fun getTabCompleter(commandSender: CommandSender, strings: Array<String>): List<String> {
        return listOf()
    }
}