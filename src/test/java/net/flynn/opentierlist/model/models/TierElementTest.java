package net.flynn.opentierlist.model.models;

import java.net.URISyntaxException;
import java.util.Objects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.flynn.opentierlist.model.enums.TieredStatus;
import net.flynn.opentierlist.persistence.ResourceHolder;

import static org.junit.Assert.*;

public class TierElementTest {

  TierElement defaultTierElement, el1, el2, el3;

  @Before
  public void setUp() throws Exception {

    final String resource = Objects.requireNonNull(getClass().getResource("/greyyakuza.jpg")).toURI().toString();

    defaultTierElement = new TierElement();
    el1 = new TierElement("elementName1");
    el2 = new TierElement("elementName2", resource);
    el3 = new TierElement(TieredStatus.TIERED, "elementName3", resource);

  }

  @After
  public void tearDown() {
    defaultTierElement = null;
    el1 = null;
    el2 = null;
    el3 = null;
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
    assertEquals("New Element", defaultTierElement.getElementName());
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

    String resource = Objects.requireNonNull(getClass().getResource("/greyyakuza.jpg")).toURI().toString();

    assertEquals(
        (Objects.requireNonNull(getClass().getResource(ResourceHolder.DEFAULT_ELEMENT_IMAGE))).toURI().toString(),
        defaultTierElement.getImageUri()
    );

    assertEquals(resource, el2.getImageUri() );
  }

  @Test
  public void updateImagePath() throws URISyntaxException {

    var el4 = new TierElement("elementName4", "nonExistentUrl");

    assertEquals(
            (Objects.requireNonNull(getClass().getResource(ResourceHolder.DEFAULT_ELEMENT_IMAGE))).toURI().toString(),
            el4.getImageUri()
    );

    el4.updateImagePath();

    assertEquals(
            (Objects.requireNonNull(getClass().getResource(ResourceHolder.DEFAULT_ELEMENT_IMAGE))).toURI().toString(),
            el4.getImageUri()
    );

  }

  @Test
  public void testEquals() {

    assertNotEquals(el1, el2);
    assertNotEquals(el2, el3);
    assertNotEquals(el3, el1);

    assertNotEquals(el2, el1);
    assertNotEquals(el3, el2);
    assertNotEquals(el1, el3);

    assertNotEquals(el1, new TierElement(el1.getElementName(), el1.getImageUri()));

  }

  @Test
  public void equalsElement() {

    assertTrue(el1.equalsElement(new TierElement(el1.getElementName(), el1.getImageUri())));
    assertFalse(el1.equalsElement(el2));

  }
}
