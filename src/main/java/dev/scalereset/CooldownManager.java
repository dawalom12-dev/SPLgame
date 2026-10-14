package dev.scalereset;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final ScaleResetPlugin plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public CooldownManager(ScaleResetPlugin plugin) {
        this.plugin = plugin;
    }

    /** Ставит кулдаун на игрока. */
    public void setCooldown(Player player) {
        int seconds = plugin.getConfig().getInt("settings.cooldown-seconds", 120);
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + (seconds * 1000L));
    }

    /** Проверяет, активен ли кулдаун. */
    public boolean isOnCooldown(Player player) {
        Long end = cooldowns.get(player.getUniqueId());
        if (end == null) return false;
        if (System.currentTimeMillis() >= end) {
            cooldowns.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    /** Возвращает оставшиеся секунды кулдауна. */
    public long getRemainingSeconds(Player player) {
        Long end = cooldowns.get(player.getUniqueId());
        if (end == null) return 0;
        long remaining = (end - System.currentTimeMillis()) / 1000;
        return Math.max(0, remaining);
    }

    /** Снимает кулдаун досрочно. */
    public void clearCooldown(Player player) {
        cooldowns.remove(player.getUniqueId());
    }
}
