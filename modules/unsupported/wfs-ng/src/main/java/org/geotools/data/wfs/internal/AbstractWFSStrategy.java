/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal;

import static org.geotools.data.wfs.internal.HttpMethod.GET;
import static org.geotools.data.wfs.internal.Loggers.requestDebug;
import static org.geotools.data.wfs.internal.Loggers.requestTrace;
import static org.geotools.data.wfs.internal.Loggers.trace;
import static org.geotools.data.wfs.internal.WFSOperationType.DESCRIBE_FEATURETYPE;
import static org.geotools.data.wfs.internal.WFSOperationType.DESCRIBE_STORED_QUERIES;
import static org.geotools.data.wfs.internal.WFSOperationType.LIST_STORED_QUERIES;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.wfs.internal.GetFeatureRequest.ResultType;
import org.geotools.filter.Capabilities;
import org.geotools.filter.visitor.CapabilitiesFilterSplitter;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.util.Version;
import org.geotools.wfs.v1_0.WFSConfiguration_1_0;
import org.geotools.xml.XMLHandlerHints;
import org.geotools.xml.filter.FilterCompliancePreProcessor;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.capability.SpatialCapabilities;
import org.opengis.filter.capability.SpatialOperators;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;
import org.opengis.filter.spatial.Intersects;

/**
 * Base template-method class for {@link WFSStrategy} implementations that leverage the GeoTools
 * {@code xml-xsd} subsystem for schema assisted parsing and encoding of WFS requests and responses.
 *
 * <p>A conformant WFS client implementation based on this abstract class should only need to
 * implement the following methods from {@link WFSStrategy}:
 *
 * <ul>
 *   <li>{@link #setCapabilities}
 *   <li>{@link #getFeatureTypeInfo}
 *   <li>{@link #getServerSupportedOutputFormats(WFSOperationType operation)}
 *   <li>{@link #getServerSupportedOutputFormats(QName typeName, WFSOperationType operation)}
 *   <li>{@link #getClientSupportedOutputFormats(WFSOperationType operation)}
 *   <li>{@link #getFeatureTypeNames()}
 *   <li>{@link #getFilterCapabilities()}
 *   <li>{@link #getSupportedCRSIdentifiers(QName typeName)}
 *   <li>{@link #supports(ResultType resultType)}
 *   <li>{@link #getServiceInfo()}
 * </ul>
 *
 * Plus the following template methods from this abstract class:
 *
 * <ul>
 *   <li>{@link #getFilterConfiguration}
 *   <li>{@link #getWfsConfiguration}
 *   <li>{@link #getOperationName}
 *   <li>{@link #createDescribeFeatureTypeRequestPost}
 *   <li>{@link #createGetFeatureRequestPost}
 *   <li>{@link #createTransactionRequest}
 * </ul>
 *
 * <p>Additionaly, specific strategy objects may override any other method to work around specific
 * service implementation oddities. To that end, the following methods might be of special interest:
 *
 * <ul>
 *   <li>{@link #buildDescribeFeatureTypeParametersForGET}
 *   <li>{@link #buildGetFeatureParametersForGET}
 * </ul>
 */
public abstract class AbstractWFSStrategy extends WFSStrategy {

    protected static final Logger LOGGER = Loggers.MODULE;

    public static final Configuration FILTER_1_0_CONFIGURATION =
            new org.geotools.filter.v1_0.OGCConfiguration();

    public static final Configuration WFS_1_0_CAPABILITIES_CONFIGURATION =
            new org.geotools.wfs.v1_0.WFSCapabilitiesConfiguration();

    public static final Configuration WFS_1_0_CONFIGURATION = new WFSConfiguration_1_0();

    public static final Configuration FILTER_1_1_CONFIGURATION =
            new org.geotools.filter.v1_1.OGCConfiguration();

    public static final Configuration WFS_1_1_CONFIGURATION =
            new org.geotools.wfs.v1_1.WFSConfiguration();

    public static final Configuration FILTER_2_0_CONFIGURATION =
            new org.geotools.filter.v2_0.FESConfiguration();

    public static final Configuration WFS_2_0_CONFIGURATION =
            new org.geotools.wfs.v2_0.WFSConfiguration();

    protected WFSConfig config;

    public AbstractWFSStrategy() {
        this.config = new WFSConfig();
    }

    /*
     * This class' extension points
     */

    /**
     * Used by {@link #getPostContents(WFSRequest)} to get the qualified operation name to encode;
     * different WFS versions may use different operation names (specially namespaces).
     */
    protected abstract QName getOperationName(WFSOperationType operation);

    /**
     * Creates the EMF object to be encoded with the {@link #getWfsConfiguration() WFS
     * configuration} when a DescribeFeatureType POST request is to be made.
     */
    protected abstract EObject createDescribeFeatureTypeRequestPost(
            DescribeFeatureTypeRequest request);

    /**
     * Creates the EMF object to be encoded with the {@link #getWfsConfiguration() WFS
     * configuration} when a GetFeature POST request is to be made.
     */
    protected abstract EObject createGetFeatureRequestPost(GetFeatureRequest query)
            throws IOException;

    /**
     * Creates the EMF object to be encoded with the {@link #getWfsConfiguration() WFS
     * configuration} when a Transaction request is to be made.
     */
    protected abstract EObject createTransactionRequest(TransactionRequest request)
            throws IOException;

    protected abstract EObject createListStoredQueriesRequestPost(ListStoredQueriesRequest request)
            throws IOException;

    protected abstract EObject createDescribeStoredQueriesRequestPost(
            DescribeStoredQueriesRequest request) throws IOException;

    /**
     * Returns the xml configuration used to encode a filter at {@link
     * #encodeGetFeatureGetFilter(Filter)}
     */
    protected abstract Configuration getFilterConfiguration();

    /**
     * Returns the xml configuration used to encode all POST requests.
     *
     * @see #getPostContents(WFSRequest)
     */
    public abstract Configuration getWfsConfiguration();

    /*
     * org.geotools.data.ows.Specification methods
     */

    @Override
    public String getVersion() {
        return getServiceVersion().toString();
    }

    /**
     * Factory method to create GetCapabilities Request
     *
     * @param server the URL that points to the server's getCapabilities document
     * @return a configured GetCapabilitiesRequest that can be used to access the Document
     */
    @Override
    public GetCapabilitiesRequest createGetCapabilitiesRequest(URL server) {
        return new GetCapabilitiesRequest(server);
    }

    /*
     * WFSStrategy methods
     */
    @Override
    public void setConfig(WFSConfig config) {
        this.config = config;
    }

    @Override
    public WFSConfig getConfig() {
        return config;
    }

    protected String encodePropertyName(String propertyName) {
        return propertyName;
    }

    protected Map<String, String> buildGetFeatureParametersForGET(GetFeatureRequest request) {
        requestDebug("Creating GetFeature request parameters for ", request.getTypeName());

        Map<String, String> map = new HashMap<String, String>();
        map.put("SERVICE", "WFS");
        Version serviceVersion = getServiceVersion();
        map.put("VERSION", serviceVersion.toString());
        map.put("REQUEST", "GetFeature");
        String outputFormat = request.getOutputFormat();
        if (outputFormat == null) {
            outputFormat = getDefaultOutputFormat(WFSOperationType.GET_FEATURE);
        }
        map.put("OUTPUTFORMAT", outputFormat);
        map.put("RESULTTYPE", request.getResultType().name());

        if (request.getMaxFeatures() != null) {
            map.put("MAXFEATURES", String.valueOf(request.getMaxFeatures()));
        }

        QName typeName = request.getTypeName();
        String queryTypeName = getPrefixedTypeName(typeName);
        map.put("TYPENAME", queryTypeName);

        if (request.getPropertyNames() != null && request.getPropertyNames().length > 0) {
            List<String> propertyNames = Arrays.asList(request.getPropertyNames());
            StringBuilder pnames = new StringBuilder();
            for (Iterator<String> it = propertyNames.iterator(); it.hasNext(); ) {
                pnames.append(it.next());
                if (it.hasNext()) {
                    pnames.append(',');
                }
            }
            map.put("PROPERTYNAME", encodePropertyName(pnames.toString()));
        }

        final String srsName = request.getSrsName();
        if (srsName != null) {
            map.put("SRSNAME", srsName.toString());
        }

        final Filter supportedFilter;
        final Filter unsupportedFilter;
        {
            final Filter filter = request.getFilter();
            Filter[] splitFilters = splitFilters(typeName, filter);
            supportedFilter = splitFilters[0];
            unsupportedFilter = splitFilters[1];
            requestTrace(
                    "Supported filter: ",
                    supportedFilter,
                    ". Unsupported filter: ",
                    unsupportedFilter);
        }

        request.setUnsupportedFilter(unsupportedFilter);

        if (supportedFilter instanceof Id) {
            final Set<Identifier> identifiers = ((Id) supportedFilter).getIdentifiers();
            StringBuffer idValues = new StringBuffer();
            for (Iterator<Identifier> it = identifiers.iterator(); it.hasNext(); ) {
                Object id = it.next().getID();
                if (id instanceof FeatureId) {
                    idValues.append(((FeatureId) id).getRid());
                } else {
                    idValues.append(String.valueOf(id));
                }
                if (it.hasNext()) {
                    idValues.append(",");
                }
            }
            map.put("FEATUREID", idValues.toString());
        } else if (Filter.INCLUDE != supportedFilter) {
            String xmlEncodedFilter;
            try {
                xmlEncodedFilter = encodeGetFeatureGetFilter(supportedFilter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            map.put("FILTER", xmlEncodedFilter);
        }

        return map;
    }

    /**
     * Returns a single-line string containing the xml representation of the given filter, as
     * appropriate for the {@code FILTER} parameter in a GetFeature request.
     */
    protected String encodeGetFeatureGetFilter(final Filter filter) throws IOException {

        final Configuration filterConfig = getFilterConfiguration();
        final QName encName;

        if (filterConfig instanceof org.geotools.filter.v1_0.OGCConfiguration
                || filterConfig instanceof org.geotools.filter.v1_1.OGCConfiguration) {
            encName = org.geotools.filter.v1_0.OGC.Filter;
        } else {
            encName = org.geotools.filter.v2_0.FES.Filter;
        }

        Encoder encoder = new Encoder(filterConfig);
        // do not write the xml declaration
        encoder.setOmitXMLDeclaration(true);
        encoder.setEncoding(Charset.forName("UTF-8"));

        String encoded = encoder.encodeAsString(filter, encName);

        encoded = encoded.replaceAll("\n", "");
        return encoded;
    }

    /*---------------------------------------------------------------------
     * WFSStrategy methods
     * ---------------------------------------------------------------------*/

    /** @see WFSStrategy#getServiceVersion() */
    @Override
    public abstract Version getServiceVersion();

    protected String getPrefixedTypeName(QName qname) {
        String prefix = qname.getPrefix();
        String simpleName;
        if (XMLConstants.DEFAULT_NS_PREFIX.equals(prefix)) {
            simpleName = qname.getLocalPart();
        } else {
            simpleName = prefix + ":" + qname.getLocalPart();
        }
        return simpleName;
    }

    /** @see WFSStrategy#supportsOperation */
    @Override
    public boolean supportsOperation(WFSOperationType operation, HttpMethod method) {
        return null != getOperationURI(operation, method);
    }

    /** @see WFSStrategy#getOperationURL */
    @Override
    public URL getOperationURL(WFSOperationType operation, HttpMethod method) {
        String href = getOperationURI(operation, method);
        if (href != null) {
            try {
                return new URL(href);
            } catch (MalformedURLException e) {
                // Log error and let the search continue
                LOGGER.log(Level.INFO, "Malformed " + method + " URL for " + operation, e);
            }
        }
        return null;
    }

    protected abstract String getOperationURI(WFSOperationType operation, HttpMethod method);

    @Override
    public String getDefaultOutputFormat(WFSOperationType operation) {
        Set<String> supportedOutputFormats = getServerSupportedOutputFormats(operation);
        String overrideOutputFormat = config.getOutputformatOverride();
        if (overrideOutputFormat != null && supportedOutputFormats.contains(overrideOutputFormat)) {
            return overrideOutputFormat;
        }

        List<String> clientSupportedFormats = getClientSupportedOutputFormats(operation);
        for (String clientSupported : clientSupportedFormats) {
            if (supportedOutputFormats.contains(clientSupported)) {
                return clientSupported;
            }
        }

        throw new IllegalArgumentException(
                "Client does not support any of the server supported output formats for "
                        + operation);
    }

    /** @see WFSStrategy#dispose() */
    @Override
    public void dispose() {
        // do nothing
    }

    protected Map<String, String> buildDescribeFeatureTypeParametersForGET(
            final DescribeFeatureTypeRequest request) {

        final QName typeName = request.getTypeName();

        // final String outputFormat = getDefaultOutputFormat(DESCRIBE_FEATURETYPE);
        try {
            getFeatureTypeInfo(typeName);
        } catch (RuntimeException e) {
            throw e;
        }

        Map<String, String> kvp = new HashMap<String, String>();
        kvp.put("SERVICE", "WFS");
        kvp.put("VERSION", getServiceVersion().toString());
        kvp.put("REQUEST", "DescribeFeatureType");
        // ommit output format by now, server should just return xml shcema
        // kvp.put("OUTPUTFORMAT", outputFormat);

        return buildDescribeFeatureTypeParametersForGET(kvp, typeName);
    }

    protected Map<String, String> buildDescribeFeatureTypeParametersForGET(
            Map<String, String> kvp, QName typeName) {
        String prefixedTypeName = getPrefixedTypeName(typeName);

        kvp.put("TYPENAME", prefixedTypeName);

        if (!XMLConstants.DEFAULT_NS_PREFIX.equals(typeName.getPrefix())) {
            String nsUri = typeName.getNamespaceURI();
            kvp.put("NAMESPACE", "xmlns(" + typeName.getPrefix() + "=" + nsUri + ")");
        }
        return kvp;
    }

    protected Map<String, String> buildDescribeStoredQueriesParametersForGET(
            final DescribeStoredQueriesRequest request) {

        Map<String, String> kvp = new HashMap<String, String>();
        kvp.put("SERVICE", "WFS");
        kvp.put("VERSION", getServiceVersion().toString());
        kvp.put("REQUEST", "DescribeStoredQueries");

        if (request.getStoredQueryIds().size() > 0) {
            StringBuffer sb = new StringBuffer();
            boolean first = true;
            for (URI storedQueryId : request.getStoredQueryIds()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(storedQueryId.toString());
            }
            kvp.put("STOREDQUERY_ID", sb.toString());
        }

        return kvp;
    }

    protected Map<String, String> buildListStoredQueriesParametersForGET(
            ListStoredQueriesRequest request) {
        Map<String, String> kvp = new HashMap<String, String>();
        kvp.put("SERVICE", "WFS");
        kvp.put("VERSION", getServiceVersion().toString());
        kvp.put("REQUEST", "ListStoredQueries");

        return kvp;
    }

    // private WFSResponse issueGetRequest(EObject request, URL baseUrl, Map<String, String> kvp)
    // throws IOException {
    // WFSResponse response;
    // URL url = URIs.buildURL(baseUrl, kvp);
    // HTTPResponse httpResponse = http.get(url);
    //
    // String responseCharset = httpResponse.getResponseHeader("charset");
    // Charset charset = responseCharset == null ? null : Charset.forName(responseCharset);
    // String contentType = httpResponse.getContentType();
    // InputStream responseStream = httpResponse.getResponseStream();
    // String target = url.toExternalForm();
    // response = new WFSResponse(target, request, charset, contentType, responseStream);
    // return response;
    // }

    // private WFSResponse issuePostRequest(final EObject request, final URL url, final Encoder
    // encoder)
    // throws IOException {
    //
    // final Charset requestCharset = config.getDefaultEncoding();
    // encoder.setEncoding(requestCharset);
    // ByteArrayOutputStream out = new ByteArrayOutputStream();
    // if (LOGGER.isLoggable(Level.FINEST)) {
    // LOGGER.finest("Sending POST request: ");
    // LOGGER.finest(out.toString(requestCharset.name()));
    // }
    // AbstractWFSStrategy.encode(request, encoder, out);
    // InputStream postContent = new ByteArrayInputStream(out.toByteArray());
    // HTTPResponse httpResponse = http.post(url, postContent, "text/xml");
    //
    // String responseCharset = httpResponse.getResponseHeader("charset");
    // Charset charset = responseCharset == null ? null : Charset.forName(responseCharset);
    // String contentType = httpResponse.getContentType();
    // InputStream responseStream = httpResponse.getResponseStream();
    // String target = url.toExternalForm();
    // WFSResponse response = new WFSResponse(target, request, charset, contentType,
    // responseStream);
    // return response;
    // }

    // /**
    // * Returns the operation URI for the given operation/http method as a String to avoid creating
    // a
    // * URL instance when not needed
    // */
    // @SuppressWarnings("unchecked")
    // private String getOperationURI(WFSOperationType operation, HttpMethod method) {
    // final OperationType operationType = getOperationMetadata(operation);
    // final List<DCPType> dcps = operationType.getDCP();
    // for (DCPType dcp : dcps) {
    // List<RequestMethodType> requests;
    // if (GET == method) {
    // requests = dcp.getHTTP().getGet();
    // } else {
    // requests = dcp.getHTTP().getPost();
    // }
    // for (RequestMethodType req : requests) {
    // String href = req.getHref();
    // return href;
    // }
    // }
    // return null;
    // }

    protected Encoder prepareEncoder(WFSRequest request) {
        final Configuration configuration = getWfsConfiguration();
        Charset charset = getConfig().getDefaultEncoding();
        if (null == charset) {
            charset = Charset.forName("UTF-8");
        }
        Encoder encoder = new Encoder(configuration);
        encoder.setEncoding(charset);
        encoder.setIndentSize(1);

        Set<QName> typeNames = new HashSet<QName>();

        if (request instanceof TransactionRequest) {
            TransactionRequest tx = (TransactionRequest) request;
            typeNames.addAll(tx.getTypeNames());
        } else {
            QName typeName = request.getTypeName();
            if (typeName != null) {
                typeNames.add(typeName);
            }
        }
        int i = 0;
        for (QName typeName : typeNames) {

            String prefix = typeName.getPrefix();
            String namespaceURI = typeName.getNamespaceURI();

            if (XMLConstants.NULL_NS_URI.equals(namespaceURI)) {
                continue;
            }

            if (XMLConstants.DEFAULT_NS_PREFIX.equals(prefix)) {
                prefix = "type_ns" + i;
            }

            encoder.getNamespaces().declarePrefix(prefix, namespaceURI);

            i++;
        }
        return encoder;
    }

    /**
     * Splits the filter provided by the geotools query into the server supported and unsupported
     * ones.
     *
     * @return a two-element array where the first element is the supported filter and the second
     *     the one to post-process
     * @see org.geotools.data.wfs.internal.WFSStrategy#splitFilters(org.opengis.filter.Filter)
     */
    @Override
    public Filter[] splitFilters(QName typeName, Filter filter) {

        FilterCapabilities filterCapabilities = getFilterCapabilities();
        Capabilities filterCaps = new Capabilities();
        if (filterCapabilities != null) {
            filterCaps.addAll(filterCapabilities);
            /*
             * General Fix for WFS 1.0 naming the "Intersects" spatial operation "Intersect", which
             * will make the CapabilitiesFilterSplitter think Intersects is not supported at
             * splitFilters
             */
            if (Versions.v1_0_0.equals(getServiceVersion())) {
                SpatialCapabilities spatialCaps = filterCapabilities.getSpatialCapabilities();
                if (spatialCaps != null) {
                    SpatialOperators spatialOps = spatialCaps.getSpatialOperators();
                    if (spatialOps != null) {
                        if (null != spatialOps.getOperator("Intersect")) {
                            trace(
                                    "WFS 1.0 capabilities states the spatial operator Intersect. ",
                                    "Assuming it is Intersects and adding Intersects as a supported filter type");
                            filterCaps.addName(Intersects.NAME);
                        }
                    }
                }
            }
        }
        filter = simplify(filter);

        Filter server;
        Filter post;

        Integer complianceLevel = getConfig().getFilterCompliance();
        if (null == complianceLevel) {
            complianceLevel = XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH;
        }
        // TODO: modify FilterCompliancePreProcessor so that it preservers original FeatureID
        // instead re creating them from the FeautreId.getID()
        FilterCompliancePreProcessor compliancePreProcessor =
                new FilterCompliancePreProcessor(complianceLevel);
        filter.accept(compliancePreProcessor, null);

        filter = compliancePreProcessor.getFilter();
        Id fidFilter = compliancePreProcessor.getFidFilter();
        if (!fidFilter.getIdentifiers().isEmpty()) {
            server = fidFilter;
            post = Filter.EXCLUDE.equals(filter) ? Filter.INCLUDE : filter;
        } else {

            CapabilitiesFilterSplitter splitter =
                    new CapabilitiesFilterSplitter(filterCaps, null, null);

            filter.accept(splitter, null);
            server = splitter.getFilterPre();
            post = splitter.getFilterPost();
        }
        return new Filter[] {server, post};
    }

    protected Filter simplify(Filter filter) {
        if (Filter.INCLUDE.equals(filter) || Filter.EXCLUDE.equals(filter)) {
            return filter;
        }

        SimplifyingFilterVisitor simplifier = new SimplifyingFilterVisitor();
        filter = (Filter) filter.accept(simplifier, null);
        return filter;
    }

    @Override
    public URL buildUrlGET(WFSRequest request) {
        final WFSOperationType operation = request.getOperation();

        Map<String, String> requestParams;

        switch (operation) {
            case GET_FEATURE:
                requestParams = buildGetFeatureParametersForGET((GetFeatureRequest) request);
                break;
            case DESCRIBE_FEATURETYPE:
                requestParams =
                        buildDescribeFeatureTypeParametersForGET(
                                (DescribeFeatureTypeRequest) request);
                break;
            case DESCRIBE_STORED_QUERIES:
                requestParams =
                        buildDescribeStoredQueriesParametersForGET(
                                (DescribeStoredQueriesRequest) request);
                break;
            case LIST_STORED_QUERIES:
                requestParams =
                        buildListStoredQueriesParametersForGET((ListStoredQueriesRequest) request);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        URL baseUrl = getOperationURL(operation, GET);

        URL finalURL = URIs.buildURL(baseUrl, requestParams);
        requestDebug("Built GET request for ", operation, ": ", finalURL);

        return finalURL;
    }

    @Override
    public String getPostContentType(WFSRequest wfsRequest) {

        final WFSOperationType operation = wfsRequest.getOperation();

        if (DESCRIBE_FEATURETYPE.equals(operation)
                || DESCRIBE_STORED_QUERIES.equals(operation)
                || LIST_STORED_QUERIES.equals(operation)) {
            return "text/xml";
        }

        String defaultOutputFormat = getDefaultOutputFormat(operation);

        return defaultOutputFormat;
    }

    /**
     * Returns the input stream with the POST body contents for the given request.
     *
     * <p>
     *
     * @see #createDescribeFeatureTypeRequestPost
     * @see #createGetFeatureRequestPost
     * @see #prepareEncoder
     * @see #getOperationName
     */
    @Override
    public InputStream getPostContents(WFSRequest request) throws IOException {
        EObject requestObject;

        switch (request.getOperation()) {
            case DESCRIBE_FEATURETYPE:
                requestObject =
                        createDescribeFeatureTypeRequestPost((DescribeFeatureTypeRequest) request);
                break;
            case DESCRIBE_STORED_QUERIES:
                requestObject =
                        createDescribeStoredQueriesRequestPost(
                                (DescribeStoredQueriesRequest) request);
                break;
            case GET_FEATURE:
                requestObject = createGetFeatureRequestPost((GetFeatureRequest) request);
                break;
            case LIST_STORED_QUERIES:
                requestObject =
                        createListStoredQueriesRequestPost((ListStoredQueriesRequest) request);
                break;
            case TRANSACTION:
                requestObject = createTransactionRequest((TransactionRequest) request);
                break;
            default:
                throw new UnsupportedOperationException(
                        "not yet implemented for " + request.getOperation());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        final Encoder encoder = prepareEncoder(request);
        final QName opName = getOperationName(request.getOperation());

        encoder.encode(requestObject, opName, out);

        requestTrace("Encoded ", request.getOperation(), " request: ", out);

        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public Map<QName, Class<?>> getFieldTypeMappings() {
        return null;
    }
}
