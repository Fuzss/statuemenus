package fuzs.statuemenus.impl.world.inventory;

import com.google.common.collect.ImmutableMap;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandStyleOption;
import fuzs.statuemenus.impl.StatueMenus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class ArmorStandSlot extends ArmorSlot {
    static final ResourceLocation EMPTY_ARMOR_SLOT_SWORD = StatueMenus.id("container/slot/sword");
    static final Map<EquipmentSlot, ResourceLocation> TEXTURE_EMPTY_SLOTS = ImmutableMap.<EquipmentSlot, ResourceLocation>builder()
            .putAll(InventoryMenu.TEXTURE_EMPTY_SLOTS)
            .put(EquipmentSlot.OFFHAND, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD)
            .put(EquipmentSlot.MAINHAND, EMPTY_ARMOR_SLOT_SWORD)
            .build();

    private final ArmorStand armorStand;
    private final EquipmentSlot slot;
    private final Player player;

    public ArmorStandSlot(Container container, ArmorStand armorStand, EquipmentSlot slot, int x, int y, Player player) {
        super(container, armorStand, slot, slot.ordinal(), x, y, TEXTURE_EMPTY_SLOTS.get(slot));
        this.armorStand = armorStand;
        this.slot = slot;
        this.player = player;
    }

    @Override
    public void setByPlayer(ItemStack newItemStack, ItemStack oldItemStack) {
        this.set(newItemStack);
        if (!newItemStack.isEmpty() && this.slot.getType() == EquipmentSlot.Type.HAND) {
            ArmorStandStyleOption.setArmorStandData(this.armorStand, true, ArmorStand.CLIENT_FLAG_SHOW_ARMS);
        }
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        if (!this.player.isCreative()) {
            if (isSlotDisabled(this.armorStand, this.slot, 0)) {
                return false;
            } else if (isSlotDisabled(this.armorStand, this.slot, ArmorStand.DISABLE_PUTTING_OFFSET) &&
                    !this.hasItem()) {
                return false;
            }
        }

        return this.slot == EquipmentSlot.HEAD || this.slot.getType() == EquipmentSlot.Type.HAND ||
                super.mayPlace(itemStack);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public boolean mayPickup(Player player) {
        if (!player.isCreative() && isSlotDisabled(this.armorStand, this.slot, ArmorStand.DISABLE_TAKING_OFFSET)) {
            return false;
        } else {
            return super.mayPickup(player);
        }
    }

    public static boolean isSlotDisabled(ArmorStand armorStand, EquipmentSlot equipmentSlot, int offset) {
        return (armorStand.disabledSlots & 1 << equipmentSlot.getFilterBit(offset)) != 0;
    }
}
