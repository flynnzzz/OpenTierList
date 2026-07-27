package net.flynn.opentierlist.model.enums;

import javafx.scene.paint.Color;
import net.flynn.opentierlist.model.models.Tier;
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
        assertTrue(s.value().equalsTier(new Tier("S", Color.ORANGERED.toString())));
        assertTrue(a.value().equalsTier(new Tier("A", Color.ORANGE.toString())));
        assertTrue(b.value().equalsTier(new Tier("B", Color.YELLOW.toString())));
        assertTrue(c.value().equalsTier(new Tier("C", Color.GREENYELLOW.toString())));
        assertTrue(d.value().equalsTier(new Tier("D", Color.LIGHTBLUE.toString())));
        assertTrue(e.value().equalsTier(new Tier("E", Color.ALICEBLUE.toString())));
        assertTrue(f.value().equalsTier(new Tier("F", Color.GRAY.toString())));
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