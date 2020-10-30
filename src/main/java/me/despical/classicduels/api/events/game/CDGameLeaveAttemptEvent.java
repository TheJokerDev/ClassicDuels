package me.despical.classicduels.api.events.game;

import me.despical.classicduels.api.events.ClassicDuelsEvent;
import me.despical.classicduels.arena.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 *
 * Called when player is attempting to leave arena.
 *
 * @author Despical
 * @since 1.0.0
 * @see Arena
 * <p>
 * Created at 11.10.2020
 */
public class CDGameLeaveAttemptEvent extends ClassicDuelsEvent {

	private final HandlerList HANDLERS = new HandlerList();
	private final Player player;

	public CDGameLeaveAttemptEvent(Player player, Arena targetArena) {
		super(targetArena);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
}