package strategy;

import application.Context;
import enums.BetType;
import model.Bet;

import java.util.List;

public class MartingaleStrategy5 extends BaseStrategy {

    private static final List<BetType> TYPES = List.of(
            BetType.STRAIGHT_UP_2,
            BetType.STRAIGHT_UP_3,
            BetType.STRAIGHT_UP_4,
            BetType.STRAIGHT_UP_5,
            BetType.STRAIGHT_UP_6,
            BetType.STRAIGHT_UP_7,
            BetType.STRAIGHT_UP_8,
            BetType.STRAIGHT_UP_9,
            BetType.STRAIGHT_UP_10,
            BetType.STRAIGHT_UP_11,
            BetType.STRAIGHT_UP_12,
            BetType.STRAIGHT_UP_13,
            BetType.STRAIGHT_UP_14,
            BetType.STRAIGHT_UP_15,
            BetType.STRAIGHT_UP_16,
            BetType.STRAIGHT_UP_17,
            BetType.STRAIGHT_UP_18,
            BetType.STRAIGHT_UP_19,
            BetType.STRAIGHT_UP_20,
            BetType.STRAIGHT_UP_21,
            BetType.STRAIGHT_UP_22,
            BetType.STRAIGHT_UP_23,
            BetType.STRAIGHT_UP_24,
            BetType.STRAIGHT_UP_25,
            BetType.STRAIGHT_UP_26,
            BetType.STRAIGHT_UP_27,
            BetType.STRAIGHT_UP_28,
            BetType.STRAIGHT_UP_29,
            BetType.STRAIGHT_UP_30,
            BetType.STRAIGHT_UP_31,
            BetType.STRAIGHT_UP_32,
            BetType.STRAIGHT_UP_33,
            BetType.STRAIGHT_UP_34,
            BetType.STRAIGHT_UP_35,
            BetType.STRAIGHT_UP_36
    );

    public MartingaleStrategy5(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return "マーチンゲール法(0、00、1以外)";
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        if (wasLastBetWon(context)) {
            return create(context.getMin());
        } else if (hasLastBet()) {
            // FIXME
            return create(lastBets.getFirst().value() * 36);
        }
        return create(context.getMin());
    }

    private List<Bet> create(long value) {
        return TYPES.stream()
                .map(type -> Bet.builder().type(type).value(value).build())
                .toList();
    }
}
