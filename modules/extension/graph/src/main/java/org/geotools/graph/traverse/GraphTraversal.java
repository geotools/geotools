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
package org.geotools.graph.traverse;

import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;

/**
 * Iterates a GraphWalker over the 
 * components of a Graph. The order in which components are iterated over
 * is determined by the GraphIterator class. The GraphTraversal is 
 * the mediator between the GraphWalker and the GraphIterator.<BR>
 * <BR>
 * Upon each visitation, the GraphWalker communicates to the GraphTraversal 
 * through a series of return codes, each specifying a different action to
 * perform at that point of the travesal. The following summarizes the meaning
 * of the codes.<BR>
 * <BR>
 * <TABLE border="1" width="60%" style="font-family:Arial;font-size=10pt">
 *   <TH width="20%">Code</TH>
 *   <TH width="40%">Action Taken</TH>
 *   <TR>
 *     <TD align="center">CONTINUE</TD>
 *     <TD>The traversal continues as normal.</TD>
 *   </TR>
 *  <TR>
 *     <TD align="center"f>SUSPEND</TD>
 *     <TD>Suspends the traversal at some intermediate stage. This code
 *         should be returned if the traversal is intended to be resumed.</TD> 
 *  </TR>
 *  <TR>
 *     <TD align="center">KILL_BRANCH</TD>
 *     <TD>Kills the current branch of the traversal. Depending on the iteration
 *         algorithm, returning this code may end the traversal.</TD>
 *   </TR>
 *  <TR>
 *     <TD align="center">STOP</TD>
 *     <TD>Stops the traversal.</TD>
 *   </TR>
 * </TABLE>
 * <BR>
 * <BR>
 * GraphTraversals are started with a call to traverse(). If the traversal is 
 * suspended at some intermediate point, an additional call to traverse() will 
 * resume the traversal.<BR>
 * <BR>
 *
 * @see GraphWalker
 * @see GraphIterator
 * @see Graph
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 * @source $URL$
 */
public interface GraphTraversal {
  
  /** Signals the traversal to continue **/
  public static int CONTINUE = 0;
  
  /** Signals the traversal to suspend at some intermediate point **/
  public static int SUSPEND = 1;
  
  /** Signals the traversal to kill the current branch **/
  public static int KILL_BRANCH = 2;
  
  /** Signals the traversal to stop **/ 
  public static int STOP = 3;
  
  /**
   * 
   * Sets the graph being traversed.
   * 
   * @param graph The graph whose components are being traversed. 
   */
  public void setGraph(Graph graph);
  
  /**
   * Returns the graph being traversed.
   * 
   * @return The graph whose components are being traversed.
   * 
   * @see Graph
   */
  public Graph getGraph();
  
  /**
   * Sets the iterator that specifies the order in which visit graph components.
   * 
   * @param iterator The iterator over the graph components.
   */
  public void setIterator(GraphIterator iterator);
  
  /**
   * Returns the iterator that specifies the order in which to visit graph 
   * components.
   * 
   * @return The iterator over the graph components.
   * 
   * @see GraphIterator
   */
  public GraphIterator getIterator();
  
  /**
   * Sets the walker (visitor) traversing the graph.
   * 
   * @param walker The walker being iterated over the components of the graph.
   */
  public void setWalker(GraphWalker walker);
  
  /**
   * Returns the walker (visitor) traversing the graph of the graph. 
   *
   * @return The walker being iterated over the components.
   */
  public GraphWalker getWalker();
  
  /**
   * Initializes the traversal.
   */
  public void init();
 
  /**
   * Starts or resumes a traversal over the components of a graph.
   */
  public void traverse(); 

  //TODO:DOCUMENT ME!
  public boolean isVisited(Graphable g);
  
  //TODO:DOCUMENT ME!
  public void setVisited(Graphable g, boolean visited);
}
