package model;

import lombok.Builder;

@Builder
public record ColorPrediction(double red, double black, double green) {
}