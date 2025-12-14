package fuzs.statuemenus.impl.world.inventory;

import fuzs.statuemenus.api.v1.world.inventory.data.StatueStyleOption;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class ArmorStandSlot extends StatueSlot {
    public static final int DISABLE_ALL_OFFSET = 0;
    public static final int DISABLE_TAKING_OFFSET = ArmorStand.DISABLE_TAKING_OFFSET;
    public static final int DISABLE_PUTTING_OFFSET = ArmorStand.DISABLE_PUTTING_OFFSET;

    private final ArmorStand armorStand;
    private final EquipmentSlot slot;
    private final Player player;

    public ArmorStandSlot(Container container, ArmorStand owner, EquipmentSlot slot, int slotIndex, int x, int y, @Nullable Identifier emptyIcon, Player player) {
        super(container, owner, slot, slotIndex, x, y, emptyIcon);
        this.armorStand = owner;
        this.slot = slot;
        this.player = player;
    }

    @Override
    public void setByPlayer(ItemStack newItemStack, ItemStack oldItemStack) {
        super.setByPlayer(newItemStack, oldItemStack);
        if (!newItemStack.isEmpty() && this.slot.getType() == EquipmentSlot.Type.HAND) {
            StatueStyleOption.setArmorStandClientFlag(this.armorStand, true, ArmorStand.CLIENT_FLAG_SHOW_ARMS);
        }
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        if (!this.player.isCreative()) {
            if (isSlotDisabled(this.armorStand, this.slot, DISABLE_ALL_OFFSET)) {
                return false;
            } else if (isSlotDisabled(this.armorStand, this.slot, DISABLE_PUTTING_OFFSET) && !this.hasItem()) {
                return false;
            }
        }

        return super.mayPlace(itemStack);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public boolean mayPickup(Player player) {
        if (!player.isCreative() && isSlotDisabled(this.armorStand, this.slot, DISABLE_TAKING_OFFSET)) {
            return false;
        } else {
            return super.mayPickup(player);
        }
    }

    public static boolean isSlotDisabled(ArmorStand armorStand, EquipmentSlot equipmentSlot, int offset) {
        return (armorStand.disabledSlots & 1 << equipmentSlot.getFilterBit(offset)) != 0;
    }
}
