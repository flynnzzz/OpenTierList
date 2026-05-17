package tests.model;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.ListTierElement;
import model.Tier;
import model.TierElement;
import model.TierElementUnranked;
import model.TierHeader;
import model.TierList;
import model.enums.TierElementStatus;
import model.exceptions.TierNotFoundException;

class TierListTest {
	private TierList emptyCtor, fullCtor, essentialsCtor, unrankedOnlyCtor, tierlist;
	private static String name;
	private static ListTierElement unranked;
	private static List<Tier> tiers;
	private static List<Tier> extraTiers;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		name = "My Epic Tier List";
		unranked = new ListTierElement(
				List.of(
					new TierElementUnranked("Jack Frost"),
					new TierElementUnranked("Pyro Jack"),
					new TierElementUnranked("Decarabia"),
					new TierElementUnranked("Mokoi"),
					new TierElementUnranked("Sagi Mitama"),
					new TierElementUnranked("Shiisa")
				)
			);
		tiers = new ArrayList<>(
				List.of(
					new Tier(new TierHeader("S", Color.RED)),
					new Tier(new TierHeader("A", Color.ORANGE)),
					new Tier(new TierHeader("B", Color.YELLOW)),
					new Tier(new TierHeader("C", Color.GREEN)),
					new Tier()
				)
			);
		
		extraTiers = new ArrayList<>(
				List.of(
						new Tier(new TierHeader("D", Color.CYAN)),
						new Tier(new TierHeader("E", Color.BLUE)),
						new Tier(new TierHeader("F", Color.MAGENTA))
					)
			);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		name = null;
		unranked = null;
		tiers = null;
	}

	@BeforeEach
	void setUp() throws Exception {
		tierlist = new TierList(name, unranked, tiers);
	}

	@AfterEach
	void tearDown() throws Exception {
		tierlist = null;
	}

	@Test
	void testCtors() {
		emptyCtor = new TierList();
		assertEquals(emptyCtor.getTierListName(), TierList.DEFAULT_TIERLIST_NAME);
		assertEquals(emptyCtor.getUnranked(), new ListTierElement());
		
		fullCtor = new TierList(name, unranked, tiers);
		assertEquals(fullCtor.getTierListName(), name);
		assertEquals(fullCtor.getUnranked(), unranked);
		
		essentialsCtor = new TierList(name, unranked);
		assertEquals(essentialsCtor.getTierListName(), name);
		assertEquals(essentialsCtor.getUnranked(), unranked);

		unrankedOnlyCtor = new TierList(unranked);
		assertEquals(unrankedOnlyCtor.getUnranked(), unranked);
		
		assertThrows(IllegalArgumentException.class, () -> new TierList(""));
		assertThrows(NullPointerException.class, () -> new TierList(null, null));
	}
	
	@Test 
	void testAddRemoveTier() {
		tierlist.addTier(extraTiers.get(0));
		assertEquals(tierlist.indexOf(extraTiers.get(0)), 5);

		tierlist.removeTier(5);
		assertThrows(IndexOutOfBoundsException.class ,() -> tierlist.indexOf(extraTiers.get(0)));

		assertThrows(TierNotFoundException.class ,
				() -> tierlist.removeTier(new Tier(new TierHeader("000", Color.RED))));
		
		assertThrows(TierNotFoundException.class ,
				() -> tierlist.removeTier(extraTiers.get(1)));
	}
	
	@Test 
	void testAddToRemoveFromUnranked() {
		int initialLenght = tierlist.getUnranked().size();
		tierlist.addToUnranked(new TierElementUnranked("Sexo"));
		assertTrue(tierlist.getUnranked().size() == initialLenght + 1);
		var real = new TierElement("Real");
		real = tierlist.addToUnranked(real);
		assertEquals(real.status(), TierElementStatus.UNRANKED);
		real = tierlist.removeFromUnranked(real);
		assertEquals(real.status(), TierElementStatus.RANKED);
	}

}
