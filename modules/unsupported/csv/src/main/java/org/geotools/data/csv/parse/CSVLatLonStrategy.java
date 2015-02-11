/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 * 	  (c) 2014 - 2015 Open Source Geospatial Foundation - all rights reserved
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

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.geotools.data.csv.CSVFileState;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class CSVLatLonStrategy extends CSVStrategy {

    private static final String GEOMETRY_COLUMN = "location";

    public CSVLatLonStrategy(CSVFileState csvFileState) {
        super(csvFileState);
    }
    
    @Override
    protected SimpleFeatureType buildFeatureType() {
    	String[] headers;
        Map<String, Class<?>> typesFromData;
        CsvReader csvReader = null;
        try {
            csvReader = csvFileState.openCSVReader();
            headers = csvReader.getHeaders();
            typesFromData = CSVStrategy.findMostSpecificTypesFromData(csvReader, headers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
        }
        SimpleFeatureTypeBuilder builder = CSVStrategy.createBuilder(csvFileState, headers,
                typesFromData);
        boolean validLat = false;
        boolean validLon = false;
        String latSpelling = null;
        String lonSpelling = null;
        for (String col : headers) {
            Class<?> type = typesFromData.get(col);
            if (isLatitude(col)) {
                latSpelling = col;
                if (CSVStrategy.isNumeric(type)) {
                    validLat = true;
                }
            } else if (isLongitude(col)) {
                lonSpelling = col;
                if (CSVStrategy.isNumeric(type)) {
                    validLon = true;
                }
            }
        }
        if (validLat && validLon) {
        	List<String> csvHeaders = Arrays.asList(headers);
        	int index = csvHeaders.indexOf(latSpelling);
        	AttributeTypeBuilder builder2 = new AttributeTypeBuilder();
        	builder2.setCRS(DefaultGeographicCRS.WGS84);
        	builder2.binding(Point.class);       	
        	AttributeDescriptor descriptor = builder2.buildDescriptor(GEOMETRY_COLUMN);
        	builder.add(index, descriptor);
			
            builder.remove(latSpelling);
            builder.remove(lonSpelling);
        }
        return builder.buildFeatureType();
    }

    private boolean isLatitude(String s) {
        return "latitude".equalsIgnoreCase(s) || "lat".equalsIgnoreCase(s);
    }

    private boolean isLongitude(String s) {
        return "lon".equalsIgnoreCase(s) || "lng".equalsIgnoreCase(s) || "long".equalsIgnoreCase(s)
                || "longitude".equalsIgnoreCase(s);
    }
    
    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        List<String> header = new ArrayList<String>();
        
        GeometryDescriptor geometryDescrptor = featureType.getGeometryDescriptor();
        if (geometryDescrptor != null
                && CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84,
                        geometryDescrptor.getCoordinateReferenceSystem())
                && geometryDescrptor.getType().getBinding().isAssignableFrom(Point.class)) {
            header.add("LAT");
            header.add("LON");
        } else {
            throw new IOException("Unable use LAT/LON to represent " + geometryDescrptor);
        }
        for (AttributeDescriptor descriptor : featureType.getAttributeDescriptors()) {
            if (descriptor instanceof GeometryDescriptor)
                continue;
            header.add(descriptor.getLocalName());
        }
        // Write out header, producing an empty file of the correct type
        CsvWriter writer = new CsvWriter(new FileWriter(this.csvFileState.getFile()),',');
        try {
            writer.writeRecord( header.toArray(new String[header.size()]));
        }
        
        finally {
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
            } else if (value instanceof Point) {
                Point point = (Point) value;
                csvRecord.add(Double.toString(point.getY()));
                csvRecord.add(Double.toString(point.getX()));
            }
            else {
                String txt = value.toString();
                csvRecord.add(txt);
            }
        }
        return csvRecord.toArray(new String[csvRecord.size()-1]);
    }
    
    @Override
    public SimpleFeature decode(String recordId, String[] csvRecord) {
        SimpleFeatureType featureType = getFeatureType();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
        GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
        GeometryFactory geometryFactory = new GeometryFactory();
        Double x = null, y = null;
        String[] headers = csvFileState.getCSVHeaders();
        for (int i = 0; i < headers.length; i++) {
            String header = headers[i];
            if (i < csvRecord.length) {
                String value = csvRecord[i].trim();
                if (geometryDescriptor != null && isLatitude(header)) {
                    y = Double.valueOf(value);
                } else if (geometryDescriptor != null && isLongitude(header)) {
                    x = Double.valueOf(value);
                } else {
                    builder.set(header, value);
                }
            } else {
                builder.set(header, null);
            }
        }
        if (x != null && y != null && geometryDescriptor != null) {
            Coordinate coordinate = new Coordinate(x, y);
            Point point = geometryFactory.createPoint(coordinate);
            builder.set(geometryDescriptor.getLocalName(), point);
        }
        return builder.buildFeature(csvFileState.getTypeName() + "-" + recordId);
    }
}
