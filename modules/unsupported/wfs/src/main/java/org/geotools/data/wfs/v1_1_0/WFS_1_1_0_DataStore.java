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
package org.geotools.data.wfs.v1_1_0;

import static org.geotools.data.wfs.protocol.wfs.WFSOperationType.GET_CAPABILITIES;
import static org.geotools.data.wfs.protocol.wfs.WFSOperationType.GET_FEATURE;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.geotools.data.DataAccess;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.LockingManager;
import org.geotools.data.MaxFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.SchemaNotFoundException;
import org.geotools.data.Transaction;
import org.geotools.data.crs.ReprojectFeatureReader;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.view.DefaultView;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSServiceInfo;
import org.geotools.data.wfs.protocol.wfs.GetFeature;
import org.geotools.data.wfs.protocol.wfs.GetFeatureParser;
import org.geotools.data.wfs.protocol.wfs.WFSException;
import org.geotools.data.wfs.protocol.wfs.WFSExtensions;
import org.geotools.data.wfs.protocol.wfs.WFSOperationType;
import org.geotools.data.wfs.protocol.wfs.WFSProtocol;
import org.geotools.data.wfs.protocol.wfs.WFSResponse;
import org.geotools.data.wfs.protocol.wfs.GetFeature.ResultType;
import org.geotools.data.wfs.v1_1_0.parsers.EmfAppSchemaParser;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

/**
 * A WFS 1.1 DataStore implementation.
 * <p>
 * Note with the current design, this class is meant to be pulled up as the single WFS DataStore
 * implementation regardless of the WFS version, since the protocol version specifics is meant to be
 * handled by the {@link WFSProtocol} implementation provided to this class. For the time being,
 * while there are no resources to spend on porting the WFS 1.0.0 datastore to the new design, this
 * keeps here in this 1.1 specific package.
 * </p>
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.5.x
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/wfs/src/main/java/org/geotools
 *         /wfs/v_1_1_0/data/WFSDataStore.java $
 */
@SuppressWarnings( { "nls" })
public final class WFS_1_1_0_DataStore implements WFSDataStore {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.wfs");

    /**
     * Whether to use POST as default HTTP method is not explicitly set
     */
    private static final boolean DEFAULT_HTTP_METHOD = true;

    private final WFSProtocol wfs;

    private Map<String, SimpleFeatureType> byTypeNameTypes;

    private Integer maxFeaturesHardLimit;

    private boolean preferPostOverGet = false;

    /**
     * The WFS capabilities document.
     * 
     * @param capabilities
     */
    public WFS_1_1_0_DataStore(final WFSProtocol wfs) {
        if (wfs == null) {
            throw new NullPointerException("wfs protocol");
        }
        this.wfs = wfs;
        byTypeNameTypes = Collections.synchronizedMap(new HashMap<String, SimpleFeatureType>());
        maxFeaturesHardLimit = Integer.valueOf(0); // not set
    }

    /**
     * @see WFSDataStore#setMaxFeatures(Integer)
     */
    public void setMaxFeatures(Integer maxFeatures) {
        this.maxFeaturesHardLimit = Integer.valueOf(maxFeatures.intValue());
    }

    /**
     * @see WFSDataStore#getMaxFeatures()
     */
    public Integer getMaxFeatures() {
        return this.maxFeaturesHardLimit;
    }

    /**
     * @see WFSDataStore#isPreferPostOverGet()
     */
    public boolean isPreferPostOverGet() {
        return preferPostOverGet;
    }

    /**
     * @see WFSDataStore#setPreferPostOverGet(boolean)
     */
    public void setPreferPostOverGet(Boolean booleanValue) {
        this.preferPostOverGet = booleanValue == null ? DEFAULT_HTTP_METHOD : booleanValue
                .booleanValue();
    }

    /**
     * @see WFSDataStore#getInfo()
     */
    public WFSServiceInfo getInfo() {
        return new CapabilitiesServiceInfo(this);
    }

    /**
     * Makes a {@code DescribeFeatureType} request for {@code typeName} feature type, parses the
     * server response into a {@link SimpleFeatureType} and returns it.
     * <p>
     * Due to a current limitation widely spread through the GeoTools library, the parsed
     * FeatureType will be adapted to share the same name than the Features produced for it. For
     * example, if the actual feature type name is {@code Streams_Type} and the features name (i.e.
     * which is the FeatureType name as stated in the WFS capabilities document) is {@code Stream},
     * the returned feature type name will also be {@code Stream}.
     * </p>
     * 
     * @param prefixedTypeName
     *            the type name as stated in the WFS capabilities document
     * @return the GeoTools FeatureType for the {@code typeName} as stated on the capabilities
     *         document.
     * @see org.geotools.data.DataStore#getSchema(java.lang.String)
     */
    public SimpleFeatureType getSchema(final String prefixedTypeName) throws IOException {
        SimpleFeatureType ftype = byTypeNameTypes.get(prefixedTypeName);
        if (ftype == null) {
            // String outputFormat = DEFAULT_OUTPUT_FORMAT;
            // WFSResponse response;
            // if (useHttpPostFor(DESCRIBE_FEATURETYPE)) {
            // response = wfs.describeFeatureTypePOST(prefixedTypeName, outputFormat);
            // } else {
            // response = wfs.describeFeatureTypeGET(prefixedTypeName, outputFormat);
            // }
            //
            // WFSResponseParser parser = WFSExtensions.findParser(response);

            final QName featureDescriptorName;
            try {
                featureDescriptorName = wfs.getFeatureTypeName(prefixedTypeName);
            } catch (IllegalArgumentException e) {
                throw new SchemaNotFoundException(prefixedTypeName);
            }

            final URL describeUrl = wfs.getDescribeFeatureTypeURLGet(prefixedTypeName);
            // @TODO remove this
            System.err.println("DecribeFT URL for " + prefixedTypeName + ": " + describeUrl);

            final SimpleFeatureType featureType;
            CoordinateReferenceSystem crs = getFeatureTypeCRS(prefixedTypeName);
            featureType = EmfAppSchemaParser.parseSimpleFeatureType(featureDescriptorName,
                    describeUrl, crs);

            // adapt the feature type name
            SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
            builder.init(featureType);
            builder.setName(prefixedTypeName);
            builder.setNamespaceURI(featureDescriptorName.getNamespaceURI());
            GeometryDescriptor defaultGeometry = featureType.getGeometryDescriptor();
            if (defaultGeometry != null) {
                builder.setDefaultGeometry(defaultGeometry.getLocalName());
                builder.setCRS(defaultGeometry.getCoordinateReferenceSystem());
            }
            final SimpleFeatureType adaptedFeatureType = builder.buildFeatureType();
            ftype = adaptedFeatureType;
            byTypeNameTypes.put(prefixedTypeName, ftype);
        }
        return ftype;
    }

    /**
     * @see DataAccess#getSchema(Name)
     * @see #getSchema(String)
     */
    public SimpleFeatureType getSchema(Name name) throws IOException {
        Set<QName> featureTypeNames = wfs.getFeatureTypeNames();

        String namespaceURI;
        String localPart;
        for (QName qname : featureTypeNames) {
            namespaceURI = name.getNamespaceURI();
            localPart = name.getLocalPart();
            if (namespaceURI.equals(qname.getNamespaceURI())
                    && localPart.equals(qname.getLocalPart())) {
                String prefixedName = qname.getPrefix() + ":" + localPart;
                return getSchema(prefixedName);
            }
        }
        throw new SchemaNotFoundException(name.getURI());
    }

    /**
     * @see DataAccess#getNames()
     */
    public List<Name> getNames() throws IOException {
        Set<QName> featureTypeNames = wfs.getFeatureTypeNames();
        List<Name> names = new ArrayList<Name>(featureTypeNames.size());
        String namespaceURI;
        String localPart;
        for (QName name : featureTypeNames) {
            namespaceURI = name.getNamespaceURI();
            localPart = name.getLocalPart();
            names.add(new NameImpl(namespaceURI, localPart));
        }
        return names;
    }

    /**
     * @see org.geotools.data.DataStore#getTypeNames()
     */
    public String[] getTypeNames() throws IOException {
        Set<QName> featureTypeNames = wfs.getFeatureTypeNames();
        List<String> sorted = new ArrayList<String>(featureTypeNames.size());
        for (QName name : featureTypeNames) {
            sorted.add(name.getPrefix() + ":" + name.getLocalPart());
        }
        Collections.sort(sorted);
        return sorted.toArray(new String[sorted.size()]);
    }

    /**
     * @see org.geotools.data.DataStore#dispose()
     */
    public void dispose() {
        wfs.dispose();
    }

    private WFSResponse executeGetFeatures(final Query query, final Transaction transaction,
            final ResultType resultType) throws IOException {
        // TODO: handle output format preferences
        final String outputFormat = wfs.getDefaultOutputFormat(GET_FEATURE);

        String srsName = adaptQueryForSupportedCrs((DefaultQuery) query);

        GetFeature request = new GetFeatureQueryAdapter(query, outputFormat, srsName, resultType);

        final WFSResponse response = sendGetFeatures(request);
        return response;
    }

    /**
     * @see org.geotools.data.DataStore#getFeatureReader(org.geotools.data.Query,
     *      org.geotools.data.Transaction)
     */
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query,
            final Transaction transaction) throws IOException {

        if (Filter.EXCLUDE.equals(query.getFilter())) {
            return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(getQueryType(query));
        }

        query = new DefaultQuery(query);
        Filter[] filters = wfs.splitFilters(query.getFilter());
        Filter supportedFilter = filters[0];
        Filter postFilter = filters[1];
        System.out.println("Supported filter:  " + supportedFilter);
        System.out.println("Unupported filter: " + postFilter);
        ((DefaultQuery) query).setFilter(supportedFilter);
        ((DefaultQuery) query).setMaxFeatures(getMaxFeatures(query));

        final CoordinateReferenceSystem queryCrs = query.getCoordinateSystem();

        WFSResponse response = executeGetFeatures(query, transaction, ResultType.RESULTS);

        Object result = WFSExtensions.process(this, response);

        GetFeatureParser parser;
        if (result instanceof WFSException) {
            // try to recover from common server implementation errors
            throw (WFSException) result;
        } else if (result instanceof GetFeatureParser) {
            parser = (GetFeatureParser) result;
        } else {
            throw new IllegalStateException("Unknown response result for GetFeature: " + result);
        }

        final SimpleFeatureType contentType = getQueryType(query);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        reader = new WFSFeatureReader((GetFeatureParser) parser);

        if (!reader.hasNext()) {
            return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(contentType);
        }

        final SimpleFeatureType readerType = reader.getFeatureType();

        CoordinateReferenceSystem readerCrs = readerType.getCoordinateReferenceSystem();
        if (queryCrs != null && !queryCrs.equals(readerCrs)) {
            try {
                reader = new ReprojectFeatureReader(reader, queryCrs);
            } catch (Exception e) {
                throw new DataSourceException(e);
            }
        }

        if (Filter.INCLUDE != postFilter) {
            reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(reader, postFilter);
        }

        if (!contentType.equals(readerType)) {
            final boolean cloneContents = false;
            reader = new ReTypeFeatureReader(reader, contentType, cloneContents);
        }

        if (this.maxFeaturesHardLimit.intValue() > 0 || query.getMaxFeatures() != Integer.MAX_VALUE) {
            int maxFeatures = maxFeaturesHardLimit.intValue() > 0 ? Math.min(maxFeaturesHardLimit.intValue(), query.getMaxFeatures()) : query.getMaxFeatures();
            reader = new MaxFeatureReader<SimpleFeatureType, SimpleFeature>(reader, maxFeatures);
        }
        return reader;
    }

    /**
     * Sends the GetFeature request using the appropriate HTTP method depending on the
     * {@link #isPreferPostOverGet()} preference and what the server supports.
     * 
     * @param request
     *            the request to send
     * @param map
     * @return the server response handle
     * @throws IOException
     *             if a communication error occurs. If a server returns an exception report that's a
     *             normal response, no exception will be thrown here.
     */
    private WFSResponse sendGetFeatures(GetFeature request) throws IOException {
        final WFSResponse response;
        if (useHttpPostFor(GET_FEATURE)) {
            response = wfs.issueGetFeaturePOST(request);
        } else {
            response = wfs.issueGetFeatureGET(request);
        }
        return response;
    }

    /**
     * Returns the feature type that shall result of issueing the given request, adapting the
     * original feature type for the request's type name in terms of the query CRS and requested
     * attributes.
     * 
     * @param query
     * @return
     * @throws IOException
     */
    SimpleFeatureType getQueryType(final Query query) throws IOException {
        final String typeName = query.getTypeName();
        final SimpleFeatureType featureType = getSchema(typeName);
        final CoordinateReferenceSystem coordinateSystemReproject = query
                .getCoordinateSystemReproject();

        String[] propertyNames = query.getPropertyNames();

        SimpleFeatureType queryType = featureType;
        if (propertyNames != null && propertyNames.length > 0) {
            try {
                queryType = DataUtilities.createSubType(queryType, propertyNames);
            } catch (SchemaException e) {
                throw new DataSourceException(e);
            }
        } else {
            propertyNames = DataUtilities.attributeNames(featureType);
        }

        if (coordinateSystemReproject != null) {
            try {
                queryType = DataUtilities.createSubType(queryType, propertyNames,
                        coordinateSystemReproject);
            } catch (SchemaException e) {
                throw new DataSourceException(e);
            }
        }

        return queryType;
    }

    /**
     * @see org.geotools.data.DataStore#getFeatureSource(java.lang.String)
     */
    public WFSFeatureSource getFeatureSource(final String typeName) throws IOException {
        return new WFSFeatureSource(this, typeName);
    }

    /**
     * @return {@code null}, no lock support so far
     * @see org.geotools.data.DataStore#getLockingManager()
     */
    public LockingManager getLockingManager() {
        return null;
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

    /**
     * @see org.geotools.data.DataAccess#getFeatureSource(org.opengis.feature.type.Name)
     */
    public SimpleFeatureSource getFeatureSource(Name typeName)
            throws IOException {
        Set<QName> featureTypeNames = wfs.getFeatureTypeNames();

        final String namespaceURI = typeName.getNamespaceURI();
        final String localPart = typeName.getLocalPart();
        for (QName qname : featureTypeNames) {
            if (namespaceURI.equals(qname.getNamespaceURI())
                    && localPart.equals(qname.getLocalPart())) {
                String prefixedName = qname.getPrefix() + ":" + localPart;
                return getFeatureSource(prefixedName);
            }
        }
        throw new SchemaNotFoundException(typeName.getURI());
    }

    /**
     * @see DataAccess#updateSchema(Name, org.opengis.feature.type.FeatureType)
     * @throws UnsupportedOperationException
     *             always since this operation does not apply to a WFS backend
     */
    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("WFS does not support update schema");
    }

    /**
     * @see org.geotools.data.DataStore#updateSchema(java.lang.String,
     *      org.opengis.feature.simple.SimpleFeatureType)
     * @throws UnsupportedOperationException
     *             always since this operation does not apply to a WFS backend
     */
    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("WFS does not support update schema");
    }

    /**
     * @see org.geotools.data.DataStore#createSchema(org.opengis.feature.simple.SimpleFeatureType)
     * @throws UnsupportedOperationException
     *             always since this operation does not apply to a WFS backend
     */
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("WFS DataStore does not support createSchema");
    }

    /**
     * @see WFSDataStore#getFeatureTypeName
     */
    public QName getFeatureTypeName(String typeName) {
        return wfs.getFeatureTypeName(typeName);
    }

    /**
     * @see WFSDataStore#getFeatureTypeTitle(String)
     */
    public String getFeatureTypeTitle(String typeName) {
        return wfs.getFeatureTypeTitle(typeName);
    }

    /**
     * @see WFSDataStore#getFeatureTypeAbstract(String)
     */
    public String getFeatureTypeAbstract(String typeName) {
        return wfs.getFeatureTypeAbstract(typeName);
    }

    /**
     * @see WFSDataStore#getFeatureTypeWGS84Bounds(String)
     */
    public ReferencedEnvelope getFeatureTypeWGS84Bounds(String typeName) {
        return wfs.getFeatureTypeWGS84Bounds(typeName);
    }

    /**
     * @see WFSDataStore#getFeatureTypeBounds(String)
     */
    public ReferencedEnvelope getFeatureTypeBounds(String typeName) {
        final ReferencedEnvelope wgs84Bounds = wfs.getFeatureTypeWGS84Bounds(typeName);
        final CoordinateReferenceSystem ftypeCrs = getFeatureTypeCRS(typeName);

        ReferencedEnvelope nativeBounds;
        try {
            nativeBounds = wgs84Bounds.transform(ftypeCrs, true);
        } catch (TransformException e) {
            LOGGER.log(Level.WARNING, "Can't transform bounds of " + typeName + " to "
                    + wfs.getDefaultCRS(typeName), e);
            nativeBounds = new ReferencedEnvelope(ftypeCrs);
        } catch (FactoryException e) {
            LOGGER.log(Level.WARNING, "Can't transform bounds of " + typeName + " to "
                    + wfs.getDefaultCRS(typeName), e);
            nativeBounds = new ReferencedEnvelope(ftypeCrs);
        }
        return nativeBounds;
    }

    /**
     * @return a non null CRS for the feature type, if the actual CRS can't be determined,
     *         {@link DefaultEngineeringCRS#GENERIC_2D} is returned
     * @see WFSDataStore#getFeatureTypeCRS(String)
     */
    public CoordinateReferenceSystem getFeatureTypeCRS(String typeName) {
        final String defaultCRS = wfs.getDefaultCRS(typeName);
        CoordinateReferenceSystem crs = null;
        try {
            crs = CRS.decode(defaultCRS);
        } catch (NoSuchAuthorityCodeException e) {
            LOGGER.info("Authority not found for " + typeName + " CRS: " + defaultCRS);
            // HACK HACK HACK!: remove when
            // http://jira.codehaus.org/browse/GEOT-1659 is fixed
            if (defaultCRS.toUpperCase().startsWith("URN")) {
                String code = defaultCRS.substring(defaultCRS.lastIndexOf(":") + 1);
                String epsgCode = "EPSG:" + code;
                try {
                    crs = CRS.decode(epsgCode);
                } catch (Exception e1) {
                    LOGGER.log(Level.WARNING, "can't decode CRS " + epsgCode + " for " + typeName
                            + ". Assigning DefaultEngineeringCRS.GENERIC_2D: "
                            + DefaultEngineeringCRS.GENERIC_2D);
                    crs = DefaultEngineeringCRS.GENERIC_2D;
                }
            }
        } catch (FactoryException e) {
            LOGGER.log(Level.WARNING, "Error creating CRS " + typeName + ": " + defaultCRS, e);
        }
        return crs;
    }

    /**
     * @see WFSDataStore#getFeatureTypeKeywords(String)
     */
    public Set<String> getFeatureTypeKeywords(String typeName) {
        return wfs.getFeatureTypeKeywords(typeName);
    }

    /**
     * @see WFSDataStore#getDescribeFeatureTypeURL(String)
     */
    public URL getDescribeFeatureTypeURL(String typeName) {
        return wfs.getDescribeFeatureTypeURLGet(typeName);
    }

    /**
     * @see WFSDataStore#getServiceAbstract()
     */
    public String getServiceAbstract() {
        return wfs.getServiceAbstract();
    }

    /**
     * @see WFSDataStore#getServiceKeywords()
     */
    public Set<String> getServiceKeywords() {
        return wfs.getServiceKeywords();
    }

    /**
     * @see WFSDataStore#getServiceProviderUri()
     */
    public URI getServiceProviderUri() {
        return wfs.getServiceProviderUri();
    }

    /**
     * @see WFSDataStore#getCapabilitiesURL()
     */
    public URL getCapabilitiesURL() {
        URL capsUrl = wfs.getOperationURL(GET_CAPABILITIES, false);
        if (capsUrl == null) {
            capsUrl = wfs.getOperationURL(GET_CAPABILITIES, true);
        }
        return capsUrl;
    }

    /**
     * @see WFSDataStore#getserviceTitle()
     */
    public String getServiceTitle() {
        return wfs.getServiceTitle();
    }

    /**
     * @see WFSDataStore#getServiceVersion()
     */
    public String getServiceVersion() {
        return wfs.getServiceVersion().toString();
    }

    /**
     * Only returns the bounds of the query (ie, the bounds of the whole feature type) if the query
     * has no filter set, otherwise the bounds may be too expensive to acquire.
     * 
     * @param query
     * @return The bounding box of the datasource in the CRS required by the query, or {@code null}
     *         if unknown and too expensive for the method to calculate or any errors occur.
     */
    public ReferencedEnvelope getBounds(final Query query) throws IOException {
        if (!Filter.INCLUDE.equals(query.getFilter())) {
            return null;
        }
        final String typeName = query.getTypeName();

        ReferencedEnvelope featureTypeBounds;

        featureTypeBounds = getFeatureTypeBounds(typeName);

        final CoordinateReferenceSystem featureTypeCrs = featureTypeBounds
                .getCoordinateReferenceSystem();
        final CoordinateReferenceSystem queryCrs = query.getCoordinateSystem();
        if (queryCrs != null && !CRS.equalsIgnoreMetadata(queryCrs, featureTypeCrs)) {
            try {
                featureTypeBounds = featureTypeBounds.transform(queryCrs, true);
            } catch (TransformException e) {
                LOGGER.log(Level.INFO, "Error transforming bounds for " + typeName, e);
                featureTypeBounds = null;
            } catch (FactoryException e) {
                LOGGER.log(Level.INFO, "Error transforming bounds for " + typeName, e);
                featureTypeBounds = null;
            }
        }
        return featureTypeBounds;
    }

    /**
     * If the query is fully supported, makes a {@code GetFeature} request with {@code
     * resultType=hits} and returns the counts returned by the server, otherwise returns {@code -1}
     * as the result is too expensive to calculate.
     * 
     * @param query
     * @return the number of features returned by a GetFeature?resultType=hits request, or {@code
     *         -1} if not supported
     */
    public int getCount(final Query query) throws IOException {
        Filter[] filters = wfs.splitFilters(query.getFilter());
        Filter postFilter = filters[1];
        if (!Filter.INCLUDE.equals(postFilter)) {
            // Filter not fully supported, can't know without a full scan of the results
            return -1;
        }

        WFSResponse response = executeGetFeatures(query, Transaction.AUTO_COMMIT, ResultType.HITS);

        Object process = WFSExtensions.process(this, response);
        if (!(process instanceof GetFeatureParser)) {
            LOGGER.info("GetFeature with resultType=hits resulted in " + process);
        }
        int hits = ((GetFeatureParser) process).getNumberOfFeatures();
        if (hits != -1 && getMaxFeatures().intValue() > 0) {
            hits = Math.min(hits, getMaxFeatures().intValue());
        }
        return hits;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("WFSDataStore[");
        sb.append("version=").append(getServiceVersion());
        URL capabilitiesUrl = getCapabilitiesURL();
        sb.append(", URL=").append(capabilitiesUrl);
        sb.append(", max features=").append(
                maxFeaturesHardLimit.intValue() == 0 ? "not set" : String
                        .valueOf(maxFeaturesHardLimit));
        sb.append(", prefer POST over GET=").append(preferPostOverGet);
        sb.append("]");
        return sb.toString();
    }

    /**
     * @return
     *         <code>true<code> if HTTP POST method should be used to issue the given WFS operation, <code>false</code>
     *         if HTTP GET method should be used instead
     */
    private boolean useHttpPostFor(final WFSOperationType operation) {
        if (preferPostOverGet) {
            if (wfs.supportsOperation(operation, true)) {
                return true;
            }
        }
        if (wfs.supportsOperation(operation, false)) {
            return false;
        }
        throw new IllegalArgumentException("Neither POST nor GET method is supported for the "
                + operation + " operation by the server");
    }

    /**
     * Checks if the query requested CRS is supported by the query feature type and if not, adapts
     * the query to the feature type default CRS, returning the CRS identifier to use for the WFS
     * query.
     * <p>
     * If the query CRS is not advertised as supported in the WFS capabilities for the requested
     * feature type, the query filter is modified so that any geometry literal is reprojected to the
     * default CRS for the feature type, otherwise the query is not modified at all. In any case,
     * the crs identifier to actually use in the WFS GetFeature operation is returned.
     * </p>
     * 
     * @param query
     * @return
     * @throws IOException
     */
    private String adaptQueryForSupportedCrs(DefaultQuery query) throws IOException {
        // The CRS the query is performed in
        final String typeName = query.getTypeName();
        final CoordinateReferenceSystem queryCrs = query.getCoordinateSystem();
        final String defaultCrs = wfs.getDefaultCRS(typeName);

        if (queryCrs == null) {
            LOGGER.warning("Query does not provide a CRS, using default: " + query);
            return defaultCrs;
        }

        String epsgCode;

        final CoordinateReferenceSystem crsNative = getFeatureTypeCRS(typeName);

        if (CRS.equalsIgnoreMetadata(queryCrs, crsNative)) {
            epsgCode = defaultCrs;
            LOGGER.fine("request and native crs for " + typeName + " are the same: " + epsgCode);
        } else {
            boolean transform = false;
            epsgCode = GML2EncodingUtils.epsgCode(queryCrs);
            if (epsgCode == null) {
                LOGGER.fine("Can't find the identifier for the request CRS, "
                        + "query will be performed in native CRS");
                transform = true;
            } else {
                epsgCode = "EPSG:" + epsgCode;
                LOGGER.fine("Request CRS is " + epsgCode + ", checking if its supported for "
                        + typeName);

                Set<String> supportedCRSIdentifiers = wfs.getSupportedCRSIdentifiers(typeName);
                if (supportedCRSIdentifiers.contains(epsgCode)) {
                    LOGGER.fine(epsgCode + " is supported, request will be performed asking "
                            + "for reprojection over it");
                } else {
                    LOGGER.fine(epsgCode + " is not supported for " + typeName
                            + ". Query will be adapted to default CRS " + defaultCrs);
                    transform = true;
                }
                if (transform) {
                    epsgCode = defaultCrs;
                    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
                    SimpleFeatureType ftype = getSchema(typeName);
                    ReprojectingFilterVisitor visitor = new ReprojectingFilterVisitor(ff, ftype);
                    Filter filter = query.getFilter();
                    Filter reprojectedFilter = (Filter) filter.accept(visitor, null);
                    if (LOGGER.isLoggable(Level.FINER)) {
                        LOGGER.finer("Original Filter: " + filter + "\nReprojected filter: "
                                + reprojectedFilter);
                    }
                    LOGGER.fine("Query filter reprojected to native CRS for " + typeName);
                    query.setFilter(reprojectedFilter);
                }
            }
        }
        return epsgCode;
    }

    protected int getMaxFeatures(Query query) {
        int maxFeaturesDataStoreLimit = getMaxFeatures().intValue();
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
}
