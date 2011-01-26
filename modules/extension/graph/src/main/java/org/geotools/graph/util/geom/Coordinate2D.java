/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.graph.util.geom;

public class Coordinate2D {
  public double x;
  public double y;
  
  public Coordinate2D(double x, double y) {
    this.x = x;
    this.y = y;
  }
  
  public boolean equals(Object obj) {
    if (obj instanceof Coordinate2D) {
      Coordinate2D other = (Coordinate2D)obj;
      return(x == other.x && y == other.y);
    }
    return(false);
  }
  
  public int hashCode() {
    long v = Double.doubleToLongBits(x + y);
    return((int)(v^(v>>>32)));  
  }  
}
