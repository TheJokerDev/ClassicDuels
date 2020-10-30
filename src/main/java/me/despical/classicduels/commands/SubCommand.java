package me.despical.classicduels.commands;

import me.despical.classicduels.Main;
import me.despical.classicduels.arena.ArenaRegistry;
import me.despical.classicduels.commands.exception.CommandException;
import me.despical.classicduels.handlers.ChatManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 11.10.2020
 */
public abstract class SubCommand {

	protected final Main plugin = JavaPlugin.getPlugin(Main.class);

	private final String name;
	private String permission;
	private final String[] aliases;

	public SubCommand(String name) {
		this(name, new String[0]);
	}

	public SubCommand(String name, String... aliases) {
		this.name = name;
		this.aliases = aliases;
	}

	public String getName() {
		return name;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public final boolean hasPermission(CommandSender sender) {
		if (permission == null) return true;
		return sender.hasPermission(permission);
	}

	public final boolean checkIsInGameInstance(Player player) {
		if (!ArenaRegistry.isInArena(player)) {
			player.sendMessage(plugin.getChatManager().colorMessage("Commands.Not-Playing", player));
			return false;
		}

		return true;
	}

	public abstract String getPossibleArguments();

	public abstract int getMinimumArguments();

	public abstract void execute(CommandSender sender, ChatManager chatManager, String[] args) throws CommandException;

	public abstract List<String> getTutorial();

	public abstract CommandType getType();

	public abstract SenderType getSenderType();

	public enum CommandType {
		GENERIC, HIDDEN
	}

	public enum SenderType {
		PLAYER, BOTH
	}

	public final boolean isValidTrigger(String name) {
		if (this.name.equalsIgnoreCase(name)) {
			return true;
		}

		if (aliases != null) {
			for (String alias : aliases) {
				if (alias.equalsIgnoreCase(name)) {
					return true;
				}
			}
		}
		return false;
	}
}