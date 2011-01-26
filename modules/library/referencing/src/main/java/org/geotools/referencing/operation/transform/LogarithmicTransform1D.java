/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.transform;

import java.io.Serializable;
import javax.measure.unit.Unit;

import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform1D;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.FloatParameter;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.operation.MathTransformProvider;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.i18n.Vocabulary;


/**
 * A one dimensional, logarithmic transform.
 * Input values <var>x</var> are converted into
 * output values <var>y</var> using the following equation:
 *
 * <p align="center"><var>y</var> &nbsp;=&nbsp;
 * {@linkplain #offset} + log<sub>{@linkplain #base}</sub>(<var>x</var>)
 * &nbsp;&nbsp;=&nbsp;&nbsp;
 * {@linkplain #offset} + ln(<var>x</var>)/ln({@linkplain #base})</p>
 *
 * This transform is the inverse of {@link ExponentialTransform1D}.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see ExponentialTransform1D
 * @see LinearTransform1D
 */
public class LogarithmicTransform1D extends AbstractMathTransform
        implements MathTransform1D, Serializable
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 1535101265352133948L;

    /**
     * Tolerance value for floating point comparaison.
     */
    private static final double EPS = 1E-8;

    /**
     * The base of the logarithm.
     */
    public final double base;

    /**
     * Natural logarithm of {@link #base}.
     */
    final double lnBase;

    /**
     * The offset to add to the logarithm.
     */
    public final double offset;

    /**
     * The inverse of this transform. Created only when first needed.
     * Serialized in order to avoid rounding error if this transform
     * is actually the one which was created from the inverse.
     */
    private MathTransform1D inverse;

    /**
     * Constructs a new logarithmic transform which is the
     * inverse of the supplied exponentional transform.
     */
    private LogarithmicTransform1D(final ExponentialTransform1D inverse) {
        this.base    = inverse.base;
        this.lnBase  = inverse.lnBase;
        this.offset  = -Math.log(inverse.scale) / lnBase;
        this.inverse = inverse;
    }

    /**
     * Constructs a new logarithmic transform. This constructor is provided for subclasses only.
     * Instances should be created using the {@linkplain #create factory method}, which
     * may returns optimized implementations for some particular argument values.
     *
     * @param base    The base of the logarithm (typically 10).
     * @param offset  The offset to add to the logarithm.
     */
    protected LogarithmicTransform1D(final double base, final double offset) {
        this.base    = base;
        this.offset  = offset;
        this.lnBase  = Math.log(base);
    }

    /**
     * Constructs a new logarithmic transform which is the
     * inverse of the supplied exponentional transform.
     */
    static LogarithmicTransform1D create(final ExponentialTransform1D inverse) {
        if (Math.abs(inverse.base - 10) < EPS) {
            return new Base10(inverse);
        }
        return new LogarithmicTransform1D(inverse);
    }

    /**
     * Constructs a new logarithmic transform.
     *
     * @param base    The base of the logarithm (typically 10).
     * @param offset  The offset to add to the logarithm.
     * @return The math transform.
     */
    public static MathTransform1D create(final double base, final double offset) {
        if (base == Double.POSITIVE_INFINITY || Math.abs(base) <= EPS) {
            return LinearTransform1D.create(0, offset);
        }
        if (Math.abs(base - 10) < EPS) {
            return new Base10(offset);
        }
        return new LogarithmicTransform1D(base, offset);
    }

    /**
     * Returns the parameter descriptors for this math transform.
     */
    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    /**
     * Returns the parameter values for this math transform.
     *
     * @return A copy of the parameter values for this math transform.
     */
    @Override
    public ParameterValueGroup getParameterValues() {
        return new org.geotools.parameter.ParameterGroup(getParameterDescriptors(),
            new ParameterValue[] {
            new FloatParameter(Provider.BASE,   base),
            new FloatParameter(Provider.OFFSET, offset)});
    }

    /**
     * Gets the dimension of input points, which is 1.
     */
    public int getSourceDimensions() {
        return 1;
    }

    /**
     * Gets the dimension of output points, which is 1.
     */
    public int getTargetDimensions() {
        return 1;
    }

    /**
     * Creates the inverse transform of this object.
     */
    @Override
    public MathTransform1D inverse() {
        if (inverse == null) {
            inverse = new ExponentialTransform1D(this);
        }
        return inverse;
    }

    /**
     * Gets the derivative of this function at a value.
     */
    public double derivative(final double value) {
        return 1 / (lnBase * value);
    }

    /**
     * Transforms the specified value.
     */
    public double transform(final double value) {
        return Math.log(value)/lnBase + offset;
    }

    /**
     * Transforms a list of coordinate point ordinal values.
     */
    @Override
    public void transform(final float[] srcPts, int srcOff,
                          final float[] dstPts, int dstOff, int numPts)
    {
        if (srcPts!=dstPts || srcOff>=dstOff) {
            while (--numPts >= 0) {
                dstPts[dstOff++] = (float) (Math.log(srcPts[srcOff++])/lnBase + offset);
            }
        } else {
            srcOff += numPts;
            dstOff += numPts;
            while (--numPts >= 0) {
                dstPts[--dstOff] = (float) (Math.log(srcPts[srcOff++])/lnBase + offset);
            }
        }
    }

    /**
     * Transforms a list of coordinate point ordinal values.
     */
    public void transform(final double[] srcPts, int srcOff,
                          final double[] dstPts, int dstOff, int numPts)
    {
        if (srcPts!=dstPts || srcOff>=dstOff) {
            while (--numPts >= 0) {
                dstPts[dstOff++] = Math.log(srcPts[srcOff++])/lnBase + offset;
            }
        } else {
            srcOff += numPts;
            dstOff += numPts;
            while (--numPts >= 0) {
                dstPts[--dstOff] = Math.log(srcPts[srcOff++])/lnBase + offset;
            }
        }
    }

    /**
     * Special case for base 10 taking advantage of extra precision provided by {@link Math#log10}.
     */
    private static final class Base10 extends LogarithmicTransform1D {
        /** For cross-version compatibility. */
        private static final long serialVersionUID = -5435804027536647558L;

        /** Constructs the inverse of the supplied exponentional transform. */
        Base10(final ExponentialTransform1D inverse) {
            super(inverse);
        }

        /** Creates a new instance with the given offset. */
        protected Base10(final double offset) {
            super(10, offset);
        }

        /** {@inheritDoc} */
        @Override
        public double transform(final double value) {
            return Math.log10(value) + offset;
        }

        /** {@inheritDoc} */
        @Override
        public void transform(final float[] srcPts, int srcOff,
                              final float[] dstPts, int dstOff, int numPts)
        {
            if (srcPts!=dstPts || srcOff>=dstOff) {
                while (--numPts >= 0) {
                    dstPts[dstOff++] = (float) (Math.log10(srcPts[srcOff++]) + offset);
                }
            } else {
                srcOff += numPts;
                dstOff += numPts;
                while (--numPts >= 0) {
                    dstPts[--dstOff] = (float) (Math.log10(srcPts[srcOff++]) + offset);
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public void transform(final double[] srcPts, int srcOff,
                              final double[] dstPts, int dstOff, int numPts)
        {
            if (srcPts!=dstPts || srcOff>=dstOff) {
                while (--numPts >= 0) {
                    dstPts[dstOff++] = Math.log10(srcPts[srcOff++]) + offset;
                }
            } else {
                srcOff += numPts;
                dstOff += numPts;
                while (--numPts >= 0) {
                    dstPts[--dstOff] = Math.log10(srcPts[srcOff++]) + offset;
                }
            }
        }
    }

    /**
     * Concatenates in an optimized way a {@link MathTransform} {@code other} to this
     * {@code MathTransform}. This implementation can optimize some concatenation with
     * {@link LinearTransform1D} and {@link ExponentialTransform1D}.
     *
     * @param  other The math transform to apply.
     * @param  applyOtherFirst {@code true} if the transformation order is {@code other}
     *         followed by {@code this}, or {@code false} if the transformation order is
     *         {@code this} followed by {@code other}.
     * @return The combined math transform, or {@code null} if no optimized combined
     *         transform is available.
     */
    @Override
    MathTransform concatenate(final MathTransform other, final boolean applyOtherFirst) {
        if (other instanceof LinearTransform) {
            final LinearTransform1D linear = (LinearTransform1D) other;
            if (applyOtherFirst) {
                if (linear.offset==0 && linear.scale>0) {
                    return create(base, Math.log(linear.scale)/lnBase+offset);
                }
            } else {
                final double newBase = Math.pow(base, 1/linear.scale);
                if (!Double.isNaN(newBase)) {
                    return create(newBase, linear.scale*offset + linear.offset);
                }
            }
        } else if (other instanceof ExponentialTransform1D) {
            return ((ExponentialTransform1D) other).concatenateLog(this, !applyOtherFirst);
        }
        return super.concatenate(other, applyOtherFirst);
    }

    /**
     * Returns a hash value for this transform.
     * This value need not remain consistent between
     * different implementations of the same class.
     */
    @Override
    public int hashCode() {
        long code;
        code = serialVersionUID + Double.doubleToLongBits(base);
        code =          code*37 + Double.doubleToLongBits(offset);
        return (int)(code >>> 32) ^ (int)code;
    }

    /**
     * Compares the specified object with this math transform for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            final LogarithmicTransform1D that = (LogarithmicTransform1D) object;
            return Double.doubleToLongBits(this.base)   == Double.doubleToLongBits(that.base) &&
                   Double.doubleToLongBits(this.offset) == Double.doubleToLongBits(that.offset);
        }
        return false;
    }

    /**
     * The provider for the {@link LogarithmicTransform1D}.
     *
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    public static class Provider extends MathTransformProvider {
        /**
         * Serial number for interoperability with different versions.
         */
        private static final long serialVersionUID = -7235097164208708484L;

        /**
         * The operation parameter descriptor for the {@link #base base} parameter value.
         * Valid values range from 0 to infinity. The default value is 10.
         */
        public static final ParameterDescriptor<Double> BASE = DefaultParameterDescriptor.create(
                "base", 10, 0, Double.POSITIVE_INFINITY, Unit.ONE);

        /**
         * The operation parameter descriptor for the {@link #offset offset} parameter value.
         * Valid values range is unrestricted. The default value is 0.
         */
        public static final ParameterDescriptor<Double> OFFSET =DefaultParameterDescriptor.create(
                "offset", 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Unit.ONE);

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.GEOTOOLS, Vocabulary.formatInternational(
                                                        VocabularyKeys.LOGARITHMIC))
            }, new ParameterDescriptor[] {
                BASE, OFFSET
            });

        /**
         * Create a provider for logarithmic transforms.
         */
        public Provider() {
            super(1, 1, PARAMETERS);
        }

        /**
         * Returns the operation type.
         */
        @Override
        public Class<Conversion> getOperationType() {
            return Conversion.class;
        }

        /**
         * Creates a logarithmic transform from the specified group of parameter values.
         *
         * @param  values The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        protected MathTransform1D createMathTransform(final ParameterValueGroup values)
                throws ParameterNotFoundException
        {
            return create(doubleValue(BASE,   values),
                          doubleValue(OFFSET, values));
        }
    }
}
