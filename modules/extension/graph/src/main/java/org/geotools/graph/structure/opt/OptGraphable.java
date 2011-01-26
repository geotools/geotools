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
package org.geotools.graph.structure.opt;

import java.io.Serializable;

import org.geotools.graph.structure.Graphable;

/**
 * Root of class hierarchy for optimized implementation of graph components. 
 * The optimizations reduce the space taken up by graph components: <BR>
 * <UL>
 * <LI>Counter implemented as byte
 * <LI>No underlying object reference.
 * </UL>
 * Objects in the optimized hierarchy implement the Serializable interface.
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 * @see Graphable
 * @source $URL$
 */
public abstract class OptGraphable implements Graphable, Serializable {

  /** visited flag **/
  private boolean m_visited;
  
  /** counter **/
  private byte m_count;
   
  /**
   * Constructs a new optimized graphable object. Visited flag it set to false
   * and counter set to -1.
   */
  public OptGraphable() {
    m_visited = false;
    m_count = -1;
  }
  
  /**
   * Does nothing.
   * 
   * @see Graphable#setID(int) 
   */
  public void setID(int id) {}
  
  /**
   * Returns 0.
   * 
   * @see Graphable#getID()
   */
  public int getID() {
    return 0;
  }
  
  /**
   * @see Graphable#setVisited(boolean)
   */
  public void setVisited(boolean visited) {
    m_visited = visited;
  }
  
  /**
   * @see Graphable#isVisited()
   */
  public boolean isVisited() {
    return(m_visited);
  }

  /**
   * To minimize space, the counter is stored as a byte. Therefore the counter
   * can take on values from -128 to 127.
   * 
   * @see Graphable#setCount(int)
   */
  public void setCount(int count) {
    m_count = (byte)count;    
  }

  /**
   * @see Graphable#getCount()
   */
  public int getCount() {
    return(m_count);
  }
  
  /**
   * Does nothing.
   * 
   * @see Graphable#setObject(Object)
   */
  public void setObject(Object obj) {}

  /**
   * Returns null.
   * 
   * @see Graphable#getObject()
   */
  public Object getObject() {
    return null;
  }
} 
