package org.geotools.renderer.lite;

import static org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.WarpBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

public class WarpBuilderTest {
    @BeforeClass
    public static void setupClass() {
        MapProjection.SKIP_SANITY_CHECKS = true;
    }

    @Test
    public void testUTM32N() throws FactoryException, TransformException {
        CoordinateReferenceSystem utm32n = CRS.decode("EPSG:32632", true);
        Rectangle screen = new Rectangle(0, 0, 512, 512);
        assertRowCols(
                new ReferencedEnvelope(9 - 40, 9, 0, 40, WGS84),
                utm32n,
                screen,
                new int[] {32, 32});
        assertRowCols(
                new ReferencedEnvelope(9 - 20, 9, 0, 20, WGS84), utm32n, screen, new int[] {8, 16});
        assertRowCols(
                new ReferencedEnvelope(9 - 10, 9, 0, 10, WGS84), utm32n, screen, new int[] {4, 8});
        assertRowCols(
                new ReferencedEnvelope(9 - 5, 9, 0, 5, WGS84), utm32n, screen, new int[] {2, 2});
        assertRowCols(
                new ReferencedEnvelope(9 - 2, 9, 0, 2, WGS84), utm32n, screen, new int[] {1, 1});
    }

    @Test
    public void testPolarStereo() throws FactoryException, TransformException {
        CoordinateReferenceSystem polar = CRS.decode("EPSG:3031", true);
        Rectangle screen = new Rectangle(0, 0, 512, 512);
        assertRowCols(
                new ReferencedEnvelope(-10, 10, -90, -85, WGS84),
                polar,
                screen,
                new int[] {16, 16});
        assertRowCols(
                new ReferencedEnvelope(-10, 10, -90, -70, WGS84),
                polar,
                screen,
                new int[] {32, 16});
        assertRowCols(
                new ReferencedEnvelope(-10, 10, -90, -45, WGS84), polar, screen, new int[] {64, 8});
        assertRowCols(
                new ReferencedEnvelope(-10, 10, -90, 0, WGS84), polar, screen, new int[] {128, 8});
        assertRowCols(
                new ReferencedEnvelope(80, 110, -90, 0, WGS84), polar, screen, new int[] {32, 32});
        assertRowCols(
                new ReferencedEnvelope(-110, -80, -90, 0, WGS84),
                polar,
                screen,
                new int[] {32, 32});
    }

    private void assertRowCols(
            ReferencedEnvelope sourceEnvelope,
            CoordinateReferenceSystem targetCRS,
            Rectangle screen,
            int[] expectedSplit)
            throws TransformException, FactoryException {
        ReferencedEnvelope targetEnvelope = sourceEnvelope.transform(targetCRS, true);
        AffineTransform at = RendererUtilities.worldToScreenTransform(targetEnvelope, screen);
        MathTransform2D crsTransform =
                (MathTransform2D)
                        CRS.findMathTransform(
                                CRS.getHorizontalCRS(sourceEnvelope.getCoordinateReferenceSystem()),
                                CRS.getHorizontalCRS(targetCRS));
        MathTransform2D screenTransform = new AffineTransform2D(at);
        MathTransform2D fullTranform =
                (MathTransform2D) ConcatenatedTransform.create(crsTransform, screenTransform);
        Rectangle2D.Double sourceDomain =
                new Rectangle2D.Double(
                        sourceEnvelope.getMinX(),
                        sourceEnvelope.getMinY(),
                        sourceEnvelope.getWidth(),
                        sourceEnvelope.getHeight());
        WarpBuilder wb = new WarpBuilder(0.8);
        int[] actualSplit = wb.getRowColsSplit(fullTranform, sourceDomain);
        Assert.assertArrayEquals(expectedSplit, actualSplit);
    }
}
