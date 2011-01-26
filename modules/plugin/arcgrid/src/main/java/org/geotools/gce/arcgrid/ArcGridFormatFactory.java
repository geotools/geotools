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
package org.geotools.gce.arcgrid;

import java.awt.RenderingHints;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.opengis.coverage.grid.Format;

/**
 * Implementation of the {@link Format} service provider interface for arc
 * grid files.
 * 
 * @author Daniele Romagnoli
 * @author Simone Giannecchini (simboss)
 */
public final class ArcGridFormatFactory implements GridFormatFactorySpi {
	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.gce.arcgrid");

	/**
	 * Tells me if the coverage plugin to access Ascii grids is availaible or
	 * not.
	 * 
	 * @return True if the plugin is availaible, False otherwise.
	 */
	public boolean isAvailable() {
		boolean available = true;

		// if these classes are here, then the runtine environment has
		// access to JAI and the JAI ImageI/O toolbox.
		try {

			Class.forName("javax.media.jai.JAI");
			Class.forName("com.sun.media.jai.operator.ImageReadDescriptor");
			Class.forName("it.geosolutions.imageio.plugins.arcgrid.AsciiGridsImageMetadata");
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("ArcGridFormatFactory is availaible.");
		} catch (ClassNotFoundException cnf) {
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("ArcGridFormatFactory is not availaible.");
			available = false;
		}

		return available;
	}

	/**
	 * Creating an {@link ArcGridFormat}.
	 * 
	 * @return An {@link ArcGridFormat}.;
	 */
	public ArcGridFormat createFormat() {
		return new ArcGridFormat();
	}

	/**
	 * Returns the implementation hints. The default implementation returns en
	 * empty map.
	 * 
	 * @return DOCUMENT ME!
	 */
	public Map<RenderingHints.Key, ?> getImplementationHints() {
		return Collections.emptyMap();
	}
}
