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
package org.geotools.gce.imagemosaic.jdbc;

/**
 * Enum for support spatial extension
 * 
 * @author mcr
 * 
 */
public enum SpatialExtension {
	DB2("DB2"), ORACLE("ORACLE"), POSTGIS("POSTGIS"), MYSQL("MYSQL"), UNIVERSAL(
			"UNIVERSAL"), GEORASTER("GEORASTER"), CUSTOM("CUSTOM");
	private SpatialExtension(String name) {
		this.name = name;
	}

	private String name;

	public String toString() {
		return name;
	}

	/**
	 * Factory method for obtaining a SpatialExtension object from a string
	 * 
	 * @param spatName
	 *            The string representation for teh SpatialExtension object
	 * @return the corresponding SpatialExtension object, null if spatname is
	 *         unknown
	 */
	static SpatialExtension fromString(String spatName) {
		if ("DB2".equalsIgnoreCase(spatName)) {
			return DB2;
		}

		if ("ORACLE".equalsIgnoreCase(spatName)) {
			return ORACLE;
		}

		if ("MYSQL".equalsIgnoreCase(spatName)) {
			return MYSQL;
		}

		if ("POSTGIS".equalsIgnoreCase(spatName)) {
			return POSTGIS;
		}

		if ("UNIVERSAL".equalsIgnoreCase(spatName)) {
			return UNIVERSAL;
		}
                if ("GEORASTER".equalsIgnoreCase(spatName)) {
                        return GEORASTER;
                }
                if ("CUSTOM".equalsIgnoreCase(spatName)) {
                    return CUSTOM;
                }

		return null;
	}
}
