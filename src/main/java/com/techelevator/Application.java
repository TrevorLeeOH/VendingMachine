package com.techelevator;


import com.techelevator.snacks.*;

public class Application {

    public static void main(String[] args) {
        VendingMachineDao dao = new TextFileVendingMachineDao();
        VendingMachine vendingMachine = new VendingMachine(dao);
        UserInterface ui = new UserInterface(vendingMachine);
        ui.mainMenu();

    }
}