package strategy;

import java.util.Collections;
import java.util.List;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

public class GrandMartingaleStrategy extends BaseStrategy {

	private static final BetType USE_BET_TYPE = BetType.RED;

	public GrandMartingaleStrategy(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "グランマーチンゲール法(赤のみ)";
	}

	@Override
	public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
		if (wasLastBetWon(rouletteContext)) {
			return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet));
		} else {
			return Collections
					.singletonList(new Bet(USE_BET_TYPE, (getLastTotalBetValue() * 2 + rouletteContext.minimumBet)));
		}
	}
}