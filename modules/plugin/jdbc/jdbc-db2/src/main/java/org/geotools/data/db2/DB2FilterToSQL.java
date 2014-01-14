/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2012, Open Source Geospatial Foundation (OSGeo)
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
 */

package org.geotools.data.db2;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.function.FilterFunction_strConcat;
import org.geotools.filter.function.FilterFunction_strEndsWith;
import org.geotools.filter.function.FilterFunction_strEqualsIgnoreCase;
import org.geotools.filter.function.FilterFunction_strIndexOf;
import org.geotools.filter.function.FilterFunction_strLength;
import org.geotools.filter.function.FilterFunction_strReplace;
import org.geotools.filter.function.FilterFunction_strStartsWith;
import org.geotools.filter.function.FilterFunction_strSubstring;
import org.geotools.filter.function.FilterFunction_strSubstringStart;
import org.geotools.filter.function.FilterFunction_strToLowerCase;
import org.geotools.filter.function.FilterFunction_strToUpperCase;
import org.geotools.filter.function.FilterFunction_strTrim;
import org.geotools.filter.function.FilterFunction_strTrim2;
import org.geotools.filter.function.math.FilterFunction_abs;
import org.geotools.filter.function.math.FilterFunction_abs_2;
import org.geotools.filter.function.math.FilterFunction_abs_3;
import org.geotools.filter.function.math.FilterFunction_abs_4;
import org.geotools.filter.function.math.FilterFunction_ceil;
import org.geotools.filter.function.math.FilterFunction_floor;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;
import org.geotools.jdbc.SQLDialect;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Generate a WHERE clause for DB2 Spatial Extender based on a spatial filter.
 * 
 * <p>
 * The following spatial filter operations are supported:
 * 
 * <ul>
 * <li>
 * GEOMETRY_BBOX</li>
 * <li>
 * GEOMETRY_CONTAINS</li>
 * <li>
 * GEOMETRY_CROSSES</li>
 * <li>
 * GEOMETRY_DISJOINT</li>
 * <li>
 * GEOMETRY_EQUALS</li>
 * <li>
 * GEOMETRY_INTERSECTS</li>
 * <li>
 * GEOMETRY_OVERLAPS</li>
 * <li>
 * GEOMETRY_TOUCHES</li>
 * <li>
 * GEOMETRY_WITHIN</li>
 * <li>
 * GEOMETRY_DWITHIN</li>
 * </ul>
 * </p>
 * 
 * @author Mueller Christian
 * 
 * 
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/jdbc/jdbc-db2/src/main/java/org/geotools
 *         /data/db2/DB2FilterToSQL.java $
 */
public class DB2FilterToSQL extends PreparedFilterToSQL {
    private static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.data.db2");

    // Class to convert geometry value into a Well-known Text string
    private static WKTWriter wktWriter = new WKTWriter();

    boolean functionEncodingEnabled = false;

    static private HashMap<Class<?>, String> DB2_SPATIAL_PREDICATES = new HashMap<Class<?>, String>();

    public DB2FilterToSQL(PreparedStatementSQLDialect dialect) {
        super(dialect);
        // TODO Auto-generated constructor stub
    }

    public DB2FilterToSQL(Writer out) {
        super(out);
        // TODO Auto-generated constructor stub
    }

    // Only intended for test purposes
    public HashMap<Class<?>, String> getPredicateMap() {
        return DB2_SPATIAL_PREDICATES;
    }

    // The SELECTIVITY clause to be used with spatial predicates.
    private String selectivityClause = null;

    private boolean looseBBOXEnabled = false;

    {
        DB2_SPATIAL_PREDICATES.put(BBOX.class, "EnvelopesIntersect");
        DB2_SPATIAL_PREDICATES.put(Contains.class, "ST_Contains");
        DB2_SPATIAL_PREDICATES.put(Crosses.class, "ST_Crosses");
        DB2_SPATIAL_PREDICATES.put(Disjoint.class, "ST_Disjoint");
        DB2_SPATIAL_PREDICATES.put(Equals.class, "ST_Equals");
        DB2_SPATIAL_PREDICATES.put(Intersects.class, "ST_Intersects");
        DB2_SPATIAL_PREDICATES.put(Overlaps.class, "ST_Overlaps");
        DB2_SPATIAL_PREDICATES.put(Touches.class, "ST_Touches");
        DB2_SPATIAL_PREDICATES.put(Within.class, "ST_Within");
        DB2_SPATIAL_PREDICATES.put(DWithin.class, "ST_Distance");
        DB2_SPATIAL_PREDICATES.put(Beyond.class, "ST_Distance");
    }

    /**
     * Construct a geometry from the WKT representation of a geometry
     * 
     * @param geom
     *            the constructor for the geometry.
     * 
     */
    public String db2Geom(Geometry geom) {
        String geomType = geom.getGeometryType();
        String g1 = geom.toText();
        String g2 = "db2gse.ST_" + geomType + "('" + g1 + "'," + getSRID() + ")";
        return g2;
    }

    /**
     * Sets the DB2 filter capabilities.
     * 
     * @return FilterCapabilities for DB2
     */
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities caps = new FilterCapabilities();
        caps.addAll(SQLDialect.BASE_DBMS_CAPABILITIES);

        // adding the spatial filters support
        caps.addType(BBOX.class);
        caps.addType(Contains.class);
        caps.addType(Crosses.class);
        caps.addType(Disjoint.class);
        caps.addType(Equals.class);
        caps.addType(Intersects.class);
        caps.addType(Overlaps.class);
        caps.addType(Touches.class);
        caps.addType(Within.class);
        caps.addType(DWithin.class);
        caps.addType(Beyond.class);

        // temporal filters
        caps.addType(After.class);
        caps.addType(Before.class);
        caps.addType(Begins.class);
        caps.addType(BegunBy.class);
        caps.addType(During.class);
        caps.addType(TOverlaps.class);
        caps.addType(Ends.class);
        caps.addType(EndedBy.class);
        caps.addType(TEquals.class);

        if (isFunctionEncodingEnabled()) {
            // add support for string functions
            caps.addType(FilterFunction_strConcat.class);
            caps.addType(FilterFunction_strEndsWith.class);
            caps.addType(FilterFunction_strStartsWith.class);
            caps.addType(FilterFunction_strEqualsIgnoreCase.class);
            caps.addType(FilterFunction_strIndexOf.class);
            caps.addType(FilterFunction_strLength.class);
            caps.addType(FilterFunction_strToLowerCase.class);
            caps.addType(FilterFunction_strToUpperCase.class);
            caps.addType(FilterFunction_strReplace.class);
            caps.addType(FilterFunction_strSubstring.class);
            caps.addType(FilterFunction_strSubstringStart.class);
            caps.addType(FilterFunction_strTrim.class);
            caps.addType(FilterFunction_strTrim2.class);

            // add support for math functions
            caps.addType(FilterFunction_abs.class);
            caps.addType(FilterFunction_abs_2.class);
            caps.addType(FilterFunction_abs_3.class);
            caps.addType(FilterFunction_abs_4.class);
            caps.addType(FilterFunction_ceil.class);
            caps.addType(FilterFunction_floor.class);
        }

        return caps;
    }

    /**
     * Sets a SELECTIVITY clause that can be included with the spatial predicate to influence the
     * query optimizer to exploit a spatial index if it exists.
     * 
     * <p>
     * The parameter should be of the form: <br>
     * "SELECTIVITY 0.001" <br>
     * where the numeric value is the fraction of rows that will be returned by using the index
     * scan. This doesn't have to be true. The value 0.001 is typically used to force the use of the
     * spatial in all cases if the spatial index exists.
     * </p>
     * 
     * @param string
     *            a selectivity clause
     */
    public void setSelectivityClause(String string) {
        this.selectivityClause = string;
    }

    @Override
    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter,
            PropertyName property, Literal geometry, boolean swapped, Object extraData) {

        if (filter instanceof DistanceBufferOperator) {
            return visitDistanceSpatialOperator((DistanceBufferOperator) filter, property,
                    geometry, swapped, extraData);
        } else {
            return visitBinarySpatialOperator(filter, (Expression) property, (Expression) geometry,
                    swapped, extraData);

        }
    }

    Object visitDistanceSpatialOperator(DistanceBufferOperator filter, PropertyName property,
            Literal geometry, boolean swapped, Object extraData) {
        try {
            if ((filter instanceof DWithin && !swapped) || (filter instanceof Beyond && swapped)) {
                out.write("db2gse.ST_Distance(");
                property.accept(this, extraData);
                out.write(",");
                geometry.accept(this, extraData);
                out.write(") < ");
                out.write(Double.toString(filter.getDistance()));
            }
            if ((filter instanceof DWithin && swapped) || (filter instanceof Beyond && !swapped)) {
                out.write("db2gse.ST_Distance(");
                property.accept(this, extraData);
                out.write(",");
                geometry.accept(this, extraData);
                out.write(") > ");
                out.write(Double.toString(filter.getDistance()));
            }
            return extraData;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter, Expression e1,
            Expression e2, Object extraData) {
        return visitBinarySpatialOperator(filter, e1, e2, false, extraData);
    }

    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter, Expression e1,
            Expression e2, boolean swapped, Object extraData) {

        try {
            // currentSRID=getSRID();
            LOGGER.finer("Generating GeometryFilter WHERE clause for " + filter);
            if (filter instanceof Equals) {
                out.write("db2gse.ST_Equals");
            } else if (filter instanceof Disjoint) {
                out.write("db2gse.ST_Disjoint");
            } else if (filter instanceof Intersects || filter instanceof BBOX) {
                out.write("db2gse.ST_Intersects");
            } else if (filter instanceof Crosses) {
                out.write("db2gse.ST_Crosses");
            } else if (filter instanceof Within) {
                if (swapped)
                    out.write("db2gse.ST_Contains");
                else
                    out.write("db2gse.ST_Within");
            } else if (filter instanceof Contains) {
                if (swapped)
                    out.write("db2gse.ST_Within");
                else
                    out.write("db2gse.ST_Contains");
            } else if (filter instanceof Overlaps) {
                out.write("db2gse.ST_Overlaps");
            } else if (filter instanceof Touches) {
                out.write("db2gse.ST_Touches");
            } else {
                throw new RuntimeException("Unsupported filter type " + filter.getClass());
            }
            out.write("(");

            e1.accept(this, extraData);
            out.write(", ");
            e2.accept(this, extraData);

            out.write(") = 1 ");
            addSelectivity(); // add selectivity clause if needed

            LOGGER.fine(this.out.toString());
            return extraData;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Construct the appropriate geometry type from the WKT representation of a literal expression
     * 
     * @param expression
     *            the expression turn into a geometry constructor.
     * 
     * @throws IOException
     *             Passes back exception if generated by this.out.write()
     */
    public void visitLiteralGeometry(Literal expression) throws IOException {
        String wktRepresentation = wktWriter.write((Geometry) expression.getValue());
        int spacePos = wktRepresentation.indexOf(" ");
        String geomType = wktRepresentation.substring(0, spacePos);
        this.out.write("db2gse.ST_" + geomType + "('" + wktRepresentation + "', " + getSRID() + ")");
    }

    protected void addSelectivity() throws IOException {
        if (this.selectivityClause != null) {
            this.out.write(" " + this.selectivityClause);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.data.jdbc.FilterToSQL#visit(org.opengis.filter.ExcludeFilter,
     * java.lang.Object)
     */
    public Object visit(ExcludeFilter filter, Object extraData) {
        try {
            out.write("1=0");
        } catch (java.io.IOException ioe) {
        }
        ;
        return extraData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.data.jdbc.FilterToSQL#visit(org.opengis.filter.IncludeFilter,
     * java.lang.Object)
     */
    public Object visit(IncludeFilter filter, Object extraData) {
        try {
            out.write("1=1");
        } catch (java.io.IOException ioe) {
        }
        ;
        return extraData;
    }

    private Integer getSRID() {
        return getSRID(featureType.getGeometryDescriptor());
    }

    private Integer getSRID(String attrName) {
        AttributeDescriptor attrDescr = featureType.getDescriptor(attrName);
        if (attrDescr instanceof GeometryDescriptor) {
            return getSRID((GeometryDescriptor) attrDescr);
        }
        return currentSRID;
    }

    private Integer getSRID(GeometryDescriptor gDescr) {
        Integer result = null;
        if (gDescr != null)
            result = (Integer) gDescr.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID);

        if (result == null)
            result = currentSRID;
        return result;
    }

    public boolean isLooseBBOXEnabled() {
        return looseBBOXEnabled;
    }

    public void setLooseBBOXEnabled(boolean looseBBOXEnabled) {
        this.looseBBOXEnabled = looseBBOXEnabled;
    }

    static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    static{
        // Set DATE_FORMAT time zone to GMT, as Date's are always in GMT internaly. Otherwise we'll
        // get a local timezone encoding regardless of the actual Date value        
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    static SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    protected void writeLiteral(Object literal) throws IOException {
        if (literal instanceof Date) {
            out.write("'");
            if (literal instanceof java.sql.Date) {
                out.write(DATE_FORMAT.format(literal));
            } else {
                out.write(DATETIME_FORMAT.format(literal));
            }
            out.write("'");
        } else {
            super.writeLiteral(literal);
        }
    }

    public boolean isFunctionEncodingEnabled() {
        return functionEncodingEnabled;
    }

    public void setFunctionEncodingEnabled(boolean functionEncodingEnabled) {
        this.functionEncodingEnabled = functionEncodingEnabled;
    }

    /**
     * Performs custom visits for functions that cannot be encoded as
     * <code>functionName(p1, p2, ... pN).</code>
     * 
     * @param function
     * @param extraData
     * @return
     */
    public boolean visitFunction(Function function, Object extraData) throws IOException {
        if (function instanceof FilterFunction_strConcat) {
            Expression s1 = getParameter(function, 0, true);
            Expression s2 = getParameter(function, 1, true);
            out.write("(");
            s1.accept(this, String.class);
            out.write(" || ");
            s2.accept(this, String.class);
            out.write(")");
        } else if (function instanceof FilterFunction_strEndsWith) {
            Expression str = getParameter(function, 0, true);
            Expression end = getParameter(function, 1, true);

            out.write(" case when ");
            out.write("( right (");
            str.accept(this, String.class);
            out.write(",length( ");
            if (end instanceof Literal)
                out.write(" cast (");
            end.accept(this, String.class);
            if (end instanceof Literal)
                out.write(" as  VARCHAR(32672))");
            out.write("))=");
            end.accept(this, String.class);
            out.write(")");
            out.write("then 1 else 0 end ");

        } else if (function instanceof FilterFunction_strStartsWith) {
            Expression str = getParameter(function, 0, true);
            Expression start = getParameter(function, 1, true);

            out.write(" case when ");
            out.write("( left (");
            str.accept(this, String.class);
            out.write(",length( ");
            if (start instanceof Literal)
                out.write(" cast (");
            start.accept(this, String.class);
            if (start instanceof Literal)
                out.write(" as  VARCHAR(32672))");
            out.write("))=");
            start.accept(this, String.class);
            out.write(")");
            out.write("then 1 else 0 end ");

        } else if (function instanceof FilterFunction_strEqualsIgnoreCase) {
            Expression first = getParameter(function, 0, true);
            Expression second = getParameter(function, 1, true);
            out.write(" case when ");
            out.write("(lower(");
            first.accept(this, String.class);
            out.write(") = lower(");
            second.accept(this, String.class);
            out.write("))");
            out.write("then 1 else 0 end ");
        } else if (function instanceof FilterFunction_strIndexOf) {
            Expression first = getParameter(function, 0, true);
            Expression second = getParameter(function, 1, true);

            // would be a simple call, but strIndexOf returns zero based indices
            out.write("(locate(");
            second.accept(this, String.class);
            out.write(", ");
            first.accept(this, String.class);
            out.write(") - 1)");
        } else if (function instanceof FilterFunction_strSubstring) {
            Expression string = getParameter(function, 0, true);
            Expression start = getParameter(function, 1, true);
            Expression end = getParameter(function, 2, true);

            out.write("substr(");
            string.accept(this, String.class);
            out.write(", ");
            start.accept(this, Integer.class);
            out.write(" + 1, (");
            end.accept(this, Integer.class);
            out.write(" - ");
            start.accept(this, Integer.class);
            out.write("))");
        } else if (function instanceof FilterFunction_strSubstringStart) {
            Expression string = getParameter(function, 0, true);
            Expression start = getParameter(function, 1, true);

            out.write("substr(");
            string.accept(this, String.class);
            out.write(", ");
            start.accept(this, Integer.class);
            out.write(" + 1)");
        } else if (function instanceof FilterFunction_strTrim) {
            Expression string = getParameter(function, 0, true);

            out.write("trim(both ' ' from ");
            string.accept(this, String.class);
            out.write(")");
        } else if (function instanceof FilterFunction_strLength) {
            Expression string = getParameter(function, 0, true);
            out.write("length(");
            string.accept(this, String.class);
            out.write(")");
        } else if (function instanceof FilterFunction_strToLowerCase) {
            Expression string = getParameter(function, 0, true);
            out.write("lower(");
            string.accept(this, String.class);
            out.write(")");
        } else if (function instanceof FilterFunction_strToUpperCase) {
            Expression string = getParameter(function, 0, true);
            out.write("upper(");
            string.accept(this, String.class);
            out.write(")");
        } else if (function instanceof FilterFunction_abs
                || function instanceof FilterFunction_abs_2
                || function instanceof FilterFunction_abs_3
                || function instanceof FilterFunction_abs_4) {
            Expression string = getParameter(function, 0, true);
            out.write("CAST (");
            out.write("abs(");
            string.accept(this, String.class);
            out.write(")");
            out.write(" AS ");
            String db2Type = null;
            if (function instanceof FilterFunction_abs)
                db2Type = "SMALLINT";
            if (function instanceof FilterFunction_abs_2)
                db2Type = "INT";
            if (function instanceof FilterFunction_abs_3)
                db2Type = "FLOAT";
            if (function instanceof FilterFunction_abs_4)
                db2Type = "DOUBLE";
            out.write(db2Type);
            out.write(")");

        } else {
            // function not supported
            return false;
        }
        return true;
    }

    @Override
    public Object visit(Function function, Object extraData) throws RuntimeException {
        try {
            encodingFunction = false;
            // encodingFunction = true;
            boolean encoded = visitFunction(function, extraData);
            // encodingFunction = false;

            if (encoded) {
                return extraData;
            } else {
                return super.visit(function, extraData);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Object visit(BBOX filter, Object extraData) throws RuntimeException {
        if (isLooseBBOXEnabled()==false)
            return super.visit(filter,extraData);
        
                        
        
        double minx = filter.getMinX();
        double maxx = filter.getMaxX();
        double miny = filter.getMinY();
        double maxy = filter.getMaxY();
        String propertyName = filter.getPropertyName();
        Integer srid =getSRID(propertyName);
                
        try {
            out.write("db2gse.EnvelopesIntersect(");
            out.write(escapeName(propertyName));
            out.write(","+minx + ", " + miny + ", "
                    + maxx + ", " + maxy + ", " + srid);
            out.write(") =1 ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;        
    }

}
