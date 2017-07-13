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

import java.util.logging.Logger;

import org.geotools.data.ows.Layer;
import org.geotools.data.wmts.WebMapTileServer;
import org.geotools.data.wmts.request.GetTileRequest;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Wraps a WMTS layer into a {@link MapLayer} for interactive rendering usage
 * 
 * TODO: expose a GetFeatureInfo that returns a feature collection
 * TODO: expose the list of named styles and allow choosing which style to use
 *
 * @author Ian Turton
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public class WMTSMapLayer extends GridReaderLayer {

    static public final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.map");
    /**
     * The default raster style
     */
    final static Style STYLE;
    static {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        RasterSymbolizer symbolizer = factory.createRasterSymbolizer();

        Rule rule = factory.createRule();
        rule.symbolizers().add(symbolizer);

        final FeatureTypeStyle type = factory.createFeatureTypeStyle();
        type.rules().add(rule);

        STYLE = factory.createStyle();
        STYLE.featureTypeStyles().add(type);
    }

    private String rawTime;

    /**
     * Builds a new WMS layer
     *
     * @param wmts
     * @param layer
     */
    public WMTSMapLayer(WebMapTileServer wmts, Layer layer) {
        super( new WMTSCoverageReader(wmts, layer), STYLE );
    }
    
    public WMTSCoverageReader getReader(){
        return (WMTSCoverageReader) this.reader;
    }

    public synchronized ReferencedEnvelope getBounds() {
        WMTSCoverageReader wmsReader = getReader();
        if( wmsReader != null ){
            return wmsReader.bounds;
        }
        return super.getBounds();
    }

    /**
     * Retrieves the feature info as text (assuming "text/plain" is a supported feature info format)
     *
     * @param pos
     *            the position to be checked, in real world coordinates
     * @return
     * @throws IOException
     */
//    public String getFeatureInfoAsText(DirectPosition2D pos, int featureCount) throws IOException {
//        BufferedReader br = null;
//        try {
//            GetTileRequest mapRequest = getReader().getTileRequest();
//            InputStream is = getReader().getFeatureInfo(pos, "text/plain", featureCount, mapRequest);
//            br = new BufferedReader(new InputStreamReader(is));
//            String line;
//            StringBuilder sb = new StringBuilder();
//            while ((line = br.readLine()) != null) {
//                sb.append(line).append("\n");
//            }
//            return sb.toString();
//        } catch (IOException e) {
//            throw e;
//        } catch (Throwable t) {
//            throw new IOException("Failed to grab feature info", t);
//        } finally {
//            if (br != null)
//                br.close();
//        }
//    }

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
//    public InputStream getFeatureInfo(DirectPosition2D pos, String infoFormat, int featureCount)
//            throws IOException {
//        GetTileRequest mapRequest = getReader().getTileRequest();
//        return getReader().getFeatureInfo(pos, infoFormat, featureCount, mapRequest);
//    }

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
//    public InputStream getFeatureInfo(ReferencedEnvelope bbox, int width, int height, int x, int y,
//            String infoFormat, int featureCount) throws IOException {
//        try {
//            getReader().initTileRequest(bbox, width, height);
//            // we need to convert x/y from the screen to the original coordinates, and then to the ones
//            // that will be used to make the request
//            AffineTransform at = RendererUtilities.worldToScreenTransform(bbox, new Rectangle(width, height));
//            Point2D screenPos = new Point2D.Double(x, y);
//            Point2D worldPos = new Point2D.Double(x, y);
//            at.inverseTransform(screenPos, worldPos);
//            DirectPosition2D fromPos = new DirectPosition2D(worldPos.getX(), worldPos.getY());
//            DirectPosition2D toPos = new DirectPosition2D();
//            MathTransform mt = CRS.findMathTransform(bbox.getCoordinateReferenceSystem(), getReader().requestCRS, true);
//            mt.transform(fromPos, toPos);
//            GetTileRequest mapRequest = getReader().getTileRequest();
//            return getReader().getFeatureInfo(toPos, infoFormat, featureCount, mapRequest);
//        } catch(IOException e) {
//            throw e;
//        } catch(Exception t) {
//            throw new IOException("Unexpected issue during GetFeatureInfo execution", t);
//        }
//    }

    /**
     * Returns the {@link WebMapServer} used by this layer
     *
     * @return
     */
    public WebMapTileServer getWebMapServer() {
        return getReader().wmts;
    }

    /**
     * Returns the CRS used to make requests to the remote WMS
     *
     * @return
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return reader.getCoordinateReferenceSystem();
    }

    /**
     * Returns last GetMap request performed by this layer
     *
     * @return
     */
    public GetTileRequest getLastGetMap() {
        return getReader().getTileRequest();
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
            return code != null && getReader().validSRS.contains(code);
        } catch (Exception t) {
            return false;
        }
    }

    public String getRawTime() {
        return rawTime;
    }

    public void setRawTime(String rawTime) {
        this.rawTime = rawTime;
        getReader().setRequestedTime(rawTime);
    }
}
