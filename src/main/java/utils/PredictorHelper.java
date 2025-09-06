package utils;

import predictor.BasePredictor;

import java.util.HashMap;
import java.util.Map;

public class PredictorHelper {

    private static final Map<Class<? extends BasePredictor>, BasePredictor> INSTANCE_MAP = new HashMap<>();

    public static BasePredictor getInstance(Class<? extends BasePredictor> clazz) {
        try {
            if (!INSTANCE_MAP.containsKey(clazz)) {
                INSTANCE_MAP.put(clazz, clazz.newInstance());
            }
            return INSTANCE_MAP.get(clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}