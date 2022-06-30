package com.techelevator;

import com.techelevator.snacks.Snack;
import java.util.*;
import java.util.Scanner;

public class UserInterface {
    Scanner userInput = new Scanner(System.in);
    private final VendingMachine vendingMachine;

    private final String[] MAIN_MENU = {" (1) Display Vending Machine Items", " (2) Purchase", " (3) Exit"};
    private final String[] PURCHASE_MENU = {" (1) Feed Money", " (2) Select Product", " (3) Finish Transaction"};
    private final int MAIN_MENU_OPTION_COUNT = 4;
    private final int PURCHASE_MENU_OPTION_COUNT = 3;

    public UserInterface(VendingMachine vendingMachine){
        this.vendingMachine = vendingMachine;

    }

    public void mainMenu() {
        do {
            System.out.println();
            System.out.println("------------ Main Menu ------------");
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
                } catch (NumberFormatException ignored) {}
                System.out.println();
                System.out.println("Please input 1, 2, or 3");
            }

            switch (menuChoice) {
                case 1:
                    displayVendingMachineItems();
                    continueMenu();
                    break;
                case 2:
                    purchaseMenu();
                    break;
                case 3:
                    System.exit(0);
                case 4:
                    printSalesReport();
                    continueMenu();
                    break;
                default:
                    System.exit(1);
            }

        } while (true);
    }

    public void displayVendingMachineItems() {
        System.out.println();
        List<String> vendingItems = new ArrayList<>();
        Map<String, SnackSlot> displayMap = vendingMachine.getInventory();
        for (Map.Entry<String,SnackSlot> entry : displayMap.entrySet()) {
            vendingItems.add(entry.getKey()
                    + " | " + entry.getValue().snackMaster.snackName
                    + " | " + VendingMachine.intCentsToStringDollars(entry.getValue().price)
                    + " | " + entry.getValue().count() + " remaining");
        }
        vendingItems.sort(null);
        for(String item : vendingItems) {
            System.out.println(item);
        }
    }

    public void printSalesReport() {
        System.out.println();
        List<String> salesReport = vendingMachine.dao.getSalesReport();
        for (String entry : salesReport) {
            System.out.println(entry);
        }
    }

    public void purchaseMenu() {
        do {
            System.out.println();
            System.out.println("------------ Purchase Menu ------------");
            for (String menuLine : PURCHASE_MENU) {
                System.out.println(menuLine);
            }
            System.out.println();
            System.out.println("Current Money Provided: " + vendingMachine.getBalanceAsMoney());
            int menuChoice = 3;
            for (int i = 0; i < 10; i++) {
                try {
                    menuChoice = Integer.parseInt(userInput.nextLine());
                    if (menuChoice >0 && menuChoice <= PURCHASE_MENU_OPTION_COUNT) {
                        break;
                    }
                } catch (NumberFormatException ignored) {}
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
                    return;
                default:
                    System.exit(1);
            }
        } while (true);
    }

    public void feedMoney() {
        System.out.println("Please insert cash; Vending Machine accepts 1$, 5$, 10$, and 20$ bills. Return to the purchase menu at any time by entering '0'");
        System.out.println();

        int attempts = 0;
        while (true) {
            try {
                int dollarAmount = Integer.parseInt(userInput.nextLine());
                if (dollarAmount == 0) {
                    break;
                } else if ((dollarAmount == 1 || dollarAmount == 5 || dollarAmount == 10 || dollarAmount == 20)) {
                    vendingMachine.insertMoney(dollarAmount * 100);
                    String newBalance = vendingMachine.getBalanceAsMoney();
                    System.out.println("New balance: " + newBalance + " ");
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
        System.out.println("Please input the slot code of your desired snack (i.e. A1, C3):");

        String productChoice = userInput.nextLine().toUpperCase();
        try {
            Snack snack = vendingMachine.purchaseSnack(productChoice);
            System.out.println(snack.getSnackMessage());
            System.out.println("Remaining balance = " + vendingMachine.getBalanceAsMoney());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void finishTransaction() {
        Map<String, Integer> change = vendingMachine.finishTransaction();
        if (change.size() > 0) {
            String changeMessage = "Please take your change -->";
            for (Map.Entry<String, Integer> entry : change.entrySet()) {
                changeMessage += " " + entry.getKey() + ": " + entry.getValue();
            }
            System.out.println(changeMessage);
            continueMenu();
        }
    }

    private void continueMenu() {
        System.out.println();
        System.out.print("Press enter to continue");
        userInput.nextLine();
    }





















}
