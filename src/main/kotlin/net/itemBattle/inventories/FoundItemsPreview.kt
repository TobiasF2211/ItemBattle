package net.itemBattle.inventories

import net.itemBattle.team.Team
import net.itemBattle.utils.Format
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

class FoundItemsPreview(private var player: Player, private var team: Team, private var place: Int) : InventoryHolder {

    private var gui: Inventory = Bukkit.createInventory(this, 6 * 9, Format.of("Place $place"))

    fun openInventory() {
        val grayGlassStack = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
        val grayGlassMeta = grayGlassStack.itemMeta
        grayGlassMeta.displayName(Component.text(""))
        grayGlassStack.setItemMeta(grayGlassMeta)

        val startStack = ItemStack(Material.LIME_WOOL)
        val startMeta = startStack.itemMeta
        startMeta.displayName(Format.of("<dark_gray>» <yellow>Start preview"))
        startMeta.lore(listOf(Format.of("<dark_gray>➥ <red>Only the operator can start the preview.")))
        startStack.setItemMeta(startMeta)

        val placeStack = ItemStack(Material.TRIPWIRE_HOOK)
        val placeStackMeta = placeStack.itemMeta
        placeStackMeta.displayName(Format.of("<dark_gray>» <gray>#<yellow>$place"))
        placeStackMeta.lore(listOf(Format.of("<dark_gray>➥ <gray>Team <yellow>???")))
        placeStack.setItemMeta(placeStackMeta)

        for (slot in 0..9) {
            gui.setItem(slot, grayGlassStack)
        }

        gui.setItem(17, grayGlassStack)
        gui.setItem(18, grayGlassStack)

        gui.setItem(26, grayGlassStack)
        gui.setItem(27, grayGlassStack)

        gui.setItem(35, grayGlassStack)
        gui.setItem(36, grayGlassStack)

        for (slot in 45..53) {
            gui.setItem(slot, grayGlassStack)
        }

        gui.setItem(4, placeStack)
        gui.setItem(10, startStack)
        
        player.openInventory(gui)
    }

    override fun getInventory(): Inventory = gui
}