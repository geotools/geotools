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

import java.util.Iterator;

import org.geotools.graph.structure.DirectedGraphable;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.traverse.GraphTraversal;


/**
 *
 * @source $URL$
 */
public class DirectedDepthFirstIterator extends DepthFirstIterator {

  public void cont(Graphable current, GraphTraversal traversal) {
    //only consider outing going related
    DirectedGraphable dg = (DirectedGraphable)current;
    for (Iterator itr = dg.getOutRelated(); itr.hasNext();) {
      DirectedGraphable related = (DirectedGraphable)itr.next();
      if (!traversal.isVisited(related)) getQueue().enq(related);  
    }
  }

}
