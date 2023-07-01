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
package org.geotools.tutorial.csv3;

import java.io.IOException;
import java.util.NoSuchElementException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.tutorial.csv3.parse.CSVIterator;
import org.geotools.tutorial.csv3.parse.CSVStrategy;

public class CSVFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    private SimpleFeatureType featureType;

    private CSVIterator iterator;

    public CSVFeatureReader(CSVStrategy csvStrategy) throws IOException {
        this(csvStrategy, Query.ALL);
    }

    public CSVFeatureReader(CSVStrategy csvStrategy, Query query) throws IOException {
        this.featureType = csvStrategy.getFeatureType();
        this.iterator = csvStrategy.iterator();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    @Override
    public void close() throws IOException {
        iterator.close();
    }

    @Override
    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        return iterator.next();
    }

    @Override
    public boolean hasNext() throws IOException {
        return iterator.hasNext();
    }
}
