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
package org.geotools.data.oracle;

import java.util.Map;

import org.geotools.jdbc.JDBCJNDIDataStoreFactory;

/**
 * JNDI DataStoreFactory for oracle database.
 * 
 * @author Christian Mueller
 * 
 *
 *
 *
 * @source $URL$
 */
public class OracleNGJNDIDataStoreFactory extends JDBCJNDIDataStoreFactory {

    public OracleNGJNDIDataStoreFactory() {
        super(new OracleNGDataStoreFactory());
    }
    
    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
        
        parameters.put(OracleNGDataStoreFactory.LOOSEBBOX.key, OracleNGDataStoreFactory.LOOSEBBOX);
        parameters.put(OracleNGDataStoreFactory.ESTIMATED_EXTENTS.key, OracleNGDataStoreFactory.ESTIMATED_EXTENTS);
        parameters.put(OracleNGDataStoreFactory.GEOMETRY_METADATA_TABLE.key, OracleNGDataStoreFactory.GEOMETRY_METADATA_TABLE);
        parameters.put(OracleNGDataStoreFactory.METADATA_BBOX.key, OracleNGDataStoreFactory.METADATA_BBOX);
    }
}
