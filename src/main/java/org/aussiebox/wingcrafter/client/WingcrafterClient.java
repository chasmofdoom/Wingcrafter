package org.aussiebox.wingcrafter.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.aussiebox.wingcrafter.client.screen.ScrollScreen;
import org.aussiebox.wingcrafter.config.ClientConfig;
import org.aussiebox.wingcrafter.datagen.AdvancementProvider;
import org.aussiebox.wingcrafter.datagen.ModelProvider;
import org.aussiebox.wingcrafter.init.ScreenHandlerTypeInit;

public class WingcrafterClient implements ClientModInitializer, DataGeneratorEntrypoint {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ScreenHandlerTypeInit.SCROLL, ScrollScreen::new);
        ClientConfig.HANDLER.load();

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            ClientConfig.HANDLER.save();
        });
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModelProvider::new);
        pack.addProvider(AdvancementProvider::new);
    }
}
