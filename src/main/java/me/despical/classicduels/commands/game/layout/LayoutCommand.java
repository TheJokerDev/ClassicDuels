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

package me.despical.classicduels.commands.game.layout;

import me.despical.classicduels.arena.ArenaRegistry;
import me.despical.classicduels.commands.SubCommand;
import me.despical.classicduels.commands.game.layout.menu.LayoutMenu;
import me.despical.classicduels.handlers.ChatManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Despical
 * @since 1.0.2
 * <p>
 * Created at 15.11.2020
 */
public class LayoutCommand extends SubCommand {

	public LayoutCommand() {
		super("layout");
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
		Player player = (Player) sender;

		if (ArenaRegistry.isInArena(player)) {
			player.sendMessage(chatManager.getPrefix() + chatManager.colorMessage("Only-Command-Ingame-Is-Leave"));
			return;
		}

		new LayoutMenu(player).openGui();
	}

	@Override
	public List<String> getTutorial() {
		return null;
	}

	@Override
	public CommandType getType() {
		return CommandType.HIDDEN;
	}

	@Override
	public SenderType getSenderType() {
		return SenderType.PLAYER;
	}
}