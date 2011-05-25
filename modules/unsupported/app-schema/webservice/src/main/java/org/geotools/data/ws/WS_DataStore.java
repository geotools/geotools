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
package org.geotools.data.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataAccess;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.complex.xml.XmlResponse;
import org.geotools.data.complex.xml.XmlXpathFilterData;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.view.DefaultView;
import org.geotools.data.ws.protocol.ws.WSProtocol;
import org.geotools.data.ws.protocol.ws.WSResponse;
import org.geotools.feature.SchemaException;
import org.geotools.util.XmlXpathUtilites;
import org.geotools.util.logging.Logging;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * An implementation of a DS that uses XML over HTTP.
 * <p>
 * Unlike normal DataStores that return features, this returns xml.
 * </p>
 * 
 * @author Russell Petty
 *
 *
 * @source $URL$
 * @version $Id$ 
 */
public final class WS_DataStore implements XmlDataStore {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.ws");

    private static final XMLOutputter out = new XMLOutputter(Format.getPrettyFormat()); 
    private static final SAXBuilder sax = new SAXBuilder();
    
    private final WSProtocol wsProtocol;
    
    //The hard limit is the maximum number of features we are allowed to call. 
    //If not set it has the value of zero, which means unlimited.
    private int maxFeaturesHardLimit;

    private Name name;
    
    private NamespaceSupport namespaces;
    
    private String itemXpath;
    /**
     * The WFS capabilities document.
     * 
     * @param capabilities
     */
    public WS_DataStore(final WSProtocol wsProtocol) {
        if (wsProtocol == null) {
            throw new NullPointerException("ws protocol");
        }
        this.wsProtocol = wsProtocol;
    }

    /**
     * @see XmlDataStore#setMaxFeatures(Integer)
     */
    public void setMaxFeatures(int maxFeatures) {
        this.maxFeaturesHardLimit = maxFeatures;
    }

    /**
     * @see XmlDataStore#getMaxFeatures()
     */
    public int getMaxFeatures() {
        return this.maxFeaturesHardLimit;
    }

    /**
     * @see XmlDataStore#getInfo()
     */
    public ServiceInfo getInfo() {
        throw new UnsupportedOperationException("DS not supported!");
    }

    public SimpleFeatureType getSchema(final String prefixedTypeName) throws IOException {
        throw new UnsupportedOperationException("DS not supported!");
    }

    /**
     * @see DataAccess#getSchema(Name)
     * @see #getSchema(String)
     */
    public SimpleFeatureType getSchema(Name name) throws IOException {
        throw new UnsupportedOperationException("DS not supported!");
    }

    /**
     * @see DataAccess#getNames()
     */
    public List<Name> getNames() throws IOException {
        throw new UnsupportedOperationException("DS not supported!");
    }

    /**
     * @see org.geotools.data.DataStore#getTypeNames()
     */
    public String[] getTypeNames() throws IOException {
        throw new UnsupportedOperationException("DS not supported!");
    }

    /**
     * @see org.geotools.data.DataStore#dispose()
     */
    public void dispose() {
        // do nothing
    }

    /**
     * @see org.geotools.data.DataStore#getFeatureReader(org.geotools.data.Query,
     *      org.geotools.data.Transaction)
     */
    public XmlResponse getXmlReader(Query query) throws IOException {

        if (Filter.EXCLUDE.equals(query.getFilter())) {
            return null; //empty response
        }

        Query callQuery = new DefaultQuery(query);

        Filter[] filters = wsProtocol.splitFilters(query.getFilter());
        Filter supportedFilter = filters[0];
        Filter postFilter = filters[1];
        LOGGER.fine("Supported filter:  " + supportedFilter);        
        LOGGER.fine("Unupported filter: " + postFilter);
        ((DefaultQuery) callQuery).setFilter(supportedFilter);
        int maxFeatures = getMaxFeatures(query);
        
        //Only set maxFeatures if the back end can handle the filter.
        //If it cannot, we don't want to limit the number of responses 
        //before filtering on the server. However a hard limit may have been specified.        
        if (Filter.INCLUDE.equals(postFilter)) {
            ((DefaultQuery) callQuery).setMaxFeatures(maxFeatures);
        } else {
            ((DefaultQuery) callQuery).setMaxFeatures(maxFeaturesHardLimit);
        }

        WSResponse response = wsProtocol.issueGetFeature(callQuery);
        Document doc = getXmlResponse(response); 
        
        List<Integer> validFeatureIndex = determineValidFeatures(postFilter, doc, maxFeatures);
        return new XmlResponse(doc, validFeatureIndex);
    }

    private List<Integer> determineValidFeatures(Filter postFilter, Document doc, int maxFeatures) {
        int nodeCount = XmlXpathUtilites.countXPathNodes(namespaces, itemXpath, doc);       
        
        List<Integer> validFeatureIndex = null;
        
        if(Filter.INCLUDE.equals(postFilter)) {
            validFeatureIndex = new ArrayList<Integer>(nodeCount);            
            //add all features to index--but no more than specified by maxFeatures.
            int maxNode = nodeCount < maxFeatures ? nodeCount : maxFeatures;
            for(int i = 1; i <= maxNode; i++) { 
                validFeatureIndex.add(i);
            }
        } else {
            validFeatureIndex = new ArrayList<Integer>(); 
            int nodeIndex = 1;
            while (nodeIndex <= nodeCount && validFeatureIndex.size() <= maxFeatures) {       
                XmlXpathFilterData peek = new XmlXpathFilterData(namespaces, doc, nodeIndex, itemXpath);
                if (postFilter.evaluate(peek)) {
                   validFeatureIndex.add(nodeIndex);
                } 
                nodeIndex++;
            }
        }
        return validFeatureIndex;
    }

    private Document getXmlResponse(WSResponse response) throws IOException {
        Document doc = null;
        try {
            doc = sax.build(response.getInputStream());             
        } catch (JDOMException e1) {
            throw new RuntimeException("error reading xml from http", e1);
        }     
        
        if(LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer(out.outputString(doc));
        }
        return doc;
    }

    /**
     * @see org.geotools.data.DataStore#getFeatureReader(org.geotools.data.Query,
     *      org.geotools.data.Transaction)
     */
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query,
            final Transaction transaction) throws IOException {
          throw new UnsupportedOperationException("DS not supported!");  
    }

    /**
     * @see org.geotools.data.DataStore#getFeatureSource(java.lang.String)
     */
    public WSFeatureSource getFeatureSource(final String typeName) throws IOException {
        return new WSFeatureSource(this, typeName, name);
    }

    /**
     * @return {@code null}, no lock support so far
     * @see org.geotools.data.DataStore#getLockingManager()
     */
    public LockingManager getLockingManager() {
        throw new UnsupportedOperationException("DS not supported!");
    }

    /**
     * Not supported.
     * 
     * @see org.geotools.data.DataStore#getFeatureWriter(java.lang.String,
     *      org.opengis.filter.Filter, org.geotools.data.Transaction)
     * @throws UnsupportedOperationException
     *             always since this operation does not apply to a WFS backend
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
            Filter filter, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException("This is a read only DataStore");
    }

    /**
     * Not supported.
     * 
     * @see org.geotools.data.DataStore#getFeatureWriter(java.lang.String,
     *      org.geotools.data.Transaction)
     * @throws UnsupportedOperationException
     *             always since this operation does not apply to a WFS backend
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
            Transaction transaction) throws IOException {
        throw new UnsupportedOperationException("This is a read only DataStore");
    }

    /**
     * Not supported.
     * 
     * @see org.geotools.data.DataStore#getFeatureWriterAppend(java.lang.String,
     *      org.geotools.data.Transaction)
     * @throws UnsupportedOperationException
     *             always since this operation does not apply to a WFS backend
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName,
            Transaction transaction) throws IOException {
        throw new UnsupportedOperationException("This is a read only DataStore");
    }

    public SimpleFeatureSource getFeatureSource(Name typeName)
            throws IOException {
        // this is a hack as this datastore only returns one type of response.
        //set the name to what is passed in as it maybe needed later.        
        this.name = typeName;      
        return getFeatureSource(typeName.getLocalPart());
    }

    /**
     * @see DataAccess#updateSchema(Name, org.opengis.feature.type.FeatureType)
     * @throws UnsupportedOperationException
     *             always since this operation does not apply to a WFS backend
     */
    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("WS does not support update schema");
    }

    /**
     * @see org.geotools.data.DataStore#updateSchema(java.lang.String,
     *      org.opengis.feature.simple.SimpleFeatureType)
     * @throws UnsupportedOperationException
     *             always since this operation does not apply to a WFS backend
     */
    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("WS does not support update schema");
    }

    /**
     * @see org.geotools.data.DataStore#createSchema(org.opengis.feature.simple.SimpleFeatureType)
     * @throws UnsupportedOperationException
     *             always since this operation does not apply to a WFS backend
     */
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("WS DataStore does not support createSchema");
    }

    /**
     * @param query
     * @return the number of features returned by a GetFeature?resultType=hits request, or {@code
     *         -1} if not supported
     */
    public int getCount(final Query query) {
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("WSDataStore[");
        sb.append(", max features=").append(
                maxFeaturesHardLimit == 0 ? "not set" : String
                        .valueOf(maxFeaturesHardLimit));
        sb.append("]");
        return sb.toString();
    }

    protected int getMaxFeatures(Query query) {
        int maxFeaturesDataStoreLimit = getMaxFeatures();
        int queryMaxFeatures = query.getMaxFeatures();
        int maxFeatures = Query.DEFAULT_MAX;
        if (Query.DEFAULT_MAX != queryMaxFeatures) {
            maxFeatures = queryMaxFeatures;
        }
        if (maxFeaturesDataStoreLimit > 0) {
            maxFeatures = Math.min(maxFeaturesDataStoreLimit, maxFeatures);
        }
        return maxFeatures;
    }

    public Name getName() {
        return name;
    }
  
    public void setNamespaces(org.xml.sax.helpers.NamespaceSupport namespaces) {
        this.namespaces = namespaces;
    }

    public void setItemXpath(String itemXpath) {
        this.itemXpath = itemXpath;
    }
    
}
