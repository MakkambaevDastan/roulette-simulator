package strategy;

import application.Context;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TwoInOneStrategy extends BaseStrategy {

    private static final BetType TYPE = BetType.RED;

    private final LinkedList<Integer> numbers;

    public TwoInOneStrategy(Context context) {
        super(context);
        numbers = new LinkedList<>();
    }

    @Override
    public String getName() {
        return TwoInOneStrategy.class.getSimpleName();
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        if (wasLastBetWon(context)) {
            if (!numbers.isEmpty()) {
                numbers.removeFirst();
            }
            if (!numbers.isEmpty()) {
                numbers.removeLast();
            }
        } else {
            if (hasLastBet()) {
                numbers.addLast(numbers.size() <= 1 ? 1 : (numbers.getFirst() + numbers.getLast()));
            }
        }

        return Collections.singletonList(Bet.builder()
                .type(TYPE)
                .value(context.getMin() * (numbers.size() <= 1 ? 1 : numbers.getFirst() + numbers.getLast()))
                .build());
    }
}