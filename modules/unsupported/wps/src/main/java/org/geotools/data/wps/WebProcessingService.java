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
package org.geotools.data.wps;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.opengis.ows11.DCPType;
import net.opengis.ows11.HTTPType;
import net.opengis.ows11.OperationType;
import net.opengis.ows11.RequestMethodType;
import net.opengis.wps10.OutputDefinitionType;
import net.opengis.wps10.ProcessOfferingsType;
import net.opengis.wps10.ResponseDocumentType;
import net.opengis.wps10.ResponseFormType;
import net.opengis.wps10.WPSCapabilitiesType;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.ResourceInfo;
import org.geotools.data.ServiceInfo;
import org.geotools.data.ows.GetCapabilitiesRequest;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.ows.Specification;
import org.geotools.data.wps.request.DescribeProcessRequest;
import org.geotools.data.wps.request.ExecuteProcessRequest;
import org.geotools.data.wps.response.DescribeProcessResponse;
import org.geotools.data.wps.response.ExecuteProcessResponse;
import org.geotools.ows.ServiceException;
import org.geotools.wps.WPS;

/**
 * WebProcessingService is a class representing a WPS. It is used to access the
 * Capabilities document and perform requests. It currently only supports version
 * 1.0.0 but will have future versions added and it
 * will perform version negotiation automatically and use the highest
 * known version that the server can communicate.
 *
 * If restriction of versions to be used is desired, this class should be
 * subclassed and it's setupSpecifications() method over-ridden. It should
 * add which version/specifications are to be used to the specs array. See
 * the current implementation for an example.
 *
 * Example usage:
 * <code><pre>
 * WebProcessingService wps = new WebProcessingService("http://some.example.com/wps");
 * WPSCapabilitiesType capabilities = wps.getCapabilities();
 * ... //configure request
 *
 *
 * @author gdavis
 *
 *
 *
 *
 *
 */
public class WebProcessingService extends AbstractWPS<WPSCapabilitiesType, Object> {

    /**
     * Utility method to fetch the GET or POST URL of the given operation from the capabilities
     * document
     *
     * @param operation the operation URL to find in the capabilities doc
     * @param cap the capabilities document (need to pass as the method is static)
     * @param getGet if true, return the GET URL, otherwise return the POST URL
     * @return the URL of the given operation from the capabilities doc, or null if not found
     */
    public static URL getOperationURL(String operation, WPSCapabilitiesType cap, boolean getGet) {
        Iterator<OperationType> iterator = cap.getOperationsMetadata().getOperation().iterator();
        while (iterator.hasNext()) {
            OperationType next = (OperationType) iterator.next();
            if (operation.compareToIgnoreCase(next.getName()) == 0) {
                Iterator<DCPType> iterator2 = next.getDCP().iterator();
                while (iterator2.hasNext()) {
                    DCPType next2 = (DCPType) iterator2.next();
                    HTTPType http = next2.getHTTP();
                    if (getGet && !http.getGet().isEmpty()) {
                        RequestMethodType rmt = (RequestMethodType) http.getGet().get(0);

                        return makeURL(rmt.getHref());
                    } else if (!http.getPost().isEmpty()) {
                        RequestMethodType rmt = (RequestMethodType) http.getPost().get(0);

                        return makeURL(rmt.getHref());
                    }
                }

                return null;
            }
        }

        return null;
    }

    // convenience method to deal with the URISyntaxException
    private static URI makeURI(String s) {
        try {
            return new URI(s);
        } catch (URISyntaxException e) {
            // do nothing
            return null;
        }
    }

    // convenience method to deal with the MalformedURLException
    private static URL makeURL(String s) {
        try {
            return new URL(s);
        } catch (MalformedURLException e) {
            // do nothing
            return null;
        }
    }

    /**
     * Creates a new WebProcessingService from a WPSCapablitiles document.
     *
     * <p>The implementation assumes that the server is located at:
     * capabilities.getRequest().getGetCapabilities().getGet()
     */
    public WebProcessingService(WPSCapabilitiesType capabilities)
            throws IOException, ServiceException {
        super(
                getOperationURL("getcapabilities", capabilities, true),
                new SimpleHttpClient(),
                capabilities);
    }

    /**
     * Creates a new WebProcessingService instance and attempts to retrieve the Capabilities
     * document specified by serverURL.
     *
     * @param serverURL a URL that points to the capabilities document of a server
     * @throws IOException if there is an error communicating with the server
     * @throws ServiceException if the server responds with an error
     */
    public WebProcessingService(final URL serverURL) throws IOException, ServiceException {
        super(serverURL);
    }

    public WebProcessingService(
            final URL serverURL,
            final HTTPClient httpClient,
            final WPSCapabilitiesType capabilities)
            throws IOException, ServiceException {
        super(serverURL, httpClient, capabilities);
    }

    /** Sets up the specifications/versions that this server is capable of communicating with. */
    protected void setupSpecifications() {
        specs = new Specification[1];
        specs[0] = new WPS1_0_0();
    }

    @Override
    protected ServiceInfo createInfo() {
        return new WPSInfo();
    }

    @Override
    protected ResourceInfo createInfo(Object resource) {
        // WPS doesn't manage a resource
        return null;
    }

    public AbstractWPSGetCapabilitiesResponse issueRequest(GetCapabilitiesRequest request)
            throws IOException, ServiceException {
        return (AbstractWPSGetCapabilitiesResponse) internalIssueRequest(request);
    }

    public DescribeProcessResponse issueRequest(DescribeProcessRequest request)
            throws IOException, ServiceException {
        return (DescribeProcessResponse) internalIssueRequest(request);
    }

    public ExecuteProcessResponse issueRequest(ExecuteProcessRequest request)
            throws IOException, ServiceException {
        return (ExecuteProcessResponse) internalIssueRequest(request);
    }

    public ExecuteProcessResponse issueStatusRequest(URL statusURL)
            throws IOException, ServiceException {
        final HTTPResponse httpResponse;

        httpResponse = httpClient.get(statusURL);

        // a request with status can never use raw requests
        return new ExecuteProcessResponse(httpResponse, false);
    }

    /**
     * Get the getCapabilities document. If there was an error parsing it during creation, it will
     * return null (and it should have thrown an exception during creation).
     *
     * @return a WPSCapabilitiesType object, representing the Capabilities of the server
     */
    public WPSCapabilitiesType getCapabilities() {
        return (WPSCapabilitiesType) capabilities;
    }

    public DescribeProcessRequest createDescribeProcessRequest()
            throws UnsupportedOperationException {
        if (getCapabilities().getProcessOfferings() == null) {
            throw new UnsupportedOperationException(
                    "Server does not specify a DescribeProcess operation. Cannot be performed.");
        }

        URL onlineResource = getOperationURL("describeprocess", capabilities, true);
        if (onlineResource == null) {
            onlineResource = serverURL;
        }

        DescribeProcessRequest request =
                getSpecification().createDescribeProcessRequest(onlineResource);

        return request;
    }

    public ExecuteProcessRequest createExecuteProcessRequest()
            throws UnsupportedOperationException {
        ProcessOfferingsType processOfferings = getCapabilities().getProcessOfferings();
        if ((processOfferings == null) || !processOfferings.eAllContents().hasNext()) {
            throw new UnsupportedOperationException(
                    "Server does not specify any processes to execute. Cannot be performed.");
        }

        URL onlineResource = getOperationURL("execute", capabilities, true);
        if (onlineResource == null) {
            onlineResource = serverURL;
        }

        ExecuteProcessRequest request =
                getSpecification().createExecuteProcessRequest(onlineResource);

        return request;
    }

    private WPSSpecification getSpecification() {
        return (WPSSpecification) specification;
    }

    public EObject createLiteralInputValue(String literalValue) {
        return getSpecification().createLiteralInputValue(literalValue);
    }

    public EObject createBoundingBoxInputValue(
            String crs, int dimensions, List<Double> lowerCorner, List<Double> upperCorner) {
        return getSpecification()
                .createBoundingBoxInputValue(crs, dimensions, lowerCorner, upperCorner);
    }

    public ResponseFormType createResponseForm(
            ResponseDocumentType responseDoc, OutputDefinitionType rawOutput) {
        return getSpecification().createResponseForm(responseDoc, rawOutput);
    }

    public ResponseDocumentType createResponseDocumentType(
            boolean lineage, boolean status, boolean storeExecuteResponse, String outputType) {
        return getSpecification()
                .createResponseDocumentType(lineage, status, storeExecuteResponse, outputType);
    }

    public OutputDefinitionType createOutputDefinitionType(String identifier) {
        return getSpecification().createOutputDefinitionType(identifier);
    }

    /**
     * Class quickly describing Web Processing Service.
     *
     * @author gdavis
     */
    protected class WPSInfo implements ServiceInfo {

        private Set<String> keywords;

        WPSInfo() {
            keywords = new HashSet<String>();
            keywords.add("WPS");
            keywords.add(serverURL.toString());
        }

        public String getDescription() {
            String description = null;
            if ((description == null) && (serverURL != null)) {
                description = "Web Processing Service " + serverURL;
            }

            return description;
        }

        public Set<String> getKeywords() {
            return keywords;
        }

        public URI getPublisher() {
            try {
                return new URI(serverURL.getProtocol() + ":" + serverURL.getHost());
            } catch (URISyntaxException e) {
            }

            return null;
        }

        /**
         * We are a Web Processing Service:
         *
         * @return WPS.getInstance().getNamespaceURI();
         */
        public URI getSchema() {
            return makeURI(WPS.getInstance().getNamespaceURI());
        }

        /**
         * The source of this WPS is the capabilities document.
         *
         * <p>We make an effort here to look in the capabilities document provided for the
         * unambiguous capabilities URI.
         */
        public URI getSource() {
            try {
                URL source = getOperationURL("getcapabilities", capabilities, true);

                // URL source = getCapabilities().getRequest().getGetCapabilities().getGet();
                return source.toURI();
            } catch (NullPointerException huh) {
            } catch (URISyntaxException e) {
            }
            try {
                return serverURL.toURI();
            } catch (URISyntaxException e) {
                return null;
            }
        }

        public String getTitle() {
            if (serverURL == null) {
                return "Unavailable";
            } else {
                return serverURL.toString();
            }
        }
    }
}
