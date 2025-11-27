/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geojson;

import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.collection.BaseSimpleFeatureCollection;
import org.geotools.util.logging.Logging;
import tools.jackson.databind.JsonNode;

/** A GeoJSON specific feature collection that can follow "next" links in order to retrieve all data */
public class PagingFeatureCollection extends BaseSimpleFeatureCollection {

    protected static final Logger LOGGER = Logging.getLogger(PagingFeatureCollection.class);

    final Integer matched;
    private final SimpleFeatureCollection first;
    private final JsonNode next;

    public PagingFeatureCollection(SimpleFeatureCollection first, JsonNode next, Integer matched) {
        super(first.getSchema());
        this.first = first;
        this.next = next;
        this.matched = matched;
    }

    /** Matched header from the GeoJSON feature collection, if found, or null otherwise. */
    public Integer getMatched() {
        return matched;
    }

    @Override
    public int size() {
        return matched != null ? matched : super.size();
    }

    @Override
    public SimpleFeatureIterator features() {
        return new PagingFeatureIterator(first.features(), next);
    }

    protected SimpleFeatureCollection getFirstCollection() {
        return first;
    }

    protected JsonNode getNext() {
        return next;
    }
    /**
     * Reads the next feature collection, or return null if there is none. Subclasses can override if they need a
     * different logic for fetching the next page.
     */
    protected SimpleFeatureCollection readNext(JsonNode next) throws IOException {
        if (next == null) return null;
        JsonNode href = next.get("href");
        if (href == null) return null;

        LOGGER.fine(() -> "Fetching next page of data at " + href.asString());
        try (GeoJSONReader reader = new GeoJSONReader(new URL(href.asString()))) {
            return reader.getFeatures();
        }
    }

    protected class PagingFeatureIterator implements SimpleFeatureIterator {
        private SimpleFeatureIterator delegate;
        private JsonNode nextPage;

        public PagingFeatureIterator(SimpleFeatureIterator delegate, JsonNode next) {
            this.delegate = delegate;
            this.nextPage = next;
        }

        @Override
        public boolean hasNext() {
            // jump to the next link, if possible, until a non-empty collection is found,
            // or no next links are found
            while (!delegate.hasNext() && nextPage != null) {
                try {
                    // call the reader again, but do not delegate to avoid creating a series of
                    // nested objects (the next collection might contain another and so on)
                    SimpleFeatureCollection features = readNext(nextPage);
                    if (features instanceof PagingFeatureCollection collection) {
                        this.nextPage = collection.getNext();
                        this.delegate = collection.getFirstCollection().features();
                    } else {
                        this.nextPage = null;
                        this.delegate = features.features();
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Switching to the next page failed", e);
                }
            }

            return delegate.hasNext();
        }

        @Override
        public SimpleFeature next() {
            if (!hasNext()) throw new NoSuchElementException();
            return delegate.next();
        }

        @Override
        public void close() {
            delegate.close();
        }
    }
}
