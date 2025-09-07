package strategy;

import application.Context;
import enums.BetType;
import model.Bet;

import java.util.ArrayList;
import java.util.List;

public class SevenKaimeStrategy extends BaseStrategy {

    public SevenKaimeStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return "7回目の法則(赤のみ)";
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        List<Bet> betList = new ArrayList<>();
        if (7 <= context.getSpotHistory().size()) {
            boolean notMatched = false;
            for (int i = 0; i < 7; i++) {
                if (!context.getSpotHistory().get(context.getSpotHistory().size() - (1 + i)).isBlack()) {
                    notMatched = true;
                    break;
                }
            }
            if (!notMatched) {
                betList.add(Bet.builder().type(BetType.RED).value(context.getMin()).build());
            }
        }
        return betList;
    }
}
