/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.map;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetFeatureInfoRequest;
import org.geotools.data.wms.request.GetMapRequest;
import org.geotools.data.wms.response.GetFeatureInfoResponse;
import org.geotools.data.wms.response.GetMapResponse;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.coverage.grid.Format;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.sun.org.apache.bcel.internal.generic.ASTORE;

/**
 * Wraps a WMS layer into a {@link MapLayer} for interactive rendering usage TODO: expose a
 * GetFeatureInfo that returns a feature collection TODO: expose the list of named styles and allow
 * choosing which style to use
 * 
 * @author Andrea Aime - OpenGeo
 */
public class WMSMapLayer extends DefaultMapLayer {
    /** The logger for the map module. */
    static public final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.map");

    /**
     * The default raster style
     */
    static Style STYLE;

    static GridCoverageFactory gcf = new GridCoverageFactory();

    WMSCoverageReader reader;

    static {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        RasterSymbolizer symbolizer = factory.createRasterSymbolizer();

        Rule rule = factory.createRule();
        rule.symbolizers().add(symbolizer);

        FeatureTypeStyle type = factory.createFeatureTypeStyle();
        type.rules().add(rule);

        STYLE = factory.createStyle();
        STYLE.featureTypeStyles().add(type);
    }

    /**
     * Builds a new WMS alyer
     * 
     * @param wms
     * @param layer
     */
    public WMSMapLayer(WebMapServer wms, Layer layer) {
        super((FeatureSource<SimpleFeatureType, SimpleFeature>) null, null, "");
        reader = new WMSCoverageReader(wms, layer);
        try {
            this.featureSource = DataUtilities.source(FeatureUtilities.wrapGridCoverageReader(
                    reader, null));
        } catch (Throwable t) {
            throw new RuntimeException("Unexpected exception occurred during map layer building", t);
        }

        this.style = STYLE;
    }

    public synchronized ReferencedEnvelope getBounds() {
        return reader.bounds;
    }

    /**
     * Retrieves the feature info as text (assuming "text/plain" is a supported feature info format)
     * 
     * @param pos
     *            the position to be checked, in real world coordinates
     * @return
     * @throws IOException
     */
    public String getFeatureInfoAsText(DirectPosition2D pos, int featureCount) throws IOException {
        BufferedReader br = null;
        try {
            InputStream is = reader.getFeatureInfo(pos, "text/plain", featureCount, reader.mapRequest);
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            throw e;
        } catch (Throwable t) {
            throw (IOException) new IOException("Failed to grab feature info").initCause(t);
        } finally {
            if (br != null)
                br.close();
        }
    }

    /**
     * Retrieves the feature info as a generic input stream, it's the duty of the caller to
     * interpret the contents and ensure the stream is closed feature info format)
     * 
     * @param pos
     *            the position to be checked, in real world coordinates
     * @param infoFormat
     *            The INFO_FORMAT parameter in the GetFeatureInfo request
     * @return
     * @throws IOException
     */
    public InputStream getFeatureInfo(DirectPosition2D pos, String infoFormat, int featureCount)
            throws IOException {
        return reader.getFeatureInfo(pos, infoFormat, featureCount, reader.mapRequest);
    }

    /**
     * Allows to run a standalone GetFeatureInfo request, without the need to have previously run a
     * GetMap request on this layer. Mostly useful for stateless users that rebuild the map context
     * for each rendering operation (e.g., GeoServer)
     * 
     * @param pos
     * @param infoFormat
     *            The INFO_FORMAT parameter in the GetFeatureInfo request
     * @return
     * @throws IOException
     */
    public InputStream getFeatureInfo(ReferencedEnvelope bbox, int width, int height, int x, int y,
            String infoFormat, int featureCount) throws IOException {
        try {
            reader.initMapRequest(bbox, width, height);
            // we need to convert x/y from the screen to the original coordinates, and then to the ones
            // that will be used to make the request
            AffineTransform at = RendererUtilities.worldToScreenTransform(bbox, new Rectangle(width, height));
            Point2D screenPos = new Point2D.Double(x, y);
            Point2D worldPos = new Point2D.Double(x, y);
            at.inverseTransform(screenPos, worldPos);
            DirectPosition2D fromPos = new DirectPosition2D(worldPos.getX(), worldPos.getY());
            DirectPosition2D toPos = new DirectPosition2D();
            MathTransform mt = CRS.findMathTransform(bbox.getCoordinateReferenceSystem(), reader.requestCRS, true);
            mt.transform(fromPos, toPos);
            return reader.getFeatureInfo(toPos, infoFormat, featureCount, reader.mapRequest);
        } catch(IOException e) {
            throw e;
        } catch(Throwable t) {
            throw (IOException) new IOException("Unexpected issue during GetFeatureInfo execution").initCause(t);
        }
    }

    /**
     * Returns the {@link WebMapServer} used by this layer
     * 
     * @return
     */
    public WebMapServer getWebMapServer() {
        return reader.wms;
    }

    /**
     * Returns the WMS {@link Layer} used by this layer
     * 
     * @return
     */
    public List<Layer> getWMSLayers() {
        return reader.layers;
    }

    /**
     * Returns the CRS used to make requests to the remote WMS
     * 
     * @return
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return reader.getCrs();
    }

    /**
     * Returns last GetMap request performed by this layer
     * 
     * @return
     */
    public GetMapRequest getLastGetMap() {
        return reader.mapRequest;
    }

    /**
     * Allows to add another WMS layer into the GetMap requests
     * 
     * @param layer
     */
    public void addLayer(Layer layer) {
        reader.addLayer(layer);
    }

    /**
     * Returns true if the specified CRS can be used directly to perform WMS requests. Natively
     * supported crs will provide the best rendering quality as no client side reprojection is
     * necessary, the image coming from the WMS server will be used as-is
     * 
     * @param crs
     * @return
     */
    public boolean isNativelySupported(CoordinateReferenceSystem crs) {
        try {
            String code = CRS.lookupIdentifier(crs, false);
            return code != null && reader.validSRS.contains(code);
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * A grid coverage readers backing onto a WMS server by issuing GetMap
     */
    static class WMSCoverageReader extends AbstractGridCoverage2DReader {
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
        private GetMapRequest mapRequest;

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
                        || "png".equals(format) || "png24".equals(format))
                    this.format = format;
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
                        break;
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

            grid = getMap(reference(requestedEnvelope), width, height);
            return grid;
        }

        /**
         * Execute the GetMap request
         */
        GridCoverage2D getMap(ReferencedEnvelope requestedEnvelope, int width, int height)
                throws IOException {
            // build the request
            ReferencedEnvelope gridEnvelope = initMapRequest(requestedEnvelope, width, height);

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
        ReferencedEnvelope initMapRequest(ReferencedEnvelope bbox, int width, int height)
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
            mapRequest.setFormat(format);
            mapRequest.setSRS(requestSrs);
            mapRequest.setBBox(gridEnvelope);
            mapRequest.setTransparent(true);

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

}
