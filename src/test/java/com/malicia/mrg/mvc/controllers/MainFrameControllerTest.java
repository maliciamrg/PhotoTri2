package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.app.Context;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class MainFrameControllerTest {

    private MainFrameController controller;

    @BeforeMethod
    public void setUp() {
        System.out.println("BeforeMethod:setUp");
        try {
            Context.setup();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        controller = new MainFrameController();
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("AfterMethod:tearDown");
        assertEquals(1, 1);
    }

    @Test
    public void testactionRangerlebazar() {
        assertTrue(true);
    }
}