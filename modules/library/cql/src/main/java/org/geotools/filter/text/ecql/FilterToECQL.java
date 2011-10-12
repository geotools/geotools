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
package org.geotools.filter.text.ecql;

import java.util.Iterator;

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
import org.opengis.filter.identity.Identifier;
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
 * This class is responsible to transform a filter to an ECQL predicate.
 * 
 * @author Mauricio Pazos
 *
 */
final class FilterToECQL implements FilterVisitor {

	
	@Override
	public Object visitNullFilter(Object extraData) {
		throw new NullPointerException("Cannot encode null as a Filter");
	}

	@Override
	public Object visit(ExcludeFilter filter, Object extraData) {
        
        return FilterToTextUtil.buildExclude(extraData);
	}

	@Override
	public Object visit(IncludeFilter filter, Object extraData) {
        return FilterToTextUtil.buildInclude(extraData);
	}

	@Override
	public Object visit(And filter, Object extraData) {
    	return FilterToTextUtil.buildBinaryLogicalOperator("AND", this, filter, extraData);
	}

	/**
	 * builds a ecql id expression: in (id1, id2, ...)
	 */
	@Override
	public Object visit(Id filter, Object extraData) {

		StringBuilder ecql = FilterToTextUtil.asStringBuilder(extraData);
		ecql.append("IN (");

		Iterator<Identifier> iter= filter.getIdentifiers().iterator();
		while(iter.hasNext()) {

			Identifier identifier = iter.next();
			ecql.append(identifier);
			
			if(iter.hasNext()){
				ecql.append(",");
			}
		}
		ecql.append(")");
		return ecql.toString();
	}

	/**
	 * builds the Not logical operator
	 */
	@Override
	public Object visit(Not filter, Object extraData) {
		return FilterToTextUtil.buildNot(this, filter, extraData);
	}


	/**
	 * builds the OR logical operator
	 */
	@Override
	public Object visit(Or filter, Object extraData) {
    	return FilterToTextUtil.buildBinaryLogicalOperator("OR", this, filter, extraData);
	}

	/**
	 * builds the BETWEEN predicate
	 */
	@Override
	public Object visit(PropertyIsBetween filter, Object extraData) {
    	return FilterToTextUtil.buildBetween(filter, extraData);
	}

	@Override
	public Object visit(PropertyIsEqualTo filter, Object extraData) {
		return FilterToTextUtil.buildComparison(filter, extraData, "=");
	}

	@Override
	public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
    	return FilterToTextUtil.buildComparison(filter, extraData, "!=");
	}

	@Override
	public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        return FilterToTextUtil.buildComparison(filter, extraData, ">");
	}

	@Override
	public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        return FilterToTextUtil.buildComparison(filter, extraData, ">=");
	}

	@Override
	public Object visit(PropertyIsLessThan filter, Object extraData) {
        return FilterToTextUtil.buildComparison(filter, extraData, "<");
	}

	@Override
	public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        return FilterToTextUtil.buildComparison(filter, extraData, "<=");
	}

	@Override
	public Object visit(PropertyIsLike filter, Object extraData) {
    	return FilterToTextUtil.buildIsLike(filter, extraData);
	}

	@Override
	public Object visit(PropertyIsNull filter, Object extraData) {
    	return FilterToTextUtil.buildIsNull(filter, extraData);
	}

	@Override
	public Object visit(PropertyIsNil filter, Object extraData) {
	throw new UnsupportedOperationException("PropertyIsNil not supported");
	}

	@Override
	public Object visit(BBOX filter, Object extraData) {
    	return FilterToTextUtil.buildBBOX(filter, extraData);
	}

	@Override
	public Object visit(Beyond filter, Object extraData) {
    	return FilterToTextUtil.buildDistanceBufferOperation("BEYOND", filter, extraData);
	}

	@Override
	public Object visit(Contains filter, Object extraData) {
    	return FilterToTextUtil.buildBinarySpatialOperator("CONTAINS", filter, extraData);
	}

	@Override
	public Object visit(Crosses filter, Object extraData) {
    	return FilterToTextUtil.buildBinarySpatialOperator("CROSSES", filter, extraData);
	}

	@Override
	public Object visit(Disjoint filter, Object extraData) {
    	return FilterToTextUtil.buildBinarySpatialOperator("DISJOINT", filter, extraData);
	}

	@Override
	public Object visit(DWithin filter, Object extraData) {
    	return FilterToTextUtil.buildDWithin(filter, extraData);
	}

	@Override
	public Object visit(Equals filter, Object extraData) {
    	return FilterToTextUtil.buildBinarySpatialOperator("EQUALS", filter, extraData);
	}

	@Override
	public Object visit(Intersects filter, Object extraData) {
    	return FilterToTextUtil.buildBinarySpatialOperator("INTERSECTS", filter, extraData);
	}

	@Override
	public Object visit(Overlaps filter, Object extraData) {
    	return FilterToTextUtil.buildBinarySpatialOperator("OVERLAP", filter, extraData);
	}

	@Override
	public Object visit(Touches filter, Object extraData) {
    	return FilterToTextUtil.buildBinarySpatialOperator("TOUCH", filter, extraData);
	}

	@Override
	public Object visit(Within filter, Object extraData) {
    	return FilterToTextUtil.buildBinarySpatialOperator("WITHIN", filter, extraData);
	}

	@Override
	public Object visit(After after, Object extraData) {
    	return FilterToTextUtil.buildBinaryTemporalOperator("AFTER", after, extraData);
	}
	@Override
	public Object visit(Before before, Object extraData) {
    	return FilterToTextUtil.buildBinaryTemporalOperator("BEFORE", before, extraData);
	}

	@Override
	public Object visit(AnyInteracts anyInteracts, Object extraData) {
		throw ecqlUnsupported("AnyInteracts"); 
	}


	@Override
	public Object visit(Begins begins, Object extraData) {
		throw ecqlUnsupported("Begins"); 
	}

	@Override
	public Object visit(BegunBy begunBy, Object extraData) {
        throw ecqlUnsupported("BegunBy");   
	}

	/**
	 * New instance of unsupported exception with the name of filter
	 * @param filterName filter unsupported
	 * @return UnsupportedOperationException
	 */
	static private UnsupportedOperationException ecqlUnsupported(final String filterName){
		return new UnsupportedOperationException("The"+ filterName + " has not an ECQL expression");
	}
	
	@Override
	public Object visit(During during, Object extraData) {
    	return FilterToTextUtil.buildDuring(during, extraData);
	}

	@Override
	public Object visit(EndedBy endedBy, Object extraData) {
		throw ecqlUnsupported("EndedBy"); 
	}

	@Override
	public Object visit(Ends ends, Object extraData) {
		throw ecqlUnsupported("EndedBy"); 
	}

	@Override
	public Object visit(Meets meets, Object extraData) {
		throw ecqlUnsupported("Meets"); 
	}

	@Override
	public Object visit(MetBy metBy, Object extraData) {
		throw ecqlUnsupported("MetBy"); 
	}

	@Override
	public Object visit(OverlappedBy overlappedBy, Object extraData) {
		throw ecqlUnsupported("OverlappedBy"); 
	}

	@Override
	public Object visit(TContains contains, Object extraData) {
		throw ecqlUnsupported("TContains"); 
	}

	@Override
	public Object visit(TEquals equals, Object extraData) {
		throw ecqlUnsupported("TContains"); 
	}

	@Override
	public Object visit(TOverlaps contains, Object extraData) {
		throw ecqlUnsupported("TContains"); 
	}

}
