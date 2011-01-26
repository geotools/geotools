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

import junit.framework.TestCase;

import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.util.graph.CycleDetector;

public class CycleDetectorTest extends TestCase {
  private GraphBuilder m_builder;
  
  public CycleDetectorTest(String name) {
    super(name);  
  }
  
  protected void setUp() throws Exception {
    super.setUp();
    
    m_builder = createBuilder();
  }
  
  /**
   * Create a graph without a cycle. <BR>
   * <BR>
   * Expected: 1. containsCycle() returns false
   */
  public void test_0() {
    GraphTestUtil.buildNoBifurcations(builder(), 100);
    
    CycleDetector detector = new CycleDetector(builder().getGraph());
    assertTrue(!detector.containsCycle());   
  }
  
  /**
   * Create a graph that contains a cycle. <BR>
   * <BR>
   * Expected: 1. containsCycle returns true
   */
  public void test_1() {
    GraphTestUtil.buildCircular(builder(), 100);
    
    CycleDetector detector = new CycleDetector(builder().getGraph());
    assertTrue(detector.containsCycle());
  }
  
  protected GraphBuilder createBuilder() {
    return(new BasicGraphBuilder());  
  }
  
  protected GraphBuilder builder() {
    return(m_builder);  
  }
 
}
