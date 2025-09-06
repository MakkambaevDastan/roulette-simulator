package strategy;

import java.util.Collections;
import java.util.List;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

public class BarnetStrategy extends BaseStrategy {

	private static final BetType USE_BET_TYPE = BetType.RED;

	private int setCount;

	public BarnetStrategy(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "バーネット法(赤のみ)";
	}

	@Override
	public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {

		if (!wasLastBetWon(rouletteContext)) {
			setCount = 0;
		}

		setCount++;

		switch ((setCount - 1) % 4) {
			case 0:
				return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet));
			case 1:
				return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet * 3));
			case 2:
				return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet * 2));
			case 3:
				return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet * 6));
			default:
				throw new IllegalArgumentException();
		}
	}
}