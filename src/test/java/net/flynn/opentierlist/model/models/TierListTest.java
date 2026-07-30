package net.flynn.opentierlist.model.models;

import net.flynn.opentierlist.model.enums.TieredStatus;
import net.flynn.opentierlist.model.exceptions.TierElementNotFoundException;
import net.flynn.opentierlist.model.exceptions.TierNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class TierListTest {

    TierList tierList;
    Tier t1, t2, t3, t4;
    TierElement el1, el2, el3, el4;
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

        // after tiering an element its status should be changed to 'TIERED'
        assertFalse(el1.isTiered());
        tierList.tier(el1, t1);
        assertTrue(el1.isTiered());
        assertEquals(List.of(el1), t1.getTiered());
        assertEquals(List.of(el2, el3, el4), tierList.getUnTiered());

        assertFalse(el2.isTiered());
        tierList.tier(el2, t1);
        assertTrue(el2.isTiered());
        assertEquals(List.of(el1, el2), t1.getTiered());
        assertEquals(List.of(el3, el4), tierList.getUnTiered());

        assertFalse(el3.isTiered());
        tierList.tier(el3, t3);
        assertTrue(el3.isTiered());
        assertEquals(List.of(el3), t3.getTiered());
        assertEquals(List.of(el4), tierList.getUnTiered());

        assertThrows(IllegalArgumentException.class, () -> tierList.tier(el1, t1)) ;
        assertThrows(IllegalArgumentException.class, () -> tierList.tier(el2, t1)) ;

        tierList.tier(el4, Tier.UNTIERED);
        assertEquals(TieredStatus.UNTIERED, el4.getStatus());

        assertThrows(TierElementNotFoundException.class, () -> tierList.tier(new TierElement(), t1)) ;
        assertThrows(TierNotFoundException.class, () -> tierList.tier(el4, new Tier())) ;

    }

    @Test
    public void tierInsertPointer() {

        assertThrows(TierElementNotFoundException.class, () -> tierList.tierInsert(el1, t1, el1));

        tierList.removeElement(el1);
        tierList.removeElement(el2);

        tierList.addElement(el1, t1);
        tierList.addElement(el2, t1);
        el1.changeTo(TieredStatus.TIERED);
        el2.changeTo(TieredStatus.TIERED);

        // t1: el1, el2
        assertEquals(List.of(el1, el2), t1.getTiered());
        assertEquals(List.of(el3, el4), tierList.getUnTiered());

        tierList.tierInsert(el3, t1, el1);
        // t1: el3, el1, el2
        assertEquals(List.of(el3, el1, el2), t1.getTiered());
        assertEquals(List.of(el4), tierList.getUnTiered());

        assertThrows(TierNotFoundException.class, () -> tierList.tierInsert(el4, new Tier(), el1));

        tierList.tierInsert(el4, t1, el1);
        // t1: el3, el4, el1, el2
        assertEquals(List.of(el3, el4, el1, el2), t1.getTiered());
        assertEquals(List.of(), tierList.getUnTiered());

        assertThrows(IllegalArgumentException.class, () -> tierList.tierInsert(el1, t1, 0));
        assertThrows(IllegalArgumentException.class, () -> tierList.tierInsert(el2, t1, 0));
        assertThrows(IllegalArgumentException.class, () -> tierList.tierInsert(el3, t1, 0));
        assertThrows(IllegalArgumentException.class, () -> tierList.tierInsert(el4, t1, 0));

    }

    @Test
    public void tierInsertIndex() {

        assertThrows(TierElementNotFoundException.class, () -> tierList.tierInsert(el1, t1, 0));
        assertThrows(TierElementNotFoundException.class, () -> tierList.tierInsert(el1, t1, 1));
        assertThrows(TierElementNotFoundException.class, () -> tierList.tierInsert(el1, t1, -1));

        tierList.removeElement(el1);
        tierList.removeElement(el2);

        tierList.addElement(el1, t1);
        tierList.addElement(el2, t1);

        el1.changeTo(TieredStatus.TIERED);
        el2.changeTo(TieredStatus.TIERED);

        // t1: el1, el2
        assertEquals(List.of(el1, el2), t1.getTiered());
        assertEquals(List.of(el3, el4), tierList.getUnTiered());

        tierList.tierInsert(el3, t1, 1);
        // t1: el1, el3, el2
        assertEquals(List.of(el1, el3, el2), t1.getTiered());
        assertEquals(List.of(el4), tierList.getUnTiered());

        assertThrows(TierNotFoundException.class, () -> tierList.tierInsert(el4, new Tier(), 0));
        assertThrows(TierNotFoundException.class, () -> tierList.tierInsert(el4, new Tier(), -1));

        tierList.tierInsert(el4, t1, 0);
        // t1: el4, el1, el3, el2
        assertEquals(List.of(el4, el1, el3, el2), t1.getTiered());
        assertEquals(List.of(), tierList.getUnTiered());

        assertTrue(
                tierList.getTiers()
                        .stream()
                        .map(Tier::getTiered)
                        .flatMap(List::stream)
                        .map(TierElement::getStatus)
                        .allMatch(
                                e -> e.equals(TieredStatus.TIERED)
                        )
        );

        assertThrows(IllegalArgumentException.class, () -> tierList.tierInsert(el1, t1, 0));
        assertThrows(IllegalArgumentException.class, () -> tierList.tierInsert(el2, t1, 0));

    }

    @Test
    public void unTier() throws FileNotFoundException {

        final var nonExistent = new TierElement();
        nonExistent.changeTo(TieredStatus.TIERED);

        assertThrows(TierElementNotFoundException.class, () -> tierList.unTier(nonExistent));
        assertThrows(IllegalArgumentException.class, () -> tierList.unTier(el1));

        tierList.tier(el1, t1);
        tierList.tier(el2, t1);
        tierList.tier(el3, t2);
        tierList.tier(el4, t3);

        assertTrue(
                tierList.getTiers()
                .stream()
                .map(Tier::getTiered)
                .flatMap(List::stream)
                .map(TierElement::getStatus)
                .allMatch(
                        e -> e.equals(TieredStatus.TIERED)
                )
        );

        assertEquals(List.of(el1, el2), t1.getTiered());
        assertEquals(List.of(el3), t2.getTiered());
        assertEquals(List.of(el4), t3.getTiered());
        assertEquals(List.of(), tierList.getUnTiered());

        tierList.unTier(el1);
        assertEquals(List.of(el2), t1.getTiered());
        assertEquals(List.of(el1), tierList.getUnTiered());

        tierList.unTier(el2);
        assertEquals(List.of(), t1.getTiered());
        assertEquals(List.of(el1, el2), tierList.getUnTiered());

        tierList.unTier(el3);
        assertEquals(List.of(), t2.getTiered());
        assertEquals(List.of(el1, el2, el3), tierList.getUnTiered());

        tierList.unTier(el4);
        assertEquals(List.of(), t3.getTiered());
        assertEquals(List.of(el1, el2, el3, el4), tierList.getUnTiered());

        assertTrue(
                tierList.getTiers()
                        .stream()
                        .map(Tier::getTiered)
                        .flatMap(List::stream)
                        .map(TierElement::getStatus)
                        .allMatch(
                                e -> e.equals(TieredStatus.UNTIERED)
                        )
        );

    }

    @Test
    public void unTierInsertPointer() throws FileNotFoundException {

        final var nonExistent = new TierElement();
        nonExistent.changeTo(TieredStatus.TIERED);

        assertThrows(TierElementNotFoundException.class, () -> tierList.unTierInsert(nonExistent, el1));
        assertThrows(IllegalArgumentException.class, () -> tierList.unTierInsert(el1, el2));

        tierList.tier(el1, t1);
        tierList.tier(el2, t1);
        tierList.tier(el3, t2);
        tierList.tier(el4, t3);

        assertTrue(
                tierList.getTiers()
                        .stream()
                        .map(Tier::getTiered)
                        .flatMap(List::stream)
                        .map(TierElement::getStatus)
                        .allMatch(
                                e -> e.equals(TieredStatus.TIERED)
                        )
        );

        assertEquals(List.of(el1, el2), t1.getTiered());
        assertEquals(List.of(el3), t2.getTiered());
        assertEquals(List.of(el4), t3.getTiered());
        assertEquals(List.of(), tierList.getUnTiered());

        tierList.unTier(el1);
        assertEquals(List.of(el2), t1.getTiered());
        assertEquals(List.of(el1), tierList.getUnTiered());

        tierList.unTierInsert(el2, el1);
        assertEquals(List.of(), t1.getTiered());
        assertEquals(List.of(el2, el1), tierList.getUnTiered());

        tierList.unTierInsert(el3, el1);
        assertEquals(List.of(), t2.getTiered());
        assertEquals(List.of(el2, el3, el1), tierList.getUnTiered());

        tierList.unTierInsert(el4, el2);
        assertEquals(List.of(), t3.getTiered());
        assertEquals(List.of(el4, el2, el3, el1), tierList.getUnTiered());

        assertTrue(
                tierList.getTiers()
                        .stream()
                        .map(Tier::getTiered)
                        .flatMap(List::stream)
                        .map(TierElement::getStatus)
                        .allMatch(
                                e -> e.equals(TieredStatus.UNTIERED)
                        )
        );

    }

    @Test
    public void unTierInsertIndex() throws FileNotFoundException {

        final var nonExistent = new TierElement();
        nonExistent.changeTo(TieredStatus.TIERED);

        assertThrows(TierElementNotFoundException.class, () -> tierList.unTierInsert(nonExistent, 0));
        assertThrows(IllegalArgumentException.class, () -> tierList.unTierInsert(el1, -1));

        tierList.tier(el1, t1);
        tierList.tier(el2, t1);
        tierList.tier(el3, t2);
        tierList.tier(el4, t3);

        assertTrue(
                tierList.getTiers()
                        .stream()
                        .map(Tier::getTiered)
                        .flatMap(List::stream)
                        .map(TierElement::getStatus)
                        .allMatch(
                                e -> e.equals(TieredStatus.TIERED)
                        )
        );

        assertEquals(List.of(el1, el2), t1.getTiered());
        assertEquals(List.of(el3), t2.getTiered());
        assertEquals(List.of(el4), t3.getTiered());
        assertEquals(List.of(), tierList.getUnTiered());

        tierList.unTier(el1);
        assertEquals(List.of(el2), t1.getTiered());
        assertEquals(List.of(el1), tierList.getUnTiered());

        assertThrows(TierElementNotFoundException.class, () -> tierList.unTierInsert(el2, 999));

        tierList.unTierInsert(el2, 0);
        assertEquals(List.of(), t1.getTiered());
        assertEquals(List.of(el2, el1), tierList.getUnTiered());

        tierList.unTierInsert(el3, 2);
        assertEquals(List.of(), t2.getTiered());
        assertEquals(List.of(el2, el1, el3), tierList.getUnTiered());

        tierList.unTierInsert(el4, 1);
        assertEquals(List.of(), t3.getTiered());
        assertEquals(List.of(el2, el4, el1, el3), tierList.getUnTiered());

        assertTrue(
                tierList.getTiers()
                        .stream()
                        .map(Tier::getTiered)
                        .flatMap(List::stream)
                        .map(TierElement::getStatus)
                        .allMatch(
                                e -> e.equals(TieredStatus.UNTIERED)
                        )
        );

    }

    @Test
    public void addTier() {

        tierList.addTier(t1);
        assertEquals(List.of(t1, t2, t3, t4, t1), tierList.getTiers());
        tierList.addTier(t2);
        assertEquals(List.of(t1, t2, t3, t4, t1, t2), tierList.getTiers());

    }

    @Test
    public void addElement() {

        tierList.removeAllElements(Set.of(el1, el2, el3, el4));

        assertTrue(
                tierList.getTiers()
                        .stream()
                        .map(Tier::getTiered)
                        .flatMap(List::stream)
                        .map(TierElement::getStatus)
                        .allMatch(
                                e -> e.equals(TieredStatus.UNTIERED)
                        )
        );

        tierList.addElement(el1, Tier.UNTIERED);
        assertEquals(List.of(el1), tierList.getUnTiered());
        tierList.addElement(el2, Tier.UNTIERED);
        assertEquals(List.of(el1, el2), tierList.getUnTiered());

        tierList.addElement(el3, t1);
        assertEquals(List.of(el3), t1.getTiered());
        tierList.addElement(el4, t1);
        assertEquals(List.of(el3, el4), t1.getTiered());

        assertTrue(
                tierList.getTiers()
                        .stream()
                        .map(Tier::getTiered)
                        .flatMap(List::stream)
                        .map(TierElement::getStatus)
                        .allMatch(
                                e -> e.equals(TieredStatus.UNTIERED)
                        )
        );

    }

    @Test
    public void addAllElements() {

        tierList.addAllElements(List.of(el1, el2, el3, el4), t4);
        assertEquals(List.of(el1, el2, el3, el4), t4.getTiered());

    }

    @Test
    public void addElementPointer() {
    }

    @Test
    public void addElementIndex() {
    }

    @Test
    public void removeTierPointer() {
    }

    @Test
    public void removeTierIndex() {
    }

    @Test
    public void removeElement() {
    }

    @Test
    public void removeAllElements() {
    }

    @Test
    public void swapTiersPointer() {
    }

    @Test
    public void swapTiersIndex() {
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
    public void containsPointer() {
    }

    @Test
    public void containsIndex() {
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
    public void moveElementPointer() {
    }

    @Test
    public void moveElementIndex() {
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