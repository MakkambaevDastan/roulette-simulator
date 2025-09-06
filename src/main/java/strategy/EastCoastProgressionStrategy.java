package strategy;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.List;

public class EastCoastProgressionStrategy extends BaseStrategy {

    private static final BetType USE_BET_TYPE = BetType.RED;

    public EastCoastProgressionStrategy(RouletteContext rouletteContext) {
        super(rouletteContext);
    }

    @Override
    public String getStrategyName() {
        return "イーストコーストプログレッション(赤のみ)";
    }

    @Override
    public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
        if (wasLastBetWon(rouletteContext)) {
            long betValue = (currentBalance - rouletteContext.initialBalance) / 2;
            if (0 < betValue) {
                return Collections.singletonList(new Bet(USE_BET_TYPE, betValue));
            } else {
                // TODO
                return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet));
            }
        } else {
            return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet * 2));
        }
    }
}