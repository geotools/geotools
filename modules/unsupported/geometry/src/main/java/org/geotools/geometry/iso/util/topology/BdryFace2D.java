/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.util.topology;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roehrig
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 *
 * @source $URL$
 */
public class BdryFace2D extends BRepFace2D {

	protected BdryRing2D extRing;
	protected ArrayList intRings = null;
	
	public BdryFace2D(BdryRing2D extRing, List intRings) {
		this.extRing = extRing;
		if (intRings!=null) {
			this.intRings = new ArrayList(intRings);
		}
	}
	
	
	/**
	 * @return Returns the extRing.
	 */
	public BdryRing2D getExtRing() {
		return extRing;
	}
	/**
	 * @return Returns the intRings.
	 */
	public ArrayList getIntRings() {
		return intRings;
	}

	public void split(double maxLength) {

		extRing.split(maxLength);
	}

}
