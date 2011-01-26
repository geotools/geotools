/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.georest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.NoSuchElementException;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.data.store.ContentState;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.GeoJSONUtil;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.sort.SortOrder;

/**
 * <p>
 * FeatureReader for the {@link GeoRestDataStore}.
 * </p>
 * 
 * @author Pieter De Graef, Geosparc
 */
public class GeoRestFeatureReader implements SimpleFeatureReader {

    private ContentState contentState;

    private FeatureJSON json = new FeatureJSON();

    private FeatureIterator<SimpleFeature> iterator;

    private boolean firstChar = true;

    /**
     * Create a new reader given a contentState and a query. This constructor will immediately open
     * a connection to the rest service and try to read from it.
     * 
     * @param contentState
     *            The ContentState associated with this reader.
     * @param query
     *            A query that determines which features to read.
     * @throws IOException
     *             oops.
     */
    public GeoRestFeatureReader(ContentState contentState, Query query) throws IOException {
        this.contentState = contentState;

        // Open the connection....
        GeoRestDataStore ds = (GeoRestDataStore) contentState.getEntry().getDataStore();
        GeoRestFeatureSource source = (GeoRestFeatureSource) ds.getFeatureSource(contentState
                .getEntry().getName());
        InputStream in = getInputStream(source.getUrl(), query);
        SimpleFeatureCollection col = (SimpleFeatureCollection) json
                .readFeatureCollection(GeoJSONUtil.toReader(in));
        iterator = col.features();
    }

    public SimpleFeatureType getFeatureType() {
        return contentState.getFeatureType();
    }

    public SimpleFeature next() throws IOException, IllegalArgumentException,
            NoSuchElementException {
        return (SimpleFeature) iterator.next();
    }

    public boolean hasNext() throws IOException {
        return iterator.hasNext();
    }

    public void close() throws IOException {
        iterator.close();
    }

    // ------------------------------------------------------------------------
    // Private methods:
    // ------------------------------------------------------------------------

    private InputStream getInputStream(URL url, Query query) throws IOException {
        // Transform query into URL parameters:
        firstChar = true;
        StringBuilder builder = new StringBuilder(url.toString());

        // Feature number limitation:
        if (!query.isMaxFeaturesUnlimited()) {
            builder.append(getGlueChar());
            builder.append("limit=");
            builder.append(query.getMaxFeatures());
        }

        // Should we retrieve all properties?
        if (!query.retrieveAllProperties() && query.getPropertyNames().length > 0) {
            builder.append(getGlueChar());
            builder.append("attrs=");
            for (int i = 0; i < query.getPropertyNames().length; i++) {
                builder.append(query.getPropertyNames()[i]);
                if (i > 0 && i < query.getPropertyNames().length - 1) {
                    builder.append(",");
                }
            }
        }

        // Perhaps use an offset to start counting from:
        if (query.getStartIndex() != null) {
            builder.append(getGlueChar());
            builder.append("offset=");
            builder.append(query.getStartIndex());
        }

        // Is there a certain order required?
        if (query.getSortBy() != null && query.getSortBy().length > 0) {
            builder.append(getGlueChar());
            builder.append("order_by=");
            builder.append(query.getSortBy()[0].getPropertyName().getPropertyName());
            builder.append(getGlueChar());
            builder.append("dir=");
            SortOrder order = query.getSortBy()[0].getSortOrder();
            builder.append(order.name());
        }

        // Is there a filter present?
        if (query.getFilter() != null) {
            GeoRestFilterVisitor visitor = new GeoRestFilterVisitor(false);
            query.getFilter().accept(visitor, null);
            visitor.finish(builder, !firstChar);
        }

        URL targetUrl = new URL(builder.toString());
        System.out.println(targetUrl);
        return targetUrl.openStream();
    }

    private String getGlueChar() {
        if (firstChar) {
            firstChar = false;
            return "?";
        }
        return "&";
    }
}
