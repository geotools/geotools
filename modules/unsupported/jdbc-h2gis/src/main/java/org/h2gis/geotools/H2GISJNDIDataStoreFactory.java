/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.h2gis.geotools;

import static org.h2gis.geotools.H2GISDataStoreFactory.ASSOCIATIONS;
import static org.h2gis.geotools.H2GISDataStoreFactory.AUTO_SERVER;
import static org.h2gis.geotools.H2GISDataStoreFactory.ENCODE_FUNCTIONS;
import static org.h2gis.geotools.H2GISDataStoreFactory.ESTIMATED_EXTENTS;
import static org.h2gis.geotools.H2GISDataStoreFactory.PREPARED_STATEMENTS;

import java.util.Map;
import org.geotools.jdbc.JDBCJNDIDataStoreFactory;

/**
 * jdbc-h2gis is an extension to connect H2GIS a spatial library that brings spatial support to the
 * H2 Java database.
 *
 * <p>JNDI DataStoreFactory for H2GIS database.
 *
 * @author Erwan Bocher
 */
public class H2GISJNDIDataStoreFactory extends JDBCJNDIDataStoreFactory {

    public H2GISJNDIDataStoreFactory() {
        super(new H2GISDataStoreFactory());
    }

    @Override
    protected void setupParameters(Map<String, Object> parameters) {
        super.setupParameters(parameters);
        parameters.put(ASSOCIATIONS.key, ASSOCIATIONS);
        parameters.put(AUTO_SERVER.key, AUTO_SERVER);
        parameters.put(ESTIMATED_EXTENTS.key, ESTIMATED_EXTENTS);
        parameters.put(PREPARED_STATEMENTS.key, PREPARED_STATEMENTS);
        parameters.put(ENCODE_FUNCTIONS.key, ENCODE_FUNCTIONS);
    }
}
