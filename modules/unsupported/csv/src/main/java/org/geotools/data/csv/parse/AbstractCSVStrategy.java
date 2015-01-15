/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 * 	  (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * 	  (c) 2012 - 2014 OpenPlans
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
package org.geotools.data.csv.parse;

import java.io.IOException;

import org.geotools.data.csv.CSVFileState;
import org.opengis.feature.simple.SimpleFeatureType;

public abstract class AbstractCSVStrategy implements CSVStrategy {

    protected final CSVFileState csvFileState;

    protected volatile SimpleFeatureType featureType;

    public AbstractCSVStrategy(CSVFileState csvFileState) {
        this.csvFileState = csvFileState;
        featureType = null;
    }

    protected abstract SimpleFeatureType buildFeatureType();

    @Override
    public SimpleFeatureType getFeatureType() {
        if (featureType == null) {
            synchronized (this) {
                if (featureType == null) {
                    featureType = buildFeatureType();
                }
            }
        }
        return featureType;
    }

    @Override
    public CSVIterator iterator() throws IOException {
        return new CSVIterator(csvFileState, this);
    }

}
