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
package org.geotools.referencing.crs;

import java.util.Collections;
import java.util.Map;

import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.DerivedCRS;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.geometry.MismatchedDimensionException;

import org.geotools.referencing.operation.DefiningConversion;
import org.geotools.referencing.operation.DefaultOperationMethod;


/**
 * A coordinate reference system that is defined by its coordinate conversion from another
 * coordinate reference system but is not a projected coordinate reference system. This
 * category includes coordinate reference systems derived from a projected coordinate
 * reference system.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class DefaultDerivedCRS extends AbstractDerivedCRS implements DerivedCRS {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -8149602276542469876L;

    /**
     * Constructs a new derived CRS with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @param crs The coordinate reference system to copy.
     *
     * @since 2.2
     */
    public DefaultDerivedCRS(final DerivedCRS crs) {
        super(crs);
    }

    /**
     * Constructs a derived CRS from a name. A {@linkplain DefaultOperationMethod default
     * operation method} is inferred from the {@linkplain MathTransform math transform}. This
     * is a convenience constructor that is not garanteed to work reliably for non-GeoTools
     * implementations. Use the constructor expecting a {@linkplain DefiningConversion
     * defining conversion} for more determinist result.
     *
     * @param  name The name.
     * @param  base Coordinate reference system to base the derived CRS on.
     * @param  baseToDerived The transform from the base CRS to returned CRS.
     * @param  derivedCS The coordinate system for the derived CRS. The number
     *         of axes must match the target dimension of the transform
     *         {@code baseToDerived}.
     * @throws MismatchedDimensionException if the source and target dimension of
     *         {@code baseToDeviced} don't match the dimension of {@code base}
     *         and {@code derivedCS} respectively.
     *
     * @since 2.5
     */
    public DefaultDerivedCRS(final String                    name,
                             final CoordinateReferenceSystem base,
                             final MathTransform    baseToDerived,
                             final CoordinateSystem     derivedCS)
            throws MismatchedDimensionException
    {
        this(Collections.singletonMap(NAME_KEY, name), base, baseToDerived, derivedCS);
    }

    /**
     * Constructs a derived CRS from a set of properties. A {@linkplain DefaultOperationMethod
     * default operation method} is inferred from the {@linkplain MathTransform math transform}.
     * This is a convenience constructor that is not garanteed to work reliably for non-GeoTools
     * implementations. Use the constructor expecting a {@linkplain DefiningConversion defining
     * conversion} for more determinist result.
     * <p>
     * The properties are given unchanged
     * to the {@linkplain AbstractDerivedCRS#AbstractDerivedCRS(Map, CoordinateReferenceSystem,
     * MathTransform, CoordinateSystem) super-class constructor}.
     *
     * @param  properties Name and other properties to give to the new derived CRS object and to
     *         the underlying {@linkplain org.geotools.referencing.operation.DefaultConversion
     *         conversion}.
     * @param  base Coordinate reference system to base the derived CRS on.
     * @param  baseToDerived The transform from the base CRS to returned CRS.
     * @param  derivedCS The coordinate system for the derived CRS. The number
     *         of axes must match the target dimension of the transform
     *         {@code baseToDerived}.
     * @throws MismatchedDimensionException if the source and target dimension of
     *         {@code baseToDeviced} don't match the dimension of {@code base}
     *         and {@code derivedCS} respectively.
     *
     * @since 2.5
     */
    public DefaultDerivedCRS(final Map<String,?>       properties,
                             final CoordinateReferenceSystem base,
                             final MathTransform    baseToDerived,
                             final CoordinateSystem     derivedCS)
            throws MismatchedDimensionException
    {
        super(properties, base, baseToDerived, derivedCS);
    }

    /**
     * Constructs a derived CRS from a set of properties. The properties are given unchanged
     * to the {@linkplain AbstractDerivedCRS#AbstractDerivedCRS(Map, OperationMethod,
     * CoordinateReferenceSystem, MathTransform, CoordinateSystem) super-class constructor}.
     *
     * @param  properties Name and other properties to give to the new derived CRS object and to
     *         the underlying {@linkplain org.geotools.referencing.operation.DefaultConversion
     *         conversion}.
     * @param  method A description of the {@linkplain Conversion#getMethod method for the
     *         conversion}.
     * @param  base Coordinate reference system to base the derived CRS on.
     * @param  baseToDerived The transform from the base CRS to returned CRS.
     * @param  derivedCS The coordinate system for the derived CRS. The number
     *         of axes must match the target dimension of the transform
     *         {@code baseToDerived}.
     * @throws MismatchedDimensionException if the source and target dimension of
     *         {@code baseToDeviced} don't match the dimension of {@code base}
     *         and {@code derivedCS} respectively.
     *
     * @deprecated Create explicitly a {@link DefiningConversion} instead.
     */
    public DefaultDerivedCRS(final Map<String,?>       properties,
                             final OperationMethod         method,
                             final CoordinateReferenceSystem base,
                             final MathTransform    baseToDerived,
                             final CoordinateSystem     derivedCS)
            throws MismatchedDimensionException
    {
        super(properties, method, base, baseToDerived, derivedCS);
    }

    /**
     * Constructs a derived CRS from a {@linkplain DefiningConversion defining conversion}. The
     * properties are given unchanged to the {@linkplain AbstractDerivedCRS#AbstractDerivedCRS(Map,
     * Conversion, CoordinateReferenceSystem, MathTransform, CoordinateSystem) super-class constructor}.
     *
     * @param  properties Name and other properties to give to the new derived CRS object.
     * @param  conversionFromBase The {@linkplain DefiningConversion defining conversion}.
     * @param  base Coordinate reference system to base the derived CRS on.
     * @param  baseToDerived The transform from the base CRS to returned CRS.
     * @param  derivedCS The coordinate system for the derived CRS. The number
     *         of axes must match the target dimension of the transform
     *         {@code baseToDerived}.
     * @throws MismatchedDimensionException if the source and target dimension of
     *         {@code baseToDerived} don't match the dimension of {@code base}
     *         and {@code derivedCS} respectively.
     */
    public DefaultDerivedCRS(final Map<String,?>       properties,
                             final Conversion  conversionFromBase,
                             final CoordinateReferenceSystem base,
                             final MathTransform    baseToDerived,
                             final CoordinateSystem     derivedCS)
            throws MismatchedDimensionException
    {
        super(properties, conversionFromBase, base, baseToDerived, derivedCS);
    }

    /**
     * Returns a hash value for this derived CRS.
     *
     * @return The hash code value. This value doesn't need to be the same
     *         in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        return (int)serialVersionUID ^ super.hashCode();
    }
}
