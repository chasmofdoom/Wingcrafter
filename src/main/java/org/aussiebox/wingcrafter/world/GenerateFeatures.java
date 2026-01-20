package org.aussiebox.wingcrafter.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.aussiebox.wingcrafter.Wingcrafter;

public class GenerateFeatures {
    public static void generate() {

        RegistryKey<PlacedFeature> key = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Wingcrafter.MOD_ID, "place_dragonflame_cactus"));
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.DESERT, BiomeKeys.BADLANDS),
                GenerationStep.Feature.VEGETAL_DECORATION, key);

        key = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Wingcrafter.MOD_ID, "place_frost_willow_tree"));
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.ICE_SPIKES, BiomeKeys.SNOWY_TAIGA, BiomeKeys.SNOWY_PLAINS, BiomeKeys.SNOWY_SLOPES),
                GenerationStep.Feature.VEGETAL_DECORATION, key);

    }
}
