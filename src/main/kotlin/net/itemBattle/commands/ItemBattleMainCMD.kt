package net.itemBattle.commands

import net.itemBattle.commands.subcmds.*
import net.itemBattle.utils.Prefix
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class ItemBattleMainCMD : CommandExecutor, TabCompleter {

    val subCommands = HashMap<String, IBCmd>()

    init {
        add(HelpCMD())
        add(TeamConfiguratorCMD())
        add(DebugCMD())
        add(StartCMD())
        add(PauseTimerCMD())
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp || !sender.hasPermission("itembattle.cmd.use")) {
            sender.sendMessage(Prefix.get() + "You have no permission to do that.")
            return true
        }

        if (!args.isEmpty() && subCommands.contains(args[0])) {
            subCommands[args[0]]?.execute(sender, args)
        } else {
            HelpCMD().execute(sender, args)
        }

        return true
    }

    private fun add(cmd: IBCmd) {
        subCommands[cmd.cmd] = cmd
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String> {
        val completions = ArrayList<String>()

        if (args.size == 1) {
            val currentInput: String = args[0].lowercase()

            completions.addAll(
                subCommands.keys.stream()
                    .filter {
                        completion: String -> completion.startsWith(currentInput)
                    }
                    .toList()
            )

            return completions
        }

        for (cmdString in subCommands.keys) {
            if (cmdString != args[0]) continue

            completions.addAll(subCommands[cmdString]!!.getTabCompleter(sender, args))
        }

        return completions
    }
}