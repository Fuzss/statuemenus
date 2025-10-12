package fuzs.statuemenus.impl.world.inventory;

import fuzs.statuemenus.api.v1.world.inventory.data.StatuePose;
import net.minecraft.Util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Optional;

public class StatuePoses {
    public static final double DEGREES_SNAP_INTERVAL = 0.125;
    public static final DecimalFormat ROTATION_FORMAT = Util.make(new DecimalFormat("#.##"),
            (DecimalFormat decimalFormat) -> {
                decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
            });
    private static final StatuePose[] VALUES = {
            StatuePose.DEFAULT,
            StatuePose.SOLEMN,
            StatuePose.ATHENA,
            StatuePose.BRANDISH,
            StatuePose.HONOR,
            StatuePose.ENTERTAIN,
            StatuePose.SALUTE,
            StatuePose.HERO,
            StatuePose.RIPOSTE,
            StatuePose.ZOMBIE,
            StatuePose.CANCAN,
            StatuePose.WALKING,
            StatuePose.RUNNING,
            StatuePose.POINTING,
            StatuePose.BLOCKING,
            StatuePose.LUNGEING,
            StatuePose.WINNING,
            StatuePose.SITTING,
            StatuePose.ARABESQUE,
            StatuePose.CUPID,
            StatuePose.CONFIDENT,
            StatuePose.DEATH,
            StatuePose.FACEPALM,
            StatuePose.LAZING,
            StatuePose.CONFUSED,
            StatuePose.FORMAL,
            StatuePose.SAD,
            StatuePose.JOYOUS,
            StatuePose.STARGAZING
    };
    static final StatuePose[] VALUES_FOR_RANDOM_SELECTION = {
            StatuePose.DEFAULT,
            StatuePose.SOLEMN,
            StatuePose.ATHENA,
            StatuePose.BRANDISH,
            StatuePose.HONOR,
            StatuePose.ENTERTAIN,
            StatuePose.SALUTE,
            StatuePose.HERO,
            StatuePose.RIPOSTE,
            StatuePose.ZOMBIE,
            StatuePose.CANCAN,
            StatuePose.WALKING,
            StatuePose.RUNNING,
            StatuePose.POINTING,
            StatuePose.BLOCKING,
            StatuePose.LUNGEING,
            StatuePose.WINNING,
            StatuePose.ARABESQUE,
            StatuePose.CUPID,
            StatuePose.CONFIDENT,
            StatuePose.FACEPALM,
            StatuePose.CONFUSED,
            StatuePose.FORMAL,
            StatuePose.SAD,
            StatuePose.JOYOUS,
            StatuePose.STARGAZING
    };

    public static Optional<StatuePose> get(int index) {
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
