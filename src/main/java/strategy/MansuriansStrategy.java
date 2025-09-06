package strategy;

import java.util.ArrayList;
import java.util.List;

import application.RouletteContext;
import model.Bet;

public class MansuriansStrategy extends BaseStrategy {

	public MansuriansStrategy(RouletteContext rouletteContext) {
		super(rouletteContext);
	}

	@Override
	public String getStrategyName() {
		return "マンシュリアン法";
	}

	@Override
	public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
		List<Bet> betList = new ArrayList<>();
		// TODO
		return betList;
	}
}