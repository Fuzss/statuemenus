package fuzs.statuemenus.api.v1.world.inventory.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public interface ArmorStandStyleOption {
    BiMap<ResourceLocation, ArmorStandStyleOption> OPTIONS_REGISTRY = HashBiMap.create();

    ResourceLocation getName();

    default String getTranslationKey() {
        return Util.makeDescriptionId("screen.style", this.getName());
    }

    default String getDescriptionKey() {
        return this.getTranslationKey() + ".description";
    }

    void setOption(ArmorStand armorStand, boolean setting);

    boolean getOption(ArmorStand armorStand);

    void toTag(CompoundTag compoundTag, boolean currentValue);

    default boolean allowChanges(Player player) {
        return true;
    }

    static void register(ArmorStandStyleOption styleOption) {
        Objects.requireNonNull(styleOption, "style option is null");
        if (OPTIONS_REGISTRY.putIfAbsent(styleOption.getName(), styleOption) != null) {
            throw new IllegalStateException("Duplicate style option for: " + styleOption.getName());
        }
    }

    static ArmorStandStyleOption get(ResourceLocation id) {
        ArmorStandStyleOption styleOption = OPTIONS_REGISTRY.get(id);
        Objects.requireNonNull(styleOption, "style option is null for: " + id);
        return styleOption;
    }

    static boolean getArmorStandData(ArmorStand armorStand, int offset) {
        return (armorStand.getEntityData().get(ArmorStand.DATA_CLIENT_FLAGS) & offset) != 0;
    }

    static void setArmorStandData(ArmorStand armorStand, boolean setting, int offset) {
        armorStand.getEntityData()
                .set(ArmorStand.DATA_CLIENT_FLAGS,
                        setBit(armorStand.getEntityData().get(ArmorStand.DATA_CLIENT_FLAGS), offset, setting));
    }

    static byte setBit(byte oldBit, int offset, boolean value) {
        if (value) {
            oldBit = (byte) (oldBit | offset);
        } else {
            oldBit = (byte) (oldBit & ~offset);
        }
        return oldBit;
    }
}
