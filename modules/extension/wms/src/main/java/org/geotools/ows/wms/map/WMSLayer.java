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
package org.geotools.ows.wms.map;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.GridReaderLayer;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.ows.wms.request.GetMapRequest;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * Wraps a WMS layer into a {@link Layer} for interactive rendering usage TODO: expose a
 * GetFeatureInfo that returns a feature collection TODO: expose the list of named styles and allow
 * choosing which style to use
 *
 * @author Andrea Aime - OpenGeo
 */
public class WMSLayer extends GridReaderLayer {

    /** The default raster style */
    static Style STYLE;

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

    /** Builds a new WMS layer */
    public WMSLayer(WebMapServer wms, Layer layer) {
        super(new WMSCoverageReader(wms, layer), STYLE);
    }

    /** Builds a new WMS layer */
    public WMSLayer(WebMapServer wms, Layer layer, String style) {
        super(new WMSCoverageReader(wms, layer, style), STYLE);
    }

    public WMSLayer(WebMapServer wms, Layer layer, String style, String imageFromat) {
        super(new WMSCoverageReader(wms, layer, style, imageFromat), STYLE);
    }

    public WMSCoverageReader getReader() {
        return (WMSCoverageReader) this.reader;
    }

    public synchronized ReferencedEnvelope getBounds() {
        WMSCoverageReader wmsReader = getReader();
        if (wmsReader != null) {
            return wmsReader.bounds;
        }
        return super.getBounds();
    }

    /**
     * Retrieves the feature info as text (assuming "text/plain" is a supported feature info format)
     *
     * @param pos the position to be checked, in real world coordinates
     */
    public String getFeatureInfoAsText(DirectPosition2D pos, int featureCount) throws IOException {
        GetMapRequest mapRequest = getReader().mapRequest;
        try (InputStream is =
                        getReader().getFeatureInfo(pos, "text/plain", featureCount, mapRequest);
                BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
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
        }
    }

    /**
     * Retrieves the feature info as a generic input stream, it's the duty of the caller to
     * interpret the contents and ensure the stream is closed feature info format)
     *
     * @param pos the position to be checked, in real world coordinates
     * @param infoFormat The INFO_FORMAT parameter in the GetFeatureInfo request
     */
    public InputStream getFeatureInfo(DirectPosition2D pos, String infoFormat, int featureCount)
            throws IOException {
        GetMapRequest mapRequest = getReader().mapRequest;
        return getReader().getFeatureInfo(pos, infoFormat, featureCount, mapRequest);
    }

    /**
     * Allows to run a standalone GetFeatureInfo request, without the need to have previously run a
     * GetMap request on this layer. Mostly useful for stateless users that rebuild the map context
     * for each rendering operation (e.g., GeoServer)
     *
     * @param infoFormat The INFO_FORMAT parameter in the GetFeatureInfo request
     */
    public InputStream getFeatureInfo(
            ReferencedEnvelope bbox,
            int width,
            int height,
            int x,
            int y,
            String infoFormat,
            int featureCount)
            throws IOException {
        try {
            getReader().initMapRequest(bbox, width, height, null);
            // we need to convert x/y from the screen to the original coordinates, and then to the
            // ones
            // that will be used to make the request
            AffineTransform at =
                    RendererUtilities.worldToScreenTransform(bbox, new Rectangle(width, height));
            Point2D screenPos = new Point2D.Double(x, y);
            Point2D worldPos = new Point2D.Double(x, y);
            at.inverseTransform(screenPos, worldPos);
            DirectPosition2D fromPos = new DirectPosition2D(worldPos.getX(), worldPos.getY());
            DirectPosition2D toPos = new DirectPosition2D();
            MathTransform mt =
                    CRS.findMathTransform(
                            bbox.getCoordinateReferenceSystem(), getReader().requestCRS, true);
            mt.transform(fromPos, toPos);
            GetMapRequest mapRequest = getReader().mapRequest;
            return getReader().getFeatureInfo(toPos, infoFormat, featureCount, mapRequest);
        } catch (IOException e) {
            throw e;
        } catch (Throwable t) {
            throw (IOException)
                    new IOException("Unexpected issue during GetFeatureInfo execution")
                            .initCause(t);
        }
    }

    /** Returns the {@link WebMapServer} used by this layer */
    public WebMapServer getWebMapServer() {
        return getReader().wms;
    }

    /** Returns the WMS {@link Layer}s used by this layer */
    public List<Layer> getWMSLayers() {
        return ((WMSCoverageReader) reader).getLayers();
    }

    /** return the names of the styles used by this layer. */
    public List<String> getWMSStyles() {
        return ((WMSCoverageReader) reader).getStyles();
    }

    /** Returns the CRS used to make requests to the remote WMS */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return reader.getCoordinateReferenceSystem();
    }

    /** Returns last GetMap request performed by this layer */
    public GetMapRequest getLastGetMap() {
        return getReader().mapRequest;
    }

    /** Allows to add another WMS layer into the GetMap requests */
    public void addLayer(Layer layer) {
        addLayer(layer, "");
    }

    /** Allows to add another WMS layer into the GetMap requests */
    public void addLayer(Layer layer, String style) {
        getReader().addLayer(layer, style);
    }

    /**
     * Returns true if the specified CRS can be used directly to perform WMS requests. Natively
     * supported crs will provide the best rendering quality as no client side reprojection is
     * necessary, the image coming from the WMS server will be used as-is
     */
    public boolean isNativelySupported(CoordinateReferenceSystem crs) {
        try {
            String code = CRS.lookupIdentifier(crs, false);
            return code != null && getReader().validSRS.contains(code);
        } catch (Throwable t) {
            return false;
        }
    }
}
