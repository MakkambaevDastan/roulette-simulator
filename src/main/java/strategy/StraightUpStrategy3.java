package strategy;

import java.util.ArrayList;
import java.util.List;

import application.RouletteContext;
import enums.Spot;
import model.Bet;
import utils.BetHelper;

public class StraightUpStrategy3 extends BaseStrategy {

	private int count = 0;

	public StraightUpStrategy3(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "ストレート複数賭け2";
	}

	@Override
	public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
		List<Bet> betList = new ArrayList<>();

		if (wasLastBetWon(rouletteContext)) {
			count++;
		} else {
			count = 0;
		}

		LOOP1: for (Spot spot : Spot.getAvailableList(rouletteContext.rouletteType)) {

			for (int i = 0; i < Math.min(rouletteContext.spotHistoryList.size(), count); i++) {
				if (spot == rouletteContext.spotHistoryList.get(rouletteContext.spotHistoryList.size() - (i + 1))) {
					continue LOOP1;
				}
			}
			betList.add(new Bet(BetHelper.getStraightUpBetType(spot), rouletteContext.minimumBet));
		}

		return betList;
	}
}