/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.cql2;

import java.util.logging.Logger;

import org.geotools.filter.text.commons.FilterToTextUtil;
import org.opengis.filter.And;
import org.opengis.filter.ExcludeFilter;
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
import org.opengis.filter.PropertyIsNil;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
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
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.AnyInteracts;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.Meets;
import org.opengis.filter.temporal.MetBy;
import org.opengis.filter.temporal.OverlappedBy;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;

/**
 * This is a utility class used by CQL.encode( Filter ) method to do the
 * hard work.
 * <p>
 * Please note that this encoder is a bit more strict than you may be used to
 * (the Common Query Language for example demands Equals.getExpression1() is a
 * PropertyName). If you used FilterFactory to produce your filter you should be
 * okay (as it only provides methods to make a valid Filter); if not please
 * expect ClassCastExceptions.
 * <p>
 * This visitor will return a StringBuilder; you can also provide a StringBuilder
 * as the data parameter in order to cut down on the number of objects
 * created during encoding.
 * <pre>
 * <code>
 * FilterToCQL toCQL = new FilterToCQL();
 * StringBuilder output = filter.accepts( toCQL, new StringBuilder() );
 * String cql = output.toString();
 * </code
 * ></pre> 
 * @author Johann Sorel
 */
class FilterToCQL implements FilterVisitor {
    /** Standard java logger */
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(FilterToCQL.class.getName());
    
    /**
     * Exclude everything; using an old SQL trick of 1=0.
     */
    public Object visit(ExcludeFilter filter, Object extraData) {
        return FilterToTextUtil.buildExclude(extraData);
    }
    /**
     * Include everything; using an old SQL trick of 1=1.
     */
    public Object visit(IncludeFilter filter, Object extraData) {
        return FilterToTextUtil.buildInclude(extraData);
    }

    public Object visit(And filter, Object extraData) {

    	return FilterToTextUtil.buildBinaryLogicalOperator("AND", this, filter, extraData);
	}

	/**
     * Encoding an Id filter is not supported by CQL.
     * <p>
     * This is because in the Catalog specification retreiving an object
     * by an id is a distinct operation seperate from a filter based query.
     */
    public Object visit(Id filter, Object extraData) {
        throw new IllegalStateException("Cannot encode an Id as legal CQL");
    }
    
    public Object visit(Not filter, Object extraData) {
        LOGGER.finer("exporting Not filter");

        return FilterToTextUtil.buildNot(this,filter, extraData);
        
    }
    
    public Object visit(Or filter, Object extraData) {
        LOGGER.finer("exporting Or filter");
        
    	return FilterToTextUtil.buildBinaryLogicalOperator("OR", this, filter, extraData);
    }
    public Object visit(PropertyIsBetween filter, Object extraData) {

    	return FilterToTextUtil.buildBetween(filter, extraData);
    }
    
    public Object visit(PropertyIsEqualTo filter, Object extraData) {

    	checkLeftExpressionIsProperty(filter.getExpression1());
    	return FilterToTextUtil.buildComparison(filter, extraData, "=");
    }
    
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {

    	checkLeftExpressionIsProperty(filter.getExpression1());
    	return FilterToTextUtil.buildComparison(filter, extraData, "!=");
    }

    
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        
    	checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildComparison(filter, extraData, ">");
    }
    
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
    	checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildComparison(filter, extraData, ">=");
    }
    
    
    public Object visit(PropertyIsLessThan filter, Object extraData) {
    	
    	checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildComparison(filter, extraData, "<");
    }
    
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
    	checkLeftExpressionIsProperty(filter.getExpression1());
        return FilterToTextUtil.buildComparison(filter, extraData, "<=");
    }
    
    public Object visit(PropertyIsLike filter, Object extraData) {

    	checkLeftExpressionIsProperty(filter.getExpression());
    	return FilterToTextUtil.buildIsLike(filter, extraData);
    	
    }
	private void checkLeftExpressionIsProperty(Expression expr) {
    	if(!(expr instanceof PropertyName)){
    		throw new RuntimeException("CQL requires a PropertyName");
    	}
	}
    
    public Object visit(PropertyIsNull filter, Object extraData) {

    	return FilterToTextUtil.buildIsNull(filter, extraData);
    }

    public Object visit(PropertyIsNil filter, Object extraData) {
        throw new UnsupportedOperationException("isNil not supported");
    }

    public Object visit(BBOX filter, Object extraData) {
        
    	return FilterToTextUtil.buildBBOX(filter, extraData);
    }
    public Object visit(Beyond filter, Object extraData) {
    	
    	checkLeftExpressionIsProperty(filter.getExpression1());
    	return FilterToTextUtil.buildDistanceBufferOperation("BEYOND", filter, extraData);
    }
    
    public Object visit(Contains filter, Object extraData) {

    	checkLeftExpressionIsProperty(filter.getExpression1());
    	return FilterToTextUtil.buildBinarySpatialOperator("CONTAINS", filter, extraData);
    }
    
    public Object visit(Crosses filter, Object extraData) {
    	checkLeftExpressionIsProperty(filter.getExpression1());
    	return FilterToTextUtil.buildBinarySpatialOperator("CROSSES", filter, extraData);
    }
    public Object visit(Disjoint filter, Object extraData) {
    	checkLeftExpressionIsProperty(filter.getExpression1());
    	return FilterToTextUtil.buildBinarySpatialOperator("DISJOINT", filter, extraData);
    }
    public Object visit(DWithin filter, Object extraData) {
     
    	checkLeftExpressionIsProperty(filter.getExpression1());
    	return FilterToTextUtil.buildDWithin(filter, extraData);
    }
    public Object visit(Equals filter, Object extraData) {
    	checkLeftExpressionIsProperty(filter.getExpression1());
    	return FilterToTextUtil.buildBinarySpatialOperator("EQUALS", filter, extraData);
    }
    public Object visit(Intersects filter, Object extraData) {
    	
    	checkLeftExpressionIsProperty(filter.getExpression1());
    	return FilterToTextUtil.buildBinarySpatialOperator("INTERSECTS", filter, extraData);
    	
    }
    public Object visit(Overlaps filter, Object extraData) {
    	checkLeftExpressionIsProperty(filter.getExpression1());
    	return FilterToTextUtil.buildBinarySpatialOperator("OVERLAPS", filter, extraData);
    }
    public Object visit(Touches filter, Object extraData) {
    	checkLeftExpressionIsProperty(filter.getExpression1());
    	return FilterToTextUtil.buildBinarySpatialOperator("TOUCHES", filter, extraData);
    }
    	     
    public Object visit(Within filter, Object extraData) {
    	checkLeftExpressionIsProperty(filter.getExpression1());
    	return FilterToTextUtil.buildBinarySpatialOperator("WITHIN", filter, extraData);
    }
    /**
     * A filter has not been provided.
     * <p>
     * In general this is a bad situtation which we ask people to
     * represent with Filter.INCLUDES or Filter.EXCLUDES depending
     * on what behaviour they want to see happen - in this case
     * literally <code>null</code> was provided.
     * <p>
     */
    public Object visitNullFilter(Object extraData) {
        throw new NullPointerException("Cannot encode null as a Filter");
    }
    
    public Object visit(After after, Object extraData) {

    	return FilterToTextUtil.buildBinaryTemporalOperator("AFTER", after, extraData);
    }
    
    public Object visit(Before before, Object extraData) {
    	return FilterToTextUtil.buildBinaryTemporalOperator("BEFORE", before, extraData);
    }
    
    public Object visit(During during, Object extraData) {

    	return FilterToTextUtil.buildDuring(during, extraData);
    }
    

    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter AnyInteracts has not a CQL expression"); 
    }
    
    
    public Object visit(Begins begins, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter Begins has not a CQL expression"); 
    }
    
    public Object visit(BegunBy begunBy, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter BegunBy has not a CQL expression"); 
    }
    
    public Object visit(EndedBy endedBy, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter EndedBy has not a CQL expression"); 
    }
    public Object visit(Ends ends, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter Ends has not a CQL expression"); 
    }
    public Object visit(Meets meets, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter Meets has not a CQL expression"); 
    }
    public Object visit(MetBy metBy, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter MetBy has not a CQL expression"); 
    }
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter OverlappedBy not implemented"); 
    }
    public Object visit(TContains contains, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter TContains has not a CQL expression");
    }
    public Object visit(TEquals equals, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter TEquals has not a CQL expression"); 
    }
    public Object visit(TOverlaps contains, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter TOverlaps has not a CQL expression"); 
    }
    
}
