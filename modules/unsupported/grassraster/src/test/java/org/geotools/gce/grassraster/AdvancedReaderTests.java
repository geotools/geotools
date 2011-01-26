package org.geotools.gce.grassraster;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.media.jai.Interpolation;
import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;

import junit.framework.TestCase;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DataUtilities;
import org.geotools.gce.grassraster.format.GrassCoverageFormatFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Reader tests for different active redgion cases.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class AdvancedReaderTests extends TestCase {

    private double n = 5140020.0;
    private double s = 5139780.0;
    private double w = 1640650.0;
    private double e = 1640950.0;
    private double xres = 30.0;
    private double yres = 30.0;
    private int rows = 8;
    private int cols = 10;
    private CoordinateReferenceSystem crs = null;

    private File pitFile;

    protected void setUp() throws Exception {
        URL pitUrl = this.getClass().getClassLoader().getResource("testlocation/test/cell/pit");
        pitFile = DataUtilities.urlToFile(pitUrl);
        crs = CRS.decode("EPSG:3004");
    }

    /**
     * Read the whole Image (at file region and resolution)
     * 
     * @throws IOException
     */
    public void testReadFromFileRegion() throws IOException {
        GeneralParameterValue[] readParams = new GeneralParameterValue[1];
        Parameter<GridGeometry2D> readGG = new Parameter<GridGeometry2D>(
                AbstractGridFormat.READ_GRIDGEOMETRY2D);
        GridEnvelope2D gridEnvelope = new GridEnvelope2D(0, 0, cols, rows);
        ReferencedEnvelope env = new ReferencedEnvelope(w, e, s, n, crs);
        readGG.setValue(new GridGeometry2D(gridEnvelope, env));
        readParams[0] = readGG;

        AbstractGridFormat format = (AbstractGridFormat) new GrassCoverageFormatFactory()
                .createFormat();
        GridCoverageReader reader = format.getReader(pitFile);
        GridCoverage2D gc = ((GridCoverage2D) reader.read(readParams));

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.matrix, 0);
    }

    /**
    * Read a region which is bigger, in all direction, than the file region.
    *
    * @throws IOException
    */

    public void testReadFromWrappingRegion() throws IOException {
        JGrassRegion r = new JGrassRegion(1640590.0, 1641010.0, 5139720.0, 5140080.0, 30.0, 30.0);

        GridCoverage2D gc = read(r);

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.matrixMore, 0);

    }

    /**
    * Read a region which dimension is smaller and completely contained
    * in thefile region.
    *
    * @throws IOException
    */
    public void testReadFromContainedRegion() throws IOException {
        JGrassRegion r = new JGrassRegion(1640710.0, 1640890.0, 5139840.0, 5139960.0, 30.0, 30.0);

        GridCoverage2D gc = read(r);

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.matrixLess, 0);
    }

    /**
    * Test the reading of the map in a region containing the upper right
    * corner.
    *
    * The schema is:
    * <table border=1>
    * <tr>
    * <td>11</td><td>12</td>
    * </tr>
    * <tr>
    * <td>21</td><td>22</td>
    * </tr>
    * </table>
    *
    * @throws IOException
    */
    public void testReadFromRegion12() throws IOException {
        JGrassRegion r = new JGrassRegion(1640710.0, 1641010.0, 5139840.0, 5140080.0, 30.0, 30.0);
        GridCoverage2D gc = read(r);

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.differentRegion1, 0);
    }

    /**
    * Test the reading of the map in a region containing the lower left
    * corner.
    *
    * The schema is:
    * <table border=1>
    * <tr>
    * <td>11</td><td>12</td>
    * </tr>
    * <tr>
    * <td>21</td><td>22</td>
    * </tr>
    * </table>
    *
    * @throws IOException
    */
    public void testReadFromRegion21() throws IOException {
        JGrassRegion r = new JGrassRegion(1640590.0, 1640890.0, 5139720.0, 5139960.0, 30.0, 30.0);
        GridCoverage2D gc = read(r);

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.differentRegion2, 0);
    }

    /**
    * Test the reading of the map in a region containing the lower right
    * corner.
    *
    * The schema is:
    * <table border=1>
    * <tr>
    * <td>11</td><td>12</td>
    * </tr>
    * <tr>
    * <td>21</td><td>22</td>
    * </tr>
    * </table>
    *
    * @throws IOException
    */
    public void testReadFromRegion22() throws IOException {
        JGrassRegion r = new JGrassRegion(1640710.0, 1641010.0, 5139720.0, 5139960.0, 30.0, 30.0);
        GridCoverage2D gc = read(r);

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.differentRegion3, 0);
    }

    /**
    * Test the reading of the map in a region containing the upper left
    * corner.
    *
    * The schema is:
    * <table border=1>
    * <tr>
    * <td>11</td><td>12</td>
    * </tr>
    * <tr>
    * <td>21</td><td>22</td>
    * </tr>
    * </table>
    *
    * @throws IOException
    */
    public void testReadFromRegion11() throws IOException {
        JGrassRegion r = new JGrassRegion(1640590.0, 1640830.0, 5139840.0, 5140080.0, 30.0, 30.0);
        GridCoverage2D gc = read(r);

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.differentRegion4, 0);
    }

    /**
    * Read the whole Image with a different resolution than
    * the original map.
    *
    * @throws IOException
    */
    public void testDifferentResolution() throws IOException {
        JGrassRegion r = new JGrassRegion(1640650.0, 1640950.0, 5139780.0, 5140020.0, 60.0, 60.0);
        GridCoverage2D gc = read(r);

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.matrixDifferentResolution, 0);

    }

    /**
    * Test for the nearster neighbour.
    */
    public void testDifferentResolution2() throws IOException {
        JGrassRegion r = new JGrassRegion(1640650.0, 1640950.0, 5139780.0, 5140020.0, 40.0, 40.0);
        GridCoverage2D gc = read(r);

        checkMatrixEqual(gc.getRenderedImage(), TestMaps.matrixDifferentResolution2, 0);

    }

    private GridCoverage2D read( JGrassRegion r ) throws IOException {
        GeneralParameterValue[] readParams = new GeneralParameterValue[1];
        Parameter<GridGeometry2D> readGG = new Parameter<GridGeometry2D>(
                AbstractGridFormat.READ_GRIDGEOMETRY2D);
        GridEnvelope2D gridEnvelope = new GridEnvelope2D(0, 0, r.getCols(), r.getRows());
        ReferencedEnvelope env = new ReferencedEnvelope(r.getWest(), r.getEast(), r.getSouth(), r
                .getNorth(), crs);
        readGG.setValue(new GridGeometry2D(gridEnvelope, env));
        readParams[0] = readGG;

        AbstractGridFormat format = (AbstractGridFormat) new GrassCoverageFormatFactory()
                .createFormat();
        GridCoverageReader reader = format.getReader(pitFile);
        GridCoverage2D gc = ((GridCoverage2D) reader.read(readParams));
        return gc;
    }

    protected void checkMatrixEqual( RenderedImage image, double[][] matrix, double delta ) {
        RectIter rectIter = RectIterFactory.create(image, null);
        int y = 0;
        do {
            int x = 0;
            do {
                double value = rectIter.getSampleDouble();
                double expectedResult = matrix[y][x];
                if (Double.isNaN(value)) {
                    assertTrue(x + " " + y, Double.isNaN(expectedResult));
                } else {
                    assertEquals(x + " " + y, expectedResult, value, delta);
                }
                x++;
            } while( !rectIter.nextPixelDone() );
            rectIter.startPixels();
            y++;
        } while( !rectIter.nextLineDone() );
    }

}
