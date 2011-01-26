/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.complex;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ComplexTestSuite {
	
    public static Test suite() {

        TestSuite suite = new TestSuite();
	
        suite.addTestSuite(CompositeSurfaceTest.class);
        suite.addTestSuite(PicoCompositeSurfaceTest.class);
        suite.addTestSuite(PicoCompositeCurveTest.class);
        suite.addTestSuite(PicoCompositePointTest.class);

        return suite;
    }

    /**
     * Runs the test suite using the textual runner.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
