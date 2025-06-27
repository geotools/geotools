/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 * 	  (C) 2014 - 2015 Open Source Geospatial Foundation (OSGeo)
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

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.data.csv.CSVFileState;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.util.Converters;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

public class CSVSpecifiedWKTStrategy extends CSVStrategy {

    private final String wktField;

    public CSVSpecifiedWKTStrategy(CSVFileState csvFileState, String wktField) {
        super(csvFileState);
        this.wktField = wktField;
    }

    @Override
    protected SimpleFeatureType buildFeatureType() {
        SimpleFeatureTypeBuilder featureBuilder = createBuilder(csvFileState);
        // For WKT strategy, we need to make sure the wktField is recognized as a Geometry
        AttributeDescriptor descriptor = featureBuilder.get(wktField);
        if (descriptor != null) {
            AttributeTypeBuilder attributeBuilder = new AttributeTypeBuilder();
            attributeBuilder.init(descriptor);
            attributeBuilder.setCRS(csvFileState.getCrs());
            attributeBuilder.binding(Geometry.class);

            AttributeDescriptor modified = attributeBuilder.buildDescriptor(wktField);
            featureBuilder.set(modified);
        }
        return featureBuilder.buildFeatureType();
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        this.featureType = featureType;

        List<String> header = new ArrayList<>();

        for (AttributeDescriptor descriptor : featureType.getAttributeDescriptors()) {
            if (descriptor instanceof GeometryDescriptor) {
                header.add(wktField);
            } else {
                header.add(descriptor.getLocalName());
            }
        }
        // Write out header, producing an empty file of the correct type
        try (CSVWriter writer = new CSVWriter(
                new FileWriter(this.csvFileState.getFile(), StandardCharsets.UTF_8),
                getSeparator(),
                getQuotechar(),
                getEscapechar(),
                getLineSeparator())) {
            writer.writeNext(header.toArray(new String[header.size()]), isQuoteAllFields());
        }
    }

    @Override
    public String[] encode(SimpleFeature feature) {
        List<String> csvRecord = new ArrayList<>();
        for (Property property : feature.getProperties()) {
            String name = property.getName().getLocalPart();
            Object value = property.getValue();
            if (value == null) {
                csvRecord.add("");
            } else if (name.compareTo(wktField) == 0) {
                WKTWriter wkt = new WKTWriter();
                String txt = wkt.write((Geometry) value);
                csvRecord.add(txt);
            } else {
                String txt = Converters.convert(value, String.class);
                csvRecord.add(txt);
            }
        }
        return csvRecord.toArray(new String[csvRecord.size() - 1]);
    }

    @Override
    public SimpleFeature decode(String recordId, String[] csvRecord) {
        SimpleFeatureType featureType = getFeatureType();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
        GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
        String[] headers = csvFileState.getCSVHeaders();
        for (int i = 0; i < headers.length; i++) {
            String header = headers[i];
            if (i < csvRecord.length) {
                String value = csvRecord[i].trim();
                if (geometryDescriptor != null && header.equals(wktField)) {
                    WKTReader wktReader = new WKTReader();
                    Geometry geometry;
                    try {
                        geometry = wktReader.read(value);
                    } catch (ParseException e) {
                        // policy decision here that just nulls out unparseable geometry
                        geometry = null;
                    }
                    builder.set(wktField, geometry);
                } else {
                    builder.set(header, value);
                }
            } else {
                LOGGER.warning("record had fewer values than header");
                if (csvRecord.length == 1 && csvRecord[0].isEmpty()) {
                    return null;
                }
                builder.set(header, null);
            }
        }
        return builder.buildFeature(csvFileState.getTypeName() + "-" + recordId);
    }
}
