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
package org.geotools.data.jdbc;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.LikeFilterImpl;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.PrimaryKey;
import org.geotools.util.Converters;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.And;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.BinaryExpression;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
import org.opengis.filter.identity.Identifier;
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

import com.vividsolutions.jts.geom.Geometry;

/**
 * Encodes a filter into a SQL WHERE statement.  It should hopefully be generic
 * enough that any SQL database will work with it.
 * This generic SQL encoder should eventually
 * be able to encode all filters except Geometry Filters.
 * This is because the OGC's SFS for SQL document specifies
 * two ways of doing SQL databases, one with native geometry types and one
 * without.  To implement an encoder for one of the two types simply subclass
 * off of this encoder and put in the proper GeometryFilter visit method. Then
 * add the filter types supported to the capabilities by overriding the
 * {{@link #createFilterCapabilities()} method.
 * 
 * 
 * This version was ported from the original to support org.opengis.filter type
 * Filters.
 *
 * @author originally by Chris Holmes, TOPP
 * @author ported by Saul Farber, MassGIS
 *
 * @task REVISIT: need to figure out exceptions, we're currently eating io
 *       errors, which is bad. Probably need a generic visitor exception.
 * 
 *
 * @source $URL$
 */
/*
 * TODO: Use the new FilterCapabilities.  This may fall out of using the new
 * PrePostFilterSplitter code.
 * 
 * TODO: Use the new Geometry classes from org.opengis.  Not sure
 * when this will be required, but it's on the horizon here.
 * 
 * Non Javadoc comments:
 * 
 * Note that the old method allowed us to write WAY fewer methods, as we didn't
 * need to cover all the cases with exlpicit methods (as the new
 * org.opengis.filter.FilterVisitor and ExpressionVisitor methods require
 * us to do).
 * 
 * The code is split into methods to support the FilterVisitor interface first
 * then the ExpressionVisitor methods second.
 *  
 */
public class FilterToSQL implements FilterVisitor, ExpressionVisitor {
    /** error message for exceptions */
    protected static final String IO_ERROR = "io problem writing filter";

    /** The filter types that this class can encode */
    protected FilterCapabilities capabilities = null;

    /** Standard java logger */
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.filter");

    /** Character used to escape database schema, table and column names */
    private String sqlNameEscape = "";

    /** where to write the constructed string from visiting the filters. */
    protected Writer out;

    /** 
     * the fid mapper used to encode the fid filters
     * @deprecated use {@link #primaryKey}  
     */
    protected FIDMapper mapper;

    /**
     * The primary key corresponding to the table the filter is being encoded
     * against.  
     */
    protected PrimaryKey primaryKey;
    
    /** The schema that contains the table the filter being encoded against. */
    protected String databaseSchema;
    
    /** the schmema the encoder will be used to be encode sql for */
    protected SimpleFeatureType featureType;

    /** flag which indicates that the encoder is currently encoding a function */
    protected boolean encodingFunction = false;
    
    /** the geometry descriptor corresponding to the current binary spatial filter being encoded */
    protected GeometryDescriptor currentGeometry;
    
    /** the srid corresponding to the current binary spatial filter being encoded */
    protected Integer currentSRID;
     
    /**
     * Default constructor
     */
    public FilterToSQL() {
    }
    
    
    public FilterToSQL(Writer out) {
        this.out = out;
    }
    
    /**
     * Sets the writer the encoder will write to.
     */
    public void setWriter(Writer out) {
        this.out = out;
    }
    
    /**
     * Performs the encoding, sends the encoded sql to the writer passed in.
     *
     * @param filter the Filter to be encoded.
     *
     * @throws OpenGISFilterToOpenGISFilterToSQLEncoderException If filter type not supported, or if there
     *         were io problems.
     */
    public void encode(Filter filter) throws FilterToSQLException {
        if (out == null) throw new FilterToSQLException("Can't encode to a null writer.");
        if (getCapabilities().fullySupports(filter)) {

            try {
                out.write("WHERE ");
                filter.accept(this, null);

                //out.write(";");
            } catch (java.io.IOException ioe) {
                LOGGER.warning("Unable to export filter" + ioe);
                throw new FilterToSQLException("Problem writing filter: ", ioe);
            }
        } else {
            throw new FilterToSQLException("Filter type not supported");
        }
    }
    
    /**
     * purely a convenience method.
     * 
     * Equivalent to:
     * 
     *  StringWriter out = new StringWriter();
     *  new FilterToSQL(out).encode(filter);
     *  out.getBuffer().toString();
     * 
     * @param filter
     * @return a string representing the filter encoded to SQL.
     * @throws FilterToSQLException
     */
    
    public String encodeToString(Filter filter) throws FilterToSQLException {
        StringWriter out = new StringWriter();
        this.out = out;
        this.encode(filter);
        return out.getBuffer().toString();
    }
    
    /**
     * Performs the encoding, sends the encoded sql to the writer passed in.
     *
     * @param filter the Filter to be encoded.
     *
     * @throws OpenGISFilterToOpenGISFilterToSQLEncoderException If filter type not supported, or if there
     *         were io problems.
     */
    public void encode(Expression expression) throws FilterToSQLException {
        if (out == null) throw new FilterToSQLException("Can't encode to a null writer.");
        expression.accept(this, null);
    }
    
    /**
     * purely a convenience method.
     * 
     * Equivalent to:
     * 
     *  StringWriter out = new StringWriter();
     *  new FilterToSQL(out).encode(filter);
     *  out.getBuffer().toString();
     * 
     * @param filter
     * @return a string representing the filter encoded to SQL.
     * @throws FilterToSQLException
     */
    
    public String encodeToString(Expression expression) throws FilterToSQLException {
        StringWriter out = new StringWriter();
        this.out = out;
        this.encode(expression);
        return out.getBuffer().toString();
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
     * Sets the FIDMapper that will be used in subsequente visit calls. There
     * must be a FIDMapper in order to invoke the FIDFilter encoder.
     *
     * @param mapper
     * @deprecated use {@link #setPrimaryKey(PrimaryKey)}
     */
    public void setFIDMapper(FIDMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * @deprecated use {@link #getPrimaryKey()}
     */
    public FIDMapper getFIDMapper() {
        return this.mapper;
    }

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }
    
    public void setPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    public String getDatabaseSchema() {
        return databaseSchema;
    }
    
    public void setDatabaseSchema(String databaseSchema) {
        this.databaseSchema = databaseSchema;
    }
    
    /**
     * Sets the capabilities of this filter.
     *
     * @return FilterCapabilities for this Filter
     */
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities capabilities = new FilterCapabilities();

        capabilities.addAll(FilterCapabilities.LOGICAL_OPENGIS);
        capabilities.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
        capabilities.addType(PropertyIsNull.class);
        capabilities.addType(PropertyIsBetween.class);
        capabilities.addType(Id.class);
        capabilities.addType(IncludeFilter.class);
        capabilities.addType(ExcludeFilter.class);

        return capabilities;
    }

    /**
     * Describes the capabilities of this encoder.
     * 
     * <p>
     * Performs lazy creation of capabilities.
     * </p>
     * 
     * If you're subclassing this class, override createFilterCapabilities
     * to declare which filtercapabilities you support.  Don't use
     * this method.
     *
     * @return The capabilities supported by this encoder.
     */
    public synchronized final FilterCapabilities getCapabilities() {
        if (capabilities == null) {
            capabilities = createFilterCapabilities();
        }

        return capabilities; //maybe clone?  Make immutable somehow
    }
    
    /**
     * Sets the capabilities for the encoder.
     */
    public void setCapabilities(FilterCapabilities capabilities) {
        this.capabilities = capabilities;
    }
    
    
    // BEGIN IMPLEMENTING org.opengis.filter.FilterVisitor METHODS
    
    /**
     * @see {@link FilterVisitor#visit(ExcludeFilter, Object)}
     * 
     * Writes the SQL for the IncludeFilter by writing "FALSE".
     * 
     * @param the filter to be visited
     */
    public Object visit(ExcludeFilter filter, Object extraData) {
        try {
            out.write("0 = 1");
        } catch (IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
        return extraData;
    }

    
    /**
     * @see {@link FilterVisitor#visit(IncludeFilter, Object)}
     * 
     * Writes the SQL for the IncludeFilter by writing "TRUE".
     * 
     * @param the filter to be visited
     *  
     */
    public Object visit(IncludeFilter filter, Object extraData) {
        try {
            out.write("1 = 1");
        } catch (IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
        return extraData;
    }

    /**
     * Writes the SQL for the PropertyIsBetween Filter.
     *
     * @param filter the Filter to be visited.
     *
     * @throws RuntimeException for io exception with writer
     */
    public Object visit(PropertyIsBetween filter, Object extraData) throws RuntimeException {
        LOGGER.finer("exporting PropertyIsBetween");

        Expression expr = (Expression) filter.getExpression();
        Expression lowerbounds = (Expression) filter.getLowerBoundary();
        Expression upperbounds = (Expression) filter.getUpperBoundary();
        
        Class context;
        AttributeDescriptor attType = (AttributeDescriptor)expr.evaluate(featureType);
        if (attType != null) {
            context = attType.getType().getBinding();
        } else {
            //assume it's a string?
            context = String.class;
        }

        try {
            expr.accept(this, extraData);
            out.write(" BETWEEN ");
            lowerbounds.accept(this, context);
            out.write(" AND ");
            upperbounds.accept(this, context);
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
        return extraData;
    }

    
    /**
     * Writes the SQL for the Like Filter.  Assumes the current java
     * implemented wildcards for the Like Filter: . for multi and .? for
     * single. And replaces them with the SQL % and _, respectively.
     *
     * @param filter the Like Filter to be visited.
     *
     * @task REVISIT: Need to think through the escape char, so it works  right
     *       when Java uses one, and escapes correctly with an '_'.
     */
    public Object visit(PropertyIsLike filter, Object extraData)
	{
    	char esc = filter.getEscape().charAt(0);
    	char multi = filter.getWildCard().charAt(0);
    	char single = filter.getSingleChar().charAt(0);
        boolean matchCase = filter.isMatchingCase();
        
        String literal = filter.getLiteral();
        Expression att = filter.getExpression();
        
        //JD: hack for date values, we append some additional padding to handle
        // the matching of time/timezone/etc...
        AttributeDescriptor ad = (AttributeDescriptor) att.evaluate( featureType );
        if ( ad != null && Date.class.isAssignableFrom( ad.getType().getBinding() ) ) {
            literal += multi;
        }
        
    	String pattern = LikeFilterImpl.convertToSQL92(esc, multi, single, matchCase, literal);
        
        try {
            if (!matchCase){
                out.write(" UPPER(");
            }

	    	att.accept(this, extraData);

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
        return extraData;
    }

    /**
     * Write the SQL for an And filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(And filter, Object extraData) {
        return visit((BinaryLogicOperator)filter, "AND");
    }
    
    /**
     * Write the SQL for a Not filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(Not filter, Object extraData) {
        return visit((BinaryLogicOperator)filter, "NOT");
    }
    
    /**
     * Write the SQL for an Or filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(Or filter, Object extraData) {
        return visit((BinaryLogicOperator)filter, "OR");
    }
    
    /**
     * Common implementation for BinaryLogicOperator filters.  This way
     * they're all handled centrally.
     *
     * @param filter the logic statement to be turned into SQL.
     * @param extraData extra filter data.  Not modified directly by this method.
     */
    protected Object visit(BinaryLogicOperator filter, Object extraData) {
        LOGGER.finer("exporting LogicFilter");

        String type = (String)extraData;

        try {
            java.util.Iterator list = filter.getChildren().iterator();

            if (filter instanceof Not) {
                out.write(type + " (");
                ((Filter) list.next()).accept(this, extraData);
                out.write(")");
            } else { //AND or OR
                out.write("(");

                while (list.hasNext()) {
                    ((Filter) list.next()).accept(this, extraData);

                    if (list.hasNext()) {
                        out.write(" " + type + " ");
                    }
                }

                out.write(")");
            }
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
        return extraData;
    }
    
    

    /**
     * Write the SQL for this kind of filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator((BinaryComparisonOperator)filter, "=");
        return extraData;
    }
    
    /**
     * Write the SQL for this kind of filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator((BinaryComparisonOperator)filter, ">=");
        return extraData;
    }
    
    /**
     * Write the SQL for this kind of filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        visitBinaryComparisonOperator((BinaryComparisonOperator)filter, ">");
        return extraData;
    }
    
    /**
     * Write the SQL for this kind of filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        visitBinaryComparisonOperator((BinaryComparisonOperator)filter, "<");
        return extraData;
    }
    
    /**
     * Write the SQL for this kind of filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator((BinaryComparisonOperator)filter, "<=");
        return extraData;
    }
    
    /**
     * Write the SQL for this kind of filter
     * 
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     * 
     */
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator((BinaryComparisonOperator)filter, "!=");
        return extraData;
    }
    
    /**
     * Common implementation for BinaryComparisonOperator filters.  This way
     * they're all handled centrally.
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
    protected void visitBinaryComparisonOperator(BinaryComparisonOperator filter, Object extraData) throws RuntimeException {
        LOGGER.finer("exporting SQL ComparisonFilter");

        Expression left = filter.getExpression1();
        Expression right = filter.getExpression2();
        Class leftContext = null, rightContext = null;
        if (left instanceof PropertyName) {
            // aha!  It's a propertyname, we should get the class and pass it in
            // as context to the tree walker.
            AttributeDescriptor attType = (AttributeDescriptor)left.evaluate(featureType);
            if (attType != null) {
                rightContext = attType.getType().getBinding();
            }
        }
        
        if (right instanceof PropertyName) {
            AttributeDescriptor attType = (AttributeDescriptor)right.evaluate(featureType);
            if (attType != null) {
                leftContext = attType.getType().getBinding();
            }
        }

        //case sensitivity
        boolean matchCase = true;
        if ( !filter.isMatchingCase() ) {
            //we only do for = and !=
            if ( filter instanceof PropertyIsEqualTo || 
                    filter instanceof PropertyIsNotEqualTo ) {
                //and only for strings
                if ( String.class.equals( leftContext ) 
                        || String.class.equals( rightContext ) ) {
                    matchCase = false;
                }
            }
        }
        
        String type = (String) extraData;

        try {
            if ( matchCase ) {
                left.accept(this, leftContext);
                out.write(" " + type + " ");
                right.accept(this, rightContext);
            }
            else {
                //wrap both sides in "lower"
                FunctionImpl f = new FunctionImpl(); 
                f.setName( "lower" );
                
                f.setParameters(Arrays.asList(left));
                f.accept(this, Arrays.asList(leftContext));
                
                out.write(" " + type + " ");
                
                f.setParameters(Arrays.asList(right));
                f.accept(this, Arrays.asList(rightContext));
            }
            
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
    public Object visit(PropertyIsNull filter, Object extraData) throws RuntimeException {
        LOGGER.finer("exporting NullFilter");

        Expression expr = filter.getExpression();

        try {
            expr.accept(this, extraData);
            out.write(" IS NULL ");
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
        return extraData;
    }

    /**
     * Encodes an Id filter
     *
     * @param filter the
     *
     * @throws RuntimeException If there's a problem writing output
     *
     */
    public Object visit(Id filter, Object extraData) {
        if (mapper == null) {
            throw new RuntimeException(
                "Must set a fid mapper before trying to encode FIDFilters");
        }

        Set ids = filter.getIdentifiers();
        
        LOGGER.finer("Exporting FID=" + ids);

        // prepare column name array
        String[] colNames = new String[mapper.getColumnCount()];

        for (int i = 0; i < colNames.length; i++) {
            colNames[i] = mapper.getColumnName(i);
        }

        for (Iterator i = ids.iterator(); i.hasNext(); ) {
            try {
                Identifier id = (Identifier) i.next();
                Object[] attValues = mapper.getPKAttributes(id.toString());

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

                if (i.hasNext()) {
                    out.write(" OR ");
                }
            } catch (java.io.IOException e) {
                throw new RuntimeException(IO_ERROR, e);
            }
        }
        
        return extraData;
    }
    
    public Object visit(BBOX filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Beyond filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Contains filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Crosses filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Disjoint filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(DWithin filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Equals filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Intersects filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Overlaps filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Touches filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    public Object visit(Within filter, Object extraData) {
        return visitBinarySpatialOperator((BinarySpatialOperator)filter, extraData);
    }
    
    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter,
            Object extraData) {
        // basic checks
        if (filter == null)
            throw new NullPointerException(
                    "Filter to be encoded cannot be null");
        if (!(filter instanceof BinaryComparisonOperator))
            throw new IllegalArgumentException(
                    "This filter is not a binary comparison, "
                            + "can't do SDO relate against it: "
                            + filter.getClass());

        // extract the property name and the geometry literal
        PropertyName property;
        Literal geometry;
        BinaryComparisonOperator op = (BinaryComparisonOperator) filter;
        if (op.getExpression1() instanceof PropertyName
                && op.getExpression2() instanceof Literal) {
            property = (PropertyName) op.getExpression1();
            geometry = (Literal) op.getExpression2();
        } else if (op.getExpression2() instanceof PropertyName
                && op.getExpression1() instanceof Literal) {
            property = (PropertyName) op.getExpression2();
            geometry = (Literal) op.getExpression1();
        } else {
            throw new IllegalArgumentException(
                    "Can only encode spatial filters that do "
                            + "compare a property name and a geometry");
        }

        // handle native srid
        currentGeometry = null;
        currentSRID = null;
        if (featureType != null) {
            // going thru evaluate ensures we get the proper result even if the
            // name has
            // not been specified (convention -> the default geometry)
            AttributeDescriptor descriptor = (AttributeDescriptor) property
                    .evaluate(featureType);
            if (descriptor instanceof GeometryDescriptor) {
                currentGeometry = (GeometryDescriptor) descriptor;
                currentSRID = (Integer) descriptor.getUserData().get(
                        JDBCDataStore.JDBC_NATIVE_SRID);
            }
        }

        return visitBinarySpatialOperator(filter, property, geometry, filter
                .getExpression1() instanceof Literal, extraData);
    }

    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter,
            PropertyName property, Literal geometry, boolean swapped,
            Object extraData) {
        throw new RuntimeException(
            "Subclasses must implement this method in order to handle geometries");
    }
    
    /**
     * Encodes a null filter value.  The current implementation
     * does exactly nothing.
     * @param extraData extra data to be used to evaluate the filter
     * @return the untouched extraData parameter
     */
    public Object visitNullFilter(Object extraData) {
        return extraData;
    }

    // END IMPLEMENTING org.opengis.filter.FilterVisitor METHODS
    
    
    // START IMPLEMENTING org.opengis.filter.ExpressionVisitor METHODS

    /**
     * Writes the SQL for the attribute Expression.
     * 
     * @param expression the attribute to turn to SQL.
     *
     * @throws RuntimeException for io exception with writer
     */
    public Object visit(PropertyName expression, Object extraData) throws RuntimeException {
        LOGGER.finer("exporting PropertyName");
        
        try {
            //first evaluate expression against feautre type get the attribute, 
            //  this handles xpath
            AttributeDescriptor attribute = null;
            try {
                attribute = (AttributeDescriptor) expression.evaluate(featureType);
            }
            catch( Exception e ) {
                //just log and fall back on just encoding propertyName straight up
                String msg = "Error occured mapping " + expression + " to feature type";
                LOGGER.log( Level.WARNING, msg, e );
            }
            if ( attribute != null ) {
                //use the name of the attribute
                out.write(escapeName(attribute.getLocalName()));
            }
            else {
                //fall back to just encoding the properyt name
                out.write(escapeName(expression.getPropertyName()));
            }
    		
        } catch (java.io.IOException ioe) {
            throw new RuntimeException("IO problems writing attribute exp", ioe);
        }
        return extraData;
    }

    /**
     * Export the contents of a Literal Expresion
     *
     * @param expression
     * the Literal to export
     *
     * @throws RuntimeException
     * for io exception with writer
     */
    public Object visit(Literal expression, Object context)
            throws RuntimeException {
        LOGGER.finer("exporting LiteralExpression");

        // type to convert the literal to
        Class target = null;
        if ( context instanceof Class ) {
            target = (Class) context;
        }

        try {
            //evaluate the expression
            Object literal = evaluateLiteral( expression, target );
            
            // handle geometry case
            if (literal instanceof Geometry) {
                // call this method for backwards compatibility with subclasses
                visitLiteralGeometry(CommonFactoryFinder.getFilterFactory(null).literal(literal));
            } else {
                // write out the literal allowing subclasses to override this
                // behaviour (for writing out dates and the like using the BDMS custom functions)
                writeLiteral(literal);
            }

        } catch (IOException e) {
            throw new RuntimeException("IO problems writing literal", e);
        }
        return context;
    }

    protected Object evaluateLiteral(Literal expression, Class target ) {
        Object literal = null;
        // HACK: let expression figure out the right value for numbers,
        // since the context is almost always improperly set and the
        // numeric converters try to force floating points to integrals 
        if(target != null && !(Number.class.isAssignableFrom(target))) 
            // use the target type
            literal = expression.evaluate(null, target);
        
        // if the target was not known, of the conversion failed, try the
        // type guessing dance literal expression does only for the following
        // method call
        if(literal == null)
            literal = expression.evaluate(null);
        
        // if that failed as well, grab the value as is
        if(literal == null)
            literal = expression.getValue();
        
        return literal;
    }

    /**
     * Writes out a non null, non geometry literal. The base class properly handles
     * null, numeric and booleans (true|false), and turns everything else into a string.
     * Subclasses are expected to override this shall they need a different treatment
     * (e.g. for dates)
     * @param literal
     * @throws IOException
     */
    protected void writeLiteral(Object literal) throws IOException {
        if(literal == null) {
          out.write("NULL");
        } else if(literal instanceof Number || literal instanceof Boolean) {
            out.write(String.valueOf(literal));
        } else {
            // we don't know what this is, let's convert back to a string
            String encoding = (String) Converters.convert(literal,
                    String.class, null);
            if (encoding == null) {
                // could not convert back to string, use original l value
                encoding = literal.toString();
            }

            // sigle quotes must be escaped to have a valid sql string
            String escaped = encoding.replaceAll("'", "''");
            out.write("'" + escaped + "'");
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
    protected void visitLiteralGeometry(Literal expression)
        throws IOException {
        throw new RuntimeException(
            "Subclasses must implement this method in order to handle geometries");
    }
    
    public Object visit(Add expression, Object extraData) {
        return visit((BinaryExpression)expression, "+", extraData);
    }
    public Object visit(Divide expression, Object extraData) {
        return visit((BinaryExpression)expression, "/", extraData);
    }
    public Object visit(Multiply expression, Object extraData) {
        return visit((BinaryExpression)expression, "*", extraData);
    }
    public Object visit(Subtract expression, Object extraData) {
        return visit((BinaryExpression)expression, "-", extraData);
    }

    /**
     * Writes the SQL for the Math Expression.
     *
     * @param expression the Math phrase to be written.
     * @param operator The operator of the expression.
     *
     * @throws RuntimeException for io problems
     */
    protected Object visit(BinaryExpression expression, String operator, Object extraData) throws RuntimeException {
        LOGGER.finer("exporting Expression Math");

        try {
            expression.getExpression1().accept(this, extraData);
            out.write(" " + operator + " ");
            expression.getExpression2().accept(this, extraData);
        } catch (java.io.IOException ioe) {
            throw new RuntimeException("IO problems writing expression", ioe);
        }
        return extraData;
    }

    /**
     * Writes sql for a function expression.
     *
     * @param expression a function expression
     *
     * @throws RuntimeException If an IO error occurs. 
     */
    public Object visit(Function function, Object extraData) throws RuntimeException {
        try {
            List<Expression> parameters = function.getParameters();
            List contexts = null;
            //check context, if a list which patches parameter size list assume its context
            // to pass along to each Expression for encoding
            if( extraData instanceof List && ((List)extraData).size() == parameters.size() ) {
                contexts = (List) extraData;
            }
            
            //set the encoding function flag to signal we are inside a function
            encodingFunction = true;
            
            //write the name
            out.write( function.getName() );
            
            //write the arguments
            out.write( "(");
            for ( int i = 0; i < parameters.size(); i++ ) {
                Expression e = parameters.get( i );
                
                Object context = contexts != null ? contexts.get( i ) : extraData; 
                e.accept(this, context);
                
                if ( i < parameters.size()-1 ) {
                    out.write( ",");    
                }
                
            }
            out.write( ")");
            
            //reset the encoding function flag
            encodingFunction = false;
        } 
        catch (IOException e) {
            throw new RuntimeException( e );
        }
        
        return extraData;
    }
    
    public Object visit(NilExpression expression, Object extraData) {
        try {
            out.write(" ");
        } catch (java.io.IOException ioe) {
            throw new RuntimeException("IO problems writing expression", ioe);
        }
        
        return extraData;
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
     * Surrounds a name with the SQL escape character.
     *
     * @param name
     *
     * @return DOCUMENT ME!
     */
    public String escapeName(String name) {
        return sqlNameEscape + name + sqlNameEscape;
    }
}
