/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureStore;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.data.crs.ReprojectFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.DataFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;

/**
 * Generic "results" of a query, class.
 *
 * <p>Please optimize this class when use with your own content. For example a "ResultSet" make a great cache for a
 * JDBCDataStore, a temporary copy of an original file may work for shapefile etc.
 *
 * @author Jody Garnett, Refractions Research
 */
public class DefaultFeatureResults extends DataFeatureCollection {
    /** Shared package logger */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(DefaultFeatureResults.class);

    /** Query used to define this subset of features from the feature source */
    protected Query query;

    /**
     * Feature source used to aquire features, note we are only a "view" of this FeatureSource, its contents,
     * transaction and events need to be forwarded through this collection api to simplier code such as renderers.
     */
    protected SimpleFeatureSource featureSource;

    protected MathTransform transform;

    /**
     * FeatureResults query against featureSource.
     *
     * <p>Please note that is object will not be valid after the transaction has closed.
     *
     * <p>Really? I think it would be, it would just reflect the same query against the SimpleFeatureSource using
     * AUTO_COMMIT.
     */
    public DefaultFeatureResults(SimpleFeatureSource source, Query query) throws IOException {
        super(null, getSchemaInternal(source, query));
        this.featureSource = source;

        SimpleFeatureType originalType = source.getSchema();

        String typeName = originalType.getTypeName();
        if (typeName.equals(query.getTypeName())) {
            this.query = query;
        } else {
            // jg: this should be an error, we are deliberatly gobbling a mistake
            // option 1: remove Query.getTypeName
            // option 2: throw a warning
            // option 3: restore exception code
            this.query = new Query(query);
            this.query.setTypeName(typeName);
        }

        if (originalType.getGeometryDescriptor() == null) {
            return; // no transform needed
        }

        CoordinateReferenceSystem cs = null;
        if (query.getCoordinateSystemReproject() != null) {
            cs = query.getCoordinateSystemReproject();
        } else if (query.getCoordinateSystem() != null) {
            cs = query.getCoordinateSystem();
        }
        CoordinateReferenceSystem originalCRS =
                originalType.getGeometryDescriptor().getCoordinateReferenceSystem();
        if (query.getCoordinateSystem() != null) {
            originalCRS = query.getCoordinateSystem();
        }
        if (cs != null && cs != originalCRS) {
            try {
                transform = CRS.findMathTransform(originalCRS, cs, true);
            } catch (FactoryException noTransform) {
                throw (IOException) new IOException("Could not reproject data to " + cs).initCause(noTransform);
            }
        }
    }

    static SimpleFeatureType getSchemaInternal(SimpleFeatureSource featureSource, Query query) {
        SimpleFeatureType originalType = featureSource.getSchema();
        SimpleFeatureType schema = null;

        CoordinateReferenceSystem cs = null;
        if (query.getCoordinateSystemReproject() != null) {
            cs = query.getCoordinateSystemReproject();
        } else if (query.getCoordinateSystem() != null) {
            cs = query.getCoordinateSystem();
        }
        try {
            if (cs == null) {
                if (query.retrieveAllProperties()) { // we can use the originalType as is
                    schema = featureSource.getSchema();
                } else {
                    schema = DataUtilities.createSubType(featureSource.getSchema(), query.getPropertyNames());
                }
            } else {
                // we need to change the projection of the original type
                schema = DataUtilities.createSubType(
                        originalType, query.getPropertyNames(), cs, query.getTypeName(), null);
            }
        } catch (SchemaException e) {
            // we were unable to create the schema requested!
            // throw new DataSourceException("Could not create schema", e);
            LOGGER.log(Level.WARNING, "Could not change projection to " + cs, e);
            schema = null; // client will notice something is amiss when getSchema() return null
        }

        return schema;
    }
    /**
     * FeatureSchema for provided query.
     *
     * <p>If query.retrieveAllProperties() is <code>true</code> the FeatureSource getSchema() will be returned.
     *
     * <p>If query.getPropertyNames() is used to limit the result of the Query a sub type will be returned based on
     * FeatureSource.getSchema().
     */
    @Override
    public SimpleFeatureType getSchema() {
        return super.getSchema();
    }

    /**
     * Returns transaction from SimpleFeatureSource (if it is a FeatureStore), or Transaction.AUTO_COMMIT if it is not.
     *
     * @return Transacstion this FeatureResults opperates against
     */
    protected Transaction getTransaction() {
        if (featureSource instanceof FeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

            return featureStore.getTransaction();
        } else {
            return Transaction.AUTO_COMMIT;
        }
    }

    /**
     * Retrieve a FeatureReader<SimpleFeatureType, SimpleFeature> for this Query
     *
     * @return FeatureReader<SimpleFeatureType, SimpleFeature> for this Query
     * @throws IOException If results could not be obtained
     */
    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> reader() throws IOException {
        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                ((DataStore) featureSource.getDataStore()).getFeatureReader(query, getTransaction());

        int maxFeatures = query.getMaxFeatures();
        if (maxFeatures != Integer.MAX_VALUE) {
            reader = new MaxFeatureReader<>(reader, maxFeatures);
        }
        if (transform != null) {
            reader = new ReprojectFeatureReader(reader, getSchema(), transform);
        }
        return reader;
    }

    /**
     * Retrieve a FeatureReader<SimpleFeatureType, SimpleFeature> for the geometry attributes only, designed for bounds
     * computation
     */
    protected FeatureReader<SimpleFeatureType, SimpleFeature> boundsReader() throws IOException {
        List<String> attributes = new ArrayList<>();
        SimpleFeatureType schema = featureSource.getSchema();
        for (int i = 0; i < schema.getAttributeCount(); i++) {
            AttributeDescriptor at = schema.getDescriptor(i);
            if (at instanceof GeometryDescriptorImpl) attributes.add(at.getLocalName());
        }

        Query q = new Query(query);
        q.setPropertyNames(attributes);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                ((DataStore) featureSource.getDataStore()).getFeatureReader(q, getTransaction());
        int maxFeatures = query.getMaxFeatures();

        if (maxFeatures == Integer.MAX_VALUE) {
            return reader;
        } else {
            return new MaxFeatureReader<>(reader, maxFeatures);
        }
    }

    /**
     * Returns the bounding box of this FeatureResults
     *
     * <p>This implementation will generate the correct results from reader() if the provided SimpleFeatureSource does
     * not provide an optimized result via FeatureSource.getBounds( Query ). If the feature has no geometry, then an
     * empty envelope is returned.
     *
     * @see org.geotools.data.FeatureResults#getBounds()
     */
    @Override
    public ReferencedEnvelope getBounds() {
        ReferencedEnvelope bounds;

        try {
            bounds = featureSource.getBounds(query);
        } catch (IOException e1) {
            bounds = new ReferencedEnvelope((CoordinateReferenceSystem) null);
        }

        if (bounds == null) {
            try {
                SimpleFeature feature;
                bounds = new ReferencedEnvelope();

                try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = boundsReader()) {
                    while (reader.hasNext()) {
                        feature = reader.next();
                        bounds.include(feature.getBounds());
                    }
                }
            } catch (IllegalAttributeException | IOException e) {
                // throw new DataSourceException("Could not read feature ", e);
                bounds = new ReferencedEnvelope();
            }
        }

        return bounds;
    }

    /**
     * Number of Features in this query.
     *
     * <p>This implementation will generate the correct results from reader() if the provided SimpleFeatureSource does
     * not provide an optimized result via FeatureSource.getCount( Query ).
     *
     * @throws IOException If feature could not be read
     * @throws DataSourceException See IOException
     * @see org.geotools.data.FeatureResults#getCount()
     */
    @Override
    public int getCount() throws IOException {
        int count = featureSource.getCount(query);

        if (count != -1) {
            // optimization worked, return maxFeatures if count is
            // greater.
            int maxFeatures = query.getMaxFeatures();
            return count < maxFeatures ? count : maxFeatures;
        }

        // Okay lets count the FeatureReader
        try {
            count = 0;

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = reader()) {
                for (; reader.hasNext(); count++) {
                    reader.next();
                }
            }

            return count;
        } catch (IllegalAttributeException e) {
            throw new DataSourceException("Could not read feature ", e);
        }
    }

    public SimpleFeatureCollection collection() throws IOException {
        try {
            DefaultFeatureCollection collection = new DefaultFeatureCollection(null, null);

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = reader()) {
                while (reader.hasNext()) {
                    collection.add(reader.next());
                }
            }

            return collection;
        } catch (org.geotools.api.feature.IllegalAttributeException e) {
            throw new DataSourceException("Could not read feature ", e);
        }
    }
}
