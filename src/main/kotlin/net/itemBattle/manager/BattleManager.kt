package net.itemBattle.manager

import net.itemBattle.ItemBattle
import net.itemBattle.inventories.FoundItemsPreview
import net.itemBattle.objects.FoundItem
import net.itemBattle.objects.TimerUtil
import net.itemBattle.renderer.Animations
import net.itemBattle.team.Team
import net.itemBattle.team.TeamManager
import net.itemBattle.utils.Format
import net.itemBattle.utils.Prefix
import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask


object BattleManager {

    val timerUtil = TimerUtil()

    var activ = false

    lateinit var itemChecker: BukkitTask

    fun getSkipItemStack(): ItemStack {
        val key = NamespacedKey(ItemBattle.instance, "skip_itemstack")

        val skipStack = ItemStack(Material.BARRIER)
        val skipMeta = skipStack.itemMeta
        skipMeta.displayName(Format.of("<dark_gray>» <yellow>Skip"))
        skipMeta.lore(listOf(Format.of("<dark_gray>➥ <gray>Skip the current item by right clicking.")))
        skipMeta.addEnchant(Enchantment.MENDING, 1, true)
        skipMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        skipMeta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1.toByte());
        skipStack.itemMeta = skipMeta

        return skipStack
    }

    fun start(timeInMinutes: Int, world: World, skipsPerPlayer: Int) {
        this.activ = true

        object : BukkitRunnable() {
            var seconds = 3

            override fun run() {
                for (uuid in TeamManager.getAllPlayers()) {
                    val player = Bukkit.getPlayer(uuid) ?: continue

                    Animations.startInAnimation(player, seconds)
                }

                seconds--

                if (seconds < 0) {
                    this.cancel()
                    timerUtil.start(timeInMinutes, { seconds -> sendReminders(seconds) }) { finished() }
                    perTeamAnimation(world, skipsPerPlayer)
                    initItemChecker()
                }
            }
        }.runTaskTimer(ItemBattle.instance, 0, 20L)
    }

    private fun perTeamAnimation(world: World, skipsPerPlayer: Int) {
        for (team in TeamManager.teams) {
            ItemGenerator.generateItem(team)
            team.itemDisplayOverHead.start(team)

            for (uuid in team.members) {
                val player = Bukkit.getPlayer(uuid) ?: continue

                Animations.startAnimation(world, player)

                val skipStack = getSkipItemStack()
                skipStack.amount = skipsPerPlayer
                team.skipsPerPlayer[uuid] = skipsPerPlayer
                player.inventory.addItem(skipStack)
            }
        }
    }

    private fun initItemChecker() {
        this.itemChecker = object : BukkitRunnable() {
            override fun run() {
                for (uuid in TeamManager.getAllPlayers()) {
                    val player = Bukkit.getPlayer(uuid) ?: continue
                    val team: Team = TeamManager.getTeamFromPlayer(player)!!

                    if (ItemGenerator.containsItem(player.inventory, team.currentItem!!)) {
                        team.foundItems.add(FoundItem(team.currentItem!!, false, timerUtil.secondsRemaining))
                        ItemGenerator.generateItem(team)
                    }
                }
            }
        }.runTaskTimerAsynchronously(ItemBattle.instance, 0, 5L)
    }

    private fun sendReminders(seconds: Int) {
        fun sendReminder(seconds: Int) {
            for (uuid in TeamManager.getAllPlayers()) {
                val player = Bukkit.getPlayer(uuid) ?: continue

                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F)
                player.sendMessage(Prefix.get() + "§e${seconds}s §7remaining.")
            }
        }

        when (seconds) {
            5, 4, 3, 2, 1, 60, 60 * 5, 60 * 10 -> sendReminder(seconds)
        }
    }

    private fun finished() {
        this.itemChecker.cancel()

        for (team in TeamManager.teams) {
            team.itemDisplayOverHead.cancel()
        }

        val players = ArrayList<Player>()

        for (uuid in TeamManager.getAllPlayers()) {
            val player = Bukkit.getPlayer(uuid) ?: continue

            players.add(player)

            player.playSound(player, Sound.ENTITY_ENDER_DRAGON_GROWL, 1F, 1F)
            player.gameMode = GameMode.SPECTATOR
        }

        ItemBattle.instance.cleanUp()

        val sortedTeams = TeamManager.teams.sortedByDescending { it.foundItems.size }

        FoundItemsPreview(players, sortedTeams, sortedTeams.size).openInventory()
    }
}