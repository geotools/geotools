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
package org.geotools.geometry;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;

import org.geotools.factory.Hints;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;


/**
 * A direct position capable to {@linkplain #transform transform} a point between an arbitrary CRS
 * and {@linkplain #getCoordinateReferenceSystem its own CRS}. This class caches the last transform
 * used in order to improve the performances when the {@linkplain CoordinateOperation#getSourceCRS
 * source} and {@linkplain CoordinateOperation#getTargetCRS target} CRS don't change often. Using
 * this class is faster than invoking <code>{@linkplain CoordinateOperationFactory#createOperation
 * CoordinateOperationFactory.createOperation}(sourceCRS, targetCRS)</code> for every points.
 *
 * <ul>
 *   <li><p><strong>Note 1:</strong>
 *   This class is advantageous on a performance point of view only if the same instance of
 *   {@code TransformedDirectPosition} is used for transforming many points between arbitrary
 *   CRS and this {@linkplain #getCoordinateReferenceSystem position CRS}.</p></li>
 *
 *   <li><p><strong>Note 2:</strong>
 *   This convenience class is useful when the source and target CRS are <em>not likely</em> to
 *   change often. If you are <em>sure</em> that the source and target CRS will not change at all
 *   for a given set of positions, then using {@link CoordinateOperation} directly gives better
 *   performances. This is because {@code TransformedDirectPosition} checks if the CRS changed
 *   before every transformations, which may be costly.</p></li>
 *
 *   <li><p><strong>Note 3:</strong>
 *   This class is called <cite>Transformed</cite> Direct Position because it is more commonly
 *   used for transforming many points from arbitrary CRS to a common CRS (using the
 *   {@link #transform(DirectPosition)} method) than the other way around.</li></p>
 * </ul>
 *
 * This class usually don't appears in a public API. It is more typicaly used as a helper private
 * field in some more complex class. For example suppose that {@code MyClass} needs to perform its
 * internal working in some particular CRS, but we want robust API that adjusts itself to whatever
 * CRS the client happen to use. {@code MyClass} could be written as below:
 *
 * <blockquote><pre>
 * public class MyClass {
 *     private static final CoordinateReferenceSystem   PUBLIC_CRS = ...
 *     private static final CoordinateReferenceSystem INTERNAL_CRS = ...
 *
 *     private final TransformedDirectPosition myPosition =
 *             new TransformedDirectPosition(PUBLIC_CRS, INTERNAL_CRS, null);
 *
 *     public void setPosition(DirectPosition position) throws TransformException {
 *         // The position CRS is usually PUBLIC_CRS, but code below will work even if it is not.
 *         myPosition.transform(position);
 *     }
 *
 *     public DirectPosition getPosition() throws TransformException {
 *         return myPosition.inverseTransform(PUBLIC_CRS);
 *     }
 * }
 * </pre></blockquote>
 *
 * @since 2.2
 * @author Martin Desruisseaux (IRD)
 *
 * @source $URL$
 * @version $Id$
 */
public class TransformedDirectPosition extends GeneralDirectPosition {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -3988283183934950437L;

    /**
     * The factory to use for creating new coordinate operation.
     */
    private final CoordinateOperationFactory factory;

    /**
     * The default source CRS. To be used only when the user invoked {@link #transform} with
     * a position without associated {@link CoordinateReferenceSystem}. May be {@code null}
     * if the default CRS is assumed equals to {@linkplain #getCoordinateReferenceSystem this
     * position CRS}.
     */
    private final CoordinateReferenceSystem defaultCRS;

    /**
     * The last source CRS used, or {@code null}. The {@code targetCRS} is the
     * {@linkplain #getCoordinateReferenceSystem CRS associated with this position}.
     */
    private transient CoordinateReferenceSystem sourceCRS;

    /**
     * The forward and inverse transforms. Will be created only when first needed.
     */
    private transient MathTransform forward, inverse;

    /**
     * Creates a new direct position initialized with the
     * {@linkplain DefaultGeographicCRS#WGS84 WGS84} CRS.
     *
     * @since 2.3
     */
    public TransformedDirectPosition() {
        this(null, DefaultGeographicCRS.WGS84, null);
    }

    /**
     * Creates a new position which will contains the result of coordinate transformations from
     * {@code sourceCRS} to {@code targetCRS}. The {@linkplain #getCoordinateReferenceSystem CRS
     * associated with this position} will be initially set to {@code targetCRS}.
     *
     * @param sourceCRS The <strong>default</strong> CRS to be used by the
     *        <code>{@link #transform transform}(position)</code> method <strong>only</strong>
     *        when the user-supplied {@code position} has a null
     *        {@linkplain DirectPosition#getCoordinateReferenceSystem associated CRS}.
     *        This {@code sourceCRS} argument may be {@code null}, in which case it is assumed
     *        the same than {@code targetCRS}.
     *
     * @param targetCRS The {@linkplain #getCoordinateReferenceSystem CRS associated with this
     *        position}. Used for every {@linkplain #transform coordinate transformations} until
     *        the next call to {@link #setCoordinateReferenceSystem setCoordinateReferenceSystem}
     *        or {@link #setLocation(DirectPosition) setLocation}. This argument can not be null.
     *
     * @param hints The set of hints to use for fetching a {@link CoordinateOperationFactory},
     *        or {@code null} if none.
     *
     * @throws IllegalArgumentException if {@code targetCRS} was {@code null}.
     * @throws FactoryRegistryException if no {@linkplain CoordinateOperationFactory coordinate
     *         operation factory} can be found for the specified hints.
     *
     * @since 2.3
     */
    public TransformedDirectPosition(final CoordinateReferenceSystem sourceCRS,
                                     final CoordinateReferenceSystem targetCRS,
                                     final Hints hints)
            throws FactoryRegistryException
    {
        super(targetCRS);
        ensureNonNull("targetCRS", targetCRS);
        defaultCRS = CRS.equalsIgnoreMetadata(sourceCRS, targetCRS) ? null : sourceCRS;
        factory = ReferencingFactoryFinder.getCoordinateOperationFactory(hints);
    }

    /**
     * Sets the coordinate reference system in which the coordinate is given.
     * The given CRS will be used as:
     * <p>
     * <ul>
     *   <li>the {@linkplain CoordinateOperation#getTargetCRS target CRS} for every call to
     *       {@link #transform(DirectPosition)}</li>
     *   <li>the {@linkplain CoordinateOperation#getSourceCRS source CRS} for every call to
     *       {@link #inverseTransform(CoordinateReferenceSystem)}</li>
     * </ul>
     *
     * @param  crs The new CRS for this direct position.
     * @throws MismatchedDimensionException if the specified CRS doesn't have the expected
     *         number of dimensions.
     */
    @Override
    public void setCoordinateReferenceSystem(final CoordinateReferenceSystem crs)
            throws MismatchedDimensionException
    {
        ensureNonNull("crs", crs);
        super.setCoordinateReferenceSystem(crs);
        forward = null;
        inverse = null;
    }

    /**
     * Sets the {@link #sourceCRS} field and create the associated {@link #forward} transform.
     * This method do not create yet the {@link #inverse} transform, since it may not be needed.
     */
    private void setSourceCRS(final CoordinateReferenceSystem crs) throws TransformException {
        final CoordinateReferenceSystem targetCRS = getCoordinateReferenceSystem();
        final CoordinateOperation operation;
        try {
            operation = factory.createOperation(crs, targetCRS);
        } catch (FactoryException exception) {
            throw new TransformException(exception.getLocalizedMessage(), exception);
        }
        /*
         * Note: 'sourceCRS' must be set last, when we are sure that all other fields
         * are set to their correct value.  This is in order to keep this instance in
         * a consistent state in case an exception is thrown.
         */
        forward   = operation.getMathTransform();
        inverse   = null;
        sourceCRS = crs;
    }

    /**
     * Transforms a given position and stores the result in this object.
     *
     * <ul>
     *   <li><p>The {@linkplain CoordinateOperation#getSourceCRS source CRS} is the
     *       {@linkplain DirectPosition#getCoordinateReferenceSystem CRS associated with the given
     *       position}, or the {@code sourceCRS} argument given at
     *       {@linkplain #TransformedDirectPosition(CoordinateReferenceSystem,
     *       CoordinateReferenceSystem, Hints) construction time} <strong>if and only if</strong>
     *       the CRS associated with {@code position} is null.</p></li>
     *
     *   <li><p>The {@linkplain CoordinateOperation#getTargetCRS target CRS} is the {@linkplain
     *       #getCoordinateReferenceSystem CRS associated with this position}. This is always the
     *       {@code targetCRS} argument given at {@linkplain
     *       #TransformedDirectPosition(CoordinateReferenceSystem, CoordinateReferenceSystem,
     *       Hints) construction time} or by the last call to {@link #setCoordinateReferenceSystem
     *       setCoordinateReferenceSystem}.</p></li>
     * </ul>
     *
     * @param  position A position using an arbitrary CRS. This object will not be modified.
     * @throws TransformException if a coordinate transformation was required and failed.
     */
    public void transform(final DirectPosition position) throws TransformException {
        CoordinateReferenceSystem userCRS = position.getCoordinateReferenceSystem();
        if (userCRS == null) {
            userCRS = defaultCRS;
            if (userCRS == null) {
                setLocation(position);
                return;
            }
        }
        /*
         * A projection may be required. Checks if it is the same one than the one used
         * last time this method has been invoked. If the specified position uses a new
         * CRS, then gets the transformation and saves it in case the next call to this
         * method would uses again the same transformation.
         */
        if (forward==null || !CRS.equalsIgnoreMetadata(sourceCRS, userCRS)) {
            setSourceCRS(userCRS);
        }
        if (forward.transform(position, this) != this) {
            throw new AssertionError(forward); // Should never occurs.
        }
    }

    /**
     * Returns a new point with the same coordinates than this one, but transformed in the given
     * CRS. This method never returns {@code this}, so the returned point usually doesn't need to
     * be cloned.
     *
     * @param  crs The CRS for the position to be returned.
     * @return The same position than {@code this}, but transformed in the specified CRS.
     * @throws TransformException if a coordinate transformation was required and failed.
     *
     * @since 2.3
     */
    public DirectPosition inverseTransform(final CoordinateReferenceSystem crs)
            throws TransformException
    {
        if (inverse==null || !CRS.equalsIgnoreMetadata(sourceCRS, crs)) {
            ensureNonNull("crs", crs);
            setSourceCRS(crs);
            inverse = forward.inverse();
        }
        return inverse.transform(this, null);
    }

    /**
     * Returns a new point with the same coordinates than this one, but transformed in the
     * {@code sourceCRS} given at {@linkplain #TransformedDirectPosition(CoordinateReferenceSystem,
     * CoordinateReferenceSystem, Hints) construction time}. This method never returns {@code this},
     * so the returned point usually doesn't need to be cloned.
     *
     * @return The same position than {@code this}, but transformed in the source CRS.
     * @throws TransformException if a coordinate transformation was required and failed.
     *
     * @since 2.3
     */
    public DirectPosition inverseTransform() throws TransformException {
        if (defaultCRS != null) {
            return inverseTransform(defaultCRS);
        } else {
            return new GeneralDirectPosition(this);
        }
    }

    /**
     * Makes sure an argument is non-null.
     *
     * @param  name   Argument name.
     * @param  object User argument.
     * @throws InvalidParameterValueException if {@code object} is null.
     */
    private static void ensureNonNull(final String name, final Object object)
        throws IllegalArgumentException
    {
        if (object == null) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, name));
        }
    }
}
