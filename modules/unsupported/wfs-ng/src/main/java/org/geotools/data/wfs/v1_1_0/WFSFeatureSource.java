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

import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.wfs.protocol.wfs.WFSProtocol;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

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

public class WFSFeatureSource extends ContentFeatureSource {

    private String typeName;

    private WFSNGDataStore dataStore;

    private SimpleFeatureType featureType;

    private QueryCapabilities queryCapabilities;

    /*public WFSFeatureSource(final WFS_1_1_0_DataStore dataStore, final String typeName) throws IOException {
        this.typeName = typeName;
        this.dataStore = dataStore;
        this.queryCapabilities = new QueryCapabilities();
        this.featureType = dataStore.getSchema(typeName);
    }*/
    public WFSFeatureSource(ContentEntry entry,Query query) throws IOException {
        super(entry,query);
    }

    public Name getName() {
        return featureType.getName();
    }

    /**
     * @see FeatureSource#getDataStore()
     */
    /*public WFSDataStore getDataStore() {
        return dataStore;
    }
*/
    /**
     * @see FeatureSource#getSchema()
     */
    /*public SimpleFeatureType getSchema() {
        return featureType;
    }*/

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
   /* public void addFeatureListener(FeatureListener listener) {

    }*/

    /**
     * @see FeatureSource#removeFeatureListener(FeatureListener)
     */
   /* public void removeFeatureListener(FeatureListener listener) {
    }*/

    /**
     * @see FeatureSource#getBounds()
     */
/*    public ReferencedEnvelope getBounds() throws IOException {
        return getInfo().getBounds();
    }*/

    /**
     * @see FeatureSource#getBounds(Query)
     */
    /*public ReferencedEnvelope getBounds(Query query) throws IOException {
        Query namedQuery = namedQuery(typeName, query);
        ReferencedEnvelope bounds = dataStore.getBounds(namedQuery);
        return bounds;
    }*/

    /**
     * @see FeatureSource#getCount(Query)
     */
  /*  public int getCount(Query query) throws IOException {
        Query namedQuery = namedQuery(typeName, query);
        int count = dataStore.getCount(namedQuery);
        return count;
    }*/

    /**
     * @see FeatureSource#getFeatures(Filter)
     */
    /*public WFSFeatureCollection getFeatures(Filter filter) throws IOException {
        return getFeatures(new DefaultQuery(typeName, filter));
    }*/

    /**
     * @see FeatureSource#getFeatures()
     */
/*    public WFSFeatureCollection getFeatures() throws IOException {
        return getFeatures(new DefaultQuery(typeName));
    }*/

    /**
     * @see FeatureSource#getFeatures(Query)
     */
/*    public WFSFeatureCollection getFeatures(final Query query) throws IOException {
        Query namedQuery = namedQuery(typeName, query);
        return new WFSFeatureCollection(dataStore, namedQuery);
    }*/

    /**
     * @see FeatureSource#getSupportedHints()
     */
/*    @SuppressWarnings("unchecked")
    public Set getSupportedHints() {
        return Collections.EMPTY_SET;
    }*/

  /* private Query namedQuery(final String typeName, final Query query) {
        if (query.getTypeName() != null && !query.getTypeName().equals(typeName)) {
            throw new IllegalArgumentException("Wrong query type name: " + query.getTypeName()
                    + ". It should be " + typeName);
        }
        DefaultQuery named = new DefaultQuery(query);
        named.setTypeName(typeName);
        return named;
    }*/

    public QueryCapabilities getQueryCapabilities() {
        return this.queryCapabilities;
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
        
    }
 
}
