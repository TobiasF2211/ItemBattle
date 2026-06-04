package net.itemBattle.commands

import org.bukkit.command.CommandSender

interface IBCmd {

    val cmd: String

    fun execute(commandSender: CommandSender, args: Array<out String>)

    fun getTabCompleter(commandSender: CommandSender, args: Array<String>): List<String>
}