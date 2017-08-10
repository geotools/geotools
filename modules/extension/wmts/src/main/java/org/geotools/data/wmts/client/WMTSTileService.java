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
package org.geotools.data.wmts.client;

import org.geotools.data.wmts.model.TileMatrixLimits;
import org.geotools.data.wmts.model.WMTSServiceType;
import org.geotools.data.wmts.model.TileMatrixSet;
import org.geotools.data.wmts.model.TileMatrix;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.tile.Tile;
import org.geotools.tile.TileFactory;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.ScaleZoomLevelMatcher;
import org.geotools.tile.impl.ZoomLevel;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.operation.TransformException;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Envelope;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.wmts.model.TileMatrixSetLink;
import org.geotools.data.wmts.model.WMTSLayer;
import org.geotools.util.logging.Logging;

/**
 * A tile service for WMTS servers.
 *
 * This is tied to a single layer, style and matrixset.
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public class WMTSTileService extends TileService {

    protected static final Logger LOGGER = Logging
            .getLogger(WMTSTileService.class.getPackage().getName());

    private static final String OWS = "http://www.opengis.net/ows/1.1";

    private static final String XLINK = "http://www.w3.org/1999/xlink";

    public static final String DIMENSION_TIME = "time";

    public static final String DIMENSION_ELEVATION = "elevation";

    public static final String EXTRA_HEADERS = "HEADERS";

    private static final TileFactory tileFactory = new WMTSTileFactory();

    private String tileMatrixSetName = "";

    private double[] scaleList;

    private TileMatrixSet matrixSet;

    private final WMTSLayer layer;

    private String layerName;

    private String styleName = ""; // Default style is ""

    private ReferencedEnvelope envelope;

    private String templateURL = "";

    private WMTSServiceType type = WMTSServiceType.REST;

    private String format = "image/png";

    private List<TileMatrixLimits> limits;

    private File cachedGetCapabilities = null;

    private Map<String, String> dimensions = new HashMap<>();

    private Map<String, Object> extrainfo = new HashMap<>();

    /**
     * create a service directly with out parsing the capabilties again.
     *
     * @param requestURL
     *            - where to ask for tiles
     * @param type
     *            - KVP or REST
     * @param layerName
     *            - name of the layer to request
     * @param styleName
     *            - name of the style to use?
     * @param tileMatrixSetName
     *            - matrixset name
     */
    public WMTSTileService(String templateURL, WMTSServiceType type, WMTSLayer layer,
            String styleName, TileMatrixSet tileMatrixSet) {
        super("wmts", templateURL);

        this.layer = layer;
        this.tileMatrixSetName = tileMatrixSet.getIdentifier();
        TileMatrixSetLink tmsLink = layer.getTileMatrixLinks().get(tileMatrixSetName);
        this.limits = tmsLink.getLimits();
        this.envelope = new ReferencedEnvelope(layer.getLatLonBoundingBox());

        this.scaleList = buildScaleList(tileMatrixSet);

        setTemplateURL(templateURL);
        setLayerName(layer.getName());
        setStyleName(styleName);
        setType(type);
        setMatrixSet(tileMatrixSet);
    }

    private static double[] buildScaleList(TileMatrixSet tileMatrixSet) {

        double[] scaleList = new double[tileMatrixSet.size()];
        int j = 0;
        for (TileMatrix tm : tileMatrixSet.getMatrices()) {
            scaleList[j++] = tm.getDenominator();
        }

        return scaleList;
    }

    @Override
    public Set<Tile> findTilesInExtent(ReferencedEnvelope requestedExtent, int scaleFactor,
            boolean recommendedZoomLevel, int maxNumberOfTiles) {

        Set<Tile> ret = Collections.emptySet();

        CoordinateReferenceSystem reqCrs = requestedExtent.getCoordinateReferenceSystem();

        LOGGER.fine("orig request bbox :" + requestedExtent + " "
                + reqCrs.getCoordinateSystem().getAxis(0).getDirection() + " (" + reqCrs.getName()
                + ")");
        System.out.println("orig request bbox :" + requestedExtent + " "
                + reqCrs.getCoordinateSystem().getAxis(0).getDirection() + " (" + reqCrs.getName()
                + ")");
        // ReferencedEnvelope reqExtentInTileCrs = createSafeEnvelopeInTileCRS(
        // _mapExtent ) ;

        ReferencedEnvelope reqExtentInTileCrs = null;
        for (CRSEnvelope layerEnv : layer.getLayerBoundingBoxes()) {
            if (CRS.equalsIgnoreMetadata(reqCrs, layerEnv.getCoordinateReferenceSystem())) {
                // crop req extent according to layer bbox
                requestedExtent = requestedExtent.intersection(new ReferencedEnvelope(layerEnv));
                System.out.println("cropping request bbox :" + requestedExtent);
                break;
            } else {
                System.out.println("... no crs match: " + "req:" + reqCrs.getName() + " cov:"
                        + layerEnv.getCoordinateReferenceSystem().getName());
            }
        }

        CoordinateReferenceSystem tileCrs;
        try { // TODO: the matrixset should provide an already decoded CRS
            tileCrs = this.matrixSet.getCoordinateReferenceSystem();
            System.out.println("tile crs orig :" + tileCrs.getName());
        } catch (FactoryException ex) {
            LOGGER.log(Level.WARNING, "Tile CRS can't be decoded");
            return ret;
        }
        if (!CRS.equalsIgnoreMetadata(tileCrs, requestedExtent.getCoordinateReferenceSystem())) {
            try {
                reqExtentInTileCrs = requestedExtent.transform(tileCrs, true);
            } catch (TransformException | FactoryException ex) {
                LOGGER.log(Level.WARNING, "Requested extent can't be projected to tile CRS ("
                        + reqCrs.getCoordinateSystem().getName() + " -> "
                        + tileCrs.getCoordinateSystem().getName() + ") :" + ex.getMessage());

                // maybe the req area is too wide for the data; let's try an
                // inverse trasformation
                try {
                    ReferencedEnvelope covExtentInReqCrs = envelope.transform(reqCrs, true);
                    requestedExtent = requestedExtent.intersection(covExtentInReqCrs);
                    System.out.println(
                            "cropping request bbox by projectet layer env: " + requestedExtent);

                } catch (TransformException | FactoryException ex2) {
                    LOGGER.log(Level.WARNING, "Incompatible CRS: " + ex2.getMessage());
                    return ret; // should throw
                }
            }
        } else {
            reqExtentInTileCrs = requestedExtent;
        }

        if (reqExtentInTileCrs == null) {
            LOGGER.log(Level.FINE, "Requested extent not in tile CRS range");
            return ret;
        }

        LOGGER.log(Level.FINE,
                "tile crs req bbox :" + reqExtentInTileCrs + " "
                        + reqExtentInTileCrs.getCoordinateReferenceSystem().getCoordinateSystem()
                                .getAxis(0).getDirection()
                        + " (" + reqExtentInTileCrs.getCoordinateReferenceSystem().getName() + ")");
        System.out.println("tile crs req bbox :" + reqExtentInTileCrs + " "
                + reqExtentInTileCrs.getCoordinateReferenceSystem().getCoordinateSystem().getAxis(0)
                        .getDirection()
                + " (" + reqExtentInTileCrs.getCoordinateReferenceSystem().getName() + ")");

        ReferencedEnvelope coverageEnvelope = getBounds();
        LOGGER.log(Level.FINE,
                "coverage bbox :" + coverageEnvelope + " "
                        + coverageEnvelope.getCoordinateReferenceSystem().getCoordinateSystem()
                                .getAxis(0).getDirection()
                        + " (" + coverageEnvelope.getCoordinateReferenceSystem().getName() + ")");
        System.out.println("coverage bbox :" + coverageEnvelope + " "
                + coverageEnvelope.getCoordinateReferenceSystem().getCoordinateSystem().getAxis(0)
                        .getDirection()
                + " (" + coverageEnvelope.getCoordinateReferenceSystem().getName() + ")");

        ReferencedEnvelope requestEnvelopeWGS84;

        boolean sameCRS = CRS.equalsIgnoreMetadata(coverageEnvelope.getCoordinateReferenceSystem(),
                reqExtentInTileCrs.getCoordinateReferenceSystem());
        if (sameCRS) {
            if (!coverageEnvelope.intersects((BoundingBox) reqExtentInTileCrs)) {
                LOGGER.log(Level.FINE, "Extents do not intersect (sameCRS))");
                return ret;
            }
        } else {
            ReferencedEnvelope dataEnvelopeWGS84;
            try {
                dataEnvelopeWGS84 = coverageEnvelope.transform(DefaultGeographicCRS.WGS84, true);

                requestEnvelopeWGS84 = requestedExtent.transform(DefaultGeographicCRS.WGS84, true);

                if (!dataEnvelopeWGS84.intersects((BoundingBox) requestEnvelopeWGS84)) {
                    LOGGER.log(Level.FINE, "Extents do not intersect");
                    return ret;
                }
            } catch (TransformException | FactoryException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        TileFactory tileFactory = getTileFactory();

        ScaleZoomLevelMatcher zoomLevelMatcher = null;
        try {

            zoomLevelMatcher = ScaleZoomLevelMatcher.createMatcher(reqExtentInTileCrs,
                    matrixSet.getCoordinateReferenceSystem(), scaleFactor);

        } catch (FactoryException | TransformException e) {
            throw new RuntimeException(e);
        }

        int zl = getZoomLevelFromMapScale(zoomLevelMatcher, scaleFactor);
        ZoomLevel zoomLevel = tileFactory.getZoomLevel(zl, this);
        long maxNumberOfTilesForZoomLevel = zoomLevel.getMaxTileNumber();

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Zoom level:" + zl + "[" + zoomLevel.getMaxTilePerColNumber()
                    + " x " + zoomLevel.getMaxTilePerRowNumber() + "]");
        }

        // Map<String, Tile> tileList = new HashMap<String, Tile>();
        Set<Tile> tileList = new HashSet<>(
                (int) Math.min(maxNumberOfTiles, maxNumberOfTilesForZoomLevel));
        Tile firstTile;
        // Let's get the first tile which covers the upper-left corner
        if (/*
             * TileMatrix.isGeotoolsLongitudeFirstAxisOrderForced() ||
             */reqExtentInTileCrs.getCoordinateReferenceSystem().getCoordinateSystem().getAxis(0)
                .getDirection().equals(AxisDirection.EAST)) {
            firstTile = tileFactory.findTileAtCoordinate(reqExtentInTileCrs.getMinX(),
                    reqExtentInTileCrs.getMaxY(), zoomLevel, this);
        } else {
            LOGGER.log(Level.FINE, "Inverted tile coords!");
            firstTile = tileFactory.findTileAtCoordinate(reqExtentInTileCrs.getMinY(),
                    reqExtentInTileCrs.getMaxX(), zoomLevel, this);
        }

        LOGGER.log(Level.FINE,
                "Adding first tile " + firstTile.getId() + " " + firstTile.getExtent() + " ("
                        + firstTile.getExtent().getCoordinateReferenceSystem().getName() + ")");
        addTileToCache(firstTile);
        tileList.add(firstTile);

        Tile firstTileOfRow = firstTile;
        Tile movingTile = firstTile;

        // Loop column
        do {
            // Loop row
            do {

                // get the next tile right of this one
                // Tile rightNeighbour = movingTile.getRightNeighbour();
                Tile rightNeighbour = tileFactory.findRightNeighbour(movingTile, this);// movingTile.getRightNeighbour();

                // Check if the new tile is still part of the extent and
                // that we don't have the first tile again
                // boolean intersects = extent.intersects((Envelope)
                // rightNeighbour.getExtent());
                boolean intersects = reqExtentInTileCrs
                        .intersects((Envelope) rightNeighbour.getExtent());
                LOGGER.log(Level.FINE, "Intersect (" + rightNeighbour.getId() + " , "
                        + rightNeighbour.getExtent() + ") --> " + intersects);
                if (intersects && !firstTileOfRow.equals(rightNeighbour)) {
                    LOGGER.log(Level.FINE, "Adding right neighbour " + rightNeighbour.getId());

                    addTileToCache(rightNeighbour);
                    tileList.add(rightNeighbour);

                    movingTile = rightNeighbour;
                } else {
                    LOGGER.log(Level.FINE, "Stopping on right neighbour " + rightNeighbour.getId());

                    break;
                }
                if (tileList.size() > maxNumberOfTiles) {
                    LOGGER.warning("Reached tile limit of " + maxNumberOfTiles
                            + ". Returning the tiles collected so far.");
                    return tileList;
                }
            } while (tileList.size() < maxNumberOfTilesForZoomLevel);

            // get the next tile under the first one of the row
            // Tile lowerNeighbour = firstTileOfRow.getLowerNeighbour();
            Tile lowerNeighbour = tileFactory.findLowerNeighbour(firstTileOfRow, this);

            // Check if the new tile is still part of the extent
            boolean intersects = reqExtentInTileCrs
                    .intersects((Envelope) lowerNeighbour.getExtent());
            LOGGER.log(Level.FINE, "Intersect (" + lowerNeighbour.getId() + ") --> " + intersects);
            if (intersects && !firstTile.equals(lowerNeighbour)) {
                LOGGER.log(Level.FINE, "Adding lower neighbour " + lowerNeighbour.getId());

                // System.out.printf("N: %s %s", lowerNeighbour.getId(),
                // addTileToList(lowerNeighbour));

                addTileToCache(lowerNeighbour);
                tileList.add(lowerNeighbour);

                firstTileOfRow = movingTile = lowerNeighbour;
            } else {
                LOGGER.log(Level.FINE, "Stopping on lower neighbour " + lowerNeighbour.getId());
                break;
            }
        } while (tileList.size() < maxNumberOfTilesForZoomLevel);

        return tileList;
    }

    /**
     * @return the type
     */
    public WMTSServiceType getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(WMTSServiceType type) {
        this.type = type;
    }

    /**
     * @param layerName
     */
    private void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    /**
     * @return the layerName
     */
    public String getLayerName() {
        return layerName;
    }

    /**
     * @return the styleName
     */
    public String getStyleName() {
        return styleName;
    }

    /**
     * @param styleName
     *            the styleName to set
     */
    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    @Override
    public double[] getScaleList() {
        return scaleList;
    }

    @Override
    public ReferencedEnvelope getBounds() {

        return envelope;
        // if (envelope != null) {
        // return envelope;
        // }
        // // Look this up from the CRS
        // CoordinateReferenceSystem projectedTileCrs = getProjectedTileCrs();
        // return envelope = getAcceptableExtent(projectedTileCrs);
    }

    @Override
    public CoordinateReferenceSystem getProjectedTileCrs() {
        try {
            return matrixSet.getCoordinateReferenceSystem();
        } catch (FactoryException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public TileFactory getTileFactory() {
        return tileFactory;
    }

    /**
     * @return the tileMatrixSetName
     */
    public String getTileMatrixSetName() {
        return tileMatrixSetName;
    }

    /**
     * @param tileMatrixSetName
     *            the tileMatrixSetName to set
     */
    public void setTileMatrixSetName(String tileMatrixSetName) {
        if (tileMatrixSetName == null || tileMatrixSetName.isEmpty()) {
            throw new IllegalArgumentException("Tile matrix set name cannot be null");
        }

        this.tileMatrixSetName = tileMatrixSetName;
    }

    /**
     * @return the limits
     */
    public List<TileMatrixLimits> getLimits() {

        return limits;
    }

    /**
     * @param limits
     *            the limits to set
     */
    public void setLimits(List<TileMatrixLimits> limits) {
        this.limits = limits;
    }

    private org.w3c.dom.Document parseCachedCapabilities() throws IOException {

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            org.w3c.dom.Document doc = db.parse(cachedGetCapabilities);
            return doc;
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("Error handling getCapabilities document", e);
        }
    }

    private org.w3c.dom.Document getKVPCapabilitiesDoc() throws IOException {

        if (cachedGetCapabilities != null) {
            return parseCachedCapabilities();
        }

        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(getBaseUrl());
        method.setQueryString("REQUEST=GetCapabilities");
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        try {
            // Execute the method.

            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                LOGGER.severe("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            byte[] responseBody = method.getResponseBody();

            // Deal with the response.
            // Use caution: ensure correct character encoding and is not binary
            // data
            // System.out.println(new String(responseBody,"UTF-8"));

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            org.w3c.dom.Document doc = db.parse(new ByteArrayInputStream(responseBody));
            return doc;
        } catch (HttpException e) {
            throw new IOException("Error handling getCapabilities document", e);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new IOException("Error handling getCapabilities document", e);
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
    }

    private org.w3c.dom.Document getRESTCapabilitiesDoc() throws IOException {

        if (cachedGetCapabilities != null) {
            return parseCachedCapabilities();
        }

        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(getBaseUrl());
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                LOGGER.severe("Method failed: " + method.getStatusLine());

            }

            // Read the response body.
            byte[] responseBody = method.getResponseBody();

            // Deal with the response.
            // Use caution: ensure correct character encoding and is not binary
            // data
            // System.out.println(new String(responseBody,"UTF-8"));

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            org.w3c.dom.Document doc = db.parse(new ByteArrayInputStream(responseBody));
            return doc;
        } catch (HttpException e) {
            throw new IOException("Error handling REST getCapabilities document", e);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new IOException("Error handling REST getCapabilities document", e);
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
    }

    /**
     * @return the templateURL
     */
    public String getTemplateURL() {
        return templateURL;
    }

    /**
     * @param templateURL
     *            the templateURL to set
     */
    public void setTemplateURL(String templateURL) {
        this.templateURL = templateURL;
    }

    /**
     * @param zoomLevel
     * @return
     */
    public TileMatrix getTileMatrix(int zoomLevel) {
        if (matrixSet == null) {
            throw new RuntimeException("TileMatrix is not set in WMTSService");
        }
        return matrixSet.getMatrices().get(zoomLevel);
    }

    /**
     * @return the matrixSet
     */
    public TileMatrixSet getMatrixSet() {
        return matrixSet;
    }

    /**
     * @param matrixSet
     *            the matrixSet to set
     */
    public void setMatrixSet(TileMatrixSet matrixSet) {
        this.matrixSet = matrixSet;
        scaleList = new double[matrixSet.size()];
        int j = 0;
        for (TileMatrix tm : matrixSet.getMatrices()) {
            scaleList[j++] = tm.getDenominator();
        }
    }

    /**
     * @return
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format
     *            the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     */
    public WMTSZoomLevel getZoomLevel(int zoom) {
        //
        return new WMTSZoomLevel(zoom, this);
    }

    public Map<String, String> getDimensions() {
        return dimensions;
    }

    public Map<String, Object> getExtrainfo() {
        return extrainfo;
    }

}
