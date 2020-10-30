package me.despical.classicduels.handlers;

import me.despical.classicduels.Main;
import me.despical.classicduels.utils.Debugger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 13.10.2020
 */
public class PermissionManager {

	private static final Main plugin = JavaPlugin.getPlugin(Main.class);
	private static String joinPerm = "cd.join.<arena>";

	public static void init() {
		setupPermissions();
	}

	public static String getJoinPerm() {
		return joinPerm;
	}

	private static void setJoinPerm(String joinPerm) {
		PermissionManager.joinPerm = joinPerm;
	}

	private static void setupPermissions() {
		PermissionManager.setJoinPerm(plugin.getConfig().getString("Basic-Permissions.Join-Permission", "tntrun.join.<arena>"));

		Debugger.debug("Basic permissions registered");
	}
}