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
package org.geotools.graph.structure.basic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;

public class BasicGraphTest extends TestCase {

  private List m_nodes;
  private List m_edges;
  private BasicGraph m_graph;
  
	public BasicGraphTest(String name) {
		super(name);
	}
    
  protected void setUp() throws Exception {
    super.setUp();
    BasicNode n1 = new BasicNode();
    BasicNode n2 = new BasicNode();
    BasicNode n3 = new BasicNode();
    BasicNode n4 = new BasicNode();
    
    BasicEdge e1 = new BasicEdge(n1,n2);
    BasicEdge e2 = new BasicEdge(n2,n3);
    BasicEdge e3 = new BasicEdge(n3,n4);
    
    n1.add(e1);
    n2.add(e1);
    n2.add(e2);
    n3.add(e2);
    n3.add(e3);
    n4.add(e3);
    
    m_nodes = new ArrayList();
    m_nodes.add(n1);
    m_nodes.add(n2);
    m_nodes.add(n3);
    m_nodes.add(n4);
    
    m_edges = new ArrayList();
    m_edges.add(e1);
    m_edges.add(e2);
    m_edges.add(e3);
    
    m_graph = new BasicGraph(m_nodes, m_edges);   
  }

  public void test_getNodes() {
    assertTrue(m_graph.getNodes() == m_nodes);    
  }
  
  public void test_getEdges() {
    assertTrue(m_graph.getEdges() == m_edges);    
  }
  
  public void test_queryNodes() {
    GraphVisitor visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        if (component == m_nodes.get(1) || component == m_nodes.get(2)) 
          return(BasicGraph.PASS_AND_CONTINUE);
        return(BasicGraph.FAIL_QUERY);
      }
    };
    List result = m_graph.queryNodes(visitor);
    
    assertTrue(result.size() == 2);
    assertTrue(result.get(0) == m_nodes.get(1));
    assertTrue(result.get(1) == m_nodes.get(2));
  }

  public void test_queryEdges() {
    GraphVisitor visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        if (component == m_edges.get(1) || component == m_edges.get(2)) 
          return(BasicGraph.PASS_AND_CONTINUE);
        return(BasicGraph.FAIL_QUERY);
      }
    };
    List result = m_graph.queryEdges(visitor);
    
    assertTrue(result.size() == 2);
    assertTrue(result.get(0) == m_edges.get(1));
    assertTrue(result.get(1) == m_edges.get(2));
  }
  
  public void test_visitNodes() {
    final HashSet visited = new HashSet();
    GraphVisitor visitor = new GraphVisitor() {
    	public int visit(Graphable component) {
    		visited.add(component);
        return(0);
    	}
    };
    
    m_graph.visitNodes(visitor);
    
    for (Iterator itr = m_nodes.iterator(); itr.hasNext();) {
      assertTrue(visited.contains(itr.next()));    
    }
  }
  
  public void test_visitEdges() {
    final HashSet visited = new HashSet();
    GraphVisitor visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        visited.add(component);
        return(0);
      }
    };
    
    m_graph.visitEdges(visitor);
    
    for (Iterator itr = m_edges.iterator(); itr.hasNext();) {
      assertTrue(visited.contains(itr.next()));    
    }
  }
  
  public void test_getNodesOfDegree() {
    List nodes = m_graph.getNodesOfDegree(1);
    assertTrue(nodes.contains(m_nodes.get(0)));
    assertTrue(nodes.contains(m_nodes.get(m_nodes.size()-1)));
    
    nodes = m_graph.getNodesOfDegree(2);
    assertTrue(nodes.contains(m_nodes.get(1)));
    assertTrue(nodes.contains(m_nodes.get(2)));
  }
  
  public void test_getVisitedNodes() {
    ((BasicNode)m_nodes.get(1)).setVisited(true);
    ((BasicNode)m_nodes.get(2)).setVisited(true);
    
    List visited = m_graph.getVisitedNodes(true);
    assertTrue(visited.size() == 2);
    assertTrue(visited.contains(m_nodes.get(1)));
    assertTrue(visited.contains(m_nodes.get(2)));
    
    visited = m_graph.getVisitedNodes(false);
    assertTrue(visited.size() == 2);
    assertTrue(visited.contains(m_nodes.get(0)));
    assertTrue(visited.contains(m_nodes.get(3)));
  }
  
  public void test_getVisitedEdges() {
    ((BasicEdge)m_edges.get(1)).setVisited(true);
    
    List visited = m_graph.getVisitedEdges(true);
    assertTrue(visited.size() == 1);
    assertTrue(visited.contains(m_edges.get(1)));
    
    visited = m_graph.getVisitedEdges(false);
    assertTrue(visited.size() == 2);
    assertTrue(visited.contains(m_edges.get(0)));
    assertTrue(visited.contains(m_edges.get(2)));
  }
  
  public void test_initNodes() {
    for (Iterator itr = m_nodes.iterator(); itr.hasNext();) {
      BasicNode n = (BasicNode)itr.next();
      n.setVisited(true);
      n.setCount(100); 
    }
    
    m_graph.initNodes();
    
    for (Iterator itr = m_nodes.iterator(); itr.hasNext();) {
      BasicNode n = (BasicNode)itr.next();
      assertTrue(!n.isVisited());
      assertTrue(n.getCount() == 0);
    }
  }
  
  public void test_initEdges() {
    for (Iterator itr = m_edges.iterator(); itr.hasNext();) {
      BasicEdge e = (BasicEdge)itr.next();
      e.setVisited(true);
      e.setCount(100); 
    }
    
    m_graph.initEdges();
    
    for (Iterator itr = m_edges.iterator(); itr.hasNext();) {
      BasicEdge e = (BasicEdge)itr.next();
      assertTrue(!e.isVisited());
      assertTrue(e.getCount() == 0);
    }
  }
}
