package model;

import enums.BetType;

public class BetTypePrediction {

	public BetType betType;

	public double probability;

	public BetTypePrediction(BetType betType, double probability) {
		this.betType = betType;
		this.probability = probability;
	}
}