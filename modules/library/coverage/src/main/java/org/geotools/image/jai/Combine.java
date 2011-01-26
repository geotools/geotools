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
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.RasterFormatException;
import java.awt.image.WritableRaster;
import java.util.Vector;

// JAI and Vecmath dependencies
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.PointOpImage;
import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;
import javax.media.jai.iterator.WritableRectIter;
import javax.media.jai.operator.BandCombineDescriptor;
import javax.vecmath.MismatchedSizeException;

// Geotools dependencies
import org.geotools.resources.XArray;
import org.geotools.resources.image.ImageUtilities;


/**
 * Computes a set of arbitrary linear combinations of the bands of many rendered source images,
 * using a specified matrix. The matrix size ({@code numRows}&times;{@code numColumns}) must be
 * equals to the following:
 * <p>
 * <ul>
 *   <li>{@code numRows}: the number of desired destination bands.</li>
 *   <li>{@code numColumns}: the total number of source bands (i.e. the
 *       sum of the number of source bands in all source images) plus one.</li>
 * </ul>
 * <p>
 * The number of source bands used to determine the matrix dimensions is given by the
 * following code regardless of the type of {@link ColorModel} the sources have:
 *
 * <blockquote><pre>
 * int sourceBands = 0;
 * for (int i=0; i<sources.length; i++) {
 *     sourceBands += sources[i].getSampleModel().getNumBands();
 * }
 * </blockquote></pre>
 * 
 * The extra column in the matrix contains constant values each of which is added to the
 * respective band of the destination. The transformation is therefore defined by the pseudocode:
 *
 * <blockquote><pre>
 * // s = source pixel (not all from the same source image)
 * // d = destination pixel
 * for (int i=0; i<destBands; i++) {
 *     d[i] = matrix[i][sourceBands];
 *     for (int j=0; j<sourceBands; j++) {
 *         d[i] += matrix[i][j]*s[j];
 *     }
 * }
 * </blockquote></pre>
 *
 * In the special case where there is only one source, this method is equivalent to JAI's
 * "{@link BandCombineDescriptor BandCombine}" operation.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Remi Eve
 */
public class Combine extends PointOpImage {
    /**
     * The linear combinaison coefficients as a matrix. This matrix may not be the same
     * than the one specified to the constructor, in that the zero coefficients may have
     * been purged (and {@link #sources} and {@link #bands} arrays adjusted accordingly})
     * for performance reason.
     */
    final double[][] matrix;

    /**
     * The source to use for each elements in {@link #matrix}.
     * This matrix size must be the same than {@code matrix}.
     */
    final int[][] sources;

    /**
     * The band to use for each elements in {@link #matrix}.
     * This matrix size must be the same than {@code matrix}.
     */
    final int[][] bands;

    /**
     * The number of source samples. This is the sum of the number of bands in
     * all source images. Each {@link #matrix} row must have this length plus 1.
     */
    final int numSamples;

    /**
     * The transform to apply on sample values before the linear combinaison,
     * or {@code null} if none.
     */
    protected final CombineTransform transform;

    /**
     * Construct an image with the specified matrix.
     *
     * @param images    The rendered sources.
     * @param matrix    The linear combinaison coefficients as a matrix.
     * @param transform The transform to apply on sample values before the linear combinaison,
     *                  or {@code null} if none.
     * @param hints     The rendering hints.
     *
     * @throws MismatchedSizeException if some rows in the {@code matrix} argument doesn't
     *         have the expected length.
     */
    public Combine(final Vector           images,
                         double[][]       matrix,
                   final CombineTransform transform,
                   final RenderingHints   hints) throws MismatchedSizeException
    {
        super(images, ImageUtilities.createIntersection(
              (ImageLayout)hints.get(JAI.KEY_IMAGE_LAYOUT), images), hints, false);
        final int numRows = matrix.length;
        this.matrix       = matrix = (double[][]) matrix.clone();
        this.sources      = new int[numRows][];
        this.bands        = new int[numRows][];
        this.transform    = transform;
        int numSamples    = 0;
        for (int i=getNumSources(); --i>=0;) {
            numSamples += getSourceImage(i).getNumBands();
        }
        this.numSamples = numSamples;
        final boolean isSeparable = (transform==null) || transform.isSeparable();
        for (int j=0; j<numRows; j++) {
            final double[] row = matrix[j];
            final int numColumns = row.length;
            if (numColumns != numSamples+1) {
                throw new MismatchedSizeException();
            }
            int source   = -1;
            int band     = -1;
            int numBands = 0;
            int count    = 0; // Number of non-zero coefficients.
            final double[] copy = new double[numColumns  ];
            final int[] sources = new int   [numColumns - 1];
            final int[]   bands = new int   [numColumns - 1];
            final int numSources = sources.length;
            for (int i=0; i<numSources; i++) {
                if (++band >= numBands) {
                    band = 0;
                    numBands = getSourceImage(++source).getNumBands();
                }
                if (row[i]!=0 || !isSeparable) {
                    copy   [count] = row[i];
                    sources[count] = source;
                    bands  [count] = band;
                    count++;
                }
            }
            copy[count] = row[row.length-1];
            this.matrix [j] = XArray.resize(copy,    count+1);
            this.sources[j] = XArray.resize(sources, count  );
            this.bands  [j] = XArray.resize(bands,   count  );
        }
        /*
         * Set the sample model according the number of destination bands.
         */
        if (getNumBands() != numRows) {
            throw new UnsupportedOperationException(
                        "Automatic derivation of SampleModel not yet implemented.");
        }
        permitInPlaceOperation();
    }

    /**
     * Compute one tile.
     *
     * @param images    An array of PlanarImage sources.
     * @param dest      A WritableRaster to be filled in.
     * @param destRect  The Rectangle within the destination to be written.
     */
    public void computeRect(final PlanarImage[]  images,
                            final WritableRaster dest,
                            final Rectangle      destRect)
    {
        /*
         * Create the iterators. The 'iterRef' array will contains a copy of 'iters' where
         * the reference to an iterator is duplicated for each band in the source image:
         *
         *                   iterRef[i] = iters[sources[band][i]]
         */
        final RectIter[] iters   = new RectIter[images.length];
        final RectIter[] iterRef = new RectIter[numSamples];
        double[]         samples = null;
        final int length = iters.length;
        for (int i=0; i<length; i++) {
            iters[i] = RectIterFactory.create(images[i], mapDestRect(destRect, i));
        }
        final WritableRectIter iTarget = RectIterFactory.createWritable(dest, destRect);
        /*
         * Iterates over all destination bands. In many case, the destination image
         * will have only one band.  Consequently, it is more efficient to iterates
         * through bands in the outer loop rather than the inner loop.
         */
        int band = 0;
        iTarget.startBands();
        boolean finished = iTarget.finishedBands();
        while (!finished) {
            final double[]   row = this.matrix [band];
            final int[]    bands = this.bands  [band];
            final int[]  sources = this.sources[band];
            final int numSamples = sources.length;
            if (numSamples>this.numSamples || numSamples>bands.length || numSamples>=row.length) {
                // Should not happen if the constructor checks was right.  We performs this
                // check unconditionnaly since it is cheap, and in the hope to help the JIT
                // to remove some array bound checks later in inner loops.
                throw new AssertionError(numSamples);
            }
            for (int i=0; i<numSamples; i++) {
                iterRef[i] = iters[sources[i]];
            }
            if (samples==null || samples.length!=numSamples) {
                samples = new double[numSamples];
            }
            /*
             * Iterates over all lines, then over all pixels. The 'finished' flag is reset
             * to 'nextXXXDone()' at the end of each loop.
             */
            iTarget.startLines();
            finished = iTarget.finishedLines();
            for (int i=0; i<iters.length; i++) {
                iters[i].startLines();
                if (iters[i].finishedLines() != finished) {
                    // Should not happen, since constructor computed
                    // the intersection of all source images.
                    throw new RasterFormatException("Missing lines");
                }
            }
            while (!finished) {
                iTarget.startPixels();
                finished = iTarget.finishedPixels();
                for (int i=0; i<iters.length; i++) {
                    iters[i].startPixels();
                    if (iters[i].finishedPixels() != finished) {
                        // Should not happen, since constructor computed
                        // the intersection of all source images.
                        throw new RasterFormatException("Missing pixels");
                    }
                }
                while (!finished) {
                    /*
                     * Computes the sample values.
                     */
                    for (int i=0; i<numSamples; i++) {
                        samples[i] = iterRef[i].getSampleDouble(bands[i]);
                    }
                    if (transform != null) {
                        transform.transformSamples(samples);
                    }
                    double value = row[numSamples];
                    for (int i=0; i<numSamples; i++) {
                        value += row[i] * samples[i];
                    }
                    iTarget.setSample(value);
                    finished = iTarget.nextPixelDone();
                    for (int i=0; i<iters.length; i++) {
                        if (iters[i].nextPixelDone() != finished) {
                            // Should not happen, since constructor computed
                            // the intersection of all source images.
                            throw new RasterFormatException("Missing pixels");
                        }
                    }
                }
                finished = iTarget.nextLineDone();
                for (int i=0; i<iters.length; i++) {
                    if (iters[i].nextLineDone() != finished) {
                        // Should not happen, since constructor computed
                        // the intersection of all source images.
                        throw new RasterFormatException("Missing lines");
                    }
                }
            }
            band++;
            finished = iTarget.nextBandDone();
        }
    }


    /**
     * Optimized {@code Combine} operation for dyadic (two sources) image. This operation
     * performs a linear combinaison of two images ({@code src0} and {@code src1}).
     * The parameters {@code scale0} and {@code scale1} indicate the scale of source
     * images {@code src0} and {@code src1}. If we consider pixel at coordinate
     * (<var>x</var>,<var>y</var>), its value is determinate by the pseudo-code:
     *
     * <blockquote><pre>
     * value = src0[x][y]*scale0 + src1[x][y]*scale1 + offset
     * </pre></blockquote>
     *
     * @version $Id$
     * @author Remi Eve
     * @author Martin Desruisseaux (IRD)
     */
    final static class Dyadic extends Combine {
        /**
         * The scale of image {@code src0} for each bands.
         */
        private final double[] scales0;

        /**
         * The scale of image {@code src1} for each bands.
         */
        private final double[] scales1;

        /**
         * The offset for each bands.
         */
        private final double[] offsets;

        /**
         * Construct a new instance of {@code Combine.Dyadic}.
         *
         * @param images  The rendered sources. This vector must contains exactly 2 sources.
         * @param matrix  The linear combinaison coefficients as a matrix.
         * @param hints   The rendering hints.
         *
         * @throws MismatchedSizeException if some rows in the {@code matrix} argument doesn't
         *         have the expected length.
         */
        public Dyadic(final Vector        images,
                      final double[][]    matrix,
                      final RenderingHints hints) throws MismatchedSizeException
        {
            super(images, matrix, null, hints);
            if (getNumSources() != 2) {
                throw new IllegalArgumentException();
            }
            final int numBands = getNumBands();
            scales0 = new double[numBands];
            scales1 = new double[numBands];
            offsets = new double[numBands];
            for (int j=0; j<numBands; j++) {
                final double[]  row = this.matrix [j];
                final int[] sources = this.sources[j];
                final int[]   bands = this.bands  [j];
                for (int i=0; i<sources.length; i++) {
                    final double coeff =    row[i];
                    final int    band =   bands[i];
                    final int  source = sources[i];
                    if (band != j) {
                        throw new AssertionError(band); // Should not happen.
                    }
                    switch (source) {
                        case 0:  scales0[band] = coeff; break;
                        case 1:  scales1[band] = coeff; break;
                        default: throw new AssertionError(source); // Should not happen.
                    }
                }
                offsets[j] = row[sources.length];
            }
        }

        /**
         * Computes one tile.
         *
         * @param images    An array of PlanarImage sources.
         * @param dest      A WritableRaster to be filled in.
         * @param destRect  The Rectangle within the destination to be written.
         */
        public void computeRect(final PlanarImage[]  images,
                                final WritableRaster dest,
                                final Rectangle      destRect)
        {
            final RectIter iSrc0 = RectIterFactory.create(images[0], mapDestRect(destRect, 0));
            final RectIter iSrc1 = RectIterFactory.create(images[1], mapDestRect(destRect, 1));
            final WritableRectIter iTarget = RectIterFactory.createWritable(dest, destRect);
            int band = 0;
            iSrc0  .startBands();
            iSrc1  .startBands();
            iTarget.startBands();
            if (!iTarget.finishedBands() &&
                !iSrc0  .finishedBands() &&
                !iSrc1  .finishedBands())
            {
                final double scale0 = scales0[Math.min(band, scales0.length-1)];
                final double scale1 = scales1[Math.min(band, scales1.length-1)];
                final double offset = offsets[Math.min(band, offsets.length-1)];
                do {
                    iSrc0  .startLines();
                    iSrc1  .startLines();
                    iTarget.startLines();
                    if (!iTarget.finishedLines() &&
                        !iSrc0  .finishedLines() &&
                        !iSrc1  .finishedLines())
                    {
                        do {
                            iSrc0  .startPixels();
                            iSrc1  .startPixels();
                            iTarget.startPixels();
                            if (!iTarget.finishedPixels() &&
                                !iSrc0  .finishedPixels() &&
                                !iSrc1  .finishedPixels())
                            {
                                do {
                                    iTarget.setSample(iSrc0.getSampleDouble() * scale0 +
                                                      iSrc1.getSampleDouble() * scale1 + offset);
                                } while (!iSrc0.nextPixelDone() &&
                                         !iSrc1.nextPixelDone() &&
                                       !iTarget.nextPixelDone());
                            }
                        } while (!iSrc0.nextLineDone() &&
                                 !iSrc1.nextLineDone() &&
                               !iTarget.nextLineDone());
                    }
                    band++;
                } while (!iSrc0.nextBandDone() &&
                         !iSrc1.nextBandDone() &&
                       !iTarget.nextBandDone());
            }
        }
    }
}
