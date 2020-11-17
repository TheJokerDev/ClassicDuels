package me.despical.classicduels.arena;

/**
 *
 * Contains all game states.
 *
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 11.10.2020
 */
public enum ArenaState {

	WAITING_FOR_PLAYERS("Waiting"), STARTING("Starting"), IN_GAME("Playing"), ENDING("Finishing"), RESTARTING("Restarting"), INACTIVE("Inactive");

	String formattedName;

	ArenaState(String formattedName) {
		this.formattedName = formattedName;
	}

	public String getFormattedName() {
		return formattedName;
	}
}