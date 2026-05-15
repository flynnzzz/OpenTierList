package tests.model;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.TierElementRanked;
import model.TierElementUnranked;
import model.Tier;
import model.TierElement;
import model.TierElementList;
import model.TierHeader;
import model.exceptions.ElementNotFoundException;

class TierTest {
	private static Tier emptyCtor, fullCtor, onlyHeaderCtor;
	private Tier tier;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		TierHeader header = new TierHeader("S", Color.RED);
		TierElementList elements = new TierElementList(List.of(new TierElementRanked(),
								   new TierElementRanked(),
								   new TierElementRanked()));
		emptyCtor = new Tier();
		onlyHeaderCtor = new Tier(header);
		fullCtor = new Tier(header, elements);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		emptyCtor = null;
		onlyHeaderCtor = null;
		fullCtor = null;
	}

	@BeforeEach
	void setUp() throws Exception {
		TierHeader header = new TierHeader("A", Color.BLUE);
		TierElement e1 = new TierElementRanked("Jumbe"),
					e2 = new TierElementRanked("Nomi"),
					e3 = new TierElementRanked("Breek");
		TierElementList elements = new TierElementList(List.of(e1, e2, e3));
		tier = new Tier(header, elements);
	}

	@AfterEach
	void tearDown() throws Exception {
		tier = null;
	}

	@Test
	void testCtors() {
		TierElementList elements = new TierElementList(List.of(new TierElementRanked(),
								   new TierElementRanked(),
								   new TierElementRanked()));
		assertEquals(emptyCtor.getHeader(), new TierHeader(Tier.DEFAULT_TIER_NAME, Tier.DEFAULT_TIER_COLOR));
		assertEquals(emptyCtor.getElements(), new TierElementList() );
		assertEquals(onlyHeaderCtor.getHeader(), new TierHeader("S", Color.RED));
		assertEquals(onlyHeaderCtor.getElements(), new TierElementList() );
		assertEquals(fullCtor.getHeader(), new TierHeader("S", Color.RED));
		assertEquals(fullCtor.getElements(), elements);
	}
	
	@Test
	void testAdd() {
		TierElement e1 = new TierElementRanked("Jumbe"); 
		
		assertThrows(IllegalArgumentException.class, () -> tier.add(e1));
		tier.add(new TierElementRanked());
		assertEquals(tier.getElements().size(), 4);
	}
	
	@Test
	void testMoveThrows() {
		TierElement e1 = new TierElementRanked("Jumbe"),
					e2 = new TierElementRanked("Nomi"), 
					e3 = new TierElementRanked("Breek");
		
		tier.moveTo(2, e1);
		assertEquals(tier.getElements().size(), 3);
		assertEquals(tier.getElements().getLast(), e1);
		assertThrows(IndexOutOfBoundsException.class, () -> tier.moveTo(5, e1));
		tier.moveTo(1, e1);
		assertEquals(tier.getElements().get(1), e1);
		assertEquals(tier.getElements().getFirst(), e2);
		assertEquals(tier.getElements().getLast(), e3);
		tier.moveTo(1, e2);
		assertEquals(tier.getElements().get(1), e2);
		assertEquals(tier.getElements().getFirst(), e1);
		// TODO: assertThrows(ElementNotFoundException.class, ...);
	}
	
	@Test
	void testMoveForwards() {
		TierElement e1 = new TierElementRanked("Jumbe"),
				e2 = new TierElementRanked("Nomi"), 
				e3 = new TierElementRanked("Breek"),
				e4 = new TierElementRanked("Lyffu"), 
				e5 = new TierElementRanked("Ruben"), 
				e6 = new TierElementRanked("Chipper");
		
		tier.add(e4); tier.add(e5); tier.add(e6);
		
		tier.moveTo(0, e1);
		assertEquals(tier.getElements().get(0), e1);
		tier.moveTo(5, e1);
		assertEquals(tier.getElements().get(0), e2);
		assertEquals(tier.getElements().get(1), e3);
		assertEquals(tier.getElements().get(2), e4);
		assertEquals(tier.getElements().get(3), e5);
		assertEquals(tier.getElements().get(4), e6);
		assertEquals(tier.getElements().get(5), e1);
	}
	
	@Test
	void testMoveBackwards() {
		TierElement e1 = new TierElementRanked("Jumbe"),
					e2 = new TierElementRanked("Nomi"), 
					e3 = new TierElementRanked("Breek"),
					e4 = new TierElementRanked("Lyffu"), 
					e5 = new TierElementRanked("Ruben"), 
					e6 = new TierElementRanked("Chipper");
		
		tier.add(e4); tier.add(e5); tier.add(e6);
		
		tier.moveTo(5, e6);
		assertEquals(tier.getElements().get(5), e6);
		tier.moveTo(0, e6);
		assertEquals(tier.getElements().get(5), e5);
		assertEquals(tier.getElements().get(4), e4);
		assertEquals(tier.getElements().get(3), e3);
		assertEquals(tier.getElements().get(2), e2);
		assertEquals(tier.getElements().get(1), e1);
		assertEquals(tier.getElements().get(0), e6);
		tier.moveTo(3, e5);
		assertEquals(tier.getElements().get(5), e4);
		assertEquals(tier.getElements().get(4), e3);
		assertEquals(tier.getElements().get(3), e5);
	}
	
	@Test
	
	void testRemove() {
		TierElement e1 = new TierElementRanked("Jumbe"),
					e2 = new TierElementRanked("Nomi"), 
					e3 = new TierElementRanked("Breek");
		
		assertEquals(tier.remove(0), e1);
		assertTrue(tier.remove(e2));
		assertThrows(ElementNotFoundException.class, () -> tier.remove(e1));
		assertThrows(ElementNotFoundException.class, () -> tier.remove(new TierElementRanked()));
		assertThrows(IndexOutOfBoundsException.class, () -> tier.remove(10));
	}

}
