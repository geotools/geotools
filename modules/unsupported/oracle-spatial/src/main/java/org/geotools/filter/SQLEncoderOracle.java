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
 */
package org.geotools.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.oracle.sdo.SDO;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Encodes Geometry filters into valid oracle SDO statements.
 * 
 * <p>
 * At this stage it only supports the GEOMETRY_BBOX types.
 * </p>
 * 
 * <p>
 * Encoded filters get written to the protected Writer called <code>out</code>
 * </p>
 *
 * @author $Author: seangeo $
 * @source $URL$
 * @version $Id$
 */
public class SQLEncoderOracle extends SQLEncoder {

    /** Logger - for logging */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.filter.SQLEncoderOracle");

    /** Contains filter type to SDO_RELATE mask type mappings */
    private static final Map SDO_RELATE_MASK_MAP = new HashMap();

    /** The standard SQL multicharacter wild card. */
    private static final String SQL_WILD_MULTI = "%";

    /** The standard SQL single character wild card. */
    private static final String SQL_WILD_SINGLE = "_";

    /** Default tolerance for spatial queries. */
    private static final String TOLERANCE = "0.001";

    static {
        SDO_RELATE_MASK_MAP.put(new Short(AbstractFilter.GEOMETRY_CONTAINS),
            "contains");
        SDO_RELATE_MASK_MAP.put(new Short(AbstractFilter.GEOMETRY_CROSSES),
            "overlapbydisjoint");
        SDO_RELATE_MASK_MAP.put(new Short(AbstractFilter.GEOMETRY_EQUALS),
            "equal");
        SDO_RELATE_MASK_MAP.put(new Short(AbstractFilter.GEOMETRY_OVERLAPS),
            "overlapbyintersect");
        SDO_RELATE_MASK_MAP.put(new Short(AbstractFilter.GEOMETRY_TOUCHES),
            "touch");
        SDO_RELATE_MASK_MAP.put(new Short(AbstractFilter.GEOMETRY_WITHIN),
            "inside");
        SDO_RELATE_MASK_MAP.put(new Short(AbstractFilter.GEOMETRY_DISJOINT),
            "anyinteract");
        
        //Ok, back to using these, as the not disjoint turned out to be a big
        //performance hit.  I would really like to see some solid testing on 
        //these though, as with a trivial case it really did not seem to work
        //right, not disjoint was giving different answers than anyinteract.
        SDO_RELATE_MASK_MAP.put(new Short(AbstractFilter.GEOMETRY_BBOX),
            "anyinteract");
        
        SDO_RELATE_MASK_MAP.put(new Short(AbstractFilter.GEOMETRY_INTERSECTS),
            "anyinteract");
    }

    /** The escaped version of the multiple wildcard for the REGEXP pattern. */
    //private String escapedWildcardMulti = "\W.\\*"; // "\\*"

    /** The escaped version of the single wildcard for the REGEXP pattern. */
    //private String escapedWildcardSingle = "\\.\\?";

    /**
     * The Spatial Reference System IDs  Keyed by ColumnName, value is Integer
     * SRID number
     */
    private Map srids;
    private String fidColumn;
    private String currentGeomColumnName = null;
    boolean inGeomFilter = false;

    public SQLEncoderOracle(String fidColumn, int defaultSRID) {
        this(new HashMap());
        this.fidColumn = fidColumn;
        srids.put(null, new Integer(defaultSRID));

        setSqlNameEscape("\"");
    }

    public SQLEncoderOracle(int defaultSRID) {
        this(null, new HashMap());
        srids.put(null, new Integer(defaultSRID));
    }

    /**
     * Creates a new SQLEncoderOracle with a specified SRID.
     *
     * @param fidColumn DOCUMENT ME!
     * @param srids The Spatial Reference ID to use when generating SDO SQL
     *        statements.
     */
    public SQLEncoderOracle(String fidColumn, Map srids) {
        currentGeomColumnName = null;
        this.fidColumn = fidColumn;
        this.srids = srids;

        Set geomCols = srids.keySet();

        if (geomCols.size() > 0) {
            currentGeomColumnName = (String) geomCols.iterator().next();
        }

        LOGGER.fine("SQLEncoderOracle: Geometric Column is: "
            + currentGeomColumnName);

        setSqlNameEscape("\"");
    }

    /**
     * Creates a new SQLEncoderOracle with a specified SRID.
     *
     * @param srids The Spatial Reference ID to use when generating SDO SQL
     *        statements.
     */
    public SQLEncoderOracle(Map srids) {
        this(null, srids);
    }

    /**
     * Sets the capabilities of this filter.
     *
     * @return FilterCapabilities for this Filter
     */
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities capabilities = new FilterCapabilities();

        capabilities.addType(FilterCapabilities.LOGICAL);
        capabilities.addAll(FilterCapabilities.LOGICAL_OPENGIS);
        capabilities.addType(FilterCapabilities.SIMPLE_COMPARISONS);
        capabilities.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
        capabilities.addType(FilterCapabilities.NULL_CHECK);
        capabilities.addType(PropertyIsNull.class);
        capabilities.addType(FilterCapabilities.BETWEEN);
        capabilities.addType(PropertyIsBetween.class);
        capabilities.addType(FilterCapabilities.FID);
        capabilities.addType(Id.class);
        capabilities.addType(FilterCapabilities.NONE);
        capabilities.addType(IncludeFilter.class);
        capabilities.addType(FilterCapabilities.ALL);
        capabilities.addType(ExcludeFilter.class);
        capabilities.addType(FilterCapabilities.LIKE);
        capabilities.addType(PropertyIsLike.class);
        capabilities.addType(FilterCapabilities.SPATIAL_BBOX);
        capabilities.addType(BBOX.class);
        capabilities.addType(FilterCapabilities.SPATIAL_CONTAINS);
        capabilities.addType(Contains.class);
        capabilities.addType(FilterCapabilities.SPATIAL_CROSSES);
        capabilities.addType(Crosses.class);
        capabilities.addType(FilterCapabilities.SPATIAL_DISJOINT);
        capabilities.addType(Disjoint.class);
        capabilities.addType(FilterCapabilities.SPATIAL_EQUALS);
        capabilities.addType(Equals.class);
        capabilities.addType(FilterCapabilities.SPATIAL_INTERSECT);
        capabilities.addType(Intersects.class);
        capabilities.addType(FilterCapabilities.SPATIAL_OVERLAPS);
        capabilities.addType(Overlaps.class);
        capabilities.addType(FilterCapabilities.SPATIAL_TOUCHES);
        capabilities.addType(Touches.class);
        capabilities.addType(FilterCapabilities.SPATIAL_WITHIN);
        capabilities.addType(Within.class);
        capabilities.addType(FilterCapabilities.SPATIAL_DWITHIN);
        capabilities.addType(DWithin.class);
        capabilities.addType(FilterCapabilities.SPATIAL_BEYOND);
        capabilities.addType(Beyond.class);

        return capabilities;
    }

    /**
     * Reverting back to just using anyinteract, as Thijs says this is a bad
     * performance hit
     * This is a special case for bbox and intersects filters, as the former
     * using of 'anyinteract' does not seem to be exactly a not disjoint, 
     * which is what is needed according to ogc specs.  
     *
     * @param geomFilter DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    /* private void doNotDisjointFilter(GeometryFilter geomFilter)
        throws IOException {
        //String mask = (String) SDO_RELATE_MASK_MAP.get(new Short(
        //            geomFilter.getFilterType()));
        Expression left = geomFilter.getLeftGeometry();
        Expression right = geomFilter.getRightGeometry();

        if (((left != null) || (currentGeomColumnName != null))
                && (right != null)) {
            inGeomFilter = true;
            out.write("NOT SDO_RELATE(");

            if (left != null) {
                left.accept(this);
            } else {
                out.write(currentGeomColumnName);
            }

            out.write(",");
            right.accept(this);
            out.write(",'mask=disjoint querytype=WINDOW') = 'TRUE' ");
            inGeomFilter = false;
        } else {
            LOGGER.warning("Invalid filter. Cannot have a Geometry filter "
                + "with only one expression.");
        }        
	}*/

    private void doSdoRelate(GeometryFilter geomFilter)
        throws IOException {
        String mask = (String) SDO_RELATE_MASK_MAP.get(new Short(
                    geomFilter.getFilterType()));
        AttributeExpression attExpr;
        LiteralExpression geomExpr;
        Expression left = geomFilter.getLeftGeometry();
        Expression right = geomFilter.getRightGeometry();
        if (left instanceof AttributeExpression &&
            right instanceof LiteralExpression) {
            attExpr = (AttributeExpression) left;
            geomExpr = (LiteralExpression) right;
        } else if (right instanceof AttributeExpression &&
                   left instanceof LiteralExpression) {
            attExpr = (AttributeExpression) right;
            geomExpr = (LiteralExpression) left;
        } else {
            String err = "Oracle currently supports one geometry and one " +
                "attribute expr.  You gave: " + left + ", " + right;
            throw new DataSourceException(err);
	}

        if (((attExpr != null) || (currentGeomColumnName != null))
                && (geomExpr != null) && (mask != null)) {
            inGeomFilter = true;
            out.write("SDO_RELATE(");

            if (attExpr != null) {
                attExpr.accept(this);
            } else {
                out.write("\"" + currentGeomColumnName + "\"");
            }

            out.write(",");
            geomExpr.accept(this);
            // for disjoint we ask for no interaction, anyinteract == false
            if(geomFilter.getFilterType() == AbstractFilter.GEOMETRY_DISJOINT) {
                out.write(",'mask=" + mask + " querytype=WINDOW') <> 'TRUE' ");
            } else {
                out.write(",'mask=" + mask + " querytype=WINDOW') = 'TRUE' ");
            }
            
            inGeomFilter = false;
        } else {
            LOGGER.warning("Invalid filter. Cannot have a Geometry filter "
                + "with only one expression.");
        }
    }

    /**
     * Performs a geometry distance filter, must be either a dwithin or a 
     * a beyond filter.  Uses the SDO_WITHIN_DISTANCE function, dwithin matches
     * for true, beyond for false.  
     * 
     * @param geomFilter the filter to use, must be a dwithin or beyond
     */
    private void doSdoDistance(GeometryDistanceFilter geomFilter)
        throws IOException {
       //  String mask = (String) SDO_RELATE_MASK_MAP.get(new Short(
       //             geomFilter.getFilterType()));
        //TODO: don't assume left and right are correct order, see doSdoRelate
        Expression left = geomFilter.getLeftGeometry();
        Expression right = geomFilter.getRightGeometry();
        double distance = geomFilter.getDistance();
	//only dwithin and beyond, dwithin matches true for sdo_within_distance
        boolean isDWithin = geomFilter.getFilterType() == 
            AbstractFilter.GEOMETRY_DWITHIN;
	String boolValue = isDWithin ? "TRUE" : "FALSE";

        if ((left != null) && (right != null)) {
            inGeomFilter = true;
            out.write("SDO_WITHIN_DISTANCE(");
            left.accept(this);
            out.write(",");
            right.accept(this);
            out.write(",'distance=" + distance + "') = '" + boolValue + "' ");
            inGeomFilter = false;
        } else {
            LOGGER.warning("Invalid filter for DWithin. Cannot have a Geometry filter "
                + "with only one expression.");
        }
    }

    /**
     * Converts JTS Geometry to a String version of a SDO Geometry.  This
     * should move to a utility class, as we now have more than  one class
     * using this (which is why it changed to public static)
     * 
     * TODO: Multi eometries
     *
     * @param geometry The JTS Geometry to convert.
     * @param srid DOCUMENT ME!
     *
     * @return A String representation of the SDO Geometry.
     */
    public static String toSDOGeom(Geometry geometry, int srid) {
        if (Point.class.isAssignableFrom(geometry.getClass())) {
            return toSDOGeom((Point) geometry, srid);
        } else if (LineString.class.isAssignableFrom(geometry.getClass())) {
            return toSDOGeom((LineString) geometry, srid);
        } else if (Polygon.class.isAssignableFrom(geometry.getClass())) {
            if(geometry.equals(geometry.getEnvelope())) {
                return toSDOGeom(geometry.getEnvelopeInternal(), srid);
            } else {
                return toSDOGeom((Polygon) geometry, srid);
            }
        } else if (MultiLineString.class.isAssignableFrom(geometry.getClass())) {
            return toSDOGeom((MultiLineString) geometry, srid);
        }
        else if (MultiPolygon.class.isAssignableFrom(geometry.getClass())) {
            return toSDOGeom((MultiPolygon) geometry, srid);
        }
        else {
            LOGGER.warning("Got a literal geometry that I can't handle: "
                + geometry.getClass().getName());

            return "";
        }
    }

    /**
     * TODO: Encode more then 1
     * @param line
     * @param srid
     */
    private static String toSDOGeom(MultiLineString line, int srid) {
         if( line.getNumGeometries() == 1 ){
        	 return toSDOGeom( line.getGeometryN(0), srid);        	 
         }
         throw new UnsupportedOperationException("Cannot encode MultiLineString (yet)");
    }
    /**
     * TODO: Encode more then 1
     * @param line
     * @param srid
     */
    private static String toSDOGeom(MultiPolygon polygon, int srid) {
        if( polygon.getNumGeometries() == 1 ){
       	 return toSDOGeom( polygon.getGeometryN(0), srid);        	 
        }
        throw new UnsupportedOperationException("Cannot encode MultiPolygon (yet)");
   }
    /**
     * Converts a LineString Geometry in an SDO SQL geometry construction
     * statement.
     * 
     * <p>
     * 2D geometries is assumed. If higher dimensional geometries are used the
     * query will be encoded as a 2D geometry.
     * </p>
     *
     * @param line The line to encode.
     * @param srid DOCUMENT ME!
     *
     * @return An SDO SQL geometry object construction statement
     */
    private static String toSDOGeom(LineString line, int srid) {
        if (SDO.D(line) > 2) {
            LOGGER.warning("" + SDO.D(line)
                + " dimensioned geometry provided."
                + " This encoder only supports 2D geometries. The query will be constructed as"
                + " a 2D query.");
        }

        StringBuffer buffer = new StringBuffer("MDSYS.SDO_GEOMETRY(");

        buffer.append(SDO.D(line));
        buffer.append("002,");

        if (srid > 0) {
            LOGGER.fine("Using layer SRID: " + srid);
            buffer.append(srid);
        } else {
            LOGGER.fine("Using NULL SRID: ");
            buffer.append("NULL");
        }

        buffer.append(",NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,2,1),");
        buffer.append("MDSYS.SDO_ORDINATE_ARRAY(");

        Coordinate[] coordinates = line.getCoordinates();

        for (int i = 0; i < coordinates.length; i++) {
            buffer.append(coordinates[i].x);
            buffer.append(",");
            buffer.append(coordinates[i].y);

            if (i != (coordinates.length - 1)) {
                buffer.append(",");
            }
        }

        buffer.append("))");

        return buffer.toString();
    }

    /**
     * Converts a Point Geometry in an SDO SQL geometry construction statement.
     * 
     * <p>
     * 2D geometries is assumed. If higher dimensional geometries are used the
     * query will be encoded as a 2D geometry.
     * </p>
     *
     * @param point The point to encode.
     * @param srid DOCUMENT ME!
     *
     * @return An SDO SQL geometry object construction statement
     */
    private static String toSDOGeom(Point point, int srid) {
        if (SDO.D(point) > 2) {
            LOGGER.warning("" + SDO.D(point)
                + " dimensioned geometry provided."
                + " This encoder only supports 2D geometries. The query will be constructed as"
                + " a 2D query.");
        }

        StringBuffer buffer = new StringBuffer("MDSYS.SDO_GEOMETRY(");

        buffer.append(SDO.D(point));
        buffer.append("001,");

        if (srid > 0) {
            LOGGER.fine("Using layer SRID: " + srid);
            buffer.append(srid);
        } else {
            LOGGER.fine("Using NULL SRID: ");
            buffer.append("NULL");
        }

        buffer.append(",MDSYS.SDO_POINT_TYPE(");
        buffer.append(point.getX());
        buffer.append(",");
        buffer.append(point.getY());
        buffer.append(",NULL),NULL,NULL)");

        return buffer.toString();
    }

    /**
     * Converts a Polygon Geometry in an SDO SQL geometry construction
     * statement.
     * 
     * <p>
     * 2D geometries is assumed. If higher dimensional geometries are used the
     * query will be encoded as a 2D geometry.
     * </p>
     *
     * @param polygon The polygon to encode.
     * @param srid DOCUMENT ME!
     *
     * @return An SDO SQL geometry object construction statement
     */
    private static String toSDOGeom(Polygon polygon, int srid) {
        StringBuffer buffer = new StringBuffer();

        if (SDO.D(polygon) > 2) {
            LOGGER.warning("" + SDO.D(polygon)
                + " dimensioned geometry provided."
                + " This encoder only supports 2D geometries. The query will be constructed as"
                + " a 2D query.");
        }

        if (polygon.getExteriorRing() != null) {
            buffer.append("MDSYS.SDO_GEOMETRY(");
            buffer.append(SDO.D(polygon));
            buffer.append("003,");

            if (srid > 0) {
                LOGGER.fine("Using layer SRID: " + srid);
                buffer.append(srid);
            } else {
                LOGGER.fine("Using NULL SRID: ");
                buffer.append("NULL");
            }

            buffer.append(",NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1),");
            buffer.append("MDSYS.SDO_ORDINATE_ARRAY(");

	    CoordinateSequenceFactory fact = polygon.getFactory().getCoordinateSequenceFactory();
	    CoordinateSequence exterior = polygon.getExteriorRing().getCoordinateSequence();
	    CoordinateSequence coordSeq = SDO.counterClockWise(fact, exterior);

            for (int i = 0, size = coordSeq.size(); i < size; i++) {
		Coordinate cur = coordSeq.getCoordinate(i);
                buffer.append(cur.x);
                buffer.append(",");
                buffer.append(cur.y);

                if (i != (size - 1)) {
                    buffer.append(",");
                }
	    }

	    /* This could be expensive if coordSeq implementation is not an
	       an array.  Leaving in for now as I can't test, and this is
	       more likely to work right.
            Coordinate[] coordinates = coordSeq.toCoordinateArray();
            for (int i = 0; i < coordinates.length; i++) {
                buffer.append(coordinates[i].x);
                buffer.append(",");
                buffer.append(coordinates[i].y);

                if (i != (coordinates.length - 1)) {
                    buffer.append(",");
                }
		}*/

            buffer.append("))");
        } else {
            LOGGER.warning("No Exterior ring on polygon.  "
                + "This encode only supports Polygons with exterior rings.");
        }

        if (polygon.getNumInteriorRing() > 0) {
            LOGGER.warning("Polygon contains Interior Rings. "
                + "These rings will not be included in the query.");
        }

        return buffer.toString();
    }
    
    /**
     * Converts an Envelope in an SDO SQL geometry construction statement.
     *
     * @param envelope The envelope to encode.
     * @param srid DOCUMENT ME!
     *
     * @return An SDO SQL geometry object construction statement
     */
    private static String toSDOGeom(Envelope envelope, int srid) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("MDSYS.SDO_GEOMETRY(");
        buffer.append("2003,");

        if (srid > 0) {
            LOGGER.fine("Using layer SRID: " + srid);
            buffer.append(srid);
        } else {
            LOGGER.fine("Using NULL SRID: ");
            buffer.append("NULL");
        }

        buffer.append(",NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,3),");
        buffer.append("MDSYS.SDO_ORDINATE_ARRAY(");
        buffer.append(envelope.getMinX());
        buffer.append(",");
        buffer.append(envelope.getMinY());
        buffer.append(",");
        buffer.append(envelope.getMaxX());
        buffer.append(",");
        buffer.append(envelope.getMaxY());
        buffer.append("))");

        return buffer.toString();
    }

    /**
     * Handles Geometry Filter encoding. Currently only supports the encoding
     * of GEOMETRY_BBOX filters. If a GEOMETRY_BBOX filter is encounter it
     * will be converted into an SDO_RELATE() function. If another filter is
     * found, nothing will happen.
     *
     * @param geomFilter The geometry filter to encode.
     *
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.GeometryFilter)
     */
    public void visit(GeometryFilter geomFilter) {
        LOGGER.finer("Visiting a Geometry filter");

        try {
	    short filterType = geomFilter.getFilterType();
	    if ((filterType == AbstractFilter.GEOMETRY_DWITHIN)
	    || (filterType == AbstractFilter.GEOMETRY_BEYOND)){
                doSdoDistance((GeometryDistanceFilter)geomFilter);
		//} else if (filterType == AbstractFilter.GEOMETRY_INTERSECTS
		//|| filterType == AbstractFilter.GEOMETRY_BBOX) {
                //doNotDisjointFilter(geomFilter);
            } else 
            if (SDO_RELATE_MASK_MAP.get(new Short(geomFilter.getFilterType())) != null) {
                doSdoRelate(geomFilter);
            } else {
                LOGGER.warning("Unknown filter type: "
                    + geomFilter.getFilterType());
            }
        } catch (IOException e) {
            LOGGER.warning("IO Error exporting geometry filter");
        }
    }

    /**
     * Writes the SQL for the Like Filter.  Assumes the current java
     * implemented wildcards for the Like Filter: . for multi and .? for
     * single. And replaces them with the SQL % and _, respectively. Currently
     * does nothing, and should not be called, not included in the
     * capabilities.
     *
     * @param filter the Like Filter to be visited.
     *
     * @task TODO: LikeFilter doesn't work right...revisit this when it does.
     *       Need to think through the escape char, so it works right when
     *       Java uses one, and escapes correctly with an '_'.
     */
    public void visit(LikeFilter filter) {
        try {
            String pattern = filter.getPattern();
            
            String multi = "\\Q"+filter.getWildcardMulti()+"\\E"; 
            pattern = pattern.replaceAll( multi, SQL_WILD_MULTI);
            
            String single = "\\Q"+filter.getWildcardSingle()+"\\E";
            pattern = pattern.replaceAll( single, SQL_WILD_SINGLE);

            //pattern = pattern.replace('\\', ''); //get rid of java escapes.
            out.write("UPPER(");
            ((Expression) filter.getValue()).accept(this);
            out.write(") LIKE ");
            out.write("UPPER('" + pattern + "')");

            String esc = filter.getEscape();

            if (pattern.indexOf(esc) != -1) { //if it uses the escape char
                out.write(" ESCAPE " + "'" + esc + "'"); //this needs testing
            }

            //TODO figure out when to add ESCAPE clause, probably just for the
            // '_' char.
        } catch (java.io.IOException ioe) {
            LOGGER.warning("Unable to export filter" + ioe);
        }
    }

    /**
     * Converts a literal expression into a valid SDO object. Only handles
     * Literal Geometries, all other literals are passed up to the parent.
     *
     * @param literal The Literal expression to encode.
     *
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.LiteralExpression)
     */
    public void visit(LiteralExpression literal) {
        if (literal.getType() == DefaultExpression.LITERAL_GEOMETRY) {
            Geometry geometry = (Geometry) literal.getLiteral();

            try {
                int srid = -1;
                Integer sridO = (Integer) srids.get(currentGeomColumnName);

                if (sridO == null) {
                    // try for default
                    sridO = (Integer) srids.get(null);
                }

                if (sridO != null) {
                    srid = sridO.intValue();
                }

                out.write(toSDOGeom(geometry, srid));
            } catch (IOException e) {
                LOGGER.warning("IO Error exporting Literal Geometry");
            }
        } else {
            // can't do it, send it off to the parent
            super.visit(literal);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param filter
     *
     * @see org.geotools.filter.SQLEncoder#visit(org.geotools.filter.FidFilter)
     */
    public void visit(FidFilter filter) {
        if (fidColumn != null) {
            String[] fids = filter.getFids();
            LOGGER.finer("Exporting FID=" + Arrays.asList(fids));

            for (int i = 0; i < fids.length; i++) {
                try {
                    out.write(fidColumn);
                    out.write(" = '");

                    int pos;

                    if ((pos = fids[i].indexOf('.')) != -1) {
                        out.write(fids[i].substring(pos + 1));
                    } else {
                        out.write(fids[i]);
                    }

                    out.write("'");

                    if (i < (fids.length - 1)) {
                        out.write(" OR ");
                    }
                } catch (IOException e) {
                    LOGGER.warning("IO Error exporting FID Filter.");
                }
            }
        } else {
            super.visit(filter);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.filter.SQLEncoder#visit(org.geotools.filter.AttributeExpression)
     */
    public void visit(AttributeExpression ae) throws RuntimeException {
        super.visit(ae);

        if (inGeomFilter) {
            if(featureType != null) {
                // handle default geometry as well
                AttributeDescriptor attributeType = (AttributeDescriptor) ae.evaluate( featureType );
                currentGeomColumnName = attributeType.getLocalName();
            } else {
                currentGeomColumnName = ae.getAttributePath();
            }
        }
    }
    
    public void visit(IncludeFilter filter) {
        try {
            out.write("1 = 1");
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
        
    }

    public void visit(ExcludeFilter filter) {
        try {
            out.write("1 = 0");
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
    }
    
    @Override
    public void visit(CompareFilter filter) throws RuntimeException {
        DefaultExpression left = (DefaultExpression) filter.getLeftValue();
        DefaultExpression right = (DefaultExpression) filter.getRightValue();
        String type = (String) comparisions.get(new Integer(
                filter.getFilterType()));
        
        if ( !filter.isMatchingCase() ) {
            //only for == or != 
            if ( filter.getFilterType() == Filter.COMPARE_EQUALS || 
                    filter.getFilterType() == Filter.COMPARE_NOT_EQUALS ) {
                
                //only for strings
                if ( left.getType() == Expression.LITERAL_STRING  
                        || right.getType() == Expression.LITERAL_STRING ) {
                    
                    try {
                        out.write( "lower(" ); left.accept( this ); out.write( ")");
                        out.write( " " + type + " " );
                        out.write( "lower(" ); right.accept( this ); out.write( ")");
                    
                        return;
                    } catch(IOException e) {
                        throw new RuntimeException("Error occurred writing filter", e);
                    }
                }
            }
        }
        
        super.visit(filter);
    }
}
