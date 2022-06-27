package com.techelevator.snacksTests;

import com.techelevator.snacks.Drink;
import com.techelevator.snacks.Gum;
import org.junit.Assert;
import org.junit.Test;

public class GumTest {

    @Test
    public void get_snack_message_gum_returns_chew_chew_yum() {
        //arrange
        Gum bazooka = new Gum("", 0.0);

        //act
        String actualMessage = bazooka.getSnackMessage();

        //assert
        Assert.assertEquals("Chew Chew, Yum!", actualMessage);
    }
}
