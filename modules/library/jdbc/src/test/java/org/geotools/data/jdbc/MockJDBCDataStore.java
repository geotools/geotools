/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;

import javax.sql.DataSource;

import org.geotools.data.AttributeReader;
import org.geotools.data.AttributeWriter;
import org.geotools.data.DataSourceException;
import org.geotools.data.ServiceInfo;
import org.geotools.data.jdbc.attributeio.AttributeIO;
import org.geotools.data.jdbc.fidmapper.BasicFIDMapper;
import org.geotools.data.jdbc.fidmapper.TypedFIDMapper;
import org.opengis.feature.type.AttributeDescriptor;

/** Provides a Mock JDBC DataStore for testing the abstract DataStore implementation.
 * 
 *  @author Sean Geoghegan, Defence Science and Technology Organisation.
 * @source $URL$
 */
public class MockJDBCDataStore extends JDBCDataStore {
	
    /**
     * @param connectionPool
     * @throws DataSourceException
     */
    public MockJDBCDataStore(DataSource dataSource) throws IOException {
        super( dataSource, new JDBCDataStoreConfig("www.refractions.net","test",10000) );                
        typeHandler.setFIDMapper("FEATURE_TYPE1", new TypedFIDMapper(new BasicFIDMapper("ID", 255), "FEATURE_TYPE1"));
		typeHandler.setFIDMapper("FEATURE_TYPE2", new TypedFIDMapper(new BasicFIDMapper("ID", 255), "FEATURE_TYPE2"));
    }

   public ServiceInfo getInfo() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.jdbc.JDBCDataStore#createGeometryReader(org.geotools.feature.AttributeType, org.geotools.data.jdbc.JDBCDataStore.QueryData, int)
     */
    protected AttributeReader createGeometryReader(AttributeDescriptor attrType, QueryData queryData, int index)
        throws DataSourceException {
        return null;
    }
    
    
    /* (non-Javadoc)
     * @see org.geotools.data.jdbc.JDBCDataStore#createGeometryWriter(org.geotools.feature.AttributeType, org.geotools.data.jdbc.JDBCDataStore.QueryData, int)
     */
    protected AttributeWriter createGeometryWriter(AttributeDescriptor attrType, QueryData queryData, int index)
        throws DataSourceException {     
        return null;
    }



    /**
     * @see org.geotools.data.jdbc.JDBCDataStore#getGeometryAttributeIO(org.geotools.feature.AttributeType)
     */
    protected AttributeIO getGeometryAttributeIO(AttributeDescriptor type, QueryData queryData) {
        return null;
    }
}
