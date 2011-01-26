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

import junit.framework.TestCase;

/**
 * Test of LockingAPI FeatureLock data object.
 * <p>
 * 
 * @see org.geotools.data
 * @author jgarnett, Refractions Reasearch Inc.
 * @source $URL$
 * @version CVS Version
 */
public class FeatureLockTest extends TestCase {
    String lockName;
    long lockDuration;
    FeatureLock lock;
    /**
     * Constructor for LockTest.
     * @param arg0
     */
    public FeatureLockTest(String arg0) {
        super(arg0);
    }
    /**
     * Sets up lock objects for use.
     * </p>
     * @throws java.lang.Exception
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        lockDuration = 4*60*60*60; // 4 hours/60 min/60 sec/60 milliseconds
        lockName = "TestLock";
        lock = DefaultFeatureLockFactory.createTestLock( lockName, lockDuration ); 
    }
    /**
     * Clears lock objects.
     * 
     * @throws java.lang.Exception
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        lockName = null;
        lock = null;
    }

    public void testGetID() {        
        assertEquals( "lockName", lockName, lock.getAuthorization() );
    }
    public void testGetExpire() {        
        assertEquals( "lockDate", lockDuration, lock.getDuration() ); 
    }
    /*
     * Test for FeatureLock generate(int)
     */
    public void testGenerateint() {
        FeatureLock lock = FeatureLockFactory.generate( "Test", 3600 );        
    }
    public void testNextIDNumberDate(){
        DefaultFeatureLockFactory.seedIdNumber( 0 );
        long number1 = DefaultFeatureLockFactory.nextIdNumber( lockDuration );
        long number2 = DefaultFeatureLockFactory.nextIdNumber( lockDuration );
        long number3 = DefaultFeatureLockFactory.nextIdNumber( lockDuration );        
        assertFalse( "lockDate:"+number1, number1 == number2 );
        assertFalse( "lockDate:"+number1, number1 == number3 );
        assertFalse( "lockDate:"+number1, number2 == number3 );
                
    }
    /*
     * Test for FeatureLock generate(String, long)
     */
    public void testGenerateStringLong() {
        FeatureLock lock = FeatureLockFactory.generate( "Test", lockDuration  );
        assertTrue( lock.getAuthorization().startsWith("Test") );
    }    
}
