/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry;

import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A possibly infinite set; restricted only to values. For example, the integers and the real
 * numbers are transfinite sets. This is actually the usual definition of set in mathematics,
 * but programming languages restrict the term set to mean finite set.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier="TransfiniteSet", specification=ISO_19107)
public interface TransfiniteSet {
    /**
     * Returns {@code true} if this {@code TransfiniteSet} contains another
     * {@code TransfiniteSet}. If the passed {@code TransfiniteSet} is a
     * {@linkplain org.opengis.geometry.primitive.Point point}, then this operation is the
     * equivalent of a set-element test for the {@linkplain DirectPosition direct position}
     * of that point within this {@code TransfiniteSet}.
     *
     * <blockquote><font size=2>
     * <strong>NOTE:</strong> {@code contains} is strictly a set theoretic containment,
     * and has no dimensionality constraint. In a {@linkplain org.opengis.geometry.complex.Complex
     * complex}, no {@linkplain org.opengis.geometry.primitive.Primitive primitive} will contain
     * another unless a dimension is skipped.
     * </font></blockquote>
     *
     * @param  pointSet The set to be checked for containment in this set.
     * @return {@code true} if this set contains all of the elements of the specified set.
     */
    boolean contains(TransfiniteSet pointSet);

    /**
     * Returns {@code true} if this {@code TransfiniteSet} contains a
     * single point given by a coordinate.
     *
     * @param  point The point to be checked for containment in this set.
     * @return {@code true} if this set contains the specified point.
     */
    boolean contains(DirectPosition point);

    /**
     * Returns {@code true} if this {@code TransfiniteSet} intersects another
     * {@code TransfiniteSet}. Withing a {@linkplain org.opengis.geometry.complex.Complex complex},
     * the {@linkplain org.opengis.geometry.primitive.Primitive primitives} do not intersect one another.
     * In general, topologically structured data uses shared geometric objects to
     * capture intersection information.
     *
     * <blockquote><font size=2>
     * <strong>NOTE:</strong> This intersect is strictly a set theoretic common containment of
     * {@linkplain DirectPosition direct positions}.
     * Two {@linkplain org.opengis.geometry.primitive.Curve curves} do not intersect if they share a common
     * end point because {@linkplain org.opengis.geometry.primitive.Primitive primitives} are considered to be
     * open (do not contain their boundary).
     * If two {@linkplain org.opengis.geometry.complex.CompositeCurve composite curves} share a common end point,
     * then they intersect because {@linkplain org.opengis.geometry.complex.Complex complexes} are considered to
     * be closed (contain their boundary).
     * </font></blockquote>
     *
     * @param  pointSet The set to be checked for intersection with this set.
     * @return {@code true} if this set intersects some of the elements of the specified set.
     */
    boolean intersects(TransfiniteSet pointSet);

    /**
     * Returns {@code true} if this {@code TransfiniteSet} is equal to another
     * {@code TransfiniteSet}. Two different instances of {@code TransfiniteSet}
     * are equal if they return the same boolean value for the operation
     * {@link #contains(DirectPosition) contains} for every tested {@linkplain DirectPosition
     * direct position} within the valid range of the coordinate reference system associated
     * to the object.
     *
     * <blockquote><font size=2>
     * <strong>NOTE:</strong> Since an infinite set of direct positions cannot be tested,
     * the internal implementation of equal must test for equivalence between two, possibly
     * quite different, representations. This test may be limited to the resolution of the
     * coordinate system or the accuracy of the data. Implementations may define a tolerance
     * that returns {@code true} if the two {@code TransfiniteSet} have the same
     * dimension and each direct position in this {@code TransfiniteSet} is within a
     * tolerance distance of a direct position in the passed {@code TransfiniteSet} and
     * vice versa.
     * </font></blockquote>
     *
     * @param pointSet The set to test for equality.
     * @return {@code true} if the two set are equals.
     */
    boolean equals(TransfiniteSet pointSet);

    /**
     * Returns the set theoretic union of this {@code TransfiniteSet} and the passed
     * {@code TransfiniteSet}.
     *
     * @param pointSet The second set.
     * @return The union of both sets.
     */
    TransfiniteSet union(TransfiniteSet pointSet);

    /**
     * Returns the set theoretic intersection of this {@code TransfiniteSet} and the passed
     * {@code TransfiniteSet}.
     *
     * @param pointSet The second set.
     * @return The intersection of both sets.
     */
    TransfiniteSet intersection(TransfiniteSet pointSet);

    /**
     * Returns the set theoretic difference of this {@code TransfiniteSet} and the passed
     * {@code TransfiniteSet}.
     *
     * @param pointSet The second set.
     * @return The difference between both sets.
     */
    TransfiniteSet difference(TransfiniteSet pointSet);

    /**
     * Returns the set theoretic symmetric difference of this {@code TransfiniteSet} and the
     * passed {@code TransfiniteSet}.
     *
     * @param pointSet The second set.
     * @return The symmetric difference between both sets.
     */
    TransfiniteSet symmetricDifference(TransfiniteSet pointSet);
}
