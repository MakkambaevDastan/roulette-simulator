package strategy;

import java.util.ArrayList;
import java.util.List;

import application.RouletteContext;
import enums.BetType;
import enums.Spot;
import model.Bet;
import model.SpotPrediction;
import predictor.BasePredictor;
import predictor.RnnPredictor;
import utils.BetHelper;
import utils.PredictorHelper;

public class StraightUpStrategy extends BaseStrategy {

	private static final BasePredictor PREDICTOR = PredictorHelper.getInstance(RnnPredictor.class);

	public StraightUpStrategy(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "1点賭け(予測器を使用)";
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
			BetType useBetType = BetHelper.getStraightUpBetType(spot);

			betList.add(new Bet(useBetType, rouletteContext.minimumBet));
		}
		return betList;
	}
}