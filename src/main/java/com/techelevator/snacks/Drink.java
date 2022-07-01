package com.techelevator.snacks;

public class Drink extends Snack {

    public Drink (String snackName) {
        super(snackName);
    }

    @Override
    public String getSnackMessage() {
        return "Glug Glug, Yum!";
    }

    @Override
    public Snack duplicate() {
        return new Drink(snackName);
    }

}
