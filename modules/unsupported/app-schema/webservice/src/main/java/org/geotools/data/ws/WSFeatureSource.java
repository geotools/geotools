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
package org.geotools.data.ws;

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
import org.geotools.data.complex.xml.XmlFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Simple implementation of XmlFeatureSource
 * <p>
 * This implementation is really simple in the sense that it delegates all the hard work to the
 * {@link XmlDataStore} provided.
 * </p>
 * 
 * @author rpetty
 * @version $Id$
 *
 * @source $URL$
 *         http://svn.geotools.org/trunk/modules/unsupported/app-schema/webservice/src/main/java/org/geotools/wfs/v_1_1_0
 *         /data/XmlSimpleFeatureParser.java $
 */

public class WSFeatureSource implements XmlFeatureSource {

    private String typeName;
    
    private XmlDataStore dataStore;

    private QueryCapabilities queryCapabilities;

    
    public WSFeatureSource(final WS_DataStore dataStore, final String typeName, final Name name)
            throws IOException {
        this.typeName = typeName;
        this.dataStore = dataStore;
        this.queryCapabilities = new QueryCapabilities();
    }

    public Name getName() {
        return dataStore.getName();
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
        throw new UnsupportedOperationException("getSchema not supported");
    }

    public void setNamespaces(NamespaceSupport namespaces) {
        dataStore.setNamespaces(namespaces);
    }
    
    public void setItemXpath(String itemXpath) {
        dataStore.setItemXpath(itemXpath);
    } 
    /**
     * Returns available metadata for this resource
     * 
     * @return
     */
    public ResourceInfo getInfo() {
        throw new UnsupportedOperationException("getInfo not supported");
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
        throw new UnsupportedOperationException("getBounds not supported");
    }

    /**
     * @see FeatureSource#getBounds(Query)
     */
    public ReferencedEnvelope getBounds(Query query) throws IOException {        
        throw new UnsupportedOperationException("getBounds not supported");
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
    public WSFeatureCollection getFeatures(Filter filter) throws IOException {
        return getFeatures(new DefaultQuery(typeName, filter));
    }

    /**
     * @see FeatureSource#getFeatures()
     */
    public WSFeatureCollection getFeatures() throws IOException {
        return getFeatures(new DefaultQuery(typeName));
    }

    /**
     * @see FeatureSource#getFeatures(Query)
     */
    public WSFeatureCollection getFeatures(final Query query) throws IOException {
        Query namedQuery = namedQuery(typeName, query);
        return new WSFeatureCollection(dataStore, namedQuery);
    }

    /**
     * @see FeatureSource#getSupportedHints()
     */
    @SuppressWarnings("unchecked")
    public Set getSupportedHints() {
        return Collections.EMPTY_SET;
    }

    private Query namedQuery(final String typeName, final Query query) {
        String quertyTypeName = query.getTypeName();
        if (quertyTypeName != null && !quertyTypeName.equals(typeName)) {           
            throw new IllegalArgumentException("Wrong query type name: " + quertyTypeName
                    + ". Name should be " + typeName);            
        }
        DefaultQuery named = new DefaultQuery(query);
        named.setTypeName(typeName);
        return named;
    }

    public QueryCapabilities getQueryCapabilities() {
        return this.queryCapabilities;
    }
}
