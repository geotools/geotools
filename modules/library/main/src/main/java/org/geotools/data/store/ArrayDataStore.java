/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.store;

import java.io.IOException;

import org.geotools.data.AbstractDataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.feature.FeatureTypes;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Simple example of an AbstractDataStore built around the provided array of features.
 * <p>
 * Please note that this is a simple example and all the features must be of the same
 * FeatureType.
 * <p>
 * This class is used by {@link DataUtilities#source(SimpleFeature[])}.
 * 
 * @author Jody Garnett
 * @since 8.0
 *
 * @source $URL$
 * @version 8.0
 */
public final class ArrayDataStore extends AbstractDataStore {
    private final SimpleFeatureType featureType;

    private final SimpleFeature[] featureArray;
    
    /**
     * Create a read-only DataStore wrapped around the provided feature array.
     * 
     * @param featureArray SimpleFeature array, if empty FeatureTypes.EMPTY is used
     */
    public ArrayDataStore(SimpleFeature[] featureArray) {
        if ((featureArray == null) || (featureArray.length == 0)) {
            this.featureType = FeatureTypes.EMPTY;
        } else {
            this.featureType = featureArray[0].getFeatureType();
        }
        this.featureArray = featureArray;
    }

    public String[] getTypeNames() {
        return new String[] { featureType.getTypeName() };
    }

    public SimpleFeatureType getSchema(String typeName) throws IOException {
        if ((typeName != null) && typeName.equals(featureType.getTypeName())) {
            return featureType;
        }

        throw new IOException(typeName + " not available");
    }

    protected FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(
            String typeName) throws IOException {
        return DataUtilities.reader(featureArray);
    }
}
