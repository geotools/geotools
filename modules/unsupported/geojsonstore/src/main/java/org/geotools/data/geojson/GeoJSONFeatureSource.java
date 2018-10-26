package org.geotools.data.geojson;

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

import java.io.IOException;
import java.util.logging.Logger;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

public class GeoJSONFeatureSource extends ContentFeatureSource {
    private static final Logger LOGGER = Logging.getLogger(GeoJSONFeatureSource.class);

    private FeatureCollection<?, ?> collection = null;

    public GeoJSONFeatureSource(ContentEntry entry, Query query) {
        super(entry, query);
        if (schema == null) {
            // first see if our datastore knows what the schema is
            schema = getDataStore().schema;
        }
        if (schema == null) {
            try {
                // failing that we'll attempt to construct it from the features
                schema = buildFeatureType();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            }
        }
    }

    public GeoJSONDataStore getDataStore() {
        return (GeoJSONDataStore) super.getDataStore();
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {

        collection = fetchFeatures();
        FeatureCollection<?, ?> sub = collection.subCollection(query.getFilter());
        ReferencedEnvelope bounds = sub.getBounds();
        if (bounds.getCoordinateReferenceSystem() == null) {
            bounds = new ReferencedEnvelope(bounds, DefaultGeographicCRS.WGS84);
        }
        return bounds;
    }

    /**
     * @param query
     * @return
     * @throws IOException
     */
    private FeatureCollection<?, ?> fetchFeatures() throws IOException {
        // Ideally we would cache the features here but then things go badly when using transactions
        LOGGER.fine("fetching reader from datastore");
        GeoJSONReader reader = getDataStore().read();
        collection = reader.getFeatures();
        LOGGER.fine("Got " + collection.size() + " features");

        return collection;
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        collection = fetchFeatures();
        FeatureCollection<?, ?> sub = collection.subCollection(query.getFilter());
        return sub.size();
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {

        return new GeoJSONFeatureReader(getState(), query);
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        if (schema == null) {
            FeatureCollection<?, ?> collection = fetchFeatures();
            SimpleFeatureType sch = (SimpleFeatureType) collection.getSchema();
            SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
            sb.setName(getState().getEntry().getTypeName());
            for (AttributeDescriptor att : sch.getAttributeDescriptors()) {
                sb.add(att);
            }
            if (sch.getCoordinateReferenceSystem() != null) {
                sb.setCRS(sch.getCoordinateReferenceSystem());
            } else {
                sb.setCRS(DefaultGeographicCRS.WGS84);
            }
            sb.setDescription(sch.getDescription());
            schema = sb.buildFeatureType();
        }
        return schema;
    }

    /**
     * Make handleVisitor package visible allowing CSVFeatureStore to delegate to this
     * implementation.
     */
    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return super.handleVisitor(query, visitor);
    }
}
