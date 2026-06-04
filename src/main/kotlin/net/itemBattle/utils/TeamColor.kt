package net.itemBattle.utils

import net.itemBattle.team.Team

enum class TeamColor(var colorString: String) {
    TEAM_1("<red>"),
    TEAM_2("<aqua>"),
    TEAM_3("<green>"),
    TEAM_4("<yellow>"),
    TEAM_5("<purple>");

    companion object {
        fun getColorFromTeam(team: Team): String {
            return TeamColor.entries[team.index()].colorString
        }
    }
}