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
package org.geotools.graph.traverse.standard;

import junit.framework.TestSuite;

public class AllTests extends TestSuite {
  public AllTests() {
    super();
    addTest(new TestSuite(DepthFirstIteratorTest.class));
    addTest(new TestSuite(DirectedDepthFirstIteratorTest.class));
    addTest(new TestSuite(BreadthFirstIteratorTest.class));
    addTest(new TestSuite(DijkstraIteratorTest.class));
    addTest(new TestSuite(NoBifurcationIteratorTest.class));
    addTest(new TestSuite(BreadthFirstTopologicalIteratorTest.class));
    addTest(new TestSuite(DepthFirstTopologicalIteratorTest.class));
    
    //addTest(new TestSuite(DepthFirstTraversalTest.class));
//    addTest(new TestSuite(BreadthFirstTraversalTest.class));
//    addTest(new TestSuite(NoBifurcationTraversalTest.class));
  } 	
    
}
