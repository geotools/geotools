/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import org.eclipse.imagen.ImageLayout;
import org.eclipse.imagen.ImageN;
import org.eclipse.imagen.ROI;
import org.eclipse.imagen.ROIShape;
import org.eclipse.imagen.RenderedOp;
import org.eclipse.imagen.media.mosaic.MosaicDescriptor;
import org.eclipse.imagen.media.range.Range;
import org.eclipse.imagen.media.range.RangeFactory;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Test;

/** Checks the mosaic over nodata-crop reduction: same pixels, one operation less. */
public class MosaicCropReductionTest {

    // the source is larger than the crop, otherwise ImageWorker.crop eliminates the operation
    private static final int SRC_W = 140;
    private static final int SRC_H = 120;
    private static final int W = 100;
    private static final int H = 80;
    private static final double FILL = 255;
    private static final Rectangle FULL = new Rectangle(0, 0, W, H);
    private static final Range NODATA = RangeFactory.create((byte) 0, true, (byte) 0, true);

    /** Thresholds of the outer mosaic, most tests do not set any. */
    private double[][] thresholds;

    @After
    public void restoreFlag() {
        ImageWorker.MOSAIC_CROP_REDUCTION_ENABLED = true;
    }

    /** The crop leaves the chain, and the pixels do not change with it. */
    @Test
    public void testCropFusedIntoMosaic() {
        RenderedImage chained = buildMosaicOverCrop(false, FULL);
        RenderedImage reduced = buildMosaicOverCrop(true, FULL);
        assertThat(firstSource(chained), isOperation("Crop"));

        // no operation took the crop's place, the mosaic reads the source itself, at its own size
        // (a plain BufferedImage source gets wrapped into a rendered image adapter)
        RenderedImage fused = firstSource(reduced);
        assertThat(fused, not(instanceOf(RenderedOp.class)));
        assertSize(fused, SRC_W, SRC_H);
        assertSamePixels(dataOf(chained), dataOf(reduced));
    }

    /** A layout reaching outside the crop must block the reduction, the crop bounds still matter. */
    @Test
    public void testLayoutWiderThanCropNotReduced() {
        RenderedImage wider = buildMosaicOverCrop(true, new Rectangle(0, 0, W + 20, H + 20));
        RenderedImage kept = firstSource(wider);
        assertThat(kept, isOperation("Crop"));
        assertSize(kept, W, H);
    }

    /**
     * The inner cropping operation is a single source Mosaic, which is what
     * {@link org.geotools.coverage.processing.operation.Crop} builds when nodata is involved.
     */
    @Test
    public void testMosaicOverMosaicReduced() {
        // reduction not enabled, mosaic over mosaic
        RenderedImage chained = buildMosaicOverMosaic(false, null);
        assertThat(firstSource(chained), isOperation("Mosaic"));

        // reduction enabled, single mosaic over bufferd image
        RenderedImage reduced = buildMosaicOverMosaic(true, null);
        RenderedImage fused = firstSource(reduced);
        assertThat(fused, instanceOf(BufferedImage.class));
        assertSize(fused, SRC_W, SRC_H);

        // however, the pixels in output are the same for the reduced and non reduced case
        assertSamePixels(dataOf(chained), dataOf(reduced));
    }

    /**
     * A ROI on the Crop does not block the reduction: the operation reduces the ROI to its bounding box, so it can only
     * shrink the crop bounds, never mask pixels inside them, and the bounds are checked already. A triangle whose
     * bounds cover the crop therefore masks nothing at all.
     */
    @Test
    public void testCropWithRoiReduced() {
        ROI roi = new ROIShape(new Polygon(new int[] {0, W, 0}, new int[] {0, 0, H}, 3));
        RenderedImage chained = buildMosaicOverCrop(false, FULL, roi);
        RenderedImage reduced = buildMosaicOverCrop(true, FULL, roi);
        assertThat(firstSource(chained), isOperation("Crop"));
        assertThat(firstSource(reduced), not(instanceOf(RenderedOp.class)));
        assertSamePixels(dataOf(chained), dataOf(reduced));
    }

    /**
     * The fusion works because the crop advertises its nodata range as a GC_NODATA property, so the outer mosaic was
     * already skipping the fill. Checked with a range wider than the fill, and hole pixels that differ from it, so the
     * two would part ways if that stopped holding.
     */
    @Test
    public void testRangeNoDataWiderThanFill() {
        Range wide = RangeFactory.create((byte) 0, true, (byte) 10, true);
        Raster chained = dataOf(buildMosaicOverCrop(false, FULL, null, wide, 7));
        Raster reduced = dataOf(buildMosaicOverCrop(true, FULL, null, wide, 7));
        assertSamePixels(chained, reduced);
    }

    /** An inner mosaic carrying its own ROI is masking, not cropping: it must be left alone. */
    @Test
    public void testInnerMosaicWithRoiNotReduced() {
        ROI roi = new ROIShape(new Rectangle(5, 5, 50, 40));
        RenderedImage kept = firstSource(buildMosaicOverMosaic(true, roi));
        assertThat(kept, isOperation("Mosaic"));
        assertSize(kept, W, H);
    }

    /**
     * The outer mosaic carrying thresholds must still honour the nodata range the fusion moves into it: thresholds only
     * stand in for a nodata that is not there.
     */
    @Test
    public void testMosaicThresholdsDoNotHideMovedNoData() {
        thresholds = new double[][] {{0}};
        Raster chained = dataOf(buildMosaicOverCrop(false, FULL));
        Raster reduced = dataOf(buildMosaicOverCrop(true, FULL));
        assertSamePixels(chained, reduced);
        // the fill really is there, the nodata block of the source is not showing through
        assertEquals(FILL, reduced.getSampleDouble(70, 20, 0), 0d);
    }

    /** Crop operation cropping with nodata, as the render chain builds it, then the mosaic over it. */
    private RenderedImage buildMosaicOverCrop(boolean reductionEnabled, Rectangle layoutBounds) {
        return buildMosaicOverCrop(reductionEnabled, layoutBounds, null, NODATA, 0);
    }

    private RenderedImage buildMosaicOverCrop(boolean reductionEnabled, Rectangle layoutBounds, ROI cropRoi) {
        return buildMosaicOverCrop(reductionEnabled, layoutBounds, cropRoi, NODATA, 0);
    }

    private RenderedImage buildMosaicOverCrop(
            boolean reductionEnabled, Rectangle layoutBounds, ROI cropRoi, Range cropNoData, int holeValue) {
        ImageWorker.MOSAIC_CROP_REDUCTION_ENABLED = reductionEnabled;
        int validFloor = cropNoData.getMax().intValue() + 1;
        ImageWorker crop = new ImageWorker(source(holeValue, validFloor));
        crop.setNoData(cropNoData);
        crop.setBackground(new double[] {FILL});
        crop.setROI(cropRoi);
        crop.crop(0, 0, W, H);
        return terminalMosaic(crop.getRenderedImage(), layoutBounds);
    }

    /** Single source Mosaic cropping through its layout, then the mosaic over it. */
    private RenderedImage buildMosaicOverMosaic(boolean reductionEnabled, ROI innerRoi) {
        ImageWorker.MOSAIC_CROP_REDUCTION_ENABLED = reductionEnabled;
        RenderedImage inner = singleSourceMosaic(source(0, 1), FULL, new double[] {FILL}, innerRoi, NODATA, null);
        return terminalMosaic(inner, FULL);
    }

    /** The mosaic the reduction acts on: three band background, a ROI, no nodata of its own. */
    private RenderedImage terminalMosaic(RenderedImage source, Rectangle layoutBounds) {
        double[] background = {FILL, FILL, FILL};
        ROI roi = new ROIShape(new Rectangle(10, 10, 60, 50));
        return singleSourceMosaic(source, layoutBounds, background, roi, null, thresholds);
    }

    /** OVERLAY mosaic with the layout pinning its bounds, the only shape these tests build. */
    private RenderedImage singleSourceMosaic(
            RenderedImage source,
            Rectangle layoutBounds,
            double[] background,
            ROI roi,
            Range nodata,
            double[][] thresholds) {
        ImageWorker iw = new ImageWorker(source);
        iw.setRenderingHint(ImageN.KEY_IMAGE_LAYOUT, layout(layoutBounds));
        iw.setBackground(background);
        iw.mosaic(
                new RenderedImage[] {source},
                MosaicDescriptor.MOSAIC_TYPE_OVERLAY,
                null,
                roi == null ? null : new ROI[] {roi},
                thresholds,
                nodata == null ? null : new Range[] {nodata});
        return iw.getRenderedImage();
    }

    private ImageLayout layout(Rectangle bounds) {
        ImageLayout layout = new ImageLayout(bounds.x, bounds.y, bounds.width, bounds.height);
        layout.setTileWidth(32);
        layout.setTileHeight(32);
        return layout;
    }

    /** Matches an image that is the given image processing operation. */
    private static Matcher<RenderedImage> isOperation(String operation) {
        return new TypeSafeMatcher<>() {

            @Override
            protected boolean matchesSafely(RenderedImage image) {
                return image instanceof RenderedOp op && operation.equals(op.getOperationName());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("the ").appendText(operation).appendText(" operation");
            }

            @Override
            protected void describeMismatchSafely(RenderedImage image, Description mismatch) {
                mismatch.appendText(
                        image instanceof RenderedOp op
                                ? "the " + op.getOperationName() + " operation"
                                : "no operation, " + image.getClass().getSimpleName());
            }
        };
    }

    private RenderedImage firstSource(RenderedImage mosaic) {
        return (RenderedImage) ((RenderedOp) mosaic).getSources().get(0);
    }

    private void assertSize(RenderedImage image, int width, int height) {
        assertEquals(width, image.getWidth());
        assertEquals(height, image.getHeight());
    }

    private Raster dataOf(RenderedImage image) {
        return image.getData(FULL);
    }

    private void assertSamePixels(Raster expected, Raster actual) {
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                for (int b = 0; b < 3; b++) {
                    assertEquals(
                            "pixel " + x + "," + y + " band " + b,
                            expected.getSample(x, y, b),
                            actual.getSample(x, y, b));
                }
            }
        }
    }

    /**
     * Three band byte image with a uniform {@code holeValue} block standing in for nodata. Every other sample is kept
     * at or above {@code validFloor}, so only the block matches the nodata range under test.
     */
    private RenderedImage source(int holeValue, int validFloor) {
        BufferedImage bi = new BufferedImage(SRC_W, SRC_H, BufferedImage.TYPE_3BYTE_BGR);
        for (int y = 0; y < SRC_H; y++) {
            for (int x = 0; x < SRC_W; x++) {
                boolean nodata = x >= 60 && x < 90 && y >= 10 && y < 50;
                int v = (x + y) % 256;
                for (int b = 0; b < 3; b++) {
                    int sample = nodata ? holeValue : validFloor + (v + 30 * b) % (256 - validFloor);
                    bi.getRaster().setSample(x, y, b, sample);
                }
            }
        }
        return bi;
    }
}
