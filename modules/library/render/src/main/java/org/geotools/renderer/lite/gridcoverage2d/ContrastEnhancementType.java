/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import it.geosolutions.jaiext.lookup.LookupTable;
import it.geosolutions.jaiext.lookup.LookupTableFactory;
import it.geosolutions.jaiext.piecewise.DefaultPiecewiseTransform1D;
import it.geosolutions.jaiext.piecewise.DefaultPiecewiseTransform1DElement;
import it.geosolutions.jaiext.piecewise.PiecewiseTransform1D;
import it.geosolutions.jaiext.range.NoDataContainer;
import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.range.RangeFactory;
import it.geosolutions.jaiext.utilities.ImageLayout2;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.media.jai.Histogram;
import javax.media.jai.JAI;
import javax.media.jai.RasterFactory;
import org.geotools.image.ImageWorker;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.styling.AbstractContrastMethodStrategy;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.ExponentialContrastMethodStrategy;
import org.geotools.styling.LogarithmicContrastMethodStrategy;
import org.geotools.styling.NormalizeContrastMethodStrategy;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;
import org.opengis.filter.expression.Expression;
import org.opengis.referencing.operation.TransformException;
import org.opengis.style.ContrastMethod;

/**
 * Starting with version 14.x, {@link ContrastEnhancement} can be customized to support different
 * {@link ContrastMethod}s algorithm and parameters.
 *
 * <p>This class contains implementations from previously defined algorithm, as well as new ones.
 *
 * <p>Define a new Type for a newly defined Method-Algorithm and implements the {@link
 * ContrastEnhancementType#process(ImageWorker, Hints, Map))} method.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public enum ContrastEnhancementType {
    EXPONENTIAL {
        @Override
        RenderedImage process(
                ImageWorker inputWorker, Hints hints, Map<String, Expression> parameters) {
            RenderedImage inputImage = inputWorker.getRenderedImage();
            assert inputImage.getSampleModel().getNumBands() == 1 : inputImage;
            final int dataType = inputImage.getSampleModel().getDataType();

            if (dataType == DataBuffer.TYPE_BYTE) {
                //
                // Optimisation for byte images
                //

                LookupTable table = createByteLookupTable(EMPTY_MAP);
                inputWorker.lookup(table);
                return inputWorker.getRenderedImage();
            }
            // General case, we use the piecewise1D transform
            //
            // STEP 1 do the extrema
            //
            inputWorker.removeRenderingHints();
            final double[] minimum = inputWorker.getMinimums();
            final double[] maximum = inputWorker.getMaximums();

            //
            // STEP 2 use generic piecewise
            //
            final PiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform =
                    generatePiecewise(setMinMaxParams(minimum[0], maximum[0]));
            inputWorker.piecewise(transform, Integer.valueOf(0));
            return inputWorker.getRenderedImage();
        }

        @Override
        LookupTable createByteLookupTable(Map<String, Object> params) {
            final byte lut[] = new byte[256];
            final double normalizationFactor = 255.0;
            final double correctionFactor = 255.0 / (Math.E - 1);
            for (int i = 1; i < lut.length; i++) {
                lut[i] =
                        (byte)
                                (0.5f
                                        + correctionFactor
                                                * (Math.exp(i / normalizationFactor) - 1.0));
            }
            return generateLookupTableByte(lut);
        }

        @Override
        PiecewiseTransform1D<DefaultPiecewiseTransform1DElement> generatePiecewise(
                Map<String, Object> params) {
            Utilities.ensureNonNull("params", params);
            double minimum = (double) params.get(KEY_MIN);
            double maximum = (double) params.get(KEY_MAX);

            final double normalizationFactor = maximum;
            final double correctionFactor = normalizationFactor / (Math.E - 1);

            final DefaultPiecewiseTransform1DElement mainElement =
                    DefaultPiecewiseTransform1DElement.create(
                            "exponential-contrast-enhancement-transform",
                            RangeFactory.create(minimum, maximum),
                            new MathTransformationAdapter() {

                                public double derivative(double value) throws TransformException {
                                    throw new UnsupportedOperationException(
                                            Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1));
                                }

                                public boolean isIdentity() {
                                    return false;
                                }

                                public double transform(double value) {
                                    value =
                                            correctionFactor
                                                    * (Math.exp(value / normalizationFactor) - 1);
                                    return value;
                                }
                            });

            return new DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement>(
                    new DefaultPiecewiseTransform1DElement[] {mainElement}, 0);
        }
    },

    LOGARITHMIC {

        @Override
        RenderedImage process(
                ImageWorker inputWorker, Hints hints, Map<String, Expression> parameters) {
            RenderedImage inputImage = inputWorker.getRenderedImage();
            assert inputImage.getSampleModel().getNumBands() == 1 : inputImage;
            final int dataType = inputImage.getSampleModel().getDataType();

            if (dataType == DataBuffer.TYPE_BYTE) {
                //
                // Optimisation for byte images m we use lookup
                //
                LookupTable table = createByteLookupTable(EMPTY_MAP);
                inputWorker.lookup(table);
                return inputWorker.getRenderedImage();
            }
            // General case
            // define a specific piecewise for the logarithm

            //
            // STEP 1 do the extrema
            //
            inputWorker.removeRenderingHints();
            final double[] minimum = inputWorker.getMinimums();
            final double[] maximum = inputWorker.getMaximums();

            //
            // STEP 2 use generic piecewise
            //
            final PiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform =
                    generatePiecewise(setMinMaxParams(minimum[0], maximum[0]));
            inputWorker.piecewise(transform, Integer.valueOf(0));
            return inputWorker.getRenderedImage();
        }

        @Override
        LookupTable createByteLookupTable(Map<String, Object> params) {
            final byte lut[] = new byte[256];
            final double normalizationFactor = 255.0;
            final double correctionFactor = 100.0;
            for (int i = 1; i < lut.length; i++) {
                lut[i] =
                        (byte)
                                (0.5f
                                        + normalizationFactor
                                                * Math.log(
                                                        (i * correctionFactor / normalizationFactor
                                                                + 1.0)));
            }
            return generateLookupTableByte(lut);
        }

        @Override
        PiecewiseTransform1D<DefaultPiecewiseTransform1DElement> generatePiecewise(
                Map<String, Object> params) {
            Utilities.ensureNonNull("params", params);
            double minimum = (double) params.get(KEY_MIN);
            double maximum = (double) params.get(KEY_MAX);

            final double normalizationFactor = maximum;
            final double correctionFactor = 100.0;

            final DefaultPiecewiseTransform1DElement mainElement =
                    DefaultPiecewiseTransform1DElement.create(
                            "logarithmic-contrast-enhancement-transform",
                            RangeFactory.create(minimum, maximum),
                            new MathTransformationAdapter() {

                                public double derivative(double value) throws TransformException {
                                    throw new UnsupportedOperationException(
                                            Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1));
                                }

                                public boolean isIdentity() {
                                    return false;
                                }

                                public double transform(double value) {
                                    value =
                                            normalizationFactor
                                                    * Math.log(
                                                            1
                                                                    + (value
                                                                            * correctionFactor
                                                                            / normalizationFactor));
                                    return value;
                                }
                            });

            return new DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement>(
                    new DefaultPiecewiseTransform1DElement[] {mainElement}, 0);
        }
    },

    HISTOGRAM {
        @Override
        RenderedImage process(
                ImageWorker inputWorker, Hints hints, Map<String, Expression> parameters) {
            //
            // IT WORKS ONLY ON BYTE DATA TYPE!!!
            //

            // convert the input image to 8 bit
            inputWorker.rescaleToBytes();

            // compute the histogram
            final Histogram h = inputWorker.removeRenderingHints().getHistogram(null, null, null);

            // do the actual lookup
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(KEY_HISTOGRAM, h);
            LookupTable table = createByteLookupTable(params);
            inputWorker.setRenderingHints(hints);
            inputWorker.lookup(table);
            return inputWorker.getRenderedImage();
        }

        @Override
        LookupTable createByteLookupTable(Map<String, Object> params) {
            Utilities.ensureNonNull("params", params);
            Histogram h = (Histogram) params.get(KEY_HISTOGRAM);
            final byte[] cumulative = new byte[h.getNumBins(0)];

            // sum of bins (we might have excluded 0 hence we cannot really optimise)
            float totalBinSum = 0;
            for (int i = 0; i < cumulative.length; i++) {
                totalBinSum += h.getBinSize(0, i);
            }

            // this is the scale factor for the histogram equalization process
            final float scale = (float) (h.getHighValue(0) - 1 - h.getLowValue(0)) / totalBinSum;
            float sum = 0;
            for (int i = 1; i < cumulative.length; i++) {
                sum += h.getBinSize(0, i - 1);
                cumulative[i] = (byte) ((sum * scale + h.getLowValue(0)) + .5F);
            }

            return generateLookupTableByte(cumulative);
        }
    },

    NORMALIZE_DEFAULT {
        @Override
        RenderedImage process(
                ImageWorker inputWorker, Hints hints, Map<String, Expression> parameters) {

            // step 1 do the extrema to get the statistics for this image
            final double[][] extrema = new double[2][];
            inputWorker.removeRenderingHints();
            RenderedImage inputImage = inputWorker.getRenderedImage();
            extrema[0] = inputWorker.getMinimums();
            extrema[1] = inputWorker.getMaximums();
            final int numBands = extrema[0].length;
            assert numBands == 1 : inputWorker.getRenderedOperation();
            final int dataType = inputImage.getSampleModel().getDataType();

            double minData = extrema[0][0];
            double maxData = extrema[1][0];
            // Shortcut for byte datatype
            Map<String, Object> params = setMinMaxParams(minData, maxData);
            if (dataType == DataBuffer.TYPE_BYTE) {
                // Optimization for byte images, we use the lookup operation

                if (maxData == MAX_BYTE && minData == MIN_BYTE) return inputImage;

                LookupTable table = createByteLookupTable(params);
                inputWorker.setRenderingHints(hints);
                inputWorker.lookup(table);
                return inputWorker.getRenderedImage();
            }

            // //
            //
            // General case, we use the rescale in order to stretch the values to highest and lowest
            // dim
            //
            // //
            // get the correct dim for this data type
            final double maximum = MAX_BYTE;
            final double minimum = MIN_BYTE;
            if (areEqual(extrema[1][0], maximum) && areEqual(extrema[0][0], minimum)) {
                return inputWorker.getRenderedImage();
            }

            NoDataContainer destinationNoData = getDestinationNoData(inputWorker);
            double offsetAdjustment = destinationNoData == null ? 0 : 1;
            // compute the scale factors
            final double delta = extrema[1][0] - extrema[0][0];
            final double scale = (maximum - minimum - offsetAdjustment) / delta;
            final double offset = minimum - scale * extrema[0][0] + offsetAdjustment;

            //
            // do the actual rescale
            //

            // create a proper layout to impose the target data type
            final ImageLayout2 layout = new ImageLayout2(inputWorker.getRenderedImage());
            SampleModel sm =
                    RasterFactory.createBandedSampleModel(
                            DataBuffer.TYPE_BYTE,
                            inputWorker.getRenderedImage().getWidth(),
                            inputWorker.getRenderedImage().getHeight(),
                            1);
            layout.setSampleModel(sm);
            layout.setColorModel(
                    new ComponentColorModel(
                            ColorSpace.getInstance(ColorSpace.CS_GRAY),
                            false,
                            false,
                            Transparency.OPAQUE,
                            DataBuffer.TYPE_BYTE));
            RenderingHints localHints = hints.clone();
            localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));

            if (destinationNoData != null) {
                // rescale op will fill pixel nodata value with background value
                // let's use the destination nodata as new background.
                inputWorker.setBackground(new double[] {destinationNoData.getAsSingleValue()});
            }
            // rescale
            inputWorker.setRenderingHints(localHints);
            inputWorker.rescale(new double[] {scale}, new double[] {offset});
            return inputWorker.getRenderedImage();
        }

        @Override
        LookupTable createByteLookupTable(Map<String, Object> params) {
            return generateLinearByteLookupTable(params);
        }
    },

    NORMALIZE_STRETCH_TO_MINMAX {
        @Override
        RenderedImage process(
                ImageWorker inputWorker, Hints hints, Map<String, Expression> parameters) {
            checkParameters(parameters, KEY_MIN, KEY_MAX);
            Map<String, Object> processParams = getMinMaxParams(parameters);
            RenderedImage inputImage = inputWorker.getRenderedImage();
            final int dataType = inputImage.getSampleModel().getDataType();
            Utilities.ensureNonNull("processParams", processParams);
            double minData = (double) processParams.get(KEY_MIN);
            double maxData = (double) processParams.get(KEY_MAX);

            if (dataType == DataBuffer.TYPE_BYTE) {

                // Optimization for byte images, we use the lookup operation
                if (maxData == MAX_BYTE && minData == MIN_BYTE) return inputImage;

                LookupTable table = createByteLookupTable(processParams);
                inputWorker.setRenderingHints(hints);
                inputWorker.lookup(table);
                return inputWorker.getRenderedImage();
            }

            //
            // General case, we use the rescale in order to stretch the values to highest and lowest
            // dim
            //
            // get the target data type
            final double maximum = MAX_BYTE;
            final double minimum = MIN_BYTE;
            if (areEqual(maxData, maximum) && areEqual(minData, minimum)) {
                return inputWorker.getRenderedImage();
            }

            NoDataContainer destinationNoData = getDestinationNoData(inputWorker);
            double offsetAdjustment = destinationNoData == null ? 0 : 1;
            // compute the scale factors
            final double delta = maxData - minData;
            final double scale = (maximum - minimum - offsetAdjustment) / delta;
            final double offset = minimum - scale * minData + offsetAdjustment;

            //
            // do the actual rescale
            //

            // create a proper layout to impose the target data type
            final ImageLayout2 layout = new ImageLayout2(inputWorker.getRenderedImage());
            ComponentColorModel colorModel =
                    new ComponentColorModel(
                            ColorSpace.getInstance(ColorSpace.CS_GRAY),
                            false,
                            false,
                            Transparency.OPAQUE,
                            DataBuffer.TYPE_BYTE);
            layout.setColorModel(colorModel);
            layout.setSampleModel(
                    colorModel.createCompatibleSampleModel(
                            inputWorker.getRenderedImage().getWidth(),
                            inputWorker.getRenderedImage().getHeight()));
            RenderingHints localHints = hints.clone();
            localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
            if (destinationNoData != null) {
                // rescale op will fill pixel nodata value with background value
                // let's use the destination nodata as new background.
                inputWorker.setBackground(new double[] {destinationNoData.getAsSingleValue()});
            }
            // rescale
            inputWorker.setRenderingHints(localHints);
            inputWorker.rescale(new double[] {scale}, new double[] {offset});
            return inputWorker.getRenderedImage();
        }

        @Override
        LookupTable createByteLookupTable(Map<String, Object> params) {
            Utilities.ensureNonNull("params", params);
            double min = (double) params.get(KEY_MIN);
            double max = (double) params.get(KEY_MAX);
            final double delta = max - min;
            final double scale = 255 / delta;
            final double offset = -scale * min;
            // create the lookup table
            final byte[] lut = new byte[256];
            for (int i = 1; i < lut.length; i++) {
                lut[i] =
                        i < min
                                ? (byte) MIN_BYTE
                                : (i > max
                                        ? (byte) MAX_BYTE
                                        : ((byte) (scale * i + offset + 0.5d)));
            }

            return generateLookupTableByte(lut);
        }
    },

    NORMALIZE_CLIP_TO_MINMAX {
        @Override
        RenderedImage process(
                ImageWorker inputWorker, Hints hints, Map<String, Expression> parameters) {
            checkParameters(parameters, KEY_MIN, KEY_MAX);
            Map<String, Object> processParams = getMinMaxParams(parameters);
            RenderedImage inputImage = inputWorker.getRenderedImage();
            final int dataType = inputImage.getSampleModel().getDataType();
            Utilities.ensureNonNull("processParams", processParams);
            double minData = (double) processParams.get(KEY_MIN);
            double maxData = (double) processParams.get(KEY_MAX);

            if (dataType == DataBuffer.TYPE_BYTE) {

                if (maxData == MAX_BYTE && minData == MIN_BYTE) {
                    return inputImage;
                }

                // Optimization for byte images, we use the lookup operation
                LookupTable table = createByteLookupTable(processParams);
                inputWorker.setRenderingHints(hints);
                inputWorker.lookup(table);
                return inputWorker.getRenderedImage();
            }

            //
            // STEP 2 use generic piecewise
            //
            final PiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform =
                    generatePiecewise(processParams);
            inputWorker.piecewise(transform, Integer.valueOf(0));
            return inputWorker.getRenderedImage();
        }

        @Override
        LookupTable createByteLookupTable(Map<String, Object> params) {
            double min = (double) params.get(KEY_MIN);
            double max = (double) params.get(KEY_MAX);

            byte[] lut = createClampingLookupTableByte(min, max, (byte) min, (byte) max);
            return generateLookupTableByte(lut);
        }

        @Override
        PiecewiseTransform1D<DefaultPiecewiseTransform1DElement> generatePiecewise(
                Map<String, Object> params) {
            Utilities.ensureNonNull("params", params);
            double minimum = (double) params.get(KEY_MIN);
            double maximum = (double) params.get(KEY_MAX);
            return generateClampingPiecewise(minimum, maximum, minimum, maximum);
        }
    },

    NORMALIZE_CLIP_TO_ZERO {
        @Override
        RenderedImage process(
                ImageWorker inputWorker, Hints hints, Map<String, Expression> parameters) {
            checkParameters(parameters, KEY_MIN, KEY_MAX);
            Map<String, Object> processParams = getMinMaxParams(parameters);
            RenderedImage inputImage = inputWorker.getRenderedImage();
            final int dataType = inputImage.getSampleModel().getDataType();
            Utilities.ensureNonNull("processParams", processParams);
            double minData = (double) processParams.get(KEY_MIN);
            double maxData = (double) processParams.get(KEY_MAX);

            if (dataType == DataBuffer.TYPE_BYTE) {

                // Optimization for byte images, we use the lookup operation
                if (maxData == MAX_BYTE && minData == MIN_BYTE) return inputImage;

                LookupTable table = createByteLookupTable(processParams);
                inputWorker.setRenderingHints(hints);
                inputWorker.lookup(table);
                return inputWorker.getRenderedImage();
            }

            //
            // STEP 2 use generic piecewise
            //
            processParams.put(KEY_DATATYPE, dataType);
            final PiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform =
                    generatePiecewise(processParams);
            inputWorker.piecewise(transform, Integer.valueOf(0));
            return inputWorker.getRenderedImage();
        }

        @Override
        LookupTable createByteLookupTable(Map<String, Object> params) {
            double min = (double) params.get(KEY_MIN);
            double max = (double) params.get(KEY_MAX);
            // create the lookup table
            final byte[] lut =
                    createClampingLookupTableByte(min, max, (byte) MIN_BYTE, (byte) MIN_BYTE);
            return generateLookupTableByte(lut);
        }

        @Override
        PiecewiseTransform1D<DefaultPiecewiseTransform1DElement> generatePiecewise(
                Map<String, Object> params) {
            Utilities.ensureNonNull("params", params);
            double minimum = (double) params.get(KEY_MIN);
            double maximum = (double) params.get(KEY_MAX);
            return generateClampingPiecewise(minimum, maximum, 0, 0);
        }
    };

    private static NoDataContainer getDestinationNoData(ImageWorker inputWorker) {
        Range nodata = inputWorker.extractNoDataProperty(inputWorker.getRenderedImage());
        NoDataContainer imposedNoData = null;
        if (nodata != null && !nodata.contains(0)) {
            imposedNoData = new NoDataContainer(0);
        }
        return imposedNoData;
    }

    /**
     * Return a Piecewise transform doing clamping outside the central range
     *
     * @param minimum minimum valid value of the central range
     * @param maximum maximum valid value of the central range
     * @param minValue minValue to be returned from values outside (below) the central range
     * @param maxValue maxValue to be returned from values outside (above) the central range
     */
    private static PiecewiseTransform1D<DefaultPiecewiseTransform1DElement>
            generateClampingPiecewise(
                    double minimum, double maximum, double minValue, double maxValue) {
        final DefaultPiecewiseTransform1DElement zeroElement =
                DefaultPiecewiseTransform1DElement.create(
                        "clamp-to-min", RangeFactory.create(0, true, minimum, false), minValue);
        final DefaultPiecewiseTransform1DElement mainElement =
                DefaultPiecewiseTransform1DElement.create(
                        "passthrough", RangeFactory.create(minimum, maximum));
        final DefaultPiecewiseTransform1DElement maxElement =
                DefaultPiecewiseTransform1DElement.create(
                        "clamp-to-max",
                        RangeFactory.create(maximum, false, Double.POSITIVE_INFINITY, true),
                        maxValue);

        return new DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement>(
                new DefaultPiecewiseTransform1DElement[] {zeroElement, mainElement, maxElement}, 0);
    }

    /** Extract min and max parameter values from the ContrastMethod parameters map */
    private static Map<String, Object> getMinMaxParams(Map<String, Expression> parameters) {
        Expression min = parameters.get(KEY_MIN);
        Expression max = parameters.get(KEY_MAX);
        if (min == null || max == null) {
            throw new IllegalArgumentException(
                    Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_PARAMETERS_$2, KEY_MIN, KEY_MAX));
        }
        Map<String, Object> params = new HashMap<String, Object>();
        Number minVal = min.evaluate(null, Double.class);
        Number maxVal = max.evaluate(null, Double.class);
        if (minVal == null || maxVal == null) {
            throw new IllegalArgumentException(
                    Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_PARAMETERS_$2, KEY_MIN, KEY_MAX));
        }
        params.put(KEY_MIN, minVal.doubleValue());
        params.put(KEY_MAX, maxVal.doubleValue());
        return params;
    }

    /**
     * Create a LookupTable for passthrough mapping except for values outside [min,max] range which
     * should be mapped to newMin, newMax respectively.
     */
    private static byte[] createClampingLookupTableByte(
            double min, double max, byte newMin, byte newMax) {
        final byte[] lut = new byte[256];
        for (int i = 1; i < lut.length; i++) {
            lut[i] = i < min ? (byte) newMin : (i > max ? (byte) newMax : (byte) i);
        }
        return lut;
    }

    /** Return the max Value for the specified dataType */
    private static double getMaxValue(int dataType) {
        switch (dataType) {
            case DataBuffer.TYPE_BYTE:
                return (double) MAX_BYTE;
            case DataBuffer.TYPE_SHORT:
                return (double) Short.MAX_VALUE;
            case DataBuffer.TYPE_USHORT:
                return 65535.0;
            case DataBuffer.TYPE_INT:
                return (double) Integer.MAX_VALUE;
            case DataBuffer.TYPE_FLOAT:
                return (double) Float.MAX_VALUE;
            case DataBuffer.TYPE_DOUBLE:
                return (double) Double.MAX_VALUE;
        }
        return Double.NaN;
    }

    /** Simple utility method to check Double values equality. */
    public static boolean areEqual(double a, double b) {
        if (!Double.isNaN(a) && !Double.isNaN(b)) {
            return Math.abs(a - b) < DELTA;
        }
        return false;
    }

    /** Generate a Linear Byte LookupTable to map from [min,max] to [0, 255] */
    static LookupTable generateLinearByteLookupTable(Map<String, Object> params) {
        Utilities.ensureNonNull("params", params);
        double min = (double) params.get(KEY_MIN);
        double max = (double) params.get(KEY_MAX);
        final double delta = max - min;
        final double scale = 255 / delta;
        final double offset = -scale * min;
        // create the lookup table
        final byte[] lut = new byte[256];
        for (int i = 1; i < lut.length; i++) {
            lut[i] = (byte) (scale * i + offset + 0.5d);
        }

        return generateLookupTableByte(lut);
    }

    /**
     * Main processing methods to be implemented by specific contrast enhancement algorithm to
     * return an enhanced image.
     */
    abstract RenderedImage process(
            ImageWorker inputWorker, Hints hints, Map<String, Expression> parameters);

    /**
     * Create a byte LookupTable, specific for optimized byte cases.
     *
     * @param params Parameters (when required) to be used by the specific implementation.
     */
    LookupTable createByteLookupTable(Map<String, Object> params) {
        throw new UnsupportedOperationException();
    };

    /**
     * Create a {@link PiecewiseTransform1D} for the general case (!= Byte dataType). Different
     * algorithms may implement it accordingly. Default implementation throws {@link
     * UnsupportedOperationException}
     *
     * @param params Parameters (when required) to be used by specific implementation. Different
     *     algorithm may requires different parameters. As an instance, some implementations
     *     requires min and max values whilst some other may require an Histogram.
     */
    PiecewiseTransform1D<DefaultPiecewiseTransform1DElement> generatePiecewise(
            Map<String, Object> params) {
        throw new UnsupportedOperationException();
    }

    private static final int MAX_BYTE = 255;

    private static final int MIN_BYTE = 0;

    /** Parameters keys */

    /** Parameter Key used to refer to the minimim value */
    public static final String KEY_MIN = "minValue";

    /** Parameter Key used to refer to the maximim value */
    public static final String KEY_MAX = "maxValue";

    /** Parameter Key used to refer to the maximim value */
    public static final String KEY_DATATYPE = "dataType";

    /** Parameter Key used to refer to the histogram instance */
    public static final String KEY_HISTOGRAM = "histogram";

    /** Parameter Key used to refer to the correctionFactor value */
    public static final String KEY_CORRECTION_FACTOR = "correctionFactor";

    /** Parameter Key used to refer to the normalizationFactor value */
    public static final String KEY_NORMALIZATION_FACTOR = "normalizationFactor";

    public static final String NORMALIZE_STRETCH_TO_MINMAX_NAME = "StretchToMinimumMaximum";
    public static final String NORMALIZE_CLIP_TO_MINMAX_NAME = "ClipToMinimumMaximum";
    public static final String NORMALIZE_CLIP_TO_ZERO_NAME = "ClipToZero";

    private static final double DELTA = 1E-8;

    /** Empty parameters map which can be used by implementations which don't need any parameters */
    private static Map<String, Object> EMPTY_MAP = Collections.emptyMap();

    /** Minimal normalized value. */
    private static final double MIN_VALUE = 0d;

    /** Maximal normalized value. */
    private static final double MAX_VALUE = 1d;

    /** Simple utility method generating a Byte Based lookup table */
    private static LookupTable generateLookupTableByte(byte[] lut) {
        return LookupTableFactory.create(lut, DataBuffer.TYPE_BYTE);
    }

    /** Generate piecewise transformation for gamma correction */
    public static PiecewiseTransform1D<DefaultPiecewiseTransform1DElement>
            generateGammaCorrectedPiecewise(
                    final double minimum, final double maximum, final double gammaValue) {

        final double scale = (maximum - minimum) / (MAX_VALUE - MIN_VALUE);
        final double offset = minimum - MIN_VALUE * scale;
        final DefaultPiecewiseTransform1DElement mainElement =
                DefaultPiecewiseTransform1DElement.create(
                        "gamma-correction-transform",
                        RangeFactory.create(minimum, maximum),
                        new MathTransformationAdapter() {

                            public double derivative(double value) throws TransformException {

                                throw new UnsupportedOperationException(
                                        Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1));
                            }

                            public boolean isIdentity() {
                                return false;
                            }

                            public double transform(double value) {
                                value = (value - offset) / scale;
                                return offset + Math.pow(value, gammaValue) * scale;
                            }
                        });

        return new DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement>(
                new DefaultPiecewiseTransform1DElement[] {mainElement}, 0);
    }

    /** Utility method setting up a Parameters Map containing minimum and maximum values. */
    private static Map<String, Object> setMinMaxParams(double minData, double maxData) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(KEY_MIN, minData);
        params.put(KEY_MAX, maxData);
        return params;
    }

    /** Check the parameters map isn't null and contains valid entries */
    private static void checkParameters(
            Map<String, Expression> parameters, String parameter1, String parameter2) {
        if (parameters == null) {
            throw new IllegalArgumentException(
                    Errors.format(ErrorKeys.NULL_ARGUMENT_$1, parameters));
        }
        if (parameters.isEmpty()) {
            throw new IllegalArgumentException(
                    Errors.format(
                            ErrorKeys.ILLEGAL_ARGUMENT_PARAMETERS_$2, parameter1, parameter2));
        }
    }

    /**
     * Return a proper {@link ContrastEnhancementType} instance depending on the provided {@link
     * AbstractContrastMethodStrategy}.
     */
    public static ContrastEnhancementType getType(AbstractContrastMethodStrategy method) {
        if (method instanceof NormalizeContrastMethodStrategy) {
            String algorithmType = parseAlgorithm(method.getAlgorithm());
            if (algorithmType == null) {
                return NORMALIZE_DEFAULT;
            } else if (NORMALIZE_STRETCH_TO_MINMAX_NAME.equals(algorithmType)) {
                return NORMALIZE_STRETCH_TO_MINMAX;
            } else if (NORMALIZE_CLIP_TO_MINMAX_NAME.equals(algorithmType)) {
                return NORMALIZE_CLIP_TO_MINMAX;
            } else if (NORMALIZE_CLIP_TO_ZERO_NAME.equals(algorithmType)) {
                return NORMALIZE_CLIP_TO_ZERO;
            }
            throw new IllegalArgumentException(
                    Errors.format(ErrorKeys.UNSUPPORTED_ALGORITHM_$1, algorithmType));
        } else if (method instanceof LogarithmicContrastMethodStrategy) {
            return LOGARITHMIC;
        } else if (method instanceof ExponentialContrastMethodStrategy) {
            return EXPONENTIAL;
        } else if (method instanceof org.geotools.styling.HistogramContrastMethodStrategy) {
            return HISTOGRAM;
        } else {
            throw new IllegalArgumentException(
                    Errors.format(ErrorKeys.UNSUPPORTED_METHOD_$1, method));
        }
    }

    /** Parse the algorithm expression */
    private static String parseAlgorithm(Expression algorithm) {
        if (algorithm != null) {
            return algorithm.evaluate(null, String.class);
        }
        return null;
    }
}
