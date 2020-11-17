/*
 * Classic Duels - Eliminate your opponent to win!
 * Copyright (C) 2020 Despical and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.despical.classicduels.commands.game.layout.menu;

import me.despical.classicduels.Main;
import me.despical.classicduels.commands.game.layout.menu.component.BaseComponent;
import me.despical.commonsbox.number.NumberUtils;
import me.despical.inventoryframework.Gui;
import me.despical.inventoryframework.pane.StaticPane;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Despical
 * @since 1.0.2
 * <p>
 * Created at 15.11.2020
 */
public class LayoutMenu {

	private final Main plugin = JavaPlugin.getPlugin(Main.class);
	private final Player player;
	private Gui gui;

	public LayoutMenu(Player player) {
		this.player = player;

		prepareGui();
	}

	private void prepareGui() {
		this.gui = new Gui(plugin, 6, plugin.getChatManager().colorRawMessage("&8Layout Editor - Classic Duels"));
		this.gui.setOnOutsideClick(e -> e.setCancelled(true));
		this.gui.setOnBottomClick(e -> e.setCancelled(true));
		this.gui.setOnGlobalClick(e -> e.setCancelled(NumberUtils.isBetween(e.getRawSlot(), 45, 53)));
		this.gui.setOnDrag(e -> e.setCancelled(NumberUtils.isBetween(e.getRawSlots().toArray(new Integer[0])[0], 47, 53) || Gui.getInventory(e.getView(), e.getRawSlots().toArray(new Integer[0])[0]).equals(e.getView().getBottomInventory())));
		StaticPane pane = new StaticPane(9, 6);
		this.gui.addPane(pane);

		prepareComponents(pane);
	}

	private void prepareComponents(StaticPane pane) {
		BaseComponent baseComponent = new BaseComponent();
		baseComponent.prepare(this);
		baseComponent.injectComponents(pane);
	}

	public void openGui() {
		gui.show(player);
	}

	public Player getPlayer() {
		return player;
	}

	public Main getPlugin() {
		return plugin;
	}

	public Gui getGui() {
		return gui;
	}
}