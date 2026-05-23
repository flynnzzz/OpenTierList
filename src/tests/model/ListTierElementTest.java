package tests.model;

import static org.junit.jupiter.api.Assertions.*;
import static model.enums.TierElementStatus.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.models.ListTierElement;
import model.models.TierElement;

class ListTierElementTest {
	private ListTierElement emptyCtor, collectionCtor, elements;
	private TierElement t1 = new TierElement(RANKED, "Kuririn"),
				t2 = new TierElement(UNRANKED, "Furiza"),
				t3 = new TierElement(UNRANKED, "Bejeeta");
	
	@BeforeEach
	void setUp() throws Exception {
		elements = new ListTierElement( List.of(t1, t2, t3) );
	}

	@AfterEach
	void tearDown() throws Exception {
		elements = null;
	}

	@Test
	void testCtors() {
		emptyCtor = new ListTierElement();
		collectionCtor = new ListTierElement( List.of(t3, t2, t1) );
		assertTrue(emptyCtor.size() == 0);
		assertEquals(emptyCtor.toString(), "[  ]");
		assertTrue(collectionCtor.size() == 3);
	}
	
	@Test
	void testToString() {
		assertEquals(elements.toString(), "[ Kuririn, Furiza, Bejeeta. ]");
	}

}
