package net.itemBattle.commands.subcmds

import net.itemBattle.commands.IBCmd
import net.itemBattle.manager.BattleManager
import net.itemBattle.utils.Prefix
import org.bukkit.command.CommandSender

class PauseTimerCMD : IBCmd {

    override val cmd: String = "pausetimer"

    override fun execute(commandSender: CommandSender, args: Array<out String>) {
        if (!BattleManager.activ) {
            commandSender.sendMessage(Prefix.get() + "No battles are running.")
            return
        }

        val timerUtil = BattleManager.timerUtil
        timerUtil.paused = !timerUtil.paused
        commandSender.sendMessage(Prefix.get() + "The timer was ${if (timerUtil.paused) "paused" else "unpaused"}")
    }

    override fun getTabCompleter(commandSender: CommandSender, args: Array<String>): List<String> {
        return emptyList()
    }
}