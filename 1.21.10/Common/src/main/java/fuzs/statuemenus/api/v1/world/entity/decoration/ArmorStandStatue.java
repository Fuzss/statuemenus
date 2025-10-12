package fuzs.statuemenus.api.v1.world.entity.decoration;

import net.minecraft.core.Rotations;
import net.minecraft.world.entity.decoration.ArmorStand;

public class ArmorStandStatue implements StatueEntity {
    protected final ArmorStand armorStand;

    public ArmorStandStatue(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    @Override
    public Rotations getHeadPose() {
        return this.armorStand.getHeadPose();
    }

    @Override
    public Rotations getBodyPose() {
        return this.armorStand.getBodyPose();
    }

    @Override
    public Rotations getLeftArmPose() {
        return this.armorStand.getLeftArmPose();
    }

    @Override
    public Rotations getRightArmPose() {
        return this.armorStand.getRightArmPose();
    }

    @Override
    public Rotations getLeftLegPose() {
        return this.armorStand.getLeftLegPose();
    }

    @Override
    public Rotations getRightLegPose() {
        return this.armorStand.getRightLegPose();
    }

    @Override
    public void setHeadPose(Rotations headPose) {
        this.armorStand.setHeadPose(headPose);
    }

    @Override
    public void setBodyPose(Rotations bodyPose) {
        this.armorStand.setBodyPose(bodyPose);
    }

    @Override
    public void setLeftArmPose(Rotations leftArmPose) {
        this.armorStand.setLeftArmPose(leftArmPose);
    }

    @Override
    public void setRightArmPose(Rotations rightArmPose) {
        this.armorStand.setRightArmPose(rightArmPose);
    }

    @Override
    public void setLeftLegPose(Rotations leftLegPose) {
        this.armorStand.setLeftLegPose(leftLegPose);
    }

    @Override
    public void setRightLegPose(Rotations rightLegPose) {
        this.armorStand.setRightLegPose(rightLegPose);
    }
}
