/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.collection;

import java.util.NoSuchElementException;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureIterator;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.GeometryDescriptor;

/**
 * Decorates a SimpleFeatureIterator with one that return features if intersect the Geometry passed
 * with the Hint.GEOMETRY_CLIP
 */
public class ClippingFeatureIterator extends DecoratingSimpleFeatureIterator {

    private FeatureIterator<SimpleFeature> delegate;

    private SimpleFeature next;

    /**
     * Wrap the provided FeatureIterator.
     *
     * @param iterator Iterator to be used as a delegate.
     */
    public ClippingFeatureIterator(SimpleFeatureIterator iterator) {
        super(iterator);
        this.delegate = iterator;
    }

    @Override
    public SimpleFeature next() throws NoSuchElementException {
        if (next == null && !this.hasNext()) {
            throw new NoSuchElementException();
        }
        SimpleFeature f = next;
        next = null;
        return f;
    }

    @Override
    public boolean hasNext() {
        if (next != null) {
            return true;
        }
        while (delegate.hasNext()) {
            SimpleFeature peek = delegate.next();
            GeometryDescriptor descriptor = peek.getFeatureType().getGeometryDescriptor();
            if (peek.hasUserData()) {
                Geometry clip = (Geometry) peek.getUserData().get(Hints.GEOMETRY_CLIP);
                Geometry geom = (Geometry) peek.getAttribute(descriptor.getName());
                if (clip.intersects(geom)) {
                    next = peek;
                    break;
                }
            } else {
                next = peek;
                break;
            }
        }
        return next != null;
    }

    @Override
    public void close() {
        delegate.close();
        delegate = null;
        next = null;
    }
}
