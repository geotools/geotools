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

/**
 * Default implementation of the FeatureLockFactory.  Generates id numbers.
 * 
 * @deprecated Please use {@link FeatureLock} directly
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @author Chris Holmes, TOPP.
 * @source $URL$
 * @version $Id$
 *
 * @task REVISIT: Should more of this code move to the parent?  I guess if
 *       other implementations came along they may want to implement
 *       differently.
 * @task REVISIT: The generation code can move to the parent. Even if other
 *       implementations come along we do not want the to implement the
 *       generation differently.
 */
public class DefaultFeatureLockFactory extends FeatureLockFactory {
    /** Count used to generate unique ID numbers */
    static long count = 0;

    protected FeatureLock createLock(String name, long duration) {
        long number = nextIdNumber(duration);

        return new DefaultFeatureLock(name + "_" + Long.toString(number, 16),
            duration);
    }

    /**
     * Package visiable generate function for JUnit tests
     *
     * @param name The name to give this lock.
     * @param duration How long it is to last.
     *
     * @return The new FeatureLock.
     */
    static FeatureLock createTestLock(String name, long duration) {
        return new DefaultFeatureLock(name, duration);
    }

    /**
     * Used to seed nextIDNumber to allow for reproduceable JUnit tests.  Not
     * part of the public API -  package protected method used for JUnits
     * Tests
     *
     * @param seed The number to start seeding with.
     */
    static void seedIdNumber(long seed) {
        count = seed;
    }

    /**
     * Produces the next ID number based on count, duration and the current
     * time.  The uniquity is 'good enough' although not provable as per
     * security systems. The method used will probably have to be changed
     * later, the api is what is important right now. 
     * <table border=1, bgcolor="lightgray", width="100%"><tr><td><code><pre>
     * count: 000000000000000000000000000001011 (increasing count)
     *  date: 000000000001111010101010101000000 (expriry date milliseconds usually empty)
     *    now: 000101100111011010011111000000000 (bit order reverse of current time)
     *         ---------------------------------
     * number: 000101100110100000110101001001011
     * </pre></code></td></tr></table>
     * Once again method is package visible for testing.
     *
     * @param duration The time for the lock to last.
     *
     * @return The next generated id number.
     */
    static long nextIdNumber(long duration) {
        long number;
        long now = System.currentTimeMillis();
        long time = now + duration;

        // start of with number
        count++;
        number = count;
        number = number ^ time; // count fills 'empty' milliseconds

        StringBuffer reverse = new StringBuffer(asBits(now));
        now = Long.parseLong(reverse.reverse().toString(), 2); // flip now around

        number = number ^ now;

        return number;
    }

    /**
     * Utility method used to reverse bits.
     * <p>
     * Since generating ID numbers is not performance critical I won't care
     * right now.
     * </p>
     *
     * @param number
     * @return Number represented as bits
     */
    private static String asBits(long number) {
        long yum = number;
        StringBuffer buf = new StringBuffer(32);

        for (int i = 0; i < 63; i++) {
            buf.append(yum & 1);
            yum = yum >> 1;
        }

        buf.reverse();

        return buf.toString();
    }
}
