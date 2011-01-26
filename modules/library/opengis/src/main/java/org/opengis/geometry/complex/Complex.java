/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.complex;

import java.util.Collection;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.primitive.Point;     // For javadoc
import org.opengis.geometry.primitive.Primitive; // For javadoc
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A collection of geometrically disjoint, simple {@linkplain Primitive primitives}. If a
 * {@linkplain Primitive primitive} (other than a {@linkplain Point point} is in a particular
 * {@code Complex}, then there exists a set of primitives of lower dimension in the same complex
 * that form the boundary of this primitive.
 * <p>
 * A geometric complex can be thought of as a set in two distinct ways. First, it is a finite set
 * of objects (via delegation to its elements member) and, second, it is an infinite set of point
 * values as a subtype of geometric object. The dual use of delegation and subtyping is to
 * disambiguate the two types of set interface. To determine if a {@linkplain Primitive primitive}
 * <var>P</var> is an element of a {@code Complex} <var>C</var>,
 * call: {@code C.element().contains(P)}.
 * <p>
 * The "{@linkplain #getElements elements}" attribute allows {@code Complex} to inherit the
 * behavior of {@link Set Set&lt;Primitive&gt;} without confusing the same sort of behavior
 * inherited from {@link org.opengis.geometry.TransfiniteSet TransfiniteSet&lt;DirectPosition&gt;}
 * inherited through {@link Geometry}. Complexes shall be used in application schemas where
 * the sharing of geometry is important, such as in the use of computational topology. In a
 * complex, primitives may be aggregated many-to-many into composites for use as attributes
 * of features.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 *
 * @todo Some associations are commented out for now.
 */
@UML(identifier="GM_Complex", specification=ISO_19107)
public interface Complex extends Geometry {
    /**
     * Returns {@code true} if and only if this {@code Complex} is maximal.
     * A complex is maximal if it is a subcomplex of no larger complex.
     *
     * @return {@code true} if this complex is maximal.
     */
    @UML(identifier="isMaximal", obligation=MANDATORY, specification=ISO_19107)
    boolean isMaximal();

    /**
     * Returns a superset of primitives that is also a complex.
     *
     * @return The supercomplexes, or an empty array if none.
     *
     * @todo Consider using a Collection return type instead.
     */
    @UML(identifier="superComplex", obligation=MANDATORY, specification=ISO_19107)
    Complex[] getSuperComplexes();

    /**
     * Returns a subset of the primitives of that complex
     * that is, in its own right, a geometric complex.
     *
     * @return The subcomplexes, or an empty array if none.
     *
     * @todo Consider using a Collection return type instead.
     */
    @UML(identifier="subComplex", obligation=MANDATORY, specification=ISO_19107)
    Complex[] getSubComplexes();

    /**
     * Returns the collection of primitives contained in this complex.
     *
     * @return The collection of primitives for this complex.
     */
    @UML(identifier="element", obligation=MANDATORY, specification=ISO_19107)
    Collection<? extends Primitive> getElements();

//    public org.opengis.topology.complex.TP_Complex topology[];
}
