package strategy;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.List;

public class ThirtyOneSystemStrategy extends BaseStrategy {

    private static final BetType USE_BET_TYPE = BetType.RED;

    private boolean wonSecondFromLastBet;

    private int setCount;

    public ThirtyOneSystemStrategy(RouletteContext rouletteContext) {
        super(rouletteContext);
    }

    @Override
    public String getStrategyName() {
        return "31システム(赤のみ)";
    }

    @Override
    public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
        boolean wasLastBetWon = wasLastBetWon(rouletteContext);
        if ((wonSecondFromLastBet && wasLastBetWon) || ((setCount - 1) % 9 == 7 && !wasLastBetWon)) {
            setCount = 0;
        }
        wonSecondFromLastBet = wasLastBetWon;

        setCount++;

        switch ((setCount - 1) % 9) {
            case 0:
            case 1:
            case 2:
                return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet));
            case 3:
            case 4:
                return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet * 2));
            case 5:
            case 6:
                return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet * 4));
            case 7:
            case 8:
                return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet * 8));
            default:
                throw new IllegalArgumentException();
        }
    }
}