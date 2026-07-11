package net.itemBattle.team

import net.itemBattle.manager.BackpackManager
import net.itemBattle.manager.BattleManager
import net.itemBattle.manager.ItemGenerator
import net.itemBattle.objects.FoundItem
import net.itemBattle.renderer.Animations
import net.itemBattle.renderer.ItemDisplayOverHead
import net.itemBattle.team.TeamManager.scoreboard
import net.itemBattle.utils.Format
import net.itemBattle.utils.TeamColor
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.Team
import java.util.*

class Team {

    val members = ArrayList<UUID>()
    val foundItems = ArrayList<FoundItem>()

    val itemDisplayOverHead = ItemDisplayOverHead()
    var scoreboardTeam = scoreboard.registerNewTeam("itembattle_${UUID.randomUUID()}")

    lateinit var prefix: String

    fun init() {
        this.prefix = "${TeamColor.getColorFromTeam(this)}Team ${index() + 1} <dark_gray>|"
        this.scoreboardTeam.prefix(Format.of("$prefix "))
        BackpackManager.createBackpack(this)
    }

    var currentItem: Material? = null
        set(material) {
            field = material ?: return

            renderCurrentItem(material)
        }

    fun addMember(player: Player) {
        this.members.add(player.uniqueId)

        scoreboardTeam.addPlayer(player)
        player.displayName(Format.of("$prefix <white>${player.name}"))
    }

    fun remove(uuid: UUID) {
        this.members.remove(uuid)

        val player = Bukkit.getPlayer(uuid) ?: return

        scoreboardTeam.removePlayer(player)
        player.displayName(Component.text(player.name))
    }

    fun index(): Int = TeamManager.teams.indexOf(this)

    fun skipCurrentItem(playerWhoSkipped: Player) {
        playerWhoSkipped.inventory.addItem(ItemStack(currentItem!!))

        for (uuid in members) {
            val player = Bukkit.getPlayer(uuid) ?: continue

            Animations.skipAnimation(player, playerWhoSkipped)
        }

        foundItems.add(FoundItem(currentItem!!, true, BattleManager.timerUtil.secondsRemaining))
        ItemGenerator.generateItem(this)
    }

    private fun renderCurrentItem(material: Material) {
        var someoneHasIt = false

        for (uuid in members) {
            val player = Bukkit.getPlayer(uuid) ?: continue

            Animations.newItemAnimation(player, material)

            // check if someone already has the item
            if (ItemGenerator.containsItem(player.inventory, material)) someoneHasIt = true
        }

        // now the item generator generates only ones.
        if (someoneHasIt) ItemGenerator.generateItem(this)
    }
}