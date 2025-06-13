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
package org.geotools.ows.wms.map;

import static java.util.function.Predicate.not;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.Position2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.io.ImageIOExt;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.ows.wms.request.GetFeatureInfoRequest;
import org.geotools.ows.wms.request.GetMapRequest;
import org.geotools.ows.wms.response.GetFeatureInfoResponse;
import org.geotools.ows.wms.response.GetMapResponse;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.RendererUtilities;

/** A grid coverage readers backing onto a WMS server by issuing GetMap */
// JNH
public class WMSCoverageReader extends AbstractGridCoverage2DReader {

    /** The logger for the map module. */
    public static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(WMSCoverageReader.class);

    static GridCoverageFactory gcf = new GridCoverageFactory();

    /** The WMS server */
    WebMapServer wms;

    /** The layers and styles */
    List<LayerStyle> layers = new ArrayList<>();

    /** The chosen SRS name */
    String srsName;

    /** The format to use for requests */
    String format;

    /** The last GetMap request */
    GetMapRequest mapRequest;

    /** The last GetMap response */
    GridCoverage2D grid;

    /** The set of SRS common to all layers */
    Set<String> validSRS;

    /** The cached layer bounds */
    ReferencedEnvelope bounds;

    /** The last request envelope */
    ReferencedEnvelope requestedEnvelope;

    /** Last request width */
    int width;

    /** Last request height */
    int height;

    /** Last request CRS (used for reprojected GetFeatureInfo) */
    CoordinateReferenceSystem requestCRS;

    /** Builds a new WMS coverage reader */
    public WMSCoverageReader(WebMapServer wms, Layer layer) {
        this(wms, layer, "");
    }

    public WMSCoverageReader(WebMapServer wms, Layer layer, String style) {
        this.wms = wms;

        // init the reader
        addLayer(layer, style);

        // best guess at the format with a preference for PNG (since it's normally transparent)
        List<String> formats = wms.getCapabilities().getRequest().getGetMap().getFormats();
        this.format = getDefaultFormat(formats);
    }

    public WMSCoverageReader(WebMapServer wms, Layer layer, String style, String preferredFormat) {
        this.wms = wms;
        // init the reader
        addLayer(layer, style);
        List<String> formats = wms.getCapabilities().getRequest().getGetMap().getFormats();
        this.format = preferredFormat;
        // verify if preferred Format is supported else fallback to default functionality
        if (!formats.contains(preferredFormat)) this.format = getDefaultFormat(formats);
    }

    public String getDefaultFormat(List<String> formats) {

        for (String format : formats) {
            if ("image/png".equals(format)
                    || "image/png24".equals(format)
                    || "png".equals(format)
                    || "png24".equals(format)
                    || "image/png; mode=24bit".equals(format)) {
                return format;
            }
        }
        // if preferred format is not supported default to first available on remote
        // if cap doc did not pass any formats, assume PNG
        return (!formats.isEmpty()) ? formats.get(0) : "image/png";
    }

    void addLayer(Layer layer) {
        addLayer(layer, "");
    }

    void addLayer(Layer layer, String style) {
        this.layers.add(new LayerStyle(layer, style));

        if (srsName == null) {
            // check first srs available on srs set and
            String priorityCRS = getCRSWithBoundingBoxes(layer);
            if (priorityCRS != null) {
                srsName = priorityCRS;
            }

            if (srsName == null) {
                // initialize from first layer
                for (String srs : layer.getBoundingBoxes().keySet()) {
                    try {
                        // check it's valid, if not we crap out and move to the next
                        CRS.decode(srs);
                        srsName = srs;
                        break;
                    } catch (Exception e) {
                        // it's fine, we could not decode that code
                    }
                }
            }

            if (srsName == null) {
                if (layer.getSrs().contains("EPSG:4326")) {
                    // otherwise we try 4326
                    srsName = "EPSG:4326";
                } else {
                    // if not even that works we just take the first...
                    srsName = layer.getSrs().iterator().next();
                }
            }
            validSRS = new LinkedHashSet<>(layer.getSrs());
        } else {
            Set<String> intersection = new LinkedHashSet<>(validSRS);
            intersection.retainAll(layer.getSrs());

            // can we reuse what we have?
            if (!intersection.contains(srsName)) {
                if (intersection.isEmpty()) {
                    throw new IllegalArgumentException("The layer being appended does "
                            + "not have any SRS in common with the ones already "
                            + "included in the WMS request, cannot be merged");
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
            LOGGER.log(Level.FINE, "Bounds unavailable for layer" + layer);
        }
        this.crs = crs;

        // update the cached bounds and the reader original envelope
        updateBounds();
    }

    /** Returns the first CRS from layer srs list that matches with bounding boxes map keys. */
    private String getCRSWithBoundingBoxes(Layer layer) {
        // return the first valid CRS with bounding box
        for (String srs : getCRSWithBoundingBoxesList(layer)) {
            try {
                // check it's valid, if not we crap out and move to the next
                CRS.decode(srs);
                return srs;
            } catch (Exception e) {
                LOGGER.log(Level.FINE, "Error decoding CRS code.", e);
            }
        }
        return null;
    }

    private List<String> getCRSWithBoundingBoxesList(Layer layer) {
        Set<String> layerSrsSet = new LinkedHashSet<>(layer.getSrs());
        Map<String, CRSEnvelope> boundingBoxes = layer.getBoundingBoxes();
        return layerSrsSet.stream().filter(boundingBoxes::containsKey).collect(Collectors.toList());
    }

    /** Issues GetFeatureInfo against a point using the params of the last GetMap request */
    public InputStream getFeatureInfo(Position2D pos, String infoFormat, int featureCount, GetMapRequest getmap)
            throws IOException {
        GetFeatureInfoRequest request = wms.createGetFeatureInfoRequest(getmap);
        request.setFeatureCount(1);
        LinkedHashSet<Layer> queryLayers = new LinkedHashSet<>();
        for (LayerStyle ls : layers) {
            queryLayers.add(ls.getLayer());
        }
        request.setQueryLayers(queryLayers);
        request.setInfoFormat(infoFormat);
        request.setFeatureCount(featureCount);
        try {
            AffineTransform tx =
                    RendererUtilities.worldToScreenTransform(requestedEnvelope, new Rectangle(width, height));
            Point2D dest = new Point2D.Double();
            Point2D src = new Point2D.Double(pos.x, pos.y);
            tx.transform(src, dest);
            request.setQueryPoint((int) dest.getX(), (int) dest.getY());
        } catch (Exception e) {
            throw new IOException("Failed to grab feature info", e);
        }

        try {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Issuing request: " + request.getFinalURL());
            }
            GetFeatureInfoResponse response = wms.issueRequest(request);
            return response.getInputStream();
        } catch (IOException e) {
            throw e;
        } catch (Throwable t) {
            throw new IOException("Failed to grab feature info", t);
        }
    }

    @Override
    public GridCoverage2D read(GeneralParameterValue... parameters) throws IllegalArgumentException, IOException {
        // try to get request params from the request
        Bounds requestedEnvelope = null;
        int width = -1;
        int height = -1;
        Color backgroundColor = null;
        if (parameters != null) {
            for (GeneralParameterValue param : parameters) {
                final ReferenceIdentifier name = param.getDescriptor().getName();
                if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
                    final GridGeometry2D gg = (GridGeometry2D) ((ParameterValue) param).getValue();
                    requestedEnvelope = gg.getEnvelope();
                    // the range high value is the highest pixel included in the raster,
                    // the actual width and height is one more than that
                    width = gg.getGridRange().getHigh(0) + 1;
                    height = gg.getGridRange().getHigh(1) + 1;
                } else if (name.equals(AbstractGridFormat.BACKGROUND_COLOR.getName())) {
                    backgroundColor = (Color) ((ParameterValue) param).getValue();
                }
            }
        }

        // fill in a reasonable default if we did not manage to get the params
        if (requestedEnvelope == null) {
            requestedEnvelope = getOriginalEnvelope();
            width = 640;
            height = (int) Math.round(requestedEnvelope.getSpan(1) / requestedEnvelope.getSpan(0) * 640);
        }

        // if the structure did not change reuse the same response
        if (grid != null
                && grid.getGridGeometry().getGridRange2D().getWidth() == width
                && grid.getGridGeometry().getGridRange2D().getHeight() == height
                && grid.getEnvelope().equals(requestedEnvelope)) return grid;

        grid = getMap(reference(requestedEnvelope), width, height, backgroundColor);
        return grid;
    }

    /** Execute the GetMap request */
    GridCoverage2D getMap(ReferencedEnvelope requestedEnvelope, int width, int height, Color backgroundColor)
            throws IOException {
        // build the request
        ReferencedEnvelope gridEnvelope = initMapRequest(requestedEnvelope, width, height, backgroundColor);

        // issue the request and wrap response in a grid coverage
        try {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Issuing request: " + mapRequest.getFinalURL());
            }
            GetMapResponse response = wms.issueRequest(mapRequest);
            try (InputStream is = response.getInputStream()) {
                RenderedImage image = ImageIOExt.read(is);
                if (image == null) {
                    throw new IOException("GetMap failed: " + mapRequest.getFinalURL());
                }
                return gcf.create(layers.get(0).getLayer().getTitle(), image, gridEnvelope);
            } finally {
                response.dispose();
            }
        } catch (ServiceException e) {
            throw new IOException("GetMap failed", e);
        }
    }

    /**
     * Sets up a max request with the provided parameters, making sure it is compatible with the layers own native SRS
     * list
     */
    ReferencedEnvelope initMapRequest(ReferencedEnvelope bbox, int width, int height, Color backgroundColor)
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
                // otherwise let's make a fuller scan, but this method is more fragile...
                code = CRS.lookupIdentifier(bbox.getCoordinateReferenceSystem(), false);
            }

            if (code != null && validSRS.contains(code)) {
                requestSrs = code;
            } else {
                // first reproject to the map CRS
                gridEnvelope = bbox.transform(getCoordinateReferenceSystem(), true);

                // then adjust the form factor
                if (gridEnvelope.getWidth() < gridEnvelope.getHeight()) {
                    height = (int) Math.round(width * gridEnvelope.getHeight() / gridEnvelope.getWidth());
                } else {
                    width = (int) Math.round(height * gridEnvelope.getWidth() / gridEnvelope.getHeight());
                }
            }
        } catch (Exception e) {
            throw new IOException("Could not reproject the request envelope", e);
        }

        GetMapRequest mapRequest = wms.createGetMapRequest();
        // for some silly reason GetMapRequest will list the layers in the opposite order...
        List<LayerStyle> reversed = new ArrayList<>(layers);
        Collections.reverse(reversed);
        for (LayerStyle layer : reversed) {
            mapRequest.addLayer(layer.getLayer(), layer.getStyle());
        }
        mapRequest.setDimensions(width, height);
        mapRequest.setFormat(format);
        if (backgroundColor == null) {
            mapRequest.setTransparent(true);
        } else {
            String rgba = Integer.toHexString(backgroundColor.getRGB());
            String rgb = rgba.substring(2, rgba.length());
            mapRequest.setBGColour("0x" + rgb.toUpperCase());
            mapRequest.setTransparent(backgroundColor.getAlpha() < 255);
        }

        try {
            this.requestCRS = CRS.decode(requestSrs);
        } catch (Exception e) {
            throw new IOException("Could not decode request SRS " + requestSrs);
        }

        ReferencedEnvelope requestEnvelope = gridEnvelope;
        mapRequest.setBBox(requestEnvelope);
        mapRequest.setSRS(requestSrs);

        long distinctVendorParameterSets = getLayers().stream()
                .map(Layer::getVendorParameters)
                .map(m -> Objects.isNull(m) ? Collections.emptyMap() : m)
                .distinct()
                .count();

        if (distinctVendorParameterSets > 1) {
            LOGGER.warning("More than one distinct vendor parameter sets found. Using the first one.");
        }

        // If there are multiple layers find the first with vendor parameters to use
        // there should only be one
        getLayers().stream()
                .map(Layer::getVendorParameters)
                .filter(Objects::nonNull)
                .filter(not(Map::isEmpty))
                .findFirst()
                .ifPresent(parameters -> parameters.forEach(mapRequest::setVendorSpecificParameter));

        this.mapRequest = mapRequest;
        this.requestedEnvelope = gridEnvelope;
        this.width = width;
        this.height = height;

        return gridEnvelope;
    }

    @Override
    public Format getFormat() {
        // this reader has not backing format
        return null;
    }

    /** Returns the layer bounds */
    public void updateBounds() {
        ReferencedEnvelope result = reference(layers.get(0).getLayer().getEnvelope(crs));
        for (int i = 1; i < layers.size(); i++) {
            ReferencedEnvelope layerEnvelope =
                    reference(layers.get(i).getLayer().getEnvelope(crs));
            result.expandToInclude(layerEnvelope);
        }

        this.bounds = result;
        this.originalEnvelope = new GeneralBounds(result);
    }

    /** Converts a {@link Bounds} into a {@link ReferencedEnvelope} */
    ReferencedEnvelope reference(Bounds envelope) {
        ReferencedEnvelope env = new ReferencedEnvelope(envelope.getCoordinateReferenceSystem());
        env.expandToInclude(envelope.getMinimum(0), envelope.getMinimum(1));
        env.expandToInclude(envelope.getMaximum(0), envelope.getMaximum(1));
        return env;
    }

    /** Converts a {@link GeneralBounds} into a {@link ReferencedEnvelope} */
    ReferencedEnvelope reference(GeneralBounds ge) {
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

    /**
     * fetch the WMS Layers used in this coverage.
     *
     * @return the layers
     */
    public List<Layer> getLayers() {
        List<Layer> ret = new ArrayList<>();
        for (LayerStyle l : layers) {
            ret.add(l.getLayer());
        }
        return ret;
    }

    /**
     * fetch the names of the styles used in this layer. Empty string means the default style.
     *
     * @return the style names.
     */
    public List<String> getStyles() {
        List<String> ret = new ArrayList<>();
        for (LayerStyle l : layers) {
            ret.add(l.getStyle());
        }
        return ret;
    }

    /**
     * Utility class to hold a layer and its style name.
     *
     * @author ian
     */
    private class LayerStyle {
        private Layer layer;

        private String style = "";

        /** */
        public LayerStyle(Layer layer, String style) {
            this.layer = layer;
            this.style = style;
        }

        /** @return the layer */
        public Layer getLayer() {
            return layer;
        }

        /** @return the style */
        public String getStyle() {
            return style;
        }
    }
}
