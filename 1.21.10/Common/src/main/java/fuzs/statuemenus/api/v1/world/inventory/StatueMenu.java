package fuzs.statuemenus.api.v1.world.inventory;

import com.google.common.collect.ImmutableMap;
import fuzs.puzzleslib.api.container.v1.QuickMoveRuleSet;
import fuzs.statuemenus.api.v1.world.entity.decoration.StatueEntity;
import fuzs.statuemenus.impl.StatueMenus;
import fuzs.statuemenus.impl.world.inventory.ArmorStandSlot;
import fuzs.statuemenus.impl.world.inventory.EquipmentContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class StatueMenu extends AbstractContainerMenu implements StatueHolder {
    public static final ResourceLocation EMPTY_ARMOR_SLOT_SWORD = StatueMenus.id("container/slot/sword");
    public static final Map<EquipmentSlot, ResourceLocation> TEXTURE_EMPTY_SLOTS = ImmutableMap.<EquipmentSlot, ResourceLocation>builder()
            .putAll(InventoryMenu.TEXTURE_EMPTY_SLOTS)
            .put(EquipmentSlot.OFFHAND, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD)
            .put(EquipmentSlot.MAINHAND, EMPTY_ARMOR_SLOT_SWORD)
            .build();
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

    private final LivingEntity livingEntity;
    @Nullable
    private final StatueEntity statueEntity;

    public StatueMenu(MenuType<?> menuType, int containerId, Inventory inventory, StatueData data, @Nullable StatueEntity statueEntity) {
        this(menuType, containerId, inventory, data.setupArmorStand(inventory.player.level()), statueEntity);
    }

    public StatueMenu(MenuType<?> menuType, int containerId, Inventory inventory, LivingEntity livingEntity, @Nullable StatueEntity statueEntity) {
        super(menuType, containerId);
        Objects.requireNonNull(livingEntity, "armor stand is null");
        this.livingEntity = livingEntity;
        Container container = new EquipmentContainer(livingEntity);
        container.startOpen(inventory.player);
        this.statueEntity = statueEntity;
        for (int i = 0; i < SLOT_IDS.length; ++i) {
            EquipmentSlot equipmentSlot = SLOT_IDS[i];
            if (equipmentSlot != null) {
                this.addSlot(this.createArmorSlot(container,
                        livingEntity,
                        equipmentSlot,
                        equipmentSlot.ordinal(),
                        58 + i / 4 * 78,
                        20 + i % 4 * 18,
                        TEXTURE_EMPTY_SLOTS.get(equipmentSlot),
                        inventory.player));
            }
        }

        this.addStandardInventorySlots(inventory, 25, 96);
    }

    protected Slot createArmorSlot(Container container, LivingEntity owner, EquipmentSlot slot, int slotIndex, int x, int y, ResourceLocation emptyIcon, Player player) {
        if (owner instanceof ArmorStand armorStand) {
            return new ArmorStandSlot(container, armorStand, slot, slotIndex, x, y, emptyIcon, player);
        } else {
            return new ArmorSlot(container, owner, slot, slotIndex, x, y, emptyIcon);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return QuickMoveRuleSet.of(this, this::moveItemStackTo)
                .addContainerSlotRule(0,
                        (Slot slot) -> this.getEntity().isEquippableInSlot(slot.getItem(), EquipmentSlot.HEAD))
                .addContainerSlotRule(1,
                        (Slot slot) -> this.getEntity().isEquippableInSlot(slot.getItem(), EquipmentSlot.CHEST))
                .addContainerSlotRule(2,
                        (Slot slot) -> this.getEntity().isEquippableInSlot(slot.getItem(), EquipmentSlot.LEGS))
                .addContainerSlotRule(3,
                        (Slot slot) -> this.getEntity().isEquippableInSlot(slot.getItem(), EquipmentSlot.FEET))
                .addContainerSlotRule(5,
                        (Slot slot) -> this.getEntity().isEquippableInSlot(slot.getItem(), EquipmentSlot.OFFHAND))
                .addInventoryRules()
                .addInventoryCompartmentRules()
                .quickMoveStack(player, index);
    }

    @Override
    public boolean stillValid(Player player) {
        // no distance check to avoid annoying issues when moving armor stand far away from configuration screen
        return this.getEntity().isAlive();
    }

    @Override
    public LivingEntity getEntity() {
        return this.livingEntity;
    }

    @Override
    public StatueMenu getMenu() {
        return this;
    }

    @Override
    public StatueEntity getStatueEntity() {
        return this.statueEntity != null ? this.statueEntity : StatueHolder.super.getStatueEntity();
    }

    public record StatueData(int entityId, boolean isInvulnerable, int disabledSlots) {
        public static final StreamCodec<ByteBuf, StatueData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT,
                StatueData::entityId,
                ByteBufCodecs.BOOL,
                StatueData::isInvulnerable,
                ByteBufCodecs.INT,
                StatueData::disabledSlots,
                StatueData::new);

        public static StatueData of(LivingEntity livingEntity) {
            return of(livingEntity, 0);
        }

        public static StatueData of(ArmorStand armorStand) {
            return of(armorStand, armorStand.disabledSlots);
        }

        public static StatueData of(LivingEntity livingEntity, int disabledSlots) {
            return new StatueData(livingEntity.getId(), livingEntity.isInvulnerable(), disabledSlots);
        }

        public LivingEntity setupArmorStand(Level level) {
            if (level.getEntity(this.entityId) instanceof LivingEntity livingEntity) {
                // vanilla doesn't sync these automatically, we need them for the menu
                livingEntity.setInvulnerable(this.isInvulnerable);
                if (livingEntity instanceof ArmorStand armorStand) {
                    armorStand.disabledSlots = this.disabledSlots;
                }

                return livingEntity;
            } else {
                // the exception is caught, so nothing will crash, only the screen will not open
                throw new IllegalStateException("Armor stand missing on client for id " + this.entityId);
            }
        }
    }
}
