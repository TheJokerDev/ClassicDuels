package me.despical.classicduels.kits;

import me.despical.classicduels.Main;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 21.10.2020
 */
public abstract class Kit {

	protected final Main plugin = JavaPlugin.getPlugin(Main.class);

	public abstract void giveItems(Player player);
}