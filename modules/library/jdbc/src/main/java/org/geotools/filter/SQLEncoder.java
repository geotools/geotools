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
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.util.Converters;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.IncludeFilter;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Encodes a filter into a SQL WHERE statement.  It should hopefully be generic
 * enough that any SQL database will work with it, though it has only been
 * tested with MySQL and Postgis.  This generic SQL encoder should eventually
 * be able to encode all filters except Geometry Filters (currently
 * LikeFilters are not yet fully implemented, but when they are they should be
 * generic enough). This is because the OGC's SFS for SQL document specifies
 * two ways of doing SQL databases, one with native geometry types and one
 * without.  To implement an encoder for one of the two types simply subclass
 * off of this encoder and put in the proper GeometryFilter visit method. Then
 * add the filter types supported to the capabilities in the static
 * capabilities.addType block.
 *
 * @author Chris Holmes, TOPP
 * 
 * @deprecated Please use org.geotools.data.jdbc.FilterToSQL which uses
 * opengis filters instead of these geotools filters.
 *
 * @task TODO: Implement LikeFilter encoding, need to figure out escape chars,
 *       the rest of the code should work right.  Once fixed be sure to add
 *       the LIKE type to capabilities, so others know that they can be
 *       encoded.
 * @task REVISIT: need to figure out exceptions, we're currently eating io
 *       errors, which is bad. Probably need a generic visitor exception.
 * @source $URL$
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class SQLEncoder implements org.geotools.filter.FilterVisitor2 {
    /** error message for exceptions */
    protected static final String IO_ERROR = "io problem writing filter";

    /** The filter types that this class can encode */
    protected FilterCapabilities capabilities = null;

    /** Standard java logger */
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.filter");

    /** Map of comparison types to sql representation */
    protected static Map comparisions = new HashMap();

    /** Map of spatial types to sql representation */
    private static Map spatial = new HashMap();

    /** Map of logical types to sql representation */
    private static Map logical = new HashMap();

    /** Map of expression types to sql representation */
    private static Map expressions = new HashMap();

    static {
        comparisions.put(new Integer(AbstractFilter.COMPARE_EQUALS), "=");
        comparisions.put(new Integer(AbstractFilter.COMPARE_NOT_EQUALS), "!=");
        comparisions.put(new Integer(AbstractFilter.COMPARE_GREATER_THAN), ">");
        comparisions.put(new Integer(AbstractFilter.COMPARE_GREATER_THAN_EQUAL),
            ">=");
        comparisions.put(new Integer(AbstractFilter.COMPARE_LESS_THAN), "<");
        comparisions.put(new Integer(AbstractFilter.COMPARE_LESS_THAN_EQUAL),
            "<=");
        comparisions.put(new Integer(AbstractFilter.LIKE), "LIKE");
        comparisions.put(new Integer(AbstractFilter.NULL), "IS NULL");
        comparisions.put(new Integer(AbstractFilter.BETWEEN), "BETWEEN");

        expressions.put(new Integer(DefaultExpression.MATH_ADD), "+");
        expressions.put(new Integer(DefaultExpression.MATH_DIVIDE), "/");
        expressions.put(new Integer(DefaultExpression.MATH_MULTIPLY), "*");
        expressions.put(new Integer(DefaultExpression.MATH_SUBTRACT), "-");

        //more to come?
        spatial.put(new Integer(AbstractFilter.GEOMETRY_EQUALS), "Equals");
        spatial.put(new Integer(AbstractFilter.GEOMETRY_DISJOINT), "Disjoint");
        spatial.put(new Integer(AbstractFilter.GEOMETRY_INTERSECTS),
            "Intersects");
        spatial.put(new Integer(AbstractFilter.GEOMETRY_TOUCHES), "Touches");
        spatial.put(new Integer(AbstractFilter.GEOMETRY_CROSSES), "Crosses");
        spatial.put(new Integer(AbstractFilter.GEOMETRY_WITHIN), "Within");
        spatial.put(new Integer(AbstractFilter.GEOMETRY_CONTAINS), "Contains");
        spatial.put(new Integer(AbstractFilter.GEOMETRY_OVERLAPS), "Overlaps");
        spatial.put(new Integer(AbstractFilter.GEOMETRY_BEYOND), "Beyond");
        spatial.put(new Integer(AbstractFilter.GEOMETRY_BBOX), "BBOX");

        logical.put(new Integer(AbstractFilter.LOGIC_AND), "AND");
        logical.put(new Integer(AbstractFilter.LOGIC_OR), "OR");
        logical.put(new Integer(AbstractFilter.LOGIC_NOT), "NOT");
    }

    //use these when Like is implemented.
    //The standard SQL multicharacter wild card. 
    //private static String SQL_WILD_MULTI = "%";
    //The standard SQL single character wild card.
    //private static String SQL_WILD_SINGLE = "_";
    // The escaped version of the single wildcard for the REGEXP pattern. 
    //private static String escapedWildcardSingle = "\\.\\?";
    // The escaped version of the multiple wildcard for the REGEXP pattern. 
    //private static String escapedWildcardMulti = "\\.\\*";

    /** Character used to escape database schema, table and column names */
    private String sqlNameEscape = "";

    /** where to write the constructed string from visiting the filters. */
    protected Writer out;

    /** the fid mapper used to encode the fid filters */
    protected FIDMapper mapper;

    /** the schmema the encoder will be used to be encode sql for */
    protected SimpleFeatureType featureType;
    
    /** 
     * A type to use as context when encoding literal.
     * NOTE: when we move to geoapi filter visitor api, this will not be needed.
     */ 
    protected Class context = null;
    
    /**
     * Empty constructor
     */
    public SQLEncoder() {
    }
    
    /**
     * Sets the featuretype the encoder is encoding sql for.
     * <p>
     * This is used for context for attribute expressions when encoding to sql. 
     * </p>
     * 
     * @param featureType
     */
    public void setFeatureType(SimpleFeatureType featureType) {
		this.featureType = featureType;
	}
  
    /**
     * Convenience constructor to perform the whole encoding process at once.
     *
     * @param out the writer to encode the SQL to.
     * @param filter the Filter to be encoded.
     *
     * @throws SQLEncoderException If there were problems encoding
     */
    public SQLEncoder(Writer out, Filter filter) throws SQLEncoderException {
        if (getCapabilities().fullySupports(filter)) {
            this.out = out;

            try {
                out.write("WHERE ");
                filter.accept(this);

                //out.write(";"); this should probably be added by client.
            } catch (java.io.IOException ioe) {
                throw new SQLEncoderException("Problem writing filter: ", ioe);
            }
        } else {
            throw new SQLEncoderException("Filter type not supported");
        }
    }

    /**
     * Sets the FIDMapper that will be used in subsequente visit calls. There
     * must be a FIDMapper in order to invoke the FIDFilter encoder.
     *
     * @param mapper
     */
    public void setFIDMapper(FIDMapper mapper) {
        this.mapper = mapper;
    }
    
    public FIDMapper getFIDMapper(){
        return this.mapper;
    }

    /**
     * Sets the capabilities of this filter.
     *
     * @return FilterCapabilities for this Filter
     */
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities capabilities = new FilterCapabilities();

        capabilities.addType(FilterCapabilities.LOGICAL);
        capabilities.addType(FilterCapabilities.SIMPLE_COMPARISONS);
        capabilities.addType(FilterCapabilities.NULL_CHECK);
        capabilities.addType(FilterCapabilities.BETWEEN);
        capabilities.addType(FilterCapabilities.FID);
        capabilities.addType(FilterCapabilities.NONE);
        capabilities.addType(FilterCapabilities.ALL);

        return capabilities;
    }

    /**
     * Performs the encoding, sends the encoded sql to the writer passed in.
     *
     * @param out the writer to encode the SQL to.
     * @param filter the Filter to be encoded.
     *
     * @throws SQLEncoderException If filter type not supported, or if there
     *         were io problems.
     */
    public void encode(Writer out, org.opengis.filter.Filter filter) throws SQLEncoderException {
        if (getCapabilities().fullySupports(filter)) {
            this.out = out;

            try {
                out.write("WHERE ");
                Filters.accept( filter, this );
            } catch (java.io.IOException ioe) {
                LOGGER.warning("Unable to export filter" + ioe);
                throw new SQLEncoderException("Problem writing filter: ", ioe);
            }
        } else {
            throw new SQLEncoderException("Filter type not supported");
        }
    }

    /**
     * Performs the encoding, returns a string of the encoded SQL.
     *
     * @param filter the Filter to be encoded.
     *
     * @return the string of the SQL where statement.
     *
     * @throws SQLEncoderException If filter type not supported, or if there
     *         were io problems.
     */
    public String encode(org.opengis.filter.Filter filter) throws SQLEncoderException {
        StringWriter output = new StringWriter();
        encode(output, filter);

        return output.getBuffer().toString();
    }
    
    public void encode(Writer out, org.opengis.filter.expression.Expression expression) throws SQLEncoderException {
        this.out = out;
        ((DefaultExpression) expression).accept(this);
    }
    
    /**
     * Performs the encoding, returns a string of the encoded SQL.
     *
     * @param expression the expression to be encoded.
     *
     * @return the correspondent SQL snippet
     *
     * @throws SQLEncoderException If expression type not supported, or if there
     *         were io problems.
     */
    public String encode(org.opengis.filter.expression.Expression expression) throws SQLEncoderException {
        StringWriter output = new StringWriter();
        encode(output, expression);

        return output.getBuffer().toString();
    }

    /**
     * Describes the capabilities of this encoder.
     * 
     * <p>
     * Performs lazy creation of capabilities.
     * </p>
     *
     * @return The capabilities supported by this encoder.
     */
    public synchronized FilterCapabilities getCapabilities() {
        if (capabilities == null) {
            capabilities = createFilterCapabilities();
        }

        return capabilities; //maybe clone?  Make immutable somehow
    }

    /**
     * This should never be called. This can only happen if a subclass of
     * AbstractFilter failes to implement its own version of
     * accept(FilterVisitor);
     *
     * @param filter The filter to visit
     *
     * @throws RuntimeException for IO Encoding problems.
     *
     * @task REVISIT: I don't think Filter.INCLUDE and Filter.EXCLUDE should be
     *       handled here.  They should have their own methods, but they don't
     *       have interfaces, so I don't know if that's possible.
     */
    public void visit(Filter filter) {
        try {
            if (filter.getFilterType() == FilterType.NONE) {
                out.write("TRUE");
            } else if (filter.getFilterType() == FilterType.ALL) {
                out.write("FALSE");
            } else {
                LOGGER.warning("exporting unknown filter type:"
                    + filter.toString());

                //throw new RuntimeException("Do not know how to export filter:"+filter.toString() );
            }
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
    }

    /**
     * Writes the SQL for the Between Filter.
     *
     * @param filter the  Filter to be visited.
     *
     * @throws RuntimeException for io exception with writer
     */
    public void visit(BetweenFilter filter) throws RuntimeException {
        LOGGER.finer("exporting BetweenFilter");

        DefaultExpression left = (DefaultExpression) filter.getLeftValue();
        DefaultExpression right = (DefaultExpression) filter.getRightValue();
        DefaultExpression mid = (DefaultExpression) filter.getMiddleValue();
        LOGGER.finer("Filter type id is " + filter.getFilterType());
        LOGGER.finer("Filter type text is "
            + comparisions.get(new Integer(filter.getFilterType())));

        try {
            mid.accept(this);
            out.write(" BETWEEN ");
            left.accept(this);
            out.write(" AND ");
            right.accept(this);
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
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
     * @throws UnsupportedOperationException always, as likes aren't
     *         implemented yet.
     *
     * @task REVISIT: Need to think through the escape char, so it works  right
     *       when Java uses one, and escapes correctly with an '_'.
     */
    public void visit(LikeFilter filter) throws UnsupportedOperationException 
	{
    	char esc = filter.getEscape().charAt(0);
    	char multi = filter.getWildcardMulti().charAt(0);
    	char single = filter.getWildcardSingle().charAt(0);
        boolean matchCase = ((LikeFilterImpl)filter).isMatchingCase();
    	String pattern = LikeFilterImpl.convertToSQL92(esc, multi, single, matchCase, 
    	        filter.getPattern());
    	
    	DefaultExpression att = (DefaultExpression) filter.getValue();
    	 
    	try {
            if (!matchCase){
                out.write(" UPPER(");
            }

	    	att.accept(this);
            if (!matchCase){
                out.write(") LIKE '");
            } else {
                out.write(" LIKE '");
            }

	    	out.write(pattern);
	    	out.write("' ");
    	} catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }    	
    }

    /**
     * Writes the SQL for the Logic Filter.
     *
     * @param filter the logic statement to be turned into SQL.
     *
     * @throws RuntimeException for io exception with writer
     */
    public void visit(LogicFilter filter) throws RuntimeException {
        LOGGER.finer("exporting LogicFilter");

        filter.getFilterType();

        String type = (String) logical.get(new Integer(filter.getFilterType()));

        try {
            java.util.Iterator list = filter.getFilterIterator();

            if (filter.getFilterType() == AbstractFilter.LOGIC_NOT) {
                out.write(" NOT (");
                Filters.accept((org.opengis.filter.Filter) list.next(),this);
                
                out.write(")");
            } else { //AND or OR
                out.write("(");

                while (list.hasNext()) {
                    Filters.accept((org.opengis.filter.Filter) list.next(),this);

                    if (list.hasNext()) {
                        out.write(" " + type + " ");
                    }
                }

                out.write(")");
            }
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
    }

    /**
     * Writes the SQL for a Compare Filter.
     *  
     *  DJB: note, postgis overwrites this implementation because of the way
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
            left.accept(this);
            out.write(" " + type + " ");
            right.accept(this);
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
    }

    /**
     * Writes the SQL for the Null Filter.
     *
     * @param filter the null filter to be written to SQL.
     *
     * @throws RuntimeException for io exception with writer
     */
    public void visit(NullFilter filter) throws RuntimeException {
        LOGGER.finer("exporting NullFilter");

        DefaultExpression expr = (DefaultExpression) filter.getNullCheckValue();

        //String type = (String) comparisions.get(new Integer(
        //          filter.getFilterType()));
        try {
            expr.accept(this);
            out.write(" IS NULL ");
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
    }

    /**
     * Encodes an FidFilter.
     *
     * @param filter
     *
     * @throws RuntimeException DOCUMENT ME!
     *
     * @see org.geotools.filter.SQLEncoder#visit(org.geotools.filter.FidFilter)
     */
    public void visit(FidFilter filter) {
        if (mapper == null) {
            throw new RuntimeException(
                "Must set a fid mapper before trying to encode FIDFilters");
        }

        String[] fids = filter.getFids();
        LOGGER.finer("Exporting FID=" + Arrays.asList(fids));

        // prepare column name array
        String[] colNames = new String[mapper.getColumnCount()];

        for (int i = 0; i < colNames.length; i++) {
            colNames[i] = mapper.getColumnName(i);
        }

        for (int i = 0; i < fids.length; i++) {
            try {
                Object[] attValues = mapper.getPKAttributes(fids[i]);

                out.write("(");

                for (int j = 0; j < attValues.length; j++) {
                    out.write( escapeName(colNames[j]) );
                    out.write(" = '");
                    out.write(attValues[j].toString()); //DJB: changed this to attValues[j] from attValues[i].
                    out.write("'");

                    if (j < (attValues.length - 1)) {
                        out.write(" AND ");
                    }
                }

                out.write(")");

                if (i < (fids.length - 1)) {
                    out.write(" OR ");
                }
            } catch (java.io.IOException e) {
                throw new RuntimeException(IO_ERROR, e);
            }
        }
    }

    /**
     * Writes the SQL for the attribute Expression.
     *
     * @param expression the attribute to turn to SQL.
     *
     * @throws RuntimeException for io exception with writer
     */
    public void visit(AttributeExpression expression) throws RuntimeException {
        LOGGER.finer("exporting ExpressionAttribute");

        try {
        	//JD: evaluate the expression agains the feature type to get at the attribute, then 
        	// encode the namee
        	context = null;
        	if ( featureType != null ) {
        		AttributeDescriptor attributeType = (AttributeDescriptor) expression.evaluate( featureType );
            	if ( attributeType != null ) {
            		out.write( escapeName( attributeType.getLocalName() ) );
            		
            		//provide context for a literal being compared to this attribute
            		context = attributeType.getType().getBinding(); 
            		return;
            	}
        	}
        	
        	//if thigns are sane, we should get here
    		out.write(escapeName(expression.getAttributePath()));	
        	
        } catch (java.io.IOException ioe) {
            throw new RuntimeException("IO problems writing attribute exp", ioe);
        }
    }

    /**
     * Writes the SQL for the attribute Expression.
     *
     * @param expression the attribute to turn to SQL.
     */
    public void visit(Expression expression) {
        LOGGER.warning("exporting unknown (default) expression");
    }

    /**
     * Export the contents of a Literal Expresion
     *
     * @param expression the Literal to export
     *
     * @throws RuntimeException for io exception with writer
     */
    public void visit(LiteralExpression expression) throws RuntimeException {
        LOGGER.finer("exporting LiteralExpression");

        //type to convert the literal to
        Class target = null;
        
        if ( context != null ) {
            //first try to evaluate the expression in the context of a type
        	target = (Class) context;
        }
        
        if ( target == null ) {
        	//next try and use the filter code
        	short type = expression.getType();

            switch (type) {
            case Expression.LITERAL_DOUBLE:
            	target = Double.class;
            	break;
            case Expression.LITERAL_INTEGER:
            	target = Integer.class; 
            	break;
            case Expression.LITERAL_LONG:
                target = Long.class;
                break;
            case Expression.LITERAL_STRING:
            	target = String.class; 
                break;
            case Expression.LITERAL_GEOMETRY:
            	target = Geometry.class;
                break;
            default:
                throw new RuntimeException("type: " + type + "not supported");
            }
        }
        
        try {
			if ( target == Geometry.class && expression.getLiteral() instanceof Geometry ) {
				//call this method for backwards compatability with subclasses
				visitLiteralGeometry( expression );
				return;
			}
			else {
				//convert the literal to the required type
				//JD except for numerics, let the database do teh converstion
				Object literal = null;
				if ( Number.class.isAssignableFrom( target ) ) {
					//dont convert
				}
				else {
					//convert
					literal = expression.evaluate( null, target );
				}
				
				if ( literal == null ) {
					//just use string
					literal = expression.getLiteral().toString();
				}
				
				//geometry hook
				//if ( literal instanceof Geometry ) {
				if ( Geometry.class.isAssignableFrom( target ) ) {
					visitLiteralGeometry( expression );
				}
				//else if ( literal instanceof Number ) {
				else if ( Number.class.isAssignableFrom( target ) ) {
					out.write( literal.toString() );
				}
				//else if ( literal instanceof String ) {
				else if ( String.class.isAssignableFrom( target ) ) {
                    // sigle quotes must be escaped to have a valid sql string
                    String escaped = literal.toString().replaceAll("'", "''");
					out.write( "'" + escaped + "'" );
					return;
				}
				else {
					//convert back to a string
					String encoding = (String)Converters.convert( literal, String.class , null );
					if ( encoding == null ) {
						//could not convert back to string, use original l value
						encoding = expression.getLiteral().toString();
					}
					
					// sigle quotes must be escaped to have a valid sql string
					out.write( "'" + encoding.replaceAll("'", "''") + "'");
				}
			}
		} catch (IOException e) {
			 throw new RuntimeException("IO problems writing literal", e);
		}
        
    }

    /**
     * Subclasses must implement this method in order to encode geometry
     * filters according to the specific database implementation
     *
     * @param expression
     *
     * @throws IOException DOCUMENT ME!
     * @throws RuntimeException DOCUMENT ME!
     */
    protected void visitLiteralGeometry(LiteralExpression expression)
        throws IOException {
        throw new RuntimeException(
            "Subclasses must implement this method in order to handle geometries");
    }
   
    /**
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.GeometryFilter)
     */
    public void visit(GeometryFilter filter) {
        throw new RuntimeException(
            "Subclasses must implement this method in order to handle geometries");
    }

    /**
     * Writes the SQL for the Math Expression.
     *
     * @param expression the Math phrase to be written.
     *
     * @throws RuntimeException for io problems
     */
    public void visit(MathExpression expression) throws RuntimeException {
        LOGGER.finer("exporting Expression Math");

        String type = (String) expressions.get(new Integer(expression.getType()));

        try {
            ((DefaultExpression) expression.getLeftValue()).accept(this);
            out.write(" " + type + " ");
            ((DefaultExpression) expression.getRightValue()).accept(this);
        } catch (java.io.IOException ioe) {
            throw new RuntimeException("IO problems writing expression", ioe);
        }
    }

    /**
     * Writes sql for a function expression.  Not currently supported.
     *
     * @param expression a function expression
     *
     * @throws UnsupportedOperationException every time, this isn't supported.
     */
    public void visit(FunctionExpression expression)
        throws UnsupportedOperationException {
        String message = "Function expression support not yet added.";
        throw new UnsupportedOperationException(message);
    }

    /**
     * Sets the SQL name escape string.
     * 
     * <p>
     * The value of this string is prefixed and appended to table schema names,
     * table names and column names in an SQL statement to support mixed-case
     * and non-English names. Without this, the DBMS may assume a mixed-case
     * name in the query should be treated as upper-case and an SQLCODE of
     * -204 or 206 may result if the name is not found.
     * </p>
     * 
     * <p>
     * Typically this is the double-quote character, ", but may not be for all
     * databases.
     * </p>
     * 
     * <p>
     * For example, consider the following query:
     * </p>
     * 
     * <p>
     * SELECT Geom FROM Spear.ArchSites May be interpreted by the database as:
     * SELECT GEOM FROM SPEAR.ARCHSITES  If the column and table names were
     * actually created using mixed-case, the query needs to be specified as:
     * SELECT "Geom" from "Spear"."ArchSites"
     * </p>
     *
     * @param escape the character to be used to escape database names
     */
    public void setSqlNameEscape(String escape) {
        sqlNameEscape = escape;
    }

    /**
     * Sets the escape character for the column name.
     *
     * @param escape The character to be used to escape database names.
     *
     * @deprecated Use setSqlNameEscape instead, as it is more aptly named.
     */
    public void setColnameEscape(String escape) {
        sqlNameEscape = escape;
    }

    /**
     * Gets the column escape name.
     *
     * @return the string to be used to properly escape a db's name.
     *
     * @deprecated the escapeName method is preferred over this, it
     *             automatically returns the name properly escaped, since
     *             that's all getColnameEscape was being used for.
     */
    protected String getColnameEscape() {
        return sqlNameEscape;
    }

    /**
     * Surrounds a name with the SQL escape character.
     *
     * @param name
     *
     * @return DOCUMENT ME!
     */
    public String escapeName(String name) {
        return sqlNameEscape + name + sqlNameEscape;
    }

    public void visit(IncludeFilter filter) {
        try {
            out.write("TRUE");
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
        
    }

    public void visit(ExcludeFilter filter) {
        try {
            out.write("FALSE");
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
    }

    
}
