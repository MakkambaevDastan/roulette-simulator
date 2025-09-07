package strategy;

import application.Context;
import constants.Configurations;
import enums.Spot;
import model.Bet;
import utils.BetHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public abstract class BaseStrategy {
    protected long curBalance;
    protected long maxBalance;
    protected long minBalance;
    protected long maxTotalBetValue;
    protected long won;
    protected long lost;
    protected long bet;
    protected long wholeTotalBetValue;
    protected long wholeTotalPayoutValue;
    protected final LinkedList<Long> historyBalance;
    protected List<Bet> lastBets;
    protected boolean lastLive;

    public BaseStrategy(Context context) {
        curBalance = context.getStart();
        maxBalance = Long.MIN_VALUE;
        minBalance = Long.MAX_VALUE;
        maxTotalBetValue = Long.MIN_VALUE;
        won = 0;
        lost = 0;
        bet = 0;
        wholeTotalBetValue = 0;
        wholeTotalPayoutValue = 0;
        historyBalance = new LinkedList<>();
    }

    public List<Bet> getNext(Context context) {
        Objects.requireNonNull(context, "Context must not be null");
        lastBets = isLive() ? getNextInternal(context) : Collections.emptyList();
        long totalBetValue = BetHelper.getTotalBetValue(lastBets);
        if (maxTotalBetValue < totalBetValue) {
            maxTotalBetValue = totalBetValue;
        }
        return lastBets;
    }

    protected abstract List<Bet> getNextInternal(Context context);

    public abstract String getName();

    public long getLastTotalBetValue() {
        return BetHelper.getTotalBetValue(lastBets);
    }

    public void updateStrategyParameter(List<Bet> betList, Spot spot) {
        if (!betList.isEmpty()) {
            long currentTotalBetValue = BetHelper.getTotalBetValue(betList);
            wholeTotalBetValue += currentTotalBetValue;
            curBalance -= currentTotalBetValue;

            long currentTotalPayout = BetHelper.getTotalPayout(betList, spot);
            wholeTotalPayoutValue += currentTotalPayout;
            curBalance += currentTotalPayout;

            bet++;
            if (BetHelper.hasWin(betList, spot)) {
                won++;
            } else {
                lost++;
            }
        }
        if (maxBalance < curBalance) {
            maxBalance = curBalance;
        }
        if (curBalance < minBalance) {
            minBalance = curBalance;
        }
        if (lastLive) {
            historyBalance.offer(curBalance);
            if (Configurations.BALANCE_HISTORY_SIZE < historyBalance.size()) {
                historyBalance.poll();
            }
        }
        lastLive = isLive();
    }

    public boolean isLive() {
        return 0 < curBalance;
    }

    public double getWinningAverage() {
        return bet == 0 ? 0 : (double) won / (double) bet;
    }

    public long getAverageTotalBetValue() {
        return bet == 0 ? 0 : wholeTotalBetValue / bet;
    }

    public long getAverageTotalPayoutValue() {
        return bet == 0 ? 0 : wholeTotalPayoutValue / bet;
    }

    protected boolean wasLastBetWon(Context context) {
        return BetHelper.hasWin(lastBets, context.getLastSpot());
    }

    protected boolean hasLastBet() {
        return lastBets != null && !lastBets.isEmpty();
    }

    public static Comparator<BaseStrategy> getBalanceComparator() {
        return (o1, o2) -> Long.compare(o2.curBalance, o1.curBalance);
    }

    public static Comparator<BaseStrategy> getStrategyNameComparator() {
        return Comparator.comparing(BaseStrategy::getName);
    }
}
