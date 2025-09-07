package strategy;

import application.Context;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.List;

public class TenPercentStrategy extends BaseStrategy {

    private static final BetType TYPE = BetType.RED;

    public TenPercentStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return TenPercentStrategy.class.getSimpleName();
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        long value = Math.min(curBalance / 10, context.getMax());
        if (context.getMin() <= value) {
            return Collections.singletonList(Bet.builder().type(TYPE).value(value).build());
        }
        // FIXME
        return Collections.singletonList(Bet.builder().type(TYPE).value(context.getMin()).build());
    }
}