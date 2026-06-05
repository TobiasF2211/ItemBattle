package net.itemBattle.manager

import net.itemBattle.ItemBattle
import net.itemBattle.renderer.Animations
import net.itemBattle.team.Team
import net.itemBattle.team.TeamManager
import net.itemBattle.utils.Format
import net.itemBattle.utils.Prefix
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
import java.util.Arrays

object BattleManager {

    val timerUtil = TimerUtil()

    var activ = false

    lateinit var itemChecker: BukkitTask

    val places = HashMap<Int, Team>()

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
                    timerUtil.start(timeInMinutes, { seconds -> sendReminders(seconds) }) { finished() }
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
                        team.foundItems.add(team.currentItem!!)
                        ItemGenerator.generateItem(team)
                    }
                }
            }
        }.runTaskTimerAsynchronously(ItemBattle.instance, 0, 5L)
    }

    private fun sendReminders(seconds: Int) {
        fun sendReminder(seconds: Int) {
            for (uuid in TeamManager.getAllPlayers()) {
                val player = Bukkit.getPlayer(uuid) ?: continue

                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F)
                player.sendMessage(Prefix.get() + "§e${seconds}s §7remaining.")
            }
        }

        when (seconds) {
            5, 4, 3, 2, 1, 60, 60 * 5, 60 * 10 -> sendReminder(seconds)
        }
    }

    private fun finished() {
        this.itemChecker.cancel()

        for (team in TeamManager.teams) {
            team.itemDisplayOverHead.cancel()
        }

        for (uuid in TeamManager.getAllPlayers()) {
            val player = Bukkit.getPlayer(uuid) ?: continue

            player.playSound(player, Sound.ENTITY_ENDER_DRAGON_GROWL, 1F, 1F)
            player.gameMode = GameMode.SPECTATOR
        }

        ItemBattle.instance.cleanUp()

        val sortedTeams = TeamManager.teams.sortedByDescending { it.foundItems.size }

        for ((place, team) in sortedTeams.withIndex()) {
            places[place + 1] = team

            Bukkit.getLogger().info(
                "place ${place + 1}: Team ${team.index() + 1} (${team.foundItems.size} items)"
            )
        }
    }
}