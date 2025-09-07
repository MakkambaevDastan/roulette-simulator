package strategy;

import application.Context;
import enums.BetType;
import model.Bet;
import model.SpotPrediction;
import predictor.BasePredictor;
import predictor.CountPredictor2;
import utils.BetHelper;
import utils.PredictorHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StraightUpStrategy2 extends BaseStrategy {

    private static final BasePredictor PREDICTOR = PredictorHelper.getInstance(CountPredictor2.class);

    public StraightUpStrategy2(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return "ストレート複数賭け(予測器を使用)";
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        final double PROBABILITY_THRESHOLD = 0.03;
        final double BUDGET_FRACTION = 0.1;
        long limit = (long) (Math.max(curBalance, context.getStart()) * BUDGET_FRACTION);
        List<SpotPrediction> predictions = PREDICTOR.getNextSpotPredictionList(context);
        return predictions.stream()
                .filter(prediction -> prediction.probability() > PROBABILITY_THRESHOLD)
                .map(prediction -> {
                    BetType type = BetHelper.getStraightUpBetType(prediction.spot());
                    long value = Math.max(context.getMin(), (long) (limit * prediction.probability()));
                    return Bet.builder()
                            .type(type)
                            .value(value)
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();
    }
}