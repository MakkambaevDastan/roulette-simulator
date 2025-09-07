package strategy;

import application.Context;
import enums.BetType;
import model.Bet;

import java.util.ArrayList;
import java.util.List;

public class MartingaleStrategy4 extends BaseStrategy {

    private static final BetType TYPE1 = BetType.FIRST_DOZEN;

    private static final BetType TYPE2 = BetType.SECOND_DOZEN;

    public MartingaleStrategy4(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return "マーチンゲール法(1st・2ndダズンのみ)";
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        List<Bet> list = new ArrayList<>();
        if (wasLastBetWon(context)) {
            list.add(Bet.builder().type(TYPE1).value(context.getMin()).build());
            list.add(Bet.builder().type(TYPE2).value(context.getMin()).build());
        } else if (hasLastBet()) {
            // FIXME
            long betValue = lastBets.getFirst().value() * 3;
            list.add(Bet.builder().type(TYPE1).value(betValue).build());
            list.add(Bet.builder().type(TYPE2).value(betValue).build());
        } else {
            list.add(Bet.builder().type(TYPE1).value(context.getMin()).build());
            list.add(Bet.builder().type(TYPE2).value(context.getMin()).build());
        }
        return list;
    }
}