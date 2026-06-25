package net.itemBattle.manager

import net.itemBattle.objects.Backpack
import net.itemBattle.team.Team
import net.itemBattle.utils.Format
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

object BackpackManager {

    private val backpacks = mutableMapOf<Team, Backpack>()

    fun createBackpack(team: Team): Backpack {
        val backpack = Backpack(team, BackpackInventory().gui)

        backpacks[team] = backpack
        return backpack
    }

    fun removeBackpack(team: Team) {
        backpacks.remove(team)
    }

    fun getBackpack(team: Team): Backpack? = backpacks[team]

    fun clear() {
        backpacks.clear()
    }

    class BackpackInventory : InventoryHolder {

        val gui = Bukkit.createInventory(this, 54, Format.of("Backpack"))

        override fun getInventory(): Inventory {
            return gui
        }
    }
}