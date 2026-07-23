package net.flynn.opentierlist;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import net.flynn.opentierlist.controller.TierListController;
import net.flynn.opentierlist.model.enums.DefaultTier;
import net.flynn.opentierlist.model.models.TierElement;
import net.flynn.opentierlist.model.models.TierList;

public class MainPersistenceTest {
  public static void main(String[] args) throws FileNotFoundException {
    var unTiered = new ArrayList<TierElement>();

    @SuppressWarnings("unused")
    TierElement m, p, s, g, o, a;
    unTiered.add(m = new TierElement("Mookka", "cow.jpg"));
    unTiered.add(p = new TierElement("Pehkura", "sheep.jpg"));
    unTiered.add(s = new TierElement("Suynoh"));
    unTiered.add(g = new TierElement("Galeena"));
    unTiered.add(o = new TierElement("Okha", "duck.jpg"));
    unTiered.add(a = new TierElement("Aseeno", "usagi.jpg"));

    var defaultTierList = new TierList(unTiered);
    for (var tier : DefaultTier.values())
      defaultTierList.addTier(tier.value());

    var controller = TierListController.of(defaultTierList);

    controller.tier(m, DefaultTier.S.value());
    controller.tier(p, DefaultTier.S.value());
    controller.tier(s, DefaultTier.A.value());

    controller.saveTierListAs("test.json");
  }
}
