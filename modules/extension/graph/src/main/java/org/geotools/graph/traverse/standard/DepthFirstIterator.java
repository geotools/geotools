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

import org.geotools.graph.structure.Graph;
import org.geotools.graph.util.Queue;
import org.geotools.graph.util.Stack;

/**
 * Iterates over the nodes of a graph in a <B>Depth First Search</B> pattern 
 * starting from a specified node. The following illustrates the iteration order. 
 * <BR>
 * <BR>
 * <IMG src="doc-files/dfs.gif"/><BR>
 * <BR>
 * The iteration operates by maintaning a node queue of <B>active</B> nodes.  
 * An <B>active</B> node is a node that will returned at a later stage of the i
 * teration. The node queue for a Depth First iteration is implemented as a 
 * <B>Last In First Out</B> queue (a Stack).
 * A node is placed in the the node queue if it has not been visited, and 
 * it is adjacent to a a node that has been visited. The node queue intially 
 * contains only the source node of the traversal.
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public class DepthFirstIterator extends BreadthFirstIterator {

  /**
   * Builds the node queue for the Iteration.
   * 
   * @param graph The graph of the iteration.
   * 
   * @return A Last In First Out queue (Stack)  
   */
  protected Queue buildQueue(Graph graph) {
    return(new Stack(graph.getNodes().size()));
  }

}
