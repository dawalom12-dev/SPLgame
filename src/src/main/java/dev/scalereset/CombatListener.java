package dev.scalereset;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatListener implements Listener {

    private final ScaleResetPlugin plugin;

    public CombatListener(ScaleResetPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPvP(EntityDamageByEntityEvent event) {
        // Проверяем, что и атакующий, и жертва — игроки
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        // Сбрасываем рост атакующему
        ScaleUtil.resetScale(attacker);
        // Ставим кулдаун атакующему
        plugin.getCooldownManager().setCooldown(attacker);
        // Отправляем сообщение
        attacker.sendMessage(ScaleResetPlugin.comp(
                plugin.getConfig().getString("settings.reset-message", "&cРост сброшен из-за PvP!")
        ));

        // Опционально: можно также сбросить рост жертве
        // ScaleUtil.resetScale(victim);
        // plugin.getCooldownManager().setCooldown(victim);
    }
}
