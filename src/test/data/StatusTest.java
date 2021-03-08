package data;

import static model.data.NonVolatile.*;
import static model.data.Volatile.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class StatusTest {

    @Test
    public void testIsVolatile() {
        assertTrue(CHARGE.isVolatile());
        assertTrue(CRITIC.isVolatile());
        assertFalse(DEMOR.isVolatile());
        assertFalse(DEPRE.isVolatile());
    }

    @Test
    public void testGetTurnLimit() {
        assertEquals(4,NAUSEA.getTurnLimit());
        assertEquals(1,DRAIND.getTurnLimit());
        assertEquals(0,CRITIC.getTurnLimit());
    }
}
