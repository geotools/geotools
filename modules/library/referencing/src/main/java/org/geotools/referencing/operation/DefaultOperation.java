/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Map;

import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.referencing.operation.Operation;
import org.opengis.referencing.operation.Transformation;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.Projection;
import org.opengis.referencing.operation.PlanarProjection;
import org.opengis.referencing.operation.CylindricalProjection;
import org.opengis.referencing.operation.ConicProjection;
import org.opengis.referencing.operation.MathTransform;

import org.geotools.referencing.wkt.Formatter;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.operation.transform.AbstractMathTransform;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.PassThroughTransform;
import org.geotools.util.UnsupportedImplementationException;


/**
 * A parameterized mathematical operation on coordinates that transforms or converts
 * coordinates to another coordinate reference system. This coordinate operation thus
 * uses an operation method, usually with associated parameter values.
 * <P>
 * In the Geotools implementation, the {@linkplain #getParameterValues parameter values}
 * are inferred from the {@linkplain #transform transform}. Other implementations may have
 * to overrides the {@link #getParameterValues} method.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see DefaultOperationMethod
 */
public class DefaultOperation extends DefaultSingleOperation implements Operation {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -8923365753849532179L;

    /**
     * The operation method.
     */
    protected final OperationMethod method;

    /**
     * Constructs a new operation with the same values than the specified defining
     * conversion, together with the specified source and target CRS. This constructor
     * is used by {@link DefaultConversion} only.
     */
    DefaultOperation(final Conversion               definition,
                     final CoordinateReferenceSystem sourceCRS,
                     final CoordinateReferenceSystem targetCRS,
                     final MathTransform             transform)
    {
        super(definition, sourceCRS, targetCRS, transform);
        method = definition.getMethod();
    }

    /**
     * Constructs an operation from a set of properties. The properties given in argument
     * follow the same rules than for the {@link AbstractCoordinateOperation} constructor.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param sourceCRS The source CRS.
     * @param targetCRS The target CRS.
     * @param transform Transform from positions in the {@linkplain #getSourceCRS source CRS}
     *                  to positions in the {@linkplain #getTargetCRS target CRS}.
     * @param method    The operation method.
     */
    public DefaultOperation(final Map<String,?>            properties,
                            final CoordinateReferenceSystem sourceCRS,
                            final CoordinateReferenceSystem targetCRS,
                            final MathTransform             transform,
                            final OperationMethod           method)
    {
        super(properties, sourceCRS, targetCRS, transform);
        ensureNonNull("method", method);
        DefaultOperationMethod.checkDimensions(method, transform);
        this.method = method;
    }

    /**
     * Returns a coordinate operation of the specified class. This method may constructs instance of
     * {@link Conversion} or {@link Transformation} among others.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param sourceCRS The source CRS.
     * @param targetCRS The target CRS.
     * @param transform Transform from positions in the {@linkplain #getSourceCRS source CRS}
     *                  to positions in the {@linkplain #getTargetCRS target CRS}.
     * @param method    The operation method, or {@code null}.
     * @param type      The minimal type as <code>{@linkplain Conversion}.class</code>,
     *                  <code>{@linkplain Projection}.class</code>, etc. This method may
     *                  create an instance of a subclass of {@code type}.
     * @return A new coordinate operation of the given type.
     *
     * @see DefaultConversion#create
     */
    public static CoordinateOperation create(final Map<String,?>            properties,
                                             final CoordinateReferenceSystem sourceCRS,
                                             final CoordinateReferenceSystem targetCRS,
                                             final MathTransform             transform,
                                             final OperationMethod           method,
                                             Class<? extends CoordinateOperation> type)
    {
        if (method != null) {
            if (method instanceof MathTransformProvider) {
                final Class<? extends Operation> candidate =
                        ((MathTransformProvider) method).getOperationType();
                if (candidate != null) {
                    if (type==null || type.isAssignableFrom(candidate)) {
                        type = candidate.asSubclass(type);
                    }
                }
            }
            if (type != null) {
                if (Transformation.class.isAssignableFrom(type)) {
                    return new DefaultTransformation(
                               properties, sourceCRS, targetCRS, transform, method);
                }
                if (ConicProjection.class.isAssignableFrom(type)) {
                    return new DefaultConicProjection(
                               properties, sourceCRS, targetCRS, transform, method);
                }
                if (CylindricalProjection.class.isAssignableFrom(type)) {
                    return new DefaultCylindricalProjection(
                               properties, sourceCRS, targetCRS, transform, method);
                }
                if (PlanarProjection.class.isAssignableFrom(type)) {
                    return new DefaultPlanarProjection(
                               properties, sourceCRS, targetCRS, transform, method);
                }
                if (Projection.class.isAssignableFrom(type)) {
                    return new DefaultProjection(
                               properties, sourceCRS, targetCRS, transform, method);
                }
                if (Conversion.class.isAssignableFrom(type)) {
                    return new DefaultConversion(
                               properties, sourceCRS, targetCRS, transform, method);
                }
            }
            return new DefaultOperation(
                       properties, sourceCRS, targetCRS, transform, method);
        }
        return new DefaultSingleOperation(properties, sourceCRS, targetCRS, transform);
    }

    /**
     * Returns the operation method.
     */
    public OperationMethod getMethod() {
        return method;
    }

    /**
     * Returns the parameter values. The default implementation infer the parameter
     * values from the {@link #transform transform}, if possible.
     *
     * @throws UnsupportedOperationException if the parameters values can't be determined
     *         for current math transform implementation.
     *
     * @see DefaultMathTransformFactory#createParameterizedTransform
     * @see org.geotools.referencing.operation.transform.AbstractMathTransform#getParameterValues
     */
    public ParameterValueGroup getParameterValues() throws UnsupportedOperationException {
        return getParameterValues(transform, method.getParameters(), true);
    }

    /**
     * Returns the parameter values for the math transform that use the specified descriptor.
     *
     * @param  mt The math transform for which parameters are desired.
     * @param  descriptor The descriptor to search for.
     * @param  required {@code true} if an exception must be thrown if parameters are unknow.
     * @return The parameter values, or null.
     * @throws UnsupportedImplementationException if the math transform implementation do not
     *         provide information about parameters.
     */
    private static ParameterValueGroup getParameterValues(MathTransform mt,
            final ParameterDescriptorGroup descriptor, boolean required)
    {
        while (mt != null) {
            if (mt instanceof ConcatenatedTransform) {
                final ConcatenatedTransform ct = (ConcatenatedTransform) mt;
                final ParameterValueGroup param1 = getParameterValues(ct.transform1, descriptor, false);
                final ParameterValueGroup param2 = getParameterValues(ct.transform2, descriptor, false);
                if (param1 == null && param2 != null) return param2;
                if (param2 == null && param1 != null) return param1;
                required = true;
            }
            if (mt instanceof AbstractMathTransform) {
                final ParameterValueGroup param = ((AbstractMathTransform) mt).getParameterValues();
                if (param != null) {
                    return param;
                }
            }
            if (mt instanceof PassThroughTransform) {
                mt = ((PassThroughTransform) mt).getSubTransform();
            } else {
                break;
            }
        }
        if (required) {
            throw new UnsupportedImplementationException(mt.getClass());
        }
        return null;
    }

    /**
     * Compare this operation method with the specified object for equality.
     * If {@code compareMetadata} is {@code true}, then all available properties
     * are compared including {@linkplain DefaultOperationMethod#getFormula formula}.
     *
     * @param  object The object to compare to {@code this}.
     * @param  compareMetadata {@code true} for performing a strict comparaison, or
     *         {@code false} for comparing only properties relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
        if (super.equals(object, compareMetadata)) {
            final DefaultOperation that = (DefaultOperation) object;
            if (compareMetadata) {
                return equals(this.method, that.method, compareMetadata);
            }
            /*
             * We consider the operation method as metadata. We could argue that OperationMethod's
             * 'sourceDimensions' and 'targetDimensions' are not metadata, but their values should
             * be identical to the 'sourceCRS' and 'targetCRS' dimensions,  already checked by the
             * superclass. We could also argue that 'OperationMethod.parameters' are not metadata,
             * but their values should have been taken in account for the MathTransform creation,
             * which was compared by the superclass.
             *
             * Comparing the MathTransforms instead of parameters avoid the problem of implicit
             * parameters.  For example in a ProjectedCRS, the "semiMajor" and "semiMinor" axis
             * lengths are sometime provided as explicit parameters, and sometime inferred from
             * the geodetic datum.  The two cases would be different set of parameters from the
             * OperationMethod's point of view, but still result in the creation of identical
             * MathTransform.
             *
             * An other rational for treating OperationMethod as metadata is that Geotools
             * MathTransformProvider extends DefaultOperationMethod. Consequently there is
             * a wide range of subclasses, which make the comparaisons more difficult. For
             * example Mercator1SP.Provider and Mercator2SP.Provider are two different ways
             * to describe the same projection. The SQL-backed EPSG factory uses yet an
             * other implementation.
             *
             * As a safety, we still compare the name. But I'm not completly sure that it is
             * necessary.
             * 
             * AA: this comparison was removed to allow the common case of Conformal 1SP vs
             * conformal 2SP equivalence to succeed a equalsIgnoreMetadata comparison. Extensive tests
             * revealed no regressions, as it was noted above, there is no proof this is actually
             * necessary
             */
            // return nameMatches(this.method, that.method);
            return true;
        }
        return false;
    }

    /**
     * Returns a hash code value for this operation method.
     */
    @Override
    public int hashCode() {
        return super.hashCode() ^ method.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        final String name = super.formatWKT(formatter);
        append(formatter, method, "METHOD");
        return name;
    }
}
