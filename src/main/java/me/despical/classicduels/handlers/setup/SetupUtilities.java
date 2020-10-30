package me.despical.classicduels.handlers.setup;

import me.despical.classicduels.Main;
import me.despical.commonsbox.serializer.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 11.10.2020
 */
public class SetupUtilities {

	private final Main plugin = JavaPlugin.getPlugin(Main.class);
	private final FileConfiguration config;

	SetupUtilities(FileConfiguration config) {
		this.config = config;
	}

	public String isOptionDoneBool(String path) {
		if (config.isSet(path)) {
			if (Bukkit.getServer().getWorlds().get(0).getSpawnLocation().equals(LocationSerializer.locationFromString(config.getString(path)))) {
				return plugin.getChatManager().colorRawMessage("&c&l✘ Not Completed");
			}

			return plugin.getChatManager().colorRawMessage("&a&l✔ Completed");
		}

		return plugin.getChatManager().colorRawMessage("&c&l✘ Not Completed");
	}
}