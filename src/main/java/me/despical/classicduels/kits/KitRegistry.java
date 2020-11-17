package me.despical.classicduels.kits;

import me.despical.classicduels.kits.base.BaseKit;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 21.10.2020
 */
public class KitRegistry {

	private static Kit baseKit;

	private KitRegistry() {}

	public static void registerBaseKit() {
		baseKit = new BaseKit();
		baseKit.registerItems();
	}

	public static Kit getBaseKit() {
		return baseKit;
	}
}