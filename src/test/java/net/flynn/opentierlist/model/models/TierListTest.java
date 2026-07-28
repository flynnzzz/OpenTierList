package net.flynn.opentierlist.model.models;

import net.flynn.opentierlist.model.enums.TieredStatus;
import net.flynn.opentierlist.model.exceptions.TierElementNotFoundException;
import net.flynn.opentierlist.model.exceptions.TierNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TierListTest {

    TierList tierList;
    Tier t1, t2, t3, t4;
    TierElement el1, el2, el3, el4, el0, elm1;
    List<TierElement> ut;

    @Before
    public void setUp() throws Exception {

        el1 = new TierElement("elementName1");
        el2 = new TierElement("elementName2");
        el3 = new TierElement("elementName3");
        el4 = new TierElement("elementName4");

        t1 = new Tier("t1");
        t2 = new Tier("t2");
        t3 = new Tier("t3");
        t4 = new Tier("t4");

        ut = new ArrayList<>(List.of(el1, el2, el3, el4));
        var tiers = new ArrayList<>(List.of(t1, t2, t3, t4));
        tierList = new TierList("Tier list", ut, tiers);

    }

    @After
    public void tearDown() {
        el1 = null; el2 = null; el3 = null; el4 = null;
        t1 = null; t2 = null; t3 = null; t4 = null;
        ut.clear(); ut = null;
    }

    @Test
    public void tier() {

        assertFalse(el1.isTiered());
        tierList.tier(el1, t1);
        assertTrue(el1.isTiered());

        assertFalse(el2.isTiered());
        tierList.tier(el2, t1);
        assertTrue(el2.isTiered());

        assertFalse(el3.isTiered());
        tierList.tier(el3, t3);
        assertTrue(el3.isTiered());

        assertThrows(IllegalArgumentException.class, () -> tierList.tier(el1, t1)) ;
        assertThrows(IllegalArgumentException.class, () -> tierList.tier(el2, t1)) ;

        tierList.tier(el4, Tier.UNTIERED);
        assertEquals(TieredStatus.UNTIERED, el4.getStatus());

        assertThrows(TierElementNotFoundException.class, () -> tierList.tier(new TierElement(), t1)) ;
        assertThrows(TierNotFoundException.class, () -> tierList.tier(el4, new Tier())) ;

    }

    @Test
    public void tierInsert() {
    }

    @Test
    public void testTierInsert() {
    }

    @Test
    public void unTier() {
    }

    @Test
    public void unTierInsert() {
    }

    @Test
    public void testUnTierInsert() {
    }

    @Test
    public void addTier() {
    }

    @Test
    public void addElement() {
    }

    @Test
    public void addAllElements() {
    }

    @Test
    public void testAddElement() {
    }

    @Test
    public void testAddElement1() {
    }

    @Test
    public void removeTier() {
    }

    @Test
    public void testRemoveTier() {
    }

    @Test
    public void removeElement() {
    }

    @Test
    public void removeAllElements() {
    }

    @Test
    public void swapTiers() {
    }

    @Test
    public void testSwapTiers() {
    }

    @Test
    public void swapElements() {
    }

    @Test
    public void indexOf() {
    }

    @Test
    public void testIndexOf() {
    }

    @Test
    public void tiersQuantity() {
    }

    @Test
    public void contains() {
    }

    @Test
    public void testContains() {
    }

    @Test
    public void moveTier() {
    }

    @Test
    public void testMoveTier() {
    }

    @Test
    public void moveElement() {
    }

    @Test
    public void testMoveElement() {
    }

    @Test
    public void testMoveElement1() {
    }

    @Test
    public void setTierName() {
    }

    @Test
    public void setTierColor() {
    }

    @Test
    public void getTierName() {
    }

    @Test
    public void getTierColor() {
    }

    @Test
    public void getUnTiered() {
    }

    @Test
    public void getTiers() {
    }

    @Test
    public void testEquals() {
    }
}