/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 *    (C) 2001-2006  Vivid Solutions
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.topograph2D.util;

import java.util.ArrayList;
import java.util.TreeSet;

import org.geotools.geometry.iso.topograph2D.Coordinate;


/**
 *  A {@link CoordinateFilter} that builds a set of <code>Coordinate</code>s.
 *  The set of coordinates contains no duplicate points.
 *
 *
 * @source $URL$
 */
public class UniqueCoordinateArrayFilter implements CoordinateFilter {
  TreeSet treeSet = new TreeSet();
  ArrayList list = new ArrayList();

  public UniqueCoordinateArrayFilter() { }

  /**
   *  Returns the gathered <code>Coordinate</code>s.
   *
   *@return    the <code>Coordinate</code>s collected by this <code>CoordinateArrayFilter</code>
   */
  public Coordinate[] getCoordinates() {
    Coordinate[] coordinates = new Coordinate[list.size()];
    return (Coordinate[]) list.toArray(coordinates);
  }

  public void filter(Coordinate coord) {
    if (!treeSet.contains(coord)) {
      list.add(coord);
      treeSet.add(coord);
    }
  }
}

