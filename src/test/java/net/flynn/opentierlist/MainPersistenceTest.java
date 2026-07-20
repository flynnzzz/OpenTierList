package net.flynn.opentierlist;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import net.flynn.opentierlist.controller.TierListController;
import net.flynn.opentierlist.model.enums.DefaultTier;
import net.flynn.opentierlist.model.models.Telement;
import net.flynn.opentierlist.model.models.TierList;

public class MainPersistenceTest {
  public static void main(String args[]) throws FileNotFoundException {
    var unranked = new ArrayList<Telement>();

    @SuppressWarnings("unused")
    Telement m, p, s, g, o, a;
    unranked.add(m = new Telement("Mookka", "cow.jpg"));
    unranked.add(p = new Telement("Pehkura", "sheep.jpg"));
    unranked.add(s = new Telement("Suynoh"));
    unranked.add(g = new Telement("Galeena"));
    unranked.add(o = new Telement("Okha", "duck.jpg"));
    unranked.add(a = new Telement("Aseeno", "usagi.jpg"));

    var defaultTierList = new TierList(unranked);
    for (var tier : DefaultTier.values())
      defaultTierList.addTier(tier.value());

    var controller = TierListController.of(defaultTierList);

    controller.rank(m, DefaultTier.S.value());
    controller.rank(p, DefaultTier.S.value());
    controller.rank(s, DefaultTier.A.value());

    controller.saveTierListAs("test.json");
  }
}
