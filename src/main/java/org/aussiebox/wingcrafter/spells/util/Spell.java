package org.aussiebox.wingcrafter.spells.util;

import lombok.Getter;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.cca.SoulComponent;
import org.aussiebox.wingcrafter.cca.SpellDataComponent;

public class Spell {
    @Getter private final String spellID;
    @Getter private final int color;
    @Getter private final Identifier buttonTexture;
    @Getter private final int soulPenalty;
    @Getter private final int castDuration;
    @Getter private final int castCooldown;

    public Spell(String spellID, int color, Identifier buttonTexture, int soulPenalty, int castDuration, int castCooldown) {
        this.spellID = spellID;
        this.color = color;
        this.buttonTexture = buttonTexture;
        this.soulPenalty = soulPenalty;
        this.castDuration = castDuration;
        this.castCooldown = castCooldown;
    }

    public void cast(ServerPlayerEntity source) {
        SpellDataComponent data = SpellDataComponent.KEY.get(source);
        if (data.getSpellCooldown(spellID) > 0) return;

        data.setSpellCooldown(spellID, this.castCooldown);
        data.setSpellDuration(spellID, this.castDuration);
    }

    public void castTick(ServerPlayerEntity source) {

    }

    public void afflictSoulPenalty(ServerPlayerEntity target) {
        SoulComponent.KEY.get(target).changeSoul(-Math.abs(this.soulPenalty));
    }
}
