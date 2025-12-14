package fuzs.statuemenus.impl;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.core.v1.context.PayloadTypesContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerInteractEvents;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.statuemenus.api.v1.helper.ArmorStandInteractHelper;
import fuzs.statuemenus.api.v1.world.entity.decoration.ArmorStandStatue;
import fuzs.statuemenus.api.v1.world.inventory.StatueMenu;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueStyleOption;
import fuzs.statuemenus.impl.network.client.*;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatueMenus implements ModConstructor {
    public static final String MOD_ID = "statuemenus";
    public static final String MOD_NAME = "Statue Menus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ResourceLocation ARMOR_STAND_IDENTIFIER = id("armor_stand");

    @Override
    public void onConstructMod() {
        setupDevelopmentEnvironment();
    }

    @SuppressWarnings("unchecked")
    private static void setupDevelopmentEnvironment() {
        if (!ModLoaderEnvironment.INSTANCE.isDevelopmentEnvironment(MOD_ID)) return;
        RegistryManager registry = RegistryManager.from(MOD_ID);
        Object[] holder = new Object[1];
        holder[0] = registry.registerMenuType(ARMOR_STAND_IDENTIFIER.getPath(),
                (int containerId, Inventory inventory, StatueMenu.Data data) -> {
                    return new <ArmorStand>StatueMenu(((Holder.Reference<MenuType<StatueMenu>>) holder[0]).value(),
                            containerId,
                            inventory,
                            data,
                            (ArmorStand armorStand) -> {
                                return (ArmorStandStatue) () -> armorStand;
                            });
                },
                StatueMenu.Data.STREAM_CODEC);
        PlayerInteractEvents.USE_ENTITY_AT.register(onUseEntityAt((Holder.Reference<MenuType<?>>) holder[0]));
    }

    private static PlayerInteractEvents.UseEntityAt onUseEntityAt(Holder<MenuType<?>> menuType) {
        return (Player player, Level level, InteractionHand interactionHand, Entity entity, Vec3 hitVector) -> {
            if (entity.getType() == EntityType.ARMOR_STAND && entity instanceof ArmorStand armorStand) {
                if (player.isShiftKeyDown() && player.getItemInHand(interactionHand).is(Items.DEBUG_STICK)) {
                    InteractionResult interactionResult = ArmorStandInteractHelper.openStatueMenu(player,
                            level,
                            armorStand,
                            menuType.value(),
                            (ArmorStandStatue) () -> armorStand);
                    if (interactionResult.consumesAction()) {
                        return EventResultHolder.interrupt(interactionResult);
                    }
                }
            }

            return EventResultHolder.pass();
        };
    }

    @Override
    public void onCommonSetup() {
        StatueStyleOption.bootstrap();
    }

    @Override
    public void onRegisterPayloadTypes(PayloadTypesContext context) {
        context.optional();
        context.playToServer(ServerboundStatueNameMessage.class, ServerboundStatueNameMessage.STREAM_CODEC);
        context.playToServer(ServerboundStatueStyleMessage.class, ServerboundStatueStyleMessage.STREAM_CODEC);
        context.playToServer(ServerboundStatuePositionMessage.class, ServerboundStatuePositionMessage.STREAM_CODEC);
        context.playToServer(ServerboundStatuePoseMessage.class, ServerboundStatuePoseMessage.STREAM_CODEC);
        context.playToServer(ServerboundStatuePropertyMessage.class, ServerboundStatuePropertyMessage.STREAM_CODEC);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
