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
package org.geotools.graph.util;

import junit.framework.TestSuite;

/** @source $URL$ */
public class AllTests extends TestSuite {
    public AllTests() {
        super();
        addTest(new TestSuite(CycleDetectorTest.class));
        addTest(new TestSuite(DijkstraShortestPathFinderTest.class));
        addTest(new TestSuite(GraphPartitionerTest.class));
        addTest(new TestSuite(GraphFuserTest.class));
    }
}
