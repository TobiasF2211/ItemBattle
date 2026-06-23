package net.itemBattle.manager

import net.itemBattle.team.Team
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import kotlin.random.Random

object ItemGenerator {

    private val illegalItems = listOf(
        Material.BARRIER, Material.BEDROCK,
        Material.COMMAND_BLOCK, Material.COMMAND_BLOCK_MINECART,
        Material.CHAIN_COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK,
        Material.TEST_BLOCK, Material.TEST_INSTANCE_BLOCK,
        Material.LIGHT, Material.AIR,
        Material.JIGSAW, Material.STRUCTURE_VOID, Material.STRUCTURE_BLOCK,
        Material.DEBUG_STICK,
        Material.REINFORCED_DEEPSLATE, Material.FIRE, Material.SOUL_FIRE, Material.LAVA,
        Material.WATER, Material.NETHER_PORTAL, Material.END_PORTAL, Material.END_PORTAL_FRAME,
        Material.END_GATEWAY, Material.SPAWNER, Material.VAULT, Material.TRIAL_SPAWNER, Material.WATER_CAULDRON, Material.LAVA_CAULDRON
    )

    fun generateItem(team: Team) {
        val random = Random

        var material: Material

        do {
            val index = random.nextInt(0, Material.entries.size)
            material = Material.entries[index]
        } while (!material.isItem || illegalItems.contains(material) || isSpawnEgg(material) || isLegacy(material))

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

    private fun isLegacy(material: Material): Boolean {
        return material.name.startsWith("LEGACY_")
    }
}