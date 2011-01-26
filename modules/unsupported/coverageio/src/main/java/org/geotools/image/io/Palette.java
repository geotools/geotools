/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import javax.imageio.IIOException;
import javax.imageio.ImageTypeSpecifier;
import javax.swing.JFrame;

import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;


/**
 * A set of RGB colors created by a {@linkplain PaletteFactory palette factory} from
 * a {@linkplain #name}. A palette can creates a {@linkplain ColorModel color model}
 * (often {@linkplain IndexColorModel indexed}) or an {@linkplain ImageTypeSpecifier
 * image type specifier} from the RGB colors. The color model is retained by the palette
 * as a {@linkplain WeakReference weak reference} (<strong>not</strong> as a {@linkplain
 * java.lang.ref.SoftReference soft reference}) because it may consume up to 256 kilobytes.
 * The purpose of the weak reference is to share existing instances in order to reduce
 * memory usage; the purpose is not to provide caching.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Antoine Hnawia
 * @author Martin Desruisseaux (IRD)
 */
public abstract class Palette {
    /**
     * The originating factory.
     */
    final PaletteFactory factory;

    /**
     * The name of this palette.
     */
    protected final String name;

    /**
     * The number of bands in the {@linkplain ColorModel color model}.
     * The value is 1 in the vast majority of cases.
     */
    protected final int numBands;

    /**
     * The band to display, in the range 0 inclusive to {@link #numBands} exclusive.
     * This is used when an image contains more than one band but only one band can
     * be used for computing the colors to display. For example {@link IndexColorModel}
     * works on only one band.
     */
    protected final int visibleBand;

    /**
     * The sample model to be given to {@link ImageTypeSpecifier}.
     */
    private transient SampleModel samples;

    /**
     * A weak reference to the color model. This color model may consume a significant
     * amount of memory (up to 256 kb). Consequently, we will prefer {@link WeakReference}
     * over {@link java.lang.ref.SoftReference}. The purpose of this weak reference is to
     * share existing instances, not to cache it since it is cheap to rebuild.
     */
    private transient Reference<ColorModel> colors;

    /**
     * A weak reference to the image specifier to be returned by {@link #getImageTypeSpecifier}.
     * We use weak reference because the image specifier contains a reference to the color model
     * and we don't want to prevent it to be garbage collected. See {@link #colors} for an
     * explanation about why we use weak instead of soft references.
     */
    private transient Reference<ImageTypeSpecifier> specifier;

    /**
     * Creates a palette with the specified name.
     *
     * @param factory     The originating factory.
     * @param name        The palette name.
     * @param numBands    The number of bands (usually 1) to assign to {@link #numBands}.
     * @param visibleBand The visible band (usually 0) to assign to {@link #visibleBand}.
     */
    protected Palette(final PaletteFactory factory, final String name,
                      final int numBands, final int visibleBand)
    {
        if (factory == null) {
            // Can't use factory.getErrorResources() here.
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.NULL_ARGUMENT_$1, "factory"));
        }
        if (name == null) {
            throw new IllegalArgumentException(factory.getErrorResources().getString(
                    ErrorKeys.NULL_ARGUMENT_$1, "name"));
        }
        ensureInsideBounds(numBands, 0, 255); // This maximal value is somewhat arbitrary.
        ensureInsideBounds(visibleBand, 0, numBands-1);
        this.factory     = factory;
        this.name        = name.trim();
        this.numBands    = numBands;
        this.visibleBand = visibleBand;
    }

    /**
     * Ensures that the specified values in inside the expected bounds (inclusives).
     *
     * @throws IllegalArgumentException if the specified values are outside the bounds.
     */
    final void ensureInsideBounds(final int value, final int min, final int max)
            throws IllegalArgumentException
    {
        if (value < min || value > max) {
            throw new IllegalArgumentException(factory.getErrorResources().getString(
                    ErrorKeys.VALUE_OUT_OF_BOUNDS_$3, value, min, max));
        }
    }

    /**
     * Returns the scale from <cite>normalized values</cite> (values in the range [0..1])
     * to values in the range of this palette.
     */
    double getScale() {
        return 1;
    }

    /**
     * Returns the offset from <cite>normalized values</cite> (values in the range [0..1])
     * to values in the range of this palette.
     */
    double getOffset() {
        return 0;
    }

    /**
     * Returns the color model for this palette. This method tries to reuse existing
     * color model if possible, since it may consume a significant amount of memory.
     *
     * @throws  FileNotFoundException If the RGB values need to be read from a file and this file
     *          (typically inferred from {@link #name}) is not found.
     * @throws  IOException  If an other find of I/O error occured.
     * @throws  IIOException If an other kind of error prevent this method to complete.
     */
    public synchronized ColorModel getColorModel() throws IOException {
        if (colors != null) {
            final ColorModel candidate = colors.get();
            if (candidate != null) {
                return candidate;
            }
        }
        return getImageTypeSpecifier().getColorModel();
    }

    /**
     * Returns the image type specifier for this palette.
     *
     * @throws  FileNotFoundException If the RGB values need to be read from a file and this file
     *          (typically inferred from {@link #name}) is not found.
     * @throws  IOException  If an other find of I/O error occured.
     * @throws  IIOException If an other kind of error prevent this method to complete.
     */
    public abstract ImageTypeSpecifier getImageTypeSpecifier() throws IOException;

    /**
     * Returns the image type specifier from the cache, or {@code null}.
     */
    final ImageTypeSpecifier queryCache() {
        if (specifier != null) {
            final ImageTypeSpecifier candidate = specifier.get();
            if (candidate != null) {
                return candidate;
            }
        }
        if (samples!=null && colors!=null) {
            final ColorModel candidate = colors.get();
            if (candidate != null) {
                final ImageTypeSpecifier its = new ImageTypeSpecifier(candidate, samples);
                specifier = new WeakReference<ImageTypeSpecifier>(its);
                return its;
            }
        }
        return null;
    }

    /**
     * Puts the specified image specifier in the cache.
     */
    final void cache(final ImageTypeSpecifier its) {
        samples   = its.getSampleModel();
        colors    = new PaletteDisposer.Reference(this, its.getColorModel());
        specifier = new WeakReference<ImageTypeSpecifier>(its);
    }

    /**
     * Returns the color palette as an image of the specified size.
     * This is useful for looking visually at a color palette.
     *
     * @param size The image size. The palette will be vertical if
     *        <code>size.{@linkplain Dimension#height height}</code> &gt;
     *        <code>size.{@linkplain Dimension#width  width }</code>
     *
     * @throws IOException if the color values can't be read.
     */
    public RenderedImage getImage(final Dimension size) throws IOException {
        final IndexColorModel colors;
        final BufferedImage   image;
        final WritableRaster  raster;
        colors = (IndexColorModel) getColorModel();
        raster = colors.createCompatibleWritableRaster(size.width, size.height);
        image  = new BufferedImage(colors, raster, false, null);
        int xmin   = raster.getMinX();
        int ymin   = raster.getMinY();
        int width  = raster.getWidth();
        int height = raster.getHeight();
        final boolean horizontal = size.width >= size.height;
        // Computation will be performed as if the image were horizontal.
        // If it is not, interchanges x and y values.
        if (!horizontal) {
            int tmp;
            tmp = xmin;  xmin  = ymin;   ymin   = tmp;
            tmp = width; width = height; height = tmp;
        }
        final int xmax = xmin + width;
        final int ymax = ymin + height;
        final double scale  = getScale() / width;
        final double offset = getOffset();
        for (int x=xmin; x<xmax; x++) {
            final double value = offset + scale*(x-xmin);
            for (int y=ymin; y<ymax; y++) {
                if (horizontal) {
                    raster.setSample(x, y, 0, value);
                } else {
                    raster.setSample(y, x, 0, value);
                }
            }
        }
        return image;
    }

    /**
     * Shows the palette in a windows. This is mostly for debugging purpose.
     *
     * @throws IOException if the color values can't be read.
     */
    @SuppressWarnings("deprecation")
    public void show() throws IOException {
        final JFrame frame = new JFrame(toString());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new javax.media.jai.widget.ImageCanvas(getImage(new Dimension(256, 32))));
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Returns a hash value for this palette.
     */
    @Override
    public int hashCode() {
        return name.hashCode() + 37*numBands + 17*visibleBand;
    }

    /**
     * Compares this palette with the specified object for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object != null && getClass().equals(object.getClass())) {
            final Palette that = (Palette) object;
            return this.numBands    == that.numBands    &&
                   this.visibleBand == that.visibleBand &&
                   Utilities.equals(this.name, that.name);
            /*
             * Note: we do not compare PaletteFactory on purpose, since two instances could be
             * identical except for the locale to use for formatting error messages.   Because
             * Palettes are used as keys in the PaletteFactory.palettes pool, we don't want to
             * get duplicated palettes only because they format error messages differently.
             */
        }
        return false;
    }
}
