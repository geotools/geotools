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
package org.geotools.mbtiles;

import java.io.IOException;
import java.util.NoSuchElementException;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;

/**
 * The class takes care to return features that intersects the geometry passed with a
 * Hints.GEOMETRY_CLIP, in order to avoid that features' buffer in mbtiles data gets deployed when a
 * rendering transformation is issued.
 */
class ClippingFeatureReader implements SimpleFeatureReader {

    private SimpleFeatureReader delegate;

    private SimpleFeature next;

    public ClippingFeatureReader(SimpleFeatureReader reader) {
        this.delegate = reader;
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return delegate.getFeatureType();
    }

    @Override
    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        if (next == null && !this.hasNext()) {
            throw new NoSuchElementException();
        }
        SimpleFeature f = next;
        next = null;
        return f;
    }

    @Override
    public boolean hasNext() throws IOException {
        if (next != null) {
            return true;
        }
        while (delegate.hasNext()) {
            SimpleFeature peek = delegate.next();
            GeometryDescriptor descriptor = peek.getFeatureType().getGeometryDescriptor();
            if (peek.hasUserData()) {
                Polygon clip = (Polygon) peek.getUserData().get(Hints.GEOMETRY_CLIP);
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
    public void close() throws IOException {
        delegate.close();
    }
}
