package fuzs.statuemenus.api.v1.world.inventory.data;

import fuzs.statuemenus.impl.world.inventory.ArmorStandPoseImpl;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ArmorStandPose {
    ArmorStandPose EMPTY = new ArmorStandPoseImpl(null, null, null, null, null, null);
    ArmorStandPose ATHENA = ArmorStandPoseImpl.ofMinecraft("athena")
            .withBodyPose(new Rotations(0.0F, 0.0F, 2.0F))
            .withHeadPose(new Rotations(-5.0F, 0.0F, 0.0F))
            .withLeftArmPose(new Rotations(10.0F, 0.0F, -5.0F))
            .withLeftLegPose(new Rotations(-3.0F, -3.0F, -3.0F))
            .withRightArmPose(new Rotations(-60.0F, 20.0F, -10.0F))
            .withRightLegPose(new Rotations(3.0F, 3.0F, 3.0F));
    ArmorStandPose BRANDISH = ArmorStandPoseImpl.ofMinecraft("brandish")
            .withBodyPose(new Rotations(0.0F, 0.0F, -2.0F))
            .withHeadPose(new Rotations(-15.0F, 0.0F, 0.0F))
            .withLeftArmPose(new Rotations(20.0F, 0.0F, -10.0F))
            .withLeftLegPose(new Rotations(5.0F, -3.0F, -3.0F))
            .withRightArmPose(new Rotations(-110.0F, 50.0F, 0.0F))
            .withRightLegPose(new Rotations(-5.0F, 3.0F, 3.0F));
    ArmorStandPose CANCAN = ArmorStandPoseImpl.ofMinecraft("cancan")
            .withBodyPose(new Rotations(0.0F, 22.0F, 0.0F))
            .withHeadPose(new Rotations(-5.0F, 18.0F, 0.0F))
            .withLeftArmPose(new Rotations(8.0F, 0.0F, -114.0F))
            .withLeftLegPose(new Rotations(-111.0F, 55.0F, 0.0F))
            .withRightArmPose(new Rotations(0.0F, 84.0F, 111.0F))
            .withRightLegPose(new Rotations(0.0F, 23.0F, -13.0F));
    ArmorStandPose DEFAULT = ArmorStandPoseImpl.ofMinecraft("default")
            .withLeftArmPose(new Rotations(-10.0F, 0.0F, -10.0F))
            .withLeftLegPose(new Rotations(-1.0F, 0.0F, -1.0F))
            .withRightArmPose(new Rotations(-15.0F, 0.0F, 10.0F))
            .withRightLegPose(new Rotations(1.0F, 0.0F, 1.0F));
    ArmorStandPose ENTERTAIN = ArmorStandPoseImpl.ofMinecraft("entertain")
            .withHeadPose(new Rotations(-15.0F, 0.0F, 0.0F))
            .withLeftArmPose(new Rotations(-110.0F, -35.0F, 0.0F))
            .withLeftLegPose(new Rotations(5.0F, -3.0F, -3.0F))
            .withRightArmPose(new Rotations(-110.0F, 35.0F, 0.0F))
            .withRightLegPose(new Rotations(-5.0F, 3.0F, 3.0F));
    ArmorStandPose HERO = ArmorStandPoseImpl.ofMinecraft("hero")
            .withBodyPose(new Rotations(0.0F, 8.0F, 0.0F))
            .withHeadPose(new Rotations(-4.0F, 67.0F, 0.0F))
            .withLeftArmPose(new Rotations(16.0F, 32.0F, -8.0F))
            .withLeftLegPose(new Rotations(0.0F, -75.0F, -8.0F))
            .withRightArmPose(new Rotations(-99.0F, 63.0F, 0.0F))
            .withRightLegPose(new Rotations(4.0F, 63.0F, 8.0F));
    ArmorStandPose HONOR = ArmorStandPoseImpl.ofMinecraft("honor")
            .withHeadPose(new Rotations(-15.0F, 0.0F, 0.0F))
            .withLeftArmPose(new Rotations(-110.0F, 35.0F, 0.0F))
            .withLeftLegPose(new Rotations(5.0F, -3.0F, -3.0F))
            .withRightArmPose(new Rotations(-110.0F, -35.0F, 0.0F))
            .withRightLegPose(new Rotations(-5.0F, 3.0F, 3.0F));
    ArmorStandPose RIPOSTE = ArmorStandPoseImpl.ofMinecraft("riposte")
            .withHeadPose(new Rotations(16.0F, 20.0F, 0.0F))
            .withLeftArmPose(new Rotations(4.0F, 8.0F, 237.0F))
            .withLeftLegPose(new Rotations(-14.0F, -18.0F, -16.0F))
            .withRightArmPose(new Rotations(246.0F, 0.0F, 89.0F))
            .withRightLegPose(new Rotations(8.0F, 20.0F, 4.0F));
    ArmorStandPose SALUTE = ArmorStandPoseImpl.ofMinecraft("salute")
            .withLeftArmPose(new Rotations(10.0F, 0.0F, -5.0F))
            .withLeftLegPose(new Rotations(-1.0F, 0.0F, -1.0F))
            .withRightArmPose(new Rotations(-70.0F, -40.0F, 0.0F))
            .withRightLegPose(new Rotations(1.0F, 0.0F, 1.0F));
    ArmorStandPose SOLEMN = ArmorStandPoseImpl.ofMinecraft("solemn")
            .withBodyPose(new Rotations(0.0F, 0.0F, 2.0F))
            .withHeadPose(new Rotations(15.0F, 0.0F, 0.0F))
            .withLeftArmPose(new Rotations(-30.0F, 15.0F, 15.0F))
            .withLeftLegPose(new Rotations(-1.0F, 0.0F, -1.0F))
            .withRightArmPose(new Rotations(-60.0F, -20.0F, -10.0F))
            .withRightLegPose(new Rotations(1.0F, 0.0F, 1.0F));
    ArmorStandPose ZOMBIE = ArmorStandPoseImpl.ofMinecraft("zombie")
            .withHeadPose(new Rotations(-10.0F, 0.0F, -5.0F))
            .withLeftArmPose(new Rotations(-105.0F, 0.0F, 0.0F))
            .withLeftLegPose(new Rotations(7.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(-100.0F, 0.0F, 0.0F))
            .withRightLegPose(new Rotations(-46.0F, 0.0F, 0.0F));
    ArmorStandPose WALKING = ArmorStandPoseImpl.ofVanillaTweaks("walking")
            .withRightArmPose(new Rotations(20.0F, 0.0F, 10.0F))
            .withLeftArmPose(new Rotations(-20.0F, 0.0F, -10.0F))
            .withRightLegPose(new Rotations(-20.0F, 0.0F, 0.0F))
            .withLeftLegPose(new Rotations(20.0F, 0.0F, 0.0F));
    ArmorStandPose RUNNING = ArmorStandPoseImpl.ofVanillaTweaks("running")
            .withRightArmPose(new Rotations(-40.0F, 0.0F, 10.0F))
            .withLeftArmPose(new Rotations(40.0F, 0.0F, -10.0F))
            .withRightLegPose(new Rotations(40.0F, 0.0F, 0.0F))
            .withLeftLegPose(new Rotations(-40.0F, 0.0F, 0.0F));
    ArmorStandPose POINTING = ArmorStandPoseImpl.ofVanillaTweaks("pointing")
            .withHeadPose(new Rotations(0.0F, 20.0F, 0.0F))
            .withRightArmPose(new Rotations(-90.0F, 18.0F, 0.0F))
            .withLeftArmPose(new Rotations(0.0F, 0.0F, -10.0F));
    ArmorStandPose BLOCKING = ArmorStandPoseImpl.ofVanillaTweaks("blocking")
            .withRightArmPose(new Rotations(-20.0F, -20.0F, 0.0F))
            .withLeftArmPose(new Rotations(-50.0F, 50.0F, 0.0F))
            .withRightLegPose(new Rotations(-20.0F, 0.0F, 0.0F))
            .withLeftLegPose(new Rotations(20.0F, 0.0F, 0.0F));
    ArmorStandPose LUNGEING = ArmorStandPoseImpl.ofVanillaTweaks("lungeing")
            .withBodyPose(new Rotations(15.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(-60.0F, -10.0F, 0.0F))
            .withLeftArmPose(new Rotations(10.0F, 0.0F, -10.0F))
            .withRightLegPose(new Rotations(-15.0F, 0.0F, 0.0F))
            .withLeftLegPose(new Rotations(30.0F, 0.0F, 0.0F));
    ArmorStandPose WINNING = ArmorStandPoseImpl.ofVanillaTweaks("winning")
            .withHeadPose(new Rotations(-15.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(-120.0F, -10.0F, 0.0F))
            .withLeftArmPose(new Rotations(10.0F, 0.0F, -10.0F))
            .withLeftLegPose(new Rotations(15.0F, 0.0F, 0.0F));
    ArmorStandPose SITTING = ArmorStandPoseImpl.ofVanillaTweaks("sitting")
            .withRightArmPose(new Rotations(-80.0F, 20.0F, 0.0F))
            .withLeftArmPose(new Rotations(-80.0F, -20.0F, 0.0F))
            .withRightLegPose(new Rotations(-90.0F, 10.0F, 0.0F))
            .withLeftLegPose(new Rotations(-90.0F, -10.0F, 0.0F));
    ArmorStandPose ARABESQUE = ArmorStandPoseImpl.ofVanillaTweaks("arabesque")
            .withHeadPose(new Rotations(-15.0F, 0.0F, 0.0F))
            .withBodyPose(new Rotations(10.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(-140.0F, -10.0F, 0.0F))
            .withLeftArmPose(new Rotations(70.0F, 0.0F, -10.0F))
            .withLeftLegPose(new Rotations(75.0F, 0.0F, 0.0F));
    ArmorStandPose CUPID = ArmorStandPoseImpl.ofVanillaTweaks("cupid")
            .withBodyPose(new Rotations(10.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(-90.0F, -10.0F, 0.0F))
            .withLeftArmPose(new Rotations(-75.0F, 0.0F, 10.0F))
            .withLeftLegPose(new Rotations(75.0F, 0.0F, 0.0F));
    ArmorStandPose CONFIDENT = ArmorStandPoseImpl.ofVanillaTweaks("confident")
            .withHeadPose(new Rotations(-10.0F, 20.0F, 0.0F))
            .withBodyPose(new Rotations(-2.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(5.0F, 0.0F, 0.0F))
            .withLeftArmPose(new Rotations(5.0F, 0.0F, 0.0F))
            .withRightLegPose(new Rotations(16.0F, 2.0F, 10.0F))
            .withLeftLegPose(new Rotations(0.0F, -10.0F, -4.0F));
    ArmorStandPose DEATH = ArmorStandPoseImpl.ofVanillaTweaks("death")
            .withHeadPose(new Rotations(-85.0F, 0.0F, 0.0F))
            .withBodyPose(new Rotations(-90.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(-90.0F, 10.0F, 0.0F))
            .withLeftArmPose(new Rotations(-90.0F, -10.0F, 0.0F));
    ArmorStandPose FACEPALM = ArmorStandPoseImpl.ofVanillaTweaks("facepalm")
            .withHeadPose(new Rotations(45.0F, -4.0F, 1.0F))
            .withBodyPose(new Rotations(10.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(18.0F, -14.0F, 0.0F))
            .withLeftArmPose(new Rotations(-72.0F, 24.0F, 47.0F))
            .withRightLegPose(new Rotations(25.0F, -2.0F, 0.0F))
            .withLeftLegPose(new Rotations(-4.0F, -6.0F, -2.0F));
    ArmorStandPose LAZING = ArmorStandPoseImpl.ofVanillaTweaks("lazing")
            .withHeadPose(new Rotations(14.0F, -12.0F, 6.0F))
            .withBodyPose(new Rotations(5.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(-40.0F, 20.0F, 0.0F))
            .withLeftArmPose(new Rotations(-4.0F, -20.0F, -10.0F))
            .withRightLegPose(new Rotations(-88.0F, 71.0F, 0.0F))
            .withLeftLegPose(new Rotations(-88.0F, 46.0F, 0.0F));
    ArmorStandPose CONFUSED = ArmorStandPoseImpl.ofVanillaTweaks("confused")
            .withHeadPose(new Rotations(0.0F, 30.0F, 0f))
            .withBodyPose(new Rotations(0.0F, 13.0F, 0.0F))
            .withRightArmPose(new Rotations(-22.0F, 31.0F, 10.0F))
            .withLeftArmPose(new Rotations(145.0F, 22.0F, -49.0F))
            .withRightLegPose(new Rotations(6.0F, -20.0F, 0.0F))
            .withLeftLegPose(new Rotations(-6.0F, 0.0F, 0.0F));
    ArmorStandPose FORMAL = ArmorStandPoseImpl.ofVanillaTweaks("formal")
            .withHeadPose(new Rotations(4.0F, 0.0F, 0.0F))
            .withBodyPose(new Rotations(4.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(30.0F, 22.0F, -20.0F))
            .withLeftArmPose(new Rotations(30.0F, -20.0F, 21.0F))
            .withRightLegPose(new Rotations(0.0F, 0.0F, 5.0F))
            .withLeftLegPose(new Rotations(0.0F, 0.0F, -5.0F));
    ArmorStandPose SAD = ArmorStandPoseImpl.ofVanillaTweaks("sad")
            .withHeadPose(new Rotations(63.0F, 0.0F, 0.0F))
            .withBodyPose(new Rotations(10.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(-5.0F, 0.0F, 5.0F))
            .withLeftArmPose(new Rotations(-5.0F, 0.0F, -5.0F))
            .withRightLegPose(new Rotations(-5.0F, -10.0F, 5.0F))
            .withLeftLegPose(new Rotations(-5.0F, 16.0F, -5.0F));
    ArmorStandPose JOYOUS = ArmorStandPoseImpl.ofVanillaTweaks("joyous")
            .withHeadPose(new Rotations(-11.0F, 0.0F, 0.0F))
            .withBodyPose(new Rotations(-4.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(0.0F, 0.0F, 100.0F))
            .withLeftArmPose(new Rotations(0.0F, 0.0F, -100.0F))
            .withRightLegPose(new Rotations(-8.0F, 0.0F, 60.0F))
            .withLeftLegPose(new Rotations(-8.0F, 0.0F, -60.0F));
    ArmorStandPose STARGAZING = ArmorStandPoseImpl.ofVanillaTweaks("stargazing")
            .withHeadPose(new Rotations(-22.0F, 25.0F, 0.0F))
            .withBodyPose(new Rotations(-4.0F, 10.0F, 0.0F))
            .withRightArmPose(new Rotations(-153.0F, 34.0F, -3.0F))
            .withLeftArmPose(new Rotations(4.0F, 18.0F, 0.0F))
            .withRightLegPose(new Rotations(-4.0F, 17.0F, 2.0F))
            .withLeftLegPose(new Rotations(6.0F, 24.0F, 0.0F));

    default boolean isEmpty() {
        return this.headPose() == null && this.bodyPose() == null && this.leftArmPose() == null
                && this.rightArmPose() == null && this.leftLegPose() == null && this.rightLegPose() == null;
    }

    @Nullable String getTranslationKey();

    List<Component> getTooltipLines();

    boolean isMirrored();

    @Nullable Rotations headPose();

    @Nullable Rotations bodyPose();

    @Nullable Rotations leftArmPose();

    @Nullable Rotations rightArmPose();

    @Nullable Rotations leftLegPose();

    @Nullable Rotations rightLegPose();

    Rotations getHeadPose();

    Rotations getBodyPose();

    Rotations getLeftArmPose();

    Rotations getRightArmPose();

    Rotations getLeftLegPose();

    Rotations getRightLegPose();

    ArmorStandPose withHeadPose(Rotations rotation);

    ArmorStandPose withBodyPose(Rotations rotation);

    ArmorStandPose withLeftArmPose(Rotations rotation);

    ArmorStandPose withRightArmPose(Rotations rotation);

    ArmorStandPose withLeftLegPose(Rotations rotation);

    ArmorStandPose withRightLegPose(Rotations rotation);

    ArmorStandPose mirror();

    ArmorStandPose copyAndFillFrom(ArmorStandPose fillFrom);

    void applyToEntity(ArmorStand armorStand);

    void serializeAllPoses(CompoundTag compoundTag);

    void serializeBodyPoses(CompoundTag compoundTag, @Nullable ArmorStandPose lastSentPose);

    void serializeArmPoses(CompoundTag compoundTag, @Nullable ArmorStandPose lastSentPose);

    void serializeLegPoses(CompoundTag compoundTag, @Nullable ArmorStandPose lastSentPose);

    boolean isVanillaTweaksCompatible();

    static ArmorStandPose fromEntity(ArmorStand armorStand) {
        return ArmorStandPoseImpl.fromEntity(armorStand);
    }

    static ArmorStandPose randomize(PosePartMutator[] mutators, boolean clampRotations) {
        return ArmorStandPoseImpl.randomize(mutators, clampRotations);
    }

    static ArmorStandPose randomValue() {
        return ArmorStandPoseImpl.randomValue();
    }
}
