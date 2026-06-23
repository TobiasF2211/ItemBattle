package net.itemBattle.objects

import net.itemBattle.team.Team
import org.bukkit.inventory.Inventory

data class Backpack(
    val team: Team,
    val inventory: Inventory
)