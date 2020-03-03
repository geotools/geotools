/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/geometry/LineStringImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry.geometry;

// J2SE direct dependencies

import java.util.List;
import org.geotools.geometry.jts.spatialschema.geometry.DirectPositionImpl;
import org.geotools.geometry.jts.spatialschema.geometry.JTSGeometry;
import org.geotools.geometry.jts.spatialschema.geometry.JTSUtils;
import org.geotools.geometry.jts.spatialschema.geometry.primitive.CurveBoundaryImpl;
import org.geotools.geometry.jts.spatialschema.geometry.primitive.PointImpl;
import org.locationtech.jts.geom.Geometry;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.coordinate.ParamForPoint;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.CurveBoundary;
import org.opengis.geometry.primitive.CurveInterpolation;
import org.opengis.geometry.primitive.CurveSegment;

/**
 * The {@code LineStringImpl} class implements the {@link LineString} interface.
 *
 * @author SYS Technologies
 * @author crossley
 * @version $Revision $
 */
public class LineStringImpl extends GenericCurveImpl implements LineString, JTSGeometry {

    /** Points comprising this geometry. */
    private PointArray controlPoints;

    // *************************************************************************
    //  Constructors
    // *************************************************************************

    /** Creates a new {@code LineStringImpl}. */
    public LineStringImpl() {
        controlPoints = new PointArrayImpl();
        ((PointArrayImpl) controlPoints).setJTSParent(this);
    }

    // *************************************************************************
    //  implement the *** interface
    // *************************************************************************

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.LineString#getControlPoints()
     */
    public PointArray getControlPoints() {
        return controlPoints;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.LineString#asLineSegments()
     */
    public List asLineSegments() {
        return null;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.primitive.CurveSegment#getBoundary()
     */
    public CurveBoundary getBoundary() {
        return new CurveBoundaryImpl(
                null, new PointImpl(getStartPoint()), new PointImpl(getEndPoint()));
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.primitive.CurveSegment#getCurve()
     */
    public Curve getCurve() {
        if (parent instanceof Curve) return (Curve) parent;
        else return null;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.primitive.CurveSegment#getInterpolation()
     */
    public CurveInterpolation getInterpolation() {
        return CurveInterpolation.LINEAR;
    }

    /**
     * A line string doesn't have any continuous derivatives since the derivative has dicontinuities
     * at the vertices.
     */
    public int getNumDerivativeInterior() {
        return 0;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.primitive.CurveSegment#getNumDerivativesAtEnd()
     */
    public int getNumDerivativesAtEnd() {
        return Integer.MAX_VALUE;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.primitive.CurveSegment#getNumDerivativesAtStart()
     */
    public int getNumDerivativesAtStart() {
        return Integer.MAX_VALUE;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.primitive.CurveSegment#getSamplePoints()
     */
    public PointArray getSamplePoints() {
        return controlPoints;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.primitive.CurveSegment#reverse()
     */
    public CurveSegment reverse() {
        LineStringImpl result = new LineStringImpl();
        PointArray pa = result.getSamplePoints();
        int n = controlPoints.size();
        for (int i = n - 1; i >= 0; i--) {
            pa.add(new DirectPositionImpl(controlPoints.get(i).getDirectPosition()));
        }
        return result;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.GenericCurve#getStartPoint()
     */
    public DirectPosition getStartPoint() {
        return (DirectPosition) controlPoints.get(0);
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.GenericCurve#getEndPoint()
     */
    public DirectPosition getEndPoint() {
        return (DirectPosition) controlPoints.get(controlPoints.size() - 1);
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.GenericCurve#getTangent(double)
     */
    public double[] getTangent(final double s) {
        return null;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.GenericCurve#getStartParam()
     */
    public double getStartParam() {
        return 0;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.GenericCurve#getEndParam()
     */
    public double getEndParam() {
        return 1;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.GenericCurve#getStartConstructiveParam()
     */
    public double getStartConstructiveParam() {
        return 0;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.GenericCurve#getEndConstructiveParam()
     */
    public double getEndConstructiveParam() {
        return 1;
    }

    /** Not implemented. Returns null. */
    /*public DirectPosition getConstructiveParam(double cp) {
        return null;
    }*/

    /** Not implemented. Returns null. */
    /*public DirectPosition getParam(double s) {
        return null;
    }*/

    /**
     * @inheritDoc
     * @see
     *     org.opengis.geometry.coordinate.GenericCurve#getParamForPoint(org.opengis.geometry.coordinate.DirectPosition)
     */
    public ParamForPoint getParamForPoint(final DirectPosition p) {
        return null;
    }

    /**
     * @inheritDoc
     * @see
     *     org.opengis.geometry.coordinate.GenericCurve#length(org.opengis.geometry.coordinate.Position,
     *     org.opengis.geometry.coordinate.Position)
     */
    public double length(final Position point1, final Position point2) {
        return 0;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.GenericCurve#length(double, double)
     */
    public double length(final double cparam1, final double cparam2) {
        return 0;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.GenericCurve#asLineString(double, double)
     */
    public LineString asLineString(final double maxSpacing, final double maxOffset) {
        return null;
    }

    /** @see com.polexis.lite.spatialschema.geometry.geometry.GenericCurveImpl#computeJTSPeer() */
    protected Geometry computeJTSPeer() {
        int n = controlPoints.size();
        org.locationtech.jts.geom.Coordinate[] coords = new org.locationtech.jts.geom.Coordinate[n];
        for (int i = 0; i < n; i++) {
            coords[i] = JTSUtils.directPositionToCoordinate((DirectPosition) controlPoints.get(i));
        }
        return JTSUtils.GEOMETRY_FACTORY.createLineString(coords);
    }

    /**
     * We'd like to return "1", but the first derivative is undefined at the corners. The subclass,
     * LineSegment, can override this to return 1.
     */
    public int getNumDerivativesInterior() {
        return 0;
    }

    /** @see org.opengis.geometry.coordinate.GenericCurve#forConstructiveParam(double) */
    public DirectPosition forConstructiveParam(double cp) {
        return null;
    }

    /** @see org.opengis.geometry.coordinate.GenericCurve#forParam(double) */
    public DirectPosition forParam(double s) {
        return null;
    }
}
