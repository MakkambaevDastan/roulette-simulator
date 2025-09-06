package strategy;

import java.util.Collections;
import java.util.List;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

public class ReverseMartingaleRedStrategy extends BaseStrategy {

	private static final BetType USE_BET_TYPE = BetType.RED;

	public ReverseMartingaleRedStrategy(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "逆マーチンゲール法(赤のみ)";
	}

	@Override
	public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
		long thresholdValue = (long) (rouletteContext.minimumBet * Math.pow(2, 5));

		if (wasLastBetWon(rouletteContext) && getLastTotalBetValue() < thresholdValue) {
			return Collections.singletonList(new Bet(USE_BET_TYPE, (getLastTotalBetValue() * 2)));
		} else {
			return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet));
		}
	}
}
