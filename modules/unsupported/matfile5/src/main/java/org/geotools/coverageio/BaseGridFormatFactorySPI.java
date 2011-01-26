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
package org.geotools.coverageio;

import java.awt.RenderingHints.Key;
import java.util.Collections;
import java.util.Map;

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.UnknownFormat;

/**
 * Base implementation for {@link GridFormatFactorySpi}.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 * @since 2.5.x
 */
public class BaseGridFormatFactorySPI implements GridFormatFactorySpi {

	public AbstractGridFormat createFormat() {
		return new UnknownFormat();
	}

	public boolean isAvailable() {
		return false;
	}

	public Map<Key, ?> getImplementationHints() {
		return Collections.emptyMap();
	}

}
