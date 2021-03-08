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

package org.geotools.data.gen;

import java.io.IOException;
import java.util.NoSuchElementException;
import org.geotools.data.FeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * @author Christian Mueller
 *     <p>Implementation of {@link FeatureReader} for {@link PreGeneralizedSimpleFeature}
 */
public class PreGeneralizedFeatureReader
        implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    protected SimpleFeatureType featureTyp;

    protected SimpleFeatureType returnedFeatureType;

    protected FeatureReader<SimpleFeatureType, SimpleFeature> backendReader;

    protected int indexMapping[];

    protected String geomPropertyName, backendGeomPropertyName;

    public PreGeneralizedFeatureReader(
            SimpleFeatureType featureTyp,
            SimpleFeatureType returnedFeatureType,
            int indexMapping[],
            FeatureReader<SimpleFeatureType, SimpleFeature> backendReader,
            String geomPropertyName,
            String backendGeomPropertyName) {
        super();
        this.featureTyp = featureTyp;
        this.returnedFeatureType = returnedFeatureType;
        this.backendReader = backendReader;
        this.geomPropertyName = geomPropertyName;
        this.backendGeomPropertyName = backendGeomPropertyName;
        this.indexMapping = indexMapping;
    }

    @Override
    public void close() throws IOException {
        backendReader.close();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return returnedFeatureType;
    }

    @Override
    public boolean hasNext() throws IOException {
        return backendReader.hasNext();
    }

    @Override
    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        SimpleFeature next = backendReader.next();
        if (next == null) return null;
        return new PreGeneralizedSimpleFeature(
                featureTyp,
                returnedFeatureType,
                indexMapping,
                next,
                geomPropertyName,
                backendGeomPropertyName);
    }
}
