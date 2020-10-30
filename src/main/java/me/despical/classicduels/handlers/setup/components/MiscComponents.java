package me.despical.classicduels.handlers.setup.components;

import com.github.despical.inventoryframework.GuiItem;
import com.github.despical.inventoryframework.pane.StaticPane;
import me.despical.classicduels.ConfigPreferences;
import me.despical.classicduels.Main;
import me.despical.classicduels.arena.Arena;
import me.despical.classicduels.handlers.setup.SetupInventory;
import me.despical.classicduels.handlers.sign.ArenaSign;
import me.despical.classicduels.utils.conversation.SimpleConversationBuilder;
import me.despical.commonsbox.compat.XMaterial;
import me.despical.commonsbox.configuration.ConfigUtils;
import me.despical.commonsbox.item.ItemBuilder;
import me.despical.commonsbox.serializer.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 12.10.2020
 */
public class MiscComponents implements SetupComponent {

	private SetupInventory setupInventory;

	@Override
	public void prepare(SetupInventory setupInventory) {
		this.setupInventory = setupInventory;
	}

	@Override
	public void injectComponents(StaticPane pane) {
		Player player = setupInventory.getPlayer();
		FileConfiguration config = setupInventory.getConfig();
		Arena arena = setupInventory.getArena();
		Main plugin = setupInventory.getPlugin();
		ItemStack bungeeItem;

		if (!plugin.getConfigPreferences().getOption(ConfigPreferences.Option.BUNGEE_ENABLED)) {
			bungeeItem = new ItemBuilder(XMaterial.OAK_SIGN.parseItem())
				.name("&e&lAdd Game Sign")
				.lore("&7Target a sign and click this.")
				.lore("&8(this will set target sign as game sign)")
				.build();
		} else {
			bungeeItem = new ItemBuilder(Material.BARRIER)
				.name("&c&lAdd Game Sign")
				.lore("&7Option disabled in bungee cord mode.")
				.lore("&8Bungee mode is meant to be one arena per server")
				.lore("&8If you wish to have multi arena, disable bungee in config!")
				.build();
		}

		pane.addItem(new GuiItem(bungeeItem, e -> {
			if (plugin.getConfigPreferences().getOption(ConfigPreferences.Option.BUNGEE_ENABLED)) {
				return;
			}

			e.getWhoClicked().closeInventory();
			Location location = player.getTargetBlock(null, 10).getLocation();

			if (!(location.getBlock().getState() instanceof Sign)) {
				player.sendMessage(plugin.getChatManager().colorMessage("Commands.Look-Sign"));
				return;
			}

			if (location.distance(e.getWhoClicked().getWorld().getSpawnLocation()) <= Bukkit.getServer().getSpawnRadius() && e.getClick() != ClickType.SHIFT_LEFT) {
				e.getWhoClicked().sendMessage(plugin.getChatManager().colorRawMessage("&c&l✖ &cWarning | Server spawn protection is set to &6" + Bukkit.getServer().getSpawnRadius() + " &cand sign you want to place is in radius of this protection! &c&lNon opped players won't be able to interact with this sign and can't join the game so."));
				return;
			}

			plugin.getSignManager().getArenaSigns().add(new ArenaSign((Sign) location.getBlock().getState(), arena));
			player.sendMessage(plugin.getChatManager().colorMessage("Signs.Sign-Created"));

			String signLoc = LocationSerializer.locationToString(location);
			List<String> locs = config.getStringList("instances." + arena.getId() + ".signs");
			locs.add(signLoc);

			config.set("instances." + arena.getId() + ".signs", locs);
			ConfigUtils.saveConfig(plugin, config, "arenas");
		}), 3, 0);

		pane.addItem(new GuiItem(new ItemBuilder(Material.NAME_TAG)
			.name("&e&lSet Map Name")
			.lore("&7Click to set arena map name")
			.lore("", "&a&lCurrently: &e" + config.getString("instances." + arena.getId() + ".mapname"))
			.build(), e -> {
			e.getWhoClicked().closeInventory();

			new SimpleConversationBuilder().withPrompt(new StringPrompt() {

				@Override
				public String getPromptText(ConversationContext context) {
					return plugin.getChatManager().colorRawMessage("&ePlease type in chat arena name! You can use color codes.");
				}

				@Override
				public Prompt acceptInput(ConversationContext context, String input) {
					String name = plugin.getChatManager().colorRawMessage(input);

					player.sendRawMessage(plugin.getChatManager().colorRawMessage("&e✔ Completed | &aName of arena " + arena.getId() + " set to " + name));
					arena.setMapName(name);
					config.set("instances." + arena.getId() + ".mapname", arena.getMapName());
					ConfigUtils.saveConfig(plugin, config, "arenas");

					new SetupInventory(arena, player).openInventory();
					return Prompt.END_OF_CONVERSATION;
				}
			}).buildFor(player);
		}), 4, 0);

		pane.addItem(new GuiItem(new ItemBuilder(XMaterial.FILLED_MAP.parseItem())
			.name("&e&lView Wiki Page")
			.lore("&7Having problems with setup or want to")
			.lore("&7know some useful tips? Click to get wiki link!")
			.build(), e -> {
			e.getWhoClicked().closeInventory();
			player.sendMessage(plugin.getChatManager().colorRawMessage("&7Check out our wiki: https://github.com/Despical/ClassicDuels/wiki"));
		}), 7, 0);
	}
}