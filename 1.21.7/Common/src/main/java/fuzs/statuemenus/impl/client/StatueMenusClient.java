package fuzs.statuemenus.impl.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.MenuScreensContext;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.statuemenus.api.v1.client.gui.screens.*;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandScreenType;
import fuzs.statuemenus.api.v1.world.inventory.data.PosePartMutator;
import fuzs.statuemenus.impl.StatueMenus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class StatueMenusClient implements ClientModConstructor {

    @Override
    public void onClientSetup() {
        ArmorStandScreenFactory.register(ArmorStandScreenType.EQUIPMENT, ArmorStandEquipmentScreen::new);
        ArmorStandScreenFactory.register(ArmorStandScreenType.ROTATIONS, ArmorStandRotationsScreen::new);
        ArmorStandScreenFactory.register(ArmorStandScreenType.STYLE, ArmorStandStyleScreen::new);
        if (ModLoaderEnvironment.INSTANCE.getModLoader().isNeoForge()) {
            ArmorStandScreenFactory.register(ArmorStandScreenType.POSES, ArmorStandPosesScreen::new);
        } else {
            ArmorStandScreenFactory.register(ArmorStandScreenType.POSES, SimpleArmorStandPosesScreen::new);
        }
        ArmorStandScreenFactory.register(ArmorStandScreenType.POSITION, ArmorStandPositionScreen::new);
        ArmorStandRotationsScreen.registerPosePartMutatorFilter(PosePartMutator.LEFT_ARM, ArmorStand::showArms);
        ArmorStandRotationsScreen.registerPosePartMutatorFilter(PosePartMutator.RIGHT_ARM, ArmorStand::showArms);
    }

    @SuppressWarnings({"unchecked", "Convert2MethodRef"})
    @Override
    public void onRegisterMenuScreens(MenuScreensContext context) {
        if (!ModLoaderEnvironment.INSTANCE.isDevelopmentEnvironment(StatueMenus.MOD_ID)) return;
        // compiler doesn't like method reference :(
        context.registerMenuScreen((MenuType<ArmorStandMenu>) BuiltInRegistries.MENU.getValue(StatueMenus.ARMOR_STAND_IDENTIFIER),
                (ArmorStandMenu menu, Inventory inventory, Component component) -> {
                    return ArmorStandScreenFactory.createLastScreenType(menu, inventory, component);
                });
    }
}
