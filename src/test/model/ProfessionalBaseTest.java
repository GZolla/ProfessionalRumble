package model;

import model.data.ProfessionalBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfessionalBaseTest {
    @Test
    public void testGetAllNamesLength(){
        assertEquals(40, ProfessionalBase.getAllNames().length);
    }
    @Test
    public void testGetAllNamesOrder(){
        assertEquals("CEO",ProfessionalBase.getAllNames()[0]);
        assertEquals("Poker player",ProfessionalBase.getAllNames()[26]);//Tests two branches constructor
        assertEquals("Environmentalist",ProfessionalBase.getAllNames()[39]);
    }
    @Test
    public void testGetByIndex(){
        assertEquals(ProfessionalBase.COMPUTER_SCIENTIST,ProfessionalBase.getByIndex(38));
        assertEquals(ProfessionalBase.PROFESSOR,ProfessionalBase.getByIndex(6));
    }
}
