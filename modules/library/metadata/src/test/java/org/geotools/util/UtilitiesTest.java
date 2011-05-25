/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import org.junit.*;
import static org.junit.Assert.*;
import static org.geotools.util.Utilities.*;


/**
 * Tests the {@link Utilities} static methods.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class UtilitiesTest {
    
    /**
     * Tests {@link Utilities#ensureNonNull}.
     */
    @Test
    public void testEnsureNonNull() {
        boolean isNull = false;
        Object sampleObject = null;
        try {
            assertNull(sampleObject);
            Utilities.ensureNonNull("sampleObject", sampleObject);
        } catch (NullPointerException npe){
            isNull = true;
        }
        assertTrue(isNull);
        sampleObject = "";
        try {
            assertNotNull(sampleObject);
            Utilities.ensureNonNull("sampleObject", sampleObject);
            isNull = false;
        } catch (NullPointerException npe){
            isNull = true;
        }
        assertFalse(isNull);
    }
    
    /**
     * Tests {@link Utilities#equals}.
     */
    @Test
    public void testEquals() {
        assertTrue (Utilities.equals(null, null));
        assertFalse(Utilities.equals(null, ""  ));
        assertFalse(Utilities.equals(""  , null));
        assertTrue (Utilities.equals(""  , ""  ));
        assertFalse(Utilities.equals(" " , ""  ));
    }

    /**
     * Tests that the proper overloaded method of {@code equals} is selected.
     * Actually there is no easy way to make sure that this test pass, except
     * follow the execution of this method step-by-step in a debugger.
     */
    @Test
    public void testEqualOverload() {
        /*
         * The following should call the overloaded method for primitive types.
         */
        char   c1='A', c2='A'; assertTrue(Utilities.equals(c1, c2));
        byte   b1=65,  b2=65;  assertTrue(Utilities.equals(b1, b2));
        short  s1=65,  s2=65;  assertTrue(Utilities.equals(s1, s2));
        int    i1=65,  i2=65;  assertTrue(Utilities.equals(i1, i2));
        long   l1=65,  l2=65;  assertTrue(Utilities.equals(l1, l2));
        float  f1=65,  f2=65;  assertTrue(Utilities.equals(f1, f2));
        double d1=65,  d2=65;  assertTrue(Utilities.equals(d1, d2));
        /*
         * The following should call the equals(Object,Object) method.
         */
        Character C1=c1, C2=new Character(c2); assertTrue(Utilities.equals(C1, C2));
        Byte      B1=b1, B2=new Byte     (b2); assertTrue(Utilities.equals(B1, B2));
        Short     S1=s1, S2=new Short    (s2); assertTrue(Utilities.equals(S1, S2));
        Integer   I1=i1, I2=new Integer  (i2); assertTrue(Utilities.equals(I1, I2));
        Long      L1=l1, L2=new Long     (l2); assertTrue(Utilities.equals(L1, L2));
        Float     F1=f1, F2=new Float    (f2); assertTrue(Utilities.equals(F1, F2));
        Double    D1=d1, D2=new Double   (d2); assertTrue(Utilities.equals(D1, D2));
        /*
         * The compiler applies widening conversions, so the following are equals even if different
         * types (including Character). The only case where the compiler seems to prefer auto-boxing
         * is when exactly one argument is of boolean type.
         */
        assertTrue(Utilities.equals(f1, d2));
        assertTrue(Utilities.equals(i1, b2));
        assertTrue(Utilities.equals(f1, b2));
        assertTrue(Utilities.equals(f1, c2));
        assertTrue(Utilities.equals(c1, i2));
        assertTrue(Utilities.equals(c1, s2));
        assertTrue(Utilities.equals(b1, c2));
        /*
         * Same tests than above, but using the wrapper classes rather than the primitive types.
         * The wrapper classes are stricter; they do not accept anymore classes of different types.
         */
        assertFalse(Utilities.equals(F1, D2));
        assertFalse(Utilities.equals(I1, B2));
        assertFalse(Utilities.equals(F1, B2));
        assertFalse(Utilities.equals(F1, C2));
        assertFalse(Utilities.equals(C1, I2));
        assertFalse(Utilities.equals(C1, S2));
        assertFalse(Utilities.equals(B1, C2));
    }

    /**
     * Tests {@link Utilities#spaces}.
     */
    @Test
    public void testSpaces() {
        assertEquals("",         spaces(0));
        assertEquals(" ",        spaces(1));
        assertEquals("        ", spaces(8));
    }
}
