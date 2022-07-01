package com.techelevator.snacks;

import java.text.NumberFormat;
import java.util.Objects;

public abstract class Snack {
    public final String snackName;

    public Snack(String snackName) {
        this.snackName = snackName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Snack)) return false;
        Snack snack = (Snack) o;
        return snackName.equals(snack.snackName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snackName);
    }

    public abstract String getSnackMessage();

    public abstract Snack duplicate();

}
