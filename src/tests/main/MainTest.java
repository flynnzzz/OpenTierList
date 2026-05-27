package tests.main;

import java.util.ArrayList;
import java.util.List;

import controller.controllers.TierListController;
import model.enums.DefaultTier;
import model.enums.TierStringFormat;
import model.models.ListTierElement;
import model.models.Tier;
import model.models.TierElement;
import model.models.TierList;

public class MainTest {
		public static void main() {
			var unranked = new ListTierElement();
			TierElement m, p, s, g, o, a;
			unranked.add(m = new TierElement("Mookka")); unranked.add(p = new TierElement("Pehkura")); unranked.add(s = new TierElement("Suynoh"));
			unranked.add(g = new TierElement("Galeena")); unranked.add(o = new TierElement("Okha")); unranked.add(a = new TierElement("Aseeno"));
			List<Tier> tiers = new ArrayList<>();
			
			Tier S, A, B, C, D, E, S_Double, S_Triple;
			tiers.add(S = DefaultTier.S.value()); 
			
			tiers.add(A = DefaultTier.A.value()); tiers.add(B = DefaultTier.B.value()); 
			tiers.add(C = DefaultTier.C.value()); tiers.add(D = DefaultTier.D.value()); tiers.add(E = DefaultTier.E.value());
			
			tiers.add(S_Double = new Tier("S"));
			tiers.add(S_Triple = new Tier("S"));
			
			var controller = TierListController.of(new TierList("My Epic Tier List", unranked, tiers));

			controller.moveUnranked(m, a);

			controller.rank(unranked.get(0), S);
			controller.rank(unranked.get(0), S_Double);
			controller.rank(unranked.get(0), A);
			controller.rank(unranked.get(0), A);
			controller.rank(unranked.get(0), A);
			
			
			IO.println(controller.toString(TierStringFormat.COMPACT));
			
			controller.moveTo(m, A, 0);
			controller.unrank(0, p);

			IO.println(controller.toString(TierStringFormat.COMPACT));
			
			controller.swapTiers(A, S);
			controller.swapTiers(A, S);
			
			controller.swapTierElements(A, s, g);
			IO.println(controller.toString(TierStringFormat.COMPACT));
			controller.swapTierElements(A, m, s);
			
			controller.removeFromUnranked(a);
			controller.removeFromUnranked(o);
			
			IO.println(controller.toString(TierStringFormat.COMPACT));
		}
}
