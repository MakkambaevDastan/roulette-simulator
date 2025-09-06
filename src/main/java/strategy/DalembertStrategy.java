package strategy;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.List;

public class DalembertStrategy extends BaseStrategy {

    private static final BetType USE_BET_TYPE = BetType.RED;

    private int multiplier = 1;

    public DalembertStrategy(RouletteContext rouletteContext) {
        super(rouletteContext);
    }

    @Override
    public String getStrategyName() {
        return "ダランベール法(赤のみ)";
    }

    @Override
    public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
        if (hasLastBet()) {
            if (wasLastBetWon(rouletteContext)) {
                multiplier++;
            } else {
                multiplier--;
                if (multiplier <= 0) {
                    multiplier = 1;
                }
            }
        }
        return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet * multiplier));
    }
}