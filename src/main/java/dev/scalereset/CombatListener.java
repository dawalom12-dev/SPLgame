package dev.scalereset;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatListener implements Listener {

    private final ScaleResetPlugin plugin;
    // Время последнего отправленного сообщения игроку
    private final Map<UUID, Long> lastMessage = new HashMap<>();

    // Погрешность для сравнения с 1.0
    private static final double SCALE_TOLERANCE = 0.01;
    // Минимум между сообщениями (5 секунд)
    private static final long MESSAGE_COOLDOWN_MS = 5000L;

    public CombatListener(ScaleResetPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPvP(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        handlePlayerInCombat(attacker);
        handlePlayerInCombat(victim);
    }

    private void handlePlayerInCombat(Player player) {
        // Всегда ставим кулдаун при ударе (чтобы нельзя было менять рост в бою)
        plugin.getCooldownManager().setCooldown(player);

        double scale = ScaleUtil.getScale(player);

        // Если рост уже 1.0 — ничего не делаем, не спамим
        if (Math.abs(scale - 1.0) <= SCALE_TOLERANCE) {
            return;
        }

        // Сбрасываем рост
        ScaleUtil.resetScale(player);

        // Проверяем кулдаун на сообщение
        long now = System.currentTimeMillis();
        Long last = lastMessage.get(player.getUniqueId());
        if (last != null && now - last < MESSAGE_COOLDOWN_MS) {
            return; // Уже недавно отправляли — молчим
        }

        lastMessage.put(player.getUniqueId(), now);
        player.sendMessage(ScaleResetPlugin.comp(
                plugin.getConfig().getString("settings.reset-message", "&c&lВАШ РОСТ СБРОШЕН &cиз-за PvP! Кулдаун: 2 минуты.")
        ));
    }
}
