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
package org.geotools.graph.build.basic;

import junit.framework.TestCase;

import org.geotools.graph.structure.DirectedEdge;
import org.geotools.graph.structure.DirectedGraph;
import org.geotools.graph.structure.DirectedNode;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;

public class BasicDirectedGraphBuilderTest extends TestCase {

  private BasicDirectedGraphBuilder m_builder;
    
  public BasicDirectedGraphBuilderTest(String name) {
    super(name);  
  } 
  
  protected void setUp() throws Exception {
    super.setUp();
    
    m_builder = new BasicDirectedGraphBuilder();
  }
  
  public void test_buildNode() {
    DirectedNode dn = (DirectedNode)m_builder.buildNode();  
    assertTrue(dn != null);
  }
  
  public void test_buildEdge() {
    Node n1 = m_builder.buildNode();
    Node n2 = m_builder.buildNode();
    
    DirectedEdge de = (DirectedEdge)m_builder.buildEdge(n1,n2);
    
    assertTrue(de != null);
    assertTrue(de.getInNode() == n1);
    assertTrue(de.getOutNode() == n2);
  }
  
  public void test_addEdge_0() {
    DirectedNode n1 = (DirectedNode)m_builder.buildNode();
    DirectedNode n2 = (DirectedNode)m_builder.buildNode();
    
    DirectedEdge e = (DirectedEdge)m_builder.buildEdge(n1,n2);
    
    m_builder.addEdge(e);
    
    assertTrue(m_builder.getEdges().contains(e));
    assertTrue(n1.getOutEdges().contains(e));
    assertTrue(n2.getInEdges().contains(e));
  }
  
  public void test_addEdge_1() {
    //add a loop edge, in degree == 1, out degree = 1, degree == 2
    DirectedNode n1 = (DirectedNode)m_builder.buildNode();
	  DirectedEdge e = (DirectedEdge)m_builder.buildEdge(n1,n1);
	  
	  m_builder.addNode(n1);
	  m_builder.addEdge(e);
	  
	  assertTrue(n1.getInEdges().size() == 1);
	  assertTrue(n1.getOutEdges().size() == 1);
	  assertTrue(n1.getEdges().size() == 2);
	  
	  assertTrue(n1.getInDegree() == 1);
	  assertTrue(n1.getOutDegree() == 1);
	  assertTrue(n1.getDegree() == 2);
  }
  	
  
  public void test_getGraph() {
    Graph graph = m_builder.getGraph();
    assertTrue(graph instanceof DirectedGraph);
  }
}
