/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 *  FeatureReader<SimpleFeatureType, SimpleFeature> that reads features from a java.util.collection of features,
 * an array of features or a FeatureCollection.
 *
 * @author jones
 *
 * @source $URL$
 */
public class CollectionFeatureReader implements  FeatureReader<SimpleFeatureType, SimpleFeature> {
    private SimpleFeatureCollection collection;
    private Iterator features;
    private SimpleFeatureType type;
    private boolean closed = false;

    /**
     * Create a new instance.
     *
     * @param featuresArg a colleciton of features.  <b>All features must be of the same FeatureType</b> 
     * @param typeArg the Feature type of of the features.
     */
    public CollectionFeatureReader(Collection featuresArg, SimpleFeatureType typeArg) {
        assert !featuresArg.isEmpty();

        if (featuresArg instanceof FeatureCollection) {
            collection = (SimpleFeatureCollection) featuresArg;
        }

        this.features = featuresArg.iterator();
        this.type = typeArg;
    }

    /**
     * Create a new instance.
     *
     * @param featuresArg a FeatureCollection.  <b>All features must be of the same FeatureType</b> 
     * @param typeArg the Feature type of of the features.
     */
    public CollectionFeatureReader(SimpleFeatureCollection featuresArg,
        SimpleFeatureType typeArg) {
        assert !featuresArg.isEmpty();
        collection = featuresArg;
        this.features = featuresArg.iterator();
        this.type = typeArg;
    }

    /**
     * Create a new instance.
     *
     * @param featuresArg an of features.  <b>All features must be of the same FeatureType</b> 
     */
    public CollectionFeatureReader(SimpleFeature[] featuresArg) {
        assert featuresArg.length > 0;
        this.features = Arrays.asList(featuresArg).iterator();
        type = featuresArg[0].getFeatureType();
    }

    /**
     * @see org.geotools.data.FeatureReader#getFeatureType()
     */
    public SimpleFeatureType getFeatureType() {
        return type;
    }

    /**
     * @see org.geotools.data.FeatureReader#next()
     */
    public SimpleFeature next()
        throws IOException, IllegalAttributeException, NoSuchElementException {
        if (closed) {
            throw new NoSuchElementException("Reader has been closed");
        }

        return (SimpleFeature) features.next();
    }

    /**
     * @see org.geotools.data.FeatureReader#hasNext()
     */
    public boolean hasNext() throws IOException {
        return features.hasNext() && !closed;
    }

    /**
     * @see org.geotools.data.FeatureReader#close()
     */
    public void close() throws IOException {
        closed = true;

        if (collection != null) {
            collection.close(features);
        }
    }
}
