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
package org.geotools.graph.build.opt;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.opt.OptEdge;
import org.geotools.graph.structure.opt.OptNode;

public class OptGraphBuilderTest extends TestCase {
  
  private OptGraphBuilder m_builder;
  
  public OptGraphBuilderTest(String name) {
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
    assertTrue(n instanceof OptNode);
  }
  
  public void test_buildEdge() {
    Node n1 = m_builder.buildNode();
    Node n2 = m_builder.buildNode();
    
    assertTrue(m_builder.getEdges().isEmpty());
    
    Edge e = m_builder.buildEdge(n1, n2);
    
    assertTrue(e != null);
    assertTrue(e instanceof OptEdge);
    assertTrue(e.getNodeA() == n1 || e.getNodeA() == n2);
    assertTrue(e.getNodeB() == n1 || e.getNodeB() == n2);
  }
  
  public void test_addNode() {
    Node n1 = m_builder.buildNode();
    m_builder.addNode(n1);
    
    assertTrue(m_builder.getNodes().size() == 1);
    assertTrue(m_builder.getNodes().contains(n1));
  }
  
  public void test_addEdge() {
    OptNode n1 = (OptNode)m_builder.buildNode();
    n1.setDegree(1);
    
    OptNode n2 = (OptNode)m_builder.buildNode();
    n2.setDegree(1);
    
    Edge e = m_builder.buildEdge(n1, n2);
    
    m_builder.addNode(n1);
    m_builder.addNode(n2);
    
    m_builder.addEdge(e);
    
    assertTrue(m_builder.getEdges().size() == 1);
    assertTrue(m_builder.getEdges().contains(e));
    
    assertTrue(n1.getEdges().contains(e));
    assertTrue(n2.getEdges().contains(e));
  }
  
  public void test_removeNode() {
    OptNode n1 = (OptNode)m_builder.buildNode();
    n1.setDegree(0);
    
    m_builder.addNode(n1);
    
    assertTrue(m_builder.getNodes().contains(n1));
    
    m_builder.removeNode(n1);
    assertTrue(m_builder.getNodes().isEmpty());
  }
  
  public void test_removeNodes() {
    OptNode n1 = (OptNode)m_builder.buildNode();
    n1.setDegree(0);
    
    OptNode n2 = (OptNode)m_builder.buildNode();
    n2.setDegree(0);
    
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
  
  public void test_removeEdge() {
    OptNode n1 = (OptNode)m_builder.buildNode();
    n1.setDegree(1);
    
    OptNode n2 = (OptNode)m_builder.buildNode();
    n2.setDegree(1);
    
    Edge e = m_builder.buildEdge(n1, n2);
    
    m_builder.addNode(n1);
    m_builder.addNode(n2);
    m_builder.addEdge(e);
    
    assertTrue(n1.getEdges().contains(e));
    assertTrue(n2.getEdges().contains(e));
    
    try {
      m_builder.removeEdge(e);
      assertTrue(false);
    }
    catch(UnsupportedOperationException uoe) {
      assertTrue(true);    
    }
  }
  
  public void test_removeEdges() {
    OptNode n1 = (OptNode)m_builder.buildNode();
    n1.setDegree(1);
    OptNode n2 = (OptNode)m_builder.buildNode();
    n2.setDegree(1);
    OptNode n3 = (OptNode)m_builder.buildNode();
    n3.setDegree(1);
    OptNode n4 = (OptNode)m_builder.buildNode();
    n4.setDegree(1);
    
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
    
    try {
       m_builder.removeEdges(toRemove);
      assertTrue(false);
    }
    catch(UnsupportedOperationException uoe) {
      assertTrue(true);    
    }
  }
  
  public void test_getGraph() {
    Graph graph = m_builder.getGraph();  
    assertTrue(graph != null);
  }
  
  private OptGraphBuilder createBuilder() {
    return(new OptGraphBuilder());
  }
  
  
  
  
  
}
