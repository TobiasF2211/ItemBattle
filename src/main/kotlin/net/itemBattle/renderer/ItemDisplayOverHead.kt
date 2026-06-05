package net.itemBattle.renderer

import net.itemBattle.ItemBattle
import net.itemBattle.team.Team
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Transformation
import org.joml.AxisAngle4f
import org.joml.Vector3f

class ItemDisplayOverHead {

    private lateinit var runnable: BukkitTask

    private val displayMap = HashMap<Player, ItemDisplay>()

    fun start(team: Team) {
        for (uuid in team.members) {
            val player = Bukkit.getPlayer(uuid) ?: continue

            val display = player.world.spawn(
                player.location,
                ItemDisplay::class.java
            ) { entity ->
                entity.isPersistent = false
                entity.teleportDuration = 3

                entity.transformation = Transformation(
                    Vector3f(0f, 3.0f, 0f),
                    AxisAngle4f(0f, 0f, 0f, 1f),
                    Vector3f(0.7f, 0.7f, 0.7f),
                    AxisAngle4f(0f, 0f, 0f, 1f)
                )
            }

            displayMap[player] = display
        }

        this.runnable = object : BukkitRunnable() {
            override fun run() {
                for (uuid in team.members) {
                    val player = Bukkit.getPlayer(uuid) ?: continue
                    val display = displayMap[player] ?: continue

                    if (display.isDead) {
                        display.remove()
                        displayMap.remove(player)
                        continue
                    }

                    if (display.itemStack.type != team.currentItem) {
                        display.setItemStack(ItemStack(team.currentItem ?: Material.BARRIER))
                    }

                    display.teleport(player.location)
                }
            }
        }.runTaskTimer(ItemBattle.instance, 0L, 2L)
    }

    fun cancel() {
        for (display in displayMap.values) {
            display.remove()
        }

        runnable.cancel()
    }
}