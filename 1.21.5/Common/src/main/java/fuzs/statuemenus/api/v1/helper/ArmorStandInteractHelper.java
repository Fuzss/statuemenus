package fuzs.statuemenus.api.v1.helper;

import fuzs.puzzleslib.api.container.v1.ContainerMenuHelper;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.util.v1.InteractionResultHelper;
import fuzs.statuemenus.api.v1.world.entity.decoration.ArmorStandDataProvider;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public final class ArmorStandInteractHelper {
    public static final String OPEN_SCREEN_TRANSLATION_KEY = Items.ARMOR_STAND.getDescriptionId() + ".description";

    private ArmorStandInteractHelper() {
        // NO-OP
    }

    public static EventResultHolder<InteractionResult> tryOpenArmorStatueMenu(Player player, Level level, InteractionHand interactionHand, ArmorStand armorStand, MenuType<?> menuType, @Nullable ArmorStandDataProvider dataProvider) {
        ItemStack itemInHand = player.getItemInHand(interactionHand);
        return itemInHand.isEmpty() ? tryOpenArmorStatueMenu(player, level, armorStand, menuType, dataProvider) :
                EventResultHolder.pass();
    }

    public static EventResultHolder<InteractionResult> tryOpenArmorStatueMenu(Player player, Level level, ArmorStand armorStand, MenuType<?> menuType, @Nullable ArmorStandDataProvider dataProvider) {
        if (player.isShiftKeyDown() && (!armorStand.isInvulnerable() || player.getAbilities().instabuild)) {
            openArmorStatueMenu(player, armorStand, menuType, dataProvider);
            return EventResultHolder.interrupt(InteractionResultHelper.sidedSuccess(level.isClientSide));
        }
        return EventResultHolder.pass();
    }

    public static void openArmorStatueMenu(Player player, ArmorStand armorStand, MenuType<?> menuType, @Nullable ArmorStandDataProvider dataProvider) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        ContainerMenuHelper.openMenu(serverPlayer,
                new SimpleMenuProvider((int containerId, Inventory inventory, Player playerX) -> {
                    return new ArmorStandMenu(menuType, containerId, inventory, armorStand, dataProvider);
                }, armorStand.getDisplayName()),
                (ServerPlayer serverPlayerX, RegistryFriendlyByteBuf buf) -> {
                    buf.writeInt(armorStand.getId());
                    buf.writeBoolean(armorStand.isInvulnerable());
                    buf.writeInt(armorStand.disabledSlots);
                });
    }

    public static Component getArmorStandHoverText() {
        Component shiftComponent = Component.keybind("key.sneak").withStyle(ChatFormatting.LIGHT_PURPLE);
        Component useComponent = Component.keybind("key.use").withStyle(ChatFormatting.LIGHT_PURPLE);
        return Component.translatable(OPEN_SCREEN_TRANSLATION_KEY, shiftComponent, useComponent)
                .withStyle(ChatFormatting.GRAY);
    }
}
