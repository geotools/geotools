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
package org.geotools.geometry.iso.operation.relate;

import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.Node;
import org.geotools.geometry.iso.topograph2D.NodeFactory;
import org.geotools.geometry.iso.topograph2D.NodeMap;

/**
 * Used by the {@link NodeMap} in a {@link RelateNodeGraph} to create
 * {@link RelateNode}s.
 *
 *
 * @source $URL$
 */
public class RelateNodeFactory extends NodeFactory {
	public Node createNode(Coordinate coord) {
		return new RelateNode(coord, new EdgeEndBundleStar());
	}
}
