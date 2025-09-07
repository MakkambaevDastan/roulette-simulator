package strategy;

import application.Context;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.List;

public class ReverseMartingaleRedStrategy extends BaseStrategy {

    private static final BetType TYPE = BetType.RED;

    public ReverseMartingaleRedStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return ReverseMartingaleRedStrategy.class.getSimpleName();
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        long value = getLastTotalBetValue();
       return Collections.singletonList(Bet.builder()
                .type(TYPE)
                .value((wasLastBetWon(context) && value < (context.getMin() * Math.pow(2, 5))) ? value * 2 : context.getMin())
                .build());
    }
}
