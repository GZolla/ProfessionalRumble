package data;



import org.junit.jupiter.api.Test;

import static model.data.Branch.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BranchTest {



    @Test
    //EFFECTS: Test effectiveness changes depending on branchIndex
    public void testGetEffectivenessChangeBranchIndex(){
        assertEquals(2,CORPOR.getEffectiveness(0));
        assertEquals(0.5,CORPOR.getEffectiveness(5));
        assertEquals(1,CORPOR.getEffectiveness(1));

    }

    @Test
    //EFFECTS: Test effectiveness can return the three different values and changes depending on object calling method
    public void testGetEffectivenessChangeCaller(){
        assertEquals(2,TRANSP.getEffectiveness(15));
        assertEquals(0.5,GOVERN.getEffectiveness(2));
        assertEquals( 0,TECHNO.getEffectiveness(9));
    }





}
