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
package org.geotools.graph.traverse.basic;

import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.traverse.GraphIterator;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.GraphWalker;

/**
 * A basic implementation of GraphTraversal.
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 * @source $URL$
 */
public class BasicGraphTraversal implements GraphTraversal {

  /** the graph being iterated over **/
  private Graph m_graph;
  
  /** the walker being iterated over the graph **/
  private GraphWalker m_walker;
  
  /** the iterator specifying the order in which to visit components of 
      the graph during the traversal **/
  private GraphIterator m_iterator;
  
  /**
   * Constrcuts a new graph traversal. 
   * 
   * @param graph The graph being traversed.
   * @param walker The walker being traversed over the components of the graph.
   * @param iterator The iterator specifying the order in which to visit 
   *        components of the graph.
   */
  public BasicGraphTraversal(
    Graph graph, GraphWalker walker, GraphIterator iterator
  ) {
    m_graph = graph;
    m_walker = walker;
    setIterator(iterator);  
  }
  
  /**
   * @see GraphTraversal#setGraph(Graph)
   */
  public void setGraph(Graph graph) {
    m_graph = graph;  
  }

  /**
   * @see GraphTraversal#getGraph()
   */
  public Graph getGraph() {
    return(m_graph);  
  }

  /**
   * Sets the iterator and intializes it.
   * 
   * @see GraphIterator#init(Graph)
   * @see GraphTraversal#setIterator(GraphIterator)
   */
  public void setIterator(GraphIterator iterator) {
    m_iterator = iterator;
    m_iterator.setTraversal(this);
    m_iterator.init(m_graph, this);
  }

  /**
   * @see GraphTraversal#getIterator()
   */
  public GraphIterator getIterator() {
    return(m_iterator);
  }
  
  /**
   * @see GraphTraversal#setWalker(GraphWalker)
   */
  public void setWalker(GraphWalker walker) {
    m_walker = walker;  
  }
  
  /**
   * @see GraphTraversal#getWalker()
   */
  public GraphWalker getWalker() {
    return(m_walker);
  }

  /**
   * Resets the visited flag and counts of all nodes of the graph.
   * 
   * @see GraphTraversal#init()
   */
  public void init() {
    //initialize the nodes of the graph
    m_graph.visitNodes(
      new GraphVisitor() {
        public int visit(Graphable component) {
          component.setVisited(false);
          component.setCount(0);
          return(0);
        }
      }
    );
  }
  
  /**
   * Upon each iteration of the traversal, a component is returned from the 
   * iterator and passed to the visitor. The traversal interprets the return 
   * codes from the walker and performs the following actions. <BR>
   * <BR>
   * <TABLE border="1" width="60%" style="font-family:Arial;font-size=10pt">
   *   <TH width="20%">Code</TH>
   *   <TH width="40%">Action Performed</TH>
   *   <TR>
   *     <TD align="center">CONTINUE</TD>
   *     <TD>The traversal instructs the iterator to continue and starts the 
   *         next stage of iteration.</TD>
   *   </TR>
   *  <TR>
   *     <TD align="center">SUSPEND</TD>
   *     <TD>The traversal instructs the iterator to continue but does not start
   *         the next stage of iteration, returning from traverse().</TD> 
   *  </TR>
   *  <TR>
   *     <TD align="center">KILL_BRANCH</TD>
   *     <TD>The traversal instructs the iterator to kill the current branch
   *         and starts the next stage of iteration.</TD>
   *   </TR>
   *  <TR>
   *     <TD align="center">STOP</TD>
   *     <TD>The traversal does not instruct the iterator to continue and
   *         does not start the next of iteration, returning from traverse()
   *     </TD>
   *   </TR>
   * </TABLE>
   * 
   * @see GraphTraversal#traverse()
   */
  public void traverse() {
    Graphable current;
    
    //get next component from iterator, null means stop
O:  while((current = m_iterator.next(this)) != null) {
      setVisited(current, true);
      
      //pass the component to the visitor
      switch(m_walker.visit(current, null)) {
        case CONTINUE: 
          //signal iterator to continue from this point and start next 
          // iteration of traversal
          m_iterator.cont(current,this); 
          continue;
        
        case SUSPEND: 
          //signal iterator to continue from this point, but do not start
          // next iteration
          m_iterator.cont(current,this);
          return;
          
        case KILL_BRANCH:
          //signal iterator to kill branch at this point and start next
          //iteration
          m_iterator.killBranch(current,this);
          continue;
          
        case STOP:
          //stop traversal
          break O;
          
        default:
          //illegal return value from walker
          throw new IllegalStateException(
            "Unrecognized return value from GraphWalker"
          );
      }
    }
    
    //traversal complete, signal to walker
    m_walker.finish();
  }
  
  public void setVisited(Graphable g, boolean visited) {
    g.setVisited(visited);  
  }
  
  public boolean isVisited(Graphable g) {
    return(g.isVisited());
  }
}
