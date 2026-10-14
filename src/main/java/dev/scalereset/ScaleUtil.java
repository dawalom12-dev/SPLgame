package dev.scalereset;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

public class ScaleUtil {

    /**
     * Сбрасывает рост игрока до 1.0, удаляя все модификаторы атрибута SCALE.
     */
    public static void resetScale(Player player) {
        AttributeInstance attr = player.getAttribute(Attribute.SCALE);
        if (attr == null) return;

        // Удаляем все модификаторы
        for (AttributeModifier mod : attr.getModifiers()) {
            attr.removeModifier(mod);
        }
        // Устанавливаем базовое значение 1.0
        attr.setBaseValue(1.0);
    }

    /**
     * Возвращает текущий рост игрока.
     */
    public static double getScale(Player player) {
        AttributeInstance attr = player.getAttribute(Attribute.SCALE);
        return attr != null ? attr.getValue() : 1.0;
    }
}
