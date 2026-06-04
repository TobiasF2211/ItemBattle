package net.itemBattle.manager

import net.itemBattle.ItemBattle
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
            var seconds = 5

            override fun run() {
                for (uuid in TeamManager.getAllPlayers()) {
                    val player = Bukkit.getPlayer(uuid) ?: continue

                    player.showTitle(Title.title(Format.of("<dark_gray>» <yellow><b>Start in $seconds</b> <dark_gray>«"), Component.empty()))
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F)
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

            for (uuid in team.members) {
                val player = Bukkit.getPlayer(uuid) ?: continue

                player.teleport(world.spawnLocation)
                player.respawnLocation = world.spawnLocation
                player.gameMode = GameMode.SURVIVAL
                player.inventory.clear()

                player.showTitle(Title.title(Format.of("<red><b>Start!"), Format.of("<gray>Good luck!")))
                player.playSound(player, Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5F, 1F)
                player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 2 * 20, 1, true))
                player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 5 * 20, 3, true))
            }
        }
    }

    private fun initItemChecker() {
        Bukkit.getLogger().info("Init")

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
        Bukkit.getLogger().info("cancel")
        this.itemChecker.cancel()
    }
}