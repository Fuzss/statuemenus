package fuzs.statuemenus.neoforge.impl;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.statuemenus.impl.StatueMenus;
import fuzs.statuemenus.impl.data.client.ModLanguageProvider;
import fuzs.statuemenus.neoforge.impl.data.client.ModSpriteSourceProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(StatueMenus.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class StatueMenusNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(StatueMenus.MOD_ID, StatueMenus::new);
        DataProviderHelper.registerDataProviders(StatueMenus.MOD_ID, ModLanguageProvider::new,
                ModSpriteSourceProvider::new
        );
    }
}
