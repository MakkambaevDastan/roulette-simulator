package strategy;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TenUnitStrategy extends BaseStrategy {

    private static final BetType USE_BET_TYPE = BetType.RED;

    private LinkedList<Integer> numberList = new LinkedList<>();

    private long setStartBalance;

    private long limitValue;

    private boolean useLastBetLeftNumber;

    private boolean useLastBetAllLimit;

    private int lastBetMultiplier;

    private int lastRemainingLimitMultiplier;

    public TenUnitStrategy(RouletteContext rouletteContext) {
        super(rouletteContext);

        initializeSet(rouletteContext);
    }

    @Override
    public String getStrategyName() {
        return "10ユニット法(赤のみ)";
    }

    @Override
    public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
        if (0 <= getRemainingLimit()) {
            initializeSet(rouletteContext);
        }

        if (wasLastBetWon(rouletteContext)) {
            if (!useLastBetLeftNumber && !useLastBetAllLimit) {
                if (!numberList.isEmpty()) {
                    numberList.removeFirst();
                }
                if (!numberList.isEmpty()) {
                    numberList.removeLast();
                }
            } else if (useLastBetAllLimit) {
                if (!numberList.isEmpty()) {
                    numberList.removeFirst();
                }
                numberList.addFirst(lastBetMultiplier);
            }
        } else {
            if (hasLastBet()) {
                if (useLastBetAllLimit) {
                    numberList.clear();
                } else if (useLastBetLeftNumber) {
                    if (!numberList.isEmpty()) {
                        numberList.removeFirst();
                    }
                    numberList.addFirst(lastRemainingLimitMultiplier);
                } else {
                    numberList.addLast(lastBetMultiplier);
                }
            }
        }
        useLastBetLeftNumber = false;
        useLastBetAllLimit = false;

        if (numberList.isEmpty()) {
            numberList.clear();
            for (int i = 0; i < 10; i++) {
                numberList.addLast(1);
            }
            initializeSet(rouletteContext);
        }

        int multiplier = numberList.getFirst() + numberList.getLast();

        lastRemainingLimitMultiplier = (int) (getRemainingLimit() / rouletteContext.minimumBet);

        if (getRemainingLimit() < rouletteContext.minimumBet * multiplier) {
            if (rouletteContext.minimumBet * numberList.getFirst() < getRemainingLimit()) {
                multiplier = numberList.getFirst();
                useLastBetLeftNumber = true;
            } else {
                multiplier = lastRemainingLimitMultiplier;
                useLastBetAllLimit = true;
            }
        }

        if (multiplier <= 0) {
            multiplier = 1;
        }

        lastBetMultiplier = multiplier;
        return Collections.singletonList(new Bet(USE_BET_TYPE, rouletteContext.minimumBet * multiplier));
    }

    private void initializeSet(RouletteContext rouletteContext) {
        if (0 < currentBalance) {
            setStartBalance = currentBalance;
            // TODO
            limitValue = currentBalance / 10;
        } else {
            setStartBalance = currentBalance;
            limitValue = rouletteContext.initialBalance / 10;
        }
    }

    private long getRemainingLimit() {
        return limitValue - (setStartBalance - currentBalance);
    }
}