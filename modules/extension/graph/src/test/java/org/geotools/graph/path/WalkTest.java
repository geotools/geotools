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
package org.geotools.graph.path;

import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.TestCase;

import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.GraphWalker;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.standard.NoBifurcationIterator;

public class WalkTest extends TestCase {
 
  private GraphBuilder m_builder;
   
  public WalkTest(String name) {
    super(name);  
  } 
   
  protected void setUp() throws Exception {
    super.setUp();
    m_builder = createBuilder();
  }

  public void test_add() {
    Node n = builder().buildNode();
    Walk walk = new Walk();
    
    walk.add(n);
     
    assertTrue(walk.size() == 1);
    assertTrue(walk.get(0).equals(n));  
  }
   
  public void test_remove() {
    Node n = builder().buildNode();
    Walk walk = new Walk();
    
    walk.add(n);
    assertTrue(!walk.isEmpty());
    
    walk.remove(n);
    assertTrue(walk.isEmpty());
  }
  
  public void test_reverse() {
    ArrayList nodes = new ArrayList();
    Walk walk = new Walk();
    
    for (int i = 0; i < 10; i++) {
      Node n = builder().buildNode();
      nodes.add(n);
      walk.add(n);  
    }
    
    Iterator itr = walk.iterator();
    for (int i = 0; i < nodes.size();i++) {
      Node n1 = (Node)nodes.get(i);
      Node n2 = (Node)itr.next();
      
      assertTrue(n1 == n2);  
    }
    
    walk.reverse();
    itr = walk.iterator();
    
    for (int i = nodes.size()-1; i >= 0; i--) {
      Node n1 = (Node)nodes.get(i);
      Node n2 = (Node)itr.next();
      
      assertTrue(n1 == n2);  
    }
  }
  
  
  public void test_isClosed() {
    Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), 10);
    final Walk walk = new Walk();
    
    NoBifurcationIterator iterator = new NoBifurcationIterator();
    iterator.setSource(ends[0]);
    
    GraphWalker walker = new GraphWalker() {
      public int visit(Graphable element, GraphTraversal traversal) {
        walk.add(element);
        return(GraphTraversal.CONTINUE);
      }

      public void finish() {
       
      }
    };
    
    BasicGraphTraversal traversal = new BasicGraphTraversal(
      builder().getGraph(), walker, iterator  
    );
    traversal.init();
    traversal.traverse();
    
    assertTrue(walk.size() == builder().getGraph().getNodes().size());
    assertTrue(walk.isValid() && !walk.isClosed());
    
    //create a new edges in the graph making the graph a cycle
    Edge e = builder().buildEdge(ends[0], ends[1]);
    builder().addEdge(e);
    
    walk.add(ends[0]);
    assertTrue(walk.isValid() && walk.isClosed());
  }
  
  public void test_getEdges() {
    Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), 10);
    final Walk walk = new Walk();
    
    NoBifurcationIterator iterator = new NoBifurcationIterator();
    iterator.setSource(ends[0]);
    
    GraphWalker walker = new GraphWalker() {
      public int visit(Graphable element, GraphTraversal traversal) {
        walk.add(element);
        return(GraphTraversal.CONTINUE);
      }

      public void finish() {
       
      }
    };
    
    BasicGraphTraversal traversal = new BasicGraphTraversal(
      builder().getGraph(), walker, iterator  
    );
    traversal.init();
    traversal.traverse();
    
    assertTrue(walk.getEdges() != null);
    assertTrue(walk.isValid());
  }
  
  public void test_truncate_0() {
  	Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), 10);
    final Walk walk = new Walk();
    
    NoBifurcationIterator iterator = new NoBifurcationIterator();
    iterator.setSource(ends[0]);
    
    GraphWalker walker = new GraphWalker() {
      public int visit(Graphable element, GraphTraversal traversal) {
        walk.add(element);
        return(GraphTraversal.CONTINUE);
      }

      public void finish() {
       
      }
    };
    
    BasicGraphTraversal traversal = new BasicGraphTraversal(
      builder().getGraph(), walker, iterator  
    );
    traversal.init();
    traversal.traverse();
    
    walk.truncate(0);
    assertTrue(walk.isEmpty());
    assertTrue(walk.isValid());
  }
  
  public void test_truncate_1() {
  	Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), 10);
    final Walk walk = new Walk();
    
    NoBifurcationIterator iterator = new NoBifurcationIterator();
    iterator.setSource(ends[0]);
    
    GraphWalker walker = new GraphWalker() {
    	int count = 0;
      public int visit(Graphable element, GraphTraversal traversal) {
        walk.add(element);
        return(GraphTraversal.CONTINUE);
      }

      public void finish() {
       
      }
    };
    
    BasicGraphTraversal traversal = new BasicGraphTraversal(
      builder().getGraph(), walker, iterator  
    );
    traversal.init();
    traversal.traverse();
    
    int size = walk.size();
    walk.truncate(size / 2);
  	assertTrue(walk.size() == size / 2);
  }
  
  public void test_truncate_2() {
  	Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), 11);
    final Walk walk = new Walk();
    
    NoBifurcationIterator iterator = new NoBifurcationIterator();
    iterator.setSource(ends[0]);
    
    GraphWalker walker = new GraphWalker() {
    	int count = 0;
      public int visit(Graphable element, GraphTraversal traversal) {
        walk.add(element);
        return(GraphTraversal.CONTINUE);
      }

      public void finish() {
       
      }
    };
    
    BasicGraphTraversal traversal = new BasicGraphTraversal(
      builder().getGraph(), walker, iterator  
    );
    traversal.init();
    traversal.traverse();
    
    int size = walk.size();
    walk.truncate(size / 2);
  	assertTrue(walk.size() == size / 2);
  }
  
  protected GraphBuilder createBuilder() {
    return(new BasicGraphBuilder());  
  }
   
  protected GraphBuilder builder() {
    return(m_builder);  
  }
   
  
}
