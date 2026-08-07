package net.flynn.opentierlist.model.enums;

import net.flynn.opentierlist.model.models.Tier;
import net.flynn.opentierlist.ui.ConfigHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class DefaultTierTest {

    private DefaultTier s, a, b, c, d, e, f;

    private Set<DefaultTier> defaultTiers;

    @Before
    public void setUp() {
        s = DefaultTier.S;
        a = DefaultTier.A;
        b = DefaultTier.B;
        c = DefaultTier.C;
        d = DefaultTier.D;
        e = DefaultTier.E;
        f = DefaultTier.F;
        defaultTiers = new HashSet<>(Arrays.asList(DefaultTier.values()));
    }

    @After
    public void tearDown() {
        s = null; a = null; b = null; c = null; d = null; e = null; f = null;
        defaultTiers.clear();
        defaultTiers = null;
    }

    @Test
    public void value() {
        assertTrue(s.value().equalsTier(new Tier("S", ConfigHolder.DEFAULT_S_COLOR)));
        assertTrue(a.value().equalsTier(new Tier("A", ConfigHolder.DEFAULT_A_COLOR)));
        assertTrue(b.value().equalsTier(new Tier("B", ConfigHolder.DEFAULT_B_COLOR)));
        assertTrue(c.value().equalsTier(new Tier("C", ConfigHolder.DEFAULT_C_COLOR)));
        assertTrue(d.value().equalsTier(new Tier("D", ConfigHolder.DEFAULT_D_COLOR)));
        assertTrue(e.value().equalsTier(new Tier("E", ConfigHolder.DEFAULT_E_COLOR)));
        assertTrue(f.value().equalsTier(new Tier("F", ConfigHolder.DEFAULT_F_COLOR)));
    }

    @Test
    public void values() {
        assertTrue(defaultTiers.containsAll(Set.of(s,a,b,c,d,e,f)));

        int TIERS_NUMBER = 7;
        assertEquals(TIERS_NUMBER, defaultTiers.size());
    }

    @Test
    public void valueOf() {
        assertEquals(s, DefaultTier.valueOf("S"));
        assertEquals(a, DefaultTier.valueOf("A"));
        assertEquals(b, DefaultTier.valueOf("B"));
        assertEquals(c, DefaultTier.valueOf("C"));
        assertEquals(d, DefaultTier.valueOf("D"));
        assertEquals(e, DefaultTier.valueOf("E"));
        assertEquals(f, DefaultTier.valueOf("F"));

        assertThrows(IllegalArgumentException.class, () -> DefaultTier.valueOf("_"));
    }
}