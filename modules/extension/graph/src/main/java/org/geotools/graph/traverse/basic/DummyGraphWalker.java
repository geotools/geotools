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

import org.geotools.graph.structure.Graphable;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.GraphWalker;

/**
 * A simple implementation of GraphWalker that does nothing but signal 
 * a graph traversal to continue.
 * 
 * @see GraphTraversal
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public class DummyGraphWalker implements GraphWalker {

  /**
   * Returns the continue signal.
   * 
   * @see GraphWalker#visit(Graphable, GraphTraversal)
   * @see GraphTraversal#CONTINUE
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
