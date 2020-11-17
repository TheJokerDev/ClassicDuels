package me.despical.classicduels.events.spectator.components;

import me.despical.classicduels.events.spectator.SpectatorSettingsMenu;
import me.despical.inventoryframework.pane.StaticPane;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 27.10.2020
 */
public interface SpectatorSettingComponent {

	void prepare(SpectatorSettingsMenu spectatorSettingsMenu);

	void injectComponents(StaticPane pane);
}