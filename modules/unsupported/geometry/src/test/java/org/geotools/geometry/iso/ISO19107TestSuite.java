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
package org.geotools.geometry.iso;

import org.geotools.geometry.iso.aggregate.AggregateTestSuite;
import org.geotools.geometry.iso.complex.ComplexTestSuite;
import org.geotools.geometry.iso.coordinate.GeometryTestSuite;
import org.geotools.geometry.iso.operations.OperationsTestSuite;
import org.geotools.geometry.iso.primitive.PrimitiveTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ISO19107TestSuite {
	
    public static Test suite() {

        TestSuite suite = new TestSuite();
	
        // *** COORDINATES
        suite.addTest(GeometryTestSuite.suite());
        
        // *** PRIMITIVES
        suite.addTest(PrimitiveTestSuite.suite());

        // *** COMPLEXES
        suite.addTest(ComplexTestSuite.suite());

        // *** AGGREGATE
        suite.addTest(AggregateTestSuite.suite());

        // *** OPERATIONS
        suite.addTest(OperationsTestSuite.suite());
        
        return suite;
    }

    /**
     * Runs the test suite using the textual runner.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
