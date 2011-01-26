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
package org.geotools.geometry.iso.util.interpolation;

//import org.wrm.gis.iso19107.coordinate.GM_CurveInterpolation;

//import org.wrm.fgeo.coordinate.DirectPosition2D;
//import org.wrm.fgeo.coordinate.GM_PointArray;
//import org.wrm.fgeo.coordinate.GM_Position;
//import org.wrm.fgeo.coordinate.GM_DirectPosition;
//import org.wrm.fgeo.operators.GM_CurveSegment;

/**
 * @author roehrig
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 * @source $URL$
 */
public class ITP_Linear { //implements GM_CurveInterpolation { 

//	private static GM_DirectPosition interpolateCurveSegment(
//		GM_CurveSegment cseg,
//		double par) {
//			GM_DirectPosition p0 = cseg.startPoint();
//			GM_DirectPosition p1 = cseg.endPoint();
//			double par1 = 1.0 - par;
//		return new DirectPosition2D(p0.x * par1 + p1.x * par, p0.y * par1 + p1.y * par );
//	}
//
//	public DirectPosition2D getDirectPositionFromParam(
//		GM_CurveSegment cseg,
//		double distance) {
//			GM_PointArray pa = cseg.samplePoint();
//			GM_Position[] position = pa.getPositions();
//			double startParam = cseg.startParam();
//			if (startParam > distance) {
//				return null;
//			}
//			for (int i=0; i<position.length-1; ++i) {
//				DirectPosition2D p0 = position[i  ].getDirectPosition();
//				DirectPosition2D p1 = position[i+i].getDirectPosition();
//				double length = p0.distance(p1);
//				if ((startParam+length) >= distance) {
//					double a = (distance - startParam) / length;
//					double b = 1.0 - a;
//					return new DirectPosition2D(p0.x*b + p1.x*a, p0.y*b + p1.y*a);
//				}
//			}
//			return null;
//	}
//
//	public DirectPosition2D getDirectPositionFromConstrParam(
//		GM_CurveSegment cseg,
//		double cp) {
//		return getDirectPositionFromParam(cseg, cp * cseg.length());
//	}

}
