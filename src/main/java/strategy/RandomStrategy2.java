package strategy;

import java.util.ArrayList;
import java.util.List;

import application.RouletteContext;
import constants.Configurations;
import enums.BetType;
import model.Bet;
import utils.BetHelper;

public class RandomStrategy2 extends BaseStrategy {

	private static int BET_LIST_SIZE = 10;

	public RandomStrategy2(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "ランダム2";
	}

	@Override
	public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
		List<Bet> betList = new ArrayList<>();
		List<BetType> betTypeList = BetType.getAvailableList(rouletteContext.rouletteType);

		if (wasLastBetWon(rouletteContext)) {
			for (Bet bet : lastBetList) {
				if (BetHelper.isWin(bet, rouletteContext.getLastSpot())) {
					betList.add(bet);
				}
			}
		}

		while (betList.size() < BET_LIST_SIZE) {
			BetType betType = betTypeList.get(Configurations.RANDOM.nextInt(betTypeList.size()));
			int multiplier = Configurations.RANDOM.nextInt(10) + 1;
			betList.add(new Bet(betType, rouletteContext.minimumBet * multiplier));
		}

		return betList;
	}
}