package fuzs.statuemenus.api.v1.network.client.data;

import fuzs.statuemenus.api.v1.world.inventory.StatueHolder;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueAlignment;
import fuzs.statuemenus.api.v1.world.inventory.data.StatuePose;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueScreenType;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueStyleOption;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface DataSyncHandler {

    StatueHolder getArmorStandHolder();

    default LivingEntity getEntity() {
        return this.getArmorStandHolder().getEntity();
    }

    void sendName(String name);

    void sendPose(StatuePose pose, boolean finalize);

    default void sendPose(StatuePose pose) {
        this.sendPose(pose, true);
    }

    @Nullable
    default StatuePose getLastSyncedPose() {
        return null;
    }

    void sendPosition(double posX, double posY, double posZ, boolean finalize);

    default void sendPosition(double posX, double posY, double posZ) {
        this.sendPosition(posX, posY, posZ, true);
    }

    void sendScale(float scale, boolean finalize);

    default void sendScale(float scale) {
        this.sendScale(scale, true);
    }

    void sendRotation(float rotation, boolean finalize);

    default void sendRotation(float rotation) {
        this.sendRotation(rotation, true);
    }

    void sendStyleOption(StatueStyleOption<?> styleOption, boolean value, boolean finalize);

    default void sendStyleOption(StatueStyleOption<?> styleOption, boolean value) {
        this.sendStyleOption(styleOption, value, true);
    }

    default void sendAlignment(StatueAlignment alignment) {
        if (!this.getEntity().isInvisible()) {
            this.sendStyleOption(StatueStyleOption.INVISIBLE, true, false);
        }

        if (!this.getEntity().isNoGravity()) {
            this.sendStyleOption(StatueStyleOption.NO_GRAVITY, true, false);
        }

        this.sendPose(alignment.getPose(), false);
        Vec3 alignmentOffset = alignment.getAlignmentOffset(this.getEntity().isBaby());
        Vec3 newPosition = getLocalPosition(this.getEntity(), alignmentOffset);
        this.sendPosition(newPosition.x(), newPosition.y(), newPosition.z(), false);
        this.finalizeCurrentOperation();
    }

    /**
     * @see net.minecraft.commands.arguments.coordinates.LocalCoordinates#getPosition(CommandSourceStack)
     */
    private static Vec3 getLocalPosition(Entity entity, Vec3 offset) {
        Vec2 vec2 = entity.getRotationVector();
        Vec3 vec3 = entity.position();
        float f = Mth.cos((vec2.y + 90.0F) * 0.017453292F);
        float g = Mth.sin((vec2.y + 90.0F) * 0.017453292F);
        float h = Mth.cos(-vec2.x * 0.017453292F);
        float i = Mth.sin(-vec2.x * 0.017453292F);
        float j = Mth.cos((-vec2.x + 90.0F) * 0.017453292F);
        float k = Mth.sin((-vec2.x + 90.0F) * 0.017453292F);
        Vec3 vec32 = new Vec3(f * h, i, g * h);
        Vec3 vec33 = new Vec3(f * j, k, g * j);
        Vec3 vec34 = vec32.cross(vec33).scale(-1.0);
        double d = vec32.x * offset.z() + vec33.x * offset.y() + vec34.x * offset.x();
        double e = vec32.y * offset.z() + vec33.y * offset.y() + vec34.y * offset.x();
        double l = vec32.z * offset.z() + vec33.z * offset.y() + vec34.z * offset.x();
        return new Vec3(vec3.x + d, vec3.y + e, vec3.z + l);
    }

    default StatueScreenType[] getScreenTypes() {
        return this.getArmorStandHolder()
                .getStatueEntity()
                .getScreenTypes()
                .stream()
                .filter(this::supportsScreenType)
                .toArray(StatueScreenType[]::new);
    }

    default boolean supportsScreenType(StatueScreenType screenType) {
        return true;
    }

    default void tick() {
        // NO-OP
    }

    default boolean shouldContinueTicking() {
        return false;
    }

    default void finalizeCurrentOperation() {
        // NO-OP
    }

    static void setCustomArmorStandName(LivingEntity livingEntity, String name) {
        name = StringUtil.filterText(name);
        if (name.length() <= 50) {
            boolean remove =
                    name.isBlank() || Objects.equals(name, livingEntity.getType().getDescription().getString());
            livingEntity.setCustomName(remove ? null : Component.literal(name));
        }
    }
}
