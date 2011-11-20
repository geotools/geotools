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
package org.geotools.data.shapefile.ng;

import java.io.IOException;

import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ng.files.ShpFiles;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.data.store.ContentState;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

/**
 * FeatureStore for the OGR store, based on the {@link ContentFeatureStore} framework
 * 
 * @author Andrea Aime - GeoSolutions
 */
@SuppressWarnings("rawtypes")
class ShapefileFeatureStore extends ContentFeatureStore {

    ShapefileFeatureSource delegate;

    public ShapefileFeatureStore(ContentEntry entry, ShpFiles files) {
        super(entry, Query.ALL);
        this.delegate = new ShapefileFeatureSource(entry, files);
    }

    @Override
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> getWriterInternal(Query query,
            int flags) throws IOException {
        if (flags == 0) {
            throw new IllegalArgumentException("no write flags set");
        }

        ShapefileFeatureReader reader = (ShapefileFeatureReader) delegate.getReaderInternal(query);
        ShapefileFeatureWriter writer = new ShapefileFeatureWriter(delegate.shpFiles, reader,
                getDataStore().getCharset(), getDataStore().getTimeZone());

        // if we only have to add move to the end.
        // TODO: just make the code transfer the bytes in bulk instead and start actual writing at
        // the end
        if ((flags | WRITER_ADD) == WRITER_ADD) {
            while (writer.hasNext()) {
                writer.next();
            }
        }

        return writer;
    }

    // ----------------------------------------------------------------------------------------
    // METHODS DELEGATED TO OGRFeatureSource
    // ----------------------------------------------------------------------------------------

    public ShapefileDataStore getDataStore() {
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
    protected boolean canRetype() {
        return delegate.canRetype();
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return delegate.handleVisitor(query, visitor);
    }

}
