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

import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.path.DijkstraShortestPathFinder;
import org.geotools.graph.path.Path;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.standard.DijkstraIterator;

public class DijkstraShortestPathFinderTest extends TestCase {  

  private GraphBuilder m_builder;
  
  public DijkstraShortestPathFinderTest(String name) {
    super(name);  
  }
  
  protected void setUp() throws Exception {
    super.setUp();
    
    m_builder = createBuilder();
  }
  
  /**
   * Create a graph with no bifurcations and calculate path from beginning
   * to end. <BR>
   * <BR>
   * Expected: 1. Path should contain every node in graph in order.
   */
  public void test_0() {
    int nnodes = 100;
    Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);
    DijkstraShortestPathFinder pfinder = new DijkstraShortestPathFinder(
      builder().getGraph(), ends[0], costFunction()
    );
    
    pfinder.calculate();
    Path p = pfinder.getPath(ends[1]);
    
    int count = 99;
    for (Iterator itr = p.iterator(); itr.hasNext();) {
      Node n = (Node)itr.next();
      assertTrue(n.getID() == count--);
    }
  }
  
  /**
   * Create a circular graph and calculate a path from beginning to end. <BR>
   * <BR>
   * Expected: 1. Path should just contain end nodes.
   *
   */
  public void test_1() {
    int nnodes = 100;
    Node[] ends = GraphTestUtil.buildCircular(builder(), nnodes);
    
    DijkstraShortestPathFinder pfinder = new DijkstraShortestPathFinder(
      builder().getGraph(), ends[0], costFunction()
    );
    
    pfinder.calculate();
    Path p = pfinder.getPath(ends[1]);
    
    assertTrue(p.size() == 2);
    assertTrue(p.get(0) == ends[1]);
    assertTrue(p.get(1) == ends[0]);
  }
  
  /**
   * Create a balanced binary tree and calculate a path from root to a leaf. <BR>
   * <BR>
   * Expected 1. Path should be links from leaf to root. 
   */
  public void test_2() {
    int k = 4;
    Object[] obj = GraphTestUtil.buildPerfectBinaryTree(builder(), k);
    Node root = (Node)obj[0];
    Map id2node = (Map)obj[1];
    
    DijkstraShortestPathFinder pfinder = new DijkstraShortestPathFinder(
      builder().getGraph(), root, costFunction()
    );
    pfinder.calculate();
    
    for (Iterator itr = builder().getGraph().getNodes().iterator(); itr.hasNext();) {
      Node node = (Node)itr.next();
      String id = node.getObject().toString();
      
      if (id2node.get(id + ".0") == null) {
        Path p = pfinder.getPath(node);
        assertTrue(p.size() == k+1);  
     
        for (Iterator pitr = p.iterator(); pitr.hasNext();) {
          Node n = (Node)pitr.next();
          assertTrue(n.getObject().toString().equals(id));
          if (id.length() > 2) id = id.substring(0, id.length()-2);
        }    
      }  
    }
  }

  protected DijkstraIterator.EdgeWeighter costFunction() {
    return(
      new DijkstraIterator.EdgeWeighter() {
        public double getWeight(Edge e) {
          return 1;
        }
      }
    );
  }
  
  protected GraphBuilder createBuilder() {
    return(new BasicGraphBuilder());  
  }
  
  protected GraphBuilder builder() {
    return(m_builder);  
  }

}
