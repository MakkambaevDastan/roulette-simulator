package strategy;

import application.Context;
import model.Bet;
import model.BetTypePrediction;
import predictor.BasePredictor;
import predictor.MarkovPredictor2;
import utils.PredictorHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MultiBetStrategy extends BaseStrategy {

    private static final BasePredictor PREDICTOR = PredictorHelper.getInstance(MarkovPredictor2.class);

    public MultiBetStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return "複数賭け(予測器を使用)";
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        long limit = Math.max(curBalance, context.getStart()) / 10;
        limit = Math.min(limit, context.getMax());

        final long finalLimit = limit;
        return PREDICTOR.getNextBetTypePredictionList(context).stream()
                .filter(prediction -> prediction.probability() > 0.3)
                .map(prediction -> Bet.builder()
                        .type(prediction.type())
                        .value(Math.max(context.getMin(), (long) (finalLimit * prediction.probability())))
                        .build())
                .toList();
    }
}