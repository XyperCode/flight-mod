package io.github.xypercode.mods.flight.fabric;

import io.github.xypercode.mods.flight.FlightMod;
import net.fabricmc.api.ModInitializer;

public class FlightModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FlightMod.init();
        FlightMod.postInit();
    }
}