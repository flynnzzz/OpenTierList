package net.flynn.opentierlist.model.enums;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class TieredStatusTest {

    private TieredStatus tiered, unTiered;

    private Set<TieredStatus> tieredStatuses;

    @Before
    public void setUp() {
        tiered = TieredStatus.TIERED;
        unTiered = TieredStatus.UNTIERED;

        tieredStatuses = new HashSet<>(Arrays.asList(TieredStatus.values()));
    }

    @After
    public void tearDown() {
        tiered = null; unTiered = null;
        tieredStatuses.clear();
        tieredStatuses = null;
    }

    @Test
    public void value() {
        assertTrue(tiered.value());
        assertFalse(unTiered.value());
    }

    @Test
    public void values() {
        assertTrue(tieredStatuses.containsAll(Set.of(tiered, unTiered)));

        int TIERED_STATUSES_NUMBER = 2;
        assertEquals(TIERED_STATUSES_NUMBER, tieredStatuses.size());
    }

    @Test
    public void valueOf() {
        assertEquals(tiered, TieredStatus.valueOf("TIERED"));
        assertEquals(unTiered, TieredStatus.valueOf("UNTIERED"));

        assertThrows(IllegalArgumentException.class, () -> TieredStatus.valueOf("_"));
    }
}