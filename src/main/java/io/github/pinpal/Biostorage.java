package io.github.pinpal;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("biostorage")
public class Biostorage {

    public Biostorage() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register mod setup methods
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);

        // Register the mod event bus to the MinecraftForge event bus
        MinecraftForge.EVENT_BUS.register(this);

        // Register RecipeRemover to listen for events
        MinecraftForge.EVENT_BUS.register(RecipeRemover.class);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Your setup code here
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // Your client-side setup code here
    }
}
