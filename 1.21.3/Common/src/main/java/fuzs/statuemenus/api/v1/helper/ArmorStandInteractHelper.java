package fuzs.statuemenus.api.v1.helper;

import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.util.v1.InteractionResultHelper;
import fuzs.statuemenus.api.v1.world.entity.decoration.ArmorStandDataProvider;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ArmorStandInteractHelper {
    public static final String OPEN_SCREEN_TRANSLATION_KEY = Items.ARMOR_STAND.getDescriptionId() + ".description";

    public static EventResultHolder<InteractionResult> tryOpenArmorStatueMenu(Player player, Level level, InteractionHand interactionHand, ArmorStand entity, MenuType<?> menuType, @Nullable ArmorStandDataProvider dataProvider) {
        ItemStack itemInHand = player.getItemInHand(interactionHand);
        return itemInHand.isEmpty() ? tryOpenArmorStatueMenu(player, level, entity, menuType, dataProvider) :
                EventResultHolder.pass();
    }

    public static EventResultHolder<InteractionResult> tryOpenArmorStatueMenu(Player player, Level level, ArmorStand entity, MenuType<?> menuType, @Nullable ArmorStandDataProvider dataProvider) {
        if (player.isShiftKeyDown() && (!entity.isInvulnerable() || player.getAbilities().instabuild)) {
            openArmorStatueMenu(player, entity, menuType, dataProvider);
            return EventResultHolder.interrupt(InteractionResultHelper.sidedSuccess(level.isClientSide));
        }
        return EventResultHolder.pass();
    }

    public static void openArmorStatueMenu(Player player, ArmorStand entity, MenuType<?> menuType, @Nullable ArmorStandDataProvider dataProvider) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        CommonAbstractions.INSTANCE.openMenu(serverPlayer, new SimpleMenuProvider((containerId, inventory, player1) -> {
            return ArmorStandMenu.create(menuType, containerId, inventory, entity, dataProvider);
        }, entity.getDisplayName()), (serverPlayer1, friendlyByteBuf) -> {
            friendlyByteBuf.writeInt(entity.getId());
            friendlyByteBuf.writeBoolean(entity.isInvulnerable());
            friendlyByteBuf.writeInt(entity.disabledSlots);
        });
    }

    public static Component getArmorStandHoverText() {
        Component shiftComponent = Component.keybind("key.sneak").withStyle(ChatFormatting.LIGHT_PURPLE);
        Component useComponent = Component.keybind("key.use").withStyle(ChatFormatting.LIGHT_PURPLE);
        return Component.translatable(OPEN_SCREEN_TRANSLATION_KEY, shiftComponent, useComponent)
                .withStyle(ChatFormatting.GRAY);
    }
}
