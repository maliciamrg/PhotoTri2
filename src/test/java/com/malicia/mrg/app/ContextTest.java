package com.malicia.mrg.app;

import com.malicia.mrg.app.Context;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ContextTest {

    @Test
    public void testSetup() {
        Context.setup();
        assertNotNull(Context.getAbsolutePathFirst());
    }
}