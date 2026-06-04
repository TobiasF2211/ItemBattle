package net.itemBattle.team

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.jetbrains.annotations.Nullable
import java.util.*

object TeamManager {

    val teams = ArrayList<Team>()

    val scoreboard = Bukkit.getScoreboardManager().mainScoreboard

    fun createTeam() {
        val scoreTeam = scoreboard.registerNewTeam(UUID.randomUUID().toString())
        val team = Team(scoreTeam)

        teams.add(team)
        team.init()
    }

    fun deleteTeam(teamIndex: Int) {
        teams[teamIndex].scoreboardTeam.unregister()
        teams.removeAt(teamIndex)
    }

    fun getTeam(teamIndex: Int): Team? {
        if (teamIndex !in teams.indices) return null

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