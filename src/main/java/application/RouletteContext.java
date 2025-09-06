package application;

import java.util.LinkedList;

import constants.Configurations;
import enums.HeatmapLayoutType;
import enums.RouletteType;
import enums.Spot;
import enums.SpotGenerateType;

public class RouletteContext {

	public final RouletteType rouletteType;

	public final HeatmapLayoutType heatmapLayoutType;

	public final SpotGenerateType spotGenerateType;

	public final LinkedList<Spot> spotHistoryList = new LinkedList<>();

	public final long initialBalance;

	public final long minimumBet;

	public final long maximumBet;

	public long currentLoopCount;

	public long simulationSpeed;

	public RouletteContext(RouletteType rouletteType, HeatmapLayoutType heatmapLayoutType, SpotGenerateType spotGenerateType, long initialBalance,
			long minimumBet, long maximumBet) {
		this.rouletteType = rouletteType;
		this.heatmapLayoutType = heatmapLayoutType;
		this.spotGenerateType = spotGenerateType;
		this.initialBalance = initialBalance;
		this.minimumBet = minimumBet;
		this.maximumBet = maximumBet;
		this.currentLoopCount = 0;
		this.simulationSpeed = 100;
	}

	public Spot getLastSpot() {
		return spotHistoryList.peekLast();
	}

	public void addSpotHistory(Spot spot) {
		spotHistoryList.offer(spot);
		if (Configurations.SPOT_HISTORY_LIST_SIZE < spotHistoryList.size()) {
			spotHistoryList.poll();
		}
	}

	public double getRedRate() {
		if (spotHistoryList.isEmpty()) {
			return 0;
		} else {
			int redCount = 0;
			for (Spot spot : spotHistoryList) {
				if (spot.isRed()) {
					redCount++;
				}
			}
			return ((double) redCount) / ((double) spotHistoryList.size());
		}
	}

	public double getBlackRate() {
		if (spotHistoryList.isEmpty()) {
			return 0;
		} else {
			int blackCount = 0;
			for (Spot spot : spotHistoryList) {
				if (spot.isBlack()) {
					blackCount++;
				}
			}
			return ((double) blackCount) / ((double) spotHistoryList.size());
		}
	}

	public double getGreenRate() {
		if (spotHistoryList.isEmpty()) {
			return 0;
		} else {
			int greenCount = 0;
			for (Spot spot : spotHistoryList) {
				if (spot.isGreen()) {
					greenCount++;
				}
			}
			return ((double) greenCount) / ((double) spotHistoryList.size());
		}
	}

	public java.util.Map<Spot, Integer> getSpotFrequency() {
		java.util.Map<Spot, Integer> frequencyMap = new java.util.HashMap<>();
		for (Spot spot : spotHistoryList) {
			frequencyMap.put(spot, frequencyMap.getOrDefault(spot, 0) + 1);
		}
		return frequencyMap;
	}

	public int getSpotCount(Spot spot) {
		int count = 0;
		for (Spot s : spotHistoryList) {
			if (s == spot) {
				count++;
			}
		}
		return count;
	}
}