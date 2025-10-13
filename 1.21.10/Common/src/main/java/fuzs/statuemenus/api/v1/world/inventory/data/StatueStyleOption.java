package fuzs.statuemenus.api.v1.world.inventory.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import fuzs.statuemenus.impl.StatueMenus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Objects;

public abstract class StatueStyleOption<T extends LivingEntity> implements StringRepresentable {
    private static final BiMap<ResourceLocation, StatueStyleOption<?>> OPTIONS_REGISTRY = HashBiMap.create();
    public static final StatueStyleOption<LivingEntity> SHOW_NAME = new StatueStyleOption<>(StatueMenus.id("show_name"),
            "CustomNameVisible") {
        @Override
        public void setOption(LivingEntity livingEntity, boolean value) {
            livingEntity.setCustomNameVisible(value);
        }

        @Override
        public boolean getOption(LivingEntity livingEntity) {
            return livingEntity.isCustomNameVisible();
        }
    };
    public static final StatueStyleOption<ArmorStand> SHOW_ARMS = new StatueStyleOption<>(StatueMenus.id("show_arms"),
            "ShowArms") {
        @Override
        public void setOption(ArmorStand armorStand, boolean value) {
            setArmorStandData(armorStand, value, ArmorStand.CLIENT_FLAG_SHOW_ARMS);
        }

        @Override
        public boolean getOption(ArmorStand armorStand) {
            return getArmorStandData(armorStand, ArmorStand.CLIENT_FLAG_SHOW_ARMS);
        }
    };
    public static final StatueStyleOption<ArmorStand> SMALL = new StatueStyleOption<>(StatueMenus.id("small"),
            "Small") {
        @Override
        public void setOption(ArmorStand armorStand, boolean value) {
            setArmorStandData(armorStand, value, ArmorStand.CLIENT_FLAG_SMALL);
        }

        @Override
        public boolean getOption(ArmorStand armorStand) {
            return getArmorStandData(armorStand, ArmorStand.CLIENT_FLAG_SMALL);
        }
    };
    public static final StatueStyleOption<LivingEntity> INVISIBLE = new StatueStyleOption<>(StatueMenus.id("invisible"),
            "Invisible") {
        @Override
        public void setOption(LivingEntity livingEntity, boolean value) {
            livingEntity.setInvisible(value);
        }

        @Override
        public boolean getOption(LivingEntity livingEntity) {
            return livingEntity.isInvisible();
        }
    };
    public static final StatueStyleOption<ArmorStand> NO_BASE_PLATE = new StatueStyleOption<>(StatueMenus.id(
            "no_base_plate"), "NoBasePlate") {
        @Override
        public void setOption(ArmorStand armorStand, boolean value) {
            setArmorStandData(armorStand, value, ArmorStand.CLIENT_FLAG_NO_BASEPLATE);
        }

        @Override
        public boolean getOption(ArmorStand armorStand) {
            return getArmorStandData(armorStand, ArmorStand.CLIENT_FLAG_NO_BASEPLATE);
        }
    };
    public static final StatueStyleOption<LivingEntity> NO_GRAVITY = new StatueStyleOption<>(StatueMenus.id("no_gravity"),
            "NoGravity") {
        @Override
        public void setOption(LivingEntity livingEntity, boolean value) {
            livingEntity.setNoGravity(value);
        }

        @Override
        public boolean getOption(LivingEntity livingEntity) {
            return livingEntity.isNoGravity();
        }
    };
    public static final StatueStyleOption<LivingEntity> SEALED = new StatueStyleOption<>(StatueMenus.id("sealed"),
            "Invulnerable") {
        private static final int ARMOR_STAND_ALL_SLOTS_DISABLED = 0b1111110011111100111111;

        @Override
        public void setOption(LivingEntity livingEntity, boolean value) {
            livingEntity.setInvulnerable(value);
            if (livingEntity instanceof ArmorStand armorStand) {
                armorStand.disabledSlots = value ? ARMOR_STAND_ALL_SLOTS_DISABLED : 0;
            }
        }

        @Override
        public boolean getOption(LivingEntity livingEntity) {
            return livingEntity.isInvulnerable();
        }

        @Override
        public void toTag(CompoundTag compoundTag, boolean currentValue) {
            super.toTag(compoundTag, currentValue);
            compoundTag.putInt("DisabledSlots", currentValue ? ARMOR_STAND_ALL_SLOTS_DISABLED : 0);
        }

        @Override
        public boolean mayEdit(Player player) {
            return player.getAbilities().instabuild;
        }
    };
    public static final List<StatueStyleOption<? super ArmorStand>> TYPES = List.of(SHOW_NAME,
            SHOW_ARMS,
            SMALL,
            INVISIBLE,
            NO_BASE_PLATE,
            NO_GRAVITY,
            SEALED);

    private final ResourceLocation id;
    private final String key;

    public StatueStyleOption(ResourceLocation id, String key) {
        this.id = id;
        this.key = key;
    }

    public ResourceLocation getName() {
        return this.id;
    }

    public String getTranslationKey() {
        return StatueScreenType.STYLE.id().toLanguageKey("screen", this.getName().toLanguageKey());
    }

    public String getDescriptionKey() {
        return this.getTranslationKey() + ".description";
    }

    public abstract void setOption(T livingEntity, boolean value);

    public abstract boolean getOption(T livingEntity);

    public void toTag(CompoundTag compoundTag, boolean currentValue) {
        compoundTag.putBoolean(this.key, currentValue);
    }

    public boolean mayEdit(Player player) {
        return true;
    }

    @Override
    public String getSerializedName() {
        return this.id.toString();
    }

    @Override
    public String toString() {
        return this.getSerializedName();
    }

    public static void register(StatueStyleOption<?> styleOption) {
        Objects.requireNonNull(styleOption, "style option is null");
        if (OPTIONS_REGISTRY.putIfAbsent(styleOption.getName(), styleOption) != null) {
            throw new IllegalStateException("Duplicate style option for: " + styleOption.getName());
        }
    }

    public static StatueStyleOption<?> get(ResourceLocation resourceLocation) {
        StatueStyleOption<?> styleOption = OPTIONS_REGISTRY.get(resourceLocation);
        Objects.requireNonNull(styleOption, "style option is null for: " + resourceLocation);
        return styleOption;
    }

    public static boolean getArmorStandData(ArmorStand armorStand, int offset) {
        return (armorStand.getEntityData().get(ArmorStand.DATA_CLIENT_FLAGS) & offset) != 0;
    }

    public static void setArmorStandData(ArmorStand armorStand, boolean value, int offset) {
        armorStand.getEntityData()
                .set(ArmorStand.DATA_CLIENT_FLAGS,
                        setBit(armorStand.getEntityData().get(ArmorStand.DATA_CLIENT_FLAGS), offset, value));
    }

    /**
     * @see ArmorStand#setBit(byte, int, boolean)
     */
    public static byte setBit(byte oldBit, int offset, boolean value) {
        if (value) {
            oldBit = (byte) (oldBit | offset);
        } else {
            oldBit = (byte) (oldBit & ~offset);
        }

        return oldBit;
    }
}
