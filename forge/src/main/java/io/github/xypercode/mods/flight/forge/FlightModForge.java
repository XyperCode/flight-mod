package io.github.xypercode.mods.flight.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.xypercode.mods.flight.FlightMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FlightMod.MOD_ID)
public class FlightModForge {
    public FlightModForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(FlightMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        FlightMod.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
    }

    public void loadComplete(FMLLoadCompleteEvent event) {
        FlightMod.postInit();
    }
}