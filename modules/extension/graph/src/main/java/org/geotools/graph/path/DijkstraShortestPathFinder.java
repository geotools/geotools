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

import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.GraphWalker;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.standard.DijkstraIterator;
import org.geotools.graph.traverse.standard.DijkstraIterator.EdgeWeighter;
import org.geotools.graph.traverse.standard.DijkstraIterator.NodeWeighter;

/**
 * Calculates node paths in a graph using Dijkstra's Shortest Path Algorithm. 
 * Dijsktras algorithm calculates a shortest path from a specefied node (the 
 * source node of the underlying dijkstra iteration) to every other node in the 
 * graph. 
 *  
 * @see DijsktraIterator 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 * @source $URL$
 */
public class DijkstraShortestPathFinder implements GraphWalker {
  
  /** Graphs to calculate paths for **/
  private Graph m_graph;
  
  /** Graph traversal used for the dijkstra iteration **/
  private GraphTraversal m_traversal;
  
  /** Underling Dijkstra iterator **/
  private DijkstraIterator m_iterator;
  
  /**
   * Constructs a new path finder.
   * 
   * @param graph The graph to calculate paths for.
   * @param iterator The dijsktra iterator to used to calculate shortest paths.
   */
  public DijkstraShortestPathFinder(Graph graph, DijkstraIterator iterator) {
    m_graph = graph;
    m_iterator = iterator;
    m_traversal = new BasicGraphTraversal(graph, this, iterator);
  }
        
  /**
   * Constructs a new path finder.
   * 
   * @param graph Graph to calculate paths for.
   * @param source Node to calculate paths from.
   * @param weighter Associates weights with edges in the graph.
   */
  public DijkstraShortestPathFinder(
    Graph graph, Graphable source, EdgeWeighter weighter
  ) {
    this(graph,source,weighter,null); 
  }
  
  /**
   * Constructs a new path finder.
   * 
   * @param graph Graph to calculate paths for.
   * @param source Node to calculate paths from.
   * @param weighter Associates weights with edges in the graph.
   * @param nweighter Associates weights with nodes in the graph.
   */
  public DijkstraShortestPathFinder(
    Graph graph, Graphable source, EdgeWeighter weighter, NodeWeighter nweighter
  ) {
    m_graph = graph;
    m_iterator = new DijkstraIterator(weighter,nweighter);
    m_iterator.setSource(source);
    m_traversal = new BasicGraphTraversal(graph, this, m_iterator); 
  }
  
  /**
   * Performs the graph traversal and calculates the shortest path from 
   * the source node to every other node in the graph.
   */
  public void calculate() {
    m_traversal.init();
    m_traversal.traverse(); 
  }
  
  /**
   * Returns a path <B>from</B> g <B>to</B> the source. If the desired path is
   * the opposite (from the source to g) can be used.
   * 
   * @param g The start node of the path to be calculated.
   * 
   * @see Path#riterator()
   * 
   * @return A path from g to the source.
   */
  public Path getPath(Graphable g) {
    Path p = new Path();
    p.add(g);
    
    Graphable parent = g;
    while((parent = m_iterator.getParent(parent)) != null) p.add(parent);
    
    if (!p.getLast().equals(m_iterator.getSource())) return(null);
    
    return(p);
  }

  /**
   * Returns the cost associated with a node calculated during the graph 
   * traversal.
   * 
   * @param g The node whose cost is desired.
   * 
   * @return The cost associated with the node.
   */
  public double getCost(Graphable g) {
    return(m_iterator.getCost(g));
  }
  
  public DijkstraIterator getIterator() {
    return(m_iterator);  
  }
  
  public GraphTraversal getTraversal() {
    return(m_traversal);  
  }
  
  /**
   * Does nothing except signal the traversal to continue.
   * 
   * @see GraphWalker#visit(Graphable, GraphTraversal)
   */
  public int visit(Graphable element, GraphTraversal traversal) {
    return(GraphTraversal.CONTINUE);
  }
  
  /**
   * Does nothing.
   * 
   * @see GraphWalker#finish()
   */
  public void finish() {}
}
