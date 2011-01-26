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

import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.Subtract;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.io.WKTWriter;


/**
 * Encodes a filter into a SQL WHERE statement for postgis.  This class adds
 * the ability to turn geometry filters into sql statements if they are
 * bboxes.
 *
 * @author Chris Holmes, TOPP
 *
 * @task TODO: integrated with SQLEncoderPostgisGeos.java, as there no  real
 *       reason to have two different classes.  We just need to do testing to
 *       make sure both handle everything.  At the very least have the geos
 *       one extend more intelligently.
 * @source $URL$
 */
public class SQLEncoderPostgis extends SQLEncoder implements
        org.geotools.filter.FilterVisitor {
    /** Standard java logger */
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.filter");

    /** To write geometry so postgis can read it. */
    private static WKTWriter wkt = new WKTWriter();

    /**
     * The srid of the schema, so the bbox conforms.  Could be better to have
     * it in the bbox filter itself, but this works for now.
     */
    private int srid;

    /** The geometry attribute to use if none is specified. */
    private String defaultGeom;
    
    /** Whether the BBOX filter should be strict (using the exact geom), or 
     *  loose (using the envelopes) */
    protected boolean looseBbox = false;

    /**
     * Whether the installed PostGIS has GEOS support. Default is false for
     * backwards compatibility.
     */
    protected boolean supportsGEOS = false;
   
    /**
     * Empty constructor TODO: rethink empty constructor, as BBOXes _need_ an
     * SRID, must make client set it somehow.  Maybe detect when encode is
     * called?
     *
     */
    public SQLEncoderPostgis() {
        capabilities = createFilterCapabilities();
        setSqlNameEscape("\"");
    }

    public SQLEncoderPostgis(boolean looseBbox) {
        this();
        this.looseBbox = looseBbox;
    }
    

    /**
     * 
     * @see org.geotools.filter.SQLEncoder#createFilterCapabilities()
     */
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities capabilities = new FilterCapabilities();

        capabilities.addType(FilterCapabilities.NONE);
        capabilities.addType(IncludeFilter.class);
        capabilities.addType(FilterCapabilities.ALL);
        capabilities.addType(ExcludeFilter.class);
        capabilities.addType(FilterCapabilities.FID);
        capabilities.addType(Id.class);
        capabilities.addType(FilterCapabilities.NULL_CHECK);
        capabilities.addType(PropertyIsNull.class);
        capabilities.addType(FilterCapabilities.BETWEEN);
        capabilities.addType(PropertyIsBetween.class);
        capabilities.addType(FilterCapabilities.LOGICAL);
        capabilities.addAll(FilterCapabilities.LOGICAL_OPENGIS);
        capabilities.addType(FilterCapabilities.SIMPLE_ARITHMETIC);
        capabilities.addType(Add.class);
        capabilities.addType(Multiply.class);
        capabilities.addType(Subtract.class);
        capabilities.addType(Divide.class);
        capabilities.addType(FilterCapabilities.SIMPLE_COMPARISONS);
        capabilities.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
        capabilities.addType(FilterCapabilities.SPATIAL_BBOX);
        capabilities.addType(BBOX.class);
        capabilities.addType(FilterCapabilities.LIKE);
        capabilities.addType(PropertyIsLike.class);

        if (supportsGEOS) {
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
        }
        
        // TODO: add SPATIAL_BEYOND, DWITHIN to capabilities and support in
        // visit(GeometryFilter)
        
        return capabilities;
    }
    
    

    /**
     * Constructor with srid.
     *
     * @param srid spatial reference id to encode geometries with.
     */
    public SQLEncoderPostgis(int srid) {
        this(true);
        this.srid = srid;
    }

    /**
     * Sets whether the Filter.BBOX query should be 'loose', meaning that it
     * should just doing a bounding box against the envelope.  If set to
     * <tt>false</tt> then the BBOX query will perform a full intersects 
     * against the geometry, ensuring that it is exactly correct.  If 
     * <tt>true</tt> then the query will likely perform faster, but may not
     * be exactly correct.
     *
     * @param isLooseBbox whether the bbox should be loose or strict.
     */
    public void setLooseBbox(boolean isLooseBbox) {
	this.looseBbox = isLooseBbox;
    }
   
    /**
     * Gets whether the Filter.BBOX query will be strict and use an intersects
     * or 'loose' and just operate against the geometry envelopes.
     *
     * @return <tt>true</tt> if this encoder is going to do loose filtering.
     */
    public boolean isLooseBbox() {
        return looseBbox;
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

    public void setSupportsGEOS(boolean supports) {
        boolean oldValue = this.supportsGEOS;
        this.supportsGEOS = supports;
        if (capabilities == null || supports != oldValue) {
            //regenerate capabilities
            capabilities = createFilterCapabilities();
        }
    }
    
    public boolean getSupportsGEOS() {
        return supportsGEOS;
    }
    
    private void encodeGeomFilter(GeometryFilter filter, String function, String comparison, boolean useIndex) {
        //this method blindly assumes that the filter is supported
        DefaultExpression left = (DefaultExpression) filter.getLeftGeometry();
        DefaultExpression right = (DefaultExpression) filter.getRightGeometry();

        try {
            //should we use the index?
            if (useIndex) {
                encodeExpression(left);
                out.write(" && ");
                encodeExpression(right);
            }
            
            // looseBbox only applies to GEOMETRY_BBOX, so unless this is a
            // BBOX, we will always generate the full SQL.
            if (filter.getFilterType() != AbstractFilter.GEOMETRY_BBOX || !looseBbox) {
                if (useIndex) {
                    out.write(" AND ");
                }
            	out.write(function + "(");
                encodeExpression(left);
                out.write(", ");
                encodeExpression(right);
                out.write(")" + comparison);
            }
        } catch (java.io.IOException ioe) {
            LOGGER.warning("Unable to export filter" + ioe);
        }
    }

    private void encodeExpression(DefaultExpression expr) throws IOException {
        if (expr == null) {
            out.write("\"" + defaultGeom + "\"");
        } else {
            expr.accept(this);
        }
    }
    
    /**
     * Turns a geometry filter into the postgis sql bbox statement.
     *
     * @param filter the geometry filter to be encoded.
     *
     * @throws RuntimeException for IO exception (need a better error)
     */
    public void visit(GeometryFilter filter) throws RuntimeException {
        LOGGER.finer("exporting GeometryFilter");

        short filterType = filter.getFilterType();
        DefaultExpression left = (DefaultExpression) filter.getLeftGeometry();
        DefaultExpression right = (DefaultExpression) filter.getRightGeometry();

        //if geos is not supported, all we can use is distance = 0 for bbox
        if (!supportsGEOS) {
            if (filterType != AbstractFilter.GEOMETRY_BBOX) {
                throw new RuntimeException(
                        "without GEOS support, only the BBOX function is supported; failed to encode "
                                + filterType);
            }
            encodeGeomFilter(filter, "distance", " < 0.00001", true);
            return;
        }
        
        // Figure out if we need to constrain this query with the && constraint.
        int literalGeometryCount = 0;

        if ((left != null)
                && (left.getType() == DefaultExpression.LITERAL_GEOMETRY)) {
            literalGeometryCount++;
        }

        if ((right != null)
                && (right.getType() == DefaultExpression.LITERAL_GEOMETRY)) {
            literalGeometryCount++;
        }

        boolean constrainBBOX = (literalGeometryCount == 1);
        boolean onlyBbox = filterType == AbstractFilter.GEOMETRY_BBOX
                && looseBbox;

        try {

            // DJB: disjoint is not correctly handled in the pre-march 22/05
            // version
            // I changed it to not do a "&&" index search for disjoint because
            // Geom1 and Geom2 can have a bbox overlap and be disjoint
            // I also added test case.
            // NOTE: this will not use the index, but its unlikely that using
            // the index
            // for a disjoint query will be the correct thing to do.

            // DJB NOTE: need to check for a NOT(A intersects G) filter
            // --> NOT( (A && G) AND intersects(A,G))
            // and check that it does the right thing.

            constrainBBOX = constrainBBOX
                    && (filterType != AbstractFilter.GEOMETRY_DISJOINT);

            if (constrainBBOX) {
                encodeExpression(left);
                out.write(" && ");
                encodeExpression(right);

                if (!onlyBbox) {
                    out.write(" AND ");
                }
            }

            String closingParenthesis = ")";

            if (!onlyBbox) {
                if (filterType == AbstractFilter.GEOMETRY_EQUALS) {
                    out.write("equals");
                } else if (filterType == AbstractFilter.GEOMETRY_DISJOINT) {
                    out.write("NOT (intersects");
                    closingParenthesis += ")";
                } else if (filterType == AbstractFilter.GEOMETRY_INTERSECTS) {
                    out.write("intersects");
                } else if (filterType == AbstractFilter.GEOMETRY_CROSSES) {
                    out.write("crosses");
                } else if (filterType == AbstractFilter.GEOMETRY_WITHIN) {
                    out.write("within");
                } else if (filterType == AbstractFilter.GEOMETRY_CONTAINS) {
                    out.write("contains");
                } else if (filterType == AbstractFilter.GEOMETRY_OVERLAPS) {
                    out.write("overlaps");
                } else if (filterType == AbstractFilter.GEOMETRY_BBOX) {
                    out.write("intersects");
                } else if (filterType == AbstractFilter.GEOMETRY_TOUCHES) {
                    out.write("touches");
                } else {
                    // this will choke on beyond and dwithin
                    throw new RuntimeException("does not support filter type "
                            + filterType);
                }
                out.write("(");

                encodeExpression(left);
                out.write(", ");
                encodeExpression(right);

                out.write(closingParenthesis);
            }
        } catch (java.io.IOException ioe) {
            LOGGER.warning("Unable to export filter" + ioe);
            throw new RuntimeException("io error while writing", ioe);
        }
    }

    
    /**
     * Checks to see if the literal is a geometry, and encodes it if it is, if
     * not just sends to the parent class.
     * 
     * @param expression
     *            the expression to visit and encode.
     * 
     * @throws IOException
     *             for IO exception (need a better error)
     */
    public void visitLiteralGeometry(LiteralExpression expression)
        throws IOException {
        Geometry bbox = (Geometry) expression.evaluate( null, Geometry.class );
        String geomText = null;
        if ( bbox instanceof LinearRing ) {
        	//postgis does not handle linear rings, convert to just a line string
        	LineString lineString = new LineString( 
    			((LinearRing)bbox).getCoordinateSequence(), bbox.getFactory()
			);
        	geomText = wkt.write( lineString );
        }
        else {
        	geomText = wkt.write(bbox);	
        }
        
        out.write("GeometryFromText('" + geomText + "', " + srid + ")");
    }

    public void visit(LikeFilter filter) throws UnsupportedOperationException {
    	char esc = filter.getEscape().charAt(0);
    	char multi = filter.getWildcardMulti().charAt(0);
    	char single = filter.getWildcardSingle().charAt(0);
        boolean matchCase = filter.isMatchingCase();
    	String pattern = LikeFilterImpl.convertToSQL92(esc, multi, single, matchCase, 
	        filter.getPattern());
    	
    	DefaultExpression att = (DefaultExpression) filter.getValue();
    	
    	try {
    		out.write( " ( " );

                if (!matchCase) {
                    out.write("UPPER( ");
                }

	    	att.accept(this);

                if (!matchCase) {
                    out.write(" ) LIKE '"); 
                } else { 
                    out.write(" LIKE '");
                }

	    	out.write(pattern);
	    	out.write("' ");	
	    	
	    	//JD: this is an ugly ugly hack!! hopefully when the new feature model is around we can 
	    	// fix this
	    	//check for context for a date
	    	if ( att instanceof AttributeExpression && context != null 
	    			&& java.util.Date.class.isAssignableFrom( context ) ) {
	    		//if it is a date, add additional logic for a timestamp, or a timestamp with 
	    		// timezone
	    		out.write( " OR " );
                        if (!matchCase) {
                            out.write("UPPER( ");
                        }
                        
                        att.accept( this );

                        if (!matchCase) {
                            out.write(" ) LIKE '"); 
                        } else { 
                            out.write(" LIKE '");
                        }

	    		out.write(pattern + " __:__:__'" );	//timestamp
	    		
	    		out.write( " OR " );
                        if (!matchCase) {
                            out.write("UPPER( ");
                        }

                        att.accept( this );

                        if (!matchCase) {
                            out.write(" ) LIKE '"); 
                        } else { 
                            out.write(" LIKE '");
                        }

	    		out.write(pattern + " __:__:_____'" );	//timestamp with time zone
		    }
	    	
	    	out.write( " ) " );
	    	
    	} catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }    	
    	
    	
    }
    
    /**
     * Checks to see if the literal is a geometry, and encodes it if it  is, if
     * not just sends to the parent class.
     *
     * @param expression the expression to visit and encode.
     *
     * @throws RuntimeException for IO exception (need a better error)
     */
    public void visit(LiteralExpression expression) throws RuntimeException {
        LOGGER.finer("exporting LiteralExpression");

        try {
            if (expression.getType() == DefaultExpression.LITERAL_GEOMETRY) {
                visitLiteralGeometry(expression);
            } else {
                super.visit(expression);
            }
        } catch (java.io.IOException ioe) {
            LOGGER.warning("Unable to export expression" + ioe);
            throw new RuntimeException("io error while writing", ioe);
        }
    }
    
    /**
     * Writes the SQL for a Compare Filter.
     *
     * DJB: note, postgis overwrites this implementation because of the way
     *       null is handled.  This is for <PropertyIsNull> filters and <PropertyIsEqual> filters
     *       are handled.  They will come here with "property = null".  
     *       NOTE:
     *        SELECT * FROM <table> WHERE <column> isnull;  -- postgresql
     *        SELECT * FROM <table> WHERE isnull(<column>); -- oracle???
     * 
     * @param filter the comparison to be turned into SQL.
     *
     * @throws RuntimeException for io exception with writer
     */
    public void visit(CompareFilter filter) throws RuntimeException {
        LOGGER.finer("exporting SQL ComparisonFilter");

        DefaultExpression left = (DefaultExpression) filter.getLeftValue();
        DefaultExpression right = (DefaultExpression) filter.getRightValue();
        LOGGER.finer("Filter type id is " + filter.getFilterType());
        LOGGER.finer("Filter type text is "
            + comparisions.get(new Integer(filter.getFilterType())));

        String type = (String) comparisions.get(new Integer(
                    filter.getFilterType()));

        try {
        	// a bit hacky, but what else can we really do?
        	if ( (right == null) && (filter.getFilterType()==FilterType.COMPARE_EQUALS ) )
        	{
        		left.accept(this);
        		out.write(" isnull");
        	}
        	else
        	{
        		//check for case insentivity (TODO: perhaps move this up to jdbc)
        		if ( !filter.isMatchingCase() ) {
        			//only for == or != 
        			if ( filter.getFilterType() == Filter.COMPARE_EQUALS || 
        					filter.getFilterType() == Filter.COMPARE_NOT_EQUALS ) {
        				
        				//only for strings
            			if ( left.getType() == Expression.LITERAL_STRING  
            					|| right.getType() == Expression.LITERAL_STRING ) {
            				
            				out.write( "lower(" ); left.accept( this ); out.write( ")");
            				out.write( " " + type + " " );
            				out.write( "lower(" ); right.accept( this ); out.write( ")");
            			
            				return;
            			}
        			}
        		}
        		
    			//normal execution
    			left.accept(this);
        		out.write(" " + type + " ");
        		right.accept(this);
    		}
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
    }
    
}
