package com.techelevator;

import com.techelevator.snacks.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TextFileVendingMachineDao implements VendingMachineDao {

    public static final String INVENTORY_FILE_PATH = "vendingmachine.csv";
    public static final String LOG_FILE_PATH = "Log.txt";
    public static final String SALES_REPORT_FILE_PATH = "SalesReport.txt";


    public TextFileVendingMachineDao() {
        initializeLog();
        initializeSalesReport();
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

    @Override
    public Map<String, SnackSlot> getInventory() {
        File inventoryFile = new File(INVENTORY_FILE_PATH);
        Map<String, SnackSlot> inventory = new HashMap<>();

        try (Scanner scanner = new Scanner(inventoryFile)) {
            while (scanner.hasNextLine()) {
                String[] inventoryEntry = scanner.nextLine().split("\\|");
                String slot = inventoryEntry[0];
                String name = inventoryEntry[1];
                int price = doublePriceToIntPrice(Double.parseDouble(inventoryEntry[2]));

                String type = inventoryEntry[3];

                Snack snack;
                switch (type) {
                    case "Chip":
                        snack = new Chip(name);
                        break;
                    case "Candy":
                        snack = new Candy(name);
                        break;
                    case "Drink":
                        snack = new Drink(name);
                        break;
                    case "Gum":
                        snack = new Gum(name);
                        break;
                    default:
                        snack = null;
                }
                inventory.put(slot, new SnackSlot(snack, price));
            }
            return inventory;
        } catch (FileNotFoundException e) {
            System.out.println("Failed to stock Vending Machine!");
            return null;
        }
    }

    private int doublePriceToIntPrice(double doublePrice) {
        doublePrice *= 100;
        return (int) doublePrice;
    }

    @Override
    public List<String> getSalesReport() {
        List<String> salesReport = new ArrayList<>();
        System.out.println();
        try (Scanner reader = new Scanner(new File(SALES_REPORT_FILE_PATH))) {
            while (reader.hasNextLine()) {
                salesReport.add(reader.nextLine());
            }

        } catch (FileNotFoundException e) {
            System.out.println("Sales Report File not found--sorry!");
        }
        return salesReport;
    }

    @Override
    public void logAction(String type, int beforeBalance, int afterBalance) {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(LOG_FILE_PATH, true))) {
            String formattedBeforeBalance = VendingMachine.intCentsToStringDollars(beforeBalance);
            String formattedAfterBalance = VendingMachine.intCentsToStringDollars(afterBalance);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
            String formattedDate = LocalDateTime.now().format(formatter);
            String logMessage = formattedDate + " " + type + " " + formattedBeforeBalance + " " + formattedAfterBalance;
            writer.println(logMessage);
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + LOG_FILE_PATH + " not found!");
        }
    }

    @Override
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
