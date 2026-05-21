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
import model.TierHeader;
import model.TierList;
import model.enums.TierElementStatus;
import model.exceptions.ElementNotFoundException;
import model.exceptions.TierNotFoundException;

class TierListTest {
	private TierList emptyCtor, fullCtor, essentialsCtor, unrankedOnlyCtor, tierlist;
	private static String name;
	private ListTierElement unranked;
	private List<Tier> tiers;
	private List<Tier> extraTiers;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		name = "My Epic Tier List";
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		name = null;
	}

	@BeforeEach
	void setUp() throws Exception {
		unranked = new ListTierElement(
				List.of(
					new TierElement("Jack Frost"),
					new TierElement("Pyro Jack"),
					new TierElement("Decarabia"),
					new TierElement("Mokoi"),
					new TierElement("Sagi Mitama"),
					new TierElement("Shiisa")
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
		tierlist = new TierList(name, unranked, new ArrayList<Tier>(tiers));
	}

	@AfterEach
	void tearDown() throws Exception {
		tierlist = null;
		unranked = null;
		tiers = null;
		extraTiers = null;
	}

	@Test
	void testCtors() {
		emptyCtor = new TierList();
		assertEquals(emptyCtor.getTierListName(), TierList.DEFAULT_TIERLIST_NAME);
		assertEquals(emptyCtor.getUnranked(), List.of());
		
		fullCtor = new TierList(name, unranked, tiers);
		assertEquals(fullCtor.getTierListName(), name);
		assertEquals(fullCtor.getUnranked(), unranked);
		
		essentialsCtor = new TierList(name, unranked);
		assertEquals(essentialsCtor.getTierListName(), name);
		assertEquals(essentialsCtor.getUnranked(), unranked);

		unrankedOnlyCtor = new TierList(unranked);
		assertEquals(unrankedOnlyCtor.getUnranked(), unranked);
		
		var nameOnlyCtor = new TierList(name);
		assertEquals(nameOnlyCtor.getTierListName(), name);
		assertEquals(nameOnlyCtor.getUnranked(), List.of());
		
		
		assertThrows(IllegalArgumentException.class, () -> new TierList(""));
		assertThrows(NullPointerException.class, () -> new TierList(null, null));
	}
	
	@Test 
	void testAddRemoveTier() {
		tierlist.addTier(extraTiers.get(0));
		assertEquals(tierlist.indexOf(extraTiers.get(0)), 5);

		tierlist.removeTier(5);
		assertThrows(IndexOutOfBoundsException.class , () -> tierlist.indexOf(extraTiers.get(0)));

		assertThrows(TierNotFoundException.class ,
				() -> tierlist.removeTier(new Tier(new TierHeader("000", Color.RED))));
		
		assertThrows(TierNotFoundException.class ,
				() -> tierlist.removeTier(extraTiers.get(1)));

		tierlist.addTier(extraTiers.get(0));
		tierlist.removeTier(extraTiers.get(0));
		assertEquals(tierlist.size(), 5);
		
		tierlist.addTier(extraTiers.get(0));
		tierlist.addTier(extraTiers.get(1));
		tierlist.addTier(extraTiers.get(0));
		tierlist.removeTier(extraTiers.get(0));
		assertEquals(tierlist.indexOf(extraTiers.get(0)), 6);
	}
	
	@Test 
	void testAddToRemoveFromUnranked() {
		int initialLenght = tierlist.getUnranked().size();
		tierlist.addToUnranked(new TierElement("Sexo"));
		assertTrue(tierlist.getUnranked().size() == initialLenght + 1);
		
		var real = new TierElement("Real");
		tierlist.addToUnranked(real);
		assertEquals(real.status(), TierElementStatus.UNRANKED);
		assertTrue(tierlist.getUnranked().size() == initialLenght + 2);

		tierlist.removeFromUnranked(real);
		assertTrue(tierlist.getUnranked().size() == initialLenght + 1);
		assertEquals(real.status(), TierElementStatus.RANKED);
		
		assertThrows(ElementNotFoundException.class ,
				() -> tierlist.removeFromUnranked(new TierElement("N/A")));
		assertThrows(NullPointerException.class ,
				() -> tierlist.removeFromUnranked(null));
		assertTrue(tierlist.getUnranked().size() == initialLenght + 1);
	}
	
	@Test
	void testAddToRemoveFromTier() {
		var newTier = new Tier("Z");
		tierlist.addTier(newTier);
		int initialLenght = newTier.getElements().size(), zTierIndex = tierlist.indexOf(newTier);
		tierlist.addToTier(zTierIndex, new TierElement("Sexo"));
		assertTrue(newTier.getElements().size() == initialLenght + 1);
		
		var real = new TierElement("Real");
		tierlist.addToTier(zTierIndex, real);
		assertTrue(newTier.getElements().size() == initialLenght + 2);
		
		tierlist.removeFromTier(zTierIndex, real);
		assertTrue(newTier.getElements().size() == initialLenght + 1);
		assertThrows(ElementNotFoundException.class ,
				() -> tierlist.removeFromTier(zTierIndex, new TierElement("N/A")));
		assertThrows(NullPointerException.class ,
				() -> tierlist.removeFromTier(zTierIndex, null));
		assertTrue(newTier.getElements().size() == initialLenght + 1);
		assertThrows(TierNotFoundException.class ,
				() -> tierlist.addToTier(-1, real));
	}
	
	@Test
	void swapTiersTests() {
		tierlist.addToTier(0, tierlist.getUnranked().get(0));
		tierlist.addToTier(1, tierlist.getUnranked().get(1));
		tierlist.addToTier(2, tierlist.getUnranked().get(2));
		tierlist.addToTier(3, tierlist.getUnranked().get(3));
		assertEquals(tierlist.indexOf(tiers.get(0)), 0);
		tierlist.swapTiers(0, 1);
		assertEquals(tierlist.indexOf(tiers.get(0)), 1);
		tierlist.swapTiers(0, 0);
		assertEquals(tierlist.indexOf(tiers.get(1)), 0);
		tierlist.swapTiers(0, 1);
		assertThrows(TierNotFoundException.class, () -> tierlist.swapTiers(0, 100));
		assertThrows(TierNotFoundException.class, () -> tierlist.swapTiers(-1, 100));
		assertThrows(TierNotFoundException.class, () -> tierlist.swapTiers(-1, 0));
	}
	
	@Test
	void swapTierElementsTests() {
		tierlist.addToTier(0, tierlist.getUnranked().get(0));
		tierlist.addToTier(0, tierlist.getUnranked().get(1));
		tierlist.addToTier(1, tierlist.getUnranked().get(1));

		assertEquals(tierlist.getTiers().get(0).getElements().get(0), unranked.get(0));
		assertEquals(tierlist.getTiers().get(0).getElements().get(1), unranked.get(1));

		tierlist.swapElements(0, unranked.get(0), unranked.get(1));
		
		assertEquals(tierlist.getTiers().get(0).getElements().get(0), unranked.get(1));
		assertEquals(tierlist.getTiers().get(0).getElements().get(1), unranked.get(0));

		tierlist.swapElements(1, unranked.get(1), unranked.get(1));
		assertEquals(tierlist.getTiers().get(1).getElements().get(0), unranked.get(1));

		assertThrows(ElementNotFoundException.class,
				() -> tierlist.swapElements(0, new TierElement("foo"), new TierElement("fee")));
		assertThrows(TierNotFoundException.class, 
				() -> tierlist.swapElements(-1, new TierElement("foo"), new TierElement("foo")));
	}
	
	@Test
	void swapUnrankedTierElementsTests() {
		var unrankedCopy = List.copyOf(unranked);
		assertEquals(tierlist.getUnranked().get(0), unrankedCopy.get(0));
		assertEquals(tierlist.getUnranked().get(1), unrankedCopy.get(1));

		tierlist.swapUnranked(unranked.get(0), unranked.get(1));
		
		assertEquals(tierlist.getUnranked().get(0), unrankedCopy.get(1));
		assertEquals(tierlist.getUnranked().get(1), unrankedCopy.get(0));

		tierlist.swapUnranked(unranked.get(1), unranked.get(0));
		
		assertEquals(tierlist.getUnranked().get(0), unrankedCopy.get(0));
		assertEquals(tierlist.getUnranked().get(1), unrankedCopy.get(1));
		assertThrows(ElementNotFoundException.class,
				() -> tierlist.swapUnranked(new TierElement("foo"), new TierElement("fee")));
		assertThrows(ElementNotFoundException.class, 
				() -> tierlist.swapUnranked(new TierElement("y"), new TierElement("foo")));
	}
	
	@Test
	void moveToTest() {
		tierlist.addToTier(0, unranked.get(0));
		tierlist.addToTier(0, unranked.get(1));
		tierlist.addToTier(1, unranked.get(2));
		tierlist.addToTier(1, unranked.get(3));
		tierlist.addToTier(2, unranked.get(4));
		
		tierlist.moveFromTierToTier(0, unranked.get(4));
		
		assertTrue(tierlist.getTiers().get(2).getElements().isEmpty());
		assertEquals(tierlist.getTiers().get(0).getElements().size(), 3);
		
		tierlist.moveFromTierToTier(2, unranked.get(4));

		tierlist.moveFromTierToTier(1, unranked.get(0));
		
		assertEquals(tierlist.getTiers().get(0).getElements().size(), 1);
		assertEquals(tierlist.getTiers().get(0).getElements(), List.of(unranked.get(1)));
		assertEquals(tierlist.getTiers().get(1).getElements().size(), 3);
		assertEquals(tierlist.getTiers().get(1).getElements(), List.of(unranked.get(2), unranked.get(3), unranked.get(0)));
	}
	
	@Test 
	void rankTest() {
		
	}
	
}
