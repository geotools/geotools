/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import java.awt.RenderingHints;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Set;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.FeatureListener;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.data.DataUtilities;
import org.geotools.data.joining.JoiningQuery;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCFeatureSource;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.util.factory.Hints;

/**
 * A FeatureSource that uses a {@linkplain org.geotools.data.complex.FeatureTypeMapping} to perform Feature fetching.
 *
 * <p>Note that the number of Features available from a MappingFeatureReader may not match the number of features that
 * resulted of executing the incoming query over the surrogate FeatureSource. This will be the case when grouping
 * attributes has configured on the FeatureTypeMapping this reader is based on.
 *
 * <p>When a MappingFeatureReader is created, a delegated FeatureIterator will be created based on the information
 * provided by the FeatureTypeMapping object. That delegate reader will be specialized in applying the appropiate
 * mapping stratagy based on wether grouping has to be performed or not.
 *
 * @author Gabriel Roldan (Axios Engineering)
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @version $Id$
 * @since 2.4
 */
public class MappingFeatureSource implements FeatureSource<FeatureType, Feature> {

    private AppSchemaDataAccess store;

    private FeatureTypeMapping mapping;

    public MappingFeatureSource(AppSchemaDataAccess store, FeatureTypeMapping mapping) {
        this.store = store;
        this.mapping = mapping;
    }

    @Override
    public void addFeatureListener(FeatureListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReferencedEnvelope getBounds() throws IOException {
        return store.getBounds(namedQuery(Filter.INCLUDE, Integer.MAX_VALUE));
    }

    private Query namedQuery(Filter filter, int countLimit) {
        return namedQuery(filter, countLimit, false);
    }

    private Query namedQuery(Filter filter, int countLimit, Hints hints) {
        return namedQuery(filter, countLimit, false, hints);
    }

    private Query namedQuery(Filter filter, int countLimit, boolean isJoining) {
        return namedQuery(filter, countLimit, isJoining, null);
    }

    private Query namedQuery(Filter filter, int countLimit, boolean isJoining, Hints hints) {
        Query query = isJoining ? new JoiningQuery() : new Query();
        if (getName().getNamespaceURI() != null) {
            try {
                query.setNamespace(new URI(getName().getNamespaceURI()));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        query.setTypeName(getName().getLocalPart());
        query.setFilter(filter);
        query.setMaxFeatures(countLimit);
        query.setHints(hints);
        return query;
    }

    private Query namedQuery(Query query) {
        Query namedQuery = namedQuery(query.getFilter(), query.getMaxFeatures(), query instanceof JoiningQuery);
        namedQuery.setProperties(query.getProperties());
        namedQuery.setCoordinateSystem(query.getCoordinateSystem());
        namedQuery.setCoordinateSystemReproject(query.getCoordinateSystemReproject());
        namedQuery.setHandle(query.getHandle());
        namedQuery.setMaxFeatures(query.getMaxFeatures());
        namedQuery.setStartIndex(query.getStartIndex());
        namedQuery.setSortBy(query.getSortBy());
        namedQuery.setHints(query.getHints());
        if (query instanceof JoiningQuery) {
            ((JoiningQuery) namedQuery).setQueryJoins(((JoiningQuery) query).getQueryJoins());
            ((JoiningQuery) namedQuery).setRootMapping(((JoiningQuery) query).getRootMapping());
        }
        return namedQuery;
    }

    @Override
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        Query namedQuery = namedQuery(query);
        return store.getBounds(namedQuery);
    }

    @Override
    public int getCount(Query query) throws IOException {
        int count = 0;
        Query namedQuery = namedQuery(query);
        FeatureSource mappedSource = mapping.getSource();
        if (!(mappedSource instanceof JDBCFeatureSource || mappedSource instanceof JDBCFeatureStore)) {
            count = store.getCount(namedQuery);
        }
        if (count >= 0) {
            // normal case
            return count;
        } else {
            // count < 0 indicates broken a datastore, such as PropertyDataStore.
            // If the data store cannot count its own features, we have to do it.
            return DataUtilities.count(getFeatures(namedQuery).features());
        }
    }

    @Override
    public DataAccess<FeatureType, Feature> getDataStore() {
        return store;
    }

    @Override
    public FeatureType getSchema() {
        return (FeatureType) mapping.getTargetFeature().getType();
    }

    public AttributeDescriptor getTargetFeature() {
        return mapping.getTargetFeature();
    }

    public FeatureTypeMapping getMapping() {
        return mapping;
    }

    @Override
    public FeatureCollection<FeatureType, Feature> getFeatures(Query query) throws IOException {
        return new MappingFeatureCollection(store, mapping, namedQuery(query));
    }

    @Override
    public FeatureCollection<FeatureType, Feature> getFeatures(Filter filter) throws IOException {
        return new MappingFeatureCollection(store, mapping, namedQuery(filter, Integer.MAX_VALUE));
    }

    public FeatureCollection<FeatureType, Feature> getFeatures(Filter filter, Hints hints) throws IOException {
        return new MappingFeatureCollection(store, mapping, namedQuery(filter, Integer.MAX_VALUE, hints));
    }

    @Override
    public FeatureCollection<FeatureType, Feature> getFeatures() throws IOException {
        return new MappingFeatureCollection(store, mapping, namedQuery(Filter.INCLUDE, Integer.MAX_VALUE));
    }

    @Override
    public void removeFeatureListener(FeatureListener listener) {
        throw new UnsupportedOperationException("this is a read only feature source");
    }

    @Override
    public ResourceInfo getInfo() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Name getName() {
        Name name = mapping.getTargetFeature().getName();
        return name;
    }

    /**
     * Not a supported operation.
     *
     * @see FeatureSource#getSupportedHints()
     */
    @Override
    public Set<RenderingHints.Key> getSupportedHints() {
        return Collections.emptySet();
    }

    /** @see FeatureSource#getQueryCapabilities() */
    @Override
    public QueryCapabilities getQueryCapabilities() {
        return mapping.getSource().getQueryCapabilities();
    }
}
