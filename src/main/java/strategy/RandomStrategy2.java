package strategy;

import application.Context;
import constants.Configurations;
import enums.BetType;
import model.Bet;
import utils.BetHelper;

import java.util.ArrayList;
import java.util.List;

public class RandomStrategy2 extends BaseStrategy {

    private static final int SIZE = 10;

    public RandomStrategy2(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return "ランダム2";
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        List<Bet> betList = new ArrayList<>();
        List<BetType> betTypeList = BetType.getAvailableList(context.getRouletteType());
        if (wasLastBetWon(context)) {
            for (Bet bet : lastBets) {
                if (BetHelper.isWin(bet, context.getLastSpot())) {
                    betList.add(bet);
                }
            }
        }
        while (betList.size() < SIZE) {
            BetType type = betTypeList.get(Configurations.RANDOM.nextInt(betTypeList.size()));
            int multiplier = Configurations.RANDOM.nextInt(10) + 1;
            betList.add(Bet.builder()
                    .type(type)
                    .value(context.getMin() * multiplier)
                    .build());
        }
        return betList;
    }
}