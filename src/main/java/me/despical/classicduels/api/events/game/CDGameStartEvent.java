package me.despical.classicduels.api.events.game;

import me.despical.classicduels.api.events.ClassicDuelsEvent;
import me.despical.classicduels.arena.Arena;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 *
 * Called when arena has started.
 *
 * @author Despical
 * @since 1.0.0
 * @see Arena
 * <p>
 * Created at 11.10.2020
 */
public class CDGameStartEvent extends ClassicDuelsEvent {

	private final HandlerList HANDLERS = new HandlerList();

	public CDGameStartEvent(Arena arena) {
		super(arena);
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
}