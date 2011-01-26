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
package org.geotools.geometry.iso.io.wkt;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geotools.geometry.iso.primitive.RingImpl;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.aggregate.MultiCurve;
import org.opengis.geometry.aggregate.MultiPoint;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.aggregate.MultiSurface;
import org.opengis.geometry.complex.CompositeCurve;
import org.opengis.geometry.complex.CompositePoint;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;

public class GeometryToWKTString {
	
	private boolean lineBreak = false;

	public GeometryToWKTString(boolean lineBreak) {
		this.lineBreak = lineBreak;
	}
	
	public String getString(Geometry geom) {

		String rString = "";
		if (this.lineBreak) {
			rString += "\n";
		}

		if (geom instanceof Curve) 
			rString += curveToString((Curve) geom);
		else if (geom instanceof Point)
			rString +=  pointToString((Point) geom);
		else if (geom instanceof Ring)
			rString += ringToString((Ring) geom);
		else if (geom instanceof SurfaceBoundary)
			rString += surfaceBoundaryToString((SurfaceBoundary) geom);
		else if (geom instanceof Surface)
			rString += surfaceToString((Surface) geom);
		else if (geom instanceof MultiPrimitive)
			rString += multiPrimitiveToString((MultiPrimitive) geom);
		else if (geom instanceof CompositePoint)
			rString += compositePointToString((CompositePoint) geom);
		else if (geom instanceof CompositeCurve)
			rString += compositeCurveToString((CompositeCurve) geom);
		else
			rString = "";
		
		return rString;
		
	}

	private String pointToString(Point c) {
		return "Point(" + pointCoordToString(c) + ")";
	}

	private String curveToString(Curve c) {
		return "Curve(" + curveCoordToString(c) + ")";
	}

	private String ringToString(Ring r) {
		return "Ring(" + ringCoordToString(r) + ")";
	}

	private String surfaceBoundaryToString(SurfaceBoundary sb) {
		return "SurfaceBoundary(" + surfaceBoundaryCoordToString(sb) + ")";
	}

	private String surfaceToString(Surface s) {
		return "Surface(" + surfaceBoundaryCoordToString((SurfaceBoundary)s.getBoundary()) + ")";
	}
	
	private String multiPrimitiveToString(MultiPrimitive mp) {
		if (mp instanceof MultiPoint) 
			return multiPointToString((MultiPoint) mp);
		else if (mp instanceof MultiCurve)
			return multiCurveToString((MultiCurve) mp);
		else if (mp instanceof MultiSurface)
			return multiSurfaceToString((MultiSurface) mp);
		else
			return "MultiPrimitive(" + this.multiPrimitiveCoordToString(mp) + ")";
	}

	private String multiPointToString(MultiPoint mp) {
		return "MultiPoint(" + this.multiPointCoordToString(mp) + ")";
	}

	private String multiCurveToString(MultiCurve mc) {
		return "MultiCurve(" + this.multiCurveCoordToString(mc) + ")";
	}

	private String multiSurfaceToString(MultiSurface ms) {
		return "MultiSurface(" + this.multiSurfaceCoordToString(ms) + ")";
	}

	private String compositePointToString(CompositePoint cp) {
		Point p = (Point) cp.getElements().iterator().next();
		return "CompositePoint(" + this.pointCoordToString(p) + ")";
	}

	private String compositeCurveToString(CompositeCurve cc) {
		return "CompositeCurve(" + compositeCurveCoordToString(cc) + ")";
	}
	
	/**
	 * 
	 * @param dp
	 * @return Format: "x1 y1 z1"
	 */
	private String directPositionToString(DirectPosition dp) {
		double coord[] = dp.getCoordinates();
		String str = Double.toString(coord[0]);
		for (int i = 1; i < coord.length; ++i) {
			str += " " + Double.toString(coord[i]);
		}
		return str;
	}
	

	private String lineStringCoordToStringWithoutFirstCoord(CurveSegment ls) {
		return pointArrayCoordToStringWithoutFirstCoord(ls.getSamplePoints());
	}

	
	private String pointArrayCoordToStringWithoutFirstCoord(PointArray pa) {
		String rString = "";
		List<Position> positions = pa.positions();
		if (positions.size() == 0)
			return "";
		for (int i = 1; i < positions.size(); i++) {
			if (i > 1) {
				rString += ", ";
			}
			rString += directPositionToString(positions.get(i).getPosition());
		}
		return rString;
	}
	
	private String curveCoordToString(Curve c) {
		String rString = "";
		List<? extends CurveSegment> segments = c.getSegments();
		rString += directPositionToString(c.getStartPoint());
		for (int i = 0; i < segments.size(); i++) {
			rString += ", ";
			rString += lineStringCoordToStringWithoutFirstCoord(segments.get(i));
		}
		return rString;
	}

	private String curveCoordToStringWithoutFirstCoord(Curve c) {
		String rString = "";
		for (CurveSegment segment : c.getSegments()) {
			rString += ", ";
			rString += lineStringCoordToStringWithoutFirstCoord(segment);
		}
		return rString;
	}
	
	private String ringCoordToString(Ring r) {
		Collection<? extends Primitive> orientableCurves = r.getGenerators();
		String rString = directPositionToString(((Curve) orientableCurves.iterator().next()).getStartPoint());
		for (Primitive p : orientableCurves) {
			rString += curveCoordToStringWithoutFirstCoord((Curve) p);
		}
		return rString;
	}
	
	private String compositeCurveCoordToString(CompositeCurve cc) {
		Collection<? extends Primitive> orientableCurves = cc.getGenerators();
		String rString = directPositionToString(((Curve) orientableCurves.iterator().next()).getStartPoint());
		for (Primitive p : orientableCurves) {
			rString += curveCoordToStringWithoutFirstCoord((Curve) p);
		}
		return rString;
	}
	
	private String surfaceBoundaryCoordToString(SurfaceBoundary sb) {
		String rString = "(";
		rString += ringCoordToString((Ring) sb.getExterior());
		rString += ")";
		List<Ring> interior = sb.getInteriors();
		if (interior.size() > 0) {
			for (int i = 0; i < interior.size(); i++) {
				rString += ", (";
				rString += ringCoordToString((Ring) interior.get(i));
				rString += ")";
			}
		}
		
		return rString;
	}
	
	private String multiPointCoordToString(MultiPoint mp) {
		Iterator mpIter = mp.getElements().iterator();
		String rString = this.pointCoordToString((Point)mpIter.next());
		while (mpIter.hasNext()) {
			rString += ", (";
			rString += this.pointCoordToString((Point)mpIter.next());
			rString += ")";
		}
		return rString;
	}

	private String multiCurveCoordToString(MultiCurve mc) {
		Iterator mpIter = mc.getElements().iterator();
		String rString = "(" + this.curveCoordToString((Curve)mpIter.next()) + ")";
		while (mpIter.hasNext()) {
			if (this.lineBreak) {
				rString += "\n\t";
			}
			rString += ", (";
			rString += this.curveCoordToString((Curve)mpIter.next());
			rString += ")";
		}
		return rString;
	}
	
	private String multiSurfaceCoordToString(MultiSurface mc) {
		Iterator mpIter = mc.getElements().iterator();
		String rString = "(" + this.surfaceBoundaryCoordToString((SurfaceBoundary) ((Surface)mpIter.next()).getBoundary()) + ")";
		while (mpIter.hasNext()) {
			if (this.lineBreak) {
				rString += "\n\t";
			}
			rString += ", ";
			rString += "(" + this.surfaceBoundaryCoordToString((SurfaceBoundary) ((Surface)mpIter.next()).getBoundary()) + ")";
		}
		return rString;
	}

	
	private String pointCoordToString(Point p) {
		return this.directPositionToString(p.getPosition());
	}

	private String multiPrimitiveCoordToString(MultiPrimitive mp) {
		Iterator<Primitive> primitives = (Iterator<Primitive>) mp.getElements().iterator();
		String rString = "";
		while (primitives.hasNext()) {
			Primitive p = primitives.next();
			if (p instanceof Point) 
				rString += "\n\t" + pointToString((Point) p);
			else if (p instanceof Curve)
				rString += "\n\t" + curveToString((Curve) p);
			else if (p instanceof Surface)
				rString += "\n\t" + surfaceToString((Surface) p);
			else
				rString += "\n[INVALID TYPE in MULTIPRIMITIVE]";
		}
		return rString;
	}



}
