package net.itemBattle.commands.subcmds

import net.itemBattle.commands.IBCmd
import net.itemBattle.manager.BattleManager
import net.itemBattle.team.TeamManager
import net.itemBattle.utils.Prefix
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class StartCMD : IBCmd {

    override val cmd: String = "start"

    override fun execute(commandSender: CommandSender, args: Array<out String>) {
        if (args.size != 3) {
            commandSender.sendMessage(Prefix.get() + "Usage§8: §e/ib start §8<§etime in minutes§8> <§eworld§8>")
            return
        }

        if (TeamManager.teams.size < 2) {
            commandSender.sendMessage(Prefix.get() + "At least two teams have to be configured.")
            return
        }

        val time = args[1].toIntOrNull()

        if (time == null || time < 0) {
            commandSender.sendMessage(Prefix.get() + "You have to enter a correct, positive number.")
            return
        }

        val world = Bukkit.getWorld(args[2])

        if (world == null) {
            commandSender.sendMessage(Prefix.get() + "World not found.")
            return
        }

        BattleManager.start(time, world)
    }

    override fun getTabCompleter(commandSender: CommandSender, args: Array<String>): List<String> {
        if (args.size == 2) {
            return listOf("timeInMinutes")
        }
        if (args.size == 3) {
            return Bukkit.getWorlds().map { it.name }
        }
        return emptyList()
    }
}