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
package org.geotools.filter.text.commons;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.BinaryExpression;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.During;
import org.opengis.temporal.Period;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

/**
 * 
 * This abstract class provides the common behavior to build the filters for the related
 * semantic actions of parsing language process.
 * 
 * <p>
 * Builds Filter or Expression and their components (literal, functions, etc).
 * It maintains the results of semantic actions in the stack used to build complex
 * filters and expressions.
 * </p>
 * 
 * <p>
 * Warning: This component is not published. It is part of module implementation. 
 * Client module should not use this feature.
 * </p>
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 *
 *
 *
 * @source $URL$
 */
public abstract class AbstractFilterBuilder {

    private final FilterFactory filterFactory;

    private final BuildResultStack resultStack;

    protected final String cqlSource;

    /**
     * New instance of FilterBuilder
     * @param cqlSource 
     * @param filterFactory
     */
    public AbstractFilterBuilder(final String cqlSource, final FilterFactory filterFactory){
        assert cqlSource != null: "illegal argument";
        assert filterFactory != null: "illegal argument";
        
        this.cqlSource = cqlSource;
        this.filterFactory = filterFactory;
        
        this.resultStack = new BuildResultStack(cqlSource);

    }

    protected FilterFactory getFilterFactory(){
        return this.filterFactory;
    }
    
    protected final BuildResultStack getResultStack(){
        return this.resultStack;
    }
    
    protected final String getStatement(){
        return this.cqlSource;
    }
    /**
     * Adds in the result stack the partial result associated to node.
     * @param built partial result
     * @param token 
     * @param type node associated to partial result
     */
    public void pushResult(final Result result) {
    
        this.resultStack.push(result);
    }

    public Result peekResult() {

        return this.resultStack.peek();
    }
    
    

    public Filter getFilter() throws CQLException {
        return resultStack.popFilter();
    }

    public Expression getExpression() throws CQLException {
        return resultStack.popExpression();
    }

    public List<Filter> getFilterList() throws CQLException {
        
        int size = resultStack.size();
        List<Filter> results = new ArrayList<Filter>(size);

        for (int i = 0; i < size; i++) {
            Result item = this.resultStack.popResult();
            Filter result = (Filter)item.getBuilt();
            results.add(0, result);
        }

        return results;
    }

    public BinaryExpression buildAddExpression() throws CQLException {
        
        Expression right = this.resultStack.popExpression();
        Expression left = this.resultStack.popExpression();

        return filterFactory.add(left, right);
    }

    public BinaryExpression buildSubtractExression() throws CQLException {
        Expression right = this.resultStack
                .popExpression();
        Expression left = this.resultStack
                .popExpression();

        return filterFactory.subtract(left, right);
    }

    public BinaryExpression buildMultiplyExpression() throws CQLException {
        
        Expression right = this.resultStack.popExpression();
        Expression left = this.resultStack.popExpression();
        
        return filterFactory.multiply(left, right);
    }

    public BinaryExpression buildDivideExpression() throws CQLException {
        
        Expression right = this.resultStack.popExpression();
        Expression left = this.resultStack.popExpression();

        return  filterFactory.divide(left, right);
    }

    public Filter buildAndFilter() throws CQLException {
        
        Filter right = this.resultStack.popFilter();
        Filter left = this.resultStack.popFilter();

        Filter logicFilter = null;
        if (Filter.INCLUDE.equals(right)) {
            logicFilter = left;
        } else if (Filter.INCLUDE.equals(left)) {
            logicFilter = right;
        } else if (Filter.EXCLUDE.equals(right)
                || Filter.EXCLUDE.equals(left)) {
            logicFilter = Filter.EXCLUDE;
        } else {
            logicFilter = filterFactory.and(left, right);
        }
        return logicFilter;
        
    }

    public Filter buildOrFilter() throws CQLException {
        Filter right = this.resultStack.popFilter();
        Filter left = this.resultStack.popFilter();

        Filter logicFilter = null;
        if (Filter.INCLUDE.equals(right) || Filter.INCLUDE.equals(left)) {
            logicFilter = Filter.INCLUDE;
        } else if (Filter.EXCLUDE.equals(left)) {
            logicFilter = right;
        } else if (Filter.EXCLUDE.equals(right)) {
            logicFilter = left;
        } else {
            logicFilter = filterFactory.or(left, right);
        }

        return logicFilter;
    }

    public Filter buildNotFilter() throws CQLException {
        
        Filter right = this.resultStack.popFilter();

        Filter logicFilter = null;
        
        if (Filter.INCLUDE.equals(right)) {
            logicFilter = Filter.EXCLUDE;
        } else if (Filter.EXCLUDE.equals(right)) {
            logicFilter = Filter.INCLUDE;
        } else {
            logicFilter = filterFactory.not(right);
        }
        
        return logicFilter;
    }
    
    /**
     * Builds a like filter
     * 
     * @matchCase 
     * 
     * @return a PropertyIsLike
     * @throws CQLException
     */
    public PropertyIsLike buildLikeFilter(boolean matchCase) throws CQLException {
        final String WC_MULTI = "%";
        final String WC_SINGLE = "_";
        final String ESCAPE = "\\";

        try {
            org.opengis.filter.expression.Expression pattern = this.resultStack
                    .popExpression();
            org.opengis.filter.expression.Expression expr = this.resultStack
                    .popExpression();

            PropertyIsLike f = filterFactory.like(expr, pattern.toString(),
                    WC_MULTI, WC_SINGLE, ESCAPE, matchCase);

            return f;
        } catch (IllegalFilterException ife) {
            throw new CQLException("Exception building LikeFilter: " + ife.getMessage(), this.cqlSource);
        }
    }

    /**
     * Builds property is null filter
     * 
     * @return PropertyIsNull
     * @throws CQLException
     */
    public PropertyIsNull buildPropertyIsNull() throws CQLException {
        try {
            org.opengis.filter.expression.Expression property = this.resultStack
                    .popExpression();

            PropertyIsNull filter = filterFactory.isNull(property);

            return filter;
        } catch (CQLException e) {
            throw new CQLException("Exception building Null Predicate", this.cqlSource);
        }
    }

    public Not buildPorpertyNotIsNull() throws CQLException {
        return  filterFactory.not( this.buildPropertyIsNull() );
    }

    /**
     * builds PropertyIsBetween filter
     * 
     * @return PropertyIsBetween
     * @throws CQLException
     */
    public PropertyIsBetween buildBetween() throws CQLException {
        try {
            org.opengis.filter.expression.Expression sup = this.resultStack
                    .popExpression();
            org.opengis.filter.expression.Expression inf = this.resultStack
                    .popExpression();
            org.opengis.filter.expression.Expression expr = this.resultStack
                    .popExpression();

            PropertyIsBetween filter = filterFactory.between(expr, inf, sup);

            return filter;
        } catch (IllegalFilterException ife) {
            throw new CQLException("Exception building CompareFilter: "+ ife.getMessage(), this.cqlSource);
        }
    }

    public Not buildNotBetween() throws CQLException {
        return filterFactory.not(buildBetween());
    }

    public Not buildNotLikeFilter(boolean matchCase) throws CQLException {

        Not filter = filterFactory.not(buildLikeFilter(matchCase));

        return filter;
    }

    /**
     * Creates PropertyIsEqualTo with PropertyExists predicate
     * 
     * @return PropertyIsEqualTo
     * @throws CQLException
     */
    public PropertyIsEqualTo buildPropertyExists() throws CQLException {

        PropertyName property = this.resultStack.popPropertyName();

        org.opengis.filter.expression.Expression[] args = new org.opengis.filter.expression.Expression[1];
        args[0] = filterFactory.literal(property);

        Function function = filterFactory.function("PropertyExists", args);
        Literal literalTrue = filterFactory.literal(Boolean.TRUE);
        
        PropertyIsEqualTo propExistsFilter = filterFactory.equals(function,
                literalTrue);

        return propExistsFilter;
    }

	/**
	 * Creates a literal with date time
	 * 
	 * @param token with date time
	 * @return Literal
	 * @throws CQLException
	 */
	public Literal buildDateTimeExpression(final IToken token)
			throws CQLException {
		return asLiteralDate(token.toString());
	}

	/**
	 * Transforms the cqlDateTime to a literal date. 
	 * @param cqlDateTime a string with the format yyyy-MM-ddTHH:mm:ss.s[(+|-)HH:mm]
	 * @return a literal Date
	 * @throws CQLException
	 */
	private Literal asLiteralDate(final String cqlDateTime) throws CQLException {

		try {
			final String strDate = extractDate(cqlDateTime);
			final String strTime = extractTime(cqlDateTime);
			String timeZoneOffset = extractTimeZone(cqlDateTime);

			StringBuilder format = new StringBuilder("yyyy-MM-dd");
			if (!"".equals(strTime)) {
				format.append(" HH:mm:ss");
			}
			TimeZone tz = null;
			if (!"".equals(timeZoneOffset)) {
				if ("Z".equals(timeZoneOffset)) { // it is Zulu or 0000 zone
													// (old syntax)
					timeZoneOffset = "GMT+00:00";
				}
				tz = TimeZone.getTimeZone(timeZoneOffset);
			} else { // the time zone offset wasn't specified then the time zone
						// is that provided by the host
				tz = TimeZone.getDefault();
			}
			DateFormat formatter = new SimpleDateFormat(format.toString());
			formatter.setTimeZone(tz);

			Date date;
			if (!"".equals(strTime)) {
				date = formatter.parse(strDate + " " + strTime);
			} else {
				date = formatter.parse(strDate);
			}
			Literal literalDate = filterFactory.literal(date);

			return literalDate;
		} catch (java.text.ParseException e) {
			throw new CQLException("Unsupported date time format: "
					+ e.getMessage(), this.cqlSource);
		}
		
	}
	
	
	

	/**
	 * Extracts the time zone from the parameter
	 * @param cqlDateTime 
	 * @return String with the time zone
	 */
    private String extractTimeZone(final String cqlDateTime) throws CQLException {
    	
		String strUpper = cqlDateTime.toUpperCase();
		assert strUpper.indexOf("T") != 0: "the time is not optional in the date-time syntax rule"; 
		
		String time = strUpper.split("T")[1];
		
		int index;
		index = time.indexOf("Z");
		if(index != -1 ){
			return "Z";
		} 
		index = time.indexOf("+");
		if(index != -1 ){
			return time.substring(index);
		}
		index = time.indexOf("-");
		if(index != -1 ){
			return time.substring(index);
		} else {
			return ""; // it will use the locale zone
		}
	}

    /**
     * Remove the ":". The Z format is [sign]hhmm 
     * @param cqlTimeZone  [sign]hh:mm
     * @return [sign]hhmm
     */
    private String toTimeZone(final String cqlTimeZone) {
		
    	String[] str = cqlTimeZone.split(":");
    	assert str.length == 2;
    	return  str[0].concat(str[1]);
	}

	/**
     * Extracts the time
     * 
     * @param cqlDateTime
     * @return the time or a null string
     */
	private String extractTime(String cqlDateTime) {
		
		// splits dateTime to get the Time and time zone in the second array element
		String strUpper = cqlDateTime.toUpperCase();
		String[] dateTime = strUpper.split("T");
		assert dateTime.length >= 2 : "date and time is required by the sintax rule";
		
		// splits the time and time zone 
		String[] time = dateTime[1].split("[-+Z]");
		
		return time[0]; // the time
	}

	/**
	 * Extracts the Date from cql date time
	 * @param cqlDateTime
	 * @return String with the date
	 */
	private String extractDate(final String cqlDateTime) {
		String strUpper = cqlDateTime.toUpperCase();
		String[] dateTime = strUpper.split("T");
		assert dateTime.length > 0 : "must have a date";
		return dateTime[0];
	}

	public Not buildNotFilter(Filter eq) {
        return filterFactory.not(eq);
    }

    public Literal buildTrueLiteral() {
        return filterFactory.literal(Boolean.TRUE);
    }

    public Literal buildFalseLiteral() {
        return filterFactory.literal(Boolean.FALSE);
    }

    public Literal buildLiteralInteger(final String tokenImage) {
        
        return filterFactory.literal(Long.parseLong(tokenImage));
    }
    public Literal buildLiteralDouble(final String tokenImage) {
        
        return filterFactory.literal(Double.parseDouble(tokenImage));    
    }
    

    public Literal buildLiteralString(final String tokenImage) {

        String strLiteral = removeQuotes(tokenImage);
        return filterFactory.literal(strLiteral);
    }

    
    /**
     * Removes initial and final "'" from string. If some "''" is found
     * it will be changed by a single quote "'".
     * 
     * @param source
     * @return string without initial and final quote, and "''" replaced 
     * by "'".
     */
    protected String removeQuotes(final String source){
        
        // checks if it has initial an final quote
        final String quote = "'";
        if( !( source.startsWith(quote) && source.endsWith(quote)) ){
                return source;
        }
    
        int length = source.length();
        
        // removes the first and last quote
        String result = source.substring(1,length -1);
        // removes internal quotes
        result = result.replaceAll("''", "'");
        
        return result;
    }

    public String buildIdentifier(final int nodeIdentifier) throws CQLException {

        // precondition: the stack have one or more identifier part parts
        try {
            
            // retrieves all part of identifier from result stack
            ArrayList<String> arrayParts = new ArrayList<String>();

            while (this.resultStack.size() > 0) {
                Result r = this.resultStack.peek();
                
                if (r.getNodeType() != nodeIdentifier) {
                    break; 
                }
                String part = this.resultStack.popIdentifierPart();
                part = removeFirstAndLastDoubleQuote(part);
                arrayParts.add(part);
            }
            assert arrayParts.size() >= 1 : "postcondition: the list of identifier part must have one or more elements ";
            
            // makes the identifier
            StringBuffer identifier = new StringBuffer(100);
            String part;

            int i = 0;

            for (i = arrayParts.size() - 1; i > 0; i--) {
                part = (String) arrayParts.get(i);
                identifier.append(part).append(":");
            } 
            assert i== 0;

            part = arrayParts.get(i);
            identifier.append(part);

            return identifier.toString();

        } catch (CQLException e) {
            throw new CQLException("Fail builing identifier: " + e.getMessage(), this.cqlSource);
        }
    }

    /**
     * Creates the identifier part. An identifier like "idpart1:idpart2:idpart3:
     * ... idpartN" has N part.
     * 
     * @return identifier part
     */
    public String buildIdentifierPart(IToken token) {
        String part = token.toString();

        return part;
    }
    
    /**
     * Removes the initial and final double quote. If the source string has not double quotes the
     * source is returned without changes. 
     * @param source
     * @return the source without double quotes (initial and final)
     */
    private String removeFirstAndLastDoubleQuote(String source) {
            
            // checks if it has initial an final quote
            final String doubleQuote = "\"";
            if( !( source.startsWith(doubleQuote) && source.endsWith(doubleQuote)) ){
                    return source; // return without changes
            }
            // removes the first and last quote
            String result = source.substring(1,source.length() -1);
            
            return result;
	}

	public PropertyName buildSimpleAttribute() throws CQLException {
        // Only retrieve the identifier built before
        String identifier = this.resultStack.popIdentifier();
        PropertyName property = filterFactory.property(identifier);

        return property;
    }
    /**
     * 
     * @param nodeSimpleAttr
     * @param nodeAttrSeparator
     * @return PropertyName
     * @throws CQLException
     */
    public PropertyName buildCompoundAttribute(final int nodeSimpleAttr, 
                                               final String nodeAttrSeparator ) 
        throws CQLException {

        ArrayList<String> arrayIdentifiers = new ArrayList<String>();

        // precondition: stack has one or more simple attributes
        while (this.resultStack.size() > 0) {
            Result r = this.resultStack.peek();

            if (r.getNodeType() !=nodeSimpleAttr ) {
                break;
            }

            PropertyName simpleAttribute = this.resultStack.popPropertyName();

            arrayIdentifiers.add(simpleAttribute.getPropertyName());
        }

        // postcondition: array has one or more simple attribute
        StringBuffer attribute = new StringBuffer(100);
        int i = 0;

        for (i = arrayIdentifiers.size() - 1; i > 0; i--) {
            attribute.append(arrayIdentifiers.get(i));
            attribute.append(nodeAttrSeparator);
        }

        attribute.append(arrayIdentifiers.get(i));

        PropertyName property = filterFactory.property(attribute.toString());

        return property;
    }
    

    public Literal buildDistanceUnit(IToken token) throws CQLException {
        Literal unit = null;

        unit = filterFactory.literal(token.toString());

        return unit;
    }
    
    public  Literal buildTolerance() throws CQLException {
        Literal tolerance = null;

        try {
            tolerance = this.resultStack.popLiteral();

            return tolerance;
        } catch (NumberFormatException e) {
            throw new CQLException("Unsupported number format",this.cqlSource);
        }
    }

    public BinarySpatialOperator buildSpatialEqualFilter() throws CQLException {
 
        Literal geom = this.resultStack.popLiteral();

        Expression property = this.resultStack.popExpression();

        FilterFactory2 ff = (FilterFactory2) filterFactory; // TODO this cast must be removed. It depends of Geometry implementation 

        return ff.equal(property, geom);
    }
    public BinarySpatialOperator buildSpatialDisjointFilter() throws CQLException {
        Literal geom = this.resultStack.popLiteral();

        Expression property = this.resultStack.popExpression();

        FilterFactory2 ff = (FilterFactory2) filterFactory; // TODO this cast must be removed. It depends of Geometry implementation 

        return  ff.disjoint(property, geom);
    }

    public BinarySpatialOperator buildSpatialIntersectsFilter() throws CQLException {
        
        Literal geom = this.resultStack.popLiteral();

        Expression property = this.resultStack.popExpression();

        FilterFactory2 ff = (FilterFactory2) filterFactory; // TODO this cast must be removed. It depends of Geometry implementation 

        return ff.intersects(property, geom);
    }

	public PropertyIsEqualTo buildSpatialRelateFilter() throws CQLException {

		Literal pattern = this.resultStack.popLiteral();

		Literal geometry = this.resultStack.popLiteral();

		PropertyName property = this.resultStack.popPropertyName();

		FilterFactory2 ff = (FilterFactory2) filterFactory;
		Expression[] args = new Expression[] { property, geometry, pattern };

		Function function = filterFactory.function("relatePattern", args);

		assert function != null: "a relatePattern function is expected";

		PropertyIsEqualTo filter = ff.equals(function, ff.literal(true));

		return filter;
	}
	/**
	 * Build the intersection matrix pattern
	 * 
	 * @param patternToken
	 * @return
	 */
    public Literal buildDE9IM(final String tokenImage){
    	
        Literal literal = filterFactory.literal(tokenImage);

        return literal;
    }
	

    public BinarySpatialOperator buildSpatialTouchesFilter() throws CQLException {
        Literal geom = this.resultStack.popLiteral();

        Expression property = this.resultStack.popExpression();

        FilterFactory2 ff = (FilterFactory2) filterFactory; // TODO this cast must be removed. It depends of Geometry implementation 

        return ff.touches(property, geom);
    }

    public BinarySpatialOperator buildSpatialCrossesFilter() throws CQLException {
        Literal geom = this.resultStack.popLiteral();

        Expression property = this.resultStack.popExpression();

        FilterFactory2 ff = (FilterFactory2) filterFactory; // TODO this cast must be removed. It depends of Geometry implementation 

        return  ff.crosses(property, geom);
    }

    public BinarySpatialOperator buildSpatialWithinFilter() throws CQLException {

        Literal geom = this.resultStack.popLiteral();

        Expression property = this.resultStack.popExpression();

        FilterFactory2 ff = (FilterFactory2) filterFactory; // TODO this cast must be removed. It depends of Geometry implementation 

        return ff.within(property, geom);
    }

    public BinarySpatialOperator buildSpatialContainsFilter() throws CQLException {

        Literal geom = this.resultStack.popLiteral();

        Expression property = this.resultStack.popExpression();

        FilterFactory2 ff = (FilterFactory2) filterFactory; // TODO this cast must be removed. It depends of Geometry implementation 

        return ff.contains(property, geom);

    }

    public BinarySpatialOperator buildSpatialOverlapsFilter() throws CQLException {

        Literal geom = this.resultStack.popLiteral();

        Expression property = this.resultStack.popExpression();

        FilterFactory2 ff = (FilterFactory2) filterFactory; // TODO this cast must be removed. It depends of Geometry implementation 

        return ff.overlaps(property, geom);
    }
    public org.opengis.filter.spatial.BBOX buildBBox() throws CQLException{

        return buildBbox(null);
    }

    public org.opengis.filter.spatial.BBOX buildBBoxWithCRS()
            throws CQLException {
            
            String crs = this.resultStack.popStringValue();
            assert crs != null;

            return buildBbox(crs);
    }
    private org.opengis.filter.spatial.BBOX buildBbox(final String crs) throws CQLException{
        
        double maxY = this.resultStack.popDoubleValue();
        double maxX = this.resultStack.popDoubleValue();
        double minY = this.resultStack.popDoubleValue();
        double minX = this.resultStack.popDoubleValue();

        PropertyName property = this.resultStack.popPropertyName();
        String strProperty = property.getPropertyName();

        org.opengis.filter.spatial.BBOX bbox = filterFactory.bbox(
                strProperty, minX, minY, maxX, maxY, crs);
        return bbox;
    }

    public DistanceBufferOperator buildSpatialDWithinFilter() throws CQLException {

        String unit = this.resultStack.popStringValue();

        double tolerance = this.resultStack.popDoubleValue();

        Expression geom = this.resultStack.popExpression();

        Expression property = this.resultStack.popExpression();
        
        FilterFactory2  ff = (FilterFactory2) filterFactory; // TODO this cast must be removed. It depends of Geometry implementation 

        return ff.dwithin(property, geom, tolerance, unit);
    }

    public DistanceBufferOperator buildSpatialBeyondFilter() throws CQLException {
        
        String unit = this.resultStack.popStringValue();

        double tolerance = this.resultStack.popDoubleValue();

        Expression geom = this.resultStack.popExpression();

        Expression property = this.resultStack.popExpression();
        
        FilterFactory2  ff = (FilterFactory2) filterFactory; // TODO this cast must be removed. It depends of Geometry implementation 

        return ff.beyond(property, geom, tolerance, unit);
    }

    /**
     * builds a PeriodNode (date1,date2)
     * 
     * @return PeriodNode
     * 
     * @throws CQLException
     */
    public PeriodNode buildPeriodBetweenDates() throws CQLException {
        org.opengis.filter.expression.Literal end = this.resultStack.popLiteral();

        org.opengis.filter.expression.Literal begin = this.resultStack.popLiteral();

        PeriodNode period = PeriodNode.createPeriodDateAndDate(begin, end);

        return period;
    }
    
    /**
     * builds a Period Node with (duration,date).
     * 
     * @return PeriodNode
     * @throws CQLException
     */
    public PeriodNode buildPeriodDurationAndDate() throws CQLException {
        Literal date = this.resultStack.popLiteral();

        Literal duration = this.resultStack.popLiteral();

        PeriodNode period = PeriodNode.createPeriodDurationAndDate(duration,
                date, filterFactory);

        return period;
    }
    
    /**
     * builds a Period with (date,duration)
     * 
     * @return PeriodNode
     * @throws CQLException
     */
    public PeriodNode buildPeriodDateAndDuration() throws CQLException {
        Literal duration = this.resultStack.popLiteral();

        Literal date = this.resultStack.popLiteral();

        PeriodNode period = PeriodNode.createPeriodDateAndDuration(date,
                duration, filterFactory);

        return period;
    }
    
    /**
     * Create an integer literal with duration value.
     * 
     * @return Literal
     */
    public org.opengis.filter.expression.Literal buildDurationExpression(final IToken token) {
        String duration = token.toString();
        org.opengis.filter.expression.Literal literalDuration = filterFactory
                .literal(duration);

        return literalDuration;
    }

    /**
     * Create an AND filter with property between dates of period. 
     * (firstDate<= property <= lastDate)
     * 
     * @return And filter
     * 
     * @throws CQLException
     */
    public And buildPropertyBetweenDates() throws CQLException {
        
        // retrieves date and duration of expression
        Result node = this.resultStack.popResult();
        PeriodNode period = (PeriodNode) node.getBuilt();

        Literal begin = period.getBeginning();
        Literal end = period.getEnding();

        // creates and filter firstDate<= property <= lastDate
        Expression property = this.resultStack.popExpression();

        And filter = filterFactory.and(filterFactory.lessOrEqual(begin,
                property), filterFactory.lessOrEqual(property, end));

        return filter;
    }

    /**
     * Builds PropertyIsGreaterThanOrEqualTo begin of period
     * 
     * @return PropertyIsGreaterThanOrEqualTo
     * @throws CQLException
     */
    public PropertyIsGreaterThanOrEqualTo buildPropertyIsGTEFirstDate()
            throws CQLException {
        Result node = this.resultStack.popResult();
        PeriodNode period = (PeriodNode) node.getBuilt();

        org.opengis.filter.expression.Literal begin = period.getBeginning();

        org.opengis.filter.expression.Expression property = (org.opengis.filter.expression.Expression) resultStack
                .popExpression();

        PropertyIsGreaterThanOrEqualTo filter = filterFactory.greaterOrEqual(
                property, begin);

        return filter;
    }
    
    /**
     * creates PropertyIsGreaterThan end date of period
     * 
     * @return PropertyIsGreaterThan
     * 
     * @throws CQLException
     */
    public PropertyIsGreaterThan buildPropertyIsGTLastDate()
            throws CQLException {
        Result node = this.resultStack.popResult();
        PeriodNode period = (PeriodNode) node.getBuilt();

        org.opengis.filter.expression.Literal date = period.getEnding();

        org.opengis.filter.expression.Expression property = this.resultStack
                .popExpression();

        PropertyIsGreaterThan filter = filterFactory.greater(property, date);

        return filter;
    }

    /**
     * @return PropertyIsLessThan
     * @throws CQLException
     */
    public PropertyIsLessThan buildPropertyIsLTFirsDate() throws CQLException {
        PeriodNode period = this.resultStack.popPeriodNode();

        org.opengis.filter.expression.Literal date = period.getBeginning();

        org.opengis.filter.expression.Expression property = this.resultStack
                .popExpression();

        PropertyIsLessThan filter = filterFactory.less(property, date);

        return filter;
    }

    /**
     * 
     * @return PropertyIsLessThanOrEqualTo
     * @throws CQLException
     */
    public PropertyIsLessThanOrEqualTo buildPropertyIsLTELastDate()
            throws CQLException {
        PeriodNode period = this.resultStack.popPeriodNode();

        org.opengis.filter.expression.Literal date = period.getEnding();

        org.opengis.filter.expression.Expression property = this.resultStack
                .popExpression();

        PropertyIsLessThanOrEqualTo filter = filterFactory.lessOrEqual(
                property, date);

        return filter;
    }

    /**
     * 
     * @return PropertyIsEqualTo 
     * @throws CQLException
     */
    public PropertyIsEqualTo buildEquals() throws CQLException {

        Expression right = this.resultStack.popExpression();
        Expression left = this.resultStack.popExpression();

        return filterFactory.equals(left, right);
    }

    /**
     * 
     * @return PropertyIsGreaterThan
     * @throws CQLException
     */
    public PropertyIsGreaterThan buildGreater() throws CQLException {
        Expression right = this.resultStack.popExpression();
        Expression left = this.resultStack.popExpression();
        return filterFactory.greater(left, right);
    }

    /**
     * 
     * @return PropertyIsLessThan 
     * @throws CQLException
     */
    public PropertyIsLessThan buildLess() throws CQLException {

        Expression right = this.resultStack.popExpression();
        Expression left = this.resultStack.popExpression();
        return filterFactory.less(left, right);
    }

    /**
     * 
     * @return PropertyIsGreaterThanOrEqualTo
     * @throws CQLException
     */
    public PropertyIsGreaterThanOrEqualTo buildGreaterOrEqual() throws CQLException {
        Expression right = this.resultStack.popExpression();
        Expression left = this.resultStack.popExpression();
        return filterFactory.greaterOrEqual(left, right);
    }

    /**
     * @return PropertyIsLessThanOrEqualTo 
     * @throws CQLException
     */
    public PropertyIsLessThanOrEqualTo buildLessOrEqual() throws CQLException {
        
        Expression right = this.resultStack.popExpression();
        Expression left = this.resultStack.popExpression();

        return filterFactory.lessOrEqual(left, right);
    }

    /**
     * Builds geometry
     * 
     * @param geometry
     * @return a geometry
     * @throws CQLException
     */
    public Literal buildGeometry(final IToken geometry) throws CQLException {
        try {
            String wktGeom = scanExpression(geometry);

            // transforms wkt to vividsolution geometry
            String vividGeom = transformWKTGeometry(wktGeom);

            WKTReader reader = new WKTReader();

            Geometry g = reader.read(vividGeom);

            Literal literal = filterFactory.literal(g);

            return literal;
        } catch (com.vividsolutions.jts.io.ParseException e) {
            throw new CQLException(e.getMessage(), geometry, e, this.cqlSource);
        } catch (Exception e) {
            throw new CQLException("Error building WKT Geometry: " + e.getMessage(),geometry, e, this.cqlSource);
        }
    }
    

    /**
     * Extracts expression between initial token and last token in buffer.
     * 
     * @param initialToken
     * 
     * @return String the expression 
     */
    protected String scanExpression(final IToken initialToken) {

        IToken end = initialToken;

        while (end.hasNext()) {
            end = end.next();
        }

        String expr = cqlSource.substring(
                initialToken.beginColumn() - 1, end.endColumn());

        return expr;
    }


    /**
     * This transformation is required because some geometries like
     * <b>Multipoint</b> has different definition in vividsolucion library.
     * <p>
     * 
     * <pre>
     * Then OGC require MULTIPOINT((1 2), (3 4))
     * but vividsolunion works without point &quot;(&quot; ans &quot;)&quot;
     * MULTIPOINT(1 2, 3 4)
     * </pre>
     * 
     * <p>
     * 
     * @param wktGeom ogc wkt geometry
     * @return String vividsolution geometry
     */
    protected String transformWKTGeometry(final String wktGeom) {
        final String MULTIPOINT_TYPE = "MULTIPOINT";

        StringBuffer transformed = new StringBuffer(30);
        StringBuffer source = new StringBuffer(wktGeom.toUpperCase());

        int cur = -1;

        if ((cur = source.indexOf(MULTIPOINT_TYPE)) != -1) {
            // extract "(" and ")" from points in arguments
            String argument = source.substring(cur + MULTIPOINT_TYPE.length()
                    + 1, source.length() - 1);

            argument = argument.replace('(', ' ');
            argument = argument.replace(')', ' ');

            transformed.append(MULTIPOINT_TYPE).append("(").append(argument)
                    .append(")");

            return transformed.toString();
        } else {
            return wktGeom;
        }
    }

    /**
     * Returns the Envelop
     * @param token
     * @return Literal
     */
    public Literal buildEnvelop(IToken token) {
        String source = scanExpression(token);

        final String ENVELOPE_TYPE = "ENVELOPE";

        int cur = source.indexOf(ENVELOPE_TYPE);

        // transforms CQL envelop envelop(West,East,North,South) to
        // GS84 West=minX, East=maxX, North=maxY, South=minY
        double minX, minY, maxX, maxY;

        cur = cur + ENVELOPE_TYPE.length() + 1;

        String argument = source.substring(cur, source.length() - 1);

        final String comma = ",";
        cur = 0;

        int end = argument.indexOf(comma, cur);
        String west = argument.substring(cur, end);
        minX = Double.parseDouble(west);

        cur = end + 1;
        end = argument.indexOf(comma, cur);

        String east = argument.substring(cur, end);
        maxX = Double.parseDouble(east);

        cur = end + 1;
        end = argument.indexOf(comma, cur);

        String north = argument.substring(cur, end);
        maxY = Double.parseDouble(north);

        cur = end + 1;

        String south = argument.substring(cur);
        minY = Double.parseDouble(south);

        // ReferencedEnvelope envelope = new
        // ReferencedEnvelope(DefaultGeographicCRS.WGS84);
        // envelope.init(minX, minY, maxX, maxY);
        GeometryFactory gf = new GeometryFactory();

        Coordinate[] coords = { new Coordinate(minX, minY),
                new Coordinate(minX, maxY), new Coordinate(maxX, maxY),
                new Coordinate(maxX, minY), new Coordinate(minX, minY) };
        LinearRing shell = gf.createLinearRing(coords);
        Polygon bbox = gf.createPolygon(shell, null);
        bbox.setUserData(DefaultGeographicCRS.WGS84);

        Literal literal = filterFactory.literal(bbox);

        return literal;
    }

    /**
     * Builds a function expression
     * 
     * @param functionNode symbol used to identify the function node in parser tree
     * @return Function
     * @throws CQLException
     */
    public Function buildFunction(final int functionNode) throws CQLException {
        
        String functionName = null; // token.image;

        // extracts the arguments from stack. Each argument in the stack
        // is preceded by an argument node. Finally extracts the function name
        List<Expression> argList = new LinkedList<Expression>();

        while (!this.resultStack.empty()) {
            Result node = this.resultStack.peek();

            if (node.getNodeType() == functionNode ) {
                // gets the function's name
                Result funcNameNode = this.resultStack.popResult();
                functionName = funcNameNode.getToken().toString();

                break;
            }

            // ejects the argument node
            this.resultStack.popResult();

            // extracts the argument value
            Expression arg = this.resultStack.popExpression();
            argList.add(arg);
        }

        // Puts the argument in correct order
        Collections.reverse(argList);

        
        Expression[] args = (Expression[]) argList
                .toArray(new Expression[argList.size()]);

        Function function = null;
        try{
            function = filterFactory.function(functionName, args);

            if (function == null) {
                throw new CQLException("Function not found.", this.cqlSource);
            }
        	
        } catch (Exception ex){
            throw new CQLException("Function not found.", this.cqlSource);
        }

        return function;
    }

    /**
     * Build an After date filter
     * 
     * @return After
     * @throws CQLException
     */
	public After buildAfterDate() throws CQLException {
        
		Expression right = this.resultStack.popExpression();
        Expression left = this.resultStack.popExpression();
        
        After filter =  this.filterFactory.after(left, right);
        
        return filter;
	}
	
	/**
	 * Builds an after period filter
	 * 
	 * @return after
	 * @throws CQLException
	 */
    public After buildAfterPeriod() 
            throws CQLException {
        Result node = this.resultStack.popResult();
        PeriodNode period = (PeriodNode) node.getBuilt();

        Literal date = period.getEnding();

        Expression property = this.resultStack.popExpression();

        After filter = filterFactory.after(property, date);

        return filter;
    }	
    
    public Before buildBeforeDate() throws CQLException {
    
		Expression right = this.resultStack.popExpression();
        Expression left = this.resultStack.popExpression();
        
        Before filter =  this.filterFactory.before(left, right);
        
        return filter;
    }
    
    public Before buildBeforePeriod() 
            throws CQLException {
        Result node = this.resultStack.popResult();
        PeriodNode period = (PeriodNode) node.getBuilt();

        Literal date = period.getEnding();

        Expression property = this.resultStack.popExpression();

        Before filter = filterFactory.before(property, date);

        return filter;
    }	
    
    public During buildDuringPeriod() throws CQLException {
        
        // retrieves date and duration from expression
		Period period = this.resultStack.popPeriod();

        Expression property = this.resultStack.popExpression();

        During filter = this.filterFactory.during(property, this.filterFactory.literal(period));

        return filter;
    }    
    
    /**
     * Builds an Or filter composed of During and After. 
     * 
     * @return Or filter
     * @throws CQLException
     */
    public Or buildDuringOrAfter() throws CQLException{

        Period period = this.resultStack.popPeriod();
        
        PropertyName property = this.resultStack.popPropertyName();

        // makes the after filter
        After right = this.filterFactory.after(property, filterFactory.literal(period.getEnding()));
        
        // makes the during filter
        During left = this.filterFactory.during(property, this.filterFactory.literal(period));

    	Or filter = this.filterFactory.or(left, right);
    	
    	return filter;
    }
    /**
     * Builds an Or filter composed of Before and During filters. 
     * 
     * @return Or filter
     * @throws CQLException
     */
    public Or buildBeforeOrDuring() throws CQLException{

        Period period = this.resultStack.popPeriod();
        
        PropertyName property = this.resultStack.popPropertyName();

        // makes the after filter
        Before right = this.filterFactory.before(property, filterFactory.literal(period.getBeginning()));
        
        // makes the during filter
        During left = this.filterFactory.during(property, this.filterFactory.literal(period));
        
        Or filter = this.filterFactory.or(right, left);
        
        return filter;
    }

}
