package fuzs.statuemenus.api.v1.world.inventory;

import com.google.common.collect.ImmutableMap;
import fuzs.puzzleslib.api.container.v1.QuickMoveRuleSet;
import fuzs.statuemenus.api.v1.world.entity.decoration.StatueEntity;
import fuzs.statuemenus.impl.StatueMenus;
import fuzs.statuemenus.impl.world.inventory.ArmorStandSlot;
import fuzs.statuemenus.impl.world.inventory.EquipmentContainer;
import fuzs.statuemenus.impl.world.inventory.StatueSlot;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class StatueMenu extends AbstractContainerMenu implements StatueHolder {
    public static final Identifier EMPTY_ARMOR_SLOT_SWORD = StatueMenus.id("container/slot/sword");
    public static final Map<EquipmentSlot, Identifier> TEXTURE_EMPTY_SLOTS = ImmutableMap.<EquipmentSlot, Identifier>builder()
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
    private final StatueEntity statueEntity;

    public <T extends LivingEntity> StatueMenu(MenuType<?> menuType, int containerId, Inventory inventory, Data data, Function<T, StatueEntity> statueEntityFactory) {
        this(menuType, containerId, inventory, (T) data.getEntity(inventory.player.level()), statueEntityFactory);
    }

    private <T extends LivingEntity> StatueMenu(MenuType<?> menuType, int containerId, Inventory inventory, T livingEntity, Function<T, StatueEntity> statueEntityFactory) {
        this(menuType, containerId, inventory, livingEntity, statueEntityFactory.apply(livingEntity));
    }

    public StatueMenu(MenuType<?> menuType, int containerId, Inventory inventory, LivingEntity livingEntity, StatueEntity statueEntity) {
        super(menuType, containerId);
        Objects.requireNonNull(livingEntity, "living entity is null");
        this.livingEntity = livingEntity;
        Objects.requireNonNull(statueEntity, "statue entity is null");
        this.statueEntity = statueEntity;
        Container container = new EquipmentContainer(livingEntity);
        container.startOpen(inventory.player);
        for (int i = 0; i < SLOT_IDS.length; ++i) {
            EquipmentSlot equipmentSlot = SLOT_IDS[i];
            if (equipmentSlot != null) {
                this.addSlot(this.createEquipmentSlot(container,
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

    protected Slot createEquipmentSlot(Container container, LivingEntity owner, EquipmentSlot slot, int slotIndex, int x, int y, Identifier emptyIcon, Player player) {
        if (owner instanceof ArmorStand armorStand) {
            return new ArmorStandSlot(container, armorStand, slot, slotIndex, x, y, emptyIcon, player);
        } else {
            return new StatueSlot(container, owner, slot, slotIndex, x, y, emptyIcon);
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
        return this.statueEntity;
    }

    public record Data(int entityId, boolean isInvulnerable, int disabledSlots) {
        public static final StreamCodec<ByteBuf, Data> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT,
                Data::entityId,
                ByteBufCodecs.BOOL,
                Data::isInvulnerable,
                ByteBufCodecs.INT,
                Data::disabledSlots,
                Data::new);

        public static Data of(LivingEntity livingEntity) {
            return of(livingEntity, 0);
        }

        public static Data of(ArmorStand armorStand) {
            return of(armorStand, armorStand.disabledSlots);
        }

        public static Data of(LivingEntity livingEntity, int disabledSlots) {
            return new Data(livingEntity.getId(), livingEntity.isInvulnerable(), disabledSlots);
        }

        public LivingEntity getEntity(Level level) {
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
