package com.techelevator.snacks;

public class Chip extends Snack {

    public Chip (String snackName) {
        super(snackName);
    }

    @Override
    public String getSnackMessage() {
        return "Crunch Crunch, Yum!";
    }

    @Override
    public Snack duplicate() {
        return new Chip(snackName);
    }

}
