/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005-2007. All rights reserved.
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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTWriter;


import org.geotools.filter.DefaultExpression;
import org.geotools.filter.FilterCapabilities;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;

import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;


/**
 * Generate a WHERE clause for DB2 Spatial Extender based on a spatial filter.
 * 
 * <p>
 * The following spatial filter operations are supported:
 * 
 * <ul>
 * <li>
 * GEOMETRY_BBOX
 * </li>
 * <li>
 * GEOMETRY_CONTAINS
 * </li>
 * <li>
 * GEOMETRY_CROSSES
 * </li>
 * <li>
 * GEOMETRY_DISJOINT
 * </li>
 * <li>
 * GEOMETRY_EQUALS
 * </li>
 * <li>
 * GEOMETRY_INTERSECTS
 * </li>
 * <li>
 * GEOMETRY_OVERLAPS
 * </li>
 * <li>
 * GEOMETRY_TOUCHES
 * </li>
 * <li>
 * GEOMETRY_WITHIN
 * </li>
 * <li>
 * GEOMETRY_DWITHIN
 * </li>
 * </ul>
 * </p>
 *
 * @author Mueller Christian
 *
 * @source $URL$
 */
public class DB2FilterToSQL extends PreparedFilterToSQL{
	    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.db2");

	    // Class to convert geometry value into a Well-known Text string	
	    private static WKTWriter wktWriter = new WKTWriter();

	    //The standard SQL multicharacter wild card.
	    private static char SQL_WILD_MULTI = '%';

	    //The standard SQL single character wild card.
	    private static char SQL_WILD_SINGLE = '_';

	    // The escaped version of the single wildcard for the REGEXP pattern.
	    private static String escapedWildcardSingle = "\\.\\?";

	    // The escaped version of the multiple wildcard for the REGEXP pattern.
	    private static String escapedWildcardMulti = "\\.\\*";
	    
	    static private HashMap<Class<?>,String> DB2_SPATIAL_PREDICATES = new HashMap<Class<?>,String>();
	    public DB2FilterToSQL(PreparedStatementSQLDialect dialect) {
			super(dialect);
			// TODO Auto-generated constructor stub
		}

		public DB2FilterToSQL(Writer out) {
			super(out);
			// TODO Auto-generated constructor stub
		}

		// Only intended for test purposes
	    public HashMap getPredicateMap() {
	    	return DB2_SPATIAL_PREDICATES;
	    }
	    
	    
	    // The SELECTIVITY clause to be used with spatial predicates.	
	    private String selectivityClause = null;
	    
	    private boolean looseBBOXEnabled=false;
	    
	     

	    {
	        DB2_SPATIAL_PREDICATES.put(BBOX.class,
	            "EnvelopesIntersect");
	        DB2_SPATIAL_PREDICATES.put(Contains.class,
	            "ST_Contains");
	        DB2_SPATIAL_PREDICATES.put(Crosses.class,
	            "ST_Crosses");
	        DB2_SPATIAL_PREDICATES.put(Disjoint.class,
	            "ST_Disjoint");
	        DB2_SPATIAL_PREDICATES.put(Equals.class,
	            "ST_Equals");
	        DB2_SPATIAL_PREDICATES.put(Intersects.class, "ST_Intersects");
	        DB2_SPATIAL_PREDICATES.put(Overlaps.class,
	            "ST_Overlaps");
	        DB2_SPATIAL_PREDICATES.put(Touches.class,
	            "ST_Touches");
	        DB2_SPATIAL_PREDICATES.put(Within.class,
	            "ST_Within");
	        DB2_SPATIAL_PREDICATES.put(DWithin.class,
	            "ST_Distance");
	        DB2_SPATIAL_PREDICATES.put(Beyond.class,
	            "ST_Distance");
	    }


	    /** HashMap<Class,String> example [BBOX.class,"EnvelopesIntersect"] */ 
	    static private HashMap getPredicateTable() {
	        return DB2_SPATIAL_PREDICATES;
	    }

	    /**
	     * Construct a geometry from the WKT representation of a geometry

	     *
	     * @param geom the constructor for the geometry.
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

	        FilterCapabilities capabilities = new FilterCapabilities();
	        
//	 New capbilities
	        capabilities.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
            for( Iterator i=getPredicateTable().keySet().iterator(); i.hasNext(); ){
                capabilities.addType( (Class) i.next() );    
            }
	        capabilities.addType(PropertyIsLike.class);
	        capabilities.addType(Id.class);
	        
//	 Old capabilities        
	        capabilities.addType(FilterCapabilities.LOGIC_OR);
	        capabilities.addType(FilterCapabilities.LOGIC_AND);
	        capabilities.addType(FilterCapabilities.LOGIC_NOT);
	        capabilities.addType(FilterCapabilities.COMPARE_EQUALS);
	        capabilities.addType(FilterCapabilities.COMPARE_NOT_EQUALS);
	        capabilities.addType(FilterCapabilities.COMPARE_LESS_THAN);
	        capabilities.addType(FilterCapabilities.COMPARE_GREATER_THAN);
	        capabilities.addType(FilterCapabilities.COMPARE_LESS_THAN_EQUAL);
	        capabilities.addType(FilterCapabilities.COMPARE_GREATER_THAN_EQUAL);
	        capabilities.addType(FilterCapabilities.LIKE);
	        capabilities.addType(FilterCapabilities.NULL_CHECK);
	        capabilities.addType(FilterCapabilities.BETWEEN);
	        capabilities.addType(FilterCapabilities.FID);
	        capabilities.addType(FilterCapabilities.NONE);
	        capabilities.addType(FilterCapabilities.ALL);
	        capabilities.addType(FilterCapabilities.SPATIAL_BBOX);
	        capabilities.addType(FilterCapabilities.SPATIAL_CONTAINS);
	        capabilities.addType(FilterCapabilities.SPATIAL_CROSSES);
	        capabilities.addType(FilterCapabilities.SPATIAL_DISJOINT);
	        capabilities.addType(FilterCapabilities.SPATIAL_EQUALS);
	        capabilities.addType(FilterCapabilities.SPATIAL_INTERSECT);
	        capabilities.addType(FilterCapabilities.SPATIAL_OVERLAPS);
	        capabilities.addType(FilterCapabilities.SPATIAL_TOUCHES);
	        capabilities.addType(FilterCapabilities.SPATIAL_WITHIN);
	        capabilities.addType(FilterCapabilities.SPATIAL_DWITHIN);
	        capabilities.addType(FilterCapabilities.SPATIAL_BEYOND);
	        

	        // Does this need to be immutable???
	        return capabilities;
	    }

	    /**
	     * Sets a SELECTIVITY clause that can be included with the spatial
	     * predicate to influence the query optimizer to exploit a spatial index
	     * if it exists.
	     * 
	     * <p>
	     * The parameter should be of the form: <br>
	     * "SELECTIVITY 0.001" <br>
	     * where the numeric value is the fraction of rows that will be returned
	     * by using the index scan.  This doesn't have to be true.  The value
	     * 0.001 is typically used to force the use of the spatial in all cases if
	     * the spatial index exists.
	     * </p>
	     *
	     * @param string a selectivity clause
	     */
	    public void setSelectivityClause(String string) {
	        this.selectivityClause = string;
	    }
	    

	    /**
	     * Encode BEYOND and DWITHIN filters using ST_Distance function.
	     *
	     * @param filter a BinarySpatialOperator (should be DWithin or Beyond subclass)
	     * @param distance the distance value
	     * @param distanceUnits the units for the distance operation or blank/null if not used
	     * @param op the distance operator, either &lt. or &gt.
	     * @param filter the GeometryDistanceFilter
	     *
	     * @throws RuntimeException
	     */
	    private void encodeDistance(BinarySpatialOperator filter, double distance, String distanceUnits, String op)
	        throws RuntimeException {
	    	DefaultExpression left = (DefaultExpression) filter.getExpression1();
	    	DefaultExpression right = (DefaultExpression) filter.getExpression2();
	        try {
	            int leftType = left.getType();
	            int rightType = right.getType();

	            // The test below should use ATTRIBUTE_GEOMETRY but only the value ATTRIBUTE
	            if ((DefaultExpression.ATTRIBUTE == leftType)
	                    && (DefaultExpression.LITERAL_GEOMETRY == rightType)) {
	                this.out.write("db2gse.ST_Distance(");
	                left.accept(this,null);
	                this.out.write(", ");
	                right.accept(this,Geometry.class);
	                if (!(distanceUnits == null || distanceUnits.length() == 0)) {  // if units were specified
	                	this.out.write(", \"" + distanceUnits + "\"");
	                }
	                this.out.write(") " + op + " " + distance);
	                addSelectivity();  // add selectivity clause if needed
	            } else {
	                String msg = "Unsupported left and right types: " + leftType
	                    + ":" + rightType;
	                LOGGER.warning(msg);
	                throw new RuntimeException(msg);
	            }
	        } catch (java.io.IOException e) {
	            LOGGER.warning("Filter not generated; I/O problem of some sort" + e);
	        }
	    }

	    public Object visit(DWithin filter, Object extraData) {
	    	
	    	double distance = filter.getDistance();
	    	String distanceUnit = filter.getDistanceUnits();
	    	encodeDistance(filter, distance, distanceUnit, "<");
	    	return extraData;
	    }
	    public Object visit(Beyond filter, Object extraData) {
	    	
	    	double distance = filter.getDistance();
	    	String distanceUnit = filter.getDistanceUnits();
	    	encodeDistance(filter, distance, distanceUnit, ">");
	    	return extraData;
	    }
	    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter, Object extraData) {
	        throw new RuntimeException(
            "SQLEncoderDB2 must implement this method in order to handle geometries");
//	    	return extraData;
	    }
	    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter, Object extraData, String db2Predicate) {
	        LOGGER.finer("Generating GeometryFilter WHERE clause for " + filter);

	        currentSRID=getSRID();
	        
	        DefaultExpression left = (DefaultExpression) filter.getExpression1();
	        DefaultExpression right = (DefaultExpression) filter.getExpression2();

	        // neither left nor right expression can be null
	        if ((null == left) || (null == right)) {
	            String msg = "Left or right expression is null - " + filter;
	            LOGGER.warning(msg);
	            throw new RuntimeException(msg);
	        }
	        try {
	            this.out.write("db2gse." + db2Predicate + "(");
	            left.accept(this, extraData);
	            this.out.write(", ");
	            right.accept(this, extraData);
	            this.out.write(") = 1");
	
	            addSelectivity();  // add selectivity clause if needed
		    } catch (IOException e) {
		        throw new RuntimeException(e);
		    }
	
		    LOGGER.fine(this.out.toString());
		    return extraData;
	    }	
	    
	    /**
	     * Encode a bounding-box filter using the EnvelopesIntersect spatial
	     * predicate.
	     *
	     * @param filter a BBOX filter object
	     * @param extraData not used
	     */
	    private Object encodeBBox(BBOX filter, Object extraData) {
	        LOGGER.finer("Generating EnvelopesIntersect WHERE clause for " + filter);

	        try {
	            String spatialColumn = filter.getPropertyName();
	            if (spatialColumn == null || spatialColumn.trim().length()==0) {
	            	spatialColumn = featureType.getGeometryDescriptor().getLocalName();
	            }
	            
                    Integer srid = getSRID(spatialColumn);
                    if (srid==null) {
                        throw new RuntimeException("Attribute: "+spatialColumn+" is not registered");
                    }

	            double minx = filter.getMinX();
	            double maxx = filter.getMaxX();
	            double miny = filter.getMinY();
	            double maxy = filter.getMaxY();

	            if (isLooseBBOXEnabled()) {
	            	this.out.write("db2gse.EnvelopesIntersect(");
	            	this.out.write(escapeName(spatialColumn));
	            	this.out.write(", ");
	            	this.out.write(minx + ", " + miny + ", "
	                    + maxx + ", " + maxy + ", " + srid);
	            	this.out.write(") = 1");
	            } else {
	            	this.out.write("db2gse.st_intersects(");
	            	this.out.write(escapeName(spatialColumn));
	            	this.out.write(", ");
	            	GeometryFactory fac = new GeometryFactory();
	            	
	            	LinearRing linearRing = fac.createLinearRing(new Coordinate[] {
	            		new Coordinate(minx,miny),
	            		new Coordinate(maxx,miny),
	            		new Coordinate(maxx,maxy),
	            		new Coordinate(minx,maxy),
	            		new Coordinate(minx,miny),
	            	});	            	
	            	Polygon poly = fac.createPolygon(linearRing, null);
	            	this.out.write("db2gse.st_polyfromtext('");
	            	WKTWriter writer = new WKTWriter();	            	
	            	this.out.write(writer.write(poly));
	            	this.out.write("',");
	            	this.out.write(srid.toString());
	            	this.out.write(")) = 1");
	            }
                addSelectivity();  // add selectivity clause if needed
	        } catch (java.io.IOException e) {
	            LOGGER.warning("Filter not generated; I/O problem of some sort" + e);
	        }
	        return extraData;
	    }	    
	    public Object visit(BBOX filter, Object extraData) {
	        return encodeBBox((BBOX)filter, extraData);
	    }

	    public Object visit(Contains filter, Object extraData) {
	        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData, "ST_Contains");
	    }
	    public Object visit(Crosses filter, Object extraData) {
	        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData, "ST_Crosses");
	    }
	    public Object visit(Disjoint filter, Object extraData) {
	        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData, "ST_Disjoint");
	    }

	    public Object visit(Equals filter, Object extraData) {
	        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData, "ST_Equals");
	    }
	    public Object visit(Intersects filter, Object extraData) {
	        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData, "ST_Intersects");
	    }
	    public Object visit(Overlaps filter, Object extraData) {
	        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData, "ST_Overlaps");
	    }
	    public Object visit(Touches filter, Object extraData) {
	        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData, "ST_Touches");
	    }
	    public Object visit(Within filter, Object extraData) {
	        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData, "ST_Within");
	    }
	    /**
	     * Construct the appropriate geometry type from the WKT representation of a literal
	     * expression
	     *
	     * @param expression the expression turn into a geometry constructor.
	     *
	     * @throws IOException Passes back exception if generated by
	     *         this.out.write()
	     */
	    public void visitLiteralGeometry(Literal expression)
	        throws IOException {
	        String wktRepresentation = wktWriter.write((Geometry) expression.getValue());
	        int spacePos = wktRepresentation.indexOf(" ");
	        String geomType = wktRepresentation.substring(0,spacePos);
	        this.out.write("db2gse.ST_" + geomType + "('" + wktRepresentation + "', "
	            + getSRID() + ")");
	    }
	    protected void addSelectivity() throws IOException {
            if (this.selectivityClause != null) {
                this.out.write(" " + this.selectivityClause);
            }
	    }

		
		/* (non-Javadoc)
		 * @see org.geotools.data.jdbc.FilterToSQL#visit(org.opengis.filter.ExcludeFilter, java.lang.Object)
		 */
		public Object visit(ExcludeFilter filter, Object extraData) {
			try {
				out.write("1=0");
			} catch (java.io.IOException ioe) {};
			return extraData;
		}

		
		/* (non-Javadoc)
		 * @see org.geotools.data.jdbc.FilterToSQL#visit(org.opengis.filter.IncludeFilter, java.lang.Object)
		 */
		public Object visit(IncludeFilter filter, Object extraData) {
			try {
				out.write("1=1");
			} catch (java.io.IOException ioe) {};
			return extraData;
		}
		
		
		private Integer getSRID() {
			return getSRID(featureType.getGeometryDescriptor());
		}
		
		private Integer getSRID(String attrName) {
			AttributeDescriptor attrDescr = featureType.getDescriptor(attrName);
			if (attrDescr instanceof GeometryDescriptor) {
				return getSRID((GeometryDescriptor)attrDescr);
			}
			return currentSRID;
		}
		
		private Integer getSRID(GeometryDescriptor gDescr) {
			Integer result = null;
			if (gDescr != null)
				result = (Integer) gDescr.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID);
			
		    if (result==null)
		    	result = currentSRID;
		   return result; 
		}


    public boolean isLooseBBOXEnabled() {
        return looseBBOXEnabled;
    }

    public void setLooseBBOXEnabled(boolean looseBBOXEnabled) {
        this.looseBBOXEnabled = looseBBOXEnabled;
    }

		
}
