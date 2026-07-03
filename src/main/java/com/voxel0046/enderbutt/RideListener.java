package com.voxel0046.enderbutt;

import org.bukkit.*;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerTeleportEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RideListener implements Listener {

    private final EnderButtPlugin plugin;
    private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> pearlByPlayer = new ConcurrentHashMap<>();

    public RideListener(EnderButtPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPearlLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof EnderPearl pearl)) return;
        if (!(pearl.getShooter() instanceof Player player)) return;
        if (!player.hasPermission("enderbutt.use")) return;

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();
        boolean isCustomPearl =
                ItemFactory.isEnderButtItem(mainHand, plugin.getItemKey()) ||
                ItemFactory.isEnderButtItem(offHand, plugin.getItemKey());

        if (!isCustomPearl) return;

        long cooldownSeconds = plugin.getConfig().getLong("cooldown-seconds", 1L);
        long cooldownMs = cooldownSeconds * 1000L;

        long now = System.currentTimeMillis();
        long last = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        if (now - last < cooldownMs) {
            event.setCancelled(true);
            return;
        }
        cooldowns.put(player.getUniqueId(), now);

        boolean mounted = pearl.addPassenger(player);
        if (mounted) pearlByPlayer.put(player.getUniqueId(), pearl.getUniqueId());

        playLaunchEffects(player, pearl.getLocation());
    }

    @EventHandler
    public void onPearlHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof EnderPearl pearl)) return;
        if (!(pearl.getShooter() instanceof Player player)) return;

        UUID tracked = pearlByPlayer.get(player.getUniqueId());
        if (tracked != null && tracked.equals(pearl.getUniqueId())) {
            pearlByPlayer.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onTeleportDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!plugin.getConfig().getBoolean("damage.disable-teleport-damage", true)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.ENDER_PEARL) {
            event.setCancelled(true);
        }

        if ((event.getCause() == EntityDamageEvent.DamageCause.FALL
                || event.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL)
                && pearlByPlayer.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPearlProjectileDamage(EntityDamageByEntityEvent event) {
        if (!plugin.getConfig().getBoolean("damage.disable-pearl-projectile-damage", true)) return;
        if (event.getDamager() instanceof EnderPearl) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (!plugin.getConfig().getBoolean("damage.disable-teleport-damage", true)) return;
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            event.getPlayer().setFallDistance(0f);
        }
    }

    private void playLaunchEffects(Player player, Location location) {
        if (plugin.getConfig().getBoolean("effects.launch-particle.enabled", true)) {
            String particleName = plugin.getConfig().getString("effects.launch-particle.type", "PORTAL");
            int count = plugin.getConfig().getInt("effects.launch-particle.count", 35);

            Particle particle = parseParticle(particleName, Particle.PORTAL);
            location.getWorld().spawnParticle(particle, location, count, 0.25, 0.25, 0.25, 0.02);
        }

        if (plugin.getConfig().getBoolean("effects.launch-sound.enabled", true)) {
            String soundName = plugin.getConfig().getString("effects.launch-sound.type", "ENTITY_ENDER_PEARL_THROW");
            float volume = (float) plugin.getConfig().getDouble("effects.launch-sound.volume", 1.0);
            float pitch = (float) plugin.getConfig().getDouble("effects.launch-sound.pitch", 1.0);

            Sound sound = parseSound(soundName, Sound.ENTITY_ENDER_PEARL_THROW);
            player.getWorld().playSound(location, sound, volume, pitch);
        }
    }

    private Particle parseParticle(String name, Particle fallback) {
        try { return Particle.valueOf(name.toUpperCase()); }
        catch (Exception ignored) { return fallback; }
    }

    private Sound parseSound(String name, Sound fallback) {
        try { return Sound.valueOf(name.toUpperCase()); }
        catch (Exception ignored) { return fallback; }
    }
}
