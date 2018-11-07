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

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * Test of LockingAPI FeatureLock data object.
 *
 * <p>
 *
 * @see org.geotools.data
 * @author jgarnett, Refractions Reasearch Inc.
 * @version CVS Version
 */
public class FeatureLockTest {
    String lockName;
    long lockDuration;
    FeatureLock lock;

    @Test
    public void testGetID() {
        lockDuration = 240; // 240 milliseconds
        lockName = "TestLock";
        lock = new FeatureLock(lockName, lockDuration);
        assertEquals("lockName", lockName, lock.getAuthorization());
    }

    @Test
    public void testGetExpire() {
        lockDuration = 240; // 240 milliseconds
        lockName = "TestLock";
        lock = new FeatureLock(lockName, lockDuration);
        assertEquals("lockDate", lockDuration, lock.getDuration());
    }

    @Test
    public void testGetExpireSeconds() {
        lockDuration = 15; // 15 seconds
        lockName = "TestLock";
        lock = new FeatureLock(lockName, lockDuration, TimeUnit.SECONDS);
        assertEquals("lockDate", lockDuration * 1000, lock.getDuration());
    }
}
