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
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.tutorial.csv3.CSVFileState;

public class CSVIterator implements Iterator<SimpleFeature> {

    private int idx;

    private SimpleFeature next;

    private final CsvReader csvReader;

    private final CSVStrategy csvStrategy;

    public CSVIterator(CSVFileState csvFileState, CSVStrategy csvStrategy) throws IOException {
        this.csvStrategy = csvStrategy;
        csvReader = csvFileState.openCSVReader();
        idx = 1;
        next = null;
    }

    private SimpleFeature buildFeature(String[] csvRecord) {
        String id = "fid" + idx;
        SimpleFeature feature = csvStrategy.decode(id, csvRecord);
        idx++;
        return feature;
    }

    @Override
    public boolean hasNext() {
        if (next != null) {
            return true;
        }
        try {
            next = readFeature();
        } catch (IOException e) {
            next = null;
        }
        return next != null;
    }

    // docs start readFeature
    private SimpleFeature readFeature() throws IOException {
        if (csvReader.readRecord()) {
            String[] csvRecord = csvReader.getValues();
            return buildFeature(csvRecord);
        }
        return null;
    }
    // docs end readFeature

    @Override
    public SimpleFeature next() {
        if (next != null) {
            SimpleFeature result = next;
            next = null;
            return result;
        }
        SimpleFeature feature;
        try {
            feature = readFeature();
        } catch (IOException e) {
            feature = null;
        }
        if (feature == null) {
            throw new NoSuchElementException();
        }
        return feature;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Cannot remove features from csv iteratore");
    }

    public void close() {
        csvReader.close();
    }
}
