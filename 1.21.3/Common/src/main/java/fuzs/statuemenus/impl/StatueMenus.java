package fuzs.statuemenus.impl;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerInteractEvents;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.network.v3.NetworkHandler;
import fuzs.statuemenus.api.v1.helper.ArmorStandInteractHelper;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandStyleOption;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandStyleOptions;
import fuzs.statuemenus.impl.network.client.*;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
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

import java.util.Locale;

public class StatueMenus implements ModConstructor {
    public static final String MOD_ID = "statuemenus";
    public static final String MOD_NAME = "Statue Menus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final NetworkHandler NETWORK = NetworkHandler.builder(MOD_ID)
            .optional()
            .registerLegacyServerbound(C2SArmorStandNameMessage.class, C2SArmorStandNameMessage::new)
            .registerLegacyServerbound(C2SArmorStandStyleMessage.class, C2SArmorStandStyleMessage::new)
            .registerLegacyServerbound(C2SArmorStandPositionMessage.class, C2SArmorStandPositionMessage::new)
            .registerLegacyServerbound(C2SArmorStandPoseMessage.class, C2SArmorStandPoseMessage::new)
            .registerLegacyServerbound(C2SArmorStandRotationMessage.class, C2SArmorStandRotationMessage::new);
    ;

    public static final ResourceLocation ARMOR_STAND_IDENTIFIER = id("armor_stand");

    @Override
    public void onConstructMod() {
        setupDevelopmentEnvironment();
    }

    @SuppressWarnings("unchecked")
    private static void setupDevelopmentEnvironment() {
        if (!ModLoaderEnvironment.INSTANCE.isDevelopmentEnvironment()) return;
        RegistryManager registry = RegistryManager.from(MOD_ID);
        Object[] holder = new Object[1];
        holder[0] = registry.registerExtendedMenuType(ARMOR_STAND_IDENTIFIER.getPath(),
                () -> (int containerId, Inventory inventory, RegistryFriendlyByteBuf buf) -> {
                    return ArmorStandMenu.create(((Holder.Reference<MenuType<ArmorStandMenu>>) holder[0]).value(),
                            containerId,
                            inventory,
                            buf,
                            null
                    );
                }
        );
        PlayerInteractEvents.USE_ENTITY_AT.register(onUseEntityAt((Holder.Reference<MenuType<?>>) holder[0]));
    }

    private static PlayerInteractEvents.UseEntityAt onUseEntityAt(Holder<MenuType<?>> menuType) {
        return (Player player, Level level, InteractionHand interactionHand, Entity entity, Vec3 hitVector) -> {
            if (player.getAbilities().mayBuild && entity.getType() == EntityType.ARMOR_STAND &&
                    player.getItemInHand(interactionHand).is(Items.DEBUG_STICK)) {
                return ArmorStandInteractHelper.tryOpenArmorStatueMenu(player,
                        level,
                        (ArmorStand) entity,
                        menuType.value(),
                        null
                );
            }
            return EventResultHolder.pass();
        };
    }

    @Override
    public void onCommonSetup() {
        // do this here instead of in enum constructor to avoid potential issues with the enum class not having been loaded yet on server-side, therefore nothing being registered
        for (ArmorStandStyleOptions styleOption : ArmorStandStyleOptions.values()) {
            ArmorStandStyleOption.register(id(styleOption.getName().toLowerCase(Locale.ROOT)), styleOption);
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
