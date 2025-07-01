package fuzs.statuemenus.neoforge.impl;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.statuemenus.impl.StatueMenus;
import net.neoforged.fml.common.Mod;

@Mod(StatueMenus.MOD_ID)
public class StatueMenusNeoForge {

    public StatueMenusNeoForge() {
        ModConstructor.construct(StatueMenus.MOD_ID, StatueMenus::new);
    }
}
