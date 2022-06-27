package com.techelevator.snacks;

public class Drink extends Snack {

    public Drink (String snackName, double snackPrice) {
        super(snackName, snackPrice);

    }

    @Override
    public String getSnackMessage() {
        return "Glug Glug, Yum!";
    }

}
