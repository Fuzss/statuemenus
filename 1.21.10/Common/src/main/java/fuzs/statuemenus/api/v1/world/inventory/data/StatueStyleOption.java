package fuzs.statuemenus.api.v1.world.inventory.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import fuzs.statuemenus.api.v1.world.entity.decoration.ArmorStandStatue;
import fuzs.statuemenus.impl.StatueMenus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class StatueStyleOption<T extends LivingEntity> implements StringRepresentable {
    private static final BiMap<ResourceLocation, StatueStyleOption<?>> OPTIONS_REGISTRY = HashBiMap.create();
    public static final StatueStyleOption<LivingEntity> SHOW_NAME = create(StatueMenus.id("show_name"),
            "CustomNameVisible",
            Entity::setCustomNameVisible,
            Entity::isCustomNameVisible);
    public static final StatueStyleOption<ArmorStand> SHOW_ARMS = create(StatueMenus.id("show_arms"),
            "ShowArms",
            ArmorStand.CLIENT_FLAG_SHOW_ARMS);
    public static final StatueStyleOption<ArmorStand> SMALL = create(StatueMenus.id("small"),
            "Small",
            ArmorStand.CLIENT_FLAG_SMALL);
    public static final StatueStyleOption<LivingEntity> INVISIBLE = create(StatueMenus.id("invisible"),
            "Invisible",
            Entity::setInvisible,
            Entity::isInvisible);
    public static final StatueStyleOption<ArmorStand> NO_BASE_PLATE = create(StatueMenus.id("no_base_plate"),
            "NoBasePlate",
            ArmorStand.CLIENT_FLAG_NO_BASEPLATE);
    public static final StatueStyleOption<LivingEntity> NO_GRAVITY = create(StatueMenus.id("no_gravity"),
            "NoGravity",
            Entity::setNoGravity,
            Entity::isNoGravity);
    public static final StatueStyleOption<LivingEntity> INVULNERABLE = create(StatueMenus.id("invulnerable"),
            "Invulnerable",
            Entity::setInvulnerable,
            Entity::isInvulnerable);
    public static final StatueStyleOption<ArmorStand> SEALED = new StatueStyleOption<>(StatueMenus.id("sealed")) {
        @Override
        public void setOption(ArmorStand armorStand, boolean value) {
            armorStand.disabledSlots = value ? ArmorStandStatue.ALL_SLOTS_DISABLED : 0;
        }

        @Override
        public boolean getOption(ArmorStand armorStand) {
            return armorStand.disabledSlots == ArmorStandStatue.ALL_SLOTS_DISABLED;
        }

        @Override
        public void toTag(CompoundTag compoundTag, boolean currentValue) {
            compoundTag.putInt("DisabledSlots", currentValue ? ArmorStandStatue.ALL_SLOTS_DISABLED : 0);
        }

        @Override
        public boolean mayEdit(Player player) {
            return player.getAbilities().instabuild;
        }
    };
    public static final List<StatueStyleOption<? super ArmorStand>> TYPES = List.of(SHOW_ARMS,
            SMALL,
            INVISIBLE,
            NO_BASE_PLATE,
            NO_GRAVITY,
            INVULNERABLE,
            SEALED);

    private final ResourceLocation id;
    @Nullable
    private final String key;

    public StatueStyleOption(ResourceLocation id) {
        this(id, null);
    }

    public StatueStyleOption(ResourceLocation id, @Nullable String key) {
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
        if (this.key != null) {
            compoundTag.putBoolean(this.key, currentValue);
        }
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

    public static <T extends LivingEntity> StatueStyleOption<T> create(ResourceLocation id, BiConsumer<T, Boolean> valueSetter, Function<T, Boolean> valueGetter) {
        return create(id, null, valueSetter, valueGetter);
    }

    public static <T extends LivingEntity> StatueStyleOption<T> create(ResourceLocation id, @Nullable String key, BiConsumer<T, Boolean> valueSetter, Function<T, Boolean> valueGetter) {
        return new StatueStyleOption<>(id, key) {
            @Override
            public void setOption(T livingEntity, boolean value) {
                valueSetter.accept(livingEntity, value);
            }

            @Override
            public boolean getOption(T livingEntity) {
                return valueGetter.apply(livingEntity);
            }
        };
    }

    public static StatueStyleOption<ArmorStand> create(ResourceLocation id, String key, int offset) {
        return new StatueStyleOption<>(id, key) {
            @Override
            public void setOption(ArmorStand armorStand, boolean value) {
                setArmorStandClientFlag(armorStand, value, offset);
            }

            @Override
            public boolean getOption(ArmorStand armorStand) {
                return getArmorStandClientFlag(armorStand, offset);
            }
        };
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

    public static boolean getArmorStandClientFlag(ArmorStand armorStand, int offset) {
        return (armorStand.getEntityData().get(ArmorStand.DATA_CLIENT_FLAGS) & offset) != 0;
    }

    public static void setArmorStandClientFlag(ArmorStand armorStand, boolean value, int offset) {
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

    public static void bootstrap() {
        register(SHOW_NAME);
        register(SHOW_ARMS);
        register(SMALL);
        register(INVISIBLE);
        register(NO_BASE_PLATE);
        register(NO_GRAVITY);
        register(INVULNERABLE);
        register(SEALED);
    }
}
