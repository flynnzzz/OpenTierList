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
			tiers.add(DefaultTier.S.value()); tiers.add(DefaultTier.A.value()); tiers.add(DefaultTier.B.value()); 
			tiers.add(DefaultTier.C.value()); tiers.add(DefaultTier.D.value()); tiers.add(DefaultTier.E.value());
			
			var controller = TierListController.of(new TierList("My Epic Tier List", unranked, tiers));

			controller.rank(unranked.get(0), tiers.indexOf(DefaultTier.S.value()));
			controller.rank(unranked.get(0), tiers.indexOf(DefaultTier.A.value()));
			controller.rank(unranked.get(0), tiers.indexOf(DefaultTier.A.value()));
			controller.rank(unranked.get(0), tiers.indexOf(DefaultTier.A.value()));
			
			IO.println(controller.toString(TierStringFormat.COMPACT));
			
			controller.moveTo(m, tiers.indexOf(DefaultTier.A.value()), 0);
			controller.unrank(0, p, tiers.indexOf(DefaultTier.A.value()));

			IO.println(controller.toString(TierStringFormat.COMPACT));
			
			controller.swapTiers(tiers.indexOf(DefaultTier.A.value()), tiers.indexOf(DefaultTier.S.value()));
			controller.swapTiers(tiers.indexOf(DefaultTier.A.value()), tiers.indexOf(DefaultTier.S.value()));
			
			controller.swapTierElements(tiers.indexOf(DefaultTier.A.value()), s, g);
			IO.println(controller.toString(TierStringFormat.COMPACT));
			controller.swapTierElements(tiers.indexOf(DefaultTier.A.value()), m, s);
			
			controller.removeFromUnranked(a);
			controller.removeFromUnranked(o);
			
			IO.println(controller.toString(TierStringFormat.COMPACT));
		}
}
