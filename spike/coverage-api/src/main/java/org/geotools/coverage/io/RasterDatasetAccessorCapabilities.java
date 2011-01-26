/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io;


/**
 * Describes the capabilities of a {@link CoverageSource}.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public enum RasterDatasetAccessorCapabilities {
	READ_RANGE_SUBSETTING,
	READ_HORIZONTAL_DOMAIN_SUBSAMBLING,
	READ_REPROJECTION,
	WRITE_HORIZONTAL_DOMAIN_SUBSAMBLING,
	WRITE_RANGE_SUBSETTING,
	WRITE_SUBSAMPLING;
	
}