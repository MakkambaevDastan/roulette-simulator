package predictor;

import application.Context;
import enums.BetType;
import enums.Spot;
import model.BetTypePrediction;
import model.ColorPrediction;
import model.SpotPrediction;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomPredictor extends BasePredictor {

    private static final double PROBABILITY = 1.0;
    private final Random random;

    public RandomPredictor() {
        random = new Random();
    }

    @Override
    public List<SpotPrediction> getNextSpotPredictionList(Context context) {
        validateContext(context);
        List<Spot> availableSpots = Spot.getAvailableList(context.getRouletteType());
        if (availableSpots.isEmpty()) {
            return List.of();
        }

        List<SpotPrediction> predictions = availableSpots.stream()
                .map(spot -> SpotPrediction.builder()
                        .spot(spot)
                        .probability(random.nextDouble())
                        .build())
                .collect(Collectors.toList());

        return normalizeProbabilities(predictions);
    }

    @Override
    public List<BetTypePrediction> getNextBetTypePredictionList(Context context) {
        validateContext(context);
        List<BetType> availableBetTypes = BetType.getAvailableList(context.getRouletteType());
        if (availableBetTypes.isEmpty()) {
            return List.of();
        }

        List<BetTypePrediction> predictions = availableBetTypes.stream()
                .map(betType -> BetTypePrediction.builder()
                        .type(betType)
                        .probability(random.nextDouble())
                        .build())
                .collect(Collectors.toList());

        return normalizeBetTypeProbabilities(predictions);
    }

    @Override
    public ColorPrediction getNextColorPrediction(Context context) {
        validateContext(context);
        double probability = PROBABILITY / 3;
        return ColorPrediction.builder()
                .red(probability)
                .black(probability)
                .green(probability)
                .build();
    }

    private List<SpotPrediction> normalizeProbabilities(List<SpotPrediction> predictions) {
        double totalProbability = predictions.stream()
                .mapToDouble(SpotPrediction::probability)
                .sum();
        if (totalProbability <= 0 || Double.isNaN(totalProbability) || Double.isInfinite(totalProbability)) {
            double uniformProbability = PROBABILITY / predictions.size();
            return predictions.stream()
                    .map(p -> SpotPrediction.builder()
                            .spot(p.spot())
                            .probability(uniformProbability)
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

    private List<BetTypePrediction> normalizeBetTypeProbabilities(List<BetTypePrediction> predictions) {
        double totalProbability = predictions.stream()
                .mapToDouble(BetTypePrediction::probability)
                .sum();
        if (totalProbability <= 0 || Double.isNaN(totalProbability) || Double.isInfinite(totalProbability)) {
            double uniformProbability = PROBABILITY / predictions.size();
            return predictions.stream()
                    .map(p -> BetTypePrediction.builder()
                            .type(p.type())
                            .probability(uniformProbability)
                            .build())
                    .collect(Collectors.toList());
        }
        return predictions.stream()
                .map(p -> BetTypePrediction.builder()
                        .type(p.type())
                        .probability(p.probability() / totalProbability)
                        .build())
                .collect(Collectors.toList());
    }

    private void validateContext(Context context) {
        Objects.requireNonNull(context, "Context must not be null");
        Objects.requireNonNull(context.getRouletteType(), "Roulette type must not be null");
    }
}