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

import org.geotools.graph.structure.Node;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Represents a node in a line network. A node in a line graph has a coordinate
 * associated with it.
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public interface XYNode extends Node {
  
  /**
   * Returns the coordinate associated with the node.
   * 
   * @return A coordinate.
   */
  public Coordinate getCoordinate();
  
  /**
   * Sets the coordinate associated with the node.
   * 
   * @param c A coordinate.
   */
  public void setCoordinate(Coordinate c);
}
