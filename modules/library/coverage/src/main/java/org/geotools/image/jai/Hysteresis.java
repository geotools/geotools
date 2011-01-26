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
package org.geotools.image.jai;

// J2SE dependencies
import java.awt.Rectangle;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Map;

// JAI dependencies
import javax.media.jai.ImageLayout;
import javax.media.jai.UntiledOpImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;


/**
 * Effectue un seuillage par hysteresis sur une image.
 * L'opération de seuillage s'effectue de la manière suivante:
 * <p>
 * On dispose d'un seuil haut, <var>sh</var>, et d'un seuil bas, <var>sb</var>.
 * Si la valeur d'un pixel est supérieur à <var>sh</var>, on la conserve, elle
 * nous interesse. Si cette valeur est inférieure à <var>sb</var>, on la supprime.
 * Si elle est entre les deux on dit le pixel indeterminé et on ne le conserve que
 * s'il est proche d'un pixel dont la valeur est au dessus de <var>sh</var>, ou
 * proche d'un pixel indéterminé que l'on a précédement trouvé proche d'un pixel
 * de valeur supérieure à <var>sh</var>. Cette recherche se fait de manière itérative,
 * jusqu'à ce que le point indéterminé n'est plus de voisins satisfaisants.
 * 
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Lionel Flahaut (2ie Technologie, IRD)
 * @author Martin Desruisseaux
 */
public class Hysteresis extends UntiledOpImage {
    /**
     * The low threshold value, inclusive.
     */
    private final double low;

    /**
     * The high threshold value, inclusive.
     */
    private final double high;

    /**
     * The value to give to filtered pixel.
     */
    private final double padValue;

    /**
     * Constructs a new Hysterisis filter for the given image.
     *
     * @param source   The source image.
     * @param layout   The image layout.
     * @param map      The rendering hints and image properties.
     * @param low      The low threshold value, inclusive.
     * @param high     The high threshold value, inclusive.
     * @param padValue The value to give to filtered pixel.
     */
    protected Hysteresis(final RenderedImage source,
                         final ImageLayout   layout,
                         final Map           map,
                         final double        low,
                         final double        high,
                         final double        padValue)
    {
        super(source, map, layout);
        this.low      = low;
        this.high     = high;
        this.padValue = padValue;
    }

    /**
     * Computes the whole image.
     */
    protected void computeImage(final Raster[]    sources,
                                final WritableRaster dest,
                                final Rectangle  destRect)
    {
        assert sources.length == 1;
        final Raster source = sources[0];
        Rectangle sourceRect = mapDestRect(destRect, 0);
        sourceRect = sourceRect.intersection(source.getBounds());
        final RandomIter iter = RandomIterFactory.create(source, sourceRect);
        final int minX = destRect.x;              // Minimum inclusive
        final int minY = destRect.y;              // Minimum inclusive
        final int maxX = destRect.width  + minX;  // Maximum exclusive
        final int maxY = destRect.height + minY;  // Maximum exclusive
        final int w    = width -1;
        final int h    = height-1;
        final boolean[] accepted = new boolean[destRect.width * destRect.height];
        final boolean[] rejected = new boolean[destRect.width * destRect.height];
        for (int band=source.getNumBands(); --band>=0;) {
            /*
             * Find immediately all accepted values (above the high threshold) and rejected values
             * (below the low threshold).    NaN values are both accepted and rejected ("accepted"
             * since they are going to be copied in the destination image, and "rejected" since
             * they do not cause the acceptation of neighbor values).
             */
            int index = 0;
            for (int y=minY; y<maxY; y++) {
                for (int x=minX; x<maxX; x++) {
                    final double current = iter.getSampleDouble(x, y, band);
                    accepted[index] = !(current < high); // Accept NaN values
                    rejected[index] = !(current >= low); // Accept NaN values
                    index++;
                }
            }
            assert index == accepted.length;
            /*
             * Complete the mask of "accepted" values. Unknow values (those which are neither
             * accepted or rejected) are tested for their proximity with an accepted value.
             * This loop will be run until there is no change.
             */
            int sign = +1;
            boolean changed;
            do {
                changed = false;
                final int stop;
                if (sign >= 0) {
                    index = 0;
                    stop  = accepted.length;
                } else {
                    index = accepted.length-1;
                    stop  = -1;
                }
                while (index != stop) {
                    if (!accepted[index] && !rejected[index]) {
                        int check;
                        final int y = index / width;
                        final int x = index % width;
                        if ((x!=0 && ((accepted[check=index-1      ] && !rejected[check])   ||
                            (y!=0 &&   accepted[check=index-1-width] && !rejected[check])   ||
                            (y!=h &&   accepted[check=index-1+width] && !rejected[check]))) ||
                            (x!=w && ((accepted[check=index+1      ] && !rejected[check])   ||
                            (y!=0 &&   accepted[check=index+1-width] && !rejected[check])   ||
                            (y!=h &&   accepted[check=index+1+width] && !rejected[check]))) ||
                            (y!=0 && ((accepted[check=index  -width] && !rejected[check]))) ||
                            (y!=w && ((accepted[check=index  +width] && !rejected[check]))))
                        {
                            accepted[index] = true;
                            changed = true;
                        }
                    }
                    index += sign;
                }
                sign = -sign;
            } while (changed);
            /*
             * Copy all accepted values in the destination raster.
             * Other values are replaced by NaN.
             */
            index = 0;
            for (int y=minY; y<maxY; y++) {
                for (int x=minX; x<maxX; x++) {
                    dest.setSample(x, y, band,
                                   accepted[index++] ? iter.getSampleDouble(x, y, band) : padValue);
                }
            }
            assert index == accepted.length;
        }
        iter.done();
    }
}
