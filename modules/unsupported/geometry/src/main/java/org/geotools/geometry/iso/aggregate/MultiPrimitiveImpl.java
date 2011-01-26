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
package org.geotools.geometry.iso.aggregate;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.coordinate.EnvelopeImpl;
import org.geotools.geometry.iso.io.GeometryToString;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


public class MultiPrimitiveImpl extends AggregateImpl implements MultiPrimitive {
    private static final long serialVersionUID = -8667095513075575773L;

    /**
	 * Creates a MultiPrimitive by a set of Primitives.
	 * @param crs
	 * @param primitives Set of Primitives which shall be contained by the MultiPrimitive
	 */
	public MultiPrimitiveImpl(CoordinateReferenceSystem crs, Set<? extends Primitive> primitives) {
		super(crs, primitives);
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getEnvelope()
	 */
	public Envelope getEnvelope() {
		EnvelopeImpl env = new EnvelopeImpl(new DirectPositionImpl( getCoordinateReferenceSystem(), (new double[] {Double.NaN, Double.NaN})) );
		Iterator<? extends Geometry> elementIter = this.elements.iterator();
		while (elementIter.hasNext()) {
			env.add((EnvelopeImpl)((Primitive)elementIter.next()).getEnvelope());
		}
		return env;		
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.root.Geometry#isSimple()
	 */
	public boolean isSimple() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.root.Geometry#getMaximalComplex()
	 */
	public Set<Complex> getMaximalComplex() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getRepresentativePoint()
	 */
	public DirectPosition getRepresentativePoint() {
		// Return the representative point of the first primitive in this aggregate
		Iterator<? extends Primitive> elementIter = getElements().iterator();
		return ((Geometry)elementIter.next()).getRepresentativePoint();
	}
	
	/**
	 * Overwrite toString method for WKT output
	 */
	public String toString() {
		return GeometryToString.getString(this);
	}

	/* (non-Javadoc)
     * @see org.geotools.geometry.featgeom.aggregate.MultiPrimitiveImpl#getElements()
     */
    @SuppressWarnings("unchecked")
	public Set<? extends Primitive> getElements() {
        return Collections.checkedSet( (Set<Primitive>) super.elements, Primitive.class );
    }
}
