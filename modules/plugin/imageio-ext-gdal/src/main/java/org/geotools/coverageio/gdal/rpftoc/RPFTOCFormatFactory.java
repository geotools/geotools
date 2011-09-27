/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverageio.gdal.rpftoc;
	   
import it.geosolutions.imageio.plugins.rpftoc.RPFTOCImageReaderSpi;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverageio.BaseGridFormatFactorySPI;
import org.opengis.coverage.grid.Format;

/**
 * Implementation of the {@link Format} service provider interface for RPFTOC
 * files.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.5.x
 *
 *
 * @source $URL$
 */
public final class RPFTOCFormatFactory extends BaseGridFormatFactorySPI  implements GridFormatFactorySpi {
	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger("org.geotools.coverageio.gdal.rpftoc");

	/**
	 * Tells me if the coverage plugin to access Erdas imagine is available or
	 * not.
	 * 
	 * @return <code>true</code> if the plugin is available,
	 *         <code>false</code> otherwise.
	 */
	public boolean isAvailable() {
		boolean available = true;

		// if these classes are here, then the runtime environment has
		// access to JAI and the JAI ImageI/O toolbox.
		try {
			Class
					.forName("it.geosolutions.imageio.plugins.rpftoc.RPFTOCImageReaderSpi");
			available = new RPFTOCImageReaderSpi().isAvailable();

			if (LOGGER.isLoggable(Level.FINE)) {
				if (available) {
					LOGGER.fine("RPFTOCFormatFactory is availaible.");
				} else {
					LOGGER.fine("RPFTOCFormatFactory is not availaible.");
				}
			}
		} catch (ClassNotFoundException cnf) {
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine("RPFTOCFormatFactory is not availaible.");
			}

			available = false;
		}

		return available;
	}

	/**
	 * Creating a {@link RPFTOCFormat}
	 * 
	 * @return A {@link RPFTOCFormat}
	 */
	public RPFTOCFormat createFormat() {
		return new RPFTOCFormat();
	}
}
