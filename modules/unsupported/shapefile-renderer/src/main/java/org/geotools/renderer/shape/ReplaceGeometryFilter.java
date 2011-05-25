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
package org.geotools.renderer.shape;

import java.util.Iterator;

import org.geotools.filter.AttributeExpression;
import org.geotools.filter.BetweenFilter;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.Expression;
import org.geotools.filter.FidFilter;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterVisitor;
import org.geotools.filter.Filters;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.LikeFilter;
import org.geotools.filter.LiteralExpression;
import org.geotools.filter.LogicFilter;
import org.geotools.filter.MathExpression;
import org.geotools.filter.NullFilter;


/**
 * Replaces all geometry filters in Filter with an implementation that can
 * process SimpleGeometries.
 *
 * @author Jesse
 *
 * @source $URL$
 */
public class ReplaceGeometryFilter implements FilterVisitor {
    /* (non-Javadoc)
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.Filter)
     */
    public void visit(Filter filter) {
        return;
    }

    /* (non-Javadoc)
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.BetweenFilter)
     */
    public void visit(BetweenFilter filter) {
        return;
    }

    /* (non-Javadoc)
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.CompareFilter)
     */
    public void visit(CompareFilter filter) {
        return;
    }

    /* (non-Javadoc)
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.GeometryFilter)
     */
    public void visit(GeometryFilter filter) {
        return;
    }

    /* (non-Javadoc)
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.LikeFilter)
     */
    public void visit(LikeFilter filter) {
        return;
    }

    /* (non-Javadoc)
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.LogicFilter)
     */
    public void visit(LogicFilter filter) {
        Iterator iter = filter.getFilterIterator();

        while (iter.hasNext()) {
            org.opengis.filter.Filter f = (org.opengis.filter.Filter) iter.next();
            Filters.accept(f, this);
        }
    }

    /* (non-Javadoc)
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.NullFilter)
     */
    public void visit(NullFilter filter) {
        return;
    }

    /* (non-Javadoc)
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.FidFilter)
     */
    public void visit(FidFilter filter) {
        return;
    }

    /* (non-Javadoc)
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.AttributeExpression)
     */
    public void visit(AttributeExpression expression) {
        return;
    }

    /* (non-Javadoc)
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.Expression)
     */
    public void visit(Expression expression) {
        return;
    }

    /* (non-Javadoc)
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.LiteralExpression)
     */
    public void visit(LiteralExpression expression) {
        return;
    }

    /* (non-Javadoc)
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.MathExpression)
     */
    public void visit(MathExpression expression) {
        return;
    }

    /* (non-Javadoc)
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.FunctionExpression)
     */
    public void visit(FunctionExpression expression) {
        return;
    }
}
