/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.graph.util.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.GraphWalker;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.basic.SourceGraphIterator;
import org.geotools.graph.traverse.standard.DepthFirstTopologicalIterator;
import org.geotools.graph.traverse.standard.NoBifurcationIterator;

/**
 * Removes all nodes of degree 2 from a graph. The following are examples
 * of graphs being fused.<BR>
 * <BR>
 * <IMG src="doc-files/fuse_0.gif"/><BR>
 * <BR>
 * <IMG src="doc-files/fuse_1.gif"/><BR>
 * <BR>
 * <IMG src="doc-files/fuse_2.gif"/><BR>
 * <BR>
 * When a node of degree 2 is removed from tan unfused graph, the two edges
 * is is adjacent to must be merged into a single edge. More generally if n 
 * adjacent nodes of degree 2 are removed, n+1 edges must be merged into a 
 * single edge. This change in graph structure has an effect on the entities 
 * modelled by the graph. Since each edge models a single object, replacing
 * multiple edges with a single edge results in an inconsistet model. Therefore
 * an EdgeMerger is used to merge the objects represented by the multiple edges
 * into a single object. This new object becomes the underlying object of the 
 * merged edge.
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public class GraphFuser {

  /** the graph being fused **/
  private Graph m_graph;
  
  /** the builder used to modify the graph being fused **/
  private GraphBuilder m_builder;
  
  /** the edge merger **/
  private EdgeMerger m_merger;
  
  /** traversal used during the graph fuse **/
  private GraphTraversal m_traversal;
  
  /** walker used in the traversal during graph fuse **/
  private GraphWalker m_walker;
  
  /** counter used to track number of unvisited nodes of degree 2 **/
  private int m_ndegree2;
  
  /** collection of node sets to fuse **/
  private ArrayList m_sets;
  
  /** individual node set **/
  private ArrayList m_nodes;
  
  /**
   * Constructs a GraphFuser.
   * 
   * @param graph Graph to fuse.
   * @param builder GraphBuilder used to fuse graph.
   * @param merger Used to merge edges.
   */
  public GraphFuser(Graph graph, GraphBuilder builder, EdgeMerger merger) {
    m_graph = graph;
    m_builder = builder;
    m_merger = merger;
  }
  
  /**
   * Performs the fuse. 
   * 
   * @return True if the fuse was successful, otherwise false.
   */
  public boolean fuse() {
    //create walker for first stage
    // if the walker sees a node of degree 2 it adds it to the current
    // set of nodes, else it starts a new set
    m_walker = new GraphWalker() {
      public int visit(Graphable element, GraphTraversal traversal) {
        Node node = (Node)element;
        
        //if the node is not of degree 2, start a new set
        if (node.getDegree() != 2) {
          finish();  
        }
        else {
          //add node to current set
          m_nodes.add(node);
          m_ndegree2--;
        }
       
        return(GraphTraversal.CONTINUE);
      }

      public void finish() {
        //no need to recreate if empty
        if (!m_nodes.isEmpty()) {
          m_sets.add(m_nodes);
          m_nodes = new ArrayList();
        }
      }
    };
    
    //perform a topological depth first traversal
    m_traversal = new BasicGraphTraversal(
      m_graph, m_walker, new DepthFirstTopologicalIterator()
    ); 
    
    //initialise set and node collections
    m_sets = new ArrayList();
    m_nodes = new ArrayList();
    
  
    m_ndegree2 = m_graph.getNodesOfDegree(2).size();
    if (m_ndegree2 == 0) return(true); // nothing to fuse
    
    m_traversal.init();
    
    //reset edge visited flags
    m_graph.visitNodes(
      new GraphVisitor() {
        public int visit(Graphable component) {
          component.setVisited(false);
          return 0;
        }
      }
    );
    
    //perform the traversal
    m_traversal.traverse();
    
    //if all nodes of degree 2 have been visited, we are finished
    if (m_ndegree2 > 0) {
    
      //if there are still nodes of degree 2 that havent been visited, it means
      // that the graph has a cycle and that the remaining degree 2 nodes are 
      // internal to the cycle, so the strategy for the second stage is to 
      // find all unvisited nodes of degree 2 that are not visited and start 
      // a no bifurcation traversal from them
      Iterator sources = m_graph.queryNodes(
        new GraphVisitor() {
          public int visit(Graphable component) {
            Node node = (Node)component;
            if (!node.isVisited() && node.getDegree() == 2) {
              //check for adjacent node of degree > 2
              for (Iterator itr = node.getRelated(); itr.hasNext(); ) {
                Node rel = (Node)itr.next();
                if (rel.getDegree() > 2) return(Graph.PASS_AND_CONTINUE);
              }
            }
            return(Graph.FAIL_QUERY);
          }
        }
      ).iterator();
      
      //if the query returned no nodes, it means that all the cycle is 
      // disconnected from the rest of graph, so just pick any node of degree 2
      if (!sources.hasNext()) {
        sources = m_graph.queryNodes(
          new GraphVisitor() {
            public int visit(Graphable component) {
              if (!component.isVisited()) return(Graph.PASS_AND_STOP);
              return(Graph.FAIL_QUERY);
            }
          }
        ).iterator();  
      }
  
      //create stage 2 walker, simple add any nodes visited nodes to the 
      // current node set
      m_walker = new GraphWalker() {
        public int visit(Graphable element, GraphTraversal traversal) {
          m_ndegree2--;
          m_nodes.add(element);
          return(GraphTraversal.CONTINUE);
        }
  
        public void finish() {
          m_sets.add(m_nodes);
          m_nodes = new ArrayList();
        }
      };
      m_traversal.setWalker(m_walker);
      m_traversal.setIterator(new NoBifurcationIterator());
      
      //clear current node list
      m_nodes = new ArrayList();
            
      Node source = null;
      while(sources.hasNext()) {
        while(sources.hasNext()) {
			    source = (Node)sources.next();
				  if (source.isVisited()) continue;
  
				  ((SourceGraphIterator)m_traversal.getIterator()).setSource(source);
				  m_traversal.traverse();
        }
      }
    }
    
    // should be zero, if not something wierd not accounted for
    if(m_ndegree2 == 0) {
      
      //build the fused graph
      for(Iterator sitr = m_sets.iterator(); sitr.hasNext();) {
        ArrayList nodes = (ArrayList)sitr.next();
        ArrayList edges = new ArrayList();
        Node first = null; //node of degree != 2 adjacent to first node in set
        Node last = null; //node of degree != 2 adjacent to last node in set
         
        if (nodes.size() == 1) {
          //set first and last to be related nodes
          Iterator related = ((Node)nodes.get(0)).getRelated();
          first = (Node)related.next();
          last = (Node)related.next();
          
          edges.addAll(((Node)nodes.get(0)).getEdges());
        }
        else {
          
          //get the node of degree != 2 adjacent to first node in set
          Node node = (Node)nodes.get(0);
          Iterator rel = node.getRelated();
          first = (Node)rel.next();
          if (first.equals(nodes.get(1))) first = (Node)rel.next();
         
          //get the node of degree != 2 adjacent to last node in set
          node = (Node)nodes.get(nodes.size()-1);
          rel = node.getRelated();
          last = (Node)rel.next();
          if (last.equals(nodes.get(nodes.size()-2))) last = (Node)rel.next();
          
          //check to see that the first node is not of degree 2, if it is we 
          // have a set of nodes that forms a cycle with no bifurcations
          // set first to point to node at index 1, and last index x, also 
          // remove node at index 0 from node set so it doesn't get removed
          if (first.getDegree() == 2) {
            first = (Node)nodes.get(1);
            last = (Node)nodes.get(0);
            first = last = (Node)nodes.get(0);
            
            //remove first node from list so that it doesn't get deleted
            nodes.remove(0); 
          }
          
          //add edge between first node in set, and the node of degree != 2
          // that is adjacent to it 
          edges.add(first.getEdge((Node)nodes.get(0)));
          
          //add middle edges
          for (int i = 1; i < nodes.size(); i++) {
            Node curr = (Node)nodes.get(i);
            Node prev = (Node)nodes.get(i-1);
            edges.add(curr.getEdge(prev));
          }
         
          //add edge between last node in set, and the node of degree != 2
          // that is adjacent to it 
          edges.add(last.getEdge((Node)nodes.get(nodes.size()-1)));
        }
        
        //merge the underlying objects of the edges into a single object
        Object obj = m_merger.merge(edges);
        
        //remove the old nodes from the graph
        m_builder.removeNodes(nodes);
        
        //add merged object to the generator
        
        //create the new edge
        Edge newEdge = m_builder.buildEdge(first, last);
        
        //set the underlying object to be the merged object we created
        m_merger.setMergedObject(newEdge, obj, edges);
        
        //add the edge to the builder
        m_builder.addEdge(newEdge);
      }
    
      return(true);  
    }
    return(false);  
  }
  
//  /**
//   * Sets the object for the newly created edge.
//   * 
//   * @param newEdge The edge created to represented the merged object.
//   * @param merged The merged object.
//   */
//  public void setObject(Edge newEdge, Object merged) {
//    newEdge.setObject(merged);
//  }
  
  /**
   * Merges the underlying objects represented by a number of edges into a 
   * single object.
   *    
   * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
   *
   */
  public static interface EdgeMerger {
    
    /**
     * Creates a single object from collection of underlying objects represented
     * by a collection of edges.
     * 
     * @param edges A collection of edges.
     * 
     * @return A single object.
     */
    public Object merge(List edges);
    
    /**
     * Sets the object for the edge created to represented the merged object.
     * 
     * @param newEdge The edge created to represent the merged object.
     * @param merged The merged object.
     * @param edges The original edges that were merged
     */
    public void setMergedObject(Edge newEdge, Object merged, List edges);
  }
  
}
