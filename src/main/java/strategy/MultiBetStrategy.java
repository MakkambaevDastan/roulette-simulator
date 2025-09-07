package strategy;

import application.Context;
import model.Bet;
import predictor.BasePredictor;
import predictor.MarkovPredictor2;
import utils.PredictorHelper;

import java.util.List;

public class MultiBetStrategy extends BaseStrategy {

    private static final BasePredictor PREDICTOR = PredictorHelper.getInstance(MarkovPredictor2.class);

    public MultiBetStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return MultiBetStrategy.class.getSimpleName();
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