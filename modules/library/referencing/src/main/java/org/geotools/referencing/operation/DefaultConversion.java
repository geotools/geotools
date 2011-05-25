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

import org.opengis.referencing.operation.*; // We use almost all of them.
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * An operation on coordinates that does not include any change of Datum. The best-known
 * example of a coordinate conversion is a map projection. The parameters describing
 * coordinate conversions are defined rather than empirically derived. Note that some
 * conversions have no parameters.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see DefaultTransformation
 */
public class DefaultConversion extends DefaultOperation implements Conversion {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -2148164324805562793L;

    /**
     * Constructs a new conversion with the same values than the specified one, together with the
     * specified source and target CRS. While the source conversion can be an arbitrary one, it is
     * typically a {@linkplain DefiningConversion defining conversion}.
     *
     * @param definition The defining conversion.
     * @param sourceCRS The source CRS.
     * @param targetCRS The target CRS.
     * @param transform Transform from positions in the {@linkplain #getSourceCRS source CRS}
     *                  to positions in the {@linkplain #getTargetCRS target CRS}.
     */
    public DefaultConversion(final Conversion               definition,
                             final CoordinateReferenceSystem sourceCRS,
                             final CoordinateReferenceSystem targetCRS,
                             final MathTransform             transform)
    {
        super(definition, sourceCRS, targetCRS, transform);
    }

    /**
     * Constructs a conversion from a set of properties. The properties given in argument
     * follow the same rules than for the {@link AbstractCoordinateOperation} constructor.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param sourceCRS The source CRS.
     * @param targetCRS The target CRS.
     * @param transform Transform from positions in the {@linkplain #getSourceCRS source CRS}
     *                  to positions in the {@linkplain #getTargetCRS target CRS}.
     * @param method    The operation method.
     */
    public DefaultConversion(final Map<String,?>             properties,
                             final CoordinateReferenceSystem sourceCRS,
                             final CoordinateReferenceSystem targetCRS,
                             final MathTransform             transform,
                             final OperationMethod           method)
    {
        super(properties, sourceCRS, targetCRS, transform, method);
    }

    /**
     * Invoked by the super-class constructor for checking argument validity. At the opposite of
     * {@link DefaultOperation}, a conversion accepts null {@code transform}, {@code sourceCRS}
     * and {@code targetCRS} providing that all of them are null together. If only one or two of
     * them is {@code null}, we will rely on the default validation which will throw an exception.
     */
    @Override
    void validate() throws IllegalArgumentException {
        if (transform != null || sourceCRS != null || targetCRS != null) {
            super.validate();
        }
    }

    /**
     * Returns a conversion from the specified {@linkplain DefiningConversion defining conversion}.
     * The new conversion will be a more specific type like a {@linkplain PlanarProjection planar},
     * {@linkplain CylindricalProjection cylindrical} or {@linkplain ConicProjection conic
     * projection}. This type is inferred from the {@code conversion} argument when possible.
     * However the inferred type is not always the most accurate one, so an optional
     * {@code typeHint} argument may be specified in order to get a more specific subclass.
     * This later argument is just a hint: it may be {@code null} and will be ignored if it
     * conflict with the automatically inferred type.
     *
     * @param definition The defining conversion.
     * @param sourceCRS  The source CRS.
     * @param targetCRS  The target CRS.
     * @param transform  Transform from positions in the {@linkplain #getSourceCRS source CRS}
     *                   to positions in the {@linkplain #getTargetCRS target CRS}.
     * @param typeHint   One of <code>{@linkplain PlanarProjection}.class</code>,
     *                   <code>{@linkplain CylindricalProjection}.class</code> or
     *                   <code>{@linkplain ConicProjection}.class</code>, or {@code null}.
     * @return The conversion of the given type if possible.
     *
     * @see DefaultOperation#create
     *
     * @since 2.4
     */
    public static Conversion create(final Conversion               definition,
                                    final CoordinateReferenceSystem sourceCRS,
                                    final CoordinateReferenceSystem targetCRS,
                                    final MathTransform             transform,
                                    final Class<? extends Conversion> typeHint)
    {
        Class<? extends CoordinateOperation> type = getType(definition);
        final OperationMethod method = definition.getMethod();
        if (method instanceof MathTransformProvider) {
            final Class<? extends Operation> candidate = ((MathTransformProvider) method).getOperationType();
            if (candidate != null) {
                if (type.isAssignableFrom(candidate)) {
                    type = candidate;
                }
            }
        }
        if (typeHint != null && type.isAssignableFrom(typeHint)) {
            type = typeHint;
        }
        if (ConicProjection.class.isAssignableFrom(type)) {
            return new DefaultConicProjection(definition, sourceCRS, targetCRS, transform);
        }
        if (CylindricalProjection.class.isAssignableFrom(type)) {
            return new DefaultCylindricalProjection(definition, sourceCRS, targetCRS, transform);
        }
        if (PlanarProjection.class.isAssignableFrom(type)) {
            return new DefaultPlanarProjection(definition, sourceCRS, targetCRS, transform);
        }
        if (Projection.class.isAssignableFrom(type)) {
            return new DefaultProjection(definition, sourceCRS, targetCRS, transform);
        }
        return new DefaultConversion(definition, sourceCRS, targetCRS, transform);
    }
}
