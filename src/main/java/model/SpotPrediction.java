package model;

import enums.Spot;

public class SpotPrediction {

	public Spot spot;

	public double probability;

	public SpotPrediction(Spot spot, double probability) {
		this.spot = spot;
		this.probability = probability;
	}
}