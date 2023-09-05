/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.store;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureReader;
import org.geotools.api.data.SimpleFeatureWriter;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.LineString;

public abstract class AbstractContentTest {

    /** Mock feature type name. */
    protected static final Name TYPENAME = new NameImpl("http://www.geotools.org", "Mock");

    /** Mock feature type. */
    protected static final SimpleFeatureType TYPE = buildType();

    /** The list of features on which paging is tested. */
    @SuppressWarnings("serial")
    List<SimpleFeature> FEATURES =
            Stream.of(buildFeature("mock.3"), buildFeature("mock.1"), buildFeature("mock.2"))
                    .collect(Collectors.toList());

    /** Build the test type. */
    protected static SimpleFeatureType buildType() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(TYPENAME);
        builder.add("geom", LineString.class);
        return builder.buildFeatureType();
    }

    /** Build a test feature with the specified id. */
    protected static SimpleFeature buildFeature(String id) {
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(TYPE);
        builder.add(new Envelope(0, 1, 0, 1));
        return builder.buildFeature(id);
    }

    /** {@link ContentDataStore} for the test features. */
    protected class MockContentDataStore extends ContentDataStore {

        {
            namespaceURI = TYPE.getName().getNamespaceURI();
        }

        /** @see org.geotools.data.store.ContentDataStore#createTypeNames() */
        @SuppressWarnings("serial")
        @Override
        protected List<Name> createTypeNames() throws IOException {
            return List.of(TYPENAME);
        }

        /**
         * @see
         *     org.geotools.data.store.ContentDataStore#createFeatureSource(org.geotools.data.store.ContentEntry)
         */
        @Override
        protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
            return new MockContentFeatureStore(entry, null);
        }
    }

    /** {@link ContentFeatureSource} that returns the test features. */
    protected class MockContentFeatureStore extends ContentFeatureStore {

        public MockContentFeatureStore(ContentEntry entry, Query query) {
            super(entry, query);
        }

        /** Not implemented. */
        @Override
        protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
            throw new UnsupportedOperationException();
        }

        /** Not implemented. */
        @Override
        protected int getCountInternal(Query query) throws IOException {
            if (query.getFilter() == Filter.INCLUDE) {
                int count = 0;
                try (FeatureReader<SimpleFeatureType, SimpleFeature> featureReader =
                        getReaderInternal(query)) {
                    while (featureReader.hasNext()) {
                        featureReader.next();
                        count++;
                    }
                }
                return count;
            }
            return -1;
        }

        /** @see org.geotools.data.store.ContentFeatureSource#getReaderInternal(Query) */
        @Override
        protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
                throws IOException {
            return new MockSimpleFeatureReader();
        }

        /** @see org.geotools.data.store.ContentFeatureSource#buildFeatureType() */
        @Override
        protected SimpleFeatureType buildFeatureType() throws IOException {
            return TYPE;
        }

        @Override
        protected FeatureWriter<SimpleFeatureType, SimpleFeature> getWriterInternal(
                Query query, int flags) throws IOException {
            return new MockSimpleFeatureWriter();
        }
    }

    /** Decorate the list of test features as a {@link SimpleFeatureReader}. */
    protected class MockSimpleFeatureReader implements SimpleFeatureReader {

        /** Index of the next test feature to be returned. */
        private int index = 0;

        /** @see FeatureReader#getFeatureType() */
        @Override
        public SimpleFeatureType getFeatureType() {
            return TYPE;
        }

        /** @see FeatureReader#next() */
        @Override
        public SimpleFeature next()
                throws IOException, IllegalArgumentException, NoSuchElementException {
            return FEATURES.get(index++);
        }

        /** @see FeatureReader#hasNext() */
        @Override
        public boolean hasNext() throws IOException {
            return index < FEATURES.size();
        }

        /** @see FeatureReader#close() */
        @Override
        public void close() throws IOException {
            // ignored
        }
    }

    /** Decorate the list of test features as a {@link SimpleFeatureReader}. */
    protected class MockSimpleFeatureWriter implements SimpleFeatureWriter {

        /** Index of the next test feature to be returned. */
        private int index = 0;

        SimpleFeature newFeature;

        @Override
        public SimpleFeatureType getFeatureType() {
            return TYPE;
        }

        @Override
        public SimpleFeature next() throws IOException {
            if (index >= FEATURES.size()) {
                newFeature = buildFeature("mock." + (++index));
                return newFeature;
            }
            return FEATURES.get(index++);
        }

        @Override
        public void remove() throws IOException {
            if (index > 0 && index <= FEATURES.size()) {
                FEATURES.remove(index - 1);
            }
        }

        @Override
        public void write() throws IOException {
            if (index > FEATURES.size()) {
                FEATURES.add(newFeature);
            }
        }

        @Override
        public boolean hasNext() throws IOException {
            return index < FEATURES.size();
        }

        @Override
        public void close() throws IOException {
            // ignored
        }
    }
}
