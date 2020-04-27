/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Map;
import javax.media.jai.AreaOpImage;
import javax.media.jai.BorderExtender;
import javax.media.jai.ImageLayout;
import javax.media.jai.RasterAccessor;
import javax.media.jai.RasterFormatTag;
import org.geotools.processing.jai.TransparencyFillDescriptor.FillType;

public class TransparencyFillOpImage extends AreaOpImage {

    private FillType type = TransparencyFillDescriptor.FILL_AVERAGE;

    private Number noData = 0;

    /** Transparency Fill algorithms. More can be added in the future for different logics */
    static enum TransparencyFillAlgorithm {

        // Fill the empty pixel by taking sourrounding pixels and computing the average.
        FILL_AVERAGE {
            @Override
            public void fillPixel(
                    int numBands,
                    byte[][] srcData,
                    byte[][] dstData,
                    int srcOffset,
                    int srcStride,
                    int dstOffset) {
                for (int k = 0; k < numBands; k++) {
                    if (k != numBands - 1) {
                        int left = (srcData[k][srcOffset - srcStride + k] & 0xFF);
                        int right = (srcData[k][srcOffset + srcStride + k] & 0xFF);
                        dstData[k][dstOffset + k] = (byte) ((left + right) / 2);
                    } else {
                        dstData[k][dstOffset + k] =
                                ((byte) ((srcData[k][srcOffset - srcStride + k])));
                    }
                }
            }

            @Override
            public void fillPixel(
                    int numBands,
                    int[][] srcData,
                    int[][] dstData,
                    int srcOffset,
                    int srcStride,
                    int dstOffset) {
                for (int k = 0; k < numBands; k++) {
                    if (k != numBands - 1) {
                        int left = (srcData[k][srcOffset - srcStride + k]);
                        int right = (srcData[k][srcOffset + srcStride + k]);
                        dstData[k][dstOffset + k] = (int) ((left + right) / 2);
                    } else {
                        dstData[k][dstOffset + k] =
                                ((int) ((srcData[k][srcOffset - srcStride + k])));
                    }
                }
            }

            @Override
            public void fillPixel(
                    int numBands,
                    short[][] srcData,
                    short[][] dstData,
                    int srcOffset,
                    int srcStride,
                    int dstOffset) {
                for (int k = 0; k < numBands; k++) {
                    if (k != numBands - 1) {
                        float left = (srcData[k][srcOffset - srcStride + k]);
                        float right = (srcData[k][srcOffset + srcStride + k]);
                        dstData[k][dstOffset + k] = (short) ((left + right) / 2);
                    } else {
                        dstData[k][dstOffset + k] =
                                ((short) ((srcData[k][srcOffset - srcStride + k])));
                    }
                }
            }

            @Override
            public void fillPixel(
                    int numBands,
                    float[][] srcData,
                    float[][] dstData,
                    int srcOffset,
                    int srcStride,
                    int dstOffset) {
                for (int k = 0; k < numBands; k++) {
                    if (k != numBands - 1) {
                        float left = (srcData[k][srcOffset - srcStride + k]);
                        float right = (srcData[k][srcOffset + srcStride + k]);
                        dstData[k][dstOffset + k] = (float) ((left + right) / 2);
                    } else {
                        dstData[k][dstOffset + k] =
                                ((float) ((srcData[k][srcOffset - srcStride + k])));
                    }
                }
            }

            @Override
            public void fillPixel(
                    int numBands,
                    double[][] srcData,
                    double[][] dstData,
                    int srcOffset,
                    int srcStride,
                    int dstOffset) {
                for (int k = 0; k < numBands; k++) {
                    if (k != numBands - 1) {
                        double left = (srcData[k][srcOffset - srcStride + k]);
                        double right = (srcData[k][srcOffset + srcStride + k]);
                        dstData[k][dstOffset + k] = (double) ((left + right) / 2);
                    } else {
                        dstData[k][dstOffset + k] =
                                ((double) ((srcData[k][srcOffset - srcStride + k])));
                    }
                }
            }
        },

        // Fill the empty pixel by taking data from the adjacent pixel (left/above pixel)
        FILL_CLONE_FIRST {
            @Override
            public void fillPixel(
                    int numBands,
                    byte[][] srcData,
                    byte[][] dstData,
                    int srcOffset,
                    int srcStride,
                    int dstOffset) {
                for (int k = 0; k < numBands; k++) {
                    dstData[k][dstOffset + k] = ((byte) ((srcData[k][srcOffset - srcStride + k])));
                }
            }

            @Override
            public void fillPixel(
                    int numBands,
                    int[][] srcData,
                    int[][] dstData,
                    int srcOffset,
                    int srcStride,
                    int dstOffset) {
                for (int k = 0; k < numBands; k++) {
                    dstData[k][dstOffset + k] = ((int) ((srcData[k][srcOffset - srcStride + k])));
                }
            }

            @Override
            public void fillPixel(
                    int numBands,
                    short[][] srcData,
                    short[][] dstData,
                    int srcOffset,
                    int srcStride,
                    int dstOffset) {
                for (int k = 0; k < numBands; k++) {
                    dstData[k][dstOffset + k] = ((short) ((srcData[k][srcOffset - srcStride + k])));
                }
            }

            @Override
            public void fillPixel(
                    int numBands,
                    float[][] srcData,
                    float[][] dstData,
                    int srcOffset,
                    int srcStride,
                    int dstOffset) {
                for (int k = 0; k < numBands; k++) {
                    dstData[k][dstOffset + k] = ((float) ((srcData[k][srcOffset - srcStride + k])));
                }
            }

            @Override
            public void fillPixel(
                    int numBands,
                    double[][] srcData,
                    double[][] dstData,
                    int srcOffset,
                    int srcStride,
                    int dstOffset) {
                for (int k = 0; k < numBands; k++) {
                    dstData[k][dstOffset + k] = ((float) ((srcData[k][srcOffset - srcStride + k])));
                }
            }
        },

        // Fill the empty pixel by taking data from the adjacent pixel (right/below pixel)
        FILL_CLONE_SECOND {
            @Override
            public void fillPixel(
                    int numBands,
                    byte[][] srcData,
                    byte[][] dstData,
                    int srcOffset,
                    int srcStride,
                    int dstOffset) {
                for (int k = 0; k < numBands; k++) {
                    dstData[k][dstOffset + k] = ((byte) ((srcData[k][srcOffset + srcStride + k])));
                }
            }

            @Override
            public void fillPixel(
                    int numBands,
                    short[][] srcData,
                    short[][] dstData,
                    int srcOffset,
                    int srcStride,
                    int dstOffset) {
                for (int k = 0; k < numBands; k++) {
                    dstData[k][dstOffset + k] = ((short) ((srcData[k][srcOffset + srcStride + k])));
                }
            }

            @Override
            public void fillPixel(
                    int numBands,
                    int[][] srcData,
                    int[][] dstData,
                    int srcOffset,
                    int srcStride,
                    int dstOffset) {
                for (int k = 0; k < numBands; k++) {
                    dstData[k][dstOffset + k] = ((int) ((srcData[k][srcOffset + srcStride + k])));
                }
            }

            @Override
            public void fillPixel(
                    int numBands,
                    float[][] srcData,
                    float[][] dstData,
                    int srcOffset,
                    int srcStride,
                    int dstOffset) {
                for (int k = 0; k < numBands; k++) {
                    dstData[k][dstOffset + k] = ((float) ((srcData[k][srcOffset + srcStride + k])));
                }
            }

            @Override
            public void fillPixel(
                    int numBands,
                    double[][] srcData,
                    double[][] dstData,
                    int srcOffset,
                    int srcStride,
                    int dstOffset) {
                for (int k = 0; k < numBands; k++) {
                    dstData[k][dstOffset + k] =
                            ((double) ((srcData[k][srcOffset + srcStride + k])));
                }
            }
        };

        public abstract void fillPixel(
                int numBands,
                byte[][] srcData,
                byte[][] dstData,
                int srcOffset,
                int srcStride,
                int dstOffset);

        public abstract void fillPixel(
                int numBands,
                float[][] srcData,
                float[][] dstData,
                int srcOffset,
                int srcStride,
                int dstOffset);

        public abstract void fillPixel(
                int numBands,
                double[][] srcData,
                double[][] dstData,
                int srcOffset,
                int srcStride,
                int dstOffset);

        public abstract void fillPixel(
                int numBands,
                int[][] srcData,
                int[][] dstData,
                int srcOffset,
                int srcStride,
                int dstOffset);

        public abstract void fillPixel(
                int numBands,
                short[][] srcData,
                short[][] dstData,
                int srcOffset,
                int srcStride,
                int dstOffset);
    };

    private TransparencyFillAlgorithm fillAlgorithm = null;

    /**
     * Creates a TransparencyFillOpImage given a ParameterBlock containing the image source. The
     * image dimensions are derived from the source image. The tile grid layout, SampleModel, and
     * ColorModel may optionally be specified by an ImageLayout object.
     *
     * @param source a RenderedImage.
     * @param extender a BorderExtender, or null.
     * @param type a {@link FillType} type to be used for transparency filling
     * @param layout an ImageLayout optionally containing the tile grid layout, SampleModel, and
     *     ColorModel, or null.
     */
    public TransparencyFillOpImage(
            RenderedImage source,
            BorderExtender extender,
            FillType type,
            Map config,
            ImageLayout layout,
            Number noData) {
        super(source, layout, config, true, extender, 1, 1, 1, 1);
        if (type == null) {
            // Setting up default
            type = TransparencyFillDescriptor.FILL_AVERAGE;
        }
        this.type = type;
        switch (this.type.getValue()) {
            case 0:
                fillAlgorithm = TransparencyFillAlgorithm.FILL_AVERAGE;
                break;
            case 1:
                fillAlgorithm = TransparencyFillAlgorithm.FILL_CLONE_FIRST;
                break;
            case 2:
                fillAlgorithm = TransparencyFillAlgorithm.FILL_CLONE_SECOND;
                break;
        }
        this.noData = noData;
    }

    /**
     * Performs fill on a specified rectangle. The sources are cobbled.
     *
     * @param sources an array of source Rasters, guaranteed to provide all necessary source data
     *     for computing the output.
     * @param dest a WritableRaster tile containing the area to be computed.
     * @param destRect the rectangle within dest to be processed.
     */
    protected void computeRect(Raster[] sources, WritableRaster dest, Rectangle destRect) {
        // Retrieve format tags.
        RasterFormatTag[] formatTags = getFormatTags();

        Raster source = sources[0];
        Rectangle srcRect = mapDestRect(destRect, 0);

        RasterAccessor srcAccessor =
                new RasterAccessor(
                        source, srcRect, formatTags[0], getSourceImage(0).getColorModel());
        RasterAccessor dstAccessor =
                new RasterAccessor(dest, destRect, formatTags[1], getColorModel());

        switch (dstAccessor.getDataType()) {
            case DataBuffer.TYPE_BYTE:
                byteLoop(srcAccessor, dstAccessor);
                break;
            case DataBuffer.TYPE_USHORT:
                ushortLoop(srcAccessor, dstAccessor);
                break;
            case DataBuffer.TYPE_SHORT:
                shortLoop(srcAccessor, dstAccessor);
                break;
            case DataBuffer.TYPE_INT:
                intLoop(srcAccessor, dstAccessor);
                break;
            case DataBuffer.TYPE_FLOAT:
                floatLoop(srcAccessor, dstAccessor);
                break;
            case DataBuffer.TYPE_DOUBLE:
                doubleLoop(srcAccessor, dstAccessor);
                break;
        }

        // If the RasterAccessor object set up a temporary buffer for the
        // op to write to, tell the RasterAccessor to write that data
        // to the raster no that we're done with it.
        if (dstAccessor.isDataCopy()) {
            dstAccessor.clampDataArrays();
            dstAccessor.copyDataToRaster();
        }
    }

    private void byteLoop(RasterAccessor src, RasterAccessor dst) {

        int dwidth = dst.getWidth();
        int dheight = dst.getHeight();
        int numBands = dst.getNumBands();

        int dstOffsetsForBands[] = dst.getOffsetsForBands();
        int dstBandOffsets[] = dst.getBandOffsets();
        int dstPixelStride = dst.getPixelStride();
        int dstScanlineStride = dst.getScanlineStride();

        int srcOffsetsForBands[] = src.getOffsetsForBands();
        int srcBandOffsets[] = src.getBandOffsets();
        int srcPixelStride = src.getPixelStride();
        int srcScanlineStride = src.getScanlineStride();

        byte dstDataArrays[][] = dst.getByteDataArrays();
        byte srcDataArrays[][] = src.getByteDataArrays();
        byte srcData[] = srcDataArrays[numBands - 1];

        int srcScanlineOffset = srcBandOffsets[numBands - 1];
        int srcScanlineDataOffset = srcBandOffsets[srcOffsetsForBands[0]];
        int dstScanlineOffset[] = new int[numBands];

        for (int i = 0; i < numBands; i++) {
            dstScanlineOffset[i] = dstBandOffsets[i];
        }

        for (int j = 0; j < dheight; j++) {
            int srcAlphaCentralPixelOffset = srcScanlineOffset + srcScanlineStride + srcPixelStride;
            int srcDataLeftPixelOffset = srcScanlineDataOffset + srcScanlineStride;
            int srcDataPixelOffset = srcDataLeftPixelOffset + srcPixelStride;
            int dstPixelOffset = dstScanlineOffset[dstOffsetsForBands[0]];
            int imageOffset = srcAlphaCentralPixelOffset;
            int imageDataOffset = srcDataPixelOffset;
            for (int i = 0; i < dwidth; i++) {
                int imageVerticalOffset = imageOffset;
                int imageDataVerticalOffset = imageDataOffset;
                int centralPixel = srcData[imageVerticalOffset];
                boolean copySource = true;
                if (centralPixel == noData.intValue()) {
                    // Check if previous and next pixels are zero.
                    int rightPixel = srcData[imageVerticalOffset + srcPixelStride];
                    int leftPixel = srcData[imageVerticalOffset - srcPixelStride];
                    if (rightPixel != noData.intValue() && leftPixel != noData.intValue()) {
                        // This has been identified as a pixel of a vertical transparent stripe
                        fillAlgorithm.fillPixel(
                                numBands,
                                srcDataArrays,
                                dstDataArrays,
                                imageDataVerticalOffset,
                                srcPixelStride,
                                dstPixelOffset);
                        copySource = false;
                    } else {
                        // A transparent pixel with adjacent transparent pixels along x
                        int upperPixel = srcData[imageVerticalOffset - srcScanlineStride];
                        if (upperPixel != noData.intValue()) {
                            int lowerPixel = srcData[imageVerticalOffset + srcScanlineStride];
                            if (lowerPixel != noData.intValue()) {
                                // This has been identified as a pixel of an horizontal transparent
                                // stripe
                                fillAlgorithm.fillPixel(
                                        numBands,
                                        srcDataArrays,
                                        dstDataArrays,
                                        imageDataVerticalOffset,
                                        srcScanlineStride,
                                        dstPixelOffset);
                                copySource = false;
                            }
                        }
                    }
                }
                if (copySource) {
                    for (int k = 0; k < numBands; k++) {
                        dstDataArrays[k][dstPixelOffset + k] =
                                srcDataArrays[k][imageDataVerticalOffset + k];
                    }
                }
                imageOffset += srcPixelStride;
                imageDataOffset += srcPixelStride;
                dstPixelOffset += dstPixelStride;
            }

            srcScanlineOffset += srcScanlineStride;
            srcScanlineDataOffset += srcScanlineStride;

            for (int i = 0; i < numBands; i++) {
                dstScanlineOffset[i] += dstScanlineStride;
            }
        }
    }

    private void intLoop(RasterAccessor src, RasterAccessor dst) {

        int dwidth = dst.getWidth();
        int dheight = dst.getHeight();
        int numBands = dst.getNumBands();

        int dstOffsetsForBands[] = dst.getOffsetsForBands();
        int dstBandOffsets[] = dst.getBandOffsets();
        int dstPixelStride = dst.getPixelStride();
        int dstScanlineStride = dst.getScanlineStride();

        int srcOffsetsForBands[] = src.getOffsetsForBands();
        int srcBandOffsets[] = src.getBandOffsets();
        int srcPixelStride = src.getPixelStride();
        int srcScanlineStride = src.getScanlineStride();

        int dstDataArrays[][] = dst.getIntDataArrays();
        int srcDataArrays[][] = src.getIntDataArrays();
        int srcData[] = srcDataArrays[numBands - 1];

        int srcScanlineOffset = srcBandOffsets[numBands - 1];
        int srcScanlineDataOffset = srcBandOffsets[srcOffsetsForBands[0]];
        int dstScanlineOffset[] = new int[numBands];

        int transparentVal = noData.intValue();
        for (int i = 0; i < numBands; i++) {
            dstScanlineOffset[i] = dstBandOffsets[i];
        }

        for (int j = 0; j < dheight; j++) {
            int srcAlphaCentralPixelOffset = srcScanlineOffset + srcScanlineStride + srcPixelStride;
            int srcDataLeftPixelOffset = srcScanlineDataOffset + srcScanlineStride;
            int srcDataPixelOffset = srcDataLeftPixelOffset + srcPixelStride;
            int dstPixelOffset = dstScanlineOffset[dstOffsetsForBands[0]];
            int imageOffset = srcAlphaCentralPixelOffset;
            int imageDataOffset = srcDataPixelOffset;
            for (int i = 0; i < dwidth; i++) {
                int imageVerticalOffset = imageOffset;
                int imageDataVerticalOffset = imageDataOffset;
                int centralPixel = srcData[imageVerticalOffset];
                boolean copySource = true;
                if (centralPixel == transparentVal) {
                    // Check if previous and next pixels are zero.
                    int rightPixel = srcData[imageVerticalOffset + srcPixelStride];
                    int leftPixel = srcData[imageVerticalOffset - srcPixelStride];
                    if (rightPixel != transparentVal && leftPixel != transparentVal) {
                        // This has been identified as a pixel of a vertical transparent stripe
                        fillAlgorithm.fillPixel(
                                numBands,
                                srcDataArrays,
                                dstDataArrays,
                                imageDataVerticalOffset,
                                srcPixelStride,
                                dstPixelOffset);
                        copySource = false;
                    } else {
                        // A transparent pixel with adjacent transparent pixels along x
                        int upperPixel = srcData[imageVerticalOffset - srcScanlineStride];
                        if (upperPixel != transparentVal) {
                            int lowerPixel = srcData[imageVerticalOffset + srcScanlineStride];
                            if (lowerPixel != transparentVal) {
                                // This has been identified as a pixel of an horizontal transparent
                                // stripe
                                fillAlgorithm.fillPixel(
                                        numBands,
                                        srcDataArrays,
                                        dstDataArrays,
                                        imageDataVerticalOffset,
                                        srcScanlineStride,
                                        dstPixelOffset);
                                copySource = false;
                            }
                        }
                    }
                }
                if (copySource) {
                    for (int k = 0; k < numBands; k++) {
                        dstDataArrays[k][dstPixelOffset + k] =
                                srcDataArrays[k][imageDataVerticalOffset + k];
                    }
                }
                imageOffset += srcPixelStride;
                imageDataOffset += srcPixelStride;
                dstPixelOffset += dstPixelStride;
            }

            srcScanlineOffset += srcScanlineStride;
            srcScanlineDataOffset += srcScanlineStride;

            for (int i = 0; i < numBands; i++) {
                dstScanlineOffset[i] += dstScanlineStride;
            }
        }
    }

    private void shortLoop(RasterAccessor src, RasterAccessor dst) {

        int dwidth = dst.getWidth();
        int dheight = dst.getHeight();
        int numBands = dst.getNumBands();

        int dstOffsetsForBands[] = dst.getOffsetsForBands();
        int dstBandOffsets[] = dst.getBandOffsets();
        int dstPixelStride = dst.getPixelStride();
        int dstScanlineStride = dst.getScanlineStride();

        int srcOffsetsForBands[] = src.getOffsetsForBands();
        int srcBandOffsets[] = src.getBandOffsets();
        int srcPixelStride = src.getPixelStride();
        int srcScanlineStride = src.getScanlineStride();

        short dstDataArrays[][] = dst.getShortDataArrays();
        short srcDataArrays[][] = src.getShortDataArrays();
        short srcData[] = srcDataArrays[numBands - 1];

        int srcScanlineOffset = srcBandOffsets[numBands - 1];
        int srcScanlineDataOffset = srcBandOffsets[srcOffsetsForBands[0]];
        int dstScanlineOffset[] = new int[numBands];

        short transparentVal = noData.shortValue();

        for (int i = 0; i < numBands; i++) {
            dstScanlineOffset[i] = dstBandOffsets[i];
        }

        for (int j = 0; j < dheight; j++) {
            int srcAlphaCentralPixelOffset = srcScanlineOffset + srcScanlineStride + srcPixelStride;
            int srcDataLeftPixelOffset = srcScanlineDataOffset + srcScanlineStride;
            int srcDataPixelOffset = srcDataLeftPixelOffset + srcPixelStride;
            int dstPixelOffset = dstScanlineOffset[dstOffsetsForBands[0]];
            int imageOffset = srcAlphaCentralPixelOffset;
            int imageDataOffset = srcDataPixelOffset;
            for (int i = 0; i < dwidth; i++) {
                int imageVerticalOffset = imageOffset;
                int imageDataVerticalOffset = imageDataOffset;
                short centralPixel = srcData[imageVerticalOffset];
                boolean copySource = true;
                if (centralPixel == transparentVal) {
                    // Check if previous and next pixels are zero.
                    short rightPixel = srcData[imageVerticalOffset + srcPixelStride];
                    short leftPixel = srcData[imageVerticalOffset - srcPixelStride];
                    if (rightPixel != transparentVal && leftPixel != transparentVal) {
                        // This has been identified as a pixel of a vertical transparent stripe
                        fillAlgorithm.fillPixel(
                                numBands,
                                srcDataArrays,
                                dstDataArrays,
                                imageDataVerticalOffset,
                                srcPixelStride,
                                dstPixelOffset);
                        copySource = false;
                    } else {
                        // A transparent pixel with adjacent transparent pixels along x
                        short upperPixel = srcData[imageVerticalOffset - srcScanlineStride];
                        if (upperPixel != transparentVal) {
                            short lowerPixel = srcData[imageVerticalOffset + srcScanlineStride];
                            if (lowerPixel != transparentVal) {
                                // This has been identified as a pixel of an horizontal transparent
                                // stripe
                                fillAlgorithm.fillPixel(
                                        numBands,
                                        srcDataArrays,
                                        dstDataArrays,
                                        imageDataVerticalOffset,
                                        srcScanlineStride,
                                        dstPixelOffset);
                                copySource = false;
                            }
                        }
                    }
                }
                if (copySource) {
                    for (int k = 0; k < numBands; k++) {
                        dstDataArrays[k][dstPixelOffset + k] =
                                srcDataArrays[k][imageDataVerticalOffset + k];
                    }
                }
                imageOffset += srcPixelStride;
                imageDataOffset += srcPixelStride;
                dstPixelOffset += dstPixelStride;
            }

            srcScanlineOffset += srcScanlineStride;
            srcScanlineDataOffset += srcScanlineStride;

            for (int i = 0; i < numBands; i++) {
                dstScanlineOffset[i] += dstScanlineStride;
            }
        }
    }

    private void ushortLoop(RasterAccessor src, RasterAccessor dst) {

        int dwidth = dst.getWidth();
        int dheight = dst.getHeight();
        int numBands = dst.getNumBands();

        int dstOffsetsForBands[] = dst.getOffsetsForBands();
        int dstBandOffsets[] = dst.getBandOffsets();
        int dstPixelStride = dst.getPixelStride();
        int dstScanlineStride = dst.getScanlineStride();

        int srcOffsetsForBands[] = src.getOffsetsForBands();
        int srcBandOffsets[] = src.getBandOffsets();
        int srcPixelStride = src.getPixelStride();
        int srcScanlineStride = src.getScanlineStride();

        short dstDataArrays[][] = dst.getShortDataArrays();
        short srcDataArrays[][] = src.getShortDataArrays();
        short srcData[] = srcDataArrays[numBands - 1];

        int srcScanlineOffset = srcBandOffsets[numBands - 1];
        int srcScanlineDataOffset = srcBandOffsets[srcOffsetsForBands[0]];
        int dstScanlineOffset[] = new int[numBands];

        short transparentValue = noData.shortValue();
        for (int i = 0; i < numBands; i++) {
            dstScanlineOffset[i] = dstBandOffsets[i];
        }

        for (int j = 0; j < dheight; j++) {
            int srcAlphaCentralPixelOffset = srcScanlineOffset + srcScanlineStride + srcPixelStride;
            int srcDataLeftPixelOffset = srcScanlineDataOffset + srcScanlineStride;
            int srcDataPixelOffset = srcDataLeftPixelOffset + srcPixelStride;
            int dstPixelOffset = dstScanlineOffset[dstOffsetsForBands[0]];
            int imageOffset = srcAlphaCentralPixelOffset;
            int imageDataOffset = srcDataPixelOffset;
            for (int i = 0; i < dwidth; i++) {
                int imageVerticalOffset = imageOffset;
                int imageDataVerticalOffset = imageDataOffset;
                short centralPixel = srcData[imageVerticalOffset];
                boolean copySource = true;
                if (centralPixel == transparentValue) {
                    // Check if previous and next pixels are zero.
                    short rightPixel = srcData[imageVerticalOffset + srcPixelStride];
                    short leftPixel = srcData[imageVerticalOffset - srcPixelStride];
                    if (rightPixel != transparentValue && leftPixel != transparentValue) {
                        // This has been identified as a pixel of a vertical transparent stripe
                        fillAlgorithm.fillPixel(
                                numBands,
                                srcDataArrays,
                                dstDataArrays,
                                imageDataVerticalOffset,
                                srcPixelStride,
                                dstPixelOffset);
                        copySource = false;
                    } else {
                        // A transparent pixel with adjacent transparent pixels along x
                        short upperPixel = srcData[imageVerticalOffset - srcScanlineStride];
                        if (upperPixel != transparentValue) {
                            short lowerPixel = srcData[imageVerticalOffset + srcScanlineStride];
                            if (lowerPixel != transparentValue) {
                                // This has been identified as a pixel of an horizontal transparent
                                // stripe
                                fillAlgorithm.fillPixel(
                                        numBands,
                                        srcDataArrays,
                                        dstDataArrays,
                                        imageDataVerticalOffset,
                                        srcScanlineStride,
                                        dstPixelOffset);
                                copySource = false;
                            }
                        }
                    }
                }
                if (copySource) {
                    for (int k = 0; k < numBands; k++) {
                        dstDataArrays[k][dstPixelOffset + k] =
                                srcDataArrays[k][imageDataVerticalOffset + k];
                    }
                }
                imageOffset += srcPixelStride;
                imageDataOffset += srcPixelStride;
                dstPixelOffset += dstPixelStride;
            }

            srcScanlineOffset += srcScanlineStride;
            srcScanlineDataOffset += srcScanlineStride;

            for (int i = 0; i < numBands; i++) {
                dstScanlineOffset[i] += dstScanlineStride;
            }
        }
    }

    private void floatLoop(RasterAccessor src, RasterAccessor dst) {

        int dwidth = dst.getWidth();
        int dheight = dst.getHeight();
        int numBands = dst.getNumBands();

        int dstOffsetsForBands[] = dst.getOffsetsForBands();
        int dstBandOffsets[] = dst.getBandOffsets();
        int dstPixelStride = dst.getPixelStride();
        int dstScanlineStride = dst.getScanlineStride();

        int srcOffsetsForBands[] = src.getOffsetsForBands();
        int srcBandOffsets[] = src.getBandOffsets();
        int srcPixelStride = src.getPixelStride();
        int srcScanlineStride = src.getScanlineStride();

        float dstDataArrays[][] = dst.getFloatDataArrays();
        float srcDataArrays[][] = src.getFloatDataArrays();
        float srcData[] = srcDataArrays[numBands - 1];

        int srcScanlineOffset = srcBandOffsets[numBands - 1];
        int srcScanlineDataOffset = srcBandOffsets[srcOffsetsForBands[0]];
        int dstScanlineOffset[] = new int[numBands];

        float transparentValue = noData.floatValue();
        for (int i = 0; i < numBands; i++) {
            dstScanlineOffset[i] = dstBandOffsets[i];
        }

        for (int j = 0; j < dheight; j++) {
            int srcAlphaCentralPixelOffset = srcScanlineOffset + srcScanlineStride + srcPixelStride;
            int srcDataLeftPixelOffset = srcScanlineDataOffset + srcScanlineStride;
            int srcDataPixelOffset = srcDataLeftPixelOffset + srcPixelStride;
            int dstPixelOffset = dstScanlineOffset[dstOffsetsForBands[0]];
            int imageOffset = srcAlphaCentralPixelOffset;
            int imageDataOffset = srcDataPixelOffset;
            for (int i = 0; i < dwidth; i++) {
                int imageVerticalOffset = imageOffset;
                int imageDataVerticalOffset = imageDataOffset;
                float centralPixel = srcData[imageVerticalOffset];
                boolean copySource = true;
                if (centralPixel == transparentValue) {
                    // Check if previous and next pixels are zero.
                    float rightPixel = srcData[imageVerticalOffset + srcPixelStride];
                    float leftPixel = srcData[imageVerticalOffset - srcPixelStride];
                    if (rightPixel != transparentValue && leftPixel != transparentValue) {
                        // This has been identified as a pixel of a vertical transparent stripe
                        fillAlgorithm.fillPixel(
                                numBands,
                                srcDataArrays,
                                dstDataArrays,
                                imageDataVerticalOffset,
                                srcPixelStride,
                                dstPixelOffset);
                        copySource = false;
                    } else {
                        // A transparent pixel with adjacent transparent pixels along x
                        float upperPixel = srcData[imageVerticalOffset - srcScanlineStride];
                        if (upperPixel != transparentValue) {
                            float lowerPixel = srcData[imageVerticalOffset + srcScanlineStride];
                            if (lowerPixel != transparentValue) {
                                // This has been identified as a pixel of an horizontal transparent
                                // stripe
                                fillAlgorithm.fillPixel(
                                        numBands,
                                        srcDataArrays,
                                        dstDataArrays,
                                        imageDataVerticalOffset,
                                        srcScanlineStride,
                                        dstPixelOffset);
                                copySource = false;
                            }
                        }
                    }
                }
                if (copySource) {
                    for (int k = 0; k < numBands; k++) {
                        dstDataArrays[k][dstPixelOffset + k] =
                                srcDataArrays[k][imageDataVerticalOffset + k];
                    }
                }
                imageOffset += srcPixelStride;
                imageDataOffset += srcPixelStride;
                dstPixelOffset += dstPixelStride;
            }

            srcScanlineOffset += srcScanlineStride;
            srcScanlineDataOffset += srcScanlineStride;

            for (int i = 0; i < numBands; i++) {
                dstScanlineOffset[i] += dstScanlineStride;
            }
        }
    }

    private void doubleLoop(RasterAccessor src, RasterAccessor dst) {

        int dwidth = dst.getWidth();
        int dheight = dst.getHeight();
        int numBands = dst.getNumBands();

        int dstOffsetsForBands[] = dst.getOffsetsForBands();
        int dstBandOffsets[] = dst.getBandOffsets();
        int dstPixelStride = dst.getPixelStride();
        int dstScanlineStride = dst.getScanlineStride();

        int srcOffsetsForBands[] = src.getOffsetsForBands();
        int srcBandOffsets[] = src.getBandOffsets();
        int srcPixelStride = src.getPixelStride();
        int srcScanlineStride = src.getScanlineStride();

        double dstDataArrays[][] = dst.getDoubleDataArrays();
        double srcDataArrays[][] = src.getDoubleDataArrays();
        double srcData[] = srcDataArrays[numBands - 1];

        int srcScanlineOffset = srcBandOffsets[numBands - 1];
        int srcScanlineDataOffset = srcBandOffsets[srcOffsetsForBands[0]];
        int dstScanlineOffset[] = new int[numBands];

        double transparentValue = noData.doubleValue();
        for (int i = 0; i < numBands; i++) {
            dstScanlineOffset[i] = dstBandOffsets[i];
        }

        for (int j = 0; j < dheight; j++) {
            int srcAlphaCentralPixelOffset = srcScanlineOffset + srcScanlineStride + srcPixelStride;
            int srcDataLeftPixelOffset = srcScanlineDataOffset + srcScanlineStride;
            int srcDataPixelOffset = srcDataLeftPixelOffset + srcPixelStride;
            int dstPixelOffset = dstScanlineOffset[dstOffsetsForBands[0]];
            int imageOffset = srcAlphaCentralPixelOffset;
            int imageDataOffset = srcDataPixelOffset;
            for (int i = 0; i < dwidth; i++) {
                int imageVerticalOffset = imageOffset;
                int imageDataVerticalOffset = imageDataOffset;
                double centralPixel = srcData[imageVerticalOffset];
                boolean copySource = true;
                if (centralPixel == transparentValue) {
                    // Check if previous and next pixels are zero.
                    double rightPixel = srcData[imageVerticalOffset + srcPixelStride];
                    double leftPixel = srcData[imageVerticalOffset - srcPixelStride];
                    if (rightPixel != transparentValue && leftPixel != transparentValue) {
                        // This has been identified as a pixel of a vertical transparent stripe
                        fillAlgorithm.fillPixel(
                                numBands,
                                srcDataArrays,
                                dstDataArrays,
                                imageDataVerticalOffset,
                                srcPixelStride,
                                dstPixelOffset);
                        copySource = false;
                    } else {
                        // A transparent pixel with adjacent transparent pixels along x
                        double upperPixel = srcData[imageVerticalOffset - srcScanlineStride];
                        if (upperPixel != transparentValue) {
                            double lowerPixel = srcData[imageVerticalOffset + srcScanlineStride];
                            if (lowerPixel != transparentValue) {
                                // This has been identified as a pixel of an horizontal transparent
                                // stripe
                                fillAlgorithm.fillPixel(
                                        numBands,
                                        srcDataArrays,
                                        dstDataArrays,
                                        imageDataVerticalOffset,
                                        srcScanlineStride,
                                        dstPixelOffset);
                                copySource = false;
                            }
                        }
                    }
                }
                if (copySource) {
                    for (int k = 0; k < numBands; k++) {
                        dstDataArrays[k][dstPixelOffset + k] =
                                srcDataArrays[k][imageDataVerticalOffset + k];
                    }
                }
                imageOffset += srcPixelStride;
                imageDataOffset += srcPixelStride;
                dstPixelOffset += dstPixelStride;
            }

            srcScanlineOffset += srcScanlineStride;
            srcScanlineDataOffset += srcScanlineStride;

            for (int i = 0; i < numBands; i++) {
                dstScanlineOffset[i] += dstScanlineStride;
            }
        }
    }
}
