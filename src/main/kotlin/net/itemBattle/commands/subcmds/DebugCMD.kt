package net.itemBattle.commands.subcmds

import net.itemBattle.commands.IBCmd
import net.itemBattle.team.TeamManager
import net.itemBattle.utils.TimerUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DebugCMD : IBCmd {

    override val cmd: String = "debug"

    override fun execute(commandSender: CommandSender, args: Array<out String>) {
        if (commandSender !is Player) return

        TimerUtil().start(TeamManager.getTeam(0)!!, 1,{
            commandSender.sendMessage("Finished")
        })
    }

    override fun getTabCompleter(commandSender: CommandSender, strings: Array<String>): List<String> {
        return emptyList()
    }
}