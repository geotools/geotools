/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geojson.store;

import java.io.IOException;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.geojson.GeoJSONReader;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

public class GeoJSONFeatureSource extends ContentFeatureSource {
    /** Should we read just the first feature to extract the Schema */
    boolean quick = true;

    public GeoJSONFeatureSource(ContentEntry entry, Query query) {
        super(entry, query);
        transaction = getState().getTransaction();
    }

    public GeoJSONFeatureSource(GeoJSONDataStore datastore) {
        this(datastore, Query.ALL);
    }

    public GeoJSONFeatureSource(GeoJSONDataStore datastore, Query query) {
        this(new ContentEntry(datastore, datastore.getTypeName()), query);
    }

    public GeoJSONFeatureSource(ContentEntry entry) {
        this(entry, Query.ALL);
    }

    @Override
    public GeoJSONDataStore getDataStore() {
        return (GeoJSONDataStore) super.getDataStore();
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        ReferencedEnvelope env = new ReferencedEnvelope(getDataStore().getCrs());

        if (query.getFilter() == Filter.INCLUDE) {

            try (GeoJSONReader reader = getDataStore().read()) {
                try (FeatureIterator<SimpleFeature> itr = reader.getIterator()) {
                    while (itr.hasNext()) {
                        SimpleFeature f = itr.next();
                        env.expandToInclude(
                                ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal());
                    }
                }
            }
        }
        return env;
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        if (query.getFilter() == Filter.INCLUDE) {

            try (GeoJSONReader reader = getDataStore().read()) {
                int count = 0;
                try (FeatureIterator<SimpleFeature> itr = reader.getIterator()) {
                    while (itr.hasNext()) {
                        itr.next();
                        count += 1;
                    }
                    return count;
                }
            }
        }
        return -1; // feature by feature scan required to count records
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        return new GeoJSONFeatureReader(getState(), query);
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {

        // read headers

        try (GeoJSONReader reader = getDataStore().read()) {
            try (FeatureIterator<SimpleFeature> itr = reader.getIterator()) {

                while (itr.hasNext()) {
                    itr.next();
                    schema = (SimpleFeatureType) reader.getSchema();
                    if (isQuick()) {
                        break;
                    }
                }
                /*
                 * In the event we are dealing with an empty file the schema may have
                 * been set in the datastore.
                 */
                if (schema == null) {
                    schema = getDataStore().getSchema();
                }
            }

            return schema;
        }
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return super.handleVisitor(query, visitor);
    }

    /** @return the quick */
    public boolean isQuick() {
        return quick;
    }

    /** @param quick the quick to set */
    public void setQuick(boolean quick) {
        this.quick = quick;
    }
}
