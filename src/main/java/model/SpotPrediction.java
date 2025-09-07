package model;

import enums.Spot;
import lombok.Builder;

@Builder
public record SpotPrediction(Spot spot, double probability) {
}