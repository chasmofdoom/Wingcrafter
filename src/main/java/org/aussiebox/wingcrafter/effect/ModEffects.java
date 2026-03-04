package org.aussiebox.wingcrafter.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;

public class ModEffects {
    public static StatusEffect FREEZE;
    public static StatusEffect SOUL_DECAY;

    public static void registerStatusEffects() {
        SOUL_DECAY =  Registry.register(Registries.STATUS_EFFECT, Identifier.of(Wingcrafter.MOD_ID, "soul_decay"),
                new SoulDecayEffect(StatusEffectCategory.HARMFUL, 0xFFF0FFFC));
    }
}
