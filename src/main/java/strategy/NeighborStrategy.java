package strategy;

import application.Context;
import enums.RouletteType;
import enums.Spot;
import model.Bet;
import model.SpotPrediction;
import predictor.BasePredictor;
import predictor.MarkovPredictor2;
import utils.BetHelper;
import utils.PredictorHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NeighborStrategy extends BaseStrategy {

    private static final BasePredictor PREDICTOR = PredictorHelper.getInstance(MarkovPredictor2.class);

    private static final Map<Integer, Set<Spot>> NEIGHBOR_GROUPS = new HashMap<>();

    static {
        NEIGHBOR_GROUPS.put(1, Set.of(
                Spot.SPOT_32, Spot.SPOT_15, Spot.SPOT_19, Spot.SPOT_04, Spot.SPOT_21,
                Spot.SPOT_02, Spot.SPOT_25, Spot.SPOT_17, Spot.SPOT_34
        ));
        NEIGHBOR_GROUPS.put(2, Set.of(
                Spot.SPOT_06, Spot.SPOT_27, Spot.SPOT_13, Spot.SPOT_36, Spot.SPOT_11,
                Spot.SPOT_30, Spot.SPOT_08, Spot.SPOT_23, Spot.SPOT_10
        ));
        NEIGHBOR_GROUPS.put(3, Set.of(
                Spot.SPOT_05, Spot.SPOT_24, Spot.SPOT_16, Spot.SPOT_33, Spot.SPOT_01,
                Spot.SPOT_20, Spot.SPOT_14, Spot.SPOT_31, Spot.SPOT_09
        ));
        NEIGHBOR_GROUPS.put(4, Set.of(
                Spot.SPOT_22, Spot.SPOT_18, Spot.SPOT_29, Spot.SPOT_07, Spot.SPOT_28,
                Spot.SPOT_12, Spot.SPOT_35, Spot.SPOT_03, Spot.SPOT_26, Spot.SPOT_0
        ));
    }

    private static final double MIN_PROBABILITY_THRESHOLD = 0.1;

    public NeighborStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return NeighborStrategy.class.getSimpleName();
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        if (context.getRouletteType() != RouletteType.EUROPEAN_STYLE) {
            return List.of();
        }

        Map<Integer, Double> groupProbabilities = new HashMap<>();
        groupProbabilities.put(1, 0.0);
        groupProbabilities.put(2, 0.0);
        groupProbabilities.put(3, 0.0);
        groupProbabilities.put(4, 0.0);

        for (SpotPrediction prediction : PREDICTOR.getNextSpotPredictionList(context)) {
            for (Map.Entry<Integer, Set<Spot>> entry : NEIGHBOR_GROUPS.entrySet()) {
                if (entry.getValue().contains(prediction.spot())) {
                    groupProbabilities.merge(entry.getKey(), prediction.probability(), Double::sum);
                    break;
                }
            }
        }

        Map.Entry<Integer, Double> maxEntry = groupProbabilities.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new IllegalStateException("No probabilities calculated"));

        if (maxEntry.getValue() < MIN_PROBABILITY_THRESHOLD) {
            return List.of();
        }

        return NEIGHBOR_GROUPS.get(maxEntry.getKey()).stream()
                .map(spot -> Bet.builder()
                        .type(BetHelper.getStraightUpBetType(spot))
                        .value(context.getMin())
                        .build())
                .toList();
    }
}