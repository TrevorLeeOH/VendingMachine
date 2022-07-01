package com.techelevator;

import com.techelevator.Exceptions.InsufficientFundsException;
import com.techelevator.Exceptions.InvalidSlotException;
import com.techelevator.Exceptions.SnackSoldOutException;
import com.techelevator.snacks.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VendingMachineTest {
    private VendingMachine vendingMachine;

    @Before
    public void setup() {
        VendingMachineDao dao = new TextFileVendingMachineDao();
        vendingMachine = new VendingMachine(dao);
    }

    @Test
    public void VendingMachine_log_file_exists_after_construction() {
        Assert.assertTrue(new File("Log.txt").exists());
    }

    @Test
    public void StockInventory_puts_A1_PotatoCrisps_3_05_Chip() {
        Snack actual = vendingMachine.getInventory().get("A1").snackMaster;
        Chip expected = new Chip("Potato Crisps");
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void StockInventory_puts_C1_Cola_1_25_Drink() {
        Snack actual = vendingMachine.getInventory().get("C1").snackMaster;
        Drink expected = new Drink("Cola");
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void purchaseSnack_throws_insufficient_funds_given_C3_with_100_cent_balance() {
        vendingMachine.insertMoney(100);

        try {
            vendingMachine.purchaseSnack("C1");
        } catch (InsufficientFundsException e) {
            return;
        } catch (SnackSoldOutException | InvalidSlotException ignored) {}
        Assert.fail("Failed to throw InsufficientFundsException");
    }

    @Test
    public void purchaseSnack_does_not_change_balance_given_C3_with_100_cent_balance() {
        vendingMachine.insertMoney(100);
        int expected = vendingMachine.getBalance();
        try {
            vendingMachine.purchaseSnack("C1");
        } catch (Exception ignored) {}
        int actual = vendingMachine.getBalance();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void purchaseSnack_does_not_change_stock_given_C3_with_100_cent_balance() {
        vendingMachine.insertMoney(100);
        int expected = vendingMachine.getInventory().get("C3").count();
        try {
            vendingMachine.purchaseSnack("C1");
        } catch (Exception ignored) {}
        int actual = vendingMachine.getInventory().get("C3").count();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void purchaseSnack_returns_Chew_Chew_Yum_given_D2_with_100_cent_balance() {
        vendingMachine.insertMoney(100);
        try {
            String actual = vendingMachine.purchaseSnack("D2").getSnackMessage();
            String expected = "Chew Chew, Yum!";
            Assert.assertEquals(actual, expected);
        } catch (Exception e) {
            Assert.fail("Failed to purchase snack");
        }
    }

    @Test
    public void purchaseSnack_leaves_5_cent_balance_given_D2_with_100_cent_balance() {
        vendingMachine.insertMoney(100);
        try {
            vendingMachine.purchaseSnack("D2");
            int actual = vendingMachine.getBalance();
            int expected = 5;
            Assert.assertEquals(expected, actual);
        } catch (Exception e) {
            Assert.fail("Failed to purchase snack");
        }
    }

    @Test
    public void purchaseSnack_decrements_stock_given_D2_with_1000_cent_balance() {
        vendingMachine.insertMoney(1000);
        int expected = vendingMachine.getInventory().get("C3").count() - 1;
        try {
            vendingMachine.purchaseSnack("C3");
            int actual = vendingMachine.getInventory().get("C3").count();
            Assert.assertEquals(expected, actual);
        } catch (Exception e) {
            Assert.fail("Failed to purchase snack");
        }
    }

    @Test
    public void insertMoney_changes_balance_accurately() {
        vendingMachine.insertMoney(500);
        int actual = vendingMachine.getBalance();
        int expected = 500;
        Assert.assertEquals(expected, actual);

        vendingMachine.insertMoney(1000);
        actual = vendingMachine.getBalance();
        expected = 1500;
        Assert.assertEquals(expected, actual);

        vendingMachine.insertMoney(100);
        actual = vendingMachine.getBalance();
        expected = 1600;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void finishTransaction_returns_3_quarter_1_dime_1_nickel_given_balance_of_90_cents() {
        vendingMachine.insertMoney(700);
        try {
            vendingMachine.purchaseSnack("A1");
            vendingMachine.purchaseSnack("A1");
            Assert.assertEquals(90, vendingMachine.getBalance());
            Map<String, Integer> actual = vendingMachine.finishTransaction();
            Map<String, Integer> expected = new HashMap<>();
            expected.put("quarters", 3);
            expected.put("dimes", 1);
            expected.put("nickels", 1);
            Assert.assertEquals(expected, actual);
        } catch (Exception e) {
            Assert.fail("Failed to purchase snack");
        }
    }

    @Test
    public void logAction_creates_proper_log_entry_in_log_txt() {
        vendingMachine.insertMoney(1000);
        try {
            vendingMachine.purchaseSnack("A1");
        } catch (Exception e) {
            Assert.fail("Failed to purchase snack");
        }
        String actual = "";

        File logFile = new File("Log.txt");
        if (logFile.exists()) {
            try (Scanner reader = new Scanner(logFile)) {
                String line = "";
                while (reader.hasNextLine()) {
                    line = reader.nextLine();
                }
                actual = line;
            } catch (FileNotFoundException e) {
                Assert.fail("Could not find log file to run test!");
            }
        } else {
            Assert.fail("Log file does not exist");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
        String formattedDate = LocalDateTime.now().format(formatter);
        String expected = formattedDate + " Potato Crisps A1 $10.00 $6.95";

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addToSalesReport_modifies_sales_report_file_properly() {
        vendingMachine.insertMoney(1000);

        String before = "";
        String actual = " ";


        File salesReportFile = new File(TextFileVendingMachineDao.SALES_REPORT_FILE_PATH);
        if (salesReportFile.exists()) {
            try (Scanner reader = new Scanner(salesReportFile)) {
                before = reader.nextLine();
            } catch (FileNotFoundException e) {
                Assert.fail("Could not find sales report file to run test!");
            }
        } else {
            Assert.fail("Sales report file does not exist");
        }


        int expectedNewSalesCount = Integer.parseInt(before.split("\\|")[1]) + 1;

        String expected = "Potato Crisps|" + expectedNewSalesCount;
        try {
            vendingMachine.purchaseSnack("A1");
        } catch (Exception e) {
            Assert.fail("Failed to purchase snack");
        }

        if (salesReportFile.exists()) {
            try (Scanner reader = new Scanner(salesReportFile)) {
                actual = reader.nextLine();
            } catch (FileNotFoundException e) {
                Assert.fail("Could not find sales report file to run test!");
            }
        } else {
            Assert.fail("Sales report file does not exist");
        }

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void purchaseSnack_throws_sold_out_exception_after_buying_all_Cola_C1() {
        vendingMachine.insertMoney(2000);
        try {
            vendingMachine.purchaseSnack("C1");
            vendingMachine.purchaseSnack("C1");
            vendingMachine.purchaseSnack("C1");
            vendingMachine.purchaseSnack("C1");
            vendingMachine.purchaseSnack("C1");
            vendingMachine.purchaseSnack("C1");
        } catch (SnackSoldOutException e) {
            return;
        } catch (InvalidSlotException | InsufficientFundsException e) {
            Assert.fail("Failed to purchase snacks");
        }
        Assert.fail("Failed to throw SnackSoldOutException");
    }



}
