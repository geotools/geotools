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

import org.geotools.data.csv.CSVFileState;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class CSVAttributesOnlyStrategy extends AbstractCSVStrategy implements CSVStrategy {

    public CSVAttributesOnlyStrategy(CSVFileState csvFileState) {
        super(csvFileState);
    }

    protected SimpleFeatureType buildFeatureType() {
        SimpleFeatureTypeBuilder builder = CSVStrategySupport.createBuilder(csvFileState);
        return builder.buildFeatureType();
    }

    @Override
    public SimpleFeature createFeature(String recordId, String[] csvRecord) {
        SimpleFeatureType featureType = getFeatureType();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
        String[] headers;
        headers = csvFileState.getCSVHeaders();
        for (int i = 0; i < headers.length; i++) {
            String header = headers[i];
            if (i < csvRecord.length) {
                String value = csvRecord[i].trim();
                builder.set(header, value);
            } else {
                // geotools converters take care of converting for us
                builder.set(header, null);
            }
        }
        return builder.buildFeature(csvFileState.getTypeName() + "-" + recordId);
    }

}
