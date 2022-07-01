package com.techelevator.snacks;

public class Candy extends Snack {

    public Candy (String snackName) {
        super(snackName);
    }

    @Override
    public String getSnackMessage() {
        return "Munch Munch, Yum!";
    }

    @Override
    public Snack duplicate() {
        return new Candy(snackName);
    }

}