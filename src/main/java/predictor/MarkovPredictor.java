package predictor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.RouletteContext;
import enums.BetType;
import enums.Spot;
import model.BetTypePrediction;
import model.SpotPrediction;
import utils.BetHelper;
import utils.LogHelper;

public class MarkovPredictor extends BasePredictor {

	private Map<Spot, Map<Spot, Long>> spotMarkovMap = new HashMap<>();

	private Map<BetType, Map<BetType, Long>> betTypeMarkovMap = new HashMap<>();

	private long totalCount = 0;

	@Override
	public List<SpotPrediction> getNextSpotPredictionList(RouletteContext rouletteContext) {
		List<SpotPrediction> spotPredictionList = new ArrayList<>();
		if (!rouletteContext.spotHistoryList.isEmpty()) {
			updateParameter(rouletteContext);

			for (Spot nextSpot : Spot.getAvailableList(rouletteContext.rouletteType)) {
				if (spotMarkovMap.containsKey(rouletteContext.getLastSpot())
						&& spotMarkovMap.get(rouletteContext.getLastSpot()).containsKey(nextSpot)) {
					spotPredictionList.add(new SpotPrediction(nextSpot,
							((double) spotMarkovMap.get(rouletteContext.getLastSpot()).get(nextSpot))
									/ ((double) totalCount)));
				}
			}
		}
		return spotPredictionList;
	}

	@Override
	public List<BetTypePrediction> getNextBetTypePredictionList(RouletteContext rouletteContext) {
		List<BetTypePrediction> betTypePredictionList = new ArrayList<>();
		if (!rouletteContext.spotHistoryList.isEmpty()) {
			updateParameter(rouletteContext);

			List<BetType> lastSpotBetTypeList = new ArrayList<>();
			for (BetType betType : BetType.getAvailableList(rouletteContext.rouletteType)) {
				if (BetHelper.isWin(betType, rouletteContext.getLastSpot())) {
					lastSpotBetTypeList.add(betType);
				}
			}

			for (BetType lastSpotBetType : lastSpotBetTypeList) {
				for (BetType nextBetType : BetType.getAvailableList(rouletteContext.rouletteType)) {
					if (betTypeMarkovMap.containsKey(lastSpotBetType)
							&& betTypeMarkovMap.get(lastSpotBetType).containsKey(nextBetType)) {
						betTypePredictionList.add(new BetTypePrediction(nextBetType,
								((double) betTypeMarkovMap.get(lastSpotBetType).get(nextBetType))
										/ ((double) totalCount)));
					}
				}
			}
		}
		return betTypePredictionList;
	}

	private void updateParameter(RouletteContext rouletteContext) {
		if (2 <= rouletteContext.spotHistoryList.size()) {
			Spot spot1 = rouletteContext.spotHistoryList.get(rouletteContext.spotHistoryList.size() - 2);
			Spot spot2 = rouletteContext.spotHistoryList.get(rouletteContext.spotHistoryList.size() - 1);

			if (spotMarkovMap.containsKey(spot1)) {
				if (spotMarkovMap.get(spot1).containsKey(spot2)) {
					spotMarkovMap.get(spot1).put(spot2, spotMarkovMap.get(spot1).get(spot2) + 1L);
				} else {
					spotMarkovMap.get(spot1).put(spot2, 1L);
				}
			} else {
				Map<Spot, Long> countMap = new HashMap<>();
				countMap.put(spot2, 1L);
				spotMarkovMap.put(spot1, countMap);
			}

			List<BetType> betTypeList1 = new ArrayList<>();
			List<BetType> betTypeList2 = new ArrayList<>();
			for (BetType betType : BetType.getAvailableList(rouletteContext.rouletteType)) {
				if (BetHelper.isWin(betType, spot1)) {
					betTypeList1.add(betType);
				}
				if (BetHelper.isWin(betType, spot2)) {
					betTypeList2.add(betType);
				}
			}

			for (BetType betType1 : betTypeList1) {
				for (BetType betType2 : betTypeList2) {
					if (betTypeMarkovMap.containsKey(betType1)) {
						if (betTypeMarkovMap.get(betType1).containsKey(betType2)) {
							betTypeMarkovMap.get(betType1).put(betType2,
									betTypeMarkovMap.get(betType1).get(betType2) + 1L);
						} else {
							betTypeMarkovMap.get(betType1).put(betType2, 1L);
						}
					} else {
						Map<BetType, Long> countMap = new HashMap<>();
						countMap.put(betType2, 1L);
						betTypeMarkovMap.put(betType1, countMap);
					}
				}
			}
			totalCount++;
		}
	}

	private void dumpMarkovMap() {
		LogHelper.debug("--- spotMarkovMap start ---");
		for (Map.Entry<Spot, Map<Spot, Long>> entry1 : spotMarkovMap.entrySet()) {
			for (Map.Entry<Spot, Long> entry2 : entry1.getValue().entrySet()) {
				LogHelper.debug(entry1.getKey().name() + "-" + entry2.getKey().name() + ":" + entry2.getValue());
			}
		}
		LogHelper.debug("--- spotMarkovMap end ---");
	}
}