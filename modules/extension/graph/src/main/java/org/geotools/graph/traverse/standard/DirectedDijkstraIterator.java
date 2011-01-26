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

public class DirectedDijkstraIterator extends DijkstraIterator {

	public DirectedDijkstraIterator(EdgeWeighter weighter) {
		super(weighter);
	}
	
	protected Iterator getRelated(Graphable current) {
		return(((DirectedGraphable)current).getOutRelated());
	}

	
}
