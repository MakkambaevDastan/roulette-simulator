package strategy;

import application.Context;
import enums.BetType;
import model.Bet;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class TenUnitStrategy extends BaseStrategy {

    private static final BetType TYPE = BetType.RED;
    private static final int NUMBER_COUNT = 10;
    private static final double BUDGET_FRACTION = 0.1;
    private static final int DEFAULT_MULTIPLIER = 1;

    private final Deque<Integer> numbers;
    private long setStartBalance;
    private long limitValue;
    private boolean useLastBetLeftNumber;
    private boolean useLastBetAllLimit;
    private int lastBetMultiplier;
    private int lastRemainingLimitMultiplier;

    public TenUnitStrategy(Context context) {
        super(context);
        this.numbers = new ArrayDeque<>(NUMBER_COUNT);
        initializeSet(context);
    }

    @Override
    public String getName() {
        return TenUnitStrategy.class.getSimpleName();
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        if (getRemainingLimit() <= 0) {
            initializeSet(context);
        }

        if (wasLastBetWon(context)) {
            if (!useLastBetLeftNumber && !useLastBetAllLimit) {
                if (!numbers.isEmpty()) numbers.removeFirst();
                if (!numbers.isEmpty()) numbers.removeLast();
            } else if (useLastBetAllLimit) {
                if (!numbers.isEmpty()) numbers.removeFirst();
                numbers.addFirst(lastBetMultiplier);
            }
        } else if (hasLastBet()) {
            if (useLastBetAllLimit) {
                numbers.clear();
            } else if (useLastBetLeftNumber) {
                if (!numbers.isEmpty()) numbers.removeFirst();
                numbers.addFirst(lastRemainingLimitMultiplier);
            } else {
                numbers.addLast(lastBetMultiplier);
            }
        }

        if (numbers.isEmpty()) {
            for (int i = 0; i < NUMBER_COUNT; i++) {
                numbers.addLast(DEFAULT_MULTIPLIER);
            }
            initializeSet(context);
        }

        int multiplier = numbers.getFirst() + numbers.getLast();
        lastRemainingLimitMultiplier = (int) (getRemainingLimit() / context.getMin());
        if (getRemainingLimit() < context.getMin() * multiplier) {
            if (context.getMin() * numbers.getFirst() < getRemainingLimit()) {
                multiplier = numbers.getFirst();
                useLastBetLeftNumber = true;
            } else {
                multiplier = lastRemainingLimitMultiplier;
                useLastBetAllLimit = true;
            }
        }

        lastBetMultiplier = Math.max(multiplier, 1);
        long betValue = context.getMin() * lastBetMultiplier;
        return List.of(Bet.builder().type(TYPE).value(betValue).build());
    }

    private void initializeSet(Context context) {
        setStartBalance = Math.max(curBalance, context.getStart());
        limitValue = (long) (setStartBalance * BUDGET_FRACTION);
    }

    private long getRemainingLimit() {
        return limitValue - (setStartBalance - curBalance);
    }
}