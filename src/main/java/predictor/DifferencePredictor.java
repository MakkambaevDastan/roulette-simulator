package predictor;

import application.Context;
import enums.Spot;
import model.SpotPrediction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DifferencePredictor extends BasePredictor {

    @Override
    public List<SpotPrediction> getNextSpotPredictionList(Context context) {
        Objects.requireNonNull(context, "Context must not be null");
        Objects.requireNonNull(context.getSpotHistory(), "Spot history must not be null");
        Objects.requireNonNull(context.getLastSpot(), "Last spot must not be null");
        if (context.getSpotHistory().isEmpty()) {
            return List.of();
        }
        double average = context.getSpotHistory().stream()
                .mapToInt(Spot::getNumber)
                .average()
                .orElse(0.0);
        int lastSpotNumber = context.getLastSpot().getNumber();
        return List.of(SpotPrediction.builder()
                        .spot(Spot.getByNumber((int) (lastSpotNumber + average)))
                        .probability(0.5)
                        .build(),
                SpotPrediction.builder()
                        .spot(Spot.getByNumber((int) (lastSpotNumber - average)))
                        .probability(0.5)
                        .build());
    }
}