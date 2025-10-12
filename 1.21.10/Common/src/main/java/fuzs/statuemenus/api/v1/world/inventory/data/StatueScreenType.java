package fuzs.statuemenus.api.v1.world.inventory.data;

import fuzs.statuemenus.impl.StatueMenus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record StatueScreenType(ResourceLocation id, ItemStack item, boolean requiresServer) {
    public static final StatueScreenType EQUIPMENT = new StatueScreenType(StatueMenus.id("equipment"),
            new ItemStack(Items.IRON_CHESTPLATE),
            true);
    public static final StatueScreenType ROTATIONS = new StatueScreenType(StatueMenus.id("rotations"),
            new ItemStack(Items.COMPASS));
    public static final StatueScreenType STYLE = new StatueScreenType(StatueMenus.id("style"),
            new ItemStack(Items.PAINTING));
    public static final StatueScreenType POSES = new StatueScreenType(StatueMenus.id("poses"),
            new ItemStack(Items.SPYGLASS));
    public static final StatueScreenType POSITION = new StatueScreenType(StatueMenus.id("position"),
            new ItemStack(Items.GRASS_BLOCK));

    public StatueScreenType(ResourceLocation id, ItemStack item) {
        this(id, item, false);
    }

    public String getTranslationKey() {
        return StatueMenus.id("type").toLanguageKey("screen", this.id.toLanguageKey());
    }
}
