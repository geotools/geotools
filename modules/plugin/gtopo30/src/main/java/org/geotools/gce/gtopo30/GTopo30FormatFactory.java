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
 *
 */
package org.geotools.gce.gtopo30;

import java.awt.RenderingHints;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.io.GridFormatFactorySpi;

/**
 * The GTopo30FormatFactory will be discovered by the GridFormatFinder. Use the
 * standard Geotools method of discovering a factory in order to create a
 * format.
 * 
 * @author Simone Giannecchini
 * @author mkraemer
 *
 * @source $URL$
 */
public class GTopo30FormatFactory implements GridFormatFactorySpi {

	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.gce.gtopo30");

	/**
	 * Creates a new instance of GTopo30Format
	 * 
	 * @return an instance of GTopo30Format
	 */
        public GTopo30Format createFormat() {
		return new GTopo30Format();
	}

	/**
	 * Checks for the JAI library which is needed by the GTopo30DataSource
	 * 
	 * @return true if all libraries are available
	 */
	public boolean isAvailable() {
		boolean available = true;

		// if these classes are here, then the runtine environment has
		// access to JAI and the JAI ImageI/O toolbox.

		try {
			Class.forName("javax.media.jai.JAI");
			Class.forName("com.sun.media.jai.operator.ImageReadDescriptor");
		} catch (ClassNotFoundException e) {
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
			available = false;
		}

		return available;
	}

	/**
	 * Returns the implementation hints
	 * 
	 * @return the implementation hints (an empty map, actually)
	 */
	public Map<RenderingHints.Key, ?> getImplementationHints() {
		return Collections.emptyMap();
	}
}
