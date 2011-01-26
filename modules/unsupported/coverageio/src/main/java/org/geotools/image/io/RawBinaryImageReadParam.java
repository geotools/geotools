/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io;

import java.awt.Dimension;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BandedSampleModel;
import java.awt.image.ColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageTypeSpecifier;
import javax.media.jai.ComponentSampleModelJAI;
import javax.media.jai.PlanarImage;

import org.geotools.resources.image.ComponentColorModelJAI;


/**
 * A class describing how a raw binary stream is to be decoded. In the context of
 * {@link RawBinaryImageReader}, the stream may not contains enough information
 * for an optimal decoding. For example the stream may not contains image's
 * width and height. The {@code RawBinaryImageReadParam} gives a chance
 * to specify those missing informations.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @todo We should consider to use Sun's RAW decoder provided with "Java Advanced Imaging Image I/O
 *       Tools" instead (download at http://java.sun.com/products/java-media/jai/). This replacement
 *       is not yet done because we need to figure out a way to handle pad values first.
 */
public class RawBinaryImageReadParam extends ImageReadParam {
    /**
     * The expected image model, or {@code null} if unknow.
     */
    private SampleModel model;

    /**
     * The expected image size, or {@code null} if unknow.
     */
    private Dimension size;

    /**
     * The expected data type, or {@link DataBuffer#TYPE_UNDEFINED} if unknow.
     */
    private int dataType = DataBuffer.TYPE_UNDEFINED;

    /**
     * The target data type, or {@link DataBuffer#TYPE_UNDEFINED} if not
     * defined. In the later case, the target data type will be the same
     * than the raw one.
     */
    private int targetDataType = DataBuffer.TYPE_UNDEFINED;

    /**
     * The pad value, or {@link Double#NaN} if there is none.
     */
    private double padValue = Double.NaN;

    /**
     * Constructs a new {@code RawBinaryImageReadParam} with default parameters.
     */
    public RawBinaryImageReadParam() {
    }

    /**
     * Specifies the image size in the input stream. Setting the size to {@code null}
     * reset the default size, which is reader dependent. Most readers will thrown an
     * exception at reading time if the image size is unspecified.
     *
     * @param size The expected image size, or {@code null} if unknow.
     */
    public void setStreamImageSize(final Dimension size) {
        this.size = (size!=null) ? new Dimension(size.width, size.height) : null;
    }

    /**
     * Returns the image size in the input stream, or {@code null} if unknow.
     * Image size is specified by the last call to {@link #setStreamImageSize} or
     * {@link #setStreamSampleModel}.
     */
    public Dimension getStreamImageSize() {
        return (size!=null) ? (Dimension) size.clone() : null;
    }

    /**
     * Checks the validity of the specified data type.
     *
     * @param  dataType The data type to check.
     * @throws IllegalArgumentException if {@code dataType} is not one of
     *         the valid enums of {@link DataBuffer}.
     */
    private static void checkDataType(final int dataType) throws IllegalArgumentException {
        if ((dataType < DataBuffer.TYPE_BYTE || dataType > DataBuffer.TYPE_DOUBLE) &&
             dataType != DataBuffer.TYPE_UNDEFINED)
        {
            throw new IllegalArgumentException(String.valueOf(dataType));
        }
    }

    /**
     * Specifies the data type in input stream. Setting data type to
     * {@link DataBuffer#TYPE_UNDEFINED} reset the default value, which
     * is reader dependent.
     *
     * @param dataType The data type, or {@link DataBuffer#TYPE_UNDEFINED} if unknow.
     *        Know data type should be a constant from {@link DataBuffer}. Common
     *        types are {@link DataBuffer#TYPE_INT}, {@link DataBuffer#TYPE_FLOAT}
     *        and {@link DataBuffer#TYPE_DOUBLE}.
     */
    public void setStreamDataType(final int dataType) {
        checkDataType(dataType);
        this.dataType = dataType;
    }

    /**
     * Returns the data type in input stream, or {@link DataBuffer#TYPE_UNDEFINED}
     * if unknow. Data type is specified by the last call to {@link #setStreamDataType}
     * or {@link #setStreamSampleModel}.
     */
    public int getStreamDataType() {
        return dataType;
    }

    /**
     * Sets the desired image type for the destination image, using one of
     * {@link DataBuffer} enumeration constant. This setting will override
     * any previous setting made with {@link #setDestinationType(ImageTypeSpecifier)}
     * or this {@code setDestinationType(int)} method.
     *
     * @param destType The data type. This should be a constant from {@link DataBuffer}.
     *        Common types are {@link DataBuffer#TYPE_INT}, {@link DataBuffer#TYPE_FLOAT}
     *        and {@link DataBuffer#TYPE_DOUBLE}.
     */
    public void setDestinationType(final int destType) {
        checkDataType(destType);
        targetDataType = destType;
        setDestinationType(getDestinationType(model!=null ? model.getNumBands() : 1));
    }

    /**
     * Creates a destination type with the specified number of bands.
     * If no such destination type is available, returns {@code null}.
     */
    final ImageTypeSpecifier getDestinationType(final int numBands) {
        if (targetDataType == DataBuffer.TYPE_UNDEFINED) {
            return null;
        }
        final SampleModel sampleModel;
        final ColorModel   colorModel;
        final ColorSpace   colorSpace = getColorSpace(numBands);
        if (model!=null) {
            /*
             * Case 1: we know the sample model for data in the
             *         underlying stream.  We will use the same
             *         model for the memory image, just changing
             *         the data type.
             */
            if (numBands != model.getNumBands()) {
                throw new IllegalArgumentException("Number of bands mismatch");
            }
            sampleModel = getStreamSampleModel(model, model, size, targetDataType);
        } else {
            /*
             * Case 2: We have to create a sample model from scratch.  We
             *         will use a banded sample model with some arbitrary
             *         color space  (which may be changed after the image
             *         reading is completed).
             */
            final int width, height;
            if (size!=null) {
                width  = size.width;
                height = size.height;
            } else {
                width = height = 1;
            }
            final int[] bankIndices = new int[numBands];
            final int[] bandOffsets = new int[numBands];
            for (int i=numBands; --i>=0;) bankIndices[i]=i;
            if (ContinuousPalette.USE_JAI_MODEL) {
                sampleModel = new ComponentSampleModelJAI(targetDataType, width, height,
                                                          1, width, bankIndices, bandOffsets);
            } else {
                return ImageTypeSpecifier.createBanded(colorSpace, bankIndices, bandOffsets,
                                                       targetDataType, false, false);
            }
        }
        /*
         * Constructs a color model likely to matches the
         * sample model, and then finish the type specifier.
         */
        if (sampleModel instanceof ComponentSampleModel) {
            // This is the most common case.
            colorModel = new ComponentColorModelJAI(getColorSpace(numBands),
                                                    false, false, Transparency.OPAQUE,
                                                    sampleModel.getDataType());
        } else {
            // Fallback to JAI helper method if we have a less common case.
            colorModel = PlanarImage.createColorModel(sampleModel);
        }
        return new ImageTypeSpecifier(colorModel, sampleModel);
    }

    /**
     * Returns a default color space for the destination sample model.
     * If no destination image has been specified, then a gray scale
     * color space will be constructed for values ranging from 0 to 1.
     */
    private ColorSpace getColorSpace(int numBands) {
        if (destination!=null) {
            return destination.getColorModel().getColorSpace();
        }
        /*
         * Overrides the number of source bands if this
         * parameter block contains enough informations.
         */
        if (sourceBands!=null) {
            numBands = sourceBands.length;
        } else if (model!=null) {
            numBands = model.getNumBands();
        }
        /*
         * Checks the number of destination bands. If 'destinationBands' is
         * null,  then all bands are going to be used.   If it is non-null,
         * then the destination image may have more bands than what we are
         * going to use. This problem still an open question... As a patch,
         * current implementation search for the greatest band number.
         */
        if (destinationBands!=null) {
            for (int i=0; i<destinationBands.length; i++) {
                if (destinationBands[i] >= numBands) {
                    numBands = destinationBands[i]+1;
                }
            }
        }
        if (numBands==1) {
            return ColorSpace.getInstance(ColorSpace.CS_GRAY);
        }
        return new ScaledColorSpace(numBands, 0, 0, 1);
    }

    /**
     * Set the pad value.
     *
     * @param padValue The pad value, or {@link Double#NaN} if there is none.
     */
    public void setPadValue(final double padValue) {
        this.padValue = padValue;
    }

    /**
     * Returns the pad value, or {@link Double#NaN} if there is none
     */
    public double getPadValue() {
        return padValue;
    }

    /**
     * Set a sample model indicating the data layout in the input stream.
     * Indications comprise image size and data type, i.e. calling this
     * method with a non-null value is equivalent to calling also the
     * following methods:
     *
     * <blockquote><pre>
     * setStreamImageSize(model.getWidth(), model.getHeight());
     * setStreamDataType(model.getDataType());
     * </pre></blockquote>
     *
     * Setting the sample model to {@code null} reset
     * the default model, which is reader dependent.
     */
    public void setStreamSampleModel(final SampleModel model) {
        this.model = model;
        if (model != null) {
            size = new Dimension(model.getWidth(), model.getHeight());
            dataType = model.getDataType();
        }
    }

    /**
     * Returns a sample model indicating the data layout in the input stream.
     * The {@link SampleModel}'s width and height should matches the image
     * size in the input stream.
     *
     * @return A sample model indicating the data layout in the input stream,
     *         or {@code null} if unknow.
     */
    public SampleModel getStreamSampleModel() {
        return model = getStreamSampleModel(null);
    }

    /**
     * Returns a sample model indicating the data layout in the input stream.
     * The {@link SampleModel}'s width and height should matches the image
     * size in the input stream.
     *
     * @param  defaultSampleModel A default sample model, or {@code null}
     *         if there is no default. If this {@code RawBinaryImageReadParam}
     *         contains unspecified sample model, image size or data type, values
     *         from {@code defaultSampleModel} will be used.
     * @return A sample model indicating the data layout in the input stream,
     *         or {@code null} if unknow.
     */
    final SampleModel getStreamSampleModel(final SampleModel defaultSampleModel) {
        return getStreamSampleModel(defaultSampleModel, model, size, dataType);
    }

    /**
     * Returns a sample model indicating the data layout in the input stream.
     * The {@link SampleModel}'s width and height should matches the image
     * size in the input stream.
     *
     * @param  defaultSampleModel A default sample model, or {@code null}
     *         if there is no default. If this {@code RawBinaryImageReadParam}
     *         contains unspecified sample model, image size or data type, values
     *         from {@code defaultSampleModel} will be used.
     * @param  model The sample model in the underlying stream, or {@code null}.
     * @param  size The image size in the underlying stream, or {@code null}.
     * @param  dataType the data type.
     * @return A sample model indicating the data layout in the input stream,
     *         or {@code null} if unknow.
     */
    private static SampleModel getStreamSampleModel(final SampleModel defaultSampleModel,
                                                    SampleModel model, Dimension size,
                                                    int dataType)
    {
        if (defaultSampleModel != null) {
            if (model == null) {
                model = defaultSampleModel;
            }
            if (size == null) {
                size = new Dimension(defaultSampleModel.getWidth(),
                                     defaultSampleModel.getHeight());
            }
            if (dataType == DataBuffer.TYPE_UNDEFINED) {
                dataType = defaultSampleModel.getDataType();
            }
        }
        if (model == null || size == null || dataType == DataBuffer.TYPE_UNDEFINED) {
            return null;
        }
        final int width  = size.width;
        final int height = size.height;
        if (dataType != model.getDataType()) {
            if (model instanceof ComponentSampleModel) {
                final ComponentSampleModel cast = (ComponentSampleModel) model;
                final int   pixelStride    = cast.getPixelStride();
                final int   scanlineStride = cast.getScanlineStride();
                final int[] bankIndices    = cast.getBankIndices();
                final int[] bandOffsets    = cast.getBandOffsets();
                if (model instanceof BandedSampleModel) {
                    model = new BandedSampleModel(dataType, width, height,
                                                  scanlineStride,
                                                  bankIndices, bandOffsets);
                } else if (model instanceof PixelInterleavedSampleModel) {
                    model = new PixelInterleavedSampleModel(dataType, width, height,
                                                            pixelStride, scanlineStride,
                                                            bandOffsets);
                } else if (model instanceof ComponentSampleModelJAI) {
                    model = new ComponentSampleModelJAI(dataType, width, height,
                                                        pixelStride, scanlineStride,
                                                        bankIndices, bandOffsets);
                } else {
                    model = new ComponentSampleModel(dataType, width, height,
                                                     pixelStride, scanlineStride,
                                                     bankIndices, bandOffsets);
                }
            } else if (model instanceof MultiPixelPackedSampleModel) {
                final MultiPixelPackedSampleModel cast = (MultiPixelPackedSampleModel) model;
                final int numberOfBits   = DataBuffer.getDataTypeSize(dataType);
                final int scanlineStride = cast.getScanlineStride();
                final int dataBitOffset  = cast.getDataBitOffset();
                model = new MultiPixelPackedSampleModel(dataType, width, height,
                                                        numberOfBits,
                                                        scanlineStride, dataBitOffset);
            } else if (model instanceof SinglePixelPackedSampleModel) {
                final SinglePixelPackedSampleModel cast = (SinglePixelPackedSampleModel) model;
                final int   scanlineStride = cast.getScanlineStride();
                final int[] bitMasks       = cast.getBitMasks();
                model = new SinglePixelPackedSampleModel(dataType, width, height,
                                                         scanlineStride, bitMasks);
            } else {
                throw new IllegalStateException(model.getClass().getName());
            }
        }
        if (model.getWidth() != width || model.getHeight() != height) {
            model = model.createCompatibleSampleModel(width, height);
        }
        return model;
    }
}
