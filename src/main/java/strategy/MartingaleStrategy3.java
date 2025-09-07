package strategy;

import application.Context;
import enums.BetType;
import model.Bet;
import model.ColorPrediction;
import predictor.BasePredictor;
import predictor.CountPredictor2;
import utils.BetHelper;
import utils.PredictorHelper;

import java.util.Collections;
import java.util.List;

import static enums.BetType.BLACK;
import static enums.BetType.RED;

public class MartingaleStrategy3 extends BaseStrategy {

    private static final BasePredictor PREDICTOR = PredictorHelper.getInstance(CountPredictor2.class);

    public MartingaleStrategy3(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return MartingaleStrategy3.class.getSimpleName();
    }

    @Override
    public List<Bet> getNextInternal(Context context) {
        ColorPrediction colorPrediction = PREDICTOR.getNextColorPrediction(context);
        BetType type = colorPrediction.black() <= colorPrediction.red() ? RED : BLACK;

        boolean wonLastBet = false;
        long lastBetValue = 0;
        if (lastBets != null) {
            lastBetValue = BetHelper.getTotalBetValue(lastBets);
            for (Bet bet : lastBets) {
                if (BetHelper.isWin(bet, context.getLastSpot())) {
                    wonLastBet = true;
                }
            }
        }

        if (wonLastBet) {
            return create(type, context.getMin());
        } else {
            // FIXME
            return create(type, (lastBetValue * 2));
        }
    }

    private List<Bet> create(BetType type, long value) {
        return Collections.singletonList(Bet.builder().type(type).value(value).build());
    }
}