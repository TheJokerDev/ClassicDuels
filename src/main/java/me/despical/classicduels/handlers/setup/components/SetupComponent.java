package me.despical.classicduels.handlers.setup.components;

import me.despical.classicduels.handlers.setup.SetupInventory;
import me.despical.inventoryframework.pane.StaticPane;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 11.10.2020
 */
public interface SetupComponent {

	void prepare(SetupInventory setupInventory);

	void injectComponents(StaticPane pane);
}