package net.itemBattle.commands

import net.itemBattle.utils.Prefix
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.Locale

import kotlin.Array
import kotlin.Boolean

class MsgCMD : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(Prefix.get() + "Use§8: §7/msg §8<§ePlayer§8> <§eMessage§8>")
            return true
        }

        val target = Bukkit.getPlayer(args[0])

        if (target == null) {
            sender.sendMessage(Prefix.get() + "This player wasn't found.")
            return true
        }

        val message = args.copyOfRange(1, args.size).joinToString(" ")

        sender.sendMessage("§8» §eYou §8→ §e${target.name} §8» §7$message")
        target.sendMessage("§8» §e${sender.name} §8→ §eYou §8» §7$message")
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String> {
        if (args.size == 1) {
            val completions: MutableList<String> = ArrayList()
            val currentInput = args[0].lowercase(Locale.getDefault())

            for (player in Bukkit.getOnlinePlayers()) {
                val name = player.name

                if (name.lowercase(Locale.getDefault()).startsWith(currentInput)) {
                    completions.add(name)
                }
            }

            return completions
        }

        return emptyList()
    }
}