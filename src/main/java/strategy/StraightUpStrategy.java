//package strategy;
//
//import application.Context;
//import enums.BetType;
//import enums.Spot;
//import model.Bet;
//import model.SpotPrediction;
//import predictor.BasePredictor;
//import predictor.RnnPredictor;
//import utils.BetHelper;
//import utils.PredictorHelper;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class StraightUpStrategy extends BaseStrategy {
//
//    private static final BasePredictor PREDICTOR = PredictorHelper.getInstance(RnnPredictor.class);
//
//    public StraightUpStrategy(Context context) {
//        super(context);
//    }
//
//    @Override
//    public String getStrategyName() {
//        return "1点賭け(予測器を使用)";
//    }
//
//    @Override
//    public List<Bet> getNextBetListImpl(Context context) {
//        List<Bet> betList = new ArrayList<>();
//
//        double maxProbability = 0;
//        Spot spot = null;
//        for (SpotPrediction spotPrediction : PREDICTOR.getNextSpotPredictionList(context)) {
//            if (maxProbability < spotPrediction.probability) {
//                maxProbability = spotPrediction.probability;
//                spot = spotPrediction.spot;
//            }
//        }
//
//        if (spot != null) {
//            BetType useBetType = BetHelper.getStraightUpBetType(spot);
//
//            betList.add(new Bet(useBetType, context.getMinimumBet()));
//        }
//        return betList;
//    }
//}