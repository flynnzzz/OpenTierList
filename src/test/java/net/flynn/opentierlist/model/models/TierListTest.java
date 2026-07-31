package net.flynn.opentierlist.model.models;

import net.flynn.opentierlist.model.enums.TieredStatus;
import net.flynn.opentierlist.model.exceptions.TierElementNotFoundException;
import net.flynn.opentierlist.model.exceptions.TierNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
    public void setUp() {

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

        final var el5 = new TierElement("el5");

        tierList.addElement(el5, Tier.UNTIERED);
        tierList.tierInsert(el5, t1, el2);
        // t1: el3, el4, el1, el5, el2
        assertEquals(List.of(el3, el4, el1, el5, el2), t1.getTiered());
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
    public void unTier() {

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
    public void unTierInsertPointer() {

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
    public void unTierInsertIndex() {

        final var nonExistent = new TierElement();
        nonExistent.changeTo(TieredStatus.TIERED);

        assertThrows(TierElementNotFoundException.class, () -> tierList.unTierInsert(nonExistent, 0));
        assertThrows(TierElementNotFoundException.class, () -> tierList.unTierInsert(el1, -1));

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

        assertThrows(TierElementNotFoundException.class, () -> tierList.addElement(el2, Tier.UNTIERED, new TierElement()));

        tierList.addElement(el2, Tier.UNTIERED, el1);
        assertEquals(List.of(el2, el1), tierList.getUnTiered());

        tierList.addElement(el3, t1);
        assertEquals(List.of(el3), t1.getTiered());

        assertThrows(TierElementNotFoundException.class, () -> tierList.addElement(el4, t1, new TierElement()));

        tierList.addElement(el4, t1, el3);
        assertEquals(List.of(el4, el3), t1.getTiered());

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
    public void addElementIndex() {

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

        tierList.addElement(el1, Tier.UNTIERED, 0);
        assertEquals(List.of(el1), tierList.getUnTiered());

        assertThrows(TierElementNotFoundException.class, () -> tierList.addElement(el2, Tier.UNTIERED, 999));

        tierList.addElement(el2, Tier.UNTIERED, 0);
        assertEquals(List.of(el2, el1), tierList.getUnTiered());

        tierList.addElement(el3, t1, 0);
        assertEquals(List.of(el3), t1.getTiered());

        assertThrows(TierElementNotFoundException.class, () -> tierList.addElement(el4, Tier.UNTIERED, -1));

        tierList.addElement(el4, t1, 1);
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
    public void removeTierPointer() {

        tierList.removeTier(t4);
        assertEquals(List.of(t1, t2, t3), tierList.getTiers());

        tierList.removeTier(t2);
        assertEquals(List.of(t1, t3), tierList.getTiers());

        tierList.removeTier(t3);
        assertEquals(List.of(t1), tierList.getTiers());

        assertThrows(TierNotFoundException.class, () -> tierList.removeTier(t4));
        assertThrows(TierNotFoundException.class, () -> tierList.removeTier(t2));
        assertThrows(TierNotFoundException.class, () -> tierList.removeTier(t3));

    }

    @Test
    public void removeTierIndex() {

        tierList.removeTier(3);
        assertEquals(List.of(t1, t2, t3), tierList.getTiers());

        tierList.removeTier(1);
        assertEquals(List.of(t1, t3), tierList.getTiers());

        tierList.removeTier(1);
        assertEquals(List.of(t1), tierList.getTiers());

        assertThrows(TierNotFoundException.class, () -> tierList.removeTier(999));
        assertThrows(TierNotFoundException.class, () -> tierList.removeTier(-1));

    }

    @Test
    public void removeElement() {

        // setup
        tierList.tier(el1, t1);
        tierList.tier(el2, t2);
        tierList.tierInsert(el3, t2, 0);
        assertEquals(List.of(el1), t1.getTiered());
        assertEquals(List.of(el3, el2), t2.getTiered());
        assertEquals(List.of(el4), tierList.getUnTiered());

        tierList.removeElement(el4);
        assertEquals(List.of(), tierList.getUnTiered());

        tierList.removeElement(el1);
        assertEquals(List.of(), t1.getTiered());

        tierList.removeElement(el3);
        assertEquals(List.of(el2), t2.getTiered());

        tierList.removeElement(el2);
        assertEquals(List.of(), t2.getTiered());

        assertThrows(TierElementNotFoundException.class, () -> tierList.removeElement(el1));
        assertThrows(TierElementNotFoundException.class, () -> tierList.removeElement(el2));
        assertThrows(TierElementNotFoundException.class, () -> tierList.removeElement(el3));
        assertThrows(TierElementNotFoundException.class, () -> tierList.removeElement(el4));

    }

    @Test
    public void removeAllElements() {
        tierList.removeAllElements(Set.of(el1, el2, el3, el4));
        assertEquals(List.of(), tierList.getUnTiered());
    }

    @Test
    public void swapTiersPointer() {

        assertEquals(List.of(t1, t2, t3, t4), tierList.getTiers());

        tierList.swapTiers(t1, t2);
        assertEquals(List.of(t2, t1, t3, t4), tierList.getTiers());

        tierList.swapTiers(t4, t2);
        assertEquals(List.of(t4, t1, t3, t2), tierList.getTiers());

        tierList.swapTiers(t4, t4);
        assertEquals(List.of(t4, t1, t3, t2), tierList.getTiers());

        assertThrows(TierNotFoundException.class, () -> tierList.swapTiers(new Tier(), t1));

    }

    @Test
    public void swapTiersIndex() {

        assertEquals(List.of(t1, t2, t3, t4), tierList.getTiers());

        tierList.swapTiers(0, 1);
        assertEquals(List.of(t2, t1, t3, t4), tierList.getTiers());

        tierList.swapTiers(3, 0);
        assertEquals(List.of(t4, t1, t3, t2), tierList.getTiers());

        tierList.swapTiers(3, 3);
        assertEquals(List.of(t4, t1, t3, t2), tierList.getTiers());

        assertThrows(TierNotFoundException.class, () -> tierList.swapTiers(-1, 0));
        assertThrows(TierNotFoundException.class, () -> tierList.swapTiers(-1, 999));

    }

    @Test
    public void indexOfTier() {

        assertEquals(0, tierList.indexOf(t1));
        assertEquals(1, tierList.indexOf(t2));
        assertEquals(2, tierList.indexOf(t3));
        assertEquals(3, tierList.indexOf(t4));

    }

    @Test
    public void indexOfElement() {

        // setup
        tierList.tier(el1, t1);
        tierList.tier(el2, t2);
        tierList.tierInsert(el3, t2, 0);
        assertEquals(List.of(el1), t1.getTiered());
        assertEquals(List.of(el3, el2), t2.getTiered());
        assertEquals(List.of(el4), tierList.getUnTiered());

        assertEquals(0, tierList.indexOf(el1));
        assertEquals(0, tierList.indexOf(el3));
        assertEquals(1, tierList.indexOf(el2));
        assertEquals(0, tierList.indexOf(el4));

    }

    @Test
    public void tiersQuantity() {

        assertEquals(4, tierList.tiersQuantity());

        tierList.removeTier(0);
        assertEquals(3, tierList.tiersQuantity());
        tierList.removeTier(0);
        assertEquals(2, tierList.tiersQuantity());
        tierList.removeTier(0);
        assertEquals(1, tierList.tiersQuantity());
        tierList.removeTier(0);
        assertEquals(0, tierList.tiersQuantity());

    }

    @Test
    public void containsTier() {

        assertTrue(tierList.contains(t1));
        assertTrue(tierList.contains(t2));
        assertTrue(tierList.contains(t3));
        assertTrue(tierList.contains(t4));

        assertFalse(tierList.contains(new Tier()));

    }

    @Test
    public void containsElement() {

        // setup
        tierList.tier(el1, t1);
        tierList.tier(el2, t2);
        tierList.tierInsert(el3, t2, 0);

        assertTrue(tierList.contains(el1));
        assertTrue(tierList.contains(el2));
        assertTrue(tierList.contains(el3));
        assertTrue(tierList.contains(el4));

        assertFalse(tierList.contains(new TierElement()));
    }

    @Test
    public void moveTierPointer() {

        assertThrows(UnsupportedOperationException.class, () -> tierList.moveTier(Tier.UNTIERED, t1));

        assertEquals(List.of(t1, t2, t3, t4), tierList.getTiers());

        tierList.moveTier(t1, t4);
        assertEquals(List.of(t2, t3, t4, t1), tierList.getTiers());

        tierList.moveTier(t4, t3);
        assertEquals(List.of(t2, t4, t3, t1), tierList.getTiers());

        tierList.moveTier(t1, t2);
        assertEquals(List.of(t1, t2, t4, t3), tierList.getTiers());

    }

    @Test
    public void moveTierIndex() {

        assertEquals(List.of(t1, t2, t3, t4), tierList.getTiers());

        tierList.moveTier(t1, 3);
        assertEquals(List.of(t2, t3, t4, t1), tierList.getTiers());

        tierList.moveTier(t4, 1);
        assertEquals(List.of(t2, t4, t3, t1), tierList.getTiers());

        tierList.moveTier(t1, 0);
        assertEquals(List.of(t1, t2, t4, t3), tierList.getTiers());

    }

    @Test
    public void moveElement() {

        assertEquals(List.of(el1, el2, el3, el4), tierList.getUnTiered());

        tierList.moveElement(el1, t1);
        assertEquals(TieredStatus.TIERED, el1.getStatus());
        assertEquals(List.of(el2, el3, el4), tierList.getUnTiered());
        assertEquals(List.of(el1), t1.getTiered());

        tierList.moveElement(el2, t2);
        assertEquals(TieredStatus.TIERED, el2.getStatus());
        assertEquals(List.of(el3, el4), tierList.getUnTiered());
        assertEquals(List.of(el1), t1.getTiered());
        assertEquals(List.of(el2), t2.getTiered());

        tierList.moveElement(el3, t3);
        assertEquals(TieredStatus.TIERED, el3.getStatus());
        assertEquals(List.of(el4), tierList.getUnTiered());
        assertEquals(List.of(el1), t1.getTiered());
        assertEquals(List.of(el2), t2.getTiered());
        assertEquals(List.of(el3), t3.getTiered());

        tierList.moveElement(el1, Tier.UNTIERED);
        assertEquals(TieredStatus.UNTIERED, el1.getStatus());
        assertEquals(List.of(el4, el1), tierList.getUnTiered());
        assertEquals(List.of(), t1.getTiered());
        assertEquals(List.of(el2), t2.getTiered());
        assertEquals(List.of(el3), t3.getTiered());

    }

    @Test
    public void moveElementPointer() {

        assertThrows(TierElementNotFoundException.class, () -> tierList.moveElement(el1, t1, new TierElement()));

        assertEquals(List.of(el1, el2, el3, el4), tierList.getUnTiered());

        tierList.moveElement(el1, t1);
        assertEquals(TieredStatus.TIERED, el1.getStatus());
        assertEquals(List.of(el2, el3, el4), tierList.getUnTiered());
        assertEquals(List.of(el1), t1.getTiered());

        tierList.moveElement(el2, t1, el1);
        assertEquals(TieredStatus.TIERED, el2.getStatus());
        assertEquals(List.of(el3, el4), tierList.getUnTiered());
        assertEquals(List.of(el2, el1), t1.getTiered());

        tierList.moveElement(el3, t1, el2);
        assertEquals(TieredStatus.TIERED, el3.getStatus());
        assertEquals(List.of(el4), tierList.getUnTiered());
        assertEquals(List.of(el3, el2, el1), t1.getTiered());

        tierList.moveElement(el1, Tier.UNTIERED, el4);
        assertEquals(TieredStatus.UNTIERED, el1.getStatus());
        assertEquals(List.of(el1, el4), tierList.getUnTiered());
        assertEquals(List.of(el3, el2), t1.getTiered());

        tierList.moveElement(el1, Tier.UNTIERED, el4);
        assertEquals(List.of(el4, el1), tierList.getUnTiered());

    }

    @Test
    public void moveElementIndex() {

        assertThrows(TierElementNotFoundException.class, () -> tierList.moveElement(el1, t1, -1));

        assertEquals(List.of(el1, el2, el3, el4), tierList.getUnTiered());

        tierList.moveElement(el1, t1, 0);
        assertEquals(TieredStatus.TIERED, el1.getStatus());
        assertEquals(List.of(el2, el3, el4), tierList.getUnTiered());
        assertEquals(List.of(el1), t1.getTiered());

        tierList.moveElement(el2, t1, 0);
        assertEquals(TieredStatus.TIERED, el2.getStatus());
        assertEquals(List.of(el3, el4), tierList.getUnTiered());
        assertEquals(List.of(el2, el1), t1.getTiered());

        tierList.moveElement(el3, t1, 1);
        assertEquals(TieredStatus.TIERED, el3.getStatus());
        assertEquals(List.of(el4), tierList.getUnTiered());
        assertEquals(List.of(el2, el3, el1), t1.getTiered());

        tierList.moveElement(el1, Tier.UNTIERED, 1);
        assertEquals(TieredStatus.UNTIERED, el1.getStatus());
        assertEquals(List.of(el4, el1), tierList.getUnTiered());
        assertEquals(List.of(el2, el3), t1.getTiered());

    }

    @Test
    public void setTierName() {

        assertEquals("t1", t1.getName());

        tierList.setTierName(0, "S Tier");
        assertEquals("S Tier", t1.getName());

        tierList.setTierName(0, "A Tier");
        assertEquals("A Tier", t1.getName());

        assertThrows(IllegalArgumentException.class, () -> t2.setName(""));

    }

    @Test
    public void setTierColor() {

        tierList.setTierColor(0, "0x000000ff");
        assertEquals("0x000000ff", t1.getColor());

        tierList.setTierColor(0, "0xff0000ff");
        assertEquals("0xff0000ff", t1.getColor());

        tierList.setTierColor(1, "0x00ff00ff");
        assertEquals("0x00ff00ff", t2.getColor());

        assertThrows(IllegalArgumentException.class, () -> tierList.setTierColor(1, ""));
        assertThrows(IllegalArgumentException.class, () -> tierList.setTierColor(1, "Invalid Color"));

    }

    @Test
    public void getTierName() {

        assertEquals("t1", t1.getName());
        assertEquals("t2", t2.getName());
        assertEquals("t3", t3.getName());
        assertEquals("t4", t4.getName());

    }

    @Test
    public void getTierColor() {

        assertNotNull(t1.getColor());
        assertNotNull(t2.getColor());
        assertNotNull(t3.getColor());
        assertNotNull(t4.getColor());

        assertTrue(t1.getColor().matches("0x[0-9a-fA-F]{8}"));
        assertTrue(t2.getColor().matches("0x[0-9a-fA-F]{8}"));

    }

    @Test
    public void getUnTiered() {

        assertEquals(List.of(el1, el2, el3, el4), tierList.getUnTiered());

        tierList.moveElement(el1, t1);
        assertEquals(List.of(el2, el3, el4), tierList.getUnTiered());

        tierList.moveElement(el2, t2);
        assertEquals(List.of(el3, el4), tierList.getUnTiered());

        tierList.moveElement(el3, t3);
        assertEquals(List.of(el4), tierList.getUnTiered());

        tierList.moveElement(el4, t4);
        assertEquals(List.of(), tierList.getUnTiered());

    }

    @Test
    public void getTiers() {

        List<Tier> tiers = tierList.getTiers();
        assertEquals(4, tiers.size());
        assertEquals(t1, tiers.get(0));
        assertEquals(t2, tiers.get(1));
        assertEquals(t3, tiers.get(2));
        assertEquals(t4, tiers.get(3));

        tierList.moveElement(el1, t1);
        tierList.moveElement(el2, t2);
        tiers = tierList.getTiers();
        assertEquals(4, tiers.size());
        assertEquals(t1, tiers.get(0));
        assertEquals(t2, tiers.get(1));

        assertFalse(tiers.contains(Tier.UNTIERED));

    }

}