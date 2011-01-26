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

import java.util.Iterator;

import junit.framework.TestCase;

import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.basic.CountingWalker;

public class DepthFirstTopologicalIteratorTest extends TestCase {
   private GraphBuilder m_builder;
   
  public DepthFirstTopologicalIteratorTest(String name) {
    super(name);  
  }
  
  protected void setUp() throws Exception {
    super.setUp();
  
    m_builder = createBuilder();
  }
  
  /**
   * Create a graph with no bifurcations and do a full traversal. <BR>
   * <BR>
   * Expected: 1. Nodes should be visited from one end to other in order.
   */ 
  public void test_0() {
    int nnodes = 100;
    GraphTestUtil.buildNoBifurcations(builder(), nnodes);
    
    CountingWalker walker = new CountingWalker() {
      public int visit(Graphable element, GraphTraversal traversal) {
        element.setCount(getCount());
        return super.visit(element, traversal);
      }
    };  
    DepthFirstTopologicalIterator iterator = createIterator();
    BasicGraphTraversal traversal = new BasicGraphTraversal(
      builder().getGraph(), walker, iterator 
    );
    traversal.init();
    traversal.traverse();
    
    assertTrue(walker.getCount() == nnodes);
    
    boolean flip = false;
    
    for (
      Iterator itr = builder().getGraph().getNodes().iterator();
      itr.hasNext();
    ) {
      Node node = (Node)itr.next();
      if (node.getID() == 0 && node.getCount() != 0) {
        flip = true;
        break;
      }
    }
    
    for (
      Iterator itr = builder().getGraph().getNodes().iterator();
      itr.hasNext();
    ) {
      Node node = (Node)itr.next();
      if (flip) assertTrue(node.getCount() == 100-1-node.getID());
      else assertTrue(node.getCount() == node.getID());    
    }
  }
  
  /**
   * Create a circular graph and do a full traversal. <BR>
   * <BR>
   * Expected: 1. No nodes should be visited.
   *
   */
  public void test_1() {
    int nnodes = 100;
    GraphTestUtil.buildCircular(builder(), nnodes);
    
    CountingWalker walker = new CountingWalker();
    BreadthFirstTopologicalIterator iterator = createIterator();
    
    BasicGraphTraversal traversal = new BasicGraphTraversal(
      builder().getGraph(), walker, iterator 
    );  
    traversal.init();
    traversal.traverse();
    
    GraphVisitor visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        assertTrue(!component.isVisited());
        return 0;
      }
    };
    builder().getGraph().visitNodes(visitor);
    
    assertTrue(walker.getCount() == 0);
  }
  
  protected GraphBuilder createBuilder() {
    return(new BasicGraphBuilder());  
  }
  
  protected GraphBuilder builder() {
    return(m_builder);   
  }
  
  protected DepthFirstTopologicalIterator createIterator() {
    return(new DepthFirstTopologicalIterator());  
  }
}
