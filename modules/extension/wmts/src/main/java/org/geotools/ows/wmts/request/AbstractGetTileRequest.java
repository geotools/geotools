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

package org.geotools.ows.wmts.request;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPClientFinder;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.StyleImpl;
import org.geotools.ows.wmts.WMTSHelper;
import org.geotools.ows.wmts.client.WMTSTileFactory;
import org.geotools.ows.wmts.client.WMTSTileService;
import org.geotools.ows.wmts.model.TileMatrixSet;
import org.geotools.ows.wmts.model.TileMatrixSetLink;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.model.WMTSServiceType;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.tile.Tile;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * (Based on existing work by rgould for WMS service)
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public abstract class AbstractGetTileRequest extends AbstractWMTSRequest implements GetTileRequest {

    /** MAXTILES */
    private static final int MAXTILES = 256;

    static WMTSTileFactory factory = new WMTSTileFactory();

    public static final String LAYER = "Layer";

    public static final String STYLE = "Style";

    public static final String FORMAT = "Format";

    private final HTTPClient client;

    protected WMTSLayer layer = null;

    protected String styleName = "";

    private String srs;

    static final Logger LOGGER = Logging.getLogger(AbstractGetTileRequest.class);

    protected WMTSServiceType type;

    protected WMTSCapabilities capabilities;

    private ReferencedEnvelope requestedBBox;

    private int requestedHeight;

    private int requestedWidth;

    private String requestedTime;

    private CoordinateReferenceSystem crs;

    private final Map<String, String> headers = new HashMap<>();

    private String format = null;

    private String tileMatrixSet = null;

    private String tileMatrix = null;

    private Integer tileRow = null;

    private Integer tileCol = null;

    /**
     * Constructs a GetMapRequest. The data passed in represents valid values that can be used.
     *
     * @param onlineResource the location that the request should be applied to
     * @param properties pre-set properties to be used. Can be null.
     */
    public AbstractGetTileRequest(URL onlineResource, Properties properties) {
        this(onlineResource, properties, HTTPClientFinder.createClient());
    }

    public AbstractGetTileRequest(URL onlineResource, Properties properties, HTTPClient client) {
        super(onlineResource, properties);
        Objects.requireNonNull(client, "client");
        this.client = client;
    }

    @Override
    protected abstract void initVersion();

    @Override
    protected void initRequest() {
        setProperty(REQUEST, "GetTile");
    }

    @Override
    public void setLayer(WMTSLayer layer) {
        if (layer == null) {
            throw new IllegalArgumentException("Attempt to add a NULL layer to WMTS");
        }
        this.layer = layer;
        if (styleName.isEmpty()) {
            StyleImpl defaultStyle = layer.getDefaultStyle();
            if (defaultStyle != null) {
                styleName = defaultStyle.getName();
            }
        }
    }

    @Override
    public void setStyle(String styleName) {
        this.styleName = styleName;
    }

    public String getFormat() {
        return format;
    }

    @Override
    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public void setTileMatrixSet(String tileMatrixSet) {
        this.tileMatrixSet = tileMatrixSet;
    }

    protected String getTileMatrixSet() {
        return tileMatrixSet;
    }

    @Override
    public void setTileMatrix(String tileMatrix) {
        this.tileMatrix = tileMatrix;
    }

    public String getTileMatrix() {
        return tileMatrix;
    }

    @Override
    public void setTileRow(Integer tileRow) {
        this.tileRow = tileRow;
    }

    protected Integer getTileRow() {
        return tileRow;
    }

    @Override
    public void setTileCol(Integer tileCol) {
        this.tileCol = tileCol;
    }

    protected Integer getTileCol() {
        return tileCol;
    }

    @Override
    public void setRequestedHeight(int height) {
        this.requestedHeight = height;
    }

    @Override
    public void setRequestedWidth(int width) {
        this.requestedWidth = width;
    }

    @Override
    public void setRequestedBBox(ReferencedEnvelope requestedBBox) {
        this.requestedBBox = requestedBBox;
    }

    @Override
    public void setRequestedTime(String requestedTime) {
        this.requestedTime = requestedTime;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    /** @return the crs */
    public CoordinateReferenceSystem getCrs() {
        return crs;
    }

    @Override
    public void setCRS(CoordinateReferenceSystem coordinateReferenceSystem) {
        crs = coordinateReferenceSystem;
    }

    /** Compute the set of tiles needed to generate the image. */
    @Override
    public Set<Tile> getTiles() throws ServiceException {
        if (layer == null) {
            throw new ServiceException("GetTiles called with no layer set");
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(
                    "getTiles:  layer:"
                            + layer
                            + " w:"
                            + requestedWidth
                            + " x h:"
                            + requestedHeight);
        }

        TileMatrixSet matrixSet = selectMatrixSet();

        String templateUrl = createTemplateUrl(matrixSet.getIdentifier());

        templateUrl = WMTSHelper.replaceToken(templateUrl, "time", requestedTime);

        WMTSTileService wmtsService =
                new WMTSTileService(templateUrl, layer, matrixSet, this.client);

        @SuppressWarnings("unchecked")
        Map<String, String> extraHeaders =
                ((Map<String, String>)
                        (wmtsService
                                .getExtrainfo()
                                .computeIfAbsent(
                                        WMTSTileService.EXTRA_HEADERS, extra -> new HashMap<>())));
        extraHeaders.putAll(this.headers);

        double scale =
                Math.round(
                        RendererUtilities.calculateOGCScale(requestedBBox, requestedWidth, null));

        return wmtsService.findTilesInExtent(requestedBBox, (int) scale, false, MAXTILES);
    }

    /**
     * The templateUrl should contain {TileMatrix}, {TileRow} and {TileCol}.
     *
     * <p>The rest of the url should constitute a sufficient url to request a tile.
     *
     * @param tileMatrixSetName
     * @return
     */
    protected abstract String createTemplateUrl(String tileMatrixSetName);

    @Override
    public URL getFinalURL() {
        if (WMTSServiceType.REST.equals(type)) {
            if (layer.getTemplate(format) == null) {
                LOGGER.info("Template URL not available for format  " + format);
                type = WMTSServiceType.KVP;
                return onlineResource;
            } else {
                try {
                    return new URL(layer.getTemplate(format));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(
                            "URL for GetTile specified within capabilities is wrong.", e);
                }
            }
        } else {
            try {
                if (layer != null) {
                    setProperty(LAYER, URLEncoder.encode(layer.getName(), "UTF-8"));
                }
                if (styleName != null) {
                    setProperty(STYLE, URLEncoder.encode(styleName, "UTF-8"));
                }
                if (format != null) {
                    setProperty(FORMAT, URLEncoder.encode(format, "UTF-8"));
                }
                return super.getFinalURL();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Missing encoding for UTF-8.", e);
            }
        }
    }

    private TileMatrixSet selectMatrixSet() throws ServiceException, RuntimeException {
        TileMatrixSet retMatrixSet = null;

        Map<String, TileMatrixSetLink> links = layer.getTileMatrixLinks();
        CoordinateReferenceSystem requestCRS = getCrs();
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("request CRS " + (requestCRS == null ? "NULL" : requestCRS.getName()));
        }
        if (requestCRS == null) {
            try {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("request CRS decoding" + srs);
                }
                requestCRS = CRS.decode(srs);

            } catch (FactoryException e) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.log(Level.FINER, e.getMessage(), e);
                }
                throw new RuntimeException(e);
            }
        }

        // See if the layer supports the requested SRS. Matching against the SRS rather than the
        // full CRS will avoid missing matches due to Longitude first being set on the request
        // CRS and therefore minimise transformations that need to be performed on the received tile
        String requestSRS = CRS.toSRS(requestCRS);
        for (TileMatrixSet matrixSet : capabilities.getMatrixSets()) {

            CoordinateReferenceSystem matrixCRS = matrixSet.getCoordinateReferenceSystem();
            String matrixSRS = CRS.toSRS(matrixCRS);
            if (requestSRS.equals(matrixSRS)) { // matching SRS
                if (links.containsKey((matrixSet.getIdentifier()))) { // and available for
                    // this layer
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("selected matrix set:" + matrixSet.getIdentifier());
                    }
                    setTileMatrixSet(matrixSet.getIdentifier());
                    retMatrixSet = matrixSet;

                    break;
                }
            }
        }

        // The layer does not provide the requested CRS, so just take any one
        if (retMatrixSet == null) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info(
                        "Failed to match the requested CRS ("
                                + requestCRS.getName()
                                + ") with any of the tile matrices!");
            }
            for (TileMatrixSet matrix : capabilities.getMatrixSets()) {
                if (links.containsKey((matrix.getIdentifier()))) { // available for this layer
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("defaulting matrix set:" + matrix.getIdentifier());
                    }
                    setTileMatrixSet(matrix.getIdentifier());
                    retMatrixSet = matrix;

                    break;
                }
            }
        }
        if (retMatrixSet == null) {
            throw new ServiceException(
                    "Unable to find a matching TileMatrixSet for layer "
                            + layer.getName()
                            + " and SRS: "
                            + requestCRS.getName());
        }
        return retMatrixSet;
    }
}
