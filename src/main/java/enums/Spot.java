package enums;

import application.RouletteContext;
import constants.Configurations;

import java.util.ArrayList;
import java.util.List;

public enum Spot {
    SPOT_0,
    SPOT_00,
    SPOT_01,
    SPOT_02,
    SPOT_03,
    SPOT_04,
    SPOT_05,
    SPOT_06,
    SPOT_07,
    SPOT_08,
    SPOT_09,
    SPOT_10,
    SPOT_11,
    SPOT_12,
    SPOT_13,
    SPOT_14,
    SPOT_15,
    SPOT_16,
    SPOT_17,
    SPOT_18,
    SPOT_19,
    SPOT_20,
    SPOT_21,
    SPOT_22,
    SPOT_23,
    SPOT_24,
    SPOT_25,
    SPOT_26,
    SPOT_27,
    SPOT_28,
    SPOT_29,
    SPOT_30,
    SPOT_31,
    SPOT_32,
    SPOT_33,
    SPOT_34,
    SPOT_35,
    SPOT_36;

    private static final Spot[] ONE_TO_36_WHEEL = {
            SPOT_01, SPOT_02, SPOT_03, SPOT_04, SPOT_05, SPOT_06, SPOT_07, SPOT_08, SPOT_09,
            SPOT_10, SPOT_11, SPOT_12, SPOT_13, SPOT_14, SPOT_15, SPOT_16, SPOT_17, SPOT_18,
            SPOT_19, SPOT_20, SPOT_21, SPOT_22, SPOT_23, SPOT_24, SPOT_25, SPOT_26, SPOT_27,
            SPOT_28, SPOT_29, SPOT_30, SPOT_31, SPOT_32, SPOT_33, SPOT_34, SPOT_35, SPOT_36
    };

    private static final Spot[] EUROPEAN_WHEEL = {
            SPOT_0, SPOT_32, SPOT_15, SPOT_19, SPOT_04, SPOT_21, SPOT_02, SPOT_25,
            SPOT_17, SPOT_34, SPOT_06, SPOT_27, SPOT_13, SPOT_36, SPOT_11, SPOT_30, SPOT_08,
            SPOT_23, SPOT_10, SPOT_05, SPOT_24, SPOT_16, SPOT_33, SPOT_01, SPOT_20, SPOT_14,
            SPOT_31, SPOT_09, SPOT_22, SPOT_18, SPOT_29, SPOT_07, SPOT_28, SPOT_12, SPOT_35,
            SPOT_03, SPOT_26
    };

    private static final Spot[] AMERICAN_WHEEL = {
            SPOT_0, SPOT_28, SPOT_09, SPOT_26, SPOT_30, SPOT_11, SPOT_07, SPOT_20, SPOT_32, SPOT_17,
            SPOT_05, SPOT_22, SPOT_34, SPOT_15, SPOT_03, SPOT_24, SPOT_36, SPOT_13, SPOT_01, SPOT_00,
            SPOT_27, SPOT_10, SPOT_25, SPOT_29, SPOT_12, SPOT_08, SPOT_19, SPOT_31, SPOT_18, SPOT_06,
            SPOT_21, SPOT_33, SPOT_16, SPOT_04, SPOT_23, SPOT_35, SPOT_14, SPOT_02
    };

    public static Spot getByNumber(int number) {
        for (Spot spot : values()) {
            if (number == spot.getNumber()) {
                return spot;
            }
        }
        throw new IllegalArgumentException();
    }

    public static List<Spot> getAvailableList(RouletteType rouletteType) {
        List<Spot> availableSpotList = new ArrayList<>();
        for (Spot spot : values()) {
            if ((rouletteType == RouletteType.ONE_TO_36 && (spot == SPOT_0 || spot == SPOT_00))
                    || (rouletteType == RouletteType.EUROPEAN_STYLE && spot == SPOT_00)) {
                continue;
            }
            availableSpotList.add(spot);
        }
        return availableSpotList;
    }

    public static Spot getRandomNextSpot(RouletteContext rouletteContext) {
        List<Spot> availableSpotList = getAvailableList(rouletteContext.rouletteType);
        switch (rouletteContext.spotGenerateType) {
            case RANDOM:
                return availableSpotList.get(Configurations.RANDOM.nextInt(availableSpotList.size()));
            case ROTATION_NUMBER:
                return availableSpotList.get((int) (rouletteContext.currentLoopCount % availableSpotList.size()));
            case ROTATION_WHEEL:
                switch (rouletteContext.rouletteType) {
                    case ONE_TO_36:
                        return ONE_TO_36_WHEEL[((int) (rouletteContext.currentLoopCount % ONE_TO_36_WHEEL.length))];
                    case EUROPEAN_STYLE:
                        return EUROPEAN_WHEEL[((int) (rouletteContext.currentLoopCount % EUROPEAN_WHEEL.length))];
                    case AMERICAN_STYLE:
                        return AMERICAN_WHEEL[((int) (rouletteContext.currentLoopCount % AMERICAN_WHEEL.length))];
                    default:
                        throw new IllegalArgumentException();
                }
            case RANDOM_RED_ONLY: {
                while (true) {
                    Spot spot = availableSpotList.get(Configurations.RANDOM.nextInt(availableSpotList.size()));
                    if (spot.isRed()) {
                        return spot;
                    }
                }
            }
            case RANDOM_BLACK_ONLY: {
                while (true) {
                    Spot spot = availableSpotList.get(Configurations.RANDOM.nextInt(availableSpotList.size()));
                    if (spot.isBlack()) {
                        return spot;
                    }
                }
            }
            case RANDOM_EXCEPT_ONE:
                while (true) {
                    Spot spot = availableSpotList.get(Configurations.RANDOM.nextInt(availableSpotList.size()));
                    if (spot != SPOT_01) {
                        return spot;
                    }
                }
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getNumber() {
        switch (this) {
            case SPOT_0:
                return 0;
            case SPOT_00:
                return -1;
            case SPOT_01:
                return 1;
            case SPOT_02:
                return 2;
            case SPOT_03:
                return 3;
            case SPOT_04:
                return 4;
            case SPOT_05:
                return 5;
            case SPOT_06:
                return 6;
            case SPOT_07:
                return 7;
            case SPOT_08:
                return 8;
            case SPOT_09:
                return 9;
            case SPOT_10:
                return 10;
            case SPOT_11:
                return 11;
            case SPOT_12:
                return 12;
            case SPOT_13:
                return 13;
            case SPOT_14:
                return 14;
            case SPOT_15:
                return 15;
            case SPOT_16:
                return 16;
            case SPOT_17:
                return 17;
            case SPOT_18:
                return 18;
            case SPOT_19:
                return 19;
            case SPOT_20:
                return 20;
            case SPOT_21:
                return 21;
            case SPOT_22:
                return 22;
            case SPOT_23:
                return 23;
            case SPOT_24:
                return 24;
            case SPOT_25:
                return 25;
            case SPOT_26:
                return 26;
            case SPOT_27:
                return 27;
            case SPOT_28:
                return 28;
            case SPOT_29:
                return 29;
            case SPOT_30:
                return 30;
            case SPOT_31:
                return 31;
            case SPOT_32:
                return 32;
            case SPOT_33:
                return 33;
            case SPOT_34:
                return 34;
            case SPOT_35:
                return 35;
            case SPOT_36:
                return 36;
            default:
                throw new IllegalArgumentException();
        }
    }

    public boolean isRed() {
        switch (this) {
            case SPOT_01:
            case SPOT_03:
            case SPOT_05:
            case SPOT_07:
            case SPOT_09:
            case SPOT_12:
            case SPOT_14:
            case SPOT_16:
            case SPOT_18:
            case SPOT_19:
            case SPOT_21:
            case SPOT_23:
            case SPOT_25:
            case SPOT_27:
            case SPOT_30:
            case SPOT_32:
            case SPOT_34:
            case SPOT_36:
                return true;
            default:
                return false;
        }
    }

    public boolean isBlack() {
        switch (this) {
            case SPOT_02:
            case SPOT_04:
            case SPOT_06:
            case SPOT_08:
            case SPOT_10:
            case SPOT_11:
            case SPOT_13:
            case SPOT_15:
            case SPOT_17:
            case SPOT_20:
            case SPOT_22:
            case SPOT_24:
            case SPOT_26:
            case SPOT_28:
            case SPOT_29:
            case SPOT_31:
            case SPOT_33:
            case SPOT_35:
                return true;
            default:
                return false;
        }
    }

    public boolean isGreen() {
        return !isRed() && !isBlack();
    }

    public boolean isEven() {
        switch (this) {
            case SPOT_02:
            case SPOT_04:
            case SPOT_06:
            case SPOT_08:
            case SPOT_10:
            case SPOT_12:
            case SPOT_14:
            case SPOT_16:
            case SPOT_18:
            case SPOT_20:
            case SPOT_22:
            case SPOT_24:
            case SPOT_26:
            case SPOT_28:
            case SPOT_30:
            case SPOT_32:
            case SPOT_34:
            case SPOT_36:
                return true;
            default:
                return false;
        }
    }

    public boolean isOdd() {
        switch (this) {
            case SPOT_01:
            case SPOT_03:
            case SPOT_05:
            case SPOT_07:
            case SPOT_09:
            case SPOT_11:
            case SPOT_13:
            case SPOT_15:
            case SPOT_17:
            case SPOT_19:
            case SPOT_21:
            case SPOT_23:
            case SPOT_25:
            case SPOT_27:
            case SPOT_29:
            case SPOT_31:
            case SPOT_33:
            case SPOT_35:
                return true;
            default:
                return false;
        }
    }

    public boolean isFirstDozenOrZeroAndDoubleZero() {
        switch (this) {
            case SPOT_0:
            case SPOT_00:
            case SPOT_01:
            case SPOT_02:
            case SPOT_03:
            case SPOT_04:
            case SPOT_05:
            case SPOT_06:
            case SPOT_07:
            case SPOT_08:
            case SPOT_09:
            case SPOT_10:
            case SPOT_11:
            case SPOT_12:
                return true;
            default:
                return false;
        }
    }

    public boolean isFirstDozen() {
        switch (this) {
            case SPOT_01:
            case SPOT_02:
            case SPOT_03:
            case SPOT_04:
            case SPOT_05:
            case SPOT_06:
            case SPOT_07:
            case SPOT_08:
            case SPOT_09:
            case SPOT_10:
            case SPOT_11:
            case SPOT_12:
                return true;
            default:
                return false;
        }
    }

    public boolean isSecondDozen() {
        switch (this) {
            case SPOT_13:
            case SPOT_14:
            case SPOT_15:
            case SPOT_16:
            case SPOT_17:
            case SPOT_18:
            case SPOT_19:
            case SPOT_20:
            case SPOT_21:
            case SPOT_22:
            case SPOT_23:
            case SPOT_24:
                return true;
            default:
                return false;
        }
    }

    public boolean isThirdDozen() {
        switch (this) {
            case SPOT_25:
            case SPOT_26:
            case SPOT_27:
            case SPOT_28:
            case SPOT_29:
            case SPOT_30:
            case SPOT_31:
            case SPOT_32:
            case SPOT_33:
            case SPOT_34:
            case SPOT_35:
            case SPOT_36:
                return true;
            default:
                return false;
        }
    }

    public boolean isFirstColumn() {
        switch (this) {
            case SPOT_01:
            case SPOT_04:
            case SPOT_07:
            case SPOT_10:
            case SPOT_13:
            case SPOT_16:
            case SPOT_19:
            case SPOT_22:
            case SPOT_25:
            case SPOT_28:
            case SPOT_31:
            case SPOT_34:
                return true;
            default:
                return false;
        }
    }

    public boolean isSecondColumn() {
        switch (this) {
            case SPOT_02:
            case SPOT_05:
            case SPOT_08:
            case SPOT_11:
            case SPOT_14:
            case SPOT_17:
            case SPOT_20:
            case SPOT_23:
            case SPOT_26:
            case SPOT_29:
            case SPOT_32:
            case SPOT_35:
                return true;
            default:
                return false;
        }
    }

    public boolean isThirdColumn() {
        switch (this) {
            case SPOT_03:
            case SPOT_06:
            case SPOT_09:
            case SPOT_12:
            case SPOT_15:
            case SPOT_18:
            case SPOT_21:
            case SPOT_24:
            case SPOT_27:
            case SPOT_30:
            case SPOT_33:
            case SPOT_36:
                return true;
            default:
                return false;
        }
    }

    public boolean is1To18() {
        return 1 <= getNumber() && getNumber() <= 18;
    }

    public boolean is19To36() {
        return 19 <= getNumber() && getNumber() <= 36;
    }

    public static Spot[] getWheelLayout(RouletteType rouletteType) {
        switch (rouletteType) {
            case EUROPEAN_STYLE:
                return EUROPEAN_WHEEL;
            case AMERICAN_STYLE:
                return AMERICAN_WHEEL;
            case ONE_TO_36:
                return ONE_TO_36_WHEEL;
            default:
                throw new IllegalArgumentException("Unsupported roulette type: " + rouletteType);
        }
    }

    public int getWheelPosition(RouletteType rouletteType) {
        Spot[] wheelLayout = getWheelLayout(rouletteType);
        for (int i = 0; i < wheelLayout.length; i++) {
            if (wheelLayout[i] == this) {
                return i;
            }
        }
        return -1;
    }

    public int getPhysicalDistance(Spot otherSpot, RouletteType rouletteType) {
        if (otherSpot == null) {
            throw new IllegalArgumentException("otherSpot cannot be null");
        }

        int thisPosition = this.getWheelPosition(rouletteType);
        int otherPosition = otherSpot.getWheelPosition(rouletteType);

        if (thisPosition == -1 || otherPosition == -1) {
            throw new IllegalArgumentException("One or both spots are not available in the specified roulette type");
        }

        Spot[] wheelLayout = getWheelLayout(rouletteType);
        int wheelSize = wheelLayout.length;

        int clockwiseDistance = Math.abs(thisPosition - otherPosition);
        int counterClockwiseDistance = wheelSize - clockwiseDistance;

        return Math.min(clockwiseDistance, counterClockwiseDistance);
    }
}