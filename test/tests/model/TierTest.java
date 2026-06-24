package tests.model;

import static org.junit.jupiter.api.Assertions.*;
import static model.enums.TierElementStatus.*;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.exceptions.ElementNotFoundException;
import model.models.Tier;
import model.models.TierElement;
import model.models.TierHeader;

class TierTest {
	private Tier emptyCtor, fullCtor, onlyHeaderCtor, tier;
	private static TierHeader header;
	private static List<TierElement> elements;
	private static TierElement e1, e2, e3, e4, e5, e6;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		header = new TierHeader("S", Color.RED);
		e1 = new TierElement(RANKED, "Jumbe");
		e2 = new TierElement(RANKED, "Nomi");
		e3 = new TierElement(RANKED, "Breek");
		e4 = new TierElement(RANKED, "Lyffu");
		e5 = new TierElement(RANKED, "Ruben"); 
		e6 = new TierElement(RANKED, "Chipper");
		elements = new ArrayList<TierElement>(List.of(new TierElement(RANKED),
				   new TierElement(RANKED),
				   new TierElement(RANKED)));
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		header = null;
		elements = null;
		e1 = null;
		e2 = null;
		e3 = null;
	}

	@BeforeEach
	void setUp() throws Exception {
		tier = new Tier(header, new ArrayList<TierElement>(List.of(e1, e2, e3)));
	}

	@AfterEach
	void tearDown() throws Exception {
		tier = null;
	}

	@Test
	void testCtors() {
		emptyCtor = new Tier();
		onlyHeaderCtor = new Tier(header);
		fullCtor = new Tier(header, elements);
		assertEquals(emptyCtor.getHeader(), new TierHeader(Tier.DEFAULT_TIER_NAME, Tier.DEFAULT_TIER_COLOR));
		assertEquals(emptyCtor.getElements(), new ArrayList<TierElement>() );
		assertEquals(onlyHeaderCtor.getHeader(), new TierHeader("S", Color.RED));
		assertEquals(onlyHeaderCtor.getElements(), new ArrayList<TierElement>() );
		assertEquals(fullCtor.getHeader(), new TierHeader("S", Color.RED));
		assertEquals(fullCtor.getElements(), elements);
	}
	
	@Test
	void testAdd() {
		assertThrows(IllegalArgumentException.class, () -> tier.add(e1));
		tier.add(new TierElement(RANKED));
		assertEquals(tier.getElements().size(), 4);
	}
	
	@Test
	void testMoveThrows() {
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
		
		assertThrows(ElementNotFoundException.class, 
				() -> tier.moveTo(0, new TierElement(RANKED)));
	}
	
	@Test
	void testMoveForwards() {
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
		assertEquals(tier.remove(0), e1);
		assertTrue(tier.remove(e2));
		assertThrows(ElementNotFoundException.class, () -> tier.remove(e1));
		assertThrows(ElementNotFoundException.class, () -> tier.remove(new TierElement(RANKED)));
		assertThrows(IndexOutOfBoundsException.class, () -> tier.remove(10));
	}
	
	@Test
	void testSwap() {
		assertEquals(tier.getElements().get(0), e1);
		assertEquals(tier.getElements().get(1), e2);
		tier.swap(e1, e2);
		assertEquals(tier.getElements().get(0), e2);
		assertEquals(tier.getElements().get(1), e1);
		assertThrows(IndexOutOfBoundsException.class,
				() -> tier.swap(new TierElement(RANKED), e1));
	}

}
