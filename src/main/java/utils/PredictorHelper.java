package utils;

import predictor.BasePredictor;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class PredictorHelper {

    private static final Map<Class<? extends BasePredictor>, BasePredictor> PREDICTOR_CACHE = new ConcurrentHashMap<>();

    private PredictorHelper() {
        throw new AssertionError("PredictorHelper cannot be instantiated");
    }

    public static BasePredictor getInstance(Class<? extends BasePredictor> clazz) {
        Objects.requireNonNull(clazz, "Predictor class must not be null");

        return PREDICTOR_CACHE.computeIfAbsent(clazz, key -> {
            try {
                return key.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new IllegalArgumentException("Failed to create instance of " + key.getName(), e);
            }
        });
    }
}