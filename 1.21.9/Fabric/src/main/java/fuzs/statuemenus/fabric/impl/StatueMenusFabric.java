package fuzs.statuemenus.fabric.impl;

import fuzs.statuemenus.impl.StatueMenus;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class StatueMenusFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(StatueMenus.MOD_ID, StatueMenus::new);
    }
}
