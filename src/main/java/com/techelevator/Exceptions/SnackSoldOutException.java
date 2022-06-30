package com.techelevator.Exceptions;

public class SnackSoldOutException extends Exception {

    public SnackSoldOutException() {
        super("Snack sold out; please select another snack");
    }

}
