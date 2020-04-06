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
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/geometry/GeometryFactoryImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry.geometry;

// J2SE direct dependencies

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.geometry.jts.spatialschema.geometry.DirectPositionImpl;
import org.geotools.geometry.jts.spatialschema.geometry.EnvelopeImpl;
import org.geotools.geometry.jts.spatialschema.geometry.primitive.PolyhedralSurfaceImpl;
import org.geotools.geometry.jts.spatialschema.geometry.primitive.SurfaceBoundaryImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.Hints;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.coordinate.Arc;
import org.opengis.geometry.coordinate.ArcByBulge;
import org.opengis.geometry.coordinate.ArcString;
import org.opengis.geometry.coordinate.ArcStringByBulge;
import org.opengis.geometry.coordinate.BSplineCurve;
import org.opengis.geometry.coordinate.BSplineSurface;
import org.opengis.geometry.coordinate.Geodesic;
import org.opengis.geometry.coordinate.GeodesicString;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.KnotType;
import org.opengis.geometry.coordinate.LineSegment;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Polygon;
import org.opengis.geometry.coordinate.PolyhedralSurface;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.coordinate.Tin;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The {@code GeometryFactoryImpl} class/interface...
 *
 * @author SYS Technologies
 * @author crossley
 * @version $Revision $
 */
public class JTSGeometryFactory implements GeometryFactory {

    // *************************************************************************
    //  Fields
    // *************************************************************************

    /** Comment for {@code crs}. */
    private CoordinateReferenceSystem crs;

    private final Map usedHints = new LinkedHashMap();

    // *************************************************************************
    //  Constructors
    // *************************************************************************

    /** No argument constructor for FactorySPI */
    public JTSGeometryFactory() {
        this(DefaultGeographicCRS.WGS84);
    }
    /** Hints constructor for FactoryRegistry */
    public JTSGeometryFactory(Hints hints) {
        this((CoordinateReferenceSystem) hints.get(Hints.CRS));
    }
    /** Direct constructor for test cases */
    public JTSGeometryFactory(CoordinateReferenceSystem crs) {
        this.crs = crs;
        usedHints.put(Hints.CRS, crs);
    }

    public Map getImplementationHints() {
        return usedHints;
    }

    // *************************************************************************
    //  implement the GeometryFactory interface
    // *************************************************************************
    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Factory#getCoordinateReferenceSystem()
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return crs;
    }

    public Position createPosition(DirectPosition point) {
        return new DirectPositionImpl(point);
    }

    public DirectPosition createDirectPosition() {
        return new DirectPositionImpl(crs);
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Factory#createDirectPosition(double[])
     */
    public DirectPosition createDirectPosition(final double[] coordinates) {
        return new DirectPositionImpl(crs, coordinates);
    }

    /**
     * @inheritDoc
     * @see
     *     org.opengis.geometry.coordinate.Factory#createEnvelope(org.opengis.geometry.coordinate.DirectPosition,
     *     org.opengis.geometry.coordinate.DirectPosition)
     */
    public Envelope createEnvelope(
            final DirectPosition lowerCorner, final DirectPosition upperCorner) {
        return new EnvelopeImpl(lowerCorner, upperCorner);
    }
    /**
     * @inheritDoc
     * @see
     *     org.opengis.geometry.coordinate.Factory#createLineSegment(org.opengis.geometry.coordinate.Position,
     *     org.opengis.geometry.coordinate.Position)
     */
    public LineSegment createLineSegment(final Position startPoint, final Position endPoint) {
        LineSegmentImpl line = new LineSegmentImpl();
        line.getControlPoints().add(startPoint);
        line.getControlPoints().add(endPoint);

        return line;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Factory#createLineString(java.util.List)
     */
    public LineString createLineString(final List /*<Position>*/ points) {
        LineString result = new LineStringImpl();
        PointArray pa = result.getControlPoints();
        List list = pa;
        Iterator it = points.iterator();
        while (it.hasNext()) {
            //            Object o = it.next();
            //            if (o instanceof DirectPosition) {
            //                list.add(o);
            //            } else if (o instanceof Position) {
            //                Position p = (Position) o;
            //                DirectPosition dp = p.getDirectPosition();
            //                /*if (dp == null) {
            //                    dp = p.getIndirect().getDirectPosition();
            //                }*/
            //                list.add(dp);
            //            }
            Position position = (Position) it.next();
            DirectPosition directPosition = position.getDirectPosition();
            list.add(directPosition);
        }
        return result;
    }

    /**
     * @inheritDoc
     * @see
     *     org.opengis.geometry.coordinate.Factory#createGeodesic(org.opengis.geometry.coordinate.Position,
     *     org.opengis.geometry.coordinate.Position)
     */
    public Geodesic createGeodesic(final Position startPoint, final Position endPoint) {
        return null;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Factory#createGeodesicString(java.util.List)
     */
    public GeodesicString createGeodesicString(final List /*<Position>*/ points) {
        return null;
    }

    /**
     * @inheritDoc
     * @see
     *     org.opengis.geometry.coordinate.Factory#createArc(org.opengis.geometry.coordinate.Position,
     *     org.opengis.geometry.coordinate.Position, org.opengis.geometry.coordinate.Position)
     */
    public Arc createArc(
            final Position startPoint, final Position midPoint, final Position endPoint) {
        return null;
    }

    /**
     * @inheritDoc
     * @see
     *     org.opengis.geometry.coordinate.Factory#createArc(org.opengis.geometry.coordinate.Position,
     *     org.opengis.geometry.coordinate.Position, double, double[])
     */
    public Arc createArc(
            final Position startPoint,
            final Position endPoint,
            final double bulge,
            final double[] normal) {
        return null;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Factory#createArcString(java.util.List)
     */
    public ArcString createArcString(final List /*<Position>*/ points) {
        return null;
    }

    /**
     * @inheritDoc
     * @see
     *     org.opengis.geometry.coordinate.Factory#createArcByBulge(org.opengis.geometry.coordinate.Position,
     *     org.opengis.geometry.coordinate.Position, double, double[])
     */
    public ArcByBulge createArcByBulge(
            final Position startPoint,
            final Position endPoint,
            final double bulge,
            final double[] normal) {
        return null;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Factory#createArcStringByBulge(java.util.List, double[],
     *     java.util.List)
     */
    public ArcStringByBulge createArcStringByBulge(
            final List /*<Position>*/ points,
            final double[] bulges,
            final List /*<double[]>*/ normals) {
        return null;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Factory#createBSplineCurve(int,
     *     org.opengis.geometry.coordinate.PointArray, java.util.List,
     *     org.opengis.geometry.coordinate.KnotType)
     */
    public BSplineCurve createBSplineCurve(int arg0, PointArray arg1, List arg2, KnotType arg3)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @inheritDoc
     * @see
     *     org.opengis.geometry.coordinate.Factory#createPolygon(org.opengis.geometry.primitive.SurfaceBoundary)
     */
    public Polygon createPolygon(SurfaceBoundary boundary)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        PolygonImpl result = new PolygonImpl(boundary);
        return result;
    }

    /**
     * @inheritDoc
     * @see
     *     org.opengis.geometry.coordinate.Factory#createPolygon(org.opengis.geometry.primitive.SurfaceBoundary,
     *     org.opengis.geometry.primitive.Surface)
     */
    public Polygon createPolygon(SurfaceBoundary boundary, Surface spanningSurface)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        PolygonImpl result = new PolygonImpl(boundary, Collections.singletonList(spanningSurface));
        return result;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Factory#createTin(java.util.Set, java.util.Set,
     *     java.util.Set, double)
     */
    public Tin createTin(Set arg0, Set arg1, Set arg2, double arg3)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see
     *     org.opengis.geometry.coordinate.Factory#createSurfaceBoundary(org.opengis.geometry.primitive.Ring,
     *     java.util.List)
     */
    public SurfaceBoundary createSurfaceBoundary(Ring exterior, List interiors)
            throws MismatchedReferenceSystemException {
        return new SurfaceBoundaryImpl(
                crs, exterior, (Ring[]) interiors.toArray(new Ring[interiors.size()]));
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Factory#createMultiPrimitive()
     */
    public MultiPrimitive createMultiPrimitive() {
        throw new UnsupportedOperationException(
                "This is the JTS Wrapper Factory which only supports implementations that align with the Simple Feature for SQL Specification.");
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Factory#createPolyhedralSurface(java.util.List)
     */
    public PolyhedralSurface createPolyhedralSurface(final List<Polygon> polygons)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        PolyhedralSurfaceImpl result = new PolyhedralSurfaceImpl(crs);
        List<?> cast = (List<?>) polygons;
        result.getPatches().addAll((List<PolygonImpl>) cast);
        return result;
    }

    public BSplineSurface createBSplineSurface(List arg0, int[] arg1, List[] arg2, KnotType arg3)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        throw new UnsupportedOperationException(
                "This is the JTS Wrapper Factory which only supports implementations that align with the Simple Feature for SQL Specification.");
    }
}
