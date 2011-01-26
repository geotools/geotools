/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureReader;
import org.geotools.data.postgis.fidmapper.VersionedFIDMapper;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A feature reader for versioned features. It assumes the internal reader has
 * already been configured not to return versioning columns, so simply handles
 * FID mutation.
 * 
 * @author aaime
 * @since 2.4
 * 
 */
class VersionedFeatureReader implements  FeatureReader<SimpleFeatureType, SimpleFeature> {

    private  FeatureReader<SimpleFeatureType, SimpleFeature> wrapped;

    private VersionedFIDMapper fidMapper;

    public VersionedFeatureReader(FeatureReader <SimpleFeatureType, SimpleFeature> wrapped, VersionedFIDMapper fidMapper) {
        this.wrapped = wrapped;
        this.fidMapper = fidMapper;
    }

    public void close() throws IOException {
        wrapped.close();
    }

    public SimpleFeatureType getFeatureType() {
        return wrapped.getFeatureType();
    }

    public boolean hasNext() throws IOException {
        return wrapped.hasNext();
    }

    public SimpleFeature next() throws IOException, IllegalAttributeException, NoSuchElementException {
        SimpleFeature feature = wrapped.next();
        SimpleFeatureType featureType = wrapped.getFeatureType();
        String id = feature.getID();

        return SimpleFeatureBuilder.build(featureType, feature
                .getAttributes(), fidMapper
                .getUnversionedFid(id));
    }
}
