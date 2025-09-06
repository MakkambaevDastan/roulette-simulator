package model;

public class ColorPrediction {

    public double redProbability;

    public double blackProbability;

    public double greenProbability;

    public ColorPrediction(double redProbability, double blackProbability, double greenProbability) {
        this.redProbability = redProbability;
        this.blackProbability = blackProbability;
        this.greenProbability = greenProbability;
    }
}