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
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.collection.BaseSimpleFeatureCollection;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A GeoJSON specific feature collection that can follow "next" links in order to retrieve all data
 */
public class PagingFeatureCollection extends BaseSimpleFeatureCollection {

    static final Logger LOGGER = Logging.getLogger(PagingFeatureCollection.class);

    final Integer matched;
    SimpleFeatureCollection first;
    URL next;

    public PagingFeatureCollection(SimpleFeatureCollection first, URL next, Integer matched) {
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
        if (matched != null) return matched;
        return super.size();
    }

    @Override
    public SimpleFeatureIterator features() {
        return new PagingFeatureIterator(first.features(), next);
    }

    protected class PagingFeatureIterator implements SimpleFeatureIterator {
        private SimpleFeatureIterator delegate;
        private URL next;

        public PagingFeatureIterator(SimpleFeatureIterator delegate, URL next) {
            this.delegate = delegate;
            this.next = next;
        }

        @Override
        public boolean hasNext() {
            // jump to the next link, if possible, until a non-empty collection is found,
            // or no next links are found
            while (!delegate.hasNext() && next != null) {
                try {
                    // call the reader again, but do not delegate to avoid creating a series of
                    // nested objects (the next collection might contain another and so on)
                    LOGGER.fine(() -> "Fetching next page of data at " + next);
                    SimpleFeatureCollection features = new GeoJSONReader(next).getFeatures();
                    this.delegate = features.features();
                    if (features instanceof PagingFeatureCollection) {
                        this.next = ((PagingFeatureCollection) features).next;
                    } else {
                        this.next = null;
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
