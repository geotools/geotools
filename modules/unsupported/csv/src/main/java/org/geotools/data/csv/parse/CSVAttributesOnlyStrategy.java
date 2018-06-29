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

import com.csvreader.CsvWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.geotools.data.csv.CSVFileState;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

public class CSVAttributesOnlyStrategy extends CSVStrategy {

    public CSVAttributesOnlyStrategy(CSVFileState csvFileState) {
        super(csvFileState);
    }

    @Override
    protected SimpleFeatureType buildFeatureType() {
        SimpleFeatureTypeBuilder builder = createBuilder(csvFileState);
        return builder.buildFeatureType();
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        List<String> header = new ArrayList<String>();
        this.featureType = featureType;
        for (AttributeDescriptor descriptor : featureType.getAttributeDescriptors()) {
            if (descriptor instanceof GeometryDescriptor) continue;
            header.add(descriptor.getLocalName());
        }

        // Write out header, producing an empty file of the correct type
        CsvWriter writer = new CsvWriter(new FileWriter(this.csvFileState.getFile()), ',');
        try {
            writer.writeRecord(header.toArray(new String[header.size()]));
        } finally {
            writer.close();
        }
    }

    @Override
    public String[] encode(SimpleFeature feature) {
        List<String> csvRecord = new ArrayList<String>();
        for (Property property : feature.getProperties()) {
            Object value = property.getValue();
            if (value == null) {
                csvRecord.add("");
            } else if (Geometry.class.isAssignableFrom(value.getClass())) {
                // skip geometries
            } else {
                String txt = value.toString();
                csvRecord.add(txt);
            }
        }
        return csvRecord.toArray(new String[csvRecord.size() - 1]);
    }

    @Override
    public SimpleFeature decode(String recordId, String[] csvRecord) {
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
