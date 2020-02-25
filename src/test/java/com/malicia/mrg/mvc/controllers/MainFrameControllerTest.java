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
    public void testregroupeEleRepHorsBazarbyGroup() {
        try {
            List<GrpPhoto> groupDePhoto = controller.regroupeEleRepHorsBazarbyGroup(Context.getBazar());
            System.out.println(Ressources.listetostring(groupDePhoto));
            assertTrue(true);
        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testgetEleBazar() {
        try {
            java.util.List<ElePhoto> elementsPhoto = controller.getEleBazar(Context.getBazar());
            System.out.println(Ressources.listetostring(elementsPhoto));
            assertTrue(true);
        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testregroupeEleRepNewbyGroup() {
        try {
            java.util.List<GrpPhoto> groupDePhoto = controller.regroupeEleRepNewbyGroup(Context.getKidsModelList());
            System.out.println(Ressources.listetostring(groupDePhoto));
            assertTrue(true);
        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testactionRangerlebazar() {
        Context.setDryRun(true);
        controller.actionRangerlebazar();
        assertTrue(true);
    }
}