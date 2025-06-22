package fuzs.statuemenus.impl.world.inventory;

import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandPose;
import fuzs.statuemenus.api.v1.world.inventory.data.PosePartMutator;
import fuzs.statuemenus.impl.StatueMenus;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record ArmorStandPoseImpl(@Nullable ResourceLocation name,
                                 @Nullable SourceType sourceType,
                                 @Nullable Rotations headPose,
                                 @Nullable Rotations bodyPose,
                                 @Nullable Rotations leftArmPose,
                                 @Nullable Rotations rightArmPose,
                                 @Nullable Rotations leftLegPose,
                                 @Nullable Rotations rightLegPose) implements ArmorStandPose {
    private static final Rotations ZERO_ROTATIONS = new Rotations(0.0F, 0.0F, 0.0F);

    private ArmorStandPoseImpl(String name, SourceType sourceType) {
        this(sourceType.id(name),
                sourceType,
                ZERO_ROTATIONS,
                ZERO_ROTATIONS,
                ZERO_ROTATIONS,
                ZERO_ROTATIONS,
                ZERO_ROTATIONS,
                ZERO_ROTATIONS);
    }

    public ArmorStandPoseImpl(@Nullable Rotations headPose, @Nullable Rotations bodyPose, @Nullable Rotations leftArmPose, @Nullable Rotations rightArmPose, @Nullable Rotations leftLegPose, @Nullable Rotations rightLegPose) {
        this(null, null, headPose, bodyPose, leftArmPose, rightArmPose, leftLegPose, rightLegPose);
    }

    public static ArmorStandPose ofMinecraft(String name) {
        Objects.requireNonNull(name, "name is null");
        return new ArmorStandPoseImpl(name, SourceType.MINECRAFT);
    }

    public static ArmorStandPose ofVanillaTweaks(String name) {
        Objects.requireNonNull(name, "name is null");
        return new ArmorStandPoseImpl(name, SourceType.VANILLA_TWEAKS);
    }

    public static ArmorStandPose fromEntity(ArmorStand armorStand) {
        return new ArmorStandPoseImpl(armorStand.getHeadPose(),
                armorStand.getBodyPose(),
                armorStand.getLeftArmPose(),
                armorStand.getRightArmPose(),
                armorStand.getLeftLegPose(),
                armorStand.getRightLegPose());
    }

    public static ArmorStandPose randomize(PosePartMutator[] mutators, boolean clampRotations) {
        return new ArmorStandPoseImpl(mutators[0].randomRotations(clampRotations),
                mutators[1].randomRotations(clampRotations),
                mutators[2].randomRotations(clampRotations),
                mutators[3].randomRotations(clampRotations),
                mutators[4].randomRotations(clampRotations),
                mutators[5].randomRotations(clampRotations));
    }

    public static ArmorStandPose randomValue() {
        List<ArmorStandPose> poses = new ArrayList<>(Arrays.asList(ArmorStandPoses.VALUES_FOR_RANDOM_SELECTION));
        Collections.shuffle(poses);
        return poses.stream().findAny().orElseThrow();
    }

    @Override
    public @Nullable String getTranslationKey() {
        return this.name != null ? Util.makeDescriptionId("screen.pose", this.name) : null;
    }

    @Override
    public List<Component> getTooltipLines() {
        String translationKey = this.getTranslationKey();
        if (translationKey != null) {
            Component component = Component.translatable(translationKey);
            List<Component> lines = new ArrayList<>();
            lines.add(component);
            if (this.sourceType != null) {
                lines.add(this.sourceType.component);
            }
            return lines;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Rotations getHeadPose() {
        return this.headPose != null ? this.headPose : ZERO_ROTATIONS;
    }

    @Override
    public Rotations getBodyPose() {
        return this.bodyPose != null ? this.bodyPose : ZERO_ROTATIONS;
    }

    @Override
    public Rotations getLeftArmPose() {
        return this.leftArmPose != null ? this.leftArmPose : ZERO_ROTATIONS;
    }

    @Override
    public Rotations getRightArmPose() {
        return this.rightArmPose != null ? this.rightArmPose : ZERO_ROTATIONS;
    }

    @Override
    public Rotations getLeftLegPose() {
        return this.leftLegPose != null ? this.leftLegPose : ZERO_ROTATIONS;
    }

    @Override
    public Rotations getRightLegPose() {
        return this.rightLegPose != null ? this.rightLegPose : ZERO_ROTATIONS;
    }

    @Override
    public ArmorStandPose withHeadPose(Rotations rotation) {
        return new ArmorStandPoseImpl(this.name,
                this.sourceType,
                rotation,
                this.bodyPose,
                this.leftArmPose,
                this.rightArmPose,
                this.leftLegPose,
                this.rightLegPose);
    }

    @Override
    public ArmorStandPose withBodyPose(Rotations rotation) {
        return new ArmorStandPoseImpl(this.name,
                this.sourceType,
                this.headPose,
                rotation,
                this.leftArmPose,
                this.rightArmPose,
                this.leftLegPose,
                this.rightLegPose);
    }

    @Override
    public ArmorStandPose withLeftArmPose(Rotations rotation) {
        return new ArmorStandPoseImpl(this.name,
                this.sourceType,
                this.headPose,
                this.bodyPose,
                rotation,
                this.rightArmPose,
                this.leftLegPose,
                this.rightLegPose);
    }

    @Override
    public ArmorStandPose withRightArmPose(Rotations rotation) {
        return new ArmorStandPoseImpl(this.name,
                this.sourceType,
                this.headPose,
                this.bodyPose,
                this.leftArmPose,
                rotation,
                this.leftLegPose,
                this.rightLegPose);
    }

    @Override
    public ArmorStandPose withLeftLegPose(Rotations rotation) {
        return new ArmorStandPoseImpl(this.name,
                this.sourceType,
                this.headPose,
                this.bodyPose,
                this.leftArmPose,
                this.rightArmPose,
                rotation,
                this.rightLegPose);
    }

    @Override
    public ArmorStandPose withRightLegPose(Rotations rotation) {
        return new ArmorStandPoseImpl(this.name,
                this.sourceType,
                this.headPose,
                this.bodyPose,
                this.leftArmPose,
                this.rightArmPose,
                this.leftLegPose,
                rotation);
    }

    @Override
    public ArmorStandPose mirror() {
        return new ArmorStandPoseImpl(this.name,
                this.sourceType,
                mirrorRotations(this.headPose),
                mirrorRotations(this.bodyPose),
                mirrorRotations(this.rightArmPose),
                mirrorRotations(this.leftArmPose),
                mirrorRotations(this.rightLegPose),
                mirrorRotations(this.leftLegPose));
    }

    @Nullable
    private static Rotations mirrorRotations(@Nullable Rotations rotations) {
        return rotations != null ? new Rotations(rotations.x(), -rotations.y(), -rotations.z()) : null;
    }

    @Override
    public ArmorStandPose copyAndFillFrom(ArmorStandPose fillFrom) {
        return new ArmorStandPoseImpl(this.name,
                this.sourceType,
                this.headPose != null ? this.headPose : fillFrom.headPose(),
                this.bodyPose != null ? this.bodyPose : fillFrom.bodyPose(),
                this.leftArmPose != null ? this.leftArmPose : fillFrom.leftArmPose(),
                this.rightArmPose != null ? this.rightArmPose : fillFrom.rightArmPose(),
                this.leftLegPose != null ? this.leftLegPose : fillFrom.leftLegPose(),
                this.rightLegPose != null ? this.rightLegPose : fillFrom.rightLegPose());
    }

    @Override
    public void applyToEntity(ArmorStand armorStand) {
        armorStand.setHeadPose(this.getHeadPose());
        armorStand.setBodyPose(this.getBodyPose());
        armorStand.setLeftArmPose(this.getLeftArmPose());
        armorStand.setRightArmPose(this.getRightArmPose());
        armorStand.setLeftLegPose(this.getLeftLegPose());
        armorStand.setRightLegPose(this.getRightLegPose());
    }

    @Override
    public void serializeAllPoses(CompoundTag compoundTag) {
        this.serializeBodyPoses(compoundTag, null);
        this.serializeArmPoses(compoundTag, null);
        this.serializeLegPoses(compoundTag, null);
    }

    @Override
    public void serializeBodyPoses(CompoundTag compoundTag, @Nullable ArmorStandPose lastSentPose) {
        if (lastSentPose == null || !Objects.equals(this.headPose, lastSentPose.headPose())) {
            compoundTag.storeNullable("Head", Rotations.CODEC, this.headPose);
        }
        if (lastSentPose == null || !Objects.equals(this.bodyPose, lastSentPose.bodyPose())) {
            compoundTag.storeNullable("Body", Rotations.CODEC, this.bodyPose);
        }
    }

    @Override
    public void serializeArmPoses(CompoundTag compoundTag, @Nullable ArmorStandPose lastSentPose) {
        if (lastSentPose == null || !Objects.equals(this.leftArmPose, lastSentPose.leftArmPose())) {
            compoundTag.storeNullable("LeftArm", Rotations.CODEC, this.leftArmPose);
        }
        if (lastSentPose == null || !Objects.equals(this.rightArmPose, lastSentPose.rightArmPose())) {
            compoundTag.storeNullable("RightArm", Rotations.CODEC, this.rightArmPose);
        }
    }

    @Override
    public void serializeLegPoses(CompoundTag compoundTag, @Nullable ArmorStandPose lastSentPose) {
        if (lastSentPose == null || !Objects.equals(this.leftLegPose, lastSentPose.leftLegPose())) {
            compoundTag.storeNullable("LeftLeg", Rotations.CODEC, this.leftLegPose);
        }
        if (lastSentPose == null || !Objects.equals(this.rightLegPose, lastSentPose.rightLegPose())) {
            compoundTag.storeNullable("RightLeg", Rotations.CODEC, this.rightLegPose);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ArmorStandPoseImpl) obj;
        return Objects.equals(this.headPose, that.headPose) && Objects.equals(this.bodyPose, that.bodyPose)
                && Objects.equals(this.leftArmPose, that.leftArmPose) && Objects.equals(this.rightArmPose,
                that.rightArmPose) && Objects.equals(this.leftLegPose, that.leftLegPose)
                && Objects.equals(this.rightLegPose, that.rightLegPose);
    }

    public enum SourceType {
        MINECRAFT("minecraft", "Minecraft"),
        VANILLA_TWEAKS("vanillatweaks", "Vanilla Tweaks");

        public static final String POSE_SOURCE_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.pose.by";

        private final String id;
        public final Component component;

        SourceType(String id, String displayName) {
            this.id = id;
            this.component = Component.translatable(POSE_SOURCE_TRANSLATION_KEY, displayName)
                    .withStyle(ChatFormatting.BLUE);
        }

        public ResourceLocation id(String path) {
            Objects.requireNonNull(path, "path is null");
            return ResourceLocationHelper.fromNamespaceAndPath(this.id, path);
        }
    }
}
