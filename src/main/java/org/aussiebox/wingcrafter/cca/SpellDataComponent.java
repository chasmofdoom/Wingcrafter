package org.aussiebox.wingcrafter.cca;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.spells.util.SpellRegistry;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.Objects;

public class SpellDataComponent implements AutoSyncedComponent, ServerTickingComponent {
    public static final ComponentKey<SpellDataComponent> KEY =
            ComponentRegistry.getOrCreate(Wingcrafter.id("spell_data"), SpellDataComponent.class);
    private final PlayerEntity player;

    private final Object2IntMap<String> spellCooldowns = new Object2IntOpenHashMap<>();

    private final Object2IntMap<String> spellDurations = new Object2IntOpenHashMap<>();

    public SpellDataComponent(PlayerEntity player) {
        this.player = player;
    }

    public int getSpellCooldown(String spellID) {
        return spellCooldowns.getOrDefault(spellID, 0);
    }

    public void setSpellCooldown(String spellID, int cooldown) {
        if (cooldown <= 0) spellCooldowns.remove(spellID, cooldown);
        else spellCooldowns.put(spellID, cooldown);
        this.sync();
    }

    public void changeSpellCooldown(String spellID, int cooldown) {
        int previousCooldown = spellCooldowns.getOrDefault(spellID, 0);
        if (previousCooldown + cooldown <= 0) spellCooldowns.remove(spellID, previousCooldown);
        else spellCooldowns.put(spellID, previousCooldown + cooldown);
        this.sync();
    }

    public int getSpellDuration(String spellID) {
        return spellDurations.getOrDefault(spellID, 0);
    }

    public void setSpellDuration(String spellID, int duration) {
        if (duration <= 0) spellDurations.remove(spellID, duration);
        else spellDurations.put(spellID, duration);
        this.sync();
    }

    public void changeSpellDuration(String spellID, int duration) {
        int previousDuration = spellDurations.getOrDefault(spellID, 0);
        if (previousDuration + duration <= 0) spellDurations.remove(spellID, previousDuration);
        else spellDurations.put(spellID, previousDuration + duration);
        this.sync();
    }

    @Override
    public void serverTick() {
        for (String spell : spellCooldowns.keySet()) {
            int cooldown = spellCooldowns.getInt(spell);
            if (cooldown-1 <= 0) {
                spellCooldowns.remove(spell, cooldown);
                continue;
            }
            spellCooldowns.put(spell, cooldown-1);
        }
        for (String spell : spellDurations.keySet()) {
            int duration = spellDurations.getInt(spell);
            if (duration-1 <= 0) {
                spellDurations.remove(spell, duration);
                continue;
            }
            if (player instanceof ServerPlayerEntity serverPlayer) {
                try {
                    Objects.requireNonNull(SpellRegistry.getSpell(spell)).castTick(serverPlayer);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            spellDurations.put(spell, duration-1);
        }
    }

    public void sync() {
        KEY.sync(this.player);
    }

    @Override
    public void readData(ReadView tag) {
        tag.read("spellCooldowns", Codec.unboundedMap(Codec.STRING, Codec.INT)).ifPresent(map -> {
            spellCooldowns.clear();
            spellCooldowns.putAll(map);
        });
        tag.read("spellDurations", Codec.unboundedMap(Codec.STRING, Codec.INT)).ifPresent(map -> {
            spellDurations.clear();
            spellDurations.putAll(map);
        });
    }

    @Override
    public void writeData(WriteView tag) {
        tag.put("spellCooldowns", Codec.unboundedMap(Codec.STRING, Codec.INT), spellCooldowns);
        tag.put("spellDurations", Codec.unboundedMap(Codec.STRING, Codec.INT), spellDurations);
    }
}
