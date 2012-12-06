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
package org.geotools.data.sqlserver;

import static org.geotools.data.sqlserver.SQLServerDataStoreFactory.*;

import java.util.Map;

import org.geotools.jdbc.JDBCJNDIDataStoreFactory;

/**
 * JNDI DataStoreFactory for sql server database.
 * 
 * @author Christian Mueller
 * 
 *
 *
 *
 * @source $URL$
 */
public class SQLServerJNDIDataStoreFactory extends JDBCJNDIDataStoreFactory {

    public SQLServerJNDIDataStoreFactory() {
        super(new SQLServerDataStoreFactory());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
        parameters.put(INTSEC.key, INTSEC);
        parameters.put(GEOMETRY_METADATA_TABLE.key, GEOMETRY_METADATA_TABLE);
    }
}
