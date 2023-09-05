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
package org.geotools.data.csv;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.data.csv.parse.CSVStrategy;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;

public class CSVDataStore extends ContentDataStore implements FileDataStore {

    private final CSVStrategy csvStrategy;

    private final CSVFileState csvFileState;

    public CSVDataStore(CSVFileState csvFileState, CSVStrategy csvStrategy) {
        this.csvFileState = csvFileState;
        this.csvStrategy = csvStrategy;
    }

    public Name getTypeName() {
        if (namespaceURI != null) {
            return new NameImpl(namespaceURI, csvFileState.getTypeName());
        } else {
            return new NameImpl(csvFileState.getTypeName());
        }
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        return Collections.singletonList(getTypeName());
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        if (csvFileState.getFile() != null && csvFileState.getFile().canWrite()) {
            return new CSVFeatureStore(csvStrategy, csvFileState, entry, Query.ALL);
        } else {
            return new CSVFeatureSource(entry, Query.ALL);
        }
    }

    @Override
    public SimpleFeatureType getSchema() throws IOException {
        return this.csvStrategy.getFeatureType();
    }

    @Override
    public void updateSchema(SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SimpleFeatureSource getFeatureSource() throws IOException {
        return super.getFeatureSource(getTypeName());
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader() throws IOException {
        return new CSVFeatureSource(this).getReader();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            Filter filter, Transaction transaction) throws IOException {
        return super.getFeatureWriter(this.csvFileState.getTypeName(), filter, transaction);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(Transaction transaction)
            throws IOException {
        return super.getFeatureWriter(this.csvFileState.getTypeName(), transaction);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
            Transaction transaction) throws IOException {
        return super.getFeatureWriterAppend(this.csvFileState.getTypeName(), transaction);
    }

    public CSVStrategy getCSVStrategy() {
        return csvStrategy;
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        this.csvStrategy.createSchema(featureType);
        this.csvFileState.setCrs(featureType.getCoordinateReferenceSystem());
    }
}
