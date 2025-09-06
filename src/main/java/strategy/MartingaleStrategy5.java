package strategy;

import application.RouletteContext;
import enums.BetType;
import model.Bet;

import java.util.ArrayList;
import java.util.List;

public class MartingaleStrategy5 extends BaseStrategy {

    public MartingaleStrategy5(RouletteContext rouletteContext) {
        super(rouletteContext);
    }

    @Override
    public String getStrategyName() {
        return "マーチンゲール法(0、00、1以外)";
    }

    @Override
    public List<Bet> getNextBetListImpl(RouletteContext rouletteContext) {
        List<Bet> betList = new ArrayList<>();

        if (wasLastBetWon(rouletteContext)) {
            for (BetType betType : getUseBetTypeList()) {
                betList.add(new Bet(betType, rouletteContext.minimumBet));
            }
        } else {
            if (hasLastBet()) {
                // FIXME
                long betValue = lastBetList.get(0).value * 36;
                for (BetType betType : getUseBetTypeList()) {
                    betList.add(new Bet(betType, betValue));
                }
            } else {
                for (BetType betType : getUseBetTypeList()) {
                    betList.add(new Bet(betType, rouletteContext.minimumBet));
                }
            }
        }
        return betList;
    }

    private static List<BetType> getUseBetTypeList() {
        List<BetType> betTypeList = new ArrayList<>();
        betTypeList.add(BetType.STRAIGHT_UP_2);
        betTypeList.add(BetType.STRAIGHT_UP_3);
        betTypeList.add(BetType.STRAIGHT_UP_4);
        betTypeList.add(BetType.STRAIGHT_UP_5);
        betTypeList.add(BetType.STRAIGHT_UP_6);
        betTypeList.add(BetType.STRAIGHT_UP_7);
        betTypeList.add(BetType.STRAIGHT_UP_8);
        betTypeList.add(BetType.STRAIGHT_UP_9);
        betTypeList.add(BetType.STRAIGHT_UP_10);
        betTypeList.add(BetType.STRAIGHT_UP_11);
        betTypeList.add(BetType.STRAIGHT_UP_12);
        betTypeList.add(BetType.STRAIGHT_UP_13);
        betTypeList.add(BetType.STRAIGHT_UP_14);
        betTypeList.add(BetType.STRAIGHT_UP_15);
        betTypeList.add(BetType.STRAIGHT_UP_16);
        betTypeList.add(BetType.STRAIGHT_UP_17);
        betTypeList.add(BetType.STRAIGHT_UP_18);
        betTypeList.add(BetType.STRAIGHT_UP_19);
        betTypeList.add(BetType.STRAIGHT_UP_20);
        betTypeList.add(BetType.STRAIGHT_UP_21);
        betTypeList.add(BetType.STRAIGHT_UP_22);
        betTypeList.add(BetType.STRAIGHT_UP_23);
        betTypeList.add(BetType.STRAIGHT_UP_24);
        betTypeList.add(BetType.STRAIGHT_UP_25);
        betTypeList.add(BetType.STRAIGHT_UP_26);
        betTypeList.add(BetType.STRAIGHT_UP_27);
        betTypeList.add(BetType.STRAIGHT_UP_28);
        betTypeList.add(BetType.STRAIGHT_UP_29);
        betTypeList.add(BetType.STRAIGHT_UP_30);
        betTypeList.add(BetType.STRAIGHT_UP_31);
        betTypeList.add(BetType.STRAIGHT_UP_32);
        betTypeList.add(BetType.STRAIGHT_UP_33);
        betTypeList.add(BetType.STRAIGHT_UP_34);
        betTypeList.add(BetType.STRAIGHT_UP_35);
        betTypeList.add(BetType.STRAIGHT_UP_36);
        return betTypeList;
    }
}