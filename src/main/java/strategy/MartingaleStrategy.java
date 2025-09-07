package strategy;

import application.Context;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.List;

public class MartingaleStrategy extends BaseStrategy {

    private static final BetType TYPE = BetType.RED;

    public MartingaleStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return "マーチンゲール法(赤のみ)";
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        if (wasLastBetWon(context)) {
            return create(context.getMin());
        } else {
            // FIXME
            return create(getLastTotalBetValue() * 2);
        }
    }

    private List<Bet> create(long value) {
        return Collections.singletonList(Bet.builder().type(TYPE).value(value).build());
    }
}