package com.techelevator.Exceptions;

public class InvalidSlotException extends Exception {

    public InvalidSlotException() {
        super("Invalid slot; please input a valid slot code.");
    }

}
