package me.despical.classicduels.kits;

import me.despical.classicduels.Main;
import me.despical.classicduels.utils.LayoutHelper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 21.10.2020
 */
public abstract class Kit {

	protected final Main plugin = JavaPlugin.getPlugin(Main.class);

	private final Map<ItemStack, Integer> items = new HashMap<>();

	public abstract void giveArmors(Player player);

	public abstract void registerItems();

	public Map<ItemStack, Integer> getItems() {
		return items;
	}

	public void addItem(ItemStack item, int slot) {
		items.put(item, slot);
	}

	public void giveKit(Player player) {
		giveArmors(player);

		File file = new File(plugin.getDataFolder() + File.separator + "layouts" + File.separator, player.getUniqueId().toString() + ".layout");

		if (file.exists()) {
			LayoutHelper.loadLayout(plugin, player);
			return;
		}

		for (Map.Entry<ItemStack, Integer> item : items.entrySet()) {
			player.getInventory().setItem(item.getValue(), item.getKey());
		}
	}
}