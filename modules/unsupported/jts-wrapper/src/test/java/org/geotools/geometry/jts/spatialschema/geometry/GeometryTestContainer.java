/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts.spatialschema.geometry;


import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * The {@code GeometryTest} class is a container that holds a {@code List} of
 * {@code GeometryTestCase}s and provides a way to execute them all.
 * 
 * @author Jody Garnett
 * @author Joel Skelton
 *
 *
 * @source $URL$
 */
public class GeometryTestContainer {
    private List testCases;

    /**
     * Constructor
     */
    public GeometryTestContainer() {
        testCases = new ArrayList();
    }

    /**
     * Adds a constructed test case into the list of available tests
     * @param testCase
     */
    public void addTestCase(GeometryTestCase testCase) {
        testCases.add(testCase);
    }

    /**
     * Runs all tests currently contained. Returns true if all tests pass, false otherwise
     * @return true if all tests pass, false otherwise
     */
    public boolean runAllTestCases() {
        for (Iterator i=testCases.iterator(); i.hasNext(); ) {
            GeometryTestCase testCase = (GeometryTestCase) i.next();
            if (!testCase.runTestCases()) {
                return false;
            }
        }
        return true;
    }


}
