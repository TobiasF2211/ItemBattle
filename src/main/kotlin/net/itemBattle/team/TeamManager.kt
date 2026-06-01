package net.itemBattle.team

class TeamManager {

    val teams = HashMap<String, Team>()

    fun createTeam(teamName: String) {
        teams[teamName] = Team(teamName)
    }

    fun getTeam(teamName: String): Team? {
        return teams[teamName]
    }
}