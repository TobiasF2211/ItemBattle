package net.itemBattle.manager

import net.itemBattle.team.Team
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import kotlin.random.Random

object ItemGenerator {

    private val illegalItems = listOf(Material.BARRIER, Material.BEDROCK)

    fun generateItem(team: Team) {
        val random = Random

        var material: Material

        do {
            val index = random.nextInt(0, Material.entries.size)
            material = Material.entries[index]
        } while (!material.isItem || illegalItems.contains(material) || isSpawnEgg(material))

        team.currentItem = material
    }

    fun containsItem(inv: Inventory, material: Material): Boolean {
        for (content in inv.contents) {
            if (content == null) continue

            if (content.type == material) return true
        }

        return false
    }

    private fun isSpawnEgg(material: Material): Boolean {
        return material.name.endsWith("_SPAWN_EGG")
    }
}