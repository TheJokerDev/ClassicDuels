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

package me.despical.classicduels.commands.admin.arena;


import me.despical.classicduels.ConfigPreferences;
import me.despical.classicduels.arena.Arena;
import me.despical.classicduels.arena.ArenaManager;
import me.despical.classicduels.arena.ArenaRegistry;
import me.despical.classicduels.commands.SubCommand;
import me.despical.classicduels.handlers.ChatManager;
import me.despical.classicduels.utils.Debugger;
import me.despical.commonsbox.serializer.InventorySerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 12.10.2020
 */
public class ReloadCommand extends SubCommand {

	private final Set<CommandSender> confirmations = new HashSet<>();

	public ReloadCommand() {
		super("reload");

		setPermission("cd.admin.reload");
	}

	@Override
	public String getPossibleArguments() {
		return null;
	}

	@Override
	public int getMinimumArguments() {
		return 0;
	}

	@Override
	public void execute(CommandSender sender, ChatManager chatManager, String[] args) {
		if (!confirmations.contains(sender)) {
			confirmations.add(sender);
			Bukkit.getScheduler().runTaskLater(plugin, () -> confirmations.remove(sender), 20 * 10);
			sender.sendMessage(chatManager.getPrefix() + chatManager.colorMessage("Commands.Are-You-Sure"));
			return;
		}

		confirmations.remove(sender);
		Debugger.debug("Initiated plugin reload by {0}", sender.getName());
		long start = System.currentTimeMillis();

		plugin.reloadConfig();
		chatManager.reloadConfig();

		for (Arena arena : ArenaRegistry.getArenas()) {
			Debugger.debug("[Reloader] Stopping {0} instance.");
			long stopTime = System.currentTimeMillis();

			for (Player player : arena.getPlayers()) {
				arena.doBarAction(Arena.BarAction.REMOVE, player);

				if (plugin.getConfigPreferences().getOption(ConfigPreferences.Option.INVENTORY_MANAGER_ENABLED)) {
					InventorySerializer.loadInventory(plugin, player);
				} else {
					player.getInventory().clear();
					player.getInventory().setArmorContents(null);
					player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
					player.setWalkSpeed(0.2f);
				}
			}

			ArenaManager.stopGame(true, arena);
			Debugger.debug("[Reloader] Instance {0} stopped took {1} ms", arena.getId(), System.currentTimeMillis() - stopTime);
		}

		ArenaRegistry.registerArenas();

		sender.sendMessage(chatManager.getPrefix() + chatManager.colorMessage("Commands.Admin-Commands.Success-Reload"));
		Debugger.debug("[Reloader] Finished reloading took {0} ms", System.currentTimeMillis() - start);
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Reload all game arenas and configurations", "All of the arenas will be stopped!");
	}

	@Override
	public CommandType getType() {
		return CommandType.GENERIC;
	}

	@Override
	public SenderType getSenderType() {
		return SenderType.BOTH;
	}
}