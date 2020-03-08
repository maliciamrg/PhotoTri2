package com.malicia.mrg.app;

import com.malicia.mrg.app.Context;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ContextTest {

    @Test
    public void testSetup() throws SQLException {
        try {
            Context.setup();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(true);
    }
}