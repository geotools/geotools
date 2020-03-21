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
package org.geotools.ows.wmts.map;

import java.util.logging.Logger;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.GridReaderLayer;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.referencing.CRS;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Wraps a WMTS layer into a {@link Layer} for interactive rendering usage.
 *
 * <p>TODO: expose a GetFeatureInfo that returns a feature collection
 *
 * <p>TODO: expose the list of named styles and allow choosing which style to use
 *
 * @author Ian Turton
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public class WMTSMapLayer extends GridReaderLayer {

    public static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(WMTSMapLayer.class);

    private static Style createStyle() {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        RasterSymbolizer symbolizer = factory.createRasterSymbolizer();

        Rule rule = factory.createRule();
        rule.symbolizers().add(symbolizer);

        final FeatureTypeStyle type = factory.createFeatureTypeStyle();
        type.rules().add(rule);

        Style style = factory.createStyle();
        style.featureTypeStyles().add(type);

        return style;
    }

    private String rawTime;

    /** Builds a new WMTS map layer */
    public WMTSMapLayer(WebMapTileServer wmts, Layer layer) {
        super(new WMTSCoverageReader(wmts, layer), createStyle());
    }

    public WMTSCoverageReader getReader() {
        return (WMTSCoverageReader) this.reader;
    }

    public synchronized ReferencedEnvelope getBounds() {
        WMTSCoverageReader wmtsReader = getReader();
        if (wmtsReader != null) {
            return wmtsReader.bounds;
        }
        return super.getBounds();
    }

    /** Returns the {@link WebMapTileServer} used by this layer */
    public WebMapTileServer getWebMapServer() {
        return getReader().wmts;
    }

    /** Returns the CRS used to make requests to the remote WMTS */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return reader.getCoordinateReferenceSystem();
    }

    /** Returns last GetMap request performed by this layer */
    public GetTileRequest getLastGetMap() {
        return getReader().getTileRequest();
    }

    /**
     * Returns true if the specified CRS can be used directly to perform WMTS requests.
     *
     * <p>Natively supported crs will provide the best rendering quality as no client side
     * reprojection is necessary, the tiles coming from the WMTS server will be used as-is
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
