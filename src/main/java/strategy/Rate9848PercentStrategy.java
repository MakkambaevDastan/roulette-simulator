package strategy;

import application.Context;
import enums.BetType;
import enums.Spot;
import model.Bet;

import java.util.ArrayList;
import java.util.List;

public class Rate9848PercentStrategy extends BaseStrategy {

    public Rate9848PercentStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return Rate9848PercentStrategy.class.getSimpleName();
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        List<Bet> betList = new ArrayList<>();
        if (3 <= context.getSpotHistory().size()) {
            Spot spot1 = context.getSpotHistory().get(context.getSpotHistory().size() - 3);
            Spot spot2 = context.getSpotHistory().get(context.getSpotHistory().size() - 2);
            Spot spot3 = context.getSpotHistory().getLast();
            if (spot1.isFirstDozenOrZeroAndDoubleZero()
                    && spot2.isFirstDozenOrZeroAndDoubleZero()
                    && spot3.isFirstDozenOrZeroAndDoubleZero()) {
                long useBet = (lastBets != null && !lastBets.isEmpty()) ? lastBets.getFirst().value() * 2 : context.getMin();
                betList.add(Bet.builder().type(BetType.SECOND_DOZEN).value(useBet).build());
                betList.add(Bet.builder().type(BetType.THIRD_DOZEN).value(useBet).build());
            }
        }
        return betList;
    }
}
