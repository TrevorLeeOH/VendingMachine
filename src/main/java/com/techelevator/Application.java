package com.techelevator;


import com.techelevator.snacks.*;

public class Application {

    public static void main(String[] args) {
        VendingMachine vendingMachine = new VendingMachine();
        UserInterface ui = new UserInterface(vendingMachine);
        ui.mainMenu();

    }
}