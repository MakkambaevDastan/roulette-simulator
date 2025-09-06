package predictor;

import application.RouletteContext;
import enums.Spot;
import model.SpotPrediction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatternPredictor extends BasePredictor {

    @Override
    public List<SpotPrediction> getNextSpotPredictionList(RouletteContext rouletteContext) {
        List<SpotPrediction> spotPredictionList = new ArrayList<>();

        if (rouletteContext.spotHistoryList.isEmpty()) {
            return spotPredictionList;
        }

        Map<Spot, Double> baseFrequency = calculateBaseFrequency(rouletteContext);

        Map<Spot, Double> hotColdWeights = calculateHotColdWeights(rouletteContext);

        Map<Spot, Double> patternWeights = calculatePatternWeights(rouletteContext);

        for (Spot spot : Spot.getAvailableList(rouletteContext.rouletteType)) {
            double baseProbability = baseFrequency.getOrDefault(spot, 0.0);
            double hotColdWeight = hotColdWeights.getOrDefault(spot, 1.0);
            double patternWeight = patternWeights.getOrDefault(spot, 1.0);

            double finalProbability = baseProbability * hotColdWeight * patternWeight;

            if (finalProbability > 0) {
                spotPredictionList.add(new SpotPrediction(spot, finalProbability));
            }
        }

        return normalizeProbabilities(spotPredictionList);
    }

    private Map<Spot, Double> calculateBaseFrequency(RouletteContext rouletteContext) {
        Map<Spot, Double> frequency = new HashMap<>();
        Map<Spot, Integer> spotCount = rouletteContext.getSpotFrequency();
        int totalCount = rouletteContext.spotHistoryList.size();

        for (Map.Entry<Spot, Integer> entry : spotCount.entrySet()) {
            frequency.put(entry.getKey(), (double) entry.getValue() / totalCount);
        }

        return frequency;
    }

    private Map<Spot, Double> calculateHotColdWeights(RouletteContext rouletteContext) {
        Map<Spot, Double> weights = new HashMap<>();

        int recentHistorySize = Math.min(10, rouletteContext.spotHistoryList.size());
        Map<Spot, Integer> recentCount = new HashMap<>();

        for (int i = rouletteContext.spotHistoryList.size() - recentHistorySize;
             i < rouletteContext.spotHistoryList.size(); i++) {
            Spot spot = rouletteContext.spotHistoryList.get(i);
            recentCount.put(spot, recentCount.getOrDefault(spot, 0) + 1);
        }

        Map<Spot, Integer> lastAppearance = new HashMap<>();
        for (int i = 0; i < rouletteContext.spotHistoryList.size(); i++) {
            lastAppearance.put(rouletteContext.spotHistoryList.get(i), i);
        }

        for (Spot spot : Spot.getAvailableList(rouletteContext.rouletteType)) {
            double weight = 1.0;

            int recentAppearances = recentCount.getOrDefault(spot, 0);
            if (recentAppearances > 1) {
                weight *= 1.2;
            }

            Integer lastPos = lastAppearance.get(spot);
            if (lastPos != null) {
                int gapSinceLastAppearance = rouletteContext.spotHistoryList.size() - lastPos - 1;
                if (gapSinceLastAppearance > 15) {
                    weight *= 0.8;
                }
            } else {
                weight *= 0.5;
            }

            weights.put(spot, weight);
        }

        return weights;
    }

    private Map<Spot, Double> calculatePatternWeights(RouletteContext rouletteContext) {
        Map<Spot, Double> weights = new HashMap<>();

        if (rouletteContext.spotHistoryList.size() < 2) {
            return weights;
        }

        Spot lastSpot = rouletteContext.getLastSpot();

        for (Spot spot : Spot.getAvailableList(rouletteContext.rouletteType)) {
            double weight = 1.0;

            if (isConsecutiveNumber(lastSpot, spot)) {
                weight *= 1.3;
            }

            if (hasRepeatingPattern(rouletteContext, spot)) {
                weight *= 1.1;
            }

            if (lastSpot == spot) {
                weight *= 0.7;
            }

            weights.put(spot, weight);
        }

        return weights;
    }

    private boolean isConsecutiveNumber(Spot spot1, Spot spot2) {
        int num1 = spot1.getNumber();
        int num2 = spot2.getNumber();

        if (num1 <= 0 || num2 <= 0) {
            return false;
        }

        return Math.abs(num1 - num2) == 1;
    }

    private boolean hasRepeatingPattern(RouletteContext rouletteContext, Spot targetSpot) {
        if (rouletteContext.spotHistoryList.size() < 4) {
            return false;
        }

        List<Spot> recentSpots = new ArrayList<>();
        for (int i = rouletteContext.spotHistoryList.size() - 3;
             i < rouletteContext.spotHistoryList.size(); i++) {
            recentSpots.add(rouletteContext.spotHistoryList.get(i));
        }

        if (recentSpots.size() >= 3) {
            Spot firstSpot = recentSpots.get(0);
            Spot secondSpot = recentSpots.get(1);
            Spot thirdSpot = recentSpots.get(2);

            if (firstSpot == thirdSpot && targetSpot == secondSpot) {
                return true;
            }
        }

        return false;
    }

    private List<SpotPrediction> normalizeProbabilities(List<SpotPrediction> spotPredictionList) {
        double totalProbability = spotPredictionList.stream()
                .mapToDouble(pred -> pred.probability)
                .sum();

        if (totalProbability <= 0) {
            return spotPredictionList;
        }

        List<SpotPrediction> normalizedList = new ArrayList<>();
        for (SpotPrediction prediction : spotPredictionList) {
            double normalizedProbability = prediction.probability / totalProbability;
            normalizedList.add(new SpotPrediction(prediction.spot, normalizedProbability));
        }

        return normalizedList;
    }
}