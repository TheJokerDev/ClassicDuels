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

package me.despical.classicduels.handlers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.despical.classicduels.api.StatsStorage;
import me.despical.classicduels.arena.Arena;
import me.despical.classicduels.arena.ArenaRegistry;
import org.bukkit.entity.Player;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 11.10.2020
 */
public class PlaceholderManager extends PlaceholderExpansion {

	@Override
	public boolean persist() {
		return true;
	}

	public String getIdentifier() {
		return "cd";
	}

	public String getAuthor() {
		return "Despical";
	}

	public String getVersion() {
		return "1.0.2";
	}

	public String onPlaceholderRequest(Player player, String id) {
		if (player == null) {
			return null;
		}

		switch (id.toLowerCase()) {
			case "kills":
				return String.valueOf(StatsStorage.getUserStats(player, StatsStorage.StatisticType.KILLS));
			case "deaths":
				return String.valueOf(StatsStorage.getUserStats(player, StatsStorage.StatisticType.DEATHS));
			case "games_played":
				return String.valueOf(StatsStorage.getUserStats(player, StatsStorage.StatisticType.GAMES_PLAYED));
			case "win_streak":
				return String.valueOf(StatsStorage.getUserStats(player, StatsStorage.StatisticType.WIN_STREAK));
			case "wins":
				return String.valueOf(StatsStorage.getUserStats(player, StatsStorage.StatisticType.WINS));
			case "loses":
				return String.valueOf(StatsStorage.getUserStats(player, StatsStorage.StatisticType.LOSES));
			default:
				return handleArenaPlaceholderRequest(id);
		}
	}

	private String handleArenaPlaceholderRequest(String id) {
		if (!id.contains(":")) {
			return null;
		}

		String[] data = id.split(":");
		Arena arena = ArenaRegistry.getArena(data[0]);

		if (arena == null) {
			return null;
		}

		switch (data[1].toLowerCase()) {
			case "players":
				return String.valueOf(arena.getPlayers().size());
			case "players_left":
				return String.valueOf(arena.getPlayersLeft().size());
			case "timer":
				return String.valueOf(arena.getTimer());
			case "state":
				return String.valueOf(arena.getArenaState());
			case "state_pretty":
				return arena.getArenaState().getFormattedName();
			case "mapname":
				return arena.getMapName();
			default:
				return null;
		}
	}
}