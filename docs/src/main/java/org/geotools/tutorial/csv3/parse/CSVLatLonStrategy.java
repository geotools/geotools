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

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.cs.AxisDirection;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.tutorial.csv3.CSVFileState;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

// docs start CSVLatLonStrategy
public class CSVLatLonStrategy extends CSVStrategy {

    /** _CRS */
    public static final DefaultGeographicCRS _CRS = DefaultGeographicCRS.WGS84;

    private String latField;

    private String lngField;

    private String pointField;

    public CSVLatLonStrategy(CSVFileState csvFileState) {
        this(csvFileState, null, null);
    }

    public CSVLatLonStrategy(CSVFileState csvFileState, String latField, String lngField) {
        this(csvFileState, latField, lngField, "location");
    }

    public CSVLatLonStrategy(CSVFileState csvFileState, String latField, String lngField, String pointField) {
        super(csvFileState);
        this.latField = latField;
        this.lngField = lngField;
        this.pointField = pointField;
    }

    @Override
    protected SimpleFeatureType buildFeatureType() {
        String[] headers;
        Map<String, Class<?>> typesFromData;
        CsvReader csvReader = null;
        try {
            csvReader = csvFileState.openCSVReader();
            headers = csvReader.getHeaders();
            typesFromData = findMostSpecificTypesFromData(csvReader, headers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
        }
        SimpleFeatureTypeBuilder builder = createBuilder(csvFileState, headers, typesFromData);

        // If the lat/lon fields were not specified, figure out their spelling now
        if (latField == null || lngField == null) {
            for (String col : headers) {
                if (isLatitude(col)) {
                    latField = col;
                } else if (isLongitude(col)) {
                    lngField = col;
                }
            }
        }

        // For LatLon strategy, we need to change the Lat and Lon columns
        // to be recognized as a Point rather than two numbers, if the
        // values in the respective columns are all accurate (numeric)
        Class<?> latClass = typesFromData.get(latField);
        Class<?> lngClass = typesFromData.get(lngField);
        if (isNumeric(latClass) && isNumeric(lngClass)) {
            List<String> csvHeaders = Arrays.asList(headers);
            int index = csvHeaders.indexOf(latField);
            AttributeTypeBuilder builder2 = new AttributeTypeBuilder();
            builder2.setCRS(_CRS);
            builder2.binding(Point.class);
            AttributeDescriptor descriptor = builder2.buildDescriptor(pointField);
            builder.add(index, descriptor);

            builder.remove(latField);
            builder.remove(lngField);
        }
        return builder.buildFeatureType();
    }

    private boolean isLatitude(String s) {
        return "latitude".equalsIgnoreCase(s) || "lat".equalsIgnoreCase(s);
    }

    private boolean isLongitude(String s) {
        return "lon".equalsIgnoreCase(s)
                || "lng".equalsIgnoreCase(s)
                || "long".equalsIgnoreCase(s)
                || "longitude".equalsIgnoreCase(s);
    }
    // docs end isLongitude

    protected static boolean isNumeric(Class<?> clazz) {
        return clazz != null && (clazz == Double.class || clazz == Integer.class);
    }

    // docs start createSchema
    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        this.featureType = featureType;

        List<String> header = new ArrayList<>();

        GeometryDescriptor gd = featureType.getGeometryDescriptor();
        CoordinateReferenceSystem crs = gd != null ? gd.getCoordinateReferenceSystem() : null;
        if (gd != null
                && CRS.equalsIgnoreMetadata(_CRS, crs)
                && gd.getType().getBinding().isAssignableFrom(Point.class)) {
            if (crs.getCoordinateSystem().getAxis(0).getDirection().equals(AxisDirection.NORTH)) {
                header.add(this.latField);
                header.add(this.lngField);
            } else {
                header.add(this.lngField);
                header.add(this.latField);
            }
        } else {
            throw new IOException("Unable use " + this.latField + "/" + this.lngField + " to represent " + gd);
        }
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
    // docs end createSchema

    // docs start decode
    @Override
    public SimpleFeature decode(String recordId, String[] csvRecord) {
        SimpleFeatureType featureType = getFeatureType();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
        GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
        GeometryFactory geometryFactory = new GeometryFactory();
        Double lat = null, lng = null;
        String[] headers = csvFileState.getCSVHeaders();
        for (int i = 0; i < headers.length; i++) {
            String header = headers[i];
            if (i < csvRecord.length) {
                String value = csvRecord[i].trim();
                if (geometryDescriptor != null && header.equals(latField)) {
                    lat = Double.valueOf(value);
                } else if (geometryDescriptor != null && header.equals(lngField)) {
                    lng = Double.valueOf(value);
                } else {
                    builder.set(header, value);
                }
            } else {
                builder.set(header, null);
            }
        }
        if (geometryDescriptor != null && lat != null && lng != null) {
            Coordinate coordinate;
            if (geometryDescriptor
                    .getCoordinateReferenceSystem()
                    .getCoordinateSystem()
                    .getAxis(0)
                    .getDirection()
                    .equals(AxisDirection.EAST)) {
                coordinate = new Coordinate(lng, lat);
            } else {
                coordinate = new Coordinate(lat, lng);
            }

            Point point = geometryFactory.createPoint(coordinate);
            builder.set(geometryDescriptor.getLocalName(), point);
        }
        return builder.buildFeature(csvFileState.getTypeName() + "-" + recordId);
    }
    // docs end decode

    // docs start encode
    @Override
    public String[] encode(SimpleFeature feature) {
        List<String> csvRecord = new ArrayList<>();
        String[] headers = csvFileState.getCSVHeaders();
        int latIndex = 0;
        int lngIndex = 0;
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equalsIgnoreCase(latField)) {
                latIndex = i;
            }
            if (headers[i].equalsIgnoreCase(lngField)) {
                lngIndex = i;
            }
        }
        for (Property property : feature.getProperties()) {
            Object value = property.getValue();
            if (value == null) {
                csvRecord.add("");
            } else if (value instanceof Point) {
                Point point = (Point) value;
                if (lngIndex < latIndex) {
                    csvRecord.add(Double.toString(point.getY()));
                    csvRecord.add(Double.toString(point.getX()));
                } else {
                    csvRecord.add(Double.toString(point.getX()));
                    csvRecord.add(Double.toString(point.getY()));
                }

            } else {
                String txt = value.toString();
                csvRecord.add(txt);
            }
        }
        return csvRecord.toArray(new String[csvRecord.size() - 1]);
    }
    // docs end encode
}
