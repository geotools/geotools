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
package org.geotools.geometry.iso.operation.overlay;

/**
 * @version 1.7.2
 */
// import com.vividsolutions.jts.geomgraph.*;
import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.DirectedEdgeStar;
import org.geotools.geometry.iso.topograph2D.Node;
import org.geotools.geometry.iso.topograph2D.NodeFactory;
import org.geotools.geometry.iso.topograph2D.PlanarGraph;

/**
 * Creates nodes for use in the {@link PlanarGraph}s constructed during overlay
 * operations.
 * 
 *
 * @source $URL$
 * @version 1.7.2
 */
public class OverlayNodeFactory extends NodeFactory {
	public Node createNode(Coordinate coord) {
		return new Node(coord, new DirectedEdgeStar());
	}
}
