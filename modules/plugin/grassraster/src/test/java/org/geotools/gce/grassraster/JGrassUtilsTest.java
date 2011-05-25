package org.geotools.gce.grassraster;

import java.awt.image.RenderedImage;
import java.io.IOException;

import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;

import junit.framework.TestCase;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Test the {@link JGrassMapEnvironment} class and the created paths.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/grassraster/src/test/java/org/geotools/gce/grassraster/JGrassUtilsTest.java $
 */
@SuppressWarnings("nls")
public class JGrassUtilsTest extends TestCase {

    public void testScaling() throws IOException, NoSuchAuthorityCodeException, FactoryException {
        double[][] mapData = new double[][]{//
        {1000.0, 1000.0, 1200.0, 1250.0, 1300.0, 1350.0, 1450.0}, //
                {750.0, 850.0, 860.0, 900.0, 1000.0, 1200.0, 1250.0}, //
                {700.0, 750.0, 800.0, 850.0, 900.0, 1000.0, 1100.0}, //
                {650.0, 700.0, 750.0, 800.0, 850.0, 490.0, 450.0}, //
                {430.0, 500.0, 600.0, 700.0, 800.0, 500.0, 450.0}, //
                {700.0, 750.0, 760.0, 770.0, 850.0, 1000.0, 1150.0} //
        };

        double[][] mapDataAfter = new double[][]{//
        {1000.0, 1200.0, 1250.0, 1300.0, 1450.0}, //
                {700.0, 800.0, 850.0, 900.0, 1100.0}, //
                {650.0, 750.0, 800.0, 850.0, 450.0}, //
                {700.0, 760.0, 770.0, 850.0, 1150.0} //
        };

        double n = 5140020.0;
        double s = 5139840.0;
        double w = 1640710.0;
        double e = 1640920.0;
        CoordinateReferenceSystem crs = CRS.decode("EPSG:32632");
        GridCoverage2D elevationCoverage = JGrassUtilities.buildCoverage("elevation", mapData, n, s, w, e, crs, true);

        RenderedImage scaledJAIImage = JGrassUtilities.scaleJAIImage(5, 4, elevationCoverage.getRenderedImage(), null);
        checkMatrixEqual(scaledJAIImage, mapDataAfter, 0.0);
    }

    // public void testScaling2() throws IOException, NoSuchAuthorityCodeException, FactoryException
    // {
    // double[][] mapData = new double[][]{//
    // {1000.0, 1000.0, 1200.0, 1250.0, 1300.0, 1350.0, 1450.0}, //
    // {750.0, 850.0, 860.0, 900.0, 1000.0, 1200.0, 1250.0}, //
    // {700.0, 750.0, 800.0, 850.0, 900.0, 1000.0, 1100.0}, //
    // {650.0, 700.0, 750.0, 800.0, 850.0, 490.0, 450.0}, //
    // {430.0, 500.0, 600.0, 700.0, 800.0, 500.0, 450.0}, //
    // {700.0, 750.0, 760.0, 770.0, 850.0, 1000.0, 1150.0} //
    // };
    //
    // double[][] mapDataAfter = new double[][]{//
    // {1000.0, 1200.0, 1250.0, 1300.0, 1450.0}, //
    // {750.0, 860.0, 900.0, 1000.0, 1250.0}, //
    // {700.0, 800.0, 850.0, 900.0, 1100.0}, //
    // // {650.0, 750.0, 800.0, 850.0, 450.0}, //
    // {430.0, 600.0, 700.0, 800.0, 450.0}, //
    // {700.0, 760.0, 770.0, 850.0, 1150.0} //
    // };
    //
    // double n = 5140020.0;
    // double s = 5139840.0;
    // double w = 1640710.0;
    // double e = 1640920.0;
    // CoordinateReferenceSystem crs = CRS.decode("EPSG:32632");
    // GridCoverage2D elevationCoverage = JGrassUtilities.buildCoverage("elevation", mapData, n, s,
    // w, e, crs, true);
    //
    // // JGrassUtilities.printImage(elevationCoverage);
    // RenderedImage scaledJAIImage = JGrassUtilities.scaleJAIImage(5, 5,
    // elevationCoverage.getRenderedImage(), null);
    // // System.out.println("***************************");
    //
    // // JGrassUtilities.printImage(scaledJAIImage);
    // // TODO: got 700 (rather than 650) on mac osx
    // checkMatrixEqual(scaledJAIImage, mapDataAfter, 0.0);
    // }

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
