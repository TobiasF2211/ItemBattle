package net.itemBattle.team

import java.util.*

class Team {

    val members = ArrayList<UUID>()

    fun addMember(uuid: UUID) {
        this.members.add(uuid)
    }

    fun remove(uuid: UUID) {
        this.members.remove(uuid)
    }

    fun index(): Int = TeamManager.teams.indexOf(this)
}