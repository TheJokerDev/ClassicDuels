package me.despical.classicduels.api.events.game;

import me.despical.classicduels.api.events.ClassicDuelsEvent;
import me.despical.classicduels.arena.Arena;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 *
 * Called when arena is stopped.
 *
 * @author Despical
 * @since 1.0.0
 * @see Arena
 * <p>
 * Created at 11.10.2020
 */
public class CDGameStopEvent extends ClassicDuelsEvent {

	private final HandlerList HANDLERS = new HandlerList();

	public CDGameStopEvent(Arena arena) {
		super(arena);
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
}