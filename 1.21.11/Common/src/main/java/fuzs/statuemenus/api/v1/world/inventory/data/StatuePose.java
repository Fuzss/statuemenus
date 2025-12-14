package fuzs.statuemenus.api.v1.world.inventory.data;

import fuzs.statuemenus.api.v1.world.entity.decoration.StatueEntity;
import fuzs.statuemenus.impl.world.inventory.StatuePoseImpl;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface StatuePose {
    StatuePose EMPTY = new StatuePoseImpl(null, null, null, null, null, null);
    StatuePose ATHENA = StatuePoseImpl.ofMinecraft("athena")
            .withBodyPose(new Rotations(0.0F, 0.0F, 2.0F))
            .withHeadPose(new Rotations(-5.0F, 0.0F, 0.0F))
            .withLeftArmPose(new Rotations(10.0F, 0.0F, -5.0F))
            .withLeftLegPose(new Rotations(-3.0F, -3.0F, -3.0F))
            .withRightArmPose(new Rotations(-60.0F, 20.0F, -10.0F))
            .withRightLegPose(new Rotations(3.0F, 3.0F, 3.0F));
    StatuePose BRANDISH = StatuePoseImpl.ofMinecraft("brandish")
            .withBodyPose(new Rotations(0.0F, 0.0F, -2.0F))
            .withHeadPose(new Rotations(-15.0F, 0.0F, 0.0F))
            .withLeftArmPose(new Rotations(20.0F, 0.0F, -10.0F))
            .withLeftLegPose(new Rotations(5.0F, -3.0F, -3.0F))
            .withRightArmPose(new Rotations(-110.0F, 50.0F, 0.0F))
            .withRightLegPose(new Rotations(-5.0F, 3.0F, 3.0F));
    StatuePose CANCAN = StatuePoseImpl.ofMinecraft("cancan")
            .withBodyPose(new Rotations(0.0F, 22.0F, 0.0F))
            .withHeadPose(new Rotations(-5.0F, 18.0F, 0.0F))
            .withLeftArmPose(new Rotations(8.0F, 0.0F, -114.0F))
            .withLeftLegPose(new Rotations(-111.0F, 55.0F, 0.0F))
            .withRightArmPose(new Rotations(0.0F, 84.0F, 111.0F))
            .withRightLegPose(new Rotations(0.0F, 23.0F, -13.0F));
    StatuePose DEFAULT = StatuePoseImpl.ofMinecraft("default")
            .withLeftArmPose(new Rotations(-10.0F, 0.0F, -10.0F))
            .withLeftLegPose(new Rotations(-1.0F, 0.0F, -1.0F))
            .withRightArmPose(new Rotations(-15.0F, 0.0F, 10.0F))
            .withRightLegPose(new Rotations(1.0F, 0.0F, 1.0F));
    StatuePose ENTERTAIN = StatuePoseImpl.ofMinecraft("entertain")
            .withHeadPose(new Rotations(-15.0F, 0.0F, 0.0F))
            .withLeftArmPose(new Rotations(-110.0F, -35.0F, 0.0F))
            .withLeftLegPose(new Rotations(5.0F, -3.0F, -3.0F))
            .withRightArmPose(new Rotations(-110.0F, 35.0F, 0.0F))
            .withRightLegPose(new Rotations(-5.0F, 3.0F, 3.0F));
    StatuePose HERO = StatuePoseImpl.ofMinecraft("hero")
            .withBodyPose(new Rotations(0.0F, 8.0F, 0.0F))
            .withHeadPose(new Rotations(-4.0F, 67.0F, 0.0F))
            .withLeftArmPose(new Rotations(16.0F, 32.0F, -8.0F))
            .withLeftLegPose(new Rotations(0.0F, -75.0F, -8.0F))
            .withRightArmPose(new Rotations(-99.0F, 63.0F, 0.0F))
            .withRightLegPose(new Rotations(4.0F, 63.0F, 8.0F));
    StatuePose HONOR = StatuePoseImpl.ofMinecraft("honor")
            .withHeadPose(new Rotations(-15.0F, 0.0F, 0.0F))
            .withLeftArmPose(new Rotations(-110.0F, 35.0F, 0.0F))
            .withLeftLegPose(new Rotations(5.0F, -3.0F, -3.0F))
            .withRightArmPose(new Rotations(-110.0F, -35.0F, 0.0F))
            .withRightLegPose(new Rotations(-5.0F, 3.0F, 3.0F));
    StatuePose RIPOSTE = StatuePoseImpl.ofMinecraft("riposte")
            .withHeadPose(new Rotations(16.0F, 20.0F, 0.0F))
            .withLeftArmPose(new Rotations(4.0F, 8.0F, 237.0F))
            .withLeftLegPose(new Rotations(-14.0F, -18.0F, -16.0F))
            .withRightArmPose(new Rotations(246.0F, 0.0F, 89.0F))
            .withRightLegPose(new Rotations(8.0F, 20.0F, 4.0F));
    StatuePose SALUTE = StatuePoseImpl.ofMinecraft("salute")
            .withLeftArmPose(new Rotations(10.0F, 0.0F, -5.0F))
            .withLeftLegPose(new Rotations(-1.0F, 0.0F, -1.0F))
            .withRightArmPose(new Rotations(-70.0F, -40.0F, 0.0F))
            .withRightLegPose(new Rotations(1.0F, 0.0F, 1.0F));
    StatuePose SOLEMN = StatuePoseImpl.ofMinecraft("solemn")
            .withBodyPose(new Rotations(0.0F, 0.0F, 2.0F))
            .withHeadPose(new Rotations(15.0F, 0.0F, 0.0F))
            .withLeftArmPose(new Rotations(-30.0F, 15.0F, 15.0F))
            .withLeftLegPose(new Rotations(-1.0F, 0.0F, -1.0F))
            .withRightArmPose(new Rotations(-60.0F, -20.0F, -10.0F))
            .withRightLegPose(new Rotations(1.0F, 0.0F, 1.0F));
    StatuePose ZOMBIE = StatuePoseImpl.ofMinecraft("zombie")
            .withHeadPose(new Rotations(-10.0F, 0.0F, -5.0F))
            .withLeftArmPose(new Rotations(-105.0F, 0.0F, 0.0F))
            .withLeftLegPose(new Rotations(7.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(-100.0F, 0.0F, 0.0F))
            .withRightLegPose(new Rotations(-46.0F, 0.0F, 0.0F));
    StatuePose WALKING = StatuePoseImpl.ofVanillaTweaks("walking")
            .withRightArmPose(new Rotations(20.0F, 0.0F, 10.0F))
            .withLeftArmPose(new Rotations(-20.0F, 0.0F, -10.0F))
            .withRightLegPose(new Rotations(-20.0F, 0.0F, 0.0F))
            .withLeftLegPose(new Rotations(20.0F, 0.0F, 0.0F));
    StatuePose RUNNING = StatuePoseImpl.ofVanillaTweaks("running")
            .withRightArmPose(new Rotations(-40.0F, 0.0F, 10.0F))
            .withLeftArmPose(new Rotations(40.0F, 0.0F, -10.0F))
            .withRightLegPose(new Rotations(40.0F, 0.0F, 0.0F))
            .withLeftLegPose(new Rotations(-40.0F, 0.0F, 0.0F));
    StatuePose POINTING = StatuePoseImpl.ofVanillaTweaks("pointing")
            .withHeadPose(new Rotations(0.0F, 20.0F, 0.0F))
            .withRightArmPose(new Rotations(-90.0F, 18.0F, 0.0F))
            .withLeftArmPose(new Rotations(0.0F, 0.0F, -10.0F));
    StatuePose BLOCKING = StatuePoseImpl.ofVanillaTweaks("blocking")
            .withRightArmPose(new Rotations(-20.0F, -20.0F, 0.0F))
            .withLeftArmPose(new Rotations(-50.0F, 50.0F, 0.0F))
            .withRightLegPose(new Rotations(-20.0F, 0.0F, 0.0F))
            .withLeftLegPose(new Rotations(20.0F, 0.0F, 0.0F));
    StatuePose LUNGEING = StatuePoseImpl.ofVanillaTweaks("lungeing")
            .withBodyPose(new Rotations(15.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(-60.0F, -10.0F, 0.0F))
            .withLeftArmPose(new Rotations(10.0F, 0.0F, -10.0F))
            .withRightLegPose(new Rotations(-15.0F, 0.0F, 0.0F))
            .withLeftLegPose(new Rotations(30.0F, 0.0F, 0.0F));
    StatuePose WINNING = StatuePoseImpl.ofVanillaTweaks("winning")
            .withHeadPose(new Rotations(-15.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(-120.0F, -10.0F, 0.0F))
            .withLeftArmPose(new Rotations(10.0F, 0.0F, -10.0F))
            .withLeftLegPose(new Rotations(15.0F, 0.0F, 0.0F));
    StatuePose SITTING = StatuePoseImpl.ofVanillaTweaks("sitting")
            .withRightArmPose(new Rotations(-80.0F, 20.0F, 0.0F))
            .withLeftArmPose(new Rotations(-80.0F, -20.0F, 0.0F))
            .withRightLegPose(new Rotations(-90.0F, 10.0F, 0.0F))
            .withLeftLegPose(new Rotations(-90.0F, -10.0F, 0.0F));
    StatuePose ARABESQUE = StatuePoseImpl.ofVanillaTweaks("arabesque")
            .withHeadPose(new Rotations(-15.0F, 0.0F, 0.0F))
            .withBodyPose(new Rotations(10.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(-140.0F, -10.0F, 0.0F))
            .withLeftArmPose(new Rotations(70.0F, 0.0F, -10.0F))
            .withLeftLegPose(new Rotations(75.0F, 0.0F, 0.0F));
    StatuePose CUPID = StatuePoseImpl.ofVanillaTweaks("cupid")
            .withBodyPose(new Rotations(10.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(-90.0F, -10.0F, 0.0F))
            .withLeftArmPose(new Rotations(-75.0F, 0.0F, 10.0F))
            .withLeftLegPose(new Rotations(75.0F, 0.0F, 0.0F));
    StatuePose CONFIDENT = StatuePoseImpl.ofVanillaTweaks("confident")
            .withHeadPose(new Rotations(-10.0F, 20.0F, 0.0F))
            .withBodyPose(new Rotations(-2.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(5.0F, 0.0F, 0.0F))
            .withLeftArmPose(new Rotations(5.0F, 0.0F, 0.0F))
            .withRightLegPose(new Rotations(16.0F, 2.0F, 10.0F))
            .withLeftLegPose(new Rotations(0.0F, -10.0F, -4.0F));
    StatuePose DEATH = StatuePoseImpl.ofVanillaTweaks("death")
            .withHeadPose(new Rotations(-85.0F, 0.0F, 0.0F))
            .withBodyPose(new Rotations(-90.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(-90.0F, 10.0F, 0.0F))
            .withLeftArmPose(new Rotations(-90.0F, -10.0F, 0.0F));
    StatuePose FACEPALM = StatuePoseImpl.ofVanillaTweaks("facepalm")
            .withHeadPose(new Rotations(45.0F, -4.0F, 1.0F))
            .withBodyPose(new Rotations(10.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(18.0F, -14.0F, 0.0F))
            .withLeftArmPose(new Rotations(-72.0F, 24.0F, 47.0F))
            .withRightLegPose(new Rotations(25.0F, -2.0F, 0.0F))
            .withLeftLegPose(new Rotations(-4.0F, -6.0F, -2.0F));
    StatuePose LAZING = StatuePoseImpl.ofVanillaTweaks("lazing")
            .withHeadPose(new Rotations(14.0F, -12.0F, 6.0F))
            .withBodyPose(new Rotations(5.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(-40.0F, 20.0F, 0.0F))
            .withLeftArmPose(new Rotations(-4.0F, -20.0F, -10.0F))
            .withRightLegPose(new Rotations(-88.0F, 71.0F, 0.0F))
            .withLeftLegPose(new Rotations(-88.0F, 46.0F, 0.0F));
    StatuePose CONFUSED = StatuePoseImpl.ofVanillaTweaks("confused")
            .withHeadPose(new Rotations(0.0F, 30.0F, 0f))
            .withBodyPose(new Rotations(0.0F, 13.0F, 0.0F))
            .withRightArmPose(new Rotations(-22.0F, 31.0F, 10.0F))
            .withLeftArmPose(new Rotations(145.0F, 22.0F, -49.0F))
            .withRightLegPose(new Rotations(6.0F, -20.0F, 0.0F))
            .withLeftLegPose(new Rotations(-6.0F, 0.0F, 0.0F));
    StatuePose FORMAL = StatuePoseImpl.ofVanillaTweaks("formal")
            .withHeadPose(new Rotations(4.0F, 0.0F, 0.0F))
            .withBodyPose(new Rotations(4.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(30.0F, 22.0F, -20.0F))
            .withLeftArmPose(new Rotations(30.0F, -20.0F, 21.0F))
            .withRightLegPose(new Rotations(0.0F, 0.0F, 5.0F))
            .withLeftLegPose(new Rotations(0.0F, 0.0F, -5.0F));
    StatuePose SAD = StatuePoseImpl.ofVanillaTweaks("sad")
            .withHeadPose(new Rotations(63.0F, 0.0F, 0.0F))
            .withBodyPose(new Rotations(10.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(-5.0F, 0.0F, 5.0F))
            .withLeftArmPose(new Rotations(-5.0F, 0.0F, -5.0F))
            .withRightLegPose(new Rotations(-5.0F, -10.0F, 5.0F))
            .withLeftLegPose(new Rotations(-5.0F, 16.0F, -5.0F));
    StatuePose JOYOUS = StatuePoseImpl.ofVanillaTweaks("joyous")
            .withHeadPose(new Rotations(-11.0F, 0.0F, 0.0F))
            .withBodyPose(new Rotations(-4.0F, 0.0F, 0.0F))
            .withRightArmPose(new Rotations(0.0F, 0.0F, 100.0F))
            .withLeftArmPose(new Rotations(0.0F, 0.0F, -100.0F))
            .withRightLegPose(new Rotations(-8.0F, 0.0F, 60.0F))
            .withLeftLegPose(new Rotations(-8.0F, 0.0F, -60.0F));
    StatuePose STARGAZING = StatuePoseImpl.ofVanillaTweaks("stargazing")
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

    StatuePose withHeadPose(Rotations rotation);

    StatuePose withBodyPose(Rotations rotation);

    StatuePose withLeftArmPose(Rotations rotation);

    StatuePose withRightArmPose(Rotations rotation);

    StatuePose withLeftLegPose(Rotations rotation);

    StatuePose withRightLegPose(Rotations rotation);

    StatuePose mirror();

    StatuePose copyAndFillFrom(StatuePose fillFrom);

    void applyToEntity(StatueEntity statueEntity);

    void serializeAllPoses(CompoundTag compoundTag);

    void serializeBodyPoses(CompoundTag compoundTag, @Nullable StatuePose lastSentPose);

    void serializeArmPoses(CompoundTag compoundTag, @Nullable StatuePose lastSentPose);

    void serializeLegPoses(CompoundTag compoundTag, @Nullable StatuePose lastSentPose);

    boolean isVanillaTweaksCompatible();

    static StatuePose fromEntity(StatueEntity statueEntity) {
        return StatuePoseImpl.fromEntity(statueEntity);
    }

    static StatuePose randomize(List<PosePartMutator> mutators, boolean clampRotations) {
        return StatuePoseImpl.randomize(mutators, clampRotations);
    }

    static StatuePose randomValue() {
        return StatuePoseImpl.randomValue();
    }
}
