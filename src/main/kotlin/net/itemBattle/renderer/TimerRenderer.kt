package net.itemBattle.renderer

import net.itemBattle.ItemBattle
import net.itemBattle.team.TeamManager
import net.itemBattle.utils.Format
import net.itemBattle.utils.TimerUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class TimerRenderer {

    lateinit var runnable: BukkitTask
    lateinit var timerUtil: TimerUtil

    fun start(timerUtil: TimerUtil) {
        this.timerUtil = timerUtil

        this.runnable = object : BukkitRunnable() {
            override fun run() {
                for (uuid in TeamManager.getAllPlayers()) {
                    val player = Bukkit.getPlayer(uuid) ?: continue

                    sendActionBar(player)
                }
            }
        }.runTaskTimer(ItemBattle.instance, 0, 1L)
    }

    fun cancel() = runnable.cancel()

    fun sendActionBar(player: Player) {
        val seconds = timerUtil.secondsRemaining
        val phase = gradientPhase()

        val hours = seconds / 3600
        val minutes = seconds / 60 % 60
        val remainingSeconds = seconds % 60

        val actionBarMessage = Format.of(
            "<dark_gray>\u00bb <gradient:#fff7b8:#ffe200:$phase> <bold>${String.format(
                "%02dh %02dm %02ds",
                hours, minutes, remainingSeconds
            )}</bold> </gradient><dark_gray>\u00ab"
        )

        player.sendActionBar(actionBarMessage)
    }

    private fun gradientPhase(): Double {
        val now = System.currentTimeMillis()

        val progress = (now % 3000) / 3000.0

        return progress * 2.0 - 1.0
    }
}