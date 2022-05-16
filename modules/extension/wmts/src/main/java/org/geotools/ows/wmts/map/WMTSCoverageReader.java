/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wmts.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.xml.Dimension;
import org.geotools.ows.wmts.WMTSSpecification;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.client.WMTSTileService;
import org.geotools.ows.wmts.model.TileMatrixSet;
import org.geotools.ows.wmts.model.TileMatrixSetLink;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.lite.gridcoverage2d.GridCoverageRendererUtilities;
import org.geotools.tile.Tile;
import org.geotools.tile.TileService;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.Format;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.operation.TransformException;

/**
 * A grid coverage readers backing onto a WMTS server by issuing GetTile requests
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public class WMTSCoverageReader extends AbstractGridCoverage2DReader {

    /** The logger for the map module. */
    public static final Logger LOGGER = Logging.getLogger(WMTSCoverageReader.class);

    static GridCoverageFactory gcf = new GridCoverageFactory();

    private static final int MAX_TILES = 1000;

    /** The WMTS server */
    final WebMapTileServer wmts;

    /** The layer */
    private final WMTSLayer layer;

    /** Cache of WMTSTileService for a given TileRequest */
    private Map<String, WMTSTileService> services = new HashMap<>();

    /** The default SRS name for this layer */
    String srsName;

    /** The format to use for requests */
    String format;

    /** The set of SRS available for this layer */
    Set<String> validSRS;

    /** The cached layer bounds */
    ReferencedEnvelope bounds;

    /** Keep the last tileRequest that was handled by this Reader */
    private GetTileRequest lastTileRequest;

    private String requestedTime;

    public final boolean debug = System.getProperty("wmts.debug") != null;

    /** Builds a new WMS coverage reader */
    public WMTSCoverageReader(WebMapTileServer server, Layer layer) {
        this.wmts = server;
        this.layer = (WMTSLayer) layer;

        // init the reader
        updateLayerBounds();

        // best guess at the format with a preference for PNG (since it's
        // normally transparent)
        List<String> formats = ((WMTSLayer) layer).getFormats();
        this.format = formats.iterator().next();
        for (String f : formats) {
            if ("image/png".equals(f)
                    || "image/png24".equals(f)
                    || "png".equals(f)
                    || "png24".equals(f)
                    || "image/png; mode=24bit".equals(f)) {
                this.format = f;
                break;
            }
        }
    }

    /** Set the srsName, validSRS and bounds based on the setting in layer */
    final void updateLayerBounds() {

        if (srsName == null) { // initialize from first (unique) layer

            // prefer 4326
            for (String preferred :
                    new String[] {"EPSG:4326", "WGS84", "CRS:84", "WGS 84", "WGS84(DD)"}) {
                if (layer.getSrs().contains(preferred)) {
                    srsName = preferred;
                    LOGGER.info(() -> "defaulting CRS to: " + srsName);
                }
            }

            // no 4326, let's see if the layer is offering something valid
            if (srsName == null) {
                for (String srs : layer.getSrs()) {
                    try {
                        // check it's valid, if not we crap out and move to the
                        // next
                        CRS.decode(srs);
                        srsName = srs;

                        LOGGER.info(() -> "setting CRS: " + srsName);
                        break;
                    } catch (Exception e) {
                        // it's fine, we could not decode that code
                    }
                }
            }

            if (srsName == null) {
                if (layer.getSrs().isEmpty()) {
                    // force 4326
                    LOGGER.info(() -> "adding default CRS to: " + srsName);
                    srsName = "EPSG:4326";
                    layer.getSrs().add(srsName);
                } else {
                    // if not even that works we just take the first...
                    srsName = layer.getSrs().iterator().next();
                    LOGGER.info(() -> "guessing CRS to: " + srsName);
                }
            }

            validSRS = layer.getSrs();

        } else {
            LOGGER.fine("Update validSRS and SrsName based on changes within layer.");

            Set<String> intersection = new HashSet<>(validSRS);
            intersection.retainAll(layer.getSrs());

            // can we reuse what we have?
            if (!intersection.contains(srsName)) {
                if (intersection.isEmpty()) {
                    throw new IllegalArgumentException(
                            "The layer being appended does "
                                    + "not have any SRS in common with the ones already "
                                    + "included in the  request, cannot be merged");
                } else if (intersection.contains("EPSG:4326")) {
                    srsName = "EPSG:4326";
                } else {
                    // if not even that works we just take the first...
                    srsName = intersection.iterator().next();
                }
                this.validSRS = intersection;
            }
        }

        CoordinateReferenceSystem crs = null;
        try {
            crs = CRS.decode(srsName);
        } catch (Exception e) {
            LOGGER.log(
                    Level.WARNING,
                    "Default crs (" + srsName + ") for layer (" + layer + ") couldn't be set.",
                    e);
        }
        this.crs = crs;
        updateBounds();
    }

    @Override
    public GridCoverage2D read(GeneralParameterValue[] parameters)
            throws IllegalArgumentException, IOException {

        // check out if time coordinate is needed, and provide a valid default
        String time = null;
        for (Dimension dim : layer.getLayerDimensions()) {
            if ("time".equalsIgnoreCase(dim.getName())) {
                time = dim.getExtent().getDefaultValue();
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine("TIME dimension found, default is " + time);
                break;
            }
        }
        if (requestedTime != null) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("TIME dimension requested: " + requestedTime);
            time = requestedTime;
        }

        WMTSReadParameters readParameters =
                new WMTSReadParameters(parameters, getOriginalEnvelope());

        ReferencedEnvelope requestedEnvelope = reference(readParameters.getRequestedEnvelope());
        if (isNativelySupported(requestedEnvelope.getCoordinateReferenceSystem())) {
            return getMap(requestedEnvelope, time, readParameters);
        } else {
            return getMapReproject(requestedEnvelope, time, readParameters);
        }
    }

    /** Execute the GetMap request without any projection. */
    GridCoverage2D getMap(
            ReferencedEnvelope requestedEnvelope, String time, WMTSReadParameters readParameters)
            throws IOException {

        TileRequest request =
                initTileRequest(
                        requestedEnvelope,
                        readParameters.getWidth(),
                        readParameters.getHeight(),
                        time);

        Set<Tile> tiles = findTiles(request);

        return createTileMap(requestedEnvelope, tiles);
    }

    /** Get the WMTSTileService to use for the given request, and use that to find tile's */
    Set<Tile> findTiles(TileRequest request) {
        TileService service = obtainTileService(request);
        double scale = computeScale(request);
        return service.findTilesInExtent(request.envelope, scale, false, MAX_TILES);
    }

    /** Computing the scale using the width and the envelope */
    double computeScale(TileRequest request) {
        return Math.round(
                RendererUtilities.calculateOGCScale(request.envelope, request.width, null));
    }

    /** Downloading and putting the individual tiles together into a single coverage */
    protected GridCoverage2D createTileMap(ReferencedEnvelope tileEnvelope, Set<Tile> tiles)
            throws IOException {

        if (tiles.isEmpty()) {
            LOGGER.fine(() -> "Found 0 tiles in " + tileEnvelope);
            throw new RuntimeException("No tiles were found in requested extent");
        }
        try {

            AffineTransform at = null;
            ReferencedEnvelope global = null;
            for (Tile tile : tiles) {
                ReferencedEnvelope extent = tile.getExtent();
                // ensure the extent has EAST_NORTH axis order because otherwise
                // RendererUtilities.worldToScreenTransform will produce
                // incorrect results:
                extent = toEastNorthAxisOrder(extent);

                if (global == null) {
                    global = new ReferencedEnvelope(extent);
                } else {
                    global.expandToInclude(extent);
                }
                BufferedImage bi = tile.getBufferedImage();
                if (at == null) {
                    at =
                            RendererUtilities.worldToScreenTransform(
                                    extent, new Rectangle(bi.getWidth(), bi.getHeight()));
                }
            }
            int imageWidth = (int) Math.round(global.getWidth() * at.getScaleX());
            int imageHeight = (int) Math.abs(Math.round(global.getHeight() * at.getScaleY()));
            BufferedImage image =
                    new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

            AffineTransform targetTransform =
                    RendererUtilities.worldToScreenTransform(
                            global, new Rectangle(0, 0, imageWidth, imageHeight));
            renderTiles(
                    tiles,
                    image.createGraphics(),
                    toEastNorthAxisOrder(tileEnvelope),
                    targetTransform);

            return gcf.create(layer.getTitle(), image, global);
        } catch (FactoryException e) {
            throw new IOException("GetMap failed", e);
        }
    }

    /** WMTSTileServices are kept in a Map to keep onto the tile-cache they possess */
    @SuppressWarnings("unchecked")
    TileService obtainTileService(TileRequest request) {
        final String key = request.tileSRS + (request.time != null ? "|" + request.time : "");
        if (services.containsKey(key)) {
            return services.get(key);
        }

        TileMatrixSet matrixSet = selectMatrixSet(request);

        WMTSTileService newService = new WMTSTileService(wmts, layer, matrixSet, format);
        if (request.time != null) {
            ((Map<String, String>) newService.getExtrainfo().get(WMTSTileService.EXTRA_PARAMETERS))
                    .put(WMTSSpecification.DIMENSION_TIME, request.time);
        }
        services.put(key, newService);
        return newService;
    }

    private TileMatrixSet selectMatrixSet(TileRequest request) {
        TileMatrixSet retMatrixSet = null;

        Map<String, TileMatrixSetLink> links = layer.getTileMatrixLinks();
        CoordinateReferenceSystem requestCRS = request.envelope.getCoordinateReferenceSystem();
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("request CRS " + (requestCRS == null ? "NULL" : requestCRS.getName()));
        }
        if (requestCRS == null && request.tileSRS != null) {
            try {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("request CRS decoding" + request.tileSRS);
                }
                requestCRS = CRS.decode(request.tileSRS);

            } catch (FactoryException e) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.log(Level.FINER, e.getMessage(), e);
                }
                throw new RuntimeException(e);
            }
        }
        if (requestCRS == null && request.envelope != null) {
            requestCRS = request.envelope.getCoordinateReferenceSystem();
        }

        if (requestCRS == null) {
            throw new IllegalStateException("CRS or SRS wasn't set for this WMTSCoverageReader.");
        }

        // See if the layer supports the requested SRS. Matching against the SRS rather than the
        // full CRS will avoid missing matches due to Longitude first being set on the request
        // CRS and therefore minimise transformations that need to be performed on the received tile
        String requestSRS = CRS.toSRS(requestCRS);
        for (TileMatrixSet matrixSet : wmts.getCapabilities().getMatrixSets()) {

            CoordinateReferenceSystem matrixCRS = matrixSet.getCoordinateReferenceSystem();
            String matrixSRS = CRS.toSRS(matrixCRS);
            if (requestSRS.equals(matrixSRS)) { // matching SRS
                if (links.containsKey((matrixSet.getIdentifier()))) { // and available for
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
                LOGGER.info(
                        "Failed to match the requested CRS ("
                                + requestCRS.getName()
                                + ") with any of the tile matrices!");
            }
            for (TileMatrixSet matrix : wmts.getCapabilities().getMatrixSets()) {
                if (links.containsKey((matrix.getIdentifier()))) { // available for this layer
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("defaulting matrix set:" + matrix.getIdentifier());
                    }
                    retMatrixSet = matrix;

                    break;
                }
            }
        }
        if (retMatrixSet == null) {
            throw new IllegalStateException(
                    "Unable to find a matching TileMatrixSet for layer "
                            + layer.getName()
                            + " and SRS: "
                            + requestCRS.getName());
        }
        return retMatrixSet;
    }

    /**
     * Checks if a referenced envelope has EAST_NORTH axis order and if not creates a copy with
     * EAST_NORTH axis order.
     *
     * @param envelope The referenced envelope.
     * @return The referenced envelope with EAST_NORTH axis order.
     * @throws FactoryException
     * @throws TransformException
     */
    private ReferencedEnvelope toEastNorthAxisOrder(ReferencedEnvelope envelope)
            throws FactoryException {
        CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
        if (CRS.getAxisOrder(crs) != CRS.AxisOrder.NORTH_EAST) {
            return envelope;
        }
        Integer epsg = CRS.lookupEpsgCode(crs, false);
        if (epsg == null) {
            return envelope;
        } else {
            CoordinateReferenceSystem eastNorthCrs = CRS.decode("EPSG:" + epsg, true);
            return new ReferencedEnvelope(
                    envelope.getMinY(),
                    envelope.getMaxY(),
                    envelope.getMinX(),
                    envelope.getMaxX(),
                    eastNorthCrs);
        }
    }

    protected void renderTiles(
            Collection<Tile> tiles,
            Graphics2D g2d,
            ReferencedEnvelope viewportExtent,
            AffineTransform worldToImageTransform) {

        g2d.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        double[] inPoints = new double[4];
        double[] outPoints = new double[4];

        for (Tile tile : tiles) {
            ReferencedEnvelope nativeTileEnvelope = tile.getExtent();

            ReferencedEnvelope tileEnvViewport;
            try {
                tileEnvViewport =
                        nativeTileEnvelope.transform(
                                viewportExtent.getCoordinateReferenceSystem(), true);
            } catch (TransformException | FactoryException e) {
                throw new RuntimeException(e);
            }

            inPoints[0] = tileEnvViewport.getMinX();
            inPoints[3] = tileEnvViewport.getMinY();
            inPoints[2] = tileEnvViewport.getMaxX();
            inPoints[1] = tileEnvViewport.getMaxY();
            worldToImageTransform.transform(inPoints, 0, outPoints, 0, 2);

            renderTile(tile, g2d, outPoints);

            if (debug) {
                g2d.setColor(Color.RED);
                g2d.drawRect(
                        (int) outPoints[0],
                        (int) outPoints[1],
                        (int) Math.ceil(outPoints[2] - outPoints[0]),
                        (int) Math.ceil(outPoints[3] - outPoints[1]));
                int x = (int) outPoints[0] + (int) (Math.ceil(outPoints[2] - outPoints[0]) / 2);
                int y = (int) outPoints[1] + (int) (Math.ceil(outPoints[3] - outPoints[1]) / 2);
                g2d.drawString(tile.getId(), x, y);
            }
        }
    }

    protected void renderTile(Tile tile, Graphics2D g2d, double[] points) {

        BufferedImage img = getTileImage(tile);
        if (img == null) {
            if (LOGGER.isLoggable(Level.INFO)) LOGGER.info("couldn't draw " + tile.getId());
            return;
        }
        int width = (int) Math.round(points[2] - points[0]);
        int height = (int) Math.round(points[3] - points[1]);
        if (width < 1) width = 1;
        if (height < 1) height = 1;
        g2d.drawImage(
                img, (int) Math.round(points[0]), (int) Math.round(points[1]), width, height, null);
    }

    protected BufferedImage getTileImage(Tile tile) {
        return tile.getBufferedImage();
    }

    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return crs;
    }

    /**
     * Sets up a map request with the provided parameters, making sure it is compatible with the
     * layers own native SRS list
     */
    TileRequest initTileRequest(ReferencedEnvelope bbox, int width, int height, String time)
            throws IOException {

        ReferencedEnvelope gridEnvelope = bbox;
        String requestSrs = srsName;
        try {
            // first see if we can cascade the request in its native SRS
            // we first look for an official epsg code
            String code = null;
            Integer epsgCode = CRS.lookupEpsgCode(bbox.getCoordinateReferenceSystem(), false);
            if (epsgCode != null) {
                code = "EPSG:" + epsgCode;
            } else {
                // otherwise let's make a fuller scan, but this method is more
                // fragile...
                code = CRS.lookupIdentifier(bbox.getCoordinateReferenceSystem(), false);
            }

            if (code != null && validSRS.contains(code)) {
                requestSrs = code;
            } else {
                // first reproject to the map CRS
                gridEnvelope = bbox.transform(getCoordinateReferenceSystem(), true);

                // then adjust the form factor
                if (gridEnvelope.getWidth() < gridEnvelope.getHeight()) {
                    height =
                            (int)
                                    Math.round(
                                            width
                                                    * gridEnvelope.getHeight()
                                                    / gridEnvelope.getWidth());
                } else {
                    width =
                            (int)
                                    Math.round(
                                            height
                                                    * gridEnvelope.getWidth()
                                                    / gridEnvelope.getHeight());
                }
            }
        } catch (Exception e) {
            throw new IOException("Could not reproject the request envelope", e);
        }

        return new TileRequest(width, height, gridEnvelope, requestSrs, time);
    }

    @Override
    public Format getFormat() {
        // this reader has not backing format
        return null;
    }

    /** Updates the coverage bounds based on layer and crs */
    public void updateBounds() {
        if (crs == null) {

            this.bounds = null;
            this.originalEnvelope = null;
            return;
        }
        if (LOGGER.isLoggable(Level.FINER)) LOGGER.entering("WMTSCoverage", "updatingBounds");
        GeneralEnvelope envelope = layer.getEnvelope(crs);
        ReferencedEnvelope result = reference(envelope);
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("setting bounds to " + result);

        this.bounds = result;
        this.originalEnvelope = new GeneralEnvelope(result);
    }

    /** Converts a {@link Envelope} into a {@link ReferencedEnvelope} */
    ReferencedEnvelope reference(Envelope envelope) {
        ReferencedEnvelope env = new ReferencedEnvelope(envelope.getCoordinateReferenceSystem());
        env.expandToInclude(envelope.getMinimum(0), envelope.getMinimum(1));
        env.expandToInclude(envelope.getMaximum(0), envelope.getMaximum(1));
        return env;
    }

    /** Converts a {@link GeneralEnvelope} into a {@link ReferencedEnvelope} */
    ReferencedEnvelope reference(GeneralEnvelope ge) {
        return new ReferencedEnvelope(
                ge.getMinimum(0),
                ge.getMaximum(0),
                ge.getMinimum(1),
                ge.getMaximum(1),
                ge.getCoordinateReferenceSystem());
    }

    @Override
    public String[] getMetadataNames() {
        return new String[] {REPROJECTING_READER};
    }

    @Override
    public String getMetadataValue(String name) {
        if (REPROJECTING_READER.equals(name)) {
            return "true";
        }
        return super.getMetadataValue(name);
    }

    /** @return the mapRequest */
    @Deprecated
    public GetTileRequest getTileRequest() {
        return this.lastTileRequest;
    }

    /** @param mapRequest the mapRequest to set */
    @Deprecated
    public void setTileRequest(GetTileRequest mapRequest) {
        this.lastTileRequest = mapRequest;
    }

    /** Get the Request time */
    public String getRequestedTime() {
        return this.requestedTime;
    }

    /** Set the request time */
    public void setRequestedTime(String requestedTime) {
        this.requestedTime = requestedTime;
    }

    /**
     * Does the WMTS layer serve this coordinate reference system natively. Going through all the
     * reported SRS's to check if they corresponds with the crs. Extra consideration must be taken
     * when crs have xy axis.
     */
    boolean isNativelySupported(CoordinateReferenceSystem crs) {
        final boolean isXY =
                crs.getCoordinateSystem().getAxis(0).getDirection() == AxisDirection.EAST;
        for (String srs : validSRS) {
            try {
                CoordinateReferenceSystem validCrs = CRS.decode(srs, isXY);
                if (!CRS.isTransformationRequired(validCrs, crs)) {
                    return true;
                }
            } catch (FactoryException e) {
                LOGGER.log(Level.WARNING, "Error with following srs:" + srs, e);
            }
        }
        return false;
    }

    /**
     * Switch the CRS definition and checks if the switched definition is contained in natively
     * supported crs. If true the switched CRS is returned, otherwise null. By switching its meant
     * the passage from an EPSG to an urn:ogc:def:crs:EPSG:: srs definition or the reverse. In case
     * the crs to switch is a Pseudo-Mercator based crs, the code will try to switch to all the
     * possible definition. Eg. assuming that the passed CRS is EPSG:3857:
     * urn:ogc:def:crs:EPSG::3857, EPSG:900913, urn:ogc:def:crs:EPSG::900913 will be tried to se if
     * they are among the natively supported srs.
     *
     * @param crs the CRS to switch.
     * @return the switched srs if any conversion result was found in the valid srs list. Null
     *     otherwise.
     */
    private String switchDefinition(CoordinateReferenceSystem crs) {
        String srs = CRS.toSRS(crs);
        String result;
        if (!srs.startsWith("urn:ogc:def:crs:EPSG::"))
            result = srs.replace("EPSG:", "urn:ogc:def:crs:EPSG::");
        else result = srs.replace("urn:ogc:def:crs:EPSG::", "EPSG");
        if (!validSRS.contains(result)) result = switchPseudoMercatorDefinition(srs);
        if (result != null && validSRS.contains(result)) return result;
        else return null;
    }

    private String switchPseudoMercatorDefinition(String srs) {
        String switched = null;
        if (srs.equals("EPSG:3857") || srs.equals("urn:ogc:def:crs:EPSG::3857")) {
            if (validSRS.contains("EPSG:900913")) {
                switched = "EPSG:900913";
            } else if (validSRS.contains("urn:ogc:def:crs:EPSG::900913")) {
                switched = "urn:ogc:def:crs:EPSG::900913";
            }
        }

        if (srs.equals("EPSG:90013") || srs.equals("urn:ogc:def:crs:EPSG::900913")) {
            if (validSRS.contains("EPSG:3857")) {
                switched = "EPSG:3857";
            } else if (validSRS.contains("urn:ogc:def:crs:EPSG::3857")) {
                switched = "urn:ogc:def:crs:EPSG::3857";
            }
        }
        return switched;
    }

    private GridCoverage2D reproject(
            GridCoverage2D coverage2D,
            GeneralEnvelope destEnvelope,
            WMTSReadParameters readParameters) {
        try {
            Hints newHints = hints.clone();
            Interpolation interpolation = readParameters.getInterpolation();
            newHints.add(new RenderingHints(JAI.KEY_INTERPOLATION, interpolation));
            GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(hints);
            return GridCoverageRendererUtilities.reproject(
                    coverage2D,
                    destEnvelope.getCoordinateReferenceSystem(),
                    interpolation,
                    destEnvelope,
                    readParameters.getBKGArray(),
                    factory,
                    newHints);
        } catch (FactoryException factoryException) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(
                        Level.SEVERE,
                        errorMessage(destEnvelope.getCoordinateReferenceSystem()),
                        factoryException);
            throw new RuntimeException(factoryException);
        }
    }

    private String errorMessage(CoordinateReferenceSystem targetCrs) {
        StringBuilder msg =
                new StringBuilder("Something wrong happended while trying to reproject ");
        if (coverageName != null) msg.append(coverageName).append(" ");
        msg.append("WMTS coverage to ");
        if (crs != null) msg.append(CRS.toSRS(targetCrs));
        else msg.append(" null srs.");
        return msg.toString();
    }

    // execute the get tiles request taking care of performing the reprojection if needed.
    private GridCoverage2D getMapReproject(
            ReferencedEnvelope requestedEnvelope, String time, WMTSReadParameters readParameters)
            throws IOException {
        try {

            CoordinateReferenceSystem targetCRS = requestedEnvelope.getCoordinateReferenceSystem();
            CoordinateReferenceSystem sourceCRS = getBestSourceCRS(targetCRS, readParameters);

            ReferencedEnvelope nativeEnvelope = requestedEnvelope.transform(sourceCRS, false);
            TileRequest request =
                    initTileRequest(
                            nativeEnvelope,
                            readParameters.getWidth(),
                            readParameters.getHeight(),
                            time);

            Set<Tile> tiles = findTiles(request);
            GridCoverage2D result = createTileMap(nativeEnvelope, tiles);

            // in case the reprojection is concerning two crs differing only by axis order
            // it should not be needed because already happened to make sure
            // map to raster space conversion doesn't fails due north east axis order.
            if (!CRS.equalsIgnoreMetadata(result.getCoordinateReferenceSystem(), targetCRS))
                result = reproject(result, new GeneralEnvelope(requestedEnvelope), readParameters);
            return result;
        } catch (FactoryException | TransformException e) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(
                        Level.SEVERE,
                        "Error while reprojecting the requested envelope to the selected native crs.",
                        e);
            throw new IOException(e);
        }
    }

    // get the CRS to be used to reproject to a CRS that the server doesn't support.
    // call this method if the targetCRS is not among the natively supported ones.
    CoordinateReferenceSystem getBestSourceCRS(
            CoordinateReferenceSystem targetCRS, WMTSReadParameters readParameters)
            throws IOException {

        String switched = switchDefinition(targetCRS);
        if (switched != null) {
            // check first by changing the srs definition
            LOGGER.fine(() -> "Will request tiles with server supported srs " + switched);
            try {
                return CRS.decode(switched);
            } catch (FactoryException e) {
                if (LOGGER.isLoggable(Level.SEVERE))
                    LOGGER.log(
                            Level.SEVERE,
                            "Error while retrieving the source CRS to perform reprojection. ",
                            e);
                throw new IOException(e);
            }
        } else if (readParameters.getSourceCRS() != null) {
            // otherwise get the preferred one
            return readParameters.getSourceCRS();
        } else {
            if (crs == null) {
                throw new IllegalStateException(
                        "Layer " + this.layer + " isn't set up with a default CRS.");
            }
            return crs;
        }
    }

    /** Keep on to values for single request */
    class TileRequest {
        int width;
        int height;

        ReferencedEnvelope envelope;

        String time;

        String tileSRS;

        TileRequest(
                int width, int height, ReferencedEnvelope envelope, String tileSRS, String time) {
            this.width = width;
            this.height = height;
            this.envelope = envelope;
            this.tileSRS = tileSRS;
            this.time = time;
        }

        @Deprecated
        GetTileRequest createTileRequest() {
            try {
                GetTileRequest tileRequest = wmts.createGetTileRequest();
                tileRequest.setCRS(CRS.decode(tileSRS));
                tileRequest.setLayer(layer);
                tileRequest.setRequestedHeight(height);
                tileRequest.setRequestedWidth(width);
                tileRequest.setRequestedBBox(envelope);
                tileRequest.setRequestedTime(time);
                tileRequest.setFormat(format);

                WMTSCoverageReader.this.lastTileRequest = tileRequest;

                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Issuing request: " + tileRequest.getFinalURL());
                }

                return tileRequest;
            } catch (FactoryException e) {
                throw new RuntimeException("Something misfit by application.", e);
            }
        }
    }
}
