package net.itemBattle.commands.subcmds

import net.itemBattle.ItemBattle
import net.itemBattle.commands.IBCmd
import net.itemBattle.manager.BattleManager
import net.itemBattle.utils.Prefix
import org.bukkit.command.CommandSender

class AbortCMD : IBCmd {

    override val cmd: String = "abort"

    override fun execute(commandSender: CommandSender, args: Array<out String>) {
        if (!BattleManager.activ) {
            commandSender.sendMessage(Prefix.get() + "No battles are running.")
            return
        }

        ItemBattle.instance.cleanUp()
        BattleManager.finalCleanUp()

        commandSender.sendMessage(Prefix.get() + "Battle was aborted.")
    }

    override fun getTabCompleter(commandSender: CommandSender, args: Array<String>): List<String> {
        return listOf()
    }
}