/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */

package org.geotools.wmts;

import java.awt.Rectangle;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.map.WMTSCoverageReader;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Uses the WMTSCoverageReader to write a mosaicked image of tiles covering a given extent.
 *
 * <p>The extent is given in a projection that is not provided by the server.
 *
 * <p>You must give a path to where the file should be saved. It will be in PNG.
 *
 * @author Roar Brænden
 */
public class WMTSCoverage {

    private static final String WMTS_ORTOFOTO_URL =
            "https://geodata.npolar.no/arcgis/rest/services/Basisdata/NP_Ortofoto_Svalbard_WMTS_25833/MapServer/WMTS/1.0.0/WMTSCapabilities.xml";

    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            throw new IllegalArgumentException("You must provide a path for the resulting image.");
        }

        File resultFile = new File(args[0]);
        // start wmtsCoverage example
        WebMapTileServer wmts = new WebMapTileServer(new URL(WMTS_ORTOFOTO_URL));
        WMTSLayer layer = wmts.getCapabilities().getLayerList().get(0);
        WMTSCoverageReader reader = new WMTSCoverageReader(wmts, layer);

        ReferencedEnvelope envelope =
                new ReferencedEnvelope(
                        17.0697, 18.1937, 79.3052, 79.4667, DefaultGeographicCRS.WGS84);

        CoordinateReferenceSystem upsCrs = CRS.decode("EPSG:32661");
        ReferencedEnvelope upsExtent =
                new ReferencedEnvelope(
                        JTS.transform(
                                envelope,
                                CRS.findMathTransform(DefaultGeographicCRS.WGS84, upsCrs)),
                        upsCrs);

        System.out.println(upsExtent);

        int width = 1400;
        int height = 1400;

        Parameter<GridGeometry2D> readGG =
                (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        readGG.setValue(
                new GridGeometry2D(new GridEnvelope2D(new Rectangle(width, height)), upsExtent));

        GeneralParameterValue[] parameters = new GeneralParameterValue[] {readGG};
        GridCoverage2D coverage = reader.read(parameters);
        ImageIO.write(coverage.getRenderedImage(), "png", resultFile);
        // end wmtsCoverage example
    }
}
