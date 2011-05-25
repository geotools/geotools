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
package org.geotools.graph.util.graph;

import org.geotools.graph.structure.Graph;
import org.geotools.graph.traverse.GraphIterator;
import org.geotools.graph.traverse.standard.DirectedBreadthFirstTopologicalIterator;

/**
 * Detects cycles in a directed graph. A directed topological iteration 
 * of the nodes of the graph is performed. If the iteration includes all nodes 
 * in the graph then the graph is cycle free, otherwise a cycle exists. 
 * 
 * @see org.geotools.graph.traverse.standard.DirectedBreadthFirstTopologicalIterator
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */

public class DirectedCycleDetector extends CycleDetector {

	public DirectedCycleDetector(Graph graph) {
		super(graph);
	}

	protected GraphIterator createIterator() {
		return(new DirectedBreadthFirstTopologicalIterator());
	}

	
}
