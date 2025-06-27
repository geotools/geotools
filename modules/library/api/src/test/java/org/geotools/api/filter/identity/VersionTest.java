package org.geotools.api.filter.identity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import org.geotools.api.filter.identity.Version.Action;
import org.junit.Test;

public class VersionTest {

    @Test
    public void bitwise() {
        for (Action action : Action.values()) {
            long encoded = Version.UNION_ACTION | action.ordinal();

            assertTrue((encoded & Version.UNION_ACTION) > 0);
            long decoded = Version.UNION_MASK & encoded;

            Action found = Action.lookup((int) decoded);
            assertEquals(action, found);
        }
    }

    @Test
    public void versionInteger() {
        try {
            new Version(-1);
            fail("Expected IAE on negative version");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            new Version(0);
            fail("Expected IAE");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        Integer testInt = Integer.valueOf(1234567890);
        Version version = new Version(testInt);

        assertNotNull(version.getIndex());
        assertTrue(version.isIndex());
        assertEquals(1234567890, (int) version.getIndex());

        assertFalse(version.isVersionAction());
        assertNull(version.getVersionAction());

        assertNull(version.getDateTime());
    }

    @Test
    public void versionDate() {
        Date now = new Date();

        Version version = new Version(now);

        assertTrue(version.isDateTime());
        assertEquals(now.getTime(), version.getDateTime().getTime());
        assertNull(version.getIndex());
        assertNull(version.getVersionAction());
    }

    @Test
    public void versionAction() {
        Version version = new Version(Version.Action.ALL);

        assertEquals(Version.Action.ALL, version.getVersionAction());

        assertTrue(version.isVersionAction());

        assertNull(version.getIndex());
        assertNull(version.getDateTime());
    }

    @Test
    public void versionEmpty() {
        Version version = new Version();

        assertTrue(version.isEmpty());
    }
}
