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
package org.geotools.coverage.processing;

import java.awt.RenderingHints;

import javax.media.jai.Interpolation;
import javax.media.jai.KernelJAI;

import org.geotools.coverage.processing.operation.Resample;
import org.geotools.factory.Hints;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.coverage.processing.Operation;
import org.opengis.coverage.processing.OperationNotFoundException;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.InvalidParameterNameException;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;


/**
 * Convenience, type-safe, methods for applying some common operations on
 * {@linkplain Coverage coverage} objects. All methods wrap their arguments in a
 * {@linkplain ParameterValueGroup parameter value group} and delegate the work to the processor's
 * {@link CoverageProcessor#doOperation doOperation} method.
 * This convenience class do not brings any new functionalities, but brings type-safety when the
 * operation is know at compile time. For operation unknown at compile time (e.g. for an operation
 * selected by users in some widget), use the {@linkplain CoverageProcessor processor} directly.
 *
 * @since 2.2
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see org.geotools.coverage.processing.operation
 */
public class Operations {
    /**
     * The default instance.
     */
    public static final Operations DEFAULT = new Operations(null);

    /**
     * The processor to use for applying operations. If null, will be created only when first
     * needed.
     *
     * @todo Uses the GeoAPI's interface instead once it will be ready.
     */
    private CoverageProcessor processor;

    /**
     * Creates a new instance using the specified hints.
     *
     * @param hints The hints, or {@code null} if none.
     */
    public Operations(final RenderingHints hints) {
        if (hints != null && !hints.isEmpty()) {
            processor = CoverageProcessor.getInstance(new Hints(hints));
        }
        // Otherwise, will creates the processor only when first needed.
    }




    /////////////////////////////////////////////////////////////////////////////////
    ////////                                                                 ////////
    ////////            A R I T H M E T I C   O P E R A T I O N S            ////////
    ////////                                                                 ////////
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * Adds constants (one for each band) to every sample values of the source coverage.
     *
     * @param source The source coverage.
     * @param constants The constants to add to each band.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.AddConst
     */
    public Coverage add(final Coverage source, final double[] constants)
            throws CoverageProcessingException
    {
        return doOperation("AddConst", source, "constants", constants);
    }
    
    /**
     * Returns the sum between two coverages
     *
     * @param source0 The first source coverage.
     * @param source1 The second source coverage.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Add
     */
    public Coverage add(final Coverage source0, final Coverage source1)
            throws CoverageProcessingException
    {
        return doOperation("Add", source0, source1);
    }
    
    /**
     * Returns the multiplication between two coverages
     *
     * @param source0 The first source coverage.
     * @param source1 The second source coverage.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Multiply
     */
    public Coverage multiply(final Coverage source0, final Coverage source1)
            throws CoverageProcessingException
    {
        return doOperation("Multiply", source0, source1);
    }

    /**
     * Subtracts constants (one for each band) from every sample values of the source coverage.
     *
     * @param source The source coverage.
     * @param constants The constants to subtract to each band.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.SubtractConst
     */
    public Coverage subtract(final Coverage source, final double[] constants)
            throws CoverageProcessingException
    {
        return doOperation("SubtractConst", source, "constants", constants);
    }

    /**
     * Subtracts every sample values of the source coverage from constants (one for each band).
     *
     * @param source The source coverage.
     * @param constants The constants to subtract from.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.SubtractFromConst
     */
    public Coverage subtractFrom(final Coverage source, final double[] constants)
            throws CoverageProcessingException
    {
        return doOperation("SubtractFromConst", source, "constants", constants);
    }

    /**
     * Multiplies every sample values of the source coverage by constants (one for each band).
     *
     * @param source The source coverage.
     * @param constants The constants to multiply to each band.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.MultiplyConst
     */
    public Coverage multiply(final Coverage source, final double[] constants)
            throws CoverageProcessingException
    {
        return doOperation("MultiplyConst", source, "constants", constants);
    }

    /**
     * Divides every sample values of the source coverage by constants (one for each band).
     *
     * @param source The source coverage.
     * @param constants The constants to divides by.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.DivideByConst
     */
    public Coverage divideBy(final Coverage source, final double[] constants)
            throws CoverageProcessingException
    {
        return doOperation("DivideByConst", source, "constants", constants);
    }

    /**
     * Maps the sample values of a coverage from one range to another range.
     *
     * @param source The source coverage.
     * @param constants The constants to multiply to each band.
     * @param offsets The constants to add to each band.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Rescale
     */
    public Coverage rescale(final Coverage source, final double[] constants, final double[] offsets)
            throws CoverageProcessingException
    {
        return doOperation("Rescale", source, "constants", constants, "offsets", offsets);
    }

    /**
     * Inverts the sample values of a coverage.
     *
     * @param source The source coverage.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Invert
     */
    public Coverage invert(final Coverage source) throws CoverageProcessingException {
        return doOperation("Invert", source);
    }

    /**
     * Computes the mathematical absolute value of each sample value.
     *
     * @param source The source coverage.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Absolute
     */
    public Coverage absolute(final Coverage source) throws CoverageProcessingException {
        return doOperation("Absolute", source);
    }

    /**
     * Takes the natural logarithm of the sample values of a coverage.
     *
     * @param source The source coverage.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Log
     */
    public Coverage log(final Coverage source) throws CoverageProcessingException {
        return doOperation("Log", source);
    }

    /**
     * Takes the exponential of the sample values of a coverage.
     *
     * @param source The source coverage.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Exp
     */
    public Coverage exp(final Coverage source) throws CoverageProcessingException {
        return doOperation("Exp", source);
    }

    /**
     * Finds the maximum and minimum pixel values for each band within that region of the coverage.
     * The coverage data pass through this operation unchanged.
     * 
     * @param source
     *            The source coverage.
     * @throws CoverageProcessingException
     *             if the operation can't be applied.
     * 
     * @see org.geotools.coverage.processing.operation.Extrema
     */
    public Coverage extrema(final Coverage source) throws CoverageProcessingException {
        return doOperation("Extrema", source);
    }

    /**
     * Replaces {@link Float#NaN NaN} values by the weighted average of neighbors values.
     * This method uses the default padding and validity threshold.
     *
     * @param source The source coverage.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.NodataFilter
     */
    public GridCoverage nodataFilter(final GridCoverage source) throws CoverageProcessingException {
        return (GridCoverage) doOperation("NodataFilter", source);
    }

    /**
     * Replaces {@link Float#NaN NaN} values by the weighted average of neighbors values.
     *
     * @param source  The source coverage.
     * @param padding The number of pixels above, below, to the left and to the right of central
     *        {@code NaN} pixel to use for computing the average. The default value is 1.
     * @param validityThreshold The minimal number of valid values required for computing the
     *        average. The {@code NaN} value will be replaced by the average only if the number
     *        of valid value is greater than or equals to this threshold. The default value is 4.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.NodataFilter
     */
    public GridCoverage nodataFilter(final GridCoverage source,
                                     final int padding,
                                     final int validityThreshold)
            throws CoverageProcessingException
    {
        return (GridCoverage) doOperation("NodataFilter",      source,
                                          "padding",           Integer.valueOf(padding),
                                          "validityThreshold", Integer.valueOf(validityThreshold));
    }

    /**
     * Specifies the interpolation type to be used to interpolate values for points which fall
     * between grid cells. The default value is nearest neighbor. The new interpolation type
     * operates on all sample dimensions.
     *
     * @param source The source coverage.
     * @param type   The interpolation type. Possible values are {@code "NearestNeighbor"},
     *               {@code "Bilinear"} and {@code "Bicubic"}.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Interpolate
     */
    public GridCoverage interpolate(final GridCoverage source, final String type)
            throws CoverageProcessingException
    {
        return (GridCoverage) doOperation("Interpolate", source, "Type", type);
    }

    /**
     * Specifies the interpolation type to be used to interpolate values for points which fall
     * between grid cells. The default value is nearest neighbor. The new interpolation type
     * operates on all sample dimensions.
     *
     * @param source The source coverage.
     * @param type   The interpolation type as a JAI interpolation object.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Interpolate
     */
    public GridCoverage interpolate(final GridCoverage source, final Interpolation type)
            throws CoverageProcessingException
    {
        return (GridCoverage) doOperation("Interpolate", source, "Type", type);
    }

    /**
     * Specifies the interpolation types to be used to interpolate values for points which fall
     * between grid cells. The first element in the array is the primary interpolation. All other
     * elements are fallback to be used if the primary interpolation returns a {@code NaN} value.
     * See {@link org.geotools.coverage.processing.operation.Interpolate} operation for details.
     *
     * @param source The source coverage.
     * @param types  The interpolation types and their fallback.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Interpolate
     */
    public GridCoverage interpolate(final GridCoverage source, final Interpolation[] types)
            throws CoverageProcessingException
    {
        return (GridCoverage) doOperation("Interpolate", source, "Type", types);
    }


    /**
     * Recolors a coverage to the specified color maps.
     *
     * @param source    The source coverage.
     * @param colorMaps The color maps to apply.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Recolor
     *
     * @since 2.4
     */
    public GridCoverage recolor(final GridCoverage source, final ColorMap[] colorMaps)
            throws CoverageProcessingException
    {
        return (GridCoverage) doOperation("Recolor", source, "ColorMaps", colorMaps);
    }

    /**
     * Chooses <var>N</var> {@linkplain org.geotools.coverage.GridSampleDimension sample dimensions}
     * from a coverage and copies their sample data to the destination grid coverage in the order
     * specified.
     *
     * @param source The source coverage.
     * @param sampleDimensions The sample dimensions to select.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.SelectSampleDimension
     */
    public Coverage selectSampleDimension(final Coverage source, final int[] sampleDimensions)
            throws CoverageProcessingException
    {
        return doOperation("SelectSampleDimension", source, "SampleDimensions", sampleDimensions);
    }




    /////////////////////////////////////////////////////////////////////////////////
    ////////                                                                 ////////
    ////////            R E S A M P L I N G   O P E R A T I O N S            ////////
    ////////                                                                 ////////
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * Resamples a coverage to the specified coordinate reference system.
     *
     * @param source The source coverage.
     * @param crs    The target coordinate reference system.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Resample
     */
    public Coverage resample(final Coverage source, final CoordinateReferenceSystem crs)
            throws CoverageProcessingException
    {
        return doOperation("Resample", source, "CoordinateReferenceSystem", crs);
    }

    /**
     * Resamples a grid coverage to the specified envelope.
     *
     * @param source The source coverage.
     * @param envelope The target envelope, including a possibly different coordinate reference system.
     * @param interpolationType The interpolation type, or {@code null} for the default one.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Resample
     * @see org.geotools.coverage.processing.operation.Resample#computeGridGeometry
     *
     * @since 2.5
     */
    public Coverage resample(final GridCoverage  source,
                             final Envelope      envelope,
                             final Interpolation interpolationType)
            throws CoverageProcessingException
    {
        final GridGeometry gridGeometry;
        try {
            gridGeometry = Resample.computeGridGeometry(source, envelope);
        } catch (TransformException exception) {
            throw new CoverageProcessingException(exception);
        }
        return doOperation("Resample",                  source,
                           "CoordinateReferenceSystem", envelope.getCoordinateReferenceSystem(),
                           "GridGeometry",              gridGeometry,
                           "InterpolationType",         interpolationType);
    }

    /**
     * Resamples a grid coverage to the specified coordinate reference system and grid geometry.
     *
     * @param source The source coverage.
     * @param crs The target coordinate reference system, or {@code null} for keeping it unchanged.
     * @param gridGeometry The grid geometry, or {@code null} for a default one.
     * @param interpolationType The interpolation type, or {@code null} for the default one.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Resample
     */
    public Coverage resample(final GridCoverage  source,
                             final CoordinateReferenceSystem crs,
                             final GridGeometry  gridGeometry,
                             final Interpolation interpolationType)
            throws CoverageProcessingException
    {
        return doOperation("Resample",                  source,
                           "CoordinateReferenceSystem", crs,
                           "GridGeometry",              gridGeometry,
                           "InterpolationType",         interpolationType);
    }
    
    /**
     * Resamples a grid coverage to the specified coordinate reference system and grid geometry.
     *
     * @param source The source coverage.
     * @param crs The target coordinate reference system, or {@code null} for keeping it unchanged.
     * @param gridGeometry The grid geometry, or {@code null} for a default one.
     * @param interpolationType The interpolation type, or {@code null} for the default one.
     * @param backgroundValues The backgroundValues, or {@code null} for the default one.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Resample
     */
    public Coverage resample(final GridCoverage  source,
                             final CoordinateReferenceSystem crs,
                             final GridGeometry  gridGeometry,
                             final Interpolation interpolationType,
                             final double[] backgroundValues)
            throws CoverageProcessingException
    {
        return doOperation("Resample",                  source,
                           "CoordinateReferenceSystem", crs,
                           "GridGeometry",              gridGeometry,
                           "InterpolationType",         interpolationType,
                           "BackgroundValues",          backgroundValues);
    }

    /**
     * Crops the image to a specified rectangular area.
     *
     * @param source   The source coverage.
     * @param envelope The rectangular area to keep.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Crop
     *
     * @since 2.3
     */
    public Coverage crop(final Coverage Source, final Envelope envelope)
            throws CoverageProcessingException
    {
        return doOperation("CoverageCrop", Source, "Envelope", envelope);
    }

    /**
     * Translates and resizes an image.
     *
     * @param source   The source coverage.
     * @param xScale   The scale factor along the <var>x</var> axis.
     * @param yScale   The scale factor along the <var>y</var> axis.
     * @param xTrans   The translation along the <var>x</var> axis.
     * @param yTrans   The translation along the <var>x</var> axis.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Scale
     *
     * @since 2.3
     */
    public GridCoverage scale(final GridCoverage source,
                              final double xScale, final double yScale,
                              final double xTrans, final double yTrans)
            throws CoverageProcessingException
    {
        return scale(source, xScale, yScale, xTrans, yTrans,
                Interpolation.getInstance(Interpolation.INTERP_NEAREST));
    }

    /**
     * Translates and resizes an image.
     *
     * @param source   The source coverage.
     * @param xScale   The scale factor along the <var>x</var> axis.
     * @param yScale   The scale factor along the <var>y</var> axis.
     * @param xTrans   The translation along the <var>x</var> axis.
     * @param yTrans   The translation along the <var>x</var> axis.
     * @param interpolation The interpolation to use, or {@code null} for the default.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.Scale
     *
     * @since 2.3
     */
    public GridCoverage scale(final GridCoverage source,
                              final double xScale, final double yScale,
                              final double xTrans, final double yTrans,
                              final Interpolation interpolation)
            throws CoverageProcessingException
    {
        return (GridCoverage) doOperation("Scale", source,
                "xScale", Float.valueOf((float) xScale),
                "yScale", Float.valueOf((float) yScale),
                "xTrans", Float.valueOf((float) xTrans),
                "yTrans", Float.valueOf((float) yTrans),
                "Interpolation", interpolation);
    }

    /////////////////////////////////////////////////////////////////////////////////
    ////////                                                                 ////////
    ////////                F I L T E R   O P E R A T I O N S                ////////
    ////////                                                                 ////////
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * Subsamples an image by averaging over a moving window
     *
     * @param source   The source coverage.
     * @param scaleX   The scale factor along the <var>x</var> axis.
     * @param scaleY   The scale factor along the <var>y</var> axis.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.SubsampleAverage
     *
     * @since 2.3
     */
    public GridCoverage subsampleAverage(final GridCoverage   source,
                                         final double         scaleX,
                                         final double         scaleY)
            throws CoverageProcessingException
    {
        return (GridCoverage) doOperation("SubsampleAverage", source,
                                          "scaleX",           Double.valueOf(scaleX),
                                          "scaleY",           Double.valueOf(scaleY));
    }
    /**
     * Subsamples an image using the default values. The scale factor is 2 and the
     * filter is a quadrant symmetric filter generated from a Gaussian kernel.
     *
     * @param source The source coverage.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.FilteredSubsample
     *
     * @since 2.3
     */
    public GridCoverage filteredSubsample(final GridCoverage source)
            throws CoverageProcessingException
    {
        return (GridCoverage) doOperation("FilteredSubsample", source);
    }

    /**
     * Subsamples an image by integral factors.
     *
     * @param source   The source coverage.
     * @param scaleX   The scale factor along the <var>x</var> axis. The default value is 2.
     * @param scaleY   The scale factor along the <var>y</var> axis. The default value is 2.
     * @param qsFilter The filter. Default to a quadrant symmetric filter generated from
     *                 a Gaussian kernel
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.FilteredSubsample
     *
     * @since 2.3
     */
    public GridCoverage filteredSubsample(final GridCoverage source,
                                          final int          scaleX,
                                          final int          scaleY,
                                          final float[]      qsFilter)
            throws CoverageProcessingException
    {
        return filteredSubsample(source, scaleX, scaleY, qsFilter,
                Interpolation.getInstance(Interpolation.INTERP_NEAREST));
    }

    /**
     * Subsamples an image by integral factors.
     *
     * @param source   The source coverage.
     * @param scaleX   The scale factor along the <var>x</var> axis. The default value is 2.
     * @param scaleY   The scale factor along the <var>y</var> axis. The default value is 2.
     * @param qsFilter The filter. Default to a quadrant symmetric filter generated from
     *                 a Gaussian kernel
     * @param interpolation The interpolation to use, or {@code null} for the default.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.FilteredSubsample
     *
     * @since 2.3
     *
     */
    public GridCoverage filteredSubsample(final GridCoverage source,
                                          final int scaleX, final int scaleY,
                                          final float[] qsFilter,
                                          final Interpolation interpolation)
            throws CoverageProcessingException
    {
        return (GridCoverage) doOperation("FilteredSubsample", source,
                "scaleX",            Integer.valueOf(scaleX),
                "scaleY",            Integer.valueOf(scaleY),
                "qsFilterArray",     qsFilter,
                "Interpolation",     interpolation);
    }

    /**
     * Edge detector which computes the magnitude of the image gradient vector in two orthogonal
     * directions. The default masks are the Sobel ones.
     *
     * @param source The source coverage.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.GradientMagnitude
     */
    public Coverage gradientMagnitude(final Coverage source)
            throws CoverageProcessingException
    {
        return doOperation("GradientMagnitude", source);
    }

    /**
     * Edge detector which computes the magnitude of the image gradient vector in two orthogonal
     * directions.
     *
     * @param source The source coverage.
     * @param mask1  The first mask.
     * @param mask2  The second mask, orthogonal to the first one.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @see org.geotools.coverage.processing.operation.GradientMagnitude
     */
    public Coverage gradientMagnitude(final Coverage source,
                                      final KernelJAI mask1, final KernelJAI mask2)
            throws CoverageProcessingException
    {
        return doOperation("GradientMagnitude", source, "mask1", mask1, "mask2", mask2);
    }




    /////////////////////////////////////////////////////////////////////////////////
    ////////                                                                 ////////
    ////////                   H E L P E R   M E T H O D S                   ////////
    ////////                                                                 ////////
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the processor, creating one if needed.
     */
    private CoverageProcessor getProcessor() {
        // No need to synchronize.
        if (processor == null) {
            processor = CoverageProcessor.getInstance();
        }
        return processor;
    }

    /**
     * Applies a process operation with default parameters.
     * This is a helper method for implementation of various convenience methods in this class.
     *
     * @param  operationName Name of the operation to be applied to the coverage.
     * @param  source The source coverage.
     * @return The result as a coverage.
     * @throws OperationNotFoundException if there is no operation named {@code operationName}.
     * @throws CoverageProcessingException if the operation can't be applied.
     */
    protected final Coverage doOperation(final String operationName, final Coverage source)
            throws OperationNotFoundException, CoverageProcessingException
    {
        final CoverageProcessor processor = getProcessor();
        final Operation operation = processor.getOperation(operationName);
        final ParameterValueGroup parameters = operation.getParameters();
        parameters.parameter("Source").setValue(source);
        return processor.doOperation(parameters);
    }
    
    /**
     * Applies a process binary operation on two coverages with default parameters.
     * This is a helper method for implementation of various convenience methods in this class.
     *
     * @param  operationName Name of the operation to be applied to the coverage.
     * @param  source0 The first source coverage.
     * @param  source1 The second source coverage.
     * 
     * @return The result as a coverage.
     * @throws OperationNotFoundException if there is no operation named {@code operationName}.
     * @throws CoverageProcessingException if the operation can't be applied.
     */
    protected final Coverage doOperation(final String operationName, final Coverage source0, final Coverage source1)
            throws OperationNotFoundException, CoverageProcessingException
    {
        final CoverageProcessor processor = getProcessor();
        final Operation operation = processor.getOperation(operationName);
        final ParameterValueGroup parameters = operation.getParameters();
        parameters.parameter("Source0").setValue(source0);
        parameters.parameter("Source1").setValue(source1);
        return processor.doOperation(parameters);
    }

    /**
     * Applies a process operation with one parameter.
     * This is a helper method for implementation of various convenience methods in this class.
     *
     * @param operationName  Name of the operation to be applied to the coverage.
     * @param source         The source coverage.
     * @param argumentName1  The name of the first parameter to setParameterValue.
     * @param argumentValue1 The value for the first parameter.
     * @return The result as a coverage.
     * @throws OperationNotFoundException if there is no operation named {@code operationName}.
     * @throws InvalidParameterNameException if there is no parameter with the specified name.
     * @throws CoverageProcessingException if the operation can't be applied.
     */
    protected final Coverage doOperation(final String operationName, final Coverage source,
                                         final String argumentName1, final Object argumentValue1)
            throws OperationNotFoundException, InvalidParameterNameException,
                   CoverageProcessingException
    {
        final CoverageProcessor processor = getProcessor();
        final Operation operation = processor.getOperation(operationName);
        final ParameterValueGroup parameters = operation.getParameters();
        parameters.parameter("Source").setValue(source);
        setParameterValue(parameters, argumentName1, argumentValue1);
        return processor.doOperation(parameters);
    }

    /**
     * Applies process operation with two parameters.
     * This is a helper method for implementation of various convenience methods in this class.
     *
     * @param operationName  Name of the operation to be applied to the coverage.
     * @param source         The source coverage.
     * @param argumentName1  The name of the first parameter to setParameterValue.
     * @param argumentValue1 The value for the first parameter.
     * @param argumentName2  The name of the second parameter to setParameterValue.
     * @param argumentValue2 The value for the second parameter.
     * @return The result as a coverage.
     * @throws OperationNotFoundException if there is no operation named {@code operationName}.
     * @throws InvalidParameterNameException if there is no parameter with the specified name.
     * @throws CoverageProcessingException if the operation can't be applied.
     */
    protected final Coverage doOperation(final String operationName, final Coverage source,
                                         final String argumentName1, final Object argumentValue1,
                                         final String argumentName2, final Object argumentValue2)
            throws OperationNotFoundException, InvalidParameterNameException,
                   CoverageProcessingException
    {
        final CoverageProcessor processor = getProcessor();
        final Operation operation = processor.getOperation(operationName);
        final ParameterValueGroup parameters = operation.getParameters();
        parameters.parameter("Source").setValue(source);
        setParameterValue(parameters, argumentName1, argumentValue1);
        setParameterValue(parameters, argumentName2, argumentValue2);
        return processor.doOperation(parameters);
    }

    /**
     * Applies a process operation with three parameters.
     * This is a helper method for implementation of various convenience methods in this class.
     *
     * @param operationName  Name of the operation to be applied to the coverage.
     * @param source         The source coverage.
     * @param argumentName1  The name of the first parameter to setParameterValue.
     * @param argumentValue1 The value for the first parameter.
     * @param argumentName2  The name of the second parameter to setParameterValue.
     * @param argumentValue2 The value for the second parameter.
     * @param argumentName3  The name of the third parameter to setParameterValue.
     * @param argumentValue3 The value for the third parameter.
     * @return The result as a coverage.
     * @throws OperationNotFoundException if there is no operation named {@code operationName}.
     * @throws InvalidParameterNameException if there is no parameter with the specified name.
     * @throws CoverageProcessingException if the operation can't be applied.
     */
    protected final Coverage doOperation(final String operationName, final Coverage source,
                                         final String argumentName1, final Object argumentValue1,
                                         final String argumentName2, final Object argumentValue2,
                                         final String argumentName3, final Object argumentValue3)
            throws OperationNotFoundException, InvalidParameterNameException,
                   CoverageProcessingException
    {
        final CoverageProcessor processor = getProcessor();
        final Operation operation = processor.getOperation(operationName);
        final ParameterValueGroup parameters = operation.getParameters();
        parameters.parameter("Source").setValue(source);
        setParameterValue(parameters, argumentName1, argumentValue1);
        setParameterValue(parameters, argumentName2, argumentValue2);
        setParameterValue(parameters, argumentName3, argumentValue3);
        return processor.doOperation(parameters);
    }

    /**
     * Applies a process operation with four parameters.
     * This is a helper method for implementation of various convenience methods in this class.
     *
     * @param  operationName  Name of the operation to be applied to the coverage.
     * @param  source         The source coverage.
     * @return The result as a coverage.
     * @throws OperationNotFoundException if there is no operation named {@code operationName}.
     * @throws InvalidParameterNameException if there is no parameter with the specified name.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @since 2.3
     */
    protected final Coverage doOperation(final String operationName, final Coverage source,
                                         final String argumentName1, final Object argumentValue1,
                                         final String argumentName2, final Object argumentValue2,
                                         final String argumentName3, final Object argumentValue3,
                                         final String argumentName4, final Object argumentValue4)
            throws OperationNotFoundException, InvalidParameterNameException,
                   CoverageProcessingException
    {
        final CoverageProcessor processor = getProcessor();
        final Operation operation = processor.getOperation(operationName);
        final ParameterValueGroup parameters = operation.getParameters();
        parameters.parameter("Source").setValue(source);
        setParameterValue(parameters, argumentName1, argumentValue1);
        setParameterValue(parameters, argumentName2, argumentValue2);
        setParameterValue(parameters, argumentName3, argumentValue3);
        setParameterValue(parameters, argumentName4, argumentValue4);
        return processor.doOperation(parameters);
    }

    /**
     * Applies a process operation with five parameters.
     * This is a helper method for implementation of various convenience methods in this class.
     *
     * @param  operationName  Name of the operation to be applied to the coverage.
     * @param  source         The source coverage.
     * @return The result as a coverage.
     * @throws OperationNotFoundException if there is no operation named {@code operationName}.
     * @throws InvalidParameterNameException if there is no parameter with the specified name.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @since 2.3
     */
    protected final Coverage doOperation(final String operationName, final Coverage source,
                                         final String argumentName1, final Object argumentValue1,
                                         final String argumentName2, final Object argumentValue2,
                                         final String argumentName3, final Object argumentValue3,
                                         final String argumentName4, final Object argumentValue4,
                                         final String argumentName5, final Object argumentValue5)
            throws OperationNotFoundException, InvalidParameterNameException,
                   CoverageProcessingException
    {
        final CoverageProcessor processor = getProcessor();
        final Operation operation = processor.getOperation(operationName);
        final ParameterValueGroup parameters = operation.getParameters();
        parameters.parameter("Source").setValue(source);
        setParameterValue(parameters, argumentName1, argumentValue1);
        setParameterValue(parameters, argumentName2, argumentValue2);
        setParameterValue(parameters, argumentName3, argumentValue3);
        setParameterValue(parameters, argumentName4, argumentValue4);
        setParameterValue(parameters, argumentName5, argumentValue5);
        return processor.doOperation(parameters);
    }

    /**
     * Applies a process operation with six parameters.
     * This is a helper method for implementation of various convenience methods in this class.
     *
     * @param  operationName  Name of the operation to be applied to the coverage.
     * @param  source         The source coverage.
     * @return The result as a coverage.
     * @throws OperationNotFoundException if there is no operation named {@code operationName}.
     * @throws InvalidParameterNameException if there is no parameter with the specified name.
     * @throws CoverageProcessingException if the operation can't be applied.
     *
     * @since 2.3
     */
    protected final Coverage doOperation(final String operationName, final Coverage source,
                                         final String argumentName1, final Object argumentValue1,
                                         final String argumentName2, final Object argumentValue2,
                                         final String argumentName3, final Object argumentValue3,
                                         final String argumentName4, final Object argumentValue4,
                                         final String argumentName5, final Object argumentValue5,
                                         final String argumentName6, final Object argumentValue6)
            throws OperationNotFoundException, InvalidParameterNameException,
                   CoverageProcessingException
    {
        final CoverageProcessor processor = getProcessor();
        final Operation operation = processor.getOperation(operationName);
        final ParameterValueGroup parameters = operation.getParameters();
        parameters.parameter("Source").setValue(source);
        setParameterValue(parameters, argumentName1, argumentValue1);
        setParameterValue(parameters, argumentName2, argumentValue2);
        setParameterValue(parameters, argumentName3, argumentValue3);
        setParameterValue(parameters, argumentName4, argumentValue4);
        setParameterValue(parameters, argumentName5, argumentValue5);
        setParameterValue(parameters, argumentName6, argumentValue6);
        return processor.doOperation(parameters);
    }

    /**
     * Set the specified parameter to the specified value, if not null.
     */
    private static void setParameterValue(final ParameterValueGroup parameters, String name, Object value)
            throws InvalidParameterNameException
    {
        if (value != null) try {
            parameters.parameter(name).setValue(value);
        } catch (ParameterNotFoundException cause) {
            final InvalidParameterNameException exception = new InvalidParameterNameException(
                    Errors.format(ErrorKeys.UNKNOW_PARAMETER_NAME_$1, name), name);
            exception.initCause(cause);
            throw exception;
        }
    }
}
