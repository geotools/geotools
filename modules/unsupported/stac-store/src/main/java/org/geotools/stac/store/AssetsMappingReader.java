/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.stac.store;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import org.geotools.data.geojson.GeoJSONReader;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;

/**
 * Maps the "assets" extra property into a legit attribute (assumes the {@link
 * STACFeatureSource#ASSETS} property is in the target type
 */
public class AssetsMappingReader implements SimpleFeatureReader {

    private final SimpleFeatureReader delegate;
    private final SimpleFeatureBuilder builder;

    public AssetsMappingReader(SimpleFeatureType targetType, SimpleFeatureReader delegate) {
        this.delegate = delegate;
        this.builder = new SimpleFeatureBuilder(targetType);
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return builder.getFeatureType();
    }

    @Override
    public boolean hasNext() throws IOException {
        return delegate.hasNext();
    }

    @Override
    @SuppressWarnings("unchecked")
    public SimpleFeature next()
            throws IOException, IllegalAttributeException, NoSuchElementException {
        SimpleFeature f = delegate.next();
        Object atts = f.getUserData().get(GeoJSONReader.TOP_LEVEL_ATTRIBUTES);
        if (atts instanceof Map) {
            Map<String, Object> attributes = (Map<String, Object>) atts;
            Object assets = attributes.get(STACFeatureSource.ASSETS);
            builder.init(f);
            builder.set(STACFeatureSource.ASSETS, assets);
            return builder.buildFeature(f.getID());
        }

        return f;
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}
