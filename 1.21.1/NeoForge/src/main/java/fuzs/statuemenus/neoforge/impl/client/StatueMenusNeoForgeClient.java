package fuzs.statuemenus.neoforge.impl.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.statuemenus.impl.StatueMenus;
import fuzs.statuemenus.impl.client.StatueMenusClient;
import fuzs.statuemenus.impl.data.client.ModLanguageProvider;
import fuzs.statuemenus.neoforge.impl.data.client.ModSpriteSourceProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = StatueMenus.MOD_ID, dist = Dist.CLIENT)
public class StatueMenusNeoForgeClient {

    public StatueMenusNeoForgeClient() {
        ClientModConstructor.construct(StatueMenus.MOD_ID, StatueMenusClient::new);
        DataProviderHelper.registerDataProviders(StatueMenus.MOD_ID,
                ModLanguageProvider::new,
                ModSpriteSourceProvider::new
        );
    }
}
