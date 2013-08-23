/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.jtinv2.index;

import java.util.Iterator;
import java.util.List;

import org.geotools.jtinv2.main.TinBoundary;
import org.geotools.jtinv2.main.TinBreakline;
import org.geotools.jtinv2.main.TinFace;
import org.geotools.jtinv2.main.TinPoint;
import org.geotools.jtinv2.main.TriangulatedIrregularNetwork;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public interface TinIndex 
{	
	public abstract TinFace getTinFaceAtLocation(Coordinate argCoordinate, double argRange);
	
	public abstract boolean hasTinFaceAtLocation(Coordinate argCoordinate, double argRange);
	
	public abstract Iterator<TinFace> getTinFacesInEnvelope(Envelope argEnvelope);
	
	public abstract boolean hasTinFacesInEnvelope(Envelope argEnvelope);
	/**
	 * Returns an ImmutableTin that contains a copy of this tin containing all 
	 * the TinFaces who's envelope intersects the given envelope. The returned 
	 * TIN will have partial faces and possibly full faces that lie outside the
	 * given envelope.
	 * 
	 * @param envelope	The bounding box delinating which TinFaces should be
	 * 					included in the returned TIN.
	 * @return			an ImmutableTin that contains all the faces within the
	 * 					given envelope.
	 */
	public abstract TriangulatedIrregularNetwork subset(Envelope envelope);
	
	
	/**
	 * Returns a list of the TinFaces that intersect the given value along
	 * their z axis.
	 * 
	 * @param z		the height band that all returned triangles will intersect
	 * @return		a list of TinFaces that intersect the given height
	 */
	public abstract Iterator<TinFace> getTrianglesAtHeight(double argElevation);
	
	public abstract boolean hasTrianglesAtHeight();
	/**
	 * Returns a list of the TinFaces that lie within the given height range
	 * at some point
	 * 
	 * @param z1	one end of the height band that all returned triangles will intersect
	 * @param z2	the other end the height band that all returned triangles will intersect
	 * @return		a list of TinFaces that intersect the given height range
	 */
	public abstract Iterator<TinFace> getTrianglesWithinRange(double argBottomElevation, double argTopElevation);

	public abstract boolean hasTriangesWithinRange();
	
	public abstract Iterator<TinPoint> getTinPointsInEnvelope(Envelope argEnvelope);
	
	public boolean hasTinPointInEnvelope(Envelope argEnvelope);
	
	public abstract Iterator<TinBreakline> getTinBreaklinesInEnvelope(Envelope argEnvelope);
	
	public boolean hasTinBreaklinesInEnvelope(Envelope argEnvelope);
	
	public abstract Iterator<TinBoundary> getTinBoundariesInEnvelope(Envelope argEnvelope);

	public abstract boolean hasTinBoundariesInEnvelope(Envelope argEnvelope);
}
