package me.despical.classicduels.utils;

import me.despical.classicduels.Main;
import me.despical.classicduels.handlers.ChatManager;
import me.despical.commonsbox.item.ItemBuilder;
import me.despical.commonsbox.item.ItemUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CuboidSelector implements Listener {

	private final Main plugin;
	private final Map<Player, Selection> selections = new HashMap<>();

	public CuboidSelector(Main plugin) {
		this.plugin = plugin;

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void giveSelectorWand(Player p) {
		ChatManager chatManager = plugin.getChatManager();
		ItemStack stack = new ItemBuilder(Material.BLAZE_ROD).name(chatManager.colorRawMessage("&6&lArea selector")).build();
		p.getInventory().addItem(stack);

		p.sendMessage(chatManager.colorRawMessage(chatManager.getPrefix() + "&eYou received area selector wand!"));
		p.sendMessage(chatManager.colorRawMessage(chatManager.getPrefix() + "&eSelect bottom corner using left click!"));
	}

	public Selection getSelection(Player p) {
		return selections.getOrDefault(p, null);
	}

	public void removeSelection(Player p) {
		selections.remove(p);
	}

	@EventHandler
	public void onWandUse(PlayerInteractEvent e) {
		if (!ItemUtils.isNamed(e.getItem()) || !e.getItem().getItemMeta().getDisplayName().equals(plugin.getChatManager().colorRawMessage("&6&lArea selector"))) {
			return;
		}

		e.setCancelled(true);

		switch (e.getAction()) {
			case LEFT_CLICK_BLOCK:
				selections.put(e.getPlayer(), new Selection(e.getClickedBlock().getLocation(), null));
				e.getPlayer().sendMessage(plugin.getChatManager().colorRawMessage("&e✔ Completed | &aNow select top corner using right click!"));
				break;
			case RIGHT_CLICK_BLOCK:
				if (!selections.containsKey(e.getPlayer())) {
					e.getPlayer().sendMessage(plugin.getChatManager().colorRawMessage("&c&l✖ &cWarning | Please select bottom corner using left click first!"));
					break;
				}

				selections.put(e.getPlayer(), new Selection(selections.get(e.getPlayer()).getFirstPos(), e.getClickedBlock().getLocation()));

				e.getPlayer().sendMessage(plugin.getChatManager().colorRawMessage("&e✔ Completed | &aNow you can set the area via setup menu!"));
				break;
			case LEFT_CLICK_AIR:
			case RIGHT_CLICK_AIR:
				e.getPlayer().sendMessage(plugin.getChatManager().colorRawMessage("&c&l✖ &cWarning | Please select solid block (not air)!"));
				break;
			default:
				break;
		}
	}

	public static class Selection {

		private final Location firstPos;
		private final Location secondPos;

		public Selection(Location firstPos, Location secondPos) {
			this.firstPos = firstPos;
			this.secondPos = secondPos;
		}

		public Location getFirstPos() {
			return firstPos;
		}

		public Location getSecondPos() {
			return secondPos;
		}
	}
}