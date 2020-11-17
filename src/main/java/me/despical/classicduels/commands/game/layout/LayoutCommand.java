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