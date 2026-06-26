package tests.model;

import static org.junit.jupiter.api.Assertions.*;

import static model.enums.TelementStatus.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.models.Telement;


/* DEPRECATED TESTS, NEED REWORK */

/*
class TierElementTest {
	private static TierElement immortal;
	private TierElement whole, noImage, noNameNoImage;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		immortal = new TierElement(RANKED, "immortal", "wheresomniman.jpg");
		
	}

	@AfterAll
	static void __tearDownAfterClass__() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		whole = new TierElement(UNRANKED, "monkey king", "sunwukong.png");
		noImage = new TierElement(UNRANKED, "makoto yuki");
		noNameNoImage = new TierElement(UNRANKED);
	}

	@AfterEach
	void tearDown() throws Exception {
		whole = null;
		noImage = null;
		noNameNoImage = null;
	}

	@Test
	void testConstructors() {

		assertTrue(immortal.status().value());
		assertFalse(whole.status().value());
		assertFalse(noImage.status().value());
		assertFalse(noNameNoImage.status().value());

		assertEquals(immortal.getName(), "immortal");
		assertEquals(whole.getName(), "monkey king");
		assertEquals(noImage.getName(), "makoto yuki");
		assertEquals(noNameNoImage.getName(), "element");
		assertNotEquals(noNameNoImage.getName(), "some name");
	}


	@Test
	void testSetRanked_trueToFalse() {
		immortal.changeTo(UNRANKED);
		assertFalse(immortal.status().value());
		immortal.changeTo(RANKED);
	}

	@Test
	void testSetRanked_falseToTrue() {
		whole.changeTo(RANKED);
		assertTrue(whole.status().value());
	}

	@Test
	void testSetRanked_noChange() {
		whole.changeTo(UNRANKED);
		assertFalse(whole.status().value());
	}


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

	@Test
	void testEquals_sameObject() {
		assertTrue(whole.equals(whole));
	}

	@Test
	void testEquals_differentName() {
		TierElement a = new TierElement(UNRANKED, "nezha", "sunwukong.png");
		assertFalse(whole.equals(a));
	}

	@Test
	void testEquals_differentImagePath() {
		TierElement a = new TierElement(UNRANKED, "monkey king", "other.png");
		assertFalse(whole.equals(a));
	}

	@Test
	void testEquals_differentRankedStatus() {
		TierElement a = new TierElement(RANKED, "monkey king", "sunwukong.png");
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
	void testHashCode_sameObjectConsistent() {
		assertEquals(whole.hashCode(), whole.hashCode());
	}

	@Test
	void testHashCode_differentObjectsDifferentHash() {
		// Not guaranteed by contract, but expected for distinct elements
		assertNotEquals(whole.hashCode(), noImage.hashCode());
	}

	@Test
	void testToString_ranked() {
		assertEquals(immortal.toString(), "immortal");
	}

	@Test
	void testToString_notRanked() {
		assertEquals(whole.toString(), "monkey king");
	}

	@Test
	void testToString_defaultElement() {
		assertEquals(noNameNoImage.toString(), "element");
	}

	@Test
	void testToString_afterSetRanked() {
		whole.changeTo(RANKED);
		assertEquals(whole.toString(), "monkey king");
	}

	@Test
	void testToString_afterSetName() {
		whole.setName("pigsy");
		assertEquals(whole.toString(), "pigsy");
	}

	@Test
	void testDefaultNameConstant() {
		assertEquals(TierElement.DEFAULT_ELEMENT_NAME, "element");
	}
} 
*/