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
import org.geotools.api.data.DelegatingFeatureWriter;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;

public class JDBCClosingFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {

    FeatureWriter writer;

    public JDBCClosingFeatureWriter(FeatureWriter writer) {
        this.writer = writer;
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return (SimpleFeatureType) writer.getFeatureType();
    }

    @Override
    public boolean hasNext() throws IOException {
        return writer.hasNext();
    }

    @Override
    public SimpleFeature next() throws IOException {
        return (SimpleFeature) writer.next();
    }

    @Override
    public void remove() throws IOException {
        writer.remove();
    }

    @Override
    public void write() throws IOException {
        writer.write();
    }

    @Override
    @SuppressWarnings("PMD.CloseResource") // we are actually closing
    public void close() throws IOException {
        FeatureWriter w = writer;
        while (w instanceof DelegatingFeatureWriter) {
            if (w instanceof JDBCFeatureReader) {
                break;
            }

            w = ((DelegatingFeatureWriter) w).getDelegate();
        }

        if (w instanceof JDBCFeatureReader) {
            JDBCFeatureReader jdbcReader = (JDBCFeatureReader) w;
            JDBCFeatureSource fs = jdbcReader.featureSource;
            Connection cx = jdbcReader.cx;

            try {
                writer.close();
            } finally {
                fs.getDataStore().releaseConnection(cx, fs.getState());
            }
        } else if (w != null) {
            writer.close();
        }
    }
}
