package fuzs.statuemenus.api.v1.world.entity.decoration;

import net.minecraft.core.Rotations;
import net.minecraft.world.entity.decoration.ArmorStand;

@FunctionalInterface
public interface ArmorStandStatue extends StatueEntity {

    ArmorStand getArmorStand();

    @Override
    default Rotations getHeadPose() {
        return this.getArmorStand().getHeadPose();
    }

    @Override
    default Rotations getBodyPose() {
        return this.getArmorStand().getBodyPose();
    }

    @Override
    default Rotations getLeftArmPose() {
        return this.getArmorStand().getLeftArmPose();
    }

    @Override
    default Rotations getRightArmPose() {
        return this.getArmorStand().getRightArmPose();
    }

    @Override
    default Rotations getLeftLegPose() {
        return this.getArmorStand().getLeftLegPose();
    }

    @Override
    default Rotations getRightLegPose() {
        return this.getArmorStand().getRightLegPose();
    }

    @Override
    default void setHeadPose(Rotations headPose) {
        this.getArmorStand().setHeadPose(headPose);
    }

    @Override
    default void setBodyPose(Rotations bodyPose) {
        this.getArmorStand().setBodyPose(bodyPose);
    }

    @Override
    default void setLeftArmPose(Rotations leftArmPose) {
        this.getArmorStand().setLeftArmPose(leftArmPose);
    }

    @Override
    default void setRightArmPose(Rotations rightArmPose) {
        this.getArmorStand().setRightArmPose(rightArmPose);
    }

    @Override
    default void setLeftLegPose(Rotations leftLegPose) {
        this.getArmorStand().setLeftLegPose(leftLegPose);
    }

    @Override
    default void setRightLegPose(Rotations rightLegPose) {
        this.getArmorStand().setRightLegPose(rightLegPose);
    }
}
