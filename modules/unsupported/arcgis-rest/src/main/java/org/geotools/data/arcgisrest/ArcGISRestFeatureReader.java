/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.data.arcgisrest;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import org.geotools.data.FeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Feature reader of the GeoJSON features
 *
 * @author lmorandini
 */
public class ArcGISRestFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    protected SimpleFeatureType featureType;
    protected GeoJSONParser parser;
    protected Logger LOGGER;

    protected int featIndex = 0;

    public ArcGISRestFeatureReader(
            SimpleFeatureType featureTypeIn, InputStream iStream, Logger logger)
            throws IOException {
        this.featureType = featureTypeIn;
        this.featIndex = 0;
        this.LOGGER = logger;

        this.parser = new GeoJSONParser(iStream, featureTypeIn, logger);
        this.parser.parseFeatureCollection();
    }

    /** @see FeatureReader#getFeatureType() */
    @Override
    public SimpleFeatureType getFeatureType() {
        if (this.featureType == null) {
            throw new IllegalStateException(
                    "No features were retrieved, shouldn't be calling getFeatureType()");
        }
        return this.featureType;
    }

    /** @see FeatureReader#hasNext() */
    @Override
    public boolean hasNext() {
        return this.parser.hasNext();
    }

    /**
     * @see FeatureReader#next()
     */
    @Override
    public SimpleFeature next() throws NoSuchElementException, IOException {
        return this.parser.next();
    }

    @Override
    public void close() {
        this.parser.close();
    }
}
