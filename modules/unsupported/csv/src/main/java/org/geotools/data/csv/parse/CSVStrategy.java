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

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.csv.CSVFileState;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.csvreader.CsvReader;
    // class definition start
public abstract class CSVStrategy {
	
    protected final CSVFileState csvFileState;

    public CSVStrategy(CSVFileState csvFileState) {
        this.csvFileState = csvFileState;
    }
    
    public CSVIterator iterator() throws IOException {
        return new CSVIterator(csvFileState, this);
    }

    protected abstract SimpleFeatureType buildFeatureType();
    
    public abstract void createSchema(SimpleFeatureType featureType) throws IOException;

    public abstract SimpleFeature decode(String recordId, String[] csvRecord);
    
    public abstract String[] encode(SimpleFeature feature);
    // class definition end
    
    // featureType end
    protected volatile SimpleFeatureType featureType = null;

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
    // featureType start
    /** 
     * Originally in a strategy support class - giving a chance to override them to
     * improve efficiency and utilize the different strategies
     */
    // builder start
    public static SimpleFeatureTypeBuilder createBuilder(CSVFileState csvFileState) {
        CsvReader csvReader = null;
        Map<String, Class<?>> typesFromData = null;
        String[] headers = null;
        try {
            csvReader = csvFileState.openCSVReader();
            headers = csvReader.getHeaders();
            typesFromData = findMostSpecificTypesFromData(csvReader, headers);
        } catch (IOException e) {
            throw new RuntimeException("Failure reading csv file", e);
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
        }
        return createBuilder(csvFileState, headers, typesFromData);
    }

    public static SimpleFeatureTypeBuilder createBuilder(CSVFileState csvFileState,
            String[] headers, Map<String, Class<?>> typesFromData) {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(csvFileState.getTypeName());
        builder.setCRS(csvFileState.getCrs());
        if (csvFileState.getNamespace() != null) {
            builder.setNamespaceURI(csvFileState.getNamespace());
        }
        for (String col : headers) {
            Class<?> type = typesFromData.get(col);
            builder.add(col, type);
        }
        return builder;
    }
    // builder end
    
    // types start
    /** Performs a full file scan attempting to guess the type of each column */
    protected static Map<String, Class<?>> findMostSpecificTypesFromData(CsvReader csvReader,
            String[] headers) throws IOException {
        Map<String, Class<?>> result = new HashMap<String, Class<?>>();
        // start off assuming Integers for everything
        for (String header : headers) {
            result.put(header, Integer.class);
        }
        while (csvReader.readRecord()) {
            String[] record = csvReader.getValues();
            List<String> values = Arrays.asList(record);
            if (record.length >= headers.length) {
                values = values.subList(0, headers.length);
            }
            int i = 0;
            for (String value : values) {
                String header = headers[i];
                Class<?> type = result.get(header);
                if (type == Integer.class) {
                    try {
                        Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        try {
                            Double.parseDouble(value);
                            type = Double.class;
                        } catch (NumberFormatException ex) {
                            type = String.class;
                        }
                    }
                } else if (type == Double.class) {
                    try {
                        Double.parseDouble(value);
                    } catch (NumberFormatException e) {
                        type = String.class;
                    }
                } else {
                    type = String.class;
                }
                result.put(header, type);
                i++;
            }
        }
        return result;
    }

    protected static boolean isNumeric(Class<?> clazz) {
        return clazz != null && (clazz == Double.class || clazz == Integer.class);
    }
    // types end
}
