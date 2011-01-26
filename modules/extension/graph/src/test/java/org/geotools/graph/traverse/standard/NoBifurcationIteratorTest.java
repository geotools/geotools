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

import junit.framework.TestCase;

import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.basic.CountingWalker;

public class NoBifurcationIteratorTest extends TestCase {
  
  private GraphBuilder m_builder;
  
  public NoBifurcationIteratorTest(String name) {
     super(name);  
  }
  
  protected void setUp() throws Exception {
    super.setUp();
  
    m_builder = createBuilder();
  }

  /**
   * Create a simple graph which has no bifurcations and do a normal traversal. 
   * <BR>
   * <BR>
   * Expected: 1. Every node should be visited.
   *           2. Nodes should be visited in order.
   */
  public void test_0() {
    int nnodes = 100;
    Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);

    CountingWalker walker = new CountingWalker() {
      public int visit(Graphable element, GraphTraversal traversal) {
        super.visit(element, traversal);
        element.setCount(getCount()-1);
        
        //nodes should be visited in order
        assertTrue(element.getID() == getCount()-1);
        return(GraphTraversal.CONTINUE);
      }
    };
        
    NoBifurcationIterator iterator = createIterator();
    iterator.setSource(ends[0]);
    
    BasicGraphTraversal traversal = new BasicGraphTraversal(
      builder().getGraph(), walker, iterator
    );
    traversal.init();
    traversal.traverse();
    
    //every node should have been visited
    assertTrue(walker.getCount() == builder().getGraph().getNodes().size());
  } 
  
   /**
   * Create a simple graph which has no bifurcations and do a traversal 
   * suspending at some intermediate node. Then continue traversal.
   * 
   * Expected: After suspend:
   *           1. Every node of with an id greater than the id of the suspending
   *              node should not be visited.
   *           After continue:
   *           1. First node visited after continue should have id = 
   *              id + suspend node + 1 
   *           2. Every node should be visited. 
   * 
   */
  public void test_1() {
    int nnodes = 100;
    Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);
    final int suspend = 50;; 
    
    CountingWalker walker = new CountingWalker() {
      private int m_mode = 0; 
      
      public int visit(Graphable element, GraphTraversal traversal) {
        super.visit(element, traversal);
        if (m_mode == 0) {
          //check for stopping node
          if (element.getID() == suspend) {
            m_mode++; 
            return(GraphTraversal.SUSPEND);
          }
        }
        else if (m_mode == 1) {
          //check first node after continue
          assertTrue(element.getID() == suspend + 1);
          m_mode++;
        }
        return(GraphTraversal.CONTINUE);
      }
    };
    
    NoBifurcationIterator iterator = createIterator();
    iterator.setSource(ends[0]);
    
    BasicGraphTraversal traversal = new BasicGraphTraversal(
      builder().getGraph(), walker, iterator
    );
    traversal.init();
    traversal.traverse();
    
    //stopping node should be visited and nodes with greater id should not
    GraphVisitor visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        if (component.getID() <= suspend) assertTrue(component.isVisited()); 
        else assertTrue(!component.isVisited());
        return(0);
      }
    };
    builder().getGraph().visitNodes(visitor);
    assertTrue(walker.getCount() == nnodes-suspend+1);
    
    traversal.traverse();
    
    //every node should now be visited
    visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        assertTrue(component.isVisited());   
        return(0);
      }
    };
    builder().getGraph().visitNodes(visitor);
    assertTrue(walker.getCount() == nnodes);
  }  
  
  /**
   * Create a simple graph which has no bifurcations and do a kill branch at 
   * some intermediate node. Then continue the traversal. 
   * 
   * Expected: After kill:
   *           1. Every node of with an id greater than the id of the killing
   *              node should not be visited.
   *           After continue:
   *           2. No more nodes should be visited. 
   * 
   */
  public void test_2() {
    int nnodes = 100;
    Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), 100);
    final int kill = 50;
    
    CountingWalker walker = new CountingWalker() {
      private int m_mode = 0; 
      
      public int visit(Graphable element, GraphTraversal traversal) {
        super.visit(element, traversal);
        if (m_mode == 0) {
          //check for stopping node
          if (element.getID() == kill) {
            m_mode++; 
            return(GraphTraversal.KILL_BRANCH);
          }
        }
        else if (m_mode == 1) {
          //should never get here
          assertTrue(false);
        }
        return(GraphTraversal.CONTINUE);
      }
    };
    
    NoBifurcationIterator iterator = createIterator();
    iterator.setSource(ends[0]);
    
    BasicGraphTraversal traversal = new BasicGraphTraversal(
      builder().getGraph(), walker, iterator
    );
    traversal.init();
    traversal.traverse();
    
    //kill node should be visited and nodes with greater id should not
    GraphVisitor visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        if (component.getID() <= kill) assertTrue(component.isVisited()); 
        else assertTrue(!component.isVisited());
        return(0);
      }
    };
    builder().getGraph().visitNodes(visitor);
    assertTrue(walker.getCount() == nnodes-kill+1);
    
    //continue, no more nodes should be visited
    traversal.traverse();
  }  
  
  /**
   * Create a simple graph with a single bifurcation and do a full traversal.
   * 
   * Exepected: 1. The traversal should stop at the bifurcating node.
   *            2. Every node after the bifurcating node should not be visited.
   */
  public void test_3() {
    int nnodes = 100;
    final int bif = 50;
    Node[] ends = GraphTestUtil.buildSingleBifurcation(builder(), nnodes, bif);
    
    CountingWalker walker = new CountingWalker();
    NoBifurcationIterator iterator = createIterator();
    iterator.setSource(ends[0]);
    
    BasicGraphTraversal traversal = new BasicGraphTraversal(
      builder().getGraph(), walker, iterator 
    );
    traversal.init();
    traversal.traverse();
    
    GraphVisitor visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        if (component.getID() < bif) {
          assertTrue(component.isVisited());  
        }  
        else if (component.getID() >= bif) {
          assertTrue(!component.isVisited());  
        }
       
        return(0); 
      }
    };
    builder().getGraph().visitNodes(visitor);
    assertTrue(walker.getCount() == nnodes-bif);
  }
  
  /**
    * Create a graph that contains a cycle and do a full traversal.<BR>
    * <BR>
    * Expected: 1. All nodes visited.
    */
   public void test_4() {
     int nnodes = 100;
     GraphTestUtil.buildCircular(builder(), nnodes);
     GraphVisitor visitor = new GraphVisitor() {
        public int visit(Graphable component) {
          if (component.getID() == 50) return(Graph.PASS_AND_CONTINUE);
          return(Graph.FAIL_QUERY);  
        }
     };
     Node source = (Node)builder().getGraph().queryNodes(visitor).get(0);
     
     CountingWalker walker = new CountingWalker();
     NoBifurcationIterator iterator = createIterator();
     iterator.setSource(source);
     
     BasicGraphTraversal traversal = new BasicGraphTraversal(
       builder().getGraph(), walker, iterator
     );
     traversal.init();
     traversal.traverse();
     
     //ensure all nodes visisited
     visitor = new GraphVisitor() {
       public int visit(Graphable component) {
         assertTrue(component.isVisited());
         return(0);
       }
     };
     
     builder().getGraph().visitNodes(visitor);
     
     assertTrue(walker.getCount() == nnodes);
  }
   
  protected GraphBuilder createBuilder() {
    return(new BasicGraphBuilder());  
  }
 
  protected GraphBuilder builder() {
    return(m_builder);  
  } 
  
  protected NoBifurcationIterator createIterator() {
    return(new NoBifurcationIterator());  
  }
}
