package net.flynn.opentierlist.persistence;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import net.flynn.opentierlist.controller.*;
import net.flynn.opentierlist.model.enums.DefaultTier;
import net.flynn.opentierlist.model.models.Telement;
import net.flynn.opentierlist.model.models.TierList;

public class PersistenceTest {
	public static void main() throws IOException {
		var unranked = new ArrayList<Telement>();

		@SuppressWarnings("unused")
		Telement m, p, s, g, o, a;
		unranked.add(m = new Telement("Mookka", "cow.jpg")); unranked.add(p = new Telement("Pehkura", "sheep.jpg")); unranked.add(s = new Telement("Suynoh"));
		unranked.add(g = new Telement("Galeena")); unranked.add(o = new Telement("Okha", "duck.jpg")); unranked.add(a = new Telement("Aseeno", "usagi.jpg"));
		
		var defaultTierList = new TierList("Test", unranked);
		for (var defaultTier : DefaultTier.values()) {
			defaultTierList.addTier(defaultTier.value());
		}
		TierListController controller = new StandardTierListController(defaultTierList);
		
		controller.rank(m, DefaultTier.S.value());
		controller.rank(p, DefaultTier.S.value());
		controller.rank(s, DefaultTier.A.value());
		
		FileWriter writer = new FileWriter("test.json");
		TierListWriter tlwriter = new TierListWriter(controller, writer);
		
		tlwriter.write();
	}
}
