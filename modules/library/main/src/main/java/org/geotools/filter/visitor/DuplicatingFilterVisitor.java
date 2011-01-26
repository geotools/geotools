/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.opengis.filter.And;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
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
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
import org.opengis.filter.identity.FeatureId;
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

/**
 * Used to duplication Filters and/or Expressions - returned object is a copy.
 * <p>
 * Extra data can be used to provide a {@link FilterFactory2} but this is NOT required.
 * This class is thread safe.
 * </ul>
 * @author Jesse
 *
 *
 * @source $URL$
 */
public class DuplicatingFilterVisitor implements FilterVisitor, ExpressionVisitor {

	protected final FilterFactory2 ff;

	public DuplicatingFilterVisitor() {
		this(CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints()));
		
	}
	
	public DuplicatingFilterVisitor(FilterFactory2 factory) {
		this.ff = factory;
	}
	
	protected FilterFactory2 getFactory(Object extraData) {
		if( extraData instanceof FilterFactory2)
			return (FilterFactory2) extraData;
		return ff;
	}

	public Object visit(ExcludeFilter filter, Object extraData) {
		return filter;
	}


	public Object visit(IncludeFilter filter, Object extraData) {
		return filter;
	}
	
	/**
	 * Null safe expression cloning
	 * @param expression
	 * @param extraData
	 * @return
	 */
	Expression visit(Expression expression, Object extraData) {
	    if(expression == null)
	        return null;
	    return (Expression) expression.accept(this, extraData);
	}

	public Object visit(And filter, Object extraData) {
		List children = filter.getChildren();
		List newChildren = new ArrayList();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Filter child = (Filter) iter.next();
			if( child!=null )
				newChildren.add(child.accept(this, extraData));
		}
		return getFactory(extraData).and(newChildren);
	}

	public Object visit(Id filter, Object extraData) {
		return getFactory(extraData).id(filter.getIdentifiers());
	}

	public Object visit(Not filter, Object extraData) {
		return getFactory(extraData).not((Filter) filter.getFilter().accept(this, extraData));
	}

	public Object visit(Or filter, Object extraData) {
		List children = filter.getChildren();
		List newChildren = new ArrayList();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Filter child = (Filter) iter.next();
			if( child!=null )
				newChildren.add(child.accept(this, extraData));
		}
		return getFactory(extraData).or(newChildren);
	}

	public Object visit(PropertyIsBetween filter, Object extraData) {
		Expression expr= visit(filter.getExpression(), extraData);
		Expression lower= visit(filter.getLowerBoundary(), extraData);
		Expression upper= visit(filter.getUpperBoundary(), extraData);
		return getFactory(extraData).between(expr, lower, upper);
	}

	public Object visit(PropertyIsEqualTo filter, Object extraData) {
		Expression expr1= visit(filter.getExpression1(), extraData);
		Expression expr2= visit(filter.getExpression2(), extraData);
		boolean matchCase=filter.isMatchingCase();
		return getFactory(extraData).equal(expr1, expr2, matchCase);
	}

	public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
	    Expression expr1= visit(filter.getExpression1(), extraData);
        Expression expr2= visit(filter.getExpression2(), extraData);
		boolean matchCase=filter.isMatchingCase();
		return getFactory(extraData).notEqual(expr1, expr2, matchCase);
	}

	public Object visit(PropertyIsGreaterThan filter, Object extraData) {
	    Expression expr1= visit(filter.getExpression1(), extraData);
        Expression expr2= visit(filter.getExpression2(), extraData);
		return getFactory(extraData).greater(expr1, expr2);
	}

	public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
	    Expression expr1= visit(filter.getExpression1(), extraData);
        Expression expr2= visit(filter.getExpression2(), extraData);
		return getFactory(extraData).greaterOrEqual(expr1, expr2);
	}

	public Object visit(PropertyIsLessThan filter, Object extraData) {
	    Expression expr1= visit(filter.getExpression1(), extraData);
        Expression expr2= visit(filter.getExpression2(), extraData);
		return getFactory(extraData).less(expr1, expr2);
	}

	public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
	    Expression expr1= visit(filter.getExpression1(), extraData);
        Expression expr2= visit(filter.getExpression2(), extraData);
		return getFactory(extraData).lessOrEqual(expr1, expr2);
	}

	public Object visit(PropertyIsLike filter, Object extraData) {
		Expression expr= visit(filter.getExpression(), extraData);
		String pattern=filter.getLiteral();
		String wildcard=filter.getWildCard();
		String singleChar=filter.getSingleChar();
		String escape=filter.getEscape();
                boolean matchCase = filter.isMatchingCase();
		return getFactory(extraData).like(expr, pattern, wildcard, singleChar, escape, matchCase);
	}

	public Object visit(PropertyIsNull filter, Object extraData) {
		Expression expr= visit(filter.getExpression(), extraData);
		return getFactory(extraData).isNull(expr);
	}

	public Object visit(BBOX filter, Object extraData) {
		String propertyName=filter.getPropertyName();
		double minx=filter.getMinX();
		double miny=filter.getMinY();
		double maxx=filter.getMaxX();
		double maxy=filter.getMaxY();
		String srs=filter.getSRS();
		return getFactory(extraData).bbox(propertyName, minx, miny, maxx, maxy, srs);
	}

	public Object visit(Beyond filter, Object extraData) {
		Expression geometry1= visit(filter.getExpression1(), extraData);
		Expression geometry2= visit(filter.getExpression2(), extraData);
		double distance=filter.getDistance();
		String units=filter.getDistanceUnits();
		return getFactory(extraData).beyond(geometry1, geometry2, distance, units);
	}

	public Object visit(Contains filter, Object extraData) {
	    Expression geometry1= visit(filter.getExpression1(), extraData);
        Expression geometry2= visit(filter.getExpression2(), extraData);
		return getFactory(extraData).contains(geometry1, geometry2);
	}

	public Object visit(Crosses filter, Object extraData) {
	    Expression geometry1= visit(filter.getExpression1(), extraData);
        Expression geometry2= visit(filter.getExpression2(), extraData);
		return getFactory(extraData).crosses(geometry1, geometry2);
	}

	public Object visit(Disjoint filter, Object extraData) {
	    Expression geometry1= visit(filter.getExpression1(), extraData);
        Expression geometry2= visit(filter.getExpression2(), extraData);
		return getFactory(extraData).disjoint(geometry1, geometry2);
	}

	public Object visit(DWithin filter, Object extraData) {
	    Expression geometry1= visit(filter.getExpression1(), extraData);
        Expression geometry2= visit(filter.getExpression2(), extraData);
		double distance=filter.getDistance();
		String units=filter.getDistanceUnits();
		return getFactory(extraData).dwithin(geometry1, geometry2, distance, units);
	}

	public Object visit(Equals filter, Object extraData) {
	    Expression geometry1= visit(filter.getExpression1(), extraData);
        Expression geometry2= visit(filter.getExpression2(), extraData);
		return getFactory(extraData).equal(geometry1, geometry2);
	}

	public Object visit(Intersects filter, Object extraData) {
	    Expression geometry1= visit(filter.getExpression1(), extraData);
        Expression geometry2= visit(filter.getExpression2(), extraData);
		return getFactory(extraData).intersects(geometry1, geometry2);
	}

	public Object visit(Overlaps filter, Object extraData) {
	    Expression geometry1= visit(filter.getExpression1(), extraData);
        Expression geometry2= visit(filter.getExpression2(), extraData);
		return getFactory(extraData).overlaps(geometry1, geometry2);
	}

	public Object visit(Touches filter, Object extraData) {
	    Expression geometry1= visit(filter.getExpression1(), extraData);
        Expression geometry2= visit(filter.getExpression2(), extraData);
		return getFactory(extraData).touches(geometry1, geometry2);
	}

	public Object visit(Within filter, Object extraData) {
	    Expression geometry1= visit(filter.getExpression1(), extraData);
        Expression geometry2= visit(filter.getExpression2(), extraData);
		return getFactory(extraData).within(geometry1, geometry2);
	}

	public Object visitNullFilter(Object extraData) {
		return null;
	}

	public Object visit(NilExpression expression, Object extraData) {
		return expression;
	}

	public Object visit(Add expression, Object extraData) {
	    Expression expr1= visit(expression.getExpression1(), extraData);
	    Expression expr2= visit(expression.getExpression2(), extraData);
		return getFactory(extraData).add(expr1, expr2);
	}

	public Object visit(Divide expression, Object extraData) {
	    Expression expr1= visit(expression.getExpression1(), extraData);
        Expression expr2= visit(expression.getExpression2(), extraData);
		return getFactory(extraData).divide(expr1, expr2);
	}

	public Object visit(Function expression, Object extraData) {
		List old = expression.getParameters();
		Expression[] args = new Expression[old.size()];
		int i = 0;
		for (Iterator iter = old.iterator(); iter.hasNext(); i++) {
			Expression exp = (Expression) iter.next();
			args[i]= visit(exp, extraData);
		}
		return getFactory(extraData).function(expression.getName(), args);
	}

	public Object visit(Literal expression, Object extraData) {
		return getFactory(extraData).literal(expression.getValue());
	}

	public Object visit(Multiply expression, Object extraData) {
	    Expression expr1= visit(expression.getExpression1(), extraData);
        Expression expr2= visit(expression.getExpression2(), extraData);
		return getFactory(extraData).multiply(expr1, expr2);
	}

	public Object visit(PropertyName expression, Object extraData) {
		return getFactory(extraData).property(expression.getPropertyName());
	}

	public Object visit(Subtract expression, Object extraData) {
	    Expression expr1= visit(expression.getExpression1(), extraData);
        Expression expr2= visit(expression.getExpression2(), extraData);
		return getFactory(extraData).subtract(expr1, expr2);
	}

}
