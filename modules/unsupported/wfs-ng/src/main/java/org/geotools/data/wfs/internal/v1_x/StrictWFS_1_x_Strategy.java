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
package org.geotools.data.wfs.internal.v1_x;

import static org.geotools.data.wfs.internal.GetFeatureRequest.ResultType.RESULTS;
import static org.geotools.data.wfs.internal.HttpMethod.GET;
import static org.geotools.data.wfs.internal.HttpMethod.POST;
import static org.geotools.data.wfs.internal.Loggers.debug;
import static org.geotools.data.wfs.internal.Loggers.info;
import static org.geotools.data.wfs.internal.Loggers.requestInfo;
import static org.geotools.data.wfs.internal.Loggers.trace;
import static org.geotools.data.wfs.internal.WFSOperationType.GET_CAPABILITIES;
import static org.geotools.data.wfs.internal.WFSOperationType.GET_FEATURE;
import static org.geotools.data.wfs.internal.WFSOperationType.TRANSACTION;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.xml.namespace.QName;

import net.opengis.ows10.DCPType;
import net.opengis.ows10.DomainType;
import net.opengis.ows10.OperationType;
import net.opengis.ows10.OperationsMetadataType;
import net.opengis.ows10.RequestMethodType;
import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.DescribeFeatureTypeType;
import net.opengis.wfs.FeatureTypeListType;
import net.opengis.wfs.FeatureTypeType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.OperationsType;
import net.opengis.wfs.PropertyType;
import net.opengis.wfs.QueryType;
import net.opengis.wfs.ResultTypeType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.UpdateElementType;
import net.opengis.wfs.WFSCapabilitiesType;
import net.opengis.wfs.WfsFactory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.wfs.impl.WFSServiceInfo;
import org.geotools.data.wfs.internal.AbstractWFSStrategy;
import org.geotools.data.wfs.internal.DescribeFeatureTypeRequest;
import org.geotools.data.wfs.internal.FeatureTypeInfo;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetFeatureRequest.ResultType;
import org.geotools.data.wfs.internal.HttpMethod;
import org.geotools.data.wfs.internal.Loggers;
import org.geotools.data.wfs.internal.TransactionRequest;
import org.geotools.data.wfs.internal.TransactionRequest.Delete;
import org.geotools.data.wfs.internal.TransactionRequest.Insert;
import org.geotools.data.wfs.internal.TransactionRequest.TransactionElement;
import org.geotools.data.wfs.internal.TransactionRequest.Update;
import org.geotools.data.wfs.internal.Versions;
import org.geotools.data.wfs.internal.WFSExtensions;
import org.geotools.data.wfs.internal.WFSGetCapabilities;
import org.geotools.data.wfs.internal.WFSOperationType;
import org.geotools.data.wfs.internal.WFSResponseFactory;
import org.geotools.data.wfs.internal.WFSStrategy;
import org.geotools.feature.FeatureCollection;
import org.geotools.util.Version;
import org.geotools.wfs.v1_0.WFS;
import org.geotools.xml.Configuration;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.sort.SortBy;

/**
 * 
 */
public class StrictWFS_1_x_Strategy extends AbstractWFSStrategy {

    private static final List<String> PREFFERRED_GETFEATURE_FORMATS = Collections
            .unmodifiableList(Arrays.asList("text/xml; subtype=gml/3.1.1",
                    "text/xml; subtype=gml/3.1.1/profiles/gmlsf/0", "GML3"));

    /**
     * The WFS GetCapabilities document. Final by now, as we're not handling updatesequence, so will
     * not ask the server for an updated capabilities during the life-time of this datastore.
     */
    protected net.opengis.wfs.WFSCapabilitiesType capabilities;

    private final Map<QName, FeatureTypeType> typeInfos;

    private Version serviceVersion;

    public StrictWFS_1_x_Strategy() {
        // default to 1.0, override at setCapabilities if needed
        this(Versions.v1_0_0);
    }

    public StrictWFS_1_x_Strategy(Version defaultVersion) {
        super();
        typeInfos = new HashMap<QName, FeatureTypeType>();
        serviceVersion = defaultVersion;
    }

    /*---------------------------------------------------------------------
     * AbstractWFSStrategy methods
     * ---------------------------------------------------------------------*/

    @Override
    protected QName getOperationName(WFSOperationType operation) {
        return new QName(WFS.NAMESPACE, operation.getName());
    }

    /**
     * @see org.geotools.data.wfs.internal.AbstractWFSStrategy#createGetFeatureRequestPost(org.geotools.data.wfs.internal.GetFeatureRequest)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected GetFeatureType createGetFeatureRequestPost(GetFeatureRequest query)
            throws IOException {

        final QName typeName = query.getTypeName();
        final FeatureTypeInfo featureTypeInfo = getFeatureTypeInfo(typeName);

        final WfsFactory factory = WfsFactory.eINSTANCE;

        GetFeatureType getFeature = factory.createGetFeatureType();
        getFeature.setService("WFS");
        getFeature.setVersion(getVersion());

        String outputFormat = query.getOutputFormat();
        getFeature.setOutputFormat(outputFormat);

        getFeature.setHandle(query.getHandle());

        Integer maxFeatures = query.getMaxFeatures();
        if (maxFeatures != null) {
            getFeature.setMaxFeatures(BigInteger.valueOf(maxFeatures.intValue()));
        }

        ResultType resultType = query.getResultType();
        getFeature.setResultType(RESULTS == resultType ? ResultTypeType.RESULTS_LITERAL
                : ResultTypeType.HITS_LITERAL);

        QueryType wfsQuery = factory.createQueryType();
        wfsQuery.setTypeName(Collections.singletonList(typeName));

        final Filter supportedFilter;
        final Filter unsupportedFilter;
        {
            final Filter filter = query.getFilter();
            Filter[] splitFilters = splitFilters(typeName, filter);
            supportedFilter = splitFilters[0];
            unsupportedFilter = splitFilters[1];
        }

        query.setUnsupportedFilter(unsupportedFilter);

        if (!Filter.INCLUDE.equals(supportedFilter)) {
            wfsQuery.setFilter(supportedFilter);
        }

        String srsName = query.getSrsName();
        if (null == srsName) {
            srsName = featureTypeInfo.getDefaultSRS();
        }
        try {
            wfsQuery.setSrsName(new URI(srsName));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Can't create a URI from the query CRS: " + srsName, e);
        }
        String[] propertyNames = query.getPropertyNames();
        boolean retrieveAllProperties = propertyNames == null;
        if (!retrieveAllProperties) {
            List<String> propertyName = wfsQuery.getPropertyName();
            for (String propName : propertyNames) {
                propertyName.add(propName);
            }
        }
        SortBy[] sortByList = query.getSortBy();
        if (sortByList != null) {
            for (SortBy sortBy : sortByList) {
                wfsQuery.getSortBy().add(sortBy);
            }
        }

        getFeature.getQuery().add(wfsQuery);

        return getFeature;
    }

    @Override
    protected DescribeFeatureTypeType createDescribeFeatureTypeRequestPost(
            DescribeFeatureTypeRequest request) {

        final WfsFactory factory = WfsFactory.eINSTANCE;

        DescribeFeatureTypeType dft = factory.createDescribeFeatureTypeType();

        Version version = getServiceVersion();
        dft.setService("WFS");
        dft.setVersion(version.toString());
        dft.setHandle(request.getHandle());

        if (Versions.v1_0_0.equals(version)) {
            dft.setOutputFormat(null);
        }

        QName typeName = request.getTypeName();
        @SuppressWarnings("unchecked")
        List<QName> typeNames = dft.getTypeName();
        typeNames.add(typeName);

        return dft;
    }

    @Override
    protected EObject createTransactionRequest(TransactionRequest request) throws IOException {
        final WfsFactory factory = WfsFactory.eINSTANCE;

        TransactionType tx = factory.createTransactionType();
        tx.setService("WFS");
        tx.setHandle(request.getHandle());
        tx.setVersion(getVersion());

        List<TransactionElement> transactionElements = request.getTransactionElements();
        if (transactionElements.isEmpty()) {
            requestInfo("Asked to perform transaction with no transaction elements");
            return tx;
        }

        @SuppressWarnings("unchecked")
        List<InsertElementType> inserts = tx.getInsert();
        @SuppressWarnings("unchecked")
        List<UpdateElementType> updates = tx.getUpdate();
        @SuppressWarnings("unchecked")
        List<DeleteElementType> deletes = tx.getDelete();

        try {
            for (TransactionElement elem : transactionElements) {
                if (elem instanceof TransactionRequest.Insert) {
                    InsertElementType insert = createInsert(factory, (Insert) elem);
                    inserts.add(insert);
                } else if (elem instanceof TransactionRequest.Update) {
                    UpdateElementType update = createUpdate(factory, (Update) elem);
                    updates.add(update);
                } else if (elem instanceof TransactionRequest.Delete) {
                    DeleteElementType delete = createDelete(factory, (Delete) elem);
                    deletes.add(delete);
                }
            }
        } catch (IOException e) {
            throw e;
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception other) {
            throw new RuntimeException(other);
        }

        return tx;
    }

    protected InsertElementType createInsert(WfsFactory factory, Insert elem) throws Exception {
        InsertElementType insert = factory.createInsertElementType();

        String srsName = getFeatureTypeInfo(elem.getTypeName()).getDefaultSRS();
        insert.setSrsName(new URI(srsName));

        List<SimpleFeature> features = elem.getFeatures();
        SimpleFeatureCollection collection = DataUtilities.collection(features);

        @SuppressWarnings({ "rawtypes", "unchecked" })
        List<FeatureCollection> featureCollections = insert.getFeature();

        featureCollections.add(collection);
        return insert;
    }

    protected UpdateElementType createUpdate(WfsFactory factory, Update elem) throws Exception {

        List<QName> propertyNames = elem.getPropertyNames();
        List<Object> newValues = elem.getNewValues();
        if (propertyNames.size() != newValues.size()) {
            throw new IllegalArgumentException("Got " + propertyNames.size()
                    + " property names and " + newValues.size() + " values");
        }

        UpdateElementType update = factory.createUpdateElementType();

        QName typeName = elem.getTypeName();
        update.setTypeName(typeName);
        String srsName = getFeatureTypeInfo(typeName).getDefaultSRS();
        update.setSrsName(new URI(srsName));

        Filter filter = elem.getFilter();
        update.setFilter(filter);

        @SuppressWarnings("unchecked")
        List<PropertyType> properties = update.getProperty();

        for (int i = 0; i < propertyNames.size(); i++) {
            QName propName = propertyNames.get(i);
            Object value = newValues.get(i);
            PropertyType property = factory.createPropertyType();
            property.setName(propName);
            property.setValue(value);

            properties.add(property);
        }

        return update;
    }

    protected DeleteElementType createDelete(WfsFactory factory, Delete elem) throws Exception {
        DeleteElementType delete = factory.createDeleteElementType();

        QName typeName = elem.getTypeName();
        delete.setTypeName(typeName);
        Filter filter = elem.getFilter();
        delete.setFilter(filter);

        return delete;
    }

    /*---------------------------------------------------------------------
     * WFSStrategy methods
     * ---------------------------------------------------------------------*/

    @Override
    public boolean supportsTransaction(QName typeName) {
        try {
            getFeatureTypeInfo(typeName);
        } catch (IllegalArgumentException e) {
            throw e;
        }
        if (!supportsOperation(TRANSACTION, POST)) {
            return false;
        }

        OperationsType operations = this.capabilities.getFeatureTypeList().getOperations();
        @SuppressWarnings("unchecked")
        List<net.opengis.wfs.OperationType> operation = operations.getOperation();
        for (net.opengis.wfs.OperationType required : Arrays.asList(
                net.opengis.wfs.OperationType.INSERT_LITERAL,
                net.opengis.wfs.OperationType.UPDATE_LITERAL,
                net.opengis.wfs.OperationType.DELETE_LITERAL)) {

            if (!operation.contains(required)) {
                info("Transactions not supported since WFS didn't declare support for "
                        + required.getName());
                return false;
            }
        }
        return true;
    }

    @Override
    public Configuration getFilterConfiguration() {
        return Versions.v1_0_0.equals(getServiceVersion()) ? FILTER_1_0_CONFIGURATION
                : FILTER_1_1_CONFIGURATION;
    }

    @Override
    public Configuration getWfsConfiguration() {
        return Versions.v1_0_0.equals(getServiceVersion()) ? WFS_1_0_CONFIGURATION
                : WFS_1_1_CONFIGURATION;
    }

    @Override
    public void setCapabilities(WFSGetCapabilities capabilities) {
        WFSCapabilitiesType caps = (WFSCapabilitiesType) capabilities.getParsedCapabilities();
        this.capabilities = caps;
        String version = caps.getVersion();
        try {
            this.serviceVersion = Versions.find(version);
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Capabilities document didn't advertise a supported version (" + version
                    + "). Defaulting to " + this.serviceVersion);
        }

        typeInfos.clear();

        FeatureTypeListType featureTypeList = this.capabilities.getFeatureTypeList();

        if (featureTypeList == null || featureTypeList.getFeatureType().isEmpty()) {
            Loggers.MODULE.info("WFS Server contains no FeatureTypes: "
                    + getOperationURI(GET_CAPABILITIES, GET));
            return;
        }

        @SuppressWarnings("unchecked")
        List<FeatureTypeType> featureTypes = featureTypeList.getFeatureType();

        for (FeatureTypeType typeInfo : featureTypes) {
            QName name = typeInfo.getName();
            typeInfos.put(name, typeInfo);
        }
    }

    @Override
    public WFSServiceInfo getServiceInfo() {

        final String schemaLocation;
        if (Versions.v1_1_0.equals(getServiceVersion())) {
            schemaLocation = "http://schemas.opengis.net/wfs/1.0.0/WFS-transaction.xsd";
        } else {
            schemaLocation = "http://schemas.opengis.net/wfs/1.1.0/wfs.xsd";
        }

        URL getCapsUrl = getOperationURL(GET_CAPABILITIES, GET);
        return new CapabilitiesServiceInfo(schemaLocation, getCapsUrl, capabilities);
    }

    @Override
    public boolean supports(ResultType resultType) {
        switch (resultType) {
        case RESULTS:
            return true;
        case HITS:
            return Versions.v1_0_0.equals(getServiceVersion()) ? false : true;
        default:
            return false;
        }
    }

    @Override
    public Version getServiceVersion() {
        return this.serviceVersion;
    }

    /**
     * @see WFSStrategy#getFeatureTypeNames()
     */
    @Override
    public Set<QName> getFeatureTypeNames() {
        return new HashSet<QName>(typeInfos.keySet());
    }

    /**
     * @see org.geotools.data.wfs.internal.WFSStrategy#getFeatureTypeInfo(javax.xml.namespace.QName)
     */
    @Override
    public FeatureTypeInfo getFeatureTypeInfo(QName typeName) {
        FeatureTypeType eType = typeInfos.get(typeName);
        if (null == eType) {
            throw new IllegalArgumentException("Type name not found: " + typeName);
        }
        return new FeatureTypeInfoImpl(eType);
    }

    /**
     * @see WFSStrategy#getFilterCapabilities()
     */
    @Override
    public FilterCapabilities getFilterCapabilities() {
        FilterCapabilities wfsFilterCapabilities;
        wfsFilterCapabilities = capabilities.getFilterCapabilities();
        return wfsFilterCapabilities;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected String getOperationURI(WFSOperationType operation, HttpMethod method) {

        trace("Looking operation URI for ", operation, "/", method);

        List<OperationType> operations = capabilities.getOperationsMetadata().getOperation();
        for (OperationType op : operations) {
            if (!operation.getName().equals(op.getName())) {
                continue;
            }
            List<DCPType> dcpTypes = op.getDCP();
            if (null == dcpTypes) {
                continue;
            }
            for (DCPType d : dcpTypes) {
                List<RequestMethodType> methods;
                if (HttpMethod.GET.equals(method)) {
                    methods = d.getHTTP().getGet();
                } else {
                    methods = d.getHTTP().getPost();
                }
                if (null == methods || methods.isEmpty()) {
                    continue;
                }

                String href = methods.get(0).getHref();
                debug("Returning operation URI for ", operation, "/", method, ": ", href);
                return href;
            }
        }

        debug("No operation URI found for ", operation, "/", method);
        return null;
    }

    /**
     * @see WFSStrategy#getServerSupportedOutputFormats(QName, WFSOperationType)
     */
    @Override
    public Set<String> getServerSupportedOutputFormats(QName typeName, WFSOperationType operation) {

        Set<String> ftypeFormats = new HashSet<String>();

        final Set<String> serviceOutputFormats = getServerSupportedOutputFormats(operation);
        ftypeFormats.addAll(serviceOutputFormats);

        if (GET_FEATURE.equals(operation)) {
            final FeatureTypeInfo typeInfo = getFeatureTypeInfo(typeName);

            final Set<String> typeAdvertisedFormats = typeInfo.getOutputFormats();

            ftypeFormats.addAll(typeAdvertisedFormats);
        }
        return ftypeFormats;
    }

    /**
     * @see #getDefaultOutputFormat
     */
    @Override
    public Set<String> getServerSupportedOutputFormats(final WFSOperationType operation) {
        final OperationType operationMetadata = getOperationMetadata(operation);

        String parameterName;

        final Version serviceVersion = getServiceVersion();
        final boolean wfs1_0 = Versions.v1_0_0.equals(serviceVersion);
        switch (operation) {
        case GET_FEATURE:
            parameterName = wfs1_0 ? "ResultFormat" : "outputFormat";
            break;
        case DESCRIBE_FEATURETYPE:
            parameterName = wfs1_0 ? "SchemaDescriptionLanguage" : "outputFormat";
            break;
        case GET_FEATURE_WITH_LOCK:
            parameterName = wfs1_0 ? "ResultFormat" : "outputFormat";
            break;
        case TRANSACTION:
            parameterName = wfs1_0 ? "" : "inputFormat";
            break;
        default:
            throw new UnsupportedOperationException("not yet implemented for " + operation);
        }

        Set<String> serverSupportedFormats;
        serverSupportedFormats = findParameters(operationMetadata, parameterName);
        return serverSupportedFormats;
    }

    /**
     * @see WFSStrategy#getSupportedCRSIdentifiers
     */
    @Override
    public Set<String> getSupportedCRSIdentifiers(QName typeName) {
        FeatureTypeInfo featureTypeInfo = getFeatureTypeInfo(typeName);

        String defaultSRS = featureTypeInfo.getDefaultSRS();

        List<String> otherSRS = featureTypeInfo.getOtherSRS();

        Set<String> ftypeCrss = new HashSet<String>();
        ftypeCrss.add(defaultSRS);
        ftypeCrss.addAll(otherSRS);

        final boolean wfs1_1 = Versions.v1_1_0.equals(getServiceVersion());
        if (wfs1_1) {
            OperationType operationMetadata = getOperationMetadata(GET_FEATURE);
            final String operationParameter = "SrsName";
            Set<String> globalSrsNames = findParameters(operationMetadata, operationParameter);
            ftypeCrss.addAll(globalSrsNames);
        }
        return ftypeCrss;
    }

    @SuppressWarnings("unchecked")
    protected Set<String> findParameters(final OperationType operationMetadata,
            final String parameterName) {
        Set<String> outputFormats = new HashSet<String>();

        List<DomainType> parameters = operationMetadata.getParameter();
        for (DomainType param : parameters) {

            String paramName = param.getName();

            if (parameterName.equals(paramName)) {

                List<String> value = param.getValue();
                outputFormats.addAll(value);
            }
        }
        return outputFormats;
    }

    @Override
    public List<String> getClientSupportedOutputFormats(WFSOperationType operation) {

        List<WFSResponseFactory> operationResponseFactories;
        operationResponseFactories = WFSExtensions.findResponseFactories(operation);

        List<String> outputFormats = new LinkedList<String>();
        for (WFSResponseFactory factory : operationResponseFactories) {
            List<String> factoryFormats = factory.getSupportedOutputFormats();
            outputFormats.addAll(factoryFormats);
        }

        if (GET_FEATURE.equals(operation)) {
            for (String preferred : PREFFERRED_GETFEATURE_FORMATS) {
                boolean hasFormat = outputFormats.remove(preferred);
                if (hasFormat) {
                    outputFormats.add(0, preferred);
                    break;
                }
            }
        }

        return outputFormats;
    }

    /**
     * @return the operation metadata advertised in the capabilities for the given operation
     * @see #getServerSupportedOutputFormats(WFSOperationType)
     */
    protected OperationType getOperationMetadata(final WFSOperationType operation) {
        final OperationsMetadataType operationsMetadata = capabilities.getOperationsMetadata();
        @SuppressWarnings("unchecked")
        final List<OperationType> operations = operationsMetadata.getOperation();
        final String expectedOperationName = operation.getName();
        for (OperationType operationType : operations) {
            String operationName = operationType.getName();
            if (expectedOperationName.equalsIgnoreCase(operationName)) {
                return operationType;
            }
        }
        throw new NoSuchElementException("Operation metadata not found for "
                + expectedOperationName + " in the capabilities document");
    }

}
