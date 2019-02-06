/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf.tools;

import java.util.NoSuchElementException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.collection.DecoratingSimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/** Wraps a delegate collection and adds the "location" attribute */
class LocationFeatureCollection extends DecoratingSimpleFeatureCollection {

    private final String location;
    private final SimpleFeatureType schema;

    public LocationFeatureCollection(
            SimpleFeatureCollection delegate, String location, SimpleFeatureType schema) {
        super(delegate);
        this.location = location;
        this.schema = schema;
    }

    @Override
    public SimpleFeatureType getSchema() {
        return schema;
    }

    @Override
    public SimpleFeatureIterator features() {
        return new LocationFeatureIterator(super.features());
    }

    /** Iterates over the features and adds the location attribute */
    private class LocationFeatureIterator extends DecoratingSimpleFeatureIterator {
        private final SimpleFeatureIterator delegate;
        private final SimpleFeatureBuilder fb;

        public LocationFeatureIterator(SimpleFeatureIterator delegate) {
            super(delegate);
            this.delegate = delegate;
            this.fb = new SimpleFeatureBuilder(getSchema());
        }

        @Override
        public SimpleFeature next() throws NoSuchElementException {
            final SimpleFeature f = delegate.next();
            fb.init(f);
            fb.set("location", location);
            return fb.buildFeature(f.getID());
        }
    }
}
