package fuzs.statuemenus.impl.world.inventory;

import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandPose;
import net.minecraft.Util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ArmorStandPoses {
    public static final double DEGREES_SNAP_INTERVAL = 0.125;
    public static final DecimalFormat ROTATION_FORMAT = Util.make(new DecimalFormat("#.##"),
            (DecimalFormat decimalFormat) -> {
                decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
            });
    private static final ArmorStandPose[] VALUES = {ArmorStandPose.DEFAULT,
            ArmorStandPose.SOLEMN,
            ArmorStandPose.ATHENA,
            ArmorStandPose.BRANDISH,
            ArmorStandPose.HONOR,
            ArmorStandPose.ENTERTAIN,
            ArmorStandPose.SALUTE,
            ArmorStandPose.HERO,
            ArmorStandPose.RIPOSTE,
            ArmorStandPose.ZOMBIE,
            ArmorStandPose.CANCAN,
            ArmorStandPose.WALKING,
            ArmorStandPose.RUNNING,
            ArmorStandPose.POINTING,
            ArmorStandPose.BLOCKING,
            ArmorStandPose.LUNGEING,
            ArmorStandPose.WINNING,
            ArmorStandPose.SITTING,
            ArmorStandPose.ARABESQUE,
            ArmorStandPose.CUPID,
            ArmorStandPose.CONFIDENT,
            ArmorStandPose.DEATH,
            ArmorStandPose.FACEPALM,
            ArmorStandPose.LAZING,
            ArmorStandPose.CONFUSED,
            ArmorStandPose.FORMAL,
            ArmorStandPose.SAD,
            ArmorStandPose.JOYOUS,
            ArmorStandPose.STARGAZING};
    static final ArmorStandPose[] VALUES_FOR_RANDOM_SELECTION = {ArmorStandPose.DEFAULT,
            ArmorStandPose.SOLEMN,
            ArmorStandPose.ATHENA,
            ArmorStandPose.BRANDISH,
            ArmorStandPose.HONOR,
            ArmorStandPose.ENTERTAIN,
            ArmorStandPose.SALUTE,
            ArmorStandPose.HERO,
            ArmorStandPose.RIPOSTE,
            ArmorStandPose.ZOMBIE,
            ArmorStandPose.CANCAN,
            ArmorStandPose.WALKING,
            ArmorStandPose.RUNNING,
            ArmorStandPose.POINTING,
            ArmorStandPose.BLOCKING,
            ArmorStandPose.LUNGEING,
            ArmorStandPose.WINNING,
            ArmorStandPose.ARABESQUE,
            ArmorStandPose.CUPID,
            ArmorStandPose.CONFIDENT,
            ArmorStandPose.FACEPALM,
            ArmorStandPose.CONFUSED,
            ArmorStandPose.FORMAL,
            ArmorStandPose.SAD,
            ArmorStandPose.JOYOUS,
            ArmorStandPose.STARGAZING};

    public static Optional<ArmorStandPose> get(int index) {
        return index < VALUES.length ? Optional.of(VALUES[index]) : Optional.empty();
    }

    public static int size() {
        return VALUES.length;
    }

    public static double snapValue(double value, double snapInterval) {
        if (snapInterval > 0.0 && snapInterval < 1.0) {
            double currentSnap = 0.0;
            while (currentSnap < 1.0) {
                double snapRegion = snapInterval * 0.1;
                if (value >= currentSnap - snapRegion && value < currentSnap + snapRegion) {
                    return currentSnap;
                }
                currentSnap += snapInterval;
            }
        }
        return value;
    }
}
