package predictor;

import application.Context;
import enums.BetType;
import enums.Spot;
import model.BetTypePrediction;
import model.ColorPrediction;
import model.SpotPrediction;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PatternPredictor extends BasePredictor {

    private static final double HOT_SPOT_WEIGHT = 1.2;
    private static final double COLD_SPOT_WEIGHT = 0.8;
    private static final double NEVER_SEEN_WEIGHT = 0.5;
    private static final double CONSECUTIVE_WEIGHT = 1.3;
    private static final double REPEATING_PATTERN_WEIGHT = 1.1;
    private static final double LAST_SPOT_WEIGHT = 0.7;
    private static final int RECENT_WINDOW = 10;
    private static final int COLD_THRESHOLD = 15;
    private static final int MIN_PATTERN_SIZE = 4;
    private static final double DEFAULT_PROBABILITY = 0.0;

    @Override
    public List<SpotPrediction> getNextSpotPredictionList(Context context) {
        validateContext(context);
        if (context.getSpotHistory().isEmpty()) {
            return List.of();
        }

        EnumMap<Spot, Double> baseFrequency = calculateBaseFrequency(context);
        EnumMap<Spot, Double> hotColdWeights = calculateHotColdWeights(context);
        EnumMap<Spot, Double> patternWeights = calculatePatternWeights(context);

        List<Spot> availableSpots = Spot.getAvailableList(context.getRouletteType());
        if (availableSpots.isEmpty()) {
            return List.of();
        }

        List<SpotPrediction> predictions = availableSpots.stream()
                .map(spot -> {
                    double baseProbability = baseFrequency.getOrDefault(spot, DEFAULT_PROBABILITY);
                    double hotColdWeight = hotColdWeights.getOrDefault(spot, 1.0);
                    double patternWeight = patternWeights.getOrDefault(spot, 1.0);
                    double finalProbability = baseProbability * hotColdWeight * patternWeight;
                    return SpotPrediction.builder()
                            .spot(spot)
                            .probability(finalProbability)
                            .build();
                })
                .filter(prediction -> prediction.probability() > 0)
                .collect(Collectors.toList());

        return normalizeProbabilities(predictions);
    }

    @Override
    public List<BetTypePrediction> getNextBetTypePredictionList(Context context) {
        validateContext(context);
        return List.of();
    }

    @Override
    public ColorPrediction getNextColorPrediction(Context context) {
        validateContext(context);
        return ColorPrediction.builder()
                .red(DEFAULT_PROBABILITY)
                .black(DEFAULT_PROBABILITY)
                .green(DEFAULT_PROBABILITY)
                .build();
    }

    private EnumMap<Spot, Double> calculateBaseFrequency(Context context) {
        EnumMap<Spot, Double> frequency = new EnumMap<>(Spot.class);
        Map<Spot, Integer> spotCount = context.getSpotFrequencies();
        long totalCount = spotCount.values().stream().mapToLong(Integer::longValue).sum();

        if (totalCount == 0) {
            return frequency;
        }

        spotCount.forEach((spot, count) -> frequency.put(spot, (double) count / totalCount));
        return frequency;
    }

    private EnumMap<Spot, Double> calculateHotColdWeights(Context context) {
        EnumMap<Spot, Double> weights = new EnumMap<>(Spot.class);
        List<Spot> history = context.getSpotHistory();
        int historySize = history.size();
        int window = Math.min(RECENT_WINDOW, historySize);

        Map<Spot, Integer> recentCount = new HashMap<>();
        Map<Spot, Integer> lastAppearance = new HashMap<>();

        for (int i = 0; i < historySize; i++) {
            Spot spot = history.get(i);
            lastAppearance.put(spot, i);
            if (i >= historySize - window) {
                recentCount.merge(spot, 1, Integer::sum);
            }
        }

        for (Spot spot : Spot.getAvailableList(context.getRouletteType())) {
            double weight = 1.0;
            int recentAppearances = recentCount.getOrDefault(spot, 0);
            if (recentAppearances > 1) {
                weight *= HOT_SPOT_WEIGHT;
            }
            Integer lastPos = lastAppearance.get(spot);
            if (lastPos != null) {
                int gapSinceLastAppearance = historySize - lastPos - 1;
                if (gapSinceLastAppearance > COLD_THRESHOLD) {
                    weight *= COLD_SPOT_WEIGHT;
                }
            } else {
                weight *= NEVER_SEEN_WEIGHT;
            }
            weights.put(spot, weight);
        }
        return weights;
    }

    private EnumMap<Spot, Double> calculatePatternWeights(Context context) {
        EnumMap<Spot, Double> weights = new EnumMap<>(Spot.class);
        List<Spot> availableSpots = Spot.getAvailableList(context.getRouletteType());
        if (context.getSpotHistory().size() < MIN_PATTERN_SIZE - 1) {
            availableSpots.forEach(spot -> weights.put(spot, 1.0));
            return weights;
        }

        Spot lastSpot = context.getLastSpot();
        for (Spot spot : availableSpots) {
            double weight = 1.0;
            if (isConsecutiveNumber(lastSpot, spot)) {
                weight *= CONSECUTIVE_WEIGHT;
            }
            if (hasRepeatingPattern(context, spot)) {
                weight *= REPEATING_PATTERN_WEIGHT;
            }
            if (lastSpot == spot) {
                weight *= LAST_SPOT_WEIGHT;
            }
            weights.put(spot, weight);
        }
        return weights;
    }

    private boolean isConsecutiveNumber(Spot spot1, Spot spot2) {
        int num1 = spot1.getNumber();
        int num2 = spot2.getNumber();
        return num1 > 0 && num2 > 0 && Math.abs(num1 - num2) == 1;
    }

    private boolean hasRepeatingPattern(Context context, Spot targetSpot) {
        List<Spot> history = context.getSpotHistory();
        if (history.size() < MIN_PATTERN_SIZE) {
            return false;
        }
        int size = history.size();
        return history.get(size - 3) == history.get(size - 1) && targetSpot == history.get(size - 2);
    }

    private List<SpotPrediction> normalizeProbabilities(List<SpotPrediction> predictions) {
        double totalProbability = predictions.stream()
                .mapToDouble(SpotPrediction::probability)
                .sum();
        if (totalProbability <= 0 || Double.isNaN(totalProbability) || Double.isInfinite(totalProbability)) {
            return predictions.stream()
                    .map(p -> SpotPrediction.builder()
                            .spot(p.spot())
                            .probability(DEFAULT_PROBABILITY)
                            .build())
                    .collect(Collectors.toList());
        }
        return predictions.stream()
                .map(p -> SpotPrediction.builder()
                        .spot(p.spot())
                        .probability(p.probability() / totalProbability)
                        .build())
                .collect(Collectors.toList());
    }

    private void validateContext(Context context) {
        Objects.requireNonNull(context, "Context must not be null");
        Objects.requireNonNull(context.getSpotHistory(), "Spot history must not be null");
        Objects.requireNonNull(context.getLastSpot(), "Last spot must not be null");
        Objects.requireNonNull(context.getRouletteType(), "Roulette type must not be null");
        Objects.requireNonNull(context.getSpotFrequencies(), "Spot frequencies must not be null");
    }
}