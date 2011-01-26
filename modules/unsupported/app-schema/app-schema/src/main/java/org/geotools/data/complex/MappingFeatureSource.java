/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.DataAccess;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * A FeatureSource that uses a {@linkplain org.geotools.data.complex.FeatureTypeMapping} to perform
 * Feature fetching.
 * 
 * <p>
 * Note that the number of Features available from a MappingFeatureReader may not match the number
 * of features that resulted of executing the incoming query over the surrogate FeatureSource. This
 * will be the case when grouping attributes has configured on the FeatureTypeMapping this reader is
 * based on.
 * </p>
 * <p>
 * When a MappingFeatureReader is created, a delegated FeatureIterator will be created based on the
 * information provided by the FeatureTypeMapping object. That delegate reader will be specialized
 * in applying the appropiate mapping stratagy based on wether grouping has to be performed or not.
 * </p>
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 * @source $URL$
 * @since 2.4
 */
class MappingFeatureSource implements FeatureSource<FeatureType, Feature> {

    private AppSchemaDataAccess store;

    private FeatureTypeMapping mapping;

    public MappingFeatureSource(AppSchemaDataAccess store, FeatureTypeMapping mapping) {
        this.store = store;
        this.mapping = mapping;
    }

    public void addFeatureListener(FeatureListener listener) {
        throw new UnsupportedOperationException();
    }

    public ReferencedEnvelope getBounds() throws IOException {
        return store.getBounds(namedQuery(Filter.INCLUDE, Integer.MAX_VALUE));
    }

    private DefaultQuery namedQuery(Filter filter, int countLimit) {
        DefaultQuery query = new DefaultQuery();
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
        return query;
    }

    private DefaultQuery namedQuery(Query query) {
        DefaultQuery namedQuery = namedQuery(query.getFilter(), query.getMaxFeatures());
        namedQuery.setPropertyNames(query.getPropertyNames());
        namedQuery.setCoordinateSystem(query.getCoordinateSystem());
        namedQuery.setCoordinateSystemReproject(query.getCoordinateSystemReproject());
        namedQuery.setHandle(query.getHandle());
        namedQuery.setMaxFeatures(query.getMaxFeatures());
        namedQuery.setSortBy(query.getSortBy());
        return namedQuery;
    }

    public ReferencedEnvelope getBounds(Query query) throws IOException {
        DefaultQuery namedQuery = namedQuery(query);
        return store.getBounds(namedQuery);
    }

    public int getCount(Query query) throws IOException {
        DefaultQuery namedQuery = namedQuery(query);
        int count = store.getCount(namedQuery);
        if (count >= 0) {
            // normal case
            return count;
        } else {
            // count < 0 indicates broken a datastore, such as PropertyDataStore.
            // If the data store cannot count its own features, we have to do it.
            int featureCount = 0;
            FeatureIterator<Feature> features = null;
            try {
                for (features = getFeatures(namedQuery).features(); features.hasNext(); features
                        .next()) {
                    featureCount++;
                }
            } finally {
                if (features != null) {
                    features.close();
                }
            }
            return featureCount;
        }
    }

    public DataAccess<FeatureType, Feature> getDataStore() {
        return store;
    }

    public FeatureType getSchema() {
        return (FeatureType) mapping.getTargetFeature().getType();
    }
    
    public AttributeDescriptor getTargetFeature() {
        return mapping.getTargetFeature();
    }

    protected FeatureTypeMapping getMapping() {
        return mapping;
    }
    
    public FeatureCollection<FeatureType, Feature> getFeatures(Query query) throws IOException {
        return new MappingFeatureCollection(store, mapping, namedQuery(query));
    }

    public FeatureCollection<FeatureType, Feature> getFeatures(Filter filter) throws IOException {
        return new MappingFeatureCollection(store, mapping, namedQuery(filter, Integer.MAX_VALUE));
    }

    public FeatureCollection<FeatureType, Feature> getFeatures() throws IOException {
        return new MappingFeatureCollection(store, mapping, namedQuery(Filter.INCLUDE,
                Integer.MAX_VALUE));
    }

    public void removeFeatureListener(FeatureListener listener) {
        throw new UnsupportedOperationException("this is a read only feature source");
    }

    public ResourceInfo getInfo() {
        throw new UnsupportedOperationException();
    }

    public Name getName() {
        Name name = mapping.getTargetFeature().getName();
        return name;
    }

    /**
     * Not a supported operation.
     * 
     * @see org.geotools.data.FeatureSource#getSupportedHints()
     */
    public Set<RenderingHints.Key> getSupportedHints() {
        return Collections.emptySet();
    }

    /**
     * @see org.geotools.data.FeatureSource#getQueryCapabilities()
     */
    public QueryCapabilities getQueryCapabilities() {
        return new QueryCapabilities();
    }

}
