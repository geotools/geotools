// docs start source
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
import java.util.HashMap;
import java.util.Map;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.process.Process;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.geotools.process.raster.RasterToVectorFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.ProgressWindow;
import org.jdesktop.swingworker.SwingWorker;

/**
 * Illustrates running a process (vectorizing regions in a grid coverage) via
 * the GeoTools process API
 *
 * @author Michael Bedward
 */
public class ProcessAPI {

    public static void main(String[] args) throws Exception {
        new ProcessAPI().demo();
    }
    // docs end main

    // docs start demo
    private void demo() throws Exception {
        /*
         * Create a sample coverage: an image of square cells of alternating
         * 0 and 1 values
         */
        ReferencedEnvelope env = new ReferencedEnvelope(0.0, 8.0, 0.0, 8.0, DefaultGeographicCRS.WGS84);
        GridCoverage2D cov = createChessboardCoverage(256, 256, 4, env);

        /*
         * Parameters for the raster to vector process
         */
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put(RasterToVectorFactory.RASTER.key, cov);
        params.put(RasterToVectorFactory.BAND.key, Integer.valueOf(0));
        params.put(RasterToVectorFactory.BOUNDS.key, env);
        params.put(RasterToVectorFactory.OUTSIDE.key, Collections.singleton(0.0d));

        /*
         * Create a new RasterToVectorProcess instance using the
         * Processors factory finder.
         */
        final Process r2v = Processors.createProcess(new NameImpl("gt", "RasterToVector"));

        /*
         * For this example we use SwingWorker to run the process on a background
         * thread and display the results when the process has finished.
         */
        SwingWorker worker = new SwingWorker<Map<String, Object>, Void>() {

            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                ProgressWindow pw = new ProgressWindow(null);
                pw.setTitle("Vectorizing coverage");
                return r2v.execute(params, pw);
            }

            @Override
            protected void done() {
                Map<String, Object> vectorizingResults = null;
                try {
                    vectorizingResults = get();
                } catch (Exception ignore) {}

                if (vectorizingResults != null) {
                    SimpleFeatureCollection fc =
                            (SimpleFeatureCollection) vectorizingResults.get(RasterToVectorFactory.RESULT_FEATURES.key);

                    MapContext map = new DefaultMapContext();
                    map.setTitle("raster to vector conversion");
                    Style style = SLD.createPolygonStyle(Color.BLUE, Color.CYAN, 1.0f);
                    map.addLayer(fc, style);
                    JMapFrame.showMap(map);
                }
            }
        };

        worker.execute();
    }
    // docs end demo

    // docs start create coverage
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
// docs end source
