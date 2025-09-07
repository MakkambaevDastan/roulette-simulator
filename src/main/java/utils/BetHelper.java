package utils;

import enums.BetType;
import enums.Spot;
import model.Bet;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BetHelper {

    private BetHelper() {
        throw new UnsupportedOperationException("Этот класс не предназначен для инстанцирования.");
    }

    public static long getTotalBetValue(List<Bet> betList) {
        return (betList == null || betList.isEmpty()) ? 0 :
                betList.stream()
                        .mapToLong(Bet::value)
                        .sum();
    }

    public static long getTotalPayout(List<Bet> betList, Spot spot) {
        Objects.requireNonNull(spot, "Выпавший номер (spot) не может быть null.");
        return (betList == null || betList.isEmpty()) ? 0 :
                betList.stream()
                        .filter(bet -> isWin(bet, spot))
                        .mapToLong(bet -> bet.value() * bet.type().getReturnMultiplier())
                        .sum();
    }

    public static boolean hasWin(List<Bet> betList, Spot spot) {
        Objects.requireNonNull(spot, "Выпавший номер (spot) не может быть null.");
        return betList != null && !betList.isEmpty() && betList.stream().anyMatch(bet -> isWin(bet, spot));
    }

    public static boolean isWin(Bet bet, Spot spot) {
        Objects.requireNonNull(bet, "Ставка (bet) не может быть null.");
        Objects.requireNonNull(spot, "Выпавший номер (spot) не может быть null.");
        return isWin(bet.type(), spot);
    }

    public static boolean isWin(BetType type, Spot spot) {
        Objects.requireNonNull(type, "Тип ставки (type) не может быть null.");
        Objects.requireNonNull(spot, "Выпавший номер (spot) не может быть null.");
        return type.getWinningSpots().contains(spot);
    }

    public static BetType getStraightUpBetType(Spot spot) {
        Objects.requireNonNull(spot, "Выпавший номер (spot) не может быть null.");
        return BetType.getStraightUpBetFor(spot);
    }

    public static List<BetType> getAllCoveringBets(Spot spot) {
        Objects.requireNonNull(spot, "Выпавший номер (spot) не может быть null.");
        return Stream.of(BetType.values())
                .filter(betType -> betType.getWinningSpots().contains(spot))
                .collect(Collectors.toList());
    }
}