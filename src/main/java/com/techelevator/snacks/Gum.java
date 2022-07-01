package com.techelevator.snacks;

public class Gum extends Snack {

    public Gum (String snackName) {
        super(snackName);
    }

    @Override
    public String getSnackMessage() {
        return "Chew Chew, Yum!";
    }


    @Override
    public Snack duplicate() {
        return new Gum(snackName);
    }

}
