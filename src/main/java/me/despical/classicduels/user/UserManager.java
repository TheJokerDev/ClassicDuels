package me.despical.classicduels.user;

import me.despical.classicduels.ConfigPreferences;
import me.despical.classicduels.Main;
import me.despical.classicduels.api.StatsStorage;
import me.despical.classicduels.arena.Arena;
import me.despical.classicduels.user.data.FileStats;
import me.despical.classicduels.user.data.MysqlManager;
import me.despical.classicduels.user.data.UserDatabase;
import me.despical.classicduels.utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 11.10.2020
 */
public class UserManager {

	private final UserDatabase database;
	private final List<User> users = new ArrayList<>();

	public UserManager(Main plugin) {
		if (plugin.getConfigPreferences().getOption(ConfigPreferences.Option.DATABASE_ENABLED)) {
			database = new MysqlManager(plugin);
			Debugger.debug("MySQL Stats enabled");
		} else {
			database = new FileStats(plugin);
			Debugger.debug("File Stats enabled");
		}

		loadStatsForPlayersOnline();
	}

	private void loadStatsForPlayersOnline() {
		Bukkit.getServer().getOnlinePlayers().stream().map(this::getUser).forEach(this::loadStatistics);
	}

	public User getUser(Player player) {
		for (User user : users) {
			if (user.getPlayer().equals(player)) {
				return user;
			}
		}

		Debugger.debug("Registering new user {0} ({1})", player.getUniqueId(), player.getName());

		User user = new User(player);
		users.add(user);
		return user;
	}

	public List<User> getUsers(Arena arena) {
		return arena.getPlayers().stream().map(this::getUser).collect(Collectors.toList());
	}

	public void saveStatistic(User user, StatsStorage.StatisticType stat) {
		if (!stat.isPersistent()) {
			return;
		}

		database.saveStatistic(user, stat);
	}

	public void saveAllStatistic(User user) {
		database.saveAllStatistic(user);
	}

	public void loadStatistics(User user) {
		database.loadStatistics(user);
	}

	public void removeUser(User user) {
		users.remove(user);
	}

	public UserDatabase getDatabase() {
		return database;
	}
}