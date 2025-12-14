package fuzs.statuemenus.api.v1.world.entity.decoration;

import net.minecraft.core.Rotations;
import net.minecraft.world.entity.decoration.ArmorStand;

@FunctionalInterface
public interface ArmorStandStatue extends StatueEntity {
    int ALL_SLOTS_DISABLED = 0b1111110011111100111111;

    ArmorStand armorStand();

    @Override
    default Rotations getHeadPose() {
        return this.armorStand().getHeadPose();
    }

    @Override
    default Rotations getBodyPose() {
        return this.armorStand().getBodyPose();
    }

    @Override
    default Rotations getLeftArmPose() {
        return this.armorStand().getLeftArmPose();
    }

    @Override
    default Rotations getRightArmPose() {
        return this.armorStand().getRightArmPose();
    }

    @Override
    default Rotations getLeftLegPose() {
        return this.armorStand().getLeftLegPose();
    }

    @Override
    default Rotations getRightLegPose() {
        return this.armorStand().getRightLegPose();
    }

    @Override
    default void setHeadPose(Rotations headPose) {
        this.armorStand().setHeadPose(headPose);
    }

    @Override
    default void setBodyPose(Rotations bodyPose) {
        this.armorStand().setBodyPose(bodyPose);
    }

    @Override
    default void setLeftArmPose(Rotations leftArmPose) {
        this.armorStand().setLeftArmPose(leftArmPose);
    }

    @Override
    default void setRightArmPose(Rotations rightArmPose) {
        this.armorStand().setRightArmPose(rightArmPose);
    }

    @Override
    default void setLeftLegPose(Rotations leftLegPose) {
        this.armorStand().setLeftLegPose(leftLegPose);
    }

    @Override
    default void setRightLegPose(Rotations rightLegPose) {
        this.armorStand().setRightLegPose(rightLegPose);
    }

    @Override
    default boolean isSealed() {
        return this.armorStand().disabledSlots == ALL_SLOTS_DISABLED;
    }
}
