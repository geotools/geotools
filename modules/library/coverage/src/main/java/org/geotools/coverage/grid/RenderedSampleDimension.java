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
package org.geotools.coverage.grid;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.util.Arrays;
import javax.measure.unit.Unit;

import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;

import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.SampleDimensionType;
import org.opengis.util.InternationalString;

import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.factory.Hints;
import org.geotools.referencing.operation.transform.LinearTransform1D;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.util.NumberRange;
import org.geotools.util.SimpleInternationalString;


/**
 * Describes the band values for a grid coverage.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class RenderedSampleDimension extends GridSampleDimension {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 946331925096804779L;

    /**
     * Band number for this sample dimension.
     */
    private final int band;

    /**
     * The number of bands in the {@link GridCoverage} who own this sample dimension.
     */
    private final int numBands;

    /**
     * The grid value data type.
     */
    private final SampleDimensionType type;

    /**
     * Constructs a sample dimension with a set of categories from an other sample dimension.
     *
     * @param band  The originating sample dimension.
     * @param image The image to be wrapped by {@link GridCoverage}.
     * @param bandNumber The band number.
     */
    private RenderedSampleDimension(final GridSampleDimension band,
                                  final RenderedImage      image,
                                  final int           bandNumber)
    {
        super(band);
        final SampleModel model = image.getSampleModel();
        this.band     = bandNumber;
        this.numBands = model.getNumBands();
        this.type     = TypeMap.getSampleDimensionType(model, bandNumber);
    }

    /**
     * Creates a set of sample dimensions for the given image. The array length of both
     * arguments must matches the number of bands in the supplied {@code image}.
     *
     * @param  name  The name for data (e.g. "Elevation").
     * @param  image The image for which to create a set of sample dimensions.
     * @param  src   User-provided sample dimensions, or {@code null} if none.
     * @param  dst   The array where to put sample dimensions.
     * @return {@code true} if all sample dimensions are geophysics (quantitative), or
     *         {@code false} if all sample dimensions are non-geophysics (qualitative).
     * @throws IllegalArgumentException if geophysics and non-geophysics dimensions are mixed.
     */
    static boolean create(final CharSequence        name,
                          final RenderedImage       image,
                          final GridSampleDimension[] src,
                          final GridSampleDimension[] dst)
    {
        final int numBands = image.getSampleModel().getNumBands();
        if (src!=null && src.length!=numBands) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NUMBER_OF_BANDS_MISMATCH_$3,
                      numBands, src.length, "SampleDimension"));
        }
        if (dst.length != numBands) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NUMBER_OF_BANDS_MISMATCH_$3,
                      numBands, dst.length, "SampleDimension"));
        }
        /*
         * Now, we know that the number of bands and the array length are consistent.
         * Search if there is any null SampleDimension. If any, replace the null value
         * by a default SampleDimension. In all cases, count the number of geophysics
         * and non-geophysics sample dimensions.
         */
        int countGeophysics = 0;
        int countIndexed    = 0;
        GridSampleDimension[] defaultSD = null;
        for (int i=0; i<numBands; i++) {
            GridSampleDimension sd = (src!=null) ? src[i] : null;
            if (sd == null) {
                /*
                 * If the user didn't provided explicitly a SampleDimension, create a default one.
                 * We will creates a SampleDimension for all bands in one step, even if only a few
                 * of them are required.
                 */
                if (defaultSD == null) {
                    defaultSD = new GridSampleDimension[numBands];
                    create(name, RectIterFactory.create(image, null), image.getSampleModel(),
                           null, null, null, null, defaultSD, null);
                }
                sd = defaultSD[i];
            }
            sd = new RenderedSampleDimension(sd, image, i);
            dst[i] = sd;
            if (sd.geophysics(true ) == sd) countGeophysics++;
            if (sd.geophysics(false) == sd) countIndexed++;
        }
        if (countGeophysics == numBands) {
            return true;
        }
        if (countIndexed == numBands) {
            return false;
        }
        throw new IllegalArgumentException(Errors.format(ErrorKeys.MIXED_CATEGORIES));
    }

    /**
     * Creates a set of sample dimensions for the given raster.
     *
     * @param  name The name for data (e.g. "Elevation").
     * @param  raster The raster.
     * @param  min The minimal value for each bands, or {@code null} for computing it automatically.
     * @param  max The maximal value for each bands, or {@code null} for computing it automatically.
     * @param  units The units of sample values, or {@code null} if unknow.
     * @param  colors The colors to use for values from {@code min} to {@code max} for each
     *         bands, or {@code null} for a default color palette. If non-null, each arrays
     *         {@code colors[b]} may have any length; colors will be interpolated as needed.
     * @param  hints An optional set of rendering hints, or {@code null} if none. Those hints will
     *         not affect the sample dimensions to be created. However, they may affect the sample
     *         dimensions to be returned by <code>{@link #geophysics geophysics}(false)</code>, i.e.
     *         the view to be used at rendering time. The optional hint
     *         {@link Hints#SAMPLE_DIMENSION_TYPE} specifies the {@link SampleDimensionType}
     *         to be used at rendering time, which can be one of
     *         {@link SampleDimensionType#UBYTE UBYTE} or
     *         {@link SampleDimensionType#USHORT USHORT}.
     * @return The sample dimension for the given raster.
     */
    static GridSampleDimension[] create(final CharSequence   name,
                                        final Raster         raster,
                                        final double[]       min,
                                        final double[]       max,
                                        final Unit<?>        units,
                                        final Color[][]      colors,
                                        final RenderingHints hints)
    {
        final GridSampleDimension[] dst = new GridSampleDimension[raster.getNumBands()];
        create(name, (min==null || max==null) ? RectIterFactory.create(raster, null) : null,
               raster.getSampleModel(), min, max, units, colors, dst, hints);
        return dst;
    }

    /**
     * Creates a set of sample dimensions for the data backing the given iterator.
     *
     * @param  name The name for data (e.g. "Elevation").
     * @param  iterator The iterator through the raster data, or {@code null}.
     * @param  model The image or raster sample model.
     * @param  min The minimal value, or {@code null} for computing it automatically.
     * @param  max The maximal value, or {@code null} for computing it automatically.
     * @param  units The units of sample values, or {@code null} if unknow.
     * @param  colors The colors to use for values from {@code min} to {@code max} for each bands,
     *         or {@code null} for a default color palette. If non-null, each arrays
     *         {@code colors[b]} may have any length; colors will be interpolated as needed.
     * @param  dst The array where to store sample dimensions. The array length must matches
     *         the number of bands.
     * @param  hints An optional set of rendering hints, or {@code null} if none.
     *         Those hints will not affect the sample dimensions to be created. However,
     *         they may affect the sample dimensions to be returned by
     *         <code>{@link #geophysics geophysics}(false)</code>, i.e.
     *         the view to be used at rendering time. The optional hint
     *         {@link Hints#SAMPLE_DIMENSION_TYPE} specifies the {@link SampleDimensionType}
     *         to be used at rendering time, which can be one of
     *         {@link SampleDimensionType#UBYTE UBYTE} or
     *         {@link SampleDimensionType#USHORT USHORT}.
     */
    private static void create(final CharSequence          name,
                               final RectIter              iterator,
                               final SampleModel           model,
                               double[]                    min,
                               double[]                    max,
                               final Unit<?>               units,
                               final Color[][]             colors,
                               final GridSampleDimension[] dst,
                               final RenderingHints        hints)
    {
        final int numBands = dst.length;
        if (min!=null && min.length != numBands) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NUMBER_OF_BANDS_MISMATCH_$3,
                      numBands, min.length, "min[i]"));
        }
        if (max!=null && max.length != numBands) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NUMBER_OF_BANDS_MISMATCH_$3,
                      numBands, max.length, "max[i]"));
        }
        if (colors!=null && colors.length != numBands) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NUMBER_OF_BANDS_MISMATCH_$3,
                      numBands, colors.length, "colors[i]"));
        }
        /*
         * Arguments are know to be valids. We now need to compute two ranges:
         *
         * STEP 1: Range of target (sample) values. This is computed in the following block.
         * STEP 2: Range of source (geophysics) values. It will be computed one block later.
         *
         * The target (sample) values will typically range from 0 to 255 or 0 to 65535, but the
         * general case is handled as well. If the source (geophysics) raster uses floating point
         * numbers, then a "nodata" category may be added in order to handle NaN values. If the
         * source raster use integer numbers instead, then we will rescale samples only if they
         * would not fit in the target data type.
         */
        final SampleDimensionType sourceType = TypeMap.getSampleDimensionType(model, 0);
        final boolean          sourceIsFloat = TypeMap.isFloatingPoint(sourceType);
        SampleDimensionType targetType = null;
        if (hints != null) {
            targetType = (SampleDimensionType) hints.get(Hints.SAMPLE_DIMENSION_TYPE);
        }
        if (targetType == null) {
            // Default to TYPE_BYTE for floating point images only; otherwise keep unchanged.
            targetType = sourceIsFloat ? SampleDimensionType.UNSIGNED_8BITS : sourceType;
        }
        // Default setting: no scaling
        final boolean targetIsFloat = TypeMap.isFloatingPoint(targetType);
        NumberRange   targetRange   = TypeMap.getRange(targetType);
        Category[]    categories    = new Category[1];
        final boolean needScaling;
        if (targetIsFloat) {
            // Never rescale if the target is floating point numbers.
            needScaling = false;
        } else if (sourceIsFloat) {
            // Always rescale for "float to integer" conversions. In addition,
            // Use 0 value as a "no data" category for unsigned data type only.
            needScaling = true;
            if (!TypeMap.isSigned(targetType)) {
                categories    = new Category[2];
                categories[1] = Category.NODATA;
                targetRange   = TypeMap.getPositiveRange(targetType);
            }
        } else {
            // In "integer to integer" conversions, rescale only if
            // the target range is smaller than the source range.
            needScaling = !targetRange.contains(TypeMap.getRange(sourceType));
        }
        /*
         * Computes the minimal and maximal values, if not explicitely provided.
         * This information is required for determining the range of geophysics
         * values.
         */
        if (needScaling && (min==null || max==null)) {
            final boolean computeMin;
            final boolean computeMax;
            if (computeMin = (min == null)) {
                min = new double[numBands];
                Arrays.fill(min, Double.POSITIVE_INFINITY);
            }
            if (computeMax = (max == null)) {
                max = new double[numBands];
                Arrays.fill(max, Double.NEGATIVE_INFINITY);
            }
            int b = 0;
            iterator.startBands();
            if (!iterator.finishedBands()) do {
                iterator.startLines();
                if (!iterator.finishedLines()) do {
                    iterator.startPixels();
                    if (!iterator.finishedPixels()) do {
                        final double z = iterator.getSampleDouble();
                        if (computeMin && z<min[b]) min[b]=z;
                        if (computeMax && z>max[b]) max[b]=z;
                    } while (!iterator.nextPixelDone());
                } while (!iterator.nextLineDone());
                if (computeMin && computeMax) {
                    if (!(min[b] < max[b])) {
                        min[b] = 0;
                        max[b] = 1;
                    }
                }
                b++;
            } while (!iterator.nextBandDone());
        }
        /*
         * Now, constructs the sample dimensions. We will inconditionnaly provides a "nodata"
         * category for floating point images targeting unsigned integers, since we don't know
         * if the user plan to have NaN values. Even if the current image doesn't have NaN values,
         * it could have NaN later if the image uses a writable raster.
         */
        final InternationalString n = SimpleInternationalString.wrap(name);
        NumberRange sourceRange = TypeMap.getRange(sourceType);
        for (int b=0; b<numBands; b++) {
            final Color[] c = colors!=null ? colors[b] : null;
            if (needScaling) {
                sourceRange = NumberRange.create(min[b], max[b]).castTo(sourceRange.getElementClass());
                categories[0] = new Category(n, c, targetRange, sourceRange);
            } else {
                categories[0] = new Category(n, c, targetRange, LinearTransform1D.IDENTITY);
            }
            dst[b] = new GridSampleDimension(name,categories, units).geophysics(true);
        }
    }

    /**
     * Returns a code value indicating grid value data type.
     * This will also indicate the number of bits for the data type.
     *
     * @return a code value indicating grid value data type.
     */
    @Override
    public SampleDimensionType getSampleDimensionType() {
        return type;
    }

    /**
     * Returns the color interpretation of the sample dimension.
     */
    @Override
    public ColorInterpretation getColorInterpretation() {
        return TypeMap.getColorInterpretation(getColorModel(), band);
    }

    /**
     * Returns a color model for this sample dimension.
     */
    @Override
    public ColorModel getColorModel() {
        return getColorModel(band, numBands);
    }

    /**
     * Returns the minimum value occurring in this sample dimension.
     */
    //  public double getMinimumValue()
    //  {return getHistogram().getLowValue(band);}

    /**
     * Returns the maximum value occurring in this sample dimension.
     */
    //  public double getMaximumValue()
    //  {return getHistogram().getHighValue(band);}

    /**
     * Determine the mode grid value in this sample dimension.
     */
    //  public double getModeValue()
    //  {throw new UnsupportedOperationException("Not implemented");}

    /**
     * Determine the median grid value in this sample dimension.
     */
    //  public double getMedianValue()
    //  {throw new UnsupportedOperationException("Not implemented");}

    /**
     * Determine the mean grid value in this sample dimension.
     */
    //  public double getMeanValue()
    //  {return getHistogram().getMean()[band];}

    /**
     * Determine the standard deviation from the mean
     * of the grid values in a sample dimension.
     */
    //  public double getStandardDeviation()
    //  {return getHistogram().getStandardDeviation()[band];}

    /**
     * Gets the histogram for the underlying grid coverage.
     */
    //  private Histogram getHistogram()
    //  {throw new UnsupportedOperationException("Not implemented");}
}
