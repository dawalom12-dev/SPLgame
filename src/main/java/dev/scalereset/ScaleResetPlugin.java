package dev.scalereset;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ScaleResetPlugin extends JavaPlugin {

    private static ScaleResetPlugin instance;
    public static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        cooldownManager = new CooldownManager(this);

        // Регистрируем слушателя PvP
        Bukkit.getPluginManager().registerEvents(new CombatListener(this), this);

        // Админ-команда
        var cmd = getCommand("scalereset");
        if (cmd != null) cmd.setExecutor(new AdminCommand(this));

        getLogger().info("ScaleReset включен. Кулдаун: " + getConfig().getInt("settings.cooldown-seconds") + " сек.");
    }

    public static ScaleResetPlugin getInstance() { return instance; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
    public static Component comp(String s) {
        return LEGACY.deserialize(s).decoration(net.kyori.adventure.text.format.TextDecoration.ITALIC, false);
    }
}
