package me.despical.classicduels.handlers.setup.components;

import com.github.despical.inventoryframework.pane.StaticPane;
import me.despical.classicduels.handlers.setup.SetupInventory;

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