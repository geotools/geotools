/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geojson.store;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStore;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.geojson.GeoJSONReader;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.URLs;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class GeoJSONDataStore extends ContentDataStore implements FileDataStore {

    private SimpleFeatureType schema;
    private URL url;
    private CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
    private NameImpl typeName;
    private boolean writeBounds;

    private ReferencedEnvelope bbox;
    private boolean quick = true;

    public GeoJSONDataStore(URL url) {
        this.setUrl(url);
    }

    public GeoJSONDataStore(File f) {
        this.setUrl(URLs.fileToUrl(f));
    }

    GeoJSONReader read() throws IOException {
        GeoJSONReader reader = new GeoJSONReader(getUrl());

        return reader;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        if ("file".equalsIgnoreCase(getUrl().getProtocol())) {
            File f = URLs.urlToFile(getUrl());
            if (!f.exists()) {
                if (!f.createNewFile()) {
                    return new GeoJSONFeatureSource(entry, Query.ALL);
                }
            }
            if (f.canWrite()) {
                GeoJSONFeatureStore store = new GeoJSONFeatureStore(entry, Query.ALL);
                store.setWriteBounds(writeBounds);
                return store;
            }
        }

        return new GeoJSONFeatureSource(entry, Query.ALL);
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        if (schema != null) {
            return Collections.singletonList(new NameImpl(schema.getTypeName()));
        }
        String name = new File(getUrl().getFile()).getName();
        int index = name.lastIndexOf('.');
        if (index > 0) {
            name = name.substring(0, index);
        } else {
            name = "Unamed URL";
        }
        typeName = new NameImpl(name);
        return Collections.singletonList(typeName);
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        schema = featureType;
    }

    @Override
    public SimpleFeatureType getSchema() throws IOException {
        if (schema == null) {
            schema = getSchema(typeName);
        }
        return schema;
    }

    @Override
    public void updateSchema(SimpleFeatureType featureType) throws IOException {
        schema = featureType;
    }

    /** @return the crs */
    public CoordinateReferenceSystem getCrs() {
        return crs;
    }

    /** @param crs the crs to set */
    public void setCrs(CoordinateReferenceSystem crs) {
        this.crs = crs;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public SimpleFeatureSource getFeatureSource() throws IOException {
        if (typeName == null) {
            createTypeNames();
        }
        return super.getFeatureSource(typeName);
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader() throws IOException {

        return new GeoJSONFeatureSource(this).getReader();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            Filter filter, Transaction transaction) throws IOException {
        return null;
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(Transaction transaction)
            throws IOException {
        return null;
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
            Transaction transaction) throws IOException {
        return null;
    }

    public Name getTypeName() {
        if (namespaceURI != null) {
            return new NameImpl(namespaceURI, typeName.getLocalPart());
        } else {
            return typeName;
        }
    }

    /** @param booleanValue */
    public void setWriteBounds(boolean booleanValue) {
        writeBounds = booleanValue;
    }

    /** @return the writeBounds */
    public boolean isWriteBounds() {
        return writeBounds;
    }

    /** @param booleanValue */
    public void setQuickSchema(boolean booleanValue) {
        quick = booleanValue;
    }

    /** @return the quick */
    public boolean isQuick() {
        return quick;
    }

    public ReferencedEnvelope getBbox() {
        return bbox;
    }

    public void setBbox(ReferencedEnvelope bbox) {
        this.bbox = bbox;
    }
}
