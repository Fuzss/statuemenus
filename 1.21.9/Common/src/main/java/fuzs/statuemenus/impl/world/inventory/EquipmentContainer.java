package fuzs.statuemenus.impl.world.inventory;

import fuzs.puzzleslib.api.container.v1.SimpleContainerImpl;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class EquipmentContainer implements SimpleContainerImpl {
    private static final EquipmentSlot[] EQUIPMENT_SLOTS = EquipmentSlot.values();

    private final LivingEntity livingEntity;

    public EquipmentContainer(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    @Override
    public int getContainerSize() {
        return EQUIPMENT_SLOTS.length;
    }

    @Override
    public void setChanged() {
        // NO-OP
    }

    @Override
    public boolean stillValid(Player player) {
        return player.canInteractWithEntity(this.livingEntity, 4.0);
    }

    @Override
    public ItemStack getContainerItem(int slot) {
        return this.livingEntity.getItemBySlot(EQUIPMENT_SLOTS[slot]);
    }

    @Override
    public ItemStack removeContainerItem(int slot, int amount) {
        return this.getContainerItem(slot).split(amount);
    }

    @Override
    public ItemStack removeContainerItemNoUpdate(int slot) {
        ItemStack itemStack = this.getContainerItem(slot);
        this.setContainerItem(slot, ItemStack.EMPTY);
        return itemStack;
    }

    @Override
    public void setContainerItem(int slot, ItemStack itemStack) {
        this.livingEntity.setItemSlot(EQUIPMENT_SLOTS[slot], itemStack);
    }
}
