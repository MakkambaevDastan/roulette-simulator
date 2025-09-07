package model;

import enums.BetType;
import lombok.Builder;

@Builder
public record Bet(BetType type, long value) {
}