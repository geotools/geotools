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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.operation;

import static org.geotools.referencing.AbstractIdentifiedObject.nameMatches;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.measure.Unit;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.crs.ProjectedCRS;
import org.geotools.api.referencing.cs.CoordinateSystem;
import org.geotools.api.referencing.operation.Conversion;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.Matrix;
import org.geotools.metadata.i18n.LoggingKeys;
import org.geotools.metadata.i18n.Loggings;
import org.geotools.referencing.CRS;
import org.geotools.referencing.cs.AbstractCS;
import org.geotools.referencing.factory.ReferencingFactory;
import org.geotools.referencing.operation.matrix.MatrixFactory;
import org.geotools.referencing.operation.matrix.XMatrix;
import org.geotools.referencing.operation.projection.MapProjection; // For javadoc
import org.geotools.referencing.operation.transform.AbstractMathTransform;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.util.Utilities;
import si.uom.SI;

/**
 * Returns a conversion from a source to target projected CRS, if this conversion is representable as an affine
 * transform. More specifically, if all projection parameters are identical except the following ones:
 *
 * <p>
 *
 * <UL>
 *   <LI>{@link MapProjection.AbstractProvider#SCALE_FACTOR scale_factor}
 *   <LI>{@link MapProjection.AbstractProvider#FALSE_EASTING false_easting}
 *   <LI>{@link MapProjection.AbstractProvider#FALSE_NORTHING false_northing}
 * </UL>
 *
 * <p>Then the conversion between two projected CRS can sometime be represented as a linear conversion. For example if
 * only false easting/northing differ, then the coordinate conversion is simply a translation.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class ProjectionAnalyzer {
    /** The map projection. */
    private final Conversion projection;

    /**
     * The affine transform applied on projected coordinates after the projection. In Geotools {@link MapProjection}
     * implementation, this is the axis swapping and scaling needed in order to get standard (<var>x</var>,<var>y</var>)
     * axis in metres. Can be {@code null} if none.
     */
    private final Matrix projectedScale;

    /**
     * The transform for the map projection alone, without the {@link #geographicScale} and {@link #projectedScale}
     * parts. In Geotools implementation, it should be an instance of {@link MapProjection}. May be {@code null} if we
     * can't handle the {@linkplain #projection}.
     */
    private final MathTransform transform;

    /** The map projection parameters values, or a copy of them. */
    private List<GeneralParameterValue> parameters;

    /**
     * Constructs a {@code ProjectionAnalyzer} for the specified projected CRS. This constructor inspects the
     * {@linkplain ProjectedCRS#getConversionFromBase conversion from base} and splits {@link ConcatenatedTransform} in
     * their {@link #geographicScale}, {@link #projectedScale} and {@link #transform} components.
     */
    private ProjectionAnalyzer(final ProjectedCRS crs) {
        Matrix geographicScale = null;
        Matrix projectedScale = null;
        projection = crs.getConversionFromBase();
        MathTransform candidate = projection.getMathTransform();
        while (candidate instanceof ConcatenatedTransform) {
            final ConcatenatedTransform ctr = (ConcatenatedTransform) candidate;
            if (ctr.transform1 instanceof LinearTransform) {
                if (geographicScale != null) {
                    // Should never happen with ConcatenatedTransform.create(...) implementation.
                    throw new IllegalStateException(String.valueOf(candidate));
                }
                geographicScale = ((LinearTransform) ctr.transform1).getMatrix();
                candidate = ctr.transform2;
                continue;
            }
            if (ctr.transform2 instanceof LinearTransform) {
                if (projectedScale != null) {
                    // Should never happen with ConcatenatedTransform.create(...) implementation.
                    throw new IllegalStateException(String.valueOf(candidate));
                }
                projectedScale = ((LinearTransform) ctr.transform2).getMatrix();
                candidate = ctr.transform1;
                continue;
            }
            // Both transforms are non-linear. We can not handle that.
            candidate = null;
            break;
        }
        //
        // TODO: We need to handle PassthroughTransform here in some future version
        //       (when we will want better handling of 3D coordinates).
        //
        /*
         * We should really fetch the parameters from the MathTransform as much as we can, since
         * this is the most robust source of information (the one which is the most likely to be
         * an accurate description of the map projection without the above geographic and projected
         * scale components). However if we are not able to query the math transform, we will query
         * the Conversion object as a fallback and hope that it describes only the map projection
         * part, as in Geotools implementation.
         */
        ParameterValueGroup group = null;
        if (candidate instanceof AbstractMathTransform) {
            group = ((AbstractMathTransform) candidate).getParameterValues();
        }
        if (group == null) {
            /*
             * Fallback path only if we don't have a Geotools MapProjection implementation.
             * NOTE: it is uncertain that we should call 'swapAndScaleAxis'. If the CS has
             * standard axis, it will not hurt since we should get the identity transform.
             * If the CS doesn't have standard axis, then 'projectedScale' should be non-
             * null and 'swapAndScaleAxis' is not needed. But if none of the above hold,
             * then some axis swapping is probably done straight into the unknown 'transform'
             * implementation and we need to "guess" what it is. Those rules are somewhat
             * heuristic; the previous "if" branch for Geotools MapProjection implementations
             * should be more determinist.
             */
            group = projection.getParameterValues();
            if (projectedScale == null) {
                final CoordinateSystem cs = crs.getCoordinateSystem();
                projectedScale = AbstractCS.swapAndScaleAxis(AbstractCS.standard(cs), cs);
            }
        }
        if (group != null) {
            parameters = group.values();
        }
        this.projectedScale = projectedScale;
        this.transform = candidate;
    }

    /** Returns the {@linkplain #transform} parameter descriptor, or {@code null} if none. */
    private ParameterDescriptorGroup getTransformDescriptor() {
        return transform instanceof AbstractMathTransform
                ? ((AbstractMathTransform) transform).getParameterDescriptors()
                : null;
    }

    /**
     * Returns the affine transform applied after the <em>normalized</em> projection in order to get the same projection
     * than {@link #transform}. The normalized projection is a imaginary transform (we don't have a
     * {@link MathTransform} instance for it, but we don't need) with {@code "scale factor"} == 1, {@code "false
     * easting"} == 0 and {@code "false northing"} == 0. In other words, this method extracts the above-cited parameters
     * in an affine transform.
     *
     * <p>As a side effect, this method removes from the {@linkplain #parameters} list all the above-cited ones
     * parameters.
     *
     * @return The affine transform.
     */
    private XMatrix normalizedToProjection() {
        parameters = new LinkedList<>(parameters); // Keep the original list unchanged.
        /*
         * Creates a matrix which will conceptually stands between the normalized transform and
         * the 'projectedScale' transform. The matrix dimensions are selected accordingly using
         * a robust code when possible, but the result should be a 3x3 matrix most of the time.
         */
        final int sourceDim = transform != null ? transform.getTargetDimensions() : 2;
        final int targetDim = projectedScale != null ? projectedScale.getNumCol() - 1 : sourceDim;
        final XMatrix matrix = MatrixFactory.create(targetDim + 1, sourceDim + 1);
        /*
         * Search for "scale factor", "false easting" and "false northing" parameters.
         * All parameters found are removed from the list. Note: we assume that linear
         * units in the "normalized projection" are metres, as specified in the legacy
         * OGC 01-009 specification, and that unit conversions (if needed) are applied
         * by 'projectedScale'. However there is no way I can see to ensure that since
         * it is really a matter of how the map projection is implemented (for example
         * the unit conversion factor could be merged with the "scale_factor" -- not a
         * clean approach and I do not recommand, but some could do in order to save a
         * few multiplications).
         *
         * We need "false easting" and "false northing" offsets in either user's unit or
         * in metres, depending if the unit conversions are applied in 'transform' or in
         * 'projectedScale' respectively. We assume the later, which stands for Geotools
         * implementation and is closer to OGC 01-009 spirit. But we will log a warning
         * in case of doubt.
         */
        Unit<?> unit = null;
        String warning = null;
        for (final Iterator<GeneralParameterValue> it = parameters.iterator(); it.hasNext(); ) {
            final GeneralParameterValue parameter = it.next();
            if (parameter instanceof ParameterValue) {
                final ParameterValue<?> value = (ParameterValue) parameter;
                final ParameterDescriptor<?> descriptor = value.getDescriptor();
                if (Number.class.isAssignableFrom(descriptor.getValueClass())) {
                    if (nameMatches(descriptor, "scale_factor")) {
                        final double scale = value.doubleValue();
                        for (int i = Math.min(sourceDim, targetDim); --i >= 0; ) {
                            matrix.setElement(i, i, matrix.getElement(i, i) * scale);
                        }
                    } else {
                        final int d;
                        if (nameMatches(descriptor, "false_easting")) {
                            d = 0;
                        } else if (nameMatches(descriptor, "false_northing")) {
                            d = 1;
                        } else {
                            continue;
                        }
                        final double offset = value.doubleValue(SI.METRE);
                        if (!Double.isNaN(offset) && offset != value.doubleValue()) {
                            // See the above comment about units. The above check could have been
                            // replaced by "if (!SI.METRE.equals(unit))", but the above avoid the
                            // warning in the very common case where 'offset == 0'.
                            unit = value.getUnit();
                            warning = descriptor.getName().getCode();
                        }
                        matrix.setElement(d, sourceDim, matrix.getElement(d, sourceDim) + offset);
                    }
                    it.remove();
                }
            }
        }
        if (warning != null) {
            final LogRecord record =
                    Loggings.format(Level.WARNING, LoggingKeys.APPLIED_UNIT_CONVERSION_$3, warning, unit, SI.METRE);
            record.setSourceClassName(getClass().getName());
            record.setSourceMethodName("createLinearConversion"); // This is the public method.
            final Logger logger = ReferencingFactory.LOGGER;
            record.setLoggerName(logger.getName());
            logger.log(record);
        }
        return matrix;
    }

    /**
     * Checks if the parameter in the two specified list contains the same values. The order parameter order is
     * irrelevant. The common parameters are removed from both lists.
     */
    private static boolean parameterValuesEqual(
            final List<GeneralParameterValue> source,
            final List<GeneralParameterValue> target,
            final double errorTolerance) {
        search:
        for (final Iterator<GeneralParameterValue> targetIter = target.iterator(); targetIter.hasNext(); ) {
            final GeneralParameterValue targetPrm = targetIter.next();
            for (final Iterator<GeneralParameterValue> sourceIter = source.iterator(); sourceIter.hasNext(); ) {
                final GeneralParameterValue sourcePrm = sourceIter.next();
                if (!nameMatches(sourcePrm.getDescriptor(), targetPrm.getDescriptor())) {
                    continue;
                }
                if (sourcePrm instanceof ParameterValue && targetPrm instanceof ParameterValue) {
                    final ParameterValue<?> sourceValue = (ParameterValue) sourcePrm;
                    final ParameterValue<?> targetValue = (ParameterValue) targetPrm;
                    if (Number.class.isAssignableFrom(
                            targetValue.getDescriptor().getValueClass())) {
                        final double sourceNum, targetNum;
                        final Unit<?> unit = targetValue.getUnit();
                        if (unit != null) {
                            sourceNum = sourceValue.doubleValue(unit);
                            targetNum = targetValue.doubleValue(unit);
                        } else {
                            sourceNum = sourceValue.doubleValue();
                            targetNum = targetValue.doubleValue();
                        }
                        double error = targetNum - sourceNum;
                        if (targetNum != 0) error /= targetNum;
                        if (!(Math.abs(error) <= errorTolerance)) { // '!' for trapping NaN
                            return false;
                        }
                    } else {
                        // The parameter do not hold a numerical value. It may be a
                        // String, etc. Use the generic Object.equals(Object) method.
                        if (!Utilities.equals(sourceValue.getValue(), targetValue.getValue())) {
                            return false;
                        }
                    }
                } else {
                    // The GeneralParameter is not a ParameterValue instance. It is probably a
                    // ParameterValueGroup. Compare all child elements without processing them.
                    if (!Utilities.equals(targetPrm, sourcePrm)) {
                        return false;
                    }
                }
                // End of processing a pair of matching parameters. The values are equal or
                // were one of the special cases processed above. Continue with a new pair.
                sourceIter.remove();
                targetIter.remove();
                continue search;
            }
            // End of iteration in the 'source' parameters. If we reach this point, then we
            // have found a target parameter without matching source parameter. We consider
            // the two projections as different kind.
            return false;
        }
        // End of iteration in the 'target' parameters, which should now be empty.
        // Check if there is any unmatched parameter left in the supplied list.
        assert target.isEmpty();
        return source.isEmpty();
    }

    /** Applies {@code normalizedToProjection} first, then {@link #projectedScale}. */
    private XMatrix applyProjectedScale(final XMatrix normalizedToProjection) {
        if (projectedScale == null) {
            return normalizedToProjection;
        }
        final XMatrix scale = MatrixFactory.create(projectedScale);
        scale.multiply(normalizedToProjection);
        return scale;
    }

    /**
     * Returns a conversion from a source to target projected CRS, if this conversion is representable as an affine
     * transform. If no linear conversion has been found between the two CRS, then this method returns {@code null}.
     *
     * @param sourceCRS The source coordinate reference system.
     * @param targetCRS The target coordinate reference system.
     * @param errorTolerance Relative error tolerance for considering two parameter values as equal. This is usually a
     *     small number like {@code 1E-10}.
     * @return The conversion from {@code sourceCRS} to {@code targetCRS} as an affine transform, or {@code null} if no
     *     linear transform has been found.
     */
    public static Matrix createLinearConversion(
            final ProjectedCRS sourceCRS, final ProjectedCRS targetCRS, final double errorTolerance) {
        /*
         * Checks if the datum are the same. To be stricter, we could compare the 'baseCRS'
         * instead. But this is not always needed. For example we don't really care if the
         * underlying geographic CRS use different axis order or units. What matter are the
         * axis order and units of the projected CRS.
         *
         * Actually, checking for 'baseCRS' causes an infinite loop (until StackOverflowError)
         * in CoordinateOperationFactory, because it prevents this method to recognize that the
         * transform between two projected CRS is the identity transform even if their underlying
         * geographic CRS use different axis order.
         */
        if (!CRS.equalsIgnoreMetadata(sourceCRS.getDatum(), targetCRS.getDatum())) {
            return null;
        }
        final ProjectionAnalyzer source = new ProjectionAnalyzer(sourceCRS);
        final ProjectionAnalyzer target = new ProjectionAnalyzer(targetCRS);
        if (!nameMatches(source.projection.getMethod(), target.projection.getMethod())) {
            /*
             * In theory, we can not find a linear conversion if the operation method is
             * not the same. In practice, it still hapen in some occasions.  For example
             * "Transverse Mercator" and "Transverse Mercator (South Oriented)"  are two
             * distinct operation methods in EPSG point of view, but in Geotools the South
             * Oriented case is implemented as a "Transverse Mercator" concatenated with
             * an affine transform performing the axis flip, which is a linear conversion.
             *
             * We may be tempted to compare the 'source.transform' and 'target.transform'
             * implementation classes, but this is not robust enough.  For example it is
             * possible to implement the "Oblique Mercator" and "Hotine Oblique Mercator"
             * projections with a single class. But both cases can have identical user-
             * supplied parameters and still be different projections (they differ in the
             * origin of their grid coordinates).
             *
             * As a compromise, we compare the method name declared by the math transform,
             * in addition of the method name declared by the conversion (the check above).
             */
            final ParameterDescriptorGroup sourceDsc = source.getTransformDescriptor();
            final ParameterDescriptorGroup targetDsc = source.getTransformDescriptor();
            if (sourceDsc == null || targetDsc == null || !nameMatches(sourceDsc, targetDsc)) {
                return null;
            }
        }
        /*
         * Extracts the "scale_factor", "false_easting" and "false_northing" parameters
         * as affine transforms. All remaining parameters must be identical.
         */
        if (source.parameters == null || target.parameters == null) {
            return null;
        }
        XMatrix sourceScale = source.normalizedToProjection();
        XMatrix targetScale = target.normalizedToProjection();
        if (!parameterValuesEqual(source.parameters, target.parameters, errorTolerance)) {
            return null;
        }
        /*
         * Creates the matrix (including axis order changes and unit conversions),
         * and apply the scale and translation inferred from the  "false_easting"
         * parameter and its friends. We perform the conversion in three conceptual
         * steps (in the end, everything is bundle in a single matrix):
         *
         *   1) remove the old false northing/easting
         *   2) apply the scales
         *   3) add the new false northing/easting
         */
        targetScale = target.applyProjectedScale(targetScale);
        sourceScale = source.applyProjectedScale(sourceScale);
        sourceScale.invert();
        targetScale.multiply(sourceScale);
        if (targetScale.isIdentity(errorTolerance)) {
            targetScale.setIdentity();
        }
        return targetScale;
    }
}
