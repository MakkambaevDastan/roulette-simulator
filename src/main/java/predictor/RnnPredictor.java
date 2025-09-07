//package predictor;
//
//import application.Context;
//import constants.Configurations;
//import enums.Spot;
//import model.SpotPrediction;
//import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
//import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
//import org.deeplearning4j.nn.conf.layers.LSTM;
//import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
//import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
//import org.deeplearning4j.nn.weights.WeightInit;
//import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
//import org.nd4j.linalg.activations.Activation;
//import org.nd4j.linalg.api.ndarray.INDArray;
//import org.nd4j.linalg.api.ops.impl.indexaccum.custom.ArgMax;
//import org.nd4j.linalg.dataset.DataSet;
//import org.nd4j.linalg.factory.Nd4j;
//import org.nd4j.linalg.learning.config.RmsProp;
//import org.nd4j.linalg.lossfunctions.LossFunctions;
//import utils.LogHelper;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RnnPredictor extends BasePredictor {
//
//    private static final int HIDDEN_LAYER_WIDTH = 50;
//
//    private static final int HIDDEN_LAYER_CONT = 2;
//    private MultiLayerNetwork useNet;
//
//    @Override
//    public List<SpotPrediction> getNextSpotPredictionList(Context context) {
//        List<SpotPrediction> spotPredictionList = new ArrayList<>();
//        if (useNet == null) {
//            if (context.getSpotHistory().size() == Configurations.SPOT_HISTORY_LIST_SIZE) {
//                useNet = getNet(context, context.getSpotHistory().toArray(new Spot[0]));
//            }
//        } else {
//            List<Spot> availableSpotList = Spot.getAvailableList(context.getRouletteType());
//
//            INDArray testInit = Nd4j.zeros(1, availableSpotList.size(), 1);
//            testInit.putScalar(availableSpotList.indexOf(context.getSpotHistory().getFirst()), 1);
//            INDArray output = useNet.rnnTimeStep(testInit);
//
//            for (int i = 1; i < context.getSpotHistory().size(); i++) {
//                int sampledCharacterIdx = Nd4j.getExecutioner().exec(new ArgMax(output, 1))[0].getInt(0);
//                INDArray nextInput = Nd4j.zeros(1, availableSpotList.size(), 1);
//                nextInput.putScalar(availableSpotList.indexOf(context.getSpotHistory().get(i)), 1);
//                output = useNet.rnnTimeStep(nextInput);
//            }
//
//            int sampledCharacterIdx = Nd4j.getExecutioner().exec(new ArgMax(output, 1))[0].getInt(0);
//            LogHelper.info("次の予想:" + availableSpotList.get(sampledCharacterIdx));
//
//            spotPredictionList.add(new SpotPrediction(availableSpotList.get(sampledCharacterIdx), 1));
//        }
//        return spotPredictionList;
//    }
//
//    private static MultiLayerNetwork getNet(Context context, Spot[] spotHistoryArray) {
//        List<Spot> availableSpotList = Spot.getAvailableList(context.getRouletteType());
//
//        NeuralNetConfiguration.Builder builder = new NeuralNetConfiguration.Builder();
//        builder.seed(123);
//        builder.biasInit(0);
//        builder.miniBatch(false);
//        builder.updater(new RmsProp(0.001));
//        builder.weightInit(WeightInit.XAVIER);
//
//        NeuralNetConfiguration.ListBuilder listBuilder = builder.list();
//
//        for (int i = 0; i < HIDDEN_LAYER_CONT; i++) {
//            LSTM.Builder hiddenLayerBuilder = new LSTM.Builder();
//            hiddenLayerBuilder.nIn(i == 0 ? availableSpotList.size() : HIDDEN_LAYER_WIDTH);
//            hiddenLayerBuilder.nOut(HIDDEN_LAYER_WIDTH);
//            hiddenLayerBuilder.activation(Activation.TANH);
//            listBuilder.layer(i, hiddenLayerBuilder.build());
//        }
//
//        RnnOutputLayer.Builder outputLayerBuilder = new RnnOutputLayer.Builder(LossFunctions.LossFunction.MCXENT);
//
//        outputLayerBuilder.activation(Activation.SOFTMAX);
//        outputLayerBuilder.nIn(HIDDEN_LAYER_WIDTH);
//        outputLayerBuilder.nOut(availableSpotList.size());
//        listBuilder.layer(HIDDEN_LAYER_CONT, outputLayerBuilder.build());
//
//        MultiLayerConfiguration conf = listBuilder.build();
//        MultiLayerNetwork net = new MultiLayerNetwork(conf);
//        net.init();
//        net.setListeners(new ScoreIterationListener(1));
//
//        INDArray input = Nd4j.zeros(1, availableSpotList.size(), spotHistoryArray.length);
//        INDArray labels = Nd4j.zeros(1, availableSpotList.size(), spotHistoryArray.length);
//
//        int samplePos = 0;
//        for (Spot currentChar : spotHistoryArray) {
//            Spot nextChar = spotHistoryArray[(samplePos + 1) % (spotHistoryArray.length)];
//
//            input.putScalar(new int[]{0, availableSpotList.indexOf(currentChar), samplePos}, 1);
//
//            labels.putScalar(new int[]{0, availableSpotList.indexOf(nextChar), samplePos}, 1);
//            samplePos++;
//        }
//        DataSet trainingData = new DataSet(input, labels);
//
//        for (int epoch = 0; epoch < 100; epoch++) {
//
//            LogHelper.debug("Epoch " + epoch);
//
//            net.fit(trainingData);
//
//            net.rnnClearPreviousState();
//
//            INDArray testInit = Nd4j.zeros(1, availableSpotList.size(), 1);
//            testInit.putScalar(availableSpotList.indexOf(spotHistoryArray[0]), 1);
//
//            INDArray output = net.rnnTimeStep(testInit);
//
//            for (Spot ignored : spotHistoryArray) {
//
//                int sampledCharacterIdx = Nd4j.getExecutioner().exec(new ArgMax(output, 1))[0].getInt(0);
//
//                System.out.print(availableSpotList.get(sampledCharacterIdx));
//                System.out.print(",");
//
//                INDArray nextInput = Nd4j.zeros(1, availableSpotList.size(), 1);
//                nextInput.putScalar(sampledCharacterIdx, 1);
//                output = net.rnnTimeStep(nextInput);
//
//            }
//            System.out.print("\n");
//        }
//
//        return net;
//    }
//}