/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.tutorial.csv3.parse;

import com.csvreader.CsvWriter;
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
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.tutorial.csv3.CSVFileState;
import org.locationtech.jts.geom.Geometry;

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
        List<String> header = new ArrayList<>();
        this.featureType = featureType;
        for (AttributeDescriptor descriptor : featureType.getAttributeDescriptors()) {
            if (descriptor instanceof GeometryDescriptor) continue;
            header.add(descriptor.getLocalName());
        }

        // Write out header, producing an empty file of the correct type
        CsvWriter writer = new CsvWriter(new FileWriter(this.csvFileState.getFile(), StandardCharsets.UTF_8), ',');
        try {
            writer.writeRecord(header.toArray(new String[header.size()]));
        } finally {
            writer.close();
        }
    }

    @Override
    public String[] encode(SimpleFeature feature) {
        List<String> csvRecord = new ArrayList<>();
        for (Property property : feature.getProperties()) {
            Object value = property.getValue();
            if (value == null) {
                csvRecord.add("");
            } else if (!Geometry.class.isAssignableFrom(value.getClass())) {
                // skip geometries
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
        String[] headers = csvFileState.getCSVHeaders();
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
