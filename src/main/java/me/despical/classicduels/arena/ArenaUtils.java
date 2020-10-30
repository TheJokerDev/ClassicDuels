package me.despical.classicduels.arena;

import me.despical.classicduels.Main;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Despical
 * @since 1.0.0
 * @see Arena
 * <p>
 * Created at 02.07.2020
 */
public class ArenaUtils {

	private static final Main plugin = JavaPlugin.getPlugin(Main.class);

	public static boolean areInSameArena(Player one, Player two) {
		if (ArenaRegistry.getArena(one) == null || ArenaRegistry.getArena(two) == null) {
			return false;
		}

		return ArenaRegistry.getArena(one).equals(ArenaRegistry.getArena(two));
	}

	public static void hidePlayer(Player p, Arena arena) {
		arena.getPlayers().forEach(player -> player.hidePlayer(plugin, p));
	}

	public static void showPlayer(Player p, Arena arena) {
		arena.getPlayers().forEach(player -> player.showPlayer(plugin, p));
	}

	public static void hidePlayersOutsideTheGame(Player player, Arena arena) {
		for (Player players : plugin.getServer().getOnlinePlayers()) {
			if (arena.getPlayers().contains(players)) {
				continue;
			}

			player.hidePlayer(plugin, players);
			players.hidePlayer(plugin, player);
		}
	}
}