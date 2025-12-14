package fuzs.statuemenus.impl.world.inventory;

import net.minecraft.resources.Identifier;
import fuzs.statuemenus.api.v1.world.entity.decoration.StatueEntity;
import fuzs.statuemenus.api.v1.world.inventory.data.PosePartMutator;
import fuzs.statuemenus.api.v1.world.inventory.data.StatuePose;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueScreenType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.*;

public record StatuePoseImpl(@Nullable Identifier name,
                             @Nullable SourceType sourceType,
                             boolean isMirrored,
                             @Nullable Rotations headPose,
                             @Nullable Rotations bodyPose,
                             @Nullable Rotations leftArmPose,
                             @Nullable Rotations rightArmPose,
                             @Nullable Rotations leftLegPose,
                             @Nullable Rotations rightLegPose) implements StatuePose {
    private static final Rotations ZERO_ROTATIONS = new Rotations(0.0F, 0.0F, 0.0F);

    private StatuePoseImpl(String name, SourceType sourceType) {
        this(sourceType.id(name),
                sourceType,
                false,
                ZERO_ROTATIONS,
                ZERO_ROTATIONS,
                ZERO_ROTATIONS,
                ZERO_ROTATIONS,
                ZERO_ROTATIONS,
                ZERO_ROTATIONS);
    }

    public StatuePoseImpl(@Nullable Rotations headPose, @Nullable Rotations bodyPose, @Nullable Rotations leftArmPose, @Nullable Rotations rightArmPose, @Nullable Rotations leftLegPose, @Nullable Rotations rightLegPose) {
        this(null, null, false, headPose, bodyPose, leftArmPose, rightArmPose, leftLegPose, rightLegPose);
    }

    public static StatuePose ofMinecraft(String name) {
        Objects.requireNonNull(name, "name is null");
        return new StatuePoseImpl(name, SourceType.MINECRAFT);
    }

    public static StatuePose ofVanillaTweaks(String name) {
        Objects.requireNonNull(name, "name is null");
        return new StatuePoseImpl(name, SourceType.VANILLA_TWEAKS);
    }

    public static StatuePose fromEntity(StatueEntity statueEntity) {
        return new StatuePoseImpl(statueEntity.getHeadPose(),
                statueEntity.getBodyPose(),
                statueEntity.getLeftArmPose(),
                statueEntity.getRightArmPose(),
                statueEntity.getLeftLegPose(),
                statueEntity.getRightLegPose());
    }

    public static StatuePose randomize(List<PosePartMutator> mutators, boolean clampRotations) {
        return new StatuePoseImpl(mutators.get(0).randomRotations(clampRotations),
                mutators.get(1).randomRotations(clampRotations),
                mutators.get(2).randomRotations(clampRotations),
                mutators.get(3).randomRotations(clampRotations),
                mutators.get(4).randomRotations(clampRotations),
                mutators.get(5).randomRotations(clampRotations));
    }

    public static StatuePose randomValue() {
        List<StatuePose> poses = new ArrayList<>(Arrays.asList(StatuePoses.VALUES_FOR_RANDOM_SELECTION));
        Collections.shuffle(poses);
        return poses.stream().findAny().orElseThrow();
    }

    @Override
    public @Nullable String getTranslationKey() {
        if (this.name != null) {
            return StatueScreenType.POSES.id().toLanguageKey("screen", this.name.toLanguageKey());
        } else {
            return null;
        }
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
    public StatuePose withHeadPose(Rotations rotation) {
        return new StatuePoseImpl(this.name,
                this.sourceType,
                false,
                rotation,
                this.bodyPose,
                this.leftArmPose,
                this.rightArmPose,
                this.leftLegPose,
                this.rightLegPose);
    }

    @Override
    public StatuePose withBodyPose(Rotations rotation) {
        return new StatuePoseImpl(this.name,
                this.sourceType,
                false,
                this.headPose,
                rotation,
                this.leftArmPose,
                this.rightArmPose,
                this.leftLegPose,
                this.rightLegPose);
    }

    @Override
    public StatuePose withLeftArmPose(Rotations rotation) {
        return new StatuePoseImpl(this.name,
                this.sourceType,
                false,
                this.headPose,
                this.bodyPose,
                rotation,
                this.rightArmPose,
                this.leftLegPose,
                this.rightLegPose);
    }

    @Override
    public StatuePose withRightArmPose(Rotations rotation) {
        return new StatuePoseImpl(this.name,
                this.sourceType,
                false,
                this.headPose,
                this.bodyPose,
                this.leftArmPose,
                rotation,
                this.leftLegPose,
                this.rightLegPose);
    }

    @Override
    public StatuePose withLeftLegPose(Rotations rotation) {
        return new StatuePoseImpl(this.name,
                this.sourceType,
                false,
                this.headPose,
                this.bodyPose,
                this.leftArmPose,
                this.rightArmPose,
                rotation,
                this.rightLegPose);
    }

    @Override
    public StatuePose withRightLegPose(Rotations rotation) {
        return new StatuePoseImpl(this.name,
                this.sourceType,
                false,
                this.headPose,
                this.bodyPose,
                this.leftArmPose,
                this.rightArmPose,
                this.leftLegPose,
                rotation);
    }

    @Override
    public StatuePose mirror() {
        return new StatuePoseImpl(this.name,
                this.sourceType,
                true,
                mirrorRotations(this.headPose),
                mirrorRotations(this.bodyPose),
                mirrorRotations(this.rightArmPose),
                mirrorRotations(this.leftArmPose),
                mirrorRotations(this.rightLegPose),
                mirrorRotations(this.leftLegPose));
    }

    @Nullable private static Rotations mirrorRotations(@Nullable Rotations rotations) {
        return rotations != null ? new Rotations(rotations.x(), -rotations.y(), -rotations.z()) : null;
    }

    @Override
    public StatuePose copyAndFillFrom(StatuePose fillFrom) {
        return new StatuePoseImpl(this.name,
                this.sourceType,
                false,
                this.headPose != null ? this.headPose : fillFrom.headPose(),
                this.bodyPose != null ? this.bodyPose : fillFrom.bodyPose(),
                this.leftArmPose != null ? this.leftArmPose : fillFrom.leftArmPose(),
                this.rightArmPose != null ? this.rightArmPose : fillFrom.rightArmPose(),
                this.leftLegPose != null ? this.leftLegPose : fillFrom.leftLegPose(),
                this.rightLegPose != null ? this.rightLegPose : fillFrom.rightLegPose());
    }

    @Override
    public void applyToEntity(StatueEntity statueEntity) {
        statueEntity.setHeadPose(this.getHeadPose());
        statueEntity.setBodyPose(this.getBodyPose());
        statueEntity.setLeftArmPose(this.getLeftArmPose());
        statueEntity.setRightArmPose(this.getRightArmPose());
        statueEntity.setLeftLegPose(this.getLeftLegPose());
        statueEntity.setRightLegPose(this.getRightLegPose());
    }

    @Override
    public void serializeAllPoses(CompoundTag compoundTag) {
        this.serializeBodyPoses(compoundTag, null);
        this.serializeArmPoses(compoundTag, null);
        this.serializeLegPoses(compoundTag, null);
    }

    @Override
    public void serializeBodyPoses(CompoundTag compoundTag, @Nullable StatuePose lastSentPose) {
        if (lastSentPose == null || !Objects.equals(this.headPose, lastSentPose.headPose())) {
            compoundTag.storeNullable("Head", Rotations.CODEC, this.headPose);
        }

        if (lastSentPose == null || !Objects.equals(this.bodyPose, lastSentPose.bodyPose())) {
            compoundTag.storeNullable("Body", Rotations.CODEC, this.bodyPose);
        }
    }

    @Override
    public void serializeArmPoses(CompoundTag compoundTag, @Nullable StatuePose lastSentPose) {
        if (lastSentPose == null || !Objects.equals(this.leftArmPose, lastSentPose.leftArmPose())) {
            compoundTag.storeNullable("LeftArm", Rotations.CODEC, this.leftArmPose);
        }

        if (lastSentPose == null || !Objects.equals(this.rightArmPose, lastSentPose.rightArmPose())) {
            compoundTag.storeNullable("RightArm", Rotations.CODEC, this.rightArmPose);
        }
    }

    @Override
    public void serializeLegPoses(CompoundTag compoundTag, @Nullable StatuePose lastSentPose) {
        if (lastSentPose == null || !Objects.equals(this.leftLegPose, lastSentPose.leftLegPose())) {
            compoundTag.storeNullable("LeftLeg", Rotations.CODEC, this.leftLegPose);
        }

        if (lastSentPose == null || !Objects.equals(this.rightLegPose, lastSentPose.rightLegPose())) {
            compoundTag.storeNullable("RightLeg", Rotations.CODEC, this.rightLegPose);
        }
    }

    @Override
    public boolean isVanillaTweaksCompatible() {
        return this.sourceType == SourceType.VANILLA_TWEAKS;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof StatuePose statuePose)) {
            return false;
        } else {
            return Objects.equals(this.headPose(), statuePose.headPose()) && Objects.equals(this.bodyPose(),
                    statuePose.bodyPose()) && Objects.equals(this.leftArmPose(), statuePose.leftArmPose())
                    && Objects.equals(this.rightArmPose(), statuePose.rightArmPose())
                    && Objects.equals(this.leftLegPose(), statuePose.leftLegPose())
                    && Objects.equals(this.rightLegPose(), statuePose.rightLegPose());
        }
    }

    public enum SourceType {
        MINECRAFT("minecraft", "Minecraft"),
        VANILLA_TWEAKS("vanillatweaks", "Vanilla Tweaks");

        private final String id;
        public final Component component;

        SourceType(String id, String displayName) {
            this.id = id;
            String translationKey = StatueScreenType.POSES.id().toLanguageKey("screen", "by");
            this.component = Component.translatable(translationKey, displayName).withStyle(ChatFormatting.BLUE);
        }

        public Identifier id(String path) {
            Objects.requireNonNull(path, "path is null");
            return Identifier.fromNamespaceAndPath(this.id, path);
        }
    }
}
