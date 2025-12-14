package fuzs.statuemenus.impl.world.inventory;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class StatueSlot extends ArmorSlot {
    protected final EquipmentSlot slot;

    public StatueSlot(Container container, LivingEntity owner, EquipmentSlot slot, int slotIndex, int x, int y, @Nullable Identifier emptyIcon) {
        super(container, owner, slot, slotIndex, x, y, emptyIcon);
        this.slot = slot;
    }

    @Override
    public void setByPlayer(ItemStack newItemStack, ItemStack oldItemStack) {
        // LivingEntity::onEquipItem is already called from EquipmentContainer
        this.set(newItemStack);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        if (this.slot.getType() == EquipmentSlot.Type.HAND) {
            return true;
        } else if (this.slot == EquipmentSlot.HEAD && itemStack.get(DataComponents.EQUIPPABLE) == null) {
            return true;
        } else {
            return super.mayPlace(itemStack);
        }
    }
}
