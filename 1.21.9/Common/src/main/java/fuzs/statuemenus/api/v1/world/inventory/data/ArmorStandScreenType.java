package fuzs.statuemenus.api.v1.world.inventory.data;

import fuzs.statuemenus.impl.StatueMenus;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record ArmorStandScreenType(ResourceLocation name, ItemStack icon, boolean requiresServer) {
    public static final ArmorStandScreenType EQUIPMENT = new ArmorStandScreenType(StatueMenus.id("equipment"),
            new ItemStack(Items.IRON_CHESTPLATE),
            true);
    public static final ArmorStandScreenType ROTATIONS = new ArmorStandScreenType(StatueMenus.id("rotations"),
            new ItemStack(Items.COMPASS));
    public static final ArmorStandScreenType STYLE = new ArmorStandScreenType(StatueMenus.id("style"),
            new ItemStack(Items.PAINTING));
    public static final ArmorStandScreenType POSES = new ArmorStandScreenType(StatueMenus.id("poses"),
            new ItemStack(Items.SPYGLASS));
    public static final ArmorStandScreenType POSITION = new ArmorStandScreenType(StatueMenus.id("position"),
            new ItemStack(Items.GRASS_BLOCK));

    public ArmorStandScreenType(ResourceLocation name, ItemStack icon) {
        this(name, icon, false);
    }

    public String getTranslationKey() {
        return Util.makeDescriptionId("screen.type", this.name());
    }
}
