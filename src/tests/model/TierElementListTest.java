package tests.model;

import static org.junit.jupiter.api.Assertions.*;
import static model.enums.TierElementStatus.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.TierElement;
import model.TierElementList;

class TierElementListTest {
	private static TierElementList emptyCtor, collectionCtor;
	private TierElementList elements;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		emptyCtor = new TierElementList();
		TierElement t1 = new TierElement(RANKED, "Kuririn"),
				t2 = new TierElement(UNRANKED, "Furiza"),
				t3 = new TierElement(UNRANKED, "Bejeeta");
		collectionCtor = new TierElementList(List.of(t1, t2, t3));
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		emptyCtor = null;
		collectionCtor = null;
	}

	@BeforeEach
	void setUp() throws Exception {
		TierElement t1 = new TierElement(RANKED, "Kuririn"),
				t2 = new TierElement(UNRANKED, "Furiza"),
				t3 = new TierElement(UNRANKED, "Bejeeta");
		elements = new TierElementList();
		elements.add(t1);
		elements.add(t2);
		elements.add(t3);
	}

	@AfterEach
	void tearDown() throws Exception {
		elements = null;
	}

	@Test
	void testCtors() {
		assertTrue(emptyCtor.size() == 0);
		assertEquals(emptyCtor.toString(), "[  ]");
		assertTrue(collectionCtor.size() == 3);
		assertEquals(collectionCtor.toString(), "[ Kuririn: ranked, Furiza: not ranked, Bejeeta: not ranked. ]");
	}
	
	@Test
	void testToString() {
		assertEquals(elements.toString(), "[ Kuririn: ranked, Furiza: not ranked, Bejeeta: not ranked. ]");
	}

}
