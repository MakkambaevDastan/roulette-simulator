package strategy;

import application.Context;
import constants.Configurations;
import enums.BetType;
import model.Bet;

import java.util.ArrayList;
import java.util.List;

public class RandomStrategy extends BaseStrategy {

    public RandomStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return RandomStrategy.class.getSimpleName();
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        List<Bet> betList = new ArrayList<>();
        List<BetType> betTypeList = BetType.getAvailableList(context.getRouletteType());
        for (int i = 0; i < Configurations.RANDOM.nextInt(100); i++) {
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