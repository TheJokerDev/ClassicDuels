package me.despical.classicduels.commands.game;

import me.despical.classicduels.ConfigPreferences;
import me.despical.classicduels.arena.Arena;
import me.despical.classicduels.arena.ArenaManager;
import me.despical.classicduels.arena.ArenaRegistry;
import me.despical.classicduels.commands.SubCommand;
import me.despical.classicduels.handlers.ChatManager;
import me.despical.classicduels.utils.Debugger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 12.10.2020
 */
public class LeaveCommand extends SubCommand {

	public LeaveCommand() {
		super("leave");
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
		if (!plugin.getConfig().getBoolean("Disable-Leave-Command")) {
			Player player = (Player) sender;

			if (!checkIsInGameInstance((Player) sender)) {
				return;
			}

			player.sendMessage(chatManager.getPrefix() + chatManager.colorMessage("Commands.Teleported-To-The-Lobby", player));

			if (plugin.getConfigPreferences().getOption(ConfigPreferences.Option.BUNGEE_ENABLED)) {
				plugin.getBungeeManager().connectToHub(player);
				Debugger.debug("{0} was teleported to the Hub server", player.getName());
				return;
			}

			Arena arena = ArenaRegistry.getArena(player);
			ArenaManager.leaveAttempt(player, arena);
			Debugger.debug("{0} has left the arena {1}! Teleported to end location.", player.getName(), arena.getId());
		}
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