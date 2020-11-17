package me.despical.classicduels.utils;

import me.despical.classicduels.kits.KitRegistry;
import me.despical.commonsbox.number.NumberUtils;
import me.despical.inventoryframework.Gui;
import me.despical.inventoryframework.GuiItem;
import me.despical.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Despical
 * @since 1.0.2
 * <p>
 * Created at 15.11.2020
 */
public class LayoutHelper {

	public static void fillWithDefaultKit(StaticPane pane) {
		Consumer<InventoryClickEvent> event = e -> e.setCancelled(false);

		for (Map.Entry<ItemStack, Integer> item : KitRegistry.getBaseKit().getItems().entrySet()) {
			if (item.getValue() < 9) {
				pane.addItem(new GuiItem(item.getKey(), event), item.getValue(), 4);
			} else {
				pane.addItem(new GuiItem(item.getKey(), event), item.getValue() % 9, 3 - item.getValue() / 8);
			}
		}
	}

	public static void fillWithCurrentOrDefault(JavaPlugin plugin, Player player, StaticPane pane) {
		String uuid = player.getUniqueId().toString();
		File file = new File(plugin.getDataFolder() + File.separator + "layouts" + File.separator, uuid + ".layout");

		if (!file.exists()) {
			fillWithDefaultKit(pane);
			return;
		}

		FileConfiguration invConfig = YamlConfiguration.loadConfiguration(file);
		ItemStack[] invContents = new ItemStack[36];

		for (int i = 0; i < 36; i++) {
			if (invConfig.contains("slot-" + i)) {
				invContents[i] = invConfig.getItemStack("slot-" + i);
			}
		}

		Consumer<InventoryClickEvent> event = e -> e.setCancelled(false);

		for (int i = 0; i < 36; i++) {
			if (invContents[i] != null) {
				if (i < 9) {
					pane.addItem(new GuiItem(invContents[i], event), i, 4);
				} else {
					pane.addItem(new GuiItem(invContents[i], event), (i - 9) % 9, i / 9 - 1);
				}
			}
		}
	}

	public static boolean saveLayoutToFile(JavaPlugin plugin, Player player, Gui gui) {
		String uuid = player.getUniqueId().toString();
		File path = new File(plugin.getDataFolder() + File.separator + "layouts");

		if (gui == null) {
			return false;
		}

		try {
			File invFile = new File(plugin.getDataFolder() + File.separator + "layouts" + File.separator, uuid + ".layout");

			if (!path.exists()) {
				path.mkdir();
			}

			if (invFile.exists()) {
				invFile.delete();
			}

			FileConfiguration invConfig = YamlConfiguration.loadConfiguration(invFile);
			ItemStack[] invContents = gui.getInventory().getContents();

			for (int i = 0; i < invContents.length; i++) {
				ItemStack itemInInv = invContents[i];

				if (NumberUtils.isBetween(i, 0, 26) || NumberUtils.isBetween(i, 36, 44)) {
					if (itemInInv != null && itemInInv.getType() != Material.AIR) {
						int slot = i < 27 ? i + 9 : i % 9;
						invConfig.set("slot-" + slot, itemInInv);
					}
				}
			}

			invConfig.save(invFile);
			return true;
		} catch (Exception ignored) {
			return false;
		}
	}

	public static void resetLayout(JavaPlugin plugin, Player player) {
		String uuid = player.getUniqueId().toString();

		try {
			File file = new File(plugin.getDataFolder() + File.separator + "layouts" + File.separator, uuid + ".layout");

			if (file.exists()) {
				file.delete();
			}

			FileConfiguration config = YamlConfiguration.loadConfiguration(file);

			for (Map.Entry<ItemStack, Integer> item : KitRegistry.getBaseKit().getItems().entrySet()) {
				config.set("slot-" + item.getValue(), item.getKey());
			}

			config.save(file);
		} catch (Exception ignored) {}
	}

	public static void loadLayout(JavaPlugin plugin, Player player) {
		String uuid = player.getUniqueId().toString();

		try {
			File file = new File(plugin.getDataFolder() + File.separator + "layouts" + File.separator, uuid + ".layout");
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);

			for (int i = 0; i < 36; i++) {
				if (config.contains("slot-" + i)) {
					player.getInventory().setItem(i, config.getItemStack("slot-" + i));
				} else {
					player.getInventory().setItem(i, new ItemStack(Material.AIR));
				}
			}
		} catch (Exception ignored) {}

	}
}