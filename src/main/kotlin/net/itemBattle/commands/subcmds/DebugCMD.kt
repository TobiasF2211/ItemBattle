package net.itemBattle.commands.subcmds

import net.itemBattle.commands.IBCmd
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DebugCMD : IBCmd {

    override val cmd: String = "debug"

    override fun execute(commandSender: CommandSender, args: Array<out String>) {
        if (commandSender !is Player) return
    }

    override fun getTabCompleter(commandSender: CommandSender, args: Array<String>): List<String> {
        return emptyList()
    }
}