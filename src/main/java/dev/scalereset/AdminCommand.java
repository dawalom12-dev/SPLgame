package dev.scalereset;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {

    private final ScaleResetPlugin plugin;
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    public AdminCommand(ScaleResetPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("scalereset.admin")) {
            sender.sendMessage(LEGACY.deserialize("&cНет прав."));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(LEGACY.deserialize("&e/scalereset clearcooldown <ник> &7— снять кулдаун"));
            sender.sendMessage(LEGACY.deserialize("&e/scalereset reset <ник> &7— сбросить рост"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "clearcooldown" -> {
                if (args.length < 2) {
                    sender.sendMessage(LEGACY.deserialize("&eУкажи ник."));
                    return true;
                }
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    sender.sendMessage(LEGACY.deserialize("&cИгрок не найден."));
                    return true;
                }
                plugin.getCooldownManager().clearCooldown(target);
                sender.sendMessage(LEGACY.deserialize("&aКулдаун снят с " + target.getName()));
            }
            case "reset" -> {
                if (args.length < 2) {
                    sender.sendMessage(LEGACY.deserialize("&eУкажи ник."));
                    return true;
                }
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    sender.sendMessage(LEGACY.deserialize("&cИгрок не найден."));
                    return true;
                }
                ScaleUtil.resetScale(target);
                sender.sendMessage(LEGACY.deserialize("&aРост сброшен у " + target.getName()));
            }
            default -> sender.sendMessage(LEGACY.deserialize("&cНеизвестная подкоманда."));
        }
        return true;
    }
}
