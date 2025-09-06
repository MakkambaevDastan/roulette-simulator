package strategy;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

import java.util.ArrayList;
import java.util.List;


public class CocomoStrategy extends BaseStrategy {

    private static final BetType USE_BET_TYPE = BetType.FIRST_DOZEN;

    private long secondFromLastTotalBetValue;

    public CocomoStrategy(RouletteContext rouletteContext) {
        super(rouletteContext);
    }

    @Override
    public String getStrategyName() {
        return "ココモ法(1stダズンのみ)";
    }

    @Override
    public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
        List<Bet> betList = new ArrayList<>();

        if (wasLastBetWon(rouletteContext)) {
            betList.add(new Bet(USE_BET_TYPE, rouletteContext.minimumBet));
        } else {
            long nextBetValue = secondFromLastTotalBetValue + getLastTotalBetValue();
            if (0 < nextBetValue) {
                betList.add(new Bet(USE_BET_TYPE, nextBetValue));
            } else {
                betList.add(new Bet(USE_BET_TYPE, rouletteContext.minimumBet));
            }
        }

        secondFromLastTotalBetValue = getLastTotalBetValue();

        return betList;
    }
}