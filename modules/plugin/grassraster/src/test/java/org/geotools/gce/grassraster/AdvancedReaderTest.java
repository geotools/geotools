/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;
import org.geotools.api.coverage.grid.GridCoverageReader;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.grassraster.format.GrassCoverageFormatFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.util.URLs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Reader tests for different active region cases.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class AdvancedReaderTest {

    private CoordinateReferenceSystem crs = null;
    private CoordinateReferenceSystem crs32632 = null;

    private File pitFile;
    private File grassFile;

    @Before
    public void setUp() throws Exception {
        URL pitUrl = this.getClass().getClassLoader().getResource("testlocation/test/cell/pit");
        pitFile = URLs.urlToFile(pitUrl);
        crs = CRS.decode("EPSG:3004");
        crs32632 = CRS.decode("EPSG:32632");

        URL testUrl = this.getClass().getClassLoader().getResource("gbovest/testcase/cell/test");
        grassFile = new File(testUrl.toURI());
    }

    /** Read the whole Image (at file region and resolution) */
    @Test
    public void testReadFromFileRegion() throws Exception {
        double n = 5140020.0;
        double s = 5139780.0;
        double w = 1640650.0;
        double e = 1640950.0;
        int rows = 8;
        int cols = 10;
        GridCoverage2D gc = readFile(pitFile, cols, rows, n, s, w, e, crs);

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.matrix, 0);
    }

    /** Read a region which is bigger, in all direction, than the file region. */
    @Test
    public void testReadFromWrappingRegion() throws IOException {
        JGrassRegion r = new JGrassRegion(1640590.0, 1641010.0, 5139720.0, 5140080.0, 30.0, 30.0);

        GridCoverage2D gc = read(pitFile, r, crs);

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.matrixMore, 0);
    }

    /** Read a region which dimension is smaller and completely contained in thefile region. */
    @Test
    public void testReadFromContainedRegion() throws IOException {
        JGrassRegion r = new JGrassRegion(1640710.0, 1640890.0, 5139840.0, 5139960.0, 30.0, 30.0);

        GridCoverage2D gc = read(pitFile, r, crs);

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.matrixLess, 0);
    }

    /**
     * Test the reading of the map in a region containing the upper right corner.
     *
     * <p>The schema is:
     *
     * <table border=1>
     * <tr>
     * <td>11</td><td>12</td>
     * </tr>
     * <tr>
     * <td>21</td><td>22</td>
     * </tr>
     * </table>
     */
    @Test
    public void testReadFromRegion12() throws IOException {
        JGrassRegion r = new JGrassRegion(1640710.0, 1641010.0, 5139840.0, 5140080.0, 30.0, 30.0);
        GridCoverage2D gc = read(pitFile, r, crs);

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.differentRegion1, 0);
    }

    /**
     * Test the reading of the map in a region containing the lower left corner.
     *
     * <p>The schema is:
     *
     * <table border=1>
     * <tr>
     * <td>11</td><td>12</td>
     * </tr>
     * <tr>
     * <td>21</td><td>22</td>
     * </tr>
     * </table>
     */
    @Test
    public void testReadFromRegion21() throws IOException {
        JGrassRegion r = new JGrassRegion(1640590.0, 1640890.0, 5139720.0, 5139960.0, 30.0, 30.0);
        GridCoverage2D gc = read(pitFile, r, crs);

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.differentRegion2, 0);
    }

    /**
     * Test the reading of the map in a region containing the lower right corner.
     *
     * <p>The schema is:
     *
     * <table border=1>
     * <tr>
     * <td>11</td><td>12</td>
     * </tr>
     * <tr>
     * <td>21</td><td>22</td>
     * </tr>
     * </table>
     */
    @Test
    public void testReadFromRegion22() throws IOException {
        JGrassRegion r = new JGrassRegion(1640710.0, 1641010.0, 5139720.0, 5139960.0, 30.0, 30.0);
        GridCoverage2D gc = read(pitFile, r, crs);

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.differentRegion3, 0);
    }

    /**
     * Test the reading of the map in a region containing the upper left corner.
     *
     * <p>The schema is:
     *
     * <table border=1>
     * <tr>
     * <td>11</td><td>12</td>
     * </tr>
     * <tr>
     * <td>21</td><td>22</td>
     * </tr>
     * </table>
     */
    @Test
    public void testReadFromRegion11() throws IOException {
        JGrassRegion r = new JGrassRegion(1640590.0, 1640830.0, 5139840.0, 5140080.0, 30.0, 30.0);
        GridCoverage2D gc = read(pitFile, r, crs);

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.differentRegion4, 0);
    }

    /** Read the whole Image with a different resolution than the original map. */
    @Test
    public void testDifferentResolution() throws IOException {
        JGrassRegion r = new JGrassRegion(1640650.0, 1640950.0, 5139780.0, 5140020.0, 60.0, 60.0);
        GridCoverage2D gc = read(pitFile, r, crs);

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.matrixDifferentResolution, 0);
    }

    @Test
    public void testRasterReaderBoundsOnly() throws Exception {
        double[][] mapData = { //
            {1000, 1000, 1200, 1250, 1300, 1350, 1450}, //
            {750, 850, 860, 900, 1000, 1200, 1250}, //
            {700, 750, 800, 850, 900, 1000, 1100}, //
            {650, 700, 750, 800, 850, 490, 450}, //
            {430, 500, 600, 700, 800, 500, 450}, //
            {700, 750, 760, 770, 850, 1000, 1150} //
        };

        double n = 5140020.0;
        double s = 5139840.0;
        double w = 1640710.0;
        double e = 1640920.0;
        int cols = 7;
        int rows = 6;

        GridCoverage2D readCoverage = readFile(grassFile, cols, rows, n, s, w, e, crs32632);
        checkMatrixEqual(readCoverage.getRenderedImage(), mapData, 0);
    }

    @Test
    public void testRasterReaderResOnly() throws Exception {
        double[][] mapData = { //
            {800.0, 1000.0, 1200.0, 1300.0, 1450.0}, //
            {500.0, 700.0, 800.0, 900.0, 1100.0}, //
            {450.0, 430.0, 600.0, 800.0, 450.0}, //
            {600.0, 750.0, 780.0, 1000.0, 1250.0} //
        };
        double n = 5140020.0;
        double s = 5139780.0;
        double w = 1640650.0;
        double e = 1640950.0;
        double xres = 60.0;
        double yres = 60.0;
        JGrassRegion r = new JGrassRegion(w, e, s, n, xres, yres);
        GridCoverage2D readCoverage = read(grassFile, r, crs32632);
        checkMatrixEqual(readCoverage.getRenderedImage(), mapData, 0);
    }

    @Test
    public void testRasterReaderBoundsAndRes() throws Exception {
        double[][] mapData = { //
            {1000.0, 1200.0, 1250.0, 1300.0, 1450.0}, //
            {700.0, 800.0, 850.0, 900.0, 1100.0}, //
            {650.0, 750.0, 800.0, 850.0, 450.0}, //
            {700.0, 760.0, 770.0, 850.0, 1150.0} //
        };

        double n = 5140020.0;
        double s = 5139840.0;
        double w = 1640710.0;
        double e = 1640920.0;
        double xres = 45.0;
        double yres = 45.0;
        JGrassRegion r = new JGrassRegion(w, e, s, n, xres, yres);
        GridCoverage2D readCoverage = read(grassFile, r, crs32632);
        checkMatrixEqual(readCoverage.getRenderedImage(), mapData, 0);
    }

    private GridCoverage2D read(File file, JGrassRegion r, CoordinateReferenceSystem crs) throws IOException {
        GeneralParameterValue[] readParams = new GeneralParameterValue[1];
        Parameter<GridGeometry2D> readGG = new Parameter<>(AbstractGridFormat.READ_GRIDGEOMETRY2D);
        GridEnvelope2D gridEnvelope = new GridEnvelope2D(0, 0, r.getCols(), r.getRows());
        ReferencedEnvelope env = new ReferencedEnvelope(r.getWest(), r.getEast(), r.getSouth(), r.getNorth(), crs);
        readGG.setValue(new GridGeometry2D(gridEnvelope, env));
        readParams[0] = readGG;

        AbstractGridFormat format = new GrassCoverageFormatFactory().createFormat();
        GridCoverageReader reader = format.getReader(file);
        GridCoverage2D gc = (GridCoverage2D) reader.read(readParams);
        return gc;
    }

    private GridCoverage2D readFile(
            File file, int cols, int rows, double n, double s, double w, double e, CoordinateReferenceSystem crs)
            throws Exception {
        // JGrassRegion jgr = new JGrassRegion(w, e, s, n, rows, cols);
        // return read(file, jgr, crs);

        // readgrassraster start

        /*
         * read a grassraster given the bounds and the number of rows and cols.
         */
        // prepare the parameters
        GeneralParameterValue[] readParams = new GeneralParameterValue[1];
        Parameter<GridGeometry2D> readGG = new Parameter<>(AbstractGridFormat.READ_GRIDGEOMETRY2D);
        GridEnvelope2D gridEnvelope = new GridEnvelope2D(0, 0, cols, rows);
        ReferencedEnvelope env = new ReferencedEnvelope(w, e, s, n, crs);
        readGG.setValue(new GridGeometry2D(gridEnvelope, env));
        readParams[0] = readGG;
        // do the reading
        AbstractGridFormat format = new GrassCoverageFormatFactory().createFormat();
        GridCoverageReader reader = format.getReader(file);
        GridCoverage2D gc = (GridCoverage2D) reader.read(readParams);

        // readgrassraster stop
        return gc;
    }

    protected void checkMatrixEqual(RenderedImage image, double[][] matrix, double delta) {
        RectIter rectIter = RectIterFactory.create(image, null);
        int y = 0;
        do {
            int x = 0;
            do {
                double value = rectIter.getSampleDouble();
                double expectedResult = matrix[y][x];
                if (Double.isNaN(value)) {
                    Assert.assertTrue(x + " " + y, Double.isNaN(expectedResult));
                } else {
                    Assert.assertEquals(x + " " + y, expectedResult, value, delta);
                }
                x++;
            } while (!rectIter.nextPixelDone());
            rectIter.startPixels();
            y++;
        } while (!rectIter.nextLineDone());
    }
}
