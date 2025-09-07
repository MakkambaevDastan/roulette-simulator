package strategy;

import application.Context;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.List;

public class BarnetStrategy extends BaseStrategy {

    private static final BetType TYPE = BetType.RED;

    private int count;

    public BarnetStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return "バーネット法(赤のみ)";
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        count = wasLastBetWon(context) ? (count + 1) : 0;
        return switch ((count - 1) % 4) {
            case 0 -> create(context.getMin());
            case 1 -> create(context.getMin() * 3);
            case 2 -> create(context.getMin() * 2);
            case 3 -> create(context.getMin() * 6);
            default -> throw new IllegalArgumentException();
        };
    }

    private List<Bet> create(long value) {
        return Collections.singletonList(Bet.builder().type(TYPE).value(value).build());
    }
}