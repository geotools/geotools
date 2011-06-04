/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.jdbc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.opengis.feature.simple.SimpleFeatureType;



/**
 * Stores information about known FeatureTypes.
 *
 * @author Sean Geoghegan, Defence Science and Technology Organisation.
 *
 * @source $URL$
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class FeatureTypeInfo {
	private String featureTypeName;
	private SimpleFeatureType schema;
	private Map sridMap = new HashMap();
	private FIDMapper mapper;

	public FeatureTypeInfo(String typeName, SimpleFeatureType schema, FIDMapper mapper) {
		this.featureTypeName = typeName;
		this.schema = schema;
		this.mapper = mapper;
	}

	/**
	 * DOCUMENT ME!
	 *
	 */
	public String getFeatureTypeName() {
		return featureTypeName;
	}

	/**
	 * DOCUMENT ME!
	 *
	 */
	public SimpleFeatureType getSchema() {
		return schema;
	}

	/**
	 * Get the DataStore specific SRID for a geometry column
	 *
	 * @param geometryAttributeName The name of the Geometry column to get the srid for.
	 *
	 * @return The srid of the geometry column.  This will only be present if
	 *         determineSRID(String) of JDBCDataStore has been overridden.  If there is no
	 *         SRID registered -1 will be returned.
	 */
	public int getSRID(String geometryAttributeName) {
		int srid = -1;

		Integer integer = (Integer) sridMap.get(geometryAttributeName);

		if (integer != null) {
			srid = integer.intValue();
		}

		return srid;
	}

	public Map getSRIDs() {
		return Collections.unmodifiableMap(sridMap);
	}

	/**
	 * Puts the srid for a geometry column in the internal map.
	 *
	 * @param geometryColumnName The geometry column name.
	 * @param srid The SRID of the geometry column.
	 */
	void putSRID(String geometryColumnName, int srid) {
		sridMap.put(geometryColumnName, new Integer(srid));
	}

	public String toString() {
		return "typeName = " + featureTypeName + " schema: " +
		schema + "srids: " + sridMap;
	}
    /**
     */
    public FIDMapper getFIDMapper() {
        return mapper;
    }

    /**
     * @param mapper
     */
    public void setFIDMapper(FIDMapper mapper) {
        this.mapper = mapper;
    }

}

