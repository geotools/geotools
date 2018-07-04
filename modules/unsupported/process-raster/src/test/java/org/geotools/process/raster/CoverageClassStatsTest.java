/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import javax.imageio.ImageIO;
import javax.media.jai.TiledImage;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.classify.ClassificationMethod;
import org.geotools.process.raster.CoverageClassStats.Results;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.datum.DefaultEngineeringDatum;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.jaitools.imageutils.ImageUtils;
import org.jaitools.numeric.Range;
import org.jaitools.numeric.Statistic;
import org.junit.Test;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;

public class CoverageClassStatsTest {

    @Test
    public void testEqualInterval() throws Exception {
        CoverageClassStats p = new CoverageClassStats();
        Results r =
                p.execute(
                        createCoverage(),
                        new LinkedHashSet<Statistic>(Arrays.asList(Statistic.MEAN, Statistic.SUM)),
                        null,
                        4,
                        ClassificationMethod.EQUAL_INTERVAL,
                        null,
                        null);

        assertResults(
                r,
                new double[] {1d, 14d, 27d, 40d, 53d},
                /*new double[][]{{5.1,46d,3.8},{21.2, 106d, 5.7},{Double.NaN,Double.NaN,Double.NaN},
                {49d,98d,4d}});*/
                new double[][] {{5.1, 46d}, {21.2, 106d}, {Double.NaN, Double.NaN}, {49d, 98d}});
    }

    @Test
    public void testEqualInterval2() throws Exception {
        CoverageClassStats p = new CoverageClassStats();
        Results r =
                p.execute(
                        createViewshedCoverage(),
                        Collections.singleton(Statistic.MEAN),
                        null,
                        4,
                        ClassificationMethod.EQUAL_INTERVAL,
                        null,
                        null);

        assertResults(
                r,
                new double[] {-1d, -0.5, 0d, 0.5, 1d},
                new double[][] {{-1d}, {Double.NaN}, {Double.NaN}, {1d}});
    }

    @Test
    public void testQuantile() throws Exception {
        CoverageClassStats p = new CoverageClassStats();
        Results r =
                p.execute(
                        createCoverage(),
                        new LinkedHashSet<Statistic>(Arrays.asList(Statistic.MEAN, Statistic.SUM)),
                        null,
                        4,
                        ClassificationMethod.QUANTILE,
                        null,
                        null);

        assertResults(
                r,
                new double[] {1d, 3d, 11d, 26d, 53d},
                // new double[][]{{1.3,4d,0.47},{6.2, 31d, 2.6},{16.3, 65d, 4.8},{37.5, 150d,
                // 11.8}});
                new double[][] {{1.3, 4d}, {6.2, 31d}, {16.3, 65d}, {37.5, 150d}});
    }

    @Test
    public void testNaturalBreaks() throws Exception {
        CoverageClassStats p = new CoverageClassStats();
        Results r =
                p.execute(
                        createCoverage(),
                        new LinkedHashSet<Statistic>(Arrays.asList(Statistic.MEAN, Statistic.SUM)),
                        null,
                        4,
                        ClassificationMethod.NATURAL_BREAKS,
                        null,
                        null);

        assertResults(
                r,
                new double[] {1d, 3d, 16d, 26d, 53d},
                new double[][] {{1.3, 4d}, {8d, 56d}, {20d, 40d}, {37.5, 150d}});
    }

    private void assertResults(Results r, double[] ranges, double[][] stats) {
        assertEquals(ranges.length - 1, r.size());
        assertEquals(stats.length, r.size());

        for (int i = 0; i < ranges.length - 1; i++) {
            Range range = r.range(i);
            assertEquals(ranges[i], range.getMin());
            assertEquals(ranges[i + 1], range.getMax());
            assertEquals(stats[i].length, r.getStats().size());

            int j = 0;
            for (Statistic s : r.getStats()) {
                assertEquals(stats[i][j++], r.value(i, s), 0.1);
            }
        }
    }

    /*@Test
    public void testSfDem() throws Exception {
        CoverageClassStats p = new CoverageClassStats();
        Results r =
            p.execute(createSfDemCoverage(), null, 4, Method.EQUAL_INTERVAL, null, -9.999999933815813E36, null);
        r.print();
    }*/

    /*
    @Test
    public void testMedfordDem() throws Exception {
        CoverageClassStats p = new CoverageClassStats();
        Results r =
            p.execute(createMedfordDemCoverage(), null, 10, Method.EQUAL_INTERVAL, null, null, null);
        r.print();
    }*/

    GridCoverage2D createMedfordDemCoverage() throws IOException {

        BufferedImage img =
                ImageIO.read(
                        new File(
                                "/Users/jdeolive/Projects/suite_trunk/git/data_dir/data/dem/medford_dem.tif"));

        Rectangle bounds =
                new Rectangle(img.getMinX(), img.getMinY(), img.getWidth(), img.getHeight());
        ReferencedEnvelope env = new ReferencedEnvelope(bounds, null);

        GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
        return factory.create("coverage", img, env);
    }

    GridCoverage2D createSfDemCoverage() throws IOException {

        BufferedImage img =
                ImageIO.read(
                        new File(
                                "/Users/jdeolive/Projects/gs_trunk/svn/data/release/data/sf/sfdem.tif"));

        Rectangle bounds =
                new Rectangle(img.getMinX(), img.getMinY(), img.getWidth(), img.getHeight());
        ReferencedEnvelope env = new ReferencedEnvelope(bounds, null);

        GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
        return factory.create("coverage", img, env);
    }

    GridCoverage2D createViewshedCoverage() throws IOException {
        URL url = getClass().getResource("viewshed.tif");
        BufferedImage img = ImageIO.read(url);

        Rectangle bounds =
                new Rectangle(img.getMinX(), img.getMinY(), img.getWidth(), img.getHeight());
        ReferencedEnvelope env = new ReferencedEnvelope(bounds, null);

        GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
        return factory.create("coverage", img, env);
    }

    GridCoverage2D createCoverage() {
        TiledImage img =
                ImageUtils.createImageFromArray(
                        new Number[] {1, 1, 2, 3, 3, 8, 8, 9, 11, 14, 16, 24, 26, 26, 45, 53},
                        4,
                        4);

        AffineTransform2D tx = new AffineTransform2D(new AffineTransform(1, 0, 0, -1, -0.5, -0.5));

        CoordinateReferenceSystem crs =
                new DefaultEngineeringCRS(
                        "test", DefaultEngineeringDatum.UNKNOWN, DefaultCartesianCS.DISPLAY);
        GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);

        GridGeometry2D gridGeom =
                new GridGeometry2D(
                        new GeneralGridEnvelope(img, 2), PixelInCell.CELL_CORNER, tx, crs, null);

        GridSampleDimension[] bands =
                new GridSampleDimension[] {
                    new GridSampleDimension(ColorInterpretation.GRAY_INDEX.name())
                };
        return factory.create("test", img, gridGeom, bands, null, null);
    }
}
