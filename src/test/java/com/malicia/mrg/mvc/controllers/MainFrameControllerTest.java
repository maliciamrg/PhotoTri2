package com.malicia.mrg.mvc.controllers;

import com.malicia.mrg.app.Context;
import com.malicia.mrg.app.Ressources;
import com.malicia.mrg.photo.ElePhoto;
import com.malicia.mrg.photo.GrpPhoto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class MainFrameControllerTest {

    private MainFrameController controller;

    @BeforeMethod
    public void setUp() {
        System.out.println("BeforeMethod:setUp");
        Context.setup();
        controller = new MainFrameController();
        Context.setDryRun(false);
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