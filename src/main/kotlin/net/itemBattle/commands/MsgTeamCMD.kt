package net.itemBattle.commands

import net.itemBattle.team.TeamManager
import net.itemBattle.utils.Prefix
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MsgTeamCMD : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) return false

        val team = TeamManager.getTeamFromPlayer(sender)

        if (team == null) {
            sender.sendMessage(Prefix.get() + "You aren't in any team.")
            return true
        }

        val message = args.joinToString(" ")

        sender.sendMessage("§8» §eYou §8→ §aTeam §8» §7$message")

        for (uuid in team.members) {
            val target = Bukkit.getPlayer(uuid) ?: continue

            target.sendMessage("§8» §e${sender.name} §8→ §aTeam §8» §7$message")
        }

        return true
    }
}