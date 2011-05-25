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

import java.util.List;

import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.coordinate.LineStringImpl;
import org.geotools.geometry.iso.coordinate.PositionImpl;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.ParamForPoint;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.OrientableCurve;

/**
 * @author roehrig
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 *
 *
 * @source $URL$
 */
public class CurveProxy extends OrientableCurveProxy implements Curve {

	/**
	 * @param curve
	 */
	protected CurveProxy(CurveImpl curve) {
		super(curve);
	}

	private CurveImpl proxy() {
		return (CurveImpl) this.getPrimitive();
	}

    public OrientableCurve[] getProxy() {
        return (OrientableCurve[]) super.getProxy();
    }

	public DirectPosition getStartPoint() {
		return this.proxy().getEndPoint();
	}

	public DirectPosition getEndPoint() {
		return this.proxy().getStartPoint();
	}

	public double[] getTangent(double distance) {
		return (this.proxy().getTangent(this.proxy().getEndParam() - distance));
	}

	public double getStartParam() {
		return this.proxy().getEndParam();
	}

	public double getEndParam() {
		return this.proxy().getStartParam();
	}

	public ParamForPoint getParamForPoint(DirectPosition p) {
		// TODO Auto-generated method stub
		return null;
	}

	public DirectPositionImpl forParam(double distance) {
		// TODO Auto-generated method stub
		return null;
	}

	public double getStartConstructiveParam() {
		return 0.0;
	}

	public double getEndConstructiveParam() {
		return 1.0;
	}

	public DirectPositionImpl constrParam(double cp) {
		// TODO Auto-generated method stub
		return null;
	}

	public double length(PositionImpl point1, PositionImpl point2) {
		return this.proxy().length(point1, point2);
	}

	public double length(double par1, double par2) {
		return this.proxy().length(par1, par2);
	}

	public double length() {
		return this.proxy().length();
	}

	public LineStringImpl asLineString() {
		/* Return reversed LineString representation of mate */
		return (LineStringImpl) this.proxy().asLineString().reverse();
	}

	public LineStringImpl asLineString(double spacing, double offset) {
		/* Return reversed LineString representation of mate */
		return (LineStringImpl) this.proxy().asLineString(spacing, offset)
				.reverse();
	}

	public List<CurveSegment> getSegments() {

		// TODO Auto-generated method stub
		return null;
	}

	public DirectPosition forConstructiveParam(double cp) {
		// TODO Auto-generated method stub
		return null;
	}

	public double length(Position point1, Position point2) {
		// TODO Auto-generated method stub
		return 0;
	}
}
