/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * A data store wrapped around a {@link SimpleFeatureSource} object. The store per se cannot do any
 * kind of reads and writes, as the methods of the store are dealing with transactions per call,
 * while the SimpleFeatureSource/SimpleFeatureStore implementations are dealing with them using a
 * field, so in a stateful way, which means the Transaction provided in the parameters cannot be
 * honored in a multi-threaded context. As a result the store won't do any attempt do to reads and
 * writes, please use the source itself to do such operations, and treat transactions accordingly.
 */
public class SingleFeatureSourceDataStore implements DataStore {

    SimpleFeatureSource source;

    public SingleFeatureSourceDataStore(SimpleFeatureSource fs) {
        this.source = fs;
    }

    @Override
    public ServiceInfo getInfo() {
        try {
            DefaultServiceInfo info = new DefaultServiceInfo();
            SimpleFeatureType schema = source.getSchema();
            info.setDescription("Features from " + schema.getName());
            info.setSchema(new URI(schema.getName().getNamespaceURI()));
            info.setTitle(schema.getName().toString());
            return info;
        } catch (Exception e) {
            throw new RuntimeException("Feature source returned an invalid namespace URI", e);
        }
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Name> getNames() throws IOException {
        return Arrays.asList(source.getSchema().getName());
    }

    @Override
    public SimpleFeatureType getSchema(Name name) throws IOException {
        SimpleFeatureType schema = source.getSchema();
        if (schema.getName().equals(name)) {
            return schema;
        } else {
            return null;
        }
    }

    @Override
    public void dispose() {
        // nothing to do here
    }

    @Override
    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getTypeNames() throws IOException {
        return new String[] { source.getSchema().getName().getLocalPart() };
    }

    @Override
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        SimpleFeatureType schema = source.getSchema();
        if (schema.getName().getLocalPart().equals(typeName)) {
            return schema;
        } else {
            throw new IOException("Schema '" + typeName + "' does not exist.");
        }
    }

    @Override
    public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
        SimpleFeatureType schema = source.getSchema();
        if (schema.getName().getLocalPart().equals(typeName)) {
            return source;
        } else {
            throw new IOException("Schema '" + typeName + "' does not exist.");
        }
    }

    @Override
    public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
        SimpleFeatureType schema = source.getSchema();
        if (schema.getName().equals(typeName)) {
            return source;
        } else {
            throw new IOException("Schema '" + typeName + "' does not exist.");
        }
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query,
            Transaction transaction) throws IOException {
        throw new UnsupportedOperationException(
                "This store is wrapping a FeatureSource/FeatureStore, which handles "
                        + "transactions in a stateful way as opposed to a per call way. "
                        + "You should get the feature source and use that one instead");
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
            Filter filter, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException(
                "This store is wrapping a FeatureSource/FeatureStore, which handles "
                        + "transactions in a stateful way as opposed to a per call way. "
                        + "You should get the feature source and use that one instead");
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
            Transaction transaction) throws IOException {
        throw new UnsupportedOperationException(
                "This store is wrapping a FeatureSource/FeatureStore, which handles "
                        + "transactions in a stateful way as opposed to a per call way. "
                        + "You should get the feature source and use that one instead");
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName,
            Transaction transaction) throws IOException {
        throw new UnsupportedOperationException(
                "This store is wrapping a FeatureSource/FeatureStore, which handles "
                        + "transactions in a stateful way as opposed to a per call way. "
                        + "You should get the feature source and use that one instead");
    }

    @Override
    public LockingManager getLockingManager() {
        return null;
    }

}
