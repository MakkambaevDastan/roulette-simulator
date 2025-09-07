package predictor;

import application.Context;
import enums.BetType;
import enums.Spot;
import model.BetTypePrediction;
import model.ColorPrediction;
import model.SpotPrediction;
import utils.BetHelper;
import utils.LogHelper;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MarkovPredictor extends BasePredictor {

    private static final int MIN_SIZE = 2;
    private static final double PROBABILITY = 0.0;

    private final Map<Spot, Map<Spot, Long>> spotMap;
    private final EnumMap<BetType, Map<BetType, Long>> typeMap;
    private long count;

    public MarkovPredictor() {
        spotMap = new HashMap<>();
        typeMap = new EnumMap<>(BetType.class);
        count = 0;
    }

    @Override
    public List<SpotPrediction> getNextSpotPredictionList(Context context) {
        validateContext(context);
        if (context.getSpotHistory().size() < MIN_SIZE) {
            return List.of();
        }

        updateParameter(context);
        Map<Spot, Long> transitions = spotMap.getOrDefault(context.getLastSpot(), Map.of());

        List<Spot> availableSpots = Spot.getAvailableList(context.getRouletteType());
        if (availableSpots.isEmpty()) {
            return List.of();
        }

        return availableSpots.stream()
                .map(spot -> SpotPrediction.builder()
                        .spot(spot)
                        .probability(calculateProbability(transitions.getOrDefault(spot, 0L)))
                        .build())
                .filter(prediction -> prediction.probability() > 0)
                .toList();
    }

    @Override
    public List<BetTypePrediction> getNextBetTypePredictionList(Context context) {
        validateContext(context);
        if (context.getSpotHistory().size() < MIN_SIZE) {
            return List.of();
        }

        updateParameter(context);
        List<BetType> lastSpotBetTypes = BetType.getAvailableList(context.getRouletteType()).stream()
                .filter(type -> BetHelper.isWin(type, context.getLastSpot()))
                .toList();

        List<BetType> availableTypes = BetType.getAvailableList(context.getRouletteType());
        if (availableTypes.isEmpty()) {
            return List.of();
        }

        return lastSpotBetTypes.stream()
                .flatMap(lastType -> {
                    Map<BetType, Long> transitions = typeMap.getOrDefault(lastType, Map.of());
                    return availableTypes.stream()
                            .map(nextType -> BetTypePrediction.builder()
                                    .type(nextType)
                                    .probability(calculateProbability(transitions.getOrDefault(nextType, 0L)))
                                    .build())
                            .filter(prediction -> prediction.probability() > 0);
                })
                .toList();
    }

    @Override
    public ColorPrediction getNextColorPrediction(Context context) {
        validateContext(context);
        return ColorPrediction.builder()
                .red(PROBABILITY)
                .black(PROBABILITY)
                .green(PROBABILITY)
                .build();
    }

    private void updateParameter(Context context) {
        if (context.getSpotHistory().size() < MIN_SIZE) {
            return;
        }

        Spot spot1 = context.getSpotHistory().get(context.getSpotHistory().size() - 2);
        Spot spot2 = context.getLastSpot();

        spotMap.computeIfAbsent(spot1, k -> new HashMap<>())
                .merge(spot2, 1L, Long::sum);

        List<BetType> betTypeList1 = BetType.getAvailableList(context.getRouletteType()).stream()
                .filter(type -> BetHelper.isWin(type, spot1))
                .toList();
        List<BetType> betTypeList2 = BetType.getAvailableList(context.getRouletteType()).stream()
                .filter(type -> BetHelper.isWin(type, spot2))
                .toList();

        for (BetType betType1 : betTypeList1) {
            for (BetType betType2 : betTypeList2) {
                typeMap.computeIfAbsent(betType1, k -> new HashMap<>())
                        .merge(betType2, 1L, Long::sum);
            }
        }
        count++;
    }

    private void validateContext(Context context) {
        Objects.requireNonNull(context, "Context must not be null");
        Objects.requireNonNull(context.getSpotHistory(), "Spot history must not be null");
        Objects.requireNonNull(context.getLastSpot(), "Last spot must not be null");
        Objects.requireNonNull(context.getRouletteType(), "Roulette type must not be null");
    }

    private double calculateProbability(long occurrences) {
        return count == 0 ? PROBABILITY : (double) occurrences / count;
    }

    private void dumpMarkovMap() {
        LogHelper.debug("--- spotMarkovMap start ---");
        for (Map.Entry<Spot, Map<Spot, Long>> entry1 : spotMap.entrySet()) {
            for (Map.Entry<Spot, Long> entry2 : entry1.getValue().entrySet()) {
                LogHelper.debug(entry1.getKey().name() + "-" + entry2.getKey().name() + ":" + entry2.getValue());
            }
        }
        LogHelper.debug("--- spotMarkovMap end ---");
    }

    public void reset() {
        spotMap.clear();
        typeMap.clear();
        count = 0;
    }
}