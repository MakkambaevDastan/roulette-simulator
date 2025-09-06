package strategy;

import java.util.ArrayList;
import java.util.List;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

public class MartingaleStrategy4 extends BaseStrategy {

	private static final BetType USE_BET_TYPE1 = BetType.FIRST_DOZEN;

	private static final BetType USE_BET_TYPE2 = BetType.SECOND_DOZEN;

	public MartingaleStrategy4(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "マーチンゲール法(1st・2ndダズンのみ)";
	}

	@Override
	public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
		List<Bet> betList = new ArrayList<>();

		if (wasLastBetWon(rouletteContext)) {
			betList.add(new Bet(USE_BET_TYPE1, rouletteContext.minimumBet));
			betList.add(new Bet(USE_BET_TYPE2, rouletteContext.minimumBet));
		} else {
			if (hasLastBet()) {
				// FIXME
				long betValue = lastBetList.get(0).value * 3;
				betList.add(new Bet(USE_BET_TYPE1, betValue));
				betList.add(new Bet(USE_BET_TYPE2, betValue));
			} else {
				betList.add(new Bet(USE_BET_TYPE1, rouletteContext.minimumBet));
				betList.add(new Bet(USE_BET_TYPE2, rouletteContext.minimumBet));
			}
		}
		return betList;
	}
}