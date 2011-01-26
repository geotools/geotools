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
package org.geotools.coverage.processing.operation;

// J2SE, JAI and extensions
import java.awt.Color;
import java.awt.geom.AffineTransform;
import javax.media.jai.KernelJAI;
import javax.media.jai.ParameterList;
import javax.media.jai.ParameterListDescriptor;
import javax.media.jai.ParameterListDescriptorImpl;
import javax.media.jai.operator.GradientMagnitudeDescriptor; // For javadoc
import javax.measure.unit.Unit;

// OpenGIS dependencies
import org.opengis.util.InternationalString;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.MathTransform2D;

// Geotools dependencies
import org.geotools.coverage.Category;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.OperationJAI;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.util.NumberRange;


/**
 * Edge detector which computes the magnitude of the image gradient vector in two orthogonal
 * directions. The result of the {@code "GradientMagnitude"} operation may be defined as:
 * <p>
 *   <BLOCKQUOTE><CODE>
 *   dst[<var>x</var>][<var>y</var>][<var>b</var>] = {@linkplain Math#sqrt sqrt}(
 *   <strong>SH</strong>(<var>x</var>,<var>y</var>,<var>b</var>)<sup>2</sup> +
 *   <strong>SV</strong>(<var>x</var>,<var>y</var>,<var>b</var>)<sup>2</sup>)
 *   &times; <var>scale</var>
 *   </CODE></BLOCKQUOTE>
 * <p>
 * where {@code SH(x,y,b)} and {@code SV(x,y,b)} are the horizontal and vertical gradient images
 * generated from band <var>b</var> of the source image by correlating it with the supplied
 * orthogonal (horizontal and vertical) gradient masks.
 * <p>
 * Before to compute the gradients, the kernels are tested against artificials horizontal and
 * vertical gradients of one <cite>unit of sample / unit of localization</cite>. For example:
 * <p>
 * <UL>
 *   <LI>For an image of elevation (meters) using a geographic coordinate system (degrees
 *       of latitude and longitude), the units are <strong>meters/degree</strong>.</LI>
 *   <LI>For an image of temperature (°C) using a projected coordinate system (kilometers),
 *       the units are <strong>°C/km</strong>.</LI>
 * </UL>
 * <p>
 * Kernels are normalized by dividing all their coefficients by the result of this test. In other
 * words, kernels are normalized in such a way that applying the {@code "GradientMagnitude"}
 * operation on a horizontal or vertical gradient of 1 such "geophysical" units will give a result
 * of 1. This is an attempt to give geophysical meaning to the numbers produced by the
 * {@code "GradientMagnitude"} operation. This normalization depends of the coverage's
 * {@linkplain GridCoverage2D#getGridGeometry grid geometry}.
 * <p>
 * <strong>NOTE:</strong> When the masks are symetric (e.g. Sobel, Prewitt (or Smoothed),
 * isotropic, <cite>etc.</cite>), then the above-cited algorithm produces the same result
 * than the "<cite>normalization factor</cite>" &times; "<cite>spatial factor</cite>" published by:
 *
 * <blockquote>
 * Simpson, J.J. (1990), "<u>On the accurate detection and enhancement of oceanic features
 * observed in satellite data</u>" <i>in</i> Remote sensing environment, <b>33:17-33</b>.
 * </blockquote>
 *
 * However, for non-symetric masks (e.g. Kirsch), then a difference is found.
 *
 * <P><STRONG>Name:</STRONG>&nbsp;<CODE>"GradientMagnitude"</CODE><BR>
 *    <STRONG>JAI operator:</STRONG>&nbsp;<CODE>"{@linkplain GradientMagnitudeDescriptor GradientMagnitude}"</CODE><BR>
 *    <STRONG>Parameters:</STRONG></P>
 *  <table border='3' cellpadding='6' bgcolor='F4F8FF'>
 *    <tr bgcolor='#B9DCFF'>
 *      <th>Name</th>
 *      <th>Class</th>
 *      <th>Default value</th>
 *      <th>Minimum value</th>
 *      <th>Maximum value</th>
 *    </tr>
 *    <tr>
 *      <td>{@code "Source"}</td>
 *      <td>{@link org.geotools.coverage.grid.GridCoverage2D}</td>
 *      <td align="center">N/A</td>
 *      <td align="center">N/A</td>
 *      <td align="center">N/A</td>
 *    </tr>
 *    <tr>
 *      <td>{@code "Mask1"}</td>
 *      <td>{@link KernelJAI}</td>
 *      <td>{@link KernelJAI#GRADIENT_MASK_SOBEL_HORIZONTAL}</td>
 *      <td align="center">N/A</td>
 *      <td align="center">N/A</td>
 *    </tr>
 *    <tr>
 *      <td>{@code "Mask2"}</td>
 *      <td>{@link KernelJAI}</td>
 *      <td>{@link KernelJAI#GRADIENT_MASK_SOBEL_VERTICAL}</td>
 *      <td align="center">N/A</td>
 *      <td align="center">N/A</td>
 *    </tr>
 *  </table>
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see org.geotools.coverage.processing.Operations#gradientMagnitude
 * @see GradientMagnitudeDescriptor
 */
public class GradientMagnitude extends OperationJAI {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -1514713427236924048L;

    /**
     * Set to {@code true} for enabling some tracing code.
     */
    private static final boolean DEBUG = false;

    /**
     * Set to {@code true} to enable automatic kernel normalization. Normalization modifies
     * kernel coefficients according the "grid to coordinate system" transform in order to get
     * some meaningful engineering units (e.g. °C/km). The normalization factor is computed by
     * testing the original kernels against synthetic horizontal and vertical gradients of
     * 1 sampleUnit/csUnit.
     */
    private static final boolean NORMALIZE = true;

    /**
     * The default scale factor to apply on the range computed by {@link #deriveCategory}. For
     * example a value of 0.25 means that only values from 0 to 25% of the expected maximum will
     * appears in different colors.
     */
    private static final double DEFAULT_RANGE_SCALE = 1.0;

    /**
     * The default color palette for the gradients.
     */
    private static final Color[] DEFAULT_COLOR_PALETTE = new Color[] {
        new Color( 16, 32, 64),
        new Color(192,224,255)
    };

    /**
     * A flag indicating that {@link #getNormalizationFactorSquared}
     * should tests the horizontal gradient computed by the supplied kernel.
     */
    private static final int HORIZONTAL = 1;

    /**
     * A flag indicating that {@link #getNormalizationFactorSquared}
     * should tests the vertical gradient computed by the supplied kernel.
     */
    private static final int VERTICAL = 2;

    /**
     * Constructs a default gradient magnitude operation.
     */
    public GradientMagnitude() {
        super("GradientMagnitude");
    }

    /**
     * Returns a scale factor for the supplied kernel. If {@code kernel} computes horizontal
     * grandient, this method returns {@code scaleX}. Otherwise, if {@code kernel} computes
     * vertical gradient, then this method returns {@code scaleY}. Otherwise, returns a geometric
     * combinaison of both.
     */
    private static double getScaleFactor(final KernelJAI kernel, double scaleX, double scaleY) {
        scaleX *= scaleX;
        scaleY *= scaleY;
        double factorX = getNormalizationFactorSquared(kernel, HORIZONTAL);
        double factorY = getNormalizationFactorSquared(kernel, VERTICAL);
        double factor2 = (factorX*scaleX + factorY*scaleY) / (factorX+factorY);
        return Math.sqrt(factor2);
    }

    /**
     * Returns the square of a normalization factor for the supplied kernel.
     * The kernel can be normalized by invoking {@link #divide(KernelJAI,double)}
     * with the square root of this value.
     *
     * @param  kernel The kernel for which to compute normalization factor.
     * @param  type Any combinaison of {@link #HORIZONTAL} and {@link #VERTICAL}.
     * @return The square of a normalization factor that could be applied on the kernel.
     */
    private static double getNormalizationFactorSquared(final KernelJAI kernel, final int type) {
        double sumH = 0;
        double sumV = 0;
        final int width  = kernel.getWidth();
        final int height = kernel.getHeight();
        /*
         * Tests the kernel with a horizontal gradient       [ -1   0   1 ]
         * of 1/pixel. For example, we get sumH=8 with       [ -2   0   2 ]
         * the horizontal Sobel kernel show on right:        [ -1   0   1 ]
         */
        if ((type & HORIZONTAL) != 0) {
            int value = kernel.getYOrigin();
            for (int y=height; --y>=0;) {
                for (int x=width; --x>=0;) {
                    sumH += value * kernel.getElement(x,y);
                }
                value--;
            }
        }
        /*
         * Tests the kernel with a vertical gradient of      [ -1  -2  -1 ]
         * 1/pixel. For example, we get sumV=8 with the      [  0   0   0 ]
         * vertical Sobel kernel show on right:              [  1   2   1 ]
         */
        if ((type & VERTICAL) != 0) {
            int value = kernel.getXOrigin();
            for (int x=width; --x>=0;) {
                for (int y=height; --y>=0;) {
                    sumV += value * kernel.getElement(x,y);
                }
                value--;
            }
        }
        return (sumH*sumH) + (sumV*sumV);
    }

    /**
     * Returns the normalization factor for the supplied kernel. The kernel can
     * be normalized by invoking {@link #divide(KernelJAI,double)} with this factor.
     *
     * @param  mask1 The first kernel for which to compute a normalization factor.
     * @param  mask2 The second kernel for which to compute a normalization factor.
     * @return The normalization factor that could be applied on both kernels.
     *
     * @todo When the masks are symetric (e.g. Sobel, Prewitt (or Smoothed), isotropic, etc.),
     *       then this algorithm matches the "normalization factor" times "spatial factor"
     *       provided by
     *
     *       J.J. Simpson (1990), "On the accurate detection and enhancement of oceanic features
     *       observed in satellite data" in Remote sensing environment, 33:17-33.
     *
     *       However, for non-symetric masks (e.g. Kirsch), then a difference is found.
     *       We should provides a way to disable normalization when the user did it himself
     *       according some other rules than the one used here.
     */
    private static double getNormalizationFactor(final KernelJAI mask1, final KernelJAI mask2) {
        double factor;
        factor  = getNormalizationFactorSquared(mask1, HORIZONTAL|VERTICAL);
        factor += getNormalizationFactorSquared(mask2, HORIZONTAL|VERTICAL);
        factor  = Math.sqrt(factor/2);
        return factor;
    }

    /**
     * Divides a kernel by some number.
     *
     * @param  kernel The kernel to divide.
     * @param  denominator The factor to divide by.
     * @return The resulting kernel.
     */
    private static KernelJAI divide(KernelJAI kernel, final double denominator) {
        if (denominator != 1) {
            final float[] data = kernel.getKernelData();
            final int length = data.length;
            for (int i=0; i<length; i++) {
                data[i] /= denominator;
            }
            kernel = new KernelJAI(kernel.getWidth(),   kernel.getHeight(),
                                   kernel.getXOrigin(), kernel.getYOrigin(), data);
        }
        return kernel;
    }

    /**
     * Applies the operation on grid coverage. The default implementation looks for kernels
     * specified in the {@link org.geotools.coverage.processing.OperationJAI.Parameters#parameters
     * parameter block} and divide them by the distance between pixels, in the grid coverage's
     * coordinate reference system.
     */
    protected GridCoverage2D deriveGridCoverage(final GridCoverage2D[] sources,
                                                final Parameters  parameters)
    {
        if (NORMALIZE) {
            final ParameterList block = parameters.parameters;
            KernelJAI mask1 = (KernelJAI) block.getObjectParameter("Mask1");
            KernelJAI mask2 = (KernelJAI) block.getObjectParameter("Mask2");
            /*
             * Normalizes the kernel in such a way that pixel values likes
             * [-2 -1 0 +1 +2] will give a gradient of about 1 unit/pixel.
             */
            double factor = getNormalizationFactor(mask1, mask2);
            if (!(factor > 0)) {
                // Do not transform if factor is 0 or NaN.
                factor = 1;
            }
            /*
             * Computes a scale factor taking in account the transformation from
             * grid to coordinate system. This scale will convert gradient from
             * 1 unit/pixel to 1 unit/meters or 1 unit/degrees, depending the
             * coordinate reference systems axis unit.
             */
            double scaleMask1 = 1;
            double scaleMask2 = 1;
            if (sources.length != 0) {
                final MathTransform2D mtr;
                mtr = ((GridGeometry2D) sources[0].getGridGeometry()).getGridToCRS2D();
                if (mtr instanceof AffineTransform) {
                    final AffineTransform tr = (AffineTransform) mtr;
                    final double scaleX = XAffineTransform.getScaleX0(tr);
                    final double scaleY = XAffineTransform.getScaleY0(tr);
                    scaleMask1 = getScaleFactor(mask1, scaleX, scaleY);
                    scaleMask2 = getScaleFactor(mask2, scaleX, scaleY);
                    if (!(scaleMask1>0 && scaleMask2>0)) {
                        // Do not rescale if scale is 0 or NaN.
                        scaleMask1 = 1;
                        scaleMask2 = 1;
                    }
                    if (DEBUG) {
                        System.out.print("factor=     "); System.out.println(factor);
                        System.out.print("scaleMask1= "); System.out.println(scaleMask1);
                        System.out.print("scaleMask1= "); System.out.println(scaleMask2);
                    }
                }
            }
            block.setParameter("Mask1", divide(mask1, factor*scaleMask1));
            block.setParameter("Mask2", divide(mask2, factor*scaleMask2));
        }
        return super.deriveGridCoverage(sources, parameters);
    }

    /**
     * Derives the quantitative category for a band in the destination image.
     * This implementation computes the expected gradient range from the two
     * masks and the value range in the source grid coverage.
     */
    protected Category deriveCategory(final Category[] categories, final Parameters parameters) {
        NumberRange          range = null;
        Category          category = categories[0];
        final NumberRange  samples = category.geophysics(false).getRange();
        final boolean isGeophysics = (category == category.geophysics(true));
        /*
         * Computes a default range of output values one from the normalized kernels.
         * The normalization has been done by 'deriveGridCoverage' before this method
         * is invoked. The algorithm is as below:
         *
         * - Computes the value produced by the kernels for an artificial gradient of 1 unit/pixel.
         * - Transforms into a lower gradient of 1 unit/(kernel size).
         * - Transforms into a gradient of (maximal range)/(kernel size).
         * - Applies an arbitrary correction factor for more convenient range in most cases.
         */
        final ParameterList block = parameters.parameters;
        final KernelJAI mask1 = (KernelJAI) block.getObjectParameter("Mask1");
        final KernelJAI mask2 = (KernelJAI) block.getObjectParameter("Mask2");
        final double size = (mask1.getWidth() + mask1.getHeight() +
                             mask2.getWidth() + mask2.getHeight()) / 4.0;
        double factor = getNormalizationFactor(mask1, mask2) / (size-1);
        if (factor>0 && !Double.isInfinite(factor)) {
            range = category.geophysics(true).getRange();
            final double minimum = range.getMinimum();
            final double maximum = range.getMaximum();
            factor *= (maximum - minimum) * DEFAULT_RANGE_SCALE;
            range = new NumberRange(0, factor);
        }
        if (range != null) {
            category = new Category(category.getName(), DEFAULT_COLOR_PALETTE, samples, range);
            return category.geophysics(isGeophysics);
        }
        return super.deriveCategory(categories, parameters);
    }

    /**
     * Derives the unit of data for a band in the destination image.
     * This method compute {@code sample}/{@code axis} where:
     *
     * <ul>
     *   <li>{@code sample} is the sample unit in source image.</li>
     *   <li>{@code axis} is the coordinate reference system axis unit.</li>
     * </ul>
     */
    protected Unit deriveUnit(final Unit[] units, final Parameters parameters) {
        final CoordinateSystem cs = parameters.crs.getCoordinateSystem();
        if (!DEBUG && units.length==1 && units[0]!=null) {
            final Unit spatialUnit = cs.getAxis(0).getUnit();
            for (int i=Math.min(cs.getDimension(), 2); --i>=0;) {
                if (!spatialUnit.equals(cs.getAxis(i).getUnit())) {
                    return super.deriveUnit(units, parameters);
                }
            }
            try {
                return units[0].divide(spatialUnit);
            } catch (RuntimeException exception) {
                // Can't compute units... We will compute image data
                // anyway, but the result will have no know unit.
                // TODO: Catch a more specific exception.
            }
        }
        return super.deriveUnit(units, parameters);
    }
}
