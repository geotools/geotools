/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014 TOPP - www.openplans.org.
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
package org.geotools.processing.jai;

import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.util.ArrayList;
import java.util.List;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import javax.media.jai.operator.TranslateDescriptor;

import org.geotools.processing.jai.nodata.Range;
import org.geotools.processing.jai.nodata.RangeFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.media.jai.util.ImageUtil;

/**
 * This test class checks if the BandMergeOpImage is able to merge various multibanded images into a single multibanded image. The test is made by
 * taking 4 images for each JAI allowed data type, and then passing them to the BandMerge operator. The test ensures that the final image band number
 * is equal to the total sources band number and each sample value is equal to that of the related source image band. If No Data are present, then the
 * test check if the every No Data value is set to the "destination No Data value".
 * 
 * @author Nicola Lagomarsini, GeoSolutions S.A.S.
 * 
 */

public class BandMergeTest {

    /** Constant indicating the image height */
    private final static int IMAGE_HEIGHT = 128;

    /** Constant indicating the image width */
    private final static int IMAGE_WIDTH = 128;

    /** Constant indicating the final number of bands for each merged image */
    private final static int BAND_NUMBER = 4;

    /** Tolerance value used for double comparison */
    private final static double TOLERANCE = 0.1d;

    private static final int ROI_WIDTH = 40;

    private static final int ROI_HEIGHT = 40;

    /** RenderedImage array used for performing the tests */
    private static RenderedImage[][] images;

    /** No Data Range array for Byte images */
    private static Range[] noDataByte;

    /** No Data Range array for Unsigned Short images */
    private static Range[] noDataUShort;

    /** No Data Range array for Short images */
    private static Range[] noDataShort;

    /** No Data Range array for Integer images */
    private static Range[] noDataInt;

    /** No Data Range array for Float images */
    private static Range[] noDataFloat;

    /** No Data Range array for Double images */
    private static Range[] noDataDouble;

    /** Double value used for destination No Data */
    private static double destNoData;

    /** ROI to use */
    private static ROI roiData;

    @BeforeClass
    public static void initialSetup() {
        // Creation of all the image array
        // The 6 inner arrays contain 4 images to merge
        // Each inner array is associated with a different data type
        images = new RenderedImage[6][4];

        byte noDataB = 50;
        short noDataS = 50;
        int noDataI = 50;
        float noDataF = 50;
        double noDataD = 50;

        for (int band = 0; band < BAND_NUMBER; band++) {
            images[DataBuffer.TYPE_BYTE][band] = createTestImage(DataBuffer.TYPE_BYTE, IMAGE_WIDTH,
                    IMAGE_HEIGHT, noDataB, 1);
            images[DataBuffer.TYPE_USHORT][band] = createTestImage(DataBuffer.TYPE_USHORT,
                    IMAGE_WIDTH, IMAGE_HEIGHT, noDataS, 1);
            images[DataBuffer.TYPE_SHORT][band] = createTestImage(DataBuffer.TYPE_SHORT,
                    IMAGE_WIDTH, IMAGE_HEIGHT, noDataS, 1);
            images[DataBuffer.TYPE_INT][band] = createTestImage(DataBuffer.TYPE_INT, IMAGE_WIDTH,
                    IMAGE_HEIGHT, noDataI, 1);
            images[DataBuffer.TYPE_FLOAT][band] = createTestImage(DataBuffer.TYPE_FLOAT,
                    IMAGE_WIDTH, IMAGE_HEIGHT, noDataF, 1);
            images[DataBuffer.TYPE_DOUBLE][band] = createTestImage(DataBuffer.TYPE_DOUBLE,
                    IMAGE_WIDTH, IMAGE_HEIGHT, noDataD, 1);
        }

        // No Data Ranges
        boolean minIncluded = true;
        boolean maxIncluded = true;

        noDataByte = new Range[] { RangeFactory.create(noDataB, minIncluded, noDataB, maxIncluded) };
        noDataUShort = new Range[] { RangeFactory.createU(noDataS, minIncluded, noDataS,
                maxIncluded) };
        noDataShort = new Range[] { RangeFactory.create(noDataS, minIncluded, noDataS, maxIncluded) };
        noDataInt = new Range[] { RangeFactory.create(noDataI, minIncluded, noDataI, maxIncluded) };
        noDataFloat = new Range[] { RangeFactory.create(noDataF, minIncluded, noDataF, maxIncluded,
                true) };
        noDataDouble = new Range[] { RangeFactory.create(noDataD, minIncluded, noDataD,
                maxIncluded, true) };

        // Destination No Data
        destNoData = 100;

        // ROI
        roiData = new ROIShape(new Rectangle(0, 0, ROI_WIDTH, ROI_HEIGHT));
    }

    @Test
    public void testBandMerge() {
        // This test checks the BandMerge operation on all the possible data types without No Data
        boolean noDataUsed = false;
        boolean roiUsed = false;

        testBandMerge(images[0], noDataUsed, roiUsed);
        testBandMerge(images[1], noDataUsed, roiUsed);
        testBandMerge(images[2], noDataUsed, roiUsed);
        testBandMerge(images[3], noDataUsed, roiUsed);
        testBandMerge(images[4], noDataUsed, roiUsed);
        testBandMerge(images[5], noDataUsed, roiUsed);
    }

    @Test
    public void testBandMergeNoData() {
        // This test checks the BandMerge operation on all the possible data types with No Data
        boolean noDataUsed = true;
        boolean roiUsed = false;

        testBandMerge(images[0], noDataUsed, roiUsed);
        testBandMerge(images[1], noDataUsed, roiUsed);
        testBandMerge(images[2], noDataUsed, roiUsed);
        testBandMerge(images[3], noDataUsed, roiUsed);
        testBandMerge(images[4], noDataUsed, roiUsed);
        testBandMerge(images[5], noDataUsed, roiUsed);
    }

    @Test
    public void testBandMergeROI() {
        // This test checks the BandMerge operation on all the possible data types with ROI
        boolean noDataUsed = false;
        boolean roiUsed = true;

        testBandMerge(images[0], noDataUsed, roiUsed);
        testBandMerge(images[1], noDataUsed, roiUsed);
        testBandMerge(images[2], noDataUsed, roiUsed);
        testBandMerge(images[3], noDataUsed, roiUsed);
        testBandMerge(images[4], noDataUsed, roiUsed);
        testBandMerge(images[5], noDataUsed, roiUsed);
    }

    @Test
    public void testBandMergeNoDataROI() {
        // This test checks the BandMerge operation on all the possible data types with No Data and ROI
        boolean noDataUsed = true;
        boolean roiUsed = true;

        testBandMerge(images[0], noDataUsed, roiUsed);
        testBandMerge(images[1], noDataUsed, roiUsed);
        testBandMerge(images[2], noDataUsed, roiUsed);
        testBandMerge(images[3], noDataUsed, roiUsed);
        testBandMerge(images[4], noDataUsed, roiUsed);
        testBandMerge(images[5], noDataUsed, roiUsed);
    }

    @Test
    public void testExtendedBandMerge() {
        // This test checks the BandMerge operation on all the possible data types without No Data and ROI
        // Also it tests if the use of AffineTransformations is correct
        boolean noDataUsed = false;
        boolean roiUsed = false;

        testExtendedBandMerge(images[0], noDataUsed, roiUsed);
        testExtendedBandMerge(images[1], noDataUsed, roiUsed);
        testExtendedBandMerge(images[2], noDataUsed, roiUsed);
        testExtendedBandMerge(images[3], noDataUsed, roiUsed);
        testExtendedBandMerge(images[4], noDataUsed, roiUsed);
        testExtendedBandMerge(images[5], noDataUsed, roiUsed);
    }

    @Test
    public void testExtendedBandMergeNoData() {
        // This test checks the BandMerge operation on all the possible data types with No Data
        // Also it tests if the use of AffineTransformations is correct
        boolean noDataUsed = true;
        boolean roiUsed = false;

        testExtendedBandMerge(images[0], noDataUsed, roiUsed);
        testExtendedBandMerge(images[1], noDataUsed, roiUsed);
        testExtendedBandMerge(images[2], noDataUsed, roiUsed);
        testExtendedBandMerge(images[3], noDataUsed, roiUsed);
        testExtendedBandMerge(images[4], noDataUsed, roiUsed);
        testExtendedBandMerge(images[5], noDataUsed, roiUsed);
    }

    @Test
    public void testExtendedBandMergeROI() {
        // This test checks the BandMerge operation on all the possible data types with ROI
        // Also it tests if the use of AffineTransformations is correct
        boolean noDataUsed = false;
        boolean roiUsed = true;

        testExtendedBandMerge(images[0], noDataUsed, roiUsed);
        testExtendedBandMerge(images[1], noDataUsed, roiUsed);
        testExtendedBandMerge(images[2], noDataUsed, roiUsed);
        testExtendedBandMerge(images[3], noDataUsed, roiUsed);
        testExtendedBandMerge(images[4], noDataUsed, roiUsed);
        testExtendedBandMerge(images[5], noDataUsed, roiUsed);
    }

    @Test
    public void testExtendedBandMergeNoDataROI() {
        // This test checks the BandMerge operation on all the possible data types with No Data and ROI
        // Also it tests if the use of AffineTransformations is correct
        boolean noDataUsed = true;
        boolean roiUsed = true;

        testExtendedBandMerge(images[0], noDataUsed, roiUsed);
        testExtendedBandMerge(images[1], noDataUsed, roiUsed);
        testExtendedBandMerge(images[2], noDataUsed, roiUsed);
        testExtendedBandMerge(images[3], noDataUsed, roiUsed);
        testExtendedBandMerge(images[4], noDataUsed, roiUsed);
        testExtendedBandMerge(images[5], noDataUsed, roiUsed);
    }

    @AfterClass
    public static void disposal() {
        // Disposal of the images
        for (int band = 0; band < BAND_NUMBER; band++) {
            ((TiledImage) images[DataBuffer.TYPE_BYTE][band]).dispose();
            ((TiledImage) images[DataBuffer.TYPE_USHORT][band]).dispose();
            ((TiledImage) images[DataBuffer.TYPE_SHORT][band]).dispose();
            ((TiledImage) images[DataBuffer.TYPE_INT][band]).dispose();
            ((TiledImage) images[DataBuffer.TYPE_FLOAT][band]).dispose();
            ((TiledImage) images[DataBuffer.TYPE_DOUBLE][band]).dispose();
        }
    }

    private void testBandMerge(RenderedImage[] sources, boolean noDataUsed, boolean roiUsed) {
        // Optional No Data Range used
        Range[] noData;
        // Source image data type
        int dataType = sources[0].getSampleModel().getDataType();
        // If no Data are present, the No Data Range associated is used
        if (noDataUsed) {

            switch (dataType) {
            case DataBuffer.TYPE_BYTE:
                noData = noDataByte;
                break;
            case DataBuffer.TYPE_USHORT:
                noData = noDataUShort;
                break;
            case DataBuffer.TYPE_SHORT:
                noData = noDataShort;
                break;
            case DataBuffer.TYPE_INT:
                noData = noDataInt;
                break;
            case DataBuffer.TYPE_FLOAT:
                noData = noDataFloat;
                break;
            case DataBuffer.TYPE_DOUBLE:
                noData = noDataDouble;
                break;
            default:
                throw new IllegalArgumentException("Wrong data type");
            }
        } else {
            noData = null;
        }

        // ROI to use
        ROI roi = null;
        if (roiUsed) {
            roi = roiData;
        }

        // BandMerge operation
        RenderedOp merged = BandMergeDescriptor
                .create(noData, destNoData, null, null, roi, sources);
        // Check if the bands number is the same
        assertEquals(BAND_NUMBER, merged.getNumBands());
        // Upper-Left tile indexes
        int minTileX = merged.getMinTileX();
        int minTileY = merged.getMinTileY();
        // Raster object
        Raster upperLeftTile = merged.getTile(minTileX, minTileY);
        // Tile bounds
        int minX = upperLeftTile.getMinX();
        int minY = upperLeftTile.getMinY();
        int maxX = upperLeftTile.getWidth() + minX;
        int maxY = upperLeftTile.getHeight() + minY;
        // Cycle on all the tile Bands
        for (int b = 0; b < BAND_NUMBER; b++) {
            // Selection of the source raster associated with the band
            Raster bandRaster = sources[b].getTile(minTileX, minTileY);
            // Cycle on the y-axis
            for (int x = minX; x < maxX; x++) {
                // Cycle on the x-axis
                for (int y = minY; y < maxY; y++) {
                    // Calculated value
                    double value = upperLeftTile.getSampleDouble(x, y, b);
                    // Old band value
                    double valueOld = bandRaster.getSampleDouble(x, y, 0);

                    // ROI CHECK
                    boolean contained = true;
                    if (roiUsed) {
                        if (!roi.contains(x, y)) {
                            contained = false;
                            // Comparison if the final value is not inside a ROI
                            assertEquals(value, destNoData, TOLERANCE);
                        }
                    }

                    if (contained) {
                        // If no Data are present, no data check is performed
                        if (noDataUsed) {
                            switch (dataType) {
                            case DataBuffer.TYPE_BYTE:
                                byte sampleB = ImageUtil.clampRoundByte(value);
                                byte sampleBOld = ImageUtil.clampRoundByte(valueOld);
                                if (noData[0].contains(sampleBOld)) {
                                    assertEquals(sampleB, destNoData, TOLERANCE);
                                } else {
                                    assertEquals(sampleB, valueOld, TOLERANCE);
                                }
                                break;
                            case DataBuffer.TYPE_USHORT:
                                short sampleUS = ImageUtil.clampRoundUShort(value);
                                short sampleUSOld = ImageUtil.clampRoundUShort(valueOld);
                                if (noData[0].contains(sampleUSOld)) {
                                    assertEquals(sampleUS, destNoData, TOLERANCE);
                                } else {
                                    assertEquals(sampleUS, valueOld, TOLERANCE);
                                }
                                break;
                            case DataBuffer.TYPE_SHORT:
                                short sampleS = ImageUtil.clampRoundShort(value);
                                short sampleSOld = ImageUtil.clampRoundShort(valueOld);
                                if (noData[0].contains(sampleSOld)) {
                                    assertEquals(sampleS, destNoData, TOLERANCE);
                                } else {
                                    assertEquals(sampleS, valueOld, TOLERANCE);
                                }
                                break;
                            case DataBuffer.TYPE_INT:
                                int sampleI = ImageUtil.clampRoundInt(value);
                                int sampleIOld = ImageUtil.clampRoundInt(valueOld);
                                if (noData[0].contains(sampleIOld)) {
                                    assertEquals(sampleI, destNoData, TOLERANCE);
                                } else {
                                    assertEquals(sampleI, valueOld, TOLERANCE);
                                }
                                break;
                            case DataBuffer.TYPE_FLOAT:
                                float sampleF = ImageUtil.clampFloat(value);
                                float sampleFOld = ImageUtil.clampFloat(valueOld);
                                if (noData[0].contains(sampleFOld)) {
                                    assertEquals(sampleF, destNoData, TOLERANCE);
                                } else {
                                    assertEquals(sampleF, valueOld, TOLERANCE);
                                }
                                break;
                            case DataBuffer.TYPE_DOUBLE:
                                if (noData[0].contains(valueOld)) {
                                    assertEquals(value, destNoData, TOLERANCE);
                                } else {
                                    assertEquals(value, valueOld, TOLERANCE);
                                }
                                break;
                            default:
                                throw new IllegalArgumentException("Wrong data type");
                            }
                        } else {
                            // Else a simple value comparison is done
                            assertEquals(value, valueOld, TOLERANCE);
                        }
                    }
                }
            }
        }
        // Disposal of the output image
        merged.dispose();
    }

    // This method is similar to the testBandMerge method but it tests the ExtendedBandMergeOpImage class
    private void testExtendedBandMerge(RenderedImage[] sources, boolean noDataUsed, boolean roiUsed) {
        // Optional No Data Range used
        Range[] noData;
        // Source image data type
        int dataType = sources[0].getSampleModel().getDataType();
        // If no Data are present, the No Data Range associated is used
        if (noDataUsed) {

            switch (dataType) {
            case DataBuffer.TYPE_BYTE:
                noData = noDataByte;
                break;
            case DataBuffer.TYPE_USHORT:
                noData = noDataUShort;
                break;
            case DataBuffer.TYPE_SHORT:
                noData = noDataShort;
                break;
            case DataBuffer.TYPE_INT:
                noData = noDataInt;
                break;
            case DataBuffer.TYPE_FLOAT:
                noData = noDataFloat;
                break;
            case DataBuffer.TYPE_DOUBLE:
                noData = noDataDouble;
                break;
            default:
                throw new IllegalArgumentException("Wrong data type");
            }
        } else {
            noData = null;
        }

        // ROI to use
        ROI roi = null;
        if (roiUsed) {
            roi = roiData;
        }

        // New array ofr the transformed source images
        RenderedOp[] translated = new RenderedOp[sources.length];

        List<AffineTransform> transform = new ArrayList<AffineTransform>();

        for (int i = 0; i < sources.length; i++) {
            // Translation coefficients
            int xTrans = (int) (Math.random() * 10);
            int yTrans = (int) (Math.random() * 10);
            // Translation operation
            AffineTransform tr = AffineTransform.getTranslateInstance(xTrans, yTrans);
            // Addition to the transformations list
            transform.add(tr);
            // Translation of the image
            translated[i] = TranslateDescriptor.create(sources[i], (float) xTrans, (float) yTrans,
                    null, null);
        }
        // Definition of the final image dimensions
        ImageLayout layout = new ImageLayout();
        layout.setMinX(sources[0].getMinX());
        layout.setMinY(sources[0].getMinY());
        layout.setWidth(sources[0].getWidth());
        layout.setHeight(sources[0].getHeight());

        RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);

        // BandMerge operation
        RenderedOp merged = BandMergeDescriptor.create(noData, destNoData, hints, transform, roi,
                translated);

        Assert.assertNotNull(merged.getTiles());
        // Check if the bands number is the same
        assertEquals(BAND_NUMBER, merged.getNumBands());
        // Upper-Left tile indexes
        int minTileX = merged.getMinTileX();
        int minTileY = merged.getMinTileY();
        // Raster object
        Raster upperLeftTile = merged.getTile(minTileX, minTileY);
        // Tile bounds
        int minX = upperLeftTile.getMinX();
        int minY = upperLeftTile.getMinY();
        int maxX = upperLeftTile.getWidth() + minX;
        int maxY = upperLeftTile.getHeight() + minY;

        // Source corners
        final int dstMinX = merged.getMinX();
        final int dstMinY = merged.getMinY();
        final int dstMaxX = merged.getMaxX();
        final int dstMaxY = merged.getMaxY();

        Point2D ptDst = new Point2D.Double(0, 0);
        Point2D ptSrc = new Point2D.Double(0, 0);

        // Cycle on all the tile Bands
        for (int b = 0; b < BAND_NUMBER; b++) {
            RandomIter iter = RandomIterFactory.create(translated[b], null);

            // Source corners
            final int srcMinX = translated[b].getMinX();
            final int srcMinY = translated[b].getMinY();
            final int srcMaxX = translated[b].getMaxX();
            final int srcMaxY = translated[b].getMaxY();

            // Cycle on the y-axis
            for (int x = minX; x < maxX; x++) {
                // Cycle on the x-axis
                for (int y = minY; y < maxY; y++) {
                    // Calculated value
                    double value = upperLeftTile.getSampleDouble(x, y, b);
                    // If the tile pixels are outside the image bounds, then no data is set.
                    if (x < dstMinX || x >= dstMaxX || y < dstMinY || y >= dstMaxY) {
                        value = destNoData;
                    }

                    // Set the x,y destination pixel location
                    ptDst.setLocation(x, y);
                    // Map destination pixel to source pixel
                    transform.get(b).transform(ptDst, ptSrc);
                    // Source pixel indexes
                    int srcX = round(ptSrc.getX());
                    int srcY = round(ptSrc.getY());

                    double valueOld = destNoData;

                    // Check if the pixel is inside the source bounds
                    if (!(srcX < srcMinX || srcX >= srcMaxX || srcY < srcMinY || srcY >= srcMaxY)) {
                        // Old band value
                        valueOld = iter.getSampleDouble(srcX, srcY, 0);
                    }

                    // ROI CHECK
                    boolean contained = true;
                    if (roiUsed) {
                        if (!roi.contains(x, y)) {
                            contained = false;
                            // Comparison if the final value is not inside a ROI
                            assertEquals(value, destNoData, TOLERANCE);
                        }
                    }

                    if (contained) {
                        // If no Data are present, no data check is performed
                        if (noDataUsed) {
                            switch (dataType) {
                            case DataBuffer.TYPE_BYTE:
                                byte sampleB = ImageUtil.clampRoundByte(value);
                                byte sampleBOld = ImageUtil.clampRoundByte(valueOld);
                                if (noData[0].contains(sampleBOld)) {
                                    assertEquals(sampleB, destNoData, TOLERANCE);
                                } else {
                                    assertEquals(sampleB, valueOld, TOLERANCE);
                                }
                                break;
                            case DataBuffer.TYPE_USHORT:
                                short sampleUS = ImageUtil.clampRoundUShort(value);
                                short sampleUSOld = ImageUtil.clampRoundUShort(valueOld);
                                if (noData[0].contains(sampleUSOld)) {
                                    assertEquals(sampleUS, destNoData, TOLERANCE);
                                } else {
                                    assertEquals(sampleUS, valueOld, TOLERANCE);
                                }
                                break;
                            case DataBuffer.TYPE_SHORT:
                                short sampleS = ImageUtil.clampRoundShort(value);
                                short sampleSOld = ImageUtil.clampRoundShort(valueOld);
                                if (noData[0].contains(sampleSOld)) {
                                    assertEquals(sampleS, destNoData, TOLERANCE);
                                } else {
                                    assertEquals(sampleS, valueOld, TOLERANCE);
                                }
                                break;
                            case DataBuffer.TYPE_INT:
                                int sampleI = ImageUtil.clampRoundInt(value);
                                int sampleIOld = ImageUtil.clampRoundInt(valueOld);
                                if (noData[0].contains(sampleIOld)) {
                                    assertEquals(sampleI, destNoData, TOLERANCE);
                                } else {
                                    assertEquals(sampleI, valueOld, TOLERANCE);
                                }
                                break;
                            case DataBuffer.TYPE_FLOAT:
                                float sampleF = ImageUtil.clampFloat(value);
                                float sampleFOld = ImageUtil.clampFloat(valueOld);
                                if (noData[0].contains(sampleFOld)) {
                                    assertEquals(sampleF, destNoData, TOLERANCE);
                                } else {
                                    assertEquals(sampleF, valueOld, TOLERANCE);
                                }
                                break;
                            case DataBuffer.TYPE_DOUBLE:
                                if (noData[0].contains(valueOld)) {
                                    assertEquals(value, destNoData, TOLERANCE);
                                } else {
                                    assertEquals(value, valueOld, TOLERANCE);
                                }
                                break;
                            default:
                                throw new IllegalArgumentException("Wrong data type");
                            }
                        } else {
                            // Else a simple value comparison is done
                            assertEquals(value, valueOld, TOLERANCE);
                        }
                    }
                }
            }
        }
        // Disposal of the output image
        merged.dispose();
    }

    /** Returns the "round" value of a float. */
    private static int round(double f) {
        return f >= 0 ? (int) (f + 0.5F) : (int) (f - 0.5F);
    }

    /** Simple method for image creation */
    private static RenderedImage createTestImage(int dataType, int width, int height,
            Number noDataValue, int bands) {
        // This values could be used for fill all the image
        byte valueB = 64;
        short valueUS = Short.MAX_VALUE / 4;
        short valueS = -50;
        int valueI = 100;
        float valueF = (255 / 2) * 5;
        double valueD = (255 / 1) * 4;

        // parameter block initialization
        int tileW = width / 8;
        int tileH = height / 8;
        int imageDim = width * height;

        final SampleModel sm;

        Byte crossValueByte = null;
        Short crossValueUShort = null;
        Short crossValueShort = null;
        Integer crossValueInteger = null;
        Float crossValueFloat = null;
        Double crossValueDouble = null;

        int numBands = bands;

        if (numBands == 3) {
            sm = new ComponentSampleModel(dataType, width, height, 3, width, new int[] { 0,
                    imageDim, imageDim * 2 });
        } else {
            sm = new ComponentSampleModel(dataType, width, height, 1, width, new int[] { 0 });
        }

        switch (dataType) {
        case DataBuffer.TYPE_BYTE:
            crossValueByte = (Byte) noDataValue;
            break;
        case DataBuffer.TYPE_USHORT:
            crossValueUShort = (Short) noDataValue;
            break;
        case DataBuffer.TYPE_SHORT:
            crossValueShort = (Short) noDataValue;
            break;
        case DataBuffer.TYPE_INT:
            crossValueInteger = (Integer) noDataValue;
            break;
        case DataBuffer.TYPE_FLOAT:
            crossValueFloat = (Float) noDataValue;
            break;
        case DataBuffer.TYPE_DOUBLE:
            crossValueDouble = (Double) noDataValue;
            break;
        default:
            throw new IllegalArgumentException("Wrong data type");
        }

        // Create the constant operation.
        TiledImage used = new TiledImage(sm, tileW, tileH);

        for (int b = 0; b < numBands; b++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < height; k++) {
                    // Addition of a cross on the image
                    if (j == k || j == width - k - 1) {
                        switch (dataType) {
                        case DataBuffer.TYPE_BYTE:
                            used.setSample(j, k, b, crossValueByte);
                            break;
                        case DataBuffer.TYPE_USHORT:
                            used.setSample(j, k, b, crossValueUShort);
                            break;
                        case DataBuffer.TYPE_SHORT:
                            used.setSample(j, k, b, crossValueShort);
                            break;
                        case DataBuffer.TYPE_INT:
                            used.setSample(j, k, b, crossValueInteger);
                            break;
                        case DataBuffer.TYPE_FLOAT:
                            used.setSample(j, k, b, crossValueFloat);
                            break;
                        case DataBuffer.TYPE_DOUBLE:
                            used.setSample(j, k, b, crossValueDouble);
                            break;
                        default:
                            throw new IllegalArgumentException("Wrong data type");
                        }
                        // If selected, the image could be filled
                    } else
                    // a little square of no data on the upper left is inserted
                    if ((j >= 20) && (j < 50) && (k >= 20) && (k < 50)) {
                        switch (dataType) {
                        case DataBuffer.TYPE_BYTE:
                            used.setSample(j, k, b, 0);
                            break;
                        case DataBuffer.TYPE_USHORT:
                            used.setSample(j, k, b, 0);
                            break;
                        case DataBuffer.TYPE_SHORT:
                            used.setSample(j, k, b, 0);
                            break;
                        case DataBuffer.TYPE_INT:
                            used.setSample(j, k, b, 0);
                            break;
                        case DataBuffer.TYPE_FLOAT:
                            used.setSample(j, k, b, 0);
                            break;
                        case DataBuffer.TYPE_DOUBLE:
                            used.setSample(j, k, b, 0);
                            break;
                        default:
                            throw new IllegalArgumentException("Wrong data type");
                        }

                        if ((j >= 30) && (j < 40) && (k >= 20) && (k < 30)) {
                            switch (dataType) {
                            case DataBuffer.TYPE_BYTE:
                                used.setSample(j, k, b, crossValueByte);
                                break;
                            case DataBuffer.TYPE_USHORT:
                                used.setSample(j, k, b, crossValueUShort);
                                break;
                            case DataBuffer.TYPE_SHORT:
                                used.setSample(j, k, b, crossValueShort);
                                break;
                            case DataBuffer.TYPE_INT:
                                used.setSample(j, k, b, crossValueInteger);
                                break;
                            case DataBuffer.TYPE_FLOAT:
                                used.setSample(j, k, b, crossValueFloat);
                                break;
                            case DataBuffer.TYPE_DOUBLE:
                                used.setSample(j, k, b, crossValueDouble);
                                break;
                            default:
                                throw new IllegalArgumentException("Wrong data type");
                            }
                        }
                    } else {
                        // No Data is set
                        switch (dataType) {
                        case DataBuffer.TYPE_BYTE:
                            used.setSample(j, k, b, valueB + b);
                            break;
                        case DataBuffer.TYPE_USHORT:
                            used.setSample(j, k, b, valueUS + b);
                            break;
                        case DataBuffer.TYPE_SHORT:
                            used.setSample(j, k, b, valueS + b);
                            break;
                        case DataBuffer.TYPE_INT:
                            used.setSample(j, k, b, valueI + b);
                            break;
                        case DataBuffer.TYPE_FLOAT:
                            float data = valueF + b / 3.0f;
                            used.setSample(j, k, b, data);
                            break;
                        case DataBuffer.TYPE_DOUBLE:
                            double dataD = valueD + b / 3.0d;
                            used.setSample(j, k, b, dataD);
                            break;
                        default:
                            throw new IllegalArgumentException("Wrong data type");
                        }
                    }
                }
            }
        }
        return used;
    }
}
