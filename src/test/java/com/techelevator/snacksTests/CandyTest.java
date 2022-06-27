package com.techelevator.snacksTests;

import com.techelevator.snacks.Candy;
import org.junit.Assert;
import org.junit.Test;

public class CandyTest {
    @Test
    public void get_snack_message_candy_returns_munch_munch_yum() {
        //arrange
        Candy snickers = new Candy("", 0.0);

        //act
        String actualMessage = snickers.getSnackMessage();

        //assert
        Assert.assertEquals("Munch Munch, Yum!", actualMessage);
    }


}
