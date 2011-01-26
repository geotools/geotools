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
package org.geotools.referencing.operation;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.*;

import org.geotools.factory.Hints;
import org.geotools.factory.OptionalFactory;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.factory.BackingStoreException;
import org.geotools.resources.i18n.Loggings;
import org.geotools.resources.i18n.LoggingKeys;

import static org.geotools.referencing.CRS.equalsIgnoreMetadata;


/**
 * A {@linkplain CoordinateOperationFactory coordinate operation factory} extended with the extra
 * informations provided by an {@linkplain CoordinateOperationAuthorityFactory authority factory}.
 * Such authority factory may help to find transformation paths not available otherwise (often
 * determined from empirical parameters). Authority factories can also provide additional
 * informations like the
 * {@linkplain CoordinateOperation#getValidArea area of validity},
 * {@linkplain CoordinateOperation#getScope scope} and
 * {@linkplain CoordinateOperation#getPositionalAccuracy positional accuracy}.
 * <p>
 * When <code>{@linkplain #createOperation createOperation}(sourceCRS, targetCRS)</code> is invoked,
 * {@code AuthorityBackedFactory} fetch the authority codes for source and target CRS and submits
 * them to the {@linkplain #getAuthorityFactory underlying authority factory} through a call to its
 * <code>{@linkplain CoordinateOperationAuthorityFactory#createFromCoordinateReferenceSystemCodes
 * createFromCoordinateReferenceSystemCodes}(sourceCode, targetCode)</code> method. If the
 * authority factory doesn't know about the specified CRS, then the default (standalone)
 * process from the super-class is used as a fallback.
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class AuthorityBackedFactory extends DefaultCoordinateOperationFactory
        implements OptionalFactory
{
    /**
     * The priority level for this factory.
     */
    static final int PRIORITY = DefaultCoordinateOperationFactory.PRIORITY + 10;

    /**
     * The default authority factory to use.
     */
    private static final String DEFAULT_AUTHORITY = "EPSG";

    /**
     * The authority factory to use for creating new operations.
     * If {@code null}, a default factory will be fetched when first needed.
     */
    private CoordinateOperationAuthorityFactory authorityFactory;

    /**
     * Used as a guard against infinite recursivity.
     */
    private final ThreadLocal<Boolean> processing = new ThreadLocal<Boolean>();

    /**
     * Creates a new factory backed by a default EPSG authority factory.
     * This factory will uses a priority slightly higher than the
     * {@linkplain DefaultCoordinateOperationFactory default (standalone) factory}.
     */
    public AuthorityBackedFactory() {
        this(null);
    }

    /**
     * Creates a new factory backed by an authority factory fetched using the specified hints.
     * This constructor recognizes the {@link Hints#CRS_FACTORY CRS}, {@link Hints#CS_FACTORY CS},
     * {@link Hints#DATUM_FACTORY DATUM} and {@link Hints#MATH_TRANSFORM_FACTORY MATH_TRANSFORM}
     * {@code FACTORY} hints.
     *
     * @param userHints The hints, or {@code null} if none.
     */
    public AuthorityBackedFactory(Hints userHints) {
        super(userHints, PRIORITY);
        /*
         * Removes the hint processed by the super-class. This include hints like
         * LENIENT_DATUM_SHIFT, which usually don't apply to authority factories.
         * An other way to see this is to said that this class "consumed" the hints.
         * By removing them, we increase the chances to get an empty map of remaining hints,
         * which in turn help to get the default CoordinateOperationAuthorityFactory
         * (instead of forcing a new instance).
         */
        userHints = new Hints(userHints);
        userHints.keySet().removeAll(hints.keySet());
        userHints.remove(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
        userHints.remove(Hints.FORCE_STANDARD_AXIS_DIRECTIONS);
        userHints.remove(Hints.FORCE_STANDARD_AXIS_UNITS);
        if (!userHints.isEmpty()) {
            noForce(userHints);
            authorityFactory = ReferencingFactoryFinder.getCoordinateOperationAuthorityFactory(
                    DEFAULT_AUTHORITY, userHints);
        }
    }

    /**
     * Makes sure that every {@code FORCE_*} hints are set to false. We do that because we want
     * {@link CoordinateOperationAuthorityFactory#createFromCoordinateReferenceSystemCodes} to
     * returns coordinate operations straight from the EPSG database; we don't want an instance
     * like {@link org.geotools.referencing.factory.OrderedAxisAuthorityFactory}. Axis swapping
     * are performed by {@link #createFromDatabase} in this class <strong>after</strong> we invoked
     * {@link CoordinateOperationAuthorityFactory#createFromCoordinateReferenceSystemCodes}. An
     * {@code OrderedAxisAuthorityFactory} instance in this class would be in the way and cause
     * an infinite recursivity.
     *
     * @see http://jira.codehaus.org/browse/GEOT-1161
     */
    private static void noForce(final Hints userHints) {
        userHints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE);
        userHints.put(Hints.FORCE_STANDARD_AXIS_DIRECTIONS,   Boolean.FALSE);
        userHints.put(Hints.FORCE_STANDARD_AXIS_UNITS,        Boolean.FALSE);
    }

    /**
     * Returns the underlying coordinate operation authority factory.
     */
    protected CoordinateOperationAuthorityFactory getAuthorityFactory() {
        /*
         * No need to synchronize. This is not a big deal if ReferencingFactoryFinder is invoked
         * twice since it is already synchronized. Actually, we should not synchronize at all.
         * Every methods from the super-class are thread-safe without synchronized statements,
         * and we should preserve this advantage in order to reduce the risk of contention.
         */
        if (authorityFactory == null) {
            /*
             * Factory creation at this stage will happen only if null hints were specified at
             * construction time, which explain why it is correct to use {@link FactoryFinder}
             * with empty hints here.
             */
            final Hints hints = new Hints();
            noForce(hints);
            authorityFactory = ReferencingFactoryFinder
                    .getCoordinateOperationAuthorityFactory(DEFAULT_AUTHORITY, hints);
        }
        return authorityFactory;
    }

    /**
     * Returns an operation for conversion or transformation between two coordinate reference
     * systems. The default implementation extracts the authority code from the supplied
     * {@code sourceCRS} and {@code targetCRS}, and submit them to the
     * <code>{@linkplain CoordinateOperationAuthorityFactory#createFromCoordinateReferenceSystemCodes
     * createFromCoordinateReferenceSystemCodes}(sourceCode, targetCode)</code> methods.
     * If no operation is found for those codes, then this method returns {@code null}.
     * <p>
     * Note that this method may be invoked recursively. For example no operation may be available
     * from the {@linkplain #getAuthorityFactory underlying authority factory} between two
     * {@linkplain org.opengis.referencing.crs.CompoundCRS compound CRS}, but an operation
     * may be available between two components of those compound CRS.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}, or {@code null}
     *         if no such operation is explicitly defined in the underlying database.
     *
     * @since 2.3
     */
    @Override
    protected CoordinateOperation createFromDatabase(final CoordinateReferenceSystem sourceCRS,
                                                     final CoordinateReferenceSystem targetCRS)
    {
        /*
         * Safety check against recursivity: returns null if the given source and target CRS
         * are already under examination by a previous call to this method. Note: there is no
         * need to synchronize since the Boolean is thread-local.
         */
        if (Boolean.TRUE.equals(processing.get())) {
            return null;
        }
        /*
         * Now performs the real work.
         */
        final CoordinateOperationAuthorityFactory authorityFactory = getAuthorityFactory();
        final Citation  authority = authorityFactory.getAuthority();
        final Identifier sourceID = AbstractIdentifiedObject.getIdentifier(sourceCRS, authority);
        if (sourceID == null) {
            return null;
        }
        final Identifier targetID = AbstractIdentifiedObject.getIdentifier(targetCRS, authority);
        if (targetID == null) {
            return null;
        }
        final String sourceCode = sourceID.getCode().trim();
        final String targetCode = targetID.getCode().trim();
        if (sourceCode.equals(targetCode)) {
            /*
             * NOTE: This check is mandatory because this method may be invoked in some situations
             *       where (sourceCode == targetCode) but (sourceCRS != targetCRS). Such situation
             *       should be illegal  (or at least the MathTransform from sourceCRS to targetCRS
             *       should be the identity transform),   but unfortunatly it still happen because
             *       EPSG defines axis order as (latitude,longitude) for geographic CRS while most
             *       softwares expect (longitude,latitude) no matter what the EPSG authority said.
             *       We will need to computes a transform from sourceCRS to targetCRS ignoring the
             *       source and target codes. The superclass can do that, providing that we prevent
             *       the authority database to (legitimately) claims that the transformation from
             *       sourceCode to targetCode is the identity transform. See GEOT-854.
             */
            return null;
        }
        final boolean inverse;
        Set<CoordinateOperation> operations;
        try {
            operations = authorityFactory.createFromCoordinateReferenceSystemCodes(sourceCode, targetCode);
            inverse = (operations == null || operations.isEmpty());
            if (inverse) {
                /*
                 * No operation from 'source' to 'target' available. But maybe there is an inverse
                 * operation. This is typically the case when the user wants to convert from a
                 * projected to a geographic CRS. The EPSG database usually contains transformation
                 * paths for geographic to projected CRS only.
                 */
                operations = authorityFactory.createFromCoordinateReferenceSystemCodes(targetCode, sourceCode);
            }
        } catch (NoSuchAuthorityCodeException exception) {
            /*
             * sourceCode or targetCode is unknow to the underlying authority factory.
             * Ignores the exception and fallback on the generic algorithm provided by
             * the super-class.
             */
            return null;
        } catch (FactoryException exception) {
            /*
             * Other kind of error. It may be more serious, but the super-class is capable
             * to provides a raisonable default behavior. Log as a warning and lets continue.
             */
            log(exception, authorityFactory);
            return null;
        }
        if (operations != null) {
            for (final Iterator<CoordinateOperation> it=operations.iterator(); it.hasNext();) {
                CoordinateOperation candidate;
                try {
                    // The call to it.next() must be inside the try..catch block,
                    // which is why we don't use the Java 5 for loop syntax here.
                    candidate = it.next();
                    if (candidate == null) {
                        continue;
                    }
                    if (inverse) {
                        candidate = inverse(candidate);
                    }
                } catch (NoninvertibleTransformException e) {
                    // The transform is non invertible. Do not log any error message, since it
                    // may be a normal failure - the transform is not required to be invertible.
                    continue;
                } catch (FactoryException exception) {
                    // Other kind of error. Log a warning and try the next coordinate operation.
                    log(exception, authorityFactory);
                    continue;
                } catch (BackingStoreException exception) {
                    log(exception, authorityFactory);
                    continue;
                }
                /*
                 * It is possible that the Identifier in user's CRS is not quite right.   For
                 * example the user may have created his source and target CRS from WKT using
                 * a different axis order than the official one and still call it "EPSG:xxxx"
                 * as if it were the official CRS. Checks if the source and target CRS for the
                 * operation just created are really the same (ignoring metadata) than the one
                 * specified by the user.
                 */
                CoordinateReferenceSystem source = candidate.getSourceCRS();
                CoordinateReferenceSystem target = candidate.getTargetCRS();
                try {
                    final MathTransform prepend, append;
                    if (!equalsIgnoreMetadata(sourceCRS, source)) try {
                        processing.set(Boolean.TRUE);
                        prepend = createOperation(sourceCRS, source).getMathTransform();
                        source  = sourceCRS;
                    } finally {
                        processing.remove();
                    } else {
                        prepend = null;
                    }
                    if (!equalsIgnoreMetadata(target, targetCRS)) try {
                        processing.set(Boolean.TRUE);
                        append = createOperation(target, targetCRS).getMathTransform();
                        target = targetCRS;
                    } finally {
                        processing.remove();
                    } else {
                        append = null;
                    }
                    candidate = transform(source, prepend, candidate, append, target);
                } catch (FactoryException exception) {
                    /*
                     * We have been unable to create a transform from the user-provided CRS to the
                     * authority-provided CRS. In theory, the two CRS should have been the same and
                     * the transform would have been the identity transform. In practice, it is not
                     * always the case because of axis swapping issue (see GEOT-854). The transform
                     * that we just tried to create in the two previous calls to the createOperation
                     * method should have been merely an affine transform for swapping axis. If they
                     * failed, then we are likely to fail for all other transforms provided in the
                     * database. So stop the loop now (at the very least, do not log the same
                     * warning for every pass of this loop!)
                     */
                    log(exception, authorityFactory);
                    return null;
                }
                if (accept(candidate)) {
                    return candidate;
                }
            }
        }
        return null;
    }

    /**
     * Appends or prepends the specified math transforms to the
     * {@linkplain CoordinateOperation#getMathTransform operation math transform}.
     * The new coordinate operation (if any) will share the same metadata
     * than the original operation, including the authority code.
     * <p>
     * This method is used in order to change axis order when the user-specified CRS
     * disagree with the authority-supplied CRS.
     *
     * @param sourceCRS The source CRS to give to the new operation.
     * @param prepend   The transform to prepend to the operation math transform.
     * @param operation The operation in which to prepend the math transforms.
     * @param append    The transform to append to the operation math transform.
     * @param targetCRS The target CRS to give to the new operation.
     * @return A new operation, or {@code operation} if {@code prepend} and {@code append} were
     *         nulls or identity transforms.
     * @throws FactoryException if the operation can't be constructed.
     */
    private CoordinateOperation transform(final CoordinateReferenceSystem sourceCRS,
                                          final MathTransform             prepend,
                                          final CoordinateOperation       operation,
                                          final MathTransform             append,
                                          final CoordinateReferenceSystem targetCRS)
            throws FactoryException
    {
        if ((prepend == null || prepend.isIdentity()) && (append == null || append.isIdentity())) {
            return operation;
        }
        final Map<String,?> properties = AbstractIdentifiedObject.getProperties(operation);
        /*
         * In the particular case of concatenated operations, we can not prepend or append a math
         * transform to the operation as a whole (the math transform for a concatenated operation
         * is computed automatically as the concatenation of the math transform from every single
         * operations, and we need to stay consistent with that). Instead, we prepend to the first
         * single operation and append to the last single operation.
         */
        if (operation instanceof ConcatenatedOperation) {
            final List<SingleOperation> c = ((ConcatenatedOperation) operation).getOperations();
            final CoordinateOperation[] op = c.toArray(new CoordinateOperation[c.size()]);
            if (op.length != 0) {
                final CoordinateOperation first = op[0];
                if (op.length == 1) {
                    op[0] = transform(sourceCRS, prepend, first, append, targetCRS);
                } else {
                    final CoordinateOperation last = op[op.length-1];
                    op[0]           = transform(sourceCRS, prepend, first, null, first.getTargetCRS());
                    op[op.length-1] = transform(last.getSourceCRS(), null, last, append, targetCRS);
                }
                return createConcatenatedOperation(properties, op);
            }
        }
        /*
         * Single operation case.
         */
        MathTransform transform = operation.getMathTransform();
        final MathTransformFactory mtFactory = getMathTransformFactory();
        if (prepend != null) {
            transform = mtFactory.createConcatenatedTransform(prepend, transform);
        }
        if (append != null) {
            transform = mtFactory.createConcatenatedTransform(transform, append);
        }
        assert !transform.equals(operation.getMathTransform()) : transform;
        final Class<? extends CoordinateOperation> type = AbstractCoordinateOperation.getType(operation);
        OperationMethod method = null;
        if (operation instanceof Operation) {
            method = ((Operation) operation).getMethod();
            if (method != null) {
                final int sourceDimensions = transform.getSourceDimensions();
                final int targetDimensions = transform.getTargetDimensions();
                if (sourceDimensions != method.getSourceDimensions() ||
                    targetDimensions != method.getTargetDimensions())
                {
                    method = new DefaultOperationMethod(method, sourceDimensions, targetDimensions);
                }
            }
        }
        return createFromMathTransform(properties, sourceCRS, targetCRS, transform, method, type);
    }

    /**
     * Logs a warning when an object can't be created from the specified factory.
     */
    private static void log(final Exception exception, final AuthorityFactory factory) {
        final LogRecord record = Loggings.format(Level.WARNING,
                                 LoggingKeys.CANT_CREATE_COORDINATE_OPERATION_$1,
                                 factory.getAuthority().getTitle());
        record.setSourceClassName(AuthorityBackedFactory.class.getName());
        record.setSourceMethodName("createFromDatabase");
        record.setThrown(exception);
        record.setLoggerName(LOGGER.getName());
        LOGGER.log(record);
    }

    /**
     * Returns {@code true} if the specified operation is acceptable. This method is invoked
     * automatically by <code>{@linkplain #createFromDatabase createFromDatabase}(...)</code>
     * for every operation candidates found. The default implementation returns always {@code
     * true}. Subclasses should override this method if they wish to filter the coordinate
     * operations to be returned.
     *
     * @since 2.3
     */
    protected boolean accept(final CoordinateOperation operation) {
        return true;
    }

    /**
     * Returns {@code true} if this factory and its underlying
     * {@linkplain #getAuthorityFactory authority factory} are available for use.
     */
    public boolean isAvailable() {
        try {
            final CoordinateOperationAuthorityFactory authorityFactory = getAuthorityFactory();
            if (authorityFactory instanceof OptionalFactory) {
                return ((OptionalFactory) authorityFactory).isAvailable();
            }
            return true;
        } catch (FactoryRegistryException exception) {
            // No factory found. Ignore the exception since it is the
            // purpose of this method to figure out this kind of case.
            return false;
        }
    }
}
