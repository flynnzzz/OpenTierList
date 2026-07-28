package net.flynn.opentierlist.model.models;

import javafx.scene.paint.Color;
import net.flynn.opentierlist.model.enums.TieredStatus;
import net.flynn.opentierlist.model.exceptions.TierElementNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TierTest {

    Tier defaultTier, a, b, c, d;
    TierElement el1, el2, el3, el4, el0, elm1;

    @Before
    public void setUp() throws Exception {
        elm1 = new TierElement();
        el0 = new TierElement();
        el1 = new TierElement("elementName1");
        el2 = new TierElement("elementName2");
        el3 = new TierElement("elementName3", "uri2");
        el4 = new TierElement(TieredStatus.TIERED, "elementName4", "uri3");

        defaultTier = new Tier();
        a = new Tier("a");
        b = new Tier("b", Color.GREEN.toString());
        c = new Tier("c", Color.RED.toString(), new ArrayList<>(List.of(el1,el2,el3,el4)));
        d = new Tier("d", Color.BLUE.toString());
    }

    @After
    public void tearDown() {
        defaultTier = null;
        a = null; b = null; c = null; d = null;
        elm1 = null; el0 = null; el1 = null;
        el2 = null; el3 = null; el4 = null;

    }

    @Test
    public void add() {
        assertEquals(0, a.elementsCount());
        assertEquals(TieredStatus.UNTIERED, elm1.getStatus());

        a.add(elm1);
        assertEquals(1, a.elementsCount());

        assertTrue(a.add(elm1));
        assertEquals(2, a.elementsCount());

        a.add(el0);
        assertEquals(3, a.elementsCount());

        assertEquals(TieredStatus.TIERED, a.getTiered().getFirst().getStatus());
        assertEquals(TieredStatus.TIERED, a.getTiered().get(1).getStatus());
    }

    @Test
    public void remove() {
        assertThrows(TierElementNotFoundException.class, () -> b.remove(el1));
        assertThrows(TierElementNotFoundException.class, () -> b.remove(100));

        a.add(elm1); a.add(elm1); a.add(el0); a.add(el0);
        a.add(elm1); a.add(elm1); a.add(el0); a.add(el0);

        assertEquals(elm1, a.remove(0));
        assertEquals(7, a.elementsCount());

        assertTrue(a.remove(elm1));
        assertEquals(6, a.elementsCount());

        for (var element : a.getTiered()) assertTrue(a.remove(element));

        assertEquals(0, a.elementsCount());
    }

    @Test
    public void swapPointers() {

        final int secondLastIndex = c.elementsCount() - 2, lastIndex = c.elementsCount() - 1;

        var first = c.getTiered().getFirst();
        var secondLast = c.getTiered().get(secondLastIndex);
        var last = c.getTiered().getLast();

        // 1, 2, 3, 4
        assertEquals(first, c.getTiered().getFirst());
        assertEquals(secondLast, c.getTiered().get(secondLastIndex));
        assertEquals(List.of(el1, el2, el3, el4), c.getTiered());
        assertEquals(lastIndex + 1, c.elementsCount());

        c.swap(first, secondLast);
        // 3, 2, 1, 4
        assertEquals(first, c.getTiered().get(secondLastIndex));
        assertEquals(secondLast, c.getTiered().getFirst());
        assertEquals(List.of(el3, el2, el1, el4), c.getTiered());
        assertEquals(lastIndex + 1, c.elementsCount());

        secondLast = c.getTiered().get(secondLastIndex);
        last = c.getTiered().getLast();

        c.swap(secondLast, last);
        // 3, 2, 4, 1
        assertEquals(lastIndex + 1, c.elementsCount());
        assertEquals(List.of(el3, el2, el4, el1), c.getTiered());

    }

    @Test
    public void swapIndexes() {

        final int secondLastIndex = c.elementsCount() - 2, lastIndex = c.elementsCount() - 1;

        var first = c.getTiered().getFirst();
        var secondLast = c.getTiered().get(secondLastIndex);

        // 1, 2, 3, 4
        assertEquals(first, c.getTiered().getFirst());
        assertEquals(secondLast, c.getTiered().get(secondLastIndex));
        assertEquals(List.of(el1, el2, el3, el4), c.getTiered());
        assertEquals(lastIndex + 1, c.elementsCount());

        c.swap(0, secondLastIndex);
        // 3, 2, 1, 4
        assertEquals(first, c.getTiered().get(secondLastIndex));
        assertEquals(secondLast, c.getTiered().getFirst());
        assertEquals(List.of(el3, el2, el1, el4), c.getTiered());
        assertEquals(lastIndex + 1, c.elementsCount());

        c.swap(secondLastIndex, lastIndex);
        // 3, 2, 4, 1
        assertEquals(lastIndex + 1, c.elementsCount());
        assertEquals(List.of(el3, el2, el4, el1), c.getTiered());

    }

    @Test
    public void contains() throws FileNotFoundException {

        assertTrue(c.contains(el1));
        assertTrue(c.contains(el2));
        assertTrue(c.contains(el3));
        assertTrue(c.contains(el4));

        assertFalse(c.contains(elm1));
        assertFalse(c.contains(el0));
        assertFalse(c.contains(new TierElement()));

    }

    @Test
    public void elementsCount() {

        assertEquals(0, defaultTier.elementsCount());
        assertEquals(0, a.elementsCount());
        assertEquals(0, b.elementsCount());
        assertEquals(4, c.elementsCount());
        assertEquals(0, d.elementsCount());
        d.add(el0);
        assertEquals(1, d.elementsCount());
        d.remove(el0);
        assertEquals(0, d.elementsCount());

    }

    @Test
    public void movePointer() {

        final int secondLastIndex = c.elementsCount() - 2, lastIndex = c.elementsCount() - 1;

        var first = c.getTiered().getFirst();
        var secondLast = c.getTiered().get(secondLastIndex);
        var last = c.getTiered().getLast();

        // 1, 2, 3, 4
        assertEquals(first, c.getTiered().getFirst());
        assertEquals(secondLast, c.getTiered().get(secondLastIndex));
        assertEquals(List.of(el1, el2, el3, el4), c.getTiered());
        assertEquals(lastIndex + 1, c.elementsCount());

        c.move(first, secondLast);
        // 2, 1, 3, 4
        assertEquals(List.of(el2, el3, el1, el4), c.getTiered());
        assertEquals(lastIndex + 1, c.elementsCount());

        c.move(secondLast, last);
        // 2, 1, 4, 3
        assertEquals(lastIndex + 1, c.elementsCount());
        assertEquals(List.of(el2, el1, el4, el3), c.getTiered());

        c.move(secondLast, first);
        // 2, 3, 1, 4
        assertEquals(lastIndex + 1, c.elementsCount());
        assertEquals(List.of(el2, el3, el1, el4), c.getTiered());

    }

    @Test
    public void moveIndex() {
        final int secondLastIndex = c.elementsCount() - 2, lastIndex = c.elementsCount() - 1;

        var first = c.getTiered().getFirst();
        var secondLast = c.getTiered().get(secondLastIndex);

        // 1, 2, 3, 4
        assertEquals(List.of(el1, el2, el3, el4), c.getTiered());
        assertEquals(lastIndex + 1, c.elementsCount());

        c.move(first, secondLastIndex);
        // 2, 1, 3, 4
        assertEquals(List.of(el2, el3, el1, el4), c.getTiered());
        assertEquals(lastIndex + 1, c.elementsCount());

        c.move(secondLast, lastIndex);
        // 2, 1, 4, 3
        assertEquals(lastIndex + 1, c.elementsCount());
        assertEquals(List.of(el2, el1, el4, el3), c.getTiered());

        c.move(secondLast, 0);
        // 3, 2, 1, 4
        assertEquals(lastIndex + 1, c.elementsCount());
        assertEquals(List.of(el3, el2, el1, el4), c.getTiered());

    }

    @Test
    public void copy() {

        final var copyc = c.copy();
        final var copyb = b.copy();

        assertTrue(c.equalsTier(copyc));
        assertTrue(copyc.equalsTier(c));

        assertTrue(b.equalsTier(copyb));
        assertTrue(copyb.equalsTier(b));

        assertNotEquals(c, copyc);
        assertNotEquals(b, copyb);

    }

    @Test
    public void indexOf() {

        assertEquals(0, c.indexOf(el1));
        assertEquals(c.elementsCount() - 3, c.indexOf(el2));
        assertEquals(c.elementsCount() - 2, c.indexOf(el3));
        assertEquals(c.elementsCount() - 1, c.indexOf(el4));

        c.swap(0, 2);
        assertEquals(2, c.indexOf(el1));
        assertEquals(0, c.indexOf(el3));

    }

    @Test
    public void testSetName() {
    }

    @Test
    public void setColor() {
    }

    @Test
    public void testGetName() {
    }

    @Test
    public void getColor() {
    }

    @Test
    public void getTiered() {
    }

    @Test
    public void testHashCode() {
    }

    @Test
    public void testEquals() {
    }

    @Test
    public void equalsTier() {
    }
}