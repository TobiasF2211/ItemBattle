package net.itemBattle.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object Format {

    fun of(text: String): Component {
        return MiniMessage.miniMessage().deserialize("<italic:false>$text")
    }
}