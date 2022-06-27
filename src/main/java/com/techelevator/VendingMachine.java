package com.techelevator;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import com.techelevator.snacks.*;

public class VendingMachine {

    private final String INVENTORY_FILE_PATH = "vendingmachine.csv";
    private final String LOG_FILE_PATH = "Log.txt";
    public final String SALES_REPORT_FILE_PATH = "SalesReport.txt";
    public final String INSUFFICIENT_FUNDS = "Insufficient balance; please insert more funds";

    private Map<String, Snack> inventory = new HashMap<>();

    private double balance = 0;

    public VendingMachine() {
        initializeLog();
        initializeSalesReport();
        stockInventory();
    }

    public double getBalance() {
        return balance;
    }

    public Map<String, Snack> getInventory() {
        return inventory;
    }
    private void initializeSalesReport() {
        File salesReport = new File(SALES_REPORT_FILE_PATH);
        if (!salesReport.exists()) {
            try {
                salesReport.createNewFile();
                try (PrintWriter writer = new PrintWriter(salesReport);
                    Scanner reader = new Scanner(new File(INVENTORY_FILE_PATH))
                ) {
                  while (reader.hasNextLine()) {
                      String[] snackEntryLineArray = reader.nextLine().split("\\|");
                      writer.println(snackEntryLineArray[1] + "|0");
                  }
                }
            } catch (IOException e) {
                System.out.println("Failed to initialize sales report!");
            }
        }

    }

    private void initializeLog() {
        File log = new File(LOG_FILE_PATH);
        if (!log.exists()) {
            try {
                log.createNewFile();
            } catch (IOException e) {
                System.out.println("Failed to initialize log!");
            }
        }
    }

    private void stockInventory() {
        File inventoryFile = new File(INVENTORY_FILE_PATH);
        try (Scanner scanner = new Scanner(inventoryFile)) {
            while (scanner.hasNextLine()) {
                String[] inventoryEntry = scanner.nextLine().split("\\|");
                String slot = inventoryEntry[0];
                String name = inventoryEntry[1];
                double price = Double.parseDouble(inventoryEntry[2]);
                String type = inventoryEntry[3];

                Snack snack;
                switch (type) {
                    case "Chip":
                        snack = new Chip(name, price);
                        break;
                    case "Candy":
                        snack = new Candy(name, price);
                        break;
                    case "Drink":
                        snack = new Drink(name, price);
                        break;
                    case "Gum":
                        snack = new Gum(name, price);
                        break;
                    default:
                        snack = null;
                }
                inventory.put(slot, snack);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Failed to stock Vending Machine!");
        }
    }

    public String purchaseSnack(String slot) {
        Snack snackToPurchase = inventory.get(slot);

        if (snackToPurchase.getStockAmount() < 1) {
            return "SOLD OUT";
        } else if (balance < snackToPurchase.getSnackPrice()) {
            return INSUFFICIENT_FUNDS;
        } else {
            double newBalance = balance - snackToPurchase.getSnackPrice();
            logAction(snackToPurchase.getSnackName() + " " + slot, balance, newBalance);
            addToSalesReport(snackToPurchase.getSnackName());
            snackToPurchase.decrementStock();
            balance = newBalance;
            return snackToPurchase.getSnackMessage();
        }
    }

    public double insertMoney(int dollarAmount) {
        double newBalance = balance + dollarAmount;
        logAction("FEED MONEY:", balance, newBalance);
        balance = newBalance;
        return balance;
    }

    public String finishTransaction() {

        double changeAmount = balance;
        int quarterCount = 0;
        int dimeCount = 0;
        int nickelCount = 0;
        String quartersMessage = "";
        String dimesMessage = "";
        String nickelsMessage = "";

        while (balance >= .25) {
            quarterCount++;
            balance -= 0.25;
        }
        while (balance >= 0.10) {
            dimeCount++;
            balance -= 0.10;
        }
        while (balance >= 0.05) {
            nickelCount++;
            balance -= 0.05;
        }
        if (quarterCount > 0) {
            quartersMessage = quarterCount == 1 ? " 1 quarter   " : quarterCount + " quarters   ";
        }
        if (dimeCount > 0) {
            dimesMessage = dimeCount == 1 ? " 1 dime   " : dimeCount + " dimes   ";
        }
        if (nickelCount > 0) {
            nickelsMessage = nickelCount == 1 ? " 1 nickel" : nickelCount + "nickels";
        }
        logAction("GIVE CHANGE:", changeAmount, balance);

        return "Please take your change: " + NumberFormat.getCurrencyInstance().format(changeAmount) + " -----> " + quartersMessage + dimesMessage + nickelsMessage;
    }

    public void logAction(String type, double beforeBalance, double afterBalance) {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(LOG_FILE_PATH, true))) {
            String formattedBeforeBalance = NumberFormat.getCurrencyInstance().format(beforeBalance);
            String formattedAfterBalance = NumberFormat.getCurrencyInstance().format(afterBalance);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
            String formattedDate = LocalDateTime.now().format(formatter);
            String logMessage = formattedDate + " " + type + " " + formattedBeforeBalance + " " + formattedAfterBalance;
            writer.println(logMessage);
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + LOG_FILE_PATH + " not found!");
        }
    }

    public void addToSalesReport(String productName) {
        File tempSalesReport = new File("SalesReportTempFile.txt");
        File salesReport = new File(SALES_REPORT_FILE_PATH);
        try {
            tempSalesReport.createNewFile();
        } catch (IOException e) {
            System.out.println("Create Temp File failed.");
        }
        try (PrintWriter writer = new PrintWriter(tempSalesReport);
            Scanner fileReader = new Scanner(salesReport)
        ) {
            while (fileReader.hasNextLine()) {
                String soldProductLine = fileReader.nextLine();
                String[] soldProductLineArray = soldProductLine.split("\\|");
                if (soldProductLineArray[0].equals(productName)) {
                    try {
                        int cumulativeSales = Integer.parseInt(soldProductLineArray[1]) + 1;
                        soldProductLineArray[1] = String.valueOf(cumulativeSales);
                    } catch (NumberFormatException e) {
                        System.out.println("Where's my number??");
                    }
                }
                soldProductLine = String.join("|", soldProductLineArray);
                writer.println(soldProductLine);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: " + SALES_REPORT_FILE_PATH + " not found!");
        } catch (SecurityException e) {
            System.out.println("Security Error");
        }
        try {
            salesReport.delete();
            tempSalesReport.renameTo(new File(SALES_REPORT_FILE_PATH));
        } catch (Exception e) {
            System.out.println("Failed to replace sales report with updated report.");
        }
    }
}
