package net.itemBattle.team

object TeamManager {

    val teams = ArrayList<Team>()

    fun createTeam() {
        teams.add(Team(teams.size))
    }

    fun deleteTeam(teamIndex: Int) {
        teams.removeAt(teamIndex)
    }

    fun getTeam(teamIndex: Int): Team? {
        if (teamIndex !in teams.indices) return null

        return teams[teamIndex]
    }
}