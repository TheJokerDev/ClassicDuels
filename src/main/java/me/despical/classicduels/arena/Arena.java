/*
 * Classic Duels - Eliminate your opponent to win!
 * Copyright (C) 2021 Despical and contributors
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

import me.confuser.barapi.BarAPI;
import me.despical.classicduels.ConfigPreferences;
import me.despical.classicduels.Main;
import me.despical.classicduels.api.StatsStorage;
import me.despical.classicduels.api.events.game.CDGameStartEvent;
import me.despical.classicduels.api.events.game.CDGameStateChangeEvent;
import me.despical.classicduels.arena.manager.ScoreboardManager;
import me.despical.classicduels.arena.options.ArenaOption;
import me.despical.classicduels.handlers.rewards.Reward;
import me.despical.classicduels.kits.KitRegistry;
import me.despical.classicduels.user.User;
import me.despical.classicduels.utils.Debugger;
import me.despical.commonsbox.configuration.ConfigUtils;
import me.despical.commonsbox.miscellaneous.AttributeUtils;
import me.despical.commonsbox.miscellaneous.MiscUtils;
import me.despical.commonsbox.serializer.InventorySerializer;
import me.despical.commonsbox.serializer.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 11.10.2020
 */
public class Arena extends BukkitRunnable {

	private final Main plugin = JavaPlugin.getPlugin(Main.class);
	private final String id;

	private final List<Player> players = new ArrayList<>();

	private final Map<ArenaOption, Integer> arenaOptions = new EnumMap<>(ArenaOption.class);
	private final Map<GameLocation, Location> gameLocations = new EnumMap<>(GameLocation.class);

	private ArenaState arenaState = ArenaState.INACTIVE;
	private boolean finished = false;
	private final ScoreboardManager scoreboardManager;
	private String mapName = "";
	private boolean ready;

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isFinished() {
		return finished;
	}

	public Arena(String id) {
		this.id = id;

		for (ArenaOption option : ArenaOption.values()) {
			arenaOptions.put(option, option.getDefaultValue());
		}

		scoreboardManager = new ScoreboardManager(this);
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	@Override
	public void run() {
		if (players.isEmpty() && arenaState == ArenaState.WAITING_FOR_PLAYERS) {
			return;
		}

		Debugger.performance("ArenaTask", "[PerformanceMonitor] [{0}] Running game task", getId());
		long start = System.currentTimeMillis();

		switch (getArenaState()) {
			case WAITING_FOR_PLAYERS:
				if (plugin.getConfigPreferences().getOption(ConfigPreferences.Option.BUNGEE_ENABLED)) {
					plugin.getServer().setWhitelist(false);
				}

				if (getPlayers().size() < 2) {
					if (getTimer() <= 0) {
						setTimer(45);
						broadcast(plugin.getChatManager().formatMessage(this, plugin.getChatManager().colorMessage("In-Game.Messages.Lobby-Messages.Waiting-For-Players"), 2));
						break;
					}
				} else {

					broadcast(plugin.getChatManager().colorMessage("In-Game.Messages.Lobby-Messages.Enough-Players-To-Start"));
					setArenaState(ArenaState.STARTING);
					setTimer(plugin.getConfig().getInt("Starting-Waiting-Time", 5));
					showPlayers();
				}

				setTimer(getTimer() - 1);
				break;
			case STARTING:
				if (players.size() == 2 && getTimer() >= plugin.getConfig().getInt("Starting-Waiting-Time", 5)) {
					setTimer(plugin.getConfig().getInt("Starting-Waiting-Time", 5));
					broadcast(plugin.getChatManager().colorMessage("In-Game.Messages.Lobby-Messages.Start-In").replace("%time%", String.valueOf(getTimer())));
				}

				if (!plugin.getConfigPreferences().getOption(ConfigPreferences.Option.DISABLE_LEVEL_COUNTDOWN)) {
					for (Player player : getPlayers()) {
						player.setExp((float) (getTimer() / plugin.getConfig().getDouble("Starting-Waiting-Time", 5)));
						player.setLevel(getTimer());
					}
				}

				if (getPlayers().size() < 2) {

					broadcast(plugin.getChatManager().formatMessage(this, plugin.getChatManager().colorMessage("In-Game.Messages.Lobby-Messages.Waiting-For-Players"), 2));

					setArenaState(ArenaState.WAITING_FOR_PLAYERS);
					Bukkit.getPluginManager().callEvent(new CDGameStartEvent(this));
					setTimer(15);

					for (Player player : getPlayers()) {
						player.setExp(1);
						player.setLevel(0);
					}

					break;
				}

				if (getTimer() == 0) {
					CDGameStartEvent gameStartEvent = new CDGameStartEvent(this);

					Bukkit.getPluginManager().callEvent(gameStartEvent);
					setArenaState(ArenaState.IN_GAME);

					setTimer(6);

					if (players.isEmpty()) {
						break;
					}

					teleportAllToStartLocation();

					for (Player player : getPlayers()) {
						AttributeUtils.setAttackCooldown(player, plugin.getConfig().getDouble("Hit-Cooldown-Delay", 4));
						player.getInventory().clear();
						KitRegistry.getBaseKit().giveKit(player);
						player.setGameMode(GameMode.SURVIVAL);

						ArenaUtils.hidePlayersOutsideTheGame(player, this);
						ArenaUtils.updateNameTagsVisibility(player);

						setTimer(plugin.getConfig().getInt("Classic-Gameplay-Time", 540));

						plugin.getUserManager().getUser(player).addStat(StatsStorage.StatisticType.GAMES_PLAYED, 1);

						for (String msg : plugin.getChatManager().getStringList("In-Game.Messages.Lobby-Messages.Game-Started")) {
							MiscUtils.sendCenteredMessage(player, plugin.getChatManager().colorRawMessage(msg).replace("%opponent%", scoreboardManager.getOpponent(plugin.getUserManager().getUser(player))));
						}

						player.updateInventory();
					}
				}

				if (getTimer() <= 5) {
					broadcast(plugin.getChatManager().colorMessage("In-Game.Messages.Lobby-Messages.Start-In").replace("seconds", getTimer() == 1 ? "second" : "seconds").replace("%time%", String.valueOf(getTimer())));
				}

				setTimer(getTimer() - 1);
				break;
			case IN_GAME:
				if (plugin.getConfigPreferences().getOption(ConfigPreferences.Option.BUNGEE_ENABLED)) {
					plugin.getServer().setWhitelist(2 <= players.size());
				}

				if (getTimer() <= 0) {
					ArenaManager.stopGame(false, this);
					return;
				}

				if (getPlayersLeft().size() < 2) {
					ArenaManager.stopGame(false, this);
					return;
				}

				for (Player player : players) {
					Player opponent = Bukkit.getPlayer(scoreboardManager.getOpponent(plugin.getUserManager().getUser(player)));

					if (opponent != null) {
						player.setCompassTarget(opponent.getLocation());
					}
				}

				setTimer(getTimer() - 1);
				break;
			case ENDING:
				scoreboardManager.stopAllScoreboards();

				if (plugin.getConfigPreferences().getOption(ConfigPreferences.Option.BUNGEE_ENABLED)) {
					plugin.getServer().setWhitelist(false);
				}

				List<Player> playersToQuit = new ArrayList<>(getPlayers());

				for (Player player : playersToQuit) {
					plugin.getUserManager().getUser(player).removeScoreboard();
					player.setGameMode(GameMode.SURVIVAL);

					AttributeUtils.resetAttackCooldown(player);

					for (Player playerToShow : Bukkit.getOnlinePlayers()) {
						player.showPlayer(playerToShow);

						if (!ArenaRegistry.isInArena(playerToShow)) {
							playerToShow.showPlayer(player);
						}
					}

					player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
					player.setWalkSpeed(0.2f);
					player.setFlying(false);
					player.setAllowFlight(false);
					player.getInventory().clear();
					player.getInventory().setArmorContents(null);
					player.setFireTicks(0);
					player.setFoodLevel(20);
				}

				teleportAllToEndLocation();

				if (plugin.getConfigPreferences().getOption(ConfigPreferences.Option.INVENTORY_MANAGER_ENABLED)) {
					players.forEach(player -> InventorySerializer.loadInventory(plugin, player));
				}

				broadcast(plugin.getChatManager().colorMessage("Commands.Teleported-To-The-Lobby"));

				for (User user : plugin.getUserManager().getUsers(this)) {
					user.setSpectator(false);
				}

				plugin.getRewardsFactory().performReward(this, Reward.RewardType.END_GAME);

				if (plugin.getConfigPreferences().getOption(ConfigPreferences.Option.BUNGEE_ENABLED)) {
					if (ConfigUtils.getConfig(plugin, "bungee").getBoolean("Shutdown-When-Game-Ends")) {
						plugin.getServer().shutdown();
					}
				}

				setArenaState(ArenaState.RESTARTING);
				break;
			case RESTARTING:
				players.clear();
				clearArea();
				setArenaState(ArenaState.WAITING_FOR_PLAYERS);
				setFinished(false);

				if (plugin.getConfigPreferences().getOption(ConfigPreferences.Option.BUNGEE_ENABLED)) {
					ArenaRegistry.shuffleBungeeArena();

					for (Player player : Bukkit.getOnlinePlayers()) {
						ArenaManager.joinAttempt(player, ArenaRegistry.getArenas().get(ArenaRegistry.getBungeeArena()));
					}
				}

				break;
			default:
				break;
		}

		Debugger.performance("ArenaTask", "[PerformanceMonitor] [{0}] Game task finished took {1} ms", getId(), System.currentTimeMillis() - start);
	}

	public ScoreboardManager getScoreboardManager() {
		return scoreboardManager;
	}

	/**
	 * Get arena identifier used to get arenas by string.
	 *
	 * @return arena name
	 * @see ArenaRegistry#getArena(String)
	 */
	public String getId() {
		return id;
	}

	/**
	 * Get arena map name.
	 *
	 * @return arena map name, it's not arena id
	 * @see #getId()
	 */
	public String getMapName() {
		return mapName;
	}

	/**
	 * Set arena map name.
	 *
	 * @param mapname new map name, it's not arena id
	 */
	public void setMapName(String mapname) {
		this.mapName = mapname;
	}

	/**
	 * Get timer of arena.
	 *
	 * @return timer of lobby time
	 */
	public int getTimer() {
		return getOption(ArenaOption.TIMER);
	}

	/**
	 * Modify game timer.
	 *
	 * @param timer timer of lobby / time to next wave
	 */
	public void setTimer(int timer) {
		setOptionValue(ArenaOption.TIMER, timer);
	}

	/**
	 * Return game state of arena.
	 *
	 * @return game state of arena
	 * @see ArenaState
	 */
	public ArenaState getArenaState() {
		return arenaState;
	}

	/**
	 * Set game state of arena.
	 *
	 * @param arenaState new game state of arena
	 * @see ArenaState
	 */
	public void setArenaState(ArenaState arenaState) {
		this.arenaState = arenaState;
		CDGameStateChangeEvent gameStateChangeEvent = new CDGameStateChangeEvent(this, getArenaState());
		Bukkit.getPluginManager().callEvent(gameStateChangeEvent);

		plugin.getSignManager().updateSigns();
	}

	/**
	 * Get all players in arena.
	 *
	 * @return set of players in arena
	 */
	public List<Player> getPlayers() {
		return players;
	}

	public void teleportToLobby(Player player) {
		player.setFoodLevel(20);
		player.setFlying(false);
		player.setAllowFlight(false);
		player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
		player.setWalkSpeed(0.2f);

		Location location = players.size() == 1 ? getFirstPlayerLocation() : getSecondPlayerLocation();

		if (location == null) {
			System.out.print("Lobby location isn't initialized for arena " + id);
			return;
		}

		player.teleport(location);
	}

	public void teleportAllToStartLocation() {
		getPlayersLeft().get(0).teleport(getFirstPlayerLocation());
		getPlayersLeft().get(1).teleport(getSecondPlayerLocation());
	}

	public Location getFirstPlayerLocation() {
		return gameLocations.get(GameLocation.FIRST_PLAYER);
	}

	public void setFirstPlayerLocation(Location location) {
		gameLocations.put(GameLocation.FIRST_PLAYER, location);
	}

	public Location getSecondPlayerLocation() {
		return gameLocations.get(GameLocation.SECOND_PLAYER);
	}

	public void setSecondPlayerLocation(Location location) {
		gameLocations.put(GameLocation.SECOND_PLAYER, location);
	}

	public void teleportAllToEndLocation() {
		if (plugin.getConfigPreferences().getOption(ConfigPreferences.Option.BUNGEE_ENABLED) && ConfigUtils.getConfig(plugin, "bungee").getBoolean("End-Location-Hub", true)) {
			players.forEach(plugin.getBungeeManager()::connectToHub);
			return;
		}

		Location location = getEndLocation();

		if (location == null) {
			location = getFirstPlayerLocation();
			System.out.print("End location for arena " + id + " isn't initialized!");
		}

		if (location != null) {
			for (Player player : getPlayers()) {
				player.teleport(location);
			}
		}
	}

	public void teleportToEndLocation(Player player) {
		if (plugin.getConfigPreferences().getOption(ConfigPreferences.Option.BUNGEE_ENABLED) && ConfigUtils.getConfig(plugin, "bungee").getBoolean("End-Location-Hub", true)) {
			plugin.getBungeeManager().connectToHub(player);
			return;
		}

		Location location = getEndLocation();

		if (location == null) {
			location = getFirstPlayerLocation();
			System.out.print("End location for arena " + id + " isn't initialized!");
		}

		if (location != null) {
			player.teleport(location);
		}
	}

	private void clearArea() {
		String s = "instances." + id + ".";
		FileConfiguration config = ConfigUtils.getConfig(plugin, "arenas");
		Location minArea = LocationSerializer.locationFromString(config.getString(s + "areaMin")), maxArea = LocationSerializer.locationFromString(config.getString(s + "areaMax"));

		if (minArea == null || maxArea == null) {
			return;
		}

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			for (int x = (int) minArea.getX(); x <= maxArea.getX(); x++) {
				for (int y = (int) minArea.getY(); y <= maxArea.getY(); y++) {
					for (int z = (int) minArea.getZ(); z <= maxArea.getZ(); z++) {
						Block block = minArea.getWorld().getBlockAt(x, y, z);

						if (plugin.getConfig().getStringList("Whitelisted-Blocks").contains(block.getType().name())) {
							Bukkit.getScheduler().runTask(plugin, () -> block.setType(Material.AIR));
						}
						Location loc = new Location(minArea.getWorld(), x, y, z);
						for(Entity e : loc.getWorld().getNearbyEntities(loc, 2, 2, 2)){
							if (e instanceof Arrow){
								e.remove();
							}
						}
					}
				}
			}
		});
	}

	/**
	 * Get end location of arena.
	 *
	 * @return end location of arena
	 */
	public Location getEndLocation() {
		return gameLocations.get(GameLocation.END);
	}

	/**
	 * Set end location of arena.
	 *
	 * @param endLoc new end location of arena
	 */
	public void setEndLocation(Location endLoc) {
		gameLocations.put(GameLocation.END, endLoc);
	}

	public void broadcast(String msg) {
		players.forEach(p -> p.sendMessage(plugin.getChatManager().colorRawMessage(msg)));
	}

	public void start() {
		Debugger.debug("[{0}] Game instance started", id);
		runTaskTimer(plugin, 20L, 20L);
		setArenaState(ArenaState.RESTARTING);
	}

	void addPlayer(Player player) {
		players.add(player);
	}

	void removePlayer(Player player) {
		if (player != null) {
			players.remove(player);
		}
	}

	public List<Player> getPlayersLeft() {
		return plugin.getUserManager().getUsers(this).stream().filter(user -> !user.isSpectator()).map(User::getPlayer).collect(Collectors.toList());
	}

	void showPlayers() {
		for (Player player : players) {
			for (Player p : players) {
				player.showPlayer(p);
				p.showPlayer(player);
			}
		}
	}

	public int getOption(ArenaOption option) {
		return arenaOptions.get(option);
	}

	public void setOptionValue(ArenaOption option, int value) {
		arenaOptions.put(option, value);
	}

	public enum BarAction {
		ADD, REMOVE
	}

	public enum GameLocation {
		END, FIRST_PLAYER, SECOND_PLAYER
	}
}