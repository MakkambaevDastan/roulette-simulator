package strategy;

import java.util.Collections;
import java.util.List;

import application.RouletteContext;
import enums.BetType;
import model.Bet;
import model.ColorPrediction;
import predictor.BasePredictor;
import predictor.CountPredictor2;
import utils.BetHelper;
import utils.PredictorHelper;

public class MartingaleStrategy3 extends BaseStrategy {

	private static final BasePredictor PREDICTOR = PredictorHelper.getInstance(CountPredictor2.class);

	public MartingaleStrategy3(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "マーチンゲール法(予測器を使用)";
	}

	@Override
	public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {

		ColorPrediction colorPrediction = PREDICTOR.getNextColorPrediction(rouletteContext);

		BetType useBetType;
		if (colorPrediction.blackProbability <= colorPrediction.redProbability) {
			useBetType = BetType.RED;
		} else {
			useBetType = BetType.BLACK;
		}

		boolean wonLastBet = false;
		long lastBetValue = 0;
		if (lastBetList != null) {
			lastBetValue = BetHelper.getTotalBetValue(lastBetList);
			for (Bet bet : lastBetList) {
				if (BetHelper.isWin(bet, rouletteContext.getLastSpot())) {
					wonLastBet = true;
				}
			}
		}

		if (wonLastBet) {
			return Collections.singletonList(new Bet(useBetType, rouletteContext.minimumBet));
		} else {
			// FIXME
			return Collections.singletonList(new Bet(useBetType, (lastBetValue * 2)));
		}
	}
}