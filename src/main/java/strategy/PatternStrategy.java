package strategy;

import java.util.ArrayList;
import java.util.List;

import application.RouletteContext;
import enums.BetType;
import enums.Spot;
import model.Bet;
import model.SpotPrediction;
import predictor.BasePredictor;
import predictor.PatternPredictor;
import utils.BetHelper;
import utils.PredictorHelper;

public class PatternStrategy extends BaseStrategy {

	private static final BasePredictor PREDICTOR = PredictorHelper.getInstance(PatternPredictor.class);

	public PatternStrategy(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "パターン分析戦略(予測器を使用)";
	}

	@Override
	public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
		List<Bet> betList = new ArrayList<>();

		long totalBudget = currentBalance / 10;
		if (totalBudget <= 0) {
			totalBudget = rouletteContext.initialBalance / 10;
		}
		if (rouletteContext.maximumBet < totalBudget) {
			totalBudget = rouletteContext.maximumBet;
		}

		List<SpotPrediction> spotPredictions = PREDICTOR.getNextSpotPredictionList(rouletteContext);
		spotPredictions.sort((a, b) -> Double.compare(b.probability, a.probability));

		double probabilityThreshold = 0.02;
		long usedBudget = 0;
		int maxBets = 5;
		int betCount = 0;

		for (SpotPrediction spotPrediction : spotPredictions) {
			if (betCount >= maxBets || usedBudget >= totalBudget) {
				break;
			}

			if (spotPrediction.probability >= probabilityThreshold) {
				long betValue = Math.max(
					rouletteContext.minimumBet,
					(long) (totalBudget * spotPrediction.probability * 2)
				);

				if (usedBudget + betValue > totalBudget) {
					betValue = totalBudget - usedBudget;
				}

				if (betValue >= rouletteContext.minimumBet) {
					BetType useBetType = BetHelper.getStraightUpBetType(spotPrediction.spot);
					if (useBetType != null) {
						betList.add(new Bet(useBetType, betValue));
						usedBudget += betValue;
						betCount++;
					}
				}
			}
		}

		if (betList.isEmpty() && !spotPredictions.isEmpty()) {
			SpotPrediction bestPrediction = spotPredictions.get(0);
			BetType useBetType = BetHelper.getStraightUpBetType(bestPrediction.spot);
			if (useBetType != null) {
				betList.add(new Bet(useBetType, rouletteContext.minimumBet));
			}
		}

		return betList;
	}
}