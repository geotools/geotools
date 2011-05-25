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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.basic.BasicGraph;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.GraphWalker;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.standard.DepthFirstIterator;

/**
 * Creates a collection of connected graphs from a single graph. A 
 * connected graph in which for every two pair of nodes, there is a path between
 * them.  
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public class GraphPartitioner implements GraphWalker {

  /** graph to be partitioned into connected components **/
  private Graph m_graph;
  
  /** paritions of graph **/
  private ArrayList m_partitions;
  
  /** current partition **/
  private ArrayList m_partition;
  
  /** visited counter **/
  private int m_nvisited;
  
  /**
   * Constructs a new partitioner for a graph.
   *  
   * @param graph Graph to be partitioned.
   */
  public GraphPartitioner(Graph graph) {
    m_graph = graph;
    m_partitions = new ArrayList();
  }
  
  /**
   * Performs the partition.
   * 
   * @return True if the partition was successful, otherwise false.
   */
  public boolean partition() {
    //strategy is to perform a depth first search from a node, every node is 
    // reaches is connected therefore in the same partition
    // when traversal ends, start from a new source, repeat until no more 
    // sources
    
    try {
      m_nvisited = m_graph.getNodes().size();
      
      DepthFirstIterator iterator = new DepthFirstIterator();
      BasicGraphTraversal traversal = new BasicGraphTraversal(
        m_graph, this, iterator
      );
      
      Iterator sources = m_graph.getNodes().iterator();
      
      traversal.init();
      m_partition = new ArrayList();
      
      while(m_nvisited > 0) {
        
        //find a node that hasn't been visited and set as source of traversal
        Node source = null;
        while(sources.hasNext() && (source = (Node)sources.next()).isVisited());
       
        //if we could not find a source, return false
        if (source == null || source.isVisited()) return(false);
        
        iterator.setSource(source);
        traversal.traverse();
      }
      
      //create the individual graphs
      HashSet nodes = null;
      HashSet edges = null;
      ArrayList graphs = new ArrayList();
      
      for (Iterator itr = m_partitions.iterator(); itr.hasNext();) {
        m_partition = (ArrayList)itr.next();
        if (m_partition.size() == 0) continue;
        
        nodes = new HashSet();
        edges = new HashSet();
        for (Iterator nitr = m_partition.iterator(); nitr.hasNext();) {
          Node node = (Node)nitr.next();
          nodes.add(node);
          edges.addAll(node.getEdges());
        } 
        
        graphs.add(new BasicGraph(nodes, edges));
      }
      
      m_partitions = graphs;
      
      return(true);
    }
    catch(Exception e) {
      e.printStackTrace();  
    }
    return(false);
  }
  
  /**
   * Returns the partitions of the graph.
   * 
   * @return A collection of Graph objects.
   * 
   * @see Graph
   */
  public List getPartitions() {
    return(m_partitions);  
  }
  
  /**
   * Adds the element to the current partition.
   * 
   * @see GraphWalker#visit(Graphable, GraphTraversal)
   */
  public int visit(Graphable element, GraphTraversal traversal) {
    //add element to current set
    m_nvisited--;
    m_partition.add(element);
    return(GraphTraversal.CONTINUE);
  }

  /**
   * Adds the current partition to the completed set of partitions and 
   * creates a new partition.
   * 
   * @see GraphWalker#finish()
   */
  public void finish() {
    //create a new set
    m_partitions.add(m_partition);
    m_partition = new ArrayList();
  }
}
