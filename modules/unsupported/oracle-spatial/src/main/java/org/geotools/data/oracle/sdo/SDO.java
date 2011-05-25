/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.vividsolutions.jts.algorithm.RobustCGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


/**
 * Oracle Spatial Data Object utilities functions.
 * 
 * <p>
 * Provide utility functions for working with JTS Geometries in terms Oracle
 * Spatial Data Objects
 * </p>
 *
 * <p>
 * This class can be used for normal JTS Geometry persistence with little fuss
 * and bother - please see GeometryConverter for an example of this.
 * <p>
 * With a little fuss and bother LRS information can also be handled.
 * Although it is very rare that JTS makes use of such things. 
 * </p>
 * @see <a href="http://otn.oracle.com/pls/db10g/db10g.to_toc?pathname=appdev.101%2Fb10826%2Ftoc.htm&remark=portal+%28Unstructured+data%29">Spatial User's Guide (10.1)</a>
 * @author Jody Garnett, Refractions Reasearch Inc.
 *
 * @source $URL$
 * @version CVS Version
 *
 * @see net.refractions.jspatial.jts
 */
public final class SDO {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.oracle.sdo");
    public static final int SRID_NULL = -1;

    /** Used to test for Counter Clockwise or Clockwise Linear Rings */
    private static RobustCGAlgorithms clock = new RobustCGAlgorithms();

    // 
    // Encoding Helper Functions
    //

    /**
     * Produce SDO_GTYPE representing provided Geometry.
     * 
     * <p>
     * Encoding of Geometry type and dimension.
     * </p>
     * 
     * <p>
     * SDO_GTYPE defined as for digits <code>[d][l][tt]</code>:
     * </p>
     * 
     * <ul>
     * <li>
     * <b><code>d</code>:</b> number of dimensions (limited to 2,3, or 4)
     * </li>
     * <li>
     * <b><code>l</code>:</b> measure representation (ordinate 3 or 4) or 0 to
     * represent none/last
     * </li>
     * <li>
     * <b><code>tt:</code></b> See the TT. constants defined in this class
     * </li>
     * </ul>
     * 
     * <p>
     * Definition provided by Oracle Spatial Userï¿½s Guide and Reference.
     * </p>
     *
     * @param geom
     *
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
     * <p>
     * For normal JTS Geometry this will be 2 or 3 depending if
     * geom.getCoordinate.z is Double.NaN.
     * </p>
     * 
     * <p>
     * Subclasses may override as required.
     * </p>
     *
     * @param geom
     *
     * @return <code>3</code>
     */
    public static int D(Geometry geom) {
        CoordinateSequenceFactory f = geom.getFactory()
                                          .getCoordinateSequenceFactory();

        if (f instanceof CoordinateAccessFactory) {
            return ((CoordinateAccessFactory) f).getDimension();
        } else {
            //return 3;
            return Double.isNaN(geom.getCoordinate().z) ? 2 : 3;
        }
    }

    /**
     * Return L as defined by SDO_GTYPE (either 3,4 or 0).
     * 
     * <p>
     * L represents support for LRS (Liniar Referencing System?). JTS Geometry
     * objects do not support LRS so this will be 0.
     * </p>
     * 
     * <p>
     * Subclasses may override as required.
     * </p>
     *
     * @param geom
     *
     * @return <code>0</code>
     */
    public static int L(Geometry geom) {
        CoordinateSequenceFactory f = geom.getFactory()
                                          .getCoordinateSequenceFactory();

        if (f instanceof CoordinateAccessFactory) {
            return ((CoordinateAccessFactory) f).getDimension();
        } else {
            return 0;
        }
    }

    /**
     * Return TT as defined by SDO_GTYPE (represents geometry type).
     * 
     * <p>
     * TT is used to represent the type of the JTS Geometry:
     * </p>
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
     * @param geom
     *
     * @return <code>TT</code> representing <code>geom</code>
     *
     * @throws IllegalArgumentException If SDO_GTYPE can not be represetned by
     *         JTS
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
            + geom.getGeometryType() + " as SDO_GTYPE "
            + "(Limitied to Point, Line, Polygon, GeometryCollection, MultiPoint,"
            + " MultiLineString and MultiPolygon)");
    }

    /**
     * Returns geometry SRID.
     * 
     * <p>
     * SRID code representing Spatial Reference System. SRID number used must
     * be defined in the Oracle MDSYS.CS_SRS table.
     * </p>
     * 
     * <p>
     * <code>SRID_NULL</code>represents lack of coordinate system.
     * </p>
     *
     * @param geom Geometry SRID Number (JTS14 uses GeometryFactor.getSRID() )
     *
     * @return <code>SRID</code> for provided geom
     */
    public static int SRID(Geometry geom) {
        return geom.getSRID();
    }

    /**
     * Return SDO_POINT_TYPE for geometry
     * 
     * <p>
     * Will return non null for Point objects. <code>null</code> is returned
     * for all non point objects.
     * </p>
     * 
     * <p>
     * You cannot use this with LRS Coordiantes
     * </p>
     * 
     * <p>
     * Subclasses may wish to repress this method and force Points to be
     * represented using SDO_ORDINATES.
     * </p>
     *
     * @param geom
     *
     */
    public static double[] point(Geometry geom) {
        if (geom instanceof Point && (L(geom) == 0)) {
            Point point = (Point) geom;
            Coordinate coord = point.getCoordinate();

            return new double[] { coord.x, coord.y, coord.z };
        }

        // SDO_POINT_TYPE only used for non LRS Points
        return null;
    }

    /**
     * Return SDO_ELEM_INFO array for geometry
     * 
     * <p>
     * Describes how to use Ordinates to represent Geometry.
     * </p>
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
     * <p>
     * For compound elements (SDO_ETYPE values 4 and 5) the last element of one
     * is the first element of the next.
     * </p>
     *
     * @param geom Geometry being represented
     *
     * @return Descriptionof Ordinates representation
     */
    public static int[] elemInfo(Geometry geom) {
        return elemInfo(geom, gType(geom));
    }

    public static int[] elemInfo(Geometry geom, final int GTYPE) {
        List list = new LinkedList();
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
     *
     * @throws IllegalArgumentException If geom cannot be encoded by ElemInfo
     */
    private static void elemInfo(List elemInfoList, Geometry geom,
        final int STARTING_OFFSET, final int GTYPE) {
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
            + geom.getGeometryType() + " as SDO_ELEM_INFO "
            + "(Limitied to Point, Line, Polygon, GeometryCollection, MultiPoint,"
            + " MultiLineString and MultiPolygon)");
    }

    /**
     * Not often called as POINT_TYPE prefered over ELEMINFO & ORDINATES.
     * 
     * <p>
     * This method is included to allow for multigeometry encoding.
     * </p>
     *
     * @param elemInfoList List containing ELEM_INFO array
     * @param point Point to encode
     * @param STARTING_OFFSET Starting offset in SDO_ORDINATE array
     */
    private static void addElemInfo(List elemInfoList, Point point,
        final int STARTING_OFFSET) {
        addInt(elemInfoList, STARTING_OFFSET);
        addInt(elemInfoList, ETYPE.POINT);
        addInt(elemInfoList, 1); // INTERPRETATION single point
    }

    private static void addElemInfo(List elemInfoList, LineString line,
        final int STARTING_OFFSET) {
        addInt(elemInfoList, STARTING_OFFSET);
        addInt(elemInfoList, ETYPE.LINE);
        addInt(elemInfoList, 1); // INTERPRETATION straight edges        
    }

    private static void addElemInfo(List elemInfoList, Polygon polygon,
        final int STARTING_OFFSET, final int GTYPE) {
        final int HOLES = polygon.getNumInteriorRing();

        if (HOLES == 0) {
            addInt(elemInfoList, STARTING_OFFSET);
            addInt(elemInfoList, elemInfoEType(polygon));
            addInt(elemInfoList, elemInfoInterpretation(polygon, ETYPE.POLYGON_EXTERIOR));

            return;
        }

        int LEN = D(GTYPE) + L(GTYPE);
        int offset = STARTING_OFFSET;
        LineString ring;

        ring = polygon.getExteriorRing();
        addInt(elemInfoList, offset);
        addInt(elemInfoList, elemInfoEType(polygon));
        addInt(elemInfoList, elemInfoInterpretation(polygon, ETYPE.POLYGON_EXTERIOR));
        offset += (ring.getNumPoints() * LEN);

        for (int i = 1; i <= HOLES; i++) {
            ring = polygon.getInteriorRingN(i - 1);
            addInt(elemInfoList, offset);
            addInt(elemInfoList, ETYPE.POLYGON_INTERIOR);
            addInt(elemInfoList, elemInfoInterpretation(ring, ETYPE.POLYGON_INTERIOR));
            offset += (ring.getNumPoints() * LEN);
        }
    }

    private static void addElemInfo(List elemInfoList, MultiPoint points,
        final int STARTING_OFFSET) {
        addInt(elemInfoList, STARTING_OFFSET);
        addInt(elemInfoList, ETYPE.POINT);
        addInt(elemInfoList, elemInfoInterpretation(points, ETYPE.POINT));
    }

    private static void addElemInfo(List elemInfoList, MultiLineString lines,
        final int STARTING_OFFSET, final int GTYPE) {
        LineString line;
        int offset = STARTING_OFFSET;

        int LEN = D(GTYPE) + L(GTYPE);

        for (int i = 0; i < lines.getNumGeometries(); i++) {
            line = (LineString) lines.getGeometryN(i);
            addElemInfo(elemInfoList, line, offset);
            offset += (line.getNumPoints() * LEN);
        }
    }

    private static void addElemInfo(List elemInfoList, MultiPolygon polys,
        final int STARTING_OFFSET, final int GTYPE) {
        Polygon poly;
        int offset = STARTING_OFFSET;

        int LEN = D(GTYPE) + L(GTYPE);

        for (int i = 0; i < polys.getNumGeometries(); i++) {
            poly = (Polygon) polys.getGeometryN(i);
            addElemInfo(elemInfoList, poly, offset, GTYPE);
            if( isRectangle( poly )){
                offset += (2 * LEN);                
            }
            else {
                offset += (poly.getNumPoints() * LEN);                
            }            
        }
    }

    private static void addElemInfo(List elemInfoList, GeometryCollection geoms,
        final int STARTING_OFFSET, final int GTYPE) {
        Geometry geom;
        int offset = STARTING_OFFSET;
        int LEN = D(GTYPE) + L(GTYPE);

        for (int i = 0; i < geoms.getNumGeometries(); i++) {
            geom = geoms.getGeometryN(i);
            elemInfo(elemInfoList, geom, offset, GTYPE);
            if( geom instanceof Polygon && isRectangle( (Polygon) geom )){
                offset += (2 * LEN);                
            }
            else {
                offset += (geom.getNumPoints() * LEN);                
            }                        
        }
    }

    /**
     * Adds contents of array to the list as Interger objects
     *
     * @param list List to append the contents of array to
     * @param array Array of ints to append
     */
    private static void addInts(List list, int[] array) {
        for (int i = 0; i < array.length; i++) {
            list.add(new Integer(array[i]));
        }
    }

    private static void addInt(List list, int i) {
        list.add(new Integer(i));
    }

    /**
     * Converts provied list to an int array
     *
     * @param list List to cast to an array type
     * @return array of ints 
     */
    private static int[] intArray(List list) {
        int[] array = new int[list.size()];
        int offset = 0;

        for (Iterator i = list.iterator(); i.hasNext(); offset++) {
            array[offset] = ((Number) i.next()).intValue();
        }

        return array;
    }

    /**
     * Starting offset used by SDO_ORDINATES as stored in the SDO_ELEM_INFO
     * array.
     * 
     * <p>
     * Starting offsets start from one.
     * </p>
     * 
     * <p>
     * Describes ordinates as part of <code>SDO_ELEM_INFO</code> data type.
     * </p>
     *
     * @param geom
     *
     * @return <code>1</code> for non nested <code>geom</code>
     */
    public static int elemInfoStartingOffset(Geometry geom) {
        return 1;
    }

    /**
     * Produce <code>SDO_ETYPE</code> for geometry description as stored in the
     * <code>SDO_ELEM_INFO</code>.
     * 
     * <p>
     * Describes how Ordinates are ordered:
     * </p>
     * <pre><code><b>
     * Value Elements Meaning</b>
     *    0           Custom Geometry (like spline) 
     *    1  simple   Point (or Points)
     *    2  simple   Line (or Lines)
     *    3           polygon ring of unknown order (discouraged update to 1003 or 2003)
     * 1003  simple   polygon ring (1 exterior counterclockwise order)
     * 2003  simple   polygon ring (2 interior clockwise order)
     *    4  compound series defines a linestring
     *    5  compound series defines a polygon ring of unknown order (discouraged)
     * 1005  compound series defines exterior polygon ring (counterclockwise order)
     * 2005  compound series defines interior polygon ring (clockwise order)
     * </code></pre>
     * 
     * <p>
     * Keep in mind:
     * </p>
     * 
     * <ul>
     * <li>
     * <code>simple</code> elements are defined by a single triplet entry in
     * the <code>SDO_ELEM_INFO</code> array
     * </li>
     * <li>
     * <code>compound</code> elements are defined by a header triplet, and a
     * series of triplets for the parts. Elements in a compound element share
     * first and last points.
     * </li>
     * <li>
     * We are not allowed to mix 1 digit and 4 digit values for ETYPE and GTYPE
     * in a single geometry
     * </li>
     * </ul>
     * 
     * <p>
     * This whole mess describes ordinates as part of
     * <code>SDO_ELEM_INFO</code> array. data type.
     * </p>
     *
     * @param geom Geometry being represented
     *
     * @return Descriptionof Ordinates representation
     *
     * @throws IllegalArgumentException
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
     * Produce <code>SDO_INTERPRETATION</code> for geometry description as
     * stored in the <code>SDO_ELEM_INFO</code>.
     * 
     * <p>
     * Describes ordinates as part of <code>SDO_ELEM_INFO</code> array.
     * </p>
     * 
     * <ul>
     * <li>
     * <b><code>compound</code> element:</b>(SDO_ETYPE 4, 1005, or 2005)<br>
     * Number of subsequent triplets are part of compound element
     * </li>
     * <li>
     * <b><code>simple</code> element:</b>(SDE_ELEM 1, 2, 1003, or 2003)<br>
     * Code defines how ordinates are interpreted (lines or curves)
     * </li>
     * </ul>
     * 
     * <p>
     * SDO_INTERPRETAION Values: (from Table 2-2 in reference docs)
     * </p>
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
     * <p>
     * When playing with circular arcs (SDO_INTERPRETATION==2) arcs are defined
     * by three points. A start point, any point on the arc and the end point.
     * The last point of an arc is the start point of the next. When used to
     * describe a polygon (SDO_ETYPE==1003 or 2003) the first and last point
     * must be the same.
     * </p>
     *
     * @param geom
     *
     */
    public static int elemInfoInterpretation(Geometry geom) {
        return elemInfoInterpretation(geom, elemInfoEType(geom));
    }

    /**
     * Allows specification of <code>INTERPRETATION</code> used to interpret
     * <code>geom</code>.
     * <p>
     * Provides the INTERPRETATION value for the ELEM_INFO triplet
     * of (STARTING_OFFSET, ETYPE, INTERPRETATION).
     * </p>
     * @param geom Geometry to encode
     * @param etype ETYPE value requiring an INTERPREATION
     *
     * @return INTERPRETATION ELEM_INFO entry for geom given etype
     *
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
                return ((MultiPoint) geom).getNumGeometries();
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
            throw new IllegalArgumentException(
                "JTS LineStrings are not composed of curves and lines.");

        case ETYPE.COMPOUND_POLYGON:
        case ETYPE.COMPOUND_POLYGON_INTERIOR:
        case ETYPE.COMPOUND_POLYGON_EXTERIOR:
            throw new IllegalArgumentException(
                "JTS Polygons are not composed of curves and lines.");
        }

        throw new IllegalArgumentException("Cannot encode JTS "
            + geom.getGeometryType() + " as "
            + "SDO_INTERPRETATION (Limitied to Point, Line, Polygon, "
            + "GeometryCollection, MultiPoint, MultiLineString and MultiPolygon)");
    }

    /**
     * Produce <code>SDO_ORDINATES</code> for geometry.
     * 
     * <p>
     * Please see SDO_ETYPE, SDO_INTERPRETATION and SDO_GTYPE for description
     * of how these ordinates are to be interpreted.
     * </p>
     * 
     * <p>
     * Ordinates are ordered by Dimension are non null:
     * </p>
     * 
     * <ul>
     * <li>
     * <p>
     * Two Dimensional:
     * </p>
     * {x1,y1,x2,y2,...}
     * </li>
     * <li>
     * <p>
     * Three Dimensional:
     * </p>
     * {x1,y1,z1,x2,y2,z2,...}
     * </li>
     * </ul>
     * 
     * <p>
     * Spatial will siliently detect and ignore the following:
     * </p>
     * 
     * <ul>
     * <li>
     * d001 point/d005 multipoint elements that are not SDO_ETYPE==1 points
     * </li>
     * <li>
     * d002 lines or curve/d006 multilines or multicurve elements that are not
     * SDO_ETYPE==2 lines or SDO_ETYPE==4 arcs
     * </li>
     * <li>
     * d003 polygon/d007 multipolygon elements that are not SDO_ETYPE==3
     * unordered polygon lines or SDO_ETYPE==5 unorderd compound polygon ring
     * are ignored
     * </li>
     * </ul>
     * 
     * <p>
     * While Oracle is silient on these errors - all other errors will not be
     * detected!
     * </p>
     *
     * @param geom
     *
     */
    public static double[] ordinates(Geometry geom) {
        List list = new ArrayList();

        coordinates(list, geom);

        return ordinates(list, geom);
    }
    
    public static CoordinateSequence getCS(Geometry geom)
    {
    	CoordinateSequence cs = null;
    	switch (TT(geom)) {
		case TT.UNKNOWN:
			break; // extend with your own custom types

		case TT.POINT:
			//addCoordinates(list, (Point) geom);

			return cs;

		case TT.LINE:
			cs = getLineStringCS((LineString) geom);

			return cs;

		case TT.POLYGON:
			//addCoordinates(list, (Polygon) geom);

			return cs;

		case TT.COLLECTION:
			//addCoordinates(list, (GeometryCollection) geom);

			return cs;

		case TT.MULTIPOINT:
			//addCoordinates(list, (MultiPoint) geom);

			return cs;

		case TT.MULTILINE:
			//addCoordinates(list, (MultiLineString) geom);

			return cs;

		case TT.MULTIPOLYGON:
			//addCoordinates(list, (MultiPolygon) geom);

			return cs;
    	}

    	throw new IllegalArgumentException("Cannot encode JTS "
			+ geom.getGeometryType() + " as "
			+ "SDO_ORDINATRES (Limitied to Point, Line, Polygon, "
			+ "GeometryCollection, MultiPoint, MultiLineString and MultiPolygon)");
    
    }

    /**
	 * getLineStringCS purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	private static CoordinateSequence getLineStringCS(LineString ls) {
		if (ls.getCoordinateSequence() instanceof CoordinateAccess)
		{
			CoordinateAccess ca = (CoordinateAccess) ls.getCoordinateSequence();
			return ca;
		}
		else
			return null;
	}

	/**
     * Encode Geometry as described by GTYPE and ELEM_INFO
     * <p>
     * CoordinateSequence & CoordinateAccess wil be used to determine the
     * dimension, and the number of ordinates added.
     * </p>
     * @param list Flat list of Double
     * @param geom Geometry 
     *
     * @throws IllegalArgumentException If geometry cannot be encoded
     */
    public static void coordinates(List list, Geometry geom) {
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
            + geom.getGeometryType() + " as "
            + "SDO_ORDINATRES (Limitied to Point, Line, Polygon, "
            + "GeometryCollection, MultiPoint, MultiLineString and MultiPolygon)");
    }

    /**
     * Adds a double array to list.
     * 
     * <p>
     * The double array will contain all the ordinates in the Coordiante
     * sequence.
     * </p>
     *
     * @param list
     * @param sequence
     */
    private static void addCoordinates(List list, CoordinateSequence sequence) {
        double[] ords;

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
        return new double[] { coord.x, coord.y, coord.z };
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
     * 
     * ordinateArray purpose.
     * <p>
     * Description ...
     * </p>
     * @param access
     * @param index
     */
	private static double[] doubleOrdinateArray(CoordinateAccess access, int index) {
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
    private static void addCoordinates(List list, Point point) {
        addCoordinates(list, point.getCoordinateSequence());
    }

    /**
     * Used with ELEM_INFO <code>(1, 2, 1)</code>
     *
     * @param list List to add coordiantes to
     * @param line LineString to be encoded
     */
    private static void addCoordinates(List list, LineString line) {
        addCoordinates(list, line.getCoordinateSequence());
    }

    /**
     * Used to addCoordinates based on polygon encoding.
     * 
     * <p>
     * Elem Info Interpretations supported:
     * 
     * <ul>
     * <li>
     * 3: Rectangle
     * </li>
     * <li>
     * 1: Standard (supports holes)
     * </li>
     * </ul>
     * </p>
     *
     * @param list List to add coordiantes to
     * @param polygon Polygon to be encoded
     */
    private static void addCoordinates(List list, Polygon polygon) {
        switch (elemInfoInterpretation(polygon)) {
        case 4:  // circle not supported
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
     * <p>
     * You should in sure that the provided <code>polygon</code> is a rectangle
     * using isRectangle( Polygon )
     * </p>
     * 
     * <p></p>
     *
     * @param list List to add coordiantes to
     * @param polygon Polygon to be encoded
     */
    private static void addCoordinatesInterpretation3(List list, Polygon poly) {
        Envelope e = poly.getEnvelopeInternal();
        list.add(new double[] { e.getMinX(), e.getMinY() });
        list.add(new double[] { e.getMaxX(), e.getMaxY() });
    }

    /**
     * Add ordinates for polygon - with hole support.
     * 
     * <p>
     * Ensure ordinates are added in the correct orientation as External or
     * Internal polygons.
     * </p>
     *
     * @param list List to add coordiantes to
     * @param polygon Polygon to be encoded
     */
    private static void addCoordinatesInterpretation1(List list, Polygon polygon) {
        int holes = polygon.getNumInteriorRing();

        addCoordinates(list,
            counterClockWise(polygon.getFactory().getCoordinateSequenceFactory(),
                polygon.getExteriorRing().getCoordinateSequence()));

        for (int i = 0; i < holes; i++) {
            addCoordinates(list,
                clockWise(polygon.getFactory().getCoordinateSequenceFactory(),
                    polygon.getInteriorRingN(i).getCoordinateSequence()));
        }
    }

    private static void addCoordinates(List list, MultiPoint points) {
        for (int i = 0; i < points.getNumGeometries(); i++) {
            addCoordinates(list, (Point) points.getGeometryN(i));
        }
    }

    private static void addCoordinates(List list, MultiLineString lines) {
        for (int i = 0; i < lines.getNumGeometries(); i++) {
            addCoordinates(list, (LineString) lines.getGeometryN(i));
        }
    }

    private static void addCoordinates(List list, MultiPolygon polys) {
        for (int i = 0; i < polys.getNumGeometries(); i++) {
            addCoordinates(list, (Polygon) polys.getGeometryN(i));
        }
    }

    private static void addCoordinates(List list, GeometryCollection geoms) {
        Geometry geom;

        for (int i = 0; i < geoms.getNumGeometries(); i++) {
            geom = geoms.getGeometryN(i);

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
     * <p>
     * Example numbering: for (x y g m) dimension==2, measure==2
     * </p>
     * 
     * <ul>
     * <li>
     * 0: x ordinate array
     * </li>
     * <li>
     * 1: y ordinate array
     * </li>
     * <li>
     * 2: g ordinate array
     * </li>
     * <li>
     * 3: m ordinate array
     * </li>
     * </ul>
     * 
     *
     * @param coords
     * @param ordinate
     *
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
                    array[i] = (c != null) ? c.x : Double.NaN;
                }
            } else if (ordinate == 1) {
                for (int i = 0; i < LENGTH; i++) {
                    c = coords.getCoordinate(i);
                    array[i] = (c != null) ? c.y : Double.NaN;
                }
            } else if (ordinate == 2) {
                for (int i = 0; i < LENGTH; i++) {
                    c = coords.getCoordinate(i);
                    array[i] = (c != null) ? c.z : Double.NaN;
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
     * <p>
     * CoordianteAccess is required for additional ordinates.
     * </p>
     * 
     * <p>
     * Ordinate limitied to:
     * </p>
     * 
     * <ul>
     * <li>
     * 0: x ordinate array
     * </li>
     * <li>
     * 1: y ordinate array
     * </li>
     * <li>
     * 2: z ordinate array
     * </li>
     * <li>
     * 3: empty ordinate array
     * </li>
     * </ul>
     * 
     *
     * @param array
     * @param ordinate
     *
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
                ords[i] = (c != null) ? c.x : Double.NaN;
            }
        } else if (ordinate == 1) {
            for (int i = 0; i < LENGTH; i++) {
                c = array[i];
                ords[i] = (c != null) ? c.y : Double.NaN;
            }
        } else if (ordinate == 2) {
            for (int i = 0; i < LENGTH; i++) {
                c = array[i];
                ords[i] = (c != null) ? c.z : Double.NaN;
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
    			ords[i] = (c != null) ? c.x : Double.NaN;
    		}
    	} else if (ordinate == 1) {
    		for (int i = 0; i < LENGTH; i++) {
    			c = (Coordinate) list.get(i);
    			ords[i] = (c != null) ? c.y : Double.NaN;
    		}
    	} else if (ordinate == 2) {
    		for (int i = 0; i < LENGTH; i++) {
    			c = (Coordinate) list.get(i);
    			ords[i] = (c != null) ? c.z : Double.NaN;
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
     * Do not use me, I am broken
     * <p>
     * Do not use me, I am broken
     * </p>
     * @deprecated Do not use me, I am broken
     * @param list
     * @param ordinate
     */
    /*
    //TODO: check if I am correct
    public static Object[] attributeArray(List list, int ordinate) {
    	if (list == null) {
    		return null;
    	}

    	final int LENGTH = list.size();
    	Object[] ords = new Object[LENGTH];
    	Coordinate c;
    	Double d;
    	String s;

    	if (ordinate == 0) {
    		for (int i = 0; i < LENGTH; i++) {
    			c = (Coordinate) list.get(i);
    			ords[i] = (c != null) ? new Double(c.x) : new Double(Double.NaN);
    		}
    	} else if (ordinate == 1) {
    		for (int i = 0; i < LENGTH; i++) {
    			c = (Coordinate) list.get(i);
    			ords[i] = (c != null) ? new Double(c.y) : new Double(Double.NaN);
    		}
    	} else if (ordinate == 2) {
    		for (int i = 0; i < LENGTH; i++) {
    			c = (Coordinate) list.get(i);
    			ords[i] = (c != null) ? new Double(c.z) : new Double(Double.NaN);
    		}
    	}
    	else if (ordinate == 3) {		//BUG I am broken, do not use me our own Z
    		for (int i = 0; i < LENGTH; i++) {
    			c = (Coordinate) list.get(i);
    			ords[i] = (c != null) ? new Double(Double.NaN) : new Double(Double.NaN);
    		}
    	}
    	else if (ordinate == 4) {		// our own T (a String)
    		for (int i = 0; i < LENGTH; i++) {
    			c = (Coordinate) list.get(i);
    			ords[i] = (c != null) ? new Double(Double.NaN) : new Double(Double.NaN);
    		}
    	}else {
    		// default to NaN
    		for (int i = 0; i < LENGTH; i++) {
    			ords[i] = list.get(i);
    		}
    	}

    	return ords;
    }
    */

    /**
     * Package up <code>array</code> in correct manner for <code>geom</code>.
     * 
     * <p>
     * Ordinates are placed into an array based on:
     * </p>
     * 
     * <ul>
     * <li>
     * geometryGTypeD - chooses between 2d and 3d representation
     * </li>
     * <li>
     * geometryGTypeL - number of LRS measures
     * </li>
     * </ul>
     * 
     *
     * @param list
     * @param geom
     *
     */
    public static double[] ordinates(List list, Geometry geom) {
        LOGGER.finest( "ordinates D:" + D(geom));
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
     * <p>
     * No assumptions are made about the order
     * </p>
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

        for (int i = 0; i < NUMBER; i++) {
            ords = (double[]) list.get(i);

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
     *
     * @return ordinate array
     */
    public static double[] ordinates3d(List list) {
        final int NUMBER = list.size();
        final int LEN = 3;
        double[] array = new double[NUMBER * LEN];
        double[] ords;
        int offset = 0;

        for (int i = 0; i < NUMBER; i++) {
            ords = (double[]) list.get(i);

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
     *
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
                array[(i * LEN) + j] = ords[j];
            }
        }

        return array;
    }

    /**
     * Ordinates (x,y,z,...id,x2,y2,z2...) from coordiante[] array.
     *
     * @param list coordinate array to be represented as ordinates
     * @param L Dimension of ordinates required for representation
     *
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
                array[(i * LEN) + j] = ords[j];
            }
        }

        return array;
    }

    /**
     * Ensure Ring of Coordinates are in a counter clockwise order.
     * 
     * <p>
     * If the Coordiante need to be reversed a copy will be returned.
     * </p>
     *
     * @param factory Factory to used to reverse CoordianteSequence
     * @param ring Ring of Coordinates
     *
     * @return coords in a CCW order
     */
    public static CoordinateSequence counterClockWise(
        CoordinateSequenceFactory factory, CoordinateSequence ring) {
        if (clock.isCCW(ring.toCoordinateArray())) {
            return ring;
        }

        return Coordinates.reverse(factory, ring);
    }

    /**
     * Ensure Ring of Coordinates are in a clockwise order.
     * 
     * <p>
     * If the Coordiante need to be reversed a copy will be returned.
     * </p>
     *
     * @param factory Factory used to reverse CoordianteSequence
     * @param ring Ring of Coordinates
     *
     * @return coords in a CW order
     */
    private static CoordinateSequence clockWise(
        CoordinateSequenceFactory factory, CoordinateSequence ring) {
        if (!clock.isCCW(ring.toCoordinateArray())) {
            return ring;
        }
        return Coordinates.reverse(factory, ring);
    }

    /**
     * Reverse the clockwise orientation of the ring of Coordiantes.
     *
     * @param ring Ring of Coordinates
     *
     * @return coords Copy of <code>ring</code> in reversed order
     */
    private static Coordinate[] reverse(Coordinate[] ring) {
        int length = ring.length;
        Coordinate[] reverse = new Coordinate[length];

        for (int i = 0; i < length; i++) {
            reverse[i] = ring[length - i - 1];
        }
        return reverse;
    }

    // Utility Functions
    //
    //

    /**
     * Will need to tell if we are encoding a Polygon Exterior or Interior so
     * we can produce the correct encoding.
     *
     * @param poly Polygon to check
     *
     * @return <code>true</code> as we expect PolygonExteriors to be passed in
     */
    private static boolean isExterior(Polygon poly) {
        return true; // JTS polygons are always exterior
    }

    /**
     * We need to check if a <code>polygon</code> a cicle so we can produce the
     * correct encoding.
     *
     * @param polygon
     *
     * @return <code>true</code> if polygon is a circle
     */
    private static boolean isCircle(Polygon polygon) {
        return false; // JTS does not do cicles
    }

    /**
     * We need to check if a <code>polygon</code> a rectangle so we can produce
     * the correct encoding.
     * 
     * <p>
     * Rectangles are only supported without a SRID!
     * </p>
     *
     * @param polygon
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

        if ((coords[0] == null) || (coords[1] == null) || (coords[2] == null)
                || (coords[3] == null)) {
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

        if ((x1 == x4) && (y1 == y2) && (x3 == x2) && (y3 == y4)) {
            // 1+-----+2
            //  |     |
            // 4+-----+3
            return true;
        }

        if ((x1 == x2) && (y1 == y4) && (x3 == x4) && (y3 == y2)) {
            // 2+-----+3
            //  |     |
            // 1+-----+4
            return true;
        }

        return false;
    }

    /**
     * We need to check if a <code>polygon</code> is defined with curves so we
     * can produce the correct encoding.
     * 
     * <p></p>
     *
     * @param polygon
     *
     * @return <code>false</code> as JTS does not support curves
     */
    private static boolean isCurve(Polygon polygon) {
        return false;
    }

    /**
     * We need to check if a <code>lineString</code> is defined with curves so
     * we can produce the correct encoding.
     * 
     * <p></p>
     *
     * @param lineString
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
     * @param elemInfo
     * @param triplet
     *
     */
    private static CoordinateSequence subList(
        CoordinateSequenceFactory factory, CoordinateSequence coords,
        int GTYPE, int[] elemInfo, int triplet) {
            
        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, triplet);
        final int ENDING_OFFSET = STARTING_OFFSET(elemInfo, triplet + 1); // -1 for end

        if ((STARTING_OFFSET == 1) && (ENDING_OFFSET == -1)) {
            // Use all Cordiantes
            return coords;
        }
        final int LEN = D(GTYPE) + L(GTYPE);

        int start = (STARTING_OFFSET - 1) / LEN;
        int end = (ENDING_OFFSET != -1) ? ((ENDING_OFFSET - 1) / LEN)
                                        : coords.size();

        return subList(factory, coords, start, end);
    }

    /**
     * Version of List.subList() that returns a CoordinateSequence.
     * 
     * <p>
     * Returns from start (inclusive) to end (exlusive):
     * </p>
     * 
     * <p>
     * Math speak: <code>[start,end)</code>
     * </p>
     *
     * @param factory Manages CoordinateSequences for JTS
     * @param coords coords to sublist
     * @param start starting offset
     * @param end upper bound of sublist 
     *
     * @return CoordianteSequence
     */
    private static CoordinateSequence subList(
        CoordinateSequenceFactory factory, CoordinateSequence coords,
        int start, int end) {
        if ((start == 0) && (end == coords.size())) {
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

    private static LineString[] toLineStringArray(List list) {
        return (LineString[]) toArray(list, LineString.class);

        /*
           if( list == null ) return null;
           LineString array[] = new LineString[ list.size() ];
           int index=0;
           for( Iterator i=list.iterator(); i.hasNext(); index++ )
           {
               array[ index ] = (LineString) i.next();
           }
           return array;
         */
    }

    private static Polygon[] toPolygonArray(List list) {
        return (Polygon[]) toArray(list, Polygon.class);
    }

    private static Geometry[] toGeometryArray(List list) {
        return (Geometry[]) toArray(list, Geometry.class);
    }

    /**
     * Useful function for converting to typed arrays for JTS API.
     * 
     * <p>
     * Example:
     * </p>
     * <pre><code>
     * new MultiPoint( toArray( list, Coordiante.class ) );
     * </code></pre>
     *
     * @param list
     * @param type
     *
     */
    private static Object toArray(List list, Class type) {
        if (list == null) {
            return null;
        }

        Object array = Array.newInstance(type, list.size());
        int index = 0;

        for (Iterator i = list.iterator(); i.hasNext(); index++) {
            Array.set(array, index, i.next());
        }

        return array;
    }

    /**
     * Access D (for dimension) as encoded in GTYPE
     *
     * @param GTYPE DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int D(final int GTYPE) {
        return GTYPE / 1000;
    }

    /**
     * Access L (for LRS) as encoded in GTYPE
     *
     * @param GTYPE DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int L(final int GTYPE) {
        return (GTYPE - (D(GTYPE) * 1000)) / 100;
    }

    /**
     * Access TT (for geometry type) as encoded in GTYPE
     *
     * @param GTYPE DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int TT(final int GTYPE) {
        return GTYPE - (D(GTYPE) * 1000) - (L(GTYPE) * 100);
    }

    /**
     * Access STARTING_OFFSET from elemInfo, or -1 if not available.
     * 
     * <p></p>
     *
     * @param elemInfo DOCUMENT ME!
     * @param triplet DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int STARTING_OFFSET(int[] elemInfo, int triplet) {
        if (((triplet * 3) + 0) >= elemInfo.length) {
            return -1;
        }

        return elemInfo[(triplet * 3) + 0];
    }

    /**
     * A version of assert that indicates range pre/post condition.
     * <p>
     * Works like assert exception IllegalArgumentException is thrown indicating this
     * is a required check.
     * </p>
     * <p>
     * Example phrased as a positive statement of the requirement to be met:
     * <pre><code>
     * ensure( "STARTING_OFFSET {1} must indicate a valid ordinate between {0} and {2}.
     * </code></pre>
     * </p>
     * @param condition MessageFormat pattern - positive statement of requirement
     * @param min minimum acceptable value ({0} in message format)
     * @param actual value supplied ({1} in message format)
     * @param max maximum acceptable value ({2} in message format)
     * @throws IllegalArgumentException unless min <= actual <= max
     */
    private static void ensure( String condition, int min, int actual, int max  ){
        if( !(min <= actual && actual <= max) ){
            String msg = MessageFormat.format( condition,
                    new Object[]{ new Integer(min), new Integer(actual), new Integer(max) } );
            throw new IllegalArgumentException( msg );
        }
    }
    /**
     * A version of assert that indicates range pre/post condition.
     * <p>
     * Works like assert exception IllegalArgumentException is thrown indicating this
     * is a required check.
     * </p>
     * <p>
     * Example phrased as a positive statement of the requirement to be met:
     * <pre><code>
     * ensure( "INTERPRETATION {0} must be on of {1}.
     * </code></pre>
     * </p>
     * @param condition MessageFormat pattern - positive statement of requirement
     * @param actual value supplied ({0} in message format)
     * @param set Array of acceptable values ({1} in message format)
     * @throws IllegalArgumentException unless actual is a member of set
     */
    private static void ensure( String condition, int actual, int[] set ){
        if( set == null ) return; // don't apparently care
        for( int i=0; i<set.length;i++){
            if( set[i] == actual ) return; // found it
        }
        StringBuffer array = new StringBuffer();        
        for( int i=0; i<set.length;i++){
            array.append( set[i] );
            if( i<set.length){
                array.append( "," );
            }
        }
        String msg = MessageFormat.format( condition,
                new Object[]{ new Integer(actual), array } );        
        throw new IllegalArgumentException( msg );
    }
    /** Returns the "length" of the ordinate array used for the
     * CoordianteSequence, GTYPE is used to determine the dimension.
     * <p>
     * This is most often used to check the STARTING_OFFSET value to ensure
     * that is falls within allowable bounds.
     * </p>
     * <p>
     * Example:<pre><code>
     * if (!(STARTING_OFFSET >= 1) ||
     *     !(STARTING_OFFSET <= ordinateSize( coords, GTYPE ))){
     *     throw new IllegalArgumentException(
     *         "ELEM_INFO STARTING_OFFSET "+STARTING_OFFSET+ 
     *         "inconsistent with COORDINATES length "+size( coords, GTYPE ) );
     * } 
     * </code></pre>
     * </p>
     * @param coords
     * @param GTYPE
     */
    private static int ordinateSize( CoordinateSequence coords, int GTYPE ){
        if( coords == null ){
            return 0;
        }
        return coords.size() * D(GTYPE);
    }
    /**
     * ETYPE access for the elemInfo triplet indicated.
     * <p>
     * @see ETYPE for an indication of possible values
     * 
     * @param elemInfo
     * @param triplet
     * @return ETYPE for indicated triplet
     */ 
    private static int ETYPE(int[] elemInfo, int triplet) {
        if (((triplet * 3) + 1) >= elemInfo.length) {
            return -1;
        }

        return elemInfo[(triplet * 3) + 1];
    }

    private static int INTERPRETATION(int[] elemInfo, int triplet) {
        if (((triplet * 3) + 2) >= elemInfo.length) {
            return -1;
        }

        return elemInfo[(triplet * 3) + 2];
    }

    /**
     * Coordiantes from <code>(x,y,x2,y2,...)</code> ordinates.
     *
     * @param ordinates DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Coordinate[] asCoordinates(double[] ordinates) {
        return asCoordiantes(ordinates, 2);
    }

    /**
     * Coordiantes from a <code>(x,y,i3..,id,x2,y2...)</code> ordinates.
     *
     * @param ordinates DOCUMENT ME!
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Coordinate[] asCoordiantes(double[] ordinates, int d) {
        int length = ordinates.length / d;
        Coordinate[] coords = new Coordinate[length];

        for (int i = 0; i < length; i++) {
            coords[i] = new Coordinate(ordinates[i * d], ordinates[(i * d) + 1]);
        }

        return coords;
    }

    /**
     * Construct CoordinateList as described by GTYPE.
     * 
     * <p>
     * GTYPE encodes the following information:
     * 
     * <ul>
     * <li>
     * D: Dimension of ordinates
     * </li>
     * <li>
     * L: Dimension of LRS measures
     * </li>
     * </ul>
     * </p>
     * 
     * <p>
     * The number of ordinates per coordinate are taken to be L+D, and the
     * number of ordinates should be a multiple of this value.
     * </p>
     * 
     * <p>
     * In the Special case of GTYPE 2001 and a three ordinates are interpreted
     * as a single Coordinate rather than an error.
     * </p>
     *
     * @param f CoordinateSequenceFactory used to encode ordiantes for JTS 
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param ordinates
     *
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static CoordinateSequence coordinates(CoordinateSequenceFactory f,
        final int GTYPE, double[] ordinates) {
        if ((ordinates == null) || (ordinates.length == 0)) {
            return f.create(new Coordinate[0]);
        }

        final int D = SDO.D(GTYPE);
        final int L = SDO.L(GTYPE);
        final int TT = SDO.TT(GTYPE);

        //      POINT_TYPE Special Case
        //
        if ((D == 2) && (L == 0) && (TT == 01) && (ordinates.length == 3)) {
            return f.create(new Coordinate[] {
                    new Coordinate(ordinates[0], ordinates[1], ordinates[2]),
                });
        }

        final int LEN = D + L;

        if ((ordinates.length % LEN) != 0) {
            throw new IllegalArgumentException("Dimension D:" + D + " and L:"
                + L + " denote Coordiantes " + "of " + LEN
                + " ordinates. This cannot be resolved with"
                + "an ordinate array of length " + ordinates.length);
        }

        final int LENGTH = ordinates.length / LEN;

        OrdinateList x = new OrdinateList(ordinates, 0, LEN);
        OrdinateList y = new OrdinateList(ordinates, 1, LEN);
        OrdinateList z = null;

        if (D == 3) {
            z = new OrdinateList(ordinates, 2, LEN);
        }

        if (L != 0) {
            OrdinateList[] m = new OrdinateList[L];

            for (int i = 0; i < L; i++) {
                m[i] = new OrdinateList(ordinates, D + i, LEN);
            }

            return coordiantes(f, x, y, z, m);
        } else {
            return coordiantes(f, x, y, z);
        }
    }

    /**
     * Construct CoordinateSequence with no LRS measures.
     * 
     * <p>
     * To produce two dimension Coordiantes pass in <code>null</code> for z
     * </p>
     *
     * @param f DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CoordinateSequence coordiantes(CoordinateSequenceFactory f,
        OrdinateList x, OrdinateList y, OrdinateList z) {
        final int LENGTH = x.size();
        Coordinate[] array = new Coordinate[LENGTH];

        if (z != null) {
            for (int i = 0; i < LENGTH; i++) {
                array[i] = new Coordinate(x.getDouble(i), y.getDouble(i),
                        z.getDouble(i));
            }
        } else {
            for (int i = 0; i < LENGTH; i++) {
                array[i] = new Coordinate(x.getDouble(i), y.getDouble(i));
            }
        }

        return f.create(array);
    }
    
    /**
     * Construct CoordinateSequence with no LRS measures.
     * 
     * <p>
     * To produce two dimension Coordiantes pass in <code>null</code> for z
     * </p>
     *
     * @param f DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CoordinateSequence coordiantes(CoordinateSequenceFactory f,
												 AttributeList x, 
												 AttributeList y, 
												 AttributeList z) {
    	final int LENGTH = x.size();
    	Coordinate[] array = new Coordinate[LENGTH];

    	if (z != null) {
    		for (int i = 0; i < LENGTH; i++) {
    			array[i] = new Coordinate(x.getDouble(i), y.getDouble(i),
    					z.getDouble(i));
    		}
    	} else {
    		for (int i = 0; i < LENGTH; i++) {
    			array[i] = new Coordinate(x.getDouble(i), y.getDouble(i));
    		}
    	}

    	return f.create(array);
    }

    /**
     * Construct SpatialCoordiantes, with LRS measure information.
     * 
     * <p>
     * To produce two dimension Coordiantes pass in <code>null</code> for z
     * </p>
     *
     * @param f DOCUMENT ME!
     * @param x x-ordinates
     * @param y y-ordinates
     * @param z z-ordinates, <code>null</code> for 2D
     * @param m column major measure information
     *
     * @return DOCUMENT ME!
     */
    public static CoordinateSequence coordiantes(CoordinateSequenceFactory f,
        OrdinateList x, OrdinateList y, OrdinateList z, OrdinateList[] m) {
        final int D = (z != null) ? 3 : 2;
        final int L = (m != null) ? m.length : 0;

        if (f instanceof CoordinateAccess && (L != 0)) {
            CoordinateAccessFactory factory = (CoordinateAccessFactory) f;
            double[][] xyz = new double[D][];
            double[][] measures = new double[L][];

            xyz[0] = x.toDoubleArray();
            xyz[1] = y.toDoubleArray();

            if (D == 3) {
                xyz[2] = z.toDoubleArray();
            }

            for (int i = 0; i < L; i++) {
                measures[i] = m[i].toDoubleArray();
            }

            return factory.create(xyz, measures);
        } else {
            return coordiantes(f, x, y, z);
        }
    }

    /**
     * Construct SpatialCoordiantes, with LRS measure information.
     * 
     * <p>
     * To produce two dimension Coordiantes pass in <code>null</code> for z
     * </p>
     *
     * @param f DOCUMENT ME!
     * @param x x-ordinates
     * @param y y-ordinates
     * @param z z-ordinates, <code>null</code> for 2D
     * @param m column major measure information
     *
     * @return DOCUMENT ME!
     */
    public static CoordinateSequence coordiantes(CoordinateSequenceFactory f,
												 AttributeList x, 
												 AttributeList y, 
												 AttributeList z, 
												 AttributeList[] m) {
    	final int D = (z != null) ? 3 : 2;
    	final int L = (m != null) ? m.length : 0;

    	if (f instanceof CoordinateAccess && (L != 0)) {
    		CoordinateAccessFactory factory = (CoordinateAccessFactory) f;
    		double[][] xyz = new double[D][];
    		Object[] measures = new Object[L];

    		xyz[0] = x.toDoubleArray();
    		xyz[1] = y.toDoubleArray();

    		if (D == 3) {
    			xyz[2] = z.toDoubleArray();
    		}

    		for (int i = 0; i < L; i++) {
    			measures[i] = m[i].toObjectArray();
    		}

    		return factory.create(xyz, measures);
    	} else {
    		return coordiantes(f, x, y, z);
    	}
    }
    
    /**
     * Decode geometry from provided SDO encoded information.
     * 
     * <p></p>
     *
     * @param gf Used to construct returned Geometry
     * @param GTYPE SDO_GTYPE represents dimension, LRS, and geometry type
     * @param SRID SDO_SRID represents Spatial Reference System
     * @param point
     * @param elemInfo
     * @param ordinates
     *
     * @return Geometry as encoded
     */
    public static Geometry create(GeometryFactory gf, final int GTYPE,
        final int SRID, double[] point, int[] elemInfo, double[] ordinates) {
        final int L = SDO.L(GTYPE);
        final int TT = SDO.TT(GTYPE);
        double[] list;
        double[][] lists;

        CoordinateSequence coords;

        if ((L == 0) && (TT == 01) && (point != null) && (elemInfo == null)) {
            // Single Point Type Optimization
            coords = SDO.coordinates(gf.getCoordinateSequenceFactory(), GTYPE,
                    point);
            elemInfo = new int[] { 1, ETYPE.POINT, 1 };
        } else {
            coords = SDO.coordinates(gf.getCoordinateSequenceFactory(), GTYPE,
                    ordinates);
        }

        return create(gf, GTYPE, SRID, elemInfo, 0, coords, -1);
    }

    /**
     * Consturct geometry with SDO encoded information over a CoordinateList.
     * 
     * <p>
     * Helpful when dealing construction Geometries with your own Coordiante
     * Types. The dimensionality specified in GTYPE will be used to interpret
     * the offsets in elemInfo.
     * </p>
     *
     * @param gf
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param SRID
     * @param elemInfo
     * @param triplet DOCUMENT ME!
     * @param coords
     * @param N Number of triplets (-1 for unknown/don't care)
     *
     * @return Geometry as encoded, or null w/ log if it cannot be represented via JTS
     */
    public static Geometry create(GeometryFactory gf, final int GTYPE,
        final int SRID, final int[] elemInfo, final int triplet,
        CoordinateSequence coords, final int N) {
        switch (SDO.TT(GTYPE)) {
        case TT.POINT:
            return createPoint(gf, GTYPE, SRID, elemInfo, triplet, coords);

        case TT.LINE:
            return createLine(gf, GTYPE, SRID, elemInfo, triplet, coords);

        case TT.POLYGON:
            return createPolygon(gf, GTYPE, SRID, elemInfo, triplet, coords);

        case TT.MULTIPOINT:
            return createMultiPoint(gf, GTYPE, SRID, elemInfo, triplet, coords);

        case TT.MULTILINE:
            return createMultiLine(gf, GTYPE, SRID, elemInfo, triplet, coords, N);

        case TT.MULTIPOLYGON:
            return createMultiPolygon(gf, GTYPE, SRID, elemInfo, triplet,
                coords, N);

        case TT.COLLECTION:
            return createCollection(gf, GTYPE, SRID, elemInfo, triplet, coords,
                N);
        
        case TT.UNKNOWN:  
        default:
            LOGGER.warning( "Cannot represent provided SDO STRUCT (GTYPE ="+GTYPE+") using JTS Geometry");
            return null;    
        }        
    }

    /**
     * Create Point as encoded.
     *
     * @param gf
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param SRID
     * @param elemInfo
     * @param element
     * @param coords
     *
     * @return Point
     */
    private static Point createPoint(GeometryFactory gf, final int GTYPE,
        final int SRID, final int[] elemInfo, final int element,
        CoordinateSequence coords) {
        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, element);
        final int etype = ETYPE(elemInfo, element);
        final int INTERPRETATION = INTERPRETATION(elemInfo, element);

		if (!(STARTING_OFFSET >= 1) || !(STARTING_OFFSET <= coords.size()))
		    throw new IllegalArgumentException("ELEM_INFO STARTING_OFFSET "+STARTING_OFFSET+" inconsistent with ORDINATES length "+coords.size());
		if (etype != ETYPE.POINT)
		    throw new IllegalArgumentException("ETYPE "+etype+" inconsistent with expected POINT");
		if (INTERPRETATION != 1){
		    LOGGER.warning( "Could not create JTS Point with INTERPRETATION "+INTERPRETATION+" - we only expect 1 for a single point");
			return null;
		}


        Point point = new Point(subList(gf.getCoordinateSequenceFactory(),
                    coords, GTYPE, elemInfo, element), gf);

        //Point point = gf.createPoint( coords.getCoordinate( index ) );
        point.setSRID(SRID);

        return point;
    }

    /**
     * Create LineString as encoded.
     *
     * @param gf
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param SRID
     * @param elemInfo
     * @param triplet
     * @param coords
     *
     *
     * @throws IllegalArgumentException If asked to create a curve
     */
    private static LineString createLine(GeometryFactory gf, final int GTYPE,
        final int SRID, final int[] elemInfo, final int triplet,
        CoordinateSequence coords) {
        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, triplet);
        final int etype = ETYPE(elemInfo, triplet);
        final int INTERPRETATION = INTERPRETATION(elemInfo, triplet);

		if (etype != ETYPE.LINE)
			return null;
		if (INTERPRETATION != 1){
		    LOGGER.warning( "Could not create JTS LineString with INTERPRETATION "+INTERPRETATION+" - we can only support 1 for straight edges");
			return null;
		}
			

        if (INTERPRETATION != 1) {
            // May be INTERPRETATION == 2 for curves
            throw new IllegalArgumentException("ELEM_INFO INTERPRETAION "
                + INTERPRETATION + " not supported"
                + "by JTS LineString.  Straight edges"
                + "( ELEM_INFO INTERPRETAION 1) is supported");
        }

        LineString line = new LineString(subList(
                    gf.getCoordinateSequenceFactory(), coords, GTYPE, elemInfo,
                    triplet), gf);
        line.setSRID(SRID);

        return line;
    }

    /**
     * Create Polygon as encoded.
     * 
     * <p>
     * Encoded as a one or more triplets in elemInfo:
     * </p>
     * 
     * <ul>
     * <li>
     * Exterior Polygon Ring: first triplet:
     * 
     * <ul>
     * <li>
     * STARTING_OFFSET: position in ordinal ordinate array
     * </li>
     * <li>
     * ETYPE: 1003 (exterior) or 3 (polygon w/ counter clockwise ordinates)
     * </li>
     * <li>
     * INTERPRETATION: 1 for strait edges, 3 for rectanlge
     * </li>
     * </ul>
     * 
     * </li>
     * <li>
     * Interior Polygon Ring(s): remaining triplets:
     * 
     * <ul>
     * <li>
     * STARTING_OFFSET: position in ordinal ordinate array
     * </li>
     * <li>
     * ETYPE: 2003 (interior) or 3 (polygon w/ clockWise ordinates)
     * </li>
     * <li>
     * INTERPRETATION: 1 for strait edges, 3 for rectanlge
     * </li>
     * </ul>
     * 
     * </li>
     * </ul>
     * 
     * <p>
     * The polygon encoding will process subsequent 2003, or 3 triples with
     * clockwise ordering as interior holes.
     * </p>
     * 
     * <p>
     * A subsequent triplet of any other type marks the end of the polygon.
     * </p>
     * 
     * <p>
     * The dimensionality of GTYPE will be used to transalte the
     * <code>STARTING_OFFSET</code> provided by elemInfo into an index into
     * <code>coords</code>.
     * </p>
     *
     * @param gf Used to construct polygon
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param SRID Spatial Reference System
     * @param elemInfo Interpretation of coords
     * @param triplet Triplet in elemInfo to process as a Polygon
     * @param coords Coordinates to interpret using elemInfo
     *
     * @return Polygon as encoded by elemInfo, or null when faced with and
     *         encoding that can not be captured by JTS
     * @throws IllegalArgumentException When faced with an invalid SDO encoding
     */
    private static Polygon createPolygon(GeometryFactory gf, final int GTYPE,
        final int SRID, final int[] elemInfo, final int triplet,
        CoordinateSequence coords) throws IllegalArgumentException  {
        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, triplet);
        final int eTYPE = ETYPE(elemInfo, triplet);
        final int INTERPRETATION = INTERPRETATION(elemInfo, triplet);

        ensure( "ELEM_INFO STARTING_OFFSET {1} must be in the range {0}..{1} of COORDINATES",
                1,STARTING_OFFSET, ordinateSize( coords, GTYPE ) );        
        if( !(1 <= STARTING_OFFSET && STARTING_OFFSET <= ordinateSize( coords, GTYPE ))){
            throw new IllegalArgumentException(
                    "ELEM_INFO STARTING_OFFSET "+STARTING_OFFSET+
                    "inconsistent with COORDINATES length "+ordinateSize( coords, GTYPE ) );
        } 
        ensure( "ETYPE {0} must be expected POLYGON or POLYGON_EXTERIOR (one of {1})",
                eTYPE, new int[]{ ETYPE.POLYGON, ETYPE.POLYGON_EXTERIOR } );
		if(!(eTYPE == ETYPE.POLYGON) && !(eTYPE == ETYPE.POLYGON_EXTERIOR)){
			throw new IllegalArgumentException(
			        "ETYPE "+eTYPE+" inconsistent with expected POLYGON or POLYGON_EXTERIOR");
		}        
		if (!(INTERPRETATION == 1) && !(INTERPRETATION == 3)){
		    LOGGER.warning( "Could not create JTS Polygon with INTERPRETATION "+INTERPRETATION+" - we can only support 1 for straight edges, and 3 for rectangle");
			return null;
		}

        LinearRing exteriorRing = createLinearRing(gf, GTYPE, SRID, elemInfo,
                triplet, coords);

        List rings = new LinkedList();
        int etype;
HOLES: 
        for (int i = triplet + 1; (etype = ETYPE(elemInfo, i)) != -1; i++) {
            if (etype == ETYPE.POLYGON_INTERIOR) {
                rings.add(createLinearRing(gf, GTYPE, SRID, elemInfo, i, coords));
            } else if (etype == ETYPE.POLYGON) { // nead to test Clockwiseness of Ring to see if it is
                                                 // interior or not - (use POLYGON_INTERIOR to avoid pain)

                LinearRing ring = createLinearRing(gf, GTYPE, SRID, elemInfo,
                        i, coords);

                if (clock.isCCW(ring.getCoordinates())) { // it is an Interior Hole
                    rings.add(ring);
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
     * <p>
     * Encoded as a single triplet in elemInfo:
     * </p>
     * 
     * <ul>
     * <li>
     * STARTING_OFFSET: position in ordinal ordinate array
     * </li>
     * <li>
     * ETYPE: 1003 (exterior) or 2003 (interior) or 3 (unknown order)
     * </li>
     * <li>
     * INTERPRETATION: 1 for strait edges, 3 for rectanlge
     * </li>
     * </ul>
     * 
     * <p>
     * The dimensionality of GTYPE will be used to transalte the
     * <code>STARTING_OFFSET</code> provided by elemInfo into an index into
     * <code>coords</code>.
     * </p>
     *
     * @param gf
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param SRID
     * @param elemInfo
     * @param triplet
     * @param coords
     *
     * @return LinearRing
     *
     * @throws IllegalArgumentException If circle, or curve is requested
     */
    private static LinearRing createLinearRing(GeometryFactory gf,
        final int GTYPE, final int SRID, final int[] elemInfo,
        final int triplet, CoordinateSequence coords) {
            
        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, triplet);
        final int eTYPE = ETYPE(elemInfo, triplet);
        final int INTERPRETATION = INTERPRETATION(elemInfo, triplet);
        final int LENGTH = coords.size()*D(GTYPE);
        
		if (!(STARTING_OFFSET >= 1) || !(STARTING_OFFSET <= LENGTH))
		    throw new IllegalArgumentException("ELEM_INFO STARTING_OFFSET "+STARTING_OFFSET+" inconsistent with ORDINATES length "+coords.size());
		if(!(eTYPE == ETYPE.POLYGON) &&
		   !(eTYPE == ETYPE.POLYGON_EXTERIOR) && !(eTYPE == ETYPE.POLYGON_INTERIOR)){
		    throw new IllegalArgumentException("ETYPE "+eTYPE+" inconsistent with expected POLYGON, POLYGON_EXTERIOR or POLYGON_INTERIOR");
		}
		if (!(INTERPRETATION == 1) && !(INTERPRETATION == 3)){
		    LOGGER.warning( "Could not create LinearRing with INTERPRETATION "+INTERPRETATION+" - we can only support 1 for straight edges");
			return null;
		}
        LinearRing ring;

        if (INTERPRETATION == 1) {
            ring = gf.createLinearRing(subList(
                        gf.getCoordinateSequenceFactory(), coords, GTYPE,
                        elemInfo, triplet));
        } else if (INTERPRETATION == 3) {
            // rectangle does not maintain measures
            //
            CoordinateSequence ext = subList(gf.getCoordinateSequenceFactory(),
                    coords, GTYPE, elemInfo, triplet);
            Coordinate min = ext.getCoordinate(0);
            Coordinate max = ext.getCoordinate(1);
            ring = gf.createLinearRing(new Coordinate[] {
                        min, new Coordinate(max.x, min.y), max,
                        new Coordinate(min.x, max.y), min
                    });
        } else {
            // May be INTERPRETATION == 2 for curves, or 4 for circle
            //
            throw new IllegalArgumentException("ELEM_INFO INTERPRETAION "
                + elemInfo[2] + " not supported"
                + "for JTS Polygon Linear Rings."
                + "ELEM_INFO INTERPRETAION 1 and 3 are supported");
        }

        ring.setSRID(SRID);

        return ring;
    }

    /**
     * Create MultiPoint as encoded by elemInfo.
     * 
     * <p>
     * Encoded as a single triplet in elemInfo:
     * </p>
     * 
     * <ul>
     * <li>
     * STARTING_OFFSET: position in ordinal ordinate array
     * </li>
     * <li>
     * ETYPE: 1 for Point
     * </li>
     * <li>
     * INTERPRETATION: number of points
     * </li>
     * </ul>
     * 
     *
     * @param gf Used to construct polygon
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param SRID Spatial Reference System
     * @param elemInfo Interpretation of coords
     * @param triplet Triplet in elemInfo to process as a Polygon
     * @param coords Coordinates to interpret using elemInfo
     *
     */
    private static MultiPoint createMultiPoint(GeometryFactory gf,
        final int GTYPE, final int SRID, final int[] elemInfo,
        final int triplet, CoordinateSequence coords) {
        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, triplet);
        final int eTYPE = ETYPE(elemInfo, triplet);
        final int INTERPRETATION = INTERPRETATION(elemInfo, triplet);

		if (!(STARTING_OFFSET >= 1) || !(STARTING_OFFSET <= coords.size()))
		    throw new IllegalArgumentException("ELEM_INFO STARTING_OFFSET "+STARTING_OFFSET+" inconsistent with ORDINATES length "+coords.size());
		if(!(eTYPE == ETYPE.POINT))
		    throw new IllegalArgumentException("ETYPE "+eTYPE+" inconsistent with expected POINT");
		//CH- changed to >= 1, for GEOS-437, Jody and I looked at docs
		//and multipoint is a superset of point, so it should be fine,
		//for cases when there is just one point.  Bart is testing.
		if (!(INTERPRETATION >= 1)){
		    LOGGER.warning( "Could not create MultiPoint with INTERPRETATION "+INTERPRETATION+" - representing the number of points");
			return null;
		}

        final int LEN = D(GTYPE) + L(GTYPE);

        int start = (STARTING_OFFSET - 1) / LEN;
        int end = start + INTERPRETATION;

        MultiPoint points = gf.createMultiPoint(subList(
                    gf.getCoordinateSequenceFactory(), coords, start, end));
        points.setSRID(SRID);

        return points;
    }

    /**
     * Create MultiLineString as encoded by elemInfo.
     * 
     * <p>
     * Encoded as a series line of triplets in elemInfo:
     * </p>
     * 
     * <ul>
     * <li>
     * STARTING_OFFSET: position in ordinal ordinate array
     * </li>
     * <li>
     * ETYPE: 2 for Line
     * </li>
     * <li>
     * INTERPRETATION: 1 for straight edges
     * </li>
     * </ul>
     * 
     * <p></p>
     *
     * @param gf Used to construct MultiLineString
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param SRID Spatial Reference System
     * @param elemInfo Interpretation of coords
     * @param triplet Triplet in elemInfo to process as a Polygon
     * @param coords Coordinates to interpret using elemInfo
     * @param N Number of triplets (or -1 for rest)
     *
     */
    private static MultiLineString createMultiLine(GeometryFactory gf,
        final int GTYPE, final int SRID, final int[] elemInfo,
        final int triplet, CoordinateSequence coords, final int N) {
        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, triplet);
        final int eTYPE = ETYPE(elemInfo, triplet);
        final int INTERPRETATION = INTERPRETATION(elemInfo, triplet);

        final int LENGTH = coords.size()*D(GTYPE);
        
		if (!(STARTING_OFFSET >= 1) || !(STARTING_OFFSET <= LENGTH))
		    throw new IllegalArgumentException("ELEM_INFO STARTING_OFFSET "+STARTING_OFFSET+" inconsistent with ORDINATES length "+coords.size());
		if(!(eTYPE == ETYPE.LINE))
		    throw new IllegalArgumentException("ETYPE "+eTYPE+" inconsistent with expected LINE");
		if (!(INTERPRETATION == 1)){
            // we cannot represent INTERPRETATION > 1 
		    LOGGER.warning( "Could not create MultiLineString with INTERPRETATION "+INTERPRETATION+" - we can only represent 1 for straight edges");
			return null;
		}

        final int LEN = D(GTYPE) + L(GTYPE);
        final int endTriplet = (N != -1) ? (triplet + N) : (elemInfo.length / 3);

        List list = new LinkedList();
        int etype;
LINES: 		// bad bad gotos jody
        for (int i = triplet;
                (i < endTriplet) && ((etype = ETYPE(elemInfo, i)) != -1);
                i++) {
            if (etype == ETYPE.LINE) {
                list.add(createLine(gf, GTYPE, SRID, elemInfo, i, coords));
            } else { // not a LinearString - get out of here

                break LINES;	// goto LINES
            }
        }

        MultiLineString lines = gf.createMultiLineString(toLineStringArray(list));
        lines.setSRID(SRID);

        return lines;
    }

    /**
     * Create MultiPolygon as encoded by elemInfo.
     * 
     * <p>
     * Encoded as a series polygon triplets in elemInfo:
     * </p>
     * 
     * <ul>
     * <li>
     * STARTING_OFFSET: position in ordinal ordinate array
     * </li>
     * <li>
     * ETYPE: 2003 or 3 for Polygon
     * </li>
     * <li>
     * INTERPRETATION: 1 for straight edges, 3 for rectangle
     * </li>
     * </ul>
     * 
     * <p></p>
     *
     * @param gf Used to construct MultiLineString
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param SRID Spatial Reference System
     * @param elemInfo Interpretation of coords
     * @param triplet Triplet in elemInfo to process as a Polygon
     * @param coords Coordinates to interpret using elemInfo
     * @param N Number of triplets (or -1 for rest)
     *
     */
    private static MultiPolygon createMultiPolygon(GeometryFactory gf,
        final int GTYPE, final int SRID, final int[] elemInfo,
        final int triplet, CoordinateSequence coords, final int N) {
        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, triplet);
        final int eTYPE = ETYPE(elemInfo, triplet);
        final int INTERPRETATION = INTERPRETATION(elemInfo, triplet);
        final int LENGTH = coords.size()*D(GTYPE);
        
		if (!(STARTING_OFFSET >= 1) || !(STARTING_OFFSET <= LENGTH))
		    throw new IllegalArgumentException("ELEM_INFO STARTING_OFFSET "+STARTING_OFFSET+" inconsistent with ORDINATES length "+coords.size());
		if(!(eTYPE == ETYPE.POLYGON) && !(eTYPE == ETYPE.POLYGON_EXTERIOR))
		    throw new IllegalArgumentException("ETYPE "+eTYPE+" inconsistent with expected POLYGON or POLYGON_EXTERIOR");
		if (INTERPRETATION != 1 && INTERPRETATION != 3){
		    LOGGER.warning( "Could not create MultiPolygon with INTERPRETATION "+INTERPRETATION +" - we can only represent 1 for straight edges, or 3 for rectangle");
			return null;
		}
        final int LEN = D(GTYPE) + L(GTYPE);
        final int endTriplet = (N != -1) ? (triplet + N)
                                         : ((elemInfo.length / 3) + 1);

        List list = new LinkedList();
        int etype;
POLYGONS: 
        for (int i = triplet;
                (i < endTriplet) && ((etype = ETYPE(elemInfo, i)) != -1);
                i++) {
            if ((etype == ETYPE.POLYGON) || (etype == ETYPE.POLYGON_EXTERIOR)) {
                Polygon poly = createPolygon(gf, GTYPE, SRID, elemInfo, i,
                        coords);
                i += poly.getNumInteriorRing(); // skip interior rings
                list.add(poly);
            } else { // not a Polygon - get out here

                break POLYGONS;
            }
        }

        MultiPolygon polys = gf.createMultiPolygon(toPolygonArray(list));
        polys.setSRID(SRID);

        return polys;
    }

    /**
     * Create MultiPolygon as encoded by elemInfo.
     * 
     * <p>
     * Encoded as a series polygon triplets in elemInfo:
     * </p>
     * 
     * <ul>
     * <li>
     * STARTING_OFFSET: position in ordinal ordinate array
     * </li>
     * <li>
     * ETYPE: 2003 or 3 for Polygon
     * </li>
     * <li>
     * INTERPRETATION: 1 for straight edges, 2 for rectangle
     * </li>
     * </ul>
     * 
     * <p></p>
     *
     * TODO: Confirm that createCollection is not getting cut&paste mistakes from polygonCollection
     * 
     * @param gf Used to construct MultiLineString
     * @param GTYPE Encoding of <b>D</b>imension, <b>L</b>RS and <b>TT</b>ype
     * @param SRID Spatial Reference System
     * @param elemInfo Interpretation of coords
     * @param triplet Triplet in elemInfo to process as a Polygon
     * @param coords Coordinates to interpret using elemInfo
     * @param N Number of triplets (or -1 for rest)
     *
     * @return GeometryCollection
     *
     * @throws IllegalArgumentException DWhen faced with an encoding error
     */
    private static GeometryCollection createCollection(GeometryFactory gf,
        final int GTYPE, final int SRID, final int[] elemInfo,
        final int triplet, CoordinateSequence coords, final int N) {
        final int STARTING_OFFSET = STARTING_OFFSET(elemInfo, triplet);
        final int eTYPE = ETYPE(elemInfo, triplet);
        final int INTERPRETATION = INTERPRETATION(elemInfo, triplet);

        final int LENGTH = coords.size()*D(GTYPE);
        
		if (!(STARTING_OFFSET >= 1) || !(STARTING_OFFSET <= LENGTH))
		    throw new IllegalArgumentException("ELEM_INFO STARTING_OFFSET "+STARTING_OFFSET+" inconsistent with ORDINATES length "+coords.size());
		
        final int LEN = D(GTYPE) + L(GTYPE);
        final int endTriplet = (N != -1) ? (triplet + N)
                                         : ((elemInfo.length / 3) + 1);

        List list = new LinkedList();
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
                    throw new IllegalArgumentException(
                        "ETYPE.POINT requires INTERPRETATION >= 1");
                }

                break;

            case ETYPE.LINE:
                geom = createLine(gf, GTYPE, SRID, elemInfo, i, coords);

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
            case ETYPE.COMPOUND_POLYGON_INTERIOR:default:
                throw new IllegalArgumentException("ETYPE " + etype
                    + " not representable as a JTS Geometry."
                    + "(Custom and Compound Straight and Curved Geometries not supported)");
            }

            list.add(geom);
        }

        GeometryCollection geoms = gf.createGeometryCollection(toGeometryArray(
                    list));
        geoms.setSRID(SRID);

        return geoms;
    }
}
