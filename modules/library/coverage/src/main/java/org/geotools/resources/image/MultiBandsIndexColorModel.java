/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.resources.image;

// J2SE dependencies
import java.awt.image.BandedSampleModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;


/**
 * An {@link IndexColorModel} tolerant with image having more than one band.
 * <p>
 * <strong>Reminder:</strong> {@link #getNumComponents} will returns 3 or 4 no matter
 * how many bands were specified to the constructor. This is not specific to this class;
 * {@code IndexColorModel} behave that way. So we can't rely on this method for checking
 * the number of bands.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Andrea Aime
 */
final class MultiBandsIndexColorModel extends IndexColorModel {
    /**
     * The number of bands.
     */
    final int numBands;

    /**
     * The visible band.
     */
    private final int visibleBand;

    /**
     * Construct an object with the specified properties.
     *
     * @param bits      The number of bits each pixel occupies.
     * @param size      The size of the color component arrays.
     * @param cmap      The array of color components.
     * @param start     The starting offset of the first color component.
     * @param hasalpha  Indicates whether alpha values are contained in the {@code cmap} array.
     * @param transparent  The index of the fully transparent pixel.
     * @param transferType The data type of the array used to represent pixel values. The
     *                     data type must be either {@code DataBuffer.TYPE_BYTE} or
     *                     {@code DataBuffer.TYPE_USHORT}.
     * @param numBands     The number of bands.
     * @param visibleBands The band to display.
     *
     * @throws IllegalArgumentException if {@code bits} is less than 1 or greater than 16.
     * @throws IllegalArgumentException if {@code size} is less than 1.
     * @throws IllegalArgumentException if {@code transferType} is not one of
     *         {@code DataBuffer.TYPE_BYTE} or {@code DataBuffer.TYPE_USHORT}.
     */
    public MultiBandsIndexColorModel(final int bits,
                                     final int size,
                                     final int[] cmap,
                                     final int start,
                                     final boolean hasAlpha,
                                     final int transparent,
                                     final int transferType,
                                     final int numBands,
                                     final int visibleBand)
    {
        super(bits, size, cmap, start, hasAlpha, transparent, transferType);
        this.numBands    = numBands;
        this.visibleBand = visibleBand;
    }

    /**
     * Returns a data element array representation of a pixel in this color model,
     * given an integer pixel representation in the default RGB color model.
     * <p>
     * This method returns an array with a length equals to the number of bands specified to
     * the constructor ({@code IndexColorModel} would returns an array of length 1). All array
     * elements are set to the same value. Replicating the pixel value is a somewhat arbitrary
     * choice, but this choice make this image appears as a gray scale image if the underlying
     * {@link DataBuffer} were displayed again with a RGB color model instead of this one. Such
     * a gray scale image seems more neutral than an image where only the Red component would vary.
     * <p>
     * All other {@code getDataElement} methods in this color model are ultimately defined in terms
     * of this method, so overriding this method should be enough.
     */
    public Object getDataElements(final int RGB, Object pixel) {
        if (pixel == null) {
            switch (transferType) {
                case DataBuffer.TYPE_SHORT:  // fall through
                case DataBuffer.TYPE_USHORT: pixel=new short[numBands]; break;
                case DataBuffer.TYPE_BYTE:   pixel=new byte [numBands]; break;
                case DataBuffer.TYPE_INT:    pixel=new int  [numBands]; break;
                default: throw new UnsupportedOperationException(unsupported());
            }
        }
        pixel = super.getDataElements(RGB, pixel);
        switch (transferType) {
            case DataBuffer.TYPE_SHORT:  // fall through
            case DataBuffer.TYPE_USHORT: {
                final short[] array = (short[]) pixel;
                Arrays.fill(array, 1, numBands, array[0]);
                break;
            }
            case DataBuffer.TYPE_BYTE: {
                final byte[] array = (byte[]) pixel;
                Arrays.fill(array, 1, numBands, array[0]);
                break;
            }
            case DataBuffer.TYPE_INT: {
                final int[] array = (int[]) pixel;
                Arrays.fill(array, 1, numBands, array[0]);
                break;
            }
            default: throw new UnsupportedOperationException(unsupported());
        }
        return pixel;
    }

    /**
     * Returns an array of unnormalized color/alpha components for a specified pixel
     * in this color model. This method is the converse of {@link #getDataElements}.
     */
    public int[] getComponents(final Object pixel, final int[] components, final int offset) {
        final int i;
        switch (transferType) {
            case DataBuffer.TYPE_SHORT:  // Fall through
            case DataBuffer.TYPE_USHORT: i=((short[])pixel)[visibleBand] & 0xffff; break;
            case DataBuffer.TYPE_BYTE:   i=((byte [])pixel)[visibleBand] & 0xff;   break;
            case DataBuffer.TYPE_INT:    i=((int  [])pixel)[visibleBand];          break;
            default: throw new UnsupportedOperationException(unsupported());
        }
        return getComponents(i, components, offset);
    }

    /**
     * Returns the red color component for the specified pixel, scaled
     * from 0 to 255 in the default RGB {@code ColorSpace}, sRGB.
     */
    public int getRed(final Object inData) {
        final int pixel;
        switch (transferType) {
            case DataBuffer.TYPE_BYTE: {
                pixel = ((byte[])inData)[visibleBand] & 0xff;
                break;
            }
            case DataBuffer.TYPE_USHORT: {
                pixel = ((short[])inData)[visibleBand] & 0xffff;
                break;
            }
            case DataBuffer.TYPE_INT: {
                pixel = ((int[])inData)[visibleBand];
                break;
            }
            default: {
               throw new UnsupportedOperationException(unsupported());
            }
        }
        return getRed(pixel);
    }

    /**
     * Returns the green color component for the specified pixel, scaled
     * from 0 to 255 in the default RGB {@code ColorSpace}, sRGB.
     */
    public int getGreen(final Object inData) {
        final int pixel;
        switch (transferType) {
            case DataBuffer.TYPE_BYTE: {
                pixel = ((byte[])inData)[visibleBand] & 0xff;
                break;
            }
            case DataBuffer.TYPE_USHORT: {
                pixel = ((short[])inData)[visibleBand] & 0xffff;
                break;
            }
            case DataBuffer.TYPE_INT: {
                pixel = ((int[])inData)[visibleBand];
                break;
            }
            default: {
               throw new UnsupportedOperationException(unsupported());
            }
        }
        return getGreen(pixel);
    }
    
    /**
     * Returns the blue color component for the specified pixel, scaled
     * from 0 to 255 in the default RGB {@code ColorSpace}, sRGB.
     */
    public int getBlue(final Object inData) {
        final int pixel;
        switch (transferType) {
            case DataBuffer.TYPE_BYTE: {
                pixel = ((byte[])inData)[visibleBand] & 0xff;
                break;
            }
            case DataBuffer.TYPE_USHORT: {
                pixel = ((short[])inData)[visibleBand] & 0xffff;
                break;
            }
            case DataBuffer.TYPE_INT: {
                pixel = ((int[])inData)[visibleBand];
                break;
            }
            default: {
               throw new UnsupportedOperationException(unsupported());
            }
        }
        return getBlue(pixel);
    }
    
    /**
     * Returns the alpha component for the specified pixel, scaled from 0 to 255.
     */
    public int getAlpha(final Object inData) {
        final int pixel;
        switch (transferType) {
            case DataBuffer.TYPE_BYTE: {
                pixel = ((byte[])inData)[visibleBand] & 0xff;
                break;
            }
            case DataBuffer.TYPE_USHORT: {
                pixel = ((short[])inData)[visibleBand] & 0xffff;
                break;
            }
            case DataBuffer.TYPE_INT: {
                pixel = ((int[])inData)[visibleBand];
                break;
            }
            default: {
               throw new UnsupportedOperationException(unsupported());
            }
        }
        return getAlpha(pixel);
    }

    /**
     * Returns an error message for unsupported operation exception.
     */
    private String unsupported() {
        return "This method has not been implemented for transferType " + transferType;
    }

    /**
     * Creates a {@code WritableRaster} with the specified width 
     * and height that has a data layout ({@code SampleModel}) 
     * compatible with this {@code ColorModel}.
     */
    public WritableRaster createCompatibleWritableRaster(final int width, final int height) {
        return Raster.createBandedRaster(transferType, width, height, numBands, null);
    }

    /**
     * Returns {@code true} if {@code raster} is compatible 
     * with this {@code ColorModel}.
     */
    public boolean isCompatibleRaster(final Raster raster) {
        return isCompatibleSampleModel(raster.getSampleModel());
    }
    
    /**
     * Creates a {@code SampleModel} with the specified 
     * width and height that has a data layout compatible with 
     * this {@code ColorModel}.  
     */
    public SampleModel createCompatibleSampleModel(final int width, final int height) {
        return new BandedSampleModel(transferType, width, height, numBands);
    }
    
    /** 
     * Checks if the specified {@code SampleModel} is compatible 
     * with this {@code ColorModel}.
     */
    public boolean isCompatibleSampleModel(final SampleModel sm) {
        return (sm instanceof ComponentSampleModel)                  &&
                sm.getTransferType()                 == transferType &&
                sm.getNumBands()                     == numBands     &&
                (1 << sm.getSampleSize(visibleBand)) >= getMapSize();
    }
}
