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
import org.geotools.geometry.iso.coordinate.LineStringImpl;
import org.geotools.geometry.iso.coordinate.PositionImpl;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.complex.CompositeCurve;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.CurveBoundary;
import org.opengis.geometry.primitive.OrientableCurve;

/**
 * @author roehrig
 *     <p>TODO To change the template for this generated type comment go to Window - Preferences -
 *     Java - Code Style - Code Templates
 */
public class OrientableCurveProxy extends OrientablePrimitiveProxy implements OrientableCurve {

    /** @param curve */
    protected OrientableCurveProxy(CurveImpl curve) {
        super(curve);
    }

    private CurveImpl curve() {
        return (CurveImpl) getPrimitive();
    }

    public Curve getPrimitive() {
        return (Curve) super.getPrimitive();
    }

    public CurveBoundary getBoundary() {
        return (CurveBoundary) super.getBoundary();
    }

    /** @return */
    public DirectPosition getStartPoint() {
        return this.curve().getEndPoint();
    }

    /** @return */
    public DirectPosition getEndPoint() {
        return this.curve().getStartPoint();
    }

    /** */
    public double[] getTangent(double distance) {
        // TODO Auto-generated method stub
        return null;
    }

    public double getStartParam() {
        return this.curve().getEndParam();
    }

    public double getEndParam() {
        return this.curve().getStartParam();
    }

    public Object[] paramForPoint(DirectPositionImpl p) {
        // TODO Auto-generated method stub
        return null;
    }

    public DirectPositionImpl param(double distance) {
        // TODO Auto-generated method stub
        return null;
    }

    public double startConstrParam() {
        return this.curve().getEndConstructiveParam();
    }

    public double endConstrParam() {
        return this.curve().getStartConstructiveParam();
    }

    public DirectPositionImpl constrParam(double cp) {
        // TODO Auto-generated method stub
        return null;
    }

    public double length(PositionImpl point1, PositionImpl point2) {
        return this.curve().length(point1, point2);
    }

    public double length(double par1, double par2) {
        return this.curve().length(par1, par2);
    }

    public double length() {
        return this.curve().length();
    }

    public LineStringImpl asLineString(double spacing, double offset) {
        return this.curve().asLineString(spacing, offset);
    }

    public LineStringImpl asLineString() {
        return this.curve().asLineString();
    }

    public CompositeCurve getComposite() {
        // TODO Auto-generated method stub
        return null;
    }
}
