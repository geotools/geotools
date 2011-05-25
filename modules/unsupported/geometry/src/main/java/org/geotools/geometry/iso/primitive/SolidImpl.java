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
package org.geotools.geometry.iso.primitive;

import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.coordinate.EnvelopeImpl;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.primitive.OrientablePrimitive;
import org.opengis.geometry.primitive.Solid;
import org.opengis.geometry.primitive.SolidBoundary;

/**
 * Solid , a subclass of Primitive, is the basis for 3-dimensional geometry. The
 * globelExtent of a solid is defined by the boundary surfaces.
 * 
 * @author Jackson Roehrig & Sanjay Jena
 * 
 *
 *
 * @source $URL$
 */
public class SolidImpl extends PrimitiveImpl implements Solid {
    private static final long serialVersionUID = 9203406429631890989L;
    
    /**
	 * 
	 */
	protected Envelope envelope;

	/**
	 * Since this standard is limited to 3-dimensional coordinate reference
	 * systems, any solid is definable by its boundary. The default constructor
	 * for a Solid is from a properly structured set of Shells organized as a
	 * SolidBoundary. Solid::Solid(boundary : SolidBoundary) : Solid
	 * @param boundary
	 */
	public SolidImpl(SolidBoundary boundary) {
		super(boundary.getCoordinateReferenceSystem(), null, null, null);
		this.envelope = (EnvelopeImpl) boundary.getEnvelope();
	}

	/**
	 * The operation "area" shall return the sum of the surface areas of all of
	 * the boundary components of a solid.
	 * 
	 * Solid::area() : Area
	 * 
	 * The class Set<Surface> has a "column operation" called "area" that
	 * accumulates the area of the components of the set. Using this, it can be
	 * said that for a Solid; Solid: area() = boundary().area()
	 * 
	 * @return Area
	 */
	public double area() {
		return 0.0;
	}

	/**
	 * The operation "volume" shall return the volume of this Solid. This is the
	 * volume interior to the exterior boundary shell minus the sum of the
	 * volumes interior to any interior boundary shell.
	 * 
	 * Solid::volume() : Volume
	 * 
	 * @return Volume
	 */
	public double volume() {
		return 0.0;
	}

	/**
	 * The operation "boundary" specializes the boundary operation defined at
	 * Object and at Primitive with the appropriate return type. It shall return
	 * a sequence of sets of Surfaces that limit the globelExtent of this Solid.
	 * These surfaces shall be organized into one set of surfaces for each
	 * boundary component of the Solid. Each of these shells shall be a cycle
	 * (closed composite surface without boundary).
	 * 
	 * Solid::boundary() : SolidBoundary
	 * 
	 * NOTE The exterior shell of a solid is defined only because the embedding
	 * coordinate space is always a 3D Euclidean one. In general, a solid in a
	 * bounded 3-dimensional manifold has no distinguished exterior boundary. In
	 * cases where "exterior" boundary is not well defined, all the shells of
	 * the SolidBoundary shall be listed as "interior". The OrientableSurfaces
	 * that bound a solid shall be oriented outward  that is, the "top" of each
	 * Surface as defined by its orientation shall face away from the interior
	 * of the solid. Each Shell, when viewed as a composite surface, shall be a
	 * cycle (see 6.2.2.6).
	 * 
	 * @return the solid boundary
	 */
	public SolidBoundary getBoundary() {
		// TODO Auto-generated method stub
		return null;
	}

	/***************************************************************************
	 * ************************************************************** * Method
	 * implementation of Interface Object **
	 * **************************************************************
	 **************************************************************************/

	/**
	 * The method <code>dimension</code> returns the inherent dimension of
	 * this Object, which is less than or equal to the coordinate dimension. The
	 * dimension of a collection of geometric objects is the largest dimension
	 * of any of its pieces. Points are 0-dimensional, curves are 1-dimensional,
	 * surfaces are 2-dimensional, and solids are 3-dimensional. Locally, the
	 * dimension of a geometric object at a point is the dimension of a local
	 * neighborhood of the point - that is the dimension of any coordinate
	 * neighborhood of the point. Dimension is unambiguously defined only for
	 * DirectPositions interior to this Object. If the passed DirectPosition2D
	 * is NULL, then the method returns the largest possible dimension for any
	 * DirectPosition2D in this Object.
	 * 
	 * @param point
	 *            a <code>DirectPosition2D</code> value
	 * @return an <code>int</code> value
	 */
	public int dimension(@SuppressWarnings("unused")
	final DirectPositionImpl point) {
		return 3;
	}

	@Override
	public SolidImpl clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	public OrientablePrimitive[] getProxy() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSimple() {
		// TODO Auto-generated method stub
		return false;
	}

	public double getArea() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getVolume() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getDimension(DirectPosition point) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Envelope getEnvelope() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectPosition getRepresentativePoint() {
		// TODO Auto-generated method stub
		return null;
	}

}
