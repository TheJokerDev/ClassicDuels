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