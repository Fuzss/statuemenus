package fuzs.statuemenus.api.v1.world.inventory.data;

import fuzs.statuemenus.impl.StatueMenus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;

import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Function;

public enum ArmorStandStyleOptions implements ArmorStandStyleOption, StringRepresentable {
    SHOW_NAME("CustomNameVisible", Entity::setCustomNameVisible, Entity::isCustomNameVisible),
    SHOW_ARMS("ShowArms", (ArmorStand armorStand, Boolean setting) -> {
        ArmorStandStyleOption.setArmorStandData(armorStand, setting, ArmorStand.CLIENT_FLAG_SHOW_ARMS);
    }, (ArmorStand armorStand) -> {
        return ArmorStandStyleOption.getArmorStandData(armorStand, ArmorStand.CLIENT_FLAG_SHOW_ARMS);
    }),
    SMALL("Small", (ArmorStand armorStand, Boolean setting) -> {
        ArmorStandStyleOption.setArmorStandData(armorStand, setting, ArmorStand.CLIENT_FLAG_SMALL);
    }, (ArmorStand armorStand) -> {
        return ArmorStandStyleOption.getArmorStandData(armorStand, ArmorStand.CLIENT_FLAG_SMALL);
    }),
    INVISIBLE("Invisible", ArmorStand::setInvisible, Entity::isInvisible),
    NO_BASE_PLATE("NoBasePlate", (ArmorStand armorStand, Boolean setting) -> {
        ArmorStandStyleOption.setArmorStandData(armorStand, setting, ArmorStand.CLIENT_FLAG_NO_BASEPLATE);
    }, (ArmorStand armorStand) -> {
        return ArmorStandStyleOption.getArmorStandData(armorStand, ArmorStand.CLIENT_FLAG_NO_BASEPLATE);
    }),
    NO_GRAVITY("NoGravity", Entity::setNoGravity, Entity::isNoGravity),
    SEALED("Invulnerable", (ArmorStand armorStand, Boolean setting) -> {
        armorStand.setInvulnerable(setting);
        armorStand.disabledSlots = setting ? ArmorStandStyleOptions.ARMOR_STAND_ALL_SLOTS_DISABLED : 0;
    }, Entity::isInvulnerable) {
        @Override
        public void toTag(CompoundTag compoundTag, boolean currentValue) {
            super.toTag(compoundTag, currentValue);
            compoundTag.putInt("DisabledSlots", currentValue ? ARMOR_STAND_ALL_SLOTS_DISABLED : 0);
        }
    };

    public static final int ARMOR_STAND_ALL_SLOTS_DISABLED = 0b1111110011111100111111;

    private final ResourceLocation name;
    private final String dataKey;
    private final BiConsumer<ArmorStand, Boolean> newValue;
    private final Function<ArmorStand, Boolean> currentValue;

    ArmorStandStyleOptions(String dataKey, BiConsumer<ArmorStand, Boolean> newValue, Function<ArmorStand, Boolean> currentValue) {
        this.name = StatueMenus.id(this.getSerializedName());
        this.dataKey = dataKey;
        this.newValue = newValue;
        this.currentValue = currentValue;
    }

    @Override
    public ResourceLocation getName() {
        return this.name;
    }

    @Override
    public void setOption(ArmorStand armorStand, boolean setting) {
        this.newValue.accept(armorStand, setting);
    }

    @Override
    public boolean getOption(ArmorStand armorStand) {
        return this.currentValue.apply(armorStand);
    }

    @Override
    public void toTag(CompoundTag compoundTag, boolean currentValue) {
        compoundTag.putBoolean(this.dataKey, currentValue);
    }

    @Override
    public boolean allowChanges(Player player) {
        return this != ArmorStandStyleOptions.SEALED || player.getAbilities().instabuild;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
