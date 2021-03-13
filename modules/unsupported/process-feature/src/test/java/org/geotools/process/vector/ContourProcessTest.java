/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.vector;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.apache.commons.lang3.ArrayUtils;
import org.geotools.data.FileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Font;
import org.geotools.styling.SLD;
import org.geotools.styling.StyleBuilder;
import org.geotools.swing.JMapFrame;
import org.geotools.test.TestData;
import org.geotools.util.logging.Logging;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.util.ProgressListener;

/** @author ian */
public class ContourProcessTest {

    static final Logger LOGGER = Logging.getLogger(ContourProcessTest.class);

    private FeatureCollection features;

    private boolean show = false;

    @Before
    public void setup() throws IOException {
        URL url = TestData.getResource(this, "heights-0.shp");
        ShapefileDataStoreFactory fac = new ShapefileDataStoreFactory();
        FileDataStore ds = fac.createDataStore(url);
        features = ds.getFeatureSource().getFeatures();
    }

    @Test
    public void testSimplePointsInterval() {
        ReferencedEnvelope bounds =
                new ReferencedEnvelope(0, 30, 0, 30, DefaultGeographicCRS.WGS84);
        Coordinate[] data = {
            new Coordinate(10, 10, 100),
            new Coordinate(10, 20, 20),
            new Coordinate(20, 10, 0),
            new Coordinate(20, 20, 80)
        };
        SimpleFeatureCollection fc = ProcessTestUtilities.createPoints(data, bounds);
        ContourProcess cp = new ContourProcess();
        double[] levels = {};
        double interval = 10;
        Boolean simplify = Boolean.TRUE;
        Boolean smooth = Boolean.TRUE;
        ProgressListener progressListener = null;
        SimpleFeatureCollection results =
                cp.execute(fc, "value", levels, interval, simplify, smooth, progressListener);

        try {
            displayResults(fc, results);
        } catch (InterruptedException e) {
        }

        assertEquals(14, results.size());
    }

    @Test
    public void testSimplePointsLevel() {
        ReferencedEnvelope bounds =
                new ReferencedEnvelope(0, 30, 0, 30, DefaultGeographicCRS.WGS84);
        Coordinate[] data = {
            new Coordinate(10, 10, 100),
            new Coordinate(10, 20, 20),
            new Coordinate(20, 10, 0),
            new Coordinate(20, 20, 80)
        };
        SimpleFeatureCollection fc = ProcessTestUtilities.createPoints(data, bounds);
        ContourProcess cp = new ContourProcess();
        double[] levels = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        double interval = 10;
        Boolean simplify = Boolean.TRUE;
        Boolean smooth = Boolean.TRUE;
        ProgressListener progressListener = null;
        SimpleFeatureCollection results =
                cp.execute(fc, "value", levels, interval, simplify, smooth, progressListener);

        try {
            displayResults(fc, results);
        } catch (InterruptedException e) {
        }

        assertEquals(14, results.size());
    }

    @Test
    public void testLevels() {
        ContourProcess cp = new ContourProcess();

        ArrayList<Double> levels = new ArrayList<>();
        for (int cc = 0; cc < 250; cc += 10) {
            levels.add((double) cc);
        }
        double interval = 0;

        Boolean simplify = Boolean.TRUE;
        Boolean smooth = Boolean.TRUE;
        ProgressListener progressListener = null;
        SimpleFeatureCollection results =
                cp.execute(
                        features,
                        "propertyVa",
                        ArrayUtils.toPrimitive(levels.toArray(new Double[] {})),
                        interval,
                        simplify,
                        smooth,
                        progressListener);
        try {
            displayResults((SimpleFeatureCollection) features, results);
        } catch (InterruptedException e) {
        }
        assertEquals(120, results.size());
    }

    /** @param results */
    private static Object lock = new Object();

    private void displayResults(SimpleFeatureCollection points, SimpleFeatureCollection results)
            throws InterruptedException {
        if (!show) return;
        MapContent map = new MapContent();
        map.addLayer(
                new FeatureLayer(
                        points, SLD.createPointStyle("circle", Color.red, Color.red, 1, 5)));
        StyleBuilder sb = new StyleBuilder();
        Font font = sb.createFont("Arial", 10);
        map.addLayer(
                new FeatureLayer(results, SLD.createLineStyle(Color.black, 1, "elevation", font)));

        JMapFrame frame = new JMapFrame();

        frame.setMapContent(map);
        frame.enableStatusBar(true);
        frame.enableToolBar(true);
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setVisible(true);

        Thread t =
                new Thread() {
                    @Override
                    public void run() {
                        synchronized (lock) {
                            while (frame.isVisible())
                                try {
                                    lock.wait();
                                } catch (InterruptedException e) {
                                    LOGGER.log(Level.WARNING, "", e);
                                }
                        }
                    }
                };
        t.start();

        frame.addWindowListener(
                new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent arg0) {
                        synchronized (lock) {
                            frame.setVisible(false);
                            lock.notify();
                        }
                    }
                });

        t.join();
    }
}
