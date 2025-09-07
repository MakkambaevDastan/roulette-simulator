package application;

import constants.Configurations;
import enums.HeatmapLayoutType;
import enums.RouletteType;
import enums.Spot;
import enums.SpotGenerateType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@Getter
public final class Context {
    private final RouletteType rouletteType;
    private final HeatmapLayoutType heatmapLayoutType;
    private final SpotGenerateType spotGenerateType;
    private final long start;
    private final long min;
    private final long max;
    private final List<Spot> spotHistory;
    private final Map<Spot, Integer> spotFrequencies;
    private long currentLoopCount;
    private final long simulationSpeed;

    private Context(Builder builder) {
        Objects.requireNonNull(builder.rouletteType, "rouletteType не может быть null");
        Objects.requireNonNull(builder.heatmapLayoutType, "heatmapLayoutType не может быть null");
        Objects.requireNonNull(builder.spotGenerateType, "spotGenerateType не может быть null");

        this.rouletteType = builder.rouletteType;
        this.heatmapLayoutType = builder.heatmapLayoutType;
        this.spotGenerateType = builder.spotGenerateType;
        this.start = builder.start;
        this.min = builder.min;
        this.max = builder.max;

        this.spotHistory = new ArrayList<>();
        this.spotFrequencies = new EnumMap<>(Spot.class);
        currentLoopCount = 0;
        simulationSpeed = 100;
    }

    public void addSpotHistory(Spot spot) {
        spotHistory.addLast(spot);
        spotFrequencies.merge(spot, 1, Integer::sum);

        if (spotHistory.size() > Configurations.SPOT_HISTORY_LIST_SIZE) {
            Spot removedSpot = spotHistory.removeFirst();
            spotFrequencies.computeIfPresent(removedSpot, (key, count) -> (count > 1) ? count - 1 : null);
        }
        currentLoopCount++;
    }

    public double getRate(Predicate<Spot> condition) {
        if (spotHistory.isEmpty()) {
            return 0.0;
        }
        double matchCount = 0;
        for (Map.Entry<Spot, Integer> entry : spotFrequencies.entrySet()) {
            if (condition.test(entry.getKey())) {
                matchCount += entry.getValue();
            }
        }
        return matchCount / spotHistory.size();
    }

    public double getRedRate() {
        return getRate(Spot::isRed);
    }

    public double getBlackRate() {
        return getRate(Spot::isBlack);
    }

    public double getGreenRate() {
        return getRate(Spot::isGreen);
    }
    public int getSpotCount(Spot spot) {
        return spotFrequencies.getOrDefault(spot, 0);
    }

    public Spot getLastSpot() {
        return spotHistory.getLast();
    }

    public static class Builder {
        private final RouletteType rouletteType;
        private final SpotGenerateType spotGenerateType;
        private HeatmapLayoutType heatmapLayoutType = HeatmapLayoutType.WHEEL_ORDER;
        private long start = 1000;
        private long min = 1;
        private long max = 100;

        public Builder(RouletteType rouletteType, SpotGenerateType spotGenerateType) {
            this.rouletteType = rouletteType;
            this.spotGenerateType = spotGenerateType;
        }

        public Builder heatmapLayoutType(HeatmapLayoutType heatmapLayoutType) {
            this.heatmapLayoutType = heatmapLayoutType;
            return this;
        }

        public Builder start(long start) {
            this.start = start;
            return this;
        }

        public Builder min(long min) {
            this.min = min;
            return this;
        }

        public Builder max(long max) {
            this.max = max;
            return this;
        }

        public Context build() {
            return new Context(this);
        }
    }
}