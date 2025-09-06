package strategy;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.List;

public class MartingaleStrategy extends BaseStrategy {

    private static final BetType USE_BET_TYPE = BetType.RED;

    public MartingaleStrategy(RouletteContext rouletteContext) {
        super(rouletteContext);
    }

    @Override
    public String getStrategyName() {
        return "マーチンゲール法(赤のみ)";
    }

    @Override
    public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
        if (wasLastBetWon(rouletteContext)) {
            return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet));
        } else {
            // FIXME
            return Collections.singletonList(new Bet(USE_BET_TYPE, (getLastTotalBetValue() * 2)));
        }
    }
}