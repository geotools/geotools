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

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collections;
import java.awt.RenderingHints;
import javax.measure.converter.ConversionException;

import org.opengis.metadata.quality.PositionalAccuracy;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.*;
import static org.opengis.referencing.IdentifiedObject.NAME_KEY;

import org.geotools.factory.Hints;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.metadata.iso.quality.PositionalAccuracyImpl;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.factory.ReferencingFactory;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.referencing.cs.AbstractCS;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.util.CanonicalSet;
import org.geotools.util.Utilities;

import static org.geotools.referencing.CRS.equalsIgnoreMetadata;


/**
 * Base class for coordinate operation factories. This class provides helper methods for the
 * construction of building blocks. It doesn't figure out any operation path by itself. This
 * more "intelligent" job is left to subclasses.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class AbstractCoordinateOperationFactory extends ReferencingFactory
        implements CoordinateOperationFactory
{
    /**
     * The identifier for an identity operation.
     */
    protected static final ReferenceIdentifier IDENTITY =
            new NamedIdentifier(Citations.GEOTOOLS,
                Vocabulary.formatInternational(VocabularyKeys.IDENTITY));

    /**
     * The identifier for conversion using an affine transform for axis swapping and/or
     * unit conversions.
     */
    protected static final ReferenceIdentifier AXIS_CHANGES =
            new NamedIdentifier(Citations.GEOTOOLS,
                Vocabulary.formatInternational(VocabularyKeys.AXIS_CHANGES));

    /**
     * The identifier for a transformation which is a datum shift.
     *
     * @see PositionalAccuracyImpl#DATUM_SHIFT_APPLIED
     */
    protected static final ReferenceIdentifier DATUM_SHIFT =
            new NamedIdentifier(Citations.GEOTOOLS,
                Vocabulary.formatInternational(VocabularyKeys.DATUM_SHIFT));

    /**
     * The identifier for a transformation which is a datum shift without
     * {@linkplain org.geotools.referencing.datum.BursaWolfParameters Bursa Wolf parameters}.
     * Only the changes in ellipsoid axis-length are taken in account. Such ellipsoid shifts
     * are approximative and may have 1 kilometer error. This transformation is allowed
     * only if the factory was created with {@link Hints#LENIENT_DATUM_SHIFT} set to
     * {@link Boolean#TRUE}.
     *
     * @see PositionalAccuracyImpl#DATUM_SHIFT_OMITTED
     */
    protected static final ReferenceIdentifier ELLIPSOID_SHIFT =
            new NamedIdentifier(Citations.GEOTOOLS,
                Vocabulary.formatInternational(VocabularyKeys.ELLIPSOID_SHIFT));

    /**
     * The identifier for a geocentric conversion.
     */
    protected static final ReferenceIdentifier GEOCENTRIC_CONVERSION =
            new NamedIdentifier(Citations.GEOTOOLS,
                Vocabulary.formatInternational(VocabularyKeys.GEOCENTRIC_TRANSFORM));

    /**
     * The identifier for an inverse operation.
     */
    protected static final ReferenceIdentifier INVERSE_OPERATION =
            new NamedIdentifier(Citations.GEOTOOLS,
                Vocabulary.formatInternational(VocabularyKeys.INVERSE_OPERATION));

    /**
     * The set of helper methods on factories.
     *
     * @see #getFactoryGroup
     */
    private final ReferencingFactoryContainer factories;

    /**
     * The underlying math transform factory. This factory is used
     * for constructing {@link MathTransform} objects for all
     * {@linkplain CoordinateOperation coordinate operations}.
     *
     * @see #getMathTransformFactory
     */
    private final MathTransformFactory mtFactory;

    /**
     * A pool of coordinate operation. This pool is used in order
     * to returns instance of existing operations when possible.
     */
    private final CanonicalSet<CoordinateOperation> pool =
            CanonicalSet.newInstance(CoordinateOperation.class);

    /**
     * Tells if {@link FactoryGroup#hints} has been invoked. It must be invoked exactly once,
     * but can't be invoked in the constructor because it causes a {@link StackOverflowError}
     * in some situations.
     */
    private boolean hintsInitialized;

    /**
     * Constructs a coordinate operation factory using the specified hints.
     * This constructor recognizes the {@link Hints#CRS_FACTORY CRS}, {@link Hints#CS_FACTORY CS},
     * {@link Hints#DATUM_FACTORY DATUM} and {@link Hints#MATH_TRANSFORM_FACTORY MATH_TRANSFORM}
     * {@code FACTORY} hints.
     *
     * @param userHints The hints, or {@code null} if none.
     */
    public AbstractCoordinateOperationFactory(final Hints userHints) {
        this(userHints, NORMAL_PRIORITY);
    }

    /**
     * Constructs a coordinate operation factory using the specified hints and priority.
     * This constructor recognizes the {@link Hints#CRS_FACTORY CRS}, {@link Hints#CS_FACTORY CS},
     * {@link Hints#DATUM_FACTORY DATUM} and {@link Hints#MATH_TRANSFORM_FACTORY MATH_TRANSFORM}
     * {@code FACTORY} hints.
     *
     * @param userHints The hints, or {@code null} if none.
     * @param priority The priority for this factory, as a number between
     *        {@link #MINIMUM_PRIORITY MINIMUM_PRIORITY} and
     *        {@link #MAXIMUM_PRIORITY MAXIMUM_PRIORITY} inclusive.
     *
     * @since 2.2
     */
    public AbstractCoordinateOperationFactory(final Hints userHints, final int priority) {
        super(priority);
        factories = ReferencingFactoryContainer.instance(userHints);
        mtFactory = factories.getMathTransformFactory();
    }

    /**
     * If the specified factory is an instance of {@code AbstractCoordinateOperationFactory},
     * fetch the {@link FactoryGroup} from this instance instead of from the hints. This
     * constructor is strictly reserved for factory subclasses that are wrapper around an
     * other factory, like {@link BufferedCoordinateOperationFactory}.
     */
    AbstractCoordinateOperationFactory(final CoordinateOperationFactory factory,
                                       final Hints hints, final int priority)
    {
        super(priority);
        if (factory instanceof AbstractCoordinateOperationFactory) {
            factories = ((AbstractCoordinateOperationFactory) factory).getFactoryContainer();
        } else {
            factories = ReferencingFactoryContainer.instance(hints);
        }
        mtFactory = factories.getMathTransformFactory();
    }

    /**
     * Returns the implementation hints for this factory. The returned map contains values for
     * {@link Hints#CRS_FACTORY CRS}, {@link Hints#CS_FACTORY CS}, {@link Hints#DATUM_FACTORY DATUM}
     * and {@link Hints#MATH_TRANSFORM_FACTORY MATH_TRANSFORM} {@code FACTORY} hints. Other values
     * may be provided as well, at implementation choice.
     */
    @Override
    public Map<RenderingHints.Key,?> getImplementationHints() {
        synchronized (hints) { // Note: avoid lock on public object.
            if (!hintsInitialized) {
                initializeHints();
                hintsInitialized = true; // Set only after success.
            }
        }
        return super.getImplementationHints();
    }

    /**
     * Invoked when the {@link #hints} map should be initialized. This method may
     * be overridden by subclasses like {@link BufferedCoordinateOperationFactory}.
     */
    void initializeHints() {
        assert Thread.holdsLock(hints);
        final ReferencingFactoryContainer factories = getFactoryContainer();
        hints.putAll(factories.getImplementationHints());
    }

    /**
     * Returns the underlying math transform factory. This factory
     * is used for constructing {@link MathTransform} objects for
     * all {@linkplain CoordinateOperation coordinate operations}.
     *
     * @return The underlying math transform factory.
     */
    public final MathTransformFactory getMathTransformFactory() {
        return mtFactory;
    }

    /**
     * Returns the set of helper methods on factories.
     */
    final ReferencingFactoryContainer getFactoryContainer() {
        return factories;
    }

    /**
     * Returns an affine transform between two coordinate systems. Only units and
     * axis order (e.g. transforming from (NORTH,WEST) to (EAST,NORTH)) are taken
     * in account.
     * <p>
     * Example: If coordinates in {@code sourceCS} are (x,y) pairs in metres and
     * coordinates in {@code targetCS} are (-y,x) pairs in centimetres, then the
     * transformation can be performed as below:
     *
     * <pre><blockquote>
     *          [-y(cm)]   [ 0  -100    0 ] [x(m)]
     *          [ x(cm)] = [ 100   0    0 ] [y(m)]
     *          [ 1    ]   [ 0     0    1 ] [1   ]
     * </blockquote></pre>
     *
     * @param  sourceCS The source coordinate system.
     * @param  targetCS The target coordinate system.
     * @return The transformation from {@code sourceCS} to {@code targetCS} as
     *         an affine transform. Only axis orientation and units are taken in account.
     * @throws OperationNotFoundException If the affine transform can't be constructed.
     *
     * @see AbstractCS#swapAndScaleAxis
     */
    protected Matrix swapAndScaleAxis(final CoordinateSystem sourceCS,
                                      final CoordinateSystem targetCS)
            throws OperationNotFoundException
    {
        try {
            return AbstractCS.swapAndScaleAxis(sourceCS,targetCS);
        } catch (IllegalArgumentException exception) {
            throw new OperationNotFoundException(getErrorMessage(sourceCS, targetCS), exception);
        } catch (ConversionException exception) {
            throw new OperationNotFoundException(getErrorMessage(sourceCS, targetCS), exception);
        }
        // No attempt to catch ClassCastException since such
        // exception would indicates a programming error.
    }

    /**
     * Returns the specified identifier in a map to be given to coordinate operation constructors.
     * In the special case where the {@code name} identifier is {@link #DATUM_SHIFT} or
     * {@link #ELLIPSOID_SHIFT}, the map will contains extra informations like positional
     * accuracy.
     *
     * @todo In the datum shift case, an operation version is mandatory but unknow at this time.
     *       However, we noticed that the EPSG database do not always defines a version neither.
     *       Consequently, the Geotools implementation relax the rule requirying an operation
     *       version and we do not try to provide this information here for now.
     */
    private static Map<String,Object> getProperties(final ReferenceIdentifier name) {
        final Map<String,Object> properties;
        if (name==DATUM_SHIFT || name==ELLIPSOID_SHIFT) {
            properties = new HashMap<String,Object>(4);
            properties.put(NAME_KEY, name);
            properties.put(CoordinateOperation.COORDINATE_OPERATION_ACCURACY_KEY,
                  new PositionalAccuracy[] {
                      name==DATUM_SHIFT ? PositionalAccuracyImpl.DATUM_SHIFT_APPLIED
                                        : PositionalAccuracyImpl.DATUM_SHIFT_OMITTED});
        } else {
            properties = Collections.singletonMap(NAME_KEY, (Object) name);
        }
        return properties;
    }

    /**
     * Creates a coordinate operation from a matrix, which usually describes an affine tranform.
     * A default {@link OperationMethod} object is given to this transform. In the special case
     * where the {@code name} identifier is {@link #DATUM_SHIFT} or {@link #ELLIPSOID_SHIFT},
     * the operation will be an instance of {@link Transformation} instead of the usual
     * {@link Conversion}.
     *
     * @param  name      The identifier for the operation to be created.
     * @param  sourceCRS The source coordinate reference system.
     * @param  targetCRS The target coordinate reference system.
     * @param  matrix    The matrix which describe an affine transform operation.
     * @return The conversion or transformation.
     * @throws FactoryException if the operation can't be created.
     */
    protected CoordinateOperation createFromAffineTransform(
                                  final ReferenceIdentifier       name,
                                  final CoordinateReferenceSystem sourceCRS,
                                  final CoordinateReferenceSystem targetCRS,
                                  final Matrix                    matrix)
            throws FactoryException
    {
        final MathTransform transform = mtFactory.createAffineTransform(matrix);
        final Map<String,?> properties = getProperties(name);
        final Class<? extends Operation> type =
                properties.containsKey(CoordinateOperation.COORDINATE_OPERATION_ACCURACY_KEY)
                           ? Transformation.class : Conversion.class;
        return createFromMathTransform(properties, sourceCRS, targetCRS, transform,
               ProjectiveTransform.ProviderAffine.getProvider(transform.getSourceDimensions(),
                                                              transform.getTargetDimensions()), type);
    }

    /**
     * Creates a coordinate operation from a set of parameters.
     * The {@linkplain OperationMethod operation method} is inferred automatically,
     * if possible.
     *
     * @param  name       The identifier for the operation to be created.
     * @param  sourceCRS  The source coordinate reference system.
     * @param  targetCRS  The target coordinate reference system.
     * @param  parameters The parameters.
     * @return The conversion or transformation.
     * @throws FactoryException if the operation can't be created.
     */
    protected CoordinateOperation createFromParameters(
                                  final ReferenceIdentifier       name,
                                  final CoordinateReferenceSystem sourceCRS,
                                  final CoordinateReferenceSystem targetCRS,
                                  final ParameterValueGroup       parameters)
            throws FactoryException
    {
        final Map<String,?> properties = getProperties(name);
        final MathTransform transform = mtFactory.createParameterizedTransform(parameters);
        final OperationMethod  method = mtFactory.getLastMethodUsed();
        return createFromMathTransform(properties, sourceCRS, targetCRS, transform,
                                       method, Operation.class);
    }

    /**
     * Creates a coordinate operation from a math transform.
     *
     * @param  name       The identifier for the operation to be created.
     * @param  sourceCRS  The source coordinate reference system.
     * @param  targetCRS  The destination coordinate reference system.
     * @param  transform  The math transform.
     * @return A coordinate operation using the specified math transform.
     * @throws FactoryException if the operation can't be constructed.
     */
    protected CoordinateOperation createFromMathTransform(
                                  final ReferenceIdentifier       name,
                                  final CoordinateReferenceSystem sourceCRS,
                                  final CoordinateReferenceSystem targetCRS,
                                  final MathTransform             transform)
            throws FactoryException
    {
        return createFromMathTransform(Collections.singletonMap(NAME_KEY, name),
                                       sourceCRS, targetCRS, transform, null,
                                       CoordinateOperation.class);
    }

    /**
     * Creates a coordinate operation from a math transform.
     * If the specified math transform is already a coordinate operation, and if source
     * and target CRS match, then {@code transform} is returned with no change.
     * Otherwise, a new coordinate operation is created.
     *
     * @param  properties The properties to give to the operation.
     * @param  sourceCRS  The source coordinate reference system.
     * @param  targetCRS  The destination coordinate reference system.
     * @param  transform  The math transform.
     * @param  method     The operation method, or {@code null}.
     * @param  type       The required super-class (e.g. <code>{@linkplain Transformation}.class</code>).
     * @return A coordinate operation using the specified math transform.
     * @throws FactoryException if the operation can't be constructed.
     */
    protected CoordinateOperation createFromMathTransform(
                                  final Map<String,?>             properties,
                                  final CoordinateReferenceSystem sourceCRS,
                                  final CoordinateReferenceSystem targetCRS,
                                  final MathTransform             transform,
                                  final OperationMethod           method,
                                  final Class<? extends CoordinateOperation> type)
            throws FactoryException
    {
        CoordinateOperation operation;
        if (transform instanceof CoordinateOperation) {
            operation = (CoordinateOperation) transform;
            if (Utilities.equals(operation.getSourceCRS(),     sourceCRS) &&
                Utilities.equals(operation.getTargetCRS(),     targetCRS) &&
                Utilities.equals(operation.getMathTransform(), transform))
            {
                if (operation instanceof Operation) {
                    if (Utilities.equals(((Operation) operation).getMethod(), method)) {
                        return operation;
                    }
                } else {
                    return operation;
                }
            }
        }
        operation = DefaultOperation.create(properties, sourceCRS, targetCRS, transform, method, type);
        operation = pool.unique(operation);
        return operation;
    }

    /**
     * Constructs a defining conversion from a set of properties.
     *
     * @param  properties Set of properties. Should contains at least {@code "name"}.
     * @param  method The operation method.
     * @param  parameters The parameter values.
     * @return The defining conversion.
     * @throws FactoryException if the object creation failed.
     *
     * @see DefiningConversion
     *
     * @since 2.5
     */
    public Conversion createDefiningConversion(
                        final Map<String,?>       properties,
                        final OperationMethod     method,
                        final ParameterValueGroup parameters) throws FactoryException
    {
        Conversion conversion = new DefiningConversion(properties, method, parameters);
        conversion = pool.unique(conversion);
        return conversion;
    }

    /**
     * Creates a concatenated operation from a sequence of operations.
     *
     * @param  properties Set of properties. Should contains at least {@code "name"}.
     * @param  operations The sequence of operations.
     * @return The concatenated operation.
     * @throws FactoryException if the object creation failed.
     */
    public CoordinateOperation createConcatenatedOperation(
                        final Map<String,?> properties,
                        final CoordinateOperation[] operations) throws FactoryException
    {
        CoordinateOperation operation;
        operation = new DefaultConcatenatedOperation(properties, operations, mtFactory);
        operation = pool.unique(operation);
        return operation;
    }

    /**
     * Concatenate two operation steps. If an operation is an {@link #AXIS_CHANGES},
     * it will be included as part of the second operation instead of creating an
     * {@link ConcatenatedOperation}. If a concatenated operation is created, it
     * will get an automatically generated name.
     *
     * @param  step1 The first  step, or {@code null} for the identity operation.
     * @param  step2 The second step, or {@code null} for the identity operation.
     * @return A concatenated operation, or {@code null} if all arguments was nul.
     * @throws FactoryException if the operation can't be constructed.
     */
    protected CoordinateOperation concatenate(final CoordinateOperation step1,
                                              final CoordinateOperation step2)
            throws FactoryException
    {
        if (step1 == null) return step2;
        if (step2 == null) return step1;
        if (false) {
            // Note: we sometime get this assertion failure if the user provided CRS with two
            //       different ellipsoids but an identical TOWGS84 conversion infos (which is
            //       usually wrong, but still happen).
            assert equalsIgnoreMetadata(step1.getTargetCRS(), step2.getSourceCRS()) :
                   "CRS 1 =" + step1.getTargetCRS() + '\n' +
                   "CRS 2 =" + step2.getSourceCRS();
        }
        if (isIdentity(step1)) return step2;
        if (isIdentity(step2)) return step1;
        final MathTransform mt1 = step1.getMathTransform();
        final MathTransform mt2 = step2.getMathTransform();
        final CoordinateReferenceSystem sourceCRS = step1.getSourceCRS();
        final CoordinateReferenceSystem targetCRS = step2.getTargetCRS();
        CoordinateOperation step = null;
        if (step1.getName()==AXIS_CHANGES && mt1.getSourceDimensions()==mt1.getTargetDimensions()) step = step2;
        if (step2.getName()==AXIS_CHANGES && mt2.getSourceDimensions()==mt2.getTargetDimensions()) step = step1;
        if (step instanceof Operation) {
            /*
             * Applies only on operation in order to avoid merging with PassThroughOperation.
             * Also applies only if the transform to hide has identical source and target
             * dimensions in order to avoid mismatch with the method's dimensions.
             */
            return createFromMathTransform(AbstractIdentifiedObject.getProperties(step),
                   sourceCRS, targetCRS, mtFactory.createConcatenatedTransform(mt1, mt2),
                   ((Operation) step).getMethod(), CoordinateOperation.class);
        }
        return createConcatenatedOperation(getTemporaryName(sourceCRS, targetCRS),
                                           new CoordinateOperation[] {step1, step2});
    }

    /**
     * Concatenate three transformation steps. If the first and/or the last operation is an
     * {@link #AXIS_CHANGES}, it will be included as part of the second operation instead of
     * creating an {@link ConcatenatedOperation}. If a concatenated operation is created, it
     * will get an automatically generated name.
     *
     * @param  step1 The first  step, or {@code null} for the identity operation.
     * @param  step2 The second step, or {@code null} for the identity operation.
     * @param  step3 The third  step, or {@code null} for the identity operation.
     * @return A concatenated operation, or {@code null} if all arguments were null.
     * @throws FactoryException if the operation can't be constructed.
     */
    protected CoordinateOperation concatenate(final CoordinateOperation step1,
                                              final CoordinateOperation step2,
                                              final CoordinateOperation step3)
            throws FactoryException
    {
        if (step1 == null) return concatenate(step2, step3);
        if (step2 == null) return concatenate(step1, step3);
        if (step3 == null) return concatenate(step1, step2);
        assert equalsIgnoreMetadata(step1.getTargetCRS(), step2.getSourceCRS()) : step1;
        assert equalsIgnoreMetadata(step2.getTargetCRS(), step3.getSourceCRS()) : step3;

        if (isIdentity(step1)) return concatenate(step2, step3);
        if (isIdentity(step2)) return concatenate(step1, step3);
        if (isIdentity(step3)) return concatenate(step1, step2);
        if (step1.getName() == AXIS_CHANGES) return concatenate(concatenate(step1, step2), step3);
        if (step3.getName() == AXIS_CHANGES) return concatenate(step1, concatenate(step2, step3));
        final CoordinateReferenceSystem sourceCRS = step1.getSourceCRS();
        final CoordinateReferenceSystem targetCRS = step3.getTargetCRS();
        return createConcatenatedOperation(getTemporaryName(sourceCRS, targetCRS),
                                           new CoordinateOperation[] {step1, step2, step3});
    }

    /**
     * Returns {@code true} if the specified operation is an identity conversion.
     * This method always returns {@code false} for transformations even if their
     * associated math transform is an identity one, because such transformations
     * are usually datum shift and must be visible.
     */
    private static boolean isIdentity(final CoordinateOperation operation) {
        return (operation instanceof Conversion) && operation.getMathTransform().isIdentity();
    }

    /**
     * Returns the inverse of the specified operation.
     *
     * @param  operation The operation to invert.
     * @return The inverse of {@code operation}.
     * @throws NoninvertibleTransformException if the operation is not invertible.
     * @throws FactoryException if the operation creation failed for an other reason.
     *
     * @since 2.3
     */
    protected CoordinateOperation inverse(final CoordinateOperation operation)
            throws NoninvertibleTransformException, FactoryException
    {
        final CoordinateReferenceSystem sourceCRS = operation.getSourceCRS();
        final CoordinateReferenceSystem targetCRS = operation.getTargetCRS();
        final Map<String,Object> properties = AbstractIdentifiedObject.getProperties(operation, null);
        properties.putAll(getTemporaryName(targetCRS, sourceCRS));
        if (operation instanceof ConcatenatedOperation) {
            final LinkedList<CoordinateOperation> inverted = new LinkedList<CoordinateOperation>();
            for (final CoordinateOperation op : ((ConcatenatedOperation) operation).getOperations()) {
                inverted.addFirst(inverse(op));
            }
            return createConcatenatedOperation(properties,
                    inverted.toArray(new CoordinateOperation[inverted.size()]));
        }
        final MathTransform transform = operation.getMathTransform().inverse();
        final Class<? extends CoordinateOperation> type = AbstractCoordinateOperation.getType(operation);
        final OperationMethod method = (operation instanceof Operation) ?
                                       ((Operation) operation).getMethod() : null;
        return createFromMathTransform(properties, targetCRS, sourceCRS, transform, method, type);
    }




    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////
    ////////////                                                         ////////////
    ////////////                M I S C E L L A N E O U S                ////////////
    ////////////                                                         ////////////
    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the dimension of the specified coordinate system,
     * or {@code 0} if the coordinate system is null.
     */
    static int getDimension(final CoordinateReferenceSystem crs) {
        return (crs!=null) ? crs.getCoordinateSystem().getDimension() : 0;
    }

    /**
     * An identifier for temporary objects. This identifier manage a count of temporary
     * identifier. The count is appended to the identifier name (e.g. "WGS84 (step 1)").
     */
    private static final class TemporaryIdentifier extends NamedIdentifier {
        /** For cross-version compatibility. */
        private static final long serialVersionUID = -2784354058026177076L;

        /** The parent identifier. */
        private final ReferenceIdentifier parent;

        /** The temporary object count. */
        private final int count;

        /** Constructs an identifier derived from the specified one. */
        public TemporaryIdentifier(final ReferenceIdentifier parent) {
            this(parent, ((parent instanceof TemporaryIdentifier) ?
                         ((TemporaryIdentifier) parent).count : 0) + 1);
        }

        /** Work around for RFE #4093999 in Sun's bug database */
        private TemporaryIdentifier(final ReferenceIdentifier parent, final int count) {
            super(Citations.GEOTOOLS, unwrap(parent).getCode() + " (step " + count + ')');
            this.parent = parent;
            this.count  = count;
        }

        /** Returns the parent identifier for the specified identifier, if any. */
        public static ReferenceIdentifier unwrap(ReferenceIdentifier identifier) {
            while (identifier instanceof TemporaryIdentifier) {
                identifier = ((TemporaryIdentifier) identifier).parent;
            }
            return identifier;
        }
    }

    /**
     * Returns the name of the GeoAPI interface implemented by the specified object.
     * In addition, the name may be added between brackets.
     */
    private static String getClassName(final IdentifiedObject object) {
        if (object != null) {
            Class type = object.getClass();
            final Class[] interfaces = type.getInterfaces();
            for (int i=0; i<interfaces.length; i++) {
                final Class candidate = interfaces[i];
                if (candidate.getName().startsWith("org.opengis.referencing.")) {
                    type = candidate;
                    break;
                }
            }
            String name = Classes.getShortName(type);
            final ReferenceIdentifier id = object.getName();
            if (id != null) {
                name = name + '[' + id.getCode() + ']';
            }
            return name;
        }
        return null;
    }

    /**
     * Returns a temporary name for object derived from the specified one.
     *
     * @param source The CRS to base name on, or {@code null} if none.
     */
    static Map<String,Object> getTemporaryName(final IdentifiedObject source) {
        final Map<String,Object> properties = new HashMap<String,Object>(4);
        properties.put(NAME_KEY, new TemporaryIdentifier(source.getName()));
        properties.put(IdentifiedObject.REMARKS_KEY, Vocabulary.formatInternational(
                       VocabularyKeys.DERIVED_FROM_$1, getClassName(source)));
        return properties;
    }

    /**
     * Returns a temporary name for object derived from a concatenation.
     *
     * @param source The CRS to base name on, or {@code null} if none.
     */
    static Map<String,?> getTemporaryName(final CoordinateReferenceSystem source,
                                          final CoordinateReferenceSystem target)
    {
        final String name = getClassName(source) + " \u21E8 " + getClassName(target);
        return Collections.singletonMap(NAME_KEY, name);
    }

    /**
     * Returns an error message for "No path found from sourceCRS to targetCRS".
     * This is used for the construction of {@link OperationNotFoundException}.
     *
     * @param  source The source CRS.
     * @param  target The target CRS.
     * @return A default error message.
     */
    protected static String getErrorMessage(final IdentifiedObject source,
                                            final IdentifiedObject target)
    {
        return Errors.format(ErrorKeys.NO_TRANSFORMATION_PATH_$2,
                             getClassName(source), getClassName(target));
    }

    /**
     * Makes sure an argument is non-null.
     *
     * @param  name   Argument name.
     * @param  object User argument.
     * @throws IllegalArgumentException if {@code object} is null.
     */
    protected static void ensureNonNull(final String name, final Object object)
            throws IllegalArgumentException
    {
        if (object == null) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, name));
        }
    }
}
