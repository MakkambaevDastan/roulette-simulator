package strategy;

import application.Context;
import enums.BetType;
import model.Bet;
import model.SpotPrediction;
import predictor.BasePredictor;
import predictor.PatternPredictor;
import utils.BetHelper;
import utils.PredictorHelper;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class PatternStrategy extends BaseStrategy {

    private static final BasePredictor PREDICTOR = PredictorHelper.getInstance(PatternPredictor.class);
    private static final double PROBABILITY_THRESHOLD = 0.02;
    private static final int MAX_BETS = 5;
    private static final double BUDGET_FRACTION = 0.1;
    private static final double VALUE_MULTIPLIER = 2.0;

    public PatternStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return PatternStrategy.class.getSimpleName();
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        Objects.requireNonNull(PREDICTOR, "Predictor must not be null");
        if (curBalance < 0) {
            throw new IllegalStateException("Current balance cannot be negative: " + curBalance);
        }
        long totalBudget = (long) Math.min(
                Math.max(curBalance, context.getStart()) * BUDGET_FRACTION,
                context.getMax()
        );
        if (totalBudget < context.getMin()) {
            return List.of();
        }
        List<SpotPrediction> predictions = PREDICTOR.getNextSpotPredictionList(context);
        if (predictions.isEmpty()) {
            return List.of();
        }

        AtomicLong usedBudget = new AtomicLong(0);
        List<Bet> bets = predictions.stream()
                .sorted(Comparator.comparingDouble(SpotPrediction::probability).reversed())
                .filter(prediction -> prediction.probability() >= PROBABILITY_THRESHOLD)
                .limit(MAX_BETS)
                .map(prediction -> {
                    long value = Math.max(context.getMin(), (long) (totalBudget * prediction.probability() * VALUE_MULTIPLIER));
                    value = Math.min(value, totalBudget - usedBudget.get());
                    BetType type = BetHelper.getStraightUpBetType(prediction.spot());
                    if (type != null && value >= context.getMin()) {
                        usedBudget.addAndGet(value);
                        return Bet.builder().type(type).value(value).build();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .takeWhile(bet -> usedBudget.get() <= totalBudget)
                .toList();
        if (bets.isEmpty()) {
            BetType type = BetHelper.getStraightUpBetType(predictions.getFirst().spot());
            if (type != null) {
                return List.of(Bet.builder().type(type).value(context.getMin()).build());
            }
        }
        return bets;
    }
}