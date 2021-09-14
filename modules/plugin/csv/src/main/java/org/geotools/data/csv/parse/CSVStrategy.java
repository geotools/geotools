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

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.data.csv.CSVFileState;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public abstract class CSVStrategy {
    /** logger */
    static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(CSVStrategy.class);

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

    protected volatile SimpleFeatureType featureType = null;

    private boolean writePrj = false;

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

    /**
     * Originally in a strategy support class - giving a chance to override them to improve
     * efficiency and utilise the different strategies
     */
    public static SimpleFeatureTypeBuilder createBuilder(CSVFileState csvFileState) {
        Map<String, Class<?>> typesFromData;
        String[] headers;
        try (CSVReader csvReader = csvFileState.openCSVReader()) {
            headers = csvFileState.getCSVHeaders();
            typesFromData = findMostSpecificTypesFromData(csvReader, headers);
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Failure reading csv file", e);
        }
        return createBuilder(csvFileState, headers, typesFromData);
    }

    public static SimpleFeatureTypeBuilder createBuilder(
            CSVFileState csvFileState, String[] headers, Map<String, Class<?>> typesFromData) {
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

    /**
     * Performs a full file scan attempting to guess the type of each column Specific strategy
     * implementations will expand this functionality by overriding the buildFeatureType() method.
     */
    protected static Map<String, Class<?>> findMostSpecificTypesFromData(
            CSVReader csvReader, String[] headers) throws IOException {
        Map<String, Class<?>> result = new HashMap<>();
        // start off assuming Integers for everything
        for (String header : headers) {
            result.put(header, Integer.class);
        }
        String[] record;
        // Read through the whole file in case the type changes in later rows
        try {
            while ((record = csvReader.readNext()) != null) {

                List<String> values = Arrays.asList(record);
                if (record.length >= headers.length) {
                    values = values.subList(0, headers.length);
                }
                int i = 0;
                for (String value : values) {
                    value = value.trim();
                    String header = headers[i];
                    Class<?> type = result.get(header);
                    // For each value in the row, ensure we can still parse it as the
                    // defined type for this column; if not, make it more general
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
                    }
                    result.put(header, type);
                    i++;
                }
            }
        } catch (CsvValidationException e) {
            throw new IOException(e);
        }
        return result;
    }

    /** @return the separator */
    public char getSeparator() {
        return csvFileState.getSeparator();
    }

    /** @param separator the separator to set */
    public void setSeparator(char separator) {
        csvFileState.setSeparator(separator);
    }

    /** @return the quotechar */
    public char getQuotechar() {
        return csvFileState.getQuotechar();
    }

    /** @param quotechar the quotechar to set */
    public void setQuotechar(char quotechar) {
        this.csvFileState.setQuotechar(quotechar);
    }

    /** @return the escapechar */
    public char getEscapechar() {
        return csvFileState.getEscapechar();
    }

    /** @param escapechar the escapechar to set */
    public void setEscapechar(char escapechar) {
        csvFileState.setEscapechar(escapechar);
    }

    /** @return the lineSeparator */
    public String getLineSeparator() {
        return csvFileState.getLineSeparator();
    }

    /** @param lineSeparator the lineSeparator to set */
    public void setLineSeparator(String lineSeparator) {
        csvFileState.setLineSeparator(lineSeparator);
    }

    /** @return the quoteAllFields */
    public boolean isQuoteAllFields() {
        return csvFileState.isQuoteAllFields();
    }

    /** @param quoteAllFields the quoteAllFields to set */
    public void setQuoteAllFields(boolean quoteAllFields) {
        csvFileState.setQuoteAllFields(quoteAllFields);
    }

    public void setWritePrj(boolean booleanValue) {
        this.writePrj = booleanValue;
    }

    public boolean isWritePrj() {
        return writePrj;
    }
}
