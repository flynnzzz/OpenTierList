package net.flynn.opentierlist;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import net.flynn.opentierlist.controller.TierListController;
import net.flynn.opentierlist.model.enums.DefaultTier;
import net.flynn.opentierlist.model.models.TierElement;
import net.flynn.opentierlist.model.models.TierList;

public class MainPersistenceTest {
  public static void main(String args[]) throws FileNotFoundException {
    var unranked = new ArrayList<TierElement>();

    @SuppressWarnings("unused")
    TierElement m, p, s, g, o, a;
    unranked.add(m = new TierElement("Mookka", "cow.jpg"));
    unranked.add(p = new TierElement("Pehkura", "sheep.jpg"));
    unranked.add(s = new TierElement("Suynoh"));
    unranked.add(g = new TierElement("Galeena"));
    unranked.add(o = new TierElement("Okha", "duck.jpg"));
    unranked.add(a = new TierElement("Aseeno", "usagi.jpg"));

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
