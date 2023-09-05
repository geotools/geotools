/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.DataFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.index.strtree.STRtree;

/**
 * IndexedFeatureReader
 *
 * @author wolf
 */
public final class IndexedFeatureResults extends DataFeatureCollection {
    STRtree index = new STRtree();
    Envelope bounds;
    int count;
    private Envelope queryBounds;

    public IndexedFeatureResults(SimpleFeatureCollection results)
            throws IOException, IllegalAttributeException {
        // copy results attributes
        super(null, results.getSchema());

        // load features into the index
        bounds = new Envelope();
        count = 0;
        try (SimpleFeatureIterator reader = results.features()) {
            SimpleFeature f;
            Envelope env;
            while (reader.hasNext()) {
                f = reader.next();
                env = ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal();
                bounds.expandToInclude(env);
                count++;
                index.insert(env, f);
            }
        }
    }

    /** @see org.geotools.data.FeatureResults#reader() */
    public FeatureReader<SimpleFeatureType, SimpleFeature> reader(Envelope envelope)
            throws IOException {
        List results = index.query(envelope);
        final Iterator resultsIterator = results.iterator();

        return new FeatureReader<SimpleFeatureType, SimpleFeature>() {
            /** @see FeatureReader#getFeatureType() */
            @Override
            public SimpleFeatureType getFeatureType() {
                return getSchema();
            }

            /** @see FeatureReader#next() */
            @Override
            public SimpleFeature next()
                    throws IOException, IllegalAttributeException, NoSuchElementException {
                return (SimpleFeature) resultsIterator.next();
            }

            /** @see FeatureReader#hasNext() */
            @Override
            public boolean hasNext() throws IOException {
                return resultsIterator.hasNext();
            }

            /** @see FeatureReader#close() */
            @Override
            public void close() throws IOException {}
        };
    }

    /** @see org.geotools.data.FeatureResults#getBounds() */
    @Override
    public ReferencedEnvelope getBounds() {
        return ReferencedEnvelope.reference(bounds);
    }

    /** @see org.geotools.data.FeatureResults#getCount() */
    @Override
    public int getCount() throws IOException {
        return count;
    }

    /** @see org.geotools.data.FeatureResults#collection() */
    public SimpleFeatureCollection collection() throws IOException {
        DefaultFeatureCollection fc = new DefaultFeatureCollection();
        @SuppressWarnings("unchecked")
        List<SimpleFeature> results = index.query(bounds);
        for (SimpleFeature result : results) {
            fc.add(result);
        }
        return fc;
    }

    /** @see org.geotools.data.FeatureResults#reader() */
    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> reader() throws IOException {
        if (queryBounds != null) return reader(queryBounds);
        else return reader(bounds);
    }

    /** @param queryBounds an Envelope defining the boundary of the query */
    public void setQueryBounds(Envelope queryBounds) {
        this.queryBounds = queryBounds;
    }
}
