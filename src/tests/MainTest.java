package tests;

import java.awt.Color;
import static model.enums.TierElementStatus.*;

import controller.StandardTierListController;
import controller.TierListController;
import model.Tier;
import model.TierElement;
import model.TierHeader;
import model.TierList;
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
			tlc.rank(new TierElement(UNRANKED, "Goku"), tl.indexOf(s));
			tlc.rank(new TierElement(UNRANKED, "Wukong"), tl.indexOf(a));
			tlc.rank(new TierElement(UNRANKED, "Jinwoo"), tl.indexOf(a));
			IO.println(tl.toString(TierStringFormat.EXTENDED));
			
			tlc.swapTiers(0, 1);
			tlc.addTier(new Tier(new TierHeader("S", Color.ORANGE)));
			tlc.addToUnranked(new TierElement("Ayanokoji"));
			tlc.addToUnranked(new TierElement("Ringo Star"));
			
			IO.println(tl);
			
		}
}
