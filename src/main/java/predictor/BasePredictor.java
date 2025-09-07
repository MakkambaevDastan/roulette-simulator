package predictor;

import application.Context;
import model.BetTypePrediction;
import model.ColorPrediction;
import model.SpotPrediction;

import java.util.Collections;
import java.util.List;

public abstract class BasePredictor {

    public List<SpotPrediction> getNextSpotPredictionList(Context context) {
        return Collections.emptyList();
    }

    public List<BetTypePrediction> getNextBetTypePredictionList(Context context) {
        return Collections.emptyList();
    }

    public ColorPrediction getNextColorPrediction(Context context) {
        return ColorPrediction.builder()
                .red(0)
                .black(0)
                .green(0)
                .build();
    }
}