package com.techelevator.snacks;

public class Candy extends Snack {

    public Candy (String snackName, double snackPrice) {
        super(snackName, snackPrice);

    }

    @Override
    public String getSnackMessage() {
        return "Munch Munch, Yum!";
    }

}