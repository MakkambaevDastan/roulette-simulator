package strategy;

import application.Context;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MonteCarloStrategy extends BaseStrategy {

    private static final BetType TYPE = BetType.FIRST_DOZEN;

    private final LinkedList<Integer> numbers = new LinkedList<>();

    public MonteCarloStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return MonteCarloStrategy.class.getSimpleName();
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        if (wasLastBetWon(context)) {
            for (int i = 0; i < 2; i++) {
                if (!numbers.isEmpty()) {
                    numbers.removeFirst();
                }
            }
            for (int i = 0; i < 2; i++) {
                if (!numbers.isEmpty()) {
                    numbers.removeLast();
                }
            }
        } else if (hasLastBet()) {
            numbers.addLast(numbers.getFirst() + numbers.getLast());
        }

        if (numbers.size() <= 1) {
            numbers.clear();
            numbers.addLast(1);
            numbers.addLast(2);
            numbers.addLast(3);
        }
        int multiplier = numbers.getFirst() + numbers.getLast();
        return Collections.singletonList(Bet.builder().type(TYPE).value(context.getMin() * multiplier).build());
    }
}