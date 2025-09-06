package strategy;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MonteCarloStrategy extends BaseStrategy {

    private static final BetType USE_BET_TYPE = BetType.FIRST_DOZEN;

    private LinkedList<Integer> numberList = new LinkedList<>();

    public MonteCarloStrategy(RouletteContext rouletteContext) {
        super(rouletteContext);
    }

    @Override
    public String getStrategyName() {
        return "モンテカルロ法(1stダズンのみ)";
    }

    @Override
    public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
        if (wasLastBetWon(rouletteContext)) {
            for (int i = 0; i < 2; i++) {
                if (!numberList.isEmpty()) {
                    numberList.removeFirst();
                }
            }
            for (int i = 0; i < 2; i++) {
                if (!numberList.isEmpty()) {
                    numberList.removeLast();
                }
            }
        } else {
            if (hasLastBet()) {
                int sum = numberList.getFirst() + numberList.getLast();
                numberList.addLast(sum);
            }
        }

        if (numberList.size() <= 1) {
            numberList.clear();
            numberList.addLast(1);
            numberList.addLast(2);
            numberList.addLast(3);
        }

        int multiplier = numberList.getFirst() + numberList.getLast();

        return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet * multiplier));
    }
}