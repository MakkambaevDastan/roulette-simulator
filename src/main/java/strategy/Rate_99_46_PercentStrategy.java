package strategy;

import java.util.ArrayList;
import java.util.List;

import application.RouletteContext;
import enums.BetType;
import enums.Spot;
import model.Bet;

public class Rate_99_46_PercentStrategy extends BaseStrategy {

	public Rate_99_46_PercentStrategy(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "99.46パーセント法";
	}

	@Override
	public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
		List<Bet> betList = new ArrayList<>();
		if (4 <= rouletteContext.spotHistoryList.size()) {

			Spot spot1 = rouletteContext.spotHistoryList.get(rouletteContext.spotHistoryList.size() - 4);
			Spot spot2 = rouletteContext.spotHistoryList.get(rouletteContext.spotHistoryList.size() - 3);
			Spot spot3 = rouletteContext.spotHistoryList.get(rouletteContext.spotHistoryList.size() - 2);
			Spot spot4 = rouletteContext.spotHistoryList.get(rouletteContext.spotHistoryList.size() - 1);

			if (spot1.isFirstDozenOrZeroAndDoubleZero() && spot2.isFirstDozenOrZeroAndDoubleZero()
					&& spot3.isFirstDozenOrZeroAndDoubleZero() && spot4.isFirstDozenOrZeroAndDoubleZero()) {
				betList.add(new Bet(BetType.SECOND_DOZEN, rouletteContext.minimumBet));
				betList.add(new Bet(BetType.THIRD_DOZEN, rouletteContext.minimumBet));
			}
		}
		return betList;
	}
}
