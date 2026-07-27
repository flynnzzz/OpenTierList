package net.flynn.opentierlist.model.models;

import junit.framework.TestCase;
import net.flynn.opentierlist.model.enums.TieredStatus;
import net.flynn.opentierlist.persistence.ResourceHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.Objects;

import static org.junit.Assert.*;

public class TierElementTest {

    TierElement defaultTierElement, el1, el2, el3;

    @Before
    public void setUp() throws Exception {
        defaultTierElement = new TierElement();
        el1 = new TierElement("elementName1");
        el2 = new TierElement("elementName2", "uri2");
        el3 = new TierElement(TieredStatus.TIERED,"elementName3", "uri3");

    }

    @After
    public void tearDown() throws Exception {
        defaultTierElement = null;
        el1 = null; el2 = null; el3 = null;
    }

    @Test
    public void getStatus() {
        assertEquals(TieredStatus.UNTIERED, defaultTierElement.getStatus());
        assertEquals(TieredStatus.UNTIERED, el1.getStatus());
        assertEquals(TieredStatus.UNTIERED, el2.getStatus());
        assertEquals(TieredStatus.TIERED, el3.getStatus());
    }

    @Test
    public void isTiered() {
        assertFalse(defaultTierElement.isTiered());
        assertFalse(el1.isTiered());
        assertFalse(el2.isTiered());
        assertTrue(el3.isTiered());
    }

    @Test
    public void changeTo() {
        el1.changeTo(TieredStatus.TIERED);
        assertTrue(el1.isTiered());
        el1.changeTo(TieredStatus.UNTIERED);
        assertFalse(el1.isTiered());

        el2.changeTo(TieredStatus.TIERED);
        assertTrue(el2.isTiered());
        el2.changeTo(TieredStatus.UNTIERED);
        assertFalse(el2.isTiered());

        el3.changeTo(TieredStatus.TIERED);
        assertTrue(el3.isTiered());
        el3.changeTo(TieredStatus.UNTIERED);
        assertFalse(el3.isTiered());

        el3.changeTo(TieredStatus.UNTIERED);
        assertFalse(el3.isTiered());
        el3.changeTo(TieredStatus.TIERED);
        assertTrue(el3.isTiered());
    }

    @Test
    public void getElementName() {
        assertEquals("element", defaultTierElement.getElementName());
        assertEquals("elementName1", el1.getElementName());
        assertEquals("elementName2", el2.getElementName());
        assertEquals("elementName3", el3.getElementName());
    }

    @Test
    public void setElementName() {
        el1.setElementName("newElementName1");
        assertEquals("newElementName1", el1.getElementName());
        el1.setElementName("elementName1");
        assertEquals("elementName1", el1.getElementName());

        el2.setElementName("newElementName2");
        assertEquals("newElementName2", el2.getElementName());

        el2.setElementName("newNewElementName2");
        assertEquals("newNewElementName2", el2.getElementName());

        el2.setElementName("elementName2");
        assertEquals("elementName2", el2.getElementName());

        assertThrows(IllegalArgumentException.class, () -> el1.setElementName(""));
        assertThrows(IllegalArgumentException.class, () -> el1.setElementName(" "));
        assertThrows(IllegalArgumentException.class, () -> el1.setElementName(System.lineSeparator()));

        el1.setElementName("elementName1");
    }

    @Test
    public void getImageUri() throws URISyntaxException {
        assertEquals((Objects.requireNonNull(getClass().getResource(ResourceHolder.getDefaultElementIcon()))).toURI().toString(), defaultTierElement.getImageUri());
    }

    @Test
    public void getId() {
    }

    @Test
    public void updateImagePath() {
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

    @Test
    public void testToString() {
    }

    @Test
    public void testToString1() {
    }
}