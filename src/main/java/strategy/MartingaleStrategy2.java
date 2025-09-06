package strategy;

import java.util.Collections;
import java.util.List;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

public class MartingaleStrategy2 extends BaseStrategy {

	public MartingaleStrategy2(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "マーチンゲール法(赤・黒のうち確率の高い方)";
	}

	@Override
	public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
		BetType useBetType;
		if (rouletteContext.getBlackRate() <= rouletteContext.getRedRate()) {
			useBetType = BetType.RED;
		} else {
			useBetType = BetType.BLACK;
		}

		if (wasLastBetWon(rouletteContext)) {
			return Collections.singletonList(new Bet(useBetType, rouletteContext.minimumBet));
		} else {
			// FIXME
			return Collections.singletonList(new Bet(useBetType, (getLastTotalBetValue() * 2)));
		}
	}
}