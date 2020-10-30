package me.despical.classicduels.api.events;

import me.despical.classicduels.arena.Arena;
import org.bukkit.event.Event;

/**
 *
 * Represents Classic Duels game related events.
 *
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 11.10.2020
 */
public abstract class ClassicDuelsEvent extends Event {

	protected Arena arena;

	public ClassicDuelsEvent(Arena eventArena) {
		arena = eventArena;
	}

	/**
	 * Returns event arena
	 *
	 * @return event arena
	 */
	public Arena getArena() {
		return arena;
	}
}