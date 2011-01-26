/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */


package org.geotools.demo.process;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Collections;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.process.raster.RasterToVectorProcess;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;

/**
 * This examples creates a GridCoverage2D object from an image with a chessboard pattern
 * (alternating squares with value 0 or 1) and then converts the coverage to polygon
 * features which it displays with a JMapFrame.
 *
 * @author Michael Bedward
 */
public class RasterToVector {
    
    public static void main(String[] args) throws Exception {
        new RasterToVector().demo();
    }

    private void demo() throws Exception {
        ReferencedEnvelope env = new ReferencedEnvelope(0.0, 8.0, 0.0, 8.0, DefaultGeographicCRS.WGS84);
        GridCoverage2D cov = createChessboardCoverage(256, 256, 32, env);
        SimpleFeatureCollection fc = RasterToVectorProcess.process(cov, 0, env, Collections.singletonList(0.0d), true, null);

        MapContext map = new DefaultMapContext();
        map.setTitle("raster to vector conversion");
        Style style = SLD.createPolygonStyle(Color.BLUE, Color.CYAN, 1.0f);
        map.addLayer(fc, style);
        JMapFrame.showMap(map);
    }

    private GridCoverage2D createChessboardCoverage(int imgWidth, int imgHeight, int squareWidth, ReferencedEnvelope env) {
        GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
        GridCoverage2D cov = factory.create("chessboard", createChessboardImage(imgWidth, imgHeight, squareWidth), env);
        return cov;
    }

    private RenderedImage createChessboardImage(int imgWidth, int imgHeight, int squareWidth) {
        BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_BYTE_BINARY);
        WritableRaster raster = img.getRaster();

        for (int y = 0; y < imgHeight; y++) {
            boolean oddRow = (y / squareWidth) % 2 == 1;
            for (int x = 0; x < imgWidth; x++) {
                boolean oddCol = (x / squareWidth) % 2 == 1;
                raster.setSample(x, y, 0, (oddCol == oddRow ? 1 : 0));
            }
        }

        return img;
    }
}
