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
import java.util.Map;

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

public class BreadthFirstIteratorTest extends TestCase {
 
  private GraphBuilder m_builder;
  
  public BreadthFirstIteratorTest(String name) {
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
        element.setCount(getCount());
        super.visit(element, traversal);
        
        //nodes should be visited in order
        assertTrue(element.getID() == getCount()-1);
        return(GraphTraversal.CONTINUE);
      }
    };
        
    BreadthFirstIterator iterator = createIterator();
    BasicGraphTraversal traversal = new BasicGraphTraversal(
      builder().getGraph(), walker, iterator
    );
    traversal.init();
    
    iterator.setSource(ends[0]);
    traversal.traverse();
    
    //every node should have been visited
    assertTrue(walker.getCount() == builder().getGraph().getNodes().size());
    
    //ensure nodes only visited once
    assertTrue(walker.getCount() == nnodes);
    
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
   */
  public void test_1() {
    int nnodes = 100;
    Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);
    final int suspend = 50;
    
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
    
    BreadthFirstIterator iterator = createIterator();
    BasicGraphTraversal traversal = new BasicGraphTraversal(
      builder().getGraph(), walker, iterator
    );
    traversal.init();
    
    iterator.setSource(ends[0]);
    traversal.traverse();
    
    //ensure nodes only visited once
    assertTrue(walker.getCount() == nnodes-suspend+1);
    
    //stopping node should be visited and nodes with greater id should not
    GraphVisitor visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        if (component.getID() <= suspend) assertTrue(component.isVisited()); 
        else assertTrue(!component.isVisited());
        return(0);
      }
    };
    builder().getGraph().visitNodes(visitor);
    
    traversal.traverse();
    
    //every node should now be visited
    visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        assertTrue(component.isVisited());   
        return(0);
      }
    };
    builder().getGraph().visitNodes(visitor);
    
    //ensure nodes only visited once
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
    Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);
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
    
    BreadthFirstIterator iterator = createIterator();
    BasicGraphTraversal traversal = new BasicGraphTraversal(
      builder().getGraph(), walker, iterator
    );
    traversal.init();
    
    iterator.setSource(ends[0]);
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
    
    //ensure nodes only visited once
    assertTrue(walker.getCount() == nnodes-kill+1);
    
    //continue, no more nodes should be visited
    
    traversal.traverse();
    
    //ensure nodes only visited once
    assertTrue(walker.getCount() == nnodes-kill+1);
  }  
  
  /**
   * Create a balanced binary tree and do a normal traversal starting at root. 
   * <BR>
   * <BR>
   * Expected: 1. Every node should be visited.
   *           2. For each level in the tree, each node in the level should 
   *              be visited before any node in a lower level
   */
   public void test_3() {
     int k = 4;
     Object[] obj = GraphTestUtil.buildPerfectBinaryTree(builder(), k);
     Node root = (Node)obj[0];
     final Map obj2node = (Map)obj[1];
     
     GraphVisitor initializer = new GraphVisitor() {
      public int visit(Graphable component) {
        component.setCount(-1);
        return 0;
      }
     };
     
     CountingWalker walker = new CountingWalker() {
      public int visit(Graphable element, GraphTraversal traversal) {
        element.setCount(getCount());
        return super.visit(element, traversal);
      }
     };
     
     BreadthFirstIterator iterator = createIterator();
     BasicGraphTraversal traversal = new BasicGraphTraversal(
       builder().getGraph(), walker, iterator
     );
     traversal.init();
     
     iterator.setSource(root);
     traversal.traverse();
     
     GraphVisitor visitor = new GraphVisitor() {
      public int visit(Graphable component) {
        //ensure component visited
        assertTrue(component.isVisited());
        
        int level = component.getObject().toString().length();
        
        //check all nodes that are at a lower level in the tree
        for (
          Iterator itr = builder().getGraph().getNodes().iterator(); 
          itr.hasNext();
        ) {
          Node other = (Node)itr.next();
          if (other.getObject().toString().length() > level) 
            assertTrue(other.getCount() > component.getCount());
            
        } 
        return 0;
      }
     };
     
     builder().getGraph().visitNodes(visitor);
     
     //ensure nodes visited once
     assertTrue(walker.getCount() == Math.pow(2,k+1)-1);
   }
   
  /**
   * Create a balanced binary tree and do a traversal starting at root and 
   * suspending at the first node seen that is not the root, (should be left 
   * child). Then continue the traversal.
   * <BR>
   * <BR>
   * Expected: After suspend:
   *           1. Only root and first non root should be visited.
   *           
   *           After continue:
   *           1. First node visited should be sibling of suspending node.
   *           2. Every node should be visited.
   */
   public void test_4() {
     int k = 4;
     Object[] obj = GraphTestUtil.buildPerfectBinaryTree(builder(), k);
     final Node root = (Node)obj[0];
     final Map obj2node = (Map)obj[1];
     final Node ln = (Node)obj2node.get(root.getObject().toString() + ".0");
     final Node rn = (Node)obj2node.get(root.getObject().toString() + ".1");
     
     CountingWalker walker = new CountingWalker() {
       private int m_mode = 0;
       
       public int visit(Graphable element, GraphTraversal traversal) {
         super.visit(element, traversal);
         if (m_mode == 0) {
           if (element != root) {
             //check which child of root was first visited
             m_mode++;
             return(GraphTraversal.SUSPEND);  
           }   
         }  
         else if (m_mode == 1) {
           String eid = element.getObject().toString();
           if (ln.isVisited()) assertTrue(element == rn);  
           else assertTrue(element == ln);
            
           m_mode++;  
         }
         
         return(GraphTraversal.CONTINUE);
       }
     };
          
     BreadthFirstIterator iterator = createIterator();
     BasicGraphTraversal traversal = new BasicGraphTraversal(
       builder().getGraph(), walker, iterator
     );
     traversal.init();
     
     iterator.setSource(root);
     traversal.traverse();
     
     //ensure that only root and one of children is visited
     assertTrue(root.isVisited());
     assertTrue(
       (rn.isVisited() && !ln.isVisited()) || 
       (!rn.isVisited() && ln.isVisited())
     ); 
     
     GraphVisitor visitor = new GraphVisitor() {
       public int visit(Graphable component) {
         if (component != root && component != ln && component != rn) {
           assertTrue(!component.isVisited());  
         } 
         return(0);
       }
     };
     builder().getGraph().visitNodes(visitor);
     
     //ensure nodes only visited once
     assertTrue(walker.getCount() == 2);
     
     traversal.traverse();
     
     //ensure all nodes visited
     visitor = new GraphVisitor() {
       public int visit(Graphable component) {
         assertTrue(component.isVisited());
         return(0);
       }
     };
     
     builder().getGraph().visitNodes(visitor);
     
     //ensure nodes visited once
     //ensure nodes only visited once
     assertTrue(walker.getCount() == (int)Math.pow(2,k+1)-1);
   }
   
   /**
   * Create a balanced binary tree and do a traversal starting at root and kill 
   * branch at the first node seen that is not the root, (should be left child). 
   * Then continue the traversal.
   * <BR>
   * <BR>
   * Expected: After kill:
   *           1. All nodes should be visited except for sub nodes of first 
   *              child of root visited. (the kill node)
   *           
   *           After continue:
   *           1. Same as after kill.
   */
   public void test_5() {
     int k = 4;
     Object[] obj = GraphTestUtil.buildPerfectBinaryTree(builder(), k);
     final Node root = (Node)obj[0];
     final Map obj2node = (Map)obj[1];
     final Node ln = (Node)obj2node.get(root.getObject().toString() + ".0");
     final Node rn = (Node)obj2node.get(root.getObject().toString() + ".1");
     
     CountingWalker walker = new CountingWalker() {
       private int m_mode = 0;
       
       public int visit(Graphable element, GraphTraversal traversal) {
         super.visit(element, traversal); //set count
         element.setCount(getCount()-1);
         if (m_mode == 0) {
           if (element != root) {
             m_mode++;
             return(GraphTraversal.KILL_BRANCH);  
           }   
         }  
         else if (m_mode == 1) {
           assertTrue(
             (ln.isVisited() && element == rn) || 
             (rn.isVisited() && element == ln)
           );
           m_mode++;  
         }
         
         return(GraphTraversal.CONTINUE);
       }
     };
          
     BreadthFirstIterator iterator = createIterator();
     BasicGraphTraversal traversal = new BasicGraphTraversal(
       builder().getGraph(), walker, iterator
     );
     traversal.init();
     
     iterator.setSource(root);
     traversal.traverse();
     
     //ensure that subnodes of first visited after root are not visited 
     final String id = (ln.getCount() < rn.getCount()) ? 
                   ln.getObject().toString() : 
                   rn.getObject().toString();
     
     GraphVisitor visitor = new GraphVisitor() {
       public int visit(Graphable component) {
         String eid = component.getObject().toString();
         if (eid.length() <= id.length()) assertTrue(component.isVisited());
         else if (eid.startsWith(id)) assertTrue(!component.isVisited());
         else assertTrue(component.isVisited());
         
         return(0);
       }
     };
     builder().getGraph().visitNodes(visitor);
     
     //ensure that nodes only visited once
     assertTrue(walker.getCount() == (int)Math.pow(2,k)+1);
     traversal.traverse();
     
     builder().getGraph().visitNodes(visitor);
     
     //ensure that nodes only visited once
     assertTrue(walker.getCount() == (int)Math.pow(2,k)+1);
   }
   
   /**
    * Create a graph that contains a cycle and do a full traversal.<BR>
    * <BR>
    * Expected: 1. All nodes visited.
    */
   public void test_6() {
     GraphTestUtil.buildCircular(builder(), 100);
     GraphVisitor visitor = new GraphVisitor() {
        public int visit(Graphable component) {
          if (component.getID() == 50) return(Graph.PASS_AND_CONTINUE);
          return(Graph.FAIL_QUERY);  
        }
     };
     Node source = (Node)builder().getGraph().queryNodes(visitor).get(0);
     
     CountingWalker walker = new CountingWalker();
     BreadthFirstIterator iterator = createIterator();
     
     BasicGraphTraversal traversal = new BasicGraphTraversal(
       builder().getGraph(), walker, iterator
     );
     traversal.init();
     
     iterator.setSource(source);
     traversal.traverse();
     
     //ensure all nodes visisited
     visitor = new GraphVisitor() {
       public int visit(Graphable component) {
         assertTrue(component.isVisited());
         return(0);
       }
     };
     
     builder().getGraph().visitNodes(visitor);
     
     //ensure all nodes only visitied once
     assertTrue(walker.getCount() == builder().getGraph().getNodes().size());
   }
  
  protected GraphBuilder createBuilder() {
    return(new BasicGraphBuilder());  
  }
  
  protected GraphBuilder builder() {
    return(m_builder);
  }
  
  protected BreadthFirstIterator createIterator() {
    return(new BreadthFirstIterator());  
  }

}
