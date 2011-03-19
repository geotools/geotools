/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of LockingAPI FeatureLock data object.
 * <p>
 * 
 * @see org.geotools.data
 * @author jgarnett, Refractions Reasearch Inc.
 * @source $URL$
 * @version CVS Version
 */
public class FeatureLockTest {
    String lockName;
    long lockDuration;
    FeatureLock lock;

    @Before
    public void setUp() throws Exception {
        lockDuration = 240; // 240 minutes
        lockName = "TestLock";
        lock = new FeatureLock(lockName, lockDuration);
    }

    @Test
    public void testGetID() {        
        assertEquals( "lockName", lockName, lock.getAuthorization() );
    }

    @Test
    public void testGetExpire() {        
        assertEquals( "lockDate", lockDuration, lock.getDuration() ); 
    }
}
