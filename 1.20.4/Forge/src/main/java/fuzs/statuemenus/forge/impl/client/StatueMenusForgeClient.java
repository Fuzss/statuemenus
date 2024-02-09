package fuzs.statuemenus.forge.impl.client;

import fuzs.statuemenus.impl.StatueMenus;
import fuzs.statuemenus.impl.client.StatueMenusClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = StatueMenus.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class StatueMenusForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientModConstructor.construct(StatueMenus.MOD_ID, StatueMenusClient::new);
    }
}
