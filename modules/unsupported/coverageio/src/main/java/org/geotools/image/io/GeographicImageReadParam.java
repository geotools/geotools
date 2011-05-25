/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Locale;
import javax.imageio.ImageReader;
import javax.imageio.ImageReadParam;

import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.IndexedResourceBundle;


/**
 * Default parameters for {@link GeographicImageReader}.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class GeographicImageReadParam extends ImageReadParam {
    /**
     * The name of the default color palette to apply when none was explicitly specified.
     *
     * @see #getPaletteName
     * @see #setPaletteName
     */
    public static final String DEFAULT_PALETTE_NAME = "rainbow";

    /**
     * The name of the color palette.
     */
    private String palette;

    /**
     * The band to display.
     */
    private int visibleBand;

    /**
     * The locale for formatting error messages. Will be inferred from
     * the {@linkplain ImageReader image reader} at construction time.
     */
    private final Locale locale;

    /**
     * Creates a new, initially empty, set of parameters.
     *
     * @param reader The reader for which this parameter block is created
     */
    public GeographicImageReadParam(final ImageReader reader) {
        locale = (reader != null) ? reader.getLocale() : null;
    }

    /**
     * Returns the resources for formatting error messages.
     */
    private IndexedResourceBundle getErrorResources() {
        return Errors.getResources(locale);
    }

    /**
     * Ensures that the specified band number is valid.
     */
    private void ensureValidBand(final int band) throws IllegalArgumentException {
        if (band < 0) {
            throw new IllegalArgumentException(getErrorResources().getString(
                    ErrorKeys.BAD_BAND_NUMBER_$1, band));
        }
    }

    /**
     * Returns the band to display in the target image. In theory, images backed by
     * {@linkplain java.awt.image.IndexColorModel index color model} should have only
     * ony band. But sometime we want to load additional bands as numerical data, in
     * order to perform computations. In such case, we need to specify which band in
     * the destination image will be used as an index for displaying the colors. The
     * default value is 0.
     */
    public int getVisibleBand() {
        return visibleBand;
    }

    /**
     * Sets the band to make visible in the destination image.
     *
     * @param  visibleBand The band to make visible.
     * @throws IllegalArgumentException if the specified band index is invalid.
     */
    public void setVisibleBand(final int visibleBand) throws IllegalArgumentException {
        ensureValidBand(visibleBand);
        this.visibleBand = visibleBand;
    }

    /**
     * Returns a name of the color palette, or a {@linkplain #DEFAULT_PALETTE_NAME default name}
     * if none were explicitly specified.
     */
    final String getNonNullPaletteName() {
        final String palette = getPaletteName();
        return (palette != null) ? palette : DEFAULT_PALETTE_NAME;
    }

    /**
     * Returns the name of the color palette to apply when creating an
     * {@linkplain java.awt.image.IndexColorModel index color model}.
     * This is the name specified by the last call to {@link #setPaletteName}.
     */
    public String getPaletteName() {
        return palette;
    }

    /**
     * Sets the color palette as one of the {@linkplain PaletteFactory#getAvailableNames available
     * names} provided by the {@linkplain PaletteFactory#getDefault default palette factory}. This
     * name will be given by the {@link GeographicImageReader} default implementation to the
     * {@linkplain PaletteFactory#getDefault default palette factory} for creating a
     * {@linkplain javax.imageio.ImageTypeSpecifier image type specifier}.
     *
     * @see PaletteFactory#getAvailableNames
     */
    public void setPaletteName(final String palette) {
        this.palette = palette;
    }

    /**
     * Returns a string representation of this block of parameters.
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder(Classes.getShortClassName(this));
        buffer.append('[');
        if (sourceRegion != null) {
            buffer.append("sourceRegion=(").
                    append(sourceRegion.x).append(':').append(sourceRegion.x + sourceRegion.width).append(',').
                    append(sourceRegion.y).append(':').append(sourceRegion.y + sourceRegion.height).append("), ");
        }
        if (sourceXSubsampling != 1 || sourceYSubsampling != 1) {
            buffer.append("subsampling=(").append(sourceXSubsampling).append(',').
                    append(sourceYSubsampling).append("), ");
        }
        if (sourceBands != null) {
            buffer.append("sourceBands={");
            for (int i=0; i<sourceBands.length; i++) {
                if (i != 0) {
                    buffer.append(',');
                }
                buffer.append(sourceBands[i]);
            }
            buffer.append("}, ");
        }
        buffer.append("palette=\"").append(palette).append("\", ").append(']');
        return buffer.toString();
    }
}
