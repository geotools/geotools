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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.basic.SourceGraphIterator;
import org.geotools.graph.util.PriorityQueue;

/**
 * Iterates over the nodes of a graph in pattern using <B>Dijkstra's 
 * Shortest Path Algorithm</B>. A Dijkstra iteration returns nodes
 * in an order of increasing cost relative to a specified node 
 * (the source node of the iteration).<BR>
 * <BR>
 * In a Dijsktra iteration, a <B>weight</B> is associated with each edge 
 * and a <B>cost</B> with each node. The iteration operates by maintaining
 * two sets of nodes. The first the set of nodes whose final cost is known, and 
 * the second is the set of nodes whose final cost is unknown. 
 * Initially, every node except for the source node has a cost of infinity, and 
 * resides in the unkown set. The source node has a cost of zero, and is 
 * is a member of the known set.<BR>
 * <BR>
 * The iteration operatates as follows:<BR>
 * <PRE>
 *   sn = source node of iteration
 *   N = set of all nodes
 *   K = set of nodes with known cost = {sn} 
 *   U = set of nodes with unknown cost = N - K
 * 
 *   cost(sn) = 0
 *   for each node $un in U
 *      cost(un) = infinity
 * 
 *   while(|U| > 0)
 *      for each node n in K
 *        find a node un in U that relates to n
 *        if cost(n) + weight(n,un) < cost(un)
 *          cost(un) = cost(n) + weight(n,un)
 *          
 *      ln = node with least cost in U
 *      remove ln from U
 *      add ln to K
 * 
 *      return ln as next node in iteration   
 * </PRE> 
 * The following is an illustration of the algorithm. Edge weights are labelled
 * in blue and the final node costs are labelled in red.<BR>
 * <IMG src="doc-files/dijkstra.gif"/>
 * <BR>
 * The nodes are returned in order of increasing cost which yields the sequence
 * A,C,B,D,E,F,G,H,I.<BR>
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public class DijkstraIterator extends SourceGraphIterator {

  /** compares two internal nodes used by the iteration by comparing costs **/
  private static Comparator<DijkstraNode> comparator = new Comparator<DijkstraNode>() {
    public int compare(DijkstraNode n1, DijkstraNode n2) {
      return(n1.cost < n2.cost ? -1 : n1.cost > n2.cost ? 1 : 0);  
    }
  };
  
  /** provides weights for edges in the graph **/
  protected EdgeWeighter weighter;
  
  /** provides weights for nodes in the graph **/
  protected NodeWeighter nweighter;
  
  /** priority queue to manage active nodes **/
  protected PriorityQueue queue;
  
  /** map of graph node to internal dijkstra node **/
  protected HashMap<Graphable,DijkstraNode> nodemap;
  
  /**
   * Constructs a new Dijkstra iterator which uses the specided EdgeWeighter.
   * 
   * @param weighter Calculates weights for edges in the graph being iterated
   *        over.
   */
  public DijkstraIterator(EdgeWeighter weighter) {
    this(weighter,null);
  }
  
  /**
   * Constructs a new Dijkstra iterator which uses the specided EdgeWeighter and 
   * NodeWeighter
   * 
   * @param weighter Calculates weights for edges in the graph being iterated
   *        over.
   * @param nweighter Calculates weights for nodes in the graph being iterated 
   *        over.
   */
  public DijkstraIterator(EdgeWeighter weighter, NodeWeighter nweighter) {
    this.weighter = weighter;
    this.nweighter = nweighter;
  }
  
  /**
   * Builds internal priority queue to manage node costs.
   * 
   * @see org.geotools.graph.traverse.GraphIterator#init(Graph)
   */
  public void init(Graph graph, GraphTraversal traversal) {
    //initialize data structures
    nodemap = new HashMap<Graphable,DijkstraNode>();
    
    queue = new PriorityQueue(comparator);
    queue.init(graph.getNodes().size());
    
    //place nodes into priority queue
    graph.visitNodes(
      new GraphVisitor() {
        public int visit(Graphable component) {
          //create a dijkstra node with infinite cost
          DijkstraNode dn = new DijkstraNode((Node)component, Double.MAX_VALUE);
          
          //create the mapping
          nodemap.put(component, dn);
          
          //source component gets a cost of 0
          if (component == getSource()) dn.cost = 0d;
          
          //place into priority queue
          queue.insert(dn);
          
          return 0;
        }
      } 
    );
  }

  /**
   * Returns the next node in the priority queue. If the next node coming out
   * of the queue has infinite cost, then the node is not adjacent to any nodes
   * in the set of nodes with known costs. This situation will end the traversal
   * every other node will also have infinite cost. This usally is the result of
   * a disconnected graph.
   *
   * @see org.geotools.graph.traverse.GraphIterator#next()  
   */
  public Graphable next(GraphTraversal traversal) {
    if (queue.isEmpty()) return(null);
    
    DijkstraNode next = (DijkstraNode)queue.extract();
    
    //check cost of node, if cost == infinity then return null
    // because no node in the visited set ever updated the node
    // since it is at the top of the heap it means no more nodes 
    // in the visited set will be visited
    if (next.cost == Double.MAX_VALUE) return(null);
    
    return(next.node);   
  }

  /**
   * Looks for adjacent nodes to the current node which are in the adjacent
   * node and updates costs.
   * 
   * @see org.geotools.graph.traverse.GraphIterator#cont(Graphable)
   */
  public void cont(Graphable current, GraphTraversal traversal) {
    DijkstraNode currdn = (DijkstraNode)nodemap.get(current);
    
    for (Iterator itr = getRelated(current); itr.hasNext();) {
      Node related = (Node)itr.next();
      if (!traversal.isVisited(related)) {
        DijkstraNode reldn = (DijkstraNode)nodemap.get(related);
        
        //calculate cost from current node to related node
        double cost = weighter.getWeight(currdn.node.getEdge(related)) 
                    + currdn.cost;
        
        //calculate the cost of going through the node
        if ( nweighter != null ) {
            double ncost = 0d;
            if(currdn.parent != null) {
                Edge e1 = currdn.parent.node.getEdge(currdn.node);
                Edge e2 = currdn.node.getEdge(related);
                ncost = nweighter.getWeight(currdn.node,e1,e2);
            }
        }
        //if cost less than current cost of related node, update 
        if (cost < reldn.cost) {
          reldn.cost = cost;
          reldn.parent = currdn;
          queue.update(reldn);  
        }
      }  
    }
  }

  /**
   * Kills the branch of the traversal by not updating the cost of any 
   * adjacent nodes.
   * 
   * @see org.geotools.graph.traverse.GraphIterator#killBranch(Graphable)
   */
  public void killBranch(Graphable current, GraphTraversal traversal) {
    //do nothing  
  }
  
  /**
   * Returns the internal cost of a node which has been calculated by the 
   * iterator.
   * 
   * @param component The component whose cost to return.
   * 
   * @return The cost associated with the component.
   */
  public double getCost(Graphable component) {
    return(((DijkstraNode)nodemap.get(component)).cost);
  }
  
  /**
   * Returns the last node in the known set to update the node. The iteration 
   * operates by nodes in the known set updating the cost of nodes in the 
   * unknown set. Each time an update occurs, the known node is set as the 
   * parent of the unkown node.
   * 
   * @param component The node whose parent to return (child)
   * 
   * @return The parent, or null if the method is supplied the source of the 
   *         iteration. 
   */
  public Graphable getParent(Graphable component) {
    if (component.equals(getSource())) return(null);
    DijkstraNode dn = (DijkstraNode)nodemap.get(component);
    
    if (dn == null || dn.parent == null) return(null);
    return(dn.parent.node);
    
    //return(((DijkstraNode)m_nodemap.get(component)).parent.node);   
  }
  
  protected PriorityQueue getQueue() {
    return(queue);	
  }
  
  protected Iterator getRelated(Graphable current) {
    return(current.getRelated());	
  }
  
  /**
   * Supplies a weight for each edge in the graph to be used by the iteration 
   * when calculating node costs. 
   * 
   * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
   *
   */
  public static interface EdgeWeighter {
    
    /**
     * Returns the weight for the associated edge.
     * 
     * @param e The edge whose weight to return.
     * 
     * @return The weight of the edge.
     */
    public double getWeight(Edge e);  
  }
  
  /**
   * Supplies a weight for each pair of adjacent edges. 
   * 
   * @author Sandeep Kumar Jakkaraju sandeepkumar@iitbombay.org
   *
   */
  public static interface NodeWeighter {

    /**
     * Returns the weight for a node, with respect to two adjecent edges.
     * 
     * @param n The node.
     * @param e1 First edge.
     * @param e2 Second edge.
     *
     * @return The weight associated with traversing through the node from 
     *  the first edge to the second.
     */
     public double getWeight(Node n, Edge e1, Edge e2);  
  }
  
  /**
   * Internal data structure used to track node costs, and parent nodes.
   * 
   * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
   *
   */
  protected static class DijkstraNode {
    /** underlying graph node **/
    public Node node;
    
    /** cost associated with the node **/
    public double cost;
    
    /** last node to update the cost the the underlying graph node **/
    public DijkstraNode parent;
      
    /**
     * Constructs a new Dijsktra node.
     * 
     * @param node Underling node in graph being iterated over.
     * @param cost Initial cost of node.
     */
    public DijkstraNode(Node node, double cost) {
      this.node = node;
      this.cost = cost;
    }
  }
}
