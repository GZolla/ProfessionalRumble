import model.data.Branch;
import model.data.NonVolatile;
import model.data.ProfessionalBase;
import model.effects.CriticalModifier;
import model.effects.StatusInflictor;
import model.moves.Damaging;
import model.moves.Move;
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
        assertArrayEquals(new String[]{
                MANUAL.getName(),
                "1", "1", "0.5", "1", "1", "1", "1", "2", "2", "1", "0.5", "1", "1", "0.5", "2", "1"
        },row1);

        String[] row2 = TECHNO.toRow();
        assertArrayEquals(new String[]{
                TECHNO.getName(),
                "0.5", "0.5", "1", "2", "1", "1", "2", "1", "2", "0", "1", "2", "1", "1", "1", "1"
        },row2);

        String[] row3 = UNEMP.toRow();
        assertEquals(2, row3.length);
        assertEquals(UNEMP.getName(),row3[0]);
        assertEquals(UNEMP.getDescription(),row3[1]);

        String[] ceo = new String[]{"CEO",CORPOR.getName(),"","70","70","85","110","100","100"};
        assertArrayEquals(ceo,CEO.toRow());


        String[] ent = new String[]{"Comedian",ENTERT.getName(),LETTER.getName(),"100","50","80","105","105","100"};
        assertArrayEquals(ent,COMEDIAN.toRow());
    }

    @Test
    public void testToRowMoves() {
        assertArrayEquals(new String[]{"Dominate", SPORT.name(), "100", "Special", "-"},DOMINATE.toRow());//null
        //null but physical:
        assertArrayEquals(new String[]{"Keyboard slam",TECHNO.getName(),"100","Physical", "-"},KEYBOARD_SLAM.toRow());
        //Different power + effect
        String effDesc = new StatusInflictor(UNEMP,2).getDescription();
        assertArrayEquals(new String[]{"Malware",TECHNO.getName(),"80","Special", effDesc},MALWARE.toRow());
        //Status
        assertArrayEquals(new String[]{
                "Shame",ENVIRO.getName(),"-","Status", new CriticalModifier(-MAXCRITS).getDescription()
        },SHAME.toRow());
    }

    @Test
    public void testEffectDescriptions() {
        //CriticalModifier
        assertEquals("User's critical points are reduced by 8 points.", GMO_GUT_PUNCH.getEffect().getDescription());
        assertEquals("User's critical points are increased by 24 points.",TEST_CRASH.getEffect().getDescription());
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

        assertEquals("Attacker \\ Defender",headers[0]);
        for (int i = 0; i < branches.length; i++) {
            assertEquals(branches[i].name().substring(0,3), headers[i + 1]);
        }

        String[] headersNV = UNEMP.getHeaders();
        assertArrayEquals(new String[]{"Name","Description"},headersNV);

        String[] headersPB = CENTRAL_BANK_CHAIR.getHeaders();
        assertArrayEquals(new String[] {
                "Name","Branches","","Life","Strength","Resistance","Special Strength","Special Resistance","Speed"
        },headersPB);

        String[] headersDMG = ICBM_STRIKE.getHeaders();
        assertArrayEquals(new String[] {
                "Name","Branch","Power","Type","Added Effect"
        },headersDMG);

        String[] headersSTS = HYPEREXAMINE.getHeaders();
        assertArrayEquals(new String[] {
                "Name","Branch","Power","Type","Added Effect"
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
        testBothStatics(Move.listAllMoves());

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
                assertArrayEquals(headers,headers2);
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
