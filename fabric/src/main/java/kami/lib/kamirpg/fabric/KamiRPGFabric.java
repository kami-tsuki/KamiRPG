package kami.lib.kamirpg.fabric;

import kami.lib.kamirpg.KamiRPG;
import net.fabricmc.api.ModInitializer;

public class KamiRPGFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        KamiRPG.init();
    }
}