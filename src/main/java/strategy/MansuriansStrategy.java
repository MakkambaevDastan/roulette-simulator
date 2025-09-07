package strategy;

import application.Context;
import model.Bet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MansuriansStrategy extends BaseStrategy {

    public MansuriansStrategy(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return MansuriansStrategy.class.getSimpleName();
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        List<Bet> betList = new ArrayList<>();
        // TODO
        return betList;
    }
}