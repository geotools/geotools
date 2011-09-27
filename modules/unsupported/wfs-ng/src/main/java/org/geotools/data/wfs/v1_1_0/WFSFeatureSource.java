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
import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;

import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.wfs.protocol.wfs.WFSProtocol;
import org.geotools.data.wfs.v1_1_0.parsers.EmfAppSchemaParser;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 *
 * @source $URL$
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
        WFSNGDataStore datastore=(WFSNGDataStore) getDataStore();
        WFSProtocol wfs= datastore.getWFSProtocol();

        //make the prefixedTypeName
        String prefixedTypeName=null;
        Set<QName> names=wfs.getFeatureTypeNames();
        Iterator<QName> it=names.iterator();
        while(it.hasNext()&& prefixedTypeName==null){
            QName name=it.next();
            if (entry.getName().getLocalPart().equals(name.getLocalPart()) &&
                    entry.getName().getNamespaceURI().equals(name.getNamespaceURI())){
                prefixedTypeName=name.getPrefix()+entry.getName().getSeparator()+name.getLocalPart();
            }
        }
        /*Parser not working (yet)
        WFSResponse response;
        String outputFormat=wfs.getDefaultOutputFormat(WFSOperationType.GET_FEATURE);
        if(datastore.isPreferPostOverGet()){
            response = wfs.describeFeatureTypePOST(getCapTypeName, outputFormat);
        }else{
            response = wfs.describeFeatureTypeGET(getCapTypeName, outputFormat);
        }
        Object result = WFSExtensions.process(datastore, response);
        */
        final SimpleFeatureType fType;
        //CoordinateReferenceSystem crs = getFeatureTypeCRS(prefixedTypeName);
        CoordinateReferenceSystem crs;
        try {
            crs = CRS.decode(wfs.getDefaultCRS(prefixedTypeName));
        } catch (NoSuchAuthorityCodeException ex) {
            IOException exception = new IOException("can't decode default CRS for featureType "+prefixedTypeName);
            exception.initCause(ex);
            throw exception;
        } catch (FactoryException ex) {
            IOException exception = new IOException("can't decode default CRS for featureType "+prefixedTypeName);
            exception.initCause(ex);
            throw exception;
        }
        final URL describeUrl = wfs.getDescribeFeatureTypeURLGet(prefixedTypeName);
        QName featureDescriptorName = wfs.getFeatureTypeName(prefixedTypeName);
        fType = EmfAppSchemaParser.parseSimpleFeatureType(featureDescriptorName,
                describeUrl, crs);
        
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        //tb.setFeatureTypeFactory( getDataStore().getFeatureTypeFactory() );

        tb.init(fType);
        //set name
        tb.setName(entry.getName());
        GeometryDescriptor defaultGeometry = fType.getGeometryDescriptor();
        if (defaultGeometry != null) {
            tb.setDefaultGeometry(defaultGeometry.getLocalName());
            tb.setCRS(defaultGeometry.getCoordinateReferenceSystem());
        }
        try{
            featureType = tb.buildFeatureType();
        }catch(Exception e){
            e.printStackTrace();
        }
        return featureType;    
    }
 
}
