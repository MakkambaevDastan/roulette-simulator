package model;

import enums.BetType;

public class Bet {

    public BetType betType;

    public long value;

    public Bet(BetType betType, long value) {
        this.betType = betType;
        this.value = value;
    }
}