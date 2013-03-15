/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.netcdf;

import java.awt.RenderingHints.Key;
import java.util.Collections;
import java.util.Map;

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;

public class NetCDFFormatFactorySPI implements GridFormatFactorySpi {

	/**
	 * Always return true. As this gets more complex we will need to add better
	 * checking here.
	 * 
	 * @return boolean isAvailable
	 */
	public boolean isAvailable() {
		return true;
	}

	/**
	 * Currently returns empty collection. TODO: Add hints
	 * 
	 * @return Map<Key, ?> hints
	 */
	@SuppressWarnings("unchecked")
	public Map<Key, ?> getImplementationHints() {
		return Collections.EMPTY_MAP;
	}

	/**
	 * @return AbstractGridFormat NetCDFFormat
	 */
	public AbstractGridFormat createFormat() {
		return new NetCDFFormat();
	}
}
