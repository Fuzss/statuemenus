package fuzs.statuemenus.neoforge.impl.client;

import fuzs.statuemenus.impl.StatueMenus;
import fuzs.statuemenus.impl.client.StatueMenusClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = StatueMenus.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class StatueMenusNeoForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientModConstructor.construct(StatueMenus.MOD_ID, StatueMenusClient::new);
    }
}
