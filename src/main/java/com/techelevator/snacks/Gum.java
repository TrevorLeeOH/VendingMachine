package com.techelevator.snacks;

public class Gum extends Snack {

    public Gum (String snackName, double snackPrice) {
        super(snackName, snackPrice);

    }

    @Override
    public String getSnackMessage() {
        return "Chew Chew, Yum!";
    }



}
