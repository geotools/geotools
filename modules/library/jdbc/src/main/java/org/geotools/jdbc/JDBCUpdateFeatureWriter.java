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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureWriter;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentState;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Id;

public class JDBCUpdateFeatureWriter extends JDBCFeatureReader implements
        FeatureWriter<SimpleFeatureType, SimpleFeature> {

    ResultSetFeature last;
    ReferencedEnvelope lastBounds;
    
    public JDBCUpdateFeatureWriter(String sql, Connection cx,
            JDBCFeatureSource featureSource, Hints hints) throws SQLException, IOException {
        
        super(sql, cx, featureSource, featureSource.getSchema(), hints);
        last = new ResultSetFeature( rs, cx );
    }
    
    public JDBCUpdateFeatureWriter(PreparedStatement ps, Connection cx,
            JDBCFeatureSource featureSource, Hints hints) throws SQLException, IOException {
        
        super(ps, cx, featureSource, featureSource.getSchema(), hints);
        last = new ResultSetFeature( rs, ps.getConnection());
    }

    public SimpleFeature next() throws IOException, IllegalArgumentException,
            NoSuchElementException {
        
        ensureNext();
        
        try {
            last.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
     
        //reset next flag
        next = null;
    
        if( this.featureSource.getEntry().getState(tx).hasListener() ){
            // record bounds as we have a listener who will be interested
            this.lastBounds = new ReferencedEnvelope( last.getBounds() );
        }        
        return last;
    }
    
    public void remove() throws IOException {
        try {
            dataStore.delete(featureType, last.getID(), st.getConnection());
            
            // issue notification
            ContentEntry entry = featureSource.getEntry();
            ContentState state = entry.getState( this.tx );
            if( state.hasListener() ){
                state.fireFeatureRemoved( featureSource, last );
            }
        } catch (SQLException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }

    public void write() throws IOException {
        try {
            //figure out what the fid is
            PrimaryKey key = dataStore.getPrimaryKey(featureType);
            String fid = dataStore.encodeFID(key, rs);

            Id filter = dataStore.getFilterFactory()
                                 .id(Collections.singleton(dataStore.getFilterFactory()
                                                                    .featureId(fid)));

            //figure out which attributes changed
            List<AttributeDescriptor> changed = new ArrayList<AttributeDescriptor>();
            List<Object> values = new ArrayList<Object>();

            for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
                if (last.isDirrty(att.getLocalName())) {
                    changed.add(att);
                    values.add(last.getAttribute(att.getLocalName()));
                }
            }

            // do the write
            dataStore.update(featureType, changed, values, filter, st.getConnection());
            
            // issue notification
            ContentEntry entry = featureSource.getEntry();
            ContentState state = entry.getState( this.tx );
            if( state.hasListener() ){
                state.fireFeatureUpdated( featureSource, last, lastBounds );
            }
        } catch (Exception e) {
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
