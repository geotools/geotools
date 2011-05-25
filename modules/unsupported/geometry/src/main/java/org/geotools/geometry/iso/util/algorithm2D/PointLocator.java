/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 *    (C) 2001-2006  Vivid Solutions
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
 */
package org.geotools.geometry.iso.util.algorithm2D;

import java.util.Iterator;
import java.util.List;

import org.geotools.geometry.iso.aggregate.MultiPrimitiveImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PrimitiveImpl;
import org.geotools.geometry.iso.primitive.RingImpl;
import org.geotools.geometry.iso.primitive.RingImplUnsafe;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.GeometryGraph;
import org.geotools.geometry.iso.topograph2D.Location;
import org.geotools.geometry.iso.topograph2D.util.CoordinateArrays;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.Ring;


/**
 * Computes the topological relationship ({@link Location}) of a single point
 * to a {@link Geometry}. The algorithm obeys the SFS Boundary Determination
 * Rule to determine whether the point lies on the boundary or not.
 * <p>
 * Notes:
 * <ul>
 * <li>{@link LinearRing}s do not enclose any area - points inside the ring
 * are still in the EXTERIOR of the ring.
 * </ul>
 * Instances of this class are not reentrant.
 * 
 *
 *
 * @source $URL$
 */
public class PointLocator {
	private boolean isIn; // true if the point lies in or on any Geometry

	// element

	private int numBoundaries; // the number of sub-elements whose boundaries

	// the point lies in

	public PointLocator() {
	}

	/**
	 * Convenience method to test a point for intersection with a Geometry
	 * 
	 * @param p
	 *            the coordinate to test
	 * @param geom
	 *            the Geometry to test
	 * @return <code>true</code> if the point is in the interior or boundary
	 *         of the Geometry
	 */
	public boolean intersects(Coordinate p, GeometryImpl geom) {
		return locate(p, geom) != Location.EXTERIOR;
	}

	/**
	 * Computes the topological relationship ({@link Location}) of a single
	 * point to a Geometry. It handles both single-element and multi-element
	 * Geometries. The algorithm for multi-part Geometries takes into account
	 * the SFS Boundary Determination Rule.
	 * 
	 * @return the {@link Location} of the point relative to the input Geometry
	 */
	public int locate(Coordinate p, GeometryImpl geom) {

		// TODO auskommentiert; checken!
		// if (geom.isEmpty())
		// return Location.EXTERIOR;

		if (geom instanceof CurveImpl) {
			return locate(p, (CurveImpl) geom);
		} else if (geom instanceof SurfaceImpl) {
			return locate(p, (SurfaceImpl) geom);
		}

		isIn = false;
		numBoundaries = 0;
		computeLocation(p, geom);
		if (GeometryGraph.isInBoundary(numBoundaries))
			return Location.BOUNDARY;
		if (numBoundaries > 0 || isIn)
			return Location.INTERIOR;
		return Location.EXTERIOR;
	}

	private void computeLocation(Coordinate p, GeometryImpl geom) {
		if (geom instanceof CurveImpl) {
			updateLocationInfo(locate(p, (CurveImpl) geom));
		} else if (geom instanceof SurfaceImpl) {
			updateLocationInfo(locate(p, (SurfaceImpl) geom));
		} else if (geom instanceof MultiPrimitiveImpl) {
			Iterator<? extends Primitive> iterator = ((MultiPrimitiveImpl)geom).getElements().iterator();
			while (iterator.hasNext()) {
				PrimitiveImpl prim = ((PrimitiveImpl)iterator.next());
				updateLocationInfo(locate(p, prim));
			}
		}
		
		// else if (geom instanceof MultiLineString) {
		// MultiLineString ml = (MultiLineString) geom;
		// for (int i = 0; i < ml.getNumGeometries(); i++) {
		// LineString l = (LineString) ml.getGeometryN(i);
		// updateLocationInfo(locate(p, l));
		// }
		// } else if (geom instanceof MultiPolygon) {
		// MultiPolygon mpoly = (MultiPolygon) geom;
		// for (int i = 0; i < mpoly.getNumGeometries(); i++) {
		// Polygon poly = (Polygon) mpoly.getGeometryN(i);
		// updateLocationInfo(locate(p, poly));
		// }
		// } else if (geom instanceof GeometryCollection) {
		// Iterator geomi = new GeometryCollectionIterator(
		// (GeometryCollection) geom);
		// while (geomi.hasNext()) {
		// GeometryImpl g2 = (GeometryImpl) geomi.next();
		// if (g2 != geom)
		// computeLocation(p, g2);
		// }
		// }
	}

	private void updateLocationInfo(int loc) {
		if (loc == Location.INTERIOR)
			isIn = true;
		if (loc == Location.BOUNDARY)
			numBoundaries++;
	}

	private int locate(Coordinate p, CurveImpl curve) {
		Coordinate[] pt = CoordinateArrays.toCoordinateArray(curve
				.asDirectPositions());

		// Annahme: curve.isClosed() = curve.isCycle() ??
		// if (!curve.isClosed()) {
		if (!curve.getStartPoint().equals(curve.getEndPoint())) {
			if (p.equals(pt[0]) || p.equals(pt[pt.length - 1])) {
				return Location.BOUNDARY;
			}
		}
		if (CGAlgorithms.isOnLine(p, pt))
			return Location.INTERIOR;
		return Location.EXTERIOR;
	}

	private int locateInPolygonRing(Coordinate p, Ring ring) {
		// can this test be folded into isPointInRing ?
		Coordinate[] coord = CoordinateArrays.toCoordinateArray(((RingImplUnsafe)ring)
				.asDirectPositions());
		if (CGAlgorithms.isOnLine(p, coord)) {
			return Location.BOUNDARY;
		}
		if (CGAlgorithms.isPointInRing(p, coord))
			return Location.INTERIOR;
		return Location.EXTERIOR;
	}

	private int locate(Coordinate p, SurfaceImpl aSurface) {

		// if (poly.isEmpty())
		// return Location.EXTERIOR;
		List<Ring> rings = aSurface.getBoundaryRings();
		Ring shell = rings.get(0);

		int shellLoc = locateInPolygonRing(p, shell);
		if (shellLoc == Location.EXTERIOR)
			return Location.EXTERIOR;
		if (shellLoc == Location.BOUNDARY)
			return Location.BOUNDARY;
		// now test if the point lies in or on the holes
		for (int i = 1; i < rings.size(); i++) {
			Ring hole = rings.get(i);
			int holeLoc = locateInPolygonRing(p, hole);
			if (holeLoc == Location.INTERIOR)
				return Location.EXTERIOR;
			if (holeLoc == Location.BOUNDARY)
				return Location.BOUNDARY;
		}
		return Location.INTERIOR;

		// OLD CODE:
		// if (poly.isEmpty())
		// return Location.EXTERIOR;
		// LinearRing shell = (LinearRing) poly.getExteriorRing();
		//
		// int shellLoc = locateInPolygonRing(p, shell);
		// if (shellLoc == Location.EXTERIOR)
		// return Location.EXTERIOR;
		// if (shellLoc == Location.BOUNDARY)
		// return Location.BOUNDARY;
		// // now test if the point lies in or on the holes
		// for (int i = 0; i < poly.getNumInteriorRing(); i++) {
		// LinearRing hole = (LinearRing) poly.getInteriorRingN(i);
		// int holeLoc = locateInPolygonRing(p, hole);
		// if (holeLoc == Location.INTERIOR)
		// return Location.EXTERIOR;
		// if (holeLoc == Location.BOUNDARY)
		// return Location.BOUNDARY;
		// }
		// return Location.INTERIOR;

	}
}
