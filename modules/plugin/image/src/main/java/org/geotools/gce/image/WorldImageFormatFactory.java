/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.gce.image;

import java.util.Collections;
import java.util.Map;

import org.geotools.coverage.grid.io.GridFormatFactorySpi;

/**
 * DOCUMENT ME!
 * 
 * @author rgould TODO To change the template for this generated type comment go
 *         to Window - Preferences - Java - Code Style - Code Templates
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/plugin/image/src/org/geotools/gce/image/WorldImageFormatFactory.java $
 */
public final class WorldImageFormatFactory implements GridFormatFactorySpi {
	public WorldImageFormat createFormat() {
		return new WorldImageFormat();
	}

	public boolean isAvailable() {
		boolean available = true;

		// if these classes are here, then the runtine environment has
		// access to JAI and the JAI ImageI/O toolbox.
		try {
			Class.forName("javax.media.jai.JAI");
			Class.forName("com.sun.media.jai.operator.ImageReadDescriptor");
		} catch (ClassNotFoundException cnf) {
			available = false;
		}

		return available;
	}

	/**
	 * Returns the implementation hints. The default implementation returns en
	 * empty map.
	 * 
	 */
	public Map getImplementationHints() {
		return Collections.EMPTY_MAP;
	}
}
