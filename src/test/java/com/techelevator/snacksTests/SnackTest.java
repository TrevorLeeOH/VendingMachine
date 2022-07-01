package com.techelevator.snacksTests;

import com.techelevator.snacks.Candy;
import com.techelevator.snacks.Chip;
import com.techelevator.snacks.Drink;
import com.techelevator.snacks.Gum;
import org.junit.Assert;
import org.junit.Test;

public class SnackTest {

    @Test
    public void get_snack_message_candy_returns_munch_munch_yum() {
        //arrange
        Candy snickers = new Candy("");

        //act
        String actualMessage = snickers.getSnackMessage();

        //assert
        Assert.assertEquals("Munch Munch, Yum!", actualMessage);
    }

    @Test
    public void get_snack_message_chip_returns_munch_munch_yum() {
        //arrange
        Chip lays = new Chip("");

        //act
        String actualMessage = lays.getSnackMessage();

        //assert
        Assert.assertEquals("Crunch Crunch, Yum!", actualMessage);
    }

    @Test
    public void get_snack_message_drink_returns_glug_glug_yum() {
        //arrange
        Drink sevenUp = new Drink("");

        //act
        String actualMessage = sevenUp.getSnackMessage();

        //assert
        Assert.assertEquals("Glug Glug, Yum!", actualMessage);
    }
    @Test
    public void get_snack_message_gum_returns_chew_chew_yum() {
        //arrange
        Gum bazooka = new Gum("");

        //act
        String actualMessage = bazooka.getSnackMessage();

        //assert
        Assert.assertEquals("Chew Chew, Yum!", actualMessage);
    }

}
