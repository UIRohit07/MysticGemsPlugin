
package com.mysticgems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MysticGems extends JavaPlugin implements Listener {
    private final Map<String, ItemStack> gemItems = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        createGems();
        getLogger().info("MysticGems enabled!");
    }

    private void createGems() {
        gemItems.put("speed", createGem("Gem of Speed", PotionEffectType.SPEED));
        gemItems.put("strength", createGem("Gem of Strength", PotionEffectType.INCREASE_DAMAGE));
        gemItems.put("invisibility", createGem("Gem of Shadow", PotionEffectType.INVISIBILITY));
        gemItems.put("healing", createGem("Gem of Healing", PotionEffectType.REGENERATION));
        gemItems.put("flame", createFireballGem());
    }

    private ItemStack createGem(String name, PotionEffectType effectType) {
        ItemStack gem = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta meta = gem.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Collections.singletonList("Mystic power: " + effectType.getName()));
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        gem.setItemMeta(meta);
        return gem;
    }

    private ItemStack createFireballGem() {
        ItemStack gem = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta meta = gem.getItemMeta();
        meta.setDisplayName("Gem of Flame");
        meta.setLore(Collections.singletonList("Right-click to shoot fireball!"));
        meta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
        gem.setItemMeta(meta);
        return gem;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        String name = item.getItemMeta().getDisplayName();
        switch (name) {
            case "Gem of Speed":
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                break;
            case "Gem of Strength":
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 1));
                break;
            case "Gem of Shadow":
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 1));
                break;
            case "Gem of Healing":
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                break;
            case "Gem of Flame":
                if (!player.hasCooldown(item.getType())) {
                    player.launchProjectile(Fireball.class);
                    player.setCooldown(item.getType(), 100);
                }
                break;
        }
    }
}
