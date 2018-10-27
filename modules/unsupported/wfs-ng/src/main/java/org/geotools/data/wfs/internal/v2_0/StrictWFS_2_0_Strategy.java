/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.v2_0;

import static org.geotools.data.wfs.internal.HttpMethod.GET;
import static org.geotools.data.wfs.internal.HttpMethod.POST;
import static org.geotools.data.wfs.internal.Loggers.debug;
import static org.geotools.data.wfs.internal.Loggers.requestInfo;
import static org.geotools.data.wfs.internal.Loggers.trace;
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
import net.opengis.fes20.AbstractQueryExpressionType;
import net.opengis.ows11.DCPType;
import net.opengis.ows11.DomainType;
import net.opengis.ows11.OperationType;
import net.opengis.ows11.OperationsMetadataType;
import net.opengis.ows11.RequestMethodType;
import net.opengis.ows11.ValueType;
import net.opengis.wfs20.AbstractTransactionActionType;
import net.opengis.wfs20.DeleteType;
import net.opengis.wfs20.DescribeFeatureTypeType;
import net.opengis.wfs20.DescribeStoredQueriesType;
import net.opengis.wfs20.FeatureTypeListType;
import net.opengis.wfs20.FeatureTypeType;
import net.opengis.wfs20.GetFeatureType;
import net.opengis.wfs20.InsertType;
import net.opengis.wfs20.ListStoredQueriesType;
import net.opengis.wfs20.ParameterType;
import net.opengis.wfs20.PropertyType;
import net.opengis.wfs20.QueryType;
import net.opengis.wfs20.ResultTypeType;
import net.opengis.wfs20.StoredQueryDescriptionType;
import net.opengis.wfs20.StoredQueryType;
import net.opengis.wfs20.TransactionType;
import net.opengis.wfs20.UpdateType;
import net.opengis.wfs20.ValueReferenceType;
import net.opengis.wfs20.WFSCapabilitiesType;
import net.opengis.wfs20.Wfs20Factory;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSServiceInfo;
import org.geotools.data.wfs.internal.AbstractWFSStrategy;
import org.geotools.data.wfs.internal.DescribeFeatureTypeRequest;
import org.geotools.data.wfs.internal.DescribeStoredQueriesRequest;
import org.geotools.data.wfs.internal.FeatureTypeInfo;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetFeatureRequest.ResultType;
import org.geotools.data.wfs.internal.HttpMethod;
import org.geotools.data.wfs.internal.ListStoredQueriesRequest;
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
import org.geotools.data.wfs.internal.v2_0.storedquery.ParameterTypeFactory;
import org.geotools.data.wfs.internal.v2_0.storedquery.StoredQueryConfiguration;
import org.geotools.util.Version;
import org.geotools.util.factory.Hints;
import org.geotools.util.factory.Hints.ConfigurationMetadataKey;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xsd.Configuration;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.capability.FilterCapabilities;

/** */
public class StrictWFS_2_0_Strategy extends AbstractWFSStrategy {

    private static final List<String> PREFERRED_FORMATS =
            Collections.unmodifiableList(
                    Arrays.asList(
                            "application/gml+xml; version=3.2", // As per Table 12 in 09-25r1 OGC
                            // Web Feature Service WFS 2.0
                            "text/xml; subtype=gml/3.2",
                            "gml32",
                            "text/xml; subtype=gml/3.1.1",
                            "gml3",
                            "text/xml; subtype=gml/2.1.2",
                            "GML2"));

    private net.opengis.wfs20.WFSCapabilitiesType capabilities;

    private final Map<QName, FeatureTypeType> typeInfos;

    private static final ConfigurationMetadataKey CONFIG_KEY =
            ConfigurationMetadataKey.get(WFSDataStore.STORED_QUERY_CONFIGURATION_HINT);

    public StrictWFS_2_0_Strategy() {
        super();
        typeInfos = new HashMap<QName, FeatureTypeType>();
    }

    /*---------------------------------------------------------------------
     * AbstractWFSStrategy methods
     * ---------------------------------------------------------------------*/
    @Override
    public Configuration getFilterConfiguration() {
        return FILTER_2_0_CONFIGURATION;
    }

    @Override
    public Configuration getWfsConfiguration() {
        return WFS_2_0_CONFIGURATION;
    }

    @Override
    protected QName getOperationName(WFSOperationType operation) {
        return new QName(WFS.NAMESPACE, operation.getName());
    }

    /*---------------------------------------------------------------------
     * WFSStrategy methods
     * ---------------------------------------------------------------------*/

    @Override
    public void setCapabilities(WFSGetCapabilities capabilities) {
        net.opengis.wfs20.WFSCapabilitiesType caps =
                (WFSCapabilitiesType) capabilities.getParsedCapabilities();
        this.capabilities = caps;

        typeInfos.clear();
        FeatureTypeListType featureTypeList = this.capabilities.getFeatureTypeList();

        @SuppressWarnings("unchecked")
        List<FeatureTypeType> featureTypes = featureTypeList.getFeatureType();

        for (FeatureTypeType typeInfo : featureTypes) {
            QName name = typeInfo.getName();
            typeInfos.put(name, typeInfo);
        }
    }

    @Override
    public WFSServiceInfo getServiceInfo() {
        URL getCapsUrl = getOperationURL(WFSOperationType.GET_CAPABILITIES, GET);
        return new Capabilities200ServiceInfo(
                "http://schemas.opengis.net/wfs/2.0/wfs.xsd", getCapsUrl, capabilities);
    }

    @Override
    public boolean supports(ResultType resultType) {
        switch (resultType) {
            case RESULTS:
            case HITS:
                return true;
            default:
                return false;
        }
    }

    @Override
    public Version getServiceVersion() {
        return Versions.v2_0_0;
    }

    /** @see WFSStrategy#getFeatureTypeNames() */
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
        return new FeatureTypeInfoImpl(eType, config);
    }

    @Override
    public FilterCapabilities getFilterCapabilities() {
        return capabilities.getFilterCapabilities();
    }

    @Override
    protected Map<String, String> buildGetFeatureParametersForGET(GetFeatureRequest query) {
        Map<String, String> kvp = null;
        if (query.isStoredQuery()) {
            FeatureTypeInfoImpl featureTypeInfo =
                    (FeatureTypeInfoImpl) getFeatureTypeInfo(query.getTypeName());
            StoredQueryDescriptionType desc = query.getStoredQueryDescriptionType();

            StoredQueryConfiguration config = null;

            kvp = new HashMap<String, String>();

            kvp.put("SERVICE", "WFS");
            kvp.put("VERSION", getVersion());
            kvp.put("REQUEST", "GetFeature");
            kvp.put("STOREDQUERY_ID", desc.getId());

            Filter originalFilter = query.getFilter();

            query.setUnsupportedFilter(originalFilter);

            Map<String, String> viewParams = null;
            if (query.getRequestHints() != null) {
                viewParams =
                        (Map<String, String>)
                                query.getRequestHints().get(Hints.VIRTUAL_TABLE_PARAMETERS);

                config = (StoredQueryConfiguration) query.getRequestHints().get(CONFIG_KEY);
            }

            List<ParameterType> params =
                    new ParameterTypeFactory(config, desc, featureTypeInfo)
                            .buildStoredQueryParameters(viewParams, originalFilter);

            for (ParameterType p : params) {
                kvp.put(p.getName(), p.getValue());
            }

        } else {
            kvp = super.buildGetFeatureParametersForGET(query);

            // Very crude
            if (query.getMaxFeatures() != null) {
                String count = kvp.remove("MAXFEATURES");
                kvp.put("COUNT", count);
            }
        }

        return kvp;
    }

    @Override
    protected EObject createDescribeFeatureTypeRequestPost(DescribeFeatureTypeRequest request) {
        final Wfs20Factory factory = Wfs20Factory.eINSTANCE;

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
    protected EObject createGetFeatureRequestPost(GetFeatureRequest query) throws IOException {
        final QName typeName = query.getTypeName();
        final FeatureTypeInfoImpl featureTypeInfo =
                (FeatureTypeInfoImpl) getFeatureTypeInfo(typeName);

        final Wfs20Factory factory = Wfs20Factory.eINSTANCE;

        GetFeatureType getFeature = factory.createGetFeatureType();
        getFeature.setService("WFS");
        getFeature.setVersion(getVersion());

        String outputFormat = query.getOutputFormat();
        getFeature.setOutputFormat(outputFormat);

        getFeature.setHandle(query.getHandle());

        Integer maxFeatures = query.getMaxFeatures();
        if (maxFeatures != null) {
            getFeature.setCount(BigInteger.valueOf(maxFeatures.intValue()));
        }

        ResultType resultType = query.getResultType();
        getFeature.setResultType(
                ResultType.RESULTS == resultType ? ResultTypeType.RESULTS : ResultTypeType.HITS);

        AbstractQueryExpressionType abstractQuery;

        if (query.isStoredQuery()) {
            StoredQueryDescriptionType desc = query.getStoredQueryDescriptionType();

            StoredQueryType storedQuery = factory.createStoredQueryType();
            storedQuery.setId(desc.getId());

            // The query filter must be processed locally in full
            query.setUnsupportedFilter(query.getFilter());

            Map<String, String> viewParams = null;
            StoredQueryConfiguration config = null;

            if (query.getRequestHints() != null) {
                viewParams =
                        (Map<String, String>)
                                query.getRequestHints().get(Hints.VIRTUAL_TABLE_PARAMETERS);

                config = (StoredQueryConfiguration) query.getRequestHints().get(CONFIG_KEY);
            }

            List<ParameterType> params =
                    new ParameterTypeFactory(config, desc, featureTypeInfo)
                            .buildStoredQueryParameters(viewParams, query.getFilter());

            storedQuery.getParameter().addAll(params);

            abstractQuery = storedQuery;

        } else {
            QueryType wfsQuery = factory.createQueryType();
            wfsQuery.getTypeNames().add(typeName);

            // Lifted from 1.0 / 1.1
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
                List<QName> propertyName = wfsQuery.getPropertyNames();
                for (String propName : propertyNames) {
                    // These get encoded into <fes:AbstractProjectionClause/> elements. Something's
                    // missing
                    propertyName.add(
                            new QName(featureTypeInfo.getQName().getNamespaceURI(), propName));
                }
            }

            /*
            * System.err.println("SortBy is not yet implemented in StrictWFS_2_0_Strategy");
               SortBy[] sortByList = query.getSortBy();
               if (sortByList != null) {
                   for (SortBy sortBy : sortByList) {
                       wfsQuery.getSortBy().add(sortBy);
                   }
               }
            */
            abstractQuery = wfsQuery;
        }
        getFeature.getAbstractQueryExpression().add(abstractQuery);

        return getFeature;
    }

    @Override
    protected EObject createListStoredQueriesRequestPost(ListStoredQueriesRequest request)
            throws IOException {
        final Wfs20Factory factory = Wfs20Factory.eINSTANCE;

        ListStoredQueriesType ret = factory.createListStoredQueriesType();

        return ret;
    }

    @Override
    protected EObject createDescribeStoredQueriesRequestPost(DescribeStoredQueriesRequest request)
            throws IOException {
        final Wfs20Factory factory = Wfs20Factory.eINSTANCE;

        DescribeStoredQueriesType ret = factory.createDescribeStoredQueriesType();

        ret.getStoredQueryId().addAll(request.getStoredQueryIds());

        return ret;
    }

    @Override
    protected EObject createTransactionRequest(TransactionRequest request) throws IOException {
        final Wfs20Factory factory = Wfs20Factory.eINSTANCE;

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
        List<AbstractTransactionActionType> actions = tx.getAbstractTransactionAction();

        try {
            for (TransactionElement elem : transactionElements) {
                AbstractTransactionActionType action = null;
                if (elem instanceof TransactionRequest.Insert) {
                    action = createInsert(factory, (Insert) elem);
                } else if (elem instanceof TransactionRequest.Update) {
                    action = createUpdate(factory, (Update) elem);
                } else if (elem instanceof TransactionRequest.Delete) {
                    action = createDelete(factory, (Delete) elem);
                }
                actions.add(action);
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

    @Override
    protected String getOperationURI(WFSOperationType operation, HttpMethod method) {
        OperationsMetadataType omt = this.capabilities.getOperationsMetadata();
        omt.getOperation();

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

    @Override
    public Set<String> getServerSupportedOutputFormats(WFSOperationType operation) {
        String parameterName;

        switch (operation) {
            case GET_FEATURE:
            case DESCRIBE_FEATURETYPE:
            case GET_FEATURE_WITH_LOCK:
                parameterName = "outputFormat";
                break;
            case TRANSACTION:
                parameterName = "inputFormat";
                break;
            case LIST_STORED_QUERIES:
            case DESCRIBE_STORED_QUERIES:
                // These return XML as specified in WFS 2.0.0
                return Collections.singleton("text/xml");
            default:
                throw new UnsupportedOperationException("not yet implemented for " + operation);
        }

        final OperationType operationMetadata = getOperationMetadata(operation);

        Set<String> serverSupportedFormats;
        serverSupportedFormats = findParameters(operationMetadata, parameterName);
        return serverSupportedFormats;
    }

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
            for (String preferred : PREFERRED_FORMATS) {
                boolean hasFormat = outputFormats.remove(preferred);
                if (hasFormat) {
                    outputFormats.add(0, preferred);
                    break;
                }
            }
        }

        return outputFormats;
    }

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

        return true;
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
        throw new NoSuchElementException(
                "Operation metadata not found for "
                        + expectedOperationName
                        + " in the capabilities document");
    }

    @Override
    public Set<String> getSupportedCRSIdentifiers(QName typeName) {
        FeatureTypeInfo featureTypeInfo = getFeatureTypeInfo(typeName);

        String defaultSRS = featureTypeInfo.getDefaultSRS();

        List<String> otherSRS = featureTypeInfo.getOtherSRS();

        Set<String> ftypeCrss = new HashSet<String>();
        ftypeCrss.add(defaultSRS);
        ftypeCrss.addAll(otherSRS);

        final boolean wfs2_0 = Versions.v2_0_0.equals(getServiceVersion());
        if (wfs2_0) {
            OperationType operationMetadata = getOperationMetadata(GET_FEATURE);
            final String operationParameter = "SrsName";
            Set<String> globalSrsNames = findParameters(operationMetadata, operationParameter);
            ftypeCrss.addAll(globalSrsNames);
        }
        return ftypeCrss;
    }

    @SuppressWarnings("unchecked")
    protected Set<String> findParameters(
            final OperationType operationMetadata, final String parameterName) {
        Set<String> outputFormats = new HashSet<String>();

        List<DomainType> parameters = operationMetadata.getParameter();
        for (DomainType param : parameters) {

            String paramName = param.getName();

            if (parameterName.equals(paramName)) {

                for (ValueType value : (List<ValueType>) param.getAllowedValues().getValue()) {
                    outputFormats.add(value.getValue());
                }
            }
        }
        return outputFormats;
    }

    protected AbstractTransactionActionType createInsert(Wfs20Factory factory, Insert elem)
            throws Exception {
        InsertType insert = factory.createInsertType();

        String srsName = getFeatureTypeInfo(elem.getTypeName()).getDefaultSRS();
        insert.setSrsName(srsName);

        List<SimpleFeature> features = elem.getFeatures();

        insert.getAny().addAll(features);

        return insert;
    }

    protected AbstractTransactionActionType createUpdate(Wfs20Factory factory, Update elem)
            throws Exception {

        List<QName> propertyNames = elem.getPropertyNames();
        List<Object> newValues = elem.getNewValues();
        if (propertyNames.size() != newValues.size()) {
            throw new IllegalArgumentException(
                    "Got "
                            + propertyNames.size()
                            + " property names and "
                            + newValues.size()
                            + " values");
        }

        UpdateType update = factory.createUpdateType();

        QName typeName = elem.getTypeName();
        update.setTypeName(typeName);
        String srsName = getFeatureTypeInfo(typeName).getDefaultSRS();
        update.setSrsName(srsName);

        Filter filter = elem.getFilter();
        update.setFilter(filter);

        @SuppressWarnings("unchecked")
        List<PropertyType> properties = update.getProperty();

        for (int i = 0; i < propertyNames.size(); i++) {
            QName propName = propertyNames.get(i);
            Object value = newValues.get(i);
            PropertyType property = factory.createPropertyType();
            ValueReferenceType ref = factory.createValueReferenceType();
            ref.setValue(propName);
            property.setValueReference(ref);
            property.setValue(value);

            properties.add(property);
        }

        return update;
    }

    protected AbstractTransactionActionType createDelete(Wfs20Factory factory, Delete elem)
            throws Exception {
        DeleteType delete = factory.createDeleteType();

        QName typeName = elem.getTypeName();
        delete.setTypeName(typeName);
        Filter filter = elem.getFilter();
        delete.setFilter(filter);

        return delete;
    }
}
