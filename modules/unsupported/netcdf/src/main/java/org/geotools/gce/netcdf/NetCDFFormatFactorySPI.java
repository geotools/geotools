/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
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
