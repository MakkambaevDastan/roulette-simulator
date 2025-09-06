package strategy;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import application.RouletteContext;
import constants.Configurations;
import enums.Spot;
import model.Bet;
import utils.BetHelper;

public abstract class BaseStrategy {

	public long currentBalance;

	public long maximumBalance = Long.MIN_VALUE;

	public long minimumBalance = Long.MAX_VALUE;

	public long maximumTotalBetValue = Long.MIN_VALUE;

	public long wonCount = 0;

	public long lostCount = 0;

	public long betCount = 0;

	public long wholeTotalBetValue = 0;

	public long wholeTotalPayoutValue = 0;

	public final LinkedList<Long> balanceHistoryList = new LinkedList<>();

	protected List<Bet> lastBetList;

	protected boolean lastLive;

	public abstract String getStrategyName();

	public BaseStrategy(RouletteContext rouletteContext) {
		currentBalance = rouletteContext.initialBalance;
	}

	public List<Bet> getNextBetList(RouletteContext rouletteContext) {
		if (isLive()) {
			lastBetList = getNextBetListImpl(rouletteContext);
		} else {
			lastBetList = Collections.emptyList();
		}

		long totalBetValue = BetHelper.getTotalBetValue(lastBetList);
		if (maximumTotalBetValue < totalBetValue) {
			maximumTotalBetValue = totalBetValue;
		}

		return lastBetList;
	}

	protected abstract List<Bet> getNextBetListImpl(RouletteContext rouletteContext);

	public long getLastTotalBetValue() {
		return BetHelper.getTotalBetValue(lastBetList);
	}

	public void updateStrategyParameter(List<Bet> betList, Spot spot) {
		if (!betList.isEmpty()) {
			long currentTotalBetValue = BetHelper.getTotalBetValue(betList);
			wholeTotalBetValue += currentTotalBetValue;
			currentBalance -= currentTotalBetValue;

			long currentTotalPayout = BetHelper.getTotalPayout(betList, spot);
			wholeTotalPayoutValue += currentTotalPayout;
			currentBalance += currentTotalPayout;

			betCount++;
			if (BetHelper.hasWin(betList, spot)) {
				wonCount++;
			} else {
				lostCount++;
			}
		}

		if (maximumBalance < currentBalance) {
			maximumBalance = currentBalance;
		}
		if (currentBalance < minimumBalance) {
			minimumBalance = currentBalance;
		}

		if (lastLive) {
			balanceHistoryList.offer(currentBalance);
			if (Configurations.BALANCE_HISTORY_SIZE < balanceHistoryList.size()) {
				balanceHistoryList.poll();
			}
		}

		lastLive = isLive();
	}

	public boolean isLive() {
		return 0 < currentBalance;
	}

	public double getWinningAverage() {
		if (betCount == 0) {
			return 0;
		} else {
			return (double) wonCount / (double) betCount;
		}
	}

	public long getAverageTotalBetValue() {
		if (betCount == 0) {
			return 0;
		} else {
			return wholeTotalBetValue / betCount;
		}
	}

	public long getAverageTotalPayoutValue() {
		if (betCount == 0) {
			return 0;
		} else {
			return wholeTotalPayoutValue / betCount;
		}
	}

	protected boolean wasLastBetWon(RouletteContext rouletteContext) {
		return BetHelper.hasWin(lastBetList, rouletteContext.getLastSpot());
	}

	protected boolean hasLastBet() {
		return CollectionUtils.isNotEmpty(lastBetList);
	}

	public static Comparator<BaseStrategy> getBalanceComparator() {
		return (o1, o2) -> Long.compare(o2.currentBalance, o1.currentBalance);
	}

	public static Comparator<BaseStrategy> getStrategyNameComparator() {
		return Comparator.comparing(BaseStrategy::getStrategyName);
	}
}
