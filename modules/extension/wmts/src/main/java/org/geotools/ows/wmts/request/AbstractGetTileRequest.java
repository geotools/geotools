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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.ows.Response;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPClientFinder;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.StyleImpl;
import org.geotools.ows.wmts.client.WMTSTileFactory;
import org.geotools.ows.wmts.client.WMTSTileService;
import org.geotools.ows.wmts.model.TileMatrixLimits;
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

    public static final String TILECOL = "TileCol";

    public static final String TILEROW = "TileRow";

    public static final String TILEMATRIX = "TileMatrix";

    public static final String TILEMATRIXSET = "TileMatrixSet";

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

    private String format;

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

    protected abstract void initVersion();

    protected void initRequest() {
        setProperty(REQUEST, "GetTile");
    }

    @Override
    public Response createResponse(HTTPResponse response) throws ServiceException, IOException {
        // TODO Auto-generated method stub
        return null;
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

    public void setFormat(String format) {
        this.format = format;
    }

    public void setRequestedHeight(int height) {
        this.requestedHeight = height;
    }

    public void setRequestedWidth(int width) {
        this.requestedWidth = width;
    }

    public void setRequestedBBox(ReferencedEnvelope requestedBBox) {
        this.requestedBBox = requestedBBox;
    }

    public void setRequestedTime(String requestedTime) {
        this.requestedTime = requestedTime;
    }

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
        Set<Tile> tiles;
        if (layer == null) {
            throw new ServiceException("GetTiles called with no layer set");
        }

        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("getTiles: layer:" + layer);

        String layerString = "";
        String styleString = "";

        try {
            // spaces are converted to plus signs, but must be %20 for url calls
            // [GEOT-4317]
            layerString = URLEncoder.encode(layer.getName(), "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException | NullPointerException e) {
            layerString = layerString + layer.getName();
        }
        styleName = styleName == null ? "" : styleName;
        try {
            styleString = URLEncoder.encode(styleName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException | NullPointerException e1) {
            styleString = styleString + styleName;
        }

        setProperty(LAYER, layerString);
        setProperty(STYLE, styleString);

        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine(
                    "getTiles:  layer:"
                            + layer
                            + " w:"
                            + requestedWidth
                            + " x h:"
                            + requestedHeight);

        TileMatrixSet matrixSet = selectMatrixSet();
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
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("Format not set, trying with " + format);
        }
        String requestUrl = getFinalURL().toExternalForm();
        // TODO - Add properties that match the URL {}
        WMTSTileService wmtsService =
                new WMTSTileService(requestUrl, type, layer, styleString, matrixSet, this.client);

        wmtsService.setFormat(format);

        wmtsService.getDimensions().put(WMTSTileService.DIMENSION_TIME, requestedTime);

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

        // these are all the tiles available in the tilematrix within the requested bbox
        tiles = wmtsService.findTilesInExtent(requestedBBox, (int) scale, false, MAXTILES);
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("found " + tiles.size() + " tiles in " + requestedBBox);
        if (tiles.isEmpty()) {
            return tiles;
        }
        Tile first = tiles.iterator().next();
        int z = first.getTileIdentifier().getZ();

        TileMatrixSetLink tmsl = layer.getTileMatrixLinks().get(matrixSet.getIdentifier());
        TileMatrixLimits limit = WMTSTileFactory.getLimits(tmsl, matrixSet, z);

        // remove tiles outside layer's limits
        List<Tile> tilesOutsideLimits = new ArrayList<>();
        for (Tile tile : tiles) {

            int x = tile.getTileIdentifier().getX();
            int y = tile.getTileIdentifier().getY();
            if (x < limit.getMincol() || x > limit.getMaxcol()) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine(
                            "col "
                                    + x
                                    + " outside limits "
                                    + limit.getMincol()
                                    + " "
                                    + limit.getMaxcol());
                tilesOutsideLimits.add(tile);
                continue;
            }

            if (y < limit.getMinrow() || y > limit.getMaxrow()) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine(
                            "row "
                                    + y
                                    + " outside limits "
                                    + limit.getMinrow()
                                    + " "
                                    + limit.getMaxrow());
                tilesOutsideLimits.add(tile);
            }
        }
        tiles.removeAll(tilesOutsideLimits);

        return tiles;
    }

    @Override
    public URL getFinalURL() {
        String requestUrl = onlineResource.toString();
        if (WMTSServiceType.REST.equals(type)) {
            requestUrl = layer.getTemplate(format);
            if (requestUrl == null) {
                if (LOGGER.isLoggable(Level.INFO))
                    LOGGER.info("Template URL not available for format  " + format);
                type = WMTSServiceType.KVP;
                requestUrl = onlineResource.toString();
            }
        }
        URL ret = null;
        try {
            ret = new URL(requestUrl);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
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
                    setProperty(TILEMATRIXSET, matrixSet.getIdentifier());
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
                    setProperty(TILEMATRIXSET, matrix.getIdentifier());
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
