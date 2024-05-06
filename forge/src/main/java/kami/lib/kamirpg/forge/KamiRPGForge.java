package kami.lib.kamirpg.forge;

import dev.architectury.platform.forge.EventBuses;
import kami.lib.kamirpg.KamiRPG;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(KamiRPG.MOD_ID)
public class KamiRPGForge {
    public KamiRPGForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(KamiRPG.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        KamiRPG.init();
    }
}