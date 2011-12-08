/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import java.io.IOException;

import org.bridj.Pointer;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.data.store.ContentState;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * FeatureStore for the OGR store, based on the {@link ContentFeatureStore} framework
 * 
 * @author Andrea Aime - GeoSolutions
 */
@SuppressWarnings("rawtypes")
class OGRFeatureStore extends ContentFeatureStore {

    OGRFeatureSource delegate;

    public OGRFeatureStore(ContentEntry entry, Query query) {
        super(entry, query);
        delegate = new OGRFeatureSource(entry, query);
    }

    @Override
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> getWriterInternal(Query query,
            int flags) throws IOException {
        Pointer dataSource = null;
        Pointer layer = null;
        boolean cleanup = true;
        try {
            // grab the layer
            String typeName = getEntry().getTypeName();
            dataSource = getDataStore().openOGRDataSource(true);
            layer = getDataStore().openOGRLayer(dataSource, typeName);

            FeatureReader<SimpleFeatureType, SimpleFeature> reader = delegate.getReaderInternal(
                    dataSource, layer, query);
            GeometryFactory gf = delegate.getGeometryFactory(query);
            OGRDirectFeatureWriter result = new OGRDirectFeatureWriter(dataSource, layer, reader,
                    getSchema(), gf);
            cleanup = false;
            return result;
        } finally {
            if (cleanup) {
                OGRUtils.releaseLayer(layer);
                OGRUtils.releaseDataSource(dataSource);
            }
        }
    }

    // ----------------------------------------------------------------------------------------
    // METHODS DELEGATED TO OGRFeatureSource
    // ----------------------------------------------------------------------------------------

    public OGRDataStore getDataStore() {
        return delegate.getDataStore();
    }

    public Transaction getTransaction() {
        return delegate.getTransaction();
    }

    public ResourceInfo getInfo() {
        return delegate.getInfo();
    }

    public QueryCapabilities getQueryCapabilities() {
        return delegate.getQueryCapabilities();
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        return delegate.getBoundsInternal(query);
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        return delegate.getCountInternal(query);
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        return delegate.getReaderInternal(query);
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        return delegate.buildFeatureType();
    }

    @Override
    public ContentEntry getEntry() {
        return delegate.getEntry();
    }

    @Override
    public Name getName() {
        return delegate.getName();
    }

    @Override
    public ContentState getState() {
        return delegate.getState();
    }

    @Override
    public void setTransaction(Transaction transaction) {
        super.setTransaction(transaction);

        if (delegate.getTransaction() != transaction) {
            delegate.setTransaction(transaction);
        }
    }

    @Override
    protected boolean canFilter() {
        return delegate.canFilter();
    }

    @Override
    protected boolean canSort() {
        return delegate.canSort();
    }

    @Override
    protected boolean canRetype() {
        return delegate.canRetype();
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return delegate.handleVisitor(query, visitor);
    }

}
