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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;

public class BasicGraphBuilderTest extends TestCase {

  private BasicGraphBuilder m_builder;
  
	public BasicGraphBuilderTest(String name) {
		super(name);
	}
  
  protected void setUp() throws Exception {
    super.setUp();
    
    m_builder = createBuilder();
  }

  public void test_buildNode() {
    assertTrue(m_builder.getNodes().isEmpty());
    
    Node n = m_builder.buildNode();
  
    assertTrue(n != null);  
  }
  
  public void test_buildEdge() {
    Node n1 = m_builder.buildNode();
    Node n2 = m_builder.buildNode();
    
    assertTrue(m_builder.getEdges().isEmpty());
    
    Edge e = m_builder.buildEdge(n1, n2);
    
    assertTrue(e != null);
    assertTrue(e.getNodeA() == n1 || e.getNodeA() == n2);
    assertTrue(e.getNodeB() == n1 || e.getNodeB() == n2);
  }
  
  public void test_addNode() {
    Node n1 = m_builder.buildNode();
    m_builder.addNode(n1);
    assertTrue(m_builder.getNodes().size() == 1);
    assertTrue(m_builder.getNodes().contains(n1));
  }
  
  public void test_addEdge_0() {
    Node n1 = m_builder.buildNode();
    Node n2 = m_builder.buildNode();
    
    Edge e = m_builder.buildEdge(n1, n2);
    
    m_builder.addNode(n1);
    m_builder.addNode(n2);
    
    m_builder.addEdge(e);
    
    assertTrue(m_builder.getEdges().size() == 1);
    assertTrue(m_builder.getEdges().contains(e));
    
    assertTrue(n1.getEdges().contains(e));
    assertTrue(n2.getEdges().contains(e));
  }
  
  public void test_addEdge_1() {
    //add a loop edge, edge list size ==1 but degree == 2
    Node n1 = m_builder.buildNode();
    Edge loop = m_builder.buildEdge(n1, n1);
    
    m_builder.addNode(n1);
    m_builder.addEdge(loop);	
    
    assertTrue(n1.getEdges().size() == 1);
    assertTrue(n1.getDegree() == 2);
  }
  
  public void test_removeNode() {
    Node n1 = m_builder.buildNode();
    m_builder.addNode(n1);
    
    assertTrue(m_builder.getNodes().contains(n1));
    
    m_builder.removeNode(n1);
    assertTrue(m_builder.getNodes().isEmpty());
  }
  
  public void test_removeNodes() {
    Node n1 = m_builder.buildNode();
    Node n2 = m_builder.buildNode();
    
    m_builder.addNode(n1);
    m_builder.addNode(n2);
    
    assertTrue(m_builder.getNodes().size() == 2);     
    assertTrue(m_builder.getNodes().contains(n1));
    assertTrue(m_builder.getNodes().contains(n2));
    
    List toRemove = new ArrayList();
    toRemove.add(n1);
    toRemove.add(n2);
    
    m_builder.removeNodes(toRemove);
    
    assertTrue(m_builder.getNodes().isEmpty());
  }
  
  public void test_removeEdge_0() {
    Node n1 = m_builder.buildNode();
    Node n2 = m_builder.buildNode();
    
    Edge e = m_builder.buildEdge(n1, n2);
    
    m_builder.addNode(n1);
    m_builder.addNode(n2);
    m_builder.addEdge(e);
    
    assertTrue(n1.getEdges().contains(e));
    assertTrue(n2.getEdges().contains(e));
    
    m_builder.removeEdge(e);
    assertTrue(!n1.getEdges().contains(e));
    assertTrue(!n2.getEdges().contains(e));
    assertTrue(m_builder.getEdges().isEmpty());
  }
  
  public void test_removeEdge_1() {
    //test a loop edge
    Node n1 = m_builder.buildNode();
    
    Edge e = m_builder.buildEdge(n1, n1);
    
    m_builder.addNode(n1);
    m_builder.addEdge(e);
    
    assertTrue(n1.getEdges().contains(e));
    
    m_builder.removeEdge(e);
    assertTrue(!n1.getEdges().contains(e));
    assertTrue(m_builder.getEdges().isEmpty());
  }
  
  public void test_removeEdges() {
    Node n1 = m_builder.buildNode();
    Node n2 = m_builder.buildNode();
    Node n3 = m_builder.buildNode();
    Node n4 = m_builder.buildNode();
    
    Edge e1 = m_builder.buildEdge(n1, n2);
    Edge e2 = m_builder.buildEdge(n3, n4);
    
    m_builder.addNode(n1);
    m_builder.addNode(n2);
    m_builder.addNode(n3);
    m_builder.addNode(n4);
    
    m_builder.addEdge(e1);
    m_builder.addEdge(e2);
    
    List toRemove = new ArrayList();
    toRemove.add(e1);
    toRemove.add(e2);
    
    assertTrue(m_builder.getEdges().contains(e1));
    assertTrue(m_builder.getEdges().contains(e2));
    
    m_builder.removeEdges(toRemove);
    
    assertTrue(m_builder.getEdges().isEmpty());
  }
  
  public void test_getGraph() {
    Graph graph = m_builder.getGraph();  
    assertTrue(graph != null);
  }
  
  private BasicGraphBuilder createBuilder() {
    return(new BasicGraphBuilder());
  }
}
