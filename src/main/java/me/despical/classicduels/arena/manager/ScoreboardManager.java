package me.despical.classicduels.arena.manager;

import me.clip.placeholderapi.PlaceholderAPI;
import me.despical.classicduels.Main;
import me.despical.classicduels.api.StatsStorage;
import me.despical.classicduels.arena.Arena;
import me.despical.classicduels.arena.ArenaState;
import me.despical.classicduels.user.User;
import me.despical.commonsbox.scoreboard.ScoreboardLib;
import me.despical.commonsbox.scoreboard.common.EntryBuilder;
import me.despical.commonsbox.scoreboard.type.Entry;
import me.despical.commonsbox.scoreboard.type.Scoreboard;
import me.despical.commonsbox.scoreboard.type.ScoreboardHandler;
import me.despical.commonsbox.string.StringFormatUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 11.10.2020
 */
public class ScoreboardManager {

	private final Main plugin = JavaPlugin.getPlugin(Main.class);
	private final List<Scoreboard> scoreboards = new ArrayList<>();
	private final Arena arena;

	public ScoreboardManager(Arena arena) {
		this.arena = arena;
	}

	/**
	 * Creates arena scoreboard for target user
	 *
	 * @param user user that represents game player
	 * @see User
	 */
	public void createScoreboard(User user) {
		Scoreboard scoreboard = ScoreboardLib.createScoreboard(user.getPlayer()).setHandler(new ScoreboardHandler() {

			@Override
			public String getTitle(Player player) {
				return plugin.getChatManager().colorMessage("Scoreboard.Title");
			}

			@Override
			public List<Entry> getEntries(Player player) {
				return formatScoreboard(user);
			}
		});

		scoreboard.activate();
		scoreboards.add(scoreboard);
	}

	/**
	 * Removes scoreboard of user
	 *
	 * @param user user that represents game player
	 * @see User
	 */
	public void removeScoreboard(User user) {
		for (Scoreboard board : scoreboards) {
			if (board.getHolder().equals(user.getPlayer())) {
				scoreboards.remove(board);
				board.deactivate();
				return;
			}
		}
	}

	/**
	 * Forces all scoreboards to deactivate.
	 */
	public void stopAllScoreboards() {
		scoreboards.forEach(Scoreboard::deactivate);
		scoreboards.clear();
	}

	private List<Entry> formatScoreboard(User user) {
		EntryBuilder builder = new EntryBuilder();
		List<String> lines;

		if (arena.getArenaState() == ArenaState.IN_GAME) {
			lines = plugin.getChatManager().getStringList("Scoreboard.Content.Playing");
		} else {
			if (arena.getArenaState() == ArenaState.ENDING) {
				lines = plugin.getChatManager().getStringList("Scoreboard.Content.Playing");
			} else {
				lines = plugin.getChatManager().getStringList("Scoreboard.Content." + arena.getArenaState().getFormattedName());
			}
		}

		for (String line : lines) {
			builder.next(formatScoreboardLine(line, user));
		}

		return builder.build();
	}

	private String formatScoreboardLine(String line, User user) {
		String formattedLine = line;

		formattedLine = StringUtils.replace(formattedLine, "%time%", String.valueOf(arena.getTimer()));
		formattedLine = StringUtils.replace(formattedLine, "%duration%", StringFormatUtils.formatIntoMMSS(plugin.getConfig().getInt("Classic-Gameplay-Time", 900) - arena.getTimer()));
		formattedLine = StringUtils.replace(formattedLine, "%formatted_time%", StringFormatUtils.formatIntoMMSS(arena.getTimer()));
		formattedLine = StringUtils.replace(formattedLine, "%mapname%", arena.getMapName());
		formattedLine = StringUtils.replace(formattedLine, "%players%", String.valueOf(arena.getPlayers().size()));
		formattedLine = StringUtils.replace(formattedLine, "%player_health%", String.valueOf((int) user.getPlayer().getHealth()));
		formattedLine = StringUtils.replace(formattedLine, "%opponent%", getOpponent(user));
		formattedLine = StringUtils.replace(formattedLine, "%opponent_health%", String.valueOf(Bukkit.getPlayerExact(getOpponent(user)) != null ? (int) Bukkit.getPlayerExact(getOpponent(user)).getHealth() : 0));
		formattedLine = StringUtils.replace(formattedLine, "%win_streak%", String.valueOf(user.getStat(StatsStorage.StatisticType.WIN_STREAK)));

		if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			formattedLine = PlaceholderAPI.setPlaceholders(user.getPlayer(), formattedLine);
		}

		return formattedLine;
	}

	public String getOpponent(User user) {
		Arena arena = user.getArena();
		Player[] players = arena.getPlayersLeft().toArray(new Player[0]);

		if (arena.getPlayersLeft().size() < 2) {
			return "";
		}

		if (players[0].equals(user.getPlayer())) {
			return players[1] != null ? players[1].getName() : "";
		} else if (players[1].equals(user.getPlayer())) {
			return players[0] != null ? players[0].getName() : "";
		}

		return "";
	}
}