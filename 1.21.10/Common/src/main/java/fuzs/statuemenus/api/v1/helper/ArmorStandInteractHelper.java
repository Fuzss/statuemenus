package fuzs.statuemenus.api.v1.helper;

import fuzs.puzzleslib.api.container.v1.ContainerMenuHelper;
import fuzs.statuemenus.api.v1.world.entity.decoration.StatueEntity;
import fuzs.statuemenus.api.v1.world.inventory.StatueMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public final class ArmorStandInteractHelper {
    public static final String OPEN_SCREEN_TRANSLATION_KEY = Items.ARMOR_STAND.getDescriptionId() + ".description";

    private ArmorStandInteractHelper() {
        // NO-OP
    }

    public static InteractionResult openStatueMenu(Player player, Level level, InteractionHand interactionHand, LivingEntity livingEntity, MenuType<?> menuType, StatueEntity statueEntity) {
        if (player.isShiftKeyDown() && player.getItemInHand(interactionHand).isEmpty()) {
            return openStatueMenu(player, level, livingEntity, menuType, statueEntity);
        } else {
            return InteractionResult.PASS;
        }
    }

    public static InteractionResult openStatueMenu(Player player, Level level, LivingEntity livingEntity, MenuType<?> menuType, StatueEntity statueEntity) {
        if (!player.isSpectator() && player.getAbilities().mayBuild && (!statueEntity.isSealed()
                || player.getAbilities().instabuild)) {
            if (player instanceof ServerPlayer serverPlayer) {
                ContainerMenuHelper.openMenu(serverPlayer,
                        new SimpleMenuProvider((int containerId, Inventory inventory, Player playerX) -> {
                            return new StatueMenu(menuType, containerId, inventory, livingEntity, statueEntity);
                        }, livingEntity.getDisplayName()),
                        StatueMenu.Data.of(livingEntity));
            }

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    public static Component getArmorStandHoverText() {
        Component shiftComponent = Component.keybind("key.sneak").withStyle(ChatFormatting.LIGHT_PURPLE);
        Component useComponent = Component.keybind("key.use").withStyle(ChatFormatting.LIGHT_PURPLE);
        return Component.translatable(OPEN_SCREEN_TRANSLATION_KEY, shiftComponent, useComponent)
                .withStyle(ChatFormatting.GRAY);
    }
}
