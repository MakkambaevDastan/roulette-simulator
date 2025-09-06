package strategy;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

public class TwoInOneStrategy extends BaseStrategy {

	private static final BetType USE_BET_TYPE = BetType.RED;

	private LinkedList<Integer> numberList = new LinkedList<>();

	public TwoInOneStrategy(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "2in1法(赤のみ)";
	}

	@Override
	public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
		if (wasLastBetWon(rouletteContext)) {
			if (!numberList.isEmpty()) {
				numberList.removeFirst();
			}
			if (!numberList.isEmpty()) {
				numberList.removeLast();
			}
		} else {
			if (hasLastBet()) {
				if (numberList.size() <= 1) {
					numberList.addLast(1);
				} else {
					int sum = numberList.getFirst() + numberList.getLast();
					numberList.addLast(sum);
				}
			}
		}

		int multiplier;
		if (numberList.size() <= 1) {
			multiplier = 1;
		} else {
			multiplier = numberList.getFirst() + numberList.getLast();
		}

		return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet * multiplier));
	}
}