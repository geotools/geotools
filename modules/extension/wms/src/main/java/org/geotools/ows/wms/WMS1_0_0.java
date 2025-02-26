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
package org.geotools.ows.wms;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.ows.AbstractGetCapabilitiesRequest;
import org.geotools.data.ows.AbstractRequest;
import org.geotools.data.ows.GetCapabilitiesRequest;
import org.geotools.data.ows.Response;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.request.AbstractGetFeatureInfoRequest;
import org.geotools.ows.wms.request.AbstractGetMapRequest;
import org.geotools.ows.wms.request.AbstractGetStylesRequest;
import org.geotools.ows.wms.request.DescribeLayerRequest;
import org.geotools.ows.wms.request.GetLegendGraphicRequest;
import org.geotools.ows.wms.request.GetStylesRequest;
import org.geotools.ows.wms.request.PutStylesRequest;
import org.geotools.ows.wms.response.GetFeatureInfoResponse;
import org.geotools.ows.wms.response.GetMapResponse;
import org.geotools.ows.wms.response.GetStylesResponse;
import org.geotools.ows.wms.response.WMSGetCapabilitiesResponse;

/**
 * Provides support for the Web Map Server 1.0 Specification.
 *
 * <p>WMS1_0_0 provides both name and version information that may be checked against a GetCapabilities document during
 * version negotiation.
 *
 * @author Jody Garnett, Refractions Research
 * @author rgould
 */
public class WMS1_0_0 extends WMSSpecification {
    static final Map<String, String> formatMimeTypes = new HashMap<>();
    static final Map<String, String> exceptionMimeTypes = new HashMap<>();

    static {
        exceptionMimeTypes.put("WMS_XML", "application/vnd.ogc.se_xml");
        exceptionMimeTypes.put("INIMAGE", "application/vnd.ogc.se_inimage"); // $NON-NLS-1$ //$NON-NLS-2$
        exceptionMimeTypes.put("BLANK", "application/vnd.ogc.se_blank"); // $NON-NLS-1$ //$NON-NLS-2$
    }

    static {
        formatMimeTypes.put("GIF", "image/gif"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("PNG", "image/png"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("JPEG", "image/jpeg"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("BMP", "image/bmp"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("WebCGM", "image/cgm;Version=4;ProfileId=WebCGM"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("SVG", "image/svg+xml"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("GML.1", "text/xml"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("GML.2", "text/xml"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("GML.3", "text/xml"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("WBMP", "image/vnd.wap.wbmp"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("WMS_XML", "application/vnd.ogc.wms_xml"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("MIME", "mime"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("INIMAGE", "application/vnd.ogc.se_inimage"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("TIFF", "image/tiff"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("GeoTIFF", "image/tiff"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("PPM", "image/x-portable-pixmap"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("BLANK", "application/vnd.ogc.se_blank"); // $NON-NLS-1$ //$NON-NLS-2$
        formatMimeTypes.put("CW_WKB", "application/x-cubestor-wkb"); // $NON-NLS-1$//$NON-NLS-2$
    }

    /** Public constructor creates the WMS1_0_0 object. */
    public WMS1_0_0() {}

    /**
     * Expected version attribute for root element.
     *
     * @return the expect version value for this specification
     */
    @Override
    public String getVersion() {
        return "1.0.0"; // $NON-NLS-1$
    }

    /**
     * Provides mapping from well known format to MIME type.
     *
     * <p>WebMapServer api uses mime type internally for format information (indeed WMS 1.0.0 is the only WMS
     * specifcation not to use MIME type directly).
     *
     * <p>
     *
     * @return MIME type for format
     */
    public static final String toFormatMIME(String format) {
        return getMimeType(format, formatMimeTypes);
    }

    /**
     * The WMS 1.0.0 specification uses a mapping of mimetypes to values to use as parameter values in requests. This
     * will take a parameter value and convert it to its according mime type.
     *
     * @param exception an exceptions parameter value, such as "WMS_XML"
     * @return a mimeType, such as "application/vnd.ogc.se_xml"
     */
    public static final String toExceptionMimeType(String exception) {
        return getMimeType(exception, exceptionMimeTypes);
    }

    private static final String getMimeType(String key, Map map) {
        if (map.containsKey(key)) {
            return (String) map.get(key);
        }

        return null;
    }

    /**
     * Provides mapping from MIME type to WMS 1.0.0 Format.
     *
     * <p>WebMapServer api uses mime type internally for format information (indeed WMS 1.0.0 is the only WMS
     * specifcation not to use MIME type directly).
     *
     * <p>
     *
     * @param mimeType MIME type such as "image/gif"
     * @return Format well known WMS 1.0.0 format such as "GIF"
     */
    public static final String getFormatValue(String mimeType) {
        return getParameterValue(mimeType, formatMimeTypes);
    }

    /**
     * The WMS 1.0.0 specification uses internal mappings in the parameter value instead of direct mime types. This will
     * map a given mime type to its proper parameter value according to the spec.
     *
     * @param mimeType the mimeType to use, such as "application/vnd.ogc.se_xml"
     * @return the proper parameter value, such as "WMS_XML"
     */
    public static final String getExceptionValue(String mimeType) {
        return getParameterValue(mimeType, exceptionMimeTypes);
    }

    private static final String getParameterValue(String mimeType, Map map) {
        for (Object o : map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;

            if (mimeType.equals(entry.getValue())) {
                return (String) entry.getKey();
            }
        }

        return null;
    }

    /**
     * Create a request for performing GetCapabilities requests on a 1.0.0 server.
     *
     * @see WMSSpecification#createGetCapabilitiesRequest(java.net.URL)
     * @param server a URL that points to the 1.0.0 server
     * @return a AbstractGetCapabilitiesRequest object that can provide a valid request
     */
    @Override
    public GetCapabilitiesRequest createGetCapabilitiesRequest(URL server) {
        return new GetCapsRequest(server);
    }

    private static String processKey(String key) {
        return key.trim().toLowerCase();
    }

    /**
     * We need a custom request object.
     *
     * <p>WMS 1.0.0 does requests a bit differently:
     *
     * <ul>
     *   <li>WMTVER=1.0.0
     */
    public static class GetCapsRequest extends AbstractGetCapabilitiesRequest {
        /**
         * Construct a Request compatable with a 1.0.0 Web Feature Server.
         *
         * @param urlGetCapabilities URL of GetCapabilities document.
         */
        public GetCapsRequest(URL urlGetCapabilities) {
            super(urlGetCapabilities);
        }

        @Override
        protected void initVersion() {
            setProperty(processKey("WMTVER"), "1.0.0"); // $NON-NLS-1$ //$NON-NLS-2$
            properties.remove(processKey("VERSION"));
        }

        @Override
        protected void initRequest() {
            setProperty(processKey("REQUEST"), "capabilities"); // $NON-NLS-1$ //$NON-NLS-2$
        }

        @Override
        protected void initService() {
            // The 1.0.0 specification does not use the service property
        }
        /*
         * @see org.geotools.data.wms.request.AbstractRequest#processKey(java.lang.String)
         */
        @Override
        protected String processKey(String key) {
            return WMS1_0_0.processKey(key);
        }

        @Override
        public Response createResponse(HTTPResponse httpResponse) throws ServiceException, IOException {
            return new WMSGetCapabilitiesResponse(httpResponse, hints);
        }
    }

    /** A GetMapRequest for a 1.0.0 Server */
    public static class GetMapRequest extends AbstractGetMapRequest {
        /**
         * Constructs a GetMapRequest for use with a 1.0.0 server
         *
         * @param onlineResource the URL for server's GetMap request
         */
        public GetMapRequest(URL onlineResource) {
            super(onlineResource, null);
        }

        @Override
        protected void initRequest() {
            setProperty(processKey("REQUEST"), "map"); // $NON-NLS-1$ //$NON-NLS-2$

            /*
             * A 1.0.0 WMS server has been encountered that has EXCEPTIONS as
             * a required parameter. It does not hurt to explicitly ask for
             * this, anyway.
             */
            setProperty(processKey("EXCEPTIONS"), AbstractRequest.EXCEPTION_XML);
        }

        @Override
        protected void initVersion() {
            setProperty(processKey(VERSION), "1.0.0");
        }

        @Override
        public void setProperty(String name, String value) {
            if (name.equalsIgnoreCase(FORMAT)) {
                value = getRequestFormat(value);
            } else if (name.equalsIgnoreCase(EXCEPTIONS)) {
                value = getRequestException(value);
            }

            super.setProperty(name, value);
        }

        @Override
        public void setExceptions(String exceptions) {
            setProperty(processKey(EXCEPTIONS), exceptions);
        }

        protected String getRequestException(String exception) {
            return getExceptionValue(exception);
        }

        protected String getRequestFormat(String format) {
            return getFormatValue(format);
        }

        @Override
        protected String processKey(String key) {
            return WMS1_0_0.processKey(key);
        }

        @Override
        public Response createResponse(HTTPResponse httpResponse) throws ServiceException, IOException {
            return new GetMapResponse(httpResponse);
        }
    }

    /** A GetFeatureInfoRequest for a 1.0.0 server */
    public static class GetFeatureInfoRequest extends AbstractGetFeatureInfoRequest {
        /** */
        public GetFeatureInfoRequest(URL onlineResource, org.geotools.ows.wms.request.GetMapRequest request) {
            super(onlineResource, request);
        }

        @Override
        protected void initVersion() {
            setProperty(processKey("WMTVER"), "1.0.0");
        }

        @Override
        protected String processKey(String key) {
            return WMS1_0_0.processKey(key);
        }

        @Override
        public Response createResponse(HTTPResponse httpResponse) throws ServiceException, IOException {
            return new GetFeatureInfoResponse(httpResponse);
        }
    }

    /** @see WMSSpecification#createGetMapRequest(java.net.URL) */
    @Override
    public org.geotools.ows.wms.request.GetMapRequest createGetMapRequest(URL get) {
        return new GetMapRequest(get);
    }

    /** @see WMSSpecification#createGetFeatureInfoRequest(java.net.URL, org.geotools.ows.wms.request.GetMapRequest) */
    @Override
    public org.geotools.ows.wms.request.GetFeatureInfoRequest createGetFeatureInfoRequest(
            URL onlineResource, org.geotools.ows.wms.request.GetMapRequest getMapRequest) {
        return new GetFeatureInfoRequest(onlineResource, getMapRequest);
    }

    /**
     * Note that WMS 1.0.0 does not support this method.
     *
     * @see WMSSpecification#createDescribeLayerRequest(java.net.URL)
     */
    @Override
    public DescribeLayerRequest createDescribeLayerRequest(URL onlineResource) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("WMS 1.0.0 does not support DescribeLayer");
    }

    /**
     * Note that WMS 1.0.0 does not support this method.
     *
     * @see WMSSpecification#createGetLegendGraphicRequest(java.net.URL)
     */
    @Override
    public GetLegendGraphicRequest createGetLegendGraphicRequest(URL onlineResource)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException("WMS 1.0.0 does not support GetLegendGraphic");
    }

    /** @see WMSSpecification#createGetStylesRequest(java.net.URL) */
    @Override
    public GetStylesRequest createGetStylesRequest(URL onlineResource) throws UnsupportedOperationException {
        return new InternalGetStylesRequest(onlineResource);
    }

    public static class InternalGetStylesRequest extends AbstractGetStylesRequest {

        /** @param onlineResource */
        public InternalGetStylesRequest(URL onlineResource) {
            super(onlineResource, null);
        }

        /* (non-Javadoc)
         * @see AbstractGetStylesRequest#initVersion()
         */
        @Override
        protected void initVersion() {
            setProperty(processKey(VERSION), "1.1.0");
        }

        @Override
        public Response createResponse(HTTPResponse httpResponse) throws ServiceException, IOException {
            return new GetStylesResponse(httpResponse);
        }
    }

    /**
     * Note that WMS 1.0.0 does not support this method
     *
     * @see WMSSpecification#createPutStylesRequest(java.net.URL)
     */
    @Override
    public PutStylesRequest createPutStylesRequest(URL onlineResource) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("WMS 1.0.0 does not support PutStyles");
    }
}
