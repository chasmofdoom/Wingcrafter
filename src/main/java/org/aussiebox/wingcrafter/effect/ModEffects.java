package org.aussiebox.wingcrafter.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;

public class ModEffects {
    public static StatusEffect FREEZE;
    public static StatusEffect FLAMEBREATH;
    public static StatusEffect SOUL_DECAY;

    public static void registerStatusEffects() {
        FREEZE =  Registry.register(Registries.STATUS_EFFECT, Identifier.of(Wingcrafter.MOD_ID, "freeze"),
                new FreezeEffect(StatusEffectCategory.HARMFUL, 0xFFA1FFEC).addAttributeModifier(
                        EntityAttributes.MOVEMENT_SPEED,
                        Identifier.of(Wingcrafter.MOD_ID, "freeze"),
                        -100,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ).addAttributeModifier(
                        EntityAttributes.JUMP_STRENGTH,
                        Identifier.of(Wingcrafter.MOD_ID, "freeze"),
                        -100,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ).addAttributeModifier(
                        EntityAttributes.MOVEMENT_EFFICIENCY,
                        Identifier.of(Wingcrafter.MOD_ID, "freeze"),
                        -100,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ).addAttributeModifier(
                        EntityAttributes.WATER_MOVEMENT_EFFICIENCY,
                        Identifier.of(Wingcrafter.MOD_ID, "freeze"),
                        -100,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ));
        FLAMEBREATH =  Registry.register(Registries.STATUS_EFFECT, Identifier.of(Wingcrafter.MOD_ID, "flamebreath"),
                new FlamebreathEffect(StatusEffectCategory.NEUTRAL, 0xFFFC6603).addAttributeModifier(
                        EntityAttributes.MOVEMENT_SPEED,
                        Identifier.of(Wingcrafter.MOD_ID, "flamebreath"),
                        -0.75,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ).addAttributeModifier(
                        EntityAttributes.JUMP_STRENGTH,
                        Identifier.of(Wingcrafter.MOD_ID, "flamebreath"),
                        -100,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ));
        SOUL_DECAY =  Registry.register(Registries.STATUS_EFFECT, Identifier.of(Wingcrafter.MOD_ID, "soul_decay"),
                new SoulDecayEffect(StatusEffectCategory.HARMFUL, 0xFFF0FFFC));
    }
}
