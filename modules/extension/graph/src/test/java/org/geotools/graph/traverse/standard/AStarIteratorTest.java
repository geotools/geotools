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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.path.AStarShortestPathFinder;
import org.geotools.graph.path.Path;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.basic.CountingWalker;
import org.geotools.graph.traverse.standard.AStarIterator.AStarNode;

public class AStarIteratorTest extends TestCase{
	 public GraphBuilder m_builder;
	   
	  public AStarIteratorTest(String name) {
	    super(name);  
	  }
	  
	  protected void setUp() throws Exception {
	    super.setUp();  
	  
	    m_builder = createBuilder();  
	  }
	  /** Test 0:
	   *  Graph with no bifurcations. 100 nodes.
	   *  G = 1 for all edges
	   *  H = TargetsID - currentID
	   *  Expected:
	   *  	1. Every node should be visited
	   *  	2. ParentID = SonsID - 1
	   * 
	   */
	  public void test_0() {
		    final int nnodes = 100;
		    Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);
		    
		    CountingWalker walker = new CountingWalker();
		    
		    final AStarIterator iterator = createIterator(ends[0],ends[ends.length-1]);
		    
		    BasicGraphTraversal traversal = new BasicGraphTraversal(
		      builder().getGraph(), walker, iterator
		    );
		    traversal.init();
		    traversal.traverse();
		    
		    GraphVisitor visitor = new GraphVisitor() {
		      public int visit(Graphable component) {
		    	assertTrue(component.isVisited());
		        if (component.getID() == 0) assertNull(iterator.getParent((Node) component));
		        else assertTrue(component.getID() == iterator.getParent((Node)component).getID() + 1);
		        return 0;
		      }
		    };
		    builder().getGraph().visitNodes(visitor);
		    
		    assertTrue(walker.getCount() == nnodes);
		  }
	  
	  /**
	   * Create a graph with no bifurcations and start a traversal from start
	   * node, then suspend, and resume. <BR>
	   * <BR>
	   * Expected: After suspend:
	   *           1. Nodes from 0 to suspend node should be visted, others not.
	   *          
	   *           After resume:
	   *           1. Next node visited should have id suspend node id + 1
	   *           2. Every node should have a parent with id node id + 1
	   */
	  public void test_1() {
	    int nnodes = 100;
	    Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);
	    final int suspend = 50;
	    
	    CountingWalker walker = new CountingWalker() {
	      int m_mode = 0;
	      public int visit(Graphable element, GraphTraversal traversal) {
	        super.visit(element, traversal);
	        if (m_mode == 0) {
	          if (element.getID() == suspend) {
	            m_mode++; 
	            return(GraphTraversal.SUSPEND);
	          }  
	        }
	        else if (m_mode == 1) {
	          assertTrue(element.getID() == suspend+1);
	          m_mode++;  
	        }
	        return(GraphTraversal.CONTINUE); 
	      }
	    };
	    
	    final AStarIterator iterator = createIterator(ends[0],ends[ends.length-1]);
	    
	    BasicGraphTraversal traversal = new BasicGraphTraversal(
	      builder().getGraph(), walker, iterator
	    );
	    traversal.init();
	    traversal.traverse();
	    
	    GraphVisitor visitor = new GraphVisitor() {
	      public int visit(Graphable component) {
	        if (component.getID() <= suspend) assertTrue(component.isVisited());
	        else assertTrue(!component.isVisited());
	        return 0;
	      }
	    };
	    builder().getGraph().visitNodes(visitor);
	    assertTrue(walker.getCount() == nnodes-suspend+1);
	    
	    //resume
	    traversal.traverse();
	    
	    visitor = new GraphVisitor() {
	      public int visit(Graphable component) {
	        assertTrue(component.isVisited());
	        if (component.getID() == 0) assertNull(iterator.getParent((Node)component));
	        else assertTrue(
	          iterator.getParent((Node)component).getID() == component.getID() - 1
	        );
	            
	        return 0;
	      }
	    };
	    builder().getGraph().visitNodes(visitor);
	    assertTrue(walker.getCount() == nnodes);
	  }
	  
	  /**
	   * Create a balanced binary tree and do a normal traversal starting at root. 
	   * <BR>
	   * <BR>
	   * Expected: 1. #nodes_visited <= #nodes
	   *           2. The parent of each node should be the same as 
	   *              the parent of the tree.
	   *           3. G = depth. 
	   *           	  H = infinity if the target is not in any subtree of this node or
	   *                  depth difference between target node and current node otherwise. 
	   * @throws WrongPathException 
	   *      
	   */
	  public void test_3() {
	    int k = 4;
	    Object[] obj = GraphTestUtil.buildPerfectBinaryTree(builder(), k);
	    final Node root = (Node)obj[0];
	    final HashMap map = (HashMap) obj[1];
	    HashMap rmap = new HashMap();
	    Map.Entry[] set = new Map.Entry[map.size()];
	    map.entrySet().toArray(set);
	    for (int i = 0; i < set.length; i++) {
	    	rmap.put(set[i].getValue(), set[i].getKey());
	    }
	    final HashMap hashmap = rmap;
	    
    	class Factory {
		    public AStarIterator.AStarFunctions createFunctions(Node target) {
				  return
				  (
						new AStarIterator.AStarFunctions(target) {
							public double cost(AStarNode n1, AStarNode n2){
								return 1;
							}
							public double h(Node n){
								String dest = hashmap.get(this.getDest()).toString();	
								String current = hashmap.get(n).toString();
								if (dest.startsWith(current)) {
									// n under dest
									dest=dest.replaceAll("\\D", "");
									current = current.replaceAll("\\D", "");
									return dest.length()-current.length();
								} else {
									return Double.POSITIVE_INFINITY;
								}
								
							}
				       	}
				  );
		    }
		}
    	Factory f = new Factory();
    	
	    AStarShortestPathFinder walker = 
	    		new AStarShortestPathFinder(builder().getGraph(),root,((Node) map.get("0.1.0.1")),
	    				                    f.createFunctions(((Node) map.get("0.1.0.1"))));
	    
	    walker.calculate();
	    MyVisitor visitor = new MyVisitor(); 
	    builder().getGraph().visitNodes(visitor);
	    //#1
	    assertTrue(visitor.count > 0);
	    assertTrue(visitor.count < map.size() + 1);
	    Path p = null;
		try {
			p = walker.getPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    p.getEdges();
	    assertTrue(p.size() == 4);
	    // #2
	    for (int j = 0; j < p.size() - 1; j++) {
	    	Node n = (Node) p.get(j);
	    	Node parent = (Node) p.get(j + 1);
	    	String n_id = rmap.get(n).toString();
	    	String parent_id = rmap.get(parent).toString();
	    	assertTrue(n_id.startsWith(parent_id));
	    }
	  }
	  

	  
      private class MyVisitor implements GraphVisitor {
         public int count = 0;
		  
		 public int visit(Graphable component) {
			if (component.isVisited()) {
	    	   count++;
	    	}
	        return 0;
		 }
		  
      }

	  protected GraphBuilder createBuilder() {
		  return(new BasicGraphBuilder());  
	  }
		  
	  protected GraphBuilder builder() {
		  return(m_builder);  
	  }
	  
	  protected AStarIterator createIterator(Node source, Node target) {
		  return
		  (
				new AStarIterator(source,
						new AStarIterator.AStarFunctions(target) {
					
							public double cost(AStarNode n1, AStarNode n2){
								return 1;
							}

							public double h(Node n){
								return (getDest().getID()-n.getID());
							}
		        	
						})
		  );
	  }
}

