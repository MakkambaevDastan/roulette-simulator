package strategy;

import application.Context;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.List;

public class EastCoastProgressionStrategy extends BaseStrategy {

    private static final BetType TYPE = BetType.RED;

    public EastCoastProgressionStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return "イーストコーストプログレッション(赤のみ)";
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        if (wasLastBetWon(context)) {
            long value = (curBalance - context.getStart()) / 2;
            if (0 < value) {
                return create(value);
            } else {
                // TODO
                return create(context.getMin());
            }
        }
        return create(context.getMin() * 2);
    }

    private List<Bet> create(long value) {
        return Collections.singletonList(Bet.builder().type(TYPE).value(value).build());
    }
}