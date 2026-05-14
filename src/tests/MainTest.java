package tests;

import java.awt.Color;

import controller.StandardTierListController;
import controller.TierListController;
import enums.TierStringFormat;
import model.TierElement;
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
			tlc.rank(new TierElement(true, "Goku"), tl.indexOf(s));
			tlc.rank(new TierElement(true, "Wukong"), tl.indexOf(a));
			tlc.rank(new TierElement(true, "Jinwoo"), tl.indexOf(a));
			IO.println(tl.toString(TierStringFormat.EXTENDED));
			
			tlc.swapTiers(0, 1);
			tlc.addTier(new Tier(new TierHeader("S", Color.ORANGE)));
			tlc.addUnranked(new UnrankedElement("Ayanokoji"));
			tlc.addUnranked(new UnrankedElement("Ringo Star"));
			
			IO.println(tl);
		}
}
