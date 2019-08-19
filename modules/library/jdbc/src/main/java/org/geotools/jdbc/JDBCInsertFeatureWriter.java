/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2015, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Arrays;
import java.util.Collection;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.data.store.ContentState;
import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Inserts features in the database. Buffers the insertions until BUFFER_SIZE is reached or the
 * writer is closed.
 */
public class JDBCInsertFeatureWriter extends JDBCFeatureReader
        implements FeatureWriter<SimpleFeatureType, SimpleFeature> {
    /** Grouping elements together in order to have a decent batch size. */
    private final ResultSetFeature[] buffer;

    private int curBufferPos = 0;

    public JDBCInsertFeatureWriter(
            String sql, Connection cx, JDBCFeatureSource featureSource, Query query)
            throws SQLException, IOException {
        super(sql, cx, featureSource, featureSource.getSchema(), query);
        md = rs.getMetaData();
        buffer = new ResultSetFeature[dataStore.getBatchInsertSize()];
    }

    public JDBCInsertFeatureWriter(
            PreparedStatement ps, Connection cx, JDBCFeatureSource featureSource, Query query)
            throws SQLException, IOException {
        super(ps, cx, featureSource, featureSource.getSchema(), query);
        md = rs.getMetaData();
        buffer = new ResultSetFeature[dataStore.getBatchInsertSize()];
    }

    public JDBCInsertFeatureWriter(JDBCUpdateFeatureWriter other) throws IOException {
        super(other);
        buffer = new ResultSetFeature[dataStore.getBatchInsertSize()];
    }

    private ResultSetFeature getOrCreateRSF() throws IOException {
        ResultSetFeature result = buffer[curBufferPos];
        if (result == null) {
            try {
                result = new ResultSetFeature(rs, cx);
                buffer[curBufferPos] = result;
            } catch (SQLException e) {
                throw new IOException(e);
            }
        }
        return result;
    }

    public boolean hasNext() throws IOException {
        return false;
    }

    public SimpleFeature next() throws IOException {
        // init, setting id to null explicity since the feature is yet to be
        // inserted
        ResultSetFeature rsf = getOrCreateRSF();
        rsf.init(null);
        return rsf;
    }

    public void remove() throws IOException {
        // noop
    }

    public void write() throws IOException {
        if (++curBufferPos >= buffer.length) {
            // buffer full => do the inserts
            flush();
        }
    }

    @Override
    protected void cleanup() throws IOException {
        try {
            flush();
        } finally {
            for (int i = 0; i < buffer.length; i++) {
                if (buffer[i] == null) {
                    break;
                }
                buffer[i].close();
                buffer[i] = null;
            }
            super.cleanup();
        }
    }

    private void flush() throws IOException {
        if (curBufferPos == 0) {
            return;
        }
        try {
            // do the insert
            Collection<ResultSetFeature> features =
                    Arrays.asList(Arrays.copyOfRange(buffer, 0, curBufferPos));
            dataStore.insert(features, featureType, st.getConnection());

            for (ResultSetFeature cur : features) {
                // the datastore sets as userData, grab it and update the fid
                final String fid = (String) cur.getUserData().get("fid");
                cur.setID(fid);
                final SimpleFeature orig =
                        (SimpleFeature)
                                cur.getUserData().get(ContentFeatureStore.ORIGINAL_FEATURE_KEY);
                if (orig != null) {
                    ((FeatureIdImpl) orig.getIdentifier()).setID(fid);
                    orig.getUserData().putAll(cur.getUserData());
                    orig.getUserData().remove(ContentFeatureStore.ORIGINAL_FEATURE_KEY);
                }

                final ContentEntry entry = featureSource.getEntry();
                final ContentState state = entry.getState(this.tx);
                state.fireFeatureAdded(featureSource, cur);
            }
        } catch (SQLException e) {
            throw new IOException(e);
        } finally {
            curBufferPos = 0;
        }
    }

    public void close() throws IOException {
        try {
            flush();
        } finally {
            super.close();
        }
    }
}
