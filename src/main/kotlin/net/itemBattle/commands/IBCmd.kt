package net.itemBattle.commands

import org.bukkit.command.CommandSender

interface IBCmd {

    val cmd: String

    fun execute(commandSender: CommandSender, args: Array<out String>)

    fun getTabCompleter(commandSender: CommandSender, strings: Array<String>): List<String>
}