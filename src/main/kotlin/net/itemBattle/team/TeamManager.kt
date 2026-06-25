package net.itemBattle.team

import net.itemBattle.manager.BackpackManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.jetbrains.annotations.Nullable
import java.util.*

object TeamManager {

    val teams = ArrayList<Team>()

    val scoreboard = Bukkit.getScoreboardManager().mainScoreboard

    fun createTeam() {
        val team = Team()
        teams.add(team)
        team.init()
    }

    fun deleteTeam(teamIndex: Int) {
        BackpackManager.removeBackpack(getTeam(teamIndex))

        teams[teamIndex].scoreboardTeam.unregister()
        teams.removeAt(teamIndex)
    }

    fun getTeam(teamIndex: Int): Team {
        return teams[teamIndex]
    }

    @Nullable
    fun getTeamFromPlayer(player: Player): Team? {
        for (team in teams) {
            if (team.members.contains(player.uniqueId)) return team
        }

        return null
    }

    fun getAllPlayers(): List<UUID> {
        val list = ArrayList<UUID>()

        for (team in teams) {
            list.addAll(team.members)
        }

        return list
    }
}