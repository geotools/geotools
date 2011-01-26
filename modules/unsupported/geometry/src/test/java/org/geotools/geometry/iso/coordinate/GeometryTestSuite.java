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
package org.geotools.geometry.iso.coordinate;

import junit.framework.Test;
import junit.framework.TestSuite;

public class GeometryTestSuite {
	
    public static Test suite() {

        TestSuite suite = new TestSuite();
	
        suite.addTestSuite(GeometryFactoryTest.class);
        suite.addTestSuite(DirectPositionTest.class);
        suite.addTestSuite(EnvelopeTest.class);
        suite.addTestSuite(LineStringLineSegmentTest.class);
        suite.addTestSuite(PointArrayTest.class);

        return suite;
    }

    /**
     * Runs the test suite using the textual runner.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
