package fuzs.statuemenus.api.v1.world.entity.decoration;

import com.google.common.util.concurrent.Runnables;
import fuzs.statuemenus.api.v1.client.gui.screens.StatueScreen;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueScreenType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Rotations;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

import java.util.List;

public interface StatueEntity {

    default List<StatueScreenType> getScreenTypes() {
        return List.of(StatueScreenType.ROTATIONS,
                StatueScreenType.POSES,
                StatueScreenType.STYLE,
                StatueScreenType.POSITION,
                StatueScreenType.EQUIPMENT);
    }

    default StatueScreenType getDefaultScreenType() {
        return StatueScreenType.ROTATIONS;
    }

    Rotations getHeadPose();

    Rotations getBodyPose();

    Rotations getLeftArmPose();

    Rotations getRightArmPose();

    Rotations getLeftLegPose();

    Rotations getRightLegPose();

    void setHeadPose(Rotations headPose);

    void setBodyPose(Rotations bodyPose);

    void setLeftArmPose(Rotations leftArmPose);

    void setRightArmPose(Rotations rightArmPose);

    void setLeftLegPose(Rotations leftLegPose);

    void setRightLegPose(Rotations rightLegPose);

    default void setArmorStandPose(ArmorStand.ArmorStandPose armorStandPose) {
        this.setHeadPose(armorStandPose.head());
        this.setBodyPose(armorStandPose.body());
        this.setLeftArmPose(armorStandPose.leftArm());
        this.setRightArmPose(armorStandPose.rightArm());
        this.setLeftLegPose(armorStandPose.leftLeg());
        this.setRightLegPose(armorStandPose.rightLeg());
    }

    /**
     * Prepares the entity for rendering via
     * {@link StatueScreen#renderArmorStandInInventory(GuiGraphics, int,
     * int, int, int, int, float, float, float)}.
     * <p>
     * The returned runnable is used for resetting the entity to the original state after rendering is done.
     *
     * @param livingEntity the entity
     * @return the reset action for the entity
     */
    default Runnable setupInInventoryRendering(LivingEntity livingEntity) {
        return Runnables.doNothing();
    }
}
