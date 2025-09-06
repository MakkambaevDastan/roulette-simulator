package strategy;

import java.util.ArrayList;
import java.util.List;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

public class TripleSixStrategy extends BaseStrategy {

	public TripleSixStrategy(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "666法";
	}

	@Override
	public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
		List<Bet> betList = new ArrayList<>();
		betList.add(new Bet(BetType.RED, rouletteContext.minimumBet * 36));
		betList.add(new Bet(BetType.SPLIT_0_2, rouletteContext.minimumBet * 4));
		betList.add(new Bet(BetType.SPLIT_8_11, rouletteContext.minimumBet * 4));
		betList.add(new Bet(BetType.SPLIT_10_13, rouletteContext.minimumBet * 4));
		betList.add(new Bet(BetType.SPLIT_17_20, rouletteContext.minimumBet * 4));
		betList.add(new Bet(BetType.SPLIT_26_29, rouletteContext.minimumBet * 4));
		betList.add(new Bet(BetType.SPLIT_28_31, rouletteContext.minimumBet * 4));
		betList.add(new Bet(BetType.STRAIGHT_UP_4, rouletteContext.minimumBet * 2));
		betList.add(new Bet(BetType.STRAIGHT_UP_6, rouletteContext.minimumBet * 2));
		betList.add(new Bet(BetType.STRAIGHT_UP_15, rouletteContext.minimumBet * 2));
		return betList;
	}
}