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

import org.geotools.geometry.iso.primitive.RingImpl;
import org.geotools.geometry.iso.primitive.RingImplUnsafe;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.Location;
import org.geotools.geometry.iso.topograph2D.util.CoordinateArrays;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.primitive.Ring;

/**
 * Computes whether a point lies in the interior of an area {@link Geometry}.
 * The algorithm used is only guaranteed to return correct results for points
 * which are <b>not</b> on the boundary of the Geometry.
 *
 * @source $URL$
 */
public class SimplePointInAreaLocator {

	/**
	 * locate is the main location function. It handles both single-element and
	 * multi-element Geometries. The algorithm for multi-element Geometries is
	 * more complex, since it has to take into account the boundaryDetermination
	 * rule
	 * 
	 * @param p
	 * @param geom
	 * @return
	 */
	public static int locate(Coordinate p, GeometryImpl geom) {
		// TODO auskommentiert; checken!
		// if (geom.isEmpty())
		// return Location.EXTERIOR;

		if (containsPoint(p, geom))
			return Location.INTERIOR;
		return Location.EXTERIOR;
	}

	private static boolean containsPoint(Coordinate p, GeometryImpl geom) {

		if (geom instanceof SurfaceImpl) {
			return containsPointInPolygon(p, (SurfaceImpl) geom);
		}
		else if (geom instanceof MultiPrimitive) {
			Iterator geomi = ((MultiPrimitive) geom).getElements().iterator();
			while (geomi.hasNext()) {
				GeometryImpl g2 = (GeometryImpl) geomi.next();
				if (g2 != geom)
					if (containsPoint(p, g2))
						return true;
			}
		}
		return false;

		// OLD CODE:
		// if (geom instanceof Polygon) {
		// return containsPointInPolygon(p, (Polygon) geom);
		// } else if (geom instanceof GeometryCollection) {
		// Iterator geomi = new GeometryCollectionIterator(
		// (GeometryCollection) geom);
		// while (geomi.hasNext()) {
		// Geometry g2 = (Geometry) geomi.next();
		// if (g2 != geom)
		// if (containsPoint(p, g2))
		// return true;
		// }
		// }
		// return false;

	}

	public static boolean containsPointInPolygon(Coordinate p,
			SurfaceImpl aSurface) {

		// TODO auskommentiert; checken!
		// if (poly.isEmpty())
		// return false;

		List<Ring> rings = aSurface.getBoundaryRings();
		Ring shell = rings.get(0);

		// The point lies in the ring defined by the coordinatearray
		// representation of the exterior ring?
		// if not, return false
		if (!CGAlgorithms.isPointInRing(p, CoordinateArrays
				.toCoordinateArray(((RingImplUnsafe)shell).asDirectPositions()))) {
			return false;
		}

		// The point lies in the ring defined by the coordinatearray
		// representation of the exterior ring?
		for (int i = 1; i < rings.size(); i++) {
			RingImplUnsafe hole = (RingImplUnsafe) rings.get(i);
			// if so, return false
			if (CGAlgorithms.isPointInRing(p, CoordinateArrays
					.toCoordinateArray(hole.asDirectPositions()))) {
				return false;
			}
		}

		// the point lies inside the exterior ring, and outside the holes, so
		// its on the polygon
		return true;

		// OLD CODE:
		// if (poly.isEmpty())
		// return false;
		// LinearRing shell = (LinearRing) poly.getExteriorRing();
		// if (!CGAlgorithms.isPointInRing(p, shell.getCoordinates()))
		// return false;
		// // now test if the point lies in or on the holes
		// for (int i = 0; i < poly.getNumInteriorRing(); i++) {
		// LinearRing hole = (LinearRing) poly.getInteriorRingN(i);
		// if (CGAlgorithms.isPointInRing(p, hole.getCoordinates()))
		// return false;
		// }
		// return true;

	}

}
