package tests;

import model.Element;
import model.ElementCollection;
import model.Tier;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {
		Element e = new Element(false, "Goku", "gokusuperpower.jpg");
		ElementCollection ec = new ElementCollection();
		Tier t = new Tier("S");
			
	@Test
	public void testElement() {
		Element dr = new Element(true, "Ritchie", "DennisRitchie.png");
		assertEquals(true, dr.isRanked());
		assertEquals("Ritchie", dr.getName());
		assertNotEquals("DennisRitchie.jpg", dr.getImagePath());
	}
	
	@Test
	public void testElementCollection() {
		Element dr = new Element(true, "Ritchie", "DennisRitchie.png");
		Element goku = new Element(false, "Goku", "gokusuperpower.jpg");
		Element simba = new Element(true, "Simba", "lion.png");
		ec.addElement(dr);
		ec.addElement(goku);
		ec.addElement(simba);
		assertEquals(ec.removeElement(simba), true);
		assertEquals(2, ec.size());
		assertTrue(ec.addElement(goku));
		assertTrue(ec.removeElement(goku));
		assertTrue(ec.removeElement(dr));
	}
	
	@Test
	public void testSwapElementCollection() {
		Element dr = new Element(true, "Ritchie", "DennisRitchie.png");
		Element goku = new Element(false, "Goku", "gokusuperpower.jpg");
		ec.addElement(dr);
		ec.addElement(goku);
		ec.swap(goku, dr);
		ElementCollection swapped = new ElementCollection();
		swapped.addElement(goku);
		swapped.addElement(dr);
		assertTrue(swapped.equals(ec));
		swapped.swap(dr, goku);
		ec.swap(goku, dr);
		assertTrue(swapped.equals(ec));
		Element notInList = new Element(false, "", "");
		assertFalse(swapped.swap(goku, notInList));
	}
	
	@Test
	public void testTier() {
		t.addElement(new Element(false, "GokuGT", "gokussj4GT.png"));
		t.addElement(new Element(false, "GokuDaima", "gokussj4Daima.png"));
		t.addElement(new Element(false, "VegitoSSJ", "supervegito.png"));
		t.addElement(e);
		assertEquals(t.getElements().size(), 4);
		assertTrue(t.getElements().get(3).equals(e));
		t.swap(t.get(0), t.get(3));
		assertEquals(t.indexOf(e), 0);
		IO.println(t.toString());
	}
}
