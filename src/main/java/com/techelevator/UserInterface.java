package com.techelevator;

import com.techelevator.snacks.Snack;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.*;
import java.util.Scanner;

public class UserInterface {
    Scanner userInput = new Scanner(System.in);
    private VendingMachine vendingMachine;

    private final String[] MAIN_MENU = {" (1) Display Vending Machine Items", " (2) Purchase", " (3) Exit"};
    private final String[] PURCHASE_MENU = {" (1) Feed Money", " (2) Select Product", " (3) Finish Transaction"};
    private final int MAIN_MENU_OPTION_COUNT = 4;
    private final int PURCHASE_MENU_OPTION_COUNT = 3;

    public UserInterface(VendingMachine vendingMachine){
        this.vendingMachine = vendingMachine;

    }

    public void mainMenu() {
        System.out.println();
        for (String menuLine : MAIN_MENU) {
            System.out.println(menuLine);
        }
        int menuChoice = 5;
        for (int i = 0; i < 10; i++) {
            try {
                menuChoice = Integer.parseInt(userInput.nextLine());
                if (menuChoice > 0 && menuChoice <= MAIN_MENU_OPTION_COUNT) {
                    break;
                }
            } catch (NumberFormatException e) {}
            System.out.println();
            System.out.println("Please input 1, 2, or 3");
        }

        switch (menuChoice) {
            case 1:
                displayVendingMachineItems();
                mainMenu();
                break;
            case 2:
                purchaseMenu();
                break;
            case 3:
                System.exit(0);
                break;
            case 4:
                printSalesReport();
                mainMenu();
                break;
            default:
                System.exit(1);
        }

    }

    public void displayVendingMachineItems() {
        System.out.println();
        List<String> vendingItems = new ArrayList<>();
        Map<String,Snack> displayMap = vendingMachine.getInventory();
        for (Map.Entry<String,Snack> entry : displayMap.entrySet()) {
            vendingItems.add(entry.getKey() + " | " +entry.getValue().toString());
        }
        vendingItems.sort(null);
        for(String item : vendingItems) {
            System.out.println(item);
        }

    }

    public void printSalesReport() {
        System.out.println();
        try (Scanner reader = new Scanner(new File(vendingMachine.SALES_REPORT_FILE_PATH))) {
            while (reader.hasNextLine()) {
                System.out.println(reader.nextLine());
            }

        } catch (FileNotFoundException e) {
            System.out.println("Sales Report File not found--sorry!");
        }

    }

    public void purchaseMenu() {
        System.out.println();
        for (String menuLine : PURCHASE_MENU) {
            System.out.println(menuLine);
        }
        System.out.println();
        System.out.println("Current Money Provided: " + NumberFormat.getCurrencyInstance().format(vendingMachine.getBalance()));
        int menuChoice = 3;
        for (int i = 0; i < 10; i++) {
            try {
                menuChoice = Integer.parseInt(userInput.nextLine());
                if (menuChoice >0 && menuChoice <= PURCHASE_MENU_OPTION_COUNT) {
                    break;
                }
            } catch (NumberFormatException e) {}
            System.out.println("Please input 1, 2, or 3");
        }

        switch (menuChoice) {
            case 1:
                feedMoney();
                break;
            case 2:
                selectProduct();
                break;
            case 3:
                finishTransaction();
                break;
            default:
                System.exit(1);
        }


    }

    public void feedMoney() {
        System.out.println("Please insert cash; Vending Machine accepts 1$, 5$, 10$, and 20$ bills");
        System.out.println("Return to the purchase menu at any time by entering '0'");

        int attempts = 0;
        while (true) {
            try {
                int dollarAmount = Integer.parseInt(userInput.nextLine());
                if (dollarAmount == 0) {
                    purchaseMenu();
                    return;
                } else if ((dollarAmount == 1 || dollarAmount == 5 || dollarAmount == 10 || dollarAmount == 20)) {
                    double newBalance = vendingMachine.insertMoney(dollarAmount);
                    System.out.println();
                    System.out.println("New balance: " + NumberFormat.getCurrencyInstance().format(newBalance));
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println("Please input 1, 5, 10, or 20");
                attempts++;
                if (attempts > 10) {
                    System.exit(1);
                }
            }
        }
    }

    public void selectProduct() {
        displayVendingMachineItems();
        System.out.println();
        System.out.println("Please input the slot letter & number of your desired snack (i.e. A1, C3): >>>");
        for (int i = 0; i < 10; i++) {
            String productChoice = userInput.nextLine().toUpperCase();
            if (vendingMachine.getInventory().containsKey(productChoice)) {
                String purchaseMessage = vendingMachine.purchaseSnack(productChoice);
                System.out.println(purchaseMessage);
                String balanceToPrint = NumberFormat.getCurrencyInstance().format(vendingMachine.getBalance());
                System.out.println("Remaining balance = " + balanceToPrint);
                purchaseMenu();
                return;
            }
            System.out.println("Please input a valid slot code.");
        }
        System.exit(1);
    }



    public void finishTransaction() {
        String changeMessage = vendingMachine.finishTransaction();
        System.out.println(changeMessage);
        mainMenu();

    }























}
