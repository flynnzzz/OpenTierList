package tests;

import java.awt.Color;

import controller.StandardTierListController;
import controller.TierListController;
import model.TierElement;
import model.Tier;
import model.TierHeader;
import model.TierList;
import model.TierElementUnranked;
import static model.enums.TierElementStatus.*;
import model.enums.TierStringFormat;

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
			tlc.rank(new TierElement(RANKED, "Goku"), tl.indexOf(s));
			tlc.rank(new TierElement(RANKED, "Wukong"), tl.indexOf(a));
			tlc.rank(new TierElement(RANKED, "Jinwoo"), tl.indexOf(a));
			IO.println(tl.toString(TierStringFormat.EXTENDED));
			
			tlc.swapTiers(0, 1);
			tlc.addTier(new Tier(new TierHeader("S", Color.ORANGE)));
			tlc.addToUnranked(new TierElementUnranked("Ayanokoji"));
			tlc.addToUnranked(new TierElementUnranked("Ringo Star"));
			
			IO.println(tl);
			
			tlc.addTier(null);
		}
}
