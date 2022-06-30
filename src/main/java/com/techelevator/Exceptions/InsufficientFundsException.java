package com.techelevator.Exceptions;

public class InsufficientFundsException extends Exception {

    public InsufficientFundsException() {
        super("Insufficient funds; please insert more cash");
    }
}
