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

import java.util.NoSuchElementException;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * @author Christian Muüller
 * 
 * Implementation of {@link FeatureIterator} for {@link PreGeneralizedSimpleFeature}
 *
 *
 * @source $URL$
 */
public class PreGeneralizedFeatureIterator implements SimpleFeatureIterator {

    protected SimpleFeatureIterator backendIterator;

    protected SimpleFeatureType featureTyp;

    protected String geomPropertyName, backendGeomPropertyName;

    protected int[] indexMapping;

    public PreGeneralizedFeatureIterator(SimpleFeatureIterator backendIterator,
            SimpleFeatureType featureTyp, int indexMapping[], String geomPropertyName,
            String backendGeomPropertyName) {
        super();
        this.backendIterator = backendIterator;
        this.featureTyp = featureTyp;
        this.geomPropertyName = geomPropertyName;
        this.backendGeomPropertyName = backendGeomPropertyName;
        this.indexMapping = indexMapping;
    }

    public void close() {
        backendIterator.close();

    }

    public boolean hasNext() {
        return backendIterator.hasNext();
    }

    public SimpleFeature next() throws NoSuchElementException {
        SimpleFeature f = backendIterator.next();
        if (f == null)
            return null;
        return new PreGeneralizedSimpleFeature(featureTyp, indexMapping, f, geomPropertyName,
                backendGeomPropertyName);
    }

}
