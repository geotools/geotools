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

import java.util.Iterator;

import org.geotools.geometry.iso.topograph2D.EdgeEnd;
import org.geotools.geometry.iso.topograph2D.EdgeEndStar;
import org.geotools.geometry.iso.topograph2D.IntersectionMatrix;

/**
 * An ordered list of {@link EdgeEndBundle}s around a {@link RelateNode}. They
 * are maintained in CCW order (starting with the positive x-axis) around the
 * node for efficient lookup and topology building.
 * 
 *
 * @source $URL$
 * @version 1.7.2
 */
public class EdgeEndBundleStar extends EdgeEndStar {

	public EdgeEndBundleStar() {
	}

	/**
	 * Insert a EdgeEnd in order in the list. If there is an existing
	 * EdgeStubBundle which is parallel, the EdgeEnd is added to the bundle.
	 * Otherwise, a new EdgeEndBundle is created to contain the EdgeEnd. <br>
	 */
	public void insert(EdgeEnd e) {
		EdgeEndBundle eb = (EdgeEndBundle) edgeMap.get(e);
		if (eb == null) {
			eb = new EdgeEndBundle(e);
			insertEdgeEnd(e, eb);
		} else {
			eb.insert(e);
		}
	}

	/**
	 * Update the IM with the contribution for the EdgeStubs around the node.
	 */
	void updateIM(IntersectionMatrix im) {
		for (Iterator it = iterator(); it.hasNext();) {
			EdgeEndBundle esb = (EdgeEndBundle) it.next();
			esb.updateIM(im);
		}
	}

}
