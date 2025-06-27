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
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.data.ServiceInfo;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.ows.AbstractOpenWebService;
import org.geotools.data.ows.OperationType;
import org.geotools.geometry.GeneralBounds;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPClientFinder;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.request.GetFeatureInfoRequest;
import org.geotools.ows.wms.response.GetFeatureInfoResponse;
import org.geotools.ows.wmts.WMTSSpecification.GetSingleTileRequest;
import org.geotools.ows.wmts.model.TileMatrixSet;
import org.geotools.ows.wmts.model.TileMatrixSetLink;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.model.WMTSServiceType;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.ows.wmts.response.GetTileResponse;
import org.geotools.referencing.CRS;
import org.geotools.tile.Tile;

/**
 * WebMapTileServer is a class representing a WMTS.
 *
 * <p>It is used to access the Capabilities document and perform requests. It will perform version negotiation
 * automatically and use the highest known version that the server can communicate.
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
    public WebMapTileServer(URL serverURL, HTTPClient httpClient) throws ServiceException, IOException {
        super(serverURL, httpClient);
        setupType();
    }

    /**
     * Set up the WebMapTileServer by the given serverUrl, using the http client with additional headers.
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
     * Set up the WebMapTileServer by the given capabilities, with the given serverURL. Using the default httpClient.
     *
     * @param serverURL
     * @param capabilities
     * @throws ServiceException
     * @throws IOException
     */
    public WebMapTileServer(URL serverURL, WMTSCapabilities capabilities) throws ServiceException, IOException {
        this(serverURL, HTTPClientFinder.createClient(), capabilities, null);
    }

    /**
     * Set up the WebMapTileServer by the given capabilities, with the given serverURL. Using the given httpClient.
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
     * Set up the WebMapTileServer by the given capabilities, with the given serverURL and additional hints. Using the
     * given http client.
     *
     * @param serverURL
     * @param httpClient
     * @param capabilities
     * @param hints
     * @throws ServiceException
     * @throws IOException
     */
    public WebMapTileServer(
            URL serverURL, HTTPClient httpClient, WMTSCapabilities capabilities, Map<String, Object> hints)
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
     * Set up a WebMapTileServer by the same serverURL, httpClient, capabilities and hints as delegate
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

    WMTSSpecification getSpecification() {
        return (WMTSSpecification) specification;
    }

    /** @deprecated - change to tileRequest.getTiles() */
    @Deprecated
    public Set<Tile> issueRequest(GetTileRequest tileRequest) throws ServiceException {
        return tileRequest.getTiles();
    }

    /** The new issueRequest, which gives a GetTileResponse in response for a GetSingleTileRequest. */
    public GetTileResponse issueRequest(GetSingleTileRequest tileRequest) throws IOException, ServiceException {
        return (GetTileResponse) internalIssueRequest(tileRequest);
    }

    /** Creates a GetMultiTileRequest. */
    public GetTileRequest createGetTileRequest() {

        OperationType getTile = getCapabilities().getRequest().getGetTile();
        URL url = findGetTileURL(getTile);

        if (url == null) {
            type = WMTSServiceType.REST;
            url = getCapabilities().getService().getOnlineResource();
        }

        Properties props = new Properties();
        props.put("type", type.name());

        GetTileRequest request =
                getSpecification().createGetMultiTileRequest(url, props, getCapabilities(), this.getHTTPClient());

        if (headers != null) {
            request.getHeaders().putAll(headers);
        }
        return request;
    }

    /**
     * Creates a GetTileRequest. Either a GetSingleTileRequest or GetMultiTileRequest.
     *
     * <p>GetSingleTileRequest is either KVP or REST
     *
     * <p>The url served to GetRestTileRequest is just the serverUrl. The proper templateUrl is found at a later stage.
     */
    public GetTileRequest createGetTileRequest(boolean multiTile) {
        if (multiTile) {
            return createGetTileRequest();
        }

        OperationType getTile = getCapabilities().getRequest().getGetTile();
        URL url = findGetTileURL(getTile);

        Properties props = new Properties();
        props.put("type", type.name());

        GetSingleTileRequest request;
        switch (type) {
            case KVP:
                request = new WMTSSpecification.GetKVPTileRequest(url, props, this.getHTTPClient());
                break;
            case REST:
                request = new WMTSSpecification.GetRestTileRequest(url, props, getHTTPClient());
                break;
            default:
                throw new RuntimeException("Unknown type");
        }
        if (headers != null) {
            request.getHeaders().putAll(headers);
        }
        return request;
    }

    /** The URL for RESTful can't be established at this point, but it can't be null either. */
    private URL findGetTileURL(OperationType operation) {
        switch (type) {
            case KVP:
                if (operation == null) {
                    return null;
                }
                return operation.getGet() != null ? operation.getGet() : serverURL;
            case REST:
                return serverURL;
            default:
                return null;
        }
    }

    /** Selects the tileMatrixSet that is linked to this layer with the given CRS */
    public TileMatrixSet selectMatrixSet(WMTSLayer layer, CoordinateReferenceSystem requestCRS)
            throws ServiceException {
        TileMatrixSet retMatrixSet = null;

        Map<String, TileMatrixSetLink> links = layer.getTileMatrixLinks();

        if (requestCRS == null) {
            throw new IllegalArgumentException("requestCRS cannot be null");
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("request CRS " + requestCRS.getName());
        }
        // See if the layer supports the requested SRS. Matching against the SRS rather than the
        // full CRS will avoid missing matches due to Longitude first being set on the request
        // CRS and therefore minimise transformations that need to be performed on the received tile
        String requestSRS = CRS.toSRS(requestCRS);
        for (TileMatrixSet matrixSet : capabilities.getMatrixSets()) {

            CoordinateReferenceSystem matrixCRS = matrixSet.getCoordinateReferenceSystem();
            String matrixSRS = CRS.toSRS(matrixCRS);
            if (requestSRS.equals(matrixSRS)) { // matching SRS
                if (links.containsKey(matrixSet.getIdentifier())) { // and available for
                    // this layer
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("selected matrix set:" + matrixSet.getIdentifier());
                    }
                    retMatrixSet = matrixSet;
                    break;
                }
            }
        }

        // The layer does not provide the requested CRS, so just take any one
        if (retMatrixSet == null) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("Failed to match the requested CRS ("
                        + requestCRS.getName()
                        + ") with any of the tile matrices!");
            }
            for (TileMatrixSet matrix : capabilities.getMatrixSets()) {
                if (links.containsKey(matrix.getIdentifier())) { // available for this layer
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("defaulting matrix set:" + matrix.getIdentifier());
                    }
                    retMatrixSet = matrix;

                    break;
                }
            }
        }
        if (retMatrixSet == null) {
            throw new ServiceException("Unable to find a matching TileMatrixSet for layer "
                    + layer.getName()
                    + " and SRS: "
                    + requestCRS.getName());
        }
        return retMatrixSet;
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
    public GeneralBounds getEnvelope(Layer layer, CoordinateReferenceSystem crs) {
        Map<String, CRSEnvelope> boundingBoxes = layer.getBoundingBoxes();
        CRSEnvelope box = boundingBoxes.get(crs.getName().getCode());
        if (box != null) {
            return new GeneralBounds(box);
        }
        for (String key : boundingBoxes.keySet()) {
            box = boundingBoxes.get(key);
            if (CRS.equalsIgnoreMetadata(crs, box.getCoordinateReferenceSystem())) {
                return new GeneralBounds(box);
            }
        }
        return null;
    }
}
