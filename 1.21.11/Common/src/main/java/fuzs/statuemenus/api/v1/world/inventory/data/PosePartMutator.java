package fuzs.statuemenus.api.v1.world.inventory.data;

import fuzs.statuemenus.impl.StatueMenus;
import fuzs.statuemenus.impl.world.inventory.StatuePoses;
import net.minecraft.core.Direction;
import net.minecraft.core.Rotations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public record PosePartMutator(Identifier id,
                              Function<StatuePose, Rotations> rotationsGetter,
                              BiFunction<StatuePose, Rotations, StatuePose> rotationsSetter,
                              List<PosePartAxisRange> axisRanges,
                              List<Direction.Axis> axisOrder,
                              byte invertedIndices) implements StringRepresentable {
    public static final String AXIS_X_TRANSLATION_KEY = StatueScreenType.ROTATIONS.id().toLanguageKey("screen", "x");
    public static final String AXIS_Y_TRANSLATION_KEY = StatueScreenType.ROTATIONS.id().toLanguageKey("screen", "y");
    public static final String AXIS_Z_TRANSLATION_KEY = StatueScreenType.ROTATIONS.id().toLanguageKey("screen", "z");
    public static final PosePartMutator HEAD = new PosePartMutator(StatueMenus.id("head"),
            StatuePose::getHeadPose,
            StatuePose::withHeadPose,
            PosePartAxisRange.range(-60.0F, 60.0F),
            PosePartAxisRange.range(-60.0F, 60.0F),
            PosePartAxisRange.range(-120.0, 120.0));
    public static final PosePartMutator BODY = new PosePartMutator(StatueMenus.id("body"),
            StatuePose::getBodyPose,
            StatuePose::withBodyPose,
            PosePartAxisRange.range(-30.0F, 30.0F),
            PosePartAxisRange.range(-30.0F, 30.0F),
            PosePartAxisRange.range(-120.0, 120.0));
    public static final PosePartMutator RIGHT_ARM = new PosePartMutator(StatueMenus.id("right_arm"),
            StatuePose::getRightArmPose,
            StatuePose::withRightArmPose,
            PosePartAxisRange.range(-180.0, 0.0),
            PosePartAxisRange.range(-90.0, 45.0),
            PosePartAxisRange.range(-120.0, 120.0));
    public static final PosePartMutator LEFT_ARM = new PosePartMutator(StatueMenus.id("left_arm"),
            StatuePose::getLeftArmPose,
            StatuePose::withLeftArmPose,
            PosePartAxisRange.range(-180.0, 0.0),
            PosePartAxisRange.range(-45.0, 90.0),
            PosePartAxisRange.range(-120.0, 120.0));
    public static final PosePartMutator RIGHT_LEG = new PosePartMutator(StatueMenus.id("right_leg"),
            StatuePose::getRightLegPose,
            StatuePose::withRightLegPose,
            PosePartAxisRange.range(-120.0, 120.0),
            PosePartAxisRange.range(-90.0, 0.0),
            PosePartAxisRange.range(-120.0, 120.0));
    public static final PosePartMutator LEFT_LEG = new PosePartMutator(StatueMenus.id("left_leg"),
            StatuePose::getLeftLegPose,
            StatuePose::withLeftLegPose,
            PosePartAxisRange.range(-120.0, 120.0),
            PosePartAxisRange.range(0.0, 90.0),
            PosePartAxisRange.range(-120.0, 120.0));
    public static final List<PosePartMutator> TYPES = List.of(PosePartMutator.HEAD,
            PosePartMutator.BODY,
            PosePartMutator.RIGHT_ARM,
            PosePartMutator.LEFT_ARM,
            PosePartMutator.RIGHT_LEG,
            PosePartMutator.LEFT_LEG);

    public PosePartMutator(Identifier id, Function<StatuePose, Rotations> rotationsGetter, BiFunction<StatuePose, Rotations, StatuePose> rotationsSetter, PosePartAxisRange rangeX, PosePartAxisRange rangeY, PosePartAxisRange rangeZ) {
        this(id,
                rotationsGetter,
                rotationsSetter,
                rangeX,
                rangeY,
                rangeZ,
                List.of(Direction.Axis.X, Direction.Axis.Y, Direction.Axis.Z),
                Direction.Axis.Y);
    }

    public PosePartMutator(Identifier id, Function<StatuePose, Rotations> rotationsGetter, BiFunction<StatuePose, Rotations, StatuePose> rotationsSetter, PosePartAxisRange rangeX, PosePartAxisRange rangeY, PosePartAxisRange rangeZ, List<Direction.Axis> axisOrder, Direction.Axis... invertedAxes) {
        this(id,
                rotationsGetter,
                rotationsSetter,
                List.of(rangeX, rangeY, rangeZ),
                axisOrder,
                computeInvertedIndices(invertedAxes));
    }

    private static byte computeInvertedIndices(Direction.Axis[] invertedAxes) {
        byte invertedIndices = 0;
        for (Direction.Axis axis : invertedAxes) {
            invertedIndices |= (byte) (1 << axis.ordinal());
        }
        return invertedIndices;
    }

    @Override
    public String getSerializedName() {
        return this.id.toString();
    }

    @Override
    public String toString() {
        return this.getSerializedName();
    }

    public String getTranslationKey() {
        return StatueScreenType.ROTATIONS.id().toLanguageKey("screen", this.id.toLanguageKey("pose"));
    }

    public Component getAxisComponent(StatuePose pose, int index) {
        double value = StatuePoses.snapValue(this.getRotationsAtAxis(index, pose), StatuePoses.DEGREES_SNAP_INTERVAL);
        return Component.translatable(this.getAxisTranslationKey(this.getAxisAt(index)),
                StatuePoses.ROTATION_FORMAT.format(value));
    }

    private String getAxisTranslationKey(Direction.Axis axis) {
        return switch (axis) {
            case X -> AXIS_X_TRANSLATION_KEY;
            case Y -> AXIS_Y_TRANSLATION_KEY;
            case Z -> AXIS_Z_TRANSLATION_KEY;
        };
    }

    public double getRotationsAtAxis(int index, StatuePose pose) {
        return this.getRotationsAtAxis(index, this.rotationsGetter.apply(pose));
    }

    private double getRotationsAtAxis(int index, Rotations rotations) {
        return switch (this.getAxisAt(index)) {
            case X -> this.invertAtAxis(Direction.Axis.X, rotations.x());
            case Y -> this.invertAtAxis(Direction.Axis.Y, rotations.y());
            case Z -> this.invertAtAxis(Direction.Axis.Z, rotations.z());
        };
    }

    public double getNormalizedRotationsAtAxis(int index, StatuePose pose, boolean clampRotations) {
        return this.getNormalizedRotationsAtAxis(index, this.rotationsGetter.apply(pose), clampRotations);
    }

    private double getNormalizedRotationsAtAxis(int index, Rotations rotations, boolean clampRotations) {
        return switch (this.getAxisAt(index)) {
            case X -> (float) this.getAxisRangeAtAxis(Direction.Axis.X, clampRotations)
                    .normalize(this.invertAtAxis(Direction.Axis.X, rotations.x()));
            case Y -> (float) this.getAxisRangeAtAxis(Direction.Axis.Y, clampRotations)
                    .normalize(this.invertAtAxis(Direction.Axis.Y, rotations.y()));
            case Z -> (float) this.getAxisRangeAtAxis(Direction.Axis.Z, clampRotations)
                    .normalize(this.invertAtAxis(Direction.Axis.Z, rotations.z()));
        };
    }

    private float invertAtAxis(Direction.Axis axis, float value) {
        return Mth.wrapDegrees((this.invertedIndices >> axis.ordinal() & 1) == 1 ? value * -1.0F : value);
    }

    public StatuePose setRotationsAtAxis(int index, StatuePose pose, double newValue, boolean clampRotations) {
        return this.rotationsSetter.apply(pose,
                this.setRotationsAtAxis(index,
                        this.rotationsGetter.apply(pose),
                        (float) this.getAxisRangeAtAxis(index, clampRotations).expand(newValue)));
    }

    private PosePartAxisRange getAxisRangeAtAxis(int index, boolean clampRotations) {
        return this.getAxisRangeAtAxis(this.getAxisAt(index), clampRotations);
    }

    private PosePartAxisRange getAxisRangeAtAxis(Direction.Axis axis, boolean clampRotations) {
        return clampRotations ? this.axisRanges.get(axis.ordinal()) : PosePartAxisRange.fullRange();
    }

    private Rotations setRotationsAtAxis(int index, Rotations rotations, float newValue) {
        return switch (this.getAxisAt(index)) {
            case X -> new Rotations(this.invertAtAxis(Direction.Axis.X, newValue), rotations.y(), rotations.z());
            case Y -> new Rotations(rotations.x(), this.invertAtAxis(Direction.Axis.Y, newValue), rotations.z());
            case Z -> new Rotations(rotations.x(), rotations.y(), this.invertAtAxis(Direction.Axis.Z, newValue));
        };
    }

    public Direction.Axis getAxisAt(int index) {
        return this.axisOrder.get(index);
    }

    public Rotations randomRotations(boolean clampRotations) {
        Rotations rotations = new Rotations((float) this.getAxisRangeAtAxis(Direction.Axis.X, clampRotations).random(),
                (float) this.getAxisRangeAtAxis(Direction.Axis.Y, clampRotations).random(),
                (float) this.getAxisRangeAtAxis(Direction.Axis.Z, clampRotations).random());
        return clampRotations ? this.setRotationsAtAxis(2, rotations, 0.0F) : rotations;
    }

    public record PosePartAxisRange(double min, double max) {
        public static final double MIN_VALUE = -180.0;
        public static final double MAX_VALUE = 180.0;

        public PosePartAxisRange {
            if (min >= max) {
                throw new IllegalArgumentException("Min must be smaller than max: %s >= %s".formatted(min, max));
            }

            if (Mth.clamp(min, MIN_VALUE, MAX_VALUE) != min) {
                throw new IllegalArgumentException("Min out of bounds, must be between %s and %s, was %s".formatted(
                        MIN_VALUE,
                        MAX_VALUE,
                        min));
            }

            if (Mth.clamp(max, MIN_VALUE, MAX_VALUE) != max) {
                throw new IllegalArgumentException("Max out of bounds, must be between %s and %s, was %s".formatted(
                        MIN_VALUE,
                        MAX_VALUE,
                        max));
            }
        }

        public static PosePartAxisRange fullRange() {
            return range(PosePartAxisRange.MIN_VALUE, PosePartAxisRange.MAX_VALUE);
        }

        public static PosePartAxisRange range(double min, double max) {
            return new PosePartAxisRange(min, max);
        }

        public double normalize(double expandedValue) {
            return (this.clamp(expandedValue) - this.min) / this.range();
        }

        public double expand(double normalizedValue) {
            return normalizedValue * this.range() + this.min;
        }

        public double clamp(double value) {
            return Mth.clamp(value, this.min, this.max);
        }

        public double random() {
            return Math.random() * this.range() + this.min;
        }

        public double range() {
            return this.max - this.min;
        }
    }
}
