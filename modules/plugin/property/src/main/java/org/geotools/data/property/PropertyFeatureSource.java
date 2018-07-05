/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.property;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.Hints;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.WKTReader2;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * @author Jody Garnett
 * @author Torben Barsballe (Boundless)
 * @source $URL$
 */
public class PropertyFeatureSource extends ContentFeatureSource {
    String typeName;
    SimpleFeatureType featureType;
    PropertyDataStore store;

    public PropertyFeatureSource(ContentEntry entry, Query query) {
        super(entry, query);
        this.store = (PropertyDataStore) entry.getDataStore();
        this.typeName = entry.getTypeName();
    }

    @Override
    protected void addHints(Set<Hints.Key> hints) {
        // mark the features as detached, that is, the user can directly alter them
        // without altering the state of the DataStore
        hints.add(Hints.FEATURE_DETACHED);
    }

    public PropertyDataStore getDataStore() {
        return (PropertyDataStore) super.getDataStore();
    }

    @Override
    protected QueryCapabilities buildQueryCapabilities() {
        return new QueryCapabilities() {
            public boolean isUseProvidedFIDSupported() {
                return true;
            }
        };
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        if (query.getFilter() == Filter.INCLUDE) { // filtering not implemented
            ReferencedEnvelope bounds =
                    ReferencedEnvelope.create(getSchema().getCoordinateReferenceSystem());
            FeatureReader<SimpleFeatureType, SimpleFeature> featureReader =
                    getReaderInternal(query);
            try {
                while (featureReader.hasNext()) {
                    SimpleFeature feature = featureReader.next();
                    bounds.include(feature.getBounds());
                }
            } finally {
                featureReader.close();
            }
            return bounds;
        }
        return null; // feature by feature scan required to count records
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        if (query.getFilter() == Filter.INCLUDE) { // filtering not implemented
            int count = 0;
            FeatureReader<SimpleFeatureType, SimpleFeature> featureReader =
                    getReaderInternal(query);
            try {
                while (featureReader.hasNext()) {
                    featureReader.next();
                    count++;
                }
            } finally {
                featureReader.close();
            }
            return count;
        }
        return -1; // feature by feature scan required to count records
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        String typeName = getEntry().getTypeName();
        String namespace = getEntry().getName().getNamespaceURI();

        String typeSpec = property("_");
        try {
            return DataUtilities.createType(namespace, typeName, typeSpec);
        } catch (SchemaException e) {
            e.printStackTrace();
            throw new DataSourceException(typeName + " schema not available", e);
        }
    }

    private String property(String key) throws IOException {
        File file = new File(store.dir, typeName + ".properties");

        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line.startsWith(key + "=")) {
                    return line.substring(key.length() + 1);
                }
            }
        } finally {
            reader.close();
        }
        return null;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        File file = new File(store.dir, typeName + ".properties");
        PropertyFeatureReader reader =
                new PropertyFeatureReader(store.getNamespaceURI(), file, getGeometryFactory(query));

        Double tolerance = (Double) query.getHints().get(Hints.LINEARIZATION_TOLERANCE);
        if (tolerance != null) {
            reader.setWKTReader(new WKTReader2(tolerance));
        }

        return reader;
    }

    private GeometryFactory getGeometryFactory(Query query) {
        Hints hints = query.getHints();
        // grab a geometry factory... check for a special hint
        GeometryFactory geometryFactory = (GeometryFactory) hints.get(Hints.JTS_GEOMETRY_FACTORY);
        if (geometryFactory == null) {
            // look for a coordinate sequence factory
            CoordinateSequenceFactory csFactory =
                    (CoordinateSequenceFactory) hints.get(Hints.JTS_COORDINATE_SEQUENCE_FACTORY);

            if (csFactory != null) {
                geometryFactory = new GeometryFactory(csFactory);
            } else {
                geometryFactory = new GeometryFactory();
            }
        }

        return geometryFactory;
    }

    /**
     * Make handleVisitor package visible allowing PropertyFeatureStore to delegate to this
     * implementation.
     */
    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return super.handleVisitor(query, visitor);
    }
}
