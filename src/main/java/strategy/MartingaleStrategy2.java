package strategy;

import application.Context;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.List;

import static enums.BetType.BLACK;
import static enums.BetType.RED;

public class MartingaleStrategy2 extends BaseStrategy {

    public MartingaleStrategy2(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return MartingaleStrategy2.class.getSimpleName();
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        BetType type = context.getBlackRate() <= context.getRedRate() ? RED : BLACK;
        if (wasLastBetWon(context)) {
            return create(type, context.getMin());
        } else {
            // FIXME
            return create(type, getLastTotalBetValue() * 2);
        }
    }

    private List<Bet> create(BetType type, long value) {
        return Collections.singletonList(Bet.builder().type(type).value(value).build());
    }
}