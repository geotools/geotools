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
import java.net.MalformedURLException;
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
 * <p>GetTile request are separated into three different objects:
 *
 * <ul>
 *   <li>GetMultiTileRequest - Used to get a set of tiles within an extent
 *   <li>GetKVPTileRequest - Used to get a tile request with query string parameteres
 *   <li>GetRestTileRequest - Used to get a tile request with url based on resourceurl in
 *       capabilities
 * </ul>
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

    /** @deprecated Use createGetMultiTileRequest */
    @Deprecated
    public GetTileRequest createGetTileRequest(
            URL server, Properties props, WMTSCapabilities caps) {
        return this.createGetTileRequest(server, props, caps, HTTPClientFinder.createClient());
    }

    /** @deprecated Use createGetMultiTileRequest */
    @Deprecated
    public GetTileRequest createGetTileRequest(
            URL server, Properties props, WMTSCapabilities caps, HTTPClient client) {
        return new GetTileRequest(server, props, caps, client);
    }

    /** Create a GetMultiTileRequest */
    GetMultiTileRequest createGetMultiTileRequest(
            URL server, Properties props, WMTSCapabilities caps, HTTPClient client) {
        return new GetMultiTileRequest(server, props, caps, client);
    }

    /** @deprecated Avoid usage of this - change to GetMultiTileRequest */
    @Deprecated
    public static class GetTileRequest extends GetMultiTileRequest {

        public GetTileRequest(
                URL onlineResource,
                Properties properties,
                WMTSCapabilities capabilities,
                HTTPClient client) {
            super(onlineResource, properties, capabilities, client);
        }

        /** Returns a GetTileResponse which in most cases represents an Image. */
        @Override
        public Response createResponse(HTTPResponse response) throws ServiceException, IOException {
            return new GetTileResponse(response, getType());
        }
    }

    /** GetMultiTileRequest - used for getting a Set of tiles */
    public static class GetMultiTileRequest extends AbstractGetTileRequest {

        private static Logger LOGGER = Logging.getLogger(GetMultiTileRequest.class);

        public static final String DIMENSION_TIME = "time";

        public static final String DIMENSION_ELEVATION = "elevation";

        /** */
        public GetMultiTileRequest(
                URL onlineResource, Properties properties, WMTSCapabilities capabilities) {
            this(onlineResource, properties, capabilities, HTTPClientFinder.createClient());
        }

        public GetMultiTileRequest(
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

            String layerString = WMTSHelper.usePercentEncodingForSpace(layer.getName());
            String styleString =
                    WMTSHelper.usePercentEncodingForSpace(styleName == null ? "" : styleName);

            switch (type) {
                case KVP:
                    return WMTSHelper.appendQueryString(
                            getFinalURL().toExternalForm(),
                            getKVPparams(layerString, styleString, tileMatrixSetName, format));
                case REST:
                    return getRESTurl(
                            getTemplateUrl(), layerString, styleString, tileMatrixSetName);
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

    /** Represents a GetTile request for a single tile. */
    public abstract static class GetSingleTileRequest extends AbstractGetTileRequest {

        GetSingleTileRequest(URL onlineResource, Properties properties, HTTPClient client) {
            super(onlineResource, properties, client);
        }

        @Override
        protected void initVersion() {
            setProperty(VERSION, WMTS_VERSION);
        }

        @Override
        protected String createTemplateUrl(String tileMatrixSetName) {
            throw new UnsupportedOperationException(
                    "Single tile request's shouldn't use a template url.");
        }
    }

    /** GetTile request base on query string parameters */
    public static class GetKVPTileRequest extends GetSingleTileRequest {

        GetKVPTileRequest(URL onlineResource, Properties properties, HTTPClient client) {
            super(onlineResource, properties, client);
            properties.setProperty("type", "KVP");
        }

        @Override
        public URL getFinalURL() {
            if (this.layer == null
                    || this.styleName == null
                    || this.getFormat() == null
                    || this.getTileMatrixSet() == null
                    || this.getTileMatrix() == null
                    || this.getTileCol() == null
                    || this.getTileRow() == null) {
                throw new IllegalStateException(
                        "Missing some properties for a proper GetTile-request.");
            }

            this.setProperty("layer", this.layer.getName());
            this.setProperty("style", this.styleName);
            this.setProperty("format", this.getFormat());
            this.setProperty("tilematrixset", this.getTileMatrixSet());
            this.setProperty("TileMatrix", this.getTileMatrix());
            this.setProperty("TileCol", this.getTileCol().toString());
            this.setProperty("TileRow", this.getTileRow().toString());

            return super.getFinalURL();
        }

        /** Creating a GetTileResponse */
        @Override
        public GetTileResponse createResponse(HTTPResponse httpResponse)
                throws ServiceException, IOException {
            return new GetTileResponse(httpResponse, WMTSServiceType.KVP);
        }
    }

    /** GetTile request based on a resourceUrl specified in Capabilities. */
    public static class GetRestTileRequest extends GetSingleTileRequest {

        /** onlineResource should be specified, but isn't used. */
        GetRestTileRequest(URL onlineResource, Properties properties, HTTPClient client) {
            super(onlineResource, properties, client);
        }

        /*
         * Uses the resourceurl specified in capabilites to create a url for one single tile
         */
        @Override
        public URL getFinalURL() {
            if (this.layer == null
                    || this.styleName == null
                    || this.getFormat() == null
                    || this.getTileMatrixSet() == null
                    || this.getTileMatrix() == null
                    || this.getTileCol() == null
                    || this.getTileRow() == null) {
                throw new IllegalStateException(
                        "Missing some properties for a proper GetTile-request.");
            }
            String baseUrl = getTemplateUrl();
            if (baseUrl == null) {
                throw new IllegalStateException(
                        String.format(
                                "ResourceUrl wasn't given for layer {%s} and format {%s}",
                                layer, getFormat()));
            }

            String layerString = WMTSHelper.usePercentEncodingForSpace(layer.getName());
            String styleString = WMTSHelper.usePercentEncodingForSpace(styleName);

            baseUrl = WMTSHelper.replaceToken(baseUrl, "layer", layerString);
            baseUrl = WMTSHelper.replaceToken(baseUrl, "style", styleString);
            baseUrl = WMTSHelper.replaceToken(baseUrl, "tilematrixset", this.getTileMatrixSet());
            baseUrl = WMTSHelper.replaceToken(baseUrl, "tilematrix", this.getTileMatrix());
            baseUrl = WMTSHelper.replaceToken(baseUrl, "tilerow", this.getTileRow().toString());
            baseUrl = WMTSHelper.replaceToken(baseUrl, "tilecol", this.getTileCol().toString());

            try {
                return new URL(baseUrl);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Error creating URL for GetTile request", e);
            }
        }

        /** Creating a GetTileResponse */
        @Override
        public GetTileResponse createResponse(HTTPResponse httpResponse)
                throws ServiceException, IOException {
            return new GetTileResponse(httpResponse, WMTSServiceType.REST);
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
