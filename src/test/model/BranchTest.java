package model;



import org.junit.jupiter.api.Test;

import static model.data.Branch.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BranchTest {

    @Test
    public void testGetEffectivenessChangeBranchIndex(){
        //Test effectiveness changes depending on branchIndex
        assertEquals(2,CORPOR.getEffectiveness(0));
        assertEquals(0.5,CORPOR.getEffectiveness(5));

    }
    @Test
    public void testGetEffectivenessChangeCaller(){
        //Test effectivenes can return the three different values and changes depending on object calling the method
        assertEquals(2,TRANSP.getEffectiveness(15));
        assertEquals(0.5,GOVERN.getEffectiveness(2));
        assertEquals( 0,TECHNO.getEffectiveness(9));

    }


}
