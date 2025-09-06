package strategy;

import application.RouletteContext;
import enums.BetType;
import enums.Spot;
import model.Bet;
import model.SpotPrediction;
import predictor.BasePredictor;
import predictor.RnnPredictor;
import utils.BetHelper;
import utils.PredictorHelper;

import java.util.ArrayList;
import java.util.List;

public class FlowerBetStrategy extends BaseStrategy {

    private static final BasePredictor PREDICTOR = PredictorHelper.getInstance(RnnPredictor.class);

    public FlowerBetStrategy(RouletteContext rouletteContext) {
        super(rouletteContext);
    }

    @Override
    public String getStrategyName() {
        return "フラワーベット(予測器を使用)";
    }

    @Override
    public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
        List<Bet> betList = new ArrayList<>();

        double maxProbability = 0;
        Spot spot = null;
        for (SpotPrediction spotPrediction : PREDICTOR.getNextSpotPredictionList(rouletteContext)) {
            if (maxProbability < spotPrediction.probability) {
                maxProbability = spotPrediction.probability;
                spot = spotPrediction.spot;
            }
        }

        if (spot != null) {
            List<BetType> betTypeList = BetHelper.getFlowerBetBetTypeList(spot);
            for (BetType betType : betTypeList) {
                betList.add(new Bet(betType, rouletteContext.minimumBet));
            }
        }
        return betList;
    }
}