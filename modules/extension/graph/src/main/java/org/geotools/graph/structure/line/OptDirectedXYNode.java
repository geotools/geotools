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
package org.geotools.graph.structure.line;

import org.geotools.graph.structure.opt.OptDirectedNode;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Optimized implementation of XYNode extended from OptDirectedNode. Instead of 
 * storing an underlying coordinate object, only a set of (x,y) values are 
 * stored eliminating the storage of additional oordinate dimensions.
 * 
 * @see org.geotools.graph.structure.opt.OptDirectedNode
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public class OptDirectedXYNode extends OptDirectedNode implements XYNode {
  
  /** x value of coordinate **/
  private double m_x;
  
  /** y value of coordinate **/
  private double m_y;
  
  /**
   * This method creates a new Coordinate object upon each call.
   * 
   * @see XYNode#getCoordinate() 
   */
  public Coordinate getCoordinate() {
    return(new Coordinate(m_x, m_y));
  }

  /**
   * This method strips only the x and y ordinates from the Coordinate object
   * and stores them.
   * 
   * @see XYNode#setCoordinate(Coordinate) 
   */
  public void setCoordinate(Coordinate c) {
    m_x = c.x;
    m_y = c.y; 
  }
}
