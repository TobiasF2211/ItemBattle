package net.itemBattle.manager

import net.itemBattle.ItemBattle
import net.itemBattle.renderer.Animations
import net.itemBattle.team.Team
import net.itemBattle.team.TeamManager
import net.itemBattle.utils.Format
import net.itemBattle.utils.TimerUtil
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

object BattleManager {

    var activ = false
    lateinit var itemChecker: BukkitTask

    fun start(timeInMinutes: Int, world: World) {
        this.activ = true

        object : BukkitRunnable() {
            var seconds = 3

            override fun run() {
                for (uuid in TeamManager.getAllPlayers()) {
                    val player = Bukkit.getPlayer(uuid) ?: continue

                    Animations.startInAnimation(player, seconds)
                }

                seconds--

                if (seconds < 0) {
                    this.cancel()
                    TimerUtil().start(timeInMinutes) { finished() }
                    perTeamAnimation(world)
                    initItemChecker()
                }
            }
        }.runTaskTimer(ItemBattle.instance, 0, 20L)
    }

    private fun perTeamAnimation(world: World) {
        for (team in TeamManager.teams) {
            ItemGenerator.generateItem(team)
            team.itemDisplayOverHead.start(team)

            for (uuid in team.members) {
                val player = Bukkit.getPlayer(uuid) ?: continue

                Animations.startAnimation(world, player)
            }
        }
    }

    private fun initItemChecker() {
        this.itemChecker = object : BukkitRunnable() {
            override fun run() {
                for (uuid in TeamManager.getAllPlayers()) {
                    val player = Bukkit.getPlayer(uuid) ?: continue
                    val team: Team = TeamManager.getTeamFromPlayer(player)!!

                    if (ItemGenerator.containsItem(player.inventory, team.currentItem!!)) {
                        ItemGenerator.generateItem(team)
                    }
                }
            }
        }.runTaskTimerAsynchronously(ItemBattle.instance, 0, 5L)
    }

    private fun finished() {
        this.itemChecker.cancel()

        for (team in TeamManager.teams) {
            team.itemDisplayOverHead.cancel()
        }
    }
}