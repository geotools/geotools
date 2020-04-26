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
package org.geotools.gce.imagemosaic.catalog;

import static org.geotools.geometry.jts.ReferencedEnvelope.reference;

import java.util.NoSuchElementException;
import java.util.function.Function;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.collection.DecoratingSimpleFeatureIterator;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Feature collection decorating features with their native bounds, used wehen the {@link
 * GranuleSource#NATIVE_BOUNDS} hint is provided in the queries
 */
class BoundsFeatureCollection extends DecoratingSimpleFeatureCollection {

    private final Function<SimpleFeature, GranuleDescriptor> granuleMapper;

    protected BoundsFeatureCollection(
            SimpleFeatureCollection delegate,
            Function<SimpleFeature, GranuleDescriptor> granuleMapper) {
        super(delegate);
        this.granuleMapper = granuleMapper;
    }

    @Override
    public SimpleFeatureIterator features() {
        return new BoundsFeatureIterator(delegate.features());
    }

    class BoundsFeatureIterator extends DecoratingSimpleFeatureIterator {

        public BoundsFeatureIterator(SimpleFeatureIterator iterator) {
            super(iterator);
        }

        @Override
        public SimpleFeature next() throws NoSuchElementException {
            SimpleFeature sf = super.next();
            GranuleDescriptor descriptor = granuleMapper.apply(sf);
            ReferencedEnvelope re = reference(descriptor.getGranuleEnvelope());
            sf.getUserData().put(GranuleSource.NATIVE_BOUNDS_KEY, re);
            return sf;
        }
    }
}
