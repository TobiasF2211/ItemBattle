package net.itemBattle.inventories

import net.itemBattle.ItemBattle
import net.itemBattle.manager.BattleManager
import net.itemBattle.renderer.Animations
import net.itemBattle.team.Team
import net.itemBattle.utils.Format
import net.itemBattle.utils.formatTime
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class FoundItemsPreview(private var players: ArrayList<Player>, private var sortedTeams: List<Team>, private var place: Int) : InventoryHolder {

    var finished = false

    private var gui: Inventory = Bukkit.createInventory(this, 6 * 9, Format.of("Place $place"))
    private var animationRunning = false
    private var team = sortedTeams[place - 1]

    private val previewSlots = listOf(
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43
    )

    fun openInventory() {
        val grayGlassStack = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
        val grayGlassMeta = grayGlassStack.itemMeta
        grayGlassMeta.displayName(Component.text(""))
        grayGlassStack.setItemMeta(grayGlassMeta)

        val startStack = ItemStack(Material.LIME_WOOL)
        val startMeta = startStack.itemMeta
        startMeta.displayName(Format.of("<dark_gray>» <yellow>Start preview"))
        startMeta.lore(listOf(Format.of("<dark_gray>➥ <red>Only an operator is able to start the preview.")))
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

        for (slot in 44..53) {
            gui.setItem(slot, grayGlassStack)
        }

        gui.setItem(4, placeStack)
        gui.setItem(10, startStack)

        for (player in players) {
            player.openInventory(gui)
        }
    }

    fun startAnimation() {
        var index = 0

        object : BukkitRunnable() {
            override fun run() {
                if (index >= team.foundItems.size) {
                    cancel()
                    finished()
                    return
                }

                val foundItem = team.foundItems[index]

                val foundItemStack = ItemStack(foundItem.item)
                val foundItemMeta = foundItemStack.itemMeta
                val lore = mutableListOf(Format.of("<gray>Found at <yellow>${formatTime(foundItem.time)}"))
                if (foundItem.skipped) lore.add(Format.of("<red>Skipped"))
                foundItemMeta.lore(lore)
                foundItemStack.setItemMeta(foundItemMeta)

                gui.setItem(previewSlots[index % (previewSlots.size)], foundItemStack)

                for (player in players) {
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F)
                }

                index++

                if ((index) % previewSlots.size == 0) {
                    for (slot in previewSlots) {
                        gui.clear(slot)
                    }
                }
            }
        }.runTaskTimer(ItemBattle.instance, 0, 20)
    }

    fun finished() {
        finished = true

        for (player in players) {
            player.closeInventory()
        }

        Animations.placeAnimation(team, place)

        Bukkit.getScheduler().runTaskLater(ItemBattle.instance, Runnable {
            if (place - 1 != 0) {
                FoundItemsPreview(players, sortedTeams, place - 1).openInventory()
            } else BattleManager.finalCleanUp()
        }, 20 * 2)
    }

    fun onClick(slot: Int, event: InventoryClickEvent) {
        if (slot == 10 && event.whoClicked.isOp && !animationRunning) {
            animationRunning = true
            startAnimation()
        }
    }

    fun onClose(player: HumanEntity) {
        // fix stackoverflow
        Bukkit.getScheduler().runTaskLater(ItemBattle.instance, Runnable {
            player.openInventory(gui)
        }, 1L)
    }

    override fun getInventory(): Inventory = gui
}