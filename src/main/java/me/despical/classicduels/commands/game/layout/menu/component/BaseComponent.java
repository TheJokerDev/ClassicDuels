/*
 * Classic Duels - Eliminate your opponent to win!
 * Copyright (C) 2021 Despical and contributors
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

package me.despical.classicduels.commands.game.layout.menu.component;

import me.despical.classicduels.Main;
import me.despical.classicduels.commands.game.layout.menu.LayoutMenu;
import me.despical.classicduels.utils.LayoutHelper;
import me.despical.commonsbox.compat.XMaterial;
import me.despical.commonsbox.item.ItemBuilder;
import me.despical.inventoryframework.GuiItem;
import me.despical.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Despical
 * @since 1.0.2
 * <p>
 * Created at 15.11.2020
 */
public class BaseComponent implements LayoutComponent {

	private LayoutMenu layoutMenu;

	@Override
	public void prepare(LayoutMenu layoutMenu) {
		this.layoutMenu = layoutMenu;
	}

	@Override
	public void injectComponents(StaticPane pane) {
		Main plugin = layoutMenu.getPlugin();
		Player player = layoutMenu.getPlayer();

		pane.fillHorizontallyWith(new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).name("&8⬆ &7Inventory").lore("&8⬇ &7Hotbar").build(), 3, e -> e.setCancelled(true));
		LayoutHelper.fillWithCurrentOrDefault(plugin, player, pane);

		pane.addItem(new GuiItem(new ItemBuilder(Material.ARROW).name("&cClose").build(), e -> {
			e.setCancelled(true);
			player.closeInventory();
		}),3, 5);

		pane.addItem(new GuiItem(new ItemBuilder(Material.CHEST)
			.name("&aSave Layout")
			.lore("&7Save your inventory layout for", "&aClassic Duels", "", "&eClick to save!")
			.build(), e -> {

			e.setCancelled(true);
			player.closeInventory();
			LayoutHelper.saveLayoutToFile(plugin, player, layoutMenu.getGui());
			player.sendMessage(plugin.getChatManager().colorRawMessage("&aYou successfully saved a new inventory layout!"));
		}),4, 5);

		pane.addItem(new GuiItem(new ItemBuilder(Material.BARRIER)
			.name("&cReset Layout")
			.lore("&7Reset your inventory layout for", "&aClassic Duels", "", "&eClick to reset!")
			.build(), e -> {

			player.closeInventory();
			LayoutHelper.resetLayout(plugin, player);
			player.sendMessage(plugin.getChatManager().colorRawMessage("&cSuccessfully reset your inventory layout!"));
		}),5, 5);
	}
}