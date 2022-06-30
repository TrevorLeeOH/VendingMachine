package com.techelevator;

import com.techelevator.snacks.Snack;

import java.util.List;
import java.util.Map;

public interface VendingMachineDao {


    public Map<String, SnackSlot> getInventory();

    public List<String> getSalesReport();

    public void logAction(String type, int beforeBalance, int afterBalance);

    public void addToSalesReport(String productName);


}
