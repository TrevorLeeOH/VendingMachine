package com.techelevator.snacks;

import java.text.NumberFormat;
import java.util.Objects;

public abstract class Snack {
    private String snackName;
    private double snackPrice;
    private int stockAmount = 5;

    public Snack(String snackName, double snackPrice) {
        this.snackName = snackName;
        this.snackPrice = snackPrice;
    }

    public String getSnackName() {
        return snackName;
    }

    public void setSnackName(String snackName) {
        this.snackName = snackName;
    }

    public double getSnackPrice() {
        return snackPrice;
    }

    public void setSnackPrice(double snackPrice) {
        this.snackPrice = snackPrice;
    }

    @Override
    public String toString() {
        String stockAmountMessage = stockAmount > 0 ? stockAmount + " remaining" : "SOLD OUT";
        return snackName + " | " + NumberFormat.getCurrencyInstance().format(snackPrice) + " | " + stockAmountMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Snack)) return false;
        Snack snack = (Snack) o;
        return Double.compare(snack.snackPrice, snackPrice) == 0 && snackName.equals(snack.snackName) && this.stockAmount == snack.stockAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(snackName, snackPrice, stockAmount);
    }

    public abstract String getSnackMessage();

    public int getStockAmount() {
        return stockAmount;
    }
    public void decrementStock() {
        this.stockAmount = stockAmount -1;
    }



}
