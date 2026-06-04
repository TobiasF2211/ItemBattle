package net.itemBattle.renderer

import net.itemBattle.utils.Format
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.title.Title
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object Animations {

    fun startAnimation(world: World, player: Player) {
        player.teleport(world.spawnLocation)
        player.respawnLocation = world.spawnLocation
        player.gameMode = GameMode.SURVIVAL
        player.inventory.clear()

        player.showTitle(Title.title(Format.of("<red><b>Start!"), Format.of("<gray>Good luck!")))
        player.playSound(player, Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5F, 1F)
        player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 2 * 20, 1, true))
        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 5 * 20, 3, true))
    }

    fun startInAnimation(player: Player, seconds: Int) {
        player.showTitle(Title.title(Format.of("<dark_gray>» <yellow><b>Start in $seconds</b> <dark_gray>«"), Component.empty()))
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F)
    }

    fun newItemAnimation(player: Player, material: Material) {
        val displayName = Component.translatable(material.translationKey()).color(TextColor.color(0xFFFF55))

        player.sendMessage(Format.of("<dark_gray>----------"))
        player.sendMessage(Format.of("<gray>New Item<dark_gray>: ").append(displayName))
        player.sendMessage(Format.of("<dark_gray>----------"))
    }
}