package net.itemBattle.team

import net.itemBattle.utils.Prefix
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import java.util.*

class Team {

    val members = ArrayList<UUID>()

    val foundItems = ArrayList<Material>()

    var currentItem: Material? = null

    fun addMember(uuid: UUID) {
        this.members.add(uuid)
    }

    fun remove(uuid: UUID) {
        this.members.remove(uuid)
    }

    fun setCurrentItem(material: Material) {
        this.currentItem = material

        // TODO: rendering

        for (uuid in members) {
            val player = Bukkit.getPlayer(uuid) ?: continue

            val displayName = Component.translatable(material.translationKey())

            player.sendMessage(Component.text("New Item: ").append(displayName))
        }
    }

    fun index(): Int = TeamManager.teams.indexOf(this)
}