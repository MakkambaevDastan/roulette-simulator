package strategy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import application.RouletteContext;
import enums.BetType;
import enums.Spot;
import model.Bet;

public class Rate_98_48_PercentStrategy extends BaseStrategy {

	public Rate_98_48_PercentStrategy(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "98.48パーセント法";
	}

	@Override
	public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
		List<Bet> betList = new ArrayList<>();
		if (3 <= rouletteContext.spotHistoryList.size()) {

			Spot spot1 = rouletteContext.spotHistoryList.get(rouletteContext.spotHistoryList.size() - 3);
			Spot spot2 = rouletteContext.spotHistoryList.get(rouletteContext.spotHistoryList.size() - 2);
			Spot spot3 = rouletteContext.spotHistoryList.get(rouletteContext.spotHistoryList.size() - 1);

			if (spot1.isFirstDozenOrZeroAndDoubleZero() && spot2.isFirstDozenOrZeroAndDoubleZero()
					&& spot3.isFirstDozenOrZeroAndDoubleZero()) {
				long useBet = rouletteContext.minimumBet;

				if (CollectionUtils.isNotEmpty(lastBetList)) {
					useBet = lastBetList.get(0).value * 2;
				}
				betList.add(new Bet(BetType.SECOND_DOZEN, useBet));
				betList.add(new Bet(BetType.THIRD_DOZEN, useBet));
			}
		}
		return betList;
	}
}
