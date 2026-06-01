package net.itemBattle.inventories

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

class TeamConfiguratorInv(private var player: Player) : InventoryHolder {

    private var gui: Inventory = Bukkit.createInventory(this, 6 * 9, Component.text("TeamConfigurator"))

    fun openInventory() {
        val createTeamStack = ItemStack(Material.LIME_WOOL)
        val createTeamMeta = createTeamStack.itemMeta
        createTeamMeta.displayName(Component.text("§8» §eCreate New Team"))
        createTeamStack.setItemMeta(createTeamMeta)



        player.openInventory(gui)
    }

    fun onClick(event: InventoryClickEvent, slot: Int) {

    }

    override fun getInventory(): Inventory = gui
}