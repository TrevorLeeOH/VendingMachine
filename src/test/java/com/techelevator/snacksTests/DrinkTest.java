package com.techelevator.snacksTests;

import com.techelevator.snacks.Chip;
import com.techelevator.snacks.Drink;
import org.junit.Assert;
import org.junit.Test;

public class DrinkTest {

    @Test
    public void get_snack_message_drink_returns_glug_glug_yum() {
        //arrange
        Drink sevenUp = new Drink("", 0.0);

        //act
        String actualMessage = sevenUp.getSnackMessage();

        //assert
        Assert.assertEquals("Glug Glug, Yum!", actualMessage);
    }
}
