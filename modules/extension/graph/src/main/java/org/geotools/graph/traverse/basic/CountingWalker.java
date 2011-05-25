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
 * An implementation of GraphWalker that counts the number of components 
 * visited. As each component is visited, the walker sets the count of the 
 * component to the value of its counter.
 * 
 * @see Graphable#setCount(int)
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public class CountingWalker implements GraphWalker {

  /** counter of how many components have been visited **/
  private int m_counter;
  
  /**
   * Sets the count of the component and increments the counter.
   * 
   * @see Graphable#setCount(int)
   * @see GraphWalker#visit(Graphable, GraphTraversal)
   */
  public int visit(Graphable element, GraphTraversal traversal) {
    element.setCount(m_counter++);
    return GraphTraversal.CONTINUE;
  }

  /**
   * Does nothing.
   * 
   * @see GraphWalker#finish()
   */
  public void finish() {}
  
  /**
   * Returns the value of the visitation counter.
   * 
   * @return int Value of the counter.
   */
  public int getCount() {
    return(m_counter);
  }
}
