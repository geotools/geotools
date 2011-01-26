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

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Map;

import javax.media.jai.AreaOpImage;
import javax.media.jai.ImageLayout;
import javax.media.jai.PlanarImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;


/**
 * Replaces {@link Double#NaN} values by the weighted average of neighbors values.
 * This operation use a box of {@code size}&times{@code size} pixels centered on
 * each {@code NaN} value. The weighted average is then computed, ignoring all
 * {@code NaN} values. If the number of valid values is greater than
 * {@code validityThreshold}, then the center {@code NaN} is replaced
 * by the computed average. Otherwise, the {@code NaN} value is left unchanged.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Lionel Flahaut (2ie Technologie, IRD)
 * @author Martin Desruisseaux
 */
public class NodataFilter extends AreaOpImage {
    /**
     * Shared instance of {@link #distances} for the common case where {@code padding==1}.
     */
    private static double[] sharedDistances;

    /**
     * Pre-computed distances. Used in order to avoid a huge amount of calls to
     * {@link Math#sqrt} in {@link #computeRect}.
     */
    private final double[] distances;

    /**
     * The minimal number of valid neighbors required in order to consider the average as valid.
     */
    private final int validityThreshold;

    /**
     * Constructs a new operation.
     *
     * @param source   The source image.
     * @param layout   The image layout.
     * @param map      The image properties and rendering hints.
     * @param padding  The number of pixel above, below, to the left and to the right of central
     *                 {@code NaN} pixel. The full box size is {@code padding}&times;2+1.
     * @param validityThreshold The minimal number of valid neighbors required in order to consider
     *                the average as valid.
     */
    protected NodataFilter(final RenderedImage source,
                           final ImageLayout   layout,
                           final Map           map,
                           final int           padding,
                           final int validityThreshold)
    {
        super(source, layout, map, false, null, padding, padding, padding, padding);
        this.validityThreshold = validityThreshold;
        /*
         * Computes the array of distances once for ever. For the special case where the padding
         * equals 1, we will try to reuse the same array for all NodataFilter instances.
         */
        if (padding==1 && sharedDistances!=null) {
            distances = sharedDistances;
        } else {
            distances = new double[(leftPadding+rightPadding+1) * (topPadding+bottomPadding+1)];
            int index = 0;
            for (int dy=-topPadding; dy<=bottomPadding; dy++) {
                for (int dx=-leftPadding; dx<=rightPadding; dx++) {
                    distances[index++] = Math.sqrt(dx*dx + dy*dy);
                }
            }
            assert index == distances.length;
            if (padding == 1) {
                sharedDistances = distances;
            }
        }
    }

    /**
     * Computes a rectangle of outputs.
     */
    protected void computeRect(final PlanarImage[] sources,
                               final WritableRaster   dest,
                               final Rectangle    destRect)
    {
        assert sources.length == 1;
        final PlanarImage source = sources[0];
        Rectangle sourceRect = mapDestRect(destRect, 0);
        sourceRect = sourceRect.intersection(source.getBounds());
        final RandomIter iter = RandomIterFactory.create(source, sourceRect);
        final int minX = destRect.x;                 // Minimum inclusive
        final int minY = destRect.y;                 // Minimum inclusive
        final int maxX = destRect.width  + minX;     // Maximum exclusive
        final int maxY = destRect.height + minY;     // Maximum exclusive
        final int hPad = leftPadding+rightPadding+1; // Horizontal padding
        for (int band=source.getNumBands(); --band>=0;) {
            for (int y=minY; y<maxY; y++) {
                final int minScanY = Math.max(minY, y -    topPadding   ); // Inclusive
                final int maxScanY = Math.min(maxY, y + bottomPadding +1); // Exclusive
                final int minScanI = (minScanY - (y-topPadding)) * hPad;
                assert minScanI>=0 && minScanI<=distances.length : minScanI;
                for (int x=minX; x<maxX; x++) {
                    final double current = iter.getSampleDouble(x, y, band);
                    if (!Double.isNaN(current)) {
                        /*
                         * Pixel is already valid: no operation here.
                         */
                        dest.setSample(x, y, band, current);
                        continue;
                    }
                    /*
                     * Computes the average and set the value if the amount of
                     * valid pixels is at least equals to the threshold amount.
                     */
                    int       count       = 0; // Number of valid values.
                    double    sumValue    = 0; // Weighted sum of values.
                    double    sumDistance = 0; // Sum of distances of valid values.
                    final int minScanX    = Math.max(minX, x -  leftPadding   ); // Inclusive
                    final int maxScanX    = Math.min(maxX, x + rightPadding +1); // Exclusive
                    final int lineOffset  = hPad - (maxScanX-minScanX);
                    int index = minScanI + (minScanX - (x-leftPadding));
                    for (int sy=minScanY; sy<maxScanY; sy++) {
                        for (int sx=minScanX; sx<maxScanX; sx++) {
                            final double scan = iter.getSampleDouble(sx, sy, band);
                            if (!Double.isNaN(scan)) {
                                final double distance = distances[index];
                                assert (Math.abs(distance-Math.hypot(sx-x, sy-y)) < 1E-6) &&
                                       (distance > 0) : distance;
                                sumValue    += distance*scan;
                                sumDistance += distance;
                                count++;
                            }
                            index++;
                        }
                        index += lineOffset;
                    }
                    dest.setSample(x, y, band,
                                   (count>=validityThreshold) ? sumValue/sumDistance : current);
                }
            }
        }
        iter.done();
    }
}
