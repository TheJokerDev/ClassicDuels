package me.despical.classicduels.commands.game.layout.menu.component;

import me.despical.classicduels.commands.game.layout.menu.LayoutMenu;
import me.despical.inventoryframework.pane.StaticPane;

/**
 * @author Despical
 * @since 1.0.2
 * <p>
 * Created at 15.11.2020
 */
public interface LayoutComponent {

	default void prepare(LayoutMenu layoutMenu) {

	}

	void injectComponents(StaticPane pane);
}