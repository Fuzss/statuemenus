package fuzs.statuemenus.impl.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.MenuScreensContext;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.statuemenus.api.v1.client.gui.screens.*;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.StatueHolder;
import fuzs.statuemenus.api.v1.world.inventory.StatueMenu;
import fuzs.statuemenus.api.v1.world.inventory.data.PosePartMutator;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueScreenType;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueStyleOption;
import fuzs.statuemenus.impl.StatueMenus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import java.util.List;

/**
 * Some lambdas / method references in this class are problematic for javac and / or Mercury. They need to be kept as
 * they are.
 */
public class StatueMenusClient implements ClientModConstructor {

    @SuppressWarnings("Convert2Lambda")
    @Override
    public void onClientSetup() {
        StatueScreenFactory.register(StatueScreenType.EQUIPMENT, StatueEquipmentScreen::new);
        StatueScreenFactory.register(StatueScreenType.ROTATIONS, new StatueScreenFactory<AbstractStatueScreen>() {
            @Override
            public AbstractStatueScreen create(StatueHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
                return new StatueRotationsScreen(holder, inventory, component, dataSyncHandler) {
                    @Override
                    protected boolean isPosePartMutatorActive(PosePartMutator posePartMutator, LivingEntity livingEntity) {
                        if (posePartMutator == PosePartMutator.LEFT_ARM
                                || posePartMutator == PosePartMutator.RIGHT_ARM) {
                            return !(livingEntity instanceof ArmorStand armorStand) || armorStand.showArms();
                        } else {
                            return super.isPosePartMutatorActive(posePartMutator, livingEntity);
                        }
                    }
                };
            }
        });
        StatueScreenFactory.register(StatueScreenType.STYLE, new StatueScreenFactory<AbstractStatueScreen>() {
            @Override
            public AbstractStatueScreen create(StatueHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
                return new StatueStyleScreen<ArmorStand>(holder, inventory, component, dataSyncHandler) {
                    @Override
                    protected List<StatueStyleOption<? super ArmorStand>> getStyleOptions() {
                        return StatueStyleOption.TYPES;
                    }

                    @Override
                    public StatueScreenType getScreenType() {
                        return StatueScreenType.STYLE;
                    }
                };
            }
        });
        StatueScreenFactory.register(StatueScreenType.POSES, StatuePosesScreen::new);
        StatueScreenFactory.register(StatueScreenType.POSITION, StatuePositionScreen::new);
    }

    @SuppressWarnings({"unchecked", "Convert2MethodRef"})
    @Override
    public void onRegisterMenuScreens(MenuScreensContext context) {
        if (!ModLoaderEnvironment.INSTANCE.isDevelopmentEnvironment(StatueMenus.MOD_ID)) {
            return;
        }

        context.registerMenuScreen((MenuType<StatueMenu>) BuiltInRegistries.MENU.getValue(StatueMenus.ARMOR_STAND_IDENTIFIER),
                (StatueMenu menu, Inventory inventory, Component component) -> {
                    return StatueScreenFactory.createLastScreenType(menu, inventory, component);
                });
    }
}
