package net.itemBattle.team

import java.util.UUID

data class Team(val teamIndex: Int) {

    val members = ArrayList<UUID>()

    fun addMember(uuid: UUID) {
        this.members.add(uuid)
    }

    fun remove(uuid: UUID) {
        this.members.remove(uuid)
    }
}