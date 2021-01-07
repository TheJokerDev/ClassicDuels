package me.despical.classicduels.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Despical
 * <p>
 * Created at 7.01.2021
 */
public class Utils {

	public static String getCardinalDirection(Player player) {
		Location origin = player.getLocation().clone();
		Vector target = player.getCompassTarget().toVector();
		origin.setDirection(target.subtract(origin.toVector()));
		double rotation = (player.getLocation().getYaw() - origin.getYaw()) % 360.0F;

		if (rotation < 0.0D) {
			rotation += 360.0D;
		}

		if ((0.0D <= rotation) && (rotation < 22.5D)) {
			return "⬆";
		}

		if ((22.5D <= rotation) && (rotation < 67.5D)) {
			return "⬉";
		}

		if ((67.5D <= rotation) && (rotation < 112.5D)) {
			return "⬅";
		}

		if ((112.5D <= rotation) && (rotation < 157.5D)) {
			return "⬋";
		}

		if ((157.5D <= rotation) && (rotation < 202.5D)) {
			return "⬇";
		}

		if ((202.5D <= rotation) && (rotation < 247.5D)) {
			return "⬊";
		}

		if ((247.5D <= rotation) && (rotation < 292.5D)) {
			return "➡";
		}

		if ((292.5D <= rotation) && (rotation < 337.5D)) {
			return "⬈";
		}

		if ((337.5D <= rotation) && (rotation < 360.0D)) {
			return "⬆";
		}

		return "null";
	}
}