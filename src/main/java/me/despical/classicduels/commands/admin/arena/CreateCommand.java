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

package me.despical.classicduels.commands.admin.arena;

import me.despical.classicduels.arena.Arena;
import me.despical.classicduels.arena.ArenaRegistry;
import me.despical.classicduels.commands.SubCommand;
import me.despical.classicduels.handlers.ChatManager;
import me.despical.commonsbox.configuration.ConfigUtils;
import me.despical.commonsbox.miscellaneous.MiscUtils;
import me.despical.commonsbox.serializer.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 12.10.2020
 */
public class CreateCommand extends SubCommand {

	public CreateCommand() {
		super("create");

		setPermission("cd.admin.create");
	}

	@Override
	public String getPossibleArguments() {
		return "<ID>";
	}

	@Override
	public int getMinimumArguments() {
		return 0;
	}

	@Override
	public void execute(CommandSender sender, ChatManager chatManager, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(chatManager.getPrefix() + chatManager.colorMessage("Commands.Type-Arena-Name"));
			return;
		}

		Player player = (Player) sender;

		for (Arena arena : ArenaRegistry.getArenas()) {
			if (arena.getId().equalsIgnoreCase(args[0])) {
				player.sendMessage(chatManager.getPrefix() + chatManager.colorRawMessage("&cArena with that ID already exists!"));
				player.sendMessage(chatManager.getPrefix() + chatManager.colorRawMessage("&cUsage: /cd create <ID>"));
				return;
			}
		}

		if (ConfigUtils.getConfig(plugin, "arenas").contains("instances." + args[0])) {
			player.sendMessage(chatManager.getPrefix() + chatManager.colorRawMessage("Instance/Arena already exists! Use another ID or delete it first!"));
		} else {
			createInstanceInConfig(args[0]);
			player.sendMessage(ChatColor.BOLD + "--------------------------------------------");
			MiscUtils.sendCenteredMessage(player, ChatColor.YELLOW + "Instance " + args[0] + " created!");
			player.sendMessage("");
			MiscUtils.sendCenteredMessage(player, ChatColor.GREEN + "Edit this arena via " + ChatColor.GOLD + "/cd edit " + args[0] + ChatColor.GREEN + "!");
			player.sendMessage(ChatColor.BOLD + "--------------------------------------------");
		}
	}

	private void createInstanceInConfig(String id) {
		String path = "instances." + id + ".";
		FileConfiguration config = ConfigUtils.getConfig(plugin, "arenas");
		String loc = LocationSerializer.locationToString(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());

		config.set(path + "endlocation", loc);
		config.set(path + "firstplayerlocation", loc);
		config.set(path + "secondplayerlocation", loc);
		config.set(path + "areaMin", loc);
		config.set(path + "areaMax", loc);
		config.set(path + "mapname", id);
		config.set(path + "signs", new ArrayList<>());
		config.set(path + "isdone", false);

		ConfigUtils.saveConfig(plugin, config, "arenas");

		Arena arena = new Arena(id);

		arena.setMapName(config.getString(path + "mapname"));
		arena.setEndLocation(LocationSerializer.locationFromString(config.getString(path + "endlocation")));
		arena.setFirstPlayerLocation(LocationSerializer.locationFromString(config.getString(path + "firstplayerlocation")));
		arena.setSecondPlayerLocation(LocationSerializer.locationFromString(config.getString(path + "secondplayerlocation")));
		arena.setReady(false);

		ArenaRegistry.registerArena(arena);
	}

	@Override
	public List<String> getTutorial() {
		return Collections.singletonList("Creates new arena with default settings");
	}

	@Override
	public CommandType getType() {
		return CommandType.GENERIC;
	}

	@Override
	public SenderType getSenderType() {
		return SenderType.PLAYER;
	}
}