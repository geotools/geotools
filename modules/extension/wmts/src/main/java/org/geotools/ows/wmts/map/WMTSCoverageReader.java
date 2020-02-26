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
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.xml.Dimension;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.referencing.CRS;
import org.geotools.tile.Tile;
import org.opengis.coverage.grid.Format;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

/**
 * A grid coverage readers backing onto a WMTS server by issuing GetTile requests
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public class WMTSCoverageReader extends AbstractGridCoverage2DReader {

    /** The logger for the map module. */
    public static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(WMTSCoverageReader.class);

    static GridCoverageFactory gcf = new GridCoverageFactory();

    /** The WMTS server */
    WebMapTileServer wmts;

    /** The layer */
    WMTSLayer layer = null;

    /** The chosen SRS name */
    String srsName;

    /** The format to use for requests */
    String format;

    /** The last GetTile request */
    private GetTileRequest tileRequest;

    /** The last GetMap response */
    GridCoverage2D grid;

    /** The set of SRS available for this layer */
    Set<String> validSRS;

    /** The cached layer bounds */
    ReferencedEnvelope bounds;

    /** The last request envelope */
    // ReferencedEnvelope requestedEnvelope;

    /** Last request width */
    int width;

    /** Last request height */
    int height;

    /** Last request CRS (used for reprojected GetFeatureInfo) */
    CoordinateReferenceSystem requestCRS;

    String requestedTime;

    public final boolean debug = System.getProperty("wmts.debug") != null;

    /** Builds a new WMS coverage reader */
    public WMTSCoverageReader(WebMapTileServer server, Layer layer) {
        this.wmts = server;

        // init the reader
        setLayer(layer);

        // best guess at the format with a preference for PNG (since it's
        // normally transparent)
        List<String> formats =
                ((WMTSLayer) layer)
                        .getFormats(); // wms2.getCapabilities().getRequest().getGetTile().getFormats();
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

    final void setLayer(Layer owsLayer) {
        this.layer = (WMTSLayer) owsLayer;

        if (srsName == null) { // initialize from first (unique) layer

            // prefer 4326
            for (String preferred :
                    new String[] {"EPSG:4326", "WGS84", "CRS:84", "WGS 84", "WGS84(DD)"}) {
                if (owsLayer.getSrs().contains(preferred)) {
                    srsName = preferred;
                    if (LOGGER.isLoggable(Level.INFO)) LOGGER.info("defaulting CRS to: " + srsName);
                }
            }

            // no 4326, let's see if the layer is offering something valid
            if (srsName == null) {
                for (String srs : owsLayer.getSrs()) {
                    try {
                        // check it's valid, if not we crap out and move to the
                        // next
                        CRS.decode(srs);
                        srsName = srs;

                        if (LOGGER.isLoggable(Level.INFO)) LOGGER.info("setting CRS: " + srsName);
                        break;
                    } catch (Exception e) {
                        // it's fine, we could not decode that code
                    }
                }
            }

            if (srsName == null) {
                if (owsLayer.getSrs().isEmpty()) {
                    // force 4326
                    if (LOGGER.isLoggable(Level.INFO))
                        LOGGER.info("adding default CRS to: " + srsName);
                    srsName = "EPSG:4326";
                    owsLayer.getSrs().add(srsName);
                } else {
                    // if not even that works we just take the first...
                    srsName = owsLayer.getSrs().iterator().next();
                    if (LOGGER.isLoggable(Level.INFO)) LOGGER.info("guessing CRS to: " + srsName);
                }
            }

            validSRS = owsLayer.getSrs();

        } else {
            LOGGER.severe("TODO: check if this code path is ever run");

            Set<String> intersection = new HashSet<>(validSRS);
            intersection.retainAll(owsLayer.getSrs());

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
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.log(Level.INFO, "Bounds unavailable for layer" + owsLayer);
        }
        this.crs = crs;
        this.requestCRS = crs;
        // update the cached bounds and the reader original envelope
        updateBounds();
    }

    @Override
    public GridCoverage2D read(GeneralParameterValue[] parameters)
            throws IllegalArgumentException, IOException {
        // try to get request params from the request
        Envelope requestedEnvelope = null;
        int width = -1;
        int height = -1;

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

        if (parameters != null) {
            for (GeneralParameterValue param : parameters) {
                final ReferenceIdentifier name = param.getDescriptor().getName();
                if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
                    final GridGeometry2D gg = (GridGeometry2D) ((ParameterValue) param).getValue();
                    requestedEnvelope = gg.getEnvelope();
                    // the range high value is the highest pixel included in the
                    // raster, the actual width and height is one more than that
                    width = gg.getGridRange().getHigh(0) + 1;
                    height = gg.getGridRange().getHigh(1) + 1;
                }
            }
        }

        // fill in a reasonable default if we did not manage to get the params
        if (requestedEnvelope == null) {
            requestedEnvelope = getOriginalEnvelope();
            width = 640;
            height =
                    (int)
                            Math.round(
                                    requestedEnvelope.getSpan(1)
                                            / requestedEnvelope.getSpan(0)
                                            * 640);
        }

        // if the structure did not change reuse the same response
        if (grid != null
                && grid.getGridGeometry().getGridRange2D().getWidth() == width
                && grid.getGridGeometry().getGridRange2D().getHeight() == height
                && grid.getEnvelope().equals(requestedEnvelope)) return grid;

        grid = getMap(reference(requestedEnvelope), width, height, time);
        return grid;
    }

    /** Execute the GetMap request */
    GridCoverage2D getMap(ReferencedEnvelope requestedEnvelope, int width, int height, String time)
            throws IOException {

        // build the request
        ReferencedEnvelope gridEnvelope = initTileRequest(requestedEnvelope, width, height, time);

        // issue the request and wrap response in a grid coverage
        try {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                        Level.FINE,
                        "Issuing request: " + getTileRequest().getFinalURL(),
                        new RuntimeException("TRACE!"));
            }

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            getTileRequest().setCRS(gridEnvelope.getCoordinateReferenceSystem());
            Set<Tile> responses = wmts.issueRequest(getTileRequest());
            double xscale = width / requestedEnvelope.getWidth();
            double yscale = height / requestedEnvelope.getHeight();

            double scale = Math.min(xscale, yscale);

            double xoff = requestedEnvelope.getMedian(0) * scale - width / 2;
            double yoff = requestedEnvelope.getMedian(1) * scale + height / 2;
            // C ould we use RenderUtilities here?
            AffineTransform worldToScreen = new AffineTransform(scale, 0, 0, -scale, -xoff, yoff);
            renderTiles(responses, image.createGraphics(), requestedEnvelope, worldToScreen);

            return gcf.create(layer.getTitle(), image, gridEnvelope);
        } catch (ServiceException e) {
            throw new IOException("GetMap failed", e);
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
        g2d.drawImage(
                img,
                (int) points[0],
                (int) points[1],
                (int) Math.ceil(points[2] - points[0]),
                (int) Math.ceil(points[3] - points[1]),
                null);
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
    ReferencedEnvelope initTileRequest(ReferencedEnvelope bbox, int width, int height, String time)
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

        GetTileRequest tileRequest = wmts.createGetTileRequest();
        setTileRequest(tileRequest);
        tileRequest.setCRS(gridEnvelope.getCoordinateReferenceSystem());
        tileRequest.setLayer(layer);
        tileRequest.setRequestedHeight(height);
        tileRequest.setRequestedWidth(width);
        tileRequest.setRequestedBBox(gridEnvelope); // should be
        // requestEnvelope?
        tileRequest.setRequestedTime(time);

        try {
            this.requestCRS = CRS.decode(requestSrs);
        } catch (Exception e) {
            throw new IOException("Could not decode request SRS " + requestSrs);
        }

        this.width = width;
        this.height = height;

        return gridEnvelope;
    }

    public Format getFormat() {
        // this reader has not backing format
        return null;
    }

    /** Returns the layer bounds */
    public void updateBounds() {
        if (LOGGER.isLoggable(Level.FINER)) LOGGER.entering("WMTSCoverage", "updatingBounds");
        GeneralEnvelope envelope = layer.getEnvelope(requestCRS);
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
    public GetTileRequest getTileRequest() {
        return tileRequest;
    }

    /** @param mapRequest the mapRequest to set */
    public void setTileRequest(GetTileRequest mapRequest) {
        this.tileRequest = mapRequest;
    }

    public String getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(String requestedTime) {
        this.requestedTime = requestedTime;
    }
}
