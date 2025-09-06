package predictor;

import application.RouletteContext;
import model.BetTypePrediction;
import model.ColorPrediction;
import model.SpotPrediction;

import java.util.Collections;
import java.util.List;

public abstract class BasePredictor {

    public List<SpotPrediction> getNextSpotPredictionList(RouletteContext rouletteContext) {
        return Collections.emptyList();
    }

    public List<BetTypePrediction> getNextBetTypePredictionList(RouletteContext rouletteContext) {
        return Collections.emptyList();
    }

    public ColorPrediction getNextColorPrediction(RouletteContext rouletteContext) {
        return new ColorPrediction(0, 0, 0);
    }
}