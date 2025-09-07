package enums;

import application.Context;
import constants.Configurations;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Spot {
    SPOT_0(0, SpotColor.GREEN),
    SPOT_00(-1, SpotColor.GREEN),
    SPOT_01(1, SpotColor.RED),
    SPOT_02(2, SpotColor.BLACK),
    SPOT_03(3, SpotColor.RED),
    SPOT_04(4, SpotColor.BLACK),
    SPOT_05(5, SpotColor.RED),
    SPOT_06(6, SpotColor.BLACK),
    SPOT_07(7, SpotColor.RED),
    SPOT_08(8, SpotColor.BLACK),
    SPOT_09(9, SpotColor.RED),
    SPOT_10(10, SpotColor.BLACK),
    SPOT_11(11, SpotColor.BLACK),
    SPOT_12(12, SpotColor.RED),
    SPOT_13(13, SpotColor.BLACK),
    SPOT_14(14, SpotColor.RED),
    SPOT_15(15, SpotColor.BLACK),
    SPOT_16(16, SpotColor.RED),
    SPOT_17(17, SpotColor.BLACK),
    SPOT_18(18, SpotColor.RED),
    SPOT_19(19, SpotColor.RED),
    SPOT_20(20, SpotColor.BLACK),
    SPOT_21(21, SpotColor.RED),
    SPOT_22(22, SpotColor.BLACK),
    SPOT_23(23, SpotColor.RED),
    SPOT_24(24, SpotColor.BLACK),
    SPOT_25(25, SpotColor.RED),
    SPOT_26(26, SpotColor.BLACK),
    SPOT_27(27, SpotColor.RED),
    SPOT_28(28, SpotColor.BLACK),
    SPOT_29(29, SpotColor.BLACK),
    SPOT_30(30, SpotColor.RED),
    SPOT_31(31, SpotColor.BLACK),
    SPOT_32(32, SpotColor.RED),
    SPOT_33(33, SpotColor.BLACK),
    SPOT_34(34, SpotColor.RED),
    SPOT_35(35, SpotColor.BLACK),
    SPOT_36(36, SpotColor.RED);

    @Getter
    private final int number;
    private final SpotColor color;

    Spot(int number, SpotColor color) {
        this.number = number;
        this.color = color;
    }

    public static final Set<Spot> REDS;
    public static final Set<Spot> BLACKS;
    public static final Set<Spot> GREENS;
    public static final Set<Spot> EVENS;
    public static final Set<Spot> ODDS;
    public static final Set<Spot> LOW_SPOTS;
    public static final Set<Spot> HIGH_SPOTS;
    public static final Set<Spot> FIRST_DOZEN_SPOTS;
    public static final Set<Spot> SECOND_DOZEN_SPOTS;
    public static final Set<Spot> THIRD_DOZEN_SPOTS;
    public static final Set<Spot> FIRST_COLUMN_SPOTS;
    public static final Set<Spot> SECOND_COLUMN_SPOTS;
    public static final Set<Spot> THIRD_COLUMN_SPOTS;

    private static final Set<Spot> FIRST_DOZEN_OR_ZERO_SPOTS;

    private static final Map<Integer, Spot> SPOT_BY_NUMBER =
            Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(Spot::getNumber, s -> s));
    private static final Map<RouletteType, List<Spot>> AVAILABLE_SPOTS_CACHE =
            new EnumMap<>(RouletteType.class);
    private static final Map<RouletteType, Spot[]> WHEEL_LAYOUTS = new EnumMap<>(RouletteType.class);
    private static final Map<RouletteType, Map<Spot, Integer>> WHEEL_POSITIONS_CACHE = new EnumMap<>(RouletteType.class);

    static {
        WHEEL_LAYOUTS.put(RouletteType.ONE_TO_36, new Spot[]{
                SPOT_01, SPOT_02, SPOT_03, SPOT_04, SPOT_05, SPOT_06, SPOT_07, SPOT_08, SPOT_09,
                SPOT_10, SPOT_11, SPOT_12, SPOT_13, SPOT_14, SPOT_15, SPOT_16, SPOT_17, SPOT_18,
                SPOT_19, SPOT_20, SPOT_21, SPOT_22, SPOT_23, SPOT_24, SPOT_25, SPOT_26, SPOT_27,
                SPOT_28, SPOT_29, SPOT_30, SPOT_31, SPOT_32, SPOT_33, SPOT_34, SPOT_35, SPOT_36
        });
        WHEEL_LAYOUTS.put(RouletteType.EUROPEAN_STYLE, new Spot[]{
                SPOT_0, SPOT_32, SPOT_15, SPOT_19, SPOT_04, SPOT_21, SPOT_02, SPOT_25,
                SPOT_17, SPOT_34, SPOT_06, SPOT_27, SPOT_13, SPOT_36, SPOT_11, SPOT_30, SPOT_08,
                SPOT_23, SPOT_10, SPOT_05, SPOT_24, SPOT_16, SPOT_33, SPOT_01, SPOT_20, SPOT_14,
                SPOT_31, SPOT_09, SPOT_22, SPOT_18, SPOT_29, SPOT_07, SPOT_28, SPOT_12, SPOT_35,
                SPOT_03, SPOT_26
        });
        WHEEL_LAYOUTS.put(RouletteType.AMERICAN_STYLE, new Spot[]{
                SPOT_0, SPOT_28, SPOT_09, SPOT_26, SPOT_30, SPOT_11, SPOT_07, SPOT_20, SPOT_32, SPOT_17,
                SPOT_05, SPOT_22, SPOT_34, SPOT_15, SPOT_03, SPOT_24, SPOT_36, SPOT_13, SPOT_01, SPOT_00,
                SPOT_27, SPOT_10, SPOT_25, SPOT_29, SPOT_12, SPOT_08, SPOT_19, SPOT_31, SPOT_18, SPOT_06,
                SPOT_21, SPOT_33, SPOT_16, SPOT_04, SPOT_23, SPOT_35, SPOT_14, SPOT_02
        });

        for (RouletteType type : RouletteType.values()) {
            AVAILABLE_SPOTS_CACHE.put(type, getAvailableSpotsUncached(type));
            Spot[] layout = WHEEL_LAYOUTS.get(type);
            if (layout != null) {
                Map<Spot, Integer> positions = new HashMap<>();
                for (int i = 0; i < layout.length; i++) {
                    positions.put(layout[i], i);
                }
                WHEEL_POSITIONS_CACHE.put(type, Collections.unmodifiableMap(positions));
            }
        }

        EnumSet<Spot> reds = EnumSet.noneOf(Spot.class);
        EnumSet<Spot> blacks = EnumSet.noneOf(Spot.class);
        EnumSet<Spot> greens = EnumSet.noneOf(Spot.class);
        EnumSet<Spot> evens = EnumSet.noneOf(Spot.class);
        EnumSet<Spot> odds = EnumSet.noneOf(Spot.class);
        EnumSet<Spot> lows = EnumSet.noneOf(Spot.class);
        EnumSet<Spot> highs = EnumSet.noneOf(Spot.class);
        EnumSet<Spot> firstDozen = EnumSet.noneOf(Spot.class);
        EnumSet<Spot> secondDozen = EnumSet.noneOf(Spot.class);
        EnumSet<Spot> thirdDozen = EnumSet.noneOf(Spot.class);
        EnumSet<Spot> firstColumn = EnumSet.noneOf(Spot.class);
        EnumSet<Spot> secondColumn = EnumSet.noneOf(Spot.class);
        EnumSet<Spot> thirdColumn = EnumSet.noneOf(Spot.class);

        for (Spot spot : values()) {
            if (spot.isRed()) reds.add(spot);
            if (spot.isBlack()) blacks.add(spot);
            if (spot.isGreen()) greens.add(spot);
            if (spot.isEven()) evens.add(spot);
            if (spot.isOdd()) odds.add(spot);
            if (spot.is1To18()) lows.add(spot);
            if (spot.is19To36()) highs.add(spot);
            if (spot.isFirstDozen()) firstDozen.add(spot);
            if (spot.isSecondDozen()) secondDozen.add(spot);
            if (spot.isThirdDozen()) thirdDozen.add(spot);
            if (spot.isFirstColumn()) firstColumn.add(spot);
            if (spot.isSecondColumn()) secondColumn.add(spot);
            if (spot.isThirdColumn()) thirdColumn.add(spot);
        }

        REDS = Collections.unmodifiableSet(reds);
        BLACKS = Collections.unmodifiableSet(blacks);
        GREENS = Collections.unmodifiableSet(greens);
        EVENS = Collections.unmodifiableSet(evens);
        ODDS = Collections.unmodifiableSet(odds);
        LOW_SPOTS = Collections.unmodifiableSet(lows);
        HIGH_SPOTS = Collections.unmodifiableSet(highs);
        FIRST_DOZEN_SPOTS = Collections.unmodifiableSet(firstDozen);
        SECOND_DOZEN_SPOTS = Collections.unmodifiableSet(secondDozen);
        THIRD_DOZEN_SPOTS = Collections.unmodifiableSet(thirdDozen);
        FIRST_COLUMN_SPOTS = Collections.unmodifiableSet(firstColumn);
        SECOND_COLUMN_SPOTS = Collections.unmodifiableSet(secondColumn);
        THIRD_COLUMN_SPOTS = Collections.unmodifiableSet(thirdColumn);

        FIRST_DOZEN_OR_ZERO_SPOTS =
                Collections.unmodifiableSet(EnumSet.of(
                        SPOT_0, SPOT_00, SPOT_01, SPOT_02, SPOT_03, SPOT_04,
                        SPOT_05, SPOT_06, SPOT_07, SPOT_08, SPOT_09, SPOT_10,
                        SPOT_11, SPOT_12
                ));
    }

    public static Spot getByNumber(int number) {
        Spot spot = SPOT_BY_NUMBER.get(number);
        if (spot == null) {
            throw new IllegalArgumentException("Не существует номера: " + number);
        }
        return spot;
    }

    private static List<Spot> getAvailableSpotsUncached(RouletteType rouletteType) {
        Stream<Spot> stream = Arrays.stream(values());
        if (rouletteType == RouletteType.ONE_TO_36) {
            stream = stream.filter(s -> s != SPOT_0 && s != SPOT_00);
        } else if (rouletteType == RouletteType.EUROPEAN_STYLE) {
            stream = stream.filter(s -> s != SPOT_00);
        }
        return stream.toList();
    }

    public static List<Spot> getAvailableList(RouletteType rouletteType) {
        return AVAILABLE_SPOTS_CACHE.getOrDefault(rouletteType, Collections.emptyList());
    }

    public static Spot getRandomNextSpot(Context context) {
        List<Spot> availableSpots = getAvailableList(context.getRouletteType());
        return switch (context.getSpotGenerateType()) {
            case RANDOM -> getRandomSpotFromList(availableSpots);
            case ROTATION_NUMBER -> availableSpots.get((int) (context.getCurrentLoopCount() % availableSpots.size()));
            case ROTATION_WHEEL -> {
                Spot[] wheel = WHEEL_LAYOUTS.get(context.getRouletteType());
                yield wheel[(int) (context.getCurrentLoopCount() % wheel.length)];
            }
            case RANDOM_RED_ONLY -> getRandomSpotFromIntersection(availableSpots, REDS);
            case RANDOM_BLACK_ONLY -> getRandomSpotFromIntersection(availableSpots, BLACKS);
            case RANDOM_EXCEPT_ONE -> getRandomSpotWithFilter(availableSpots, s -> s != SPOT_01);
        };
    }

    private static Spot getRandomSpotFromList(List<Spot> spots) {
        if (spots == null || spots.isEmpty()) {
            throw new IllegalStateException("Невозможно выбрать случайное поле из пустого списка.");
        }
        return spots.get(Configurations.RANDOM.nextInt(spots.size()));
    }

    private static Spot getRandomSpotWithFilter(List<Spot> spots, Predicate<Spot> filter) {
        List<Spot> filteredSpots = spots.stream().filter(filter).collect(Collectors.toList());
        return getRandomSpotFromList(filteredSpots);
    }

    private static Spot getRandomSpotFromIntersection(List<Spot> source, Set<Spot> subset) {
        List<Spot> intersection = source.stream().filter(subset::contains).collect(Collectors.toList());
        return getRandomSpotFromList(intersection);
    }

    public boolean isRed() {
        return color == SpotColor.RED;
    }

    public boolean isBlack() {
        return color == SpotColor.BLACK;
    }

    public boolean isGreen() {
        return color == SpotColor.GREEN;
    }

    public boolean isEven() {
        return number > 0 && number % 2 == 0;
    }

    public boolean isOdd() {
        return number > 0 && number % 2 != 0;
    }

    public boolean isFirstDozen() {
        return number >= 1 && number <= 12;
    }

    public boolean isSecondDozen() {
        return number >= 13 && number <= 24;
    }

    public boolean isThirdDozen() {
        return number >= 25 && number <= 36;
    }

    public boolean isFirstColumn() {
        return number > 0 && number % 3 == 1;
    }

    public boolean isSecondColumn() {
        return number > 0 && number % 3 == 2;
    }

    public boolean isThirdColumn() {
        return number > 0 && number % 3 == 0;
    }

    public boolean is1To18() {
        return number >= 1 && number <= 18;
    }

    public boolean is19To36() {
        return number >= 19 && number <= 36;
    }

    public int getWheelPosition(RouletteType rouletteType) {
        return WHEEL_POSITIONS_CACHE.getOrDefault(rouletteType, Collections.emptyMap()).getOrDefault(this, -1);
    }

    public int getPhysicalDistance(Spot otherSpot, RouletteType rouletteType) {
        Objects.requireNonNull(otherSpot, "otherSpot не может быть null");
        int thisPos = getWheelPosition(rouletteType);
        int otherPos = otherSpot.getWheelPosition(rouletteType);

        if (thisPos == -1 || otherPos == -1) {
            throw new IllegalArgumentException("Одно или оба поля недоступны в указанном типе рулетки.");
        }

        int wheelSize = WHEEL_LAYOUTS.get(rouletteType).length;
        int diff = Math.abs(thisPos - otherPos);

        return Math.min(diff, wheelSize - diff);
    }

    public boolean isFirstDozenOrZeroAndDoubleZero() {
        return FIRST_DOZEN_OR_ZERO_SPOTS.contains(this);
    }
}