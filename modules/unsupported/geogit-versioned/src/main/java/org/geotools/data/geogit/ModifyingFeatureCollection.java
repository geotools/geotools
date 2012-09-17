/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geogit;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.DecoratingFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;

/**
 * FeatureCollection decorator whose iterators apply feature modifications on
 * the fly
 * 
 * @author groldan
 */
class ModifyingFeatureCollection extends
        DecoratingFeatureCollection<SimpleFeatureType, SimpleFeature> {

    private final Name[] attributeNames;

    private final Object[] attributeValues;

    private final Map<Iterator<SimpleFeature>, Iterator<SimpleFeature>> openIterators = new ConcurrentHashMap<Iterator<SimpleFeature>, Iterator<SimpleFeature>>();

    protected ModifyingFeatureCollection(
            final SimpleFeatureCollection delegate,
            final Name[] attributeNames, final Object[] attributeValues) {
        super(delegate);
        this.attributeNames = Arrays.copyOf(attributeNames, attributeNames.length);
        this.attributeValues = Arrays.copyOf(attributeValues, attributeValues.length);
    }

    @Override
    public SimpleFeatureIterator features() {

        final FeatureIterator<SimpleFeature> original = delegate.features();
        final ModifyFunction modifier = new ModifyFunction();

        SimpleFeatureIterator modifying = new SimpleFeatureIterator() {

            @Override
            public SimpleFeature next() throws NoSuchElementException {
                return modifier.apply(original.next());
            }

            @Override
            public boolean hasNext() {
                return original.hasNext();
            }

            @Override
            public void close() {
                original.close();
            }
        };
        return modifying;
    }

    private class ModifyFunction implements
            Function<SimpleFeature, SimpleFeature> {

        @Override
        public SimpleFeature apply(final SimpleFeature input) {
            Name attributeName;
            Object attributeValue;
            for (int i = 0; i < attributeNames.length; i++) {
                attributeName = attributeNames[i];
                attributeValue = attributeValues[i];
                input.setAttribute(attributeName, attributeValue);
            }
            return input;
        }
    }
}