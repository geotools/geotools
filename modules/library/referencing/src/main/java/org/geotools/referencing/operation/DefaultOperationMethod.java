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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.Operation;
import org.opengis.referencing.operation.Projection;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.util.InternationalString;

import org.geotools.parameter.Parameters;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.operation.transform.AbstractMathTransform;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.PassThroughTransform;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.util.Utilities;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;


/**
 * Definition of an algorithm used to perform a coordinate operation. Most operation
 * methods use a number of operation parameters, although some coordinate conversions
 * use none. Each coordinate operation using the method assigns values to these parameters.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see DefaultOperation
 */
public class DefaultOperationMethod extends AbstractIdentifiedObject implements OperationMethod {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -98032729598205972L;

    /**
     * List of localizable properties. To be given to {@link AbstractIdentifiedObject} constructor.
     */
    private static final String[] LOCALIZABLES = {FORMULA_KEY};

    /**
     * Formula(s) or procedure used by this operation method. This may be a reference to a
     * publication. Note that the operation method may not be analytic, in which case this
     * attribute references or contains the procedure, not an analytic formula.
     */
    private final InternationalString formula;

    /**
     * Number of dimensions in the source CRS of this operation method.
     */
    protected final int sourceDimensions;

    /**
     * Number of dimensions in the target CRS of this operation method.
     */
    protected final int targetDimensions;

    /**
     * The set of parameters, or {@code null} if none.
     */
    private final ParameterDescriptorGroup parameters;

    /**
     * Convenience constructor that creates an operation method from a math transform.
     * The information provided in the newly created object are approximative, and
     * usually acceptable only as a fallback when no other information are available.
     *
     * @param transform The math transform to describe.
     */
    public DefaultOperationMethod(final MathTransform transform) {
        this(getProperties(transform),
             transform.getSourceDimensions(),
             transform.getTargetDimensions(),
             getDescriptor(transform));
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database
     * ("Relax constraint on placement of this()/super() call in constructors").
     */
    private static Map<String,?> getProperties(final MathTransform transform) {
        ensureNonNull("transform", transform);
        final Map<String,?> properties;
        if (transform instanceof AbstractMathTransform) {
            final AbstractMathTransform mt = (AbstractMathTransform) transform;
            properties = getProperties(mt.getParameterDescriptors(), null);
        } else {
            properties = Collections.singletonMap(NAME_KEY, Vocabulary.format(VocabularyKeys.UNKNOW));
        }
        return properties;
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database
     * ("Relax constraint on placement of this()/super() call in constructors").
     * This code should have been merged with {@code getProperties} above.
     */
    private static ParameterDescriptorGroup getDescriptor(final MathTransform transform) {
        ParameterDescriptorGroup descriptor = null;
        if (transform instanceof AbstractMathTransform) {
            descriptor = ((AbstractMathTransform) transform).getParameterDescriptors();
        }
        return descriptor;
    }

    /**
     * Constructs a new operation method with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @param method The operation method to copy.
     */
    public DefaultOperationMethod(final OperationMethod method) {
        super(method);
        formula          = method.getFormula();
        parameters       = method.getParameters();
        sourceDimensions = method.getSourceDimensions();
        targetDimensions = method.getTargetDimensions();
    }

    /**
     * Constructs a new operation method with the same values than the specified one
     * except the dimensions.
     *
     * @param method The operation method to copy.
     * @param sourceDimensions Number of dimensions in the source CRS of this operation method.
     * @param targetDimensions Number of dimensions in the target CRS of this operation method.
     */
    public DefaultOperationMethod(final OperationMethod method,
                                  final int sourceDimensions,
                                  final int targetDimensions)
    {
        super(method);
        this.formula    = method.getFormula();
        this.parameters = method.getParameters();
        this.sourceDimensions = sourceDimensions;
        this.targetDimensions = targetDimensions;
        ensurePositive("sourceDimensions", sourceDimensions);
        ensurePositive("targetDimensions", targetDimensions);
    }

    /**
     * Constructs an operation method from a set of properties and a descriptor group.
     * The properties given in argument follow the same rules than for the
     * {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map) super-class constructor}.
     * Additionally, the following properties are understood by this construtor:
     * <br><br>
     * <table border='1'>
     *   <tr bgcolor="#CCCCFF" class="TableHeadingColor">
     *     <th nowrap>Property name</th>
     *     <th nowrap>Value type</th>
     *     <th nowrap>Value given to</th>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@link #FORMULA_KEY "formula"}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link String} or {@link InternationalString}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getFormula}</td>
     *   </tr>
     * </table>
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param sourceDimensions Number of dimensions in the source CRS of this operation method.
     * @param targetDimensions Number of dimensions in the target CRS of this operation method.
     * @param parameters The set of parameters, or {@code null} if none.
     */
    public DefaultOperationMethod(final Map<String,?> properties,
                                  final int sourceDimensions,
                                  final int targetDimensions,
                                  final ParameterDescriptorGroup parameters)
    {
        this(properties, new HashMap<String,Object>(), sourceDimensions, targetDimensions, parameters);
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database
     * ("Relax constraint on placement of this()/super() call in constructors").
     */
    private DefaultOperationMethod(final Map<String,?> properties,
                                   final Map<String,Object> subProperties,
                                   final int sourceDimensions,
                                   final int targetDimensions,
                                   ParameterDescriptorGroup parameters)
    {
        super(properties, subProperties, LOCALIZABLES);
        formula = (InternationalString) subProperties.get(FORMULA_KEY);
        // 'parameters' may be null, which is okay. A null value will
        // make serialization smaller and faster than an empty object.
        this.parameters       = parameters;
        this.sourceDimensions = sourceDimensions;
        this.targetDimensions = targetDimensions;
        ensurePositive("sourceDimensions", sourceDimensions);
        ensurePositive("targetDimensions", targetDimensions);
    }

    /**
     * Ensure that the specified value is positive.
     * An {@link IllegalArgumentException} is throws if it is not.
     *
     * @param name  The parameter name.
     * @param value The parameter value.
     * @throws IllegalArgumentException if the specified value is not positive.
     */
    private static void ensurePositive(final String name, final int value)
            throws IllegalArgumentException
    {
        if (value < 0) {
            throw new IllegalArgumentException(Errors.format(
                      ErrorKeys.ILLEGAL_ARGUMENT_$2, name, value));
        }
    }

    /**
     * Formula(s) or procedure used by this operation method. This may be a reference to a
     * publication. Note that the operation method may not be analytic, in which case this
     * attribute references or contains the procedure, not an analytic formula.
     */
    public InternationalString getFormula() {
        return formula;
    }

    /**
     * Number of dimensions in the source CRS of this operation method.
     *
     * @return The dimension of source CRS.
     */
    public int getSourceDimensions() {
        return sourceDimensions;
    }

    /**
     * Number of dimensions in the target CRS of this operation method.
     */
    public int getTargetDimensions() {
        return targetDimensions;
    }

    /**
     * Returns the set of parameters.
     */
    public ParameterDescriptorGroup getParameters() {
        return (parameters!=null) ? parameters : Parameters.EMPTY_GROUP;
    }

    /**
     * Returns the operation type. Current implementation returns {@code Projection.class} for
     * proper WKT formatting using an unknow implementation. But the {@link MathTransformProvider}
     * subclass (with protected access) will overrides this method with a more conservative default
     * value.
     *
     * @return The GeoAPI interface implemented by this operation.
     */
    Class<? extends Operation> getOperationType() {
        return Projection.class;
    }

    /**
     * Compare this operation method with the specified object for equality.
     * If {@code compareMetadata} is {@code true}, then all available
     * properties are compared including {@linkplain #getFormula formula}.
     *
     * @param  object The object to compare to {@code this}.
     * @param  compareMetadata {@code true} for performing a strict comparaison, or
     *         {@code false} for comparing only properties relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
        if (object == this) {
            return true; // Slight optimization.
        }
        if (super.equals(object, compareMetadata)) {
            final DefaultOperationMethod that = (DefaultOperationMethod) object;
            if (this.sourceDimensions == that.sourceDimensions &&
                this.targetDimensions == that.targetDimensions &&
                equals(this.parameters,  that.parameters, compareMetadata))
            {
                return !compareMetadata || Utilities.equals(this.formula, that.formula);
            }
        }
        return false;
    }

    /**
     * Returns a hash code value for this operation method.
     */
    @Override
    public int hashCode() {
        int code = (int)serialVersionUID + sourceDimensions + 37*targetDimensions;
        if (parameters != null) {
            code = code * 37 + parameters.hashCode();
        }
        return code;
    }

    /**
     * Format the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> element.
     *
     * @param  formatter The formatter to use.
     * @return The WKT element name.
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        if (Projection.class.isAssignableFrom(getOperationType())) {
            return "PROJECTION";
        }
        return super.formatWKT(formatter);
    }

    /**
     * Returns {@code true} if the specified transform is likely to exists only for axis switch
     * and/or unit conversions. The heuristic rule checks if the transform is backed by a square
     * matrix with exactly one non-null value in each row and each column. This method is used
     * for implementation of the {@link #checkDimensions} method only.
     */
    private static boolean isTrivial(final MathTransform transform) {
        if (transform instanceof LinearTransform) {
            final Matrix matrix = ((LinearTransform) transform).getMatrix();
            final int size = matrix.getNumRow();
            if (matrix.getNumCol() == size) {
                for (int j=0; j<size; j++) {
                    int n1=0, n2=0;
                    for (int i=0; i<size; i++) {
                        if (matrix.getElement(j,i) != 0) n1++;
                        if (matrix.getElement(i,j) != 0) n2++;
                    }
                    if (n1 != 1 || n2 != 1) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if an operation method and a math transform have a compatible number of source
     * and target dimensions. In the particular case of a {@linkplain PassThroughTransform pass
     * through transform} with more dimension than the expected number, the check will rather be
     * performed against the {@linkplain PassThroughTransform#getSubTransform sub transform}.
     * <p>
     * This convenience method is provided for argument checking.
     *
     * @param  method    The operation method to compare to the math transform, or {@code null}.
     * @param  transform The math transform to compare to the operation method, or {@code null}.
     * @throws MismatchedDimensionException if the number of dimensions are incompatibles.
     *
     * @todo The check for {@link ConcatenatedTransform} and {@link PassThroughTransform} works
     *       only for Geotools implementation.
     */
    public static void checkDimensions(final OperationMethod method, MathTransform transform)
            throws MismatchedDimensionException
    {
        if (method!=null && transform!=null) {
            int actual, expected=method.getSourceDimensions();
            while ((actual=transform.getSourceDimensions()) > expected) {
                if (transform instanceof ConcatenatedTransform) {
                    // Ignore axis switch and unit conversions.
                    final ConcatenatedTransform c = (ConcatenatedTransform) transform;
                    if (isTrivial(c.transform1)) {
                        transform = c.transform2;
                    } else if (isTrivial(c.transform2)) {
                        transform = c.transform1;
                    } else {
                        // The transform is something more complex than an axis switch.
                        // Stop the loop with the current illegal transform and let the
                        // exception be thrown after the loop.
                        break;
                    }
                } else if (transform instanceof PassThroughTransform) {
                    transform = ((PassThroughTransform) transform).getSubTransform();
                } else {
                    break;
                }
            }
            final String name;
            if (actual != expected) {
                name = "sourceDimensions";
            } else {
                actual   = transform.getTargetDimensions();
                expected =    method.getTargetDimensions();
                if (actual != expected) {
                    name = "targetDimensions";
                } else {
                    return;
                }
            }
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.MISMATCHED_DIMENSION_$3, name, actual, expected));
        }
    }
}
