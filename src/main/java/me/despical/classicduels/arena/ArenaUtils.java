/*
 * Classic Duels - Eliminate your opponent to win!
 * Copyright (C) 2020 Despical and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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