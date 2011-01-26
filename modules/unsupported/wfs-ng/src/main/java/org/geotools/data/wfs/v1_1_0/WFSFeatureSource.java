/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_1_0;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * Simple implementation of FeatureSource for a WFS 1.1 server.
 * <p>
 * This implementation is really simple in the sense that it delegates all the hard work to the
 * {@link WFSDataStore} provided.
 * </p>
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id: WFSFeatureSource.java 35310 2010-04-30 10:32:15Z jive $
 * @since 2.5.x
 * @source $URL:
 *         http://svn.geotools.org/trunk/modules/plugin/wfs/src/main/java/org/geotools/wfs/v_1_1_0
 *         /data/XmlSimpleFeatureParser.java $
 */

public class WFSFeatureSource implements SimpleFeatureSource {

    private String typeName;

    private WFS_1_1_0_DataStore dataStore;

    private SimpleFeatureType featureType;

    private QueryCapabilities queryCapabilities;

    public WFSFeatureSource(final WFS_1_1_0_DataStore dataStore, final String typeName)
            throws IOException {
        this.typeName = typeName;
        this.dataStore = dataStore;
        this.queryCapabilities = new QueryCapabilities();
        this.featureType = dataStore.getSchema(typeName);
    }

    public Name getName() {
        return featureType.getName();
    }

    /**
     * @see FeatureSource#getDataStore()
     */
    public DataStore getDataStore() {
        return dataStore;
    }

    /**
     * @see FeatureSource#getSchema()
     */
    public SimpleFeatureType getSchema() {
        return featureType;
    }

    /**
     * Returns available metadata for this resource
     * 
     * @return
     */
    public ResourceInfo getInfo() {
        return new CapabilitiesResourceInfo(typeName, dataStore);
    }

    /**
     * @see FeatureSource#addFeatureListener(FeatureListener)
     */
    public void addFeatureListener(FeatureListener listener) {

    }

    /**
     * @see FeatureSource#removeFeatureListener(FeatureListener)
     */
    public void removeFeatureListener(FeatureListener listener) {
    }

    /**
     * @see FeatureSource#getBounds()
     */
    public ReferencedEnvelope getBounds() throws IOException {
        return getInfo().getBounds();
    }

    /**
     * @see FeatureSource#getBounds(Query)
     */
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        Query namedQuery = namedQuery(typeName, query);
        ReferencedEnvelope bounds = dataStore.getBounds(namedQuery);
        return bounds;
    }

    /**
     * @see FeatureSource#getCount(Query)
     */
    public int getCount(Query query) throws IOException {
        Query namedQuery = namedQuery(typeName, query);
        int count = dataStore.getCount(namedQuery);
        return count;
    }

    /**
     * @see FeatureSource#getFeatures(Filter)
     */
    public WFSFeatureCollection getFeatures(Filter filter) throws IOException {
        return getFeatures(new DefaultQuery(typeName, filter));
    }

    /**
     * @see FeatureSource#getFeatures()
     */
    public WFSFeatureCollection getFeatures() throws IOException {
        return getFeatures(new DefaultQuery(typeName));
    }

    /**
     * @see FeatureSource#getFeatures(Query)
     */
    public WFSFeatureCollection getFeatures(final Query query) throws IOException {
        Query namedQuery = namedQuery(typeName, query);
        return new WFSFeatureCollection(dataStore, namedQuery);
    }

    /**
     * @see FeatureSource#getSupportedHints()
     */
    @SuppressWarnings("unchecked")
    public Set getSupportedHints() {
        return Collections.EMPTY_SET;
    }

    private Query namedQuery(final String typeName, final Query query) {
        if (query.getTypeName() != null && !query.getTypeName().equals(typeName)) {
            throw new IllegalArgumentException("Wrong query type name: " + query.getTypeName()
                    + ". It should be " + typeName);
        }
        DefaultQuery named = new DefaultQuery(query);
        named.setTypeName(typeName);
        return named;
    }

    public QueryCapabilities getQueryCapabilities() {
        return this.queryCapabilities;
    }
}
