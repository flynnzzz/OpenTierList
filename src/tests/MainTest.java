package tests;

import java.awt.Color;

import controller.StandardTierListController;
import controller.TierListController;
import model.Element;
import model.Tier;
import model.TierHeader;
import model.TierList;
import model.UnrankedElement;

public class MainTest {
		public static void main() {
			TierList tl = new TierList();
			TierListController tlc = new StandardTierListController(tl);
			
			Tier s = new Tier(new TierHeader("S", Color.ORANGE)),
				a = new Tier(new TierHeader("A", Color.ORANGE)),
				b = new Tier(new TierHeader("B", Color.ORANGE));
			
			tlc.addTier(s);
			tlc.addTier(a);
			tlc.addTier(b);
			tlc.addTier(new Tier());
			tlc.rank(new Element(true, "Goku"), s);
			tlc.rank(new Element(true, "Wukong"), a);
			tlc.rank(new Element(true, "Jinwoo"), a);
			IO.println(tl);
			
			tlc.swapTiers(0, 1);
			tlc.addTier(new Tier(new TierHeader("S", Color.ORANGE)));
			tlc.addUnranked(new UnrankedElement("Ayanokoji"));
			tlc.addUnranked(new UnrankedElement("Ringo Star"));
			
			IO.println(tl);
		}
}
