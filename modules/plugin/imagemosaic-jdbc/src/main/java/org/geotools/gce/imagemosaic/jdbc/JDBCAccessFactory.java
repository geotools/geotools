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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.geotools.gce.imagemosaic.jdbc.custom.JDBCAccessOracleGeoRaster;

/**
 * Factory for JDBCAccess Objects.
 * 
 * The following rule applies:
 * 
 * For each Config object exists exactly one JDBCAccess object !
 * 
 * @author mcr
 * 
 */
class JDBCAccessFactory {
	static Map<String, JDBCAccess> JDBCAccessMap = new HashMap<String, JDBCAccess>();

	/**
	 * Factory method
	 * 
	 * @param config
	 *            The Config object
	 * @return the corresponding JDBCAccess object
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
    static synchronized JDBCAccess getJDBCAcess(Config config) throws Exception {
		JDBCAccess jdbcAccess = JDBCAccessMap.get(config.getXmlUrl());

		if (jdbcAccess != null) {
			return jdbcAccess;
		}

		SpatialExtension type = config.getSpatialExtension();

		if (type == null) {
			throw new Exception("Property <spatialExtension> missing");
		}

		if (type == SpatialExtension.DB2) {
			jdbcAccess = new JDBCAccessDB2(config);
		} else if (type == SpatialExtension.POSTGIS) {
			jdbcAccess = new JDBCAccessPostGis(config);
		} else if (type == SpatialExtension.MYSQL) {
			jdbcAccess = new JDBCAccessMySql(config);
		} else if (type == SpatialExtension.UNIVERSAL) {
			jdbcAccess = new JDBCAccessUniversal(config);
		} else if (type == SpatialExtension.ORACLE) {
			jdbcAccess = new JDBCAccessOracle(config);
                } else if (type == SpatialExtension.GEORASTER) {
                    jdbcAccess = new JDBCAccessOracleGeoRaster(config);
                } else if (type == SpatialExtension.CUSTOM) {
                    String jdbcAccessClassName = config.getJdbcAccessClassName();
                    Class jdbcAccessClass = Class.forName(jdbcAccessClassName);
                    try {
                        Constructor cons = jdbcAccessClass.getConstructor(new Class[] { Config.class });
                        jdbcAccess = (JDBCAccess) cons.newInstance(new Object[] {config });
                    } catch (Exception ex) {
                        String msg = "No public Constructor with an "+config.getClass().getName()+ 
                               " argument for class "+jdbcAccessClassName ;
                        throw new RuntimeException(msg, ex);
                    }
                                        
		} else {
			throw new Exception("spatialExtension: " + type + " not supported");
		}

		jdbcAccess.initialize();
		JDBCAccessMap.put(config.getXmlUrl(), jdbcAccess);

		return jdbcAccess;
	}
}
