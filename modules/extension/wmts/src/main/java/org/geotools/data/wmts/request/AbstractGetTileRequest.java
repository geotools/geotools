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

package org.geotools.data.wmts.request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.Response;
import org.geotools.data.wmts.client.WMTSTileService;
import org.geotools.data.wmts.client.WMTSTileFactory;
import org.geotools.data.wmts.model.WMTSCapabilities;
import org.geotools.data.wmts.model.WMTSLayer;
import org.geotools.data.wmts.model.TileMatrixLimits;
import org.geotools.data.wmts.model.TileMatrixSet;
import org.geotools.data.wmts.model.TileMatrixSetLink;
import org.geotools.data.wmts.model.WMTSServiceType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.tile.Tile;

import org.geotools.util.logging.Logging;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

/**
 * 
 * (Based on existing work by rgould for WMS service)
 * 
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public abstract class AbstractGetTileRequest extends AbstractWMTSRequest implements GetTileRequest {

    /** MAXTILES */
    private static final int MAXTILES = 256;

    /** DPI */
    private static final double DPI = 96.0;

    static WMTSTileFactory factory = new WMTSTileFactory();

    public static final String LAYER = "Layer";

    public static final String STYLE = "Style";

    public static final String TILECOL = "TileCol";

    public static final String TILEROW = "TileRow";

    public static final String TILEMATRIX = "TileMatrix";

    public static final String TILEMATRIXSET = "TileMatrixSet";

    private WMTSLayer layer = null;

    private String styleName = "";

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

    /**
     * Constructs a GetMapRequest. The data passed in represents valid values
     * that can be used.
     *
     * @param onlineResource
     *            the location that the request should be applied to
     * @param properties
     *            pre-set properties to be used. Can be null.
     */
    public AbstractGetTileRequest(URL onlineResource, Properties properties) {
        super(onlineResource, properties);
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
        this.layer = layer;
    }

    @Override
    public void setStyle(String styleName) {
        this.styleName = styleName;
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

    // @Override
    // public void setSRS(String srs) {
    // this.srs = srs;
    // super.setSRS(srs);
    // }

    /**
     * @return the crs
     */
    public CoordinateReferenceSystem getCrs() {
        return crs;
    }

    @Override
    public void setCRS(CoordinateReferenceSystem coordinateReferenceSystem) {
        crs = coordinateReferenceSystem;

    }

    /**
     * fetch the tiles we need to generate the image
     * 
     * @throws ServiceException
     */
    @Override
    public Set<Tile> getTiles() throws ServiceException {
        Set<Tile> tiles;
        if (layer == null) {
            throw new ServiceException("GetTiles called with no layer set");
        }

        LOGGER.warning("===== getTiles    layer:" + layer);

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

        LOGGER.warning("===== getTiles    layer:" + layer + " w:" + requestedWidth + " x h:"
                + requestedHeight);

        TileMatrixSet matrixSet = selectMatrixSet();

        String requestUrl = onlineResource.toString();
        if (WMTSServiceType.REST.equals(type)) {
            String format = (String) getProperties().get("Format");
            if (format == null || format.isEmpty()) {
                format = "image/png";
                LOGGER.fine("Format not set, trying with " + format);
            }
            requestUrl = layer.getTemplate(format);
            if (requestUrl == null) {
                LOGGER.info("Template URL not available for format  " + format);
                format = layer.getFormats().get(0);
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(
                            "Available formats: " + layer.getFormats() + " -- Selecting " + format);
                }
                requestUrl = layer.getTemplate(format);
            }
        }

        WMTSTileService wmtsService = new WMTSTileService(requestUrl, type, layer, styleString,
                matrixSet);

        wmtsService.getDimensions().put(WMTSTileService.DIMENSION_TIME, requestedTime);

        ((Map) (wmtsService.getExtrainfo().computeIfAbsent(WMTSTileService.EXTRA_HEADERS,
                extra -> new HashMap<>()))).putAll(this.headers);

        // zoomLevel = factory.getZoomLevel(zoom, wmtsService);
        int scale = 0;

        try {
            scale = (int) Math.round(RendererUtilities.calculateScale(requestedBBox, requestedWidth,
                    requestedHeight, DPI));
        } catch (FactoryException | TransformException ex) {
            throw new RuntimeException("Failed to calculate scale", ex);
        }
        tiles = wmtsService.findTilesInExtent(requestedBBox, scale, false, MAXTILES);
        LOGGER.fine("found " + tiles.size() + " tiles in " + requestedBBox);
        if (tiles.isEmpty()) {
            return tiles;
        }
        Tile first = tiles.iterator().next();
        int z = first.getTileIdentifier().getZ();
        List<TileMatrixLimits> limits = layer.getTileMatrixLinks().get(matrixSet.getIdentifier())
                .getLimits();
        TileMatrixLimits limit;
        if (!limits.isEmpty()) {

            limit = limits.get(z);
        } else {
            // seems that MapProxy (and all REST APIs?) don't create limits
            limit = new TileMatrixLimits();
            limit.setMaxCol(matrixSet.getMatrices().get(z).getMatrixWidth() - 1);
            limit.setMaxRow(matrixSet.getMatrices().get(z).getMatrixHeight() - 1);
            limit.setMinCol(0);
            limit.setMinRow(0);
            limit.setTileMatix(matrixSet.getIdentifier());
        }
        List<Tile> remove = new ArrayList<>();
        for (Tile tile : tiles) {

            int x = tile.getTileIdentifier().getX();
            int y = tile.getTileIdentifier().getY();
            if (x < limit.getMincol() || x > limit.getMaxcol()) {
                LOGGER.fine(
                        x + " exceeds col limits " + limit.getMincol() + " " + limit.getMaxcol());
                remove.add(tile);
                continue;
            }

            if (y < limit.getMinrow() || y > limit.getMaxrow()) {
                LOGGER.fine(
                        y + " exceeds row limits " + limit.getMinrow() + " " + limit.getMaxrow());
                remove.add(tile);
            }
        }
        tiles.removeAll(remove);

        return tiles;
    }

    private TileMatrixSet selectMatrixSet() throws ServiceException, RuntimeException {
        TileMatrixSet retMatrixSet = null;

        Map<String, TileMatrixSetLink> links = layer.getTileMatrixLinks();
        CoordinateReferenceSystem requestCRS = getCrs();
        LOGGER.fine("request CRS " + requestCRS.getName());
        if (requestCRS == null) {
            try {
                LOGGER.fine("request CRS decoding" + srs);
                requestCRS = CRS.decode(srs);

            } catch (FactoryException e) {
                LOGGER.log(Level.FINER, e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        /* System.out.println(requestCRS); */
        for (TileMatrixSet matrixSet : capabilities.getMatrixSets()) {

            CoordinateReferenceSystem matrixCRS = null;
            try {
                matrixCRS = matrixSet.getCoordinateReferenceSystem();
            } catch (FactoryException e) {
                LOGGER.log(Level.FINER, e.getMessage(), e);
            }
            /* System.out.println("comparing "+coordinateReferenceSystem); */
            // TODO: possible issues here if axis order is not the same
            if (CRS.equalsIgnoreMetadata(requestCRS, matrixCRS)) {// matching
                                                                      // SRS
                if (links.containsKey((matrixSet.getIdentifier()))) { // and
                                                                          // available
                                                                      // for
                                                                      // this
                                                                      // layer
                    LOGGER.fine("selected matrix set:" + matrixSet.getIdentifier());
                    setProperty(TILEMATRIXSET, matrixSet.getIdentifier());
                    retMatrixSet = matrixSet;

                    break;
                }
            }
        }

        if (retMatrixSet == null) {
            // Just pick one!
            LOGGER.warning("Failed to match the requested CRS (" + requestCRS.getName()
                    + ") with any of the tile matrices!");
            for (TileMatrixSet matrix : capabilities.getMatrixSets()) {
                if (links.containsKey((matrix.getIdentifier()))) { // available
                                                                       // for this
                                                                   // layer
                    LOGGER.fine("defaulting matrix set:" + matrix.getIdentifier());
                    setProperty(TILEMATRIXSET, matrix.getIdentifier());
                    retMatrixSet = matrix;

                    break;
                }
            }
            if (retMatrixSet == null) {
                throw new ServiceException("Unable to find a matching TileMatrixSet for layer "
                        + layer.getName() + " and SRS: " + requestCRS.getName());
            }
        }
        return retMatrixSet;
    }

}
