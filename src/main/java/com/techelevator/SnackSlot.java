package com.techelevator;

import com.techelevator.snacks.Snack;

import java.math.BigDecimal;

public class SnackSlot {

    private static final int CAPACITY = 5;

    public final Snack snackMaster;
    public final int price; //price in cents
    private SnackQueueNode head;
    private SnackQueueNode tail;

    public SnackSlot(Snack snackMaster, int price) {
        this.snackMaster = snackMaster;
        this.price = price;
        restock();
    }

    public void restock() {
        int snacksToAdd = CAPACITY - count();
        for (int i = 0; i < snacksToAdd; i++) {
            enqueue(snackMaster.duplicate());
        }
    }

    private void enqueue(Snack snack) {
        SnackQueueNode newNode = new SnackQueueNode(snack);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.nextInLine = newNode;
            tail = tail.nextInLine;
        }
    }

    public Snack dequeue() {
        if (isEmpty()) {
            return null;
        }
        Snack snackToReturn = head.value;
        head = head.nextInLine;
        return snackToReturn;
    }

    public int count() {
        int count = 0;
        SnackQueueNode pointer = head;
        while (pointer != null) {
            count++;
            pointer = pointer.nextInLine;
        }
        return count;
    }

    public boolean isEmpty() {
        return head == null;
    }

    private class SnackQueueNode {

        public Snack value;
        public SnackQueueNode nextInLine;

        public SnackQueueNode(Snack value) {
            this.value = value;
        }

    }

}
