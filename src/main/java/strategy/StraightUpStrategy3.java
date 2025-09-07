package strategy;

import application.Context;
import enums.BetType;
import enums.Spot;
import model.Bet;
import utils.BetHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class StraightUpStrategy3 extends BaseStrategy {

    private int count = 0;

    public StraightUpStrategy3(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return "ストレート複数賭け2";
    }


    @Override
    public List<Bet> getNextInternal(Context context) {
        count = wasLastBetWon(context) ? count + 1 : 0;

        List<Spot> spotHistory = context.getSpotHistory();
        Set<Spot> excludedSpots = new HashSet<>(spotHistory.subList(
                Math.max(0, spotHistory.size() - count),
                spotHistory.size()
        ));

        List<Spot> availableSpots = Spot.getAvailableList(context.getRouletteType());

        return availableSpots.stream()
                .filter(spot -> !excludedSpots.contains(spot))
                .map(spot -> {
                    BetType type = BetHelper.getStraightUpBetType(spot);
                    return Bet.builder()
                            .type(type)
                            .value(context.getMin())
                            .build();
                })
                .toList();
    }
}