/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.raster;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.Map;

import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.Process;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.geotools.referencing.crs.DefaultGeographicCRS;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.ProgressListener;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael Bedward
 *
 * @source $URL$
 */
public class VectorToRasterProcessTest {

    public VectorToRasterProcessTest() {
    }

    @Test
    public void testProcess() throws Exception {
        System.out.println("process");
        SimpleFeatureCollection features = createTestFeatures();
        ReferencedEnvelope bounds = features.getBounds();

        Dimension gridDim = new Dimension(
                (int)bounds.getWidth(),
                (int)bounds.getHeight());

        String covName = "Test";
        ProgressListener monitor = null;

        GridCoverage2D cov = VectorToRasterProcess.process(features, "value", gridDim, bounds, covName, monitor);
        //textPrint(cov);
        /*
         * Compare the coverage to the input features. We are expecting
         * to see the two small rectangles (values 1 and 3) 'on top' of
         * the larger rectangle (value 2)
         */
        Map<Integer, Envelope> rects = new HashMap<Integer, Envelope>();
        SimpleFeatureIterator iter = features.features();
        while (iter.hasNext()) {
            SimpleFeature sf = iter.next();
            rects.put((Integer)sf.getAttribute("value"),
                      ((Geometry) sf.getDefaultGeometry()).getEnvelopeInternal());
        }

        try {
            MathTransform2D mt = cov.getGridGeometry().getGridToCRS2D(PixelOrientation.CENTER);
            Point gridP = new Point();
            Point2D.Double geoP = new Point2D.Double();
            int[] covValue = new int[1];
            for (gridP.y = 0; gridP.y < gridDim.height; gridP.y++) {
                for (gridP.x = 0; gridP.x < gridDim.width; gridP.x++) {
                    mt.transform(gridP, geoP);
                }

                cov.evaluate(geoP, covValue);
                if (covValue[0] != 0) {
                    assertTrue(rects.get(covValue[0]).contains(geoP.x, geoP.y));
                }
            }

        } catch (TransformException tex) {
            throw new RuntimeException(tex);
        }

    }

    @Test
    public void testCreateProcess() throws Exception {
        System.out.println("   create process");
        Process p = Processors.createProcess(new NameImpl("gt", "VectorToRaster"));
        assertNotNull(p);
        assertTrue(p instanceof VectorToRasterProcess);
    }

    /**
     * Create a set of three features, each of which is a rectangular
     * polygon. The features are arranged roughly like this:
     * <pre>
     *             |---------------|
     *             |               |
     *       |-----|----|     |----|-----|
     *       |     |    |     |    |     |
     *       |     |    |     |    |     |
     *       |     |    |     |    |     |
     *       |     |    |     |    |     |
     *       |-----|----|     |----|-----|
     *             |               |
     *             |---------------|
     * </pre>
     */
    private SimpleFeatureCollection createTestFeatures() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("testType");
        tb.setNamespaceURI("http://www.geotools.org/");
        tb.setCRS(DefaultGeographicCRS.WGS84);

        //add attributes
        tb.add("shape", MultiPolygon.class);
        tb.add("name", String.class);
        tb.add("value", Integer.class);

        SimpleFeatureType type = tb.buildFeatureType();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        WKTReader reader = new WKTReader();

        SimpleFeatureCollection fc = FeatureCollections.newCollection();
        SimpleFeature feature;

        feature = buildFeature(builder, reader,
                "MULTIPOLYGON(((10 10, 10 20, 30 20, 30 10, 10 10)))", "left", 1);
        fc.add(feature);

        feature = buildFeature(builder, reader,
                "MULTIPOLYGON(((40 10, 40 20, 60 20, 60 10, 40 10)))", "right", 3);
        fc.add(feature);

        feature = buildFeature(builder, reader,
                "MULTIPOLYGON(((20 0, 20 30, 50 30, 50 0, 20 0)))", "middle", 2);
        fc.add(feature);

        return fc;
    }

    private SimpleFeature buildFeature(SimpleFeatureBuilder builder,
            WKTReader reader,
            String wkt, String name, int value) {

        try {
            Geometry geom = reader.read(wkt);

            builder.add(geom);
            builder.add(name);
            builder.add(value);
            return builder.buildFeature(null);

        } catch (ParseException pex) {
            throw new RuntimeException("Error in the wkt: " + wkt);
        }
    }

    /**
     * Dump the values in a (small) grid coverage to the console
     */
    private void textPrint(GridCoverage2D cov) {
        RenderedImage img = cov.getRenderedImage();
        int nb = img.getSampleModel().getNumBands();
        RectIter iter = RectIterFactory.create(img, null);
        int[] pixel = new int[nb];
        int w = img.getWidth();
        do {
            do {
                iter.getPixel(pixel);
                for (int i = 0; i < nb; i++) {
                    System.out.print(pixel[i]);
                }
            } while (!iter.nextPixelDone());
            iter.startPixels();
            System.out.println();
        } while (!iter.nextLineDone());
    }
}
