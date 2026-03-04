package org.aussiebox.wingcrafter.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.aussiebox.wingcrafter.Wingcrafter;

public class ModEntities {

    public static final EntityType<DragonflameCactusEntity> DragonflameCactusEntityType = Registry.register(Registries.ENTITY_TYPE,
            Wingcrafter.id("dragonflame_cactus"),
            EntityType.Builder.<DragonflameCactusEntity>create(DragonflameCactusEntity::new, SpawnGroup.MISC)
                    .dimensions(0.4F, 0.4F).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Wingcrafter.id("dragonflame_cactus")))
    );
    public static final EntityType<MoonGlobeEntity> MoonGlobeEntityType = Registry.register(Registries.ENTITY_TYPE,
            Wingcrafter.id("moon_globe"),
            EntityType.Builder.create(MoonGlobeEntity::new, SpawnGroup.MISC)
                    .dimensions(0.65F, 0.65F).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Wingcrafter.id("moon_globe")))
    );

    public static void registerModEntities() {
        Wingcrafter.LOGGER.info("Registering Mod Entities for " + Wingcrafter.MOD_ID);
    }
}
