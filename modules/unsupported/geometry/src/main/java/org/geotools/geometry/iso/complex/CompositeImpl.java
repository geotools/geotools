/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.complex;

import java.util.List;
import org.opengis.geometry.complex.Composite;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A geometric composite, Composite, shall be a geometric complex with an underlying core geometry
 * that is isomorphic to a primitive. Thus, a composite curve is a collection of curves whose
 * geometry interface could be satisfied by a single curve (albeit a much more complex one).
 * Composites are intended for use as attribute values in datasets in which the underlying geometry
 * has been decomposed, usually to expose its topological nature.
 *
 * @author Jackson Roehrig & Sanjay Jena
 * @param <T>
 */
public abstract class CompositeImpl<T> extends ComplexImpl implements Composite {

    /**
     * The type of geometry in a generator shall be completely determined by the dimension of the
     * composite object. The component curves and surfaces are oriented to allow assembly into the
     * composite in a properly organized manner. CompositePoint: generator.type = Point
     * CompositeCurve: generator.type = OrientableCurve CompositeSurface: generator.type =
     * OrientableSurface CompositeSolid: generator.type = Solid
     */

    /**
     * The association role Composition::generator shall be a homogeneous collection of Primitives
     * whose union would be the core geometry of the composite. The complex would include all
     * primitives in the generator and all primitives on the boundary of these primitives, and so
     * forth until Points are included. Thus the association role Composition::generator on
     * Composite is a subset of the association role Complex::element on Complex.
     *
     * <p>Composite::generator[1..n] : Primitive Is realised by the ArrayList of elements in the
     * super Class Complex The Generator elements will be passed through the super constructor
     */
    public CompositeImpl(List<? extends Primitive> generatorElements) {
        super(generatorElements);
    }

    /** Constructs a Composite by calling the super constructor Elements have to be added after */
    public CompositeImpl(CoordinateReferenceSystem crs) {
        super(crs);
    }

    /** @return the class */
    public abstract Class getGeneratorClass();
}
