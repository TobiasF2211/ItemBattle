package net.itemBattle.commands

import net.itemBattle.manager.BackpackManager
import net.itemBattle.team.TeamManager
import net.itemBattle.utils.Prefix
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BackpackCMD : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) return false

        val team = TeamManager.getTeamFromPlayer(sender)

        if (team == null) {
            sender.sendMessage(Prefix.get() + "You aren't in any team.")
            return true
        }

        sender.openInventory(BackpackManager.getBackpack(team)!!.inventory)
        return true
    }
}