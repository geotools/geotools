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
package org.geotools.graph.path;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a path in a graph. A <B>path</B> P is defined as a <B>walk</B> 
 * in which there are no node repetitions. 
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public class Path extends Walk {

  public Path() {
    super();	
  }
  
  //TODO: DOCUMENT ME!
  public Path(Collection nodes) {
    super(nodes);
  }

  /**
   * Tests if the path is valid. A valid path satisfies two conditions: <BR>
   * <BR>
   * 1. Each pair of adjacent nodes share an edge.<BR>
   * 2. There are no node repetitions.
   */
  public boolean isValid() {
    if (super.isValid()) {
      //test repetitions
      HashSet s = new HashSet(this);
      return(size() == s.size());
    }
    
    return(false);
  }

}
