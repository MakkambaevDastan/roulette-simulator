package strategy;

import application.Context;
import enums.BetType;
import enums.Spot;
import model.Bet;

import java.util.ArrayList;
import java.util.List;

public class Rate9946PercentStrategy extends BaseStrategy {

    public Rate9946PercentStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return Rate9946PercentStrategy.class.getSimpleName();
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        List<Bet> betList = new ArrayList<>();
        if (4 <= context.getSpotHistory().size()) {
            Spot spot1 = context.getSpotHistory().get(context.getSpotHistory().size() - 4);
            Spot spot2 = context.getSpotHistory().get(context.getSpotHistory().size() - 3);
            Spot spot3 = context.getSpotHistory().get(context.getSpotHistory().size() - 2);
            Spot spot4 = context.getSpotHistory().getLast();
            if (spot1.isFirstDozenOrZeroAndDoubleZero()
                    && spot2.isFirstDozenOrZeroAndDoubleZero()
                    && spot3.isFirstDozenOrZeroAndDoubleZero()
                    && spot4.isFirstDozenOrZeroAndDoubleZero()) {
                betList.add(Bet.builder().type(BetType.SECOND_DOZEN).value(context.getMin()).build());
                betList.add(Bet.builder().type(BetType.THIRD_DOZEN).value(context.getMin()).build());
            }
        }
        return betList;
    }
}
