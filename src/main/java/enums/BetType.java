package enums;

import lombok.Getter;

import java.awt.Point;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum BetType {
    // === Straight Up ===
    STRAIGHT_UP_1(35, new Point(1, 5), Spot.SPOT_01),
    STRAIGHT_UP_2(35, new Point(1, 3), Spot.SPOT_02),
    STRAIGHT_UP_3(35, new Point(1, 1), Spot.SPOT_03),
    STRAIGHT_UP_4(35, new Point(3, 5), Spot.SPOT_04),
    STRAIGHT_UP_5(35, new Point(3, 3), Spot.SPOT_05),
    STRAIGHT_UP_6(35, new Point(3, 1), Spot.SPOT_06),
    STRAIGHT_UP_7(35, new Point(5, 5), Spot.SPOT_07),
    STRAIGHT_UP_8(35, new Point(5, 3), Spot.SPOT_08),
    STRAIGHT_UP_9(35, new Point(5, 1), Spot.SPOT_09),
    STRAIGHT_UP_10(35, new Point(7, 5), Spot.SPOT_10),
    STRAIGHT_UP_11(35, new Point(7, 3), Spot.SPOT_11),
    STRAIGHT_UP_12(35, new Point(7, 1), Spot.SPOT_12),
    STRAIGHT_UP_13(35, new Point(9, 5), Spot.SPOT_13),
    STRAIGHT_UP_14(35, new Point(9, 3), Spot.SPOT_14),
    STRAIGHT_UP_15(35, new Point(9, 1), Spot.SPOT_15),
    STRAIGHT_UP_16(35, new Point(11, 5), Spot.SPOT_16),
    STRAIGHT_UP_17(35, new Point(11, 3), Spot.SPOT_17),
    STRAIGHT_UP_18(35, new Point(11, 1), Spot.SPOT_18),
    STRAIGHT_UP_19(35, new Point(13, 5), Spot.SPOT_19),
    STRAIGHT_UP_20(35, new Point(13, 3), Spot.SPOT_20),
    STRAIGHT_UP_21(35, new Point(13, 1), Spot.SPOT_21),
    STRAIGHT_UP_22(35, new Point(15, 5), Spot.SPOT_22),
    STRAIGHT_UP_23(35, new Point(15, 3), Spot.SPOT_23),
    STRAIGHT_UP_24(35, new Point(15, 1), Spot.SPOT_24),
    STRAIGHT_UP_25(35, new Point(17, 5), Spot.SPOT_25),
    STRAIGHT_UP_26(35, new Point(17, 3), Spot.SPOT_26),
    STRAIGHT_UP_27(35, new Point(17, 1), Spot.SPOT_27),
    STRAIGHT_UP_28(35, new Point(19, 5), Spot.SPOT_28),
    STRAIGHT_UP_29(35, new Point(19, 3), Spot.SPOT_29),
    STRAIGHT_UP_30(35, new Point(19, 1), Spot.SPOT_30),
    STRAIGHT_UP_31(35, new Point(21, 5), Spot.SPOT_31),
    STRAIGHT_UP_32(35, new Point(21, 3), Spot.SPOT_32),
    STRAIGHT_UP_33(35, new Point(21, 1), Spot.SPOT_33),
    STRAIGHT_UP_34(35, new Point(23, 5), Spot.SPOT_34),
    STRAIGHT_UP_35(35, new Point(23, 3), Spot.SPOT_35),
    STRAIGHT_UP_36(35, new Point(23, 1), Spot.SPOT_36),
    ZERO(35, new Point(-1, 3), EnumSet.of(RouletteType.EUROPEAN_STYLE, RouletteType.AMERICAN_STYLE), Spot.SPOT_0),
    DOUBLE_ZERO(35, new Point(-1, 5), EnumSet.of(RouletteType.AMERICAN_STYLE), Spot.SPOT_00),

    // === Split ===
    SPLIT_1_2(17, new Point(1, 4), Spot.SPOT_01, Spot.SPOT_02),
    SPLIT_2_3(17, new Point(1, 2), Spot.SPOT_02, Spot.SPOT_03),
    SPLIT_35_36(17, new Point(23, 2), Spot.SPOT_35, Spot.SPOT_36),
    SPLIT_1_4(17, new Point(2, 5), Spot.SPOT_01, Spot.SPOT_04),
    SPLIT_8_11(17, new Point(6, 3), Spot.SPOT_08, Spot.SPOT_11),
    SPLIT_10_13(17, new Point(8, 5), Spot.SPOT_10, Spot.SPOT_13),
    SPLIT_17_20(17, new Point(12, 3), Spot.SPOT_17, Spot.SPOT_20),
    SPLIT_26_29(17, new Point(18, 3), Spot.SPOT_26, Spot.SPOT_29),
    SPLIT_28_31(17, new Point(20, 5), Spot.SPOT_28, Spot.SPOT_31),
    // === Street ===
    STREET_1_2_3(11, new Point(1, 0), Spot.SPOT_01, Spot.SPOT_02, Spot.SPOT_03),
    STREET_34_35_36(11, new Point(23, 0), Spot.SPOT_34, Spot.SPOT_35, Spot.SPOT_36),

    // === Corner ===
    CORNER_1_2_4_5(8, new Point(2, 4), Spot.SPOT_01, Spot.SPOT_02, Spot.SPOT_04, Spot.SPOT_05),
    CORNER_32_33_35_36(8, new Point(22, 2), Spot.SPOT_32, Spot.SPOT_33, Spot.SPOT_35, Spot.SPOT_36),

    // === Six Line ===
    SIX_LINE_1_2_3_4_5_6(5, new Point(2, 0),
            Spot.SPOT_01, Spot.SPOT_02, Spot.SPOT_03, Spot.SPOT_04, Spot.SPOT_05, Spot.SPOT_06),
    SIX_LINE_31_32_33_34_35_36(5, new Point(22, 0),
            Spot.SPOT_31, Spot.SPOT_32, Spot.SPOT_33, Spot.SPOT_34, Spot.SPOT_35, Spot.SPOT_36),

    // === Special with Zero ===
    TOP_LINE_OR_BASKET(6, new Point(-1, 0), EnumSet.of(RouletteType.AMERICAN_STYLE),
            Spot.SPOT_0, Spot.SPOT_00, Spot.SPOT_01, Spot.SPOT_02, Spot.SPOT_03),
    TRIO_0_1_2(11, new Point(0, 4), EnumSet.of(RouletteType.EUROPEAN_STYLE),
            Spot.SPOT_0, Spot.SPOT_01, Spot.SPOT_02),
    TRIO_0_2_3(11, new Point(0, 2), EnumSet.of(RouletteType.EUROPEAN_STYLE),
            Spot.SPOT_0, Spot.SPOT_02, Spot.SPOT_03),
    SPLIT_0_00(17, new Point(-1, 4), EnumSet.of(RouletteType.AMERICAN_STYLE), Spot.SPOT_0, Spot.SPOT_00),
    SPLIT_0_2(17, new Point(0, 3), EnumSet.of(RouletteType.EUROPEAN_STYLE), Spot.SPOT_0, Spot.SPOT_02),
    STREET_0_00_2(11, new Point(-1, 2), EnumSet.of(RouletteType.AMERICAN_STYLE),
            Spot.SPOT_0, Spot.SPOT_00, Spot.SPOT_02),
    CORNER_0_1_2_3(8, new Point(0, 0), EnumSet.of(RouletteType.EUROPEAN_STYLE),
            Spot.SPOT_0, Spot.SPOT_01, Spot.SPOT_02, Spot.SPOT_03),

    // === Outside Bets ===
    FIRST_COLUMN(2, new Point(24, 5), Spot.FIRST_COLUMN_SPOTS),
    SECOND_COLUMN(2, new Point(24, 3), Spot.SECOND_COLUMN_SPOTS),
    THIRD_COLUMN(2, new Point(24, 1), Spot.THIRD_COLUMN_SPOTS),
    FIRST_DOZEN(2, new Point(7, -2), Spot.FIRST_DOZEN_SPOTS),
    SECOND_DOZEN(2, new Point(15, -2), Spot.SECOND_DOZEN_SPOTS),
    THIRD_DOZEN(2, new Point(23, -2), Spot.THIRD_DOZEN_SPOTS),
    ONE_TO_EIGHTEEN(1, new Point(3, -4), Spot.LOW_SPOTS),
    EVEN(1, new Point(7, -4), Spot.EVENS),
    RED(1, new Point(11, -4), Spot.REDS),
    BLACK(1, new Point(15, -4), Spot.BLACKS),
    ODD(1, new Point(19, -4), Spot.ODDS),
    NINETEEN_TO_THIRTY_SIX(1, new Point(23, -4), Spot.HIGH_SPOTS);

    @Getter
    private final int payout;
    @Getter
    private final Point tablePosition;
    private final EnumSet<RouletteType> availableIn;
    @Getter
    private final Set<Spot> winningSpots;

    private static final Map<Spot, BetType> STRAIGHT_UP_MAP = new EnumMap<>(Spot.class);

    static {
        STRAIGHT_UP_MAP.put(Spot.SPOT_0, ZERO);
        STRAIGHT_UP_MAP.put(Spot.SPOT_00, DOUBLE_ZERO);
        for (Spot spot : Spot.values()) {
            if (spot.getNumber() > 0) {
                String betTypeName = "STRAIGHT_UP_" + spot.getNumber();
                try {
                    STRAIGHT_UP_MAP.put(spot, BetType.valueOf(betTypeName));
                } catch (IllegalArgumentException e) {
                    System.err.println("Не удалось найти BetType для " + betTypeName);
                }
            }
        }
    }

    // Constructors
    BetType(int payout, Point tablePosition, Spot... spots) {
        this(payout, tablePosition, EnumSet.allOf(RouletteType.class), spots);
    }

    BetType(int payout, Point tablePosition, EnumSet<RouletteType> availableIn, Spot... spots) {
        this.payout = payout;
        this.tablePosition = tablePosition;
        this.availableIn = availableIn;
        this.winningSpots = spots.length == 0 ? EnumSet.noneOf(Spot.class) : EnumSet.copyOf(List.of(spots));
    }

    BetType(int payout, Point tablePosition, Set<Spot> spots) {
        this(payout, tablePosition, EnumSet.allOf(RouletteType.class), spots);
    }

    BetType(int payout, Point tablePosition, EnumSet<RouletteType> availableIn, Set<Spot> spots) {
        this.payout = payout;
        this.tablePosition = tablePosition;
        this.availableIn = availableIn;
        this.winningSpots = (spots == null || spots.isEmpty())
                ? EnumSet.noneOf(Spot.class)
                : EnumSet.copyOf(spots);
    }

    public static List<BetType> getAvailableList(RouletteType rouletteType) {
        return Stream.of(values())
                .filter(bet -> bet.isAvailableIn(rouletteType))
                .collect(Collectors.toList());
    }

    public boolean isAvailableIn(RouletteType rouletteType) {
        return this.availableIn.contains(rouletteType);
    }

    public int getReturnMultiplier() {
        return payout + 1;
    }

    public double getPhysicalDistance(BetType otherBetType) {
        if (otherBetType == null) {
            throw new IllegalArgumentException("otherBetType не может быть null");
        }
        return this.tablePosition.distance(otherBetType.tablePosition);
    }

    public static BetType getStraightUpBetFor(Spot spot) {
        BetType bet = STRAIGHT_UP_MAP.get(spot);
        if (bet == null) {
            throw new IllegalArgumentException("Нет ставки 'Straight Up' для номера: " + spot);
        }
        return bet;
    }
}
