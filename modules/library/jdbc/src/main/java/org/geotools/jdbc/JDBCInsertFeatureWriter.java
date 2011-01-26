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
package org.geotools.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.geotools.data.FeatureWriter;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentState;
import org.geotools.factory.Hints;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class JDBCInsertFeatureWriter extends JDBCFeatureReader implements FeatureWriter<SimpleFeatureType, SimpleFeature> {

    
    ResultSetFeature last;
    
    public JDBCInsertFeatureWriter(String sql, Connection cx,
            JDBCFeatureSource featureSource, Hints hints) throws SQLException, IOException {
        super(sql, cx, featureSource, featureSource.getSchema(), hints);
        last = new ResultSetFeature( rs, cx );
    }

    public JDBCInsertFeatureWriter(PreparedStatement ps, Connection cx, JDBCFeatureSource featureSource, Hints hints)
        throws SQLException, IOException {
        super( ps, cx, featureSource, featureSource.getSchema(), hints );
        last = new ResultSetFeature( rs, ps.getConnection() );
    }
    
    public JDBCInsertFeatureWriter(JDBCUpdateFeatureWriter other) {
        super(other);
        last = other.last;
    }

    public boolean hasNext() throws IOException {
        return false;
    }

    public SimpleFeature next() throws IOException {
        //init, setting id to null explicity since the feature is yet to be 
        // inserted
        last.init(null);
        return last;
    }

    public void remove() throws IOException {
        //noop
    }

    public void write() throws IOException {
        try {
            //do the insert
            dataStore.insert(last, featureType, st.getConnection());
            
            //the datastore sets as userData, grab it and update the fid
            String fid = (String) last.getUserData().get( "fid" );
            last.setID( fid );
            
            ContentEntry entry = featureSource.getEntry();
            ContentState state = entry.getState( this.tx );            
            state.fireFeatureAdded( featureSource, last );
        } catch (SQLException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }

    public void close() throws IOException {
        super.close();
        
        if ( last != null ) {
            last.close();
            last = null;
        }
    }
}
