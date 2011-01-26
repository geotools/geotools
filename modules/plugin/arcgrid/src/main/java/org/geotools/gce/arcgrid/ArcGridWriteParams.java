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

import java.util.Locale;

import javax.imageio.ImageWriteParam;

import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;

/**
 * @author Simone Giannecchini
 * @since 2.3.x
 * 
 *
 * @source $URL$
 */
public class ArcGridWriteParams extends GeoToolsWriteParams {

	/**
	 * Default constructor.
	 */
	public ArcGridWriteParams() {
		super(new ImageWriteParam(Locale.getDefault()));
		// allowed compression types
		compressionTypes = null;
		// default compression type
		compressionType = null;
		canWriteCompressed = false;
		canWriteProgressive = false;
		canWriteTiles = false;
		canOffsetTiles = false;
		controller = null;

	}

}
