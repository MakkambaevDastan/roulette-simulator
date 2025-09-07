package strategy;

import application.Context;
import enums.BetType;
import model.Bet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CocomoStrategy extends BaseStrategy {

    private static final BetType TYPE = BetType.FIRST_DOZEN;

    private long secondFromLastTotalBetValue;

    public CocomoStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return "ココモ法(1stダズンのみ)";
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        long lastValue = getLastTotalBetValue();
        long nextValue = secondFromLastTotalBetValue + lastValue;
        secondFromLastTotalBetValue = lastValue;
        return Collections.singletonList(Bet.builder()
                .type(TYPE)
                .value(wasLastBetWon(context) ? context.getMin() : (nextValue > 0) ? nextValue : context.getMin())
                .build());
    }
}