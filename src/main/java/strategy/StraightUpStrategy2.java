package strategy;

import application.RouletteContext;
import enums.BetType;
import model.Bet;
import model.SpotPrediction;
import predictor.BasePredictor;
import predictor.CountPredictor2;
import utils.BetHelper;
import utils.PredictorHelper;

import java.util.ArrayList;
import java.util.List;

public class StraightUpStrategy2 extends BaseStrategy {

    private static final BasePredictor PREDICTOR = PredictorHelper.getInstance(CountPredictor2.class);

    public StraightUpStrategy2(RouletteContext rouletteContext) {
        super(rouletteContext);
    }

    @Override
    public String getStrategyName() {
        return "ストレート複数賭け(予測器を使用)";
    }

    @Override
    public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
        List<Bet> betList = new ArrayList<>();

        long limit = currentBalance / 10;
        if (limit < 0) {
            limit = rouletteContext.initialBalance / 10;
        }

        for (SpotPrediction spotPrediction : PREDICTOR.getNextSpotPredictionList(rouletteContext)) {

            BetType useBetType = BetHelper.getStraightUpBetType(spotPrediction.spot);

            if (0.03 < spotPrediction.probability) {
                long betValue = (long) (limit * spotPrediction.probability);
                if (rouletteContext.minimumBet < betValue) {
                    betList.add(new Bet(useBetType, betValue));
                } else {
                    betList.add(new Bet(useBetType, rouletteContext.minimumBet));
                }
            }
        }
        return betList;
    }
}