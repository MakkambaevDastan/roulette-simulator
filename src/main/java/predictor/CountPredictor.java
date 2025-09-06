package predictor;

import application.RouletteContext;
import enums.BetType;
import enums.Spot;
import model.BetTypePrediction;
import model.ColorPrediction;
import model.SpotPrediction;
import utils.BetHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountPredictor extends BasePredictor {

    private Map<Spot, Long> spotCountMap = new HashMap<>();

    private Map<BetType, Long> betTypeCountMap = new HashMap<>();

    private long totalCount = 0;

    @Override
    public List<SpotPrediction> getNextSpotPredictionList(RouletteContext rouletteContext) {
        updateParameter(rouletteContext);

        List<SpotPrediction> spotPredictionList = new ArrayList<>();
        for (Spot spot : Spot.getAvailableList(rouletteContext.rouletteType)) {
            if (spotCountMap.containsKey(spot)) {
                spotPredictionList
                        .add(new SpotPrediction(spot, ((double) spotCountMap.get(spot)) / ((double) totalCount)));
            } else {
                spotPredictionList.add(new SpotPrediction(spot, 0));
            }
        }
        return spotPredictionList;
    }

    @Override
    public List<BetTypePrediction> getNextBetTypePredictionList(RouletteContext rouletteContext) {
        updateParameter(rouletteContext);

        List<BetTypePrediction> betTypePredictionList = new ArrayList<>();
        for (BetType betType : BetType.getAvailableList(rouletteContext.rouletteType)) {
            if (betTypeCountMap.containsKey(betType)) {
                betTypePredictionList.add(new BetTypePrediction(betType,
                        ((double) betTypeCountMap.get(betType)) / ((double) totalCount)));
            } else {
                betTypePredictionList.add(new BetTypePrediction(betType, 0));
            }
        }
        return betTypePredictionList;
    }

    @Override
    public ColorPrediction getNextColorPrediction(RouletteContext rouletteContext) {
        double redCount = 0;
        double blackCount = 0;
        double greenCount = 0;
        for (Spot spot : Spot.getAvailableList(rouletteContext.rouletteType)) {
            if (spotCountMap.containsKey(spot)) {
                if (spot.isRed()) {
                    redCount += spotCountMap.get(spot);
                } else if (spot.isBlack()) {
                    blackCount += spotCountMap.get(spot);
                } else if (spot.isGreen()) {
                    greenCount += spotCountMap.get(spot);
                }
            }
        }
        double totalCount = redCount + blackCount + greenCount;

        return new ColorPrediction(redCount / totalCount, blackCount / totalCount, greenCount / totalCount);
    }

    private void updateParameter(RouletteContext rouletteContext) {
        if (spotCountMap.containsKey(rouletteContext.getLastSpot())) {
            spotCountMap.put(rouletteContext.getLastSpot(), spotCountMap.get(rouletteContext.getLastSpot()) + 1L);
        } else {
            spotCountMap.put(rouletteContext.getLastSpot(), 1L);
        }

        for (BetType betType : BetType.getAvailableList(rouletteContext.rouletteType)) {
            if (BetHelper.isWin(betType, rouletteContext.getLastSpot())) {
                if (betTypeCountMap.containsKey(betType)) {
                    betTypeCountMap.put(betType, betTypeCountMap.get(betType) + 1L);
                } else {
                    betTypeCountMap.put(betType, 1L);
                }
            }
        }
        totalCount++;
    }
}