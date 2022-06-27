package com.techelevator;

import com.techelevator.snacks.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class VendingMachineTest {
    private VendingMachine vendingMachine;

    @Before
    public void setup() {
        vendingMachine = new VendingMachine();
    }

    @Test
    public void VendingMachine_log_file_exists_after_construction() {
        Assert.assertTrue(new File("Log.txt").exists());
    }

    @Test
    public void StockInventory_puts_A1_PotatoCrisps_3_05_Chip() {
        Snack actual = vendingMachine.getInventory().get("A1");
        Chip expected = new Chip("Potato Crisps", 3.05);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void StockInventory_puts_C1_Cola_1_25_Drink() {
        Snack actual = vendingMachine.getInventory().get("C1");
        Drink expected = new Drink("Cola", 1.25);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void purchaseSnack_returns_insufficient_balance_given_C3_with_1_00_balance() {
        vendingMachine.insertMoney(1);

        String actual = vendingMachine.purchaseSnack("C1");
        String expected = vendingMachine.INSUFFICIENT_FUNDS;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void purchaseSnack_does_not_change_balance_given_C3_with_1_00_balance() {
        vendingMachine.insertMoney(1);
        double expected = vendingMachine.getBalance();
        String message = vendingMachine.purchaseSnack("C1");
        double actual = vendingMachine.getBalance();
        Assert.assertEquals(actual, expected, 0.001);
    }

    @Test
    public void purchaseSnack_does_not_change_stock_given_C3_with_1_00_balance() {
        vendingMachine.insertMoney(1);
        int expected = vendingMachine.getInventory().get("C3").getStockAmount();
        String message = vendingMachine.purchaseSnack("C1");
        int actual = vendingMachine.getInventory().get("C3").getStockAmount();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void purchaseSnack_returns_Chew_Chew_Yum_given_D2_with_1_00_balance() {
        vendingMachine.insertMoney(1);

        String actual = vendingMachine.purchaseSnack("D2");
        String expected = "Chew Chew, Yum!";
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void purchaseSnack_gives_0_05_change_given_D2_with_1_00_balance() {
        vendingMachine.insertMoney(1);
        String message = vendingMachine.purchaseSnack("D2");
        double actual = vendingMachine.getBalance();
        double expected = 0.05;
        Assert.assertEquals(expected, actual, 0.001);
    }

    @Test
    public void purchaseSnack_decrements_stock_given_D2_with_1_00_balance() {
        vendingMachine.insertMoney(10);
        int initialStock = vendingMachine.getInventory().get("C3").getStockAmount();
        String message = vendingMachine.purchaseSnack("C3");
        int actual = vendingMachine.getInventory().get("C3").getStockAmount();
        int expected = initialStock - 1;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void insertMoney_returns_5_00_given_5_00() {
        double actual = vendingMachine.insertMoney(5);
        double expected = 5.0;
        Assert.assertEquals(actual, expected, 0.001);
    }

    @Test
    public void insertMoney_changes_balance_to_5_00_given_5_00() {
        vendingMachine.insertMoney(5);
        double actual = vendingMachine.getBalance();
        double expected = 5.0;
        Assert.assertEquals(actual, expected, 0.001);
    }

    @Test
    public void finishTransaction_returns_3_quarter_1_dime_1_nickel_given_balance_of_0_90() {
        vendingMachine.insertMoney(7);
        vendingMachine.purchaseSnack("A1");
        vendingMachine.purchaseSnack("A1");
        //balance should be 0.90
        String actual = vendingMachine.finishTransaction();
        String expected = "Please take your change: " + "$0.90" + " -----> " + 3 + " quarters   " + " 1 dime   " + " 1 nickel";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void logAction_creates_proper_log_entry_in_log_txt() {
        vendingMachine.insertMoney(10);
        vendingMachine.purchaseSnack("A1");
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
        vendingMachine.insertMoney(10);

        String before = "";
        String actual = " ";


        File salesReportFile = new File(vendingMachine.SALES_REPORT_FILE_PATH);
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

        vendingMachine.purchaseSnack("A1");

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
    public void purchaseSnack_returns_SOLD_OUT_after_buy_all_Cola_C1_() {
        vendingMachine.insertMoney(20);
        vendingMachine.purchaseSnack("C1");
        vendingMachine.purchaseSnack("C1");
        vendingMachine.purchaseSnack("C1");
        vendingMachine.purchaseSnack("C1");
        vendingMachine.purchaseSnack("C1");
        String actual = vendingMachine.purchaseSnack("C1");
        Assert.assertEquals("SOLD OUT", actual);
    }


}
