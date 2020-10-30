package me.despical.classicduels.api.events.game;

import me.despical.classicduels.api.events.ClassicDuelsEvent;
import me.despical.classicduels.arena.Arena;
import me.despical.classicduels.arena.ArenaState;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 *
 * Called when arena game state has changed.
 *
 * @author Despical
 * @since 1.0.0
 * @see Arena
 * <p>
 * Created at 11.10.2020
 */
public class CDGameStateChangeEvent extends ClassicDuelsEvent {

	private final HandlerList HANDLERS = new HandlerList();
	private final ArenaState arenaState;

	public CDGameStateChangeEvent(Arena eventArena, ArenaState arenaState) {
		super(eventArena);
		this.arenaState = arenaState;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public ArenaState getArenaState() {
		return arenaState;
	}
}