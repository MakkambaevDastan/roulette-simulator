package model;

import enums.BetType;
import lombok.Builder;

@Builder
public record BetTypePrediction(BetType type, double probability) {
}