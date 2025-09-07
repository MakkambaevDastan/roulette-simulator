package strategy;

import application.Context;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.List;

public class GrandMartingaleStrategy extends BaseStrategy {

    private static final BetType TYPE = BetType.RED;

    public GrandMartingaleStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return GrandMartingaleStrategy.class.getSimpleName();
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        if (wasLastBetWon(context)) {
            return create(context.getMin());
        } else {
            // FIXME
            return create(getLastTotalBetValue() * 2 + context.getMin());
        }
    }

    private List<Bet> create(long value) {
        return Collections.singletonList(Bet.builder().type(TYPE).value(value).build());
    }
}