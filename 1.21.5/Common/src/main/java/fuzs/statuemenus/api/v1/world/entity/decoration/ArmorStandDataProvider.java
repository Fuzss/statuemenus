package fuzs.statuemenus.api.v1.world.entity.decoration;

import com.google.common.util.concurrent.Runnables;
import fuzs.statuemenus.api.v1.world.inventory.data.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Rotations;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface ArmorStandDataProvider {
    ArmorStandDataProvider INSTANCE = new ArmorStandDataProvider() {
        // NO-OP
    };

    default ArmorStandScreenType[] getScreenTypes() {
        return new ArmorStandScreenType[]{
                ArmorStandScreenType.ROTATIONS,
                ArmorStandScreenType.POSES,
                ArmorStandScreenType.STYLE,
                ArmorStandScreenType.POSITION,
                ArmorStandScreenType.EQUIPMENT
        };
    }

    default ArmorStandScreenType getDefaultScreenType() {
        return ArmorStandScreenType.ROTATIONS;
    }

    default PosePartMutator[] getPosePartMutators() {
        return new PosePartMutator[]{
                PosePartMutator.HEAD,
                PosePartMutator.BODY,
                PosePartMutator.RIGHT_ARM,
                PosePartMutator.LEFT_ARM,
                PosePartMutator.RIGHT_LEG,
                PosePartMutator.LEFT_LEG
        };
    }

    default ArmorStandPose getRandomPose(boolean clampRotations) {
        return ArmorStandPose.randomize(this.getPosePartMutators(), clampRotations)
                .withBodyPose(new Rotations(0.0F, 0.0F, 0.0F));
    }

    default ArmorStandStyleOption[] getStyleOptions() {
        return ArmorStandStyleOptions.values();
    }

    /**
     * Prepares an armor stand entity for rendering via
     * {@link net.minecraft.client.gui.screens.inventory.InventoryScreen#renderEntityInInventory(GuiGraphics, float,
     * float, float, Vector3f, Quaternionf, Quaternionf, LivingEntity)}, the returned runnable is used for resetting the
     * entity to the original state after rendering is done.
     *
     * @param armorStand the armor stand
     * @return the runnable for resetting the entity
     */
    default Runnable setupInInventoryRendering(ArmorStand armorStand) {
        return Runnables.doNothing();
    }
}
