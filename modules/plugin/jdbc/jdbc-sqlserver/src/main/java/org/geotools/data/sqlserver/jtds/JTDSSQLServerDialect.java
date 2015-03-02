/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sqlserver.jtds;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;

import org.geotools.data.sqlserver.SQLServerDialect;
import org.geotools.jdbc.JDBCDataStore;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author ian.turton
 *
 */
public class JTDSSQLServerDialect extends SQLServerDialect {

    /**
     * @param dataStore
     */
    public JTDSSQLServerDialect(JDBCDataStore dataStore) {
        super(dataStore);

    }

    @Override
    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {

        super.registerClassToSqlMappings(mappings);

        // mappings.put(DateTime.class,Types.TIMESTAMP);
    }

    @Override
    public void registerSqlTypeNameToClassMappings(Map<String, Class<?>> mappings) {

        super.registerSqlTypeNameToClassMappings(mappings);
        mappings.put("datetime", Timestamp.class);
        mappings.put("time", Time.class);
        mappings.put("date", Date.class);
        mappings.put( "image", Geometry.class );
        mappings.put("smallmoney", Float.class);
        mappings.put("money", Double.class);
    }

}
