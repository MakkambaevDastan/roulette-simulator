package strategy;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.List;

public class TenPercentStrategy extends BaseStrategy {

    private static final BetType USE_BET_TYPE = BetType.RED;

    public TenPercentStrategy(RouletteContext rouletteContext) {
        super(rouletteContext);
    }

    @Override
    public String getStrategyName() {
        return "10%法(赤のみ)";
    }

    @Override
    public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {

        long value = Math.min(currentBalance / 10, rouletteContext.maximumBet);

        if (rouletteContext.minimumBet <= value) {
            return Collections.singletonList(new Bet(USE_BET_TYPE, value));
        } else {
            // FIXME
            return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet));
        }
    }
}