package strategy;

import java.util.Collections;
import java.util.List;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

public class GoodmanStrategy extends BaseStrategy {

	private static final BetType USE_BET_TYPE = BetType.RED;

	private int setCount;

	public GoodmanStrategy(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "グッドマン法(赤のみ)";
	}

	@Override
	public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
		if (!wasLastBetWon(rouletteContext)) {
			setCount = 0;
		}

		setCount++;

		switch (setCount) {
			case 0:
				return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet));
			case 1:
				return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet * 2));
			case 2:
				return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet * 3));
			case 3:
			default:
				return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet * 5));
		}
	}
}