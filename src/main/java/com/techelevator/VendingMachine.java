package com.techelevator;

import java.util.HashMap;
import java.util.Map;
import com.techelevator.Exceptions.*;
import com.techelevator.snacks.*;

public class VendingMachine {

    public final VendingMachineDao dao;
    private final Map<String, SnackSlot> inventory;

    private int balance = 0;

    public VendingMachine(VendingMachineDao dao) {
        this.dao = dao;
        inventory = dao.getInventory();
    }

    public int getBalance() {
        return balance;
    }

    public String getBalanceAsMoney() {
        return intCentsToStringDollars(balance);
    }

    public Map<String, SnackSlot> getInventory() {
        return inventory;
    }

    public Snack purchaseSnack(String slot)
            throws InvalidSlotException, SnackSoldOutException, InsufficientFundsException {
        if (!inventory.containsKey(slot)) {
            throw new InvalidSlotException();
        }
        SnackSlot snackSlot = inventory.get(slot);
        if (snackSlot.isEmpty()) {
            throw new SnackSoldOutException();
        }
        if (balance < snackSlot.price) {
            throw new InsufficientFundsException();
        }

        Snack snack = snackSlot.dequeue();
        int newBalance = balance - snackSlot.price;
        dao.logAction(snack.snackName + " " + slot, balance, newBalance);
        dao.addToSalesReport(snack.snackName);
        balance = newBalance;
        return snack;
    }

    public void insertMoney(int insertedCents) {
        int newBalance = balance + insertedCents;
        dao.logAction("FEED MONEY:", balance, newBalance);
        balance = newBalance;
    }

    public Map<String, Integer> finishTransaction() {
        Map<String, Integer> change = new HashMap<>();
        int changeAmount = balance;

        while (balance >= 25) {
            change.put("quarters", change.containsKey("quarters") ? change.get("quarters") + 1 : 1);
            balance -= 25;
        }
        while (balance >= 10) {
            change.put("dimes", change.containsKey("dimes") ? change.get("dimes") + 1 : 1);
            balance -= 10;
        }
        while (balance >= 5) {
            change.put("nickels", change.containsKey("nickels") ? change.get("nickels") + 1 : 1);
            balance -= 5;
        }

        dao.logAction("GIVE CHANGE:", changeAmount, balance);

        return change;
    }


    public static String intCentsToStringDollars(int cents) {
        int num = cents;
        int val1 = num % 10;
        num /= 10;
        int val2 = num % 10;
        num /= 10;
        return "$" + num + "." + val2 + val1;
    }



}
