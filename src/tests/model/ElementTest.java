package tests.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Element;

class ElementTest {
	private static Element immortal;
	private Element whole, noImage, noNameNoImage;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		immortal = new Element(true, "immortal", "wheresomniman.jpg");
	}

	@AfterAll
	static void __tearDownAfterClass__() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		whole = new Element(false, "monkey king", "sunwukong.png");
		noImage = new Element(false, "makoto yuki");
		noNameNoImage = new Element(false);
	}

	@AfterEach
	void tearDown() throws Exception {
		whole = null;
		noImage = null;
		noNameNoImage = null;
	}

	@Test
	void testConstructors() {

		// -- status --
		assertTrue(immortal.isRanked());
		assertFalse(whole.isRanked());
		assertFalse(noImage.isRanked());
		assertFalse(noNameNoImage.isRanked());

		// -- names --
		assertEquals(immortal.getName(), "immortal");
		assertEquals(whole.getName(), "monkey king");
		assertEquals(noImage.getName(), "makoto yuki");
		assertEquals(noNameNoImage.getName(), "element");
		assertNotEquals(noNameNoImage.getName(), "some name");

		// -- imagePaths --
		assertEquals(immortal.getImagePath(), "wheresomniman.jpg");
		assertEquals(whole.getImagePath(), "sunwukong.png");
		assertEquals(noImage.getImagePath(), "NONE");
		assertEquals(noNameNoImage.getImagePath(), "NONE");
		assertNotEquals(noNameNoImage.getImagePath(), "some path");
	}

	// --- setRanked ---

	@Test
	void testSetRanked_trueToFalse() {
		immortal.changeTo(false);
		assertFalse(immortal.isRanked());
		// restore for other tests
		immortal.changeTo(true);
	}

	@Test
	void testSetRanked_falseToTrue() {
		whole.changeTo(true);
		assertTrue(whole.isRanked());
	}

	@Test
	void testSetRanked_noChange() {
		whole.changeTo(false);
		assertFalse(whole.isRanked());
	}

	// --- setName ---

	@Test
	void testSetName_normal() {
		whole.setName("goku");
		assertEquals(whole.getName(), "goku");
	}

	@Test
	void testSetName_overwriteDefault() {
		noNameNoImage.setName("vegeta");
		assertEquals(noNameNoImage.getName(), "vegeta");
		assertNotEquals(noNameNoImage.getName(), "element");
	}

	@Test
	void testSetName_emptyString() {
		assertThrows(IllegalArgumentException.class, () -> {
			whole.setName("");			
		});
		assertEquals(whole.getName(), "monkey king");
	}

	@Test
	void testSetName_whitespace() {
		assertThrows(IllegalArgumentException.class, () -> {
			whole.setName("   ");
		});
		assertEquals(whole.getName(), "monkey king");
	}

	@Test
	void testSetName_multipleUpdates() {
		whole.setName("first");
		whole.setName("second");
		assertEquals(whole.getName(), "second");
	}

	// --- setImagePath ---

	@Test
	void testSetImagePath_normal() {
		noImage.setImagePath("newimage.png");
		assertEquals(noImage.getImagePath(), "newimage.png");
	}

	@Test
	void testSetImagePath_overwriteDefault() {
		noNameNoImage.setImagePath("added.jpg");
		assertEquals(noNameNoImage.getImagePath(), "added.jpg");
		assertNotEquals(noNameNoImage.getImagePath(), "NONE");
	}

	@Test
	void testSetImagePath_emptyString() {
		assertThrows(IllegalArgumentException.class, () -> {
			whole.setImagePath("");			
		});
		assertEquals(whole.getImagePath(), "sunwukong.png");
	}

	@Test
	void testSetImagePath_multipleUpdates() {
		whole.setImagePath("first.png");
		whole.setImagePath("second.png");
		assertEquals(whole.getImagePath(), "second.png");
	}

	// --- equals ---

	@Test
	void testEquals_sameObject() {
		assertTrue(whole.equals(whole));
	}

	@Test
	void testEquals_equalElements() {
		Element a = new Element(false, "monkey king", "sunwukong.png");
		assertTrue(whole.equals(a));
	}

	@Test
	void testEquals_differentName() {
		Element a = new Element(false, "nezha", "sunwukong.png");
		assertFalse(whole.equals(a));
	}

	@Test
	void testEquals_differentImagePath() {
		Element a = new Element(false, "monkey king", "other.png");
		assertFalse(whole.equals(a));
	}

	@Test
	void testEquals_differentRankedStatus() {
		Element a = new Element(true, "monkey king", "sunwukong.png");
		assertFalse(whole.equals(a));
	}

	@Test
	void testEquals_null() {
		assertFalse(whole.equals(null));
	}

	@Test
	void testEquals_differentType() {
		assertFalse(whole.equals("monkey king"));
	}

	@Test
	void testEquals_twoDefaultElements() {
		Element a = new Element(false);
		Element b = new Element(false);
		assertTrue(a.equals(b));
	}

	// --- hashCode ---

	@Test
	void testHashCode_equalObjectsSameHash() {
		Element a = new Element(false, "monkey king", "sunwukong.png");
		assertEquals(whole.hashCode(), a.hashCode());
	}

	@Test
	void testHashCode_sameObjectConsistent() {
		assertEquals(whole.hashCode(), whole.hashCode());
	}

	@Test
	void testHashCode_differentObjectsDifferentHash() {
		// Not guaranteed by contract, but expected for distinct elements
		assertNotEquals(whole.hashCode(), noImage.hashCode());
	}

	// --- toString ---

	@Test
	void testToString_ranked() {
		assertEquals(immortal.toString(), "immortal: ranked");
	}

	@Test
	void testToString_notRanked() {
		assertEquals(whole.toString(), "monkey king: not ranked");
	}

	@Test
	void testToString_defaultElement() {
		assertEquals(noNameNoImage.toString(), "element: not ranked");
	}

	@Test
	void testToString_afterSetRanked() {
		whole.changeTo(true);
		assertEquals(whole.toString(), "monkey king: ranked");
	}

	@Test
	void testToString_afterSetName() {
		whole.setName("pigsy");
		assertEquals(whole.toString(), "pigsy: not ranked");
	}

	// --- constants ---

	@Test
	void testDefaultNameConstant() {
		assertEquals(Element.DEFAULT_ELEMENT_NAME, "element");
	}

	@Test
	void testDefaultImagePathConstant() {
		assertEquals(Element.DEFAULT_ELEMENT_IMAGE_PATH, "NONE");
	}
}