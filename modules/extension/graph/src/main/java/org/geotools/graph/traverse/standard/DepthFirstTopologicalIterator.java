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
 * Iterates over the nodes of a graph in <B>Depth First Topological Sort</B> 
 * pattern. The following is an illustration of the iteration.<BR>
 * <IMG src="doc-files/depth_topo.gif"><BR>
 * <BR>
 * Initially all nodes of degree less than two are <B>active</B> 
 * (ready to be visited). As nodes are visited, a node can become active 
 * when all but one of its related nodes have been visited (
 * degree = counter + 1). When a node becomes active it is placed into the
 * <B>active node queue</B> (queue of nodes to be visited). 
 * The Depth First Topological iterator places
 * nodes into the queue in <B>Last In First Out</B> order (a Stack).<BR>
 * <BR>
 * To determine when a node is to become active the iterator uses the counter
 * associated with each node. If these counters are modified by an entity 
 * other then the iterator, the iteration may be affected in undefined ways. 
 *  
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 * @source $URL$
 */
public class DepthFirstTopologicalIterator 
  extends BreadthFirstTopologicalIterator {
  
  /**
   * Builds the active node queue. 
   * 
   * @param graph The Graph whose components are being iterated over.
   * 
   * @return A last in first out queue (a stack)
   */
  protected Queue buildQueue(Graph graph) {
    return(new Stack(graph.getNodes().size()));  
  }

}
