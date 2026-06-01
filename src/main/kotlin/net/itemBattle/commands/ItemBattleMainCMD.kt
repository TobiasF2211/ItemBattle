package net.itemBattle.commands

import net.itemBattle.commands.subcmds.HelpCMD
import net.itemBattle.commands.subcmds.TeamConfiguratorCMD
import net.itemBattle.utils.Prefix
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ItemBattleMainCMD : CommandExecutor {

    val subCommands = HashMap<String, IBCmd>()

    init {
        add(HelpCMD())
        add(TeamConfiguratorCMD())
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
}