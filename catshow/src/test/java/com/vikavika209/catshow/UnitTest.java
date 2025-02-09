package com.vikavika209.catshow;

import com.vikavika209.catshow.model.Breed;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.model.Show;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnitTest {

    @Test
    public void createNewOwnerTest(){
        Owner owner = new Owner("test@test.com", "123456", "Test Name", "Test City");

        Assertions.assertEquals("test@test.com", owner.getUsername());
        Assertions.assertEquals("123456", owner.getPassword());
        Assertions.assertEquals("Test Name", owner.getName());
        Assertions.assertEquals("Test City", owner.getCity());
    }

    @Test
    public void createNewCatTest(){
        Owner owner = new Owner("test@test.com", "123456", "Test Name", "Test City");
        Cat cat = new Cat("Barsik", owner, 0, Breed.MAINECOON);

        Assertions.assertEquals("Barsik", cat.getName());
        Assertions.assertEquals("Test Name", cat.getOwner().getName());
        Assertions.assertEquals("Мейкун", cat.getBreed().getDisplayName());
    }

    @Test
    public void createNewShowTest() throws ParseException {

        String inputDate = "30-01-2025";

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        Date date = formatter.parse(inputDate);

        Show show = new Show("Test City", date, "Test address");

        Assertions.assertEquals("Thu Jan 30 00:00:00 QYZT 2025", show.getDate().toString());
        Assertions.assertEquals("Test City", show.getCity());
        Assertions.assertEquals("Test address", show.getAddress());
    }
}
