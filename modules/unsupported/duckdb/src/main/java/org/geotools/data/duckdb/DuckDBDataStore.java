/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import java.io.IOException;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.jdbc.JDBCDataStore;

/** Public-facing DuckDB datastore wrapper that avoids exposing the raw JDBC datastore type. */
public class DuckDBDataStore extends ForwardingDataStore<JDBCDataStore> {

    private final boolean readOnly;

    public DuckDBDataStore(JDBCDataStore delegate) {
        this(delegate, false);
    }

    public DuckDBDataStore(JDBCDataStore delegate, boolean readOnly) {
        super(delegate);
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
        return ForwardingFeatureSource.wrap(delegate.getFeatureSource(typeName), this, readOnly);
    }

    @Override
    public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
        return ForwardingFeatureSource.wrap(delegate.getFeatureSource(typeName), this, readOnly);
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        assertWritable("createSchema");
        super.createSchema(featureType);
    }

    @Override
    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        assertWritable("updateSchema");
        super.updateSchema(typeName, featureType);
    }

    @Override
    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        assertWritable("updateSchema");
        super.updateSchema(typeName, featureType);
    }

    @Override
    public void removeSchema(Name typeName) throws IOException {
        assertWritable("removeSchema");
        super.removeSchema(typeName);
    }

    @Override
    public void removeSchema(String typeName) throws IOException {
        assertWritable("removeSchema");
        super.removeSchema(typeName);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Filter filter, Transaction transaction) throws IOException {
        assertWritable("getFeatureWriter");
        return super.getFeatureWriter(typeName, filter, transaction);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName, Transaction transaction)
            throws IOException {
        assertWritable("getFeatureWriter");
        return super.getFeatureWriter(typeName, transaction);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
            String typeName, Transaction transaction) throws IOException {
        assertWritable("getFeatureWriterAppend");
        return super.getFeatureWriterAppend(typeName, transaction);
    }

    private void assertWritable(String operation) throws IOException {
        if (readOnly) {
            throw new IOException(
                    "DuckDB datastore is read-only (read_only=true), operation not permitted: " + operation);
        }
    }
}
