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

import org.geotools.coverage.io.CoverageAccess.AccessType;

/**
 * Describes the capabilities of this {@link CoverageSource}
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-api/src/main/java/org/geotools/coverage/io/CoverageCapabilities.java $
 */
public enum CoverageCapabilities {
	READ_SUBSAMPLING(AccessType.READ_ONLY),
	READ_RANGE_SUBSETTING(AccessType.READ_ONLY),
	READ_HORIZONTAL_DOMAIN_SUBSAMBLING(AccessType.READ_ONLY),
	READ_REPROJECTION(AccessType.READ_ONLY),
	WRITE_HORIZONTAL_DOMAIN_SUBSAMBLING(AccessType.READ_WRITE),
	WRITE_RANGE_SUBSETTING(AccessType.READ_WRITE),
	WRITE_SUBSAMPLING(AccessType.READ_WRITE);
	
	/**
	 * Access requirement for this capability to be allowed.
	 */
	final AccessType access;
	
	private CoverageCapabilities(AccessType accessType) {
		access = accessType;
	}
	
	/**
	 * Check if this capability is permissible for the provided access type.
	 * 
	 * @param type
	 * @return true if capability is permissible
	 */
	public boolean isSupported( AccessType type ){
		return access.compareTo(type) <= 0;
	}
}
