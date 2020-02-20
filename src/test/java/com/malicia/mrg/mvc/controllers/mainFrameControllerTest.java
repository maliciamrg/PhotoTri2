package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.app.Context;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class mainFrameControllerTest {

    private mainFrameController controller;

    @BeforeMethod
    public void setUp() {
        System.out.println("BeforeMethod:setUp");
        controller = new mainFrameController();
        Context.setup();
        Context.setDryRun(false);
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("AfterMethod:tearDown");
        assertEquals(1, 1);
    }

    @Test
    public void testComposeRelativeRep() {
        String expected = controller.composeRelativeRep("D:/50_Phototheque/", "D:/50_Phototheque/!!Legacy/0000_misc/");
        String actual = "!!Legacy/0000_misc/";
        assertEquals(actual,expected);
    }

    @Test
    public void testRenommerUnRepertoire() {
        try {
            controller.renommerUnRepertoire("D:/50_Phototheque/!!Legacy/0000_misc/", "D:/50_Phototheque/!!Legacy/0000_misc/" , "45","D:/50_Phototheque/");
            assertTrue(true);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getStackTrace());
            assertTrue(false);
        }
    }

//    @Test
//    public void testDeleteEmptyDirectory() {
//        assertTrue(controller.deleteEmptyDirectory()>0);
//    }

}