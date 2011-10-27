package org.opengis.filter.identity;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.opengis.filter.identity.Version.Action;

public class VersionTest {

    @Test
    public void bitwise() {
        for (Action action : Action.values()) {
            long encoded = Version.UNION_ACTION | ((long) action.ordinal());
            
            assertTrue( (encoded & Version.UNION_ACTION) > 0 );
            long decoded = Version.UNION_MASK & ((long)encoded);
            
            Action found = Action.lookup((int)decoded);
            assertEquals( action, found );
        }

    }

    @Test
    public void versionInteger() {
        Version version = new Version(1234567890);

        assertNotNull(version.getIndex());
        assertTrue( version.isIndex() );
        assertEquals(1234567890, (int) version.getIndex());
        
        assertFalse( version.isVersionAction() );
        assertNull(version.getVersionAction());
        
        assertNull(version.getDateTime());
    }

    @Test
    public void versionDate() {
        Date now = new Date();

        Version version = new Version(now);

        assertEquals(now, version.getDateTime());
        assertNull(version.getIndex());
        assertNull(version.getVersionAction());
    }

    @Test
    public void versionAction() {
        Version version = new Version(Version.Action.ALL);

        assertEquals(Version.Action.ALL, version.getVersionAction());
        assertNull(version.getIndex());
        assertNull(version.getDateTime());
    }
}
