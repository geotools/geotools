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

import static org.geotools.data.wfs.protocol.http.HttpMethod.GET;
import static org.geotools.data.wfs.protocol.http.HttpMethod.POST;
import static org.geotools.data.wfs.protocol.wfs.WFSOperationType.DESCRIBE_FEATURETYPE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import net.opengis.ows10.DCPType;
import net.opengis.ows10.DomainType;
import net.opengis.ows10.KeywordsType;
import net.opengis.ows10.OnlineResourceType;
import net.opengis.ows10.OperationType;
import net.opengis.ows10.OperationsMetadataType;
import net.opengis.ows10.Ows10Factory;
import net.opengis.ows10.RequestMethodType;
import net.opengis.ows10.ServiceIdentificationType;
import net.opengis.ows10.ServiceProviderType;
import net.opengis.ows10.WGS84BoundingBoxType;
import net.opengis.wfs.DescribeFeatureTypeType;
import net.opengis.wfs.FeatureTypeType;
import net.opengis.wfs.GetCapabilitiesType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.GetGmlObjectType;
import net.opengis.wfs.LockFeatureType;
import net.opengis.wfs.OutputFormatListType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.WFSCapabilitiesType;

import org.eclipse.emf.ecore.EObject;
import org.geotools.data.DataSourceException;
import org.geotools.data.wfs.protocol.http.HTTPProtocol;
import org.geotools.data.wfs.protocol.http.HTTPResponse;
import org.geotools.data.wfs.protocol.http.HttpMethod;
import org.geotools.data.wfs.protocol.http.HTTPProtocol.POSTCallBack;
import org.geotools.data.wfs.protocol.wfs.GetFeature;
import org.geotools.data.wfs.protocol.wfs.Version;
import org.geotools.data.wfs.protocol.wfs.WFSOperationType;
import org.geotools.data.wfs.protocol.wfs.WFSProtocol;
import org.geotools.data.wfs.protocol.wfs.WFSResponse;
import org.geotools.data.wfs.v1_1_0.WFSStrategy.RequestComponents;
import org.geotools.filter.Capabilities;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;
import org.geotools.wfs.WFS;
import org.geotools.xml.Configuration;
import org.geotools.xml.Encoder;
import org.geotools.xml.Parser;
import org.opengis.filter.Filter;
import org.opengis.filter.capability.FilterCapabilities;
import org.xml.sax.SAXException;

/**
 * {@link WFSProtocol} implementation to talk to a WFS 1.1.0 server leveraging the GeoTools {@code
 * xml-xsd} subsystem for schema assisted parsing and encoding of WFS requests and responses.
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id$
 * @since 2.6
 *
 * @source $URL$
 *         http://gtsvn.refractions.net/trunk/modules/plugin/wfs/src/main/java/org/geotools/data
 *         /wfs/v1_1_0/WFS_1_1_0_Protocol.java $
 */
@SuppressWarnings( { "unchecked", "nls" })
public class WFS_1_1_0_Protocol implements WFSProtocol {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.wfs");

    private WFSStrategy strategy;

    /**
     * The WFS GetCapabilities document. Final by now, as we're not handling updatesequence, so will
     * not ask the server for an updated capabilities during the life-time of this datastore.
     */
    final WFSCapabilitiesType capabilities;

    /**
     * Per featuretype name Map of capabilities feature type information. Not to be used directly
     * but through {@link #getFeatureTypeInfo(String)}
     */
    private final Map<String, FeatureTypeType> typeInfos;

    private HTTPProtocol http;

    public WFS_1_1_0_Protocol(InputStream capabilitiesReader, HTTPProtocol http) throws IOException {
        this.strategy = new DefaultWFSStrategy();
        this.capabilities = parseCapabilities(capabilitiesReader);
        this.http = http;
        this.typeInfos = new HashMap<String, FeatureTypeType>();

        final List<FeatureTypeType> ftypes = capabilities.getFeatureTypeList().getFeatureType();
        QName typeName;
        for (FeatureTypeType ftype : ftypes) {
            typeName = ftype.getName();
            assert !("".equals(typeName.getPrefix()));
            String prefixedTypeName = typeName.getPrefix() + ":" + typeName.getLocalPart();
            typeInfos.put(prefixedTypeName, ftype);
        }
    }

    public void setStrategy(WFSStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * @return {@link Version#v1_1_0}
     * @see WFSProtocol#getServiceVersion()
     */
    public Version getServiceVersion() {
        return Version.v1_1_0;
    }

    /**
     * @see WFSProtocol#getServiceTitle()
     */
    public String getServiceTitle() {
        return getServiceIdentification().getTitle();
    }

    /**
     * @see WFSProtocol#getServiceAbstract()
     */
    public String getServiceAbstract() {
        return getServiceIdentification().getAbstract();
    }

    /**
     * @see WFSProtocol#getServiceKeywords()
     */
    public Set<String> getServiceKeywords() {
        List<KeywordsType> capsKeywords = getServiceIdentification().getKeywords();
        return extractKeywords(capsKeywords);
    }

    private ServiceIdentificationType getServiceIdentification() {
        ServiceIdentificationType serviceId = capabilities.getServiceIdentification();
        if (serviceId == null) {
            LOGGER.info("Capabilities did not provide a ServiceIdentification section");
            serviceId = Ows10Factory.eINSTANCE.createServiceIdentificationType();
            capabilities.setServiceIdentification(serviceId);
        }
        return serviceId;
    }

    /**
     * @see WFSProtocol#getServiceProviderUri()
     */
    public URI getServiceProviderUri() {
        ServiceProviderType serviceProvider = capabilities.getServiceProvider();
        if (serviceProvider == null) {
            return null;
        }
        OnlineResourceType providerSite = serviceProvider.getProviderSite();
        if (providerSite == null) {
            return null;
        }
        String href = providerSite.getHref();
        if (href == null) {
            return null;
        }
        try {
            return new URI(href);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * @see WFSProtocol#getSupportedGetFeatureOutputFormats()
     */
    public Set<String> getSupportedGetFeatureOutputFormats() {
        OperationType operationMetadata = getOperationMetadata(WFSOperationType.GET_FEATURE);
        List<DomainType> parameters = operationMetadata.getParameter();
        List featuretypes = capabilities.getFeatureTypeList().getFeatureType();

        List supportedByAllFeatureTypes= null;
        for (int i=0; i < featuretypes.size() ; i++){
            net.opengis.wfs.FeatureTypeType ft = (FeatureTypeType) featuretypes.get(i);
            if (ft.getOutputFormats()!=null){
                List value=ft.getOutputFormats().getFormat();
                if (supportedByAllFeatureTypes ==null){
                    supportedByAllFeatureTypes=value;
                }else{
                    List removeOutputFormats= new ArrayList();
                    for (Object o : supportedByAllFeatureTypes){
                        if (!value.contains(o)){
                            removeOutputFormats.add(o);
                        }
                    }
                    for (Object o : removeOutputFormats){
                       supportedByAllFeatureTypes.remove(o);
                    }
                    if (supportedByAllFeatureTypes.size() ==0){
                        break;
                    }
                }
            }
        }

        Set<String> outputFormats = new HashSet<String>();
        for (DomainType param : parameters) {
            String paramName = param.getName();
            if ("outputFormat".equals(paramName)) {
                List value = param.getValue();
                outputFormats.addAll(value);
            }
        }
        if (supportedByAllFeatureTypes!=null)
            outputFormats.addAll(supportedByAllFeatureTypes);
        return outputFormats;
    }

    /**
     * @see WFSProtocol#getSupportedOutputFormats(String)
     */
    public Set<String> getSupportedOutputFormats(String typeName) {
        final Set<String> serviceOutputFormats = getSupportedGetFeatureOutputFormats();
        final FeatureTypeType typeInfo = getFeatureTypeInfo(typeName);
        final OutputFormatListType outputFormats = typeInfo.getOutputFormats();

        Set<String> ftypeFormats = new HashSet<String>();
        if (outputFormats != null) {
            List<String> ftypeDeclaredFormats = outputFormats.getFormat();
            ftypeFormats.addAll(ftypeDeclaredFormats);
        }

        ftypeFormats.addAll(serviceOutputFormats);
        return ftypeFormats;
    }

    /**
     * @see WFSProtocol#getFeatureTypeNames()
     */
    public Set<QName> getFeatureTypeNames() {
        Set<QName> typeNames = new HashSet<QName>();
        for (FeatureTypeType typeInfo : typeInfos.values()) {
            QName name = typeInfo.getName();
            typeNames.add(name);
        }
        return typeNames;
    }

    /**
     * @see WFSProtocol#getFeatureTypeName(String)
     */
    public QName getFeatureTypeName(String typeName) {
        FeatureTypeType featureTypeInfo = getFeatureTypeInfo(typeName);
        QName name = featureTypeInfo.getName();
        return name;
    }

    /**
     * @see WFSProtocol#getFilterCapabilities()
     */
    public FilterCapabilities getFilterCapabilities() {
        FilterCapabilities wfsFilterCapabilities;
        wfsFilterCapabilities = capabilities.getFilterCapabilities();
        return wfsFilterCapabilities;
    }

    /**
     * @see WFSProtocol#supportsOperation(WFSOperationType, boolean)
     */
    public boolean supportsOperation(WFSOperationType operation, boolean post) {
        if (post && !strategy.supportsPost()) {
            return false;
        }
        if (!post && !strategy.supportsGet()) {
            return false;
        }

        HttpMethod method = post ? POST : GET;
        return null != getOperationURI(operation, method);
    }

    /**
     * @see WFSProtocol#getOperationURL(WFSOperationType, boolean)
     */
    public URL getOperationURL(WFSOperationType operation, boolean post) {
        HttpMethod method = post ? POST : GET;
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

    /**
     * @see WFSProtocol#getFeatureTypeTitle(String)
     */
    public String getFeatureTypeTitle(String typeName) {
        FeatureTypeType featureTypeInfo = getFeatureTypeInfo(typeName);
        return featureTypeInfo.getTitle();
    }

    /**
     * @see WFSProtocol#getFeatureTypeAbstract(String)
     */
    public String getFeatureTypeAbstract(String typeName) {
        FeatureTypeType featureTypeInfo = getFeatureTypeInfo(typeName);
        return featureTypeInfo.getAbstract();
    }

    /**
     * @see WFSProtocol#getFeatureTypeWGS84Bounds(String)
     */
    public ReferencedEnvelope getFeatureTypeWGS84Bounds(String typeName) {
        final FeatureTypeType featureTypeInfo = getFeatureTypeInfo(typeName);
        List<WGS84BoundingBoxType> bboxList = featureTypeInfo.getWGS84BoundingBox();
        if (bboxList != null && bboxList.size() > 0) {
            WGS84BoundingBoxType bboxType = bboxList.get(0);
            List lowerCorner = bboxType.getLowerCorner();
            List upperCorner = bboxType.getUpperCorner();
            double minLon = (Double) lowerCorner.get(0);
            double minLat = (Double) lowerCorner.get(1);
            double maxLon = (Double) upperCorner.get(0);
            double maxLat = (Double) upperCorner.get(1);

            ReferencedEnvelope latLonBounds = new ReferencedEnvelope(minLon, maxLon, minLat,
                    maxLat, DefaultGeographicCRS.WGS84);

            return latLonBounds;
        }
        throw new IllegalStateException(
                "The capabilities document does not supply the ows:WGS84BoundingBox element");
    }

    /**
     * @see WFSProtocol#getDefaultCRS(String)
     */
    public String getDefaultCRS(String typeName) {
        FeatureTypeType featureTypeInfo = getFeatureTypeInfo(typeName);
        String defaultSRS = featureTypeInfo.getDefaultSRS();
        return defaultSRS;
    }

    /**
     * @see WFSProtocol#getSupportedCRSIdentifiers(String)
     */
    public Set<String> getSupportedCRSIdentifiers(String typeName) {
        FeatureTypeType featureTypeInfo = getFeatureTypeInfo(typeName);
        // TODO: another wrong emf mapping: getOtherSRS():String? should be a list
        String defaultSRS = featureTypeInfo.getDefaultSRS();
        List<String> otherSRS = featureTypeInfo.getOtherSRS();

        Set<String> ftypeCrss = new HashSet<String>();
        ftypeCrss.add(defaultSRS);
        ftypeCrss.addAll(otherSRS);
        return ftypeCrss;
    }

    /**
     * @see WFSProtocol#getFeatureTypeKeywords(String)
     */
    public Set<String> getFeatureTypeKeywords(String typeName) {
        FeatureTypeType featureTypeInfo = getFeatureTypeInfo(typeName);
        List<KeywordsType> ftKeywords = featureTypeInfo.getKeywords();
        Set<String> ftypeKeywords = extractKeywords(ftKeywords);
        return ftypeKeywords;
    }

    /**
     * @see WFSProtocol#getDescribeFeatureTypeURLGet(String)
     */
    public URL getDescribeFeatureTypeURLGet(String typeName) {
        final String outputFormat = "text/xml; subtype=gml/3.1.1";
        return getDescribeFeatureTypeURLGet(typeName, outputFormat);
    }

    /**
     * @throws IOException
     * @see WFSProtocol#describeFeatureTypeGET(String, String)
     */
    public WFSResponse describeFeatureTypeGET(String typeName, String outputFormat)
            throws IOException {
        if (!supportsOperation(DESCRIBE_FEATURETYPE, false)) {
            throw new UnsupportedOperationException(
                    "The server does not support DescribeFeatureType for HTTP method GET");
        }

        URL url = getDescribeFeatureTypeURLGet(typeName, outputFormat);
        WFSResponse response = issueGetRequest(null, url, Collections.EMPTY_MAP);
        return response;
    }

    /**
     * @throws IOException
     * @see WFSProtocol#describeFeatureTypePOST(String, String)
     */
    public WFSResponse describeFeatureTypePOST(String typeName, String outputFormat)
            throws IOException {
        throw new UnsupportedOperationException("POST not implemented yet for DescribeFeatureType");

    }

    /**
     * @see WFSProtocol#issueGetFeatureGET(GetFeatureType, Map)
     */
    public WFSResponse issueGetFeatureGET(final GetFeature request) throws IOException {
        if (!supportsOperation(WFSOperationType.GET_FEATURE, false)) {
            throw new UnsupportedOperationException(
                    "The server does not support GetFeature for HTTP method GET");
        }
        URL url = getOperationURL(WFSOperationType.GET_FEATURE, false);

        RequestComponents reqParts = strategy.createGetFeatureRequest(this, request);
        Map<String, String> getFeatureKvp = reqParts.getKvpParameters();
        GetFeatureType requestType = reqParts.getServerRequest();

        System.out.println(" > getFeatureGET: Request url: " + url + ". Parameters: "
                + getFeatureKvp);
        WFSResponse response = issueGetRequest(requestType, url, getFeatureKvp);

        return response;
    }

    /**
     * @see WFSProtocol#getFeaturePOST(Query, String)
     */
    public WFSResponse issueGetFeaturePOST(final GetFeature request) throws IOException {
        if (!supportsOperation(WFSOperationType.GET_FEATURE, true)) {
            throw new UnsupportedOperationException(
                    "The server does not support GetFeature for HTTP method POST");
        }
        URL url = getOperationURL(WFSOperationType.GET_FEATURE, true);

        RequestComponents reqParts = strategy.createGetFeatureRequest(this, request);
        GetFeatureType serverRequest = reqParts.getServerRequest();

        Encoder encoder = new Encoder(strategy.getWfsConfiguration());

        // If the typeName is of the form prefix:typeName we better declare the namespace since we
        // don't know how picky the server parser will be
        String typeName = reqParts.getKvpParameters().get("TYPENAME");
        QName fullName = getFeatureTypeName(typeName);
        String prefix = fullName.getPrefix();
        String namespace = fullName.getNamespaceURI();
        if (!XMLConstants.DEFAULT_NS_PREFIX.equals(prefix)) {
            encoder.getNamespaces().declarePrefix(prefix, namespace);
        }
        WFSResponse response = issuePostRequest(serverRequest, url, encoder);

        return response;
    }

    /**
     * @see WFSProtocol#dispose()
     */
    public void dispose() {
        // do nothing
    }

    /**
     * Returns the feature type metadata object parsed from the capabilities document for the given
     * {@code typeName}
     * <p>
     * NOTE: this method is package protected just to be also accessed by unit test.
     * </p>
     * 
     * @param typeName
     *            the typeName as stated in the capabilities {@code FeatureTypeList} to get the info
     *            for
     * @return the WFS capabilities metadata {@link FeatureTypeType metadata} for {@code typeName}
     * @throws IllegalArgumentException
     *             if {@code typeName} is not the name of a FeatureType stated in the capabilities
     *             document.
     */
    private FeatureTypeType getFeatureTypeInfo(final String typeName) {
        if (!typeInfos.containsKey(typeName)) {
            throw new IllegalArgumentException("Type name not found: " + typeName);
        }
        return typeInfos.get(typeName);
    }

    private WFSCapabilitiesType parseCapabilities(InputStream capabilitiesReader)
            throws IOException {
        final Configuration wfsConfig = strategy.getWfsConfiguration();
        final Parser parser = new Parser(wfsConfig);
        final Object parsed;
        try {
            parsed = parser.parse(capabilitiesReader);
        } catch (SAXException e) {
            throw new DataSourceException("Exception parsing WFS 1.1.0 capabilities", e);
        } catch (ParserConfigurationException e) {
            throw new DataSourceException("WFS 1.1.0 parsing configuration error", e);
        }
        if (parsed == null) {
            throw new DataSourceException("WFS 1.1.0 capabilities was not parsed");
        }
        if (!(parsed instanceof WFSCapabilitiesType)) {
            throw new DataSourceException("Expected WFS Capabilities, got " + parsed);
        }
        return (WFSCapabilitiesType) parsed;
    }

    private Set<String> extractKeywords(List<KeywordsType> keywordsList) {
        Set<String> keywords = new HashSet<String>();
        for (KeywordsType keys : keywordsList) {
            keywords.addAll(keys.getKeyword());
        }
        return keywords;
    }

    private OperationType getOperationMetadata(WFSOperationType operation) {
        final OperationsMetadataType operationsMetadata = capabilities.getOperationsMetadata();
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

    private URL getDescribeFeatureTypeURLGet(String typeName, String outputFormat) {
        final FeatureTypeType typeInfo = getFeatureTypeInfo(typeName);

        final URL describeFeatureTypeUrl = getOperationURL(DESCRIBE_FEATURETYPE, false);

        Map<String, String> kvp = new HashMap<String, String>();
        kvp.put("SERVICE", "WFS");
        kvp.put("VERSION", getServiceVersion().toString());
        kvp.put("REQUEST", "DescribeFeatureType");
        kvp.put("TYPENAME", typeName);

        QName name = typeInfo.getName();
        if (!XMLConstants.DEFAULT_NS_PREFIX.equals(name.getPrefix())) {
            String nsUri = name.getNamespaceURI();
            kvp.put("NAMESPACE", "xmlns(" + name.getPrefix() + "=" + nsUri + ")");
        }

        // ommit output format by now, server should just return xml shcema
        // kvp.put("OUTPUTFORMAT", outputFormat);

        URL url;
        try {
            url = http.createUrl(describeFeatureTypeUrl, kvp);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return url;
    }

    private WFSResponse issueGetRequest(EObject request, URL url, Map<String, String> kvp)
            throws IOException {
        WFSResponse response;
        HTTPResponse httpResponse = http.issueGet(url, kvp);

        String responseCharset = httpResponse.getResponseCharset();
        Charset charset = responseCharset == null ? null : Charset.forName(responseCharset);
        String contentType = httpResponse.getContentType();
        InputStream responseStream = httpResponse.getResponseStream();
        String target = httpResponse.getTargetUrl();
        response = new WFSResponse(target, request, charset, contentType, responseStream);
        return response;
    }

    private WFSResponse issuePostRequest(final EObject request, final URL url, final Encoder encoder)
            throws IOException {

        final POSTCallBack requestBodyCallback = new POSTCallBack() {
            public long getContentLength() {
                // don't know
                return -1;
            }

            public String getContentType() {
                return "text/xml";
            }

            public void writeBody(final OutputStream out) throws IOException {
                final Charset charset = Charset.forName("UTF-8");
                encoder.setEncoding(charset);
                // if (LOGGER.isLoggable(Level.FINEST)) {
                // System.err.println("Sending POST request: ");
                // WFS_1_1_0_Protocol.encode(request, wfsConfig, System.err, charset);
                // }
                WFS_1_1_0_Protocol.encode(request, encoder, out);
            }
        };

        HTTPResponse httpResponse = http.issuePost(url, requestBodyCallback);

        String responseCharset = httpResponse.getResponseCharset();
        Charset charset = responseCharset == null ? null : Charset.forName(responseCharset);
        String contentType = httpResponse.getContentType();
        InputStream responseStream = httpResponse.getResponseStream();
        String target = httpResponse.getTargetUrl();
        WFSResponse response = new WFSResponse(target, request, charset, contentType,
                responseStream);
        return response;
    }

    /**
     * Returns the operation URI for the given operation/http method as a String to avoid creating a
     * URL instance when not needed
     */
    private String getOperationURI(WFSOperationType operation, HttpMethod method) {
        final OperationType operationType = getOperationMetadata(operation);
        final List<DCPType> dcps = operationType.getDCP();
        for (DCPType dcp : dcps) {
            List<RequestMethodType> requests;
            if (GET == method) {
                requests = dcp.getHTTP().getGet();
            } else {
                requests = dcp.getHTTP().getPost();
            }
            for (RequestMethodType req : requests) {
                String href = req.getHref();
                return href;
            }
        }
        return null;
    }

    /**
     * Encodes a WFS request into {@code out}
     * 
     * @param request
     *            one of {@link GetCapabilitiesType}, {@link GetFeatureType}, etc
     * @param configuration
     *            the wfs configuration to use for encoding the request into the output stream
     * @param out
     *            the output stream where to encode the request into
     * @param charset
     *            the charset to use to encode the request in
     * @throws IOException
     */
    public static void encode(final EObject request, final Configuration configuration,
            final OutputStream out, final Charset charset) throws IOException {
        Encoder encoder = new Encoder(configuration);
        encoder.setEncoding(charset);
        encode(request, encoder, out);
    }

    private static void encode(EObject request, Encoder encoder, OutputStream out)
            throws IOException {
        encoder.setIndentSize(1);
        QName encodeElementName = getElementName(request);
        encoder.encode(request, encodeElementName, out);
    }

    private static QName getElementName(EObject originatingRequest) {
        QName encodeElementName;
        if (originatingRequest instanceof GetCapabilitiesType) {
            encodeElementName = WFS.GetCapabilities;
        } else if (originatingRequest instanceof GetFeatureType) {
            encodeElementName = WFS.GetFeature;
        } else if (originatingRequest instanceof DescribeFeatureTypeType) {
            encodeElementName = WFS.DescribeFeatureType;
        } else if (originatingRequest instanceof GetCapabilitiesType) {
            encodeElementName = WFS.GetCapabilities;
        } else if (originatingRequest instanceof GetGmlObjectType) {
            encodeElementName = WFS.GetGmlObject;
        } else if (originatingRequest instanceof LockFeatureType) {
            encodeElementName = WFS.LockFeature;
        } else if (originatingRequest instanceof TransactionType) {
            encodeElementName = WFS.Transaction;
        } else {
            throw new IllegalArgumentException("Unkown xml element name for " + originatingRequest);
        }
        return encodeElementName;
    }

    public String getDefaultOutputFormat(WFSOperationType operation) {
        return strategy.getDefaultOutputFormat(this, operation);
    }

    public Filter[] splitFilters(Filter filter) {
        FilterCapabilities filterCapabilities = getFilterCapabilities();
        Capabilities filterCaps = new Capabilities();
        if (filterCapabilities != null) {
            filterCaps.addAll(filterCapabilities);
        }
        return strategy.splitFilters(filterCaps, filter);
    }

}
