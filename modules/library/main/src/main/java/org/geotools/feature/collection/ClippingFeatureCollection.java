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

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;

/**
 * The class decorates a SimpleFeatureCollection with one that returns features that intersect the
 * geometry passed with a Hints.GEOMETRY_CLIP
 */
public class ClippingFeatureCollection extends DecoratingSimpleFeatureCollection {

    public ClippingFeatureCollection(SimpleFeatureCollection delegate) {
        super(delegate);
    }

    @Override
    public SimpleFeatureIterator features() {
        return new ClippingFeatureIterator(delegate.features());
    }

    @Override
    public int size() {
        int count = 0;
        SimpleFeatureIterator i = features();
        try {
            while (i.hasNext()) {
                count++;
                i.next();
            }

            return count;
        } finally {
            i.close();
        }
    }
}
