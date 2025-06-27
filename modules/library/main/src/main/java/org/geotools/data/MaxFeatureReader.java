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
import java.util.NoSuchElementException;
import org.geotools.api.data.DelegatingFeatureReader;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.type.FeatureType;

/**
 * Basic support for a FeatureReader<SimpleFeatureType, SimpleFeature> that limits itself to the number of features
 * passed in.
 *
 * @author Chris Holmes
 * @version $Id$
 */
public class MaxFeatureReader<T extends FeatureType, F extends Feature> implements DelegatingFeatureReader<T, F> {

    protected final FeatureReader<T, F> featureReader;
    protected final int maxFeatures;
    protected int counter = 0;

    /**
     * Creates a new instance of MaxFeatureReader
     *
     * @param featureReader FeatureReader being maxed
     */
    public MaxFeatureReader(FeatureReader<T, F> featureReader, int maxFeatures) {
        this.featureReader = featureReader;
        this.maxFeatures = maxFeatures;
    }

    @Override
    public FeatureReader<T, F> getDelegate() {
        return featureReader;
    }

    @Override
    public F next() throws IOException, IllegalAttributeException, NoSuchElementException {
        if (hasNext()) {
            counter++;

            return featureReader.next();
        } else {
            throw new NoSuchElementException("No such Feature exists");
        }
    }

    @Override
    public void close() throws IOException {
        featureReader.close();
    }

    @Override
    public T getFeatureType() {
        return featureReader.getFeatureType();
    }

    /**
     * @return <code>true</code> if the featureReader has not passed the max and still has more features.
     * @throws IOException If the reader we are filtering encounters a problem
     */
    @Override
    public boolean hasNext() throws IOException {
        return featureReader.hasNext() && counter < maxFeatures;
    }
}
