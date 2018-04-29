/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geobuf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentState;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class GeobufFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    private ContentState state;

    private SimpleFeature nextFeature;

    private int featureIndex = 0;

    private FileInputStream in;

    private Geobuf.Data data;

    private GeobufGeometry geobufGeometry;

    private GeobufFeature geobufFeature;

    private SimpleFeatureBuilder featureBuilder;

    public GeobufFeatureReader(ContentState state, Query query, int precision, int dimension)
            throws IOException {
        this.state = state;
        this.geobufGeometry =
                new GeobufGeometry(precision, dimension, JTSFactoryFinder.getGeometryFactory(null));
        this.geobufFeature = new GeobufFeature(geobufGeometry);
        this.featureBuilder = new SimpleFeatureBuilder(state.getFeatureType());
        File file;
        DataStore dataStore = state.getEntry().getDataStore();
        if (dataStore instanceof GeobufDirectoryDataStore) {
            file =
                    ((GeobufDirectoryDataStore) dataStore)
                            .getDataStore(state.getFeatureType().getTypeName())
                            .getFile();
        } else {
            file = ((GeobufDataStore) dataStore).getFile();
        }
        this.in = new FileInputStream(file);
        this.data = Geobuf.Data.parseFrom(in);
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return state.getFeatureType();
    }

    @Override
    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        SimpleFeature feature;
        if (nextFeature != null) {
            feature = nextFeature;
            nextFeature = null;
        } else {
            feature = geobufFeature.decode(data, featureIndex, featureBuilder);
            featureIndex++;
        }
        return feature;
    }

    @Override
    public boolean hasNext() throws IOException {
        if (nextFeature != null) {
            return true;
        } else {
            nextFeature = geobufFeature.decode(data, featureIndex, featureBuilder);
            featureIndex++;
            return nextFeature != null;
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
