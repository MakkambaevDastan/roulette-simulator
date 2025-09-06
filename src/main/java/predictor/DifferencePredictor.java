package predictor;

import application.RouletteContext;
import enums.Spot;
import model.SpotPrediction;

import java.util.ArrayList;
import java.util.List;

public class DifferencePredictor extends BasePredictor {

    @Override
    public List<SpotPrediction> getNextSpotPredictionList(RouletteContext rouletteContext) {
        List<SpotPrediction> spotPredictionList = new ArrayList<>();
        if (!rouletteContext.spotHistoryList.isEmpty()) {
            int sumNumber = 0;
            double averageDifference = 0;
            for (Spot spot : rouletteContext.spotHistoryList) {
                sumNumber += spot.getNumber();
            }
            averageDifference = sumNumber / rouletteContext.spotHistoryList.size();

            try {
                Spot spot = Spot.getByNumber((int) (rouletteContext.getLastSpot().getNumber() + averageDifference));
                spotPredictionList.add(new SpotPrediction(spot, 0.5));
            } catch (Exception e) {
            }
            try {
                Spot spot = Spot.getByNumber((int) (rouletteContext.getLastSpot().getNumber() - averageDifference));
                spotPredictionList.add(new SpotPrediction(spot, 0.5));
            } catch (Exception e) {
            }
        }
        return spotPredictionList;
    }
}