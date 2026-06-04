package net.itemBattle.commands.subcmds

import net.itemBattle.commands.IBCmd
import org.bukkit.command.CommandSender

class HelpCMD : IBCmd {

    override val cmd = "help"

    override fun execute(commandSender: CommandSender, args: Array<out String>) {

        fun helpMes(cmd: String, description: String) {
            commandSender.sendMessage("§8> §e$cmd §8| §7$description")
        }

        commandSender.sendMessage("§8--- §eHELP §8---")
        helpMes("help", "Sends this message.")
        helpMes("teamconfig", "Opens the TeamConfigurator GUI.")
        helpMes("start", "Starts the item battle.")
        commandSender.sendMessage("§8--- §eHELP §8---")
    }

    override fun getTabCompleter(commandSender: CommandSender, args: Array<String>): List<String> {
        return emptyList()
    }
}