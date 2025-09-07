package strategy;

import application.Context;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.List;

public class DalembertStrategy extends BaseStrategy {

    private static final BetType TYPE = BetType.RED;

    private int multiplier;

    public DalembertStrategy(Context context) {
        super(context);
        multiplier = 1;
    }

    @Override
    public String getName() {
        return "ダランベール法(赤のみ)";
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        if (hasLastBet()) {
            multiplier = wasLastBetWon(context) ? ++multiplier : Math.max(--multiplier, 1);
        }
        return Collections.singletonList(Bet.builder().type(TYPE).value(context.getMin() * multiplier).build());
    }
}