/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2014, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    Refractions Research Inc. Can be found on the web at:
 *    http://www.refractions.net/
 */
package org.geotools.data.oracle.sdo;

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import org.geotools.geometry.jts.CircularArc;
import org.geotools.geometry.jts.CompoundCurvedGeometry;
import org.geotools.geometry.jts.CurvedGeometries;
import org.geotools.geometry.jts.CurvedGeometryFactory;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.locationtech.jts.algorithm.Orientation;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.CoordinateSequences;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * Oracle Spatial Data Object utilities functions.
 *
 * <p>Provide utility functions for working with JTS Geometries in terms Oracle Spatial Data Objects
 *
 * <p>This class can be used for normal JTS Geometry persistence with little fuss and bother - please see
 * GeometryConverter for an example of this.
 *
 * <p>With a little fuss and bother LRS information can also be handled. Although it is very rare that JTS makes use of
 * such things.
 *
 * @see <a
 *     href="http://otn.oracle.com/pls/db10g/db10g.to_toc?pathname=appdev.101%2Fb10826%2Ftoc.htm&remark=portal+%28Unstructured+data%29">Spatial
 *     User's Guide (10.1)</a>
 * @author Jody Garnett, Refractions Reasearch Inc.
 * @version CVS Version
 * @see net.refractions.jspatial.jts
 */
public final class SDO {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(SDO.class);
    public static final int SRID_NULL = -1;

    //
    // Encoding Helper Functions
    //

    /**
     * Produce SDO_GTYPE representing provided Geometry.
     *
     * <p>Encoding of Geometry type and dimension.
     *
     * <p>SDO_GTYPE defined as for digits <code>[d][l][tt]</code>:
     *
     * <ul>
     *   <li><b><code>d</code>:</b> number of dimensions (limited to 2,3, or 4)
     *   <li><b><code>l</code>:</b> measure representation (ordinate 3 or 4) or 0 to represent none/last
     *   <li><b><code>tt:</code></b> See the TT. constants defined in this class
     * </ul>
     *
     * <p>Definition provided by Oracle Spatial User�s Guide and Reference.
     */
    public static int gType(Geometry geom) {
        int d = D(geom) * 1000;
        int l = L(geom) * 100;
        int tt = TT(geom);

        return d + l + tt;
    }

    /**
     * Return D as defined by SDO_GTYPE (either 2,3 or 4).
     *
     * <p>For normal JTS Geometry this will be 2 or 3 depending if geom.getCoordinate.z is Double.NaN.
     *
     * <p>Subclasses may override as required.
     *
     * @return <code>3</code>
     */
    public static int D(Geometry geom) {
        CoordinateSequenceFactory f = geom != null ? geom.getFactory().getCoordinateSequenceFactory() : null;

        if (f instanceof CoordinateAccessFactory) {
            return ((CoordinateAccessFactory) f).getDimension();
        } else if (geom == null || geom.getCoordinate() == null || geom.isEmpty()) {
            return 2;
        } else {
            // return 3;
            return Double.isNaN(geom.getCoordinate().getZ()) ? 2 : 3;
        }
    }

    /**
     * Return L as defined by SDO_GTYPE (either 3,4 or 0).
     *
     * <p>L represents support for LRS (Liniar Referencing System?). JTS Geometry objects do not support LRS so this
     * will be 0.
     *
     * <p>Subclasses may override as required.
     *
     * @return <code>0</code>
     */
    public static int L(Geometry geom) {
        CoordinateSequenceFactory f = geom.getFactory().getCoordinateSequenceFactory();

        if (f instanceof CoordinateAccessFactory) {
            return ((CoordinateAccessFactory) f).getDimension();
        } else {
            return 0;
        }
    }

    /**
     * Return TT as defined by SDO_GTYPE (represents geometry type).
     *
     * <p>TT is used to represent the type of the JTS Geometry:
     *
     * <pre><code>
     * <b>Value Geometry Type    JTS Geometry</b>
     * 00    UNKNOWN_GEOMETRY null
     * 01    POINT            Point
     * 02    LINE             LineString
     *       CURVE            <i>not supported</i>
     * 03    POLYGON          Polygon
     * 04    COLLECTION       GeometryCollection
     * 05    MULTIPOINT       MultiPoint
     * 06    MULTILINE        MultiLineString
     *       MULTICURVE       <i>not supported</i>
     * 07    MULTIPOLYGON     MultiPolygon
     * </code></pre>
     *
     * @return <code>TT</code> representing <code>geom</code>
     * @throws IllegalArgumentException If SDO_GTYPE can not be represetned by JTS
     */
    public static int TT(Geometry geom) {
        if (geom == null) {
            return TT.UNKNOWN; // UNKNOWN
        } else if (geom instanceof Point) {
            return TT.POINT;
        } else if (geom instanceof LineString) {
            return TT.LINE;
        } else if (geom instanceof Polygon) {
            return TT.POLYGON;
        } else if (geom instanceof MultiPoint) {
            return TT.MULTIPOINT;
        } else if (geom instanceof MultiLineString) {
            return TT.MULTILINE;
        } else if (geom instanceof MultiPolygon) {
            return TT.MULTIPOLYGON;
        } else if (geom instanceof GeometryCollection) {
            return TT.COLLECTION;
        }

        throw new IllegalArgumentException("Cannot encode JTS "
                + geom.getGeometryType()
                + " as SDO_GTYPE "
                + "(Limitied to Point, Line, Polygon, GeometryCollection, MultiPoint,"
                + " MultiLineString and MultiPolygon)");
    }

    /**
     * Returns geometry SRID.
     *
     * <p>SRID code representing Spatial Reference System. SRID number used must be defined in the Oracle MDSYS.CS_SRS
     * table.
     *
     * <p><code>SRID_NULL</code>represents lack of coordinate system.
     *
     * @param geom Geometry SRID Number (JTS14 uses GeometryFactor.getSRID() )
     * @return <code>SRID</code> for provided geom
     */
    public static int SRID(Geometry geom) {
        return geom.getSRID();
    }

    /**
     * Return SDO_POINT_TYPE for geometry
     *
     * <p>Will return non null for Point objects. <code>null</code> is returned for all non point objects.
     *
     * <p>You cannot use this with LRS Coordinates
     *
     * <p>Subclasses may wish to repress this method and force Points to be represented using SDO_ORDINATES.
     */
    public static double[] point(Geometry geom) {
        if (geom instanceof Point && L(geom) == 0) {
            Point point = (Point) geom;
            Coordinate coord = point.getCoordinate();

            return new double[] {coord.x, coord.y, coord.getZ()};
        }

        // SDO_POINT_TYPE only used for non LRS Points
        return null;
    }

    /**
     * Return SDO_ELEM_INFO array for geometry
     *
     * <p>Describes how to use Ordinates to represent Geometry.
     *
     * <pre><code><b>
     * # Name                Meaning</b>
     * 0 SDO_STARTING_OFFSET Offsets start at one
     * 1 SDO_ETYPE           Describes how ordinates are ordered
     * 2 SDO_INTERPRETATION  SDO_ETYPE: 4, 1005, or 2005
     *                       Number of triplets involved in compound geometry
     *
     *                       SDO_ETYPE: 1, 2, 1003, or 2003
     *                       Describes ordering of ordinates in geometry
     * </code></pre>
     *
     * <p>For compound elements (SDO_ETYPE values 4 and 5) the last element of one is the first element of the next.
     *
     * @param geom Geometry being represented
     * @return Descriptionof Ordinates representation
     */
    public static int[] elemInfo(Geometry geom) {
        return elemInfo(geom, gType(geom));
    }

    public static int[] elemInfo(Geometry geom, final int GTYPE) {
        List<Integer> list = new ArrayList<>();
        elemInfo(list, geom, 1, GTYPE);

        return intArray(list);
    }

    /**
     * Add to SDO_ELEM_INFO list for geometry and GTYPE.
     *
     * @param elemInfoList List used to gather SDO_ELEM_INFO
     * @param geom Geometry to encode
     * @param STARTING_OFFSET Starting offset in SDO_ORDINATES
     * @param GTYPE Encoding of dimension, measures and geometry type
     * @throws IllegalArgumentException If geom cannot be encoded by ElemInfo
     */
    private static void elemInfo(
            List<Integer> elemInfoList, Geometry geom, final int STARTING_OFFSET, final int GTYPE) {
        final int tt = TT(geom);

        switch (tt) {
            case TT.POINT:
                addElemInfo(elemInfoList, (Point) geom, STARTING_OFFSET);

                return;

            case TT.LINE:
                addElemInfo(elemInfoList, (LineString) geom, STARTING_OFFSET);

                return;

            case TT.POLYGON:
                addElemInfo(elemInfoList, (Polygon) geom, STARTING_OFFSET, GTYPE);

                return;

            case TT.MULTIPOINT:
                addElemInfo(elemInfoList, (MultiPoint) geom, STARTING_OFFSET);

                return;

            case TT.MULTILINE:
                addElemInfo(elemInfoList, (MultiLineString) geom, STARTING_OFFSET, GTYPE);

                return;

            case TT.MULTIPOLYGON:
                addElemInfo(elemInfoList, (MultiPolygon) geom, STARTING_OFFSET, GTYPE);

                return;

            case TT.COLLECTION:
                addElemInfo(elemInfoList, (GeometryCollection) geom, STARTING_OFFSET, GTYPE);

                return;
        }

        throw new IllegalArgumentException("Cannot encode JTS "
                + geom.getGeometryType()
                + " as SDO_ELEM_INFO "
                + "(Limitied to Point, Line, Polygon, GeometryCollection, MultiPoint,"
                + " MultiLineString and MultiPolygon)");
    }

    /**
     * Not often called as POINT_TYPE prefered over ELEMINFO & ORDINATES.
     *
     * <p>This method is included to allow for multigeometry encoding.
     *
     * @param elemInfoList List containing ELEM_INFO array
     * @param point Point to encode
     * @param STARTING_OFFSET Starting offset in SDO_ORDINATE array
     */
    private static void addElemInfo(List<Integer> elemInfoList, Point point, final int STARTING_OFFSET) {
        addInt(elemInfoList, STARTING_OFFSET);
        addInt(elemInfoList, ETYPE.POINT);
        addInt(elemInfoList, 1); // INTERPRETATION single point
    }

    private static void addElemInfo(List<Integer> elemInfoList, LineString line, final int STARTING_OFFSET) {
        addInt(elemInfoList, STARTING_OFFSET);
        addInt(elemInfoList, ETYPE.LINE);
        addInt(elemInfoList, 1); // INTERPRETATION straight edges
    }

    private static void addElemInfo(
            List<Integer> elemInfoList, Polygon polygon, final int STARTING_OFFSET, final int GTYPE) {
        final int HOLES = polygon.getNumInteriorRing();

        if (HOLES == 0) {
            addInt(elemInfoList, STARTING_OFFSET);
            addInt(elemInfoList, elemInfoEType(polygon));
            addInt(elemInfoList, elemInfoInterpretation(polygon, ETYPE.POLYGON_EXTERIOR));

            return;
        }

        int LEN = D(GTYPE) + L(GTYPE);
        int offset = STARTING_OFFSET;

        LineString ring = polygon.getExteriorRing();
        addInt(elemInfoList, offset);
        addInt(elemInfoList, elemInfoEType(polygon));
        addInt(elemInfoList, elemInfoInterpretation(polygon, ETYPE.POLYGON_EXTERIOR));
        offset += ring.getNumPoints() * LEN;

        for (int i = 1; i <= HOLES; i++) {
            ring = polygon.getInteriorRingN(i - 1);
            addInt(elemInfoList, offset);
            addInt(elemInfoList, ETYPE.POLYGON_INTERIOR);
            addInt(elemInfoList, elemInfoInterpretation(ring, ETYPE.POLYGON_INTERIOR));
            offset += ring.getNumPoints() * LEN;
        }
    }

    private static void addElemInfo(List<Integer> elemInfoList, MultiPoint points, final int STARTING_OFFSET) {
        addInt(elemInfoList, STARTING_OFFSET);
        addInt(elemInfoList, ETYPE.POINT);
        addInt(elemInfoList, elemInfoInterpretation(points, ETYPE.POINT));
    }

    private static void addElemInfo(
            List<Integer> elemInfoList, MultiLineString lines, final int STARTING_OFFSET, final int GTYPE) {
        LineString line;
        int offset = STARTING_OFFSET;

        int LEN = D(GTYPE) + L(GTYPE);

        for (int i = 0; i < lines.getNumGeometries(); i++) {
            line = (LineString) lines.getGeometryN(i);
            addElemInfo(elemInfoList, line, offset);
            offset += line.getNumPoints() * LEN;
        }
    }

    private static void addElemInfo(
            List<Integer> elemInfoList, MultiPolygon polys, final int STARTING_OFFSET, final int GTYPE) {
        Polygon poly;
        int offset = STARTING_OFFSET;

        int LEN = D(GTYPE) + L(GTYPE);

        for (int i = 0; i < polys.getNumGeometries(); i++) {
            poly = (Polygon) polys.getGeometryN(i);
            if (poly != null && !poly.isEmpty()) {
                addElemInfo(elemInfoList, poly, offset, GTYPE);
                if (isRectangle(poly)) {
                    offset += 2 * LEN;
                } else {
                    offset += poly.getNumPoints() * LEN;
                }
            }
        }
    }

    private static void addElemInfo(
            List<Integer> elemInfoList, GeometryCollection geoms, final int STARTING_OFFSET, final int GTYPE) {
        Geometry geom;
        int offset = STARTING_OFFSET;
        int LEN = D(GTYPE) + L(GTYPE);

        for (int i = 0; i < geoms.getNumGeometries(); i++) {
            geom = geoms.getGeometryN(i);
            elemInfo(elemInfoList, geom, offset, GTYPE);
            if (geom instanceof Polygon && isRectangle((Polygon) geom)) {
                offset += 2 * LEN;
            } else {
                offset += geom.getNumPoints() * LEN;
            }
        }
    }

    private static void addInt(List<Integer> list, int i) {
        list.add(Integer.valueOf(i));
    }

    /**
     * Converts provied list to an int array
     *
     * @param list List to cast to an array type
     * @return array of ints
     */
    private static int[] intArray(List<Integer> list) {
        int[] array = new int[list.size()];
        int offset = 0;
        for (Integer i : list) {
            array[offset++] = i;
        }

        return array;
    }

    /**
     * Starting offset used by SDO_ORDINATES as stored in the SDO_ELEM_INFO array.
     *
     * <p>Starting offsets start from one.
     *
     * <p>Describes ordinates as part of <code>SDO_ELEM_INFO</code> data type.
     *
     * @return <code>1</code> for non nested <code>geom</code>
     */
    public static int elemInfoStartingOffset(Geometry geom) {
        return 1;
    }

    /**
     * Produce <code>SDO_ETYPE</code> for geometry description as stored in the <code>SDO_ELEM_INFO
     * </code>.
     *
     * <p>Describes how Ordinates are ordered:
     *
     * <pre><code><b>
     * Value Elements Meaning</b>
     *    0           Custom Geometry (like spline)
     *    1  simple   Point (or Points)
     *    2  simple   Line (or Lines)
     *    3           polygon ring of unknown order (discouraged update to 1003 or 2003)
     * 1003  simple   polygon ring (1 exterior counterOrientationwise order)
     * 2003  simple   polygon ring (2 interior Orientationwise order)
     *    4  compound series defines a linestring
     *    5  compound series defines a polygon ring of unknown order (discouraged)
     * 1005  compound series defines exterior polygon ring (counterOrientationwise order)
     * 2005  compound series defines interior polygon ring (Orientationwise order)
     * </code></pre>
     *
     * <p>Keep in mind:
     *
     * <ul>
     *   <li><code>simple</code> elements are defined by a single triplet entry in the <code>
     *       SDO_ELEM_INFO</code> array
     *   <li><code>compound</code> elements are defined by a header triplet, and a series of triplets for the parts.
     *       Elements in a compound element share first and last points.
     *   <li>We are not allowed to mix 1 digit and 4 digit values for ETYPE and GTYPE in a single geometry
     * </ul>
     *
     * <p>This whole mess describes ordinates as part of <code>SDO_ELEM_INFO</code> array. data type.
     *
     * @param geom Geometry being represented
     * @return Descriptionof Ordinates representation
     */
    protected static int elemInfoEType(Geometry geom) {
        switch (TT(geom)) {
            case TT.UNKNOWN:
                return ETYPE.CUSTOM;

            case TT.POINT:
                return ETYPE.POINT;

            case TT.LINE:
                return ETYPE.LINE;

            case TT.POLYGON:
                return isExterior((Polygon) geom)
                        ? ETYPE.POLYGON_EXTERIOR // cc order
                        : ETYPE.POLYGON_INTERIOR; // ccw order

            default:

                // should never happen!
                throw new IllegalArgumentException("Unknown encoding of SDO_GTYPE");
        }
    }

    /**
     * Produce <code>SDO_INTERPRETATION</code> for geometry description as stored in the <code>
     * SDO_ELEM_INFO</code>.
     *
     * <p>Describes ordinates as part of <code>SDO_ELEM_INFO</code> array.
     *
     * <ul>
     *   <li><b><code>compound</code> element:</b>(SDO_ETYPE 4, 1005, or 2005)<br>
     *       Number of subsequent triplets are part of compound element
     *   <li><b><code>simple</code> element:</b>(SDE_ELEM 1, 2, 1003, or 2003)<br>
     *       Code defines how ordinates are interpreted (lines or curves)
     * </ul>
     *
     * <p>SDO_INTERPRETAION Values: (from Table 2-2 in reference docs)
     *
     * <pre><code><b>
     * SDO_ETYPE Value    Meaning</b>
     * 0         anything Custom Geometry
     * 1         1        Point
     * 1         N > 1    N points
     * 2         1        LineString of straight lines
     * 2         2        LineString connected by circ arcs (start,any,end pt)
     * 1003/2003 1        Polygon Edged with straight lines
     * 1003/2003 2        Polygon Edged with circ arcs (start, any, end pt)
     * 1003/2003 3        Non SRID rectangle defined by (bottomleft,topright pt)
     * 1003/2003 4        Circle defined by three points on circumference
     * 4         N > 1    Compound Line String made of N (ETYPE=2) lines and arcs
     * 1005/2005 N > 1    Polygon defined by (ETYPE=2) lines and arcs
     *
     *
     * </code></pre>
     *
     * <p>When playing with circular arcs (SDO_INTERPRETATION==2) arcs are defined by three points. A start point, any
     * point on the arc and the end point. The last point of an arc is the start point of the next. When used to
     * describe a polygon (SDO_ETYPE==1003 or 2003) the first and last point must be the same.
     */
    public static int elemInfoInterpretation(Geometry geom) {
        return elemInfoInterpretation(geom, elemInfoEType(geom));
    }

    /**
     * Allows specification of <code>INTERPRETATION</code> used to interpret <code>geom</code>.
     *
     * <p>Provides the INTERPRETATION value for the ELEM_INFO triplet of (STARTING_OFFSET, ETYPE, INTERPRETATION).
     *
     * @param geom Geometry to encode
     * @param etype ETYPE value requiring an INTERPREATION
     * @return INTERPRETATION ELEM_INFO entry for geom given etype
     * @throws IllegalArgumentException If asked to encode a curve
     */
    public static int elemInfoInterpretation(Geometry geom, final int etype) {
        switch (etype) {
            case ETYPE.CUSTOM: // customize for your own Geometries
                break;

            case ETYPE.POINT:
                if (geom instanceof Point) {
                    return 1;
                }

                if (geom instanceof MultiPoint) {
                    return geom.getNumGeometries();
                }

                break;

            case ETYPE.LINE:
                if (isCurve((LineString) geom)) {
                    return 2;
                }

                return 1;

            case ETYPE.POLYGON:
            case ETYPE.POLYGON_EXTERIOR:
            case ETYPE.POLYGON_INTERIOR:
                if (geom instanceof Polygon) {
                    Polygon polygon = (Polygon) geom;

                    if (isCurve(polygon)) {
                        return 2;
                    }

                    if (isRectangle(polygon)) {
                        return 3;
                    }

                    if (isCircle(polygon)) {
                        return 4;
                    }
                }

                return 1;

            case ETYPE.COMPOUND:
                throw new IllegalArgumentException("JTS LineStrings are not composed of curves and lines.");

            case ETYPE.COMPOUND_POLYGON:
            case ETYPE.COMPOUND_POLYGON_INTERIOR:
            case ETYPE.COMPOUND_POLYGON_EXTERIOR:
                throw new IllegalArgumentException("JTS Polygons are not composed of curves and lines.");
        }

        throw new IllegalArgumentException("Cannot encode JTS "
                + geom.getGeometryType()
                + " as "
                + "SDO_INTERPRETATION (Limitied to Point, Line, Polygon, "
                + "GeometryCollection, MultiPoint, MultiLineString and MultiPolygon)");
    }

    /**
     * Produce <code>SDO_ORDINATES</code> for geometry.
     *
     * <p>Please see SDO_ETYPE, SDO_INTERPRETATION and SDO_GTYPE for description of how these ordinates are to be
     * interpreted.
     *
     * <p>Ordinates are ordered by Dimension are non null:
     *
     * <ul>
     *   <li>
     *       <p>Two Dimensional: {x1,y1,x2,y2,...}
     *   <li>
     *       <p>Three Dimensional: {x1,y1,z1,x2,y2,z2,...}
     * </ul>
     *
     * <p>Spatial will siliently detect and ignore the following:
     *
     * <ul>
     *   <li>d001 point/d005 multipoint elements that are not SDO_ETYPE==1 points
     *   <li>d002 lines or curve/d006 multilines or multicurve elements that are not SDO_ETYPE==2 lines or SDO_ETYPE==4
     *       arcs
     *   <li>d003 polygon/d007 multipolygon elements that are not SDO_ETYPE==3 unordered polygon lines or SDO_ETYPE==5
     *       unorderd compound polygon ring are ignored
     * </ul>
     *
     * <p>While Oracle is silient on these errors - all other errors will not be detected!
     */
    public static double[] ordinates(Geometry geom) {
        List<double[]> list = new ArrayList<>();

        coordinates(list, geom);

        return ordinates(list, geom);
    }

    public static CoordinateSequence getCS(Geometry geom) {
        CoordinateSequence cs = null;
        switch (TT(geom)) {
            case TT.UNKNOWN:
                break; // extend with your own custom types

            case TT.POINT:
                // addCoordinates(list, (Point) geom);

                return cs;

            case TT.LINE:
                cs = getLineStringCS((LineString) geom);

                return cs;

            case TT.POLYGON:
                // addCoordinates(list, (Polygon) geom);

                return cs;

            case TT.COLLECTION:
                // addCoordinates(list, (GeometryCollection) geom);

                return cs;

            case TT.MULTIPOINT:
                // addCoordinates(list, (MultiPoint) geom);

                return cs;

            case TT.MULTILINE:
                // addCoordinates(list, (MultiLineString) geom);

                return cs;

            case TT.MULTIPOLYGON:
                // addCoordinates(list, (MultiPolygon) geom);

                return cs;
        }

        throw new IllegalArgumentException("Cannot encode JTS "
                + geom.getGeometryType()
                + " as "
                + "SDO_ORDINATRES (Limitied to Point, Line, Polygon, "
                + "GeometryCollection, MultiPoint, MultiLineString and MultiPolygon)");
    }

    /**
     * getLineStringCS purpose.
     *
     * <p>Description ...
     */
    private static CoordinateSequence getLineStringCS(LineString ls) {
        if (ls.getCoordinateSequence() instanceof CoordinateAccess) {
            CoordinateAccess ca = (CoordinateAccess) ls.getCoordinateSequence();
            return ca;
        } else return null;
    }

    /**
     * Encode Geometry as described by GTYPE and ELEM_INFO
     *
     * <p>CoordinateSequence & CoordinateAccess wil be used to determine the dimension, and the number of ordinates
     * added.
     *
     * @param list Flat list of Double
     * @param geom Geometry
     * @throws IllegalArgumentException If geometry cannot be encoded
     */
    public static void coordinates(List<double[]> list, Geometry geom) {
        switch (TT(geom)) {
            case TT.UNKNOWN:
                break; // extend with your own custom types

            case TT.POINT:
                addCoordinates(list, (Point) geom);

                return;

            case TT.LINE:
                addCoordinates(list, (LineString) geom);

                return;

            case TT.POLYGON:
                addCoordinates(list, (Polygon) geom);

                return;

            case TT.COLLECTION:
                addCoordinates(list, (GeometryCollection) geom);

                return;

            case TT.MULTIPOINT:
                addCoordinates(list, (MultiPoint) geom);

                return;

            case TT.MULTILINE:
                addCoordinates(list, (MultiLineString) geom);

                return;

            case TT.MULTIPOLYGON:
                addCoordinates(list, (MultiPolygon) geom);

                return;
        }

        throw new IllegalArgumentException("Cannot encode JTS "
                + geom.getGeometryType()
                + " as "
                + "SDO_ORDINATRES (Limitied to Point, Line, Polygon, "
                + "GeometryCollection, MultiPoint, MultiLineString and MultiPolygon)");
    }

    /**
     * Adds a double array to list.
     *
     * <p>The double array will contain all the ordinates in the Coordinate sequence.
     */
    private static void addCoordinates(List<double[]> list, CoordinateSequence sequence) {
        if (sequence instanceof CoordinateAccess) {
            CoordinateAccess access = (CoordinateAccess) sequence;

            for (int i = 0; i < access.size(); i++) {
                list.add(ordinateArray(access, i));
            }
        } else {
            for (int i = 0; i < sequence.size(); i++) {
                list.add(ordinateArray(sequence.getCoordinate(i)));
            }
        }
    }

    private static double[] ordinateArray(Coordinate coord) {
        return new double[] {coord.x, coord.y, coord.getZ()};
    }

    private static double[] ordinateArray(CoordinateAccess access, int index) {
        final int D = access.getDimension();
        final int L = access.getNumAttributes();
        final int LEN = D + L;
        double[] ords = new double[LEN];

        for (int i = 0; i < LEN; i++) {
            ords[i] = access.getOrdinate(index, i);
        }

        return ords;
    }

    /**
     * Used with ELEM_INFO <code>( 1, 1, 1)</code>
     *
     * @param list List to add coordiantes to
     * @param point Point to be encoded
     */
    private static void addCoordinates(List<double[]> list, Point point) {
        addCoordinates(list, point.getCoordinateSequence());
    }

    /**
     * Used with ELEM_INFO <code>(1, 2, 1)</code>
     *
     * @param list List to add coordiantes to
     * @param line LineString to be encoded
     */
    private static void addCoordinates(List<double[]> list, LineString line) {
        addCoordinates(list, line.getCoordinateSequence());
    }

    /**
     * Used to addCoordinates based on polygon encoding.
     *
     * <p>Elem Info Interpretations supported:
     *
     * <ul>
     *   <li>3: Rectangle
     *   <li>1: Standard (supports holes)
     * </ul>
     *
     * @param list List to add coordiantes to
     * @param polygon Polygon to be encoded
     */
    private static void addCoordinates(List<double[]> list, Polygon polygon) {
        switch (elemInfoInterpretation(polygon)) {
            case 4: // circle not supported
                break;

            case 3:
                addCoordinatesInterpretation3(list, polygon);
                break;

            case 2: // curve not suppported
                break;

            case 1:
                addCoordinatesInterpretation1(list, polygon);

                break;
        }
    }

    /**
     * Rectangle ordinates for polygon.
     *
     * <p>You should in sure that the provided <code>polygon</code> is a rectangle using isRectangle( Polygon )
     *
     * <p>
     *
     * @param list List to add coordiantes to
     * @param poly Polygon to be encoded
     */
    private static void addCoordinatesInterpretation3(List<double[]> list, Polygon poly) {
        Envelope e = poly.getEnvelopeInternal();
        list.add(new double[] {e.getMinX(), e.getMinY()});
        list.add(new double[] {e.getMaxX(), e.getMaxY()});
    }

    /**
     * Add ordinates for polygon - with hole support.
     *
     * <p>Ensure ordinates are added in the correct orientation as External or Internal polygons.
     *
     * @param list List to add coordiantes to
     * @param polygon Polygon to be encoded
     */
    private static void addCoordinatesInterpretation1(List<double[]> list, Polygon polygon) {
        int holes = polygon.getNumInteriorRing();
        if (!polygon.isEmpty()) {
            addCoordinates(
                    list,
                    counterClockWise(
                            polygon.getFactory().getCoordinateSequenceFactory(),
                            polygon.getExteriorRing().getCoordinateSequence()));

            for (int i = 0; i < holes; i++) {
                addCoordinates(
                        list,
                        clockWise(
                                polygon.getFactory().getCoordinateSequenceFactory(),
                                polygon.getInteriorRingN(i).getCoordinateSequence()));
            }
        }
    }

    private static void addCoordinates(List<double[]> list, MultiPoint points) {
        for (int i = 0; i < points.getNumGeometries(); i++) {
            Geometry geometryN = points.getGeometryN(i);
            if (geometryN != null && !geometryN.isEmpty()) addCoordinates(list, (Point) geometryN);
        }
    }

    private static void addCoordinates(List<double[]> list, MultiLineString lines) {
        for (int i = 0; i < lines.getNumGeometries(); i++) {
            Geometry geometryN = lines.getGeometryN(i);
            if (geometryN != null && !geometryN.isEmpty()) addCoordinates(list, (LineString) geometryN);
        }
    }

    private static void addCoordinates(List<double[]> list, MultiPolygon polys) {
        for (int i = 0; i < polys.getNumGeometries(); i++) {

            Geometry geometryN = polys.getGeometryN(i);
            if (geometryN != null && !geometryN.isEmpty()) addCoordinates(list, (Polygon) geometryN);
        }
    }

    private static void addCoordinates(List<double[]> list, GeometryCollection geoms) {
        Geometry geom;

        for (int i = 0; i < geoms.getNumGeometries(); i++) {
            geom = geoms.getGeometryN(i);
            if (geom == null || geom.isEmpty()) continue;
            if (geom instanceof Point) {
                addCoordinates(list, (Point) geom);
            } else if (geom instanceof LineString) {
                addCoordinates(list, (LineString) geom);
            } else if (geom instanceof Polygon) {
                addCoordinates(list, (Polygon) geom);
            } else if (geom instanceof MultiPoint) {
                addCoordinates(list, (MultiPoint) geom);
            } else if (geom instanceof MultiLineString) {
                addCoordinates(list, (MultiLineString) geom);
            } else if (geom instanceof MultiPolygon) {
                addCoordinates(list, (MultiPolygon) geom);
            } else if (geom instanceof GeometryCollection) {
                addCoordinates(list, (GeometryCollection) geom);
            }
        }
    }

    /**
     * Package up array of requested ordinate, regardless of geometry
     *
     * <p>Example numbering: for (x y g m) dimension==2, measure==2
     *
     * <ul>
     *   <li>0: x ordinate array
     *   <li>1: y ordinate array
     *   <li>2: g ordinate array
     *   <li>3: m ordinate array
     * </ul>
     */
    public static double[] ordinateArray(CoordinateSequence coords, int ordinate) {
        if (coords instanceof CoordinateAccess) {
            CoordinateAccess access = (CoordinateAccess) coords;

            return access.toOrdinateArray(ordinate);
        } else {
            final int LENGTH = coords.size();
            Coordinate c;
            double[] array = new double[LENGTH];

            if (ordinate == 0) {
                for (int i = 0; i < LENGTH; i++) {
                    c = coords.getCoordinate(i);
                    array[i] = c != null ? c.x : Double.NaN;
                }
            } else if (ordinate == 1) {
                for (int i = 0; i < LENGTH; i++) {
                    c = coords.getCoordinate(i);
                    array[i] = c != null ? c.y : Double.NaN;
                }
            } else if (ordinate == 2) {
                for (int i = 0; i < LENGTH; i++) {
                    c = coords.getCoordinate(i);
                    array[i] = c != null ? c.getZ() : Double.NaN;
                }
            } else {
                // default to NaN
                for (int i = 0; i < LENGTH; i++) {
                    array[i] = Double.NaN;
                }
            }

            return array;
        }
    }

    /**
     * Ordinate access.
     *
     * <p>CoordinateAccess is required for additional ordinates.
     *
     * <p>Ordinate limitied to:
     *
     * <ul>
     *   <li>0: x ordinate array
     *   <li>1: y ordinate array
     *   <li>2: z ordinate array
     *   <li>3: empty ordinate array
     * </ul>
     */
    public static double[] ordinateArray(Coordinate[] array, int ordinate) {
        if (array == null) {
            return null;
        }

        final int LENGTH = array.length;
        double[] ords = new double[LENGTH];
        Coordinate c;

        if (ordinate == 0) {
            for (int i = 0; i < LENGTH; i++) {
                c = array[i];
                ords[i] = c != null ? c.x : Double.NaN;
            }
        } else if (ordinate == 1) {
            for (int i = 0; i < LENGTH; i++) {
                c = array[i];
                ords[i] = c != null ? c.y : Double.NaN;
            }
        } else if (ordinate == 2) {
            for (int i = 0; i < LENGTH; i++) {
                c = array[i];
                ords[i] = c != null ? c.getZ() : Double.NaN;
            }
        } else {
            // default to NaN
            for (int i = 0; i < LENGTH; i++) {
                ords[i] = Double.NaN;
            }
        }

        return ords;
    }

    public static double[] ordinateArray(List list, int ordinate) {
        if (list == null) {
            return null;
        }

        final int LENGTH = list.size();
        double[] ords = new double[LENGTH];
        Coordinate c;

        if (ordinate == 0) {
            for (int i = 0; i < LENGTH; i++) {
                c = (Coordinate) list.get(i);
                ords[i] = c != null ? c.x : Double.NaN;
            }
        } else if (ordinate == 1) {
            for (int i = 0; i < LENGTH; i++) {
                c = (Coordinate) list.get(i);
                ords[i] = c != null ? c.y : Double.NaN;
            }
        } else if (ordinate == 2) {
            for (int i = 0; i < LENGTH; i++) {
                c = (Coordinate) list.get(i);
                ords[i] = c != null ? c.getZ() : Double.NaN;
            }
        } else {
            // default to NaN
            for (int i = 0; i < LENGTH; i++) {
                ords[i] = Double.NaN;
            }
        }

        return ords;
    }

    /**
     * Package up <code>array</code> in correct manner for <code>geom</code>.
     *
     * <p>Ordinates are placed into an array based on:
     *
     * <ul>
     *   <li>geometryGTypeD - chooses between 2d and 3d representation
     *   <li>geometryGTypeL - number of LRS measures
     * </ul>
     */
    public static double[] ordinates(List list, Geometry geom) {
        LOGGER.finest("ordinates D:" + D(geom));
        LOGGER.finest("ordinates L:" + L(geom));

        if (D(geom) == 3) {
            return ordinates3d(list, L(geom));
        } else {
            return ordinates2d(list, L(geom));
        }
    }

    /**
     * Ordinates (x,y,x1,y1,...) from coordiante list.
     *
     * <p>No assumptions are made about the order
     *
     * @param list coordinate list
     * @return ordinate array
     */
    public static double[] ordinates2d(List list) {
        final int NUMBER = list.size();
        final int LEN = 2;
        double[] array = new double[NUMBER * LEN];
        double[] ords;
        int offset = 0;

        for (Object o : list) {
            ords = (double[]) o;

            if (ords != null) {
                array[offset++] = ords[0];
                array[offset++] = ords[1];
            } else {
                array[offset++] = Double.NaN;
                array[offset++] = Double.NaN;
            }
        }

        return array;
    }

    /**
     * Ordinates (x,y,z,x2,y2,z2...) from coordiante[] array.
     *
     * @param list List of coordiante
     * @return ordinate array
     */
    public static double[] ordinates3d(List list) {
        final int NUMBER = list.size();
        final int LEN = 3;
        double[] array = new double[NUMBER * LEN];
        double[] ords;
        int offset = 0;

        for (Object o : list) {
            ords = (double[]) o;

            if (ords != null) {
                array[offset++] = ords[0];
                array[offset++] = ords[1];
                array[offset++] = ords[2];
            } else {
                array[offset++] = Double.NaN;
                array[offset++] = Double.NaN;
                array[offset++] = Double.NaN;
            }
        }

        return array;
    }

    /**
     * Ordinates (x,y,...id,x2,y2,...) from coordiante[] List.
     *
     * @param list coordiante list
     * @param L Dimension of ordinates required for representation
     * @return ordinate array
     */
    public static double[] ordinates2d(List list, final int L) {
        if (L == 0) {
            return ordinates2d(list);
        }

        final int NUMBER = list.size();
        final int LEN = 2 + L;
        double[] array = new double[NUMBER * LEN];
        double[] ords;

        for (int i = 0; i < NUMBER; i++) {
            ords = (double[]) list.get(i);

            for (int j = 0; j < LEN; j++) {
                array[i * LEN + j] = ords[j];
            }
        }

        return array;
    }

    /**
     * Ordinates (x,y,z,...id,x2,y2,z2...) from coordiante[] array.
     *
     * @param list coordinate array to be represented as ordinates
     * @param L Dimension of ordinates required for representation
     * @return ordinate array
     */
    public static double[] ordinates3d(List list, final int L) {
        if (L == 0) {
            return ordinates3d(list);
        }

        final int NUMBER = list.size();
        final int LEN = 3 + L;
        double[] array = new double[NUMBER * LEN];
        double[] ords;

        for (int i = 0; i < NUMBER; i++) {
            ords = (double[]) list.get(i);

            for (int j = 0; j < LEN; j++) {
                array[i * LEN + j] = ords[j];
            }
        }

        return array;
    }

    /**
     * Ensure Ring of Coordinates are in a counter Orientationwise order.
     *
     * <p>If the Coordinate need to be reversed a copy will be returned.
     *
     * @param factory Factory to used to reverse CoordinateSequence
     * @param ring Ring of Coordinates
     * @return coords in a CCW order
     */
    public static CoordinateSequence counterClockWise(CoordinateSequenceFactory factory, CoordinateSequence ring) {
        if (Orientation.isCCW(ring.toCoordinateArray())) {
            return ring;
        }

        return Coordinates.reverse(factory, ring);
    }

    /**
     * Ensure Ring of Coordinates are in a Orientationwise order.
     *
     * <p>If the Coordinate need to be reversed a copy will be returned.
     *
     * @param factory Factory used to reverse CoordinateSequence
     * @param ring Ring of Coordinates
     * @return coords in a CW order
     */
    private static CoordinateSequence clockWise(CoordinateSequenceFactory factory, CoordinateSequence ring) {
        if (!Orientation.isCCW(ring.toCoordinateArray())) {
            return ring;
        }
        return Coordinates.reverse(factory, ring);
    }

    // Utility Functions
    //
    //

    /**
     * Will need to tell if we are encoding a Polygon Exterior or Interior so we can produce the correct encoding.
     *
     * @param poly Polygon to check
     * @return <code>true</code> as we expect PolygonExteriors to be passed in
     */
    private static boolean isExterior(Polygon poly) {
        return true; // JTS polygons are always exterior
    }

    /**
     * We need to check if a <code>polygon</code> a cicle so we can produce the correct encoding.
     *
     * @return <code>true</code> if polygon is a circle
     */
    private static boolean isCircle(Polygon polygon) {
        return false; // JTS does not do cicles
    }

    /**
     * We need to check if a <code>polygon</code> a rectangle so we can produce the correct encoding.
     *
     * <p>Rectangles are only supported without a SRID!
     *
     * @return <code>true</code> if polygon is SRID==0 and a rectangle
     */
    private static boolean isRectangle(Polygon polygon) {
        if (polygon.getFactory().getSRID() != SRID_NULL) {
            // Rectangles only valid in CAD applications
            // that do not have an SRID system
            //
            return false;
        }

        if (L(polygon) != 0) {
            // cannot support LRS on a rectangle
            return false;
        }

        Coordinate[] coords = polygon.getCoordinates();

        if (coords.length != 5) {
            return false;
        }

        if (coords[0] == null || coords[1] == null || coords[2] == null || coords[3] == null) {
            return false;
        }

        if (!coords[0].equals2D(coords[4])) {
            return false;
        }

        double x1 = coords[0].x;
        double y1 = coords[0].y;
        double x2 = coords[1].x;
        double y2 = coords[1].y;
        double x3 = coords[2].x;
        double y3 = coords[2].y;
        double x4 = coords[3].x;
        double y4 = coords[3].y;

        if (x1 == x4 && y1 == y2 && x3 == x2 && y3 == y4) {
            // 1+-----+2
            //  |     |
            // 4+-----+3
            return true;
        }

        if (x1 == x2 && y1 == y4 && x3 == x4 && y3 == y2) {
            // 2+-----+3
            //  |     |
            // 1+-----+4
            return true;
        }

        return false;
    }

    /**
     * We need to check if a <code>polygon</code> is defined with curves so we can produce the correct encoding.
     *
     * <p>
     *
     * @return <code>false</code> as JTS does not support curves
     */
    private static boolean isCurve(Polygon polygon) {
        return false;
    }

    /**
     * We need to check if a <code>lineString</code> is defined with curves so we can produce the correct encoding.
     *
     * <p>
     *
     * @return <code>false</code> as JTS does not support curves
     */
    private static boolean isCurve(LineString lineString) {
        return false;
    }

    // Decoding Helper Functions
    //
    //
    /**
     * Returns a range from a CoordinateList, based on ELEM_INFO triplets.
     *
     * @param factory Factory used for JTS
     * @param coords Coordinates
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     */
    private static CoordinateSequence subList(
            CoordinateSequenceFactory factory,
            CoordinateSequence coords,
            int GTYPE,
            int[] elemInfo,
            int triplet,
            boolean compoundElement) {

        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, triplet);
        int ENDING_OFFSET = STARTING_OFFSET(elemInfo, triplet + 1); // -1 for end
        final int LEN = D(GTYPE);
        if (ENDING_OFFSET != -1 && compoundElement) {
            ENDING_OFFSET += LEN;
        }

        if (STARTING_OFFSET == 1 && ENDING_OFFSET == -1) {
            // Use all Cordiantes
            return coords;
        }

        int start = (STARTING_OFFSET - 1) / LEN;
        int end = ENDING_OFFSET != -1 ? (ENDING_OFFSET - 1) / LEN : coords.size();

        return subList(factory, coords, start, end);
    }

    /**
     * Version of List.subList() that returns a CoordinateSequence.
     *
     * <p>Returns from start (inclusive) to end (exlusive):
     *
     * <p>Math speak: <code>[start,end)</code>
     *
     * @param factory Manages CoordinateSequences for JTS
     * @param coords coords to sublist
     * @param start starting offset
     * @param end upper bound of sublist
     * @return CoordinateSequence
     */
    private static CoordinateSequence subList(
            CoordinateSequenceFactory factory, CoordinateSequence coords, int start, int end) {
        if (start == 0 && end == coords.size()) {
            return coords;
        }

        return Coordinates.subList(factory, coords, start, end);
    }

    private static LinearRing[] toInteriorRingArray(List list) {
        return (LinearRing[]) toArray(list, LinearRing.class);

        /*
          if( list == null ) return null;
          LinearRing array[] = new LinearRing[ list.size() ];
          int index=0;
          for( Iterator i=list.iterator(); i.hasNext(); index++ )
          {
              array[ index ] = (LinearRing) i.next();
          }
          return array;
        */
    }

    private static LineString[] toLineStringArray(List<LineString> list) {
        return (LineString[]) toArray(list, LineString.class);
    }

    private static Polygon[] toPolygonArray(List<Polygon> list) {
        return (Polygon[]) toArray(list, Polygon.class);
    }

    private static Geometry[] toGeometryArray(List<Geometry> list) {
        return (Geometry[]) toArray(list, Geometry.class);
    }

    /**
     * Useful function for converting to typed arrays for JTS API.
     *
     * <p>Example:
     *
     * <pre><code>
     * new MultiPoint( toArray( list, Coordinate.class ) );
     * </code></pre>
     */
    private static Object toArray(List list, Class type) {
        if (list == null) {
            return null;
        }

        Object array = Array.newInstance(type, list.size());
        int index = 0;
        for (Object o : list) {
            Array.set(array, index++, o);
        }

        return array;
    }

    /** Access D (for dimension) as encoded in GTYPE */
    public static int D(final int GTYPE) {
        return GTYPE / 1000;
    }

    /** Access L (for LRS) as encoded in GTYPE */
    public static int L(final int GTYPE) {
        return (GTYPE - D(GTYPE) * 1000) / 100;
    }

    /** Access TT (for geometry type) as encoded in GTYPE */
    public static int TT(final int GTYPE) {
        return GTYPE - D(GTYPE) * 1000 - L(GTYPE) * 100;
    }

    /**
     * Access STARTING_OFFSET from elemInfo, or -1 if not available.
     *
     * <p>
     */
    private static int STARTING_OFFSET(int[] elemInfo, int triplet) {
        if (triplet * 3 + 0 >= elemInfo.length) {
            return -1;
        }

        return elemInfo[triplet * 3 + 0];
    }

    /**
     * A version of assert that indicates range pre/post condition.
     *
     * <p>Works like assert exception IllegalArgumentException is thrown indicating this is a required check.
     *
     * <p>Example phrased as a positive statement of the requirement to be met:
     *
     * <pre><code>
     * ensure( "STARTING_OFFSET {1} must indicate a valid ordinate between {0} and {2}.
     * </code></pre>
     *
     * @param condition MessageFormat pattern - positive statement of requirement
     * @param min minimum acceptable value ({0} in message format)
     * @param actual value supplied ({1} in message format)
     * @param max maximum acceptable value ({2} in message format)
     * @throws IllegalArgumentException unless min <= actual <= max
     */
    private static void ensure(String condition, int min, int actual, int max) {
        if (!(min <= actual && actual <= max)) {
            String msg = MessageFormat.format(
                    condition, new Object[] {Integer.valueOf(min), Integer.valueOf(actual), Integer.valueOf(max)});
            throw new IllegalArgumentException(msg);
        }
    }
    /**
     * A version of assert that indicates range pre/post condition.
     *
     * <p>Works like assert exception IllegalArgumentException is thrown indicating this is a required check.
     *
     * <p>Example phrased as a positive statement of the requirement to be met:
     *
     * <pre><code>
     * ensure( "INTERPRETATION {0} must be on of {1}.
     * </code></pre>
     *
     * @param condition MessageFormat pattern - positive statement of requirement
     * @param actual value supplied ({0} in message format)
     * @param set Array of acceptable values ({1} in message format)
     * @throws IllegalArgumentException unless actual is a member of set
     */
    private static void ensure(String condition, int actual, int[] set) {
        if (set == null) return; // don't apparently care
        for (int j : set) {
            if (j == actual) return; // found it
        }
        StringBuffer array = new StringBuffer();
        for (int i = 0; i < set.length; i++) {
            array.append(set[i]);
            if (i < set.length) {
                array.append(",");
            }
        }
        String msg = MessageFormat.format(condition, new Object[] {Integer.valueOf(actual), array});
        throw new IllegalArgumentException(msg);
    }
    /**
     * Returns the "length" of the ordinate array used for the CoordinateSequence, GTYPE is used to determine the
     * dimension.
     *
     * <p>This is most often used to check the STARTING_OFFSET value to ensure that is falls within allowable bounds.
     *
     * <p>Example:
     *
     * <pre><code>
     * if (!(STARTING_OFFSET >= 1) ||
     *     !(STARTING_OFFSET <= ordinateSize( coords, GTYPE ))){
     *     throw new IllegalArgumentException(
     *         "ELEM_INFO STARTING_OFFSET "+STARTING_OFFSET+
     *         "inconsistent with COORDINATES length "+size( coords, GTYPE ) );
     * }
     * </code></pre>
     */
    private static int ordinateSize(CoordinateSequence coords, int GTYPE) {
        if (coords == null) {
            return 0;
        }
        return coords.size() * D(GTYPE);
    }
    /**
     * ETYPE access for the elemInfo triplet indicated.
     *
     * <p>
     *
     * @see ETYPE for an indication of possible values
     * @return ETYPE for indicated triplet
     */
    private static int ETYPE(int[] elemInfo, int triplet) {
        if (triplet * 3 + 1 >= elemInfo.length) {
            return -1;
        }

        return elemInfo[triplet * 3 + 1];
    }

    private static int INTERPRETATION(int[] elemInfo, int triplet) {
        if (triplet * 3 + 2 >= elemInfo.length) {
            return -1;
        }

        return elemInfo[triplet * 3 + 2];
    }

    /** Coordinates from <code>(x,y,x2,y2,...)</code> ordinates. */
    public static Coordinate[] asCoordinates(double[] ordinates) {
        return asCoordinates(ordinates, 2);
    }

    /** Coordinates from a <code>(x,y,i3..,id,x2,y2...)</code> ordinates. */
    public static Coordinate[] asCoordinates(double[] ordinates, int d) {
        int length = ordinates.length / d;
        Coordinate[] coords = new Coordinate[length];

        for (int i = 0; i < length; i++) {
            coords[i] = new Coordinate(ordinates[i * d], ordinates[i * d + 1]);
        }

        return coords;
    }

    /**
     * Construct CoordinateList as described by GTYPE.
     *
     * <p>GTYPE encodes the following information:
     *
     * <ul>
     *   <li>D: Dimension of ordinates
     *   <li>L: Ordinate that represents the LRS measure
     * </ul>
     *
     * <p>The number of ordinates per coordinate are taken to be D, and the number of ordinates should be a multiple of
     * this value.
     *
     * <p>In the Special case of GTYPE 2001 and a three ordinates are interpreted as a single Coordinate rather than an
     * error.
     *
     * <p>For 3-dimensional coordinates we assume z to be the third ordinate. If the LRS measure value is stored in the
     * third ordinate (L=3) we assume a 2-dimensional coordinate.
     *
     * @param f CoordinateSequenceFactory used to encode ordiantes for JTS
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     */
    public static CoordinateSequence coordinates(CoordinateSequenceFactory f, final int GTYPE, double[] ordinates) {
        if (ordinates == null || ordinates.length == 0) {
            return f.create(new Coordinate[0]);
        }

        final int D = SDO.D(GTYPE);
        final int L = SDO.L(GTYPE);
        final int TT = SDO.TT(GTYPE);

        //      POINT_TYPE Special Case
        //
        if (D == 2 && L == 0 && TT == 1) {
            CoordinateSequence cs = JTS.createCS(f, 1, 2);
            for (int i = 0; i < 2; i++) cs.setOrdinate(0, i, ordinates[i]);
            return cs;
        }

        final int LEN = D; // bugfix 20121231-BK: LEN = D instead of LEN = D + L as Oracle supports only one
        // LRS ordinate!

        if (ordinates.length % LEN != 0) {
            // bugfix 20121231-BK: LEN is D instead of D + L
            throw new IllegalArgumentException("Dimension D:"
                    + D
                    + " denote Coordinates "
                    + "of "
                    + LEN
                    + " ordinates. This cannot be resolved with"
                    + "an ordinate array of length "
                    + ordinates.length);
        }

        // bugfix 20121231-BK: throw exception if L > D (4 lines added)
        if (L != 0 && L > D) {
            throw new IllegalArgumentException(
                    "Dimension D:" + D + " and LRS with L: " + L + " is not supported at a position > D");
        }

        // special optimization for faster 2D rendering
        if (D == 2 && L == 0 && f instanceof LiteCoordinateSequenceFactory) {
            return ((LiteCoordinateSequenceFactory) f).create(ordinates);
        }

        OrdinateList x = new OrdinateList(ordinates, 0, LEN);
        OrdinateList y = new OrdinateList(ordinates, 1, LEN);
        OrdinateList z = null;

        // bugfix 20121231-BK: add z-Coordinate just if D >= 3 and L != 3
        if (D >= 3 && L != 3) {
            z = new OrdinateList(ordinates, 2, LEN);
        }

        if (L != 0) {
            // bugfix 20121231-BK: Oracle supports only one LRS ordinate! (removed 6 lines, added 2)
            OrdinateList m = new OrdinateList(ordinates, L - 1, LEN);

            // TODO org.geotools.geometry.jts.CoordinateSequenceFactory does not support 4
            // dimensions - thus we will get only 3 dimensions!
            return coordiantes(f, x, y, z, m);
        } else {
            return coordiantes(f, x, y, z);
        }
    }

    /**
     * Construct CoordinateSequence with no LRS measures.
     *
     * <p>To produce two dimension Coordinates pass in <code>null</code> for z
     */
    public static CoordinateSequence coordiantes(
            CoordinateSequenceFactory f, OrdinateList x, OrdinateList y, OrdinateList z) {
        final int LENGTH = x.size();
        // Coordinate[] array = new Coordinate[LENGTH];
        CoordinateSequence cs = JTS.createCS(f, LENGTH, z == null ? 2 : 3);

        if (z != null) {
            for (int i = 0; i < LENGTH; i++) {
                cs.setOrdinate(i, 0, x.getDouble(i));
                cs.setOrdinate(i, 1, y.getDouble(i));
                cs.setOrdinate(i, 2, z.getDouble(i));
            }
        } else {
            for (int i = 0; i < LENGTH; i++) {
                cs.setOrdinate(i, 0, x.getDouble(i));
                cs.setOrdinate(i, 1, y.getDouble(i));
            }
        }

        return cs;
    }

    /**
     * Construct CoordinateSequence with no LRS measures.
     *
     * <p>To produce two dimension Coordinates pass in <code>null</code> for z
     */
    public static CoordinateSequence coordiantes(
            CoordinateSequenceFactory f, AttributeList x, AttributeList y, AttributeList z) {
        final int LENGTH = x.size();
        Coordinate[] array = new Coordinate[LENGTH];

        if (z != null) {
            for (int i = 0; i < LENGTH; i++) {
                array[i] = new Coordinate(x.getDouble(i), y.getDouble(i), z.getDouble(i));
            }
        } else {
            for (int i = 0; i < LENGTH; i++) {
                array[i] = new Coordinate(x.getDouble(i), y.getDouble(i));
            }
        }

        return f.create(array);
    }

    /**
     * bugfix 20121231-BK: added: TODO org.geotools.geometry.jts.CoordinateSequenceFactory does not support 4
     * dimensions! Construct CoordinateSequence with LRS measures.
     *
     * <p>To produce three dimension Coordinates pass in <code>null</code> for z
     *
     * @param f {@link CoordinateSequenceFactory}
     * @param x x-ordinates
     * @param y y-ordinates
     * @param z z-ordinates, <code>null</code> for 2D
     * @param m m-ordinates
     * @return {@link CoordinateSequence}
     */
    public static CoordinateSequence coordiantes(
            CoordinateSequenceFactory f, OrdinateList x, OrdinateList y, OrdinateList z, OrdinateList m) {
        final int LENGTH = x.size();
        // TODO org.geotools.geometry.jts.LiteCoordinateSequenceFactory does not support 4
        // dimensions!
        // CoordinateSequence cs = f.create(LENGTH, z == null ? 3: 4);
        CoordinateSequence cs = JTS.createCS(f, LENGTH, z == null ? 2 : 3);

        if (z != null) {
            for (int i = 0; i < LENGTH; i++) {
                cs.setOrdinate(i, 0, x.getDouble(i));
                cs.setOrdinate(i, 1, y.getDouble(i));
                cs.setOrdinate(i, 2, z.getDouble(i));
                // cs.setOrdinate(i, 3, m.getDouble(i));
            }
        } else {
            for (int i = 0; i < LENGTH; i++) {
                cs.setOrdinate(i, 0, x.getDouble(i));
                cs.setOrdinate(i, 1, y.getDouble(i));
                // cs.setOrdinate(i, 2, m.getDouble(i));
            }
        }

        return cs;
    }

    /**
     * Decode geometry from provided SDO encoded information.
     *
     * <p>
     *
     * @param gf Used to construct returned Geometry
     * @param GTYPE SDO_GTYPE represents dimension, LRS, and geometry type
     * @param SRID SDO_SRID represents Spatial Reference System
     * @return Geometry as encoded
     */
    public static Geometry create(
            GeometryFactory gf, final int GTYPE, final int SRID, double[] point, int[] elemInfo, double[] ordinates) {
        final int L = SDO.L(GTYPE);
        final int TT = SDO.TT(GTYPE);

        CoordinateSequence coords;

        if (L == 0 && TT == 01 && point != null && elemInfo == null) {
            // Single Point Type Optimization
            coords = SDO.coordinates(gf.getCoordinateSequenceFactory(), GTYPE, point);
            elemInfo = new int[] {1, ETYPE.POINT, 1};
        } else {
            int element = 0;
            int etype = ETYPE(elemInfo, element);
            if (etype == 0) {
                // complex type, search for encapsulated simpletype (with etype != 0)
                int startpointCoordinates = 0;

                // look for a simple one
                while (etype == 0) {
                    element++;
                    etype = ETYPE(elemInfo, element);
                    startpointCoordinates = STARTING_OFFSET(elemInfo, element);
                }

                // if we found the simple fallback, read it
                if (etype != -1) {
                    int ol = ordinates.length;
                    int elemsToCopy = ol - (startpointCoordinates - 1);
                    double[] newOrdinates = new double[elemsToCopy];
                    System.arraycopy(ordinates, startpointCoordinates - 1, newOrdinates, 0, elemsToCopy);
                    elemInfo = new int[] {1, etype, INTERPRETATION(elemInfo, element)};
                    ordinates = newOrdinates;
                }
            }
            coords = SDO.coordinates(gf.getCoordinateSequenceFactory(), GTYPE, ordinates);
        }

        return create(gf, GTYPE, SRID, elemInfo, 0, coords, -1);
    }

    /**
     * Consturct geometry with SDO encoded information over a CoordinateList.
     *
     * <p>Helpful when dealing construction Geometries with your own Coordinate Types. The dimensionality specified in
     * GTYPE will be used to interpret the offsets in elemInfo.
     *
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param N Number of triplets (-1 for unknown/don't care)
     * @return Geometry as encoded, or null w/ log if it cannot be represented via JTS
     */
    public static Geometry create(
            GeometryFactory gf,
            final int GTYPE,
            final int SRID,
            final int[] elemInfo,
            final int triplet,
            CoordinateSequence coords,
            final int N) {
        CurvedGeometryFactory curvedFactory = getCurvedGeometryFactory(gf);

        switch (SDO.TT(GTYPE)) {
            case TT.POINT:
                return createPoint(curvedFactory, GTYPE, SRID, elemInfo, triplet, coords);

            case TT.LINE:
                return createLine(curvedFactory, GTYPE, SRID, elemInfo, triplet, coords, false);

            case TT.POLYGON:
                return createPolygon(curvedFactory, GTYPE, SRID, elemInfo, triplet, coords);

            case TT.MULTIPOINT:
                return createMultiPoint(curvedFactory, GTYPE, SRID, elemInfo, triplet, coords);

            case TT.MULTILINE:
                return createMultiLine(curvedFactory, GTYPE, SRID, elemInfo, triplet, coords, N);

            case TT.MULTIPOLYGON:
                return createMultiPolygon(curvedFactory, GTYPE, SRID, elemInfo, triplet, coords, N, false);

            case TT.COLLECTION:
                return createCollection(curvedFactory, GTYPE, SRID, elemInfo, triplet, coords, N);

            case TT.SOLID:
                return createMultiPolygon(curvedFactory, GTYPE, SRID, elemInfo, triplet, coords, N, true);

            case TT.UNKNOWN:
            default:
                LOGGER.warning("Cannot represent provided SDO STRUCT (GTYPE =" + GTYPE + ") using JTS Geometry");
                return null;
        }
    }

    /**
     * Casts the provided geometry factory to a curved one if possible, or wraps it into one with infinite tolerance
     * (the linearization will happen using the default base segments number set in {@link CircularArc}
     */
    private static CurvedGeometryFactory getCurvedGeometryFactory(GeometryFactory gf) {
        CurvedGeometryFactory curvedFactory;
        if (gf instanceof CurvedGeometryFactory) {
            curvedFactory = (CurvedGeometryFactory) gf;
        } else {
            curvedFactory = new CurvedGeometryFactory(gf, Double.MAX_VALUE);
        }
        return curvedFactory;
    }

    /**
     * Create Point as encoded.
     *
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @return Point
     */
    private static Point createPoint(
            GeometryFactory gf,
            final int GTYPE,
            final int SRID,
            final int[] elemInfo,
            final int element,
            CoordinateSequence coords) {
        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, element);
        final int etype = ETYPE(elemInfo, element);
        final int INTERPRETATION = INTERPRETATION(elemInfo, element);
        final int LENGTH = coords.size() * D(GTYPE);

        if (!(STARTING_OFFSET >= 1) || !(STARTING_OFFSET <= LENGTH))
            throw new IllegalArgumentException("Invalid ELEM_INFO STARTING_OFFSET ");
        if (etype != ETYPE.POINT)
            throw new IllegalArgumentException("ETYPE " + etype + " inconsistent with expected POINT");
        if (INTERPRETATION != 1) {
            LOGGER.warning("Could not create JTS Point with INTERPRETATION "
                    + INTERPRETATION
                    + " - we only expect 1 for a single point");
            return null;
        }

        Point point =
                gf.createPoint(subList(gf.getCoordinateSequenceFactory(), coords, GTYPE, elemInfo, element, false));

        // Point point = gf.createPoint( coords.getCoordinate( index ) );
        point.setSRID(SRID);

        return point;
    }

    /**
     * Create LineString as encoded.
     *
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param compoundElement TODO
     * @throws IllegalArgumentException If asked to create a curve
     */
    private static LineString createLine(
            CurvedGeometryFactory gf,
            final int GTYPE,
            final int SRID,
            final int[] elemInfo,
            final int triplet,
            CoordinateSequence coords,
            boolean compoundElement) {
        final int etype = ETYPE(elemInfo, triplet);
        final int INTERPRETATION = INTERPRETATION(elemInfo, triplet);

        if (etype != ETYPE.LINE && etype != ETYPE.COMPOUND) return null;

        LineString result;
        if (etype == ETYPE.LINE && INTERPRETATION == 1) {
            CoordinateSequence subList =
                    subList(gf.getCoordinateSequenceFactory(), coords, GTYPE, elemInfo, triplet, compoundElement);
            result = gf.createLineString(subList);
        } else if (etype == ETYPE.LINE && INTERPRETATION == 2) {
            CoordinateSequence subList =
                    subList(gf.getCoordinateSequenceFactory(), coords, GTYPE, elemInfo, triplet, compoundElement);
            result = gf.createCurvedGeometry(subList);
        } else if (etype == ETYPE.COMPOUND) {
            int triplets = INTERPRETATION;
            List<LineString> components = new ArrayList<>(triplets);
            for (int i = 1; i <= triplets; i++) {
                LineString component = createLine(gf, GTYPE, SRID, elemInfo, triplet + i, coords, true);
                components.add(component);
            }
            result = gf.createCurvedGeometry(components);
        } else {
            throw new IllegalArgumentException("ELEM_INFO ETYPE "
                    + etype
                    + " with INTERPRETAION "
                    + INTERPRETATION
                    + " not supported by this decoder");
        }
        result.setSRID(SRID);

        return result;
    }

    /**
     * Create Polygon as encoded.
     *
     * <p>Encoded as a one or more triplets in elemInfo:
     *
     * <ul>
     *   <li>Exterior Polygon Ring: first triplet:
     *       <ul>
     *         <li>STARTING_OFFSET: position in ordinal ordinate array
     *         <li>ETYPE: 1003 (exterior) or 3 (polygon w/ counter Orientationwise ordinates)
     *         <li>INTERPRETATION: 1 for strait edges, 3 for rectanlge
     *       </ul>
     *   <li>Interior Polygon Ring(s): remaining triplets:
     *       <ul>
     *         <li>STARTING_OFFSET: position in ordinal ordinate array
     *         <li>ETYPE: 2003 (interior) or 3 (polygon w/ OrientationWise ordinates)
     *         <li>INTERPRETATION: 1 for strait edges, 3 for rectanlge
     *       </ul>
     * </ul>
     *
     * <p>The polygon encoding will process subsequent 2003, or 3 triples with Orientationwise ordering as interior
     * holes.
     *
     * <p>A subsequent triplet of any other type marks the end of the polygon.
     *
     * <p>The dimensionality of GTYPE will be used to transalte the <code>STARTING_OFFSET</code> provided by elemInfo
     * into an index into <code>coords</code>.
     *
     * @param gf Used to construct polygon
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param SRID Spatial Reference System
     * @param elemInfo Interpretation of coords
     * @param triplet Triplet in elemInfo to process as a Polygon
     * @param coords Coordinates to interpret using elemInfo
     * @return Polygon as encoded by elemInfo, or null when faced with and encoding that can not be captured by JTS
     * @throws IllegalArgumentException When faced with an invalid SDO encoding
     */
    private static Polygon createPolygon(
            CurvedGeometryFactory gf,
            final int GTYPE,
            final int SRID,
            final int[] elemInfo,
            int triplet,
            CoordinateSequence coords)
            throws IllegalArgumentException {
        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, triplet);
        final int eTYPE = ETYPE(elemInfo, triplet);
        final int INTERPRETATION = INTERPRETATION(elemInfo, triplet);

        ensure(
                "ELEM_INFO STARTING_OFFSET {1} must be in the range {0}..{1} of COORDINATES",
                1, STARTING_OFFSET, ordinateSize(coords, GTYPE));
        if (!(1 <= STARTING_OFFSET && STARTING_OFFSET <= ordinateSize(coords, GTYPE))) {
            throw new IllegalArgumentException("ELEM_INFO STARTING_OFFSET "
                    + STARTING_OFFSET
                    + "inconsistent with COORDINATES length "
                    + ordinateSize(coords, GTYPE));
        }
        ensure(
                "ETYPE {0} must be expected POLYGON or POLYGON_EXTERIOR (one of {1})",
                eTYPE, new int[] {
                    ETYPE.COMPOUND_POLYGON_EXTERIOR,
                    ETYPE.COMPOUND_POLYGON,
                    ETYPE.POLYGON,
                    ETYPE.POLYGON_EXTERIOR,
                    ETYPE.FACE_EXTERIOR
                });
        if (eTYPE != ETYPE.COMPOUND_POLYGON_EXTERIOR
                && eTYPE != ETYPE.COMPOUND_POLYGON
                && (INTERPRETATION < 1 || INTERPRETATION > 4)) {
            LOGGER.warning("Could not create JTS Polygon with INTERPRETATION "
                    + INTERPRETATION
                    + " "
                    + "- we can only support 1 for straight edges, 2 for circular ones, "
                    + "3 for rectangle and 4 for circles");
            return null;
        }

        LinearRing exteriorRing = createLinearRing(gf, GTYPE, SRID, elemInfo, triplet, coords);
        if (eTYPE == ETYPE.COMPOUND_POLYGON_EXTERIOR) {
            triplet = triplet + elemInfo[2];
        }

        List<LinearRing> rings = new ArrayList<>();
        int etype;
        HOLES:
        for (int i = triplet + 1; (etype = ETYPE(elemInfo, i)) != -1; ) {
            if (etype == ETYPE.POLYGON_INTERIOR) {
                rings.add(createLinearRing(gf, GTYPE, SRID, elemInfo, i, coords));
                i++;
            } else if (etype == ETYPE.COMPOUND_POLYGON_INTERIOR) {
                int subelements = INTERPRETATION(elemInfo, i);
                rings.add(createLinearRing(gf, GTYPE, SRID, elemInfo, i, coords));
                i = i + subelements + 1;
            } else if (etype == ETYPE.POLYGON) { // nead to test Orientationwiseness of Ring to see if it
                // is
                // interior or not - (use POLYGON_INTERIOR to avoid
                // pain)

                LinearRing ring = createLinearRing(gf, GTYPE, SRID, elemInfo, i, coords);

                if (Orientation.isCCW(ring.getCoordinates())) { // it is an Interior Hole
                    rings.add(ring);
                    i++;
                } else { // it is the next Polygon! - get out of here

                    break HOLES;
                }
            } else { // not a LinearRing - get out of here

                break HOLES;
            }
        }

        Polygon poly = gf.createPolygon(exteriorRing, toInteriorRingArray(rings));
        poly.setSRID(SRID);

        return poly;
    }

    /**
     * Create Linear Ring for exterior/interior polygon ELEM_INFO triplets.
     *
     * <p>Encoded as a single triplet in elemInfo:
     *
     * <ul>
     *   <li>STARTING_OFFSET: position in ordinal ordinate array
     *   <li>ETYPE: 1003 (exterior) or 2003 (interior) or 3 (unknown order)
     *   <li>INTERPRETATION: 1 for strait edges, 3 for rectanlge
     * </ul>
     *
     * <p>The dimensionality of GTYPE will be used to transalte the <code>STARTING_OFFSET</code> provided by elemInfo
     * into an index into <code>coords</code>.
     *
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @return LinearRing
     * @throws IllegalArgumentException If circle, or curve is requested
     */
    private static LinearRing createLinearRing(
            CurvedGeometryFactory gf,
            final int GTYPE,
            final int SRID,
            final int[] elemInfo,
            final int triplet,
            CoordinateSequence coords) {

        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, triplet);
        final int eTYPE = ETYPE(elemInfo, triplet);
        final int INTERPRETATION = INTERPRETATION(elemInfo, triplet);
        final int LENGTH = coords.size() * D(GTYPE);

        if (!(STARTING_OFFSET >= 1) || !(STARTING_OFFSET <= LENGTH))
            throw new IllegalArgumentException("ELEM_INFO STARTING_OFFSET "
                    + STARTING_OFFSET
                    + " inconsistent with ORDINATES length "
                    + coords.size());

        ensure(
                "ETYPE {0} must be expected POLYGON or POLYGON_EXTERIOR (one of {1})",
                eTYPE, new int[] {
                    ETYPE.COMPOUND_POLYGON,
                    ETYPE.COMPOUND_POLYGON_EXTERIOR,
                    ETYPE.COMPOUND_POLYGON_INTERIOR,
                    ETYPE.POLYGON,
                    ETYPE.POLYGON_EXTERIOR,
                    ETYPE.POLYGON_INTERIOR,
                    ETYPE.FACE_EXTERIOR
                });
        if (eTYPE != ETYPE.COMPOUND_POLYGON_EXTERIOR
                && eTYPE != ETYPE.COMPOUND_POLYGON
                && eTYPE != ETYPE.COMPOUND_POLYGON_INTERIOR
                && (INTERPRETATION < 1 || INTERPRETATION > 4)) {
            LOGGER.warning("Could not create LinearRing with INTERPRETATION "
                    + INTERPRETATION
                    + " - we can only support 1, 2, 3 and 4");
            return null;
        }
        LinearRing ring;

        if (eTYPE == ETYPE.COMPOUND_POLYGON_EXTERIOR || eTYPE == ETYPE.COMPOUND_POLYGON_INTERIOR) {
            int triplets = INTERPRETATION;
            List<LineString> components = new ArrayList<>(triplets);
            for (int i = 1; i <= triplets; i++) {
                LineString component = createLine(gf, GTYPE, SRID, elemInfo, triplet + i, coords, i < triplets);
                components.add(component);
            }
            ring = (LinearRing) gf.createCurvedGeometry(components);
        } else if (INTERPRETATION == 1) {
            CoordinateSequence coordSeq =
                    subList(gf.getCoordinateSequenceFactory(), coords, GTYPE, elemInfo, triplet, false);
            coordSeq = CoordinateSequences.ensureValidRing(gf.getCoordinateSequenceFactory(), coordSeq);
            ring = gf.createLinearRing(coordSeq);
        } else if (INTERPRETATION == 2) {
            CoordinateSequence coordSeq =
                    subList(gf.getCoordinateSequenceFactory(), coords, GTYPE, elemInfo, triplet, false);
            // not calling ensureValidRing, according to Oracle docs tolerance is not considered:
            // For example, five coordinates are used to describe a polygon made up of two connected
            // circular arcs. Points 1, 2, and 3 define the first arc, and points 3, 4, and 5 define
            // the second arc. The coordinates for points 1 and 5 must be the same
            // (tolerance is not considered), and point 3 is not repeated.

            ring = (LinearRing) gf.createCurvedGeometry(coordSeq);
        } else if (INTERPRETATION == 3) {
            // rectangle does not maintain measures
            //
            CoordinateSequence ext =
                    subList(gf.getCoordinateSequenceFactory(), coords, GTYPE, elemInfo, triplet, false);
            Coordinate min = ext.getCoordinate(0);
            Coordinate max = ext.getCoordinate(1);
            ring = gf.createLinearRing(
                    new Coordinate[] {min, new Coordinate(max.x, min.y), max, new Coordinate(min.x, max.y), min});
        } else if (INTERPRETATION == 4) {
            // rectangle does not maintain measures
            //
            CoordinateSequence ext =
                    subList(gf.getCoordinateSequenceFactory(), coords, GTYPE, elemInfo, triplet, false);
            if (ext.size() != 3) {
                throw new IllegalArgumentException(
                        "The coordinate sequence for the circle creation must contain 3 points, the one at hand contains "
                                + ext.size()
                                + " instead");
            }
            double[] cp = new double[ext.size() * 2 + 2];
            for (int i = 0; i < ext.size(); i++) {
                cp[i * 2] = ext.getOrdinate(i, 0);
                cp[i * 2 + 1] = ext.getOrdinate(i, 1);
            }
            // figure out the other point
            CircularArc arc = new CircularArc(cp[0], cp[1], cp[2], cp[3], cp[4], cp[5]);
            ring = CurvedGeometries.toCircle(arc, gf, gf.getTolerance());
        } else {
            throw new IllegalArgumentException("ELEM_INFO INTERPRETAION "
                    + elemInfo[2]
                    + " not supported"
                    + "for JTS Polygon Linear Rings."
                    + "ELEM_INFO INTERPRETATION 1,2 and 3 are supported");
        }

        ring.setSRID(SRID);

        return ring;
    }

    /**
     * Create MultiPoint as encoded by elemInfo.
     *
     * <p>Encoded as a single triplet in elemInfo:
     *
     * <ul>
     *   <li>STARTING_OFFSET: position in ordinal ordinate array
     *   <li>ETYPE: 1 for Point
     *   <li>INTERPRETATION: number of points
     * </ul>
     *
     * @param gf Used to construct polygon
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param SRID Spatial Reference System
     * @param elemInfo Interpretation of coords
     * @param triplet Triplet in elemInfo to process as a Polygon
     * @param coords Coordinates to interpret using elemInfo
     */
    private static MultiPoint createMultiPoint(
            GeometryFactory gf,
            final int GTYPE,
            final int SRID,
            final int[] elemInfo,
            final int triplet,
            CoordinateSequence coords) {
        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, triplet);
        final int eTYPE = ETYPE(elemInfo, triplet);
        final int INTERPRETATION = INTERPRETATION(elemInfo, triplet);
        final int LENGTH = coords.size() * D(GTYPE);

        if (!(STARTING_OFFSET >= 1) || !(STARTING_OFFSET <= LENGTH))
            throw new IllegalArgumentException("ELEM_INFO STARTING_OFFSET "
                    + STARTING_OFFSET
                    + " inconsistent with ORDINATES length "
                    + coords.size());
        if (!(eTYPE == ETYPE.POINT))
            throw new IllegalArgumentException("ETYPE " + eTYPE + " inconsistent with expected POINT");
        // CH- changed to >= 1, for GEOS-437, Jody and I looked at docs
        // and multipoint is a superset of point, so it should be fine,
        // for cases when there is just one point.  Bart is testing.
        if (!(INTERPRETATION >= 1)) {
            LOGGER.warning("Could not create MultiPoint with INTERPRETATION "
                    + INTERPRETATION
                    + " - representing the number of points");
            return null;
        }

        final int LEN = D(GTYPE);

        int start = (STARTING_OFFSET - 1) / LEN;
        int end = start + INTERPRETATION;

        MultiPoint points = gf.createMultiPoint(subList(gf.getCoordinateSequenceFactory(), coords, start, end));
        points.setSRID(SRID);

        return points;
    }

    /**
     * Create MultiLineString as encoded by elemInfo.
     *
     * <p>Encoded as a series line of triplets in elemInfo:
     *
     * <ul>
     *   <li>STARTING_OFFSET: position in ordinal ordinate array
     *   <li>ETYPE: 2 for Line
     *   <li>INTERPRETATION: 1 for straight edges
     * </ul>
     *
     * <p>
     *
     * @param gf Used to construct MultiLineString
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param SRID Spatial Reference System
     * @param elemInfo Interpretation of coords
     * @param triplet Triplet in elemInfo to process as a Polygon
     * @param coords Coordinates to interpret using elemInfo
     * @param N Number of triplets (or -1 for rest)
     */
    private static MultiLineString createMultiLine(
            CurvedGeometryFactory gf,
            final int GTYPE,
            final int SRID,
            final int[] elemInfo,
            final int triplet,
            CoordinateSequence coords,
            final int N) {
        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, triplet);
        final int eTYPE = ETYPE(elemInfo, triplet);
        final int INTERPRETATION = INTERPRETATION(elemInfo, triplet);

        final int LENGTH = coords.size() * D(GTYPE);

        if (!(STARTING_OFFSET >= 1) || !(STARTING_OFFSET <= LENGTH))
            throw new IllegalArgumentException("ELEM_INFO STARTING_OFFSET "
                    + STARTING_OFFSET
                    + " inconsistent with ORDINATES length "
                    + coords.size());
        if (!(eTYPE == ETYPE.LINE))
            throw new IllegalArgumentException("ETYPE " + eTYPE + " inconsistent with expected LINE");
        if (!(INTERPRETATION == 1)) {
            // we cannot represent INTERPRETATION > 1
            LOGGER.warning("Could not create MultiLineString with INTERPRETATION "
                    + INTERPRETATION
                    + " - we can only represent 1 for straight edges");
            return null;
        }

        // final int LEN = D(GTYPE);
        final int endTriplet = N != -1 ? triplet + N : elemInfo.length / 3;

        List<LineString> list = new LinkedList<>();
        int etype;
        LINES: // bad bad gotos jody
        for (int i = triplet; i < endTriplet && (etype = ETYPE(elemInfo, i)) != -1; i++) {
            if (etype == ETYPE.LINE) {
                list.add(createLine(gf, GTYPE, SRID, elemInfo, i, coords, false));
            } else { // not a LinearString - get out of here

                break LINES; // goto LINES
            }
        }

        MultiLineString lines = gf.createMultiLineString(toLineStringArray(list));
        lines.setSRID(SRID);

        return lines;
    }

    /**
     * Create MultiPolygon as encoded by elemInfo.
     *
     * <p>Encoded as a series polygon triplets in elemInfo:
     *
     * <ul>
     *   <li>STARTING_OFFSET: position in ordinal ordinate array
     *   <li>ETYPE: 2003 or 3 for Polygon
     *   <li>INTERPRETATION: 1 for straight edges, 3 for rectangle
     * </ul>
     *
     * <p>
     *
     * @param gf Used to construct MultiLineString
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param SRID Spatial Reference System
     * @param elemInfo Interpretation of coords
     * @param triplet Triplet in elemInfo to process as a Polygon
     * @param coords Coordinates to interpret using elemInfo
     * @param N Number of triplets (or -1 for rest)
     */
    private static MultiPolygon createMultiPolygon(
            CurvedGeometryFactory gf,
            final int GTYPE,
            final int SRID,
            final int[] elemInfo,
            final int triplet,
            CoordinateSequence coords,
            final int N,
            boolean threeDimensional) {
        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, triplet);
        final int eTYPE = ETYPE(elemInfo, triplet);
        final int INTERPRETATION = INTERPRETATION(elemInfo, triplet);
        final int LENGTH = coords.size() * D(GTYPE);

        if (!(STARTING_OFFSET >= 1) || !(STARTING_OFFSET <= LENGTH))
            throw new IllegalArgumentException("ELEM_INFO STARTING_OFFSET "
                    + STARTING_OFFSET
                    + " inconsistent with ORDINATES length "
                    + coords.size());
        if (!(eTYPE == ETYPE.POLYGON)
                && !(eTYPE == ETYPE.POLYGON_EXTERIOR)
                && !(eTYPE == ETYPE.FACE_EXTERIOR)
                && !(eTYPE == ETYPE.FACE_INTERIOR)
                && !(eTYPE == ETYPE.COMPOUND_POLYGON)
                && !(eTYPE == ETYPE.COMPOUND_POLYGON_EXTERIOR))
            throw new IllegalArgumentException(
                    "ETYPE " + eTYPE + " inconsistent with expected POLYGON or POLYGON_EXTERIOR");
        if (!(eTYPE == ETYPE.COMPOUND_POLYGON)
                && !(eTYPE == ETYPE.COMPOUND_POLYGON_EXTERIOR)
                && INTERPRETATION != 1
                && INTERPRETATION != 3) {
            LOGGER.warning("Could not create MultiPolygon with INTERPRETATION "
                    + INTERPRETATION
                    + " - we can only represent 1 for straight edges, or 3 for rectangle");
            return null;
        }
        final int endTriplet = N != -1 ? triplet + N : elemInfo.length / 3 + 1;

        List<Polygon> list = new LinkedList<>();
        int etype;
        POLYGONS:
        for (int i = triplet; i < endTriplet && (etype = ETYPE(elemInfo, i)) != -1; i++) {
            if (etype == ETYPE.POLYGON
                    || etype == ETYPE.POLYGON_EXTERIOR
                    || etype == ETYPE.FACE_EXTERIOR
                    || etype == ETYPE.FACE_INTERIOR) {
                Polygon poly = createPolygon(gf, GTYPE, SRID, elemInfo, i, coords);
                i += poly.getNumInteriorRing(); // skip interior rings
                list.add(poly);
            } else if (etype == ETYPE.COMPOUND_POLYGON_EXTERIOR || etype == ETYPE.COMPOUND_POLYGON) {
                Polygon poly = createPolygon(gf, GTYPE, SRID, elemInfo, i, coords);
                int curvilinearElementsCount = getCurvilinearElementsCount(poly);
                i += curvilinearElementsCount - 1;
                list.add(poly);
            } else { // not a Polygon - get out here

                break POLYGONS;
            }
        }

        MultiPolygon polys = gf.createMultiPolygon(toPolygonArray(list));
        polys.setSRID(SRID);

        return polys;
    }

    private static int getCurvilinearElementsCount(Polygon poly) {
        int sum = getCurvilinearElementsCount(poly.getExteriorRing());
        for (int i = 0; i < poly.getNumInteriorRing(); i++) {
            sum += getCurvilinearElementsCount(poly.getInteriorRingN(i));
        }
        return sum;
    }

    private static int getCurvilinearElementsCount(LineString ls) {
        if (ls instanceof CompoundCurvedGeometry<?>) {
            CompoundCurvedGeometry<?> curved = (CompoundCurvedGeometry<?>) ls;
            // take into account the elemInfo describing the compound
            return curved.getComponents().size() + 1;
        } else {
            return 1;
        }
    }

    /**
     * Create MultiPolygon as encoded by elemInfo.
     *
     * <p>Encoded as a series polygon triplets in elemInfo:
     *
     * <ul>
     *   <li>STARTING_OFFSET: position in ordinal ordinate array
     *   <li>ETYPE: 2003 or 3 for Polygon
     *   <li>INTERPRETATION: 1 for straight edges, 2 for rectangle
     * </ul>
     *
     * <p>TODO: Confirm that createCollection is not getting cut&paste mistakes from polygonCollection
     *
     * @param gf Used to construct MultiLineString
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param SRID Spatial Reference System
     * @param elemInfo Interpretation of coords
     * @param triplet Triplet in elemInfo to process as a Polygon
     * @param coords Coordinates to interpret using elemInfo
     * @param N Number of triplets (or -1 for rest)
     * @return GeometryCollection
     * @throws IllegalArgumentException DWhen faced with an encoding error
     */
    private static GeometryCollection createCollection(
            CurvedGeometryFactory gf,
            final int GTYPE,
            final int SRID,
            final int[] elemInfo,
            final int triplet,
            CoordinateSequence coords,
            final int N) {
        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, triplet);

        final int LENGTH = coords.size() * D(GTYPE);

        if (!(STARTING_OFFSET >= 1) || !(STARTING_OFFSET <= LENGTH))
            throw new IllegalArgumentException("ELEM_INFO STARTING_OFFSET "
                    + STARTING_OFFSET
                    + " inconsistent with ORDINATES length "
                    + coords.size());

        final int endTriplet = N != -1 ? triplet + N : elemInfo.length / 3 + 1;

        List<Geometry> list = new LinkedList<>();
        int etype;
        int interpretation;
        Geometry geom;

        GEOMETRYS:
        for (int i = triplet; i < endTriplet; i++) {
            etype = ETYPE(elemInfo, i);
            interpretation = INTERPRETATION(elemInfo, i);

            switch (etype) {
                case -1:
                    break GEOMETRYS; // We are the of the list - get out of here

                case ETYPE.POINT:
                    if (interpretation == 1) {
                        geom = createPoint(gf, GTYPE, SRID, elemInfo, i, coords);
                    } else if (interpretation > 1) {
                        geom = createMultiPoint(gf, GTYPE, SRID, elemInfo, i, coords);
                    } else {
                        throw new IllegalArgumentException("ETYPE.POINT requires INTERPRETATION >= 1");
                    }

                    break;

                case ETYPE.LINE:
                    geom = createLine(gf, GTYPE, SRID, elemInfo, i, coords, false);

                    break;

                case ETYPE.POLYGON:
                case ETYPE.POLYGON_EXTERIOR:
                    geom = createPolygon(gf, GTYPE, SRID, elemInfo, i, coords);
                    i += ((Polygon) geom).getNumInteriorRing();

                    break;

                case ETYPE.POLYGON_INTERIOR:
                    throw new IllegalArgumentException(
                            "ETYPE 2003 (Polygon Interior) no expected in a GeometryCollection"
                                    + "(2003 is used to represent polygon holes, in a 1003 polygon exterior)");

                case ETYPE.CUSTOM:
                case ETYPE.COMPOUND:
                case ETYPE.COMPOUND_POLYGON:
                case ETYPE.COMPOUND_POLYGON_EXTERIOR:
                case ETYPE.COMPOUND_POLYGON_INTERIOR:
                default:
                    throw new IllegalArgumentException("ETYPE "
                            + etype
                            + " not representable as a JTS Geometry."
                            + "(Custom and Compound Straight and Curved Geometries not supported)");
            }

            list.add(geom);
        }

        GeometryCollection geoms = gf.createGeometryCollection(toGeometryArray(list));
        geoms.setSRID(SRID);

        return geoms;
    }
}
