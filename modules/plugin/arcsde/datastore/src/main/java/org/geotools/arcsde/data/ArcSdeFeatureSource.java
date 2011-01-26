/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.arcsde.data;

import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.versioning.ArcSdeVersionHandler;
import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;

public class ArcSdeFeatureSource implements SimpleFeatureSource {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.arcsde.data");

    /**
     * {@link Hints#FEATURE_DETACHED} and the ones supported by
     * {@link ArcSDEDataStore#getGeometryFactory}
     * 
     * @see #getSupportedHints()
     */
    private static final Set<Key> supportedHints = new HashSet<Key>();
    static {
        supportedHints.add(Hints.FEATURE_DETACHED);
        supportedHints.add(Hints.JTS_GEOMETRY_FACTORY);
        supportedHints.add(Hints.JTS_COORDINATE_SEQUENCE_FACTORY);
        supportedHints.add(Hints.JTS_PRECISION_MODEL);
        supportedHints.add(Hints.JTS_SRID);
    };

    protected Transaction transaction = Transaction.AUTO_COMMIT;

    protected FeatureTypeInfo typeInfo;

    protected ArcSDEDataStore dataStore;

    private ArcSdeResourceInfo resourceInfo;

    private QueryCapabilities queryCapabilities;

    public ArcSdeFeatureSource(final FeatureTypeInfo typeInfo, final ArcSDEDataStore dataStore) {
        this.typeInfo = typeInfo;
        this.dataStore = dataStore;
        this.queryCapabilities = new QueryCapabilities() {
            @Override
            public boolean supportsSorting(final SortBy[] sortAttributes) {
                final SimpleFeatureType featureType = typeInfo.getFeatureType();
                for (SortBy sortBy : sortAttributes) {
                    if (SortBy.NATURAL_ORDER == sortBy || SortBy.REVERSE_ORDER == sortBy) {
                        // TODO: we should be able to support natural order
                        return false;
                    } else {
                        String attName = sortBy.getPropertyName().getPropertyName();
                        AttributeDescriptor descriptor = featureType.getDescriptor(attName);
                        if (descriptor == null) {
                            return false;
                        }
                        if (descriptor instanceof GeometryDescriptor) {
                            return false;
                        }
                    }
                }
                return true;
            }
        };
    }

    /**
     * Returns the same name than the feature type (ie, {@code getSchema().getName()} to honor the
     * simple feature land common practice of calling the same both the Features produces and their
     * types
     * 
     * @since 2.5
     * @see FeatureSource#getName()
     */
    public Name getName() {
        return getSchema().getName();
    }

    /**
     * @see FeatureSource#getInfo()
     */
    public synchronized ArcSdeResourceInfo getInfo() {
        if (this.resourceInfo == null) {
            this.resourceInfo = new ArcSdeResourceInfo(this.typeInfo, this);
        }
        return this.resourceInfo;
    }

    public QueryCapabilities getQueryCapabilities() {
        return this.queryCapabilities;
    }

    /**
     * @see FeatureSource#addFeatureListener(FeatureListener)
     */
    public final void addFeatureListener(final FeatureListener listener) {
        dataStore.listenerManager.addFeatureListener(this, listener);
    }

    /**
     * @see FeatureSource#removeFeatureListener(FeatureListener)
     */
    public final void removeFeatureListener(final FeatureListener listener) {
        dataStore.listenerManager.removeFeatureListener(this, listener);
    }

    /**
     * @see FeatureSource#getBounds()
     */
    public final ReferencedEnvelope getBounds() throws IOException {
        return getBounds(Query.ALL);
    }

    /**
     * @return The bounding box of the query or null if unknown and too expensive for the method to
     *         calculate or any errors occur.
     * @see FeatureSource#getBounds(Query)
     */
    public final ReferencedEnvelope getBounds(final Query query) throws IOException {
        final Query namedQuery = namedQuery(query);
        final ISession session = getSession();
        ReferencedEnvelope ev;
        try {
            ev = getBounds(namedQuery, session);
        } finally {
            session.dispose();
        }
        return ev;
    }

    /**
     * @param namedQuery
     * @param session
     * @return The bounding box of the query or null if unknown and too expensive for the method to
     *         calculate or any errors occur.
     * @throws DataSourceException
     * @throws IOException
     */
    protected ReferencedEnvelope getBounds(final Query namedQuery, final ISession session)
            throws DataSourceException, IOException {

        final String typeName = typeInfo.getFeatureTypeName();
        final ArcSdeVersionHandler versionHandler = dataStore.getVersionHandler(typeName,
                transaction);

        Envelope ev = ArcSDEQuery.calculateQueryExtent(session, typeInfo, namedQuery,
                versionHandler);

        if (ev != null) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("ArcSDE optimized getBounds call returned: " + ev);
            }
            final ReferencedEnvelope envelope;
            final GeometryDescriptor defaultGeometry = getSchema().getGeometryDescriptor();
            if (defaultGeometry == null) {
                envelope = ReferencedEnvelope.reference(ev);
            } else {
                envelope = new ReferencedEnvelope(ev,
                        defaultGeometry.getCoordinateReferenceSystem());
            }
            return envelope;
        }
        LOGGER.finer("ArcSDE couldn't process all filters in this query, "
                + "so optimized getBounds() returns null.");

        return null;
    }

    /**
     * @see FeatureSource#getCount(Query)
     */
    public final int getCount(final Query query) throws IOException {
        final Query namedQuery = namedQuery(query);
        final ISession session = getSession();
        final int count;
        try {
            count = getCount(namedQuery, session);
        } finally {
            session.dispose();
        }
        return count;
    }

    /**
     * @see FeatureSource#getCount(Query)
     */
    protected int getCount(final Query namedQuery, final ISession session) throws IOException {
        final int count;
        final String typeName = typeInfo.getFeatureTypeName();
        final ArcSdeVersionHandler versionHandler = dataStore.getVersionHandler(typeName,
                transaction);
        count = ArcSDEQuery.calculateResultCount(session, typeInfo, namedQuery, versionHandler);
        return count;
    }

    /**
     * Returns a session appropriate for the current transaction
     * <p>
     * This is convenient way to get a connection for {@link #getBounds()} and
     * {@link #getCount(Query)}. {@link ArcSdeFeatureStore} overrides to get the connection from the
     * transaction instead of the pool.
     * </p>
     * 
     * @return
     */
    protected final ISession getSession() throws IOException {
        return dataStore.getSession(transaction);
    }

    private Query namedQuery(final Query query) {
        final String localName = typeInfo.getFeatureTypeName();
        final String typeName = query.getTypeName();
        if (typeName != null && !localName.equals(typeName)) {
            throw new IllegalArgumentException("Wrong type name: " + typeName + " (this is "
                    + localName + ")");
        }
        Query namedQuery = new Query(query);
        namedQuery.setTypeName(localName);
        return namedQuery;
    }

    /**
     * @see FeatureSource#getDataStore()
     */
    public final ArcSDEDataStore getDataStore() {
        return dataStore;
    }

    /**
     * @see FeatureSource#getFeatures(Query)
     */
    public final SimpleFeatureCollection getFeatures(final Query query) throws IOException {
        final Query namedQuery = namedQuery(query);
        SimpleFeatureCollection collection;
        SimpleFeatureType queryType = dataStore.getQueryType(namedQuery);
        collection = new ArcSdeFeatureCollection(this, queryType, namedQuery);
        return collection;
    }

    /**
     * @see FeatureSource#getFeatures(Filter)
     */
    public final SimpleFeatureCollection getFeatures(final Filter filter) throws IOException {
        Query query = new Query(typeInfo.getFeatureTypeName(), filter);
        return getFeatures(query);
    }

    /**
     * @see FeatureSource#getFeatures()
     */
    public final SimpleFeatureCollection getFeatures() throws IOException {
        return getFeatures(Filter.INCLUDE);
    }

    /**
     * @see FeatureSource#getSchema();
     */
    public final SimpleFeatureType getSchema() {
        return typeInfo.getFeatureType();
    }

    /**
     * ArcSDE features are always "detached", so we return the FEATURE_DETACHED hint here, as well
     * as the JTS related ones.
     * <p>
     * The JTS related hints supported are:
     * <ul>
     * <li>JTS_GEOMETRY_FACTORY
     * <li>JTS_COORDINATE_SEQUENCE_FACTORY
     * <li>JTS_PRECISION_MODEL
     * <li>JTS_SRID
     * </ul>
     * Note, however, that if a {@link GeometryFactory} is provided through the
     * {@code JTS_GEOMETRY_FACTORY} hint, that very factory is used and takes precedence over all
     * the other ones.
     * </p>
     * 
     * @see FeatureSource#getSupportedHints()
     * @see Hints#FEATURE_DETACHED
     * @see Hints#JTS_GEOMETRY_FACTORY
     * @see Hints#JTS_COORDINATE_SEQUENCE_FACTORY
     * @see Hints#JTS_PRECISION_MODEL
     * @see Hints#JTS_SRID
     */
    public final Set<RenderingHints.Key> getSupportedHints() {
        return supportedHints;
    }

    public ArcSdeVersionHandler getVersionHandler() throws IOException {
        return dataStore.getVersionHandler(typeInfo.getFeatureTypeName(), transaction);
    }

    public FeatureReader<SimpleFeatureType, SimpleFeature> getfeatureReader(
            SimpleFeatureType targetSchema, Query query) throws IOException {
        FeatureReader<SimpleFeatureType, SimpleFeature> featureReader;
        featureReader = dataStore.getFeatureReader(query, transaction, targetSchema);
        return featureReader;
    }
}
