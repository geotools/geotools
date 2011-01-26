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

import java.io.IOException;
import java.sql.SQLException;

import org.geotools.data.FeatureReader;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.Geometry;


/**
 * JDBCDataStore specific implementation of the  FeatureReader<SimpleFeatureType, SimpleFeature> interface
 *
 * @author aaime
 * @source $URL$
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class JDBCFeatureReader implements  FeatureReader<SimpleFeatureType, SimpleFeature> {
    SimpleFeatureType featureType;
    QueryData queryData;
    Object[] fidAttributes;
    SimpleFeatureBuilder builder;

    /**
     * Creates a new JDBCFeatureReader object.
     *
     * @param queryData 
     *
     * @throws IOException 
     */
    public JDBCFeatureReader(QueryData queryData) throws IOException {
        this.queryData = queryData;
        fidAttributes = new Object[queryData.getMapper().getColumnCount()];
        builder = new SimpleFeatureBuilder(queryData.getFeatureType());
    }

    /**
     * @see org.geotools.data.FeatureReader#close()
     */
    public void close() throws IOException {
        close(null);
    }

    void close(SQLException sqlException) {
        queryData.close(sqlException);
    }

    /**
     * @see org.geotools.data.FeatureReader#hasNext()
     */
    public boolean hasNext() throws IOException {
        if (queryData.isClosed()) {
            throw new IOException("Reader is closed");
        }

        return queryData.hasNext();
    }

    /**
     * @see org.geotools.data.FeatureReader#next()
     */
    public SimpleFeature next() throws IllegalAttributeException, IOException {
        if (queryData.isClosed()) {
            throw new IOException("The feature reader has been closed");
        }

        return readFeature();
    }

    /**
     * Really reads the feature from the QueryData object
     * @throws IllegalAttributeException
     * @throws IOException
     */
    private SimpleFeature readFeature() throws IllegalAttributeException, IOException {
        queryData.next();
        
        for (int i = 0; i < fidAttributes.length; i++) {
            fidAttributes[i] = queryData.readFidColumn(i);
        }

        FIDMapper mapper = queryData.getMapper();
        String fid = mapper.getID(fidAttributes);

        int attributeCount = queryData.getFeatureType().getAttributeCount();
		for (int i = 0; i < attributeCount; i++) {
        	Object attribute = queryData.read( i );
        	
        	//JD: check for a coordinate system, if present on the type, set on the geometry
        	// I know this is pretty loose, but its better then nothing
        	if ( attribute instanceof Geometry && 
        			queryData.getFeatureType().getDescriptor( i ) instanceof GeometryDescriptor ) {
        		Geometry geometry = (Geometry) attribute;
        		GeometryDescriptor geometryType = 
        			(GeometryDescriptor) queryData.getFeatureType().getDescriptor( i );
        		
        		if ( geometryType.getCoordinateReferenceSystem() != null ) {
        			geometry.setUserData( geometryType.getCoordinateReferenceSystem() );
        		}
        	}
        	
            builder.add(attribute);
        }

		return builder.buildFeature(fid);
    }

    /**
     * @see org.geotools.data.FeatureReader#getFeatureType()
     */
    public SimpleFeatureType getFeatureType() {
        return queryData.getFeatureType();
    }
}
