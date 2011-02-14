package org.geotools.map;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetFeatureInfoRequest;
import org.geotools.data.wms.request.GetMapRequest;
import org.geotools.data.wms.response.GetFeatureInfoResponse;
import org.geotools.data.wms.response.GetMapResponse;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.RendererUtilities;
import org.opengis.coverage.grid.Format;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A grid coverage readers backing onto a WMS server by issuing GetMap
 */
class WMSCoverageReader extends AbstractGridCoverage2DReader {
    
    /** The logger for the map module. */
    static public final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.map");
    
    static GridCoverageFactory gcf = new GridCoverageFactory();

    /**
     * The WMS server
     */
    WebMapServer wms;

    /**
     * The layer
     */
    List<Layer> layers = new ArrayList<Layer>();

    /**
     * The chosen SRS name
     */
    String srsName;

    /**
     * The format to use for requests
     */
    String format;

    /**
     * The last GetMap request
     */
    GetMapRequest mapRequest;

    /**
     * The last GetMap response
     */
    GridCoverage2D grid;

    /**
     * The set of SRS common to all layers
     */
    Set<String> validSRS;

    /**
     * The cached layer bounds
     */
    ReferencedEnvelope bounds;

    /**
     * The last request envelope
     */
    ReferencedEnvelope requestedEnvelope;

    /**
     * Last request width
     */
    int width;

    /**
     * Last request height
     */
    int height;

    /**
     * Last request CRS (used for reprojected GetFeatureInfo)
     */
    CoordinateReferenceSystem requestCRS;

    /**
     * Builds a new WMS coverage reader
     * 
     * @param wms
     * @param layer
     */
    public WMSCoverageReader(WebMapServer wms, Layer layer) {
        this.wms = wms;
        addLayer(layer);

        // best guess at the format with a preference for PNG (since it's normally transparent)
        List<String> formats = wms.getCapabilities().getRequest().getGetMap().getFormats();
        this.format = formats.iterator().next();
        for (String format : formats) {
            if ("image/png".equals(format) || "image/png24".equals(format)
                    || "png".equals(format) || "png24".equals(format)
                    || "image/png; mode=24bit".equals(format)) {
                this.format = format;
                break;
            }
        }
    }

    void addLayer(Layer layer) {
        this.layers.add(layer);

        if (srsName == null) {
            // initialize from first layer
            if (layer.getBoundingBoxes().size() > 0) {
                // we assume the layer is declared in its native bounding box
                CRSEnvelope envelope = layer.getBoundingBoxes().get(
                        layer.getBoundingBoxes().keySet().iterator().next());
                srsName = envelope.getEPSGCode();
            } else if (layer.getSrs().contains("EPSG:4326")) {
                // otherwise we try 4326
                srsName = "EPSG:4326";
            } else {
                // if not even that works we just take the first...
                srsName = (String) layer.getSrs().iterator().next();
            }
            validSRS = layer.getSrs();
        } else {
            Set<String> intersection = new HashSet<String>(validSRS);
            intersection.retainAll(layer.getSrs());

            // can we reuse what we have?
            if (!intersection.contains(srsName)) {
                if (intersection.size() == 0) {
                    throw new IllegalArgumentException("The layer being appended does "
                            + "not have any SRS in common with the ones already "
                            + "included in the WMS request, cannot be merged");
                } else if (intersection.contains("EPSG:4326")) {
                    srsName = "EPSG:4326";
                } else {
                    // if not even that works we just take the first...
                    srsName = (String) intersection.iterator().next();
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

    /**
     * Issues GetFeatureInfo against a point using the params of the last GetMap request
     * 
     * @param pos
     * @return
     * @throws IOException
     */
    public InputStream getFeatureInfo(DirectPosition2D pos, String infoFormat, int featureCount, GetMapRequest getmap)
            throws IOException {
        GetFeatureInfoRequest request = wms.createGetFeatureInfoRequest(getmap);
        request.setFeatureCount(1);
        request.setQueryLayers(new LinkedHashSet<Layer>(layers));
        request.setInfoFormat(infoFormat);
        request.setFeatureCount(featureCount);
        try {
            AffineTransform tx = RendererUtilities.worldToScreenTransform(requestedEnvelope,
                    new Rectangle(width, height));
            Point2D dest = new Point2D.Double();
            Point2D src = new Point2D.Double(pos.x, pos.y);
            tx.transform(src, dest);
            request.setQueryPoint((int) dest.getX(), (int) dest.getY());
        } catch (Exception e) {
            throw (IOException) new IOException("Failed to grab feature info").initCause(e);
        }

        try {
            GetFeatureInfoResponse response = wms.issueRequest(request);
            return response.getInputStream();
        } catch (IOException e) {
            throw e;
        } catch (Throwable t) {
            throw (IOException) new IOException("Failed to grab feature info").initCause(t);
        }
    }

    @Override
    public GridCoverage2D read(GeneralParameterValue[] parameters)
            throws IllegalArgumentException, IOException {
        // try to get request params from the request
        Envelope requestedEnvelope = null;
        int width = -1;
        int height = -1;
        Color backgroundColor = null;
        if (parameters != null) {
            for (GeneralParameterValue param : parameters) {
                final ReferenceIdentifier name = param.getDescriptor().getName();
                if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
                    final GridGeometry2D gg = (GridGeometry2D) ((ParameterValue) param)
                            .getValue();
                    requestedEnvelope = gg.getEnvelope();
                    // the range high value is the highest pixel included in the raster,
                    // the actual width and height is one more than that
                    width = gg.getGridRange().getHigh(0) + 1;
                    height = gg.getGridRange().getHigh(1) + 1;
                } else if(name.equals(AbstractGridFormat.BACKGROUND_COLOR.getName())) {
                    backgroundColor = (Color)  ((ParameterValue) param).getValue();
                }
            }
        }

        // fill in a reasonable default if we did not manage to get the params
        if (requestedEnvelope == null) {
            requestedEnvelope = getOriginalEnvelope();
            width = 640;
            height = (int) Math.round(requestedEnvelope.getSpan(1)
                    / requestedEnvelope.getSpan(0) * 640);
        }

        // if the structure did not change reuse the same response
        if (grid != null && grid.getGridGeometry().getGridRange2D().getWidth() == width
                && grid.getGridGeometry().getGridRange2D().getHeight() == height
                && grid.getEnvelope().equals(requestedEnvelope))
            return grid;

        grid = getMap(reference(requestedEnvelope), width, height, backgroundColor);
        return grid;
    }

    /**
     * Execute the GetMap request
     */
    GridCoverage2D getMap(ReferencedEnvelope requestedEnvelope, int width, int height, Color backgroundColor)
            throws IOException {
        // build the request
        ReferencedEnvelope gridEnvelope = initMapRequest(requestedEnvelope, width, height, backgroundColor);

        // issue the request and wrap response in a grid coverage
        InputStream is = null;
        try {
            GetMapResponse response = wms.issueRequest(mapRequest);
            is = response.getInputStream();
            BufferedImage image = ImageIO.read(is);
            LOGGER.fine("GetMap completed");
            return gcf.create(layers.get(0).getTitle(), image, gridEnvelope);
        } catch (ServiceException e) {
            throw (IOException) new IOException("GetMap failed").initCause(e);
        }
    }

    /**
     * Sets up a max request with the provided parameters, making sure it is compatible with
     * the layers own native SRS list
     * @param bbox
     * @param width
     * @param height
     * @return
     * @throws IOException
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
            if(epsgCode != null) {
                code = "EPSG:" + epsgCode;
            } else {
                // otherwise let's make a fuller scan, but this method is more fragile...
                code = CRS.lookupIdentifier(bbox.getCoordinateReferenceSystem(), false);
            }
            
            if (code != null && validSRS.contains(code)) {
                requestSrs = code;
            } else {
                // first reproject to the map CRS
                gridEnvelope = bbox.transform(getCrs(), true);

                // then adjust the form factor
                if (gridEnvelope.getWidth() < gridEnvelope.getHeight()) {
                    height = (int) Math.round(width * gridEnvelope.getHeight()
                            / gridEnvelope.getWidth());
                } else {
                    width = (int) Math.round(height * gridEnvelope.getWidth()
                            / gridEnvelope.getHeight());
                }
            }
        } catch (Exception e) {
            throw (IOException) new IOException("Could not reproject the request envelope")
                    .initCause(e);
        }

        GetMapRequest mapRequest = wms.createGetMapRequest();
        // for some silly reason GetMapRequest will list the layers in the opposite order...
        List<Layer> reversed = new ArrayList<Layer>(layers);
        Collections.reverse(reversed);
        for (Layer layer : reversed) {
            mapRequest.addLayer(layer);
        }
        mapRequest.setDimensions(width, height);
        mapRequest.setFormat(URLEncoder.encode(format, "ASCII"));
        mapRequest.setSRS(requestSrs);
        mapRequest.setBBox(gridEnvelope);
        if(backgroundColor == null) {
            mapRequest.setTransparent(true);
        } else {
            String rgba = Integer.toHexString(backgroundColor.getRGB());
            String rgb = rgba.substring(2, rgba.length());
            mapRequest.setBGColour("0x" + rgb.toUpperCase());
            mapRequest.setTransparent(backgroundColor.getAlpha() < 255);
        }

        try {
            this.requestCRS = CRS.decode(requestSrs);
        } catch(Exception e) {
            throw new IOException("Could not decode request SRS " + requestSrs);
        }
        this.mapRequest = mapRequest;
        this.requestedEnvelope = gridEnvelope;
        this.width = width;
        this.height = height;

        return gridEnvelope;
    }

    public Format getFormat() {
        // this reader has not backing format
        return null;
    }

    /**
     * Returns the layer bounds
     * 
     * @return
     */
    public void updateBounds() {
        ReferencedEnvelope result = reference(layers.get(0).getEnvelope(crs));
        for (int i = 1; i < layers.size(); i++) {
            ReferencedEnvelope layerEnvelope = reference(layers.get(i).getEnvelope(crs));
            result.expandToInclude(layerEnvelope);
        }

        this.bounds = result;
        this.originalEnvelope = new GeneralEnvelope(result);
    }
    
    /**
     * Converts a {@link Envelope} into a {@link ReferencedEnvelope}
     * 
     * @param envelope
     * @return
     */
    ReferencedEnvelope reference(Envelope envelope) {
        ReferencedEnvelope env = new ReferencedEnvelope(envelope.getCoordinateReferenceSystem());
        env.expandToInclude(envelope.getMinimum(0), envelope.getMinimum(1));
        env.expandToInclude(envelope.getMaximum(0), envelope.getMaximum(1));
        return env;
    }

    /**
     * Converts a {@link GeneralEnvelope} into a {@link ReferencedEnvelope}
     * 
     * @param ge
     * @return
     */
    ReferencedEnvelope reference(GeneralEnvelope ge) {
        return new ReferencedEnvelope(ge.getMinimum(0), ge.getMaximum(0), ge.getMinimum(1), ge
                .getMaximum(1), ge.getCoordinateReferenceSystem());
    }
}