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
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.geotools.data.ResourceInfo;
import org.geotools.data.ServiceInfo;
import org.geotools.data.ows.AbstractOpenWebService;
import org.geotools.data.ows.OperationType;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPClientFinder;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.request.GetFeatureInfoRequest;
import org.geotools.ows.wms.response.GetFeatureInfoResponse;
import org.geotools.ows.wmts.WMTSSpecification.GetSingleTileRequest;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSServiceType;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.ows.wmts.response.GetTileResponse;
import org.geotools.referencing.CRS;
import org.geotools.tile.Tile;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * WebMapTileServer is a class representing a WMTS.
 *
 * <p>It is used to access the Capabilities document and perform requests. It will perform version
 * negotiation automatically and use the highest known version that the server can communicate.
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public class WebMapTileServer extends AbstractOpenWebService<WMTSCapabilities, Layer> {

    private WMTSServiceType type;

    /**
     * Set up the WebMapTileServer by the given serverUrl using the given http client.
     *
     * @param serverURL
     * @param httpClient
     * @throws ServiceException
     * @throws IOException
     */
    public WebMapTileServer(URL serverURL, HTTPClient httpClient)
            throws ServiceException, IOException {
        super(serverURL, httpClient);
        setupType();
    }

    /**
     * Set up the WebMapTileServer by the given serverUrl, using the http client with additional
     * headers.
     *
     * @param serverURL
     * @param httpClient
     * @param headers
     * @throws ServiceException
     * @throws IOException
     */
    public WebMapTileServer(URL serverURL, HTTPClient httpClient, Map<String, String> headers)
            throws ServiceException, IOException {
        super(serverURL, httpClient, null, null, headers);
        setupType();
    }

    /**
     * Set up the WebMapTileServer by the given capabilities, with the given serverURL. Using the
     * default httpClient.
     *
     * @param serverURL
     * @param capabilities
     * @throws ServiceException
     * @throws IOException
     */
    public WebMapTileServer(URL serverURL, WMTSCapabilities capabilities)
            throws ServiceException, IOException {
        this(serverURL, HTTPClientFinder.createClient(), capabilities, null);
    }

    /**
     * Set up the WebMapTileServer by the given capabilities, with the given serverURL. Using the
     * given httpClient.
     *
     * @param serverURL
     * @param httpClient
     * @param capabilities
     * @throws ServiceException
     * @throws IOException
     */
    public WebMapTileServer(URL serverURL, HTTPClient httpClient, WMTSCapabilities capabilities)
            throws ServiceException, IOException {
        this(serverURL, httpClient, capabilities, null);
    }

    /**
     * Set up the WebMapTileServer by the given capabilities, with the given serverURL and
     * additional hints. Using the given http client.
     *
     * @param serverURL
     * @param httpClient
     * @param capabilities
     * @param hints
     * @throws ServiceException
     * @throws IOException
     */
    public WebMapTileServer(
            URL serverURL,
            HTTPClient httpClient,
            WMTSCapabilities capabilities,
            Map<String, Object> hints)
            throws ServiceException, IOException {
        super(serverURL, httpClient, capabilities, hints);
        setupType();
    }

    /**
     * Set up the WebMapTileServer by calling serverURL with the default http client.
     *
     * @param serverURL
     * @throws IOException
     * @throws ServiceException
     */
    public WebMapTileServer(URL serverURL) throws IOException, ServiceException {
        super(serverURL);
        setupType();
    }

    /**
     * Set up the WebMapTileServer with the given capabilities. Using the default http client.
     *
     * @param capabilities
     * @throws ServiceException
     * @throws IOException
     * @deprecated Use constructor with serverUrl and capabilities
     */
    @Deprecated
    public WebMapTileServer(WMTSCapabilities capabilities) throws ServiceException, IOException {
        this(capabilities, HTTPClientFinder.createClient());
    }

    /**
     * Set up the WebMapTileServer with the given capabilities, using a custom http client.
     *
     * @param capabilities
     * @throws ServiceException
     * @throws IOException
     * @deprecated Use constructor with serverUrl, capabilities and httpClient.
     */
    @Deprecated
    public WebMapTileServer(WMTSCapabilities capabilities, HTTPClient httpClient)
            throws ServiceException, IOException {
        super(extractServerURL(capabilities), httpClient, capabilities);
        setupType();
    }

    private static URL extractServerURL(WMTSCapabilities capabilities) {
        URL url = capabilities.getRequest().getGetCapabilities().getGet();
        if (url == null) {
            try {
                url = new URL("http://missing.url/");
            } catch (MalformedURLException e) {
                //
            }
        }
        return url;
    }

    /**
     * Set up a WebMapTileServer by the same serverURL, httpClient, capabilities and hints as
     * delegate
     *
     * @param delegate
     * @throws ServiceException
     * @throws IOException
     */
    public WebMapTileServer(WebMapTileServer delegate) throws ServiceException, IOException {
        this(delegate.serverURL, delegate.getHTTPClient(), delegate.capabilities, delegate.hints);
    }

    @Override
    public WMTSCapabilities getCapabilities() {

        return capabilities;
    }

    @Override
    protected ServiceInfo createInfo() {
        return null;
    }

    @Override
    protected ResourceInfo createInfo(Layer resource) {
        return null;
    }

    @Override
    protected void setupSpecifications() {
        specs = new WMTSSpecification[1];
        specs[0] = new WMTSSpecification();
    }

    /** @deprecated - change to tileRequest.getTiles() */
    @Deprecated
    public Set<Tile> issueRequest(GetTileRequest tileRequest) throws ServiceException {
        return tileRequest.getTiles();
    }

    /**
     * The new issueRequest, which gives a GetTileResponse in response for a GetSingleTileRequest.
     */
    public GetTileResponse issueRequest(GetSingleTileRequest tileRequest)
            throws IOException, ServiceException {
        return (GetTileResponse) internalIssueRequest(tileRequest);
    }

    /** Creates a GetMultiTileRequest, signature kept for now. */
    public GetTileRequest createGetTileRequest() {

        URL url;
        OperationType getTile = getCapabilities().getRequest().getGetTile();
        if (getTile == null) {
            type = WMTSServiceType.REST;
        }
        if (WMTSServiceType.KVP.equals(type)) {
            url = findURL(getTile);
        } else {
            type = WMTSServiceType.REST;
            url = serverURL;
        }
        Properties props = new Properties();
        props.put("type", type.name());
        GetTileRequest request =
                ((WMTSSpecification) specification)
                        .createGetMultiTileRequest(url, props, capabilities, this.getHTTPClient());

        if (headers != null) {
            request.getHeaders().putAll(headers);
        }
        return request;
    }

    private URL findURL(OperationType operation) {
        if (operation == null) {
            type = WMTSServiceType.REST;
            return null;
        }
        if (WMTSServiceType.KVP.equals(type)) {
            if (operation.getGet() != null) {
                return operation.getGet();
            }
            return serverURL;

        } else {
            return null;
        }
    }

    /** */
    public GetFeatureInfoRequest createGetFeatureInfoRequest(GetTileRequest getmap) {
        return null;
    }

    /** */
    public GetFeatureInfoResponse issueRequest(GetFeatureInfoRequest request) {
        return null;
    }

    private void setupType() {
        this.type = getCapabilities().getType();
    }

    /** @param type */
    public void setType(WMTSServiceType type) {
        this.type = type;
    }

    public WMTSServiceType getType() {
        return type;
    }

    /** */
    public GeneralEnvelope getEnvelope(Layer layer, CoordinateReferenceSystem crs) {
        Map<String, CRSEnvelope> boundingBoxes = layer.getBoundingBoxes();
        CRSEnvelope box = boundingBoxes.get(crs.getName().getCode());
        if (box != null) {
            return new GeneralEnvelope(box);
        }
        for (String key : boundingBoxes.keySet()) {
            box = boundingBoxes.get(key);
            if (CRS.equalsIgnoreMetadata(crs, box.getCoordinateReferenceSystem())) {
                return new GeneralEnvelope(box);
            }
        }
        return null;
    }
}
