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
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.tutorial.csv3.CSVFileState;

// docs start CSVStrategy
public abstract class CSVStrategy {

    protected final CSVFileState csvFileState;

    public CSVStrategy(CSVFileState csvFileState) {
        this.csvFileState = csvFileState;
    }

    public CSVIterator iterator() throws IOException {
        return new CSVIterator(csvFileState, this);
    }
    // docs end CSVStrategy

    protected abstract SimpleFeatureType buildFeatureType();

    public abstract void createSchema(SimpleFeatureType featureType) throws IOException;

    public abstract SimpleFeature decode(String recordId, String[] csvRecord);

    public abstract String[] encode(SimpleFeature feature);

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

    // docs start createBuilder
    /**
     * Originally in a strategy support class - giving a chance to override them to improve efficiency and utilize the
     * different strategies
     */
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
     * Performs a full file scan attempting to guess the type of each column Specific strategy implementations will
     * expand this functionality by overriding the buildFeatureType() method.
     */
    protected static Map<String, Class<?>> findMostSpecificTypesFromData(CsvReader csvReader, String[] headers)
            throws IOException {
        Map<String, Class<?>> result = new HashMap<>();
        // start off assuming Integers for everything
        for (String header : headers) {
            result.put(header, Integer.class);
        }
        // Read through the whole file in case the type changes in later rows
        while (csvReader.readRecord()) {
            List<String> values = Arrays.asList(csvReader.getValues());
            if (values.size() >= headers.length) {
                values = values.subList(0, headers.length);
            }
            int i = 0;
            for (String value : values) {
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
        return result;
    }
    // docs end findMostSpecificTypesFromData
}
