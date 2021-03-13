import model.data.Branch;
import model.data.NonVolatile;
import model.data.ProfessionalBase;
import model.effects.CriticalModifier;
import model.effects.StatusInflictor;
import model.moves.Damaging;
import model.moves.NonDamaging;
import org.junit.jupiter.api.Test;
import ui.TableAble;

import static model.data.Branch.*;
import static model.data.NonVolatile.*;
import static model.data.ProfessionalBase.*;
import static model.effects.CriticalModifier.MAXCRITS;
import static model.moves.Damaging.*;
import static model.moves.NonDamaging.HYPEREXAMINE;
import static model.moves.NonDamaging.SHAME;
import static org.junit.jupiter.api.Assertions.*;

//Test the methods of TestAbleTest on the classes that implement it
public class TableAbleTest {
    @Test
    public void testGetIndex() {
        assertEquals(0,CORPOR.getIndex());
        assertEquals(4,SCIENCE.getIndex());
        assertEquals(8,NUMBER.getIndex());
        assertEquals(15,ENVIRO.getIndex());

        assertEquals(0,UNEMP.getIndex());
        assertEquals(5,FAINT.getIndex());

        assertEquals(12,LAWYER.getIndex());
        assertEquals(21,CHICAGO_ECON.getIndex());
    }

    @Test
    //EFFECTS: Test correct length and content of toRow for classes in src.model.data
    public void  testToRow() {
        String[] row1 = MANUAL.toRow();
        assertEquals(Branch.values().length + 1,row1.length);
        assertEquals(MANUAL.getName(),row1[0]);
        for (int i = 0; i < row1.length - 1; i++) {
            assertEquals(MANUAL.getEffectiveness(i) + "",row1[i + 1]);
        }

        String[] row2 = TECHNO.toRow();
        assertEquals(Branch.values().length + 1,row2.length);
        assertEquals(TECHNO.getName(),row2[0]);
        for (int i = 0; i < row2.length - 1; i++) {
            assertEquals(TECHNO.getEffectiveness(i) + "",row2[i + 1]);
        }

        String[] row3 = UNEMP.toRow();
        assertEquals(2, row3.length);
        assertEquals(UNEMP.getName(),row3[0]);
        assertEquals(UNEMP.getDescription(),row3[1]);

        String[] ceo = new String[]{"0","CEO",CORPOR.getName(),"70","70","85","110","100","100"};
        for (int i = 0; i < ceo.length; i++) {
            String[] row = CEO.toRow();
            assertEquals(ceo[i],row[i]);
        }


        String[] cs = new String[]{"38","Computer scientist",TECHNO.getName(),"100","50","60","110","140","75"};
        for (int i = 0; i < cs.length; i++) {
            String[] row = COMPUTER_SCIENTIST.toRow();
            assertEquals(cs[i],row[i]);
        }
    }

    @Test
    public void testToRowMoves() {
        areEqual(new String[]{"0", "Dominate", SPORT.name(), "100", "Special", "-"},DOMINATE.toRow());//null
        //null but physical:
        areEqual(new String[]{"2","Keyboard slam",TECHNO.getName(),"100","Physical", "-"},KEYBOARD_SLAM.toRow());
        //Different power + effect
        String effDesc = new StatusInflictor(UNEMP,2).getDescription();
        areEqual(new String[]{"89","Malware",TECHNO.getName(),"80","Special", effDesc},MALWARE.toRow());
        //Status
        areEqual(new String[]{
                "1","Shame",ENVIRO.getName(), new CriticalModifier(-MAXCRITS).getDescription()
        },SHAME.toRow());
    }

    @Test
    public void testEffectDescriptions() {
        //CriticalModifier
        assertEquals("User's critical points are reduced by 2 points.", GMO_GUT_PUNCH.getEffect().getDescription());
        assertEquals("User's critical points are increased by 6 points.",TEST_CRASH.getEffect().getDescription());
        assertEquals("Opponent's critical points are reduced by 32 points.",SHAME.getEffect().getDescription());
        assertEquals(
                "If the foe is not defeated after this move, user takes 2/3 of the damage caused to foe as recoil.",
                KIDNAP.getEffect().getDescription()
        );
        assertEquals(
                "If the foe is not defeated after this move, user becomes unemployed.",
                PREDATORY_PRICING.getEffect().getDescription()
        );


        assertEquals("Move needs charging before use.",LONG_AWAITED_NOVEL .getEffect().getDescription());
        assertEquals("Professional digs in before attacking.",UNDERGROUND_UPPERCUT.getEffect().getDescription());
        assertEquals(
                "Move fails if foe doesn't attack or if it moves first.",
                INSIDER_TRADING.getEffect().getDescription()
        );
        assertEquals("Move fails if foe doesn't attack.",LANDMINE.getEffect().getDescription());
        assertEquals("Move fails if user is damaged before attacking.",HARVEST_HAVOC.getEffect().getDescription());
    }


    @Test
    //EFFECTS: Test correct length and content for getHeaders
    public void testGetHeaders() {
        Branch[] branches = Branch.values();
        String[] headers = CORPOR.getHeaders();

        assertEquals(Branch.values().length + 1,headers.length);

        assertEquals("V Attacker \\ Defender >",headers[0]);
        for (int i = 0; i < branches.length; i++) {
            assertEquals(branches[i].name(), headers[i + 1]);
        }

        String[] headersNV = UNEMP.getHeaders();
        areEqual(new String[]{"Name","Description"},headersNV);

        String[] headersPB = CENTRAL_BANK_CHAIR.getHeaders();
        areEqual(new String[] {
                "ID","Name","Branch","Life","Strength","Resistance","Special Strength","Special Resistance","Speed"
        },headersPB);

        String[] headersDMG = ICBM_STRIKE.getHeaders();
        areEqual(new String[] {
                "ID","Name","Branch","Power","Type","Added Effect"
        },headersDMG);

        String[] headersSTS = HYPEREXAMINE.getHeaders();
        areEqual(new String[] {
                "ID","Name","Branch","Added Effect"
        },headersSTS);


    }

    @Test
    //EFFECTS: Test getHeaders and getValues is the same regardless of caller
    public void testGetHeadersStaticBehaviour() {

        //Branches
        testBothStatics(Branch.values());


        //NonVolatile
        testBothStatics(NonVolatile.values());

        //Professional Base
        testBothStatics(ProfessionalBase.values());

        //Moves
        testBothStatics(Damaging.values());
        testBothStatics(NonDamaging.values());

    }

    //EFFECTS: checks that both given String[] are equal
    public static void areEqual(String[] a, String[] b) {
        if (a.length != b.length) {
            fail();
        }
        for (int i = 0; i < a.length; i++) {
            assertEquals(a[i],b[i]);
        }
    }

    //EFFECTS: calls both methods that check static behaviour
    public void testBothStatics(TableAble[] values) {
        testStaticHeaders(values);
        testStaticValues(values);

    }


    public void testStaticHeaders(TableAble[] values) {
        for (int i = 0; i < values.length; i++) {
            String[] headers = values[i].getHeaders();
            for (int j = 0; j < values.length; j++) {
                String[] headers2 = values[j].getHeaders();
                areEqual(headers,headers2);
            }
        }
    }

    public void testStaticValues(TableAble[] values) {
        for (int i = 0; i < values.length; i++) {
            TableAble[] testedValues = values[i].getValues();
            for (int j = 0; j < testedValues.length; j++) {
                assertEquals(values[j], testedValues[j]);
            }
        }
    }

}
