package me.despical.classicduels.events.spectator;

import me.despical.classicduels.Main;
import me.despical.classicduels.arena.Arena;
import me.despical.classicduels.arena.ArenaRegistry;
import me.despical.commonsbox.compat.XMaterial;
import me.despical.commonsbox.item.ItemUtils;
import me.despical.commonsbox.number.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 27.10.2020
 */
public class SpectatorItemEvents implements Listener {

	private final Main plugin;

	public SpectatorItemEvents(Main plugin) {
		this.plugin = plugin;

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onSpectatorItemClick(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() != Action.PHYSICAL) {
			if (!ArenaRegistry.isInArena(e.getPlayer())) {
				return;
			}

			ItemStack stack = e.getPlayer().getInventory().getItemInMainHand();

			if (!stack.hasItemMeta() || !stack.getItemMeta().hasDisplayName()) {
				return;
			}

			e.setCancelled(true);

			if (stack.getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getChatManager().colorMessage("In-Game.Spectator.Spectator-Item-Name"))) {
				openSpectatorMenu(e.getPlayer().getWorld(), e.getPlayer());
			} else if (stack.getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getChatManager().colorMessage("In-Game.Spectator.Settings-Menu.Item-Name"))) {
				new SpectatorSettingsMenu(e.getPlayer()).openInventory();
			}
		}
	}

	private void openSpectatorMenu(World world, Player p) {
		Inventory inventory = plugin.getServer().createInventory(null, NumberUtils.serializeInt(ArenaRegistry.getArena(p).getPlayers().size()),
			plugin.getChatManager().colorMessage("In-Game.Spectator.Spectator-Menu-Name"));
		List<Player> players = ArenaRegistry.getArena(p).getPlayers();

		for (Player player : world.getPlayers()) {
			if (players.contains(player) && !plugin.getUserManager().getUser(player).isSpectator()) {
				ItemStack skull = XMaterial.PLAYER_HEAD.parseItem();
				SkullMeta meta = (SkullMeta) skull.getItemMeta();
				meta = ItemUtils.setPlayerHead(player, meta);
				meta.setDisplayName(player.getName());

				skull.setDurability((short) SkullType.PLAYER.ordinal());
				skull.setItemMeta(meta);
				inventory.addItem(skull);
			}
		}

		p.openInventory(inventory);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onSpectatorInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		if (!ArenaRegistry.isInArena(p)) {
			return;
		}

		Arena arena = ArenaRegistry.getArena(p);

		if (!ItemUtils.isNamed(e.getCurrentItem())) {
			return;
		}

		if (!e.getView().getTitle().equalsIgnoreCase(plugin.getChatManager().colorMessage("In-Game.Spectator.Spectator-Menu-Name", p))) {
			return;
		}

		e.setCancelled(true);
		ItemMeta meta = e.getCurrentItem().getItemMeta();

		for (Player player : arena.getPlayers()) {
			if (player.getName().equalsIgnoreCase(meta.getDisplayName()) || ChatColor.stripColor(meta.getDisplayName()).contains(player.getName())) {
				p.sendMessage(plugin.getChatManager().getPrefix() + plugin.getChatManager().formatMessage(arena, plugin.getChatManager().colorMessage("Commands.Admin-Commands.Teleported-To-Player"), player));
				p.teleport(player);
				p.closeInventory();
				return;
			}
		}

		p.sendMessage(plugin.getChatManager().getPrefix() + plugin.getChatManager().colorMessage("Commands.Admin-Commands.Player-Not-Found"));
	}
}