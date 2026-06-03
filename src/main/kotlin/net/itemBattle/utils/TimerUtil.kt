package net.itemBattle.utils

import net.itemBattle.ItemBattle
import net.itemBattle.renderer.TimerRenderer
import net.itemBattle.team.Team
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class TimerUtil {

    var paused = false

    lateinit var runnable: BukkitTask

    var secondsRemaining = 0

    fun start(team: Team, timeInMinutes: Int, timerFinished: () -> Unit) {
        val timerRenderer = TimerRenderer()
        secondsRemaining = timeInMinutes * 60
        timerRenderer.start(team, this)

        this.runnable = object : BukkitRunnable() {
            override fun run() {
                if (!paused) secondsRemaining--

                if (secondsRemaining <= 0) {
                    runnable.cancel()
                    timerRenderer.cancel()
                    timerFinished.apply {}
                }
            }
        }.runTaskTimer(ItemBattle.instance, 0L, 20L)
    }
}