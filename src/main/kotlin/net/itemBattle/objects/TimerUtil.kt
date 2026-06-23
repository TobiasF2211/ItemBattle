package net.itemBattle.objects

import net.itemBattle.ItemBattle
import net.itemBattle.renderer.TimerRenderer
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class TimerUtil {

    var paused = false

    lateinit var runnable: BukkitTask

    var secondsRemaining = 0

    fun start(timeInMinutes: Int, timePassed: (Int) -> Unit, timerFinished: () -> Unit) {
        val timerRenderer = TimerRenderer()
        secondsRemaining = timeInMinutes * 60
        timerRenderer.start(this)

        this.runnable = object : BukkitRunnable() {
            override fun run() {
                if (!paused) {
                    secondsRemaining--
                    timePassed(secondsRemaining)
                }

                if (secondsRemaining <= 0) {
                    timerFinished()
                    runnable.cancel()
                    timerRenderer.cancel()
                }
            }
        }.runTaskTimer(ItemBattle.instance, 0L, 20L)
    }
}