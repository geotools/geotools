/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.logging.Logger;

import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BBOX;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Encodes a filter into a SQL WHERE statement for MySQL.  This class adds
 * the ability to turn geometry filters into sql statements if they are
 * bboxes.
 *
 * @author Chris Holmes, TOPP
 * @author Debasish Sahu, debasish.sahu@rmsi.com
 * @author Harry Bullen
 *
 *
 * @source $URL$
 */
public class SQLEncoderMySQL extends org.geotools.data.jdbc.FilterToSQL implements org.opengis.filter.FilterVisitor {
    /** Standard java logger */
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.filter");

    /** To write geometry so mysql can read it. */
    private static WKTWriter wkt = new WKTWriter();

    /** The standard SQL multicharacter wild card. */
    private static final String SQL_WILD_MULTI = "%";

    /** The standard SQL single character wild card. */
    private static final String SQL_WILD_SINGLE = "_";

    /**
     * The srid of the schema, so the bbox conforms.  Could be better to have
     * it in the bbox filter itself, but this works for now.
     */
    private int srid;

    /** The geometry attribute to use if none is specified. */
    private String defaultGeom;

    /** The escaped version of the multiple wildcard for the REGEXP pattern. */
    private String escapedWildcardMulti = "\\.\\*";

    /** The escaped version of the single wildcard for the REGEXP pattern. */
    private String escapedWildcardSingle = "\\.\\?";

    /**
     * Empty constructor TODO: rethink empty constructor, as BBOXes _need_ an
     * SRID, must make client set it somehow.  Maybe detect when encode is
     * called?
     */
    public SQLEncoderMySQL() {
        capabilities = createFilterCapabilities();

        setSqlNameEscape("");
    }

    public SQLEncoderMySQL(int srid) {
        this.srid = srid;
    }

    /**
     * @see org.geotools.filter.SQLEncoder#createFilterCapabilities()
     */
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities capabilities = new FilterCapabilities();

        capabilities.addType(FilterCapabilities.LOGIC_OR);
        capabilities.addType(FilterCapabilities.LOGIC_AND);
        capabilities.addType(FilterCapabilities.LOGIC_NOT);
        capabilities.addAll(FilterCapabilities.LOGICAL_OPENGIS);
        capabilities.addType(FilterCapabilities.COMPARE_EQUALS);
        capabilities.addType(FilterCapabilities.COMPARE_NOT_EQUALS);
        capabilities.addType(FilterCapabilities.COMPARE_LESS_THAN);
        capabilities.addType(FilterCapabilities.COMPARE_GREATER_THAN);
        capabilities.addType(FilterCapabilities.COMPARE_LESS_THAN_EQUAL);
        capabilities.addType(FilterCapabilities.COMPARE_GREATER_THAN_EQUAL);
        capabilities.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
        capabilities.addType(FilterCapabilities.NULL_CHECK);
        capabilities.addType(PropertyIsNull.class);
        capabilities.addType(FilterCapabilities.BETWEEN);
        capabilities.addType(PropertyIsBetween.class);
        capabilities.addType(FilterCapabilities.NONE);
        capabilities.addType(FilterCapabilities.ALL);
        capabilities.addType(FilterCapabilities.SPATIAL_BBOX);
        capabilities.addType(BBOX.class);
        capabilities.addType(FilterCapabilities.FID);
        capabilities.addType(Id.class);
        capabilities.addType(FilterCapabilities.LIKE);
        capabilities.addType(PropertyIsLike.class);
        
        return capabilities;
    }

    /**
     * Sets a spatial reference system ESPG number, so that the geometry can be
     * properly encoded for postgis.  If geotools starts actually creating
     * geometries with valid srids then this method will no longer be needed.
     *
     * @param srid the integer code for the EPSG spatial reference system.
     */
    public void setSRID(int srid) {
        this.srid = srid;
    }

    /**
     * Sets the default geometry, so that filters with null for one of their
     * expressions can assume that the default geometry is intended.
     *
     * @param name the name of the default geometry Attribute.
     *
     * @task REVISIT: pass in a featureType so that geometries can figure out
     *       their own default geometry?
     */
    public void setDefaultGeometry(String name) {
        //Do we really want clients to be using malformed filters?  
        //I mean, this is a useful method for unit tests, but shouldn't 
        //fully formed filters usually be used?  Though I guess adding 
        //the option wouldn't hurt. -ch
        this.defaultGeom = name;
    }

    /**
     * Turns a geometry filter into the mySQL bbox statement.
     *
     * @param filter the geometry filter to be encoded.
     *
     * @throws RuntimeException for IO exception (need a better error)
     */
    
    public Object visit(BBOX filter, Object extra)
    {
            DefaultExpression left = (DefaultExpression) filter.getExpression1();
            DefaultExpression right = (DefaultExpression) filter.getExpression2();

            // left and right have to be valid expressions
            try {
                out.write("MBRIntersects(");

                if (left == null) {
                    out.write(defaultGeom);
                } else {
			// we want this to be returned as a Geometry
                    left.accept(this,Geometry.class);
                }

                out.write(", ");

                if (right == null) {
                    out.write(defaultGeom);
                } else {
			// we want this to be returned as a Geometry
                    right.accept(this,Geometry.class);
                }

                out.write(")");
            } catch (java.io.IOException ioe) {
                LOGGER.warning("Unable to export filter" + ioe);
            }
	    return extra;
    }

    /**
     * Checks to see if the literal is a geometry, and encodes it if it  is, if
     * not just sends to the parent class.
     *
     * @param expression the expression to visit and encode.
     *
     * @throws IOException for IO exception (need a better error)
     * TODO: this should use WKB
     */
    public void visitLiteralGeometry(Literal expression)
        throws IOException {
        Geometry bbox = (Geometry) expression.getValue();
        String geomText = wkt.write(bbox);
        out.write("GeometryFromText('" + geomText + "', " + srid + ")");
    }

    public void visit(PropertyIsLike filter) {
        try {
            String pattern = filter.getLiteral();

            pattern = pattern.replaceAll(escapedWildcardMulti, SQL_WILD_MULTI);
            pattern = pattern.replaceAll(escapedWildcardSingle, SQL_WILD_SINGLE);

            //pattern = pattern.replace('\\', ''); //get rid of java escapes.
            out.write("UPPER(");
            filter.getExpression().accept(this,null);
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
}
