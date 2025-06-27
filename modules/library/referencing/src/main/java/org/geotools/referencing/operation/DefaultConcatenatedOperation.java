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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.api.metadata.quality.PositionalAccuracy;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.ConcatenatedOperation;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransformFactory;
import org.geotools.api.referencing.operation.SingleOperation;
import org.geotools.api.referencing.operation.Transformation;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.util.Classes;
import org.geotools.util.UnmodifiableArrayList;

/**
 * An ordered sequence of two or more single coordinate operations. The sequence of operations is constrained by the
 * requirement that the source coordinate reference system of step (<var>n</var>+1) must be the same as the target
 * coordinate reference system of step (<var>n</var>). The source coordinate reference system of the first step and the
 * target coordinate reference system of the last step are the source and target coordinate reference system associated
 * with the concatenated operation.
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class DefaultConcatenatedOperation extends AbstractCoordinateOperation implements ConcatenatedOperation {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 4199619838029045700L;

    /** The sequence of operations. */
    private final List<SingleOperation> operations;

    /**
     * Constructs a concatenated operation from the specified name.
     *
     * @param name The operation name.
     * @param operations The sequence of operations.
     */
    public DefaultConcatenatedOperation(final String name, final CoordinateOperation... operations) {
        this(Collections.singletonMap(NAME_KEY, name), operations);
    }

    /**
     * Constructs a concatenated operation from a set of properties. The properties given in argument follow the same
     * rules than for the {@link AbstractCoordinateOperation} constructor.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param operations The sequence of operations.
     */
    public DefaultConcatenatedOperation(final Map<String, ?> properties, final CoordinateOperation... operations) {
        this(properties, new ArrayList<>(operations.length), operations);
    }

    /**
     * Constructs a concatenated operation from a set of properties and a {@linkplain MathTransformFactory math
     * transform factory}. The properties given in argument follow the same rules than for the
     * {@link AbstractCoordinateOperation} constructor.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param operations The sequence of operations.
     * @param factory The math transform factory to use for math transforms concatenation.
     * @throws FactoryException if the factory can't concatenate the math transforms.
     */
    public DefaultConcatenatedOperation(
            final Map<String, ?> properties, final CoordinateOperation[] operations, final MathTransformFactory factory)
            throws FactoryException {
        this(properties, new ArrayList<>(operations.length), operations, factory);
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database ("Relax constraint on placement of this()/super() call in
     * constructors").
     */
    private DefaultConcatenatedOperation(
            final Map<String, ?> properties,
            final List<SingleOperation> list,
            final CoordinateOperation... operations) {
        this(properties, expand(operations, list), list);
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database ("Relax constraint on placement of this()/super() call in
     * constructors").
     */
    private DefaultConcatenatedOperation(
            final Map<String, ?> properties,
            final List<SingleOperation> list,
            final CoordinateOperation[] operations,
            final MathTransformFactory factory)
            throws FactoryException {
        this(properties, expand(operations, list, factory, true), list);
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database ("Relax constraint on placement of this()/super() call in
     * constructors").
     */
    private DefaultConcatenatedOperation(
            final Map<String, ?> properties, final MathTransform transform, final List<SingleOperation> operations) {
        super(
                mergeAccuracy(properties, operations),
                operations.get(0).getSourceCRS(),
                operations.get(operations.size() - 1).getTargetCRS(),
                transform);
        this.operations = UnmodifiableArrayList.wrap(operations.toArray(new SingleOperation[operations.size()]));
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database ("Relax constraint on placement of this()/super() call in
     * constructors").
     */
    private static MathTransform expand(final CoordinateOperation[] operations, final List<SingleOperation> list) {
        try {
            return expand(operations, list, null, true);
        } catch (FactoryException exception) {
            // Should not happen, since we didn't used any MathTransformFactory.
            throw new AssertionError(exception);
        }
    }

    /**
     * Transforms the list of operations into a list of single operations. This method also check against null value and
     * make sure that all CRS dimension matches.
     *
     * @param operations The array of operations to expand.
     * @param target The destination list in which to add {@code SingleOperation}.
     * @param factory The math transform factory to use, or {@code null}
     * @param wantTransform {@code true} if the concatenated math transform should be computed. This is set to
     *     {@code false} only when this method invokes itself recursively.
     * @return The concatenated math transform.
     * @throws FactoryException if the factory can't concatenate the math transforms.
     */
    private static MathTransform expand(
            final CoordinateOperation[] operations,
            final List<SingleOperation> target,
            final MathTransformFactory factory,
            final boolean wantTransform)
            throws FactoryException {
        MathTransform transform = null;
        ensureNonNull("operations", operations);
        for (int i = 0; i < operations.length; i++) {
            ensureNonNull("operations", operations, i);
            final CoordinateOperation op = operations[i];
            if (op instanceof SingleOperation) {
                target.add((SingleOperation) op);
            } else if (op instanceof ConcatenatedOperation) {
                final ConcatenatedOperation cop = (ConcatenatedOperation) op;
                final List<SingleOperation> cops = cop.getOperations();
                expand(cops.toArray(new CoordinateOperation[cops.size()]), target, factory, false);
            } else {
                throw new IllegalArgumentException(
                        MessageFormat.format(ErrorKeys.ILLEGAL_CLASS_$2, Classes.getClass(op), SingleOperation.class));
            }
            /*
             * Check the CRS dimensions.
             */
            if (i != 0) {
                final CoordinateReferenceSystem previous = operations[i - 1].getTargetCRS();
                final CoordinateReferenceSystem next = op.getSourceCRS();
                if (previous != null && next != null) {
                    final int dim1 = previous.getCoordinateSystem().getDimension();
                    final int dim2 = next.getCoordinateSystem().getDimension();
                    if (dim1 != dim2) {
                        throw new IllegalArgumentException(
                                MessageFormat.format(ErrorKeys.MISMATCHED_DIMENSION_$2, dim1, dim2));
                    }
                }
            }
            /*
             * Concatenates the math transform.
             */
            if (wantTransform) {
                final MathTransform step = op.getMathTransform();
                if (transform == null) {
                    transform = step;
                } else if (factory != null) {
                    transform = factory.createConcatenatedTransform(transform, step);
                } else {
                    transform = ConcatenatedTransform.create(transform, step);
                }
            }
        }
        if (wantTransform) {
            final int size = target.size();
            if (size <= 1) {
                throw new IllegalArgumentException(
                        MessageFormat.format(ErrorKeys.MISSING_PARAMETER_$1, "operations[" + size + ']'));
            }
        }
        return transform;
    }

    /**
     * If no accuracy were specified in the given properties map, add all accuracies found in the operation to
     * concatenate. This method considers only {@link Transformation} components and ignores all conversions. According
     * ISO 19111, the accuracy attribute is allowed only for transformations. However, this restriction is not enforced
     * everywhere. The EPSG database declares an accuracy of 0 meters for conversions, which is conceptually exact.
     * Ourself we are departing from the specification, since we are adding accuracy informations to a concatenated
     * operation. This departure should be considered as a convenience feature only; accuracies are really relevant in
     * transformations only.
     *
     * <p>There is also a technical reasons for ignoring conversions. If a concatenated operation contains a datum shift
     * (i.e. a transformation) with unknow accuracy, and a projection (i.e. a conversion) with a declared 0 meter error,
     * we don't want to declare this 0 meter error as the concatenated operation's accuracy; it would be a false
     * information.
     *
     * <p>Note that a concatenated operation typically contains an arbitrary amount of conversions, but only one
     * transformation. So considering transformation only usually means to pickup only one operation in the given
     * {@code operations} list.
     *
     * @todo We should use a Map and merge only one accuracy for each specification.
     */
    private static Map<String, ?> mergeAccuracy(
            final Map<String, ?> properties, final List<? extends CoordinateOperation> operations) {
        if (!properties.containsKey(COORDINATE_OPERATION_ACCURACY_KEY)) {
            Set<PositionalAccuracy> accuracy = null;
            for (final CoordinateOperation op : operations) {
                if (op instanceof Transformation) {
                    // See javadoc for a rational why we take only transformations in account.
                    Collection<PositionalAccuracy> candidates = op.getCoordinateOperationAccuracy();
                    if (candidates != null && !candidates.isEmpty()) {
                        if (accuracy == null) {
                            accuracy = new LinkedHashSet<>();
                        }
                        accuracy.addAll(candidates);
                    }
                }
            }
            if (accuracy != null) {
                final Map<String, Object> merged = new HashMap<>(properties);
                merged.put(
                        COORDINATE_OPERATION_ACCURACY_KEY, accuracy.toArray(new PositionalAccuracy[accuracy.size()]));
                return merged;
            }
        }
        return properties;
    }

    /** Returns the sequence of operations. */
    @Override
    public List<SingleOperation> getOperations() {
        return operations;
    }

    /**
     * Compare this concatenated operation with the specified object for equality. If {@code compareMetadata} is
     * {@code true}, then all available properties are compared including {@linkplain #getDomainOfValidity() valid area}
     * and {@linkplain #getScope scope}.
     *
     * @param object The object to compare to {@code this}.
     * @param compareMetadata {@code true} for performing a strict comparaison, or {@code false} for comparing only
     *     properties relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
        if (object == this) {
            return true; // Slight optimization.
        }
        if (super.equals(object, compareMetadata)) {
            final DefaultConcatenatedOperation that = (DefaultConcatenatedOperation) object;
            return equals(this.operations, that.operations, compareMetadata);
        }
        return false;
    }

    /** Returns a hash code value for this concatenated operation. */
    @Override
    @SuppressWarnings("PMD.OverrideBothEqualsAndHashcode")
    public int hashCode() {
        return operations.hashCode() ^ (int) serialVersionUID;
    }

    /** {@inheritDoc} */
    @Override
    protected String formatWKT(final Formatter formatter) {
        final String label = super.formatWKT(formatter);
        for (SingleOperation operation : operations) {
            formatter.append((CoordinateOperation) operation);
        }
        return label;
    }
}
