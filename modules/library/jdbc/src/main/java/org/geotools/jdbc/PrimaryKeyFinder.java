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
 */
package org.geotools.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.opengis.feature.type.FeatureType;

/**
 * A strategy object used by the {@link JDBCDataStore} to determine the {@link PrimaryKey} for a
 * certain {@link FeatureType}
 * 
 * @author Andrea Aime - OpenGeo
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/jdbc/src/main/java/org/geotools/jdbc/PrimaryKeyFinder.java $
 */
public abstract class PrimaryKeyFinder {

    /**
     * Returns the {@link PrimaryKey}, or {@code null} if a specific type could not be determined
     * (the datastore will fall back on {@link NullPrimaryKey} in that case. It is advised to return
     * 
     * @param schema
     * @param table
     * @param cx
     * @return
     */
    public abstract PrimaryKey getPrimaryKey(JDBCDataStore store, String schema, String table, Connection cx) throws SQLException;
}
