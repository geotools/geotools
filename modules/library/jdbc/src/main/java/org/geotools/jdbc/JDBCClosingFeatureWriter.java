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

import java.io.IOException;
import java.sql.Connection;

import org.geotools.data.DelegatingFeatureWriter;
import org.geotools.data.FeatureWriter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class JDBCClosingFeatureWriter implements FeatureWriter<SimpleFeatureType,SimpleFeature> {

    FeatureWriter writer;
    
    public JDBCClosingFeatureWriter(FeatureWriter writer) {
        this.writer = writer;
    }

   public SimpleFeatureType getFeatureType() {
        return (SimpleFeatureType) writer.getFeatureType();
    }

    public boolean hasNext() throws IOException {
        return writer.hasNext();
    }


    public SimpleFeature next() throws IOException {
        return (SimpleFeature) writer.next();
    }

    
    public void remove() throws IOException {
        writer.remove();
    }

    public void write() throws IOException {
        writer.write();
    }
    
    public void close() throws IOException {
        FeatureWriter w = writer;
        while( w instanceof DelegatingFeatureWriter ) {
            if ( w instanceof JDBCFeatureReader ) {
                break;
            }
            
            w = ((DelegatingFeatureWriter)w).getDelegate();
        }
        
        if ( w instanceof JDBCFeatureReader ) {
            JDBCFeatureReader jdbcReader = (JDBCFeatureReader) w;
            JDBCFeatureSource fs = jdbcReader.featureSource;
            Connection cx = jdbcReader.cx;

            try {
                writer.close();
            }
            finally {
                fs.getDataStore().releaseConnection( cx, fs.getState() );
            }
        }
    }
}
