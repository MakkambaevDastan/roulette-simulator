package strategy;

import java.util.ArrayList;
import java.util.List;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

public class SevenKaimeStrategy extends BaseStrategy {

	public SevenKaimeStrategy(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "7回目の法則(赤のみ)";
	}

	@Override
	public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
		List<Bet> betList = new ArrayList<>();
		if (7 <= rouletteContext.spotHistoryList.size()) {

			boolean notMatched = false;
			for (int i = 0; i < 7; i++) {
				if (!rouletteContext.spotHistoryList.get(rouletteContext.spotHistoryList.size() - (1 + i)).isBlack()) {
					notMatched = true;
				}
			}

			if (!notMatched) {
				betList.add(new Bet(BetType.RED, rouletteContext.minimumBet));
			}
		}
		return betList;
	}
}
