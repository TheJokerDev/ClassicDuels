package me.despical.classicduels.kits.base;

import me.despical.classicduels.kits.Kit;
import me.despical.commonsbox.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 21.10.2020
 */
public class BaseKit extends Kit {

	@Override
	public void giveItems(Player player) {
		player.getInventory().setHelmet(new ItemBuilder(Material.IRON_HELMET).name("&bIron Helmet").enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
		player.getInventory().setChestplate(new ItemBuilder(Material.IRON_CHESTPLATE).name("&bIron Chestplate").enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
		player.getInventory().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).name("&bIron Leggings").enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
		player.getInventory().setBoots(new ItemBuilder(Material.IRON_BOOTS).name("&bIron Boots").enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).enchantment(Enchantment.PROTECTION_FIRE).build());
		player.getInventory().setItem(0, new ItemBuilder(Material.IRON_SWORD).build());
		player.getInventory().setItem(1, new ItemBuilder(Material.BOW).build());
		player.getInventory().setItem(2, new ItemBuilder(Material.FISHING_ROD).build());
		player.getInventory().setItem(3, new ItemBuilder(Material.FLINT_AND_STEEL).durability((short) (64 - plugin.getConfig().getInt("FNS-Durability"))).build());
		player.getInventory().setItem(9, new ItemBuilder(Material.ARROW).amount(5).build());
	}
}