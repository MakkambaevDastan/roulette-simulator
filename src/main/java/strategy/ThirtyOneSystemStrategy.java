package strategy;

import application.Context;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.List;

public class ThirtyOneSystemStrategy extends BaseStrategy {

    private static final BetType TYPE = BetType.RED;

    private boolean wonSecondFromLastBet;
    private int count;

    public ThirtyOneSystemStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return ThirtyOneSystemStrategy.class.getSimpleName();
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        boolean wasLastBetWon = wasLastBetWon(context);
        count = ((wonSecondFromLastBet && wasLastBetWon) || ((count - 1) % 9 == 7 && !wasLastBetWon)) ? 0 : (count + 1);
        wonSecondFromLastBet = wasLastBetWon;
        return switch ((count - 1) % 9) {
            case 0, 1, 2 -> Collections.singletonList(Bet.builder().type(TYPE).value(context.getMin()).build());
            case 3, 4 -> Collections.singletonList(Bet.builder().type(TYPE).value(context.getMin() * 2).build());
            case 5, 6 -> Collections.singletonList(Bet.builder().type(TYPE).value(context.getMin() * 4).build());
            case 7, 8 -> Collections.singletonList(Bet.builder().type(TYPE).value(context.getMin() * 8).build());
            default -> throw new IllegalArgumentException();
        };
    }
}