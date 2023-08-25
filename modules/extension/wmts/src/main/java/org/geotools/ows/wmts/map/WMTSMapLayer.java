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
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.GridReaderLayer;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.Parameter;
import org.geotools.styling.*;

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

    public static final DefaultParameterDescriptor<CoordinateReferenceSystem> SOURCE_CRS =
            new DefaultParameterDescriptor<>(
                    "SourceCRS", CoordinateReferenceSystem.class, null, null);

    private static StyleImpl createStyle() {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        RasterSymbolizerImpl symbolizer = factory.createRasterSymbolizer();

        RuleImpl rule = (RuleImpl) factory.createRule();
        rule.symbolizers().add(symbolizer);

        final FeatureTypeStyleImpl type = factory.createFeatureTypeStyle();
        type.rules().add(rule);

        StyleImpl style = factory.createStyle();
        style.featureTypeStyles().add(type);

        return style;
    }

    private String rawTime;

    /** Builds a new WMTS map layer */
    public WMTSMapLayer(WebMapTileServer wmts, Layer layer) {
        super(new WMTSCoverageReader(wmts, layer), createStyle());
    }
    /** Builds a new WMTS map layer */
    public WMTSMapLayer(WebMapTileServer wmts, Layer layer, CoordinateReferenceSystem sourceCRS) {
        super(new WMTSCoverageReader(wmts, layer), createStyle());
        GeneralParameterValue[] generalParameterValues = new GeneralParameterValue[1];
        Parameter<CoordinateReferenceSystem> parameter =
                new Parameter<>(WMTSMapLayer.SOURCE_CRS, sourceCRS);
        generalParameterValues[0] = parameter;
        this.params = generalParameterValues;
    }

    @Override
    public WMTSCoverageReader getReader() {
        return (WMTSCoverageReader) this.reader;
    }

    @Override
    public synchronized ReferencedEnvelope getBounds() {
        WMTSCoverageReader wmtsReader = getReader();
        if (wmtsReader != null) {
            return wmtsReader.bounds;
        }
        return super.getBounds();
    }

    /** Returns the CRS used to make requests to the remote WMTS */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return reader.getCoordinateReferenceSystem();
    }

    /**
     * Returns true if the specified CRS can be used directly to perform WMTS requests.
     *
     * <p>Natively supported crs will provide the best rendering quality as no client side
     * reprojection is necessary, the tiles coming from the WMTS server will be used as-is
     */
    public boolean isNativelySupported(CoordinateReferenceSystem crs) {
        return getReader().isNativelySupported(crs);
    }

    public String getRawTime() {
        return rawTime;
    }

    /** Set request Time the way the server wants it */
    public void setRawTime(String rawTime) {
        this.rawTime = rawTime;
        getReader().setRequestedTime(rawTime);
    }
}
