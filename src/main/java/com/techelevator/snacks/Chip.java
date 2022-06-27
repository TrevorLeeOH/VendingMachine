package com.techelevator.snacks;

public class Chip extends Snack {

    public Chip (String snackName, double snackPrice) {
        super(snackName, snackPrice);

    }

    @Override
    public String getSnackMessage() {
        return "Crunch Crunch, Yum!";
    }

}
