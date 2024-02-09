package fuzs.statuemenus.neoforge.impl.data.client;

import fuzs.puzzleslib.neoforge.api.data.v2.client.AbstractSpriteSourceProvider;
import fuzs.puzzleslib.neoforge.api.data.v2.core.ForgeDataProviderContext;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;

import java.util.Optional;

public class ModSpriteSourceProvider extends AbstractSpriteSourceProvider {

    public ModSpriteSourceProvider(ForgeDataProviderContext context) {
        super(context);
    }

    @Override
    public void addSpriteSources() {
        this.atlas(BLOCKS_ATLAS).addSource(new SingleFile(ArmorStandMenu.EMPTY_ARMOR_SLOT_SWORD, Optional.empty()));
    }
}
