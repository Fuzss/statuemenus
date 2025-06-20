package fuzs.statuemenus.api.v1.world.inventory;

import fuzs.puzzleslib.api.container.v1.ContainerMenuHelper;
import fuzs.puzzleslib.api.container.v1.QuickMoveRuleSet;
import fuzs.statuemenus.api.v1.world.entity.decoration.ArmorStandDataProvider;
import fuzs.statuemenus.impl.world.inventory.ArmorStandSlot;
import fuzs.statuemenus.impl.world.inventory.EquipmentContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ArmorStandMenu extends AbstractContainerMenu implements ArmorStandHolder {
    public static final EquipmentSlot[] SLOT_IDS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET,
            null,
            null,
            EquipmentSlot.MAINHAND,
            EquipmentSlot.OFFHAND
    };

    private final ArmorStand armorStand;
    @Nullable
    private final ArmorStandDataProvider dataProvider;

    public ArmorStandMenu(MenuType<?> menuType, int containerId, Inventory inventory, ArmorStandData data, @Nullable ArmorStandDataProvider dataProvider) {
        this(menuType, containerId, inventory, data.setupArmorStand(inventory.player.level()), dataProvider);
    }

    public ArmorStandMenu(MenuType<?> menuType, int containerId, Inventory inventory, ArmorStand armorStand, @Nullable ArmorStandDataProvider dataProvider) {
        super(menuType, containerId);
        Objects.requireNonNull(armorStand, "armor stand is null");
        this.armorStand = armorStand;
        Container container = new EquipmentContainer(armorStand);
        container.startOpen(inventory.player);
        this.dataProvider = dataProvider;
        for (int i = 0; i < SLOT_IDS.length; ++i) {
            EquipmentSlot equipmentSlot = SLOT_IDS[i];
            if (equipmentSlot != null) {
                this.addSlot(new ArmorStandSlot(container,
                        armorStand,
                        equipmentSlot,
                        58 + i / 4 * 78,
                        20 + i % 4 * 18,
                        inventory.player));
            }
        }

        ContainerMenuHelper.addInventorySlots(this, inventory, 25, 96);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return QuickMoveRuleSet.of(this, this::moveItemStackTo)
                .addContainerSlotRule(0,
                        (Slot slot) -> this.armorStand.isEquippableInSlot(slot.getItem(), EquipmentSlot.HEAD))
                .addContainerSlotRule(1,
                        (Slot slot) -> this.armorStand.isEquippableInSlot(slot.getItem(), EquipmentSlot.CHEST))
                .addContainerSlotRule(2,
                        (Slot slot) -> this.armorStand.isEquippableInSlot(slot.getItem(), EquipmentSlot.LEGS))
                .addContainerSlotRule(3,
                        (Slot slot) -> this.armorStand.isEquippableInSlot(slot.getItem(), EquipmentSlot.FEET))
                .addContainerSlotRule(5,
                        (Slot slot) -> this.armorStand.isEquippableInSlot(slot.getItem(), EquipmentSlot.OFFHAND))
                .addInventoryRules()
                .addInventoryCompartmentRules()
                .quickMoveStack(player, index);
    }

    @Override
    public boolean stillValid(Player player) {
        // no distance check to avoid annoying issues when moving armor stand far away from configuration screen
        return this.armorStand.isAlive();
    }

    @Override
    public ArmorStand getArmorStand() {
        return this.armorStand;
    }

    @Override
    public ArmorStandDataProvider getDataProvider() {
        return this.dataProvider != null ? this.dataProvider : ArmorStandHolder.super.getDataProvider();
    }

    public record ArmorStandData(int entityId, boolean isInvulnerable, int disabledSlots) {
        public static final StreamCodec<ByteBuf, ArmorStandData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT,
                ArmorStandData::entityId,
                ByteBufCodecs.BOOL,
                ArmorStandData::isInvulnerable,
                ByteBufCodecs.INT,
                ArmorStandData::disabledSlots,
                ArmorStandData::new);

        public static ArmorStandData of(ArmorStand armorStand) {
            return new ArmorStandData(armorStand.getId(), armorStand.isInvulnerable(), armorStand.disabledSlots);
        }

        public ArmorStand setupArmorStand(Level level) {
            if (level.getEntity(this.entityId) instanceof ArmorStand armorStand) {
                // vanilla doesn't sync these automatically, we need them for the menu
                armorStand.setInvulnerable(this.isInvulnerable);
                armorStand.disabledSlots = this.disabledSlots;
                // also create the armor stand container client side, so that visual update instantly instead of having to wait for the server to resync data
                return armorStand;
            } else {
                // exception is caught, so nothing will crash, only the screen will not open
                // not sure how this is even possible, but there was a report about it
                // report was concerning just placed statues, so maybe entity data arrived at remote after menu was opened
                throw new IllegalStateException("Armor stand missing on client for id " + this.entityId);
            }
        }
    }
}
