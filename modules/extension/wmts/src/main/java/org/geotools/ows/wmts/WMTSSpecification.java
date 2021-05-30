/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wmts;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.ows.AbstractGetCapabilitiesRequest;
import org.geotools.data.ows.GetCapabilitiesRequest;
import org.geotools.data.ows.Response;
import org.geotools.data.ows.Specification;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPClientFinder;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSServiceType;
import org.geotools.ows.wmts.request.AbstractGetTileRequest;
import org.geotools.ows.wmts.response.GetTileResponse;
import org.geotools.ows.wmts.response.WMTSGetCapabilitiesResponse;
import org.geotools.util.logging.Logging;

/**
 * WMTS version 1.0.0 specification.
 *
 * <p>Used to create GetCapabilities and GetTile requests.
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public class WMTSSpecification extends Specification {

    public static final String WMTS_VERSION = "1.0.0";

    /** */
    public WMTSSpecification() {}

    @Override
    public String getVersion() {
        //
        return WMTS_VERSION;
    }

    @Override
    public GetCapabilitiesRequest createGetCapabilitiesRequest(URL server) {
        return new GetCapsRequest(server);
    }

    public GetTileRequest createGetTileRequest(
            URL server, Properties props, WMTSCapabilities caps) {
        return this.createGetTileRequest(server, props, caps, HTTPClientFinder.createClient());
    }

    public GetTileRequest createGetTileRequest(
            URL server, Properties props, WMTSCapabilities caps, HTTPClient client) {
        return new GetTileRequest(server, props, caps, client);
    }

    public static class GetTileRequest extends AbstractGetTileRequest {

        private static Logger LOGGER = Logging.getLogger(GetTileRequest.class);

        public static final String DIMENSION_TIME = "time";

        public static final String DIMENSION_ELEVATION = "elevation";

        /** */
        public GetTileRequest(
                URL onlineResource, Properties properties, WMTSCapabilities capabilities) {
            this(onlineResource, properties, capabilities, HTTPClientFinder.createClient());
        }

        public GetTileRequest(
                URL onlineResource,
                Properties properties,
                WMTSCapabilities capabilities,
                HTTPClient client) {
            super(onlineResource, properties, client);
            if (properties.containsKey("type")) {
                String t = (String) properties.get("type");
                if ("REST".equalsIgnoreCase(t)) {
                    this.type = WMTSServiceType.REST;
                } else if ("KVP".equalsIgnoreCase(t)) {
                    this.type = WMTSServiceType.KVP;
                }
            } else {
                this.type = capabilities.getType();
            }
            this.capabilities = capabilities;
        }

        @Override
        public Response createResponse(HTTPResponse response) throws ServiceException, IOException {
            return new GetTileResponse(response, getType());
        }

        @Override
        protected void initVersion() {
            setProperty(VERSION, WMTS_VERSION);
        }

        /** @return the type */
        public WMTSServiceType getType() {
            return type;
        }

        /** @param type the type to set */
        public void setType(WMTSServiceType type) {
            this.type = type;
        }

        @Override
        protected String createTemplateUrl(String tileMatrixSetName) {

            String baseUrl = getFinalURL().toExternalForm();

            String layerString = WMTSHelper.usePercentEncodingForSpace(layer.getName());
            String styleString =
                    WMTSHelper.usePercentEncodingForSpace(styleName == null ? "" : styleName);

            String format = getFormat();

            if (StringUtils.isEmpty(format)) {
                if (!layer.getFormats().isEmpty()) {
                    format = layer.getFormats().get(0);
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(
                                "Format is not set, available formats: "
                                        + layer.getFormats()
                                        + " -- Selecting "
                                        + format);
                    }
                }
            }

            if (StringUtils.isEmpty(format)) {
                format = "image/png";
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine("Format not set, trying with " + format);
            }
            setFormat(format);

            switch (type) {
                case KVP:
                    return WMTSHelper.appendQueryString(
                            baseUrl,
                            getKVPparams(layerString, styleString, tileMatrixSetName, format));
                case REST:
                    return getRESTurl(baseUrl, layerString, styleString, tileMatrixSetName);
                default:
                    throw new IllegalArgumentException("Unexpected WMTS Service type " + type);
            }
        }

        private String getRESTurl(
                String baseUrl, String layerString, String styleString, String tileMatrixSetName) {
            baseUrl = WMTSHelper.replaceToken(baseUrl, "layer", layerString);
            baseUrl = WMTSHelper.replaceToken(baseUrl, "style", styleString);
            baseUrl = WMTSHelper.replaceToken(baseUrl, "tilematrixset", tileMatrixSetName);
            return baseUrl;
        }

        /**
         * Returns the properties for KVP WMTS, as well as placeholder's for the specific parameters
         * of GetTile
         *
         * @param layerString
         * @param styleString
         * @param tileMatrixSetName
         * @param format
         * @return
         */
        public static HashMap<String, String> getKVPparams(
                String layerString, String styleString, String tileMatrixSetName, String format) {
            HashMap<String, String> params = new HashMap<>();
            params.put("service", "WMTS");
            params.put("version", WMTS_VERSION);
            params.put("request", "GetTile");
            params.put("layer", layerString);
            params.put("style", styleString);
            params.put("format", format);
            params.put("tilematrixset", tileMatrixSetName);
            params.put("TileMatrix", "{TileMatrix}");
            params.put("TileCol", "{TileCol}");
            params.put("TileRow", "{TileRow}");

            return params;
        }
    }

    public static class GetCapsRequest extends AbstractGetCapabilitiesRequest {
        /**
         * Construct a Request compatible with a 1.0.1 WMTS.
         *
         * @param urlGetCapabilities URL of GetCapabilities document.
         */
        public GetCapsRequest(URL urlGetCapabilities) {
            super(urlGetCapabilities);
        }

        @Override
        protected void initService() {
            setProperty(processKey(SERVICE), "WMTS");
        }

        @Override
        protected void initVersion() {
            setProperty(processKey(VERSION), WMTS_VERSION); // $NON-NLS-1$ //$NON-NLS-2$
        }

        @Override
        protected String processKey(String key) {
            return WMTSSpecification.processKey(key);
        }

        @Override
        public WMTSGetCapabilitiesResponse createResponse(HTTPResponse httpResponse)
                throws ServiceException, IOException {
            return new WMTSGetCapabilitiesResponse(httpResponse, hints);
        }
    }

    /** */
    public static String processKey(String key) {
        if (keywords.contains(key.toLowerCase())) return key.trim().toUpperCase();
        else return key;
    }

    static Set<String> keywords = new TreeSet<>();

    static { // a list of keywords from the WMTS spec -
        // http://portal.opengeospatial.org/files/?artifact_id=35326
        String[] words = {
            "request",
            "version",
            "layer",
            "acceptversions",
            "sections",
            "updatesequence",
            "acceptformats",
            "style",
            "format",
            "tilematrixset",
            "tilematrix",
            "tilerow",
            "tilecol",
            "j",
            "i",
            "infoformat",
            "service",
            "time",
            "elevation",
            "band"
        };
        keywords.addAll(Arrays.asList(words));
    }
}
