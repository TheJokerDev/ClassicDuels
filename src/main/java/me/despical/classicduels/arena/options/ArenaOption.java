package me.despical.classicduels.arena.options;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 11.10.2020
 */
public enum ArenaOption {

	/**
	 * Current arena timer, ex. 30 seconds before game starts.
	 */
	TIMER(0);

	private final int defaultValue;

	ArenaOption(int defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getDefaultValue() {
		return defaultValue;
	}
}